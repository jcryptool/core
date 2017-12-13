/* *****************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * ***************************************************************************
 */
package org.jcryptool.analysis.vigenere.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.ui.IEditorReference;
import org.jcryptool.analysis.vigenere.exceptions.IllegalActionException;
import org.jcryptool.analysis.vigenere.exceptions.IllegalInputException;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.core.util.fonts.FontService;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation, company or business for any purpose whatever) then
 * you should purchase a license for each developer using Jigloo. Please visit www.cloudgarden.com for details. Use of
 * Jigloo implies acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO
 * JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class FriedmanGui extends Content {
    private FriedmanContainer container;
    final private String chiffre;
    final private String edtitle;
    private Text tfile;

    private Composite cselection;
    private Composite cgraph;
	private Composite mainComposite;
	private Composite passwordComposite;
	private Composite buttonComposite;

    private Label lstwo;
    private Label lsthree;
    private Label lsone;
    private Label lsepend;
    private Label llength;

    private Text thelp;
    private Text tlength;

    private Button bback;
    private Button bnext;

    /**
     * Constructor for FriedmanGui
     * @param parent
     * @param chiffre
     * @param title
     */
    public FriedmanGui(final ContentDelegator parent, final String chiffre, final String title) {
        super(parent, SWT.NONE);
        this.chiffre = chiffre;
        this.edtitle = title;
        initGUI();
    }

    /**
     * Constructor for FriedmanGui
     * @param parent
     * @param chiffre
     * @param selection
     */
    public FriedmanGui(VigenereBreakerGui parent, String chiffre, IEditorReference selection) {
        super(parent, SWT.NONE);
        this.chiffre = chiffre;
        this.edtitle = selection.getTitle();
        initGUI();
    }

    /**
     * initializes the GUI for the friedman test window
     */
    private void initGUI() {
        try {
        	this.setLayout(new GridLayout());
        	this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
            
        	cselection = new Composite(this, SWT.NONE);
			cselection.setLayout(new GridLayout(3, false));
			cselection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

			lsone = new Label(cselection, SWT.BORDER | SWT.CENTER);
			lsone.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			lsone.setText(Messages.VigenereGlobal_navi_friedman);
			lsone.setFont(FontService.getLargeFont());

			lstwo = new Label(cselection, SWT.BORDER | SWT.CENTER);
			lstwo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			lstwo.setText(Messages.VigenereGlobal_navi_frequency);
			lstwo.setFont(FontService.getLargeFont());
			lstwo.setForeground(ColorService.GRAY);

			lsthree = new Label(cselection, SWT.BORDER | SWT.CENTER);
			lsthree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			lsthree.setText(Messages.VigenereGlobal_navi_decryption);
			lsthree.setFont(FontService.getLargeFont());
			lsthree.setForeground(ColorService.GRAY);

			mainComposite = new Composite(this, SWT.NONE);
			mainComposite.setLayout(new GridLayout(2, true));
			mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			
			tfile = new Text(mainComposite, SWT.NONE);
			tfile.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
            tfile.setText(Messages.FriedmanGui_text_graph + " " + edtitle);
            tfile.setEditable(false);
            
            cgraph = new Composite(mainComposite, SWT.NONE);
            cgraph.setLayout(new GridLayout());
            GridData gd_cgraph = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
            gd_cgraph.widthHint = 600;
            gd_cgraph.minimumHeight = 250;
            cgraph.setLayoutData(gd_cgraph);

            container = new FriedmanContainer(cgraph, this.chiffre);
            container.initGraph();

            thelp = new Text(mainComposite, SWT.MULTI | SWT.WRAP);
            GridData gd_thelp = new GridData(SWT.FILL, SWT.FILL, false, false);
            gd_thelp.widthHint = 300;
            thelp.setLayoutData(gd_thelp);
            thelp.setText(Messages.FriedmanGui_text_help);
            thelp.setFont(FontService.getSmallFont());
            thelp.setEditable(false);
            
            passwordComposite = new Composite(mainComposite, SWT.NONE);
            passwordComposite.setLayout(new GridLayout(2, true));
            passwordComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, false));
            
            llength = new Label(passwordComposite, SWT.NONE);
            llength.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
            llength.setText(Messages.FriedmanGui_0);
            
            tlength = new Text(passwordComposite, SWT.BORDER);
            tlength.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
            tlength.setTextLimit(3);
            tlength.setFocus();
            tlength.addKeyListener(new KeyAdapter() {
                @Override
				public void keyPressed(KeyEvent event) {
                    if (13 == event.keyCode) {
                        start();
                    }
                }
            });
            autodetect();
            
            final ToolTip tip = new ToolTip(tlength.getShell(), SWT.BALLOON);
            tip.setMessage(Messages.FriedmanGui_2);
//            tip.setLocation(new Point(tlength.getBounds().x, tlength.getBounds().y));
            tip.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					tip.dispose();
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					
				}
			});
            tip.setVisible(true);
            
            lsepend = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
            lsepend.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

            buttonComposite = new Composite(this, SWT.NONE);
            buttonComposite.setLayout(new GridLayout(2, true));
            buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
            
            bback = new Button(buttonComposite, SWT.PUSH);
            bback.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false));
            bback.setText(Messages.FriedmanGui_button_back);
            bback.setToolTipText(Messages.FriedmanGui_ttip_back);
            bback.addSelectionListener(new SelectionAdapter() {
                @Override
				public void widgetSelected(SelectionEvent event) {
                    back();
                }
            });
            
            bnext = new Button(buttonComposite, SWT.PUSH | SWT.CENTER);
            bnext.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, false));
            bnext.setText(Messages.FriedmanGui_button_next);
            bnext.setToolTipText(Messages.FriedmanGui_ttip_next);
            bnext.addSelectionListener(new SelectionAdapter() {
                @Override
				public void widgetSelected(SelectionEvent event) {
                    start();
                }
            });
            
        } catch (Exception ex) {
            LogUtil.logError(ex);
        }
    }

    private void start() {
        int step = 0;
        try {
            if (tlength.getText().length() == 0)
                throw new NumberFormatException(Messages.FriedmanGui_3);
            step = 1;
            int length = Integer.parseInt(tlength.getText());
            step = 2;
            checkNumber(length);
            check(length, chiffre);
            check(length);
            ContentDelegator del = (ContentDelegator) getParent();
            del.toFrequency(length);
        } catch (NumberFormatException nfEx) {
            MessageBox box = new MessageBox(getShell(), SWT.ICON_ERROR);
            String message = Messages.FriedmanGui_mbox_number;
            box.setText(Messages.VigenereGlobal_mbox_warning);
            box.setMessage(step == 1 ? message : nfEx.getLocalizedMessage());
            box.open();
            tlength.setText(""); //$NON-NLS-1$
            tlength.setFocus();
        } catch (IllegalInputException iiEx) {
            MessageBox box = new MessageBox(getShell(), SWT.ICON_INFORMATION);
            String message = Messages.FriedmanGui_mbox_tally;
            box.setText(Messages.VigenereGlobal_mbox_info);
            box.setMessage(step == 1 ? message : iiEx.getLocalizedMessage());
            box.open();
            tlength.setText(""); //$NON-NLS-1$
            tlength.setFocus();
        } catch (IllegalActionException iaEx) {
            MessageBox box = new MessageBox(getShell(), SWT.ICON_INFORMATION);
            String message = Messages.FriedmanGui_mbox_length;
            box.setText(Messages.VigenereGlobal_mbox_info);
            box.setMessage(step == 1 ? message : iaEx.getLocalizedMessage());
            box.open();
            tlength.setText(""); //$NON-NLS-1$
            tlength.setFocus();
        }
    }

    private void check(final int length) throws IllegalInputException {
        if (PasswordContainer.TALLY < length) {
            throw new IllegalInputException("Only " //$NON-NLS-1$
                    + PasswordContainer.TALLY + " characters allowed!"); //$NON-NLS-1$
        }
    }

    private void check(final int length, final String chiff) throws IllegalActionException {
        if (length > chiff.length()) {
            throw new IllegalActionException("Passphrase to long"); //$NON-NLS-1$
        }
    }

    private void checkNumber(final int length) throws NumberFormatException {
        if (0 >= length) {
            throw new NumberFormatException("Length cannot be 0 or negative."); //$NON-NLS-1$
        }
    }

    private void back() {
        ContentDelegator del = (ContentDelegator) getParent();
        del.backSummary();
    }

    protected void reset(final int length) {
        tlength.setText(String.valueOf(length));
        tlength.selectAll();
        tlength.setFocus();
    }

    private void autodetect() {
        int length = container.findKeyLength(chiffre);
        tlength.setText(String.valueOf(length));
        tlength.setFocus();
        tlength.selectAll();
    }
}
