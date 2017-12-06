package org.jcryptool.visual.huffmanCoding.views;

import java.text.Collator;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.huffmanCoding.algorithm.BitString;
import org.jcryptool.visual.huffmanCoding.algorithm.Node;

/**
 * @author Miray Inel
 * @author <i>revised by</i>
 * @author Michael Altenhuber
 * 
 */
public class HuffmanCodingViewTable extends Composite {

	private StyledText styledTextCodetable;
	private Button btnShowBranch;
	private Table table;
	private boolean tblclmnCharacterMode = false;
	private boolean tblclmnProbabilityMode = false;
	private boolean tblclmnCodeMode = false;
	private boolean tblclmnCodeLengthMode = false;
	private BitString[] bitStrings;
	private Node[] nodes;
	private int mode;

	public HuffmanCodingViewTable(Composite parent, int style, HuffmanCodingView mainView) {
		super(parent, style);
		this.setLayout(new GridLayout(2, false));

		nodes = mainView.getCompressedNodes();
		bitStrings = mainView.getBitStringTable();
		mode = mainView.getMode();

		styledTextCodetable = new StyledText(this, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		styledTextCodetable.setText(Messages.HuffmanCodingView_29);
		styledTextCodetable.setFont(FontService.getNormalFont());
		styledTextCodetable.setEditable(false);
		GridData gd_styledTextCodetable = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_styledTextCodetable.heightHint = 90;
		styledTextCodetable.setLayoutData(gd_styledTextCodetable);

		btnShowBranch = new Button(this, SWT.NONE);
		btnShowBranch.setEnabled(false);
		btnShowBranch.setText(Messages.HuffmanCodingView_15);
		btnShowBranch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				int selection = table.getSelectionIndex();

				TableItem tmpItem = table.getItem(selection);
				String code;
				if (mode == HuffmanCodingView.COMPRESS) {
					code = tmpItem.getText(2);
				} else {
					code = tmpItem.getText(1);
				}
				mainView.setAndHiglightGraph(code);
			}
		});

		GridData gd_btnShowBranch = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnShowBranch.heightHint = 40;
		gd_btnShowBranch.widthHint = 120;
		btnShowBranch.setLayoutData(gd_btnShowBranch);

		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnShowBranch.setEnabled(true);
			}

		});

		final TableColumn tblclmnCharacter = new TableColumn(table, SWT.NONE);
		tblclmnCharacter.setWidth(100);
		tblclmnCharacter.setText(Messages.HuffmanCodingView_tblclmnCharacter_text);
		tblclmnCharacter.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				TableItem[] items = table.getItems();
				Collator collator = Collator.getInstance(Locale.getDefault());
				for (int i = 1; i < items.length; i++) {
					String value1 = items[i].getText(0);
					for (int j = 0; j < i; j++) {
						String value2 = items[j].getText(0);
						if (tblclmnCharacterMode) {
							if (collator.compare(value1, value2) < 0) {

								String[] values = { items[i].getText(0), items[i].getText(1), items[i].getText(2),
										items[i].getText(3) };
								items[i].dispose();
								TableItem item = new TableItem(table, SWT.NONE, j);
								item.setText(values);
								items = table.getItems();
								break;
							}
						} else {
							if (collator.compare(value1, value2) > 0) {
								String[] values = { items[i].getText(0), items[i].getText(1), items[i].getText(2),
										items[i].getText(3) };
								items[i].dispose();
								TableItem item = new TableItem(table, SWT.NONE, j);
								item.setText(values);
								items = table.getItems();
								break;
							}
						}
					}
				}
				if (tblclmnCharacterMode) {
					tblclmnCharacterMode = false;
				} else {
					tblclmnCharacterMode = true;
				}
				btnShowBranch.setEnabled(false);
			}
		});

		TableColumn tblclmnProbability = new TableColumn(table, SWT.NONE);
		tblclmnProbability.setWidth(140);
		tblclmnProbability.setText(Messages.HuffmanCodingView_tblclmnPropability_text);
		tblclmnProbability.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {
				TableItem[] items = table.getItems();
				Collator collator = Collator.getInstance(Locale.getDefault());
				for (int i = 1; i < items.length; i++) {
					String value1 = items[i].getText(1);
					for (int j = 0; j < i; j++) {
						String value2 = items[j].getText(1);
						if (tblclmnProbabilityMode) {
							if (collator.compare(value1, value2) < 0) {
								String[] values = { items[i].getText(0), items[i].getText(1), items[i].getText(2),
										items[i].getText(3) };
								items[i].dispose();
								TableItem item = new TableItem(table, SWT.NONE, j);
								item.setText(values);
								items = table.getItems();
								break;
							}
						} else {
							if (collator.compare(value1, value2) > 0) {
								String[] values = { items[i].getText(0), items[i].getText(1), items[i].getText(2),
										items[i].getText(3) };
								items[i].dispose();
								TableItem item = new TableItem(table, SWT.NONE, j);
								item.setText(values);
								items = table.getItems();
								break;
							}
						}
					}
				}
				if (tblclmnProbabilityMode) {
					tblclmnProbabilityMode = false;
				} else {
					tblclmnProbabilityMode = true;
				}
				btnShowBranch.setEnabled(false);
			}
		});

		TableColumn tblclmnCode = new TableColumn(table, SWT.NONE);
		tblclmnCode.setWidth(140);
		tblclmnCode.setText(Messages.HuffmanCodingView_tblclmnCode_text);
		tblclmnCode.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				TableItem[] items = table.getItems();
				Collator collator = Collator.getInstance(Locale.getDefault());
				for (int i = 1; i < items.length; i++) {
					String value1 = items[i].getText(2);
					for (int j = 0; j < i; j++) {
						String value2 = items[j].getText(2);
						if (tblclmnCodeMode) {
							if (collator.compare(value1, value2) < 0) {
								String[] values = { items[i].getText(0), items[i].getText(1), items[i].getText(2),
										items[i].getText(3) };
								items[i].dispose();
								TableItem item = new TableItem(table, SWT.NONE, j);
								item.setText(values);
								items = table.getItems();
								break;
							}
						} else {
							if (collator.compare(value1, value2) > 0) {
								String[] values = { items[i].getText(0), items[i].getText(1), items[i].getText(2),
										items[i].getText(3) };
								items[i].dispose();
								TableItem item = new TableItem(table, SWT.NONE, j);
								item.setText(values);
								items = table.getItems();
								break;
							}
						}
					}
				}
				if (tblclmnCodeMode) {
					tblclmnCodeMode = false;
				} else {
					tblclmnCodeMode = true;
				}
				btnShowBranch.setEnabled(false);
			}
		});

		TableColumn tblclmnCodeLength = new TableColumn(table, SWT.NONE);
		tblclmnCodeLength.setWidth(100);
		tblclmnCodeLength.setText(Messages.HuffmanCodingView_tblclmnCodeLength_text);
		tblclmnCodeLength.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				TableItem[] items = table.getItems();
				for (int i = 1; i < items.length; i++) {
					int value1 = Integer.parseInt(items[i].getText(3));
					for (int j = 0; j < i; j++) {
						int value2 = Integer.parseInt(items[j].getText(3));
						if (tblclmnCodeLengthMode) {
							if (value1 > value2) {
								String[] values = { items[i].getText(0), items[i].getText(1), items[i].getText(2),
										items[i].getText(3) };
								items[i].dispose();
								TableItem item = new TableItem(table, SWT.NONE, j);
								item.setText(values);
								items = table.getItems();
								break;
							}
						} else {
							if (value1 < value2) {
								String[] values = { items[i].getText(0), items[i].getText(1), items[i].getText(2),
										items[i].getText(3) };
								items[i].dispose();
								TableItem item = new TableItem(table, SWT.NONE, j);
								item.setText(values);
								items = table.getItems();
								break;
							}
						}
					}
				}
				if (tblclmnCodeLengthMode) {
					tblclmnCodeLengthMode = false;
				} else {
					tblclmnCodeLengthMode = true;
				}
				btnShowBranch.setEnabled(false);
			}
		});

		int counter = 0;
		double avarageCodelength = 0.0;
		int maxCodelenght = bitStrings[0].getLength();
		int minCodelenght = bitStrings[0].getLength();

		if (bitStrings != null) {
			for (int i = 0; i < bitStrings.length; i++) {
				if (bitStrings[i] != null) {

					TableItem item = new TableItem(table, SWT.NONE);

					switch (i) {
					case 0:
						item.setText(0, "NUL"); // Null //$NON-NLS-1$
						break;
					case 9:
						item.setText(0, "TAB"); // Tabulator //$NON-NLS-1$
						break;
					case 10:
						item.setText(0, "LF"); // Line Feed //$NON-NLS-1$
						break;
					case 13:
						item.setText(0, "CR"); // Carriage Return //$NON-NLS-1$
						break;
					case 32:
						item.setText(0, "\u2423"); // Space //$NON-NLS-1$
						break;
					case 38:
						item.setText(0, "&&"); // & //$NON-NLS-1$

						break;
					default:
						item.setText(0, String.valueOf((char) i));
					}
					item.setText(2, bitStrings[i].toString());
					item.setText(3, String.valueOf(bitStrings[i].toString().length()));

					Node tmp = null;
					for (Node n : nodes) {
						if (n.getName() == i) {
							tmp = n;
							break;
						}
					}

					item.setText(1, String.valueOf(String.format("%2.9f", tmp.getValue()))); //$NON-NLS-1$

					if (minCodelenght > bitStrings[i].getLength())
						minCodelenght = bitStrings[i].getLength();

					if (maxCodelenght < bitStrings[i].getLength())
						maxCodelenght = bitStrings[i].getLength();

					counter++;
					avarageCodelength += tmp.getValue() * bitStrings[i].getLength();
				}
			}
			StringBuilder sb = new StringBuilder();
			sb.append(Messages.HuffmanCodingView_codetable_stat_header);

			sb.append(Messages.HuffmanCodingView_codetable_stat_1 + counter);
			sb.append("\t\t\t"); //$NON-NLS-1$

			sb.append(Messages.HuffmanCodingView_codetable_stat_3 + minCodelenght);
			sb.append("\t\t\t"); //$NON-NLS-1$

			sb.append(Messages.HuffmanCodingView_codetable_stat_4 + maxCodelenght);
			sb.append("\t\t\t"); //$NON-NLS-1$

			if (mode == HuffmanCodingView.COMPRESS) { // $NON-NLS-1$
				sb.append(Messages.HuffmanCodingView_codetable_stat_5
						+ String.valueOf(String.format("%2.3f", avarageCodelength))); //$NON-NLS-1$
			}

			sb.append("\n\n" + Messages.HuffmanCodingView_codetable_stat_2);
			styledTextCodetable.setText(sb.toString());

			StyleRange title = new StyleRange();
			title.start = 0;
			title.length = Messages.HuffmanCodingView_codetable_stat_header.length();
			title.fontStyle = SWT.BOLD;
			styledTextCodetable.setStyleRange(title);
		}
	}

}
