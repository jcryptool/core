//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2019 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.webbrowser.ui;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.webbrowser.BrowserPlugin;

public class Controls extends Composite{

	private Combo combo_url;
	private Combo combo_search;
	private Button button_forward;
	private Button button_back;
	private Button button_reload;
	private boolean animateReloadButton = false;
	protected ImageLoader loader;
	protected int imageNumber = 0;
	protected GC gc;
	private Image image;
	private HashMap<String, String> search_history = new HashMap<String, String>();
	protected String last_search;
	private Button button_stop;

	public Controls(Composite parent, int style, final BrowserView view) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(9, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);

		button_back = new Button(this, SWT.PUSH);
		button_back.setToolTipText(Messages.getString("Controls.back")); //$NON-NLS-1$
		button_back.setImage(ImageService.getImage(BrowserPlugin.PLUGIN_ID, "icons/back.gif"));
		button_back.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			public void widgetSelected(SelectionEvent e) {
				animateReloadButton(true);
				view.getBrowser().back();
			}
		});
		button_forward = new Button(this, SWT.PUSH);
		button_forward.setToolTipText(Messages.getString("Controls.forward")); //$NON-NLS-1$
		button_forward.setImage(ImageService.getImage(BrowserPlugin.PLUGIN_ID, "icons/forward.gif"));
		button_forward.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			public void widgetSelected(SelectionEvent e) {
				animateReloadButton(true);
				view.getBrowser().forward();
			}
		});

		Button button_home = new Button(this, SWT.PUSH);
		button_home.setToolTipText(Messages.getString("Controls.home")); //$NON-NLS-1$
		button_home.setImage(ImageService.getImage(BrowserPlugin.PLUGIN_ID, "icons/home.gif"));
		button_home.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			public void widgetSelected(SelectionEvent e) {
				animateReloadButton(true);
				view.getBrowser().setUrl(BrowserView.BROWSER_HOME);
			}
		});

		button_stop = new Button(this, SWT.PUSH);
		button_stop.setToolTipText(Messages.getString("Controls.stop")); //$NON-NLS-1$
		button_stop.setImage(ImageService.getImage(BrowserPlugin.PLUGIN_ID, "icons/stop.gif"));
		button_stop.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			public void widgetSelected(SelectionEvent e) {
				view.getBrowser().stop();
				animateReloadButton(false);
			}
		});

		button_reload = new Button(this, SWT.PUSH);
		button_reload.setToolTipText(Messages.getString("Controls.reload")); //$NON-NLS-1$
		GridData gridData = new GridData();
		gridData.widthHint = 26;
		gridData.heightHint = 26;
		button_reload.setLayoutData(gridData);
		button_reload.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			public void widgetSelected(SelectionEvent e) {
				animateReloadButton(true);
				view.getBrowser().refresh();
			}
		});
		button_reload.addPaintListener(new PaintListener(){
			public void paintControl(PaintEvent e) {
				if(animateReloadButton && image!= null)
					e.gc.drawImage(image, 5, 5);
				else
					e.gc.drawImage(ImageService.getImage(BrowserPlugin.PLUGIN_ID, "icons/reload.gif"), 5, 5);
			}
		});

		loader = new ImageLoader();
		loader.load(getClass().getResourceAsStream("/icons/reloading.gif")); //$NON-NLS-1$
		image = new Image(button_reload.getDisplay(),loader.data[0]);
		gc = new GC(image);

		combo_url = new Combo(this, SWT.BORDER | SWT.SINGLE);
		combo_url.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		combo_url.setToolTipText(Messages.getString("Controls.url")); //$NON-NLS-1$
		combo_url.setVisibleItemCount(10);
		combo_url.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			public void widgetSelected(SelectionEvent e) {
				if((combo_url.getSelectionIndex() >= 0 && !combo_url.getText().equalsIgnoreCase(combo_url.getItem(combo_url.getSelectionIndex()))) || !combo_url.getText().equalsIgnoreCase(view.getBrowser().getUrl())){
					animateReloadButton(true);
					view.getBrowser().setUrl(combo_url.getText());
				}
			}
		});

		Button button_go = new Button(this, SWT.PUSH);
		button_go.setToolTipText(Messages.getString("Controls.open")); //$NON-NLS-1$
		button_go.setImage(ImageService.getImage(BrowserPlugin.PLUGIN_ID, "icons/go.gif"));
		button_go.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			public void widgetSelected(SelectionEvent e) {
				if((combo_url.getSelectionIndex() >= 0 && !combo_url.getText().equalsIgnoreCase(combo_url.getItem(combo_url.getSelectionIndex()))) || !combo_url.getText().equalsIgnoreCase(view.getBrowser().getUrl())){
					animateReloadButton(true);
					view.getBrowser().setUrl(combo_url.getText());
				}
			}
		});

		combo_search = new Combo(this, SWT.BORDER | SWT.SINGLE);
		gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gridData.widthHint = 80;
		combo_search.setLayoutData(gridData);
		combo_search.setVisibleItemCount(10);
		combo_search.setToolTipText(Messages.getString("Controls.keywords")); //$NON-NLS-1$
		combo_search.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			public void widgetSelected(SelectionEvent e) {
				if((last_search != null && !combo_search.getText().equals(last_search)) || last_search == null){
					search_history.put(combo_search.getText(), combo_search.getText());
					String temp = combo_search.getText();
					combo_search.setItems(search_history.values().toArray(new String[0]));
					combo_search.setText(temp);
					animateReloadButton(true);
					view.getBrowser().setUrl(Messages.getString("Controls.search_engine")+combo_search.getText()); //$NON-NLS-1$
					last_search  = combo_search.getText();
				}
			}
		});

		Button button_search = new Button(this, SWT.PUSH);
		button_search.setToolTipText(Messages.getString("Controls.search")); //$NON-NLS-1$
		button_search.setImage(ImageService.getImage(BrowserPlugin.PLUGIN_ID, "icons/search.gif"));
		button_search.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			public void widgetSelected(SelectionEvent e) {
				if((last_search != null && !combo_search.getText().equals(last_search)) || last_search == null){
					search_history.put(combo_search.getText(), combo_search.getText());
					String temp = combo_search.getText();
					combo_search.setItems(search_history.values().toArray(new String[0]));
					combo_search.setText(temp);
					animateReloadButton(true);
					view.getBrowser().setUrl(Messages.getString("Controls.search_engine")+combo_search.getText()); //$NON-NLS-1$
					last_search  = combo_search.getText();
				}
			}
		});

	}

	public Combo getUrlField(){return combo_url;}
	public Button getBackButton(){return button_back;}
	public Button getForwardButton(){return button_forward;}
	public Button getStopButton(){return button_stop;}
	public void animateReloadButton(boolean animate) {
		if(!animateReloadButton && animate) {
			animateReloadButton = animate;
			(new Thread(new MyThread())).start();
		}
		animateReloadButton = animate;
		button_stop.setEnabled(animate);
		imageNumber = 0;
		button_reload.redraw();
	}

	/**
	 * Thread to animate the reload button graphic
	 * @author mwalthart
	 *
	 */
	class MyThread implements Runnable{
		public void run(){
			while(animateReloadButton){
				try{
					Thread.sleep(loader.data[imageNumber].delayTime*5/3*10);
					if(animateReloadButton){
						if(!button_reload.getDisplay().isDisposed())
							button_reload.getDisplay().syncExec(new Runnable(){
								public void run(){
									imageNumber = (imageNumber+1) % loader.data.length;
									ImageData nextFrameData = loader.data[imageNumber];
									image = new Image(Display.getCurrent(),nextFrameData);
									button_reload.redraw();
								}
							});
					}
				}
				catch(Exception e) {}
			}
		}
	}
}
