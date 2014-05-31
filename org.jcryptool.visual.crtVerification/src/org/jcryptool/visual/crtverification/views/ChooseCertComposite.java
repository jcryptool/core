package org.jcryptool.visual.crtverification.views;

import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.jcryptool.visual.crtverification.keystore.KeystoreConnector;

public class ChooseCertComposite extends Composite {
	private Composite parent;
	private ChooseCertPage page;
	private int SelectedCertIndex;
	KeystoreConnector ksc = new KeystoreConnector();
	private CrtVerViewController controller = new CrtVerViewController();

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
        list.setBounds(10, 10, 430, 280);
        
        for(int i=0;i<ksc.getAllCertificates().size();i++){
        	list.add(ksc.getAllCertificates().get(i).getSubjectDN().toString());
        }
        
        list.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		page.setPageComplete(true);
        		SelectedCertIndex = list.getSelectionIndex();
        		controller.setTN(ksc.getAllCertificates().get(SelectedCertIndex));
        	}
        });
        
       
       
       
        // Example End
        
    }
}
