package org.jcryptool.visual.jctca;

import org.eclipse.swt.SWT;
import org.jcryptool.core.util.fonts.*;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.jctca.tabs.CertificationTab;
import org.jcryptool.visual.jctca.tabs.RegistrationTab;
import org.jcryptool.visual.jctca.tabs.UserTab;

public class JCTCA_Visual extends ViewPart {
/*TODO: erklaerungsboxen individuell aendern, architekturskizzen einbinden, loooooogik.  */
	
	
	// define used colors
	private static final Color WHITE = Display.getDefault().getSystemColor(
			SWT.COLOR_WHITE);

	private ScrolledComposite sc;
	private Composite composite;
	private GridLayout gl;
	private Composite headComposite;
	private StyledText head_description;
	private Composite comp_center;
	private TabFolder tabFolder;
	private Label txt_explain;
	private Group grp_explain;

	public JCTCA_Visual() {
	}

	@Override
	public void createPartControl(Composite parent) {
		try {
			sc = new ScrolledComposite(parent, SWT.BORDER);
			composite = new Composite(sc, SWT.NONE);
			sc.setContent(composite);
			sc.setExpandHorizontal(true);
			sc.setExpandVertical(true);
			
			gl = new GridLayout(1, false);
			gl.verticalSpacing = 20;
			composite.setLayout(gl);

			// Begin - Header
			headComposite = new Composite(composite, SWT.NONE);
			headComposite.setBackground(WHITE);
			headComposite.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,
					false));
			headComposite.setLayout(new GridLayout());

			Label headline = new Label(headComposite, SWT.NONE);
			headline.setFont(FontService.getHeaderFont());
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

			txt_explain.setText("Dies ist Erklärungstext.");

			UserTab user = new UserTab(tabFolder, grp_explain, SWT.NONE);
			RegistrationTab ra = new RegistrationTab(tabFolder, grp_explain, SWT.NONE);
			CertificationTab ca = new CertificationTab(tabFolder, grp_explain, SWT.NONE);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
