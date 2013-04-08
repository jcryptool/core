package org.jcryptool.visual.jctca.UserViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CreateCert implements Views{

	Composite composite;
	
	Label lbl_firstname;
	Text txt_firstname;
	
	Label lbl_lastname;
	Text txt_lastname;
	
	Label lbl_street;
	Text txt_street;
	
	Label lbl_ZIP;
	Text txt_ZIP;
	
	Label lbl_city;
	Text txt_city;
	
	Label lbl_country;
	Text txt_country;
	
	Label lbl_mail;
	Text txt_mail;
	
	Label lbl_proof;
	Button btn_proof;
	
	Label lbl_key;
	Button btn_key;
	
	Label lbl_plain;
	Button btn_send;
	
	Label lbl_plain1;
	Button btn_genKey;
	
	public CreateCert(Composite content){
        composite = new Composite(content, SWT.NONE);
        composite.setLayout(new GridLayout(2, true));
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        lbl_firstname = new Label(composite, SWT.NONE);
        lbl_firstname.setText("Vornamen");    
        txt_firstname = new Text(composite, SWT.SINGLE | SWT.BORDER);
        txt_firstname.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_lastname = new Label(composite, SWT.None);
        lbl_lastname.setText("Nachname");
        txt_lastname = new Text(composite, SWT.SINGLE | SWT.BORDER);
        txt_lastname.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_street = new Label(composite, SWT.None);
        lbl_street.setText("Straße und Hausnummer");
        txt_street = new Text(composite, SWT.SINGLE | SWT.BORDER);
        txt_street.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_ZIP = new Label(composite, SWT.None);
        lbl_ZIP.setText("Postleitzahl");
        txt_ZIP = new Text(composite, SWT.SINGLE | SWT.BORDER);
        txt_ZIP.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_city = new Label(composite, SWT.None);
        lbl_city.setText("Ort");
        txt_city = new Text(composite, SWT.SINGLE | SWT.BORDER);
        txt_city.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_country = new Label(composite, SWT.None);
        lbl_country.setText("Land");
        txt_country = new Text(composite, SWT.SINGLE | SWT.BORDER);
        txt_country.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_mail = new Label(composite, SWT.None);
        lbl_mail.setText("E-Mail");
        txt_mail = new Text(composite, SWT.BORDER | SWT.SINGLE);
        txt_mail.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_proof = new Label(composite, SWT.None);
        lbl_proof.setText("Identitätsnachweis");
        btn_proof = new Button(composite, SWT.None);
        btn_proof.setText("Datei auswählen");
        btn_proof.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_key = new Label(composite, SWT.None);
        lbl_key.setText("Öffentlicher Schlüssel");
        btn_key = new Button(composite, SWT.NONE);
        btn_key.setText("Öffentlichen Schlüssel auswählen");
        btn_key.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_plain = new Label(composite, SWT.NONE);
        btn_genKey = new Button(composite, SWT.NONE);
        btn_genKey.setText("Schlüsselpaar generieren");
        btn_genKey.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lbl_plain1 = new Label(composite, SWT.None);
        btn_send = new Button(composite, SWT.NONE);
        btn_send.setText("CSR abschicken");
        btn_send.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
                
        composite.setVisible(false);
	}
	
	public void dispose(){
		this.composite.dispose();
	}
	
	public void setVisible(boolean visible){
		this.composite.setVisible(visible);
	}
}
