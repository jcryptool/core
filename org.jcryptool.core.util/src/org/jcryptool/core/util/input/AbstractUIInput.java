// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.util.input;

import java.util.Observable;
import java.util.Observer;

import org.jcryptool.core.logging.utils.LogUtil;

/**
 * An AbstractUIInput instance stands for one single user input. It ensures, that the stored input
 * is always <br />
 * <br />
 * - valid (specify validation routines)<br />
 * - coherent with the UI side of the input.<br />
 * <br />
 * 
 * The AbstractUIInput is an Observable, sending updates <b>with data = null</b> to all Observers
 * when really changed. <br />
 * It also send updates whenever a user action was put through verification, with the data part of
 * the update containing the verification report ({@link InputVerificationResult}). <br />
 * <br />
 * 
 * Use this class by overriding all necessary methods, and make sure, that every UI-side input made
 * by the user and concerning either the UI state of the input or the content, notifies this input
 * about it by calling {@link #synchronizeWithUserSide()}.
 * 
 * @author Simon L
 */
public abstract class AbstractUIInput<Content> extends Observable implements Observer {

    protected String mask_external_reset = Messages.AbstractUIInput_0;
    private Content internalRepresentation;

    /**
     * Generates a generic AbstractUIInput for putting as origin for rereads originating not from a
     * UIInputObject.
     * 
     * @param origin the generic origin name
     * @return a generic AbstractUIInput which exists solely for reading its name.
     */
    private static AbstractUIInput<?> GENERIC_INPUT(final String origin) {
        return new AbstractUIInput<Object>() {
            @Override
            protected InputVerificationResult verifyUserChange() {
                return InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK;
            }

            @Override
            public Object readContent() {
                return null;
            }

            @Override
            public void writeContent(Object content) {
            }

            @Override
            protected Object getDefaultContent() {
                return null;
            }

            @Override
            public String getName() {
                return origin;
            }
        };
    };

    /**
     * Constructs a UI Input and sets the default value.
     */
    public AbstractUIInput() {
        initializationActions();
        try {
            tryToSetDefaultValues();
        } catch (Exception ex) {
            LogUtil.logError(ex);
        }
    }

    /**
     * Anchor method to allow actions before any other things in the constructor. For Subclasses of
     * AbstractUIInput derivates that use gui elements, this method could be overridden to
     * initialize the gui elements first.
     */
    protected void initializationActions() {
    }

    /**
     * returns the String, just with its first character transformed into uppercase. This for
     * forming correct sentences from generic blocks.
     */
    public static String firstUppercase(String string) {
        if (string != null) {
            if (string.length() > 0) {
                return String.valueOf(string.charAt(0)).toUpperCase().concat(string.substring(1));
            }
            return string;
        }
        return null;
    }

    // /**
    // * calculates what Text will be created when the Modification, intercepted by
    // * the VerifyEvent, happens. Just character additions are handled.
    // *
    // * @param textBeforeTry text before Modification
    // * @param mod the Verification event that intercepted the modification
    // * @return
    // */
    // private String calculateTextAfterModification(String textBeforeTry, VerifyEvent mod) {
    // String beforeSel = textBeforeTry.substring(0, mod.start);
    // String afterSel = textBeforeTry.substring(mod.start, textBeforeTry.length());
    // String result;
    //
    // if(mod.character == SWT.DEL) {
    // if(mod.start == mod.end) {
    // result = beforeSel + afterSel.substring(1);
    // } else {
    // result = beforeSel + afterSel;
    // }
    // } else if(mod.character == SWT.DEL) {
    // if(mod.start == mod.end) {
    // result = beforeSel.substring(0, beforeSel.length()-1) + afterSel;
    // } else {
    // result = beforeSel + afterSel;
    // }
    // } else if(mod.character != SWT.MODIFIER_MASK) {
    // result = beforeSel + mod.text + afterSel;
    // } else {
    // result = textBeforeTry;
    // }
    //
    // return result;
    // }
    //

    /**
     * default inner method to set the default values at initialization.
     */
    private void tryToSetDefaultValues() {
        setInputContent(getDefaultContent(), InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK);
        saveDefaultRawUserInput();
        resetUserInput();
    }

    /**
     * This method should be called, when the user tried to make a change DIRECTLY concerning this
     * input (applies not when another input was changed, that has influence on e. g. the semantical
     * correctness of this input, because this is handled over the Observer dependency structure of
     * UIInputs.
     * 
     * <br/>
     * <br />
     * Use this method, when a text verification event can be retrieved, which allows to revert
     * changes on semantical incorrectness directly.
     * 
     * @param evt the VerificationEvent
     * @param textBeforeTry the text before the tried change
     * @throws NullPointerException when the event is null
     * 
     */

    /**
     * Should be used when a reread must be done, but there is no UIInput which requested it. <br />
     * <br />
     * <b>Note, that this is NOT the standard method for synchronizing UI with Input</b>. The
     * default method to be called e. g. inside a textfield listener (when the textfield regarding
     * this input has a changed text) is {@link #synchronizeWithUserSide()}.
     * 
     * @param originDescription a textual description of the context in which the reread occured.
     *            Name pattern: same as the return value of {@link #getName()} -- think of it as of
     *            a name of a AbstractUIInput.
     */
    public void reread(String originDescription) {
        reread(GENERIC_INPUT(originDescription));
    }

    /**
     * Should only be invoked, when circumstances change, that do not directly belong to the input,
     * but have influence on its semantics. Normally, such things should be handled by having these
     * influences as UIInputs, too, and adding this AbstractUIInput as Observer. <br />
     * <br />
     * <b>Note, that this is NOT the standard method for synchronizing UI with Input.</b> The
     * default method to be called e. g. inside a textfield listener (when the textfield regarding
     * this input has a changed text) is {@link #synchronizeWithUserSide()}.
     * 
     * @param inputWhichCausedThis The UIInput which caused the reread.
     */
    public void reread(AbstractUIInput<?> inputWhichCausedThis) {
        InputVerificationResult verificationResult;

        if ((verificationResult = verifyUserChangeWithAutocorrection()).isValid()) { // if the user
                                                                                     // made a
                                                                                     // semantically
                                                                                     // OK action
            setInputContent(readContent(), verificationResult); // read out the user input into the
                                                                // content

            this.setChanged();
            notifyObservers(verificationResult); // send ui the verification result

            // and (by default this calls nothing) save the user input
            // (it is assumed that the user input can be reset to the state it is now
            // by just calling writeContent -- user input and saved data is related bijective.
            // But if its not, classes will override and change this.
            saveRawUserInput();
        } else {
            resetExternallyCaused(inputWhichCausedThis);

            setChanged();
            notifyObservers(makeVerificationResultExternallyCaused(verificationResult, inputWhichCausedThis));
        }
    }

    /**
     * This method tries to generate a message which indicates that the content of this input just
     * got invalid because of another input that has changed, making this input invalid.<br />
     * <br />
     * The standard implementation relies on that the incoming verification results have as message
     * a string "X" that fits in the scheme <i>The input has been reset/changed automatically,
     * because <b>X</b>.</i> -- the messages best responds to the "because" scheme. Also, the
     * Verification result type will have the type {@link MWizardMessage#INFORMATION INFORMATION}. <br />
     * <i>This is just a standard implementation as said, so this method is meant to be
     * overridden.</i>
     * 
     * @param verificationResult comes from the verification that was stating this input is not
     *            valid anymore. Contains mostly a textual reason.
     * @param inputWhichCausedThis The input which caused the new invalidity.
     * @return the Verification result that is handed to the observer in the end.
     */
    protected InputVerificationResult makeVerificationResultExternallyCaused(
            final InputVerificationResult verificationResult, final AbstractUIInput<?> inputWhichCausedThis) {

        return new InputVerificationResult() {
            public boolean isStandaloneMessage() {
                return true;
            }

            public MessageType getMessageType() {
                return MessageType.INFORMATION;
            }

            public String getMessage() {
                String mask = mask_external_reset;
                return String.format(mask, inputWhichCausedThis.getName(), getName(), verificationResult.getMessage());
            }

            public boolean isValid() {
                return true;
            }
        };
    }

    /**
     * This is the main method for synchronizing the input object with the user side. Every time the
     * user made a input that concerns either the internal input representation (the semantic part)
     * or even solely the UI state of this input (for example the position of the cursor in the
     * corresponding text field) this method should be called. Of course, only call this method,
     * when the changed information is processed in {@link #saveRawUserInput()} or
     * {@link #verifyUserChange()} or {@link #readContent()}.
     * 
     * <br />
     * <br />
     * The AbstractUIInput tries the following:<br />
     * * call {@link #verifyUserChange()}; <br />
     * * if the result of it (of type InputVerificationResult) has the flag
     * {@link InputVerificationResult#isValid() .isValid()} <br />
     * ~~> read the input into the internal representation via {@link #readContent()} <br />
     * ~~> if the internal representation did change because of this, notification to all Observers
     * (data: null) and another one (data: verification result object) is sent <br />
     * * if the verification was not valid (flag was false): <br />
     * ~~> call {@link #resetUserInput()} (try to pretend this erroneus input did not happen) ~~>
     * send notification to all observers (data: the verification result object)
     */
    public void synchronizeWithUserSide() {
        InputVerificationResult verificationResult;

        if ((verificationResult = verifyUserChangeWithAutocorrection()).isValid()) { // if the user
                                                                                     // made a
                                                                                     // semantically
                                                                                     // OK action
            Content previousContent = getContent();
            boolean changed = setInputContent(readContent(), verificationResult); // read out the
                                                                                  // user input into
                                                                                  // the content

            if (decideNotifyObserversAboutUserSideSynchronization(changed, previousContent, getContent(),
                    verificationResult)) {
                this.setChanged();
                notifyObservers(verificationResult); // send ui the verification result
            }

            // and (by default this calls nothing) save the user input
            // (it is assumed that the user input can be reset to the state it is now
            // by just calling writeContent -- user input and saved data is related bijective.
            // But if its not, classes will override and change this.
            saveRawUserInput();
        } else {
            // by default, this tries to revert the user's change to the last saved
            // state (which is supposedly valid) by calling writeContent. Although, when
            // User input and saved data is not related bijectively, the userInput may not
            // be directly reverted, and look different and such, confusing, to the user.
            // so, classes can override this, too.
            resetUserInput();

            this.setChanged();
            notifyObservers(verificationResult); // notify UI/whatever observer
        }
    }

    /**
     * this method decides whether to inform all observers about: - synchronizing with the user side
     * when - the validation returned 'valid'
     * 
     * <br>
     * <br>
     * by default, the observers are informed when the content of this UIInput object did actually
     * change (see {@link #setInputContent(Object, InputVerificationResult)} return contract). <br>
     * Override this function to define your own strategy.
     * 
     * @param changed
     * @param previousContent
     * @param newContent
     * @param verificationResult
     * @return
     */
    protected boolean decideNotifyObserversAboutUserSideSynchronization(boolean changed, Content previousContent,
            Content newContent, InputVerificationResult verificationResult) {
        return changed;
    }

    /**
     * This method is called when the user input is no more valid because of inputs that were
     * outside the competency of this input.<br />
     * <br />
     * It is now tried to reset the input to a state where it complies to the new situation. By
     * default this is the standard content generated the same way as at default AbstractUIInput
     * construction. Override, to make a better adaption to changed circumstances (to not erase the
     * whole entered content, in many cases).
     * 
     * @param inputWhichCausedThis the input which caused this.
     */
    protected void resetExternallyCaused(AbstractUIInput<?> inputWhichCausedThis) {
        tryToSetDefaultValues();
    }

    /**
     * This method is intended to reset the user input data on the UI level to the state where it
     * was when the last valid data was read from the user input. The default implementation uses
     * the {@link #writeContent(Object)} method to generate a UI user input presentation from the
     * saved, valid input. Although, when user input on the UI side, and the read data from it (in
     * this class) is not isomorph/bijective formed, this may result in incoherence on the UI side,
     * and such, this method may be needed to save additional information about what the user told
     * the UI when the last valid input occured.
     */
    protected void resetUserInput() {
        writeContent(getContent());
    }

    /**
     * This method is optional and is intended to save the raw user input which was used to generate
     * the data that is now contained in this instance. By default, this method does nothing,
     * because the standard implementation uses the writeContent method to generate the user input
     * back from the data that was drawn from it. So this method may be subclassed when coherent
     * userdata presentation is needed, because the standard implementation assumes that user input
     * presentation and saved data presentation is isomorph / bijective.
     * 
     * <br />
     * <br />
     * <b>important:</b> If you extend this method to save something the user data can be reset to
     * by calling {@link #resetUserInput()}, make sure you do also extend
     * {@link #saveDefaultRawUserInput()}, because {@link #resetUserInput()} is called directly
     * after initializing this instance with the default data ({@link #getDefaultContent()}), and
     * such, this method could run into null pointers or other uninitialized data.
     */
    protected void saveRawUserInput() {

    }

    /**
     * Saves generic raw user input information for use of {@link #resetUserInput()}. This method is
     * to ensure that there is always initialized data for the {@link #resetUserInput()} method
     * (which is optional, so this method is optional, too). By default, it does nothing but calling
     * {@link #saveRawUserInput()}.
     * 
     */
    protected void saveDefaultRawUserInput() {
        saveRawUserInput();
    }

    /**
     * validates the user input (not yet taken into this instances actual content). Note: This
     * method must validate that the user input is compliant with every other input at this moment,
     * so check in dependency of other inputs, too. <br />
     * <br />
     * This method will be called, too, when another Input has changed, maybe notifying this input,
     * issuing a revalidation (happening in this method).<br />
     * <br />
     * 
     * Note, that this method must absolutely reject every invalid input. The <I>autocorrection</I>
     * methods ({@link #canAutocorrect(InputVerificationResult)},
     * {@link #autocorrect(InputVerificationResult)}) are the functions to even out minor flaws in
     * the user input, but these flaws have to be pointed out through this method. <br />
     * <br />
     * 
     * <b>Output format:</b><br />
     * - {@link InputVerificationResult#isValid()} must return the right thing<br />
     * - {@link InputVerificationResult#getMessageType()} should be {@link MWizardMessage#NONE NONE}
     * if everything is alright, and in nearly every other case, {@link MWizardMessage#WARNING
     * WARNING}.<br />
     * - {@link InputVerificationResult#getMessage()} should return a short reason of the rejection,
     * or warning in the following style: <i>the frequency input must be a number</i> (or: text
     * fragment stating a reason, which could follow on 'because')<br />
     * 
     * <br />
     * <br />
     * 
     * Scenario: until 10 milliseconds ago, the user wrote just the text '124.38', a decimal number,
     * which is the desired input format. Now, hey typed an "§" (accidentally, instead of a '3'),
     * and therefore violates the user input format. The text field already contains this character
     * in this scenario.<br />
     * What has to be done by this function, is: <br />
     * return an appropriate InputVerificationResult; recommended: a warning-style message (because
     * this error will be reverted instantly), with invalid flag. <br />
     * <br />
     * What follows: The input will of course not be taken into this input object, because it is no
     * decimal number. Instead, this input object will try to revert the UI side to the last saved
     * validated state.
     * 
     * @return information object about the validation. Use
     *         {@link InputVerificationResult#DEFAULT_RESULT_EVERYTHING_OK} for signalising
     *         completely validation withour errors, tips or warnings.
     * 
     * @see #saveRawUserInput()
     * @see #resetUserInput()
     * 
     */
    protected abstract InputVerificationResult verifyUserChange();

    /**
     * @return verifies the user change and applies auto correction if possible
     */
    private InputVerificationResult verifyUserChangeWithAutocorrection() {
        InputVerificationResult verifyResult = verifyUserChange();

        if (canAutocorrect(verifyResult)) {
            while (canAutocorrect(verifyResult)) {
                autocorrect(verifyResult);
                verifyResult = verifyUserChange();
            }
        }

        return verifyResult;
    }

    /**
     * Reads the userInput from somewhere (SWT widgets, variables, etc.), so that the content can be
     * stored in this class in its pure form (e. g. the user made a string input which represents a
     * key. With this method, the AbstractUIInput tries to read the string key from the user,
     * transforming it into a, say, CustomKey object.<br />
     * <br />
     * This method is normally executed only when the user input from which the data is read is
     * verified semantically correct.<br />
     * <br />
     * This method itself does not store information in the object!
     */
    public abstract Content readContent();

    /**
     * Writes the content to somewhere user-visible (write text to swt widgets, call functions which
     * do that, etc). <br />
     * Please notice, that the {@link #resetUserInput()} is often making changes to the user
     * interface side too, sometimes completely shadowing the efforts of this method.
     * 
     * <br />
     * This makes sense, because often the simple conversion of the internal representation into
     * user-visible data can be ambiguous, so for example, the usere entered the string
     * "100.0000000000", and the number "100" would have been stored internally. Then, a the user
     * made an erroneous input, maybe an invalid character that has nothing to do with a number, and
     * now this input of the user has to be reverted to the alst saved state, which was "100". the
     * string displayed would now be "100", and the string wouldve been jumped from "100.0000000" to
     * "100", which is bad. <br />
     * <br />
     * The methods {@link #saveRawUserInput()} and {@link #resetUserInput()} take care of making the
     * user side of the input look consistent. In the described case, everytime the user made change
     * to the text, the saveRawUserInput woudve saved the pure string, and in the reset case, the
     * resetUserInput method wouldve reset the text fields content to the saved string, and not just
     * to the pure number "100".
     * 
     * 
     * 
     * @param content the content to be written/displayed/whatever.
     */
    public abstract void writeContent(Content content);

    /**
     * @return the content that represents the initial state of this input
     */
    protected abstract Content getDefaultContent();

    /**
     * For giving the input a name.
     * 
     * @return the name of the input.
     */
    public abstract String getName();

    /**
     * @return the internally stored (pure) form of the user input.
     */
    public Content getContent() {
        return internalRepresentation;
    }

    /**
     * @param internalRepresentation the content
     * @param verificationResult the verification result that accompanied this setting.
     * @return whether the input actually did change
     */
    protected boolean setInputContent(Content inputContent, InputVerificationResult verificationResult) {
        if ((this.internalRepresentation != null && inputContent == null)
                || (inputContent != null && !inputContent.equals(this.internalRepresentation))) {
            this.internalRepresentation = inputContent;
            this.setChanged();
            notifyObservers();
            return true;
        } else {
            this.internalRepresentation = inputContent;
            return false;
        }
    }

    public void update(Observable o, Object arg) {
        // the observable that notifies here is another AbstractUIInput.
        // It signals that it has changed, and since this is an Observer to
        // the notifying AbstractUIInput, this AbstractUIInput need to be verified semantically now.

        if (o instanceof AbstractUIInput) {
            if (arg == null) { // if the input was really changed
                reread((AbstractUIInput<?>) o);
            }
        }

    }

    /**
     * At verification, tries to determine if a InputVerificationResult (preferrably those which
     * would cause a reset) can be autocorrected. Example: only Uppercase characters are allowed, so
     * the input (in which occurs as it happens a lowercase character), the verification process
     * returns a result which states that the input is invalid. However, this method returns true,
     * such, that the method #autocorrect(InputVerificationResult) will fix the input directly in
     * the textfield (in this example). <br />
     * The user input is then verified again, and in this second pass, this verification obstacle
     * will hopefully not occur again.
     * 
     * @param result the input verificationResult whichs cause has to be considered now for
     *            autocorrection
     * @return
     */
    protected boolean canAutocorrect(InputVerificationResult result) {
        return false;
    }

    /**
     * see also #canAutocorrect(InputVerificationResult) <br />
     * Tries to automatically correct minimal malformed inputs from the user side. This method
     * should restrain to correct only the "cause" of the verification result which is passed. After
     * returning, the input will be verified again, and possibly corrected again with the new
     * verificationResult, until {@link #canAutocorrect(InputVerificationResult)} does return false.
     * 
     * @param result the InputVerificationResult which caused the autocorrection.
     * @return
     */
    protected void autocorrect(InputVerificationResult result) {

    }

    public void forceNotifyObservers() {
        this.setChanged();
        this.notifyObservers();
    }

}
