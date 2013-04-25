package org.jcryptool.visual.jctca.UserViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.jcryptool.core.util.fonts.FontService;

public class ShowCert implements Views {
	Composite composite;
	Composite left;
	Composite right;
	List lst_certs;
	Label lbl_plain, lbl_plain1, lbl_plain2, lbl_plain3, lbl_plain4, lbl_plain5, lbl_plain6, lbl_plain7, lbl_plain8;
	
	Label lbl_issued_to;
	Label lbl_issued_by;
	
	Label lbl_common;
	Label lbl_value_common;
	Label lbl_common_by;
	Label lbl_value_common_by;

	Label lbl_org;
	Label lbl_value_org;
	Label lbl_org_by;
	Label lbl_value_org_by;

	Label lbl_orgUnit;
	Label lbl_value_orgUnit;
	Label lbl_orgUnit_by;
	Label lbl_value_orgUnit_by;
	
	Label lbl_city;
	Label lbl_value_city;

	Label lbl_state;
	Label lbl_value_state;

	Label lbl_country;
	Label lbl_value_country;

	Label lbl_mail;
	Label lbl_value_mail;
	
	Label lbl_issued_on;
	Label lbl_value_issued_on;
	Label lbl_expired_on;
	Label lbl_value_expired_on;

	Button btn_revoke;
	
	public ShowCert(Composite content, Composite exp) {
		composite = new Composite(content, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		GridData gd_comp = new GridData(SWT.FILL, SWT.TOP, true, true);
		composite.setLayoutData(gd_comp);
		
		Group showCertGroup = new Group(composite, SWT.NONE);
		showCertGroup.setLayout(new GridLayout(2, false));
		GridData gd_grp = new GridData(SWT.FILL, SWT.FILL, true, true);
		showCertGroup.setLayoutData(gd_grp);
		showCertGroup.setText("Zertifikate verwalten");
		

		left = new Composite(showCertGroup, SWT.NONE);
		left.setLayout(new GridLayout(1, true));
		left.setLayoutData(new GridData(SWT.NONE,SWT.FILL,false,true));
		right = new Composite(showCertGroup, SWT.NONE);
		right.setLayout(new GridLayout(2, false));

		lst_certs = new List(left, SWT.BORDER);
		lst_certs.setLayoutData(new GridData(SWT.NONE,SWT.FILL,false,true));
		lst_certs.add("   Zertifikat #1   ");
		lst_certs.add("   Zertifikat #2   ");
		lst_certs.add("   Zertifikat #3   ");
		lst_certs.add("   Zertifikat #4   ");

		lbl_issued_to = new Label(right,SWT.NONE);
		lbl_issued_to.setFont(FontService.getNormalBoldFont());
		lbl_issued_to.setText("Ausgestellt für");
		lbl_plain = new Label(right,SWT.NONE);
		
		lbl_common = new Label(right, SWT.NONE);
		lbl_common.setText("Common Name");
		lbl_value_common = new Label(right, SWT.None);
		lbl_value_common.setText("Erika Musterfrau");

		lbl_org = new Label(right, SWT.None);
		lbl_org.setText("Organisation");
		lbl_value_org = new Label(right, SWT.None);
		lbl_value_org.setText("nicht Teil dieses Zertifikats");

		lbl_orgUnit = new Label(right, SWT.None);
		lbl_orgUnit.setText("Organisationseinheit  ");
		lbl_value_orgUnit= new Label(right, SWT.None);
		lbl_value_orgUnit.setText("nicht Teil dieses Zertifikats");

		lbl_city = new Label(right, SWT.None);
		lbl_city.setText("Ort");
		lbl_value_city= new Label(right, SWT.None);
		lbl_value_city.setText("Berlin");

//		lbl_state = new Label(right, SWT.None);
//		lbl_state.setText("Bundesland");
//		lbl_value_state = new Label(right, SWT.None);
//		lbl_value_state.setText("Berlin");

		lbl_country = new Label(right, SWT.None);
		lbl_country.setText("Land");
		lbl_value_country =new Label(right, SWT.None);
		lbl_value_country.setText("Deutschland");

		lbl_mail = new Label(right, SWT.None);
		lbl_mail.setText("E-Mail");
		lbl_value_mail = new Label(right, SWT.None);
		lbl_value_mail.setText("e.musterfrau@gmail.com");
		
		lbl_plain1 = new Label(right,SWT.NONE);
		lbl_plain2 = new Label(right,SWT.NONE);
		lbl_issued_by = new Label(right,SWT.NONE);
		lbl_issued_by.setFont(FontService.getNormalBoldFont());
		lbl_issued_by.setText("Ausgestellt von");
		lbl_plain3 = new Label(right,SWT.NONE);
		
		lbl_common_by = new Label(right, SWT.NONE);
		lbl_common_by.setText("Common Name");
		lbl_value_common_by = new Label(right, SWT.None);
		lbl_value_common_by.setText("JCrypTool Certificate Authority");

		lbl_org_by = new Label(right, SWT.None);
		lbl_org_by.setText("Organisation");
		lbl_value_org_by = new Label(right, SWT.None);
		lbl_value_org_by.setText("JCrypTool");

		lbl_orgUnit_by = new Label(right, SWT.None);
		lbl_orgUnit_by.setText("Organisationseinheit  ");
		lbl_value_orgUnit_by= new Label(right, SWT.None);
		lbl_value_orgUnit_by.setText("Certificate Authority");
		
		lbl_plain4 = new Label(right,SWT.NONE);
		lbl_plain5 = new Label(right,SWT.NONE);
		lbl_issued_by = new Label(right,SWT.NONE);
		lbl_issued_by.setFont(FontService.getNormalBoldFont());
		lbl_issued_by.setText("Gültigkeitszeitraum");
		lbl_plain6 = new Label(right,SWT.NONE);
		
		lbl_issued_on = new Label(right, SWT.None);
		lbl_issued_on.setText("Ausgestellt am");
		lbl_value_issued_on = new Label(right, SWT.None);
		lbl_value_issued_on.setText("9/4/13");
		lbl_expired_on = new Label(right, SWT.None);
		lbl_expired_on.setText("Gültig bis");
		lbl_value_expired_on = new Label(right, SWT.None);
		lbl_value_expired_on.setText("10/4/4");
		
		lbl_plain7 = new Label(right,SWT.NONE);
		lbl_plain8 = new Label(right,SWT.NONE);
		btn_revoke = new Button(right,SWT.NONE);
		btn_revoke.setText("Zertifikat widerrufen");
		
	    Label lbl_exp = (Label)exp.getChildren()[0];
        lbl_exp.setText("In dieser Ansicht können Sie Ihre bereits ausgestellten Zertifikate anzehen. Die hier angezeigten Felder entsprechen dem bereits erwähnten X.509 Standard für digitale Zertifikate. Zuerst können Sie sehen, für wen das Zertifikat ausgestellt wurde. Der Common Name ist in diesem Fall der Name, den Sie bei der Erstellung Ihres CSR angegeben haben. Hier könnte aber auch eine E-Mail-Adresse oder der Name einer Firma stehen. Mehr zu den verschiedenen Arten von Zertifikaten lesen Sie in der Onlinehilfe.\n" +
        		"\"Organisation\" und \"Organisationseinheit\" dienen dazu, Firmen, Organisationen und beispielsweise Abteilungen dieser Firma oder Organisation anzugeben. Da dieses Zertifikat auf eine Privatperson ausgestellt wurde, sind diese Felder einfach leer.\n\n" +
        		"Nach den Angaben zum Zertifikatsinhaber folgen die Informationen über die CA, welche das Zertifikat ausgestellt hat. Zuletzt beinhaltet ein Zertifikat immer auch ein Ausstellungsdatum und ein Gültigkeitsdatum. Wie sich dieses Gültigkeitsdatum auf Signaturen auswirkt, wird in der Ansicht \"2. Benutzer\" behandelt.\n\n" +
        		"Mittels \"Zertifikat widerrufen\" können Sie ein Zertifikat, das laut Gültigkeitsdatum noch gültig wäre, als ungültig erklären. Dies kann beispielsweise notwendig sein, weil der private Schlüssel kompromittiert (also gestohlen oder gebrochen) oder verloren wurde, weil etwa in eine CA eingebrochen wurde oder einfach auch, weil eine Person aus der im Zertifikat angegebenen Organisation ausgeschieden ist. Mehr zu Information dazu lesen Sie in der Onlinehilfe.");
		composite.setVisible(false);
	}

	public void dispose() {
		this.composite.dispose();
	}

	public void setVisible(boolean visible) {
		composite.setVisible(visible);
	}
}
