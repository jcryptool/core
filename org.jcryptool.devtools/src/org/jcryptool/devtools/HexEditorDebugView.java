package org.jcryptool.devtools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.AbstractEditorService;
import org.jcryptool.core.operations.editors.EditorsManager;

public class HexEditorDebugView extends ViewPart {
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private ViewState state;

	public HexEditorDebugView() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Stores the state of the plugin, more or less half-assed. "State" currently refers to the UTF8- and hex input.
	 * 
	 * Persistence is done in the {@link #getPersistenceFile()}
	 * 
	 * @author simlei
	 *
	 */
	public static class ViewState {
		private static final String separator = "~~~~~~~~~~";
		public String utf8FieldText = "";
		public String byteFieldText = "";
		public List<Byte> bytes = new LinkedList<>();
		
		/**
		 * @return the persistence file
		 */
		public static File getPersistenceFile() {
			File userhome = new File(System.getProperty("user.home"));
			return new File(userhome, "jct-dev-hexeditordebug-persistence.txt");
		}
		
		
		/**
		 * persists the state of the plugin to disk
		 */
		public void persist() {
			getPersistenceFile();
			String content = this.utf8FieldText + separator + this.byteFieldText;
			try {
				Files.writeString(getPersistenceFile().toPath(), content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * depending on the presence of {@link #getPersistenceFile()}, this loads the last state of the plugin or creates fresh (blank) state
		 * 
		 * @return the state
		 */
		public static ViewState initialize() {
			if (getPersistenceFile().exists()) {
				try {
					String content = Files.readString(getPersistenceFile().toPath());
					String[] separated = content.split(separator);
					ViewState result = new ViewState();
					result.utf8FieldText = separated[0];
					if (separated.length > 1) {
						result.byteFieldText = separated[1];
					}
					System.out.println("loaded: " + result.utf8FieldText + "|" + result.byteFieldText);
					return result;
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("Continuing to initialize hex editor debug view cleanly because of the preceding exception.");
					return initializeFresh();
				}
			} else {
				return initializeFresh();
			}
		}
		private static ViewState initializeFresh() {
			var result = new ViewState();
			result.utf8FieldText = "";
			result.byteFieldText = "";
			return result;
		}

		public void setUtf8FieldText(String utf8FieldText) {
			this.utf8FieldText = utf8FieldText;
			persist();
		}
		public void setByteFieldText(String byteFieldText) {
			this.byteFieldText = byteFieldText;
			persist();
		}
		public void setByUtf8() {
			setBytes(HexEditorDebugLogic.utf8ToBytes(this.utf8FieldText));
		}
		public void setByBytes() {
			setBytes(HexEditorDebugLogic.bytetextToBytes(this.byteFieldText));
		}
		private void setBytes(List<Byte> bytelist) {
			this.bytes = new LinkedList<>(bytelist);
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		this.state = ViewState.initialize();
		
		parent.setLayout(new GridLayout(1, false));
		
		Group grpInput = new Group(parent, SWT.NONE);
		grpInput.setText("Input");
		grpInput.setLayout(new GridLayout(3, false));
		grpInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel = new Label(grpInput, SWT.NONE);
		lblNewLabel.setText("UTF8 string");
		
		text = new Text(grpInput, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		text.setText(state.utf8FieldText);
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				state.setUtf8FieldText(text.getText());
			}
		});
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text.heightHint = 50;
		text.setLayoutData(gd_text);
		
		Button btnNewButton = new Button(grpInput, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					state.setByUtf8();
					text_1.setText(HexEditorDebugLogic.bytesToString(state.bytes));
				} catch (Exception ex) {
					LogUtil.logError(ex);
				}
			}
		});
		btnNewButton.setText("set");
		
		Label lblBytes = new Label(grpInput, SWT.NONE);
		lblBytes.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBytes.setText("bytes");
		
		text_3 = new Text(grpInput, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		text_3.setText(state.byteFieldText);
		text_3.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				state.setByteFieldText(text_3.getText());
			}
		});
		GridData gd_text_3 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text_3.heightHint = 58;
		text_3.setLayoutData(gd_text_3);
		
		Button btnNewButton_3 = new Button(grpInput, SWT.NONE);
		btnNewButton_3.setText("set");
		btnNewButton_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					state.setByBytes();
					text_1.setText(HexEditorDebugLogic.bytesToString(state.bytes));
				} catch (Exception ex) {
					LogUtil.logError(ex);
				}
			}
		});
		
		Label label = new Label(grpInput, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Label lblNewLabel_1 = new Label(grpInput, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblNewLabel_1.setText("byte representation");
		new Label(grpInput, SWT.NONE);
		
		text_1 = new Text(grpInput, SWT.BORDER);
		text_1.setEditable(false);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(2, false));
		
		Button btnOpenInHexEditor = new Button(composite, SWT.NONE);
		btnOpenInHexEditor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnOpenInHexEditor.setText("Open in Hex Editor");
		btnOpenInHexEditor.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				openHexEditor(state.bytes);
			};
		});
		
		Button btnOpenInTxtEditor = new Button(composite, SWT.NONE);
		btnOpenInTxtEditor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnOpenInTxtEditor.setText("Open in Text Editor");
		btnOpenInTxtEditor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openTxtEditor(state.bytes);
			}
		});
		
		Button btnNewButton_2 = new Button(parent, SWT.NONE);
		btnNewButton_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				writeHexEditorContentIntoTextfield();
			}
		});
		btnNewButton_2.setText("Read from Editor");
		
		Group grpOutput = new Group(parent, SWT.NONE);
		grpOutput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpOutput.setText("Output");
		grpOutput.setLayout(new GridLayout(1, false));
		
		Label lblNewLabel_1_1 = new Label(grpOutput, SWT.NONE);
		lblNewLabel_1_1.setText("byte representation");
		
		text_2 = new Text(grpOutput, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		GridData gd_text_2 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text_2.heightHint = 49;
		text_2.setLayoutData(gd_text_2);
		// TODO Auto-generated method stub

	}

	protected void writeHexEditorContentIntoTextfield() {
		byte[] content = EditorsManager.getInstance().getContentAsBytes(EditorsManager.getInstance().getActiveEditorReference().getEditor(false));
		String collect = HexEditorDebugLogic.bytesToString(content);
		text_2.setText(collect);
	}

	protected void openHexEditor(List<Byte> bytes) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			byte[] bytearr = new byte[bytes.size()];
			for (int i = 0; i < bytes.size(); i++) {
				int b = bytes.get(i);
				bytearr[i] = (byte) b;
			}
			IEditorInput outputfile = AbstractEditorService.createOutputFile(bytearr, ".bytes");
			page.openEditor(outputfile, "org.jcryptool.editors.hex.HexEditor");
		} catch (Exception e) {
            LogUtil.logError(e);
		}
		
	}

	protected void openTxtEditor(List<Byte> bytes) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			byte[] bytearr = new byte[bytes.size()];
			for (int i = 0; i < bytes.size(); i++) {
				int b = bytes.get(i);
				bytearr[i] = (byte) b;
			}
			IEditorInput outputfile = AbstractEditorService.createOutputFile(bytearr, ".txt");
			page.openEditor(outputfile, "org.jcryptool.editor.text.editor.JCTTextEditor");
		} catch (Exception e) {
            LogUtil.logError(e);
		}
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
