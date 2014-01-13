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

public class ClientFinishedComposite extends Composite implements ProtocolStep {
	private Button btnInformationen;
	private boolean infoText = false;
	private String strText;
	private SslView sslView;
	private Group grpClientFinished;
	private Label lblFinished;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public ClientFinishedComposite(Composite parent, int style,
			final SslView sslView) {
		super(parent, style);
		this.sslView = sslView;

		grpClientFinished = new Group(this, SWT.NONE);
		grpClientFinished.setBounds(0, 0, 326, 175);
		grpClientFinished
		.setText(Messages.ClientFinishedCompositeGrpServerFinished);
		
		lblFinished = new Label(grpClientFinished, SWT.NONE);
		lblFinished.setBounds(10, 25, 50, 20);
		lblFinished.setText(Messages.ClientFinishedCompositeLblFinished);
		
		btnInformationen = new Button(grpClientFinished, SWT.NONE);
		btnInformationen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				infoText = !infoText;
				if(btnInformationen.getText().equals(Messages.btnInformationToggleParams)){
					btnInformationen.setText(Messages.ClientCertificateCompositeBtnInfo);
				}else{
					btnInformationen.setText(Messages.btnInformationToggleParams);
				}
				refreshInformations();
				
			}
		});
		btnInformationen.setLocation(216, 140);
		btnInformationen.setSize(100, 25);
		btnInformationen
		.setText(Messages.ClientFinishedCompositeBtnInformation);
	}

	public void startStep() {
		strText = Messages.ClientFinishedInitationText;
		refreshInformations();
	}

	public void refreshInformations() {
	}

	public void enableControls() {
		btnInformationen.setEnabled(true);
		refreshInformations();
	}

	public void disableControls() {
		btnInformationen.setEnabled(false);
		btnInformationen.setText(Messages.ClientCertificateCompositeBtnInfo);
	}

	public boolean checkParameters() {
		return true;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public void resetStep() {
		infoText = false;
		btnInformationen.setText(Messages.ClientCertificateCompositeBtnInfo);
	}
}
