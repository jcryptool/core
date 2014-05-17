package org.jcryptool.visual.crtverification.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;

public class TestComposite extends Composite {
    private Text text;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public TestComposite(Composite parent, int style) {
        super(parent, style);
        
        // Design with Window Builder
        // Example Start
        text = new Text(this, SWT.BORDER);
        text.setBounds(10, 10, 430, 153);
        
        Button btnTestButton = new Button(this, SWT.NONE);
        btnTestButton.setBounds(346, 169, 94, 28);
        btnTestButton.setText("Test Button");
        // Example End
        
    }
}
