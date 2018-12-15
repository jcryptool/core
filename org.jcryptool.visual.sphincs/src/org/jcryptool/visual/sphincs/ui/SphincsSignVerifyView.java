//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.sphincs.ui;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
//import org.eclipse.swt.widgets.MessageBox;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.sphincs.SphincsDescriptions;
import org.jcryptool.visual.sphincs.algorithm.*;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;


/**
 * Class for the Composite of Tabpage "Sign and Verify"
 * It provides a GUI interface to sign messages and see the signature result and to verify the signature.
 * 
 * @author Philipp Guggenberger
 * 
 * 
 * @author Klaus Kaiser
 *
 */
public class SphincsSignVerifyView extends Composite {
    
   
   private Signature signature = null;
   
   private Button btnSign;
   private Button btnVerify;
   private Button btnColorAuthpath;
   private Button btnColorIndex;
   private Button btnColorSignature;
  
   private StyledText txtMessage;
   private StyledText txtSignature;
   private StyledText txtStatus;
   
   private Label lblColorLabel;
   
   private Group defineMessageGroup;
   private Group signatureGroup;
   private GridData messageGroupLayout;
   
   boolean authFlag = false, indeFlag = false, signFlag = false, verified=false;
   
   private String authPath;
   private String index;
   private String horstSignature;
   
   private Color defaultColor = new Color(getDisplay(), 225,225,225);
   
  

    public SphincsSignVerifyView(Composite parent, int style, aSPHINCS256 sphincs) {
        super(parent, style);        
        this.setLayout(new GridLayout(6, true));

        // ***********************************
        // Beginning of GUI elements
        // ***********************************

        defineMessageGroup = new Group(this, SWT.NONE);
        defineMessageGroup.setLayout(new GridLayout(6, true));
        defineMessageGroup.setFont(FontService.getNormalBoldFont());
        defineMessageGroup.setText(SphincsDescriptions.SphincsSign_Group_0);

        messageGroupLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1);
        messageGroupLayout.minimumHeight = 200;
        defineMessageGroup.setLayoutData(messageGroupLayout);
        new Label(this, SWT.NONE);
        
        btnSign = new Button(this, SWT.NONE);
        btnSign.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
        btnSign.setText(SphincsDescriptions.SphincsSign_Button_0);
       
        // sign message
        btnSign.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
               String message = txtMessage.getText();
               
               try {                    
                signature = sphincs.sign(message.getBytes());
              
                if (signature != null) {
                    restoreForNewSign();
                    
                    authPath = signature.getAuthPath();
                    horstSignature = signature.getHorstSignature();
                    index = signature.getIndex();
                   
                    txtSignature.setText(authPath + horstSignature + index);
                   
                   if (verified == false) {
                       txtStatus.setBackground(ColorConstants.white);
                       txtStatus.setText("");
                   }
                   
                   authFlag = false;
                   indeFlag = false;
                   signFlag = false;
                   
                   setSphincsSignatur(signature);
                }
               } catch(NullPointerException ex) {
                   MessageBox messageBoxx = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
                   messageBoxx.setMessage(SphincsDescriptions.NoKeyText);
                   messageBoxx.setText(SphincsDescriptions.Info);
                   messageBoxx.open();
               }
            }
         });
          
        txtStatus = new StyledText(this, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
        txtStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        txtStatus.setAlignment(SWT.CENTER);
        
        btnVerify = new Button(this, SWT.NONE);
        btnVerify.setEnabled(false);
        
        // verify signature
        btnVerify.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                               
                if(signature.getHorstSignature().equals(sphincs.sign(txtMessage.getText()).getHorstSignature())) {
                   txtStatus.setBackground(ColorConstants.green);
                   txtStatus.setText(SphincsDescriptions.SphincsVerify_Success);
                   verified = true;
                }
                else
                {
                   txtStatus.setBackground(ColorConstants.red);
                   txtStatus.setText(SphincsDescriptions.SphincsVerify_Fail);
                   verified = false;
                }
                
                
            }
        });
        btnVerify.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 1));
        btnVerify.setText(SphincsDescriptions.SphincsVerify_Button_0);
        new Label(this, SWT.NONE);
               
        signatureGroup = new Group(this, SWT.NONE);
        signatureGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, SphincsConstant.H_SPAN_MAIN, 1));
        signatureGroup.setLayout(new GridLayout(8, true));
        signatureGroup.setText(SphincsDescriptions.SphincsSign_Group_1);
        signatureGroup.setFont(FontService.getNormalBoldFont());

        txtMessage = new StyledText(defineMessageGroup, SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
        txtMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
        txtMessage.setText(SphincsDescriptions.SphincsSign_Text_0);
            
        lblColorLabel = new Label(signatureGroup, SWT.NONE);
        lblColorLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        lblColorLabel.setText(SphincsDescriptions.SphincsSign_Label_0);
        
        btnColorAuthpath = new Button(signatureGroup, SWT.NONE);
        
        btnColorAuthpath.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
               
                int indexAuthPath = txtSignature.getText().indexOf(authPath); //liefert den Index des ersten Zeichens in der gesamten Signatur
           
                
                if (indeFlag == false) {
                   
                    txtSignature.setStyleRange(new StyleRange(indexAuthPath, authPath.length(), ColorConstants.cyan,  txtSignature.getBackground()));
                    txtSignature.setTopIndex(0);
                    btnColorAuthpath.setBackground(ColorConstants.cyan);
                    txtSignature.setFocus();
                    indeFlag = true;
                }
                else {
                    txtSignature.setStyleRange(new StyleRange(indexAuthPath, authPath.length(), ColorConstants.black,  txtSignature.getBackground()));
                    txtSignature.setTopIndex(0);
                    btnColorAuthpath.setBackground(defaultColor);
                    txtSignature.setFocus();
                    indeFlag = false;
                }
                
            }
        });
        btnColorAuthpath.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        btnColorAuthpath.setText(SphincsDescriptions.SphincsSign_Button_1);
        btnColorAuthpath.setEnabled(false);
        
             
        btnColorIndex = new Button(signatureGroup, SWT.NONE);
        
        btnColorIndex.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                
                int leafIndexinSig = txtSignature.getText().indexOf(index); //liefert wieder ersten index vom index in der Signatur
                
                if (authFlag == false) {
                    
                    
                    txtSignature.setStyleRange(new StyleRange(leafIndexinSig, index.length(), ColorConstants.lightBlue,  txtSignature.getBackground()));
                    txtSignature.setTopIndex(0);
                    btnColorIndex.setBackground(ColorConstants.lightBlue);
                    txtSignature.setFocus();                   
                    authFlag = true;
                }
                else {
                    txtSignature.setStyleRange(new StyleRange(leafIndexinSig, index.length(), ColorConstants.black,  txtSignature.getBackground()));
                    txtSignature.setTopIndex(0);
                    btnColorIndex.setBackground(defaultColor);
                    txtSignature.setFocus();
                    
                    authFlag = false;
                }
                
                
            }
        });
      
        btnColorIndex.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        btnColorIndex.setText(SphincsDescriptions.SphincsSign_Button_2);
        btnColorIndex.setEnabled(false);
        
        
        btnColorSignature = new Button(signatureGroup, SWT.NONE);
        btnColorSignature.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                               
                int indexHorstSignature = txtSignature.getText().indexOf(horstSignature); //enthält Index des Beginns der HORST-Signatur
                
                if (signFlag == false) {
                    txtSignature.setStyleRange(new StyleRange(indexHorstSignature, horstSignature.length(), ColorConstants.green,  txtSignature.getBackground()));
                    txtSignature.setTopIndex(0);
                    btnColorSignature.setBackground(ColorConstants.green);
                    txtSignature.setFocus();
                    signFlag = true;
                }
                else {
                    txtSignature.setStyleRange(new StyleRange(indexHorstSignature, horstSignature.length(), ColorConstants.black,  txtSignature.getBackground()));
                    txtSignature.setTopIndex(0);
                    btnColorSignature.setBackground(defaultColor);
                    txtSignature.setFocus();
                    signFlag = false;
                }
            }
        });
        
        btnColorSignature.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,1, 1));
        btnColorSignature.setText(SphincsDescriptions.SphincsSign_Button_3);
        btnColorSignature.setEnabled(false);
        new Label(signatureGroup, SWT.NONE);
        new Label(signatureGroup, SWT.NONE);
        
          
        txtSignature = new StyledText(signatureGroup, SWT.READ_ONLY | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
        txtSignature.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
      
                
        // Disables the sign button if no message is given in the text field
        txtMessage.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                if (txtMessage.getText().length() > 0) {
                    btnSign.setEnabled(true);
                } else {
                    btnSign.setEnabled(false);
                }
                
                verified = false;
                }
            });
 
        // Enables the "color" buttons if signature is given in the text field
        txtSignature.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                if (txtSignature.getText().length() > 0) {
                    btnColorSignature.setEnabled(true);
                    btnColorIndex.setEnabled(true);
                    btnColorAuthpath.setEnabled(true);
                    btnVerify.setEnabled(true);
                } else {
                    btnColorSignature.setEnabled(false);
                    btnColorIndex.setEnabled(false);
                    btnColorAuthpath.setEnabled(false);
                    btnVerify.setEnabled(false);
                }

            }
        });
    } 
    
    
    public Signature getSphincsSignatur () {
        return this.signature;
    }
    
    public void setSphincsSignatur (Signature signature) {
        this.signature = signature;
    }
        
    private void restoreForNewSign() {
        txtStatus.setBackground(ColorConstants.white);
        txtStatus.setText("");
        
        txtSignature.setText("");
        
        btnColorSignature.setBackground(defaultColor);
        btnColorIndex.setBackground(defaultColor);
        btnColorAuthpath.setBackground(defaultColor);
    }
   
 }
