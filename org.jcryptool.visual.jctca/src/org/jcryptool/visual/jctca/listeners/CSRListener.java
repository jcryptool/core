package org.jcryptool.visual.jctca.listeners;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.jcryptool.visual.jctca.Util;
import org.jcryptool.visual.jctca.CertificateClasses.CSR;

public class CSRListener implements SelectionListener{

	
	Label first; 
	Label last;
	Label street; 
	Label zip; 
	Label town;
	Label country;
	Label mail;
	Label img;
		
	public CSRListener(Label first, Label last, Label street, Label zip,
			Label town, Label country, Label mail, Label img) {
		super();
		this.first = first;
		this.last = last;
		this.street = street;
		this.zip = zip;
		this.town = town;
		this.country = country;
		this.mail = mail;
		this.img = img;
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		List lst = (List)arg0.getSource();
		int index = lst.getSelectionIndex();
		CSR csr = Util.getCSR(index);
		if(csr != null){
			first.setText(csr.getFirst());
			last.setText(csr.getLast());
			street.setText(csr.getStreet());
			zip.setText(csr.getZip());
			town.setText(csr.getTown());
			country.setText(csr.getCountry());
			mail.setText(csr.getMail());
			img.setImage(new Image(Display.getCurrent(), csr.getProof()));
		}
	}

}
