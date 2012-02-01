// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.numbershark.optStrat;

import java.io.FileWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.games.numbershark.NumberSharkPlugin;
import org.jcryptool.games.numbershark.util.CSVConverter;

/**
 * Result dialog for the calculation of the optimal strategies
 * @author Johannes Spaeth
 * @version 0.9.5
 */
public class ResultDialog extends TitleAreaDialog {
	private int min = 2;
	private int max = 100;
	private int stoppedAt = 98;
	public static final int SAVE = 9999;
	public static final int PLAY = 9998;
	private Table sequences;
	private ArrayList<Integer> play = new ArrayList<Integer>();

	private boolean calculate = true;
	private Button playSelected;

	public ResultDialog(Shell shell, boolean calculate) {
		super(shell);
		this.calculate = calculate;
		setShellStyle(SWT.TITLE | SWT.APPLICATION_MODAL);
	}

	protected Control createDialogArea(Composite parent) {
		CalculationThread calculateSequences = new CalculationThread(min, max);
		final Shell shell = parent.getShell();
		if (calculate) {
			try {
				new ProgressMonitorDialog(shell).run(true, true,calculateSequences);

			} catch (InvocationTargetException e) {
			} catch (InterruptedException e) {
			}
			tableContent = calculateSequences.getSharkOutput();
			stoppedAt = calculateSequences.getStoppedAt();
		}

		setTitle(Messages.ShowOptStrategy_1);
		setMessage(Messages.ShowOptStrategy_2, IMessageProvider.INFORMATION);
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new GridLayout(1, false));
		createTable(area);

		PlatformUI
				.getWorkbench()
				.getHelpSystem()
				.setHelp(parent,
						NumberSharkPlugin.PLUGIN_ID + ".optStratResultDialog"); //$NON-NLS-1$

		return area;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(640, 500);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.ShowOptStrategy_0);
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		playSelected = createButton(parent, PLAY, Messages.ShowOptStrategy_9, true);
		playSelected.setEnabled(false);
		playSelected.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TableItem[] selectedTabItem = sequences.getSelection();
		        setReturnCode(PLAY);
		        play.add(Integer.parseInt(selectedTabItem[0].getText(0)));
		        String items = selectedTabItem[0].getText(3);
		        int index = items.indexOf(",");
				while(index != -1){
					play.add(Integer.parseInt(items.substring(0, index)));
					items = items.substring(index+1, items.length());
					index = items.indexOf(",");
				}
				
				play.add(Integer.parseInt(items));
				close();
			}
		});
		
		Button saveAsButton = createButton(parent, SAVE, Messages.ShowOptStrategy_8, true);
		saveAsButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FileDialog saveDialog = new FileDialog(Display.getCurrent()
						.getActiveShell(), SWT.SAVE);
				saveDialog.setFilterPath(DirectoryService.getUserHomeDir());
				saveDialog.setFilterNames(new String[] { "CSV-File",
						"All Files (*.*)" });
				saveDialog.setFilterExtensions(new String[] { "*.csv", "*.*" });
				saveDialog.setFileName("log_numberShark.csv");
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
				}
			}
		});
		
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.CLOSE_LABEL,
				true);

		
	}
	

	public void createTable(Composite parent) {
		sequences = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
		sequences.setHeaderVisible(true);
		GridData gd_sequences = new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1);
		gd_sequences.widthHint = 304;
		sequences.setLayoutData(gd_sequences);
		sequences.setLinesVisible(true);
		
		TableColumn[] columns = new TableColumn[5];

		for (int i = 0; i < 5; i++) {
			columns[i] = new TableColumn(sequences, SWT.NONE);
		}
		columns[0].setText(Messages.ShowOptStrategy_3);
		columns[1].setText(Messages.ShowOptStrategy_4);
		columns[2].setText(Messages.ShowOptStrategy_5);
		columns[3].setText(Messages.ShowOptStrategy_6);
		columns[4].setText(Messages.ShowOptStrategy_7);

		for (int k = min; k < stoppedAt + 1; k++) {
			TableItem item = new TableItem(sequences, SWT.NONE);
			item.setText(tableContent[k]);
		}

		for (int i = 0; i < 5; i++) {
			columns[i].pack();
		}
		
		sequences.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				if(sequences.getSelection().length > 0){
					playSelected.setEnabled(true);
				}
			}
		});
		
	}

	public void setBounds(int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	public ArrayList<Integer> getPlaySequence(){
		return play;
	}

	String[][] tableContent= {{},{},
			{"2","2","1","2","0ms"},{
			"3","3","3","3","0ms"},{
			"4","7","3","3,4","0ms"},{
			"5","9","6","5,4","0ms"},{
			"6","15","6","5,4,6","0ms"},{
			"7","17","11","7,4,6","0ms"},{
			"8","21","15","7,8,6","0ms"},{
			"9","30","15","7,9,6,8","0ms"},{
			"10","40","15","7,9,6,10,8","0ms"},{
			"11","44","22","11,9,6,10,8","0ms"},{
			"12","50","28","11,9,10,8,12","0ms"},{
			"13","52","39","13,9,10,8,12","0ms"},{
			"14","66","39","13,9,14,10,8,12","0ms"},{
			"15","81","39","13,9,15,10,14,8,12","0ms"},{
			"16","89","47","13,9,15,10,14,16,12","0ms"},{
			"17","93","60","17,9,15,10,14,16,12","0ms"},{
			"18","111","60","17,9,15,10,18,14,12,16","0ms"},{
			"19","113","77","19,9,15,10,18,14,12,16","0ms"},{
			"20","124","86","19,15,10,20,16,14,12,18","0ms"},{
			"21","144","87","19,9,21,15,14,18,12,20,16","0ms"},{
			"22","166","87","19,9,21,15,14,22,18,12,20,16","0ms"},{
			"23","170","106","23,9,21,15,14,22,18,12,20,16","0ms"},{
			"24","182","118","23,9,21,15,14,22,18,20,16,24","0ms"},{
			"25","198","127","23,25,15,21,14,22,20,16,24,18","0ms"},{
			"26","224","127","23,25,15,21,14,26,22,20,16,24,18","0ms"},{
			"27","251","127","23,25,15,27,21,14,26,22,18,20,16,24","0ms"},{
			"28","279","127","23,25,15,27,21,14,28,26,22,20,18,16,24","0ms"},{
			"29","285","150","29,25,15,27,21,14,28,26,22,20,18,16,24","0ms"},{
			"30","301","164","29,25,15,27,21,26,22,18,30,20,28,16,24","0ms"},{
			"31","303","193","31,25,15,27,21,26,22,18,30,20,28,16,24","0ms"},{
			"32","319","209","31,25,15,27,21,26,22,18,30,20,28,32,24","1ms"},{
			"33","352","209","31,25,15,33,27,22,26,21,18,30,20,28,32,24","0ms"},{
			"34","386","209","31,25,15,33,27,22,34,26,21,18,30,20,28,32,24","0ms"},{
			"35","418","212","31,25,35,21,33,27,22,34,26,18,12,28,24,32,20,30","0ms"},{
			"36","442","224","31,25,35,21,33,27,22,34,26,18,36,28,24,32,20,30","0ms"},{
			"37","448","255","37,25,35,21,33,27,22,34,26,18,36,28,24,32,20,30","0ms"},{
			"38","486","255","37,25,35,21,33,27,22,38,34,26,18,36,28,24,32,20,30","0ms"},{
			"39","503","277","37,25,35,21,39,33,27,26,38,34,18,36,28,24,32,20,30","0ms"},{
			"40","525","295","37,25,35,21,39,33,27,26,38,34,28,20,40,32,30,24,36","0ms"},{
			"41","529","332","41,25,35,21,39,33,27,26,38,34,28,20,40,32,30,24,36","0ms"},{
			"42","571","332","41,25,35,21,39,33,27,26,38,34,42,28,20,40,32,30,24,36","0ms"},{
			"43","573","373","43,25,35,21,39,33,27,26,38,34,42,28,20,40,32,30,24,36","0ms"},{
			"44","617","373","43,25,35,21,39,33,27,26,38,34,44,28,42,20,40,32,30,24,36","0ms"},{
			"45","660","375","43,25,35,21,39,33,27,45,26,38,34,18,42,30,28,44,36,24,40,32","1ms"},{
			"46","706","375","43,25,35,21,39,33,27,45,26,46,38,34,18,42,30,28,44,36,24,40,32","0ms"},{
			"47","710","418","47,25,35,21,39,33,27,45,26,46,38,34,18,42,30,28,44,36,24,40,32","0ms"},{
			"48","734","442","47,25,35,21,39,33,27,45,26,46,38,34,18,42,30,28,44,36,40,32,48","0ms"},{
			"49","758","467","47,49,35,21,39,33,27,45,26,46,38,34,18,42,30,28,44,36,40,32,48","0ms"},{
			"50","808","467","47,49,35,21,39,33,27,45,26,46,38,34,18,42,30,50,28,44,36,40,32,48","0ms"},{
			"51","833","493","47,49,35,21,51,39,34,46,38,33,27,45,18,42,30,50,28,44,36,40,32,48","0ms"},{
			"52","885","493","47,49,35,21,51,39,34,46,38,33,27,45,18,42,30,50,28,52,44,36,40,32,48","0ms"},{
			"53","891","540","53,49,35,21,51,39,34,46,38,33,27,45,18,42,30,50,28,52,44,36,40,32,48","0ms"},{
			"54","940","545","53,49,35,14,46,38,34,51,39,33,28,52,44,27,45,20,50,40,32,30,54,42,36,48","1ms"},{
			"55","981","559","53,49,35,55,33,51,39,34,46,38,27,45,54,30,50,20,52,44,40,36,32,48,28,42","0ms"},{
			"56","1017","579","53,49,35,55,33,51,39,34,46,38,27,45,54,30,50,52,44,36,28,56,42,40,32,48","0ms"},{
			"57","1040","613","53,49,35,55,33,57,51,39,38,46,27,45,54,30,50,52,44,36,28,56,42,40,32,48","0ms"},{
			"58","1098","613","53,49,35,55,33,57,51,39,38,58,46,27,45,54,30,50,52,44,36,28,56,42,40,32,48","0ms"},{
			"59","1104","666","59,49,35,55,33,57,51,39,38,58,46,27,45,54,30,50,52,44,36,28,56,42,40,32,48","0ms"},{
			"60","1137","693","59,49,35,55,33,57,51,39,38,58,46,52,44,28,56,32,50,40,45,30,60,48,42,36,54","1ms"},{
			"61","1139","752","61,49,35,55,33,57,51,39,38,58,46,52,44,28,56,32,50,40,45,30,60,48,42,36,54","0ms"},{
			"62","1201","752","61,49,35,55,33,57,51,39,38,62,58,46,52,44,28,56,32,50,40,45,30,60,48,42,36,54","0ms"},{
			"63","1264","752","61,49,35,55,33,57,51,39,38,62,58,46,63,45,52,44,28,56,42,32,30,50,40,60,48,36,54","0ms"},{
			"64","1296","784","61,49,35,55,33,57,51,39,38,62,58,46,63,45,52,44,28,56,42,30,50,40,60,36,54,64,48","1ms"},{
			"65","1328","817","61,49,35,65,55,39,57,51,38,62,58,46,63,45,52,44,28,56,42,30,50,40,60,36,54,64,48","0ms"},{
			"66","1394","817","61,49,35,65,55,39,57,51,38,62,58,46,63,45,52,44,28,56,42,66,30,50,40,60,36,54,64,48","0ms"},{
			"67","1400","878","67,49,35,65,55,39,57,51,38,62,58,46,63,45,52,44,28,56,42,66,30,50,40,60,36,54,64,48","1ms"},{
			"68","1468","878","67,49,35,65,55,39,57,51,38,62,58,46,68,52,44,28,56,66,42,63,45,30,50,40,60,36,54,64,48","0ms"},{
			"69","1499","916","67,49,35,65,55,39,69,57,51,46,62,58,68,52,44,28,56,66,42,63,45,30,50,40,60,36,54,64,48","0ms"},{
			"70","1566","919","67,49,35,65,55,39,69,57,51,46,62,58,27,63,45,70,50,42,54,28,68,56,52,44,66,40,36,60,64,48","1ms"},{
			"71","1570","986","71,49,35,65,55,39,69,57,51,46,62,58,27,63,45,70,50,42,54,28,68,56,52,44,66,40,36,60,64,48","0ms"},{
			"72","1642","986","71,49,35,65,55,39,69,57,51,46,62,58,27,63,45,70,50,42,54,28,68,56,52,44,66,40,36,72,60,48,64","0ms"},{
			"73","1644","1057","73,49,35,65,55,39,69,57,51,46,62,58,27,63,45,70,50,42,54,28,68,56,52,44,66,40,36,72,60,48,64","0ms"},{
			"74","1718","1057","73,49,35,65,55,39,69,57,51,46,74,62,58,27,63,45,70,50,42,54,28,68,56,52,44,66,40,36,72,60,48,64","0ms"},{
			"75","1793","1057","73,49,35,65,55,39,69,57,51,46,74,62,58,27,63,45,75,50,70,42,54,28,68,56,52,44,66,40,36,72,60,48,64","1ms"},{
			"76","1869","1057","73,49,35,65,55,39,69,57,51,46,74,62,58,27,63,45,75,50,70,42,54,28,76,68,56,52,44,66,40,36,72,60,48,64","0ms"},{
			"77","1914","1089","73,49,77,55,65,39,69,57,51,46,74,62,58,75,50,45,63,30,42,70,28,76,68,56,52,44,66,40,60,36,72,54,48,64","0ms"},{
			"78","1991","1090","73,49,77,55,65,39,69,57,51,46,74,62,58,27,63,45,75,50,30,78,54,52,76,68,44,66,42,70,36,60,40,72,56,48,64","0ms"},{
			"79","1997","1163","79,49,77,55,65,39,69,57,51,46,74,62,58,27,63,45,75,50,30,78,54,52,76,68,44,66,42,70,36,60,40,72,56,48,64","1ms"},{
			"80","2041","1199","79,49,77,55,65,39,69,57,51,46,74,62,58,27,63,45,75,50,30,78,54,52,76,68,44,66,42,70,60,40,80,64,56,48,72","0ms"},{
			"81","2105","1216","79,49,77,55,65,21,69,63,81,57,51,46,74,62,58,45,75,50,30,54,42,70,28,76,68,56,52,78,44,66,40,80,64,60,48,72","1ms"},{
			"82","2187","1216","79,49,77,55,65,21,69,63,81,57,51,46,82,74,62,58,45,75,50,30,54,42,70,28,76,68,56,52,78,44,66,40,80,64,60,48,72","0ms"},{
			"83","2191","1295","83,49,77,55,65,21,69,63,81,57,51,46,82,74,62,58,45,75,50,30,54,42,70,28,76,68,56,52,78,44,66,40,80,64,60,48,72","0ms"},{
			"84","2263","1307","83,49,77,55,65,39,69,57,51,46,82,74,62,58,78,52,76,68,44,66,28,56,42,84,63,81,54,45,75,50,70,40,80,64,60,48,72","1ms"},{
			"85","2309","1346","83,49,77,55,85,65,51,69,57,46,82,74,62,58,81,63,45,75,50,76,68,52,44,28,70,56,42,84,78,66,54,40,80,64,60,48,72","1ms"},{
			"86","2395","1346","83,49,77,55,85,65,51,69,57,46,86,82,74,62,58,81,63,45,75,50,76,68,52,44,28,70,56,42,84,78,66,54,40,80,64,60,48,72","0ms"},{
			"87","2436","1392","83,49,77,55,85,65,51,87,69,58,86,82,74,62,57,81,63,45,75,50,76,68,52,44,28,70,56,42,84,78,66,54,40,80,64,60,48,72","0ms"},{
			"88","2496","1420","83,49,77,55,85,65,51,87,69,58,86,82,74,62,57,81,63,45,75,50,76,68,52,44,88,40,80,64,78,66,54,42,70,56,84,60,48,72","2ms"},{
			"89","2502","1503","89,49,77,55,85,65,51,87,69,58,86,82,74,62,57,81,63,45,75,50,76,68,52,44,88,40,80,64,78,66,54,42,70,56,84,60,48,72","0ms"},{
			"90","2552","1543","89,49,77,55,85,65,51,87,69,58,86,82,74,62,57,81,63,45,75,50,76,68,52,44,88,78,66,54,90,42,70,56,84,60,80,64,48,72","4ms"},{
			"91","2588","1598","89,49,91,77,65,85,51,87,69,58,86,82,74,62,57,81,63,45,75,50,76,68,52,44,88,78,66,54,90,42,70,56,84,60,80,64,48,72","0ms"},{
			"92","2680","1598","89,49,91,77,65,85,51,87,69,58,86,82,74,62,57,92,76,68,52,44,88,81,63,45,75,50,78,66,54,90,42,70,56,84,60,80,64,48,72","0ms"},{
			"93","2715","1656","89,49,91,77,65,85,51,93,87,69,62,86,82,74,57,92,76,68,52,44,88,81,63,45,75,50,78,66,54,90,42,70,56,84,60,80,64,48,72","0ms"},{
			"94","2809","1656","89,49,91,77,65,85,51,93,87,69,62,94,86,82,74,57,92,76,68,52,44,88,81,63,45,75,50,78,66,54,90,42,70,56,84,60,80,64,48,72","1ms"},{
			"95","2853","1707","89,49,91,77,65,95,85,57,93,87,69,62,94,86,82,74,92,76,68,52,44,88,81,63,45,75,50,78,66,54,90,42,70,56,84,60,80,64,48,72","0ms"},{
			"96","2901","1755","89,49,91,77,65,95,85,57,93,87,69,62,94,86,82,74,92,76,68,52,44,88,81,63,45,75,50,78,66,54,90,42,70,56,84,60,80,64,96,72","8ms"},{
			"97","2909","1844","97,49,91,77,65,95,85,57,93,87,69,62,94,86,82,74,92,76,68,52,44,88,81,63,45,75,50,78,66,54,90,42,70,56,84,60,80,64,96,72","0ms"},{
			"98","3007","1844","97,49,91,77,65,95,85,57,93,87,69,62,98,94,86,82,74,92,76,68,52,44,88,56,81,63,45,75,50,70,42,84,78,66,54,90,60,80,64,96,72","4ms"},{
			"99","3106","1844","97,49,91,77,65,95,85,57,93,87,69,62,98,94,86,82,74,99,81,63,45,75,50,70,42,66,54,90,44,92,88,76,68,56,84,60,52,78,80,64,96,72","1ms"},{
			"100","3164","1886","97,49,91,77,65,95,85,57,93,87,69,62,98,94,86,82,74,99,81,63,45,75,50,70,100,92,76,68,52,44,88,66,78,56,54,90,60,84,80,64,96,72","7ms"}};
}