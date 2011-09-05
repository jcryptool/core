//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.model.ui.wizard.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.core.util.input.AbstractUIInput;
import org.jcryptool.core.util.input.InputVerificationResult;
import org.jcryptool.core.util.input.handler.WidgetRelatedUIInputResultHandler;
import org.jcryptool.core.util.ui.SingleVanishTooltipLauncher;
import org.jcryptool.crypto.classic.model.ui.wizard.Messages;

/**
 * UIInputhandler which shows Bubbles as informations about verifications beneath
 * the widgets which are mapped to the UIInputs. Customize the messages by overriding
 * the message generation methods.
 *
 * @author Simon L
 */
public class WidgetBubbleUIInputHandler implements
		WidgetRelatedUIInputResultHandler, Observer {

	private static final int STATIC_ADDITIVE_FADETIME_DIMINISHMENT = 400;
	private static final int STANDARD_TIP_SHOWTIME = 14000;
	private static final double RECURRING_FADETIME_DIMINISHMENT = 0.42;

	public static final int STD_RESULT_TYPE = Integer.MIN_VALUE;

	private Map<AbstractUIInput<?>, Control> widgetMap;
	protected Map<AbstractUIInput<?>, SingleVanishTooltipLauncher> tooltipMap;

	private Shell shell;

	private Map<AbstractUIInput<?>, Object> lastDisplayedResultTypes = new HashMap<AbstractUIInput<?>, Object>();

	/**
	 * Creates a new Handler, displaying Balloon Tooltips on the specified shell.
	 *
	 * @param shell the Shell.
	 */
	public WidgetBubbleUIInputHandler(Shell shell) {
		super();
		this.shell = shell;
		this.widgetMap = new HashMap<AbstractUIInput<?>, Control>();
		this.tooltipMap = new HashMap<AbstractUIInput<?>, SingleVanishTooltipLauncher>();
	}

	/**
	 * Creates the object, and registers itself as Observer to the specified UIInputs.
	 *
	 * @param shell the shell where to display the tooltipps
	 * @param inputs the inputs
	 */
	public WidgetBubbleUIInputHandler(Shell shell, List<AbstractUIInput<?>> inputs) {
		this(shell);
		addAsObserverForInputs(inputs);
	}

	/**
	 * Adds this Handler as Observer for the given Input.
	 *
	 * @param input the UIInput that notifies this Handler then.
	 */
	public void addAsObserverForInput(AbstractUIInput<?> input) {
		input.addObserver(this);
	}

	public void addAsObserverForInputs(List<AbstractUIInput<?>> inputs) {
		for(AbstractUIInput<?> input: inputs) {
			addAsObserverForInput(input);
		}
	}

	/**
	 * This is, to (by default) decrease the time a tooltip is displayed, when only
	 * valid and totally "OK" inputs were made. This is, in common sense, when an accidental
	 * invalid input happened, and the user just types along (now just valid inputs),
	 * so that it seems that the tooltip can be closed earlier.
	 *
	 * @param tip the tooltip to decide about.
	 */
	protected void changeTooltipDurationAtCleaninputButNotHidden(AbstractUIInput<?> input) {
		if(tooltipMap.get(input) != null) {
			int remainingTime = (int) tooltipMap.get(input).getTimeToVanish();
			int newRemainingTime = (int) (remainingTime*RECURRING_FADETIME_DIMINISHMENT) - STATIC_ADDITIVE_FADETIME_DIMINISHMENT;

			tooltipMap.get(input).setTimeToVanish(newRemainingTime);
		}
	}

	/**
	 * whether to display a balloon tip for a specific verification message. Default:
	 * display message, when the result does not match {@link InputVerificationResult#DEFAULT_RESULT_EVERYTHING_OK}.
	 *
	 * @param origin the UIInput where the verification happened
	 * @param result the verification result
	 * @return whether to display a balloon tooltip
	 */
	protected boolean shallDisplayBalloonFor(AbstractUIInput<?> origin,
			InputVerificationResult result) {
		return ! result.equals(InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK);
	}

	/**
	 * calculates which message (the "body") to display on a balloon tooltip.
	 *
	 * @param origin the origin (an UIInput) of the whole ballon message
	 * @param result the verification result that led to the balloon
	 */
	protected String calcMsgForBalloon(AbstractUIInput<?> origin,
			InputVerificationResult result) {
		String mask = Messages.WidgetBubbleUIInputHandler_inputreset_reason_balloon_message;

		if(! result.isStandaloneMessage()) {
			return String.format(mask, result.getMessage());
		} else {
			return result.getMessage();
		}
	}

	/**
	 * Calculates the title of a balloon tooltip.
	 *
	 * @param origin the origin (an UIInput) of the whole ballon message
	 * @param result the verification result that led to the balloon
	 * @return
	 */
	protected String calcTitleForBalloon(AbstractUIInput<?> origin,
			InputVerificationResult result) {
		String mask = Messages.WidgetBubbleUIInputHandler_balloon_errorhandling_title;
		return String.format(mask, AbstractUIInput.firstUppercase(origin.getName()));
	}

	protected int calcTooltipDuration(AbstractUIInput<?> origin,
			InputVerificationResult result) {
		if(result.isStandaloneMessage()) {
			return 700 * (result.getMessage().length() / 7);
		} else {
			return (int) (STANDARD_TIP_SHOWTIME*0.7 + 700 * (result.getMessage().length() / 7));
		}
	}

	public void handleVerificationResultMsg(AbstractUIInput<?> origin,
			InputVerificationResult result) {

		boolean displayBalloon = shallDisplayBalloonFor(origin, result);
		if(getWidgetFor(origin) != null) {
			if(displayBalloon)
			{
				displayBalloonFor(origin, result);
				lastDisplayedResultTypes.put(origin, result.getResultType());
			} else {
				changeTooltipDurationAtCleaninputButNotHidden(origin);
			}
		}

	}

	public Object getLastDisplayedResultType(AbstractUIInput<?> input) {
		return lastDisplayedResultTypes.get(input);
	}

	/**
	 * Method for displaying a balloon. Override for customization.
	 *
	 * @param origin the UIInput where the processed InputVerificationResult came from
	 * @param result the InputVerificationResult
	 */
	protected void displayBalloonFor(AbstractUIInput<?> origin, InputVerificationResult result) {
		String title = calcTitleForBalloon(origin, result);
		String msg = calcMsgForBalloon(origin, result);
		Control ctrl = getWidgetFor(origin);
		Point ctrlcoords = ctrl.toDisplay(new Point(ctrl.getBounds().width-2, 2));
		int duration = calcTooltipDuration(origin, result);

		if(duration > 0) {
			//make a new Tooltip generator if this is the first tip ever
			if(tooltipMap.get(origin) == null) {
				tooltipMap.put(origin, new SingleVanishTooltipLauncher(shell));
			}
			SingleVanishTooltipLauncher launcher = tooltipMap.get(origin);
			launcher.showNewTooltip(ctrlcoords, duration, title, msg);
		}
	}

	/**
	 * The method that combines both mapping methods (first: overriding
	 * {@link #mapInputToWidget(UIInput)}; second: using {@link #addInputWidgetMapping(UIInput, Control)}).
	 *
	 * @param input the input, for which the mapped Control has to be retrieved.
	 * @return the mapped Control.
	 *
	 * @see #mapInputToWidget(UIInput)
	 * @see #addInputWidgetMapping(UIInput, Control)
	 */
	public Control getWidgetFor(AbstractUIInput<?> input) {
		Control result;
		if((result=mapInputToWidget(input)) == null) {
			return widgetMap.get(input);
		}

		return result;
	}

	/**
	 * Convenience form of creating a mapping between UIInputs and Widgets, next to, or
	 * instead of overriding {@link #getControlFor(UIInput)}, which is the universal
	 * method for that purpose.<br /><br />
	 * The mapping in execution will prefer the mappings generated by {@link #getControlFor(UIInput)}
	 * over mappings by this method. Such, a Control w will only be mapped to an input
	 * i in the end (when {@link #addInputWidgetMapping(UIInput, Control) addInputMapping(i, w)} was called, when {@link #getControlFor(UIInput)} returns null for i.
	 *
	 * @param input
	 * @param mapping
	 */
	public void addInputWidgetMapping(AbstractUIInput<?> input, Control mapping) {
		widgetMap.put(input, mapping);
	}

	public Control mapInputToWidget(AbstractUIInput<?> input) {
		return null;
	}

	/**
	 * Disposed all tooltips.
	 */
	public void dispose() {
		for(Entry<AbstractUIInput<?>, SingleVanishTooltipLauncher> entry: tooltipMap.entrySet()) {
			entry.getValue().dispose();
		}
	}

	public void update(Observable o, Object arg) {
		if(o instanceof AbstractUIInput<?>) {
			AbstractUIInput<?> input = (AbstractUIInput<?>) o;
			if(arg instanceof InputVerificationResult) {
				InputVerificationResult result = (InputVerificationResult) arg;
				handleVerificationResultMsg(input, result);
			}
		}
	}

}
