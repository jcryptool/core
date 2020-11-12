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
package org.jcryptool.games.numbershark.strategies;

import java.io.FileWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.games.numbershark.NumberSharkPlugin;
import org.jcryptool.games.numbershark.util.CSVConverter;

/**
 * Result dialog for the calculation of the optimal strategies
 * 
 * @author Johannes Spaeth
 * @version 0.9.5
 */

public abstract class AbstractResultDialog extends TitleAreaDialog {
    public static final int SAVE = 9999;
    public static final int PLAY = 9998;
    protected Table sequences;
    protected ArrayList<Integer> play = new ArrayList<Integer>();
    private int selectedStrategy;
    private Button playSelected;
    private int min = 2;
    private int max = 519;
    TableColumn[] columns;

    public AbstractResultDialog(Shell shell, int selectedStrategy) {
        super(shell);
        setShellStyle(SWT.TITLE | SWT.APPLICATION_MODAL | SWT.CLOSE | SWT.RESIZE);
        this.selectedStrategy = selectedStrategy;
    }

    @Override
	protected Control createDialogArea(Composite parent) {
        CalculationThread calculateSequences = new CalculationThread(min, max, selectedStrategy);
        final Shell shell = parent.getShell();

        try {
            new ProgressMonitorDialog(shell).run(true, true, calculateSequences);
        } catch (InvocationTargetException e) {
        	e.printStackTrace();
        } catch (InterruptedException e) {
        	e.printStackTrace();
        	
        }
        
        String[][] tableContent = calculateSequences.getSharkOutput();
        int stoppedAt = calculateSequences.getStoppedAt();

        if (stoppedAt < max) {
            tableContent = Arrays.copyOf(tableContent, stoppedAt - min);
            max = stoppedAt;
        }

         Composite area = (Composite) super.createDialogArea(parent);
        parent.setLayout(new GridLayout());
        //parent.setLayoutData(new GridData(GridData.FILL_BOTH));

        
        Composite composite = new Composite(area, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite.setLayout(new GridLayout());
        
//        createTable(area);
        createTable(composite);
        setTableContent(tableContent);
        
        Label separator = new Label(area, SWT.SEPARATOR | SWT.HORIZONTAL);
        separator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        PlatformUI.getWorkbench().getHelpSystem()
                .setHelp(parent, NumberSharkPlugin.PLUGIN_ID + ".optStratResultDialog"); //$NON-NLS-1$

        return area;
    }

    @Override
    protected Point getInitialSize() {
        return new Point(700, 550);
    }

//    @Override
//    protected void configureShell(Shell newShell) {
//        super.configureShell(newShell);
//    }

    /**
     * Create contents of the button bar.
     * 
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        playSelected = createButton(parent, PLAY, Messages.ShowOptStrategy_9, true);
        playSelected.setEnabled(false);
        playSelected.addSelectionListener(playSelection);

        Button saveAsButton = createButton(parent, SAVE, Messages.ShowOptStrategy_8, true);
        saveAsButton.addSelectionListener(new SelectionAdapter() {
        	
            @Override
			public void widgetSelected(SelectionEvent e) {

                int min = Integer.parseInt(sequences.getItem(0).getText(0));
                int max = Integer.parseInt(sequences.getItem(sequences.getItemCount() - 1).getText(0));

                FileDialog saveDialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
                saveDialog.setFilterPath(DirectoryService.getUserHomeDir());
                saveDialog.setFilterNames(new String[] { "CSV-File", "All Files" });
                saveDialog.setFilterExtensions(new String[] { "*.csv", "*" });
                saveDialog.setFileName("log_numberShark_" + min + "-" + max + ".csv");
                saveDialog.setOverwrite(true);

                String fileName = saveDialog.open();
                if (fileName == null) {
                    return;
                }

                try {
                    CSVConverter converter = new CSVConverter(sequences);
                    FileWriter writer = new FileWriter(fileName);

                    writer.append(converter.getContentToCSV());

                    writer.flush();
                    writer.close();
                } catch (Exception ex) {
                	LogUtil.logError(NumberSharkPlugin.PLUGIN_ID, ex);
                }
            }
        });

        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.CLOSE_LABEL, true);

    }

    public void createTable(Composite parent) {
        sequences = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
        sequences.setHeaderVisible(true);
        GridData gd_sequences = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gd_sequences.widthHint = 304;
        sequences.setLayoutData(gd_sequences);
        sequences.setLinesVisible(true);
        sequences.addListener(SWT.MouseDown, new Listener() {
            @Override
            public void handleEvent(Event event) {
                if (event.button == 3) {
                    Table table = (Table) event.widget;
                    Menu popupmenu = new Menu(table.getShell(), SWT.POP_UP);

                    Point zeroPointShell = table.getShell().getLocation();
                    Point zeroPointTable = table.getLocation();
                    Point point = new Point(zeroPointTable.x + zeroPointShell.x + event.x + 5, 104 + zeroPointShell.y
                            + event.y);
                    popupmenu.setLocation(point);
                    popupmenu.setVisible(true);
                    MenuItem play = new MenuItem(popupmenu, SWT.PUSH);
                    play.setText(Messages.OptStratDialog_9);
                    play.addSelectionListener(playSelection);
                    Image image = ImageService.getImage(NumberSharkPlugin.PLUGIN_ID, "icons/play_icon.png");
                    play.setImage(image);
                    MenuItem copy = new MenuItem(popupmenu, SWT.PUSH);
                    copy.setText(Messages.OptStratDialog_10);

                    image = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_COPY);

                    copy.setImage(image);
                    copy.addSelectionListener(new SelectionAdapter() {
                        @Override
						public void widgetSelected(SelectionEvent e) {
                            Clipboard cb = new Clipboard(sequences.getDisplay());
                            TextTransfer textTransfer = TextTransfer.getInstance();
                            StringBuffer sb = new StringBuffer(sequences.getSelection()[0].getText(0));
                            for (int i = 1; i < 5; i++) {
                                sb.append("|" + sequences.getSelection()[0].getText(i));
                            }
                            cb.setContents(new Object[] { sb.toString() }, new Transfer[] { textTransfer });
                        }
                    });
                    while (!popupmenu.isDisposed() && popupmenu.isVisible()) {
                        if (!table.getDisplay().readAndDispatch())
                            table.getDisplay().sleep();
                    }
                    popupmenu.dispose();
                }
            }
        });

        sequences.addSelectionListener(new SelectionAdapter() {
            @Override
			public void widgetSelected(SelectionEvent e) {
                if (sequences.getSelection().length > 0) {
                    playSelected.setEnabled(true);
                }
            }
        });

    }

    public void setTableContent(String[][] content) {
        int numOfCols = content[0].length;
        int numOfRows = content.length;
        columns = new TableColumn[numOfCols];
        for (int i = 0; i < numOfCols; i++) {
            columns[i] = new TableColumn(sequences, SWT.NONE);

        }

        for (int k = 0; k < numOfRows; k++) {
            TableItem item = new TableItem(sequences, SWT.NONE);
            item.setText(content[k]);
        }

        for (int i = 0; i < numOfCols; i++) {
            columns[i].pack();
        }

    }

    public ArrayList<Integer> getPlaySequence() {
        return play;
    }

    SelectionAdapter playSelection = new SelectionAdapter() {
        @Override
		public void widgetSelected(SelectionEvent e) {
            TableItem[] selectedTabItem = sequences.getSelection();
            setReturnCode(PLAY);
            play.add(Integer.parseInt(selectedTabItem[0].getText(0)));
            String items = selectedTabItem[0].getText(3);
            int index = items.indexOf(",");
            while (index != -1) {
                play.add(Integer.parseInt(items.substring(0, index)));
                items = items.substring(index + 1, items.length());
                index = items.indexOf(",");
            }

            play.add(Integer.parseInt(items));
            close();
        }
    };

    public int getSelectedStrategy() {
        return selectedStrategy;
    }

    public void setBounds(int min, int max) {
        this.min = min;
        this.max = max;
    }

}