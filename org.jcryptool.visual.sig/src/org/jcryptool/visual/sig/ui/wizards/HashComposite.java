package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;

/**
 * @author Grebe Contains the elements (2 group boxes) of the HashWizard
 * 
 */
public class HashComposite extends Composite implements SelectionListener {
    private Group grpHashes;
    private Text txtDescription;
    private Button rdo1;
    private Button rdo2;
    private Button rdo3;
    private Button rdo4;
    private Button rdo5;
    private Menu menuHash;
    private MenuItem mntmHash;

    // Constructor
    public HashComposite(Composite parent, int style) {
        super(parent, style);
        // Draw the controls
        initialize();
    }

    /**
     * Draws all the controls of the composite
     */
    private void initialize() {
        GridData gd_grpSignatures = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_grpSignatures.widthHint = 141;

        Group grpDescription = new Group(this, SWT.NONE);
        GridData gd_grpDescription = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_grpDescription.widthHint = 129;
        grpDescription.setLayoutData(gd_grpDescription);
        grpDescription.setText(Messages.HashWizard_grpDescription);
        grpDescription.setBounds(10, 187, 300, 246);

        txtDescription = new Text(grpDescription, SWT.WRAP | SWT.TRANSPARENT);
        txtDescription.setEditable(false);
        // txtDescription.setBackground(new Color(Display.getCurrent(), 220, 220, 220));
        txtDescription.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
        txtDescription.setBounds(10, 18, 275, 201);
        txtDescription.setText(Messages.HashWizard_rdomd5_description);

        menuHash = new Menu(txtDescription);
        txtDescription.setMenu(menuHash);

        mntmHash = new MenuItem(menuHash, SWT.NONE);
        mntmHash.setText(Messages.Wizard_menu);
        // To select all text
        mntmHash.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                txtDescription.selectAll();
            }// end widgetSelected
        });

        setSize(new Point(321, 443));

        grpHashes = new Group(this, SWT.NONE);
        grpHashes.setLayoutData(gd_grpSignatures);
        grpHashes.setText(Messages.HashWizard_grpHashes);
        grpHashes.setBounds(10, 10, 300, 171);

        rdo1 = new Button(grpHashes, SWT.RADIO);
        // rdo1.setSelection(true);
        rdo1.setBounds(10, 19, 91, 18);
        rdo1.setText("MD5 (128)");

        rdo2 = new Button(grpHashes, SWT.RADIO);
        rdo2.setBounds(10, 43, 91, 18);
        rdo2.setText("SHA-1 (160)");

        rdo3 = new Button(grpHashes, SWT.RADIO);
        rdo3.setBounds(10, 67, 91, 18);
        rdo3.setText(Messages.HashWizard_rdosha256);

        rdo4 = new Button(grpHashes, SWT.RADIO);
        rdo4.setBounds(10, 91, 91, 18);
        rdo4.setText(Messages.HashWizard_rdosha384);

        rdo5 = new Button(grpHashes, SWT.RADIO);
        rdo5.setBounds(10, 115, 91, 18);
        rdo5.setText(Messages.HashWizard_rdosha512);

        // Add event listeners
        rdo1.addSelectionListener(this);
        rdo2.addSelectionListener(this);
        rdo3.addSelectionListener(this);
        rdo4.addSelectionListener(this);
        rdo5.addSelectionListener(this);

        // If called by JCT-CA only SHA-256 can be used!
        // if (org.jcryptool.visual.sig.algorithm.Input.privateKey != null) {
        // rdo1.setEnabled(false);
        // rdo2.setEnabled(false);
        // rdo4.setEnabled(false);
        // rdo5.setEnabled(false);
        // rdo3.setSelection(true);
        // }

        rdo1.setSelection(false);
        rdo2.setSelection(false);
        rdo3.setSelection(false);
        rdo4.setSelection(false);
        rdo5.setSelection(false);

        // Load the previous selection
        switch (org.jcryptool.visual.sig.algorithm.Input.h) {
            case 0:
                rdo1.setSelection(true);
                break;
            case 1:
                rdo2.setSelection(true);
                break;
            case 2:
                rdo3.setSelection(true);
                break;
            case 3:
                rdo4.setSelection(true);
                break;
            case 4:
                rdo5.setSelection(true);
                break;
            default:
                rdo1.setSelection(true);
                break;
        }
        // Fire an event to show the correct text. It doesn't matter which radio button triggers the event
        // because it is checked in the event handler
        rdo1.notifyListeners(SWT.Selection, new Event());
    }

    /**
     * @return the grpHashes
     */
    public Group getGrpHashes() {
        return grpHashes;
    }

    @Override
    public void widgetSelected(SelectionEvent e) {
        if (rdo1.getSelection()) {
            txtDescription.setText(Messages.HashWizard_rdomd5_description);
            org.jcryptool.visual.sig.algorithm.Input.h = 0;
        }

        else if (rdo2.getSelection()) {
            txtDescription.setText(Messages.HashWizard_rdosha1_description);
            org.jcryptool.visual.sig.algorithm.Input.h = 1;
        }

        else if (rdo3.getSelection()) {
            txtDescription.setText(Messages.HashWizard_rdosha256_description);
            org.jcryptool.visual.sig.algorithm.Input.h = 2;
        }

        else if (rdo4.getSelection()) {
            txtDescription.setText(Messages.HashWizard_rdosha384_description);
            org.jcryptool.visual.sig.algorithm.Input.h = 3;
        }

        else if (rdo5.getSelection()) {
            txtDescription.setText(Messages.HashWizard_rdosha512_description);
            org.jcryptool.visual.sig.algorithm.Input.h = 4;
        }

    }// end widgetSelected

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
        // TODO Auto-generated method stub

    }
}
