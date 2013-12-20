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

public class ServerFinishedComposite extends Composite 
{
	private boolean infoText=false;
	private Button btnInformation;
	private SslView sslView;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ServerFinishedComposite(Composite parent, int style, final SslView sslView) 
	{
		super(parent, style);
		this.sslView = sslView;
		Group grpServerFinished = new Group(this, SWT.NONE);
		grpServerFinished.setText(Messages.ServerFinishedCompositeGrpServerFinished);
		grpServerFinished.setBounds(10, 0, 326, 175);
		
		Label lblFinished = new Label(grpServerFinished, SWT.NONE);
		lblFinished.setBounds(10, 25, 50, 20);
		lblFinished.setText(Messages.ServerFinishedCompositeLblFinished);
		
		btnInformation = new Button(grpServerFinished, SWT.NONE);
		btnInformation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				/*MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_INFORMATION|SWT.OK);
				 messageBox.setMessage(Messages.ServerFinishedInformationText);
				 messageBox.setText(Messages.ServerFinishedCompositeBtnInformation);
				 messageBox.open();*/
				infoText=true;
				refreshInformations();
			}
		});
		btnInformation.setLocation(216, 140);
		btnInformation.setSize(100, 25);
		btnInformation.setText(Messages.ServerFinishedCompositeBtnInformation);
		refreshInformations();
	}

	public void refreshInformations()
	{
		if(infoText)
		{
			sslView.setStxInformationText(Messages.ServerFinishedInformationText);
		}
		else
		{
			sslView.setStxInformationText("");
		}
	}
	
	public void enableControls()
	{
		btnInformation.setEnabled(true);
		refreshInformations();
	}
	
	public void disableControls() 
	{
		btnInformation.setEnabled(false);
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
