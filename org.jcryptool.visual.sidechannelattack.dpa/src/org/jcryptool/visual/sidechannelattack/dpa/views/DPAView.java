/**
 * This Class is used to create a View of DPA, which introduces the basic principle, process and countermeasures of DPA
 * attack.
 *
 *
 * @author Biqiang Jiang
 *
 * @version 1.0, 01/09/09
 *
 * @since JDK1.5.7
 */

package org.jcryptool.visual.sidechannelattack.dpa.views;

import java.math.BigInteger;
import java.security.spec.ECFieldFp;
import java.security.spec.ECPoint;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.algorithm.ECCAdd;
import org.jcryptool.algorithm.ECCDouble;
import org.jcryptool.algorithm.ECCMultiply;
import org.jcryptool.algorithm.ECCOrderAndPoints;
import org.jcryptool.algorithm.RandomFactorCreator;
import org.jcryptool.visual.sidechannelattack.DPAPlugIn;

import com.swtdesigner.SWTResourceManager;

/**
 * This Class is used to create a View of DPA, which introduces the basic principle of DPA and visualizes the process
 * and countermeasures of DPA attack.
 *
 * @author Biqiang Jiang
 * @version 1.0, 01/09/09
 * @since JDK1.5.7
 */

public class DPAView extends ViewPart implements Constants {

    private StyledText eCCAlgorithmText;
    private StyledText unsecureText;
    private StyledText parameterOfCountermeasuresText;
    private Combo scalarParameterCombo;
    private StyledText orderOfECPointText;
    private Combo eCPointscombo;
    private StyledText orderOfCurveText;
    private StyledText eCCurveText;
    private Combo parameterBCombo;
    private Combo parameterACombo;
    private Combo primeFieldSelectCombo;
    private int primeFieldSelected = 0;
    private int paraA = 0;
    private int paraB = 0;
    private long newparaA = 0;
    private long newparaB = 0;
    private int randomR = 0;
    private int kSelected = 0;
    private ECPoint ecPointSelected;
    private ECPoint allPoints[];
    private ECPoint rplusP;
    private ECPoint newP;
    private ECPoint kR;
    private ECPoint originalRPlusP;
    private ECPoint originalNewP;
    private Combo countermeasureselectionCombo;
    ECCOrderAndPoints ecc;
    private Table recordTable;
    private int orderOfSelectedECPoint;
    private int orderOfCurve;
    private int new_k;
    private int exceptionFlag;
    private String newkInBinaryForm;
    private ECPoint Q;
    private int outputFlag = 0;
    private int counterFlag = 1;

    // create a visual panel of DPA
    public void createPartControl(final Composite parent) {

        // define the subscript and superscript correctly
        final StyleRange styleRange = new StyleRange();
        styleRange.start = 282;
        styleRange.length = 1;
        styleRange.fontStyle = SWT.BOLD;

        // define the special font of stylerange
        Font initialFont = parent.getFont();
        FontData[] fontData = initialFont.getFontData();
        Font newFont = new Font(parent.getDisplay(), fontData[0].getName(), fontData[0].getHeight() * 14 / 15, fontData[0].getStyle());
        styleRange.font = newFont;
        styleRange.rise = -fontData[0].getHeight() / 2;

        // add horizontal and vitical scrollbar when contents on the mainGroup are out of bound
        final ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL
                | SWT.V_SCROLL);

        // define an imbedding maingroup
        final Group mainGroup = new Group(scrolledComposite, SWT.NONE);
        mainGroup.setText(MAIN_GROUP_TITLE);
        mainGroup.setSize(1200, 800);
        scrolledComposite.setContent(mainGroup);

        // define a group called eccAlgorithmgroup which introduces the definition and basic principle of ECC algorithm
        final Group eccAlgorithmGroup = new Group(mainGroup, SWT.NONE);
        eccAlgorithmGroup.setText(ECC_ALG_GROUP_TITLE);
        eccAlgorithmGroup.setBounds(0, 20, 305, 170);

        // define the style of the Text shown in the eccAlgorithmGroup
        eCCAlgorithmText = new StyledText(eccAlgorithmGroup, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        eCCAlgorithmText.setEditable(false);
        eCCAlgorithmText.setBounds(10, 25, 295, 145);
        eCCAlgorithmText.setText(ECC_ALG_TEXT);

        // define a group in which the algorithm "double and add always" is introduced in pseudo code form
        final Group explanationOfAlgGroup = new Group(mainGroup, SWT.NONE);
        explanationOfAlgGroup.setText(EXPLANATION_OF_ALG);
        explanationOfAlgGroup.setBounds(0, 195, 305, 320);

        // define the content and script of the introduction of "double and add always"
        unsecureText = new StyledText(explanationOfAlgGroup, SWT.BORDER | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
        unsecureText.setEditable(false);
        unsecureText.setBounds(10, 20, 295, 290);
        unsecureText.setText(UNSECURE_DOUBLE_ADD_ALWAYS_TEXT);

        // define the color of special text
        final RGB cc = new RGB(250, 5, 60);
        unsecureText.setLineBackground(4, 3, new Color(parent.getDisplay(), cc));

        // define a group in which the user can choose which sort of countermeasures should be visualized in the view
        final Group counterButtonGroup = new Group(mainGroup, SWT.NONE);
        counterButtonGroup.setBounds(10, 515, 295, 131);

        // define a group in which all the parameters of visualization will be listed
        final Group parameterOfCountermeasuresGroup = new Group(mainGroup, SWT.NONE);
        parameterOfCountermeasuresGroup.setText(TITLE_OF_PARAMETEROFCOUNTERMEASURESGROUP);
        parameterOfCountermeasuresGroup.setBounds(310, 316, 230, 330);
        countermeasureselectionCombo = new Combo(counterButtonGroup, SWT.READ_ONLY);
        countermeasureselectionCombo.setForeground(SWTResourceManager.getColor(0, 128, 0));
        countermeasureselectionCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {

                parameterOfCountermeasuresGroup.setText(PARAM_OF_COUNTERMEASURES_GROUP_TITEL);
                counterFlag = countermeasureselectionCombo.getSelectionIndex() + 2;

                // countermeasure 1: randomized scalar multiplier
                if (counterFlag == 2) {

                    unsecureText.setText("");
                    unsecureText.setText(RANDOMIZED_SCALAR_MULTIPLIER_TEXT);

                }
                // countermeasure 2: randomized initial point
                else if (counterFlag == 3) {

                    unsecureText.setText("");
                    unsecureText.setText(RANDOMIZED_INITIAL_POINT_TEXT);

                }
                // countermeasure 3: randomized isomorphic curve
                else if (counterFlag == 4) {

                    unsecureText.setText("");
                    unsecureText.setText(RANDOMIZED_ISOMORPHIC_CURVE_TEXT);

                }
            }
        });

        // define parameters of countermeasuresselectionCombo
        countermeasureselectionCombo.setBackground(SWTResourceManager.getColor(255, 255, 255));
        countermeasureselectionCombo.setBounds(5, 81, 265, 25);
        countermeasureselectionCombo.add(COUTNERMEASURES_CCOMBO_RANDOMIZED_SCALAR_MULTIPLIER);
        countermeasureselectionCombo.add(COUNTERMEASURES_CCOMBO_RANDOMIZED_INITIAL_POINT);
        countermeasureselectionCombo.add(COUNTERMEASURES_CCOMBO_RANDOMIZED_ISOMORPHIC_CURVE);

        // besides the 3 countermeasures a button to visualize the original vulnerable algorithm "add and double always"
        // will also be provided
        final Button insecureAlgoButton = new Button(counterButtonGroup, SWT.NONE);
        insecureAlgoButton.setForeground(SWTResourceManager.getColor(255, 128, 128));
        insecureAlgoButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {

                parameterOfCountermeasuresText.setText(TEXT_OF_PARAMETEROFCOUNTERMEASURESTEXT);
                parameterOfCountermeasuresGroup.setText(TITLE_OF_PARAMETEROFCOUNTERMEASURESGROUP);
                counterFlag = 1;
                unsecureText.setText("");
                unsecureText.setText(UNSECURE_DOUBLE_ADD_ALWAYS_TEXT);
                unsecureText.setLineBackground(4, 3, new Color(parent.getDisplay(), cc));

            }
        });

        insecureAlgoButton.setText(INSECURE_ALG_LABEL_TEXT);
        insecureAlgoButton.setBounds(5, 15, 265, 25);

        // add a cue label of countermeasures
        final CLabel secureAlgoLabel = new CLabel(counterButtonGroup, SWT.NONE);
        secureAlgoLabel.setBackground(SWTResourceManager.getColor(128, 255, 0));
        secureAlgoLabel.setText(SECURE_ALG_LABEL_TEXT);
        secureAlgoLabel.setBounds(5, 50, 265, 25);

        // define a group in which the user can select the parameters of ECC and initialize an EC, on which the
        // visualization will be based
        final Group parameterOfECCGroup = new Group(mainGroup, SWT.NONE);
        parameterOfECCGroup.setBounds(310, 20, 230, 290);
        parameterOfECCGroup.setText(PARAM_OF_ECC_GROUP_TITEL);
        
        // define a group in which the whole process of encryption will be visualized with table form
        final Group visualizedGroup = new Group(mainGroup, SWT.NONE);
        visualizedGroup.setBounds(545, 0, 655, 650);

        // define the style of the text shown in the parameterOfCountermeasuresGroup
        parameterOfCountermeasuresText = new StyledText(parameterOfCountermeasuresGroup, SWT.BORDER | SWT.MULTI
                | SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY);
        parameterOfCountermeasuresText.setBackground(SWTResourceManager.getColor(239, 239, 239));
        parameterOfCountermeasuresText.setBounds(0, 20, 230, 276);
        parameterOfCountermeasuresText.setText(TEXT_OF_PARAMETEROFCOUNTERMEASURESTEXT_1);

        // define a group in which the process of algorithm will be shown
        final Group calculateTableGroup = new Group(visualizedGroup, SWT.NONE);
        calculateTableGroup.setText(CALCULATION_TABLE_GROUP_TITEL);
        calculateTableGroup.setBounds(0, 0, 655, 350);

        // define a table to display the content and process of algorithm
        recordTable = new Table(calculateTableGroup, SWT.BORDER);
        recordTable.setBounds(5, 20, 650, 320);
        recordTable.setLinesVisible(true);
        recordTable.setHeaderVisible(true);

        // define the first low of recorder table, in which initial information, result of the computation will be
        // displayed
        final TableColumn roundCol = new TableColumn(recordTable, SWT.NONE);
        roundCol.setWidth(190);
        roundCol.setText(FIRST_COLUMN_IN_TABLE);

        // define the second low of recorder table, in which the result after Doubling operation of certain loop will be
        // given
        final TableColumn resSquareCol = new TableColumn(recordTable, SWT.NONE);
        resSquareCol.setWidth(225);
        resSquareCol.setText(SECOND_COLUMN_IN_TABLE);

        // define the last low of recorder table, in which the result after Adding operation of certain loop will be
        // given
        final TableColumn resMultiCol = new TableColumn(recordTable, SWT.NONE);
        resMultiCol.setWidth(225);
        resMultiCol.setText(THIRD_COLUMN_IN_TABLE);

        // define a group in which the power traces of each turn computation will be visualized.
        final Group powerTraceGroup = new Group(visualizedGroup, SWT.NONE);
        powerTraceGroup.setBounds(0, 350, 655, 300);

        // end table define

        // define a combo in which the user can choose a prime as prime field GF(p)
        primeFieldSelectCombo = new Combo(parameterOfECCGroup, SWT.READ_ONLY);
        primeFieldSelectCombo.setToolTipText(TOOLTIPTEXT_OF_PRIMEFIELDSELECTCOMBO);
        primeFieldSelectCombo.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        primeFieldSelectCombo.setBounds(10, 50, 50, 25);
        
        // define a dropdown combo in which the user can select a number as parameter A
        parameterACombo = new Combo(parameterOfECCGroup, SWT.READ_ONLY);
        parameterACombo.setToolTipText(TOOLTIPTEXT_OF_PARAMETERACOMBO);
        parameterACombo.setBounds(80, 50, 50, 25);

        // define a dropdown combo in which the user can select a number as parameter B
        parameterBCombo = new Combo(parameterOfECCGroup, SWT.READ_ONLY);
        parameterBCombo.setToolTipText(TOOLTIPTEXT_OF_PARAMETERBCOMBO);
        parameterBCombo.setBounds(150, 50, 50, 25);

        eCCurveText = new StyledText(parameterOfECCGroup, SWT.BORDER);
        eCCurveText.setToolTipText(TOOLTIPTEXT_OF_ECCURVETEXT);
        eCCurveText.setEditable(false);
        eCCurveText.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {

                ecc = new ECCOrderAndPoints(BigInteger.valueOf(paraA), BigInteger.valueOf(paraB), BigInteger.valueOf(primeFieldSelected));

                orderOfCurveText.setText(ORDER_OF_CURVE_TEXT + ecc.getStepsofCurve());

                orderOfCurve = ecc.getStepsofCurve();

                eCPointscombo.removeAll();

                allPoints = ecc.getAllPoints();

                int data_length = allPoints.length;
                int data_element_index = 0;
                while (data_length > 0) {

                    eCPointscombo.add("(" + allPoints[data_element_index].getAffineX() + ","
                            + allPoints[data_element_index].getAffineY() + ")");
                    data_length--;
                    data_element_index++;

                }

            }
        });
        eCCurveText.setBounds(10, 90, 190, 25);
        
        // define a text field in which the computed order will be displayed
        orderOfCurveText = new StyledText(parameterOfECCGroup, SWT.BORDER);

        // define a toolbar-text to explain the meaning of order
        orderOfCurveText.setToolTipText(TOOLTIPTEXT_OF_ORDEROFCURVETEXT);
        orderOfCurveText.setEditable(false);
        orderOfCurveText.setBounds(10, 130, 140, 25);

        // define a combo in which the user can select a random point of EC to process the computation
        eCPointscombo = new Combo(parameterOfECCGroup, SWT.READ_ONLY);

        // define a toolbar-text to explain the aiming of selection
        eCPointscombo.setToolTipText(TOOLTIPTEXT_OF_ECPOINTSCOMBO);
        eCPointscombo.setBackground(SWTResourceManager.getColor(255, 255, 255));
        eCPointscombo.setBounds(20, 195, 84, 20);

        // define a cue label
        final CLabel chooseAnEcpointLabel = new CLabel(parameterOfECCGroup, SWT.NONE);
        chooseAnEcpointLabel.setText(CHOOSE_AN_ECPOINT_LABEL_TEXT);
        chooseAnEcpointLabel.setBounds(10, 165, 210, 25);

        // define a text field in which the order of selected point will be displayed
        orderOfECPointText = new StyledText(parameterOfECCGroup, SWT.BORDER);

        // define a toolbar-text to explain the meaning of order
        orderOfECPointText.setToolTipText(TOOLTIPTEXT_OF_ORDEROFECPOINTTEXT);
        orderOfECPointText.setEditable(false);
        orderOfECPointText.setBounds(10, 225, 210, 25);

        scalarParameterCombo = new Combo(parameterOfECCGroup, SWT.READ_ONLY);
        scalarParameterCombo.setToolTipText(TOOLTIPTEXT_OF_SCALARPARAMETERCOMBO);
        
        final Button executeButton = new Button(parameterOfECCGroup, SWT.NONE);
        executeButton.setText(TEXT_OF_EXECUTEBUTTON);
        executeButton.setBounds(110, 192, 110, 30);

        executeButton.setEnabled(false);
        
        // define a cue label
        final CLabel primeFieldFLabel = new CLabel(parameterOfECCGroup, SWT.NONE);
        primeFieldFLabel.setText(PRIME_FIELD_LABEL_TEXT);
        primeFieldFLabel.setBounds(10, 20, 50, 20);

        // define a cue label
        final CLabel cueLabel = new CLabel(parameterOfECCGroup, SWT.NONE);
        cueLabel.setBounds(10, 290, 230, 25);

        // define a cue label
        final CLabel pPLabel = new CLabel(parameterOfECCGroup, SWT.NONE);
        pPLabel.setBounds(125, 260, 95, 23);

        // define an integer array as the elements of primefieldselectcombo
        final int[] primeData = {307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401,
                409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499};

        int data_length = primeData.length;
        int data_element_index = 0;

        // add all elements to primefieldselectcombo
        while (data_length > 0) {

            primeFieldSelectCombo.add(String.valueOf(primeData[data_element_index]));
            data_length--;
            data_element_index++;

        }
        
        

        // add a listener on primefieldselectcombo to determine which prime number the user has chosen as prime field
        primeFieldSelectCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {

                executeButton.setEnabled(false);

                primeFieldSelected = primeData[primeFieldSelectCombo.getSelectionIndex()];

                parameterACombo.clearSelection();
                parameterACombo.setItems(new String[] {});
                parameterBCombo.clearSelection();
                parameterBCombo.setItems(new String[] {});
                orderOfECPointText.setText("");
                eCCurveText.setText("");
                
                for (int i = 0; i < primeFieldSelected; i++) {
                    parameterACombo.add(String.valueOf(i));
                }

                for (int i = 1; i < primeFieldSelected; i++) {
                    parameterBCombo.add(String.valueOf(i));
                }

                scalarParameterCombo.removeAll();

                if (!eCCurveText.getText().equals("")) {

                    eCCurveText.setText(ECCURVE_TEXT_PART1 + paraA + ECCURVE_TEXT_PART2 + paraB + ECCURVE_TEXT_PART3
                            + primeFieldSelected + ")");

                }

            }
        });

        // add a listener on parameterAcombo to determine which number the user has chosen as parameter A
        parameterACombo.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {

                executeButton.setEnabled(false);

                try {

                    if (Integer.parseInt(parameterACombo.getText()) >= primeFieldSelected) {

                        parameterACombo.setText(Integer.valueOf((Integer.parseInt(parameterACombo.getText()) % primeFieldSelected)).toString());

                        return;

                    }

                    else if (!parameterBCombo.getText().equals("")) {

                        paraA = Integer.parseInt(parameterACombo.getText());
                        paraB = Integer.parseInt(parameterBCombo.getText());
                        if ((4 * paraA * paraA * paraA + 27 * paraB * paraB) % primeFieldSelected != 0) {

                            eCCurveText.setText(ECCURVE_TEXT_PART1 + paraA + ECCURVE_TEXT_PART2 + paraB
                                    + ECCURVE_TEXT_PART3 + primeFieldSelected + ")");

                        } else {

                        }

                    }

                } catch (Exception ee) {
                    return;
                }

            }

        });

        // add a listener on parameterBcombo to determine which number the user has chosen as parameter B
        parameterBCombo.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {

                executeButton.setEnabled(false);

                try {
                    if (Integer.parseInt(parameterBCombo.getText()) >= primeFieldSelected) {

                        return;

                    } else if (!parameterACombo.getText().equals("")) {

                        paraA = Integer.parseInt(parameterACombo.getText());
                        paraB = Integer.parseInt(parameterBCombo.getText());
                        if (4 * paraA * paraA * paraA + 27 * paraB * paraB != 0) {

                            eCCurveText.setText(ECCURVE_TEXT_PART1 + paraA + ECCURVE_TEXT_PART2 + paraB
                                    + ECCURVE_TEXT_PART3 + primeFieldSelected + ")");

                        } else {
                        }
                    }
                } catch (Exception ee) {
                    if (!parameterBCombo.getText().equals("")) {

                    }

                    return;
                }
            }
        });

        // add a listener on ecPointscombo to determine which point has been chosen as the initial point of the
        // computation
        eCPointscombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {

                executeButton.setEnabled(false);

                ecPointSelected = allPoints[eCPointscombo.getSelectionIndex()];

                orderOfSelectedECPoint = ecc.getStepsOfPoint(ecPointSelected, paraA, new ECFieldFp(new BigInteger(String.valueOf(primeFieldSelected))));

                orderOfECPointText.setText(ORDER_OF_SELECTED_POINT_TEXT + orderOfSelectedECPoint);

                pPLabel.setText(" P = (" + allPoints[eCPointscombo.getSelectionIndex()].getAffineX() + ","
                        + allPoints[eCPointscombo.getSelectionIndex()].getAffineY() + ")");

                scalarParameterCombo.removeAll();

                final CLabel gLabel = new CLabel(parameterOfECCGroup, SWT.NONE);
                gLabel.setText(TEXT_OF_GLABEL);
                gLabel.setBounds(10, 258, 45, 20);

                int stepOfPoint = 0;

                stepOfPoint = ecc.getStepsOfPoint(ecPointSelected, paraA, new ECFieldFp(new BigInteger(String.valueOf(primeFieldSelected))));

                int j = 2;

                while (stepOfPoint - 2 > 0) {

                    scalarParameterCombo.add(j + "P");
                    j++;
                    stepOfPoint--;

                }
                scalarParameterCombo.setBounds(55, 260, 60, 20);
            }
        });

        // add a cue label
        final CLabel aLabel = new CLabel(parameterOfECCGroup, SWT.NONE);
        aLabel.setText(TEXT_OF_ALABEL);
        aLabel.setBounds(80, 20, 50, 20);

        // add a cue label
        final CLabel bLabel = new CLabel(parameterOfECCGroup, SWT.NONE);
        bLabel.setText(TEXT_OF_BLABEL);
        bLabel.setBounds(150, 20, 50, 20);

        // add a text field which displays the EC with selected primefield, parameterA and parameterB
                // define the layout of powerTraceGroup
        StackLayout stacklayout = new StackLayout();
        powerTraceGroup.setLayout(stacklayout);

        // define a ScrolledComposite with which the horizontal and vertical scrolled-bar can be added.
        final ScrolledComposite scrolledCompositeOfVisual = new ScrolledComposite(powerTraceGroup, SWT.BORDER
                | SWT.H_SCROLL | SWT.V_SCROLL);

        stacklayout.topControl = scrolledCompositeOfVisual;
        powerTraceGroup.layout();

        // define a sub- composite in scrolledcompositeofvisual to display the visualization of power traces
        final Composite powerTraceVisualGroup = new Composite(scrolledCompositeOfVisual, SWT.NONE);
        powerTraceVisualGroup.setSize(645, 280);
        scrolledCompositeOfVisual.setContent(powerTraceVisualGroup);

        // define three kinds of visualization of power traces of different operations

        final Label powertrace_DoubleAndAddLabel = new Label(powerTraceVisualGroup, SWT.NONE);
        powertrace_DoubleAndAddLabel.setBounds(0, 0, 685, 288);
        powertrace_DoubleAndAddLabel.setImage(DPAPlugIn.getImageDescriptor(IMGADDRESSE_TEST_ENG).createImage());

        // add a listener on executeButton, which is used to start the process of selected algorithm
        executeButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(final SelectionEvent e) {

                Q = null;
                outputFlag = 0;
                recordTable.removeAll();

                // kselected saves selected scalar multiplier k (Q = k*P)
                kSelected = scalarParameterCombo.getSelectionIndex() + 2;
                String kInBinaryForm = Integer.toBinaryString(kSelected);
                int counter = 2;

                // declaration three objects to process the computation of algorithm "double and add" of EC
                ECCAdd eccAdd = new ECCAdd();
                ECCDouble eccDouble = new ECCDouble();
                ECCMultiply eccMul = new ECCMultiply();
                ECPoint ecPoint = ecPointSelected;
                int klength = kInBinaryForm.length();
                int counterP = 2;

                // counterFlag = 0 means processing algorithm "double and add"
                if (counterFlag == 0) {

                    final TableItem initialTableItemBasis = new TableItem(recordTable, SWT.BORDER);
                    initialTableItemBasis.setText(0, INITIALTABLEITEM_INPUT);
                    initialTableItemBasis.setText(1, INITIALTABLEITEM_DOUBLE);
                    initialTableItemBasis.setText(2, INITIALTABLEITEM_ADD);

                    // define the contents of recorder table
                    final TableItem initialTableItemInputEC = new TableItem(recordTable, SWT.BORDER);
                    initialTableItemInputEC.setText(0, ECCURVE_TEXT_PART1 + paraA + ECCURVE_TEXT_PART2 + paraB
                            + ECCURVE_TEXT_PART3 + primeFieldSelected + ")");

                    final TableItem initialTableItemInputECPoint = new TableItem(recordTable, SWT.BORDER);
                    initialTableItemInputECPoint.setText(0, INITIAL_TABLE_ITEM_PART_1 + "("
                            + allPoints[eCPointscombo.getSelectionIndex()].getAffineX() + ","
                            + allPoints[eCPointscombo.getSelectionIndex()].getAffineY() + ")");

                    final TableItem initialTableItemInputScalar = new TableItem(recordTable, SWT.BORDER);
                    initialTableItemInputScalar.setText(0, INITIAL_TABLE_ITEM_PART_2 + String.valueOf(kSelected)
                            + DECIMAL_ABBR);

                    final TableItem initialTableItemInputScalarBinary = new TableItem(recordTable, SWT.BORDER);
                    initialTableItemInputScalarBinary.setText(0, INITIAL_TABLE_ITEM_BINARY
                            + String.valueOf(kInBinaryForm) + " (bin.)");

                    final TableItem initialTableItemKinBinary = new TableItem(recordTable, SWT.BORDER);
                    initialTableItemKinBinary.setText(0, kInBinaryForm);

                    final TableItem initialTableItemProcess = new TableItem(recordTable, SWT.BORDER);
                    initialTableItemProcess.setText(0, INITIAL_TABLE_ITEM_PROCESS);

                } else {
                    final TableItem initialTableItemBasis = new TableItem(recordTable, SWT.BORDER);

                    initialTableItemBasis.setText(0, INITIAL_TABLE_ITEM_INPUT);

                    if (counterFlag == 3 || counterFlag == 4) {

                        initialTableItemBasis.setText(1, TEXT_OF_DOUBLE_FORMEL);
                        initialTableItemBasis.setText(2, TEXT_OF_ADD_FORMEL);

                    } else {

                        initialTableItemBasis.setText(1, INITIAL_TABLE_ITEM_DOUBLE);
                        initialTableItemBasis.setText(2, INITIAL_TABLE_ITEM_ADD);

                    }

                    // counterflag = 2 means the processing algorithm is randomized scalar multiplier k
                    if (counterFlag == 2) {

                        RandomFactorCreator rfc = new RandomFactorCreator();
                        int randomFactor = rfc.randomCreator(primeFieldSelected - 1);

                        new_k = kSelected + randomFactor * orderOfSelectedECPoint;
                        newkInBinaryForm = Integer.toBinaryString(new_k);
                        klength = newkInBinaryForm.length();
                        final TableItem initialTableItemInputnewScalarBinary = new TableItem(recordTable, SWT.BORDER);
                        initialTableItemInputnewScalarBinary.setText(0, "k': " + String.valueOf(newkInBinaryForm)
                                + BINARY_ABBR);

                        parameterOfCountermeasuresText.setText("");
                        parameterOfCountermeasuresText.setText(RANDOMIZED_K_TEXT_PART1 + RANDOMIZED_K_TEXT_PART2
                                + paraA + "x + " + paraB + RANDOMIZED_K_TEXT_PART3 + primeFieldSelected
                                + RANDOMIZED_K_TEXT_PART4 + paraA + RANDOMIZED_K_TEXT_PART5 + paraB
                                + RANDOMIZED_K_TEXT_PART6 + "("
                                + allPoints[eCPointscombo.getSelectionIndex()].getAffineX() + ","
                                + allPoints[eCPointscombo.getSelectionIndex()].getAffineY() + ")"
                                + RANDOMIZED_K_TEXT_PART7 + String.valueOf(kSelected) + DECIMAL_ABBR
                                + RANDOMIZED_K_TEXT_PART8 + String.valueOf(kInBinaryForm) + BINARY_ABBR
                                + RANDOMIZED_K_TEXT_PART9 + randomFactor + RANDOMIZED_K_TEXT_PART10
                                + String.valueOf(orderOfSelectedECPoint) + RANDOMIZED_K_TEXT_PART11 + new_k
                                + RANDOMIZED_K_TEXT_PART12 + String.valueOf(newkInBinaryForm) + BINARY_ABBR);

                    }

                    // counterflag = 3 means the processing algorithm is randomized initial point P
                    if (counterFlag == 3) {

                        RandomFactorCreator rfc = new RandomFactorCreator();

                        int randomFactor = rfc.randomCreator(orderOfCurve - 1);

                        while (ecc.getStepsOfPoint(allPoints[randomFactor], paraA, new ECFieldFp(BigInteger.valueOf(primeFieldSelected))) < kSelected
                                || (allPoints[randomFactor].getAffineX().equals(ecPoint.getAffineX()))) {

                            randomFactor = rfc.randomCreator(orderOfCurve - 1);

                        }

                        final TableItem initialTableItemRandomPoint = new TableItem(recordTable, SWT.BORDER);
                        initialTableItemRandomPoint.setText(0, RANDOMIZED_ECPOINT_TEXT_PART1
                                + allPoints[randomFactor].getAffineX() + "," + allPoints[randomFactor].getAffineY()
                                + ")");

                        rplusP = eccAdd.ecAddition(allPoints[randomFactor], ecPoint, new ECFieldFp(BigInteger.valueOf(primeFieldSelected)));

                        originalRPlusP = rplusP;

                        if (originalRPlusP.getAffineY().equals(BigInteger.ZERO)) {

                            recordTable.clearAll();

                            final Point pt = executeButton.toDisplay(executeButton.getLocation());

                            new Thread() {

                                Event event;

                                public void run() {
                                    try {
                                        Thread.sleep(30);
                                    } catch (InterruptedException e) {
                                    }
                                    event = new Event();
                                    event.type = SWT.MouseMove;
                                    event.x = pt.x - 55;
                                    event.y = pt.y - 180;

                                    executeButton.getDisplay().post(event);

                                    try {
                                        Thread.sleep(30);
                                    } catch (InterruptedException e) {
                                    }
                                    event.type = SWT.MouseDown;
                                    event.button = 1;
                                    executeButton.getDisplay().post(event);
                                    try {
                                        Thread.sleep(30);
                                    } catch (InterruptedException e) {
                                    }
                                    event.type = SWT.MouseUp;
                                    executeButton.getDisplay().post(event);
                                }
                            }.start();

                        }

                        final TableItem initialTableItemRandomPointPPlusR = new TableItem(recordTable, SWT.BORDER);
                        initialTableItemRandomPointPPlusR.setText(0, "P + R = " + "(" + rplusP.getAffineX() + ","
                                + rplusP.getAffineY() + ")");

                        kR = eccMul.eccMultiply(allPoints[randomFactor], kSelected, paraA, new ECFieldFp(BigInteger.valueOf(primeFieldSelected)));

                        final TableItem initialTableItemRandomPointS = new TableItem(recordTable, SWT.BORDER);
                        initialTableItemRandomPointS.setText(0, RANDOMIZED_ECPOINT_TEXT_PART2 + kSelected
                                + RANDOMIZED_ECPOINT_TEXT_PART3 + "(" + kR.getAffineX() + "," + kR.getAffineY() + ")");

                        parameterOfCountermeasuresText.setText("");
                        parameterOfCountermeasuresText.setText(RANDOMIZED_ECPOINT_TEXT_PART4
                                + RANDOMIZED_ECPOINT_TEXT_PART5 + paraA + RANDOMIZED_ECPOINT_TEXT_PART6 + paraB
                                + RANDOMIZED_ECPOINT_TEXT_PART7 + primeFieldSelected + RANDOMIZED_ECPOINT_TEXT_PART8
                                + paraA + RANODMIZED_ECPOINT_TEXT_PART9 + paraB + RANODMIZED_ECPOINT_TEXT_PART10 + "("
                                + allPoints[eCPointscombo.getSelectionIndex()].getAffineX() + ","
                                + allPoints[eCPointscombo.getSelectionIndex()].getAffineY() + ")"
                                + RANODMIZED_ECPOINT_TEXT_PART11 + String.valueOf(kSelected) + DECIMAL_ABBR
                                + RANODMIZED_ECPOINT_TEXT_PART12 + String.valueOf(kInBinaryForm) + BINARY_ABBR
                                + RANODMIZED_ECPOINT_TEXT_PART13 + RANODMIZED_ECPOINT_TEXT_PART14
                                + allPoints[randomFactor].getAffineX() + "," + allPoints[randomFactor].getAffineY()
                                + ")" + RANODMIZED_ECPOINT_TEXT_PART15 + "(" + rplusP.getAffineX() + ","
                                + rplusP.getAffineY() + ")" + RANODMIZED_ECPOINT_TEXT_PART16 + kSelected
                                + RANODMIZED_ECPOINT_TEXT_PART17 + "(" + kR.getAffineX() + "," + kR.getAffineY() + ")"
                                + RANODMIZED_ECPOINT_TEXT_PART18 + RANODMIZED_ECPOINT_TEXT_PART19
                                + RANODMIZED_ECPOINT_TEXT_PART20);

                    }

                    // counterflag = 4 means the processing algorithm is randomized isomorphic curve
                    if (counterFlag == 4) {

                        RandomFactorCreator rfc = new RandomFactorCreator();

                        int randomFactor = rfc.randomCreator(orderOfCurve - 1);

                        randomR = randomFactor;

                        newparaA = BigInteger.valueOf(randomFactor).pow(4).mod(BigInteger.valueOf(primeFieldSelected)).multiply(BigInteger.valueOf(paraA)).mod(BigInteger.valueOf(primeFieldSelected)).intValue();
                        newparaB = BigInteger.valueOf(randomFactor).pow(6).mod(BigInteger.valueOf(primeFieldSelected)).multiply(BigInteger.valueOf(paraB)).mod(BigInteger.valueOf(primeFieldSelected)).intValue();

                        BigInteger randomFactorHoch2 = BigInteger.valueOf(randomFactor).pow(2).mod(BigInteger.valueOf(primeFieldSelected));
                        BigInteger randomFactorHoch3 = BigInteger.valueOf(randomFactor).pow(3).mod(BigInteger.valueOf(primeFieldSelected));
                        int newXp = ecPointSelected.getAffineX().intValue() * randomFactorHoch2.intValue();
                        int newYp = ecPointSelected.getAffineY().intValue() * randomFactorHoch3.intValue();

                        newP = new ECPoint(BigInteger.valueOf(newXp).mod(BigInteger.valueOf(primeFieldSelected)), BigInteger.valueOf(newYp).mod(BigInteger.valueOf(primeFieldSelected)));

                        originalNewP = newP;

                        parameterOfCountermeasuresText.setText("");
                        parameterOfCountermeasuresText.setText(RANDOMIZED_ISOMORPHIC_TEXT_PART1
                                + RANDOMIZED_ISOMORPHIC_TEXT_PART2 + paraA + RANDOMIZED_ISOMORPHIC_TEXT_PART3 + paraB
                                + RANDOMIZED_ISOMORPHIC_TEXT_PART4 + primeFieldSelected
                                + RANDOMIZED_ISOMORPHIC_TEXT_PART5 + paraA + RANDOMIZED_ISOMORPHIC_TEXT_PART6 + paraB
                                + RANDOMIZED_ISOMORPHIC_TEXT_PART7 + "("
                                + allPoints[eCPointscombo.getSelectionIndex()].getAffineX() + ","
                                + allPoints[eCPointscombo.getSelectionIndex()].getAffineY() + ")"
                                + RANDOMIZED_ISOMORPHIC_TEXT_PART8 + String.valueOf(kSelected) + DECIMAL_ABBR
                                + RANDOMIZED_ISOMORPHIC_TEXT_PART9 + String.valueOf(kInBinaryForm) + BINARY_ABBR
                                + RANDOMIZED_ISOMORPHIC_TEXT_PART10 + RANDOMIZED_ISOMORPHIC_TEXT_PART11 + randomFactor
                                + RANDOMIZED_ISOMORPHIC_TEXT_PART12 + newparaA + RANDOMIZED_ISOMORPHIC_TEXT_PART13
                                + newparaB + RANDOMIZED_ISOMORPHIC_TEXT_PART14
                                + String.valueOf(BigInteger.valueOf(newXp).mod(BigInteger.valueOf(primeFieldSelected)))
                                + ","
                                + String.valueOf(BigInteger.valueOf(newYp).mod(BigInteger.valueOf(primeFieldSelected)))
                                + ")" + RANDOMIZED_ISOMORPHIC_TEXT_PART15 + RANDOMIZED_ISOMORPHIC_TEXT_PART16
                                + newparaA + RANDOMIZED_ISOMORPHIC_TEXT_PART17 + newparaB);

                    }

                    final TableItem initialTableItemKinBinary = new TableItem(recordTable, SWT.BORDER);
                    initialTableItemKinBinary.setText(0, TEXT_OF_K_IN_BINARYFORM + kInBinaryForm);

                    final TableItem initialTableItemProcess = new TableItem(recordTable, SWT.BORDER);
                    initialTableItemProcess.setText(0, INITIAL_TABLE_ITEM_PROCESS);

                }

                while (klength - 1 > 0) {

                    // counterflag = 0 means the processing algorithm is "double and add"
                    if (counterFlag == 0) {

                        final TableItem tempTableItems = new TableItem(recordTable, SWT.BORDER);
                        tempTableItems.setText(0, "  " + counter + INITIAL_TABLE_ITEM_HIGHEST_BIT
                                + kInBinaryForm.charAt(counter - 1));

                        if (Character.valueOf(kInBinaryForm.charAt(counter - 1)).equals('0')) {

                            ecPoint = eccDouble.eccDouble(ecPoint, paraA, new ECFieldFp(BigInteger.valueOf(primeFieldSelected)));

                            tempTableItems.setText(1, TABLE_ITEM_DOUBLE + counterP + TABLE_ITEM_P_EQUALS
                                    + ecPoint.getAffineX() + "," + ecPoint.getAffineY() + ")");

                        }

                        else if (Character.valueOf(kInBinaryForm.charAt(counter - 1)).equals('1')) {

                            ecPoint = eccDouble.eccDouble(ecPoint, paraA, new ECFieldFp(BigInteger.valueOf(primeFieldSelected)));

                            tempTableItems.setText(1, TABLE_ITEM_DOUBLE + counterP + TABLE_ITEM_P_EQUALS
                                    + ecPoint.getAffineX() + "," + ecPoint.getAffineY() + ")");

                            ecPoint = eccAdd.ecAddition(ecPointSelected, ecPoint, new ECFieldFp(BigInteger.valueOf(primeFieldSelected)));

                            counterP++;

                            tempTableItems.setText(2, TABLE_ITEM_ADD + counterP + TABLE_ITEM_P_EQUALS
                                    + ecPoint.getAffineX() + "," + ecPoint.getAffineY() + ")");

                        }

                        klength--;
                        counter++;
                        counterP = counterP * 2;

                    }
                    // counterflag = 1 means the processing algorithms are "double and add always" and
                    // "randomizing the scalar multiplier"
                    else if (counterFlag == 1 || counterFlag == 2) {

                        if (counterFlag == 2) {
                            kInBinaryForm = newkInBinaryForm;
                        }

                        final TableItem tempTableItems = new TableItem(recordTable, SWT.BORDER);
                        tempTableItems.setText(0, "  " + counter + INITIAL_TABLE_ITEM_HIGHEST_BIT
                                + kInBinaryForm.charAt(counter - 1));

                        ECPoint ecPointbyBit0;

                        ECPoint ecPointbyBit1;

                        // choose a new ecpoint automatically when the randomly chosen point is unsuitable
                        if (ecPoint.getAffineY().intValue() == 0) {

                            recordTable.clearAll();

                            final Point pt = executeButton.toDisplay(executeButton.getLocation());

                            new Thread() {

                                Event event;

                                public void run() {
                                    try {
                                        Thread.sleep(30);
                                    } catch (InterruptedException e) {
                                    }
                                    event = new Event();
                                    event.type = SWT.MouseMove;
                                    event.x = pt.x - 55;
                                    event.y = pt.y - 180;

                                    executeButton.getDisplay().post(event);

                                    try {
                                        Thread.sleep(30);
                                    } catch (InterruptedException e) {
                                    }
                                    event.type = SWT.MouseDown;
                                    event.button = 1;
                                    executeButton.getDisplay().post(event);
                                    try {
                                        Thread.sleep(30);
                                    } catch (InterruptedException e) {
                                    }
                                    event.type = SWT.MouseUp;
                                    executeButton.getDisplay().post(event);
                                }
                            }.start();

                            exceptionFlag = 1;
                            break;

                        }

                        ecPointbyBit0 = eccDouble.eccDouble(ecPoint, paraA, new ECFieldFp(BigInteger.valueOf(primeFieldSelected)));

                        tempTableItems.setText(1, TABLE_ITEM_Q0_DOUBLE + counterP + TABLE_ITEM_P_EQUALS
                                + ecPointbyBit0.getAffineX() + "," + ecPointbyBit0.getAffineY() + ")");

                        // choose a new ecpoint automatically when the randomly chosen point is unsuitable
                        if (Character.valueOf(kInBinaryForm.charAt(counter - 1)).equals('1')
                                && ecPointbyBit0.getAffineX().equals(ecPointSelected.getAffineX())
                                && !ecPointbyBit0.equals(ecPointSelected)) {

                            recordTable.clearAll();

                            final Point pt = executeButton.toDisplay(executeButton.getLocation());

                            new Thread() {

                                Event event;

                                public void run() {
                                    try {
                                        Thread.sleep(30);
                                    } catch (InterruptedException e) {
                                    }
                                    event = new Event();
                                    event.type = SWT.MouseMove;
                                    event.x = pt.x - 55;
                                    event.y = pt.y - 180;

                                    executeButton.getDisplay().post(event);

                                    try {
                                        Thread.sleep(30);
                                    } catch (InterruptedException e) {
                                    }
                                    event.type = SWT.MouseDown;
                                    event.button = 1;
                                    executeButton.getDisplay().post(event);
                                    try {
                                        Thread.sleep(30);
                                    } catch (InterruptedException e) {
                                    }
                                    event.type = SWT.MouseUp;
                                    executeButton.getDisplay().post(event);
                                }
                            }.start();

                            exceptionFlag = 1;
                            break;

                        }

                        if (ecPointSelected.equals(ecPointbyBit0)) {
                            ecPointbyBit1 = eccDouble.eccDouble(ecPointbyBit0, paraA, new ECFieldFp(BigInteger.valueOf(primeFieldSelected)));
                        } else {
                            ecPointbyBit1 = eccAdd.ecAddition(ecPointSelected, ecPointbyBit0, new ECFieldFp(BigInteger.valueOf(primeFieldSelected)));
                        }

                        tempTableItems.setText(2, TABLE_ITEM_Q1_DOUBLE + (counterP + 1) + TABLE_ITEM_P_EQUALS
                                + ecPointbyBit1.getAffineX() + "," + ecPointbyBit1.getAffineY() + ")");

                        if (Character.valueOf(kInBinaryForm.charAt(counter - 1)).equals('0')) {

                            ecPoint = ecPointbyBit0;
                            counterP = counterP * 2;

                        } else if (Character.valueOf(kInBinaryForm.charAt(counter - 1)).equals('1')) {

                            ecPoint = ecPointbyBit1;
                            counterP = (counterP + 1) * 2;

                        }

                        klength--;
                        counter++;

                    }
                    // counterFlag == 3 stands for the "randomizing the initial point"
                    else if (counterFlag == 3) {

                        ECPoint ecPointbyBit0;

                        ECPoint ecPointbyBit1;

                        ecPointbyBit0 = eccDouble.eccDouble(rplusP, paraA, new ECFieldFp(BigInteger.valueOf(primeFieldSelected)));

                        final TableItem tempTableItems = new TableItem(recordTable, SWT.BORDER);
                        tempTableItems.setText(0, "  " + counter + INITIAL_TABLE_ITEM_HIGHEST_BIT
                                + kInBinaryForm.charAt(counter - 1));

                        tempTableItems.setText(1, TABLE_ITEM_Q0_DOUBLE_NEW + counterP + TABLE_ITEM_P_PLUS_R
                                + ecPointbyBit0.getAffineX() + "," + ecPointbyBit0.getAffineY() + ")");

                        if (rplusP.equals(ecPointbyBit0)) {
                            ecPointbyBit1 = eccDouble.eccDouble(ecPointbyBit0, paraA, new ECFieldFp(BigInteger.valueOf(primeFieldSelected)));
                        }

                        // choose a new ecpoint automatically when the randomly chosen point is unsuitable
                        else if (ecPointbyBit0.getAffineY().equals(BigInteger.ZERO)) {

                            recordTable.clearAll();

                            final Point pt = executeButton.toDisplay(executeButton.getLocation());

                            new Thread() {

                                Event event;

                                public void run() {
                                    try {
                                        Thread.sleep(30);
                                    } catch (InterruptedException e) {
                                    }
                                    event = new Event();
                                    event.type = SWT.MouseMove;
                                    event.x = pt.x - 55;
                                    event.y = pt.y - 180;

                                    executeButton.getDisplay().post(event);

                                    try {
                                        Thread.sleep(30);
                                    } catch (InterruptedException e) {
                                    }
                                    event.type = SWT.MouseDown;
                                    event.button = 1;
                                    executeButton.getDisplay().post(event);
                                    try {
                                        Thread.sleep(30);
                                    } catch (InterruptedException e) {
                                    }
                                    event.type = SWT.MouseUp;
                                    executeButton.getDisplay().post(event);
                                }
                            }.start();

                            outputFlag = 1;

                            break;
                        }

                        // choose a new ecpoint automatically when the randomly chosen point is unsuitable
                        else if (originalRPlusP.equals(ecPointbyBit0)) {

                            recordTable.clearAll();

                            final Point pt = executeButton.toDisplay(executeButton.getLocation());

                            new Thread() {

                                Event event;

                                public void run() {
                                    try {
                                        Thread.sleep(30);
                                    } catch (InterruptedException e) {
                                    }
                                    event = new Event();
                                    event.type = SWT.MouseMove;
                                    event.x = pt.x - 55;
                                    event.y = pt.y - 180;

                                    executeButton.getDisplay().post(event);

                                    try {
                                        Thread.sleep(30);
                                    } catch (InterruptedException e) {
                                    }
                                    event.type = SWT.MouseDown;
                                    event.button = 1;
                                    executeButton.getDisplay().post(event);
                                    try {
                                        Thread.sleep(30);
                                    } catch (InterruptedException e) {
                                    }
                                    event.type = SWT.MouseUp;
                                    executeButton.getDisplay().post(event);
                                }
                            }.start();

                            outputFlag = 1;

                            break;

                        }

                        else if (!(originalRPlusP.equals(ecPointbyBit0))
                                && originalRPlusP.getAffineX().equals(ecPointbyBit0.getAffineX())) {

                            recordTable.clearAll();

                            final Point pt = executeButton.toDisplay(executeButton.getLocation());

                            new Thread() {

                                Event event;

                                public void run() {
                                    try {
                                        Thread.sleep(30);
                                    } catch (InterruptedException e) {
                                    }
                                    event = new Event();
                                    event.type = SWT.MouseMove;
                                    event.x = pt.x - 55;
                                    event.y = pt.y - 180;

                                    executeButton.getDisplay().post(event);

                                    try {
                                        Thread.sleep(30);
                                    } catch (InterruptedException e) {
                                    }
                                    event.type = SWT.MouseDown;
                                    event.button = 1;
                                    executeButton.getDisplay().post(event);
                                    try {
                                        Thread.sleep(30);
                                    } catch (InterruptedException e) {
                                    }
                                    event.type = SWT.MouseUp;
                                    executeButton.getDisplay().post(event);
                                }
                            }.start();

                            outputFlag = 1;

                            break;

                        }

                        else {

                            ecPointbyBit1 = eccAdd.ecAddition(originalRPlusP, ecPointbyBit0, new ECFieldFp(BigInteger.valueOf(primeFieldSelected)));

                            if (ecPointbyBit1.getAffineY().equals(BigInteger.ZERO)) {

                                recordTable.clearAll();

                                final Point pt = executeButton.toDisplay(executeButton.getLocation());

                                new Thread() {

                                    Event event;

                                    public void run() {
                                        try {
                                            Thread.sleep(30);
                                        } catch (InterruptedException e) {
                                        }
                                        event = new Event();
                                        event.type = SWT.MouseMove;
                                        event.x = pt.x - 55;
                                        event.y = pt.y - 180;

                                        executeButton.getDisplay().post(event);

                                        try {
                                            Thread.sleep(30);
                                        } catch (InterruptedException e) {
                                        }
                                        event.type = SWT.MouseDown;
                                        event.button = 1;
                                        executeButton.getDisplay().post(event);
                                        try {
                                            Thread.sleep(30);
                                        } catch (InterruptedException e) {
                                        }
                                        event.type = SWT.MouseUp;
                                        executeButton.getDisplay().post(event);
                                    }
                                }.start();

                                outputFlag = 1;

                                break;

                            }

                        }

                        tempTableItems.setText(2, TABLE_ITEM_Q1_ADD_NEW1 + (counterP + 1) + TABLE_ITEM_P_PLUS_R
                                + ecPointbyBit1.getAffineX() + "," + ecPointbyBit1.getAffineY() + ")");

                        // process the doubling operation when the current bit is 0;
                        if (Character.valueOf(kInBinaryForm.charAt(counter - 1)).equals('0')) {

                            rplusP = ecPointbyBit0;
                            counterP = counterP * 2;

                        }

                        // process the addition operation when the current bit is 1;
                        else if (Character.valueOf(kInBinaryForm.charAt(counter - 1)).equals('1')) {

                            rplusP = ecPointbyBit1;
                            counterP = (counterP + 1) * 2;

                        }

                        klength--;
                        counter++;

                    }

                    // counterFlag == 4 stands for the countermeasure "randomizing the isomorphic curve"
                    else if (counterFlag == 4) {

                        final TableItem tempTableItems = new TableItem(recordTable, SWT.BORDER);
                        tempTableItems.setText(0, "  " + counter + INITIAL_TABLE_ITEM_HIGHEST_BIT
                                + kInBinaryForm.charAt(counter - 1));

                        ECPoint ecPointbyBit0;

                        ECPoint ecPointbyBit1;

                        ecPointbyBit0 = eccDouble.eccDouble(newP, (int) newparaA, new ECFieldFp(BigInteger.valueOf(primeFieldSelected)));

                        tempTableItems.setText(1, TABLE_ITEM_Q0_DOUBLE_NEW + counterP + TABLE_ITEM_P_NEW_EQUALS
                                + ecPointbyBit0.getAffineX() + "," + ecPointbyBit0.getAffineY() + ")");

                        if (ecPointSelected.equals(ecPointbyBit0)) {
                            ecPointbyBit1 = eccDouble.eccDouble(ecPointbyBit0, (int) newparaA, new ECFieldFp(BigInteger.valueOf(primeFieldSelected)));
                        } else {
                            ecPointbyBit1 = eccAdd.ecAddition(originalNewP, ecPointbyBit0, new ECFieldFp(BigInteger.valueOf(primeFieldSelected)));
                        }

                        tempTableItems.setText(2, TABLE_ITEM_Q1_ADD_NEW2 + (counterP + 1) + TABLE_ITEM_P_NEW_EQUALS
                                + ecPointbyBit1.getAffineX() + "," + ecPointbyBit1.getAffineY() + ")");

                        if (Character.valueOf(kInBinaryForm.charAt(counter - 1)).equals('0')) {

                            newP = ecPointbyBit0;
                            counterP = counterP * 2;

                        } else if (Character.valueOf(kInBinaryForm.charAt(counter - 1)).equals('1')) {
                            newP = ecPointbyBit1;
                            counterP = (counterP + 1) * 2;

                        }

                        klength--;
                        counter++;

                    }

                }

                final TableItem initialTableItemOutput = new TableItem(recordTable, SWT.BORDER);

                if (exceptionFlag != 1)
                    initialTableItemOutput.setText(0, TABLE_ITEM_OUTPUT);

                if (counterFlag == 3) {

                    if (outputFlag == 1) {

                    }

                    else {
                        final TableItem initialTableItemnewGStep1 = new TableItem(recordTable, SWT.BORDER);
                        initialTableItemnewGStep1.setText(0, TABLE_ITEM_NEW_G_PART1 + "(" + rplusP.getAffineX() + ","
                                + rplusP.getAffineY() + ")");

                        final TableItem initialTableItemnewGStep2 = new TableItem(recordTable, SWT.BORDER);
                        initialTableItemnewGStep2.setText(0, TABLE_ITEM_NEW_G_PART2 + "(" + kR.getAffineX() + ","
                                + kR.getAffineY().negate() + ")");

                        ECPoint minS = new ECPoint(new BigInteger(kR.getAffineX().toString()), new BigInteger(kR.getAffineY().negate().toString()));

                        Q = eccAdd.ecAddition(rplusP, minS, new ECFieldFp(BigInteger.valueOf(primeFieldSelected)));

                        // choose a new ecpoint automatically when the randomly chosen point is unsuitable: the X-Axis
                        // value of S = kR and Q' = k(P+R) is identical
                        if (Q.equals(ECPoint.POINT_INFINITY)) {

                            recordTable.clearAll();

                            final Point pt = executeButton.toDisplay(executeButton.getLocation());

                            new Thread() {

                                Event event;

                                public void run() {
                                    try {
                                        Thread.sleep(30);
                                    } catch (InterruptedException e) {
                                    }
                                    event = new Event();
                                    event.type = SWT.MouseMove;
                                    event.x = pt.x - 55;
                                    event.y = pt.y - 180;

                                    executeButton.getDisplay().post(event);

                                    try {
                                        Thread.sleep(30);
                                    } catch (InterruptedException e) {
                                    }
                                    event.type = SWT.MouseDown;
                                    event.button = 1;
                                    executeButton.getDisplay().post(event);
                                    try {
                                        Thread.sleep(30);
                                    } catch (InterruptedException e) {
                                    }
                                    event.type = SWT.MouseUp;
                                    executeButton.getDisplay().post(event);
                                }
                            }.start();

                        } else {
                            final TableItem initialTableItemnewGStep3 = new TableItem(recordTable, SWT.BORDER);
                            initialTableItemnewGStep3.setText(0, TABLE_ITEM_NEW_G_PART3);

                            final TableItem initialTableItemnewGStep4 = new TableItem(recordTable, SWT.BORDER);
                            initialTableItemnewGStep4.setText(0, TABLE_ITEM_Q_EQUALS + rplusP.getAffineX() + ","
                                    + rplusP.getAffineY() + ") + " + "(" + kR.getAffineX() + ","
                                    + kR.getAffineY().negate() + ") = (" + Q.getAffineX() + "," + Q.getAffineY() + ")");
                        }
                    }

                }
                // counterFlag == 4 stands for "randomizing the isomorphic curve"
                else if (counterFlag == 4) {

                    final TableItem initialTableItemOutputStep1 = new TableItem(recordTable, SWT.BORDER);
                    initialTableItemOutputStep1.setText(0, TABLE_ITEM_NEW_Q + "(" + newP.getAffineX() + ","
                            + newP.getAffineY() + ") ");

                    final TableItem initialTableItemOutputStep2 = new TableItem(recordTable, SWT.BORDER);
                    initialTableItemOutputStep2.setText(0, TABLE_ITEM_Q_EQUALS + newP.getAffineX() + "*" + randomR
                            + " " + UNICODE_1 + UNICODE_2 + "," + newP.getAffineY() + "*" + randomR + " " + UNICODE_1
                            + UNICODE_3 + ")");

                    final TableItem initialTableItemExecution = new TableItem(recordTable, SWT.BORDER);

                    long Xq = BigInteger.valueOf(randomR).pow(2).mod(BigInteger.valueOf(primeFieldSelected)).modInverse(BigInteger.valueOf(primeFieldSelected)).multiply(newP.getAffineX()).mod(BigInteger.valueOf(primeFieldSelected)).intValue();

                    long Yq = BigInteger.valueOf(randomR).pow(3).mod(BigInteger.valueOf(primeFieldSelected)).modInverse(BigInteger.valueOf(primeFieldSelected)).multiply(newP.getAffineY()).mod(BigInteger.valueOf(primeFieldSelected)).intValue();

                    initialTableItemExecution.setText(0, TABLE_ITEM_Xq_EQUALS + Xq + ", " + TABLE_ITEM_Yq_EQUALS + Yq);

                    final TableItem initialTableItemOutputStep3 = new TableItem(recordTable, SWT.BORDER);
                    initialTableItemOutputStep3.setText(0, TABLE_ITEM_Q_EQUALS + Xq + "," + Yq + ")");

                } else if (exceptionFlag == 1) {

                    exceptionFlag = 0;

                } else {

                    initialTableItemOutput.setText(0, TABLE_ITEM_Q_EQUALS + ecPoint.getAffineX() + ","
                            + ecPoint.getAffineY() + ")");

                }
            }
        });

        scalarParameterCombo.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {

                executeButton.setEnabled(true);

            }

        });

        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent.getShell(), DPA_PLUGIN_ID);

    }

    /**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus() {

    }
}