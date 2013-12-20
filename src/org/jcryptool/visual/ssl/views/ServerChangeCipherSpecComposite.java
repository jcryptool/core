package org.jcryptool.visual.ssl.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Group;
import org.jcryptool.visual.ssl.protocol.Message;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.ui.PlatformUI;

public class ServerChangeCipherSpecComposite extends Composite 
{
	private boolean infoText=false;
	private Button btnInformation;
	private Button btnNextStep;
	private SslView sslView;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ServerChangeCipherSpecComposite(Composite parent, int style, final SslView sslView) 
	{
		super(parent, style);
		this.sslView = sslView;
		Group grpServerChangeCipher = new Group(this, SWT.NONE);
		grpServerChangeCipher.setText(Messages.ServerChangeCipherSpecCompositeLblServerChangeCipher);
		grpServerChangeCipher.setBounds(10, 0, 326, 175);
		
		Label lblChangeCipherSpec = new Label(grpServerChangeCipher, SWT.NONE);
		lblChangeCipherSpec.setBounds(10, 25, 110, 20);
		lblChangeCipherSpec.setText(Messages.ServerChangeCipherSpecCompositeLblServerChangeCipherSpec);
		
		btnInformation = new Button(grpServerChangeCipher, SWT.NONE);
		btnInformation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				/*MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_INFORMATION|SWT.OK);
				 messageBox.setMessage(Messages.ServerChangeCipherSpecInformationText);
				 messageBox.setText(Messages.ServerChangeCipherSpecCompositeBtnInformation);
				 messageBox.open();*/
				infoText=true;
				refreshInformations();
			}
		});
		btnInformation.setLocation(70, 140);
		btnInformation.setSize(100, 25);
		btnInformation.setText(Messages.ServerChangeCipherSpecCompositeBtnInformation);
		
		btnNextStep = new Button(grpServerChangeCipher, SWT.NONE);
		btnNextStep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				sslView.nextStep();
			}
		});
		btnNextStep.setText(Messages.ServerChangeCipherSpecCompositeBtnNextStep);
		btnNextStep.setBounds(176, 140, 140, 25);
		refreshInformations();
	}

	public void refreshInformations()
	{
		if(infoText)
		{
			sslView.setStxInformationText(Messages.ServerChangeCipherSpecInformationText);
		}
		else
		{
			sslView.setStxInformationText("");
		}
	}
	public void enableControls()
	{
		btnInformation.setEnabled(true);
		btnNextStep.setEnabled(true);
		refreshInformations();
	}
	
	public void disableControls() 
	{
		btnInformation.setEnabled(false);
		btnNextStep.setEnabled(false);
	}

	public boolean checkParameters()
	{
		return true;
	}
	
	@Override
	protected void checkSubclass() 
	{
		// Disable the check that prevents subclassing of SWT components
	}
}
