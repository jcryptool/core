/*
 * @author Daniel Hofmann
 */
package org.jcryptool.visual.errorcorrectingcodes.ui.widget;

import java.util.ArrayList;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.jcryptool.visual.errorcorrectingcodes.data.Matrix2D;

/**
 * The Class InteractiveMatrix is a custom composite to represent a binary matrix that can be
 * modified by user input. It consists of a rows * columns grid of buttons that switch their value
 * from 1 to 0 on mouse click.
 * 
 */
public class InteractiveMatrix extends Composite {

    Matrix2D matrix;

    /** The button grid represented as a one-dimensional ArrayList. */
    private ArrayList<Button> buttonGrid;
    
    /** Button to view and edit the matrix via text.*/
    private Button btnEdit;
    
    /** The modified flag, false by default */
    boolean modified;

    /** The permutation matrix flag, false by default */
    boolean permutation;

    private int rows, columns;

    private Composite compMatrixElements;

    private Composite compControlButtons;

    private MatrixEditDialog matrixEditDialog;

    /**
     * Instantiates a new interactive matrix.
     *
     * @param parent the parent composite
     * @param rows the number of rows
     * @param columns the number of columns
     */
    public InteractiveMatrix(Composite parent, int rows, int columns) {
        super(parent, SWT.NONE);
        this.matrix = new Matrix2D(rows, columns);
        this.rows = rows;
        this.columns = columns;
        this.permutation = false;
        buttonGrid = new ArrayList<>();
       
        compMatrixElements = new Composite(this, SWT.NONE);
        GridLayoutFactory.fillDefaults().numColumns(columns).applyTo(compMatrixElements);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(compMatrixElements);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Button btn = new Button(compMatrixElements, SWT.NONE);
                btn.setText("0");
                btn.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
                btn.setData(new Point(i, j));

                btn.addListener(SWT.Selection, e -> {
                    setMatrixValues(e, (Point) btn.getData());
                    modified = true;
                });

                GridDataFactory.fillDefaults().applyTo(btn);
                buttonGrid.add(btn);
            }
        }
        
        compControlButtons = new Composite(this, SWT.NONE); 
        GridLayoutFactory.fillDefaults().applyTo(compControlButtons);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(compControlButtons);

        btnEdit = new Button(compControlButtons,SWT.NONE);
        btnEdit.setText("Edit");
        btnEdit.addListener(SWT.Selection, e -> {
            matrixEditDialog = new MatrixEditDialog(this.getShell());
            matrixEditDialog.setMatrix(matrix);
            
            if (matrixEditDialog.open() == Window.OK) {
                this.setMatrix(matrixEditDialog.getMatrix());
            }
        });
        
      
    }

    private void setMatrixValues(Event e, Point p) {
        Button b = (Button) e.widget;
        if (b.getText().equals("0")) {
            b.setText("1");
            matrix.set(p.x, p.y, 1);
            
            if (isPermutation()) {
                // value 1 sets every other value in its row and column to 0
                for (int row = 0; row < rows; row++) {
                    if (row != p.x) {
                        buttonGrid.get(p.y + (row * rows)).setText("0");
                        matrix.set(row, p.y, 0);
                    }
                }

                for (int col = 0; col < columns; col++) {
                    if (col != p.y) {
                        buttonGrid.get((p.x * rows) + col).setText("0");
                        matrix.set(p.x, col, 0);
                    }
                }
            }
        } else {
            b.setText("0");
            matrix.set(p.x, p.y, 0);
        }
    }

    /**
     * Match the button values to the given binary matrix and set the "modified" flag.
     *
     * @param m the new matrix
     */
    public void setMatrix(Matrix2D m) {
        for (int row = 0; row < m.getRowCount(); row++) {
            for (int col = 0; col < m.getColCount(); col++) {
                buttonGrid.get(col + (row * (m.getColCount()))).setText(String.valueOf(m.get(row, col)));

            }
        }
        this.matrix = m;
        modified = true;
    }

    /**
     * Return the represented matrix by parsing the button values.
     *
     * @return the matrix
     */
    public Matrix2D getMatrix() {
        return matrix;
    }

    /**
     * Checks if the modified flag is set.
     *
     * @return true, if is modified
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * Sets the modified flag.
     *
     * @param modified the new modified
     */
    public void setModified(boolean modified) {
        this.modified = modified;
    }

    /**
     * Checks if the permutation flag is set.
     *
     * @return true, if is permutation
     */
    public boolean isPermutation() {
        return permutation;
    }

    /**
     * Sets the permutation flag.
     *
     * @param permutation the new permutation
     */
    public void setPermutation(boolean permutation) {
        this.permutation = permutation;
    }

    /**
     * Reset the buttons to 0.
     */
    public void reset() {
        for (int i = 0; i < rows * columns; i++) {
            buttonGrid.get(i).setText("0");
        }
    }
}
