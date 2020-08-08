/*******************************************************************************
 * Copyright (c) 2010 Ahmed Mahran and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0 
 *
 * Contributors:
 *     Ahmed Mahran - initial API and implementation
 *******************************************************************************/

package org.eclipse.nebula.effects.stw;

import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Object implementing this interface enables the {@link TransitionManager}
 * to carry out transition effects either on it or on the object it's delegate for.<br/><br/>
 * 
 * A "transitionable" widget is a widget that can provide a set of methods to a {@link TransitionManager}
 * either through direct implementation of this interface or through 
 * delegation by providing a delegate object implementing this interface.<br/><br/>
 * 
 * A "transitionable" widget is supposed to have a list of {@link Control} objects of at 
 * least one {@link Control} object. Each {@link Control} object has an index that's used
 * to get and set the current viewed {@link Control} object using the {@link Transitionable#getSelection()}
 * and {@link Transitionable#setSelection(int)} method. The index is also used to get the
 * corresponding {@link Control} object using {@link Transitionable#getControl(int)} method.
 * 
 * 
 * @author Ahmed Mahran (ahmahran@gmail.com)
 */
public interface Transitionable {

    /**
     * This method is called once by the {@link TransitionManager}'s constructor 
     * to add a {@link SelectionListener} to the "transitionable" widget to start
     * the transition effect whenever the widget is selected.   
     * 
     * @param listener the {@link SelectionListener} instance provided by the {@link TransitionManager}
     */
    public void addSelectionListener(SelectionListener listener);

    /**
     * returns the {@link Control} object at index <i>index</i>
     * 
     * @param index the index of the {@link Control} object to return
     * @return the {@link Control} object at the specified index
     */
    public Control getControl(int index);
    
    /**
     * returns the {@link Composite} at which the transition should
     * be shown. It could be considered the composite that 
     * contains all {@link Control} objects.
     * 
     * @return the composite at which the transition should be shown
     */
    public Composite getComposite();
    
    /**
     * returns the index of the current selected {@link Control} object
     * 
     * @return the index of the current selected {@link Control} object
     */
    public int getSelection();
    
    /**
     * sets the current selected {@link Control} object
     * 
     * @param index the index of the {@link Control} object to be set as the current selection
     */
    public void setSelection(int index);
    
    /**
     * should compare toIndex with fromIndex and return 
     * the required direction of the transition.
     * 
     * @param toIndex index of the {@link Control} object to make transition to
     * @param fromIndex index of the {@link Control} object to make transition from
     * @return the required direction
     */
    public double getDirection(int toIndex, int fromIndex);
    
}
