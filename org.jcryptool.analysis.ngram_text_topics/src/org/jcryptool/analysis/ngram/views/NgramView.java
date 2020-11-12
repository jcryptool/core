package org.jcryptool.analysis.ngram.views;

import java.io.File;
import java.io.FileInputStream;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.analysis.ngram.tools.NgramCode;
import org.jcryptool.core.util.ui.auto.SmoothScroller;
import org.osgi.framework.Bundle;

public class NgramView extends ViewPart {
	public static final String ID = "org.jcryptool.analysis.ngram.views.NgramView"; //$NON-NLS-1$

	private ScrolledComposite sc_Container;
	private Composite cp_Container;
	private Label lbl_Header;
	private Group grp_LoadText;
	private GridData gd_grp_LoadText;
	private Combo cb_LoadText;
	private Label lbl_InputMethod;
	private Combo cb_ChooseLang;
	private Label lbl_ChooseLang;
	private Combo cb_ChooseDist;
	private Label lbl_ChooseDist;
	private Group grp_CipherText;
	private GridData gd_grp_CipherText;
	private FillLayout fl_grp_CipherText;
	private Text txt_CipherText;
	private Group grp_LoadReferenceText;
	private GridData gd_grp_LoadReferenceText;
	private Group grp_AnalizeText;
	private GridData gd_grp_AnalizeText;
	private Button btn_AnalizeText;
	private Button btn_LoadReferenceText;
	private Group grp_ResultText;
	private GridData gd_grp_ResultText;
	private FillLayout fl_grp_ResultText;
	private Text txt_Reference;
	private Label lbl_Reference;
	private Button btn_ReferenceSubmit;
	private Label lbl_FileInfo;
	private Text txt_ResultText;
	private String referenceText;

	public NgramView() {
		referenceText = ""; //$NON-NLS-1$
	}

	@Override
	public void setFocus() {
		txt_CipherText.setFocus();
	}

	@Override
	public void createPartControl(Composite parent) {
		sc_Container = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		sc_Container.setExpandHorizontal(true);
		sc_Container.setExpandVertical(true);

		cp_Container = new Composite(sc_Container, SWT.NONE);
		cp_Container.setLayout(new GridLayout(1, false));

		lbl_Header = new Label(cp_Container, SWT.NONE);
		lbl_Header.setFont(SWTResourceManager.getFont("Lucida Grande", 14, SWT.BOLD)); //$NON-NLS-1$
		lbl_Header.setText(Messages.NgramView_lblPageHeading);
		lbl_Header.setBounds(10, 10, 600, 100);

		grp_LoadText = new Group(cp_Container, SWT.NONE);
		gd_grp_LoadText = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd_grp_LoadText.widthHint = 462;
		gd_grp_LoadText.heightHint = 60;
		grp_LoadText.setLayoutData(gd_grp_LoadText);
		grp_LoadText.setText(Messages.NgramView_4);
		// the input method, the language of the text you want to analyze, and
		// the analysis method

		lbl_InputMethod = new Label(grp_LoadText, SWT.NONE);
		lbl_InputMethod.setText(Messages.NgramView_5);
		lbl_InputMethod.setBounds(14, 20, 145, 14);

		cb_LoadText = new Combo(grp_LoadText, SWT.READ_ONLY);
		cb_LoadText.setItems(new String[] { Messages.NgramView_6, Messages.NgramView_7, Messages.NgramView_8,
				Messages.NgramView_9 });
		cb_LoadText.setBounds(10, 37, 145, 23);
		cb_LoadText.select(0);
		cb_LoadText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (cb_LoadText.getSelectionIndex() == 0) {
					txt_CipherText.setText(""); //$NON-NLS-1$
					txt_CipherText.setEditable(true);
					txt_CipherText.setFocus();
				}

				if (cb_LoadText.getSelectionIndex() == 1) {
					try {
						Display display = Display.getDefault();
						Shell dialogShell = new Shell(display, SWT.APPLICATION_MODAL);
						FileDialog fd_ChooseFile = new FileDialog(dialogShell, SWT.OPEN);
						fd_ChooseFile.setFilterPath("\\"); //$NON-NLS-1$
						fd_ChooseFile.setFilterExtensions(new String[] { "*.txt" }); //$NON-NLS-1$
						File file_CipherText = new File(fd_ChooseFile.open());
						FileInputStream fis;
						fis = new FileInputStream(file_CipherText);
						byte[] content = new byte[fis.available()];
						fis.read(content);
						fis.close();
						txt_CipherText.setText(new String(content));
						txt_CipherText.setEditable(true);
					} catch (Exception ex) {
						txt_CipherText.setText(""); //$NON-NLS-1$
						txt_CipherText.setEditable(false);
					}
				}

				if (cb_LoadText.getSelectionIndex() == 2) {
					Bundle bundle = Platform.getBundle("org.jcryptool.analysis.ngram"); //$NON-NLS-1$
					java.net.URL fileURL = bundle.getEntry("asset/Sample_text_EN.txt"); //$NON-NLS-1$

					File file = null;
					FileInputStream fin = null;
					byte[] content = null;

					try {
						file = new File(FileLocator.resolve(fileURL).toURI());
						fin = new FileInputStream(file);
						content = new byte[fin.available()];
						fin.read(content);
						fin.close();
					} catch (Exception ex) {
					}

					txt_CipherText.setText(new String(content));
					txt_CipherText.setEditable(true);
					cb_ChooseLang.select(0);
				}

				if (cb_LoadText.getSelectionIndex() == 3) {
					Bundle bundle = Platform.getBundle("org.jcryptool.analysis.ngram"); //$NON-NLS-1$
					java.net.URL fileURL = bundle.getEntry("asset/Sample_text_DE.txt"); //$NON-NLS-1$

					File file = null;
					FileInputStream fin = null;
					byte[] content = null;

					try {
						file = new File(FileLocator.resolve(fileURL).toURI());
						fin = new FileInputStream(file);
						content = new byte[fin.available()];
						fin.read(content);
						fin.close();
					} catch (Exception ex) {
					}

					txt_CipherText.setText(new String(content));
					txt_CipherText.setEditable(true);
					cb_ChooseLang.select(1);
				}

				txt_ResultText.setText(""); //$NON-NLS-1$
			}
		});

		lbl_ChooseLang = new Label(grp_LoadText, SWT.NONE);
		lbl_ChooseLang.setText(Messages.NgramView_19);
		lbl_ChooseLang.setBounds(239, 20, 145, 14);

		cb_ChooseLang = new Combo(grp_LoadText, SWT.READ_ONLY);
		// #be cb_ChooseLang.setItems(new String[] {"English", "German"});
		// //$NON-NLS-1$ //$NON-NLS-2$
		cb_ChooseLang.setItems(new String[] { Messages.NgramView_17, Messages.NgramView_18 });
		cb_ChooseLang.setBounds(235, 37, 145, 23);
		cb_ChooseLang.select(0);
		cb_ChooseLang.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				txt_ResultText.setText(""); //$NON-NLS-1$
			}
		});

		lbl_ChooseDist = new Label(grp_LoadText, SWT.NONE);
		lbl_ChooseDist.setText(Messages.NgramView_23);
		lbl_ChooseDist.setBounds(464, 20, 145, 14);

		cb_ChooseDist = new Combo(grp_LoadText, SWT.READ_ONLY);
		cb_ChooseDist.setItems(new String[] { Messages.NgramView_24, Messages.NgramView_25 });
		cb_ChooseDist.setBounds(460, 37, 145, 23);
		cb_ChooseDist.select(0);
		cb_ChooseDist.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				txt_ResultText.setText(""); //$NON-NLS-1$
			}
		});

		grp_CipherText = new Group(cp_Container, SWT.NONE);
		gd_grp_CipherText = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd_grp_CipherText.heightHint = 160;
		grp_CipherText.setLayoutData(gd_grp_CipherText);
		grp_CipherText.setText(Messages.NgramView_27);
		fl_grp_CipherText = new FillLayout(SWT.HORIZONTAL);
		fl_grp_CipherText.marginWidth = 8;
		fl_grp_CipherText.marginHeight = 8;
		grp_CipherText.setLayout(fl_grp_CipherText);
		txt_CipherText = new Text(grp_CipherText, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.MULTI);

		// TODO: empty other variables as well?
		txt_CipherText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				txt_ResultText.setText(""); //$NON-NLS-1$
				grp_CipherText.setText(Messages.NgramView_29 + Messages.NgramView_30 + txt_CipherText.getText().length()
						+ Messages.NgramView_0); // $NON-NLS-3$ 
			}
		});

		txt_CipherText.setEditable(true);
		txt_CipherText.setFocus();

		grp_LoadReferenceText = new Group(cp_Container, SWT.NONE);
		gd_grp_LoadReferenceText = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd_grp_LoadReferenceText.heightHint = 65;
		grp_LoadReferenceText.setLayoutData(gd_grp_LoadReferenceText);
		grp_LoadReferenceText.setText(Messages.NgramView_32);

		btn_LoadReferenceText = new Button(grp_LoadReferenceText, SWT.NONE);
		btn_LoadReferenceText.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				try {
					Display display = Display.getDefault();
					Shell dialogShell = new Shell(display, SWT.APPLICATION_MODAL);
					FileDialog fd_ChooseFile = new FileDialog(dialogShell, SWT.OPEN);
					fd_ChooseFile.setFilterPath("\\"); //$NON-NLS-1$
					fd_ChooseFile.setFilterExtensions(new String[] { "*" }); //$NON-NLS-1$
					File file_LoadReferenceText = new File(fd_ChooseFile.open());
					FileInputStream fis = new FileInputStream(file_LoadReferenceText);
					byte[] content = new byte[fis.available()];
					referenceText = new String(content);
					txt_Reference.setEnabled(true);
					txt_Reference.setText("Name_of_new_topic"); //$NON-NLS-1$ xxxxxxxxxxxxxxxxxxxx
					txt_Reference.setFocus();
					btn_ReferenceSubmit.setEnabled(true);
					String path = file_LoadReferenceText.getAbsolutePath();
					if (fis.available() < 512) {
						lbl_FileInfo.setText(Messages.NgramView_36);
						fis.close();
						throw new Exception();
					} else {
						lbl_FileInfo.setText(Messages.NgramView_37 + (path.length() - 60 < 0 ? "" : "...") //$NON-NLS-1$//$NON-NLS-2$
																											// //$NON-NLS-3$
								+ path.substring(Math.max(path.length() - 60, 0), Math.max(path.length(), 60))
								+ Messages.NgramView_40 + fis.available() + Messages.NgramView_1); // $NON-NLS-2$
																									// //$NON-NLS-1$
					}
					fis.read(content);
					fis.close();
				} catch (Exception ex) {
					txt_Reference.setEnabled(false);
				}
			}
		});

		btn_LoadReferenceText.setBounds(10, 22, 145, 23);
		btn_LoadReferenceText.setText(Messages.NgramView_42);

		lbl_Reference = new Label(grp_LoadReferenceText, SWT.NONE);
		lbl_Reference.setText(Messages.NgramView_43);
		lbl_Reference.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, false, false, 1, 1));
		lbl_Reference.setBounds(170, 27, 60, 23);

		txt_Reference = new Text(grp_LoadReferenceText, SWT.BORDER);
		txt_Reference.setEnabled(false);
		txt_Reference.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, false, false, 1, 1));
		txt_Reference.setBounds(235, 25, 145, 23);
		txt_Reference.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) { // file we have loaded
													// topic's name
				if (referenceText.length() < 1 || txt_Reference.getText().length() < 1)
					btn_ReferenceSubmit.setEnabled(false);
				else
					btn_ReferenceSubmit.setEnabled(true);
			}
		});

		btn_ReferenceSubmit = new Button(grp_LoadReferenceText, SWT.NONE);
		btn_ReferenceSubmit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (txt_Reference.getText().equals(Messages.NgramView_44)
						|| txt_Reference.getText().equals(Messages.NgramView_45)
						|| txt_Reference.getText().equals(Messages.NgramView_46)
						|| txt_Reference.getText().equals(Messages.NgramView_47)
						|| txt_Reference.getText().equals(Messages.NgramView_48)) {
					Display display = Display.getDefault();
					Shell dialogShell = new Shell(display, SWT.APPLICATION_MODAL);

					MessageBox messageBox = new MessageBox(dialogShell, SWT.ICON_WARNING | SWT.OK);
					messageBox.setText(Messages.NgramView_49);
					messageBox.setMessage(Messages.NgramView_50);
					messageBox.open();
				}

				else if (txt_Reference.getText().equals("Name_of_new_topic")) //$NON-NLS-1$ xxxxxxxxxxxxxxxxxxxxxxxx
				{
					txt_Reference.setEnabled(false);
					btn_ReferenceSubmit.setEnabled(false);
				}

				else if (txt_Reference.getText().length() > 0) {
					txt_Reference.setEnabled(false);
					btn_ReferenceSubmit.setEnabled(false);
				}
			}
		});
		btn_ReferenceSubmit.setBounds(460, 25, 145, 23);
		btn_ReferenceSubmit.setText(Messages.NgramView_52);
		btn_ReferenceSubmit.setEnabled(false);

		lbl_FileInfo = new Label(grp_LoadReferenceText, SWT.NONE);
		lbl_FileInfo.setText(Messages.NgramView_53);
		lbl_FileInfo.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, false, false, 1, 1));
		lbl_FileInfo.setBounds(14, 55, 600, 23);

		grp_AnalizeText = new Group(cp_Container, SWT.NONE);
		gd_grp_AnalizeText = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd_grp_AnalizeText.heightHint = 45;
		grp_AnalizeText.setLayoutData(gd_grp_AnalizeText);
		grp_AnalizeText.setText(Messages.NgramView_54);
		btn_AnalizeText = new Button(grp_AnalizeText, SWT.NONE);
		btn_AnalizeText.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (txt_CipherText.getText().length() < 512) {
					Display display = Display.getDefault();
					Shell dialogShell = new Shell(display, SWT.APPLICATION_MODAL);

					MessageBox messageBox = new MessageBox(dialogShell, SWT.ICON_WARNING | SWT.OK);
					messageBox.setText(Messages.NgramView_55);
					messageBox.setMessage(Messages.NgramView_56);
					messageBox.open();
				}

				else if ((referenceText.isEmpty() && !txt_Reference.getEnabled())
						|| (!referenceText.isEmpty() && !txt_Reference.getEnabled())) {
					NgramCalculate();
				}

				else {
					Display display = Display.getDefault();
					Shell dialogShell = new Shell(display, SWT.APPLICATION_MODAL);

					MessageBox messageBox = new MessageBox(dialogShell, SWT.ICON_WARNING | SWT.OK);
					messageBox.setText(Messages.NgramView_57);
					messageBox.setMessage(Messages.NgramView_58);
					messageBox.open();
				}
			}
		});
		btn_AnalizeText.setBounds(10, 22, 145, 23);
		btn_AnalizeText.setText(Messages.NgramView_59);

		grp_ResultText = new Group(cp_Container, SWT.NONE);
		gd_grp_ResultText = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd_grp_ResultText.heightHint = 130;
		grp_ResultText.setLayoutData(gd_grp_ResultText);
		grp_ResultText.setText(Messages.NgramView_60);
		fl_grp_ResultText = new FillLayout(SWT.HORIZONTAL);
		fl_grp_ResultText.marginWidth = 8;
		fl_grp_ResultText.marginHeight = 8;
		grp_ResultText.setLayout(fl_grp_ResultText);
		txt_ResultText = new Text(grp_ResultText, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		txt_ResultText.setEditable(false);

		sc_Container.setContent(cp_Container);
		sc_Container.setMinSize(cp_Container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		// This makes the ScrolledComposite scrolling, when the mouse 
		// is on a Text with one or more of the following tags: SWT.READ_ONLY,
		// SWT.V_SCROLL or SWT-H_SCROLL.
		SmoothScroller.scrollSmooth(sc_Container);

		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, "org.jcryptool.analysis.ngram.view"); //$NON-NLS-1$

	}

	public void reset() {
		cb_LoadText.select(0);
		cb_ChooseLang.select(0);
		cb_ChooseDist.select(0);
		txt_CipherText.setText(""); //$NON-NLS-1$
		referenceText = ""; //$NON-NLS-1$
		txt_Reference.setText(""); //$NON-NLS-1$
		txt_Reference.setEnabled(false);
		lbl_FileInfo.setText(Messages.NgramView_65);
		grp_CipherText.setText(Messages.NgramView_66);
		txt_ResultText.setText(""); //$NON-NLS-1$
	}

	public void NgramCalculate() {
		new NgramCode().NgramCalculate(this);
	}

	public String getCipherText() {
		return (txt_CipherText.getText());
	}

	public int getCipherLanguage() {
		return (cb_ChooseLang.getSelectionIndex());
	}

	public int getDistanceMethod() {
		return (cb_ChooseDist.getSelectionIndex());
	}

	public String getReferenceText() {
		return (referenceText);
	}

	public String getReferenceTitle() {
		return (txt_Reference.getText());
	}

	public void setResultText(String text) {
		txt_ResultText.setText(text);
	}
}