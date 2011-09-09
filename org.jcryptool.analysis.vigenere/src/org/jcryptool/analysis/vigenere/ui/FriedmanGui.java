/* *****************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * ****************************************************************************/
package org.jcryptool.analysis.vigenere.ui;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorReference;
import org.jcryptool.analysis.vigenere.exceptions.IllegalActionException;
import org.jcryptool.analysis.vigenere.exceptions.IllegalInputException;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.core.util.ui.SingleVanishTooltipLauncher;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class FriedmanGui extends Content {
    private FriedmanContainer container;
    final private String chiffre;
    final private String edtitle;
    private Text tfile;

    private Composite cselection;
    private Composite cvariable;
    private Composite cgraph;

    private Label lstwo;
    private Label lsthree;
    private Label lsone;
    private Label lsep;
    private Label lsepend;
    private Label llength;

    private Text thelp;
    private Text tlength;

    private Button bback;
    private Button bnext;

    public FriedmanGui(final ContentDelegator parent, final String chiffre,
            final String title) {
        super(parent, SWT.NONE);
        this.chiffre = chiffre;
        this.edtitle = title;
        initGUI();
    }

    public FriedmanGui(VigenereBreakerGui parent, String chiffre,
			IEditorReference selection) {
    	super(parent, SWT.NONE);
        this.chiffre = chiffre;
        this.edtitle = selection.getTitle();
        initGUI();
	}

	private void initGUI() {
        try {
            FormLayout thisLayout = new FormLayout();
            this.setSize(780, 620);
            this.setLayout(thisLayout);
            {
                cselection = new Composite(this, SWT.NONE);
                FormLayout cselectionLayout = new FormLayout();
                FormData cselectionLData = new FormData();
                cselectionLData.left = new FormAttachment(0, 1000, 10);
                cselectionLData.top = new FormAttachment(0, 1000, 0);
                cselectionLData.width = 760;
                cselectionLData.height = 30;
                cselection.setLayoutData(cselectionLData);
                cselection.setLayout(cselectionLayout);
                {
                    lsep = new Label(cselection, SWT.SEPARATOR | SWT.HORIZONTAL);
                    FormData lsepLData = new FormData();
                    lsepLData.left = new FormAttachment(0, 1000, 0);
                    lsepLData.top = new FormAttachment(0, 1000, 26);
                    lsepLData.width = 760;
                    lsepLData.height = 2;
                    lsep.setLayoutData(lsepLData);
                }
                {
                    lsthree = new Label(cselection, SWT.BORDER | SWT.CENTER);
                    FormData lsthreeLData = new FormData();
                    lsthreeLData.left = new FormAttachment(0, 1000, 480);
                    lsthreeLData.top = new FormAttachment(0, 1000, 0);
                    lsthreeLData.width = 238;
                    lsthreeLData.height = 24;
                    lsthree.setLayoutData(lsthreeLData);
                    lsthree.setText(Messages.VigenereGlobal_navi_decryption);
                    lsthree.setFont(FontService.getLargeFont());
                    lsthree.setEnabled(false);
                }
                {
                    lstwo = new Label(cselection, SWT.BORDER | SWT.CENTER);
                    FormData lstwoLData = new FormData();
                    lstwoLData.left = new FormAttachment(0, 1000, 240);
                    lstwoLData.top = new FormAttachment(0, 1000, 0);
                    lstwoLData.width = 238;
                    lstwoLData.height = 24;
                    lstwo.setLayoutData(lstwoLData);
                    lstwo.setText(Messages.VigenereGlobal_navi_frequency);
                    lstwo.setFont(FontService.getLargeFont());
                    lstwo.setEnabled(false);
                }
                {
                    lsone = new Label(cselection, SWT.BORDER | SWT.CENTER);
                    FormData lsoneLData = new FormData();
                    lsoneLData.left = new FormAttachment(0, 1000, 0);
                    lsoneLData.top = new FormAttachment(0, 1000, 0);
                    lsoneLData.width = 238;
                    lsoneLData.height = 24;
                    lsone.setLayoutData(lsoneLData);
                    lsone.setText(Messages.VigenereGlobal_navi_friedman);
                    lsone.setFont(FontService.getLargeFont());
                }
            }
            {
                cvariable = new Composite(this, SWT.NONE);
                FormLayout cvariableLayout = new FormLayout();
                FormData cvariableLData = new FormData();
                cvariableLData.left = new FormAttachment(0, 1000, 10);
                cvariableLData.top = new FormAttachment(0, 1000, 30);
                cvariableLData.width = 760;
                cvariableLData.height = 560;
                cvariable.setLayoutData(cvariableLData);
                cvariable.setLayout(cvariableLayout);
                {
                    tfile = new Text(cvariable, SWT.NONE);
                    FormData tfileLData = new FormData();
                    tfileLData.left = new FormAttachment(0, 1000, 0);
                    tfileLData.top = new FormAttachment(0, 1000, 5);
                    tfileLData.width = 754;
                    tfileLData.height = 15;
                    tfile.setLayoutData(tfileLData);
                    tfile.setFont(FontService.getNormalFont());
                    tfile.setText(Messages.FriedmanGui_text_graph + edtitle);
                    tfile.setEditable(false);
                    tfile.setEnabled(false);
                }
                {
                    bback = new Button(cvariable, SWT.PUSH | SWT.CENTER);
                    FormData bbackLData = new FormData();
                    bbackLData.left = new FormAttachment(0, 1000, 0);
                    bbackLData.top = new FormAttachment(0, 1000, 402);
                    bbackLData.width = 90;
                    bbackLData.height = 25;
                    bback.setLayoutData(bbackLData);
                    bback.setText(Messages.FriedmanGui_button_back);
                    bback.setFont(FontService.getNormalFont());
                    bback.setToolTipText(Messages.FriedmanGui_ttip_back);
                    bback.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent event) {
                            back();
                        }
                    });
                }
                {
                    lsepend = new Label(cvariable, SWT.SEPARATOR
                            | SWT.HORIZONTAL);
                    FormData label1LData = new FormData();
                    label1LData.left = new FormAttachment(0, 1000, 0);
                    label1LData.top = new FormAttachment(0, 1000, 387);
                    label1LData.width = 760;
                    label1LData.height = 2;
                    lsepend.setLayoutData(label1LData);
                }
                {
                    thelp = new Text(cvariable, SWT.MULTI | SWT.WRAP);
                    FormData tadecrLData = new FormData();
                    tadecrLData.left =  new FormAttachment(0, 1000, 0);
                    tadecrLData.top =  new FormAttachment(0, 1000, 275);
                    tadecrLData.width = 394;
                    tadecrLData.height = 110;
                    thelp.setLayoutData(tadecrLData);
                    thelp.setText(Messages.FriedmanGui_text_help);
                    thelp.setFont(FontService.getSmallFont());
                    thelp.setBackground(new Color(getDisplay(), 240, 240, 240));
                    thelp.setEditable(false);
                    thelp.setEnabled(false);
                }
                {
                    cgraph = new Composite(cvariable, SWT.NONE);
                    GridLayout cagraphLayout = new GridLayout();
                    cagraphLayout.makeColumnsEqualWidth = true;
                    FormData cagraphLData = new FormData();
                    cagraphLData.left = new FormAttachment(0, 1000, 0);
                    cagraphLData.top = new FormAttachment(0, 1000, 20);
                    cagraphLData.width = 760;
                    cagraphLData.height = 240;
                    cgraph.setLayoutData(cagraphLData);
                    cgraph.setLayout(cagraphLayout);

                    container = new FriedmanContainer(cgraph, this.chiffre);
                    container.initGraph();
                }
                {
                    bnext = new Button(cvariable, SWT.PUSH | SWT.CENTER);
                    FormData banextLData = new FormData();
                    banextLData.left = new FormAttachment(0, 1000, 670);
                    banextLData.top = new FormAttachment(0, 1000, 402);
                    banextLData.width = 90;
                    banextLData.height = 25;
                    bnext.setLayoutData(banextLData);
                    bnext.setText(Messages.FriedmanGui_button_next);
                    bnext.setFont(FontService.getSmallFont());
                    bnext.setToolTipText(Messages.FriedmanGui_ttip_next);
                    bnext.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent event) {
                            start();
                        }
                    });
                }
                {
                    llength = new Label(cvariable, SWT.NONE);
                    FormData lalengthLData = new FormData();
                    lalengthLData.left =  new FormAttachment(0, 1000, 440);
                    lalengthLData.top =  new FormAttachment(0, 1000, 284);
                    lalengthLData.width = 160;
                    lalengthLData.height = 15;
                    llength.setLayoutData(lalengthLData);
                    llength.setText("Länge des Passwortes:");
                    llength.setFont(FontService.getSmallFont());
                    llength.setAlignment(SWT.RIGHT);
                }
                {
                    FormData talengthLData = new FormData();
                    talengthLData.left =  new FormAttachment(0, 1000, 610);
                    talengthLData.top =  new FormAttachment(0, 1000, 280);
                    talengthLData.width = 38;
                    talengthLData.height = 19;
                    tlength = new Text(cvariable, SWT.BORDER | SWT.RIGHT);
                    tlength.setLayoutData(talengthLData);
                    tlength.setTextLimit(3);
                    tlength.setFocus();
                    tlength.setFont(FontService.getSmallFont());
                    tlength.addKeyListener(new KeyAdapter() {
                        public void keyPressed(KeyEvent event) {
                            if (13 == event.keyCode) {
                                start();
                            }
                        }
                    });
                    autodetect();
                }
            }

            this.layout();
            final SingleVanishTooltipLauncher launcher = new SingleVanishTooltipLauncher(this.getShell());
            launcher.showNewTooltip(tlength.toDisplay(new Point(tlength.getBounds().width-1, 1)), 20000, "Analysehilfe", "Dies ist die vom JCT ermittelte Vermutung über die Schlüssellänge, basierend auf obigem Diagramm.\n\n[Klicken zum Schließen]");
            this.addDisposeListener(new DisposeListener() {
    			public void widgetDisposed(DisposeEvent e) {
    				launcher.dispose();
    			}
    		});
        } catch (Exception ex) {
            LogUtil.logError(ex);
        }
    }

    private void start() {
        int step = 0;
    	try {
    		if(tlength.getText().length()==0) throw new NumberFormatException("Bitte geben Sie die Länge des Passworts ein!");
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
            box.setMessage(step==1?message:nfEx.getLocalizedMessage());
            box.open();
            tlength.setText(""); //$NON-NLS-1$
            tlength.setFocus();
        } catch (IllegalInputException iiEx) {
            MessageBox box = new MessageBox(getShell(), SWT.ICON_INFORMATION);
            String message = Messages.FriedmanGui_mbox_tally;
            box.setText(Messages.VigenereGlobal_mbox_info);
            box.setMessage(step==1?message:iiEx.getLocalizedMessage());
            box.open();
            tlength.setText(""); //$NON-NLS-1$
            tlength.setFocus();
        } catch (IllegalActionException iaEx) {
            MessageBox box = new MessageBox(getShell(), SWT.ICON_INFORMATION);
            String message = Messages.FriedmanGui_mbox_length;
            box.setText(Messages.VigenereGlobal_mbox_info);
            box.setMessage(step==1?message:iaEx.getLocalizedMessage());
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

    private void check(final int length, final String chiff)
            throws IllegalActionException {
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
