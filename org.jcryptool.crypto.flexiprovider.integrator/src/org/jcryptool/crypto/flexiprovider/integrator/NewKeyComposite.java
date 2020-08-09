// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.integrator;
import java.util.Observable;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class NewKeyComposite extends org.eclipse.swt.widgets.Composite {


	private Canvas canvas1;
	private Label labelOwner;
	private Label labelType;
	private Button buttonDelete;
	private Label labelInfo1;
	private Composite infoComposite;
	private KeyStoreAlias publicKeyAlias;
	private Observable removeObserver;

	/**
	* Overriding checkSubclass allows this class to extend org.eclipse.swt.widgets.Composite
	*/
	protected void checkSubclass() {
	}

	protected String getKeyLabel() {
		return Messages.getString("NewKeyComposite.key_label"); //$NON-NLS-1$
	}

	public NewKeyComposite(org.eclipse.swt.widgets.Composite parent, KeyStoreAlias key) {
		super(parent, SWT.BORDER);
		this.publicKeyAlias = key;
		removeObserver = new Observable() {
			public void notifyObservers(Object arg) {
				this.setChanged();
				super.notifyObservers(arg);
			};
		};
		initGUI();
	}

	private void initGUI() {
		try {
			GridLayout thisLayout = new GridLayout();
			thisLayout.numColumns = 3;
			this.setLayout(thisLayout);
//			this.setSize(396, 100);
			{
				GridData canvas1LData = new GridData();
				canvas1LData.widthHint = 48;
				canvas1LData.heightHint = 48;
				canvas1 = new Canvas(this, SWT.NONE);
				canvas1.setLayoutData(canvas1LData);
				canvas1.addPaintListener(new PaintListener() {
					ImageDescriptor imgDescriptor = getKeyImageDescriptor();
					public void paintControl(PaintEvent e) {
						e.gc.drawImage(imgDescriptor.createImage(), 0, 0); //$NON-NLS-1$
					}
				});
			}
			{
				infoComposite = new Composite(this, SWT.NONE);
				GridLayout infoCompositeLayout = new GridLayout();
				infoCompositeLayout.makeColumnsEqualWidth = true;
				infoCompositeLayout.verticalSpacing = 1;
				infoCompositeLayout.marginHeight = 0;
				GridData infoCompositeLData = new GridData();
				infoCompositeLData.grabExcessHorizontalSpace = true;
				infoCompositeLData.grabExcessVerticalSpace = true;
				infoCompositeLData.horizontalAlignment = GridData.FILL;
				infoCompositeLData.verticalAlignment = GridData.CENTER;
				infoComposite.setLayoutData(infoCompositeLData);
				infoComposite.setLayout(infoCompositeLayout);
				{
					labelInfo1 = new Label(infoComposite, SWT.NONE);
					GridData labelInfo1LData = new GridData();
					labelInfo1LData.grabExcessHorizontalSpace = true;
					labelInfo1LData.horizontalAlignment = GridData.FILL;
					labelInfo1.setLayoutData(labelInfo1LData);
					setInfoLabelText(); //$NON-NLS-1$
					Font segeo = new Font(labelInfo1.getDisplay(), new FontData("Segoe UI", 9, 1));
					labelInfo1.setFont(segeo); //$NON-NLS-1$
				}
				{
					labelOwner = new Label(infoComposite, SWT.NONE);
					GridData labelOwnerLData = new GridData();
					labelOwner.setLayoutData(labelOwnerLData);
					labelOwner.setText(Messages.getString("NewKeyComposite.owner") + publicKeyAlias.getContactName()); //$NON-NLS-1$
				}
				{
					labelType = new Label(infoComposite, SWT.NONE);
					GridData labelTypeLData = new GridData();
					labelType.setLayoutData(labelTypeLData);
					labelType.setText(publicKeyAlias.getOperation());
				}
			}
			{
				GridData buttonDeleteLData = new GridData();
				buttonDeleteLData.widthHint = 24;
				buttonDeleteLData.heightHint = 24;
				buttonDeleteLData.grabExcessVerticalSpace = true;
				buttonDeleteLData.verticalAlignment = SWT.FILL;
				buttonDelete = new Button(this, SWT.PUSH | SWT.CENTER);
				buttonDelete.setLayoutData(buttonDeleteLData);
				buttonDelete.setImage(ImageService.getImage(KeyStorePlugin.PLUGIN_ID, "icons/16x16/cancel.png"));
				buttonDelete.setToolTipText(Messages.getString("NewKeyComposite.removeKeypairBtn")); //$NON-NLS-1$
				buttonDelete.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						removeKeyFromKeystore();
					}
				});
			}
			this.layout();
		} catch (Exception ex) {
			LogUtil.logError(IntegratorPlugin.PLUGIN_ID, ex);
		}
	}

	protected ImageDescriptor getKeyImageDescriptor() {
		return ImageService.getImageDescriptor(KeyStorePlugin.PLUGIN_ID, "icons/48x48/kgpg_key1.png");
	}

	protected void removeKeyFromKeystore() {
		KeyStoreManager.getInstance().deleteEntry(publicKeyAlias);
		removeObserver.notifyObservers(null);
	}

	protected void setInfoLabelText(String string) {
		labelInfo1.setText(getKeyLabel() + string); //$NON-NLS-1$
	}

	protected void setInfoLabelText() {
		setInfoLabelText(""); //$NON-NLS-1$
	}

	public Observable getRemoveObserver() {
		return removeObserver;
	}

	public KeyStoreAlias getPublicKeyAlias() {
		return publicKeyAlias;
	}

}
