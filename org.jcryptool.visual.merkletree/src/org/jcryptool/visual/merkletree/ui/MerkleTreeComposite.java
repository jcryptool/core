package org.jcryptool.visual.merkletree.ui;

import org.eclipse.osgi.service.resolver.DisabledInfo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
//import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.merkletree.Descriptions;

/**
 * Class for the Composite with the Descriptions in Tabpage 1
 * @author Kevin Muehlboeck
 *
 */
public class MerkleTreeComposite extends Composite {

	private Composite descr;
	private MerkleTreeSeed seedc;
	private ViewPart masterView;
	private boolean extended;

	/**
	 * Create the composite.
	 * Including Descriptions and Seed
	 * @param parent
	 * @param style
	 */
	public MerkleTreeComposite(Composite parent, int style, ViewPart masterView) {
		super(parent, SWT.NONE);
		this.masterView = masterView;
		this.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN, true));

		// to make the text wrap lines automatically
		descr = new Composite(this, SWT.WRAP | SWT.BORDER | SWT.LEFT);
		descr.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, MerkleConst.H_SPAN_MAIN, 1));
		descr.setLayout(new GridLayout(1, true));
		
		Combo combo = new Combo(descr, SWT.NONE);
		combo.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 0, 1));
		combo.add(Descriptions.CompositeDescriptionMerkleTree);
		combo.add(Descriptions.CompositeDescriptionXMSS);
		combo.setEnabled(false);
		combo.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( combo.getText().equals(Descriptions.CompositeDescriptionXMSS))
					
				if (combo.getSelectionIndex() == 0) {
					extended = false;

				} else if (combo.getSelectionIndex() == 1) {
					extended = true;
				}
				seedc = new MerkleTreeSeed(descr, SWT.WRAP | SWT.BORDER | SWT.LEFT, extended, masterView);
			}
		});
		combo.select(0);
		// the heading of the description; is not selectable by mouse
		Label descLabel = new Label(descr, SWT.NONE);
		descLabel.setText(Descriptions.CompositeDescriptionMerkleTree);
		//descLabel.setFont(FontService.getHeaderFont());

		// this divide has been made to allow selection of text in this section
		// but not of the
		// heading
		// while not allowing modification of either section
		StyledText descText = new StyledText(descr, SWT.WRAP | SWT.BORDER);
		descText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2));
		descText.setCaret(null);
		descText.setText(Descriptions.PlugInDescription);
		descText.setEditable(false);
		seedc = new MerkleTreeSeed(descr, SWT.FILL, extended, masterView);
		seedc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, SWT.FILL));

	}
}
