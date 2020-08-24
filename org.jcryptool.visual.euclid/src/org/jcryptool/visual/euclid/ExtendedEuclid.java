package org.jcryptool.visual.euclid;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.core.util.ui.TitleAndDescriptionComposite;

public class ExtendedEuclid extends Composite {
	
	private View parentView;
	private Group grpInput_2;
	private Label lblP_2;
	private Label lblQ_2;
	private Text textP_2;
	private Text textQ_2;
	private Button btnNextStep_2;
	private Button btnPreviousStep_2;
	private Button btnCompute_2;
	private Button btnResetTable_2;
	private Button btnResetAll_2;
	private Group grpComputation_2;
	private StyledText styledText;
	private Table table;
	private TableColumn tblclmnIndex;
	private TableColumn tblclmnQuotient;
	private TableColumn tblclmnRest;
	private TableColumn tblclmnX;
	private TableColumn tblclmnY;
	
	private static final Color BLACK = ColorService.BLACK;
	private static final Color GREEN = ColorService.GREEN;
	private static final Color RED = ColorService.RED;
	private static final Color BLUE = ColorService.BLUE;
	private static final Color MAGENTA = ColorService.getColor(SWT.COLOR_MAGENTA);
	
	private int state;

	public ExtendedEuclid(Composite parent, int style, View parentView) {
		super(parent, style);
		this.parentView = parentView;

		setLayout(new GridLayout(5, false));
		
		TitleAndDescriptionComposite titleAndDescriptionComposite2 = new TitleAndDescriptionComposite(this);
		GridData gd_titleAndDescriptionComposite2 = new GridData(SWT.FILL, SWT.FILL, true, false, 5, 1);
		gd_titleAndDescriptionComposite2.widthHint = 600;
		titleAndDescriptionComposite2.setLayoutData(gd_titleAndDescriptionComposite2);
		titleAndDescriptionComposite2.setTitle(Messages.Euclid_XEuclidean);
		titleAndDescriptionComposite2.setDescription(Messages.Euclid_Description_2);

		grpInput_2 = new Group(this, SWT.NONE);
		grpInput_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		grpInput_2.setText(Messages.Euclid_Input);
		grpInput_2.setLayout(new GridLayout(5, false));

		lblP_2 = new Label(grpInput_2, SWT.NONE);
		lblP_2.setText(Messages.Euclid_P);

		textP_2 = new Text(grpInput_2, SWT.BORDER);
		textP_2.setText("44");
		textP_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		textP_2.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				if (e.text.matches("[0-9]*") || e.keyCode == SWT.BS || e.keyCode == SWT.DEL) {
					if (textP_2.getText().length() == 0 && e.text.compareTo("0") == 0) {
						e.doit = false;
					} else if (textP_2.getSelection().x == 0 && e.keyCode == 48) {
						e.doit = false;
					} else {
						e.doit = true;
					}
				} else {
					e.doit = false;
				}
			}
		});

		lblQ_2 = new Label(grpInput_2, SWT.NONE);
		lblQ_2.setText(Messages.Euclid_Q);

		textQ_2 = new Text(grpInput_2, SWT.BORDER);
		textQ_2.setText("18");
		textQ_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		textQ_2.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				if (e.text.matches("[0-9]*") || e.keyCode == SWT.BS || e.keyCode == SWT.DEL) {
					if (textQ_2.getText().length() == 0 && e.text.compareTo("0") == 0) {
						e.doit = false;
					} else if (textQ_2.getSelection().x == 0 && e.keyCode == 48) {
						e.doit = false;
					} else {
						e.doit = true;
					}
				} else {
					e.doit = false;
				}
			}
		});

		btnNextStep_2 = new Button(this, SWT.NONE);
		btnNextStep_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				nextStep_2();
			}
		});
		btnNextStep_2.setText(Messages.Euclid_NextStep_Button);

		btnPreviousStep_2 = new Button(this, SWT.NONE);
		btnPreviousStep_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				prevStep_2();
			}
		});
		btnPreviousStep_2.setEnabled(false);
		btnPreviousStep_2.setText(Messages.Euclid_PrevStep_Button);

		btnCompute_2 = new Button(this, SWT.NONE);
		btnCompute_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				compute_2();
			}
		});
		btnCompute_2.setText(Messages.Euclid_Compute_Button);

		btnResetTable_2 = new Button(this, SWT.NONE);
		btnResetTable_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				resetTable_2();
				btnNextStep_2.setEnabled(true);
				btnPreviousStep_2.setEnabled(false);
				btnCompute_2.setEnabled(true);
				btnResetTable_2.setEnabled(false);
				
				parentView.disableSafeButtons();
			}
		});
		btnResetTable_2.setText(Messages.Euclid_ResetTable_Button);

		btnResetAll_2 = new Button(this, SWT.NONE);
		btnResetAll_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				resetTable_2();
				textP_2.setText("44");
				textQ_2.setText("18");
				btnNextStep_2.setEnabled(true);
				btnPreviousStep_2.setEnabled(false);
				btnCompute_2.setEnabled(true);
				btnResetTable_2.setEnabled(false);
				
				parentView.disableSafeButtons();
			}
		});
		btnResetAll_2.setText(Messages.Euclid_Reset_Button);

		grpComputation_2 = new Group(this, SWT.NONE);
		grpComputation_2.setLayout(new GridLayout(1, false));
		grpComputation_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));
		grpComputation_2.setText(Messages.Euclid_Computation);

		styledText = new StyledText(grpComputation_2, SWT.BORDER | SWT.SINGLE);
		styledText.setEditable(false);
		styledText.setText("");
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		table = new Table(grpComputation_2, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		tblclmnIndex = new TableColumn(table, SWT.NONE);
		tblclmnIndex.setWidth(100);
		tblclmnIndex.setText("Index");

		tblclmnQuotient = new TableColumn(table, SWT.NONE);
		tblclmnQuotient.setWidth(100);
		tblclmnQuotient.setText(Messages.Euclid_Quotient);

		tblclmnRest = new TableColumn(table, SWT.NONE);
		tblclmnRest.setWidth(100);
		tblclmnRest.setText(Messages.Euclid_Remainder);

		tblclmnX = new TableColumn(table, SWT.NONE);
		tblclmnX.setWidth(100);
		tblclmnX.setText("x");

		tblclmnY = new TableColumn(table, SWT.NONE);
		tblclmnY.setWidth(100);
		tblclmnY.setText("y");

	}
	
	private void initialize_2() {
		TableItem tableItem1 = new TableItem(table, SWT.BORDER);
		TableItem tableItem2 = new TableItem(table, SWT.BORDER);
		int p = Integer.parseInt(textP_2.getText());
		int q = Integer.parseInt(textQ_2.getText());
		if (p > q) {
			tableItem1.setText(0, "0");
			tableItem1.setText(2, "" + p);
			tableItem1.setText(3, "1");
			tableItem1.setText(4, "0");
			tableItem2.setText(0, "1");
			tableItem2.setText(2, "" + q);
			tableItem2.setText(3, "0");
			tableItem2.setText(4, "1");
		} else {
			tableItem1.setText(0, "0");
			tableItem1.setText(2, "" + q);
			tableItem1.setText(3, "1");
			tableItem1.setText(4, "0");
			tableItem2.setText(0, "1");
			tableItem2.setText(2, "" + p);
			tableItem2.setText(3, "0");
			tableItem2.setText(4, "1");
		}
		state = 0;
		btnNextStep_2.setEnabled(true);
		btnCompute_2.setEnabled(true);
		btnResetTable_2.setEnabled(true);
	}

	private void nextStep_2() {
		if (table.getItemCount() == 0) {
			initialize_2();
			return;
		}

		StyleRange stylerange_1 = new StyleRange();
		StyleRange stylerange_2 = new StyleRange();
		StyleRange stylerange_3 = new StyleRange();
		StyleRange stylerange_4 = new StyleRange();

		TableItem item0;
		if (table.getItemCount() < 3)
			item0 = table.getItem(table.getItemCount() - 1);
		else
			item0 = table.getItem(table.getItemCount() - 3);
		TableItem item1 = table.getItem(table.getItemCount() - 2);
		TableItem item2 = table.getItem(table.getItemCount() - 1);

		clearTblItems();

		switch (state) {
		case 0: // Quotient
			int q1 = Integer.parseInt(item1.getText(2));
			int q2 = Integer.parseInt(item2.getText(2));
			int q = q1 / q2;
			item2.setText(1, "" + q);
			item1.setForeground(2, GREEN);
			item2.setForeground(2, RED);
			item2.setForeground(1, BLUE);

			styledText.setText(Messages.Euclid_Quotient + ": " + q1 + "/" + q2 + " = " + q);
			stylerange_1.start = (Messages.Euclid_Quotient + ": ").length();
			stylerange_1.length = ("" + q1).length();
			stylerange_1.foreground = GREEN;
			stylerange_1.fontStyle = SWT.BOLD;
			styledText.setStyleRange(stylerange_1);

			stylerange_2.start = (Messages.Euclid_Quotient + ": " + q1 + "/").length();
			stylerange_2.length = ("" + q2).length();
			stylerange_2.foreground = RED;
			stylerange_2.fontStyle = SWT.BOLD;
			styledText.setStyleRange(stylerange_2);

			stylerange_3.start = (Messages.Euclid_Quotient + ": " + q1 + "/" + q2 + " = ").length();
			stylerange_3.length = ("" + q).length();
			stylerange_3.foreground = BLUE;
			stylerange_3.fontStyle = SWT.BOLD;
			styledText.setStyleRange(stylerange_3);

			state = 1;
			break;
		case 1: // Remainder
			int r1 = Integer.parseInt(item1.getText(2));
			int r2 = Integer.parseInt(item2.getText(2));
			int r = r1 % r2;
			TableItem new_item = new TableItem(table, SWT.BORDER);
			new_item.setText(0, "" + (Integer.parseInt(item2.getText(0)) + 1));
			new_item.setText(2, "" + r);
			item1.setForeground(2, GREEN);
			item2.setForeground(2, RED);
			new_item.setForeground(2, BLUE);

			styledText.setText(Messages.Euclid_Remainder + ": " + r1 + "%" + r2 + " = " + r);
			stylerange_1.start = (Messages.Euclid_Remainder + ": ").length();
			stylerange_1.length = ("" + r1).length();
			stylerange_1.foreground = GREEN;
			stylerange_1.fontStyle = SWT.BOLD;
			styledText.setStyleRange(stylerange_1);

			stylerange_2.start = (Messages.Euclid_Remainder + ": " + r1 + "/").length();
			stylerange_2.length = ("" + r2).length();
			stylerange_2.foreground = RED;
			stylerange_2.fontStyle = SWT.BOLD;
			styledText.setStyleRange(stylerange_2);

			stylerange_3.start = (Messages.Euclid_Remainder + ": " + r1 + "/" + r2 + " = ").length();
			stylerange_3.length = ("" + r).length();
			stylerange_3.foreground = BLUE;
			stylerange_3.fontStyle = SWT.BOLD;
			styledText.setStyleRange(stylerange_3);

			state = 2;
			break;
		case 2: // X
			item0 = table.getItem(table.getItemCount() - 3);
			int x1 = Integer.parseInt(item0.getText(3));
			int x2 = Integer.parseInt(item1.getText(3));
			int q3 = Integer.parseInt(item1.getText(1));
			int x = x1 - q3 * Integer.parseInt(item1.getText(3));
			item2.setText(3, "" + x);
			item0.setForeground(3, GREEN);
			item1.setForeground(1, MAGENTA);
			item1.setForeground(3, RED);
			item2.setForeground(3, BLUE);

			String x1_text = "" + x1;
			if (x1 < 0)
				x1_text = "(" + x1 + ")";
			String x2_text = "" + x2;
			if (x2 < 0)
				x2_text = "(" + x2 + ")";

			styledText.setText("x: " + x1_text + "-" + q3 + "*" + x2_text + " = " + x);
			stylerange_1.start = ("x: ").length();
			stylerange_1.length = (x1_text).length();
			stylerange_1.foreground = GREEN;
			stylerange_1.fontStyle = SWT.BOLD;
			styledText.setStyleRange(stylerange_1);

			stylerange_2.start = ("x: " + x1_text + "-").length();
			stylerange_2.length = ("" + q3).length();
			stylerange_2.foreground = MAGENTA;
			stylerange_2.fontStyle = SWT.BOLD;
			styledText.setStyleRange(stylerange_2);

			stylerange_3.start = ("x: " + x1_text + "-" + q3 + "*").length();
			stylerange_3.length = (x2_text).length();
			stylerange_3.foreground = RED;
			stylerange_3.fontStyle = SWT.BOLD;
			styledText.setStyleRange(stylerange_3);

			stylerange_4.start = ("x: " + x1_text + "-" + q3 + "*" + x2_text + " = ").length();
			stylerange_4.length = ("" + x).length();
			stylerange_4.foreground = BLUE;
			stylerange_4.fontStyle = SWT.BOLD;
			styledText.setStyleRange(stylerange_4);

			state = 3;
			break;
		case 3: // Y
			item0 = table.getItem(table.getItemCount() - 3);
			int y1 = Integer.parseInt(item0.getText(4));
			int y2 = Integer.parseInt(item1.getText(4));
			int q4 = Integer.parseInt(item1.getText(1));
			int y = y1 - q4 * y2;
			item2.setText(4, "" + y);
			item0.setForeground(4, GREEN);
			item1.setForeground(1, MAGENTA);
			item1.setForeground(4, RED);
			item2.setForeground(4, BLUE);

			String y1_text = "" + y1;
			if (y1 < 0)
				y1_text = "(" + y1 + ")";
			String y2_text = "" + y2;
			if (y2 < 0)
				y2_text = "(" + y2 + ")";

			styledText.setText("y: " + y1_text + "-" + q4 + "*" + y2_text + " = " + y);
			stylerange_1.start = ("y: ").length();
			stylerange_1.length = (y1_text).length();
			stylerange_1.foreground = GREEN;
			stylerange_1.fontStyle = SWT.BOLD;
			styledText.setStyleRange(stylerange_1);

			stylerange_2.start = ("y: " + y1_text + "-").length();
			stylerange_2.length = ("" + q4).length();
			stylerange_2.foreground = MAGENTA;
			stylerange_2.fontStyle = SWT.BOLD;
			styledText.setStyleRange(stylerange_2);

			stylerange_3.start = ("y: " + y1_text + "-" + q4 + "*").length();
			stylerange_3.length = (y2_text).length();
			stylerange_3.foreground = RED;
			stylerange_3.fontStyle = SWT.BOLD;
			styledText.setStyleRange(stylerange_3);

			stylerange_4.start = ("y: " + y1_text + "-" + q4 + "*" + y2_text + " = ").length();
			stylerange_4.length = ("" + y).length();
			stylerange_4.foreground = BLUE;
			stylerange_4.fontStyle = SWT.BOLD;
			styledText.setStyleRange(stylerange_4);

			if (item2.getText(2).equals("0"))
				state = 4;
			else
				state = 0;
			break;
		case 4: // Finished
			item1.setForeground(2, BLUE);
			item1.setForeground(3, GREEN);
			item1.setForeground(4, RED);

			String a = table.getItem(0).getText(2);
			String b = table.getItem(1).getText(2);
			r = Integer.parseInt(item1.getText(2));
			x = Integer.parseInt(item1.getText(3));
			String x_text = "" + x;
			if (x < 0)
				x_text = "(" + x + ")";
			y = Integer.parseInt(item1.getText(4));
			String y_text = "" + y;
			if (y < 0)
				y_text = "(" + y + ")";

			styledText.setText(Messages.Euclid_GCD + a + "," + b + ") = " + x_text + "*" + a + " + " + y_text + "*" + b
					+ " = " + r);
			StyleRange stylerange_x = new StyleRange();
			stylerange_x.start = (Messages.Euclid_GCD + a + "," + b + ") = ").length();
			stylerange_x.length = x_text.length();
			stylerange_x.foreground = GREEN;
			stylerange_x.fontStyle = SWT.BOLD;
			styledText.setStyleRange(stylerange_x);

			StyleRange stylerange_y = new StyleRange();
			stylerange_y.start = (Messages.Euclid_GCD + a + "," + b + ") = " + x_text + "*" + a + " + ").length();
			stylerange_y.length = y_text.length();
			stylerange_y.foreground = RED;
			stylerange_y.fontStyle = SWT.BOLD;
			styledText.setStyleRange(stylerange_y);

			StyleRange stylerange_r = new StyleRange();
			stylerange_r.start = (Messages.Euclid_GCD + a + "," + b + ") = " + x_text + "*" + a + " + " + y_text + "*"
					+ b + " = ").length();
			stylerange_r.length = ("" + r).length();
			stylerange_r.foreground = BLUE;
			stylerange_r.fontStyle = SWT.BOLD;
			styledText.setStyleRange(stylerange_r);

			state = 5;

			btnNextStep_2.setEnabled(false);
			btnCompute_2.setEnabled(false);
			
			parentView.enableSafeButtons();
			break;
		}
		btnPreviousStep_2.setEnabled(true);
		btnResetTable_2.setEnabled(true);
	}

	private void prevStep_2() {
		TableItem item1 = table.getItem(table.getItemCount() - 1);

		clearTblItems();

		switch (state) {
		case 0: // Remove Y
			item1.setText(4, "");
			state = 2;
			nextStep_2();
			break;
		case 1: // Remove Quotient
			item1.setText(1, "");
			if (table.getItemCount() == 2) {
				styledText.setText("");
				state = 0;
				btnPreviousStep_2.setEnabled(false);
			} else {
				state = 3;
				nextStep_2();
			}
			break;
		case 2: // Remove Remainder
			table.remove(table.getItemCount() - 1);
			state = 0;
			nextStep_2();
			break;
		case 3: // Remove X
			table.remove(table.getItemCount() - 1);
			state = 1;
			nextStep_2();
			break;
		case 4:
			item1.setText(4, "");
			state = 2;
			nextStep_2();
			break;
		case 5:
			state = 3;
			nextStep_2();
			break;
		}
		btnNextStep_2.setEnabled(true);
		btnCompute_2.setEnabled(true);
		
		parentView.disableSafeButtons();
	}

	private void compute_2() {
		resetTable_2();
		initialize_2();
		TableItem item;
		item = table.getItem(table.getItemCount() - 2);
		int r1 = Integer.parseInt(item.getText(2));
		int x1 = Integer.parseInt(item.getText(3));
		int y1 = Integer.parseInt(item.getText(4));
		item = table.getItem(table.getItemCount() - 1);
		int r2 = Integer.parseInt(item.getText(2));
		int x2 = Integer.parseInt(item.getText(3));
		int y2 = Integer.parseInt(item.getText(4));
		int q, tmp;
		while (true) {
			// Quotient
			q = r1 / r2;
			item.setText(1, "" + q);
			// Remainder
			tmp = r1 % r2;
			r1 = r2;
			r2 = tmp;
			item = new TableItem(table, SWT.BORDER);
			item.setText(0, "" + (Integer.parseInt(table.getItem(table.getItemCount() - 2).getText(0)) + 1));
			item.setText(2, "" + tmp);
			// X
			tmp = x1 - q * x2;
			x1 = x2;
			x2 = tmp;
			item.setText(3, "" + x2);
			// Y
			tmp = y1 - q * y2;
			y1 = y2;
			y2 = tmp;
			item.setText(4, "" + y2);
			if (r2 == 0)
				break;
		}

		item = table.getItem(table.getItemCount() - 2);
		item.setForeground(2, BLUE);
		item.setForeground(3, GREEN);
		item.setForeground(4, RED);

		String a = table.getItem(0).getText(2);
		String b = table.getItem(1).getText(2);
		String r = "" + r1;
		String x = "" + x1;
		if (x1 < 0)
			x = "(" + x + ")";
		String y = "" + y1;
		if (y1 < 0)
			y = "(" + y + ")";

		styledText.setText(Messages.Euclid_GCD + a + "," + b + ") = " + x + "*" + a + " + " + y + "*" + b + " = " + r);
		StyleRange stylerange_x = new StyleRange();
		stylerange_x.start = (Messages.Euclid_GCD + a + "," + b + ") = ").length();
		stylerange_x.length = x.length();
		stylerange_x.foreground = GREEN;
		stylerange_x.fontStyle = SWT.BOLD;
		styledText.setStyleRange(stylerange_x);

		StyleRange stylerange_y = new StyleRange();
		stylerange_y.start = (Messages.Euclid_GCD + a + "," + b + ") = " + x + "*" + a + " + ").length();
		stylerange_y.length = y.length();
		stylerange_y.foreground = RED;
		stylerange_y.fontStyle = SWT.BOLD;
		styledText.setStyleRange(stylerange_y);

		StyleRange stylerange_r = new StyleRange();
		stylerange_r.start = (Messages.Euclid_GCD + a + "," + b + ") = " + x + "*" + a + " + " + y + "*" + b + " = ")
				.length();
		stylerange_r.length = r.length();
		stylerange_r.foreground = BLUE;
		stylerange_r.fontStyle = SWT.BOLD;
		styledText.setStyleRange(stylerange_r);

		btnNextStep_2.setEnabled(false);
		btnPreviousStep_2.setEnabled(true);
		btnCompute_2.setEnabled(false);
		btnResetTable_2.setEnabled(true);
		
		parentView.enableSafeButtons();
		state = 5;
	}

	private void resetTable_2() {
		styledText.setText("");
		table.removeAll();
		table.setItemCount(0);
	}

	private void clearTblItems() {
		for (int i = 1; i <= table.getItemCount(); i++)
			for (int j = 0; j < 5; j++)
				table.getItem(table.getItemCount() - i).setForeground(j, BLACK);
	}

	private String[] exportArray() {
		String[] array = new String[table.getItemCount() * 5];
		TableItem item;
		for (int i = 0; i < table.getItemCount(); i++) {
			item = table.getItem(i);
			for (int j = 0; j < 5; j++)
				array[i * 5 + j] = item.getText(j);
		}

		return array;
	}
	
	public void completeReset() {
		resetTable_2();
		textP_2.setText("44");
		textQ_2.setText("18");
		btnNextStep_2.setEnabled(true);
		btnPreviousStep_2.setEnabled(false);
		btnCompute_2.setEnabled(true);
		btnResetTable_2.setEnabled(false);
	}
	
	public int getState() {
		return state;
	}
	
	/**
	 * This function replaces the exportToCSVAction.
	 */
	public void exportToCSV() {
		FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
		dialog.setFilterPath(DirectoryService.getUserHomeDir());
		dialog.setFileName(Messages.Euclid_SaveDialog + textP_2.getText() + "_" + textQ_2.getText() + ".csv");
		dialog.setFilterExtensions(new String[] { IConstants.CSV_FILTER_EXTENSION });
		dialog.setFilterNames(new String[] { IConstants.CSV_FILTER_NAME });
		dialog.setOverwrite(true);

		String filename = dialog.open();

		if (filename != null) {
			FileExporter csvExport = new FileExporter(exportArray(), filename);
			csvExport.exportToCSV();
		}
	}

	/**
	 * This method replaces exportToLatexAction
	 */
	public void exportToTex() {
		FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
		dialog.setFilterPath(DirectoryService.getUserHomeDir());
		dialog.setFileName(Messages.Euclid_SaveDialog + textP_2.getText() + "_" + textQ_2.getText() + ".tex");
		dialog.setFilterExtensions(new String[] { IConstants.TEX_FILTER_EXTENSION });
		dialog.setFilterNames(new String[] { IConstants.TEX_FILTER_NAME });
		dialog.setOverwrite(true);

		String filename = dialog.open();

		if (filename != null) {
			FileExporter latexExport = new FileExporter(exportArray(), filename);
			latexExport.exportToLatex();
		}
	}

}
