//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2020 JCrypTool Team and Contributors
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.games.numbershark.dialogs;

import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.games.numbershark.util.CSVConverter;
import org.jcryptool.games.numbershark.views.NumberSharkView;

public class EndOfGameDialog {
    private MessageBox mb = null;
    private NumberSharkView view;
    private int[] optValues = { 0, 2, 3, 7, 9, 15, 17, 21, 30, 40, 44, 50, 52, 66, 81, 89, 93, 111, 113, 124, 144, 166,
            170, 182, 198, 224, 251, 279, 285, 301, 303, 319, 352, 386, 418, 442, 448, 486, 503, 525, 529, 571, 573,
            617, 660, 706, 710, 734, 758, 808, 833, 885, 891, 940, 981, 1017, 1040, 1098, 1104, 1137, 1139, 1201, 1264,
            1296, 1328, 1394, 1400, 1468, 1499, 1566, 1570, 1642, 1644, 1718, 1793, 1869, 1914, 1991, 1997, 2041, 2105,
            2187, 2191, 2263, 2309, 2395, 2436, 2496, 2502, 2552, 2588, 2680, 2715, 2809, 2853, 2901, 2909, 3007, 3106,
            3164, 3168, 3270, 3272, 3332, 3434, 3540, 3544, 3652, 3654, 3764, 3813, 3925, 3929, 4043, 4101, 4217, 4334,
            4452, 4506, 4593, 4689, 4811, 4860, 4984, 5109, 5191, 5205, 5301, 5348, 5478, 5482, 5572, 5620, 5754, 5844,
            5928, 5934, 6072, 6074, 6164, 6219, 6361, 6427, 6523, 6599, 6745, 6892, 7040, 7050, 7137, 7139, 7223, 7376,
            7530, 7598, 7688, 7694, 7852, 7917, 8005, 8071, 8233, 8239, 8403, 8568, 8734, 8738, 8906, 8954, 9124, 9295,
            9467, 9473, 9647, 9822, 9985, 10056, 10234, 10240, 10401, 10403, 10543, 10608, 10744, 10836, 11022, 11132,
            11320, 11447, 11637, 11647, 11787, 11789, 11983, 12164, 12332, 12336, 12471, 12473, 12648, 12727, 12929,
            13017, 13146, 13240, 13446, 13653, 13832, 13922, 14066, 14078, 14290, 14369, 14583, 14675, 14834, 14906,
            15124, 15201, 15346, 15424, 15646, 15658, 15827, 15952, 16178, 16182, 16308, 16310, 16540, 16771, 16911,
            16915, 17068, 17174, 17410, 17501, 17739, 17745, 17877, 17879, 18121, 18364, 18608, 18853, 19099, 19159,
            19291, 19382, 19533, 19543, 19795, 19915, 20169, 20424, 20552, 20558, 20816, 20920, 21075, 21336, 21598,
            21604, 21859, 21983, 22249, 22350, 22618, 22624, 22894, 22896, 23142, 23404, 23678, 23953, 24115, 24121,
            24399, 24678, 24934, 24938, 25220, 25222, 25506, 25791, 26077, 26179, 26395, 26515, 26805, 26918, 27210,
            27220, 27514, 27650, 27822, 28041, 28339, 28429, 28691, 28787, 29089, 29198, 29430, 29558, 29796, 29810,
            30031, 30138, 30448, 30452, 30719, 30721, 31035, 31248, 31564, 31568, 31886, 32044, 32289, 32404, 32726,
            32828, 33074, 33396, 33722, 33835, 34015, 34129, 34354, 34368, 34700, 35033, 35367, 35519, 35774, 35780,
            36118, 36239 };

    public EndOfGameDialog(Shell shell, NumberSharkView view) {
        this.view = view;
        mb = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.YES | SWT.NO);
        int playerScore = view.getPlayerScore();
        int sharkScore = view.getSharkScore();
        String msg = null;
        int gameNMax = view.getNumberOfFields();
        if (playerScore > sharkScore) {
            if (view.getNumberOfFields() - 1 <= optValues.length) {
                if (playerScore == optValues[gameNMax - 1]) {
                    msg = NLS.bind(Messages.EndOfGameDialog_0, new Object[] { playerScore, sharkScore, gameNMax });
                } else {
                    msg = NLS.bind(Messages.EndOfGameDialog_1, new Object[] { playerScore, sharkScore, gameNMax,
                            optValues[gameNMax - 1] });
                }
            } else {
                msg = NLS.bind(Messages.EndOfGameDialog_2, new Object[] { playerScore, sharkScore });
            }
        } else {
            msg = NLS.bind(Messages.EndOfGameDialog_3, new Object[] { playerScore, sharkScore });
        }
        mb.setText(Messages.EndOfGameDialog_4);
        mb.setMessage(msg);
    }

    public void open() {
        int answer = mb.open();
        if (answer == SWT.YES) {
            FileDialog saveDialog = new FileDialog(mb.getParent(), SWT.SAVE);
            saveDialog.setFilterPath(DirectoryService.getUserHomeDir());
            saveDialog.setFilterNames(new String[] { "CSV-File", "All Files" });
            saveDialog.setFilterExtensions(new String[] { "*.csv", "*" });
            saveDialog
                    .setFileName("log_numberShark_" + view.getNumberOfFields() + "-" + view.getPlayerScore() + ".csv");
            saveDialog.setOverwrite(true);
            String fileName = saveDialog.open();

            if (fileName != null) {
                CSVConverter converter = new CSVConverter(view.getTable());
                try {
                    FileWriter writer = new FileWriter(fileName);

                    writer.append(converter.getContentToCSV());

                    writer.flush();
                    writer.close();
                } catch (IOException ex) {
                    LogUtil.logError(ex);
                }
            }
        }
    }
}
