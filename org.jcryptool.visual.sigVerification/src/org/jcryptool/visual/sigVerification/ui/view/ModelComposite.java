// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2013 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sigVerification.ui.view;

import java.util.Date;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.sigVerification.Messages;
import org.jcryptool.visual.sigVerification.SigVerificationPlugin;
import org.jcryptool.visual.sigVerification.cert.CertGeneration;

/**
 * This class contains all the code required for the design and functionality of the verification
 * model view.
 *
 */
public class ModelComposite extends Composite {
    private Text lblGeneralDescription;
    private Text lblHeader;
    private Text lblTitle;
    private Button btnShellM;
    private Button btnChainM;
    private Label lblRoot;
    private Label lbllevel2;
    private Label lbllevel3;
    private Text lblrootChoose;
    private Text lbllevel2Choose;
    private Text lbllevel3Choose;
    private Button btnNewResult;
    private Text lblChoose;
    private CertGeneration Certificates;
    private Text textValidDate;
	private String now;
	private String dateRoot;
	private String dateLevel2;
	private String dateUser;
	private Date temp;
	private Date changeTest;
	private Date changeRoot;
	private Date changeLevel2;
	private Date changeUser;
	private Label lblResult1;
	private Label lblResult2;
	private Composite border;


    public ModelComposite(final Composite parent, final int style, final SigVerView sigVerView) {
        super(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        createContents(parent);
        createActions();

        // Adds reset button to the Toolbar
         IToolBarManager toolBarMenu = sigVerView.getViewSite().getActionBars().getToolBarManager();
         Action action = new Action("Reset", IAction.AS_PUSH_BUTTON) {public void run() {reset();}}; //$NON-NLS-1$
         action.setImageDescriptor(SigVerificationPlugin.getImageDescriptor("icons/reset.gif")); //$NON-NLS-1$
         toolBarMenu.add(action);
    }


    /**
     * Create contents of the application window.
     *
     * @param parent
     */
    private void createContents(final Composite parent) {
        parent.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
        parent.setLayout(null);
        setLayout(null);

        Certificates = new CertGeneration();
        try {//create default certificates
			Certificates.setRoot(Certificates.createCertificate(1));
			Certificates.setLevel2(Certificates.createCertificate(2));
			Certificates.setUser(Certificates.createCertificate(3));
		} catch (Exception e) {
            LogUtil.logError(e);
		}

        lblGeneralDescription = new Text(this, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
        lblGeneralDescription.setEditable(false);
		lblGeneralDescription.setBounds(10, 37, 964, 78);
		lblGeneralDescription.setBackground(SWTResourceManager.getColor(255, 255, 255));
		lblGeneralDescription.setText(Messages.ModelComposite_description);

		lblHeader = new Text(this, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
        lblHeader.setEditable(false);
        lblHeader.setBounds(10, 10, 964, 31);
        lblHeader.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
        lblHeader.setText(Messages.ModelComposite_lblHeader);
        lblHeader.setBackground(SWTResourceManager.getColor(255, 255, 255));

        lblTitle = new Text(this, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
        lblTitle.setEditable(false);
        lblTitle.setBounds(20, 121, 216, 27);
        lblTitle.setText(Messages.ModelComposite_lblTitle);

        border = new Composite(this, SWT.BORDER);
		border.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		border.setBounds(10, 132, 964, 560);

		btnShellM = new Button(border, SWT.NONE);
		btnShellM.setBounds(293, 10, 180, 36);
		btnShellM.setText(Messages.ModelComposite_btnShellM);
		{
		    btnChainM = new Button(border, SWT.NONE);
		    btnChainM.setEnabled(false);
		    btnChainM.setBounds(470, 10, 171, 36);
		    btnChainM.setText(Messages.ModelComposite_btnChainM);

		}
		{
		    lblRoot = new Label(border, SWT.NONE);
		    lblRoot.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		    lblRoot.setBounds(64, 133, 380, 67);
		    lblRoot.setText(Messages.ModelComposite_lblroot);
		}
		{
		    lbllevel2 = new Label(border, SWT.NONE);
		    lbllevel2.setText(Messages.ModelComposite_lbllevel2);
		    lbllevel2.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		    lbllevel2.setBounds(103, 219, 341, 67);
		}
		{
		    lbllevel3 = new Label(border, SWT.NONE);
		    lbllevel3.setText(Messages.ModelComposite_lbllevel3);
		    lbllevel3.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		    lbllevel3.setBounds(145, 304, 299, 67);
		}
		temp=Certificates.getRoot().getNotAfter();
		dateRoot=setFormat(temp);
		{
		    lblrootChoose = new Text(border, SWT.BORDER);
		    lblrootChoose.setEditable(true);
		    lblrootChoose.setText(dateRoot);
		    lblrootChoose.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		    lblrootChoose.setBounds(773, 133, 146, 67);
		}
		temp=Certificates.getLevel2().getNotAfter();
		dateLevel2=setFormat(temp);
		{
		    lbllevel2Choose = new Text(border, SWT.BORDER);
		    lbllevel2Choose.setEditable(true);
		    lbllevel2Choose.setText(dateLevel2);
		    lbllevel2Choose.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		    lbllevel2Choose.setBounds(773, 219, 146, 67);
		}
		temp=Certificates.getUser().getNotAfter();
		dateUser=setFormat(temp);
		{
		    lbllevel3Choose = new Text(border, SWT.BORDER);
		    lbllevel3Choose.setEditable(true);
		    lbllevel3Choose.setText(dateUser);
		    lbllevel3Choose.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		    lbllevel3Choose.setBounds(773, 304, 146, 67);
		}
		{
		    btnNewResult = new Button(border, SWT.NONE);
		    btnNewResult.setEnabled(true);
		    btnNewResult.setBounds(293, 515, 322, 31);
		    btnNewResult.setText(Messages.ModelComposite_btnNewResult);
		}
		Button btnReset = new Button(border, SWT.NONE);
		btnReset.setLocation(829, 515);
		btnReset.setSize(90, 30);
		btnReset.addSelectionListener(new SelectionAdapter() {
		    @Override
		    public void widgetSelected(SelectionEvent e) {
		    	reset();
		    }
		});
		btnReset.setText(Messages.ModelComposite_btnReset);
		{
		    lblChoose = new Text(border, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
		    lblChoose.setEditable(false);
		    lblChoose.setText(Messages.ModelComposite_Choose);
		    lblChoose.setBounds(719, 70, 200, 50);
		}
		temp=Certificates.getNow();//default date for validity checks
		now=setFormat(temp); //to string
		{
			textValidDate = new Text(border, SWT.BORDER);
			textValidDate.setEditable(true);
			textValidDate.setText(now);
			textValidDate.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		    textValidDate.setBounds(402, 453, 146, 36);
		}
		lblResult1 = new Label(border, SWT.NONE);
		lblResult1.setBounds(275, 395, 121, 107);

		lblResult2=new Label (border, SWT.NONE);
		lblResult2.setBounds(275, 395, 121, 107);
        }



    private void createActions() {
    	//Listener for the new date to valid at
    	 textValidDate.addModifyListener(new ModifyListener() {
             public void modifyText(final ModifyEvent e) {
                 if (textValidDate.getText().length() > 0) {
                 	String temp=new String(textValidDate.getText());
                 	changeTest=toDate(temp);
                 }
             }
         });

    	 //listener for the root validity
    	 lblrootChoose.addModifyListener(new ModifyListener() {
             public void modifyText(final ModifyEvent e) {
                 if (lblrootChoose.getText().length() > 0) {
                 	String temp=new String(lblrootChoose.getText());
                 	changeRoot=toDate(temp);
                 }
             }
         });

    	//listener for the level2 validity
    	 lbllevel2Choose.addModifyListener(new ModifyListener() {
             public void modifyText(final ModifyEvent e) {
                 if (lbllevel2Choose.getText().length() > 0) {
                 	String temp=new String(lbllevel2Choose.getText());
                 	changeLevel2=toDate(temp);
                 }
             }
         });

    	//listener for the user validity
    	 lbllevel3Choose.addModifyListener(new ModifyListener() {
             public void modifyText(final ModifyEvent e) {
                 if (lbllevel3Choose.getText().length() > 0) {
                 	String temp=new String(lbllevel3Choose.getText());
                 	changeUser=toDate(temp);
                 }
             }
         });


     	btnNewResult.addSelectionListener(new SelectionAdapter(){
     		public void widgetSelected(SelectionEvent e) {
     			boolean result = false;

                try {
                    if(changeRoot!=null){//if root has a new validity
              			Certificates.setRoot(Certificates.createCertificateNew(1,changeRoot));
                    }
                    if(changeLevel2!=null){//if level2 has a new validity
              			Certificates.setLevel2(Certificates.createCertificateNew(2,changeLevel2));
                    }
                    if(changeUser!=null){//if user has a new validity
               			Certificates.setUser(Certificates.createCertificateNew(3,changeUser));
                    }

                    if(changeTest!=null){//if the date to validate was changed
                       	result=Certificates.verify(changeTest);
                    }else if(changeTest==null){
                    	result=Certificates.verify(Certificates.getNow());
                    }

                    if(result==true){//if certificates are valid show green tick
                    	lblResult1.moveAbove(lblResult2);
                      	lblResult1.setImage(SWTResourceManager.getImage(SigVerComposite.class, "/icons/gruenerHacken.png"));
                    }else{//if certificates are not valid show red cross
                    	lblResult2.moveAbove(lblResult1);
                      	lblResult2.setImage(SWTResourceManager.getImage(SigVerComposite.class, "/icons/rotesKreuz.png"));
                    }

                 } catch (Exception ex) {
                     LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
                 }

             }
         });
    }

    /**
     * Converts a date from a String to a Date and requests for the date to be checked
     * @param string - the date (in form of a string) to save
     * @return the string as a date
     */
    @SuppressWarnings("deprecation")
 	private Date toDate(final String string){
     	Date date=new Date();
     	char[] temp=string.toCharArray();
     	int i=1;
     	int day=0,month = 0,year=0;

     	for(;i<string.length();i++){
     		if(i<=1){//day
     			day=((temp[i-1]-48)*10)+(temp[i]-48);
     			date.setDate(day);
     		}else if(i==4){//month
     			month=(((temp[i-1]-48)*10)+(temp[i]-48)-1);
     			date.setMonth(month);
     		}else if(i==9){//year
     			year=(((temp[i-3]-48)*1000)+((temp[i-2]-48)*100)+((temp[i-1]-48)*10)+(temp[i]-48)-1900);
     			date.setYear(year);
     		}
     	}
     	if(checkDate(day,(month+1),(year+1990))!=true){
     		btnNewResult.setEnabled(false);
     	}else{
     		btnNewResult.setEnabled(true);
     	}
     	return date;
     }


    /**
     * checks whether a date is a valid date
     * @param day
     * @param month
     * @param year
     * @return true - if the given date exists
     */
    private boolean checkDate(int day, int month, int year) {
    	boolean result=true;

		if(month<1 || month>12){
			result=false;
		}
		/*months with 31 days*/
		if(month == 1 || month == 3 ||month == 5 ||month == 7 ||month == 8 ||month == 10 ||month == 12){
			if(day <1 || day>31){
				result=false;
			}
		}
		/*months with 30 days*/
		if(month == 4 || month == 6 ||month == 9 ||month == 11){
			if(day <1 || day >30){
				result=false;
			}
		}
		if(month==2 && leapyear(year)==true){
			if(day <1 || day >29){
				result=false;
				}
			}
		if(month==2 && leapyear(year)==false){
			if(day<1 || day >28){
				result=false;
			}
		}
		return result;
	}

    /**
     * checks whether a given year is a leap year
     * @param year
     * @return true if it's a leap year
     */
    public static boolean leapyear(int year){
		if(year%4 == 0 && (year%100 != 0 || year%400 == 0)){
			return true;
		}else{
			return false;
		}
	}

    /**
     * Converts a Date to a String (in order to be displayed on the user interface)
     * @param date to convert
     * @return the String
     */
    @SuppressWarnings("deprecation")
 	public String setFormat(Date date){
     	String result;

     	if(date.getDate()<=9){
     		result="0"+date.getDate()+".";
     	}else{
     		result=date.getDate()+".";
     	}
     	if(date.getMonth()<=9){
     		result+="0"+(date.getMonth()+1)+"."+(date.getYear()+1900)+" ";
     	}else{
     		result+=(date.getMonth()+1)+"."+(date.getYear()+1900)+" ";
     	}

   	  return result;
     }

	/**
	 * resets the certificates to the default certificates
	 * and resets the shown dates
	 */
	private void reset() {
    	btnNewResult.setEnabled(true);
        try {
			Certificates.setRoot(Certificates.createCertificate(1));
			Certificates.setLevel2(Certificates.createCertificate(2));
	 		Certificates.setUser(Certificates.createCertificate(3));

	 		temp=Certificates.getEnd();
			dateRoot=setFormat(temp);
			lblrootChoose.setText(dateRoot);

			dateLevel2=setFormat(temp);
			lbllevel2Choose.setText(dateLevel2);

			dateUser=setFormat(temp);
			lbllevel3Choose.setText(dateUser);
		} catch (Exception e) {
            LogUtil.logError(e);
		}
    }



    /**
     * Create the menu manager.
     *
     * @return the menu manager
     */
    /*
     * @Override protected MenuManager createMenuManager() { MenuManager menuManager = new
     * MenuManager("menu"); return menuManager; }
     */

    /**
     * Create the status line manager.
     *
     * @return the status line manager
     */
    /*
     * @Override protected StatusLineManager createStatusLineManager() { StatusLineManager
     * statusLineManager = new StatusLineManager(); return statusLineManager; } /** Return the
     * initial size of the window.
     */
    /*
     * @Override protected Point getInitialSize() { return new Point(1270, 750); }
     */

}
