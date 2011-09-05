/**
 * This Class is used to create a View of SPA, which introduces the basic principle of SPA and visualizes the process
 * and countermeasures of SPA attack.
 *
 * @author Biqiang Jiang
 * @version 1.0, 01/09/09
 * @since JDK1.5.7
 */

package org.jcryptool.visual.sidechannelattack.spa.views;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.algorithm.SquareandMultiply;
import org.jcryptool.visual.sidechannelattack.SPAPlugIn;

import com.swtdesigner.SWTResourceManager;

public class SPAView extends ViewPart implements Constants {

    private StyledText noteIntResStyledText;
    private Table recordTable;
    private Text mod;
    private Combo pSelectCombo;
    private Combo qSelectCombo;
    private Text result;
    private Combo exponent;
    private Text basis;
    private int counter = 1;
    private int exp_selected = 0;
    private StyledText indicationOfVulnerabilityText;
    // this is the ....
    private int q_selected = 0;
    private int p_selected = 0;
    private StyledText rsaProcessText;

    // declare a object of SquareandMultiply, which is used to process the "square and multiply" algorithm
    SquareandMultiply squareandMultiply = new SquareandMultiply();

    public void createPartControl(final Composite parent) {

        Color color = parent.getDisplay().getSystemColor(SWT.COLOR_RED);
        StyleRange styleRange = new StyleRange();
        styleRange.start = 0;
        styleRange.length = 6;
        styleRange.fontStyle = SWT.BOLD;
        styleRange.foreground = color;

        StyleRange[] styles = new StyleRange[2];
        styles[0] = new StyleRange();
        styles[0].fontStyle = SWT.BOLD;
        Font initialFont = parent.getFont();
        FontData[] fontData = initialFont.getFontData();

        // the modified font is used to display the subscript and superscript
        Font newFont = new Font(parent.getDisplay(), fontData[0].getName(), fontData[0].getHeight() * 14 / 15, fontData[0].getStyle());
        styles[0].font = newFont;
        styles[0].rise = fontData[0].getHeight() / 2;

        styles[1] = new StyleRange();
        styles[1].fontStyle = SWT.BOLD;

        styles[1].font = newFont;
        styles[1].rise = fontData[0].getHeight() / 2;

        int[] ranges = new int[] {454, 1, 495, 1};

        final ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL
                | SWT.BORDER);

        // the scrollbar can be adjusted by changing "the values of composite" inserted in the scrolledcomposite
        final Group mainGroup = new Group(scrolledComposite, SWT.NONE);
        mainGroup.setText(MAIN_GROUP_TITLE);
        mainGroup.setSize(1200, 800);

        scrolledComposite.setContent(mainGroup);

        // the rsaAlgorithmGroup is used to present the basic Definition of RSA Algorithm
        final Group rsaAlgorithmGroup = new Group(mainGroup, SWT.NONE);
        rsaAlgorithmGroup.setText(RSA_ALG_GROUP_TITLE);
        rsaAlgorithmGroup.setBounds(0, 20, 305, 330);

        rsaProcessText = new StyledText(rsaAlgorithmGroup, SWT.MULTI | SWT.NONE | SWT.H_SCROLL | SWT.V_SCROLL);
        rsaProcessText.setBounds(5, 20, 295, 310);
        rsaProcessText.setText(RSA_PROCESS_TEXT);
        rsaProcessText.setEditable(false);
        rsaProcessText.setStyleRanges(ranges, styles);
        rsaProcessText.setLineBackground(15, 3, parent.getDisplay().getSystemColor(SWT.COLOR_YELLOW));

        // squareAndMultiAlgGroup is used to introduce the "square and multiply" algorithm
        final Group squareAndMultiAlgGroup = new Group(mainGroup, SWT.NONE);
        squareAndMultiAlgGroup.setText(SQUARE_MULTI_ALG_GROUP_TITLE);
        squareAndMultiAlgGroup.setBounds(0, 355, 305, 290);

        // styledText is used here to enactment different styles in the Text
        StyledText squareAndMultiAlgText = new StyledText(squareAndMultiAlgGroup, SWT.MULTI | SWT.NONE | SWT.V_SCROLL);
        squareAndMultiAlgText.setEditable(false);
        squareAndMultiAlgText.setBounds(5, 20, 295, 255);

        StyleRange[] newStyles = new StyleRange[3];
        newStyles[0] = new StyleRange();
        newStyles[0].fontStyle = SWT.BOLD;

        newStyles[0].font = newFont;
        newStyles[0].rise = fontData[0].getHeight() / 2;

        newStyles[1] = new StyleRange();
        newStyles[1].fontStyle = SWT.BOLD;

        newStyles[1].font = newFont;
        newStyles[1].rise = fontData[0].getHeight() / 2;

        newStyles[2] = new StyleRange();
        newStyles[2].fontStyle = SWT.BOLD;

        newStyles[2].font = newFont;
        newStyles[2].rise = -fontData[0].getHeight() / 2;

        int[] newRanges = new int[] {503, 1, 610, 1, 652, 1};

        squareAndMultiAlgText.setText(NOTE_RSA_VULNERABILITY + INPUT_BOLD_ENG + INPUT_ENG + OUTPUT_BOLD_ENG
                + OUTPUT_ENG + PROCESS_BOLD_ENG + PROCESS_ENG);

        RGB c = new RGB(252, 99, 103);

        squareAndMultiAlgText.setLineBackground(22, 5, new Color(parent.getDisplay(), c));
        squareAndMultiAlgText.setStyleRanges(newRanges, newStyles);

        // parameterOfRSAGroup is used here to determine the parameters of RSA
        final Group parameterOfRSAGroup = new Group(mainGroup, SWT.NONE);
        parameterOfRSAGroup.setBounds(310, 20, 230, 260);
        parameterOfRSAGroup.setText(PARAM_OF_RSA_GROUP_TITLE);

        // it's some cue labels.
        final Label cue_label = new Label(parameterOfRSAGroup, SWT.NONE);
        cue_label.setBounds(10, 230, 215, 25);
        final Label enterTheBasisLabel = new Label(parameterOfRSAGroup, SWT.NONE);
        enterTheBasisLabel.setBounds(10, 25, 100, 20);
        enterTheBasisLabel.setText(BASIS_LABEL);


        // basis is used to save the ciphertext c in RSA: R = c^d mod n
        basis = new Text(parameterOfRSAGroup, SWT.BORDER);
        basis.setTextLimit(9);

        basis.setToolTipText(TOOL_TIP_TEXT_BASIS);
        basis.setBounds(10, 45, 75, 25);

        // the verifylistener on basis is used to verify the correctness of input
        basis.addVerifyListener(new VerifyListener() {
            public void verifyText(VerifyEvent e) {
                // Pattern pattern = Pattern.compile("[0-9]\\d*");
                Pattern pattern = Pattern.compile("^[0-9]{1,9}$");
                Matcher matcher = pattern.matcher(e.text);

                if (matcher.matches())
                    e.doit = true;
                else if (e.text.length() > 0)
                    e.doit = false;
                else
                    e.doit = true;
            }
        });

        // it's a cue label.
        final Label enterTheExponentLabel = new Label(parameterOfRSAGroup, SWT.NONE);
        enterTheExponentLabel.setBounds(10, 75, 100, 20);
        enterTheExponentLabel.setText(EXPONENT_LABEL);

        // basis is used to save the exponent d in RSA: R = c^d mod n
        exponent = new Combo(parameterOfRSAGroup, SWT.READ_ONLY);
        exponent.setToolTipText(TOOL_TIP_TEXT_EXPONENT);
        exponent.setBounds(10, 95, 75, 25);

        // the rang of prime number can be selected as exponent
        final int[] primeDataExponent = {101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173,
                179, 181, 191, 193, 197, 199};

        int dataLengthExponent = primeDataExponent.length;
        int dataElementIndexExponent = 0;
        while (dataLengthExponent > 0) {

            exponent.add(String.valueOf(primeDataExponent[dataElementIndexExponent]));
            dataLengthExponent--;
            dataElementIndexExponent++;
        }



        // it's a cue label
        final Label theModularLabel = new Label(parameterOfRSAGroup, SWT.NONE);
        theModularLabel.setBounds(10, 175, 210, 17);
        theModularLabel.setText(TOOL_TIP_TEXT_RESULT_LABEL);

        // two cue labels
        final Label chooseQLabel = new Label(parameterOfRSAGroup, SWT.NONE);
        chooseQLabel.setBounds(85, 125, 65, 20);
        chooseQLabel.setText(CHOOSE_Q_LABEL);
        final Label choosePLabel = new Label(parameterOfRSAGroup, SWT.NONE);
        choosePLabel.setBounds(10, 125, 65, 20);
        choosePLabel.setText(CHOOSE_P_LABEL);

        // the range in which p and q can be selected
        final int[] primeData = {101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181,
                191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293};

        // Q and P can be selected in qselectcombo and pselectcombo
        pSelectCombo = new Combo(parameterOfRSAGroup, SWT.READ_ONLY);
        pSelectCombo.setToolTipText(TOOL_TIP_TEXT_P_SELECTION);
        pSelectCombo.setBounds(10, 145, 55, 25);


        qSelectCombo = new Combo(parameterOfRSAGroup, SWT.READ_ONLY);
        qSelectCombo.setToolTipText(TOOL_TIP_TEXT_Q_SELECTION);
        qSelectCombo.setBounds(85, 145, 55, 25);

        int data_length = primeData.length;
        int data_element_index = 0;
        while (data_length > 0) {

            qSelectCombo.add(String.valueOf(primeData[data_element_index]));
            data_length--;
            data_element_index++;

        }

        data_length = primeData.length;
        data_element_index = 0;
        while (data_length > 0) {

            pSelectCombo.add(String.valueOf(primeData[data_element_index]));
            data_length--;
            data_element_index++;

        }


        // the selectionlistener is used to determine which prime number has been chosen
        qSelectCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {

                q_selected = Integer.parseInt(qSelectCombo.getItem(qSelectCombo.getSelectionIndex()));

                for (int i = 0; i < pSelectCombo.getItemCount(); i++) {

                    if (pSelectCombo.getItem(i).equals(String.valueOf(q_selected))) {

                        pSelectCombo.remove(i);
                        break;
                    }
                }

                if (p_selected != 0 && q_selected != 0) {
                    cue_label.setText("");

                    mod.setText(String.valueOf((p_selected) * (q_selected)));
                }

            }
        });

        // the selectionlistener is used to determine which prime number has been chosen
        pSelectCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {

                p_selected = Integer.parseInt(pSelectCombo.getItem(pSelectCombo.getSelectionIndex()));

                for (int i = 0; i < qSelectCombo.getItemCount(); i++) {

                    if (qSelectCombo.getItem(i).equals(String.valueOf(p_selected))) {

                        qSelectCombo.remove(i);
                        break;
                    }
                }

                if (p_selected != 0 && q_selected != 0) {
                    cue_label.setText("");
                    mod.setText(String.valueOf((p_selected) * (q_selected)));
                }

            }
        });

        // the visualizedgroup here is used to present the process and the power traces of "square and multiply"
        final Group visualizedgroup = new Group(mainGroup, SWT.NONE);
        visualizedgroup.setBounds(545, 0, 655, 800);

        final Group visualTableandPowerTraceGroup = new Group(visualizedgroup, SWT.NONE);

        visualTableandPowerTraceGroup.setBounds(0, 0, 655, 800);

        // the powertracevisualizationgroup is used to display the power traces of different operations
        final Group powerTraceVisualizationGroup = new Group(visualTableandPowerTraceGroup, SWT.NONE);
        powerTraceVisualizationGroup.setBounds(0, 350, 655, 160);
        powerTraceVisualizationGroup.setText(POWER_TRACE_VISUAL_GROUP_TITLE);

        // in calculatetablegrout recorders the process of computation
        final Group calculateTableGroup = new Group(visualTableandPowerTraceGroup, SWT.NONE);
        calculateTableGroup.setText(CALCULATION_TABLE_GROUP_TITLE);
        calculateTableGroup.setBounds(0, 0, 655, 350);

        recordTable = new Table(calculateTableGroup, SWT.BORDER);
        recordTable.setBounds(5, 20, 650, 320);
        recordTable.setLinesVisible(true);
        recordTable.setHeaderVisible(true);

        // roundCol is the first low of recorder table in which some initial information will be saved
        final TableColumn roundCol = new TableColumn(recordTable, SWT.NONE);
        roundCol.setWidth(180);
        roundCol.setText(FIRST_COLUMN_IN_TABLE);

        // resSquareCol is the second low of recorder table in which the result after squaring will be saved
        final TableColumn resSquareCol = new TableColumn(recordTable, SWT.NONE);
        resSquareCol.setWidth(210);
        resSquareCol.setText(SECOND_COLUMN_IN_TABLE);

        // resMultiCol is the last low in which the result after multiplication will be saved
        final TableColumn resMultiCol = new TableColumn(recordTable, SWT.NONE);
        resMultiCol.setWidth(255);
        resMultiCol.setText(THIRD_COLUMN_IN_TABLE);

        new TableCursor(recordTable, SWT.NONE);

        // indicationGroup is used to give some explanations to power traces
        final Group indicationGroup = new Group(visualTableandPowerTraceGroup, SWT.NONE);
        indicationGroup.setBounds(0, 515, 650, 120);
        indicationGroup.setVisible(true);
        indicationOfVulnerabilityText = new StyledText(indicationGroup, SWT.BORDER | SWT.WRAP | SWT.MULTI
                | SWT.V_SCROLL);


        // the executeButton is used here to start the process of "Square and Multiply" Algorithm
        final Button executeButton = new Button(parameterOfRSAGroup, SWT.NONE);
        executeButton.setEnabled(false);
        executeButton.setToolTipText(TOOL_TIP_TEXT_INSECURE_EXECUTEBUTTON);

        // executeButton is used to start the process of "square and multiply" algorithm
        executeButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {

                visualTableandPowerTraceGroup.setVisible(true);

                recordTable.removeAll();

                counter = 1;

                cue_label.setVisible(false);

                StackLayout stacklayout = new StackLayout();
                powerTraceVisualizationGroup.setLayout(stacklayout);

                int exp_in_decimal = Integer.parseInt(exponent.getText());
                int[] tempresult = new int[Integer.toBinaryString(Integer.parseInt(exponent.getText())).length()];
                int count = 0;
                long res = 1;
                String exp_in_binary = Integer.toBinaryString(exp_in_decimal);
                int exp_in_binar_length = exp_in_binary.length();

                final TableItem initialTableItemBasis = new TableItem(recordTable, SWT.BORDER);
                initialTableItemBasis.setText(0, INPUT_BASIS_ENG);
                initialTableItemBasis.setText(1, RES_AFTER_SQUARE_ENG);
                initialTableItemBasis.setText(2, RES_AFTER_MUL_ENG);

                final TableItem initialTableItemInput = new TableItem(recordTable, SWT.BORDER);
                initialTableItemInput.setText(0, INITIAL_ITEM_TEXT_1_IN_TABLE + basis.getText() + INPUT_RES_1_ENG);

                final TableItem initialTableItemExponent = new TableItem(recordTable, SWT.BORDER);
                initialTableItemExponent.setText(0, EXP_ENG + exponent.getText() + DEC_ENG);

                final TableItem initialTableItemExponent_binary = new TableItem(recordTable, SWT.BORDER);
                initialTableItemExponent_binary.setText(0, INITIAL_ITEM_TEXT_2_IN_TABLE + exp_in_binary + BIN_ENG);

                final TableItem initialTableItemModul = new TableItem(recordTable, SWT.BORDER);
                initialTableItemModul.setText(0, "  " + MODUL_ENG + mod.getText());

                final TableItem initialTableItemProcess = new TableItem(recordTable, SWT.BORDER);
                initialTableItemProcess.setText(0, INITIAL_ITEM_TEXT_3_IN_TABLE);
                int achse_x = 30;

                final ScrolledComposite powerTraceVisualScrollComposite = new ScrolledComposite(powerTraceVisualizationGroup, SWT.NONE
                        | SWT.H_SCROLL);

                stacklayout.topControl = powerTraceVisualScrollComposite;
                powerTraceVisualizationGroup.layout();

                final Composite powerTraceComposite = new Composite(powerTraceVisualScrollComposite, SWT.NONE);

                powerTraceComposite.setSize(1250, 160);
                powerTraceVisualScrollComposite.setContent(powerTraceComposite);

                final Label achseY_squareLabel = new Label(powerTraceComposite, SWT.NONE);
                achseY_squareLabel.setBounds(achse_x, 10, 85, 110);
                achseY_squareLabel.setImage(SPAPlugIn.getImageDescriptor(IMGADRESSE_Y_ACHSE_ENG).createImage());

                achse_x = achse_x + 85;

                while (exp_in_binar_length > 0) {
                    final TableItem tempTableItems = new TableItem(recordTable, SWT.BORDER);
                    tempTableItems.setText(0, "  " + counter + HIGHEST_BIT_ENG + exp_in_binary.charAt(count));

                    long tempres_byExp = res;

                    res = (long) Math.pow(res, 2) % Integer.parseInt(mod.getText());
                    tempTableItems.setText(1, RES_EQUAL_ENG + tempres_byExp + HOCH_2_MOD_ENG + mod.getText() + " = "
                            + res);

                    if (exp_in_binary.charAt(count) == '1') {
                        long tempres_byMul = res;
                        res = (res * Integer.parseInt(basis.getText()) % Integer.parseInt(mod.getText()));
                        tempTableItems.setText(2, RES_EQUAL_ENG + tempres_byMul + " * " + basis.getText() + " mod "
                                + mod.getText() + " = " + res);

                        final Label powertrace_sqMulLabel = new Label(powerTraceComposite, SWT.NONE);
                        powertrace_sqMulLabel.setBounds(achse_x, 10, 85, 110);
                        powertrace_sqMulLabel.setImage(SPAPlugIn.getImageDescriptor(IMGADDRESSE_SQMUL_ENG).createImage());
                        achse_x = achse_x + 85;
                    } else {
                        final Label powertrace_squareLabel = new Label(powerTraceComposite, SWT.NONE);

                        powertrace_squareLabel.setBounds(achse_x, 10, 42, 110);
                        powertrace_squareLabel.setImage(SPAPlugIn.getImageDescriptor(IMGADDRESSE_SQ_ENG).createImage());
                        achse_x = achse_x + 42;
                    }
                    count++;
                    counter++;
                    exp_in_binar_length--;
                }

                final Label achseX_squareLabel = new Label(powerTraceComposite, SWT.NONE);
                achseX_squareLabel.setBounds(achse_x, 10, 85, 110);
                achseX_squareLabel.setImage(SPAPlugIn.getImageDescriptor(IMGADDRESSE_X_ACHSE_ENG).createImage());

                final TableItem outPutTableItems = new TableItem(recordTable, SWT.BORDER);
                outPutTableItems.setText(0, OUTPUT_TABLE_ITEM_TEXT);

                final TableItem finalTableItems = new TableItem(recordTable, SWT.BORDER);

                finalTableItems.setText(0, "  " + FINAL_RESULT_ENG + res);
                result.setText("" + res);

                res = squareandMultiply.sqmulExcution(Integer.parseInt(basis.getText()), Integer.parseInt(exponent.getText()), Integer.parseInt(mod.getText()));

                indicationGroup.setText(INDICATION_GROUP_SQUARE_MLUTI_TITLE);
                //
                indicationOfVulnerabilityText.setText(INDICATION_OF_VULNERABILITY_TEXT);
                indicationOfVulnerabilityText.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
                indicationOfVulnerabilityText.setEditable(false);
                indicationOfVulnerabilityText.setBounds(5, 15, 645, 100);

                String str = "(" + tempresult[--count];
                while (count != 0) {
                    str = str + "*" + tempresult[--count];
                }
                cue_label.setText("");
                cue_label.setForeground(SWTResourceManager.getColor(255, 0, 0));
                cue_label.setVisible(true);

            }

        });
        // the samaExecuteButton here is used to start the more vulnerably algorithm "Square and Multiply Always"(sama)
        final Button samaExecuteButton = new Button(parameterOfRSAGroup, SWT.NONE);
        samaExecuteButton.setEnabled(false);
        samaExecuteButton.setToolTipText(TOOL_TIP_TEXT_SECURE_EXECUTEBUTTON);

        executeButton.setBounds(125, 15, 100, 25);
        executeButton.setText(INSECURE_EXECUTION_BUTTON_TEXT);

        basis.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {

                if (!basis.getText().equals("") && (exp_selected != 0) && (!mod.getText().equals(""))) {

                    executeButton.setEnabled(true);
                    samaExecuteButton.setEnabled(true);

                }

            }
        });

     // the selectionlistener here is used to determine which prime number has been chosen as exponent
        exponent.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {

                exp_selected = primeDataExponent[exponent.getSelectionIndex()];

                if (!basis.getText().equals("") && (exp_selected != 0) && (!mod.getText().equals(""))) {

                    executeButton.setEnabled(true);
                    samaExecuteButton.setEnabled(true);

                }

            }
        });

        // clearButton is used to clear the table
        final Button clearButton = new Button(parameterOfRSAGroup, SWT.NONE);
        clearButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {

                recordTable.removeAll();
                cue_label.setVisible(false);
                basis.setText("");
                exponent.setText("");
                mod.setText("");
                exp_selected = 0;
                result.setText("");
                pSelectCombo.deselectAll();
                qSelectCombo.deselectAll();
                p_selected = 0;
                q_selected = 0;
                executeButton.setEnabled(false);
                samaExecuteButton.setEnabled(false);
                basis.setFocus();
            }
        });
        clearButton.setToolTipText(TOOL_TIP_TEXT_CLEARBUTTON);
        clearButton.setBounds(125, 85, 100, 25);
        clearButton.setText(CLEAR_BUTTON_TEXT);

     // mod is used here to save the module n in R = c^d mod n
        mod = new Text(parameterOfRSAGroup, SWT.BORDER);

        mod.setToolTipText(TOOL_TIP_TEXT_MODULE_N);
        mod.setBounds(160, 145, 60, 25);
        mod.setEditable(false);

        mod.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {

                if (!basis.getText().equals("") && (exp_selected != 0) && (!mod.getText().equals(""))) {

                    executeButton.setEnabled(true);
                    samaExecuteButton.setEnabled(true);

                }

            }
        });


        // result is used to save the result R in R = c^d mod n
        result = new Text(parameterOfRSAGroup, SWT.BORDER);
        result.setToolTipText(TOOL_TIP_TEXT_RESULT);
        result.setBounds(10, 195, 75, 25);
        result.setEditable(false);



        final Label theModuleMLabel = new Label(parameterOfRSAGroup, SWT.NONE);
        theModuleMLabel.setBounds(160, 125, 65, 20);
        theModuleMLabel.setText(MODULE_LABEL_TEXT);

        // samaExecuteButton is used to start the process of "square and multiply always"
        samaExecuteButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {

                visualTableandPowerTraceGroup.setVisible(true);

                recordTable.removeAll();

                counter = 1;

                cue_label.setVisible(false);

                StackLayout stacklayout = new StackLayout();
                powerTraceVisualizationGroup.setLayout(stacklayout);

                int exp_in_decimal = Integer.parseInt(exponent.getText());
                int[] tempresult = new int[Integer.toBinaryString(Integer.parseInt(exponent.getText())).length()];
                int count = 0;
                long res = 1;
                String exp_in_binary = Integer.toBinaryString(exp_in_decimal);
                int exp_in_binar_length = exp_in_binary.length();

                // declare some initial parameters of "square and multiply always" in table
                final TableItem initialTableItemBasis = new TableItem(recordTable, SWT.BORDER);
                initialTableItemBasis.setText(0, INPUT_BASIS_ENG);
                initialTableItemBasis.setText(1, INITIAL_TABLE_ITEM_SQUARE);
                initialTableItemBasis.setText(2, INITIAL_TABLE_ITEM_MULTIPLY);

                // display the cipher text in table
                final TableItem initialTableItemInput = new TableItem(recordTable, SWT.BORDER);
                initialTableItemInput.setText(0, INITIAL_ITEM_TEXT_1_IN_TABLE + basis.getText() + INPUT_RES_1_ENG);

                // display the exponent text in table
                final TableItem initialTableItemExponent = new TableItem(recordTable, SWT.BORDER);
                initialTableItemExponent.setText(0, EXP_ENG + exponent.getText() + DEC_ENG);

                // display the exponent text in binary form in table
                final TableItem initialTableItemExponent_binary = new TableItem(recordTable, SWT.BORDER);
                initialTableItemExponent_binary.setText(0, INITIAL_ITEM_TEXT_2_IN_TABLE + exp_in_binary + BIN_ENG);

                // display the module text in table
                final TableItem initialTableItemModul = new TableItem(recordTable, SWT.BORDER);
                initialTableItemModul.setText(0, "  " + MODUL_ENG + mod.getText());

                final TableItem initialTableItemProcess = new TableItem(recordTable, SWT.BORDER);
                initialTableItemProcess.setText(0, INITIAL_ITEM_TEXT_3_IN_TABLE);

                int achse_x = 30;

                // scrolledcomposite is added in table which enables to display the contents out of range
                final ScrolledComposite powerTraceVisualScrollComposite = new ScrolledComposite(powerTraceVisualizationGroup, SWT.NONE
                        | SWT.H_SCROLL);

                stacklayout.topControl = powerTraceVisualScrollComposite;
                powerTraceVisualizationGroup.layout();

                final Composite powerTraceComposite = new Composite(powerTraceVisualScrollComposite, SWT.NONE);

                powerTraceComposite.setSize(1250, 160);
                powerTraceVisualScrollComposite.setContent(powerTraceComposite);

                // image_achse_y, image_achse_x and so on is the visualization of power traces in picture form

                final Label achseY_squareLabel = new Label(powerTraceComposite, SWT.NONE);
                achseY_squareLabel.setBounds(achse_x, 10, 85, 110);
                achseY_squareLabel.setImage(SPAPlugIn.getImageDescriptor(IMGADRESSE_Y_ACHSE_ENG).createImage());

                achse_x = achse_x + 85;

                while (exp_in_binar_length > 0) {
                    final TableItem tempTableItems = new TableItem(recordTable, SWT.BORDER);
                    tempTableItems.setText(0, "  " + counter + HIGHEST_BIT_ENG + exp_in_binary.charAt(count));

                    long tempres_byExp = res;

                    res = (long) Math.pow(res, 2) % Integer.parseInt(mod.getText());
                    tempTableItems.setText(1, RES_SQUARE_MULTI_ALWAYS_SQUARE + tempres_byExp + HOCH_2_MOD_ENG
                            + mod.getText() + " = " + res);
                    long tempres = res;

                    if (exp_in_binary.charAt(count) == '1') {
                        long tempres_byMul = res;
                        res = (res * Integer.parseInt(basis.getText()) % Integer.parseInt(mod.getText()));
                        tempTableItems.setText(2, RES_SQUARE_MULTI_ALWAYS_MULTI + tempres_byMul + " * "
                                + basis.getText() + " mod " + mod.getText() + " = " + res);

                        final Label powertrace_sqMulLabel = new Label(powerTraceComposite, SWT.NONE);
                        powertrace_sqMulLabel.setBounds(achse_x, 10, 85, 110);
                        powertrace_sqMulLabel.setImage(SPAPlugIn.getImageDescriptor(IMGADDRESSE_SQMUL_ENG).createImage());
                        achse_x = achse_x + 85;
                    } else {
                        long tempres_byMul = res;
                        res = (res * Integer.parseInt(basis.getText()) % Integer.parseInt(mod.getText()));
                        tempTableItems.setText(2, RES_SQUARE_MULTI_ALWAYS_MULTI + tempres_byMul + " * "
                                + basis.getText() + " mod " + mod.getText() + " = " + res);
                        res = tempres;

                        final Label powertrace_sqMulLabel = new Label(powerTraceComposite, SWT.PUSH);
                        powertrace_sqMulLabel.setBounds(achse_x, 10, 85, 110);
                        powertrace_sqMulLabel.setImage(SPAPlugIn.getImageDescriptor(IMGADDRESSE_SQMUL_0_ENG).createImage());
                        achse_x = achse_x + 85;

                    }
                    count++;
                    counter++;
                    exp_in_binar_length--;
                }

                final Label achseX_squareLabel = new Label(powerTraceComposite, SWT.NONE);
                achseX_squareLabel.setBounds(achse_x, 10, 85, 110);
                achseX_squareLabel.setImage(SPAPlugIn.getImageDescriptor(IMGADDRESSE_X_ACHSE_ENG).createImage());

                final TableItem outPutTableItems = new TableItem(recordTable, SWT.BORDER);
                outPutTableItems.setText(0, OUTPUT_TABLE_ITEM_TEXT);

                final TableItem finalTableItems = new TableItem(recordTable, SWT.BORDER);

                finalTableItems.setText(0, "  " + FINAL_RESULT_ENG + res);
                result.setText("" + res);

                res = squareandMultiply.sqmulExcution(Integer.parseInt(basis.getText()), Integer.parseInt(exponent.getText()), Integer.parseInt(mod.getText()));

                // indicationGroup is used to give some explanation to visualization of power traces
                indicationGroup.setText(INDICATION_GROUP_SQUARE_MULTI_ALWAYS_TITLE);
                indicationOfVulnerabilityText.setText(INDICATION_SQUARE_MULTI_ALWAYS);
                indicationOfVulnerabilityText.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
                indicationOfVulnerabilityText.setEditable(false);
                indicationOfVulnerabilityText.setBounds(5, 15, 645, 100);

                String str = "(" + tempresult[--count];
                while (count != 0) {
                    str = str + "*" + tempresult[--count];
                }
                cue_label.setText("");
                cue_label.setForeground(SWTResourceManager.getColor(255, 0, 0));
                cue_label.setVisible(true);

            }
        });
        samaExecuteButton.setText(SQUARE_MULTI_EXECUTION_BUTTON_TEXT);
        samaExecuteButton.setBounds(125, 50, 100, 25);

        // in part6SquareGroup the introduction and explanation of countermeasure against SPA will be given.
        final Group part6SquareGroup = new Group(mainGroup, SWT.NONE);
        part6SquareGroup.setText(COUNTERMEASURES_GROUP_TITLE);
        part6SquareGroup.setBounds(310, 280, 230, 365);

        // here the styledtext is used to display texts in different styles
        noteIntResStyledText = new StyledText(part6SquareGroup, SWT.BORDER | SWT.WRAP | SWT.MULTI);
        noteIntResStyledText.setBounds(0, 20, 230, 345);

        StyleRange[] newerStyles = new StyleRange[5];
        newerStyles[0] = new StyleRange();
        newerStyles[0].fontStyle = SWT.BOLD;

        newerStyles[0].font = newFont;
        newerStyles[0].rise = -fontData[0].getHeight() / 4;

        newerStyles[1] = new StyleRange();
        newerStyles[1].fontStyle = SWT.BOLD;

        newerStyles[1].font = newFont;
        newerStyles[1].rise = fontData[0].getHeight() / 2;

        newerStyles[2] = new StyleRange();
        newerStyles[2].fontStyle = SWT.BOLD;

        newerStyles[2].font = newFont;
        newerStyles[2].rise = -fontData[0].getHeight() / 4;

        newerStyles[3] = new StyleRange();
        newerStyles[3].fontStyle = SWT.BOLD;

        newerStyles[3].font = newFont;
        newerStyles[3].rise = -fontData[0].getHeight() / 4;

        newerStyles[4] = new StyleRange();
        newerStyles[4].fontStyle = SWT.BOLD;

        newerStyles[4].font = newFont;
        newerStyles[4].rise = -fontData[0].getHeight() / 3;

        int[] newerRanges = new int[] {424, 1, 431, 1, 465, 1, 473, 1, 517, 1};

        noteIntResStyledText.setText(SQUARE_MULTI_ALWAYS_ALG_TEXT);
        noteIntResStyledText.setStyleRanges(newerRanges, newerStyles);

        noteIntResStyledText.setLineBackground(5, 3, parent.getDisplay().getSystemColor(SWT.COLOR_GREEN));

        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent.getShell(), "org.jcryptool.visual.sidechannelattack.spa.spaview");
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus() {

    }

}