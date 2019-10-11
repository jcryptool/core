package org.jcryptool.visual.errorcorrectingcodes.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TableViewerRow;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerRow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.errorcorrectingcodes.EccPlugin;
import org.jcryptool.visual.errorcorrectingcodes.data.EccData;
import org.jcryptool.visual.errorcorrectingcodes.data.Matrix2D;

public class InteractiveMatrix extends Composite {

    private ArrayList<Text> dataGrid;
    boolean modified;
    private int rows, columns;

    InteractiveMatrix(Composite parent, int rows, int cols) {
        super(parent, SWT.NONE);
        this.rows = rows;
        this.columns = cols;
        dataGrid = new ArrayList<>();

        GridLayoutFactory.fillDefaults().numColumns(cols).applyTo(this);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(this);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Text t = new Text(this, SWT.BORDER);
                t.setText("0");
                t.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
                t.addListener(SWT.Verify, e -> verifyBinary(e));
                t.addListener(SWT.Modify, e -> modified = true);
                GridDataFactory.fillDefaults().applyTo(t);
                dataGrid.add(t);
            }
        }
        }

    public void setMatrix(Matrix2D m) {
        for (int row = 0; row < m.getRowCount(); row++) {
            for (int col = 0; col < m.getColCount(); col++) {
                dataGrid.get(col + (row * m.getRowCount())).setText(String.valueOf(m.get(row, col)));
            }
        }
    }

    public Matrix2D getMatrix() {
        int[][] data = new int[rows][columns];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                data[r][c] = Integer.valueOf(dataGrid.get(c + (r * columns)).getText());
            }
        }
        return new Matrix2D(data);
    }

    private void verifyBinary(Event e) {
        String in = ((Text) e.widget).getText();
        try {
            int number = Integer.valueOf(e.text);
            String oldNumber = in;
           
            if ((number != 0 && number != 1)) {
                e.doit = false;
            } else  if (oldNumber.length() > 0)
                ((Text) e.widget).setText("");
        } catch (Exception ex) {
            if (e.text != "")
                e.doit = false;
        }
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public void reset() {
        for (int i = 0; i < rows*columns; i++) {
            dataGrid.get(i).setText("0");
        }
    }
}
