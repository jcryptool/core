//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2013 JCrypTool Team and Contributors
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.games.numbershark.util;

import org.eclipse.swt.widgets.Table;

/**
 * @author Johannes Spaeth
 * @version 1.0
 */

public class CSVConverter {
    StringBuffer content = new StringBuffer();

    public CSVConverter(Table table) {
        int numOfColumns = table.getColumnCount();
        int numOfRows = table.getItemCount();
        for (int i = 0; i < numOfColumns - 1; i++) {
            content.append("\"" + table.getColumn(i).getText() + "\";");
        }
        content.append("\"" + table.getColumn(numOfColumns - 1).getText() + "\"");
        for (int j = 0; j < numOfRows; j++) {
            content.append("\n");
            for (int i = 0; i < numOfColumns - 1; i++) {
                content.append("\"" + table.getItem(j).getText(i) + "\";");
            }
            content.append("\"" + table.getItem(j).getText(numOfColumns - 1) + "\"");
        }
    }

    public String getContentToCSV() {
        return content.toString();
    }
}
