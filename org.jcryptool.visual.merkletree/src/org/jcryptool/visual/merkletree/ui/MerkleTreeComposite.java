package org.jcryptool.visual.merkletree.ui;

import org.eclipse.osgi.service.resolver.DisabledInfo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.part.ViewPart;
//import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.ui.MerkleConst.SUIT;

/**
 * Class for the Composite with the Descriptions in Tabpage 1
 * @author Kevin Muehlboeck
 *
 */
public class MerkleTreeComposite extends Composite {

	private Composite descr;
	private MerkleTreeSeed seedc;
	private ViewPart masterView;
	private SUIT verfahren;

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
		combo.add(Descriptions.CompositeDescriptionXMSS_MT);
		combo.select(1);
		verfahren = SUIT.XMSS;
		combo.setEnabled(true);
		
		
		
		combo.addSelectionListener(new SelectionAdapter() {
			//TODO switch statt ififif
			@Override
			public void widgetSelected(SelectionEvent e) {
				switch(combo.getSelectionIndex()){
				case 0:
					verfahren = SUIT.MSS;
					break;
				case 1:
					verfahren = SUIT.XMSS;
					MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_INFORMATION | SWT.OK);
					messageBox.setMessage(Descriptions.MerkleTreeKey_Message);
					messageBox.setText(e.text + "--" + e.detail + "---" + combo.getSelectionIndex() + Descriptions.XMSS.Tab0_Head1);
					messageBox.open();
					break;
				default:
					verfahren = SUIT.XMSS_MT;
					break;
				}
				MerkleTreeDescription(descr, verfahren);
				seedc = new MerkleTreeSeed(descr, SWT.WRAP | SWT.BORDER | SWT.LEFT, verfahren, masterView);
				seedc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, SWT.FILL));
			}
		});
		
		// the heading of the description; is not selectable by mouse
		
		//descLabel.setFont(FontService.getHeaderFont());

		// this divide has been made to allow selection of text in this section
		// but not of the
		// heading
		// while not allowing modification of either section

		combo.select(0);
		verfahren = SUIT.MSS;
		MerkleTreeDescription(descr, verfahren);
		seedc = new MerkleTreeSeed(descr, SWT.WRAP | SWT.BORDER | SWT.LEFT, verfahren, masterView);
		seedc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, SWT.FILL));

		
		/*
		Label descLabel = new Label(descr, SWT.NONE);
		descLabel.setText(Descriptions.MSS.Tab0_Head0);
		StyledText descText = new StyledText(descr, SWT.WRAP | SWT.BORDER);
		descText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2));
		descText.setCaret(null);
		descText.setText(Descriptions.MSS.Tab0_Txt0);
		descText.setEditable(false);
		//seedc = new MerkleTreeSeed(descr, SWT.FILL, verfahren, masterView);
		//seedc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, SWT.FILL));
		 * 
		 */
		
	}
	public void MerkleTreeDescription(Composite descr, SUIT verfahren){
		
		Label descLabel = new Label(descr, SWT.NONE);
		descLabel.setText(verfahren.toString());
		StyledText descText = new StyledText(descr, SWT.WRAP | SWT.BORDER);
		descText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2));
		descText.setCaret(null);
		descText.setText(Descriptions.MSS.Tab0_Txt0);
		descText.setEditable(false);
	}
}
