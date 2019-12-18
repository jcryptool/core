package org.jcryptool.visual.rainbow.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.RowLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.core.logging.utils.LogUtil;

public class ViParameterWidget extends Composite {

    private static final String[] NUM_LAYERS = new String[] { "2", "3", "4", "5", "6", "7"};
    private List<Integer> viList;

    private Combo comboNumLayers;

    private Composite compParams;
    private Composite compWrapVi;

    private Label lblLayers;
    private Label lblNumLayers;
    private Button btnApply;

    public ViParameterWidget(Composite parent) {
        this(parent, new int[5]);
    }

    public ViParameterWidget(Composite parent, int[] vi) {
        super(parent, SWT.NONE);
        GridLayoutFactory glf = GridLayoutFactory.fillDefaults().margins(new Point(5, 5));
        GridDataFactory gdf = GridDataFactory.fillDefaults().grab(true, true);
        glf.numColumns(2).applyTo(this);
        gdf.applyTo(this);

        viList = Arrays.stream(vi).boxed().collect(Collectors.toList());

        lblNumLayers = new Label(this, SWT.NONE);
        lblNumLayers.setText("Number of Layers: ");
        comboNumLayers = new Combo(this, SWT.READ_ONLY);
        comboNumLayers.setItems(NUM_LAYERS);
        comboNumLayers.setText(String.valueOf(vi.length));
        comboNumLayers.addListener(SWT.Modify, e -> {
            viList = new ArrayList<>();
            for (int i = 0; i < Integer.valueOf(comboNumLayers.getText()); i++) {
                viList.add(0);
            }
            compParams.dispose();
            compParams = createViComposite(compWrapVi);
            compWrapVi.layout();
        });

        lblLayers = new Label(this, SWT.NONE);
        lblLayers.setText("Variables per Layer: ");
        gdf.applyTo(lblLayers);

        compWrapVi = new Composite(this, SWT.NONE);
        glf.numColumns(1).applyTo(compWrapVi);
        gdf.applyTo(compWrapVi);
        compParams = createViComposite(compWrapVi);
        btnApply = new Button(this, SWT.NONE);
        btnApply.setText("Apply");

    }

    private Composite createViComposite(Composite parent) {
        Composite comp = new Composite(parent, SWT.NONE);
        comp.setData(viList);
        RowLayoutFactory.fillDefaults().justify(true).spacing(10).applyTo(comp);
        int numOfLayers = viList.size();

        for (int i = 0; i < numOfLayers; i++) {
            StyledText v = new StyledText(comp, SWT.BORDER);
            v.setText(String.valueOf(viList.get(i)));
            v.setData(i);
            v.addListener(SWT.Verify, this::verifyNumeric);
            v.setLayoutData(new RowData(20, SWT.DEFAULT));

            if (i < numOfLayers - 1) {
                Label lblLesser = new Label(comp, SWT.NONE);
                lblLesser.setText("<");
            }

            v.addListener(SWT.Modify, e -> {
                String number = ((StyledText) e.widget).getText();
                if (!number.isEmpty()) {
                    try {
                        int idx = (int) e.widget.getData();
                        viList.set(idx, Integer.valueOf(number));
                    } catch (NumberFormatException ex) {
                        LogUtil.logError(ex);
                    }
                }
            });
        }

        return comp;
    }

    private void verifyNumeric(Event e) {
        String string = e.text;
        char[] chars = new char[string.length()];
        string.getChars(0, chars.length, chars, 0);
        for (int j = 0; j < chars.length; j++) {
           if (!('0' <= chars[j] && chars[j] <= '9')) {
              e.doit = false;
              return;
           }
        }
    }

    public List<Integer> getViList() {
        return viList;
    }

    public void setViList(List<Integer> vi) {
        this.viList = vi;
    }

    public Button getBtnApply() {
        return btnApply;
    }
}
