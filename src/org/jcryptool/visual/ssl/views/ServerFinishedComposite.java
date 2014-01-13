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

public class ServerFinishedComposite extends Composite implements ProtocolStep{
	private boolean infoText = false;
	private Button btnInformation;
	private String strText;
	private SslView sslView;
	private Group grpServerFinished;
	private Label lblFinished;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public ServerFinishedComposite(Composite parent, int style,
			final SslView sslView) {
		super(parent, style);
		this.sslView = sslView;

		grpServerFinished = new Group(this, SWT.NONE);
		grpServerFinished.setBounds(0, 0, 326, 175);
		grpServerFinished
		.setText(Messages.ServerFinishedCompositeGrpServerFinished);

		lblFinished = new Label(grpServerFinished, SWT.NONE);
		lblFinished.setBounds(10, 25, 50, 20);
		lblFinished.setText(Messages.ServerFinishedCompositeLblFinished);

		btnInformation = new Button(grpServerFinished, SWT.NONE);
		btnInformation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				infoText = !infoText;
				if(btnInformation.getText().equals(Messages.btnInformationToggleParams)){
					btnInformation.setText(Messages.ClientCertificateCompositeBtnInfo);
				}else{
					btnInformation.setText(Messages.btnInformationToggleParams);
				}
				refreshInformations();
			}
		});
		btnInformation.setLocation(216, 140);
		btnInformation.setSize(100, 25);
		btnInformation.setText(Messages.ServerFinishedCompositeBtnInformation);
	}

	public void startStep() {
		strText = Messages.ServerFinishedInitationText;
		refreshInformations();
	}

	public void refreshInformations() {
	}

	public void enableControls() {
		btnInformation.setEnabled(true);
		refreshInformations();
	}

	public void disableControls() {
		btnInformation.setEnabled(false);
		btnInformation.setText(Messages.ClientCertificateCompositeBtnInfo);
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
		infoText=false;
		btnInformation.setText(Messages.ClientCertificateCompositeBtnInfo);
	}
}
