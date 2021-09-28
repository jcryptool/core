package org.jcryptool.bci.views;


import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import javax.inject.Inject;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.crypto.ui.textloader.ui.wizard.TextLoadController;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class OperationView extends ViewPart {
	public OperationView() {
	}

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.jcryptool.bci.views.OperationView";

	@Inject IWorkbench workbench;
	
	private TableViewer viewer;
	private Action action1;
	private Action action2;
	private Action doubleClickAction;
	public Composite composite;
	public Label lblNewLabel;
	public Group grpInput;
	public Group grpOutput;
	public Button btnNewButton;

	private TextLoadController textloader;

	private Button btnOutput1;

	private Button btnOutput2;

	private Label labelOutput;

	private Composite compositeLeft;

	private Composite compositeRight;

	private Label lblRight;

	private Composite runComposite;

	private Button btnEncrypt;

	private Button btnDecrypt;
	 

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}
		@Override
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}
		@Override
		public Image getImage(Object obj) {
			return workbench.getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		
		composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		
		compositeLeft = new Composite(composite, SWT.NONE);
		compositeLeft.setLayout(new GridLayout(1, false));
		compositeLeft.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));


		compositeRight = new Composite(composite, SWT.NONE);
		compositeRight.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeRight.setLayout(new GridLayout(1, false));
		compositeRight.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		lblNewLabel = new Label(compositeLeft, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("Ubuntu", 13, SWT.BOLD));
		lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblNewLabel.setText("Current Operation: AES-256");

		lblRight = new Label(compositeRight, SWT.NONE);
		lblRight.setFont(SWTResourceManager.getFont("Ubuntu", 13, SWT.BOLD));
		lblRight.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblRight.setText("Parameters:");
		
		
		
		runComposite = new Composite(compositeLeft, SWT.NONE);
		runComposite.setLayout(new GridLayout(2, true));
		runComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		btnEncrypt = new Button(runComposite, SWT.PUSH);
		btnEncrypt.setText("Encrypt");
		GridData btnEncryptData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		btnEncryptData.heightHint = 50;
		btnEncrypt.setLayoutData(btnEncryptData);
		
		btnDecrypt = new Button(runComposite, SWT.PUSH);
		btnDecrypt.setText("Decrypt");
		GridData btnDecryptData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		btnDecryptData.heightHint = 50;
		btnDecrypt.setLayoutData(btnDecryptData);

		
		
		grpInput = new Group(compositeLeft, SWT.NONE);
		grpInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpInput.setText("Input");
		grpInput.setLayout(new GridLayout(2, false));
		
		btnNewButton = new Button(grpInput, SWT.TOGGLE);
		btnNewButton.setText("Use the current editor");
		btnNewButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		btnNewButton.setSelection(true);
		
		textloader = new TextLoadController(grpInput, composite, SWT.NONE, false, true);
		textloader.btnLoadText.setText("Choose a text or another editor...");
		textloader.btnLoadText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		grpOutput = new Group(compositeLeft, SWT.NONE);
		grpOutput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpOutput.setText("Output");
		grpOutput.setLayout(new GridLayout(2, false));
		
		btnOutput1 = new Button(grpOutput, SWT.TOGGLE);
		btnOutput1.setText("Use the current editor");
		btnOutput1.setSelection(false);
		btnOutput1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		btnOutput2 = new Button(grpOutput, SWT.TOGGLE);
		btnOutput2.setText("Use a new editor");
		btnOutput2.setSelection(true);
		btnOutput2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		labelOutput = new Label(grpOutput, SWT.WRAP);
		labelOutput.setText("By default, encryption opens the output in a hex editor, decryption opens in a text editor. The context menu of the editor lets you switch to the other type manually.");
		GridData labelOutputData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		labelOutput.setLayoutData(labelOutputData);
		labelOutputData.widthHint = 400;
		

		
		viewer = new TableViewer(compositeRight, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		Table table = viewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setSize(297, 469);
		
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setInput(new String[] { "Key", "Initialization vector", "Mode", "Padding" });
	viewer.setLabelProvider(new ViewLabelProvider());
		getSite().setSelectionProvider(viewer);
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				OperationView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				showMessage("Action 1 executed");
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		action2 = new Action() {
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(workbench.getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			public void run() {
				IStructuredSelection selection = viewer.getStructuredSelection();
				Object obj = selection.getFirstElement();
				showMessage("Double-click detected on "+obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"Bouncycastle — Operation",
			message);
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}
