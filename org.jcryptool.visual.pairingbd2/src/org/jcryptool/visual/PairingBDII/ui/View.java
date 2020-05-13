//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.PairingBDII.ui;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.internal.C;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.util.ui.auto.LayoutAdvisor;

public class View extends ViewPart {
	private IntroductionAndParameters situation;
	private Illustration illustration;
	private DefinitionAndDetails protocol;
	private Logging tryagain;
	private ScrolledComposite scrolledComposite;
	private Composite parent;

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;

		scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);

		Composite scrolledContent = new Composite(scrolledComposite, SWT.NONE);
		scrolledContent.setLayout(new GridLayout(2, false));
		
		situation = new IntroductionAndParameters(scrolledContent);
		illustration = new Illustration(scrolledContent);
		protocol = new DefinitionAndDetails(scrolledContent);
		tryagain = new Logging(scrolledContent);

		Model.getDefault().setLinks(situation, illustration, protocol, tryagain);
		Model.getDefault().setNumberOfUsers(4);
		Model.getDefault().reset();
		Model.getDefault().setupStep1();

		scrolledComposite.setContent(scrolledContent);
		scrolledComposite.setMinSize(scrolledContent.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
//		scrolledComposite.setOr
		
		LayoutAdvisor.addPreLayoutRootComposite(parent);
		
		// Register the context help for this plugin.
		PlatformUI.getWorkbench().getHelpSystem()
			.setHelp(parent.getShell(),"org.jcryptool.visual.PairingBDII.view");
		
		addScrollListnerToChildren(parent);
		
	}
	
	private void addScrollListnerToChildren(Control control) {
		for (Control c : ((Composite) control).getChildren()) {
			// Chec
			System.out.println(c.toString());
			if (c instanceof Text) {
				// Check if the control has no MouseWheel Listener
				if (c.getListeners(SWT.MouseWheel) != null) {

					c.addListener(SWT.MouseVerticalWheel, new Listener() {
						
						@Override
						public void handleEvent(Event e) {
							System.err.println("Listener triggered; Object: " + e.widget.toString());
							
							if (e.type == SWT.MouseWheel) {
								
//								((Text) c).getVerticalBar()
//								Display.getCurrent().getCursorControl();
//								Display.getCurrent().setcur
//								c.getListeners(SWT.MouseWheel);
								ScrollBar verticalBar = ((Text) e.widget).getVerticalBar();
								Event event = e;
//								event.type = SWT.MouseVerticalWheel;
//								event.count = e.count;
//								event.display = e.display;
								event.widget = ((Control) e.widget).getParent();
//								event.x = e.x;
//								event.y = e.y;
//								event.
//								e.s
								
								if (verticalBar != null) {
									System.out.println("Vertical bar: " + verticalBar.toString());
								} else {
									System.out.println("Vertical bar: null");
//									Event event 
//									event.count = e.count;
									// The control has no scrollbar
//									((Control) e.getSource()).getParent().

//									c.notifyListeners(SWT.Mouse, event);
									((Control) e.widget).getParent().notifyListeners(SWT.MouseVerticalWheel, event);
								}
								
//								scrollparent(((Control) e.getSource()).getParent());
								
								// The user scrolled up
								if (e.count > 0) {

								} else if (e.count < 0) {
									// The user scrolled down.
									
								}
								
							}
							
						}
					});
//					c.f
//					EventH
					c.addMouseWheelListener(new MouseWheelListener() {
						
						@Override
						public void mouseScrolled(MouseEvent e) {
							System.err.println("MouseWheelListener triggered; Object: " + e.getSource().toString());
//							((Text) c).getVerticalBar()
//							Display.getCurrent().getCursorControl();
//							Display.getCurrent().setcur
//							c.getListeners(SWT.MouseWheel);
							ScrollBar verticalBar = ((Scrollable) e.widget).getVerticalBar();
							Event event = new Event();
							event.type = SWT.MouseVerticalWheel;
							event.count = e.count;
							event.display = e.display;
							event.widget = ((Control) e.widget).getParent();
//							e.g
							event.x = e.x;
							event.y = e.y;
//							event.
//							e.s
							
							if (verticalBar != null) {
								System.out.println("Vertical bar: " + verticalBar.toString());
							} else {
								System.out.println("Vertical bar: null");
//								Event event 
//								event.count = e.count;
								// The control has no scrollbar
//								((Control) e.getSource()).getParent().

//								c.notifyListeners(SWT.Mouse, event);
								((Control) e.widget).getParent().notifyListeners(SWT.MouseVerticalWheel, event);
							}
							
//							scrollparent(((Control) e.getSource()).getParent());
							
							// The user scrolled up
							if (e.count > 0) {

							} else if (e.count < 0) {
								// The user scrolled down.
								
							}
						}

//						private void scrollparent(Control parent) {
//							// TODO Auto-generated method stub
//							parent.
//							
//						}
					});
				}

//				((Text) c).getMouseWheelListener();
			}
			Class<?> cls = c.getClass();
//			Scrollable.class.isAssignableFrom(cls);
//			boolean subtype = c.getClass().isAssignableFrom(cls);
			if (Composite.class.isAssignableFrom(cls)) {

				addScrollListnerToChildren(c);
			}
		}
	}

	@Override
	public void setFocus() {
		scrolledComposite.setFocus();
	}

	public void reset() {
		Control[] children = parent.getChildren();
		for (Control control : children) {
			control.dispose();
		}
		createPartControl(parent);
		parent.layout();
	}
}