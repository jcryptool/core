//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.actions.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.actions.core.registry.ActionCascadeService;
import org.jcryptool.actions.core.types.ActionItem;
import org.jcryptool.actions.ui.views.ActionView;

/**
 * <b>Start handler</b> for the Actions view. Runs the created or imported action cascade.
 *
 * @author Thomas Wiese
 * @version 0.5.0
 */
public class MoveUpHandler extends AbstractHandler {
	
    @Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
        ISelection selection = HandlerUtil.getCurrentSelection(event);
        if (selection instanceof IStructuredSelection) {
        	ActionView view = (ActionView) HandlerUtil.getActivePart(event);
            TableViewer viewer = view.getViewer();
            
            ActionItem item = (ActionItem) ((IStructuredSelection) selection).getFirstElement();
            ActionCascadeService.getInstance().moveUp(item);
            
            //Set focus on moved row
            viewer.setSelection(new StructuredSelection(item)); 
            
        }
        return null;
    }
    
 }