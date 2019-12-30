//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2012, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.ui.textblockloader;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.util.input.AbstractUIInput;
import org.jcryptool.crypto.ui.textblockloader.conversion.AlphabetCharsToNumbers;
import org.jcryptool.crypto.ui.textblockloader.conversion.ConversionStringToBlocks;
import org.jcryptool.crypto.ui.textblockloader.conversion.NumbersToBlocksConversion;
import org.jcryptool.crypto.ui.textloader.ui.wizard.loadtext.LoadTextWizardPage;
import org.jcryptool.crypto.ui.textsource.TextInputWithSource;

public class TANLTextPage extends LoadTextWizardPage {

	private CharsToNumbersComposite charsToNumbersComposite;
	private NumberblocksAndTextViewer txtNumberPreview;
	private int maxNumber;

	public TANLTextPage(int maxNumber) {
		super();
		this.maxNumber = maxNumber;
		this.setTitle(Messages.TANLTextPage_wtitle);
		this.setMessage(Messages.TANLTextPage_wmessage);
	}
	
	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		super.createControl(parent);
		super.getTextInput().writeContent(new TextInputWithSource("")); //$NON-NLS-1$
		super.getTextInput().synchronizeWithUserSide();
		
		Group compConversionParams1 = new Group(container, SWT.NONE);
		compConversionParams1.setLayout(new GridLayout());
		compConversionParams1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		compConversionParams1.setText(Messages.TANLTextPage_3);
		
		this.charsToNumbersComposite = new CharsToNumbersComposite(compConversionParams1, SWT.NONE, maxNumber);
		this.charsToNumbersComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		Composite compFilterInfo = new Composite(compConversionParams1, SWT.NONE);
		GridLayout layoutFilterInfo = new GridLayout(2, false);
		layoutFilterInfo.marginWidth = 0;
		layoutFilterInfo.marginHeight = 0;
		compFilterInfo.setLayout(layoutFilterInfo);
		GridData layoutData = new GridData();
		layoutData.horizontalIndent = 5;
		compFilterInfo.setLayoutData(layoutData);
		Label lblFilterIcon = new Label(compFilterInfo, SWT.NONE);
		lblFilterIcon.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_INFO_TSK));
		Label lblFilterInfo = new Label(compFilterInfo, SWT.NONE);
		lblFilterInfo.setText(Messages.TANLTextPage_4);
		
		Group compNumberPreview = new Group(container, SWT.NONE);
		compNumberPreview.setLayout(new GridLayout());
		compNumberPreview.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		compNumberPreview.setText(Messages.TANLTextPage_5);
		
		Repr[] viewOptions = new Repr[]{
					Repr.DECIMAL, 
					Repr.HEX, 
					Repr.BINARY, 
					Repr.STRING 
				};
		txtNumberPreview = new NumberblocksAndTextViewer(compNumberPreview, SWT.NONE, 
				viewOptions);
		GridData txtNumberPreviewLayoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		txtNumberPreviewLayoutData.heightHint = 110;
		txtNumberPreview.setLayoutData(txtNumberPreviewLayoutData);
		
		Composite compNextPageInfo = new Composite(container, SWT.NONE);
		compNextPageInfo.setLayout(new GridLayout(2, false));
		Label lblNextPageIcon = new Label(compNextPageInfo, SWT.NONE);
		lblNextPageIcon.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_INFO_TSK));
		Label lblNextPageInfo = new Label(compNextPageInfo, SWT.NONE);
		lblNextPageInfo.setText(Messages.TANLTextPage_6);

		Observer previewRefreshObserver = new Observer() {
			@Override
			public void update(Observable arg0, Object arg1) {
				ConversionCharsToNumbers ctn = charsToNumbersComposite.getConversionInput().getContent();
				ConversionNumbersToBlocks ntb = new NumbersToBlocksConversion(1, NumbersToBlocksConversion.getDefaultBaseForNumberToBlockConversion(ctn));
				ConversionStringToBlocks conversion = new ConversionStringToBlocks(ctn, ntb);
				TextInputWithSource stringContent = TANLTextPage.super.getPageConfiguration();
				
				txtNumberPreview.setContent(stringContent.getText(), conversion);
				
				getWizard().getContainer().updateButtons();
			}
		};
		
		super.getTextInput().addObserver(previewRefreshObserver);
		this.charsToNumbersComposite.getConversionInput().addObserver(previewRefreshObserver);
//		previewRefreshObserver.update(null, null);
	}
	
	protected void refreshMaxPotNumberNextPage() {
		getDefaultWiz().getPageBlockParams().setMaxPotentialNumber(charsToNumbersComposite.getConversionInput().getContent().getMaxNumberValue());
	}

	public AbstractUIInput<AlphabetCharsToNumbers> getCharsToNumbersConversionInput() {
		return charsToNumbersComposite.getConversionInput();
	}
	
	private TextAsNumbersLoaderWizard getDefaultWiz() {
		return (TextAsNumbersLoaderWizard) getWizard();
	}
	
	@Override
	public boolean canFlipToNextPage() {
		return this.getTextInput().getContent().getText().length() > 0;
	}
	
}
