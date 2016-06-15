package org.jcryptool.visual.merkletree.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.MerkleTreeView;
import org.jcryptool.visual.merkletree.ui.MerkleConst.SUIT;


/**
 * Class for the Composite with the Descriptions in Tabpage 1
 * @author Kevin Muehlboeck
 * @author <i>revised by</i>
 * @author Maximilian Lindpointner
 * 
 */
 
public class MerkleTreeComposite extends Composite {

	private Composite descr;
	private MerkleTreeSeed seedc;
	private SUIT mode;

	/**
	 * Create the composite.
	 * Including Descriptions, Seed (,Bitmask) and Key
	 * @param parent Parent Composite
	 * @param masterView Plugin-Main-Composite
	 */
	public MerkleTreeComposite(Composite parent, ViewPart masterView) {
		super(parent, SWT.NONE);
		
		this.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN, true));
		mode = SUIT.MSS;
		
		// to make the text wrap lines automatically
		descr = new Composite(this, SWT.WRAP | SWT.BORDER | SWT.LEFT);
		descr.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, MerkleConst.H_SPAN_MAIN, 1));
		descr.setLayout(new GridLayout(1, true));
		
		
		//Combobox, to switch the different SUIT's (MSS,XMSS,XMSS_MT)
		Combo combo = new Combo(descr, SWT.NONE);
		GridData gd_combo = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 0, 1);
		gd_combo.widthHint = 340;
		combo.setLayoutData(gd_combo);
		combo.add(Descriptions.CompositeDescriptionMerkleTree);
		combo.add(Descriptions.CompositeDescriptionXMSS);
		combo.add(Descriptions.CompositeDescriptionXMSS_MT);
		combo.setEnabled(true);
		combo.select(0);
		
		
		//listener if another SUIT is selected
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				switch(combo.getSelectionIndex()){
				case 1:
					mode = SUIT.XMSS;
					break;
				case 2:
					mode = SUIT.XMSS_MT;
					break;
				case 0:
				default:
					mode = SUIT.MSS;
					break;
				}
				((MerkleTreeView) masterView).setAlgorithm(null, mode);
				//clear actual frame
				Control[] children = descr.getChildren();
				for (Control control : children) {
					if(control.getClass() != Combo.class)
						control.dispose();
				}
				
				//generates new view:
				//main-descriptions
				MerkleTreeDescription(descr, mode);
				//seed-description (->key and bitmask)
				seedc = new MerkleTreeSeed(descr, SWT.WRAP | SWT.BORDER | SWT.LEFT, mode, masterView);
				seedc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, SWT.FILL));		
				descr.layout();
			}
		});
		
		//initial MSS - Layout
		MerkleTreeDescription(descr, mode);
		seedc = new MerkleTreeSeed(descr, SWT.WRAP | SWT.BORDER | SWT.LEFT, mode, masterView);
		seedc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, SWT.FILL));	
	}
	/**
	 * Generates the main description for the first tab
	 * @param descr Parent Composite
	 * @param mode SUIT { MSS, XMSS or XMSS_MT }
	 * @author Maximilian Lindpointner
	 */
	private void MerkleTreeDescription(Composite descr, SUIT mode){
		
		Label descLabel = new Label(descr, SWT.NONE);
		StyledText descText = new StyledText(descr, SWT.WRAP | SWT.BORDER);
		descText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2));
		descText.setCaret(null);
		descText.setEditable(false);
		descLabel.setText(Descriptions.XMSS.Tab0_Head0);
		descText.setText(Descriptions.XMSS.Tab0_Txt0);
	}
	
	public MerkleTreeSeed getMTS(){
		return seedc;
	}
}
