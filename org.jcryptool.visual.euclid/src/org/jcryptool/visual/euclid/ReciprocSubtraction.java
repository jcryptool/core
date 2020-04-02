package org.jcryptool.visual.euclid;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.core.util.ui.TitleAndDescriptionComposite;

public class ReciprocSubtraction extends Composite {
	
	private Group grpInput_1;
	private Label lblP_1;
	private Label lblQ_1;
	private Text textP_1;
	private Text textQ_1;
	private Button btnPrevStep_1;
	private Button btnNextStep_1;
	private Button btnCompute_1;
	private Button btnResetCanvas_1;
	private Button btnResetAll_1;
	private Group grpComputation_1;
	private ScrolledComposite scrolledComposite_canvas;
	private Canvas canvas;
	
	private static final Color BLACK = ColorService.BLACK;
	private static final Color GREEN = ColorService.GREEN;
	private static final Color RED = ColorService.RED;
	private static final Color BLUE = ColorService.BLUE;
	
	private ArrayList<int[]> values;
	private int step = -1;

	public ReciprocSubtraction(Composite parent, int style, View parentView) {
		super(parent, style);
		
		setLayout(new GridLayout(6, false));
		
		TitleAndDescriptionComposite titleAndDescriptionComposite = new TitleAndDescriptionComposite(this);
		GridData gd_titleAndDescriptionComposite = new GridData(SWT.FILL, SWT.FILL, true, false, 6, 1);
		gd_titleAndDescriptionComposite.widthHint = 600;
		titleAndDescriptionComposite.setLayoutData(gd_titleAndDescriptionComposite);
		titleAndDescriptionComposite.setTitle(Messages.Euclid_Euclidean);
		titleAndDescriptionComposite.setDescription(Messages.Euclid_Description_1);

		grpInput_1 = new Group(this, SWT.NONE);
		grpInput_1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 6, 1));
		grpInput_1.setLayout(new GridLayout(4, false));
		grpInput_1.setText(Messages.Euclid_Input);

		lblP_1 = new Label(grpInput_1, SWT.NONE);
		lblP_1.setText(Messages.Euclid_P);

		textP_1 = new Text(grpInput_1, SWT.BORDER);
		textP_1.setText("44");
		textP_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		textP_1.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				if (e.text.matches("[0-9]*") || e.keyCode == SWT.BS || e.keyCode == SWT.DEL) {
					if (textP_1.getText().length() == 0 && e.text.compareTo("0") == 0) {
						e.doit = false;
					} else if (textP_1.getSelection().x == 0 && e.keyCode == 48) {
						e.doit = false;
					} else {
						e.doit = true;
					}
				} else {
					e.doit = false;
				}
			}
		});

		lblQ_1 = new Label(grpInput_1, SWT.NONE);
		lblQ_1.setText(Messages.Euclid_Q);

		textQ_1 = new Text(grpInput_1, SWT.BORDER);
		textQ_1.setText("18");
		textQ_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		textQ_1.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				if (e.text.matches("[0-9]*") || e.keyCode == SWT.BS || e.keyCode == SWT.DEL) {
					if (textQ_1.getText().length() == 0 && e.text.compareTo("0") == 0) {
						e.doit = false;
					} else if (textQ_1.getSelection().x == 0 && e.keyCode == 48) {
						e.doit = false;
					} else {
						e.doit = true;
					}
				} else {
					e.doit = false;
				}
			}
		});

		btnNextStep_1 = new Button(this, SWT.NONE);
		btnNextStep_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				nextStep_1();
			}
		});
		btnNextStep_1.setText(Messages.Euclid_NextStep_Button);

		btnPrevStep_1 = new Button(this, SWT.NONE);
		btnPrevStep_1.setEnabled(false);
		btnPrevStep_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				prevStep_1();
			}
		});
		btnPrevStep_1.setText(Messages.Euclid_PrevStep_Button);

		btnCompute_1 = new Button(this, SWT.NONE);
		btnCompute_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				compute_1();
			}
		});
		btnCompute_1.setText(Messages.Euclid_Compute_Button);

		btnResetCanvas_1 = new Button(this, SWT.NONE);
		btnResetCanvas_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				reset_1();
			}
		});
		btnResetCanvas_1.setEnabled(false);
		btnResetCanvas_1.setText(Messages.Euclid_ResetCanvas_Button);

		btnResetAll_1 = new Button(this, SWT.NONE);
		btnResetAll_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				reset_1();
				textP_1.setText("44");
				textQ_1.setText("18");
				canvas.redraw();
				canvas.update();
			}
		});
		btnResetAll_1.setEnabled(false);
		btnResetAll_1.setText(Messages.Euclid_Reset_Button);

		new Label(this, SWT.NONE);

		textP_1.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (btnResetCanvas_1.getEnabled()) {
					reset_1();
				}
			}
		});

		textQ_1.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (btnResetCanvas_1.getEnabled()) {
					reset_1();
				}
			}
		});

		grpComputation_1 = new Group(this, SWT.NONE);
		grpComputation_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1));
		grpComputation_1.setLayout(new GridLayout(1, false));
		grpComputation_1.setText(Messages.Euclid_Computation);

		scrolledComposite_canvas = new ScrolledComposite(grpComputation_1, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd_scrolledComposite_canvas = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_scrolledComposite_canvas.widthHint = 500;
		scrolledComposite_canvas.setLayoutData(gd_scrolledComposite_canvas);
		scrolledComposite_canvas.setExpandHorizontal(true);
		scrolledComposite_canvas.setExpandVertical(true);

		canvas = new Canvas(scrolledComposite_canvas, SWT.NO_REDRAW_RESIZE);
		scrolledComposite_canvas.setContent(canvas);
		scrolledComposite_canvas.setMinSize(canvas.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		canvas.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				// Only paint if the user started the algorithm.
				if (step > -1) {
					// This are the first and secon line in the canvas "Long line" and 
					// "shot line"
					if (step >= 0) {
						e.gc.setForeground(BLACK);
						e.gc.drawText(Messages.Euclid_Long_Line + values.get(0)[0], 10, 10, true);
						e.gc.drawText(Messages.Euclid_Short_Line + values.get(0)[1], 10, 40, true);
						drawLine(10, 30, values.get(0)[0], GREEN, e.gc);
						drawLine(10, 60, values.get(0)[1], RED, e.gc);
					}

					for (int i = 1; i <= step; i++) {
						e.gc.setForeground(BLACK);
						e.gc.drawText(
								values.get(i)[0] + " - " + values.get(i)[2] + "*" + values.get(i)[1] + " = " + values.get(i)[3], 10,
								75 + 45 * (i - 1), true);
						drawLine(10, 95 + 45 * (i - 1), values.get(i)[0], GREEN, e.gc);
						for (int j = 0; j < values.get(i)[2]; j++) {
							drawLine(10 + 5 * j * values.get(i)[1], 105 + 45 * (i - 1) + (j % 2) * 2, values.get(i)[1], RED,
									e.gc);
						}

						if (values.get(i)[3] == 0) {
							e.gc.setForeground(BLACK);
							e.gc.drawText(
									Messages.Euclid_GCD + values.get(0)[0] + "," + values.get(0)[1] + ") = " + values.get(i)[1], 10,
									75 + 45 * (i), true);
						} else {
							drawLine(10 + 5 * values.get(i)[1] * values.get(i)[2], 105 + 45 * (i - 1), values.get(i)[3], BLUE,
									e.gc);
						}
					}
				}
			}
			
		    private void drawLine(int x, int y, int w, Color color, GC gc) {
		        gc.setForeground(color);
		        gc.drawLine(x, y, x + w * 5, y);
		        gc.drawLine(x, y + 5, x + w * 5, y + 5);
		        for (int i = 0; i <= w; i++) {
		            gc.drawLine(x + 5 * i, y, x + 5 * i, y + 5);
		        }
		    }
		});
		
	}
	
	private void initialize_1() {
		values = new ArrayList<int[]>();
		int p = Integer.parseInt(textP_1.getText());
		int q = Integer.parseInt(textQ_1.getText());
		int n;
		int r;
		if (p > q) {
			values.add(new int[] { p, q });
			n = p / q;
			r = p % q;
			values.add(new int[] { p, q, n, r });
		} else {
			values.add(new int[] { q, p });
			n = q / p;
			r = q % p;
			values.add(new int[] { q, p, n, r });
		}

		while (values.get(values.size() - 1)[3] != 0) {
			p = values.get(values.size() - 1)[1];
			q = values.get(values.size() - 1)[3];
			n = p / q;
			r = p % q;
			values.add(new int[] { p, q, n, r });
		}

		// Recalculate the size of the scrolled composite holding the 
		// canvas. This is necessary in the case the user entered a large p value
		// and the available space on the screen is not enough to display it.
		// This will then show the scrollbars.
		scrolledComposite_canvas.setMinSize(canvas.computeSize(20 + 5 * values.get(0)[0], 100 + 45 * values.size()));

		btnResetAll_1.setEnabled(true);
	}
	

	private void nextStep_1() {
		step++;

		if (step == 0) {
			initialize_1();
		}

		// paint(step);
		canvas.redraw();
		canvas.update();

		if (step > 0) {
			btnPrevStep_1.setEnabled(true);
		}
		if (step == values.size() - 1) {
			btnNextStep_1.setEnabled(false);
			btnCompute_1.setEnabled(false);
		}
		btnResetCanvas_1.setEnabled(true);
	}

	private void prevStep_1() {
		step--;

		canvas.redraw();
		canvas.update();

		btnNextStep_1.setEnabled(true);
		btnCompute_1.setEnabled(true);
		if (step == 0)
			btnPrevStep_1.setEnabled(false);
	}

	private void compute_1() {
		if (step == -1) {
			initialize_1();
		}

		step = values.size() - 1;

		canvas.redraw();
		canvas.update();

		btnNextStep_1.setEnabled(false);
		btnPrevStep_1.setEnabled(true);
		btnCompute_1.setEnabled(false);
		btnResetCanvas_1.setEnabled(true);
	}

	private void reset_1() {
		values = null;
		step = -1;
		btnNextStep_1.setEnabled(true);
		btnPrevStep_1.setEnabled(false);
		btnCompute_1.setEnabled(true);
		btnResetAll_1.setEnabled(false);
		btnResetCanvas_1.setEnabled(false);
		canvas.redraw();
		canvas.update();
	}
	
	public void completeReset() {
		reset_1();
		textP_1.setText("44");
		textQ_1.setText("18");
	}
	
	

}
