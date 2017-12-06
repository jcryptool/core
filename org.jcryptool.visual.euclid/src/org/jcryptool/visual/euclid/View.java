// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.euclid;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.core.util.fonts.FontService;

/**
 * @author Felix Eckardt
 */
public class View extends ViewPart {
	public View() {
	}
    /*
     *  name_1 refers to the reciprocal subtraction
     *  name_2 refers to the extended euclidean algorithm
     */
    private Composite parent;
    private Action exportToLatexAction;
    private Action exportToCSVAction;
    private Action exportToPdfAction;
    private Action restartAction;
    private ScrolledComposite scrolledComposite;
    private Composite composite_1;
    private Label lblHeader_1;
    private TabFolder tabFolder;
    private TabItem tbtmEuclidean;
    private StyledText txtDescription_1;
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
    
    private TabItem tbtmXEuclidean;
    private Composite composite_2;
    private Label lblHeader_2;
    private StyledText txtDescription_2;
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
    
    private IMenuManager menuManager;
    private IToolBarManager toolBarManager;
    
    private static final Color WHITE = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
    private static final Color BLACK = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
    private static final Color GREEN = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
    private static final Color RED = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
    private static final Color BLUE = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
    private static final Color MAGENTA = Display.getCurrent().getSystemColor(SWT.COLOR_MAGENTA);
    
    private ImageBuffer imageBuffer;
    private ArrayList<int[]> values;
    private int step = -1;
    private int state;


    private void initialize_1() {        
        values = new ArrayList<int[]>();
        int p = Integer.parseInt(textP_1.getText());
        int q = Integer.parseInt(textQ_1.getText());
        int n;
        int r;
        if (p>q) {
            values.add(new int[]{p,q});
            n = p/q;
            r = p%q;
            values.add(new int[]{p, q, n, r});
        }
        else {
            values.add(new int[]{q,p});
            n = q/p;
            r = q%p;
            values.add(new int[]{q, p, n, r});
        }
        
        
        while (values.get(values.size()-1)[3]!=0) {
            p = values.get(values.size()-1)[1];
            q = values.get(values.size()-1)[3];
            n = p/q;
            r = p%q;
            values.add(new int[]{p, q, n, r});
        }
        
        scrolledComposite_canvas.setMinSize(canvas.computeSize(20+5*values.get(0)[0], 100+45*values.size()));
        Device device = this.getSite().getShell().getDisplay();
        imageBuffer = new ImageBuffer(device, canvas.getSize().x, canvas.getSize().y);
        imageBuffer.paintImage(values);
        
        btnResetAll_1.setEnabled(true);
    }
    
    private void nextStep_1() {
        step++;
        
        if (step==0)
            initialize_1();
        
        //paint(step);
        canvas.redraw();
        canvas.update();
       
        if (step>0)
            btnPrevStep_1.setEnabled(true);
        if (step == values.size()-1) {
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
        if (step==-1)
            initialize_1();
        
        step=values.size()-1;
        
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
        canvas.redraw();
        canvas.update();
    }
    
/*    private void paint(final int s) {        
        if(s==0) {
            canvas.addPaintListener(new PaintListener() {
                public void paintControl(PaintEvent e) {
                    e.gc.setForeground(BLACK);
                    e.gc.drawText(Messages.Euclid_Long_Line+values.get(0)[0], 10, 10, true);
                    e.gc.drawText(Messages.Euclid_Short_Line+values.get(0)[1], 10, 40, true);
                    drawLine(10, 30, values.get(0)[0], GREEN, e.gc);
                    drawLine(10, 60, values.get(0)[1], RED, e.gc);
                }
            });
            canvas.redraw();
        } else {
            canvas.addPaintListener(new PaintListener() {
                public void paintControl(PaintEvent e) {
                    int i=s;
                    e.gc.setForeground(BLACK);
                    e.gc.drawText(values.get(i)[0]+" - "+values.get(i)[2]+"*"+values.get(i)[1]+" = "+values.get(i)[3], 10, 75+45*(i-1), true);
                    drawLine(10, 95+45*(i-1), values.get(i)[0], GREEN, e.gc);
                    for(int j=0; j<values.get(i)[2]; j++) {
                        drawLine(10+5*j*values.get(i)[1], 105+45*(i-1)+(j%2)*2, values.get(i)[1], RED, e.gc);
                    }
                    
                    if (values.get(i)[3]==0) {
                        e.gc.setForeground(BLACK);
                        e.gc.drawText(Messages.Euclid_GCD+values.get(0)[0]+","+values.get(0)[1]+") = "+values.get(i)[1], 10, 75+45*(i), true);
                    } else {
                        drawLine(10+5*values.get(i)[1]*values.get(i)[2], 105+45*(i-1), values.get(i)[3], BLUE, e.gc);
                    }
                }
            });
        }
        canvas.redraw(0, 75+45*(s-1), 20+5*values.get(0)[0], 75+45*s, false);
        canvas.update();
    }*/
    
    private void initialize_2() {
        TableItem tableItem1 = new TableItem(table, SWT.BORDER);
        TableItem tableItem2 = new TableItem(table, SWT.BORDER);
        int p = Integer.parseInt(textP_2.getText());
        int q = Integer.parseInt(textQ_2.getText());
        if (p>q) {
            tableItem1.setText(0, "0");
            tableItem1.setText(2, ""+p);
            tableItem1.setText(3, "1");
            tableItem1.setText(4, "0");
            tableItem2.setText(0, "1");
            tableItem2.setText(2, ""+q);
            tableItem2.setText(3, "0");
            tableItem2.setText(4, "1");
        } else {
            tableItem1.setText(0, "0");
            tableItem1.setText(2, ""+q);
            tableItem1.setText(3, "1");
            tableItem1.setText(4, "0");
            tableItem2.setText(0, "1");
            tableItem2.setText(2, ""+p);
            tableItem2.setText(3, "0");
            tableItem2.setText(4, "1");
        }
        state=0;
        btnNextStep_2.setEnabled(true);
        btnCompute_2.setEnabled(true);
        btnResetTable_2.setEnabled(true);
    }
    
    private void nextStep_2() {
        if (table.getItemCount()==0) {
            initialize_2();
            return;
        }
        
        StyleRange stylerange_1 = new StyleRange();
        StyleRange stylerange_2 = new StyleRange();
        StyleRange stylerange_3 = new StyleRange();
        StyleRange stylerange_4 = new StyleRange();
        
        TableItem item0;
        if (table.getItemCount()<3)
            item0 = table.getItem(table.getItemCount()-1);
        else
            item0 = table.getItem(table.getItemCount()-3);
        TableItem item1 = table.getItem(table.getItemCount()-2);
        TableItem item2 = table.getItem(table.getItemCount()-1);
        
        clearTblItems();
        
        switch (state) {
        case 0: //Quotient
            int q1 = Integer.parseInt(item1.getText(2));
            int q2 = Integer.parseInt(item2.getText(2));
            int q = q1/q2;
            item2.setText(1, ""+q);
            item1.setForeground(2, GREEN);
            item2.setForeground(2, RED);
            item2.setForeground(1, BLUE);
            
            styledText.setText(Messages.Euclid_Quotient+": "+q1+"/"+q2+" = "+q);
            stylerange_1.start = (Messages.Euclid_Quotient+": ").length();
            stylerange_1.length = (""+q1).length();
            stylerange_1.foreground = GREEN;
            stylerange_1.fontStyle = SWT.BOLD;
            styledText.setStyleRange(stylerange_1);
            
            stylerange_2.start = (Messages.Euclid_Quotient+": "+q1+"/").length();
            stylerange_2.length = (""+q2).length();
            stylerange_2.foreground = RED;
            stylerange_2.fontStyle = SWT.BOLD;
            styledText.setStyleRange(stylerange_2);
            
            stylerange_3.start = (Messages.Euclid_Quotient+": "+q1+"/"+q2+" = ").length();
            stylerange_3.length = (""+q).length();
            stylerange_3.foreground = BLUE;
            stylerange_3.fontStyle = SWT.BOLD;
            styledText.setStyleRange(stylerange_3);
            
            state = 1;
            break;
        case 1: //Remainder
            int r1 = Integer.parseInt(item1.getText(2));
            int r2 = Integer.parseInt(item2.getText(2));
            int r = r1%r2;
            TableItem new_item = new TableItem(table, SWT.BORDER);
            new_item.setText(0, ""+(Integer.parseInt(item2.getText(0))+1));
            new_item.setText(2, ""+r);
            item1.setForeground(2, GREEN);
            item2.setForeground(2, RED);
            new_item.setForeground(2, BLUE);
            
            styledText.setText(Messages.Euclid_Remainder+": "+r1+"%"+r2+" = "+r);
            stylerange_1.start = (Messages.Euclid_Remainder+": ").length();
            stylerange_1.length = (""+r1).length();
            stylerange_1.foreground = GREEN;
            stylerange_1.fontStyle = SWT.BOLD;
            styledText.setStyleRange(stylerange_1);
            
            stylerange_2.start = (Messages.Euclid_Remainder+": "+r1+"/").length();
            stylerange_2.length = (""+r2).length();
            stylerange_2.foreground = RED;
            stylerange_2.fontStyle = SWT.BOLD;
            styledText.setStyleRange(stylerange_2);
            
            stylerange_3.start = (Messages.Euclid_Remainder+": "+r1+"/"+r2+" = ").length();
            stylerange_3.length = (""+r).length();
            stylerange_3.foreground = BLUE;
            stylerange_3.fontStyle = SWT.BOLD;
            styledText.setStyleRange(stylerange_3);
            
            state = 2;
            break;
        case 2: //X
            item0 = table.getItem(table.getItemCount()-3);
            int x1 = Integer.parseInt(item0.getText(3));
            int x2 = Integer.parseInt(item1.getText(3));
            int q3 = Integer.parseInt(item1.getText(1));
            int x = x1-q3*Integer.parseInt(item1.getText(3));
            item2.setText(3, ""+x);
            item0.setForeground(3, GREEN);
            item1.setForeground(1, MAGENTA);
            item1.setForeground(3, RED);
            item2.setForeground(3, BLUE);
            
            String x1_text = ""+x1;
            if (x1<0)
                x1_text = "("+x1+")";
            String x2_text = ""+x2;
            if (x2<0)
                x2_text = "("+x2+")";
            
            styledText.setText("x: "+x1_text+"-"+q3+"*"+x2_text+" = "+x);
            stylerange_1.start = ("x: ").length();
            stylerange_1.length = (x1_text).length();
            stylerange_1.foreground = GREEN;
            stylerange_1.fontStyle = SWT.BOLD;
            styledText.setStyleRange(stylerange_1);
            
            stylerange_2.start = ("x: "+x1_text+"-").length();
            stylerange_2.length = (""+q3).length();
            stylerange_2.foreground = MAGENTA;
            stylerange_2.fontStyle = SWT.BOLD;
            styledText.setStyleRange(stylerange_2);
            
            stylerange_3.start = ("x: "+x1_text+"-"+q3+"*").length();
            stylerange_3.length = (x2_text).length();
            stylerange_3.foreground = RED;
            stylerange_3.fontStyle = SWT.BOLD;
            styledText.setStyleRange(stylerange_3);
           
            stylerange_4.start = ("x: "+x1_text+"-"+q3+"*"+x2_text+" = ").length();
            stylerange_4.length = (""+x).length();
            stylerange_4.foreground = BLUE;
            stylerange_4.fontStyle = SWT.BOLD;
            styledText.setStyleRange(stylerange_4);
            
            state = 3;
            break;
        case 3: //Y
            item0 = table.getItem(table.getItemCount()-3);
            int y1 = Integer.parseInt(item0.getText(4));
            int y2 = Integer.parseInt(item1.getText(4));
            int q4 = Integer.parseInt(item1.getText(1));
            int y = y1-q4*y2;
            item2.setText(4, ""+y);
            item0.setForeground(4, GREEN);
            item1.setForeground(1, MAGENTA);
            item1.setForeground(4, RED);
            item2.setForeground(4, BLUE);
            
            String y1_text = ""+y1;
            if (y1<0)
                y1_text = "("+y1+")";
            String y2_text = ""+y2;
            if (y2<0)
                y2_text = "("+y2+")";
            
            styledText.setText("y: "+y1_text+"-"+q4+"*"+y2_text+" = "+y);
            stylerange_1.start = ("y: ").length();
            stylerange_1.length = (y1_text).length();
            stylerange_1.foreground = GREEN;
            stylerange_1.fontStyle = SWT.BOLD;
            styledText.setStyleRange(stylerange_1);
            
            stylerange_2.start = ("y: "+y1_text+"-").length();
            stylerange_2.length = (""+q4).length();
            stylerange_2.foreground = MAGENTA;
            stylerange_2.fontStyle = SWT.BOLD;
            styledText.setStyleRange(stylerange_2);
            
            stylerange_3.start = ("y: "+y1_text+"-"+q4+"*").length();
            stylerange_3.length = (y2_text).length();
            stylerange_3.foreground = RED;
            stylerange_3.fontStyle = SWT.BOLD;
            styledText.setStyleRange(stylerange_3);
           
            stylerange_4.start = ("y: "+y1_text+"-"+q4+"*"+y2_text+" = ").length();
            stylerange_4.length = (""+y).length();
            stylerange_4.foreground = BLUE;
            stylerange_4.fontStyle = SWT.BOLD;
            styledText.setStyleRange(stylerange_4);
            
            if (item2.getText(2).equals("0"))
                state = 4;
            else
                state = 0;            
            break;
        case 4: //Finished
            item1.setForeground(2, BLUE);
            item1.setForeground(3, GREEN);
            item1.setForeground(4, RED);
            
            String a = table.getItem(0).getText(2);
            String b = table.getItem(1).getText(2);
            r = Integer.parseInt(item1.getText(2));
            x = Integer.parseInt(item1.getText(3));
            String x_text = ""+x;
            if (x<0)
                x_text = "("+x+")";
            y = Integer.parseInt(item1.getText(4));
            String y_text = ""+y;
            if (y<0)
                y_text = "("+y+")";
            
            styledText.setText(Messages.Euclid_GCD+a+","+b+") = "+x_text+"*"+a+" + "+y_text+"*"+b+" = "+r);
            StyleRange stylerange_x = new StyleRange();
            stylerange_x.start = (Messages.Euclid_GCD+a+","+b+") = ").length();
            stylerange_x.length = x_text.length();
            stylerange_x.foreground = GREEN;
            stylerange_x.fontStyle = SWT.BOLD;
            styledText.setStyleRange(stylerange_x);
            
            StyleRange stylerange_y = new StyleRange();
            stylerange_y.start = (Messages.Euclid_GCD+a+","+b+") = "+x_text+"*"+a+" + ").length();
            stylerange_y.length = y_text.length();
            stylerange_y.foreground = RED;
            stylerange_y.fontStyle = SWT.BOLD;
            styledText.setStyleRange(stylerange_y);
            
            StyleRange stylerange_r = new StyleRange();
            stylerange_r.start = (Messages.Euclid_GCD+a+","+b+") = "+x_text+"*"+a+" + "+y_text+"*"+b+" = ").length();
            stylerange_r.length = (""+r).length();
            stylerange_r.foreground = BLUE;
            stylerange_r.fontStyle = SWT.BOLD;
            styledText.setStyleRange(stylerange_r);
            
            state=5;
            
            btnNextStep_2.setEnabled(false);
            btnCompute_2.setEnabled(false);
            exportToCSVAction.setEnabled(true);
            exportToPdfAction.setEnabled(true);
            exportToLatexAction.setEnabled(true);
            break;
        }
        btnPreviousStep_2.setEnabled(true);
        btnResetTable_2.setEnabled(true);
    }
    
    private void prevStep_2() {        
        TableItem item1 = table.getItem(table.getItemCount()-1);
        
        clearTblItems();

        switch (state) {
            case 0: //Remove Y
                item1.setText(4, "");
                state = 2;
                nextStep_2();
                break;
            case 1: //Remove Quotient
                item1.setText(1, "");
                if (table.getItemCount()==2) {
                    styledText.setText("");
                    state = 0;
                    btnPreviousStep_2.setEnabled(false);
                } else {
                    state = 3;
                    nextStep_2();
                }
                break;
            case 2: //Remove Remainder
                table.remove(table.getItemCount()-1);
                state = 0;
                nextStep_2();
                break;
            case 3: //Remove X
                table.remove(table.getItemCount()-1);
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
        exportToCSVAction.setEnabled(false);
        exportToPdfAction.setEnabled(false);
        exportToLatexAction.setEnabled(false);
    }
    
    private void compute_2() {
        resetTable_2();
        initialize_2();
        TableItem item;
        item = table.getItem(table.getItemCount()-2);
        int r1 = Integer.parseInt(item.getText(2));
        int x1 = Integer.parseInt(item.getText(3));
        int y1 = Integer.parseInt(item.getText(4));
        item = table.getItem(table.getItemCount()-1);
        int r2 = Integer.parseInt(item.getText(2));
        int x2 = Integer.parseInt(item.getText(3));
        int y2 = Integer.parseInt(item.getText(4));
        int q, tmp;
        while (true) {
            //Quotient
            q = r1/r2;
            item.setText(1, ""+q);
            //Remainder
            tmp = r1%r2;
            r1 = r2;
            r2 = tmp;
            item = new TableItem(table, SWT.BORDER);
            item.setText(0, ""+(Integer.parseInt(table.getItem(table.getItemCount()-2).getText(0))+1));
            item.setText(2, ""+tmp);
            //X
            tmp = x1-q*x2;
            x1 = x2;
            x2 = tmp;
            item.setText(3, ""+x2);
            //Y
            tmp = y1-q*y2;
            y1 = y2;
            y2 = tmp;
            item.setText(4, ""+y2);
            if (r2==0)
                break;
        }
        
        item = table.getItem(table.getItemCount()-2);
        item.setForeground(2, BLUE);
        item.setForeground(3, GREEN);
        item.setForeground(4, RED);
        
        String a = table.getItem(0).getText(2);
        String b = table.getItem(1).getText(2);
        String r = ""+r1;
        String x = ""+x1;
        if (x1<0)
            x = "("+x+")";
        String y = ""+y1;
        if (y1<0)
            y = "("+y+")";
        
        styledText.setText(Messages.Euclid_GCD+a+","+b+") = "+x+"*"+a+" + "+y+"*"+b+" = "+r);
        StyleRange stylerange_x = new StyleRange();
        stylerange_x.start = (Messages.Euclid_GCD+a+","+b+") = ").length();
        stylerange_x.length = x.length();
        stylerange_x.foreground = GREEN;
        stylerange_x.fontStyle = SWT.BOLD;
        styledText.setStyleRange(stylerange_x);
        
        StyleRange stylerange_y = new StyleRange();
        stylerange_y.start = (Messages.Euclid_GCD+a+","+b+") = "+x+"*"+a+" + ").length();
        stylerange_y.length = y.length();
        stylerange_y.foreground = RED;
        stylerange_y.fontStyle = SWT.BOLD;
        styledText.setStyleRange(stylerange_y);
        
        StyleRange stylerange_r = new StyleRange();
        stylerange_r.start = (Messages.Euclid_GCD+a+","+b+") = "+x+"*"+a+" + "+y+"*"+b+" = ").length();
        stylerange_r.length = r.length();
        stylerange_r.foreground = BLUE;
        stylerange_r.fontStyle = SWT.BOLD;
        styledText.setStyleRange(stylerange_r);
        
        btnNextStep_2.setEnabled(false);
        btnPreviousStep_2.setEnabled(true);
        btnCompute_2.setEnabled(false);
        btnResetTable_2.setEnabled(true);
        exportToCSVAction.setEnabled(true);
        exportToPdfAction.setEnabled(true);
        exportToLatexAction.setEnabled(true);
        state = 5;
    }
    
    private void resetTable_2() {
        styledText.setText("");
        table.removeAll();
        table.setItemCount(0);
    }
    
    private void clearTblItems() {
        for(int i=1; i<=table.getItemCount(); i++)
            for(int j=0; j<5; j++)
                table.getItem(table.getItemCount()-i).setForeground(j, BLACK);
    }

    private String[] exportArray() {
        String[] array = new String[table.getItemCount()*5];
        TableItem item;
        for(int i=0; i<table.getItemCount(); i++) {
            item = table.getItem(i);
            for(int j=0; j<5; j++)
                array[i*5+j] = item.getText(j);
        }
        
        return array;
    }
    
    private void createActions() {
        exportToPdfAction = new Action(Messages.Euclid_Export_PDF) {
            public void run() {
                FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
                dialog.setFilterPath(DirectoryService.getUserHomeDir());
                dialog.setFileName(Messages.Euclid_SaveDialog+textP_2.getText()+"_"+textQ_2.getText()+".pdf");
                dialog.setFilterExtensions(new String[] {IConstants.PDF_FILTER_EXTENSION});
                dialog.setFilterNames(new String[] {IConstants.PDF_FILTER_NAME});
                dialog.setOverwrite(true);

                String filename = dialog.open();

                if (filename != null) {
                    FileExporter pdfExport = new FileExporter(exportArray(), filename);
                    pdfExport.exportToPDF();
                }
            }
        };
        exportToPdfAction.setEnabled(false);

        exportToCSVAction = new Action(Messages.Euclid_Export_CSV) {
            public void run() {
                FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
                dialog.setFilterPath(DirectoryService.getUserHomeDir());
                dialog.setFileName(Messages.Euclid_SaveDialog+textP_2.getText()+"_"+textQ_2.getText()+".csv");
                dialog.setFilterExtensions(new String[] {IConstants.CSV_FILTER_EXTENSION});
                dialog.setFilterNames(new String[] {IConstants.CSV_FILTER_NAME});
                dialog.setOverwrite(true);

                String filename = dialog.open();

                if (filename != null) {
                    FileExporter csvExport = new FileExporter(exportArray(), filename);
                    csvExport.exportToCSV();
                }
            }
        };
        exportToCSVAction.setEnabled(false);

        exportToLatexAction = new Action(Messages.Euclid_Export_Tex) {
            public void run() {
                FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
                dialog.setFilterPath(DirectoryService.getUserHomeDir());
                dialog.setFileName(Messages.Euclid_SaveDialog+textP_2.getText()+"_"+textQ_2.getText()+".tex");
                dialog.setFilterExtensions(new String[] {IConstants.TEX_FILTER_EXTENSION});
                dialog.setFilterNames(new String[] {IConstants.TEX_FILTER_NAME});
                dialog.setOverwrite(true);

                String filename = dialog.open();

                if (filename != null) {
                    FileExporter latexExport = new FileExporter(exportArray(), filename);
                    latexExport.exportToLatex();
                }

            }
        };
        exportToLatexAction.setEnabled(false);

        	Image img = EuclidPlugin.getImageDescriptor("/icons/view.gif").createImage();
        	restartAction = new Action("", ImageDescriptor.createFromImage(img)) { 
        		public void run() {
                    reset_1();
                    resetTable_2();
                    textP_2.setText("44");
                    textQ_2.setText("18");
                    btnNextStep_2.setEnabled(true);
                    btnPreviousStep_2.setEnabled(false);
                    btnCompute_2.setEnabled(true);
                    btnResetTable_2.setEnabled(false);
                    exportToCSVAction.setEnabled(false);
                    exportToPdfAction.setEnabled(false);
                    exportToLatexAction.setEnabled(false);
                    tabFolder.setSelection(0);
                }
            };
    }

    /**
     * Initialize the menu
     */
    private void initializeMenu() {
        menuManager = getViewSite().getActionBars().getMenuManager();
        toolBarManager = getViewSite().getActionBars().getToolBarManager();

        menuManager.add(exportToPdfAction);
        menuManager.add(exportToLatexAction);
        menuManager.add(exportToCSVAction);
        toolBarManager.add(restartAction);
    }

    public void createPartControl(Composite parent) {
        this.parent = parent;
        
        createActions();
        initializeMenu();
        
        parent.setLayout(new GridLayout(1, false));
        
        scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        
        tabFolder = new TabFolder(scrolledComposite, SWT.NONE);
        tabFolder.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if(tabFolder.getSelectionIndex()==0) {
                    menuManager.setVisible(false);
                    exportToCSVAction.setEnabled(false);
                    exportToPdfAction.setEnabled(false);
                    exportToLatexAction.setEnabled(false);
                } else {
                    menuManager.setVisible(true);
                    if(state==5) {
                        exportToCSVAction.setEnabled(true);
                        exportToPdfAction.setEnabled(true);
                        exportToLatexAction.setEnabled(true);
                    }
                }
            }
        });
        
        tbtmEuclidean = new TabItem(tabFolder, SWT.NONE);
        tbtmEuclidean.setText(Messages.Euclid_Euclidean);
        
        composite_1 = new Composite(tabFolder, SWT.NONE);
        tbtmEuclidean.setControl(composite_1);
        composite_1.setLayout(new GridLayout(6, false));
        
//        headerBox_1 = new Composite(composite_1, SWT.NONE);
//        headerBox_1.setLayout(new FormLayout());
//        lblHeader_1 = new Label(headerBox_1, SWT.NONE);
        lblHeader_1 = new Label(composite_1, SWT.NONE);
        lblHeader_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));
//        lblHeader_1.setLayoutData(new FormData());
        lblHeader_1.setFont(FontService.getHeaderFont());
        lblHeader_1.setText(Messages.Euclid_Euclidean);
        
        
        txtDescription_1 = new StyledText(composite_1, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
        txtDescription_1.setText(Messages.Euclid_Description_1);
        GridData gd_txtDescription_1 = new GridData(SWT.FILL, SWT.FILL, true, false, 6, 1);
        gd_txtDescription_1.widthHint = 600;
        txtDescription_1.setLayoutData(gd_txtDescription_1);
        
        
        grpInput_1 = new Group(composite_1, SWT.NONE);
        grpInput_1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 6, 1));
        grpInput_1.setLayout(new GridLayout(4, false));
        grpInput_1.setText(Messages.Euclid_Input);
        
        lblP_1 = new Label(grpInput_1, SWT.NONE);
        lblP_1.setText(Messages.Euclid_P);
        
        textP_1 = new Text(grpInput_1, SWT.BORDER);
        textP_1.setText("44");
        textP_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
        textP_1.addVerifyListener(new VerifyListener() {
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
        
        btnNextStep_1 = new Button(composite_1, SWT.NONE);
        btnNextStep_1.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                nextStep_1();
            }
        });
        btnNextStep_1.setText(Messages.Euclid_NextStep_Button);
         
          btnPrevStep_1 = new Button(composite_1, SWT.NONE);
          btnPrevStep_1.setEnabled(false);
          btnPrevStep_1.addSelectionListener(new SelectionAdapter() {
              public void widgetSelected(SelectionEvent e) {
                  prevStep_1();
              }
          });
          btnPrevStep_1.setText(Messages.Euclid_PrevStep_Button);
        
        btnCompute_1 = new Button(composite_1, SWT.NONE);
        btnCompute_1.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                compute_1();
            }
        });
        btnCompute_1.setText(Messages.Euclid_Compute_Button);
        
        btnResetCanvas_1 = new Button(composite_1, SWT.NONE);
        btnResetCanvas_1.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                reset_1();
            }
        });
        btnResetCanvas_1.setEnabled(false);
        btnResetCanvas_1.setText(Messages.Euclid_ResetCanvas_Button);
        
        btnResetAll_1 = new Button(composite_1, SWT.NONE);
        btnResetAll_1.addSelectionListener(new SelectionAdapter() {
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
        new Label(composite_1, SWT.NONE);
        
        textP_1.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                if(btnResetCanvas_1.getEnabled()) {
                reset_1();
                }
            }
        });

        textQ_1.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                if(btnResetCanvas_1.getEnabled()) {
                    reset_1();
                }
            }
        });

        grpComputation_1 = new Group(composite_1, SWT.NONE);
        grpComputation_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1));
        grpComputation_1.setLayout(new GridLayout(1, false));
        grpComputation_1.setText(Messages.Euclid_Computation);
        
        scrolledComposite_canvas = new ScrolledComposite(grpComputation_1, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite_canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        scrolledComposite_canvas.setExpandHorizontal(true);
        scrolledComposite_canvas.setExpandVertical(true);
        
        canvas = new Canvas(scrolledComposite_canvas, SWT.NO_REDRAW_RESIZE);
        scrolledComposite_canvas.setContent(canvas);
        scrolledComposite_canvas.setMinSize(canvas.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        canvas.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                if(step>-1) {
                    imageBuffer.copyImageToUI(e);
                    if(step!=values.size()-1) {
                        e.gc.setBackground(WHITE);
                        e.gc.fillRectangle(0, 75+45*(step), canvas.getSize().x, canvas.getSize().y-(75+45*(step)));
                    }
                }
            }
        });
        
        tbtmXEuclidean = new TabItem(tabFolder, SWT.NONE);
        tbtmXEuclidean.setText(Messages.Euclid_XEuclidean);
        
        composite_2 = new Composite(tabFolder, SWT.NONE);
        tbtmXEuclidean.setControl(composite_2);
        composite_2.setLayout(new GridLayout(5, false));
        
//        headerBox_2 = new Composite(composite_2, SWT.NONE);
//        headerBox_2.setLayout(new FormLayout());
//        lblHeader_2 = new Label(headerBox_2, SWT.NONE);
        lblHeader_2 = new Label(composite_2, SWT.NONE);
        lblHeader_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 5, 1));
//        lblHeader_2.setLayoutData(new FormData());
        lblHeader_2.setFont(FontService.getHeaderFont());
        lblHeader_2.setText(Messages.Euclid_XEuclidean);
//        new Label(composite_2, SWT.NONE);
//        new Label(composite_2, SWT.NONE);
//        new Label(composite_2, SWT.NONE);
//        new Label(composite_2, SWT.NONE);
        
        txtDescription_2 = new StyledText(composite_2, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
        txtDescription_2.setText(Messages.Euclid_Description_2);
        GridData gd_txtDescription_2 = new GridData(SWT.FILL, SWT.FILL, false, false, 5, 1);
//        gd_styledTextDescription2.widthHint = 0;
        gd_txtDescription_2.widthHint = 600;
//        gd_styledTextDescription2.heightHint = 40;
        txtDescription_2.setLayoutData(gd_txtDescription_2);
        
        grpInput_2 = new Group(composite_2, SWT.NONE);
        grpInput_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
        grpInput_2.setText(Messages.Euclid_Input);
        grpInput_2.setLayout(new GridLayout(5, false));
        
        lblP_2 = new Label(grpInput_2, SWT.NONE);
        lblP_2.setText(Messages.Euclid_P);
        
        textP_2 = new Text(grpInput_2, SWT.BORDER);
        textP_2.setText("44");
        textP_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
        textP_2.addVerifyListener(new VerifyListener() {
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
        
        btnNextStep_2 = new Button(composite_2, SWT.NONE);
        btnNextStep_2.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                nextStep_2();
            }
        });
        btnNextStep_2.setText(Messages.Euclid_NextStep_Button);
        
        btnPreviousStep_2 = new Button(composite_2, SWT.NONE);
        btnPreviousStep_2.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                prevStep_2();
            }
        });
        btnPreviousStep_2.setEnabled(false);
        btnPreviousStep_2.setText(Messages.Euclid_PrevStep_Button);
        
        btnCompute_2 = new Button(composite_2, SWT.NONE);
        btnCompute_2.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                compute_2();
            }
        });
        btnCompute_2.setText(Messages.Euclid_Compute_Button);
        
        btnResetTable_2 = new Button(composite_2, SWT.NONE);
        btnResetTable_2.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                resetTable_2();
                btnNextStep_2.setEnabled(true);
                btnPreviousStep_2.setEnabled(false);
                btnCompute_2.setEnabled(true);
                btnResetTable_2.setEnabled(false);
                exportToCSVAction.setEnabled(false);
                exportToPdfAction.setEnabled(false);
                exportToLatexAction.setEnabled(false);
            }
        });
        btnResetTable_2.setText(Messages.Euclid_ResetTable_Button);
        
        btnResetAll_2 = new Button(composite_2, SWT.NONE);
        btnResetAll_2.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                resetTable_2();
                textP_2.setText("44");
                textQ_2.setText("18");
                btnNextStep_2.setEnabled(true);
                btnPreviousStep_2.setEnabled(false);
                btnCompute_2.setEnabled(true);
                btnResetTable_2.setEnabled(false);
                exportToCSVAction.setEnabled(false);
                exportToPdfAction.setEnabled(false);
                exportToLatexAction.setEnabled(false);
            }
        });
        btnResetAll_2.setText(Messages.Euclid_Reset_Button);
        
        grpComputation_2 = new Group(composite_2, SWT.NONE);
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
        
        scrolledComposite.setContent(tabFolder);
        scrolledComposite.setMinSize(tabFolder.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }

    public void reset() {
        Control[] children = parent.getChildren();
        for (Control control : children) {
            control.dispose();
        }
        createPartControl(parent);
        parent.layout();
    }
    
    public void setFocus() {
    }
}
