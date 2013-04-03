package org.jcryptool.visual.jctca;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.crypto.keystore.ui.views.nodes.ContactManager;


public class JCTCA_Visual extends ViewPart {

    private final String ALICE = "Alice";
    private final String BOB = "Bob";
    private final String BLANK = "Blank?";

    private ScrolledComposite sc;
    private Composite composite;
    private GridLayout gl;
    private Composite headComposite;
    private StyledText head_description;
    private Group grp_id_mgmt;
    private Button btn_newID;
    private Button btn_manageID;
    private Button btn_delID;
    private Composite comp_center;
    private TabFolder tabFolder;
    private Label txtExplain;

    public JCTCA_Visual() {
    }

    @Override
    public void createPartControl(Composite parent) {

        // make the composite scrollable
        sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        composite = new Composite(sc, SWT.NONE);
        sc.setContent(composite);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        sc.setMinSize(composite.computeSize(1000, 680));

        gl = new GridLayout(1, false);
        gl.verticalSpacing = 20;
        composite.setLayout(gl);

        // Begin - Header
        headComposite = new Composite(composite, SWT.NONE);
      //  headComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        headComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        headComposite.setLayout(new GridLayout());

        Label label = new Label(headComposite, SWT.NONE);
 //       label.setFont(FontService.getHeaderFont());
   //     label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label.setText("Meine Mama");
        head_description = new StyledText(headComposite, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
        head_description.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        head_description.setText("hat drei busen");
        // End - Header

        grp_id_mgmt = new Group(composite, SWT.NONE);
        grp_id_mgmt.setText("Text 1");
        grp_id_mgmt.setLayout(new GridLayout(3, true));

        btn_newID = new Button(grp_id_mgmt, SWT.PUSH);
        btn_manageID = new Button(grp_id_mgmt, SWT.PUSH);
        btn_delID = new Button(grp_id_mgmt, SWT.PUSH);

        btn_newID.setText("Text 2");
        btn_newID.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

        btn_manageID.setText("Text 3");
        btn_manageID.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        
        btn_delID.setText("Text 4");
        btn_delID.setEnabled(ContactManager.getInstance().getContactSize() > 2);

        btn_newID.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

        comp_center = new Composite(composite, SWT.NONE);
        // 2 columns (tabs and explanation) --> new GridLayout(2, false);
        comp_center.setLayout(new GridLayout(2, false));
        comp_center.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        tabFolder = new TabFolder(comp_center, SWT.NONE);
        tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Group grp_explain = new Group(comp_center, SWT.NONE);
        grp_explain.setLayout(new GridLayout(1, true));
        GridData gd_explain = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
        gd_explain.widthHint = 300;

        grp_explain.setText("Text 1337");

        txtExplain = new Label(grp_explain, SWT.WRAP);
        GridData gd_txtEplain = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gd_txtEplain.heightHint = 300;
        txtExplain.setLayoutData(gd_txtEplain);

        grp_explain.setLayoutData(gd_explain);
        
        TabItem t = new TabItem(tabFolder, SWT.None);

    }

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
