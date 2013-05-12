package org.jcryptool.visual.jctca.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.jcryptool.visual.jctca.Util;
import org.jcryptool.visual.jctca.CertificateClasses.CSR;
import org.jcryptool.visual.jctca.CertificateClasses.CertificateCSRR;
import org.jcryptool.visual.jctca.CertificateClasses.RegistrarCSR;
import org.jcryptool.visual.jctca.RegistrarViews.Messages;

public class CSRListener implements SelectionListener {

	Label first;
	Label last;
	Label street;
	Label zip;
	Label town;
	Label country;
	Label mail;
	Button btn_forward;
	Button btn_reject;
	List csrs;
	public CSRListener(Label first, Label last, Label street, Label zip,
			Label town, Label country, Label mail, Button btn_forward,
			Button btn_reject, List csrs) {
		super();
		this.first = first;
		this.last = last;
		this.street = street;
		this.zip = zip;
		this.town = town;
		this.country = country;
		this.mail = mail;
		this.btn_forward = btn_forward;
		this.btn_reject = btn_reject;
		this.csrs = csrs;
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		Object src = arg0.getSource();
		int index = csrs.getSelectionIndex();
		RegistrarCSR regCSR = RegistrarCSR.getInstance();
		CSR csr = regCSR.getCSR(index);
		if(src instanceof List && csr != null){
			this.setLabels(csr);
		}
		else if(src instanceof Button && csr != null){
			Button btn = (Button)src;
			Integer data = (Integer) btn.getData();
			if(data.equals(0)){
				csrs.remove(index);
				regCSR.removeCSR(csr);
				if(csr.getPrivAlias()==null){
					Util.showMessageBox("Gratulation!", "Sehr gut! Dieser CSR war gefälscht. Sie haben ihn abgelehnt und dadurch verhindert, dass sich diese Person als eine andere ausgeben könnte.", SWT.ICON_WARNING);
				}
				this.setLabels(new CSR("","","","","","","", "", null, null));
			}
			else if(data.equals(1)){
				csrs.remove(index);
				regCSR.removeCSR(csr);
				if(csr.getPrivAlias()==null){
					Util.showMessageBox("Vorsicht!", "Sie hätten gerade einen gefälschten CSR weitergeleitet und dem Antragsteller somit ermöglicht, sich als eine andere Person auszugeben. Das darf einem echten PKI-Betreiber nicht passieren!", SWT.ICON_WARNING);
				}
				this.setLabels(new CSR("","","","","","","","", null, null));
				if(csr.getPubAlias()!=null){
					CertificateCSRR.getInstance().addCSR(csr);
				}
			}
		}
		 mail.getParent().layout();
	}
	
	public void setLabels(CSR csr){
		first.setText(csr.getFirst());
		last.setText(csr.getLast());
		street.setText(csr.getStreet());
		zip.setText(csr.getZip());
		town.setText(csr.getTown());
		country.setText(csr.getCountry());
		mail.setText(csr.getMail());
		btn_forward.setEnabled(csr.isForwardenabled());
		btn_reject.setEnabled(csr.isRejectenabled());
		mail.getParent().layout();
	}

}
