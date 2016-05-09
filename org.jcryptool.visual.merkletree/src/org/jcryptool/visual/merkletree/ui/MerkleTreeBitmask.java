package org.jcryptool.visual.merkletree.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.ui.MerkleConst.SUIT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.MerkleTreeView;
import org.jcryptool.visual.merkletree.algorithm.ISimpleMerkle;
import org.jcryptool.visual.merkletree.algorithm.SimpleMerkleTree;
import org.jcryptool.visual.merkletree.ui.MerkleConst.SUIT;

public class MerkleTreeBitmask extends Composite{
	
	Button buttonCreateKeys;
	Label createLabel;
	StyledText descText;
	Text textSeed;
	Label bitLable;

	public MerkleTreeBitmask(Composite parent, int style, SUIT verfahren, ViewPart masterView) {
		super(parent, style);
		
		this.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN, true));

		createLabel = new Label(this, SWT.NONE);
		createLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN, 1));
		
		descText = new StyledText(this, SWT.WRAP | SWT.BORDER);
		descText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, MerkleConst.H_SPAN_MAIN, 2));
		descText.setCaret(null);

		textSeed = new Text(this, SWT.BORDER | SWT.CENTER);
		textSeed.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1));
		
		buttonCreateKeys = new Button(this, SWT.NONE);
		buttonCreateKeys.setEnabled(false);
		buttonCreateKeys.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 3, 1));
		
		switch(verfahren){
		case XMSS_MT:
			createLabel.setText(Descriptions.XMSS_MT.Tab0_Head3);
			descText.setText(Descriptions.XMSS_MT.Tab0_Txt3);
			buttonCreateKeys.setText(Descriptions.XMSS_MT.Tab0_Button3);
			break;
		case XMSS:
		case MSS:
		default:
			createLabel.setText(Descriptions.XMSS.Tab0_Head3);
			descText.setText(Descriptions.XMSS.Tab0_Txt3);
			buttonCreateKeys.setText(Descriptions.XMSS.Tab0_Button3);
			break;
		}
		descText.setEditable(false);
		
		
		

	}
	

}
