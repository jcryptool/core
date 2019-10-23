package org.jcryptool.visual.errorcorrectingcodes.ui.binding;

import org.eclipse.jface.databinding.swt.WidgetValueProperty;
import org.jcryptool.visual.errorcorrectingcodes.data.Matrix2D;
import org.jcryptool.visual.errorcorrectingcodes.ui.widget.InteractiveMatrix;

public class InteractiveMatrixProperty extends WidgetValueProperty<InteractiveMatrix, Matrix2D> {

    @Override
    public Object getValueType() {
        return Matrix2D.class;
    }

    @Override
    protected Matrix2D doGetValue(InteractiveMatrix source) {
        return source.getMatrix();

    }

    @Override
    protected void doSetValue(InteractiveMatrix source, Matrix2D value) {
        if(value != null)
            source.setMatrix(value);
    }

}
