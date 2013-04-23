package org.jcryptool.visual.jctca;

import org.eclipse.swt.SWT;
import org.jcryptool.core.util.fonts.*;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.jctca.listeners.PluginBtnListener;
import org.jcryptool.visual.jctca.listeners.TabItemListener;
import org.jcryptool.visual.jctca.tabs.CertificationTab;
import org.jcryptool.visual.jctca.tabs.RegistrationTab;
import org.jcryptool.visual.jctca.tabs.UserTab;

public class JCTCA_Visual extends ViewPart {
/*TODO: erklaerungsboxen individuell aendern, architekturskizzen einbinden, loooooogik.  */
	
	JCTCA_Visual visual;
	// define used colors
	private static final Color WHITE = Display.getDefault().getSystemColor(
			SWT.COLOR_WHITE);

	private ScrolledComposite root;
	private Composite composite;
	private GridLayout gl;
	private Composite headComposite;
	private StyledText head_description;
	private Composite comp_center;
	private TabFolder tabFolder;
	private Label txt_explain;
	private Group grp_explain;

	private Composite comp_image;

	private Composite comp_btns;

	private Button btn_showCreate;

	private Button btn_showRevoke;

	private Button btn_showSign;

	private Button btn_proceed;

	private Label lbl_img;
	public JCTCA_Visual() {
		this.visual = this;
	}

	@Override
	public void createPartControl(Composite parent) {
		
		root = new ScrolledComposite(parent, SWT.BORDER);
		composite = new Composite(root, SWT.NONE);
		root.setContent(composite);
		root.setExpandHorizontal(true);
		root.setExpandVertical(true);
		
		gl = new GridLayout(1, false);
		gl.verticalSpacing = 20;
		composite.setLayout(gl);

		// Begin - Header
		headComposite = new Composite(composite, SWT.NONE);
		// headComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		headComposite.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,
				false));
		headComposite.setLayout(new GridLayout());

		Label headline = new Label(headComposite, SWT.NONE);
		headline.setFont(FontService.getHeaderFont());
		headline.setBackground(WHITE);
		headline.setText("JCrypTool Certificate Authority (JCT-CA)");
		head_description = new StyledText(headComposite, SWT.READ_ONLY
				| SWT.MULTI | SWT.WRAP);
		head_description.setLayoutData(new GridData(SWT.FILL, SWT.NONE,
				true, false));
		head_description
				.setText("Asymmetrische Kryptosysteme sind eine verbreitete Methode, um im Internet dafür zu sorgen,"
						+ " dass Daten am Übertragungsweg nicht verändert wurden und von einem bestimmten Sender stammen. Eine Certificate Authority"
						+ " stellt dabei mit ihrer eigenen Signatur die eindeutige Zuordnung zwischen einem öffentlichen Schlüssel und einer Person her.");
		// End - Header
		showArchitecture();

	}

	public void showArchitecture(){
		comp_center = new Composite(composite, SWT.NONE);
		comp_center.setLayout(new GridLayout(1, false));
		comp_center.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,1));
		
		comp_image = new Composite(comp_center, SWT.FILL);
		comp_image.setLayout(new GridLayout(1,false));
		comp_image.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,1));
		
		String create_img = "icons/minica_create_cert.jpg";
		Image help = Activator.getImageDescriptor(create_img).createImage();
		//comp_image.setBackgroundImage(help);
		lbl_img = new Label(comp_image, SWT.NONE);
		lbl_img.setImage(help);
		
		comp_btns = new Composite(comp_center, SWT.NONE);
		comp_btns.setLayout(new GridLayout(4,false));
		comp_btns.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,1,1));
		
		btn_showCreate = new Button(comp_btns, SWT.PUSH);
		btn_showCreate.setText("Zertifikat erzeugen");
		btn_showCreate.addSelectionListener(new PluginBtnListener(visual, lbl_img));
		
		btn_showRevoke = new Button(comp_btns, SWT.PUSH);
		btn_showRevoke.setText("Zertifikat widerrufen");
		btn_showRevoke.addSelectionListener(new PluginBtnListener(visual, lbl_img));
		
		
		btn_showSign = new Button(comp_btns, SWT.PUSH);
		btn_showSign.setText("Signatur überprüfen");
		btn_showSign.addSelectionListener(new PluginBtnListener(visual, lbl_img));
		
		btn_proceed = new Button(comp_btns, SWT.PUSH);
		btn_proceed.setText("Weiter zum Plugin");
		btn_proceed.addSelectionListener(new PluginBtnListener(visual, lbl_img));
		composite.layout(true);
	}
	public void showCenter(){
		comp_center = new Composite(composite, SWT.NONE);
		// 2 columns (tabs and explanation) --> new GridLayout(2, false);
		comp_center.setLayout(new GridLayout(2, false));
		comp_center.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		tabFolder = new TabFolder(comp_center, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		grp_explain = new Group(comp_center, SWT.NONE);
		grp_explain.setLayout(new GridLayout(1, true));
		GridData gd_explain = new GridData(SWT.FILL, SWT.FILL, false, true,
				1, 1);
		gd_explain.widthHint = 300;
		grp_explain.setLayoutData(gd_explain);
		grp_explain.setText("Erklärung");
		
		txt_explain = new Label(grp_explain, SWT.WRAP);
		GridData gd_txt_explain = new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1);
		gd_txt_explain.heightHint = 300;
		txt_explain.setLayoutData(gd_txt_explain);

		txt_explain.setText("In dieser Ansicht verkörpern Sie den Benutzer, der sich ein neues Zertifikat ausstellen lassen möchte.");
		
		
		//TODO: besser machen
		TabItemListener tabItemListener = new TabItemListener(tabFolder, grp_explain);
		tabFolder.addSelectionListener(tabItemListener);
		
		UserTab user = new UserTab(tabFolder, grp_explain, SWT.NONE);
		RegistrationTab ra = new RegistrationTab(tabFolder, grp_explain, SWT.NONE);
		CertificationTab ca = new CertificationTab(tabFolder, grp_explain, SWT.NONE);
		composite.layout(true);
	}
	
	public void disposeCompCenter(){
		this.comp_center.dispose();
	}
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
