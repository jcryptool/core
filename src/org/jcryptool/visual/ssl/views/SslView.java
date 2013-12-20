package org.jcryptool.visual.ssl.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * Represents the visual TLS-Plugin.
 * 
 * @author Florian Stehrer
 */
public class SslView extends ViewPart
{
	private Composite parent;
	private ScrolledComposite scrolledComposite;
	private Arrows arrow;
	private Composite content;
	private Composite mainContent;
	private Composite infoContent;
	private ClientHelloComposite clientHelloComposite;
	private ServerHelloComposite serverHelloComposite;
	private ServerCertificateComposite serverCertificateComposite;
	private ClientCertificateComposite clientCertificateComposite;
	private ServerChangeCipherSpecComposite serverChangeCipherSpecComposite;
	private ClientChangeCipherSpecComposite clientChangeCipherSpecComposite;
	private ClientFinishedComposite clientFinishedComposite;
	private ServerFinishedComposite serverFinishedComposite;
	private StyledText stxInformation;
	
	public static final String ID = "org.jcryptool.visual.ssl.views.SslView"; //$NON-NLS-1$
	
	public SslView() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(final Composite parent) 
	{
		this.parent = parent;
		scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		mainContent = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(mainContent);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setMinSize(mainContent.computeSize(1300, 900));
		
		//mainContent.setLayout(new GridLayout(2,false));
		//mainContent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		content = new Composite(mainContent, SWT.NONE);
		
		GridLayout gl = new GridLayout(1, false);
        gl.verticalSpacing = 20;
        mainContent.setLayout(gl);

		infoContent = new Composite(mainContent, SWT.NONE);
		infoContent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		createGui();
		
		scrolledComposite.pack();
		
		//Fuer die Hilfe:
		//PlatformUI.getWorkbench().getHelpSystem().setHelp(parent.getShell(), SslPlugin.PLUGIN_ID + ".sslview");
	}
	
	/**
	 * Creates the Elements of the GUI.
	 */
	private void createGui()
	{
		Composite swtAwtComponent = new Composite(content, SWT.EMBEDDED);
		swtAwtComponent.setBounds(350, 50, 200, 760);
		swtAwtComponent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		swtAwtComponent.setLayout(new GridLayout());
		java.awt.Frame frame = SWT_AWT.new_Frame(swtAwtComponent);
		arrow = new Arrows();
		frame.add(arrow);
		
		stxInformation = new StyledText(content, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
		stxInformation.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		stxInformation.setVisible(true);
		stxInformation.setBounds(900, 50, 400, 785);
		stxInformation.setLayoutData(new GridData(SWT.FILL, SWT.END, true, false));
		stxInformation.setText(Messages.SslViewStxInformation);
		
		//stxInformation.setLocation(0, 73);
		//GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		//gridData.horizontalAlignment = GridData.FILL;
		//gridData.grabExcessHorizontalSpace = true;
		//gridData.widthHint = 400;
		//gridData.heightHint = 750;
		//stxInformation.setLayoutData(gridData);
		
		clientHelloComposite = new ClientHelloComposite(content, SWT.NONE, this);
		clientHelloComposite.setBounds(10, 50, 349, 177);
		clientHelloComposite.setLayout(new GridLayout(3, true));
		
		Button btnPreviousStep = new Button(content, SWT.NONE);
		btnPreviousStep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				previousStep();
			}
		});
		btnPreviousStep.setText(Messages.SslViewBtnPreviousStep);
		btnPreviousStep.setBounds(300, 865, 140, 25);
		btnPreviousStep.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		
		Button btnNextStep = new Button(content, SWT.NONE);
		btnNextStep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) 
			{
				nextStep();
			}
		});
		btnNextStep.setText(Messages.SslViewBtnNextStep);
		btnNextStep.setBounds(460, 865, 140, 25);
		btnNextStep.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		
		Button btnReset = new Button(content, SWT.NONE);
		btnReset.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseUp(MouseEvent e) 
			{
				resetStep();
			}
		});
		btnReset.setText(Messages.SslViewBtnReset);
		btnReset.setBounds(620,865, 140, 25);
		btnReset.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		
		Label lblClient = new Label(content, SWT.NONE);
		lblClient.setBounds(159, 20, 32, 15);
		lblClient.setText(Messages.SslViewLblClient);
		
		Label lblServer = new Label(content, SWT.NONE);
		lblServer.setBounds(700, 20, 32, 15);
		lblServer.setText(Messages.SslViewLblServer);
		
		Label test = new Label(content, SWT.NONE);
		test.setBounds(1030, 20, 100, 15);
		test.setText(Messages.SslViewLblInfo);
	}

	/**
	 * Sets the text of the Information-Box.
	 * All other content of the Information-Box will be deleted.
	 * @param text
	 */
	public void setStxInformationText(String text)
	{
		stxInformation.setText(text);
	}
	
	/**
	 * Returns the text of the Information-Box.
	 * @return
	 */
	public String getStxInformationText()
	{
		return stxInformation.getText();
	}
	
	/**
	 * Adds text to the Information-Box.
	 * In contrast to the function "setStxInformationText()",
	 * all other content of the Information-Box will not be deleted.
	 * @param text
	 */
	public void addTextToStxInformationText(String text)
	{
		stxInformation.setText(stxInformation.getText()+text);
	}
	
	/**
	 * This method uses the "previousStep()"-function to
	 * move from ServerHelloComposite to ClientHelloComposite
	 */
	public void backToClientHello()
	{
		arrow.moveArrowsby(18);
		arrow.nextArrow(200,75,10,75,0,0,0);
		arrow.nextArrow(10,75,200,75,0,0,0);
		previousStep();
	}
	
	/**
	 * Initializes the next Composite in the TLS-Handshake.
	 */
	public void nextStep()
	{
		if(serverHelloComposite == null)
		{
			if(clientHelloComposite.checkParameters())
			{
				arrow.moveArrowsby(18);
				serverHelloComposite = new ServerHelloComposite(content, SWT.NONE, this);
				serverHelloComposite.setBounds(550, 50, 349, 177);
				serverHelloComposite.setLayout(new GridLayout(3, true));
				clientHelloComposite.disableControls();
				arrow.nextArrow(10,75,200,75,0,0,0);
			}
		}
		else if(serverCertificateComposite == null)
		{
			if(serverHelloComposite.checkParameters())
			{
				serverCertificateComposite = new ServerCertificateComposite(content, SWT.NONE, this);
				serverCertificateComposite.setBounds(550, 233, 349, 177);
				serverCertificateComposite.setLayout(new GridLayout(3, true));
				serverHelloComposite.disableControls();
			}
		}
		else if(clientCertificateComposite == null)
		{
			if(serverCertificateComposite.checkParameters())
			{
				clientCertificateComposite = new ClientCertificateComposite(content, SWT.NONE, this);
				clientCertificateComposite.setBounds(10, 295, 349, 177);
				clientCertificateComposite.setLayout(new GridLayout(3, true));
				serverCertificateComposite.disableControls();
				arrow.nextArrow(345,250,10,330,0,0,0);
			}
		}
		else if(serverChangeCipherSpecComposite == null)
		{
			if(clientCertificateComposite.checkParameters())
			{
				serverChangeCipherSpecComposite = new ServerChangeCipherSpecComposite(content, SWT.NONE, this);
				serverChangeCipherSpecComposite.setBounds(550, 483, 349, 177);
				serverChangeCipherSpecComposite.setLayout(new GridLayout(3, true));
				serverFinishedComposite = new ServerFinishedComposite(content, SWT.NONE, this);
				serverFinishedComposite.setBounds(550, 680, 349,177);
				serverFinishedComposite.setLayout(new GridLayout(3, true));
				clientCertificateComposite.disableControls();
				arrow.nextArrow(10,350,200,520,0,0,0);
			}
		}
		else if(clientChangeCipherSpecComposite == null)
		{
			if(serverChangeCipherSpecComposite.checkParameters())
			{
				clientChangeCipherSpecComposite = new ClientChangeCipherSpecComposite(content, SWT.NONE, this);
				clientChangeCipherSpecComposite.setBounds(10, 483, 349, 177);
				clientChangeCipherSpecComposite.setLayout(new GridLayout(3, true));
				clientFinishedComposite = new ClientFinishedComposite(content, SWT.NONE, this);
				clientFinishedComposite.setBounds(10, 680, 349, 177);
				clientFinishedComposite.setLayout(new GridLayout(3, true));
				serverChangeCipherSpecComposite.disableControls();
				serverFinishedComposite.disableControls();
				arrow.nextArrow(345, 540, 10, 540, 0,0,0);
				arrow.nextArrow(10, 575, 200, 575, 0, 0, 0);
				arrow.nextArrow(345, 700, 10, 700, 0,180,0);
				arrow.nextArrow(10, 750, 200, 750, 0, 180, 0);
			}
		}
	}
	
	/**
	 * Restarts the whole Plugin.
	 */
	public void resetStep()
	{
		if(clientChangeCipherSpecComposite != null)
		{
			clientFinishedComposite.setVisible(false);
			clientFinishedComposite = null;
			clientChangeCipherSpecComposite.setVisible(false);
			clientChangeCipherSpecComposite = null;
		}
		if(serverChangeCipherSpecComposite != null)
		{
			serverFinishedComposite.setVisible(false);
			serverFinishedComposite = null;
			serverChangeCipherSpecComposite.setVisible(false);
			serverChangeCipherSpecComposite = null;
		}
		if(clientCertificateComposite != null)
		{
			clientCertificateComposite.setVisible(false);
			clientCertificateComposite = null;
		}
		if(serverCertificateComposite != null)
		{
			serverCertificateComposite.setVisible(false);
			serverCertificateComposite = null;
		}	
		if(serverHelloComposite != null)
		{
			serverHelloComposite.setVisible(false);
			serverHelloComposite = null;
		}		
		clientHelloComposite.setVisible(false);
		clientHelloComposite = null;
		clientHelloComposite = new ClientHelloComposite(content, SWT.NONE, this);
		clientHelloComposite.setBounds(10, 50, 349, 177);
		clientHelloComposite.enableControls();
		arrow.resetArrows();
	}
	
	/**
	 * Removes the active Composite and goes back to the previous one.
	 * All selected options in the active Composite are set back.
	 */
	public void previousStep()
	{
		if(clientChangeCipherSpecComposite != null)
		{
			clientFinishedComposite.setVisible(false);
			clientFinishedComposite = null;
			clientChangeCipherSpecComposite.setVisible(false);
			clientChangeCipherSpecComposite = null;
			serverChangeCipherSpecComposite.enableControls();
			serverFinishedComposite.enableControls();
			arrow.removeLastArrow();
			arrow.removeLastArrow();
			arrow.removeLastArrow();
			arrow.removeLastArrow();
		}
		else if(serverChangeCipherSpecComposite != null)
		{
			serverFinishedComposite.setVisible(false);
			serverFinishedComposite = null;
			serverChangeCipherSpecComposite.setVisible(false);
			serverChangeCipherSpecComposite = null;
			clientCertificateComposite.enableControls();
			arrow.removeLastArrow();
		}
		else if(clientCertificateComposite != null)
		{
			clientCertificateComposite.setVisible(false);
			clientCertificateComposite = null;
			serverCertificateComposite.enableControls();
			arrow.removeLastArrow();
		}
		else if(serverCertificateComposite != null)
		{
			serverCertificateComposite.setVisible(false);
			serverCertificateComposite = null;
			serverHelloComposite.enableControls();
			serverHelloComposite.refreshInformations();
		}
		else if(serverHelloComposite != null)
		{
			serverHelloComposite.setVisible(false);
			serverHelloComposite = null;
			clientHelloComposite.enableControls();
			clientHelloComposite.refreshInformations();
			arrow.removeLastArrow();
		}
	}

	@Override
	public void setFocus() {
		// Set the focus
	}
}
