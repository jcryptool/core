//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.verifiablesecretsharing.views;


import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.part.*;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.ui.*;
import org.eclipse.swt.SWT;
import org.jcryptool.visual.verifiablesecretsharing.VerifiableSecretSharingPlugin;


/**
 *
 * @author Dulghier Christoph, Reisinger Kerstin, Tiefenbacher Stefan, Wagner Thomas
 */

public class VerifiableSecretSharingView extends ViewPart {

	private Composite parent;
	private VerifiableSecretSharingComposite vssc;
	private TabFolder tf;
	private ScrolledComposite scVssc;
	private ReconstructionChartComposite cc;
	private TabItem tiCc;
	private TabItem tiVssc;
	private ScrolledComposite scCc;

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(final Composite parent) {
		this.parent = parent;
		
		tf = new TabFolder(parent, SWT.TOP);


//
//		// Viterbi Tab
//		ti = new TabItem(tf, SWT.NONE);
//		ti.setText(Messages.ViterbiComposite_tab_title);
//		sc = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
//		sc.setExpandHorizontal(true);
//		sc.setExpandVertical(true);
//		viterbiComposite = new ViterbiComposite(sc, SWT.NONE, this);
//		sc.setContent(viterbiComposite);
//		sc.setMinSize(viterbiComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//		ti.setControl(sc);
//		viterbiComposite.displayDefaultTexts();
//		PlatformUI.getWorkbench().getHelpSystem()
//				.setHelp(parent, ViterbiPlugin.PLUGIN_ID + ".view"); //$NON-NLS-1$
		
		tiVssc = new TabItem(tf, SWT.NONE);
		tiVssc.setText(Messages.VerifiableSecretSharingComposite_tab_title);
		scVssc = new ScrolledComposite(tf, SWT.H_SCROLL	| SWT.V_SCROLL);
		scVssc.setExpandHorizontal(true);
		scVssc.setExpandVertical(true);
		vssc = new VerifiableSecretSharingComposite(scVssc, SWT.NONE, this);
		scVssc.setContent(vssc);
		scVssc.setMinSize(vssc.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		tiVssc.setControl(scVssc);
		
		tiCc = new TabItem(tf, SWT.NONE);
		tiCc.setText(Messages.ChartComposite_tab_title);
		scCc = new ScrolledComposite(tf, SWT.H_SCROLL	| SWT.V_SCROLL);
		scCc.setExpandHorizontal(true);
		scCc.setExpandVertical(true);
		cc = new ReconstructionChartComposite(scCc, SWT.NONE, this);
		scCc.setContent(cc);
		scCc.setMinSize(cc.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		tiCc.setControl(scCc);
		
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, VerifiableSecretSharingPlugin.PLUGIN_ID + ".views"); //$NON-NLS-1$
		
		
        hookActionBar();
	}

    private void hookActionBar() {
        IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
        mgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        getViewSite().getActionBars().updateActionBars();
    }
	
	public void restartVerifiableSecretSharing() {
		vssc.dispose();
		vssc = new VerifiableSecretSharingComposite(scVssc, SWT.NONE, this);
		scVssc.setContent(vssc);
		scVssc.setMinSize(vssc.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		cc.dispose();
		cc = new ReconstructionChartComposite(scCc, SWT.NONE, this);
		scCc.setContent(cc);
		scCc.setMinSize(cc.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	public ReconstructionChartComposite getReconstructionChartComposite() {
		return cc;
	}
	
	public void setFocusOnReconstructionTab(boolean setFocus) {
		if(setFocus) {
			tf.setSelection(tiCc);
		}
		else {
			tf.setSelection(tiVssc);
		}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
}