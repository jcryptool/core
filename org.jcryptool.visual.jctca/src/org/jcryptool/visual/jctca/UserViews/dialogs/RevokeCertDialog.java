/**
 * 
 */
package org.jcryptool.visual.jctca.UserViews.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * @author sho
 *
 */
public class RevokeCertDialog extends Dialog {
	String txt_reason;
	Combo reason;
	/**
	 * @param parentShell
	 */
	public RevokeCertDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gl = new GridLayout(1, false);
		container.setLayout(gl);
		GridData gd = new GridData(SWT.FILL, SWT.NONE, false, true);

		container.setLayoutData(gd);

		Label lbl_explain = new Label(container, SWT.WRAP);
		gd.widthHint = 550;
		lbl_explain.setLayoutData(gd);
		lbl_explain.setText("Sie sind kurz davor, Ihr Zertifikat zu widerrufen. Dies wird zur Folge haben, dass Signaturen nach dem Widerrufszeitpunkt nicht mehr gültig sind. Wenn Sie Ihr Zertifikat tatsächlich widerrufen wollen, wählen Sie einen der Gründe aus der Dropdownliste aus und klicken Sie auf \"Widerruf an die RA weiterleiten\"");

		reason = new Combo(container, SWT.DROP_DOWN);
		GridData reason_gd = new GridData(SWT.LEFT, SWT.NONE, false, false);
		reason_gd.widthHint=530;
		reason.setLayoutData(reason_gd);
		reason.add("Privater Schlüssel wurde kompromittiert.");
		reason.add("Privater Schlüssel wurde verloren.");
		reason.select(0);
		reason.pack(true);

		container.layout();
		return container;

	}

	
	@Override
	protected void okPressed(){
		txt_reason = reason.getText();
		super.okPressed();
	}
	  @Override
	  protected void createButtonsForButtonBar(Composite parent) {
	    createButton(parent, IDialogConstants.OK_ID, "Widerruf an RA weiterleiten", true);
	    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	  }
	  
	  public String getReason(){
		  return txt_reason;
	  }
	  
	  @Override
	  protected void configureShell(Shell shell){
		  super.configureShell(shell);
		  shell.setText("Zertifikat widerrufen");
	  }

}
