package org.jcryptool.visual.jctca;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.jctca.listeners.PluginBtnListener;
import org.jcryptool.visual.jctca.listeners.ResizeListener;
import org.jcryptool.visual.jctca.listeners.TabItemListener;
import org.jcryptool.visual.jctca.tabs.CertificationTab;
import org.jcryptool.visual.jctca.tabs.RegistrationTab;
import org.jcryptool.visual.jctca.tabs.SecondUserTab;
import org.jcryptool.visual.jctca.tabs.UserTab;

/**
 * 
 * This class implements the Certificate Authority visual for the JCrypTool.
 * 
 * @author Marco Macala, Kerstin Reisinger
 * 
 */

public class JCTCA_Visual extends ViewPart {
	/*
	 * TODO: loooogik
	 */

	JCTCA_Visual visual;
	// define used colors
	private static final Color WHITE = Display.getDefault().getSystemColor(
			SWT.COLOR_WHITE);

	private ScrolledComposite root;
	private Composite composite;
	private GridLayout gl;
	private Composite head_composite;
	private StyledText head_description;
	private Composite comp_center;
	private TabFolder tabFolder;
	private Label lbl_explain;
	private Group grp_explain;

	private Composite comp_image;

	private Composite comp_buttons;

	private Button btn_showCreate;

	private Button btn_showRevoke;

	private Button btn_showCheck;

	private Button btn_continue;

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
		// gl.verticalSpacing = 20;
		composite.setLayout(gl);

		// Begin - headline area
		head_composite = new Composite(composite, SWT.NONE);
		head_composite.setBackground(WHITE);
		head_composite.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,
				false));
		head_composite.setLayout(new GridLayout());

		Label headline = new Label(head_composite, SWT.NONE);
		headline.setFont(FontService.getHeaderFont());
		headline.setBackground(WHITE);
		// Set the headline text to the title of the plugin
		headline.setText(Messages.JCTCA_Visual_Plugin_Headline);
		head_description = new StyledText(head_composite, SWT.READ_ONLY
				| SWT.MULTI | SWT.WRAP);
		head_description.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,
				false));
		// set the short introduction text for the certificate creation picture
		// because this is the first text that needs to be shown
		head_description.setText(Messages.JCTCA_Visual_archpic_create_text);
		// End - Header
		showArchitecture();

	}

	/**
	 * 
	 * Displays the architecture pictures of jct-ca
	 * 
	 */

	public void showArchitecture() {
		comp_center = new Composite(composite, SWT.NONE);
		comp_center.setLayout(new GridLayout(1, false));
		comp_center.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));

		comp_buttons = new Composite(comp_center, SWT.NONE);
		comp_buttons.setLayout(new GridLayout(4, false));
		GridData btns_ld = new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1);
		btns_ld.minimumHeight = 30;
		comp_buttons.setLayoutData(btns_ld);

		comp_image = new Composite(comp_center, SWT.FILL);
		comp_image.setLayout(new GridLayout(1, false));
		comp_image.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		// set path_to_create_img and load image at that path into help
		String path_to_create_img = "icons/minica_create.png"; //$NON-NLS-1$
		Image help = Activator.getImageDescriptor(path_to_create_img)
				.createImage();
		lbl_img = new Label(comp_image, SWT.WRAP | SWT.RESIZE);
		lbl_img.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		lbl_img.setImage(help);
		lbl_img.addControlListener(new ResizeListener(lbl_img, comp_image));
		btn_showCreate = new Button(comp_buttons, SWT.PUSH);
		btn_showCreate.setText(Messages.JCTCA_Visual_btn_show_archpic_create);
		btn_showCreate.setData(new Integer(0)); // set data for the listener -
												// see PluginBtnListener.java
		btn_showCreate.addSelectionListener(new PluginBtnListener(visual,
				lbl_img, head_description));

		btn_showRevoke = new Button(comp_buttons, SWT.PUSH);
		btn_showRevoke.setText(Messages.JCTCA_Visual_btn_show_archpic_revoke);
		btn_showRevoke.setData(new Integer(1));
		btn_showRevoke.addSelectionListener(new PluginBtnListener(visual,
				lbl_img, head_description));

		btn_showCheck = new Button(comp_buttons, SWT.PUSH);
		btn_showCheck.setText(Messages.JCTCA_Visual_btn_show_archpic_check);
		btn_showCheck.setData(new Integer(2));
		btn_showCheck.addSelectionListener(new PluginBtnListener(visual,
				lbl_img, head_description));

		btn_continue = new Button(comp_buttons, SWT.PUSH);
		btn_continue.setText(Messages.JCTCA_Visual_btn_continue_to_plugin);
		btn_continue.setData(new Integer(3));
		btn_continue.addSelectionListener(new PluginBtnListener(visual,
				lbl_img, head_description));
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
		GridData gd_explain = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_explain.minimumWidth = 300;
		grp_explain.setLayoutData(gd_explain);
		grp_explain.setText(Messages.JCTCA_Visual_grp_explain_headline);

		// scrolled composite for vertical scrollbar
		ScrolledComposite scrolled_comp = new ScrolledComposite(grp_explain,
				SWT.V_SCROLL);
		scrolled_comp.setLayout(new GridLayout(1, true));
		scrolled_comp.setExpandVertical(true);
		scrolled_comp.setExpandHorizontal(true);
		scrolled_comp.setLayoutData(gd_explain);

		// content of scrolled composite
		Composite comp_vscroll = new Composite(scrolled_comp, SWT.NONE);
		comp_vscroll.setLayout(new GridLayout(1, true));
		comp_vscroll.setLayoutData(gd_explain);
		comp_vscroll.setSize(comp_vscroll
				.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrolled_comp.setContent(comp_vscroll);

		// label for showing explanation texts
		lbl_explain = new Label(comp_vscroll, SWT.WRAP);
		GridData gd_txt_explain = new GridData(SWT.FILL, SWT.NONE, true, false,
				1, 1);
		lbl_explain.setLayoutData(gd_txt_explain);
		lbl_explain
				.setSize(scrolled_comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		TabItemListener tabItemListener = new TabItemListener(tabFolder,
				comp_vscroll);
		tabFolder.addSelectionListener(tabItemListener);

		@SuppressWarnings("unused")
		UserTab user = new UserTab(tabFolder, comp_vscroll, SWT.NONE);
		@SuppressWarnings("unused")
		RegistrationTab ra = new RegistrationTab(tabFolder, comp_vscroll,
				SWT.NONE);
		@SuppressWarnings("unused")
		CertificationTab ca = new CertificationTab(tabFolder, comp_vscroll,
				SWT.NONE);
		@SuppressWarnings("unused")
		SecondUserTab scndUser = new SecondUserTab(tabFolder, comp_vscroll,
				SWT.NONE);

		tabFolder.setSelection(0);
		composite.layout(true);
	}

	public void disposeCompCenter() {
		this.comp_center.dispose();
	}

	@Override
	public void setFocus() {

	}

}
