package org.jcryptool.visual.crtverification.views;

import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

public class ChooseCertComposite extends Composite {
	private ChooseCertPage page;
	CrtVerViewController controller = new CrtVerViewController();
	Button btnLoad;
    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public ChooseCertComposite(Composite parent, int style, ChooseCertPage p) {
        super(parent, style);
        this.page = p;
        
        ListViewer listViewer = new ListViewer(this, SWT.BORDER | SWT.V_SCROLL);
        final List list = listViewer.getList();
        list.setBounds(10, 10, 430, 211);
        
        btnLoad = new Button(this, SWT.NONE);
        btnLoad.setBounds(346, 227, 94, 28);
        btnLoad.setText("Load");
        btnLoad.setEnabled(false);
        
        // Wenn der Load-Button gedrueckt wird wird das Certifikat geladen und der Finish-Button aktiv
        btnLoad.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		controller.loadCertificate(page, list);
        	}
        });
        
        // Wenn ein Listen-Element ausgewaehlt wird, wird der Load-Button aktiv 
        list.addSelectionListener(new SelectionAdapter(){
            @Override
            public void widgetSelected(SelectionEvent e) {
               btnLoad.setEnabled(true);
            }
        });
        
        for(int i=0;i<controller.getKsc().getAllCertificates().size();i++){
        	list.add(controller.getKsc().getAllCertificates().get(i).getSubjectDN().toString());
        } 
       
        
    }
}
