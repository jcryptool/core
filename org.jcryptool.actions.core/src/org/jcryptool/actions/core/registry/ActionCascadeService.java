// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.actions.core.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.jcryptool.actions.core.types.ActionCascade;
import org.jcryptool.actions.core.types.ActionItem;

/**
 * <p>
 * <code>ActionCascadeService</code> object class. Contains the currently opened <code>ActionCascade</code>.
 * </p>
 *
 * @author Thomas Wiese
 * @version 0.5.0
 */
public final class ActionCascadeService {
    private static ActionCascadeService service = null;

    private IObservableList<ActionItem> actionItems = new WritableList<ActionItem>();
    private ActionCascade currentActionCascade;

    private ActionCascadeService() {
        currentActionCascade = null;
    }

    public static synchronized ActionCascadeService getInstance() {
        if (service == null) {
            service = new ActionCascadeService();
        }
        return service;
    }

    private void refreshObservable(ActionCascade ac) {
        actionItems.clear();
        actionItems.addAll(ac.getAllItems());
    }

    public void setCurrentActionCascade(ActionCascade ac) {
        refreshObservable(ac);
        this.currentActionCascade = ac;
    }

    public ActionCascade getCurrentActionCascade() {
        return this.currentActionCascade;
    }

    public void clearCurrentActionCascade() {
        this.currentActionCascade = null;
    }

    public void addItem(ActionItem item) {
        currentActionCascade.addItem(item);
        actionItems.add(item);
    }

    public void removeItem(ActionItem item) {
        this.currentActionCascade.removeItem(item);
        actionItems.remove(item);
    }

    public void setActionItems(WritableList<ActionItem> ai) {
        this.actionItems = ai;
    }

    public List<ActionItem> getActionItems() {
        return (List<ActionItem>) Collections.checkedList(actionItems, ActionItem.class);
    }

    public IObservableList<ActionItem> observeActionItems() {
        return actionItems;
    }

    public void moveUp(ActionItem item) {
        ArrayList<ActionItem> l = currentActionCascade.getAllItems();
        int i = l.indexOf(item);

        if (i > 0) {
            l.remove(i);
            l.add(i - 1, (ActionItem) (item));
            actionItems.remove(i);
            actionItems.add(i - 1, (ActionItem) item);
        }
    }

    public void moveDown(ActionItem item) {
        ArrayList<ActionItem> l = currentActionCascade.getAllItems();
        int i = l.indexOf(item);

        if (i < l.size() - 1) {
            l.remove(i);
            l.add(i + 1, (ActionItem) (item));
            actionItems.remove(i);
            actionItems.add(i + 1, (ActionItem) item);
        }
    }
}
