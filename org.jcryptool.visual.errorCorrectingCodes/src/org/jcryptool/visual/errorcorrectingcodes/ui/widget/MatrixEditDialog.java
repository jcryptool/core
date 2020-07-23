package org.jcryptool.visual.errorcorrectingcodes.ui.widget;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.RowLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.visual.errorcorrectingcodes.data.Matrix2D;
import org.jcryptool.visual.errorcorrectingcodes.data.MatrixFormatException;
import org.jcryptool.visual.errorcorrectingcodes.ui.UIHelper;

public class MatrixEditDialog extends Dialog {

    private Matrix2D matrix;
    private StyledText txtMatrix;
    private Label lblError;
    private Button btnSave;

    protected MatrixEditDialog(Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        txtMatrix = UIHelper.mutltiLineText(container, SWT.FILL, SWT.FILL, SWT.DEFAULT, 8);
        lblError = new Label(container, SWT.NONE);
        GridDataFactory.fillDefaults().applyTo(lblError);
        lblError.setText("Format error, please check your input and try again.");
        lblError.setForeground(ColorService.RED);
        lblError.setVisible(false);
        if (matrix != null)
            txtMatrix.setText(matrix.toString());
        return container;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Edit Matrix");
    }

    @Override
    protected void okPressed() {
        if (saveInput())
            super.okPressed();
    }

    private boolean saveInput() {
        try {
            Matrix2D matrixNew = Matrix2D.parseBinaryMatrix2D(txtMatrix.getText());
            if (matrixNew.getRowCount() != matrix.getRowCount() || matrixNew.getColCount() != matrix.getColCount())
                return false;
            else {
                matrix = matrixNew;
                return true;
            }
        } catch (MatrixFormatException e) {
            LogUtil.logError(e);
            lblError.setVisible(true);
            return false;
        }
    }

    public void setMatrix(Matrix2D m) {
        matrix = m;
    }

    public Matrix2D getMatrix() {
        return matrix;
    }
}
