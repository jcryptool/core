package org.jcryptool.analysis.bruteforce;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.logging.utils.LogUtil;

public class View extends ViewPart {

	public static IProgressMonitor monitor;
	private Button okButton;
	private Text[] keyPattern;
	private Text ciphertext;
	private Text[] keyFound;
	private Text likelihood;
	
	public View() {}

	@Override
	public void createPartControl(final Composite parent) {
		parent.setLayout(new GridLayout());

		{
			Group ciphertextGroup = new Group(parent, SWT.NONE);
			ciphertextGroup.setText("Ciphertext");
			GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
			ciphertextGroup.setLayoutData(gridData);
			ciphertextGroup.setLayout(new FillLayout());

			ciphertext = new Text(ciphertextGroup, SWT.MULTI);
		}
		{
			Group keyGroup = new Group(parent, SWT.NONE);
			keyGroup.setText("Keys");
			GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
			keyGroup.setLayoutData(gridData);
			keyGroup.setLayout(new GridLayout(18, false));

			Label label = new Label(keyGroup, SWT.NONE);
			label.setText("Pattern:");
			
			keyPattern = new Text[16];
			gridData = new GridData();
			gridData.widthHint=25;
			for(int i=0; i<keyPattern.length; i++){
				keyPattern[i] = new Text(keyGroup, SWT.BORDER);
				keyPattern[i].setText("00");
				keyPattern[i].setLayoutData(gridData);
				
			}
			
			label = new Label(keyGroup, SWT.NONE);
			
			label = new Label(keyGroup, SWT.NONE);
			label.setText("Found:");
			
			keyFound = new Text[16];
			for(int i=0; i<keyFound.length; i++){
				keyFound[i] = new Text(keyGroup, SWT.READ_ONLY | SWT.BORDER);
				keyFound[i].setText("??");
				keyFound[i].setLayoutData(gridData);
			}
			likelihood = new Text(keyGroup, SWT.READ_ONLY | SWT.BORDER);
			likelihood.setLayoutData(gridData);
			
		}
		{
			Group operationsGroup = new Group(parent, SWT.NONE);
			operationsGroup.setText("Operations");
			GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
			operationsGroup.setLayoutData(gridData);
			operationsGroup.setLayout(new GridLayout(3, false));

			okButton = new Button(operationsGroup, SWT.PUSH);
			okButton.setText("Execute");
			final View gui = this;
			okButton.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						String key = getKey();
						setKey("????????????????????????????????", 0);
						String ciphertext = getCiphertext();
						ProgressDialog progressDialog = new ProgressDialog(key, ciphertext, gui);
						progressDialog.start();
					} catch (InvocationTargetException ex) {
					    LogUtil.logError(ex);
					} catch (InterruptedException ex) {
					    LogUtil.logError(ex);
					}
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			});			
		}

	}

	private String getCiphertext(){
		return ciphertext.getText();
	}

	protected void setKey(String key, double likelihood) {
		for(int i=0; i<this.keyFound.length; i++)
			this.keyFound[i].setText(key.substring(2*i, 2*i+2));
		this.likelihood.setText(Math.round(Math.floor(likelihood*100))+"%");
	}

	private String getKey(){
		StringBuilder stringBuilder = new StringBuilder();
		for(Text keyPart : keyPattern){
			stringBuilder.append(keyPart.getText().toUpperCase());
		}
		return stringBuilder.toString();
	}

	protected void setProgress(){
		monitor.worked(1);
	}

	@Override
	public void setFocus() {}
	
	private class ProgressDialog implements IRunnableWithProgress {

		private String key;
		private String ciphertext;
		private View gui;

		public ProgressDialog(String key, String ciphertext, View view) {
			this.key = key;
			this.ciphertext = ciphertext;
			this.gui = view;
		}
		
		@Override
		public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			final Bruteforce bruteforce = new Bruteforce(key, ciphertext, gui);
			View.monitor = monitor;			
			monitor.beginTask("Bruteforce suche", bruteforce.getKeySearchSpace());			
			
			CancelListener cancelListener = new CancelListener(bruteforce, monitor);
			cancelListener.start();
			
			bruteforce.searchKey();
			
			monitor.done();
			cancelListener.stopListening();
		}
		
		public void start() throws InvocationTargetException, InterruptedException{
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			ProgressMonitorDialog pmd = new ProgressMonitorDialog(shell);
			pmd.run(true, true, this);
		}		
	}

	private class CancelListener extends Thread {
		
		private boolean running;
		private Bruteforce bruteforce;
		private IProgressMonitor monitor;

		public CancelListener(Bruteforce bruteforce, IProgressMonitor monitor) {
			this.bruteforce = bruteforce;
			this.monitor = monitor;
		}
		
		public void stopListening() {
			running = false;
		}
		
		@Override
		public void run() {
			running = true;
			while(running){
				try{Thread.sleep(100);} catch (Exception e) {}
				if(monitor.isCanceled())
					bruteforce.cancel();
			}			
		}
	}
}
