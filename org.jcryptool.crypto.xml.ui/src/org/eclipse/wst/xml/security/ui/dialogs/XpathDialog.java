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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.wst.xml.security.core.utils.Utils;
import org.eclipse.wst.xml.security.ui.XSTUIPlugin;
import org.eclipse.wst.xml.security.ui.utils.IContextHelpIds;
import org.w3c.dom.Document;

/**
 * <p>Provides a dialog window (<code>ElementListSelectionDialog</code>) with the XPath
 * expressions of all the elements of the selected XML document. Used on the first pages of the
 * XML Signature Wizard and the XML Encryption Wizard.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class XpathDialog extends ElementListSelectionDialog {
  /** The XML document. */
  private Document doc;

  /**
   * First constructor for the XPath dialog with two parameters.
   *
   * @param shell The parent shell
   * @param labelProvider The ILabelProvider
   */
  public XpathDialog(Shell shell, ILabelProvider labelProvider) {
    super(shell, labelProvider);
  }

  /**
   * Second constructor for the XPath dialog with four parameters.
   *
   * @param shell The parent shell
   * @param labelProvider ILabelProvider
   * @param doc The XML document
   * @param message The message to display in the dialog window
   */
  public XpathDialog(Shell shell, ILabelProvider labelProvider, Document doc, String message) {
    super(shell, labelProvider);
    setTitle(Messages.xpathSelectionTitle);
    setMessage(message);
    this.doc = doc;
    setElements(createContent());
    open();
  }

  /**
   * Adds the context help to the dialog.
   *
   * @param parent The parent composite
   * @return The manipulated parent composite
   */
  protected Control createDialogArea(Composite parent) {
    Control comp = super.createDialogArea(parent);
    PlatformUI.getWorkbench().getHelpSystem().setHelp(comp, IContextHelpIds.XPATH_DIALOG);
    return comp;
  }

  /**
   * Collects all the nodes from the XML document.
   *
   * @return Object array with all the nodes
   */
  private Object[] createContent() {
    try {
      return Utils.getCompleteXpath(doc);
    } catch (Exception e) {
      IStatus status = new Status(IStatus.ERROR, XSTUIPlugin.getDefault().getBundle().getSymbolicName(), 0,
          Messages.xpathSelectionError, e);
      XSTUIPlugin.getDefault().getLog().log(status);
      return null;
    }
  }
}
