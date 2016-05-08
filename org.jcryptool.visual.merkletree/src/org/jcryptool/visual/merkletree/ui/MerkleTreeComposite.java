package org.jcryptool.visual.merkletree.ui;

//import org.eclipse.osgi.service.resolver.DisabledInfo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
//import org.eclipse.swt.widgets.MessageBox;
//import org.eclipse.swt.widgets.Shell;
//import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.part.ViewPart;
//import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.ui.MerkleConst.SUIT;

//TODO: MAXI Kommentare
/**
 * Class for the Composite with the Descriptions in Tabpage 1
 * @author Kevin Muehlboeck
 *
 */
public class MerkleTreeComposite extends Composite {

	private Composite descr;
	private MerkleTreeSeed seedc;
	private SUIT verfahren;

	/**
	 * Create the composite.
	 * Including Descriptions and Seed
	 * @param parent
	 * @param style
	 */
	public MerkleTreeComposite(Composite parent, int style, ViewPart masterView) {
		super(parent, SWT.NONE);
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
		combo.setEnabled(true);
		
		
		
		combo.addSelectionListener(new SelectionAdapter() {
			//TODO MAXI content finished?
			@Override
			public void widgetSelected(SelectionEvent e) {
				switch(combo.getSelectionIndex()){
				case 1:
					verfahren = SUIT.XMSS;
					break;
				case 2:
					verfahren = SUIT.XMSS_MT;
					break;
				case 0:
				default:
					verfahren = SUIT.MSS;
					break;
				}
				Control[] children = descr.getChildren();
				for (Control control : children) {
					if(control.getClass() != Combo.class)
						control.dispose();
				}
				MerkleTreeDescription(descr, verfahren);
				seedc = new MerkleTreeSeed(descr, SWT.WRAP | SWT.BORDER | SWT.LEFT, verfahren, masterView);
				seedc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, SWT.FILL));		
				descr.layout();

			}
		});
		//TODO: MAXI was steht da? 
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

		
	}
	public void MerkleTreeDescription(Composite descr, SUIT verfahren){
		//TODO: MAXI Kommentare
		Label descLabel = new Label(descr, SWT.NONE);
		StyledText descText = new StyledText(descr, SWT.WRAP | SWT.BORDER);
		descText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2));
		descText.setCaret(null);
		descText.setEditable(false);
		
		switch(verfahren){
		case XMSS:
			descText.setText(Descriptions.XMSS.Tab0_Txt0);
			descLabel.setText(Descriptions.XMSS.Tab0_Head0);
			break;
		case XMSS_MT:
			descText.setText(Descriptions.XMSS_MT.Tab0_Txt0);
			descLabel.setText(Descriptions.XMSS_MT.Tab0_Head0);
			break;
		case MSS:
		default:
			descText.setText(Descriptions.MSS.Tab0_Txt0);
			descLabel.setText(Descriptions.MSS.Tab0_Head0);
			break;
		
		}
	}
}
