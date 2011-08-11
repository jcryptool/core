// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge;

/**
 * Interface für alle Protokolle, die in das Plugin eingebunden werden sollen. Ein Protokoll muss
 * zwei unterschiedliche Fälle haben (Alice und Carol), alles bis auf das Geheimnis und allem
 * dazugehörigen muss zurücksetzbar sein und das ganze Protokoll muss zurückgesetzt werden können.
 * Nur wenn dieses Interface implementiert ist, kann das Protokoll die gegebene Einleitung
 * (Introduction) und die gegebene Buttons-Klasse verwenden. Ansonsten müssen diese Teile selbst
 * implementiert werden.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public interface Protocol {

    /**
     * setzt das gesamte Protokoll zurück. Wird aufgerufen, wenn der Button "Zurücksetzen" betätigt
     * wird.
     */
    public void reset();

    /**
     * setzt alles außer dem Geheimnis und allem, was dazugehört zurück. Wird verwendet, wenn ein
     * neuer Durchlauf durch das Protokoll gewünscht wird.
     */
    public void resetNotSecret();

    /**
     * setzt den Fall "Alice"
     *
     * @param firstCase true, wenn Alice antworten soll, sonst false.
     */
    public void setFirstCase(boolean firstCase);
}
