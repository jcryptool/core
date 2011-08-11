//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.commands.ui.scanner;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.jcryptool.core.logging.utils.LogUtil;

public class DebugPartitioner extends FastPartitioner {

    public DebugPartitioner(IPartitionTokenScanner scanner, String[] legalContentTypes) {
        super(scanner, legalContentTypes);
    }

    public ITypedRegion[] computePartitioning(int offset, int length,
            boolean includeZeroLengthPartitions) {
        return super.computePartitioning(offset, length, includeZeroLengthPartitions);
    }

    public void connect(IDocument document, boolean delayInitialization) {
        super.connect(document, delayInitialization);
        printPartitions(document);
    }

    public void printPartitions(IDocument document) {
        StringBuffer buffer = new StringBuffer();

        ITypedRegion[] partitions = computePartitioning(0, document.getLength());
        for (int i = 0; i < partitions.length; i++) {
            try {
                buffer.append("Partition type: " + partitions[i].getType() + ", offset: " //$NON-NLS-1$ //$NON-NLS-2$
                        + partitions[i].getOffset() + ", length: " + partitions[i].getLength()); //$NON-NLS-1$
                buffer.append("\n"); //$NON-NLS-1$
                buffer.append("Text:\n"); //$NON-NLS-1$
                buffer.append(document.get(partitions[i].getOffset(), partitions[i].getLength()));
                buffer.append("\n---------------------------\n\n\n"); //$NON-NLS-1$
            } catch (BadLocationException e) {
                LogUtil.logError(e);
            }
        }
    }

}
