package org.jcryptool.visual.extendedrsa;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
//import org.jcryptool.visual.extendedrsa.ui.wizard.KeyringWizard;
import org.jcryptool.visual.extendedrsa.ui.wizard.NewIdentityWizard;
//import org.jcryptool.visual.extendedrsa.ui.wizard.NewKeypairWizard;

public class Test extends ViewPart{
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	public static final String ID = "org.jcryptool.visual.extendedrsa.Test";
	public Test() {
	}
	
	@Override
	public void createPartControl(Composite parent) {
		
		Group grpTeeest = new Group(parent, SWT.NONE);
		TabFolder tabFolder = new TabFolder(grpTeeest, SWT.NONE);
		tabFolder.setLocation(0, 83);
		tabFolder.setSize(1422, 649);
		
		TabItem tbtmAlice_1 = new TabItem(tabFolder, SWT.NONE);
		tbtmAlice_1.setText("Verschlüsseln");
		
		Group grpLalala = new Group(tabFolder, SWT.NONE);
		grpLalala.setText("");
		grpLalala.setVisible(false);
		tbtmAlice_1.setControl(grpLalala);
	    
	    Group grpAsdf = new Group(grpLalala, SWT.NONE);
	    grpAsdf.setText("Verschlüsselung");
	    grpAsdf.setBounds(185, 10, 433, 322);
	    
	    Label lblText = new Label(grpAsdf, SWT.NONE);
	    lblText.setBounds(21, 43, 59, 14);
	    lblText.setText("Klartext");
	    
	    text = new Text(grpAsdf, SWT.BORDER);
	    text.setBounds(94, 40, 315, 223);
	    
	    Label lblEmpfnger = new Label(grpAsdf, SWT.NONE);
	    lblEmpfnger.setText("Empfänger");
	    lblEmpfnger.setBounds(21, 10, 59, 14);
	    
	    Combo myCombo = new Combo(grpAsdf, SWT.DROP_DOWN | SWT.READ_ONLY);
	    myCombo.add("Bob - multiprime RSA (AA BB CC)");
	    myCombo.add("Dave - RSA (BB CC AA)");
	    myCombo.select(0);
	    myCombo.setBounds(94, 10, 315, 15);
	    
	    Button btnVerschlsseln = new Button(grpAsdf, SWT.NONE);
	    btnVerschlsseln.setBounds(298, 269, 111, 25);
	    btnVerschlsseln.setText("Verschlüsseln");
	    
	    Button btnSchrittweiseVerschlsseln = new Button(grpAsdf, SWT.NONE);
	    btnSchrittweiseVerschlsseln.setBounds(117, 267, 175, 28);
	    btnSchrittweiseVerschlsseln.setText("Schrittweise verschlüsseln");
	    
	    Group grpSchrittweiseVerschlsselung = new Group(grpLalala, SWT.NONE);
	    grpSchrittweiseVerschlsselung.setText("Schrittweise Verschlüsselung");
	    grpSchrittweiseVerschlsselung.setBounds(624, 10, 397, 322);
	    
	    Group grpErgebnis = new Group(grpLalala, SWT.NONE);
	    grpErgebnis.setText("Chiffrat (Hex)");
	    grpErgebnis.setBounds(1027, 10, 372, 322);
	    
	    text_1 = new Text(grpErgebnis, SWT.BORDER);
	    text_1.setBounds(10, 10, 348, 251);
	    
	    Button btnChiffratsenden = new Button(grpErgebnis, SWT.NONE);
	    btnChiffratsenden.setBounds(241, 267, 117, 28);
	    btnChiffratsenden.setText("Chiffrat senden");
	    
	    Group grpErklrungen = new Group(grpLalala, SWT.NONE);
	    grpErklrungen.setText("Erklärungen");
	    grpErklrungen.setBounds(10, 338, 1389, 249);
	    
	    Button btnMeineSchlssel = new Button(grpLalala, SWT.NONE);
//	    btnMeineSchlssel.addSelectionListener(new SelectionAdapter() {
//	    	@Override
//	    	public void widgetSelected(SelectionEvent e) {
//	    		new WizardDialog(getSite().getShell(), new KeyringWizard()).open();
//	    	}
//	    });
	    btnMeineSchlssel.setBounds(10, 257, 141, 50);
	    btnMeineSchlssel.setText("Meine Schlüssel");
	    
	    Button btnNeuesSchlsselpaarErstellen = new Button(grpLalala, SWT.NONE);
//	    btnNeuesSchlsselpaarErstellen.addSelectionListener(new SelectionAdapter() {
//	    	@Override
//	    	public void widgetSelected(SelectionEvent e) {
//	    		new WizardDialog(getSite().getShell(), new NewKeypairWizard()).open();
//	    	}
//	    });
	    btnNeuesSchlsselpaarErstellen.setBounds(10, 201, 141, 50);
	    btnNeuesSchlsselpaarErstellen.setText("Neues Schlüsselpaar");
	    
	    Button btnIdentittErstellen = new Button(grpLalala, SWT.NONE);
	    btnIdentittErstellen.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
//	    		new WizardDialog(getSite().getShell(), new NewIdentityWizard(tabFolder)).open();
	    	}
	    });
	    btnIdentittErstellen.setBounds(10, 32, 141, 50);
	    btnIdentittErstellen.setText("Identität erstellen");
	    
	    Label lblNewLabel = new Label(grpLalala, SWT.NONE);
	    lblNewLabel.setBounds(10, 93, 103, 14);
	    lblNewLabel.setText("Aktuelle Identität: ");
	    Combo myCombo3 = new Combo(grpLalala, SWT.DROP_DOWN | SWT.READ_ONLY);
	    myCombo3.add("Alice");
	    myCombo3.select(0);
	    myCombo3.setBounds(10, 112, 141, 20);
	    
	    Label label = new Label(grpLalala, SWT.SEPARATOR | SWT.HORIZONTAL);
	    label.setBounds(10, 159, 151, 6);
	    
	    TabItem tbtmBob = new TabItem(tabFolder, SWT.NONE);
	    tbtmBob.setText("Entschlüsseln");
	    
	    Group group = new Group(tabFolder, SWT.NONE);
	    group.setVisible(false);
	    group.setText("");
	    tbtmBob.setControl(group);
	    
	    Group grpEntschlsselung = new Group(group, SWT.NONE);
	    grpEntschlsselung.setText("Entschlüsselung");
	    grpEntschlsselung.setBounds(185, 10, 433, 322);
	    
	    text_2 = new Text(grpEntschlsselung, SWT.BORDER);
	    text_2.setBounds(83, 50, 326, 213);
	    
	    Button btnEntschlsseln = new Button(grpEntschlsselung, SWT.NONE);
	    btnEntschlsseln.setText("Entschlüsseln");
	    btnEntschlsseln.setBounds(298, 269, 111, 25);
	    
	    Button btnSchrittweiseEntschlsseln = new Button(grpEntschlsselung, SWT.NONE);
	    btnSchrittweiseEntschlsseln.setText("Schrittweise entschlüsseln");
	    btnSchrittweiseEntschlsseln.setBounds(117, 267, 175, 28);
	    
	    Label lblSchlssel = new Label(grpEntschlsselung, SWT.NONE);
	    lblSchlssel.setText("Schlüssel");
	    lblSchlssel.setBounds(18, 10, 59, 14);
	    
	    Combo combo = new Combo(grpEntschlsselung, SWT.READ_ONLY);
	    combo.setBounds(83, 10, 315, 15);
	    combo.select(0);
	    
	    Label lblChiffrat = new Label(grpEntschlsselung, SWT.NONE);
	    lblChiffrat.setBounds(18, 50, 59, 14);
	    lblChiffrat.setText("Chiffrat");
	    
	    Group grpSchrittweiseEntschlsselung = new Group(group, SWT.NONE);
	    grpSchrittweiseEntschlsselung.setText("Schrittweise Entschlüsselung");
	    grpSchrittweiseEntschlsselung.setBounds(624, 10, 397, 322);
	    
	    Group grpKlartext = new Group(group, SWT.NONE);
	    grpKlartext.setText("Klartext");
	    grpKlartext.setBounds(1027, 10, 372, 322);
	    
	    text_3 = new Text(grpKlartext, SWT.BORDER);
	    text_3.setBounds(10, 10, 348, 251);
	    
	    Button btnZurcksetzen = new Button(grpKlartext, SWT.NONE);
	    btnZurcksetzen.setBounds(244, 267, 104, 28);
	    btnZurcksetzen.setText("Zurücksetzen");
	    
	    Group group_4 = new Group(group, SWT.NONE);
	    group_4.setText("Erklärungen");
	    group_4.setBounds(10, 338, 1389, 249);
	    
	    Button button = new Button(group, SWT.NONE);
	    button.setText("Meine Schlüssel");
	    button.setBounds(10, 257, 141, 50);
	    
	    Button button_1 = new Button(group, SWT.NONE);
	    button_1.setText("Neues Schlüsselpaar");
	    button_1.setBounds(10, 201, 141, 50);
	    
	    
	    
	    
	    Button button_2 = new Button(group, SWT.NONE);
	    button_2.setText("Identität erstellen");
	    button_2.setBounds(10, 32, 141, 50);
	    
	    Label label_1 = new Label(group, SWT.NONE);
	    label_1.setText("Aktuelle Identität: ");
	    label_1.setBounds(10, 93, 103, 14);
	    
	    Combo combo_1 = new Combo(group, SWT.READ_ONLY);
	    combo_1.setBounds(10, 112, 141, 20);
	    combo_1.add("Bob");
	    combo_1.select(0);
	    
	    Label label_2 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
	    label_2.setBounds(10, 159, 151, 6);
	    
	    Composite composite = new Composite(grpTeeest, SWT.NONE);
	    composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
	    composite.setBounds(10, 10, 556, 64);
	    
	    Label lblHierStehtDie = new Label(composite, SWT.NONE);
	    lblHierStehtDie.setBounds(0, 28, 556, 26);
	    lblHierStehtDie.setText("Hier steht die Beschreibung");
	    
	    Label lblRsaKryptosystemNeu = new Label(composite, SWT.NONE);
	    lblRsaKryptosystemNeu.setFont(SWTResourceManager.getFont("Lucida Grande", 13, SWT.BOLD));
	    lblRsaKryptosystemNeu.setBounds(0, 0, 168, 22);
	    lblRsaKryptosystemNeu.setText("RSA Kryptosystem neu");
		
	    
	    PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,Activator.PLUGIN_ID + ".rsaExtView"); //$NON-NLS-1$
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
}
