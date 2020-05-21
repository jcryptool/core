package org.jcryptool.core.util.ui.auto;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Text;

public class SmoothScroller {
	
	private static SmoothScroller defaultInstance;
 
	public static SmoothScroller getInstance() {
		if (SmoothScroller.defaultInstance == null) {
			SmoothScroller.defaultInstance = new SmoothScroller();
		}
		return SmoothScroller.defaultInstance;
	}
	
	public static void scrollTextfieldSmooth(Composite c) {
		SmoothScroller sm = SmoothScroller.getInstance();
		sm.addTo(c);
	}
	
	private void addTo(Control sc) {
		for (Control child : ((Composite) sc).getChildren()) {
			if (child instanceof Text) {
				Listener[] l = child.getListeners(SWT.MouseVerticalWheel);
				
				// True, if no listeneres are registered on the object
				if (l.length == 0) {
					child.addMouseWheelListener(new MouseWheelListener() {
						
						@Override
						public void mouseScrolled(MouseEvent e) {
							
							Event event = new Event();
							event.count = e.count;
							event.type = SWT.MouseVerticalWheel;
//							event.x = 3;
//							event.y = 100;
							event.display = e.display;

							Text t = (Text) e.getSource();
							Composite parent = t.getParent();

							
							ScrollBar childsVerticalBar = ((Text) child).getVerticalBar();
							// The Text has no Scrollbar -> Scroll the outer scrollbar
							// There is no scrollbar or it cant be used
							// Copied from the internet. Should be slightly more stable
							// than only childsVerticalBar == null.
							if (childsVerticalBar == null || ((childsVerticalBar.getMinimum() == 0
			                        && childsVerticalBar.getMaximum() == 0
			                        && childsVerticalBar.getSelection() == 0)
			                            || !childsVerticalBar.isEnabled()
			                            || !childsVerticalBar.isVisible())) {
								

								parent.notifyListeners(SWT.MouseVerticalWheel, event);
							
							} else {
								// The Text has a scrollbar -> Scroll the inner scrollbar until it reached a maximum and
								// then scroll the inner scrollbar.
								System.out.println("ChildVerticalSC minimum: " + childsVerticalBar.getMinimum());
								System.out.println("ChildVerticalSC maximum: " + childsVerticalBar.getMaximum());
								System.out.println("ChildVerticalSC selection: " + childsVerticalBar.getSelection());
								System.out.println("ChildVertical size: " + childsVerticalBar.getSize().toString());
								
								// Das Mausrad wird nach oben bewegt: e.count = 3
								if (e.count == 3) {
									// Die Scrollbar des Textes ist ganz oben -> das parent Control
									// soll jetzt nach oben gescrollt werden.
									if (childsVerticalBar.getSelection() == 0) {
										parent.notifyListeners(SWT.MouseVerticalWheel, event);
									}
								}
								
								// Das Mausrad wird nach unten bewegt: e.count = -3
								if (e.count == -3) {
									// Die Scrollbar des Textes steht ganz unten
									// Das parent Comtrol sollte jetzt nach unten gescrollt werden.
									if (childsVerticalBar.getMaximum() - childsVerticalBar.getSelection() == childsVerticalBar.getSize().y) {
										parent.notifyListeners(SWT.MouseVerticalWheel, event);
									}
								}
							}
						}
					});
				}

			}
			
			// Das macht den rekursiven Aufruf der Methode.
			// Es prüft, ob das aktuelle Control ein Composite ist
			// oder eine Klasse die davon erbt. Nur dann soll die
			// Methode aufgerufen werden, da andere Klassen nicht
			// die Methode getChildren() haben, mit der ich in 
			// Zeile 40 die childs eines Controls hole.
			Class<?> cls = child.getClass();
			if (Composite.class.isAssignableFrom(cls)) {
				addTo(child);
			}
		}

	}
	
	

}
