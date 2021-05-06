// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.logging.utils;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.jcryptool.core.logging.LoggingPlugin;
import org.jcryptool.core.logging.dialogs.JCTMessageDialog;

/**
 * <p>
 * JCrypTool logging plug-in. This class offers several convenience methods to log messages. The
 * workbench log file is used to store all messages. All methods are static for easy access.
 * </p>
 *
 * <p>
 * Only the <code>logError</code> method with bundle ID, error message and an exception may be
 * configured to show an error dialog to the user. All other methods will only log a message into
 * the JCrypTool log in case the configured error message level is lower or equal the current the
 * current message.
 * </p>
 *
 * @author Dominik Schadow
 * @version 0.9.5
 */
public class LogUtil {
    public static final String LOGGER_LOG_LEVEL = "org.jcryptool.core.logging.logLevel"; //$NON-NLS-1$
    private static int loglevel = IStatus.ERROR;
    private static boolean jctVersionLogged = false;
    
    private static Map<String, Boolean> autoMessageBoxOnError = new HashMap<>();

    public static boolean isAutoMessageboxOnError(String bundleId) {
    	return autoMessageBoxOnError.getOrDefault(bundleId, false);
    }
    
    public static void setAutoMessageboxOnError(String bundleId, boolean autoErrorDialog) {
    	autoMessageBoxOnError.put(bundleId, autoErrorDialog);
    }
    
    
    /**
     * Sets a new logging level.
     *
     * @param level New log level. Only messages are logged if the log level is lower than the
     *        message severity.
     */
    public static void setLogLevel(int level) {
        logLogLevelChange(getLogLevel(), level);
        loglevel = level;
    }

    /**
     * Returns the current log level
     *
     * @return The current log level
     */
    public static int getLogLevel() {
        return loglevel;
    }

    /**
     * Logs the message with status <b>info</b>. Using this method will never automatically show an
     * error dialog for the user.
     *
     * @param message The message to log
     */
    public static void logInfo(String message) {
        log(null, message, null, IStatus.INFO, false);
    }

    /**
     * Logs the message with status <b>info</b>. Using this method will never automatically show an
     * error dialog for the user.
     *
     * @param bundleId The bundle that caused the error
     * @param message The message to log
     */
    public static void logInfo(String bundleId, String message) {
        log(bundleId, message, null, IStatus.INFO, false);
    }

    /**
     * Logs the message with status <b>warning</b>. Using this method will never automatically show
     * an error dialog for the user.
     *
     * @param message The message to log
     */
    public static void logWarning(String message) {
        log(null, message, null, IStatus.WARNING, false);
    }

    /**
     * Logs the message with status <b>warning</b>. Using this method will never automatically show
     * an error dialog for the user.
     *
     * @param bundleId The bundle that caused the error
     * @param message The message to log
     */
    public static void logWarning(String bundleId, String message) {
        log(bundleId, message, null, IStatus.WARNING, false);
    }

    /**
     * Logs the message with status <b>error</b>. Using this method will never automatically show an
     * error dialog for the user.
     *
     * @param message An additional message to log
     */
    public static void logError(String message) {
        log(null, message, null, IStatus.ERROR, false);
    }

    /**
     * Logs the message with status <b>error</b>. Using this method will never automatically show an
     * error dialog for the user.
     *
     * @param bundleId The bundle that caused the error
     * @param message An additional message to log
     */
    public static void logError(String bundleId, String message) {
        log(bundleId, message, null, IStatus.ERROR, false);
    }

    /**
     * Logs the exception with status <b>error</b>. Using this method will never automatically show
     * an error dialog for the user.
     *
     * @param ex The exception to log
     */
    public static void logError(Exception ex) {
        logError(ex, false);
    }

    /**
     * Logs the exception with status <b>error</b>. This shows an error dialog     *
     * @param ex The exception to log
     */
    public static void logError(Exception ex, boolean showAsMessagebox) {
        log(null, ex.getMessage(), ex, IStatus.ERROR, showAsMessagebox);
    }

    /**
     * Logs the exception with status <b>error</b>. Using this method will never automatically show
     * an error dialog for the user.
     *
     * @param bundleId The bundle that caused the error
     * @param ex The exception to log
     */
    public static void logError(String bundleId, Exception ex) {
        log(bundleId, ex.getMessage(), ex, IStatus.ERROR, false);
    }

    /**
     * Logs the exception and the message with status <b>error</b>. This method may optionally show
     * an error dialog for the user in case the <code>showErrorDialog</code> variable is set to
     * true. In case of true the given message must be externalized and it must be understandable by
     * a <i>normal</i> user.
     *
     * @param bundleId The bundle that caused the error
     * @param message An additional message to log
     * @param ex The exception to log
     * @param showErrorDialog Show an error dialog for the user
     */
    public static void logError(String bundleId, String message, Exception ex,
            boolean showErrorDialog) {
        log(bundleId, message, ex, IStatus.ERROR, showErrorDialog);
    }

    /**
     * Logs the exception and the message with status defined by severity. If the given bundle id is
     * null this plug-ins bundle id is used instead. The message is only logged if the severity is
     * higher than the current log level.
     *
     * @param bundleId The bundle that caused the error
     * @param message An additional message to log
     * @param ex The exception to log
     * @param severity Error severity
     * @param showErrorDialog Show an error dialog for the user, may only set with a completely
     *        described error message
     */
    private static void log(String bundleId, String message, Exception ex, int severity,
            boolean showErrorDialog) {
    	boolean showAsDialog = showErrorDialog || ( isAutoMessageboxOnError(bundleId) && severity >= IStatus.ERROR );
        if (bundleId == null) {
            bundleId = LoggingPlugin.PLUGIN_ID;
        }
        if (getLogLevel() <= severity) {
            String logMessage = message;

            if (ex != null) {
                logMessage = ex.getMessage();
            }
            
            if (!jctVersionLogged) {
                // Logging the currently used JCT version.
                Status versionInfoStatus = new Status(0, "JCT Version Information", 
                		"Currently used JCT Version: " + 
                		Platform.getProduct().getDefiningBundle().getVersion() + 
                		" " + 
                		Platform.getProduct().getProperty("mavenBuildTimestamp"));
                Platform.getLog(Platform.getBundle(bundleId)).log(versionInfoStatus);
                jctVersionLogged = true;
            }

            
            //Logging of the error.
            Status status = new Status(severity, bundleId, logMessage, ex);
            Platform.getLog(Platform.getBundle(bundleId)).log(status);


            if (showAsDialog) {
                JCTMessageDialog.showErrorDialog(status, message);
            }
        }
    }

    /**
     * Logs a message with status <b>info</b> indicating that the log level has been changed.
     *
     * @param oldLogLevel Origin log level
     * @param newLogLevel New log level
     */
    private static void logLogLevelChange(int oldLogLevel, int newLogLevel) {
        if (oldLogLevel != newLogLevel) {
            logInfo(LoggingPlugin.PLUGIN_ID,
                    "Changed log level from " + oldLogLevel + " to " + newLogLevel); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }
}
