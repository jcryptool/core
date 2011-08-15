/*******************************************************************************
 * Copyright (c) 2008 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.dialogs;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * <p>Special dialog type (based on InputDialog) to provide a password dialog with an encrypted
 * textfield (echo). Used for several Quick Functions to ask the user for passwords.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class PasswordDialog extends InputDialog {
  /**
   * PasswordDialog constructor without input validator.
   *
   * @param parentShell The parent shell
   * @param dialogTitle The dialog title
   * @param dialogMessage The dialog message
   * @param initialValue The initial value
   */
  public PasswordDialog(Shell parentShell, String dialogTitle, String dialogMessage,
      String initialValue) {
    super(parentShell, dialogTitle, dialogMessage, initialValue, null);
  }

  /**
   * PasswordDialog constructor with input validator.
   *
   * @param parentShell The parent shell
   * @param dialogTitle The dialog title
   * @param dialogMessage The dialog message
   * @param initialValue The initial value
   * @param validator The input validator
   */
  public PasswordDialog(Shell parentShell, String dialogTitle, String dialogMessage,
      String initialValue, IInputValidator validator) {
    super(parentShell, dialogTitle, dialogMessage, initialValue, validator);
  }

  /**
   * Creates the dialog area and sets the <strong>*</strong> as echo character for the textfield.
   *
   * @param parent The parent composite
   * @return The Control
   */
  protected Control createDialogArea(Composite parent) {
    Control control = super.createDialogArea(parent);
    Text text = getText();
    text.setEchoChar('*');
    return control;
  }

  /**
   * @see org.eclipse.jface.dialogs.Dialog#okPressed()
   */
  protected void okPressed() {
    super.okPressed();
  }
}
