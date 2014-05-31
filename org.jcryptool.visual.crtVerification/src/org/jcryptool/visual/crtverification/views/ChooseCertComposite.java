package org.jcryptool.visual.crtverification.views;

import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.jcryptool.visual.crtverification.keystore.KeystoreConnector;

public class ChooseCertComposite extends Composite {
	private Composite parent;
	private ChooseCertPage page;
	KeystoreConnector ksc = new KeystoreConnector();

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public ChooseCertComposite(Composite parent, int style, ChooseCertPage p) {
        super(parent, style);
        this.parent = parent;
        this.page = p;
        
        
        ListViewer listViewer = new ListViewer(this, SWT.BORDER | SWT.V_SCROLL);
        final List list = listViewer.getList();
        list.setBounds(10, 10, 430, 211);
        
        Button btnLoad = new Button(this, SWT.NONE);
        btnLoad.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		page.setPageComplete(true);
        		CrtVerViewController.setTN(ksc.getAllCertificates().get(list.getSelectionIndex()));
        	}
        });
        btnLoad.setBounds(346, 227, 94, 28);
        btnLoad.setText("Load");
        
        for(int i=0;i<ksc.getAllCertificates().size();i++){
        	list.add(ksc.getAllCertificates().get(i).getSubjectDN().toString());
        } 
       
        
    }
}
