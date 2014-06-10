package org.jcryptool.visual.crtverification.views;

import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;
import org.jcryptool.crypto.keystore.keys.KeyType;

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
        setLayout(new FormLayout());
        
        ListViewer listViewer = new ListViewer(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        final List list = listViewer.getList();
        FormData fd_list = new FormData();
        fd_list.top = new FormAttachment(0, 5);
        fd_list.right = new FormAttachment(100, -10);
        fd_list.left = new FormAttachment(0, 5);
        list.setLayoutData(fd_list);
        
        btnLoad = new Button(this, SWT.NONE);
        fd_list.bottom = new FormAttachment(btnLoad, -6);
        FormData fd_btnLoad = new FormData();
        fd_btnLoad.bottom = new FormAttachment(100, -10);
        fd_btnLoad.right = new FormAttachment(100, -10);
        btnLoad.setLayoutData(fd_btnLoad);
        btnLoad.setText("Load");
        btnLoad.setEnabled(false);
        
        // Wenn der Load-Button gedrueckt wird wird das Certifikat geladen und der Finish-Button aktiv
        btnLoad.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		controller.loadCertificate(page, (X509Certificate)controller.ksc.getAllCertificates().get(list.getSelectionIndex()));
        	}
        });
        
        // Wenn ein Listen-Element ausgewaehlt wird, wird der Load-Button aktiv 
        list.addSelectionListener(new SelectionAdapter(){
            @Override
            public void widgetSelected(SelectionEvent e) {
               btnLoad.setEnabled(true);
            }
        });
        
       
        //ArrayList<IKeyStoreAlias> publicKeys = new ArrayList<IKeyStoreAlias>();
        Enumeration<String> aliases = controller.getKsc().getAllAliases();
        //ArrayList<X509Certificate> certificates = controller.getKsc().getAllCertificates();
		while (aliases.hasMoreElements()) {
		    IKeyStoreAlias alias = new KeyStoreAlias(aliases.nextElement());
		    KeyType type = alias.getKeyStoreEntryType();
		    if(type == KeyType.KEYPAIR_PUBLIC_KEY || type == KeyType.PUBLICKEY){
		    	list.add(alias.getContactName());
		    }
		}  
		
		/* for(int i=0;i<controller.getKsc().getAllCertificates().size();i++){
	        	list.add(controller.getKsc().getAllCertificates().get(i).getSubjectDN().toString());
	        } */	
	        
		
    }
}
