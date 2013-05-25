// -----BEGIN DISCLAIMER-----
package org.jcryptool.analysis.vigenere.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.analysis.vigenere.VigenereBreakerPlugin;

/**
 * This handler displays the dynamic help identified by the given context help id.
 *
 * @author Dominik Schadow
 * @version 0.9.2
 */
public class HelpHandler extends AbstractHandler {
    @Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
        PlatformUI.getWorkbench().getHelpSystem().displayHelp(VigenereBreakerPlugin.PLUGIN_ID + ".vigenerebreaker");
        return null;
    }
}
