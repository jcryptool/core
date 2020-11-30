//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2012, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.arc4.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.jcryptool.core.util.ui.TitleAndDescriptionComposite;
import org.jcryptool.visual.arc4.ARC4Con;
import org.jcryptool.visual.arc4.Messages;
import org.jcryptool.visual.arc4.Type;
import org.jcryptool.visual.arc4.algorithm.ARC4Algorithm;
import org.jcryptool.visual.arc4.algorithm.AlgARC4;
import org.jcryptool.visual.arc4.algorithm.AlgSpritz;

/**
 * This class holds the contents of the plug-in
 * 
 * @author Luca Rupp
 * @author Thorben Groos (switchable keylength)
 */
public class ARC4Composite extends Composite {

    // This is what does the calculation
    private ARC4Algorithm alg;

    // The labels that form the byte vector
    private VectorVisual vector;

    // The byte vectors that represent the plaintext, the key, the ciphertext and the pseudorandom
    // bytes
    private DatavectorVisual plain, key, enc, random;

    // The portion of the UI that allows you to control the algorithm
    private InstructionVisual inst;

    // descr holds the description of the plugin as a whole; xor displays a picture, arrow displays another picture
    private Composite xor, arrow;

    // Part of the UI that shows the internal variables of the algorithm
    private VariablesVisual var;
    
    

    // radioyes is the button to highlight changes
    // radiono is the opposite
    // arc4 is the radio button for the arc4 algorithm
    // spritz is the radio button for the spritz algorithm
    // restart is the button to reset the step counter
    // randomize is the button that lets you generate a new random key and plaintext
    private Button radioyes, radiono, restart, randomize, arc4, spritz;

    // The messagebox that is displayed when the algorithm is finished
    private MessageBox finish;

    // The portion of the UI, that holds misc settings for the plugin
    // algo is the section that allows you to choose one of the variants of ARC4
    private Group miscGroup, algo;
    
    // Let the user choose a value for w, if in spritz mode
    private Combo chooseW;
    
    // the label that labels the w combo that allows the user to choose a value for w
    private Label wlabel;

    
    /**
     * Constructor for the ARC4Composite
     * 
     * @param parent the parent of the composite
     * @param style this is ignored, is is necessary because of the inheritance
     */
    public ARC4Composite(Composite parent, int style) {
        super(parent, SWT.NONE);
        // Initialize the messagebox
        finish = new MessageBox(this.getShell(), SWT.OK);
        finish.setText(Messages.CompositeAlgFinishTitle);
        finish.setMessage(Messages.CompositeAlgFinishText);

        // Create a new Algorithm object with keylength 16, random
        // key and plaintext.
        alg = new AlgARC4();

        // The layout is intended as three to one, but as it is rather difficult to let the xor
        // image hover in the
        // 1.5th column (in the middle of the byte vectors) the layout is six to two
        setLayout(new GridLayout(ARC4Con.H_SPAN_MAIN, true));

        // initialize the description of the plug-in
        initDsc();

        // the s-box of the algorithm
        vector = new VectorVisual(this, SWT.NONE);
        vector.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, ARC4Con.H_SPAN_LEFT, ARC4Con.S_BOX_HEIGTH));
               
        // initialize the variables section
        var = new VariablesVisual(this, SWT.NONE);
        var.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, ARC4Con.H_SPAN_RIGHT, 1));

        // initialize the control section
        inst = new InstructionVisual(this, SWT.NONE, alg);
        inst.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, ARC4Con.H_SPAN_RIGHT, 1));

        // initialize the section that allows you to choose a variant of the ARC4 algorithm
        initAlgoSec();    
        
        // initialize the misc settings section
        initMisc();

        // initialize the key section
        key = new DatavectorVisual(this, SWT.BORDER, Type.KEY, alg);
        key.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, ARC4Con.H_SPAN_MAIN, 1));
        
        // a seperator to make the relation between plaintext, pseudorandom numbers and ciphertext clear
        Label sep1 = new Label(this, SWT.NONE);
        sep1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, ARC4Con.H_SPAN_MAIN, 1));
        
        // initialize the plaintext section
        plain = new DatavectorVisual(this, SWT.BORDER, Type.PLAIN, alg);
        plain.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, ARC4Con.H_SPAN_MAIN, 1));

        // initialize the xor
        // the xor just displays a image
        xor = new Composite(this, SWT.NONE);
        xor.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, ARC4Con.H_SPAN_LEFT, 1));
        xor.setLayout(new GridLayout(1, true));
        Label xorpic = new Label(xor, SWT.CENTER);
        xorpic.setImage(ImageDescriptor.createFromURL(getClass().getResource(ARC4Con.PATH_TO_XOR_IMAGE)).createImage());

        // initialize the vector with the pseudorandom numbers
        random = new DatavectorVisual(this, SWT.BORDER, Type.RAND, alg);
        random.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, ARC4Con.H_SPAN_MAIN, 1));
        
        // initialize the image of the arrow
        arrow = new Composite(this, SWT.NONE);
        arrow.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, ARC4Con.H_SPAN_LEFT, 1));
        arrow.setLayout(new GridLayout(1, true));
        Label arrowpic = new Label(arrow, SWT.CENTER);
        arrowpic.setImage(ImageDescriptor.createFromURL(getClass().getResource(
                ARC4Con.PATH_TO_ARROW_IMAGE)).createImage());

        // initialize the vector with the ciphertext
        enc = new DatavectorVisual(this, SWT.BORDER, Type.ENC, alg);
        enc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, ARC4Con.H_SPAN_MAIN, 1));

        // fetch the data from the algorithm object and fill it into the GUI
        syncronizeInternWithExtern();
    }

    /**
     * Create the part of the plug-in that displays its description
     */
    private void initDsc() {
    	TitleAndDescriptionComposite td = new TitleAndDescriptionComposite(this);
    	td.setLayoutData(new GridData(SWT.FILL, SWT.UP, true, false, ARC4Con.H_SPAN_MAIN, ARC4Con.DESC_HEIGHT));
    	td.setTitle(Messages.PluginDescriptionCaption);
    	td.setDescription(Messages.PluginDescription);
    	// Disable autp resizing of the TaD.
    	td.setResize(false);
    	
    }
    
    private void initAlgoSec() {
        // the composite that holds the buttons that allow you to choose an algorithm
        algo = new Group(this, SWT.NONE);
        algo.setText(Messages.AlgSelectionBoxText);
        algo.setToolTipText(Messages.AlgSelectionBoxTool);
        algo.setLayout(new GridLayout(2, true));
        algo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, ARC4Con.H_SPAN_RIGHT, 1));
        
        arc4 = new Button(algo, SWT.RADIO);
        arc4.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
        arc4.setSelection(true);
        arc4.setText(Messages.CompositeARC4);
        arc4.setToolTipText(Messages.CompositeARC4Tool);
        arc4.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                alg = new AlgARC4(alg.getKey(), alg.getPlain());
                chooseW.setEnabled(false);
            }
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                
            } 
        });
        
        //Only a spacer
        new Label(algo, SWT.NONE);
        
        spritz = new Button(algo, SWT.RADIO);
        spritz.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
        spritz.setSelection(false);
        spritz.setText(Messages.CompositeSPRITZ);
        spritz.setToolTipText(Messages.CompositeSPRITZTool);
        spritz.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                alg = new AlgSpritz(alg.getKey(), alg.getPlain(), 1);
                chooseW.setEnabled(true);
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                
            }
        });
        
        // holds a label and the combo widget
        Composite wholder = new Composite(algo, SWT.BORDER);
        wholder.setToolTipText(Messages.CompositeWTool);
        wholder.setLayout(new GridLayout(2, false));
        wholder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        
        wlabel = new Label(wholder, SWT.CENTER);
        wlabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
        wlabel.setText(Messages.VariableW);
        wlabel.setToolTipText(Messages.CompositeWTool);
        
        // the drop down box to choose the value for w
        chooseW = new Combo(wholder, SWT.READ_ONLY);
        chooseW.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
        chooseW.setToolTipText(Messages.CompositeWTool);
        String[] values = new String[ARC4Con.TWO_FIFE_SIX / 2];
        for (int a = 0; a < (ARC4Con.TWO_FIFE_SIX / 2); a++) {
            values[a] = Integer.toString(2 * a + 1);
        }
        chooseW.setItems(values);
        // default value
        chooseW.select(ARC4Con.DEFAULT_W_INDEX);
        chooseW.setEnabled(false);
        chooseW.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                 ((AlgSpritz) alg).setW(Integer.parseInt(chooseW.getText()));
            }
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                
            }
        });
    }

    /**
     * Initialize the section that allows you to make misc settings to the plug-in
     */
    private void initMisc() {
        // labeled border of the section
    	miscGroup = new Group(this, SWT.NONE);
        miscGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, ARC4Con.H_SPAN_RIGHT, 1));
        miscGroup.setText(Messages.CompositeSettingsText);
        miscGroup.setToolTipText(Messages.CompositeSettingsTool);
        miscGroup.setLayout(new GridLayout(2, true));

        // the button for enabling highlighting
        radioyes = new Button(miscGroup, SWT.RADIO);
        radioyes.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
        radioyes.setSelection(true);
        radioyes.setText(Messages.InstructionVisualRYESText);
        radioyes.setToolTipText(Messages.InstructionVisualRYESTool);
        radioyes.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                vector.highlight(alg.getI(), alg.getJ());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });

        // the button for disabling highlighting
        radiono = new Button(miscGroup, SWT.RADIO);
        radiono.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
        radiono.setSelection(false);
        radiono.setText(Messages.InstructionVisualRNOText);
        radiono.setToolTipText(Messages.InstructionVisualRNOTool);
        radiono.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                vector.clearColor();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                
            }
        });

        // the button to reset the step counter to zero
        restart = new Button(miscGroup, SWT.PUSH);
        restart.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
        restart.setText(Messages.CompositeResetText);
        restart.setToolTipText(Messages.CompositeResetTool);
        restart.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                reset();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });

        // the button to fill key and plaintext with new pseudorandom numbers
        randomize = new Button(miscGroup, SWT.PUSH);
        randomize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
        randomize.setText(Messages.CompositeSettingsRandText);
        randomize.setToolTipText(Messages.CompositeSettingsRandTool);
        randomize.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                alg.randomizeKeyPlain();
                syncronizeInternWithExtern();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
    }

    /**
     * Reset the step counter to zero; it effectively restarts the plug-in, the only thing left
     * behind is the users chosen step size
     */
    private void reset() {
        alg.reset();
        radioyes.setSelection(true);
        radiono.setSelection(false);
        randomize.setEnabled(true);
        chooseW.setEnabled(false);
        // default value
        chooseW.select(ARC4Con.DEFAULT_W_INDEX);
        arc4.setEnabled(true);
        spritz.setEnabled(true);
        arc4.setSelection(true);
        spritz.setSelection(false);
        key.switchButton(true);
        plain.switchButton(true);
        syncronizeInternWithExtern();
    }

    /**
     * Pull the data from the algorithm object and fill it into the UI
     */
    public void syncronizeInternWithExtern() {
        vector.setDataToGUI(alg.getVector());
        key.setDataToGUI(alg.getKey());
        plain.setDataToGUI(alg.getPlain());
        random.setDataToGUI(alg.getRandom());
        enc.setDataToGUI(alg.getEnc());
        var.setI(alg.getI());
        var.setJ(alg.getJ());
        // to highlight the changes on every step if the option is active
        if (radioyes.getSelection()) {
            vector.highlight(alg.getI(), alg.getJ());
        }
        var.setStep(alg.getStep());
        // display messagebox if the algorithm is finished and the user tries to continue anyway
        if (alg.getFinish()) {
            finish.open();
        }
    }

    /**
     * Let the algorithm proceed a certain amount of steps and show the changes in the UI
     * 
     * @param steps how many steps you want the algorithm to proceed. If encryption is already
     *            complete invoking this method has no effect except showing a messagebox that
     *            informs you that the algorithm is finished
     */
    public void performSteps(int steps) {
        if (steps > 0) {
            // if the execution of the algorithm has already been started you cannot change the key
            // or the plaintext
            key.switchButton(false);
            plain.switchButton(false);
            randomize.setEnabled(false);
            arc4.setEnabled(false);
            spritz.setEnabled(false);
            chooseW.setEnabled(false);
            // perform the encryption
            alg.encrypt(steps);
            // show the changes in the UI
            syncronizeInternWithExtern();
        }
    }


}