package org.jcryptool.visual.ssl.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Group;
import org.jcryptool.visual.ssl.protocol.Message;
import org.jcryptool.visual.ssl.protocol.ProtocolStep;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.ui.PlatformUI;

public class ClientChangeCipherSpecComposite extends Composite implements ProtocolStep
{
	private Button btnInformationen;
	private boolean infoText=false;
	private SslView sslView;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ClientChangeCipherSpecComposite(Composite parent, int style, final SslView sslView) 
	{
		super(parent, style);
		this.sslView = sslView;
		Group grpClientChangeCipher = new Group(this, SWT.NONE);
		grpClientChangeCipher.setText(Messages.ClientChangeCipherSpecCompositeLblClientChangeCipher);
		grpClientChangeCipher.setBounds(10, 0, 326, 175);
		
		Label lblChangeCipherSpec = new Label(grpClientChangeCipher, SWT.NONE);
		lblChangeCipherSpec.setBounds(10, 25, 110, 20);
		lblChangeCipherSpec.setText(Messages.ClientChangeCipherSpecCompositeLblClientChangeCipherSpec);
		
		btnInformationen = new Button(grpClientChangeCipher, SWT.NONE);
		btnInformationen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				/*MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_INFORMATION|SWT.OK);
				 messageBox.setMessage(Messages.ClientChangeCipherSpecInformationText);
				 messageBox.setText(Messages.ClientChangeCipherSpecCompositeBtnInformation);
				 messageBox.open();*/
				//sslView.addTextToStxInformationText(Messages.ClientChangeCipherSpecInformationText);
				infoText=true;
				refreshInformations();
			}
		});
		btnInformationen.setLocation(216, 140);
		btnInformationen.setSize(100, 25);
		btnInformationen.setText(Messages.ClientChangeCipherSpecCompositeBtnInformation);
		refreshInformations();
	}

	public void refreshInformations()
	{
		if(infoText)
		{
			sslView.setStxInformationText(Messages.ClientChangeCipherSpecInformationText);
		}
		else
		{
			sslView.setStxInformationText("");
		}
	}
	public void enableControls()
	{
		btnInformationen.setEnabled(true);
		refreshInformations();
	}
	
	public void disableControls() 
	{
		btnInformationen.setEnabled(false);
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
