package org.jcryptool.crypto.classic.transposition.algorithm;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorPart;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmConfiguration;
import org.jcryptool.crypto.classic.transposition.ui.TranspositionKeyInputComposite;

public class TranspositionConfiguration extends ClassicAlgorithmConfiguration {

	private String key1; 
	private String key2;
	private boolean readInOrder1;
	private boolean readInOrder2;
	private boolean readOutOrder1;
	private boolean readOutOrder2;
	private boolean twoKeys;
	
	public TranspositionConfiguration(boolean encryptMode, AbstractAlphabet plaintextAlpha,
			boolean filterNonalpha, TransformData preOpTransformData, 
			String key1, 
			String key2,
			boolean readInOrder1,
			boolean readInOrder2,
			boolean readOutOrder1,
			boolean readOutOrder2
			) {
		super(encryptMode, Messages.TranspositionConfiguration_0, plaintextAlpha, filterNonalpha, preOpTransformData);
		this.key1 = key1;
		this.key2 = key2;
		this.readInOrder1 = readInOrder1;
		this.readInOrder2 = readInOrder2;
		this.readOutOrder1 = readOutOrder1;
		this.readOutOrder2 = readOutOrder2;
		this.twoKeys = !key2.equals(""); //$NON-NLS-1$
	}

	@Override
	public Composite displayAlgorithmParameters(Composite parent, IEditorPart editor) {
		//TODO: check for GridLayout in parent
		Composite main = super.displayAlgorithmParameters(parent, editor);
		
		{
			Label descr = new Label(main, SWT.NONE);
			descr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			descr.setText(Messages.TranspositionConfiguration_2);
			
			Composite keysDisplay = generateKeysDisplay(main, editor, twoKeys);
		}
		
		return main;
	}

	private Composite generateKeysDisplay(Composite main,
			IEditorPart editor, boolean twoKeys) {
		
		Composite display = new Composite(main, SWT.BORDER);
		GridLayout layout = new GridLayout(twoKeys?3:1, false);
		layout.horizontalSpacing = 15;
		display.setLayout(layout);
		display.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		
		{
			TranspositionKeyInputComposite key1Display = new TranspositionKeyInputComposite(display, false);
			key1Display.setTitle(Messages.TranspositionConfiguration_3+(twoKeys?" 1":"")); //$NON-NLS-2$ //$NON-NLS-3$
			key1Display.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
			
			key1Display.getKeyInput().setTextfieldTextExternal(key1);
			key1Display.getKeyInput().synchronizeWithUserSide();
			
			key1Display.getReadInInput().writeContent(readInOrder1);
			key1Display.getReadInInput().synchronizeWithUserSide();
			key1Display.getReadOutInput().writeContent(readOutOrder1);
			key1Display.getReadOutInput().synchronizeWithUserSide();
			
			key1Display.makeEditable(false);
		}
		
		if(twoKeys) {
			Label sep = new Label(display, SWT.SEPARATOR | SWT.VERTICAL);
			sep.setLayoutData(new GridData(SWT.BEGINNING, SWT.FILL, false, false));
			
			TranspositionKeyInputComposite key2Display = new TranspositionKeyInputComposite(display, false);
			key2Display.setTitle(Messages.TranspositionConfiguration_6);
			key2Display.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
			
			key2Display.getKeyInput().setTextfieldTextExternal(key2);
			key2Display.getKeyInput().synchronizeWithUserSide();
			
			key2Display.getReadInInput().writeContent(readInOrder2);
			key2Display.getReadInInput().synchronizeWithUserSide();
			key2Display.getReadOutInput().writeContent(readOutOrder2);
			key2Display.getReadOutInput().synchronizeWithUserSide();
			
			key2Display.makeEditable(false);
		}
		
		return display;
	}
	

}
