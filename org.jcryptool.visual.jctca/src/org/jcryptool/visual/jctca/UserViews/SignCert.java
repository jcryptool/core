package org.jcryptool.visual.jctca.UserViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.jctca.Util;
import org.jcryptool.visual.jctca.listeners.SelectFileListener;

public class SignCert implements Views {

	Composite composite;
	Combo cmb_priv_key;
	Label selected_file;

	public SignCert(Composite content, Composite exp) {
		this.composite = new Composite(content, SWT.NONE);
		this.composite.setLayout(new GridLayout(1, false));
		GridData gd_comp = new GridData(SWT.FILL, SWT.TOP, true, true);
		composite.setLayoutData(gd_comp);

		Group signCertGroup = new Group(composite, SWT.NONE);
		signCertGroup.setLayout(new GridLayout(1, false));
		GridData gd_grp = new GridData(SWT.FILL, SWT.TOP, true, true);
		signCertGroup.setLayoutData(gd_grp);
		signCertGroup.setText("Signatur erstellen");

		Button btn_select_file = new Button(signCertGroup, SWT.PUSH);
		btn_select_file.setText("Datei ausw\u00E4hlen");
		btn_select_file.setData("select");//$NON-NLS-1$
		
		Composite cmp_mini = new Composite(signCertGroup, SWT.NONE);
		cmp_mini.setLayout(new GridLayout(2,false));
		cmp_mini.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false,true,1,1));
		
		selected_file = new Label(cmp_mini, SWT.NONE);
		Button btn_deselect_file = new Button(cmp_mini,SWT.NONE);
		btn_deselect_file.setText("Auswahl aufheben");
		btn_deselect_file.setData("deselect");//$NON-NLS-1$
		btn_deselect_file.setVisible(false);
		
		Label lbl_or = new Label(signCertGroup, SWT.CENTER);
		lbl_or.setText("oder");

		GridData gd_txt = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 20);
		Text txt_sign = new Text(signCertGroup, SWT.LEFT | SWT.MULTI
				| SWT.BORDER);
		txt_sign.setText("Text zum Signieren eingeben...");
		txt_sign.setLayoutData(gd_txt);

		
		btn_select_file.addSelectionListener(new SelectFileListener(selected_file, txt_sign, btn_deselect_file));
		btn_deselect_file.addSelectionListener(new SelectFileListener(selected_file, txt_sign, btn_deselect_file));
		
		cmb_priv_key = new Combo(signCertGroup, SWT.DROP_DOWN);
		cmb_priv_key.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, true));
		addRSAAndDSAPrivateKeysToDropdown();
		cmb_priv_key.select(0);

		Button btn_detail = new Button(signCertGroup, SWT.CHECK);
		btn_detail.setText("Signaturvorgang visualisiert anzeigen");

		Button btn_sign = new Button(signCertGroup, SWT.NONE);
		btn_sign.setText("Text mit ausgew\u00E4hltem Schl\u00FCssel signieren");
		Label lbl_exp = (Label) exp.getChildren()[0];
		lbl_exp.setText("In dieser Ansicht haben Sie die Möglichkeit, eine Datei auf Ihrem Computer oder den hier eingegebenen Text zu signieren. Signiert wird immer mit dem privaten Schlüssel eines Schlüsselpaares. Aus den privaten Schlüssel, für deren korrespondierenden öffentlichen Schlüssel Sie bereits Zertifikate ausgestellt bekommen haben, können Sie in der Dropdownliste auswählen. Der Text oder die Datei wird mit dem ausgewählten Schlüssel signiert. Die signierten Daten und die dazugehörige Signatur stehen danach in der Ansicht \"2. Benutzer\" zur Überprüfung zur Verfügung.\n\nWenn Sie genauer wissen wollen, was beim Erstellen einer Signatur mathematisch passiert, können Sie mittels \"Visualisiert signieren\" in ein JCrypTool-Plugin wechseln, das sich genauer mit Signaturen auseinandersetzt. Die Daten und Signaturen, die Sie in diesem Plugin erstellen, stehen danach ebenfalls in der Ansicht \"2. Benutzer\" zur Verfügung.\n\n\"Organisation\" und \"Organisationseinheit\" dienen dazu, Firmen, Organisationen und beispielsweise Abteilungen dieser Firma oder Organisation anzugeben. Da dieses Zertifikat auf eine Privatperson ausgestellt wurde, sind diese Felder einfach leer.\n\nNach den Angaben zum Zertifikatsinhaber folgen die Informationen über die CA, welche das Zertifikat ausgestellt hat. Zuletzt beinhaltet ein Zertifikat immer auch ein Ausstellungsdatum und ein Gültigkeitsdatum. Wie sich dieses Gültigkeitsdatum auf Signaturen auswirkt, wird in der Ansicht \"2. Benutzer\" behandelt.\n\nMittels \"Zertifikat widerrufen\" können Sie ein Zertifikat, das laut Gültigkeitsdatum noch gültig wäre, als ungültig erklären. Dies kann beispielsweise notwendig sein, weil der private Schlüssel kompromittiert (also gestohlen oder gebrochen) oder verloren wurde, weil etwa in eine CA eingebrochen wurde oder einfach auch, weil eine Person aus der im Zertifikat angegebenen Organisation ausgeschieden ist. Mehr zu Information dazu lesen Sie in der Onlinehilfe.");

		composite.setVisible(false);
	}

	private void addRSAAndDSAPrivateKeysToDropdown(){
		KeyStoreManager ksm = KeyStoreManager.getInstance();
		for (KeyStoreAlias ksAlias : Util.getAllRSAAndDSAPublicKeys(ksm)) {
			String listEntry = ksAlias.getContactName() + " (" + ksAlias.getKeyLength() + "bit ";
			if (ksAlias.getOperation().contains("RSA")) {
				listEntry += "RSA";
			} else {
				listEntry += "DSA";
			}
			listEntry+=", Hash: " + ksAlias.getHashValue() + ")";
			
			cmb_priv_key.add(listEntry);
		}
	}
	
	@Override
	public void setVisible(boolean visible) {
		composite.setVisible(visible);
	}

	@Override
	public void dispose() {
		composite.dispose();
	}

}
