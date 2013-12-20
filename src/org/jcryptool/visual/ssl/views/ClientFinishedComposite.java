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

public class ClientFinishedComposite extends Composite implements ProtocolStep
{
	private Button btnInformationen;
	private boolean infoText=false;
	private SslView sslView;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ClientFinishedComposite(Composite parent, int style, final SslView sslView) 
	{
		super(parent, style);
		this.sslView = sslView;
		Group grpClientFinished = new Group(this, SWT.NONE);
		grpClientFinished.setText(Messages.ClientFinishedCompositeGrpServerFinished);
		grpClientFinished.setBounds(10, 0, 326, 175);
		
		Label lblFinished = new Label(grpClientFinished, SWT.NONE);
		lblFinished.setBounds(10, 25, 50, 20);
		lblFinished.setText(Messages.ClientFinishedCompositeLblFinished);
		
		btnInformationen = new Button(grpClientFinished, SWT.NONE);
		btnInformationen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				/*MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_INFORMATION|SWT.OK);
				 messageBox.setMessage(Messages.ClientFinishedInformationText);
				 messageBox.setText(Messages.ClientFinishedCompositeBtnInformation);
				 messageBox.open();*/
				//sslView.addTextToStxInformationText(Messages.ClientFinishedInformationText);
				infoText=true;
				refreshInformations();
			}
		});
		btnInformationen.setLocation(216, 140);
		btnInformationen.setSize(100, 25);
		btnInformationen.setText(Messages.ClientFinishedCompositeBtnInformation);
		refreshInformations();
	}

	public void refreshInformations()
	{
		if(infoText)
		{
			sslView.setStxInformationText(Messages.ClientFinishedInformationText);
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
