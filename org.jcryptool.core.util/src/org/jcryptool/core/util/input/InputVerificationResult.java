// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.util.input;

/**
 * Contains the results of an input verification. the main indicator is {@link #isValid()}, because this is the most
 * important thing to know about a verification. Furthermore, there can be error messages or warnings, or no comments at
 * all (see {@link #getMessage()} and {@link #getMessageType()}).
 * 
 * @author Simon L
 */
public abstract class InputVerificationResult {

    public static final String RESULT_TYPE_DEFAULT = "DEFAULT"; //$NON-NLS-1$

    public static final InputVerificationResult DEFAULT_RESULT_EVERYTHING_OK = new InputVerificationResult() {
        public MessageType getMessageType() {
            return MessageType.NONE;
        }

        public String getMessage() {
            return "";} //$NON-NLS-1$

        public boolean isValid() {
            return true;
        }

        public boolean isStandaloneMessage() {
            return false;
        }

        public String toString() {
            return "OK";} //$NON-NLS-1$
    };

    public static final int PERSISTENCE_DEFAULT = 0;
    public static final int PERSISTENCE_FOREVER = 1000 * 60 * 60 * 24; // 1 day in eternity ;)

    public static enum MessageType {
        ERROR(4), WARNING(3), INFORMATION(2), NONE(1);

        private int level;

        MessageType(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }

        public int compareWith(MessageType o2) {
            if (!this.equals(o2)) {
                if (this.getLevel() == o2.getLevel()) {
                    return Integer.valueOf(this.hashCode()).compareTo(o2.getLevel());
                } else {
                    return Integer.valueOf(this.getLevel()).compareTo(o2.getLevel());
                }
            } else {
                return 0;
            }
        }

    }

    /**
     * The message displayed for the verification (mostly only if it runs through with some error, warning or
     * information)
     */
    public abstract String getMessage();

    /**
     * The message type
     */
    public abstract MessageType getMessageType();

    public Object getResultType() {
        return RESULT_TYPE_DEFAULT;
    }

    public int getMessagePersistenceCategory() {
        return InputVerificationResult.PERSISTENCE_DEFAULT;
    }

    /**
     * @return whether the message is a piece of text that has to be inserted into a pattern of text in a 'usual way' or
     *         whether it has to be displayed single and unchanged.
     */
    public abstract boolean isStandaloneMessage();

    /**
     * @return whether the validation was successful in general or not. In Context of AbstractUIInput, changed made by
     *         user which create a InputVerificationResult which returns false here, are rejected.
     */
    public abstract boolean isValid();

    public static InputVerificationResult generateIVR(final boolean isValid, final String message,
            final MessageType messageType, final boolean isStandaloneMessage) {
        return generateIVR(isValid, message, messageType, isStandaloneMessage, RESULT_TYPE_DEFAULT, PERSISTENCE_DEFAULT);
    }

    public static InputVerificationResult generateIVR(final boolean isValid, final String message,
            final MessageType messageType, final boolean isStandaloneMessage, final Object resultType) {
        return generateIVR(isValid, message, messageType, isStandaloneMessage, resultType, PERSISTENCE_DEFAULT);
    }

    public static InputVerificationResult generateIVR(final boolean isValid, final String message,
            final MessageType messageType, final boolean isStandaloneMessage, final Object resultType,
            final int messagePersistence) {
        return new InputVerificationResult() {
            @Override
            public String getMessage() {
                return message;
            }

            @Override
            public MessageType getMessageType() {
                return messageType;
            }

            @Override
            public Object getResultType() {
                return resultType;
            }

            @Override
            public int getMessagePersistenceCategory() {
                return messagePersistence;
            }

            @Override
            public boolean isStandaloneMessage() {
                return isStandaloneMessage;
            }

            @Override
            public boolean isValid() {
                return isValid;
            }
        };
    }

}
