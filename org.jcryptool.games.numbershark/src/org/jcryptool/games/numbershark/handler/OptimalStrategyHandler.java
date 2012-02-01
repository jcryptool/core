// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.numbershark.handler;

import java.util.ArrayList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.games.numbershark.optStrat.OptimalStrategyDialog;
import org.jcryptool.games.numbershark.optStrat.ResultDialog;
import org.jcryptool.games.numbershark.util.CommandState;
import org.jcryptool.games.numbershark.util.CommandStateChanger;
import org.jcryptool.games.numbershark.views.NumberSharkView;

/**
 * This handler starts the wizard for calculating optimal strategies.
 * 
 * @author Johannes Späth
 * @version 0.9.5
 */
public class OptimalStrategyHandler extends AbstractHandler {
	public static final int PLAY = 9998;

	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (HandlerUtil.getActivePart(event) instanceof NumberSharkView) {

			NumberSharkView view = ((NumberSharkView) HandlerUtil
					.getActivePart(event));

			OptimalStrategyDialog optStrat = new OptimalStrategyDialog(
					HandlerUtil.getActiveShell(event));
			optStrat.create();
			optStrat.open();

			if (optStrat.getReturnCode() == IDialogConstants.OK_ID) {
				
				ResultDialog calculate;
				if (optStrat.getCalculateStrategy()) {
					calculate = new ResultDialog(
							HandlerUtil.getActiveShell(event), true);
					calculate.setBounds(optStrat.getMin(), optStrat.getMax());
				} else {
					calculate = new ResultDialog(
							HandlerUtil.getActiveShell(event), false);
				}
				calculate.open();

				if (calculate.getReturnCode() == PLAY) {
					ArrayList<Integer> playSeq = calculate.getPlaySequence();
					view.cleanPlayingField();
					view.createPlayingField(playSeq.get(0));
					for (int i = 1; i < playSeq.size(); i++) {
						view.deactivateNumber(playSeq.get(i));
					}
					ArrayList<Integer> sharkMealList = view.getSharkMealList();
					int[] lostNumbers = new int[sharkMealList.size()];

					for (int i = 0; i < sharkMealList.size(); i++) {
						lostNumbers[i] = sharkMealList.get(i);
						view.disableNumber(lostNumbers[i] - 1);
					}

					view.addMoveToTable(0, lostNumbers);
					CommandStateChanger commandStateChanger = new CommandStateChanger();
					commandStateChanger.chageCommandState(
							CommandState.Variable.SHARKMEAL_STATE,
							CommandState.State.SHARKMEAL_DISABLED);
				}

			}
		}
		return null;
	}
}
