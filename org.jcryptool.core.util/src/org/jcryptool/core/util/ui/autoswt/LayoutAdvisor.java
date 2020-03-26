package org.jcryptool.core.util.ui.autoswt;

import java.rmi.UnexpectedException;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.eclipse.jface.viewers.CellEditor.LayoutData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IViewPart;

public class LayoutAdvisor {
	
	private static LayoutAdvisor defaultInstance;

	public Map<IHandledUIElement<Control>, List<IControlEventHandler>> managed = new IdentityHashMap<IHandledUIElement<Control>, List<IControlEventHandler>>();
	
	private List<IControlEventHandler> getOrCreateHandlersForManaged(IHandledUIElement c) {
		List<IControlEventHandler> list = managed.get(c);
		if (! managed.containsKey(c)) {
			throw new RuntimeException(String.format("Control %s is still unregistered with the LayoutAdvisor", c));
			
		}
		if (list == null) {
			list = new LinkedList<>();
		}
		return list;
	}
	
	public IHandledUIElement<Control> getHandledFor(Object registrationObject)

	public void addHandler(IHandledUIElement c, IControlEventHandler handler) {
		List<IControlEventHandler> list = getOrCreateHandlersForManaged(c);
		list.add(handler);
		this.managed.put(c, list);
	}

	public void addHandlers(IHandledUIElement c, Collection<? extends IControlEventHandler> handlers) {
		List<IControlEventHandler> list = getOrCreateHandlersForManaged(c);
		list.addAll(handlers);
		this.managed.put(c, list);
	}

	public List<IControlEventHandler> getHandlers(IHandledUIElement c) {
		List<IControlEventHandler> list = managed.get(c);
		if (list == null) {
			list = new LinkedList<>();
		}
		return list;
	}
	
	public static LayoutAdvisor getInstance() {
		if( LayoutAdvisor.defaultInstance == null ) {
			LayoutAdvisor.defaultInstance = new LayoutAdvisor();
			return getInstance();
		}
		return LayoutAdvisor.defaultInstance;
	}
	
	public void addManaged(Control root) {
		this.addManaged(root, root);
		this.managed.put(root, null);
	}

	public void addManaged(Object something, Control root) {
		Object key = something;

		if (this.managed.containsKey(key)) {
			return;
		}
		IHandledUIElement<Control> handled = new DefaultHandledControlElement(root, something);
		this.managed.put(key, null);
	}

	public void addHandler(IViewPart viewPart,
			IControlEventHandler createScrolledWrapWidthHintHandler) {
		// TODO Auto-generated method stub
		
	}
	
}
