/* *****************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * ****************************************************************************/
package org.jcryptool.analysis.vigenere.ui;

import java.util.Vector;

import org.eclipse.swt.widgets.Composite;
import org.jcryptool.analysis.vigenere.exceptions.IllegalInputException;
import org.jcryptool.analysis.vigenere.exceptions.NoContentException;

public class PasswordContainer {
    /**
     * Contains the number of input field for character the implemention is able
     * to show.
     */
    protected static final int TALLY = 30;
    private final Vector<PasswordElementGui> list;
    private final FrequencyContainer container;
    private final int passlenght;

    public PasswordContainer(final Composite parent, final int length,
            final FrequencyContainer fq) {
        this.list = new Vector<PasswordElementGui>();
        this.container = fq;
        this.passlenght = length;
        init(parent, passlenght);
    }

    private void init(final Composite parent, final int length) {
        int max = TALLY < length ? TALLY : length;

        for (int i = 1; i <= max; i++) {
            list.add(new PasswordElementGui(parent, i, this));
        }
    }

    protected void showAnalysis(final int number) {
        if (container.getActiveNo() == number) {
            list.get(number - 1).select();
            return;
        }

        container.showGraph(number, container.getAlphabet());
    }

    /**
     * @return
     * @throws NoContentException
     * @deprecated use {@link PasswordContainer#findNextOpen(int)} with
     *             <code>1</code> for the index instead.
     * 
     */
    protected int findLowestOpen() throws NoContentException {
        for (PasswordElementGui elem : list) {
            if (!elem.isFound()) {
                return elem.getNumber();
            }
        }

        throw new NoContentException("No more open elements found!"); //$NON-NLS-1$
    }

    /**
     * Searches for the next open (not decrypted) character of the pass phrase.
     * Throws exception if all elements of pass phrase are known.
     * 
     * @param index
     *            the number of the preceding character to decrypt.
     * @return the number of the next open characters to decrypt.
     * @throws NoContentException
     *             if no more open elements are found.
     */
    protected int findNextOpen(final int index) throws NoContentException {
        // start from index until reaching end of list.
        for (int i = index - 1; i < passlenght; i++) {
            PasswordElementGui elem = list.get(i);

            if (!elem.isFound()) {
                return elem.getNumber();
            }
        }

        // if element is not found from index to end;
        // search beginning of list until reaching index.
        for (int i = 0; i < index - 1; i++) {
            PasswordElementGui elem = list.get(i);

            if (!elem.isFound()) {
                return elem.getNumber();
            }
        }

        // no open element left.
        throw new NoContentException("No more open elements found!"); //$NON-NLS-1$
    }

    protected boolean isFound(final int number) {
        return list.get(number - 1).isFound();
    }

    protected void signalFound(final String character, final int number) {
        list.get(number - 1).hightlight(character);
    }

    protected void highlight(final int number) {
        // highlight current text field.
        list.get(number - 1).select();

        // reset background of last highlighted text field.
        // if -1: no last highlighted text field, therefore
        // no text field should have focus.
        if (-1 == container.getActiveNo()) {
            return;
        }

        int unmark = container.getActiveNo() - 1;

        // if negative returns and tries not to get a container element
        // of pass phrase. otherwise triggers exceptions. special case
        // occurs when changing options while highlighting first element
        // of pass phrase.
        if (0 > unmark) {
            return;
        }

        PasswordElementGui elem = list.get(unmark);

        elem.unselect();
    }

    protected String getCharacter(final int index) {
        return list.get(index - 1).getCharacter();
    }

    protected String getCharacters() throws IllegalInputException {
        StringBuffer buffer = new StringBuffer();

        for (PasswordElementGui elem : list) {
            if (!elem.isFound()) {
                String message = "Pass phrase not entirely found!"; //$NON-NLS-1$
                throw new IllegalInputException(message);
            }

            buffer.append(elem.getCharacter());
        }

        return buffer.toString();
    }

    protected void showCompletePass(String in) {
        // just to be sure.
        if (passlenght < in.length()) {
            in = in.substring(0, passlenght);
        }

        for (int i = 0; i < in.length(); i++) {
            PasswordElementGui elem = list.get(i);
            elem.hightlight(in.substring(i, i + 1));
        }
    }
    
    public int getPasswordLength() {
    	return passlenght;
    }
    
    public int getFoundCharCount() {
    	int count = getPasswordLength();
    	for (PasswordElementGui elem : list) {
            if (!elem.isFound()) {
            	count--;
            }
    	}
    	return count;
    }
}
