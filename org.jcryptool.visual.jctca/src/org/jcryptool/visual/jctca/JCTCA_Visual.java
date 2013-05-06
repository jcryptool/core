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
import org.jcryptool.visual.jctca.listeners.ResizeListener;
import org.jcryptool.visual.jctca.listeners.TabItemListener;
import org.jcryptool.visual.jctca.tabs.CertificationTab;
import org.jcryptool.visual.jctca.tabs.RegistrationTab;
import org.jcryptool.visual.jctca.tabs.SecondUserTab;
import org.jcryptool.visual.jctca.tabs.UserTab;

public class JCTCA_Visual extends ViewPart {
	/*
	 * TODO: einleitungstext ändern -- bei architekturskizzen wird der aktuelle
	 * angezeigt looooooooogik
	 */

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
//		gl.verticalSpacing = 20;
		composite.setLayout(gl);

		// Begin - Header
		headComposite = new Composite(composite, SWT.NONE);
		headComposite.setBackground(WHITE);
		headComposite.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,
				false));
		headComposite.setLayout(new GridLayout());

		Label headline = new Label(headComposite, SWT.NONE);
		headline.setFont(FontService.getHeaderFont());
		headline.setBackground(WHITE);
		headline.setText(Messages.JCTCA_Visual_headline);
		head_description = new StyledText(headComposite, SWT.READ_ONLY
				| SWT.MULTI | SWT.WRAP);
		head_description.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,
				false));
		head_description.setText(Messages.JCTCA_Visual_intro_txt);
		// End - Header
		showArchitecture();

	}

	// TODO: Einleitungstext anzeigen, nicht den allgemeinen Erklärungstext
	public void showArchitecture() {
		comp_center = new Composite(composite, SWT.NONE);
		comp_center.setLayout(new GridLayout(1, false));
		comp_center.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));

		comp_btns = new Composite(comp_center, SWT.NONE);
		comp_btns.setLayout(new GridLayout(4, false));
		GridData btns_ld = new GridData(SWT.FILL, SWT.NONE, true, false, 1,1);
		btns_ld.minimumHeight=30;
		comp_btns.setLayoutData(btns_ld);
		
		comp_image = new Composite(comp_center, SWT.FILL);
		comp_image.setLayout(new GridLayout(1, false));
		comp_image.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));

		String create_img = Messages.JCTCA_Visual_arch_pic_path;
		Image help = Activator.getImageDescriptor(create_img).createImage();
		// comp_image.setBackgroundImage(help);
		lbl_img = new Label(comp_image, SWT.WRAP | SWT.RESIZE);
		lbl_img.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,1));
		lbl_img.setImage(help);
		lbl_img.addControlListener(new ResizeListener(lbl_img, comp_image));
		btn_showCreate = new Button(comp_btns, SWT.PUSH);
		btn_showCreate.setText(Messages.JCTCA_Visual_create_cert_arch_headline);
		btn_showCreate.addSelectionListener(new PluginBtnListener(visual,
				lbl_img, head_description));

		btn_showRevoke = new Button(comp_btns, SWT.PUSH);
		btn_showRevoke.setText(Messages.JCTCA_Visual_revoke_cert_arch_headline);
		btn_showRevoke.addSelectionListener(new PluginBtnListener(visual,
				lbl_img,head_description));
		

		btn_showSign = new Button(comp_btns, SWT.PUSH);
		btn_showSign.setText(Messages.JCTCA_Visual_check_sig_arch_headline);
		btn_showSign
				.addSelectionListener(new PluginBtnListener(visual, lbl_img, head_description));

		btn_proceed = new Button(comp_btns, SWT.PUSH);
		btn_proceed.setText(Messages.JCTCA_Visual_continue_to_plugin);
		btn_proceed
				.addSelectionListener(new PluginBtnListener(visual, lbl_img,head_description));
		composite.layout(true);
	}

	public void showCenter() {
		comp_center = new Composite(composite, SWT.NONE);
		// 2 columns (tabs and explanation) --> new GridLayout(2, false);
		comp_center.setLayout(new GridLayout(2, false));
		comp_center.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));

		tabFolder = new TabFolder(comp_center, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));

		grp_explain = new Group(comp_center, SWT.NONE);
		grp_explain.setLayout(new GridLayout(1, true));
		GridData gd_explain = new GridData(SWT.FILL, SWT.FILL, false, true, 1,
				1);
		gd_explain.widthHint = 400;
		grp_explain.setLayoutData(gd_explain);
		grp_explain.setText(Messages.JCTCA_Visual_explain_headline);
		grp_explain.setToolTipText(Messages.JCTCA_Visual_explain_tooltip0);

		txt_explain = new Label(grp_explain, SWT.WRAP);
		GridData gd_txt_explain = new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1);
		gd_txt_explain.heightHint = 300;
		txt_explain.setLayoutData(gd_txt_explain);
		txt_explain.setToolTipText(Messages.JCTCA_Visual_explain_tooltip1);

		TabItemListener tabItemListener = new TabItemListener(tabFolder,
				grp_explain);
		tabFolder.addSelectionListener(tabItemListener);

		UserTab user = new UserTab(tabFolder, grp_explain, SWT.NONE);
		RegistrationTab ra = new RegistrationTab(tabFolder, grp_explain,
				SWT.NONE);
		CertificationTab ca = new CertificationTab(tabFolder, grp_explain,
				SWT.NONE);
		SecondUserTab scndUser = new SecondUserTab(tabFolder, grp_explain,
				SWT.NONE);

		tabFolder.setSelection(0);
		composite.layout(true);
	}

	public void disposeCompCenter() {
		this.comp_center.dispose();
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
