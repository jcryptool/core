package org.jcryptool.visual.sphincsplus.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.sphincsplus.SphincsPlusPlugin;

/**
 * 
 * @author Sebastian Ranftl
 *
 */
public class SphincsPlusView extends ViewPart {
    public SphincsPlusView() {
    }

    private Composite parent;
    private TabFolder tf;
    private TabItem ti_parameterView;
    private TabItem ti_signAndVerifyView;
    private TabItem ti_treeView;
    private TabItem ti_forsView;
    private ScrolledComposite sc_parameterView;
    private ScrolledComposite sc_signAndVerifyView;
    private ScrolledComposite sc_treeView;
    private ScrolledComposite sc_forsView;
    private SphincsPlusParameterView parameterView;
    public SphincsPlusSignAndVerifyView signAndVerifyView;
    private SphincsPlusTreeView treeView;
    private SphincsPlusForsView forsView;

    @Override
    public void createPartControl(final Composite parent) {
        this.parent = parent;

        tf = new TabFolder(parent, SWT.TOP);

        ti_parameterView = new TabItem(tf, SWT.NONE);
        ti_parameterView.setText(Messages.SphincsPlusTab_1);

        sc_parameterView = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
        sc_parameterView.setExpandHorizontal(true);
        sc_parameterView.setExpandVertical(true);

        parameterView = new SphincsPlusParameterView(sc_parameterView, SWT.NONE, this);
        sc_parameterView.setContent(parameterView);
        sc_parameterView.setMinSize(parameterView.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        ti_parameterView.setControl(sc_parameterView);

        ti_signAndVerifyView = new TabItem(tf, SWT.NONE);
        ti_signAndVerifyView.setText(Messages.SphincsPlusTab_2);

        sc_signAndVerifyView = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
        sc_signAndVerifyView.setExpandHorizontal(true);
        sc_signAndVerifyView.setExpandVertical(true);

        signAndVerifyView = new SphincsPlusSignAndVerifyView(sc_signAndVerifyView, SWT.NONE, this);
        sc_signAndVerifyView.setContent(signAndVerifyView);
        sc_signAndVerifyView.setMinSize(signAndVerifyView.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        ti_signAndVerifyView.setControl(sc_signAndVerifyView);

        ti_treeView = new TabItem(tf, SWT.NONE);
        ti_treeView.setText(Messages.SphincsPlusTab_3);

        sc_treeView = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
        sc_treeView.setExpandHorizontal(true);
        sc_treeView.setExpandVertical(true);

        treeView = new SphincsPlusTreeView(sc_treeView, SWT.NONE, this);
        sc_treeView.setContent(treeView);
        sc_treeView.setMinSize(treeView.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        ti_treeView.setControl(sc_treeView);

        ti_forsView = new TabItem(tf, SWT.NONE);
        ti_forsView.setText(Messages.SphincsPlusTab_4);

        sc_forsView = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
        sc_forsView.setExpandHorizontal(true);
        sc_forsView.setExpandVertical(true);

        forsView = new SphincsPlusForsView(sc_forsView, SWT.NONE, this);
        sc_forsView.setContent(forsView);
        sc_forsView.setMinSize(forsView.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        ti_forsView.setControl(sc_forsView);

        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, SphincsPlusPlugin.PLUGIN_ID + ".view"); //$NON-NLS-1$
    }

    public void restartSphincsPlusView() {

        SphincsPlusParameterView.key_generate_pressed = false;
        SphincsPlusSignAndVerifyView.message_signed = false;
        SphincsPlusParameterView.keys_check_box_selected = false;

        tf.setSelection(ti_parameterView);

        parameterView.dispose();
        parameterView = new SphincsPlusParameterView(sc_parameterView, SWT.NONE, this);
        sc_parameterView.setContent(parameterView);
        sc_parameterView.setMinSize(parameterView.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        signAndVerifyView.dispose();
        signAndVerifyView = new SphincsPlusSignAndVerifyView(sc_signAndVerifyView, SWT.NONE, this);
        sc_signAndVerifyView.setContent(signAndVerifyView);
        sc_signAndVerifyView.setMinSize(signAndVerifyView.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        treeView.dispose();
        treeView = new SphincsPlusTreeView(sc_treeView, SWT.NONE, this);
        sc_treeView.setContent(treeView);
        sc_treeView.setMinSize(treeView.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        forsView.dispose();
        forsView = new SphincsPlusForsView(sc_forsView, SWT.NONE, this);
        sc_forsView.setContent(forsView);
        sc_forsView.setMinSize(forsView.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }

    public SphincsPlusForsView getSphincsPlusForsView() {
        return forsView;
    }

    public void setFocusOnSphincsPlusForsView(boolean setFocus) {
        if (setFocus) {
            tf.setSelection(ti_forsView);
        } else {
            tf.setSelection(ti_treeView);
        }
    }

    @Override
    public void setFocus() {
        parent.setFocus();

    }

}