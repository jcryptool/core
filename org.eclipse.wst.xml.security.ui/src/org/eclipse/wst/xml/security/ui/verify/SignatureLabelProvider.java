/*******************************************************************************
 * Copyright (c) 2009 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.verify;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.security.core.verify.VerificationResult;
import org.eclipse.wst.xml.security.ui.XSTUIPlugin;

/**
 * <p>The label provider for the <b>XML Signatures</b> view. Prepares
 * the signature data for display.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class SignatureLabelProvider extends LabelProvider implements ITableLabelProvider {
    /**
     * Returns the text for the current column.
     *
     * @param element The object representing the entire row, or null indicating that no input
     *        object is set in the viewer
     * @param columnIndex The zero-based index of the column in which the label appears
     * @return The text of the column
     */
    public String getColumnText(final Object element, final int columnIndex) {
        VerificationResult result = (VerificationResult) element;
        switch (columnIndex) {
            case 1:
                return result.getId();
            case 2:
                return result.getType();
            case 3:
                return result.getAlgorithm();
            default:
                return null;
        }
    }

    /**
     * Returns the label image for the given column of the given element.
     *
     * @param element The object representing the entire row, or null indicating that no input
     *        object is set in the viewer
     * @param columnIndex The zero-based index of the column in which the label appears
     * @return Image or null if there is no image for the given object at columnIndex
     */
    public Image getColumnImage(final Object element, final int columnIndex) {
        VerificationResult result = (VerificationResult) element;

        if (columnIndex != 0 || result == null) {
            return null;
        }

        if (VerificationResult.VALID.equals(result.getStatus())) {
            return XSTUIPlugin.getDefault().getImageRegistry().get("sig_valid_small");
        } else if (VerificationResult.INVALID.equals(result.getStatus())) {
            return XSTUIPlugin.getDefault().getImageRegistry().get("sig_invalid_small");
        } else {
            return XSTUIPlugin.getDefault().getImageRegistry().get("sig_unknown_small");
        }
    }
}
