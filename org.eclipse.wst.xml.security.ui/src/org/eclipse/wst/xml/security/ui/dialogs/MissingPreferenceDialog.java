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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

/**
 * <p>Special dialog type (based on MessageDialog) to show a warning message about a missing
 * preference setting. This dialog includes a link to open the preference page directly and a button
 * to cancel the operation. The process continues after the user provided the required
 * information.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class MissingPreferenceDialog extends MessageDialog {
  /** The target for the preference page link. */
  private String target;

  /**
   * Constructor for the PreferenceDialog.
   *
   * @param shell The parent shell
   * @param title The dialog title
   * @param text The dialog info text
   * @param linkTarget The preference page to open
   */
  public MissingPreferenceDialog(Shell shell, String title, String text, String linkTarget) {
    super(shell, title, null, text, MessageDialog.WARNING, new String[] {"Continue", "Cancel"}, 0);
    target = linkTarget;
  }

  /**
   * Creates the customized dialog area with an image, info text and a link to the preference page.
   *
   * @param parent The parent composite
   * @return The updated composite
   */
  protected Control createCustomArea(Composite parent) {
    Label focusLabel = new Label(parent, SWT.None);
    focusLabel.forceFocus();
    Link prefs = new Link(parent, SWT.NONE);
    prefs.setText(NLS.bind(Messages.prefsDialogText, "<a href=\"\">Preferences</a>"));
    prefs.setToolTipText(Messages.prefsLinkToolTip);
    prefs.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        openPreferencePage();
      }
    });
    return parent;
  }

  /**
   * Opens the preferences with the node selected given by the target parameter. If the preferences
   * are closed by clicking on the OK button this dialog will disappear and the needed preference
   * values will be verified again.
   */
  private void openPreferencePage() {
    IWorkbench workbench = PlatformUI.getWorkbench();
    PreferenceManager pm = workbench.getPreferenceManager();
    if (pm != null) {
      PreferenceDialog d = new PreferenceDialog(workbench.getActiveWorkbenchWindow().getShell(),
          pm);
      d.setSelectedNode(target);
      d.create();

      if (d.open() == Window.OK) {
        close();
      }
    }
  }
}
