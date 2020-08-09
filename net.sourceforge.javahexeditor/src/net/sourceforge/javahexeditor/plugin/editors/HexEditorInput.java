/*
 * javahexeditor, a java hex editor
 * Copyright (C) 2006, 2009 Jordi Bergenthal, pestatije(-at_)users.sourceforge.net
 * The official javahexeditor site is sourceforge.net/projects/javahexeditor
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sourceforge.javahexeditor.plugin.editors;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.editors.text.ILocationProvider;
import org.eclipse.ui.part.FileEditorInput;

import net.sourceforge.javahexeditor.Texts;
import net.sourceforge.javahexeditor.common.TextUtility;
import net.sourceforge.javahexeditor.plugin.HexEditorPlugin;

/**
 * Container for file backed input.
 * 
 * @author Peter Dell
 *
 */
public final class HexEditorInput {
	private boolean valid;

	private String inputName;

	private File contentFile;
	private boolean temporary;
	private String charset;

	public HexEditorInput() {
		inputName = Texts.EMPTY;
	}

	public boolean isValid() {
		return valid;
	}

	public String getInputName() {
		return inputName;
	}

	public File getContentFile() {
		return contentFile;
	}

	public String getCharset() {
		return charset;
	}

	public void dispose() {
		if (temporary) {
			contentFile.delete();
		}
		contentFile = null;
		charset = null;
		inputName = Texts.EMPTY;
		valid = false;
	}

	public void open(IEditorInput editorInput) throws CoreException {
		IFile localFile;
		localFile = null;
		boolean fromHistory = false;
		if (editorInput instanceof FileEditorInput) {
			localFile = ((FileEditorInput) editorInput).getFile();
		} else if (editorInput instanceof IPathEditorInput) { // eg.
			// FileInPlaceEditorInput
			IPathEditorInput file = (IPathEditorInput) editorInput;
			contentFile = file.getPath().toFile();
		} else if (editorInput instanceof ILocationProvider) {
			ILocationProvider location = (ILocationProvider) editorInput;
			IWorkspaceRoot rootWorkspace = ResourcesPlugin.getWorkspace().getRoot();
			localFile = rootWorkspace.getFile(location.getPath(location));
		} else if (editorInput instanceof IURIEditorInput) {
			URI uri = ((IURIEditorInput) editorInput).getURI();
			if (uri != null) {
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				IFile[] files = root.findFilesForLocationURI(uri);

				if (files.length != 0) {
					localFile = files[0];
				} else {
					contentFile = new File(uri);
				}
			}
		} else if (editorInput instanceof IStorageEditorInput) {
			// Entries opened from other editor inputs or the history are copied into a
			// temporary file in a temporary folder which is deleted when copying
			// fails, another file is opened or when then program is terminated.
			IStorageEditorInput input = (IStorageEditorInput) editorInput;
			try {
				Path tmpDir = Files.createTempDirectory(null);
				Path tmpFile = tmpDir.resolve(input.getStorage().getName());
				contentFile = tmpFile.toFile();
				contentFile.createNewFile();
				contentFile.deleteOnExit(); // to be on the safe side
				Files.copy(input.getStorage().getContents(), contentFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException ex) {
				contentFile.delete();
				throw new CoreException(new Status(IStatus.ERROR, HexEditorPlugin.ID,
						TextUtility.format(Texts.MANAGER_OPEN_MESSAGE_CANNOT_OPEN_FILE, contentFile.getAbsolutePath()),
						ex));

			} catch (CoreException ex) {
				contentFile.delete();
				throw ex;
			}
			if (input.getClass().getName().contains("FileRevisionEditorInput")) {
				fromHistory = true;
			}
		}
		// Determine charset from local file
		if (localFile != null) {
			contentFile = localFile.getLocation().toFile();
			try {
				charset = localFile.getCharset(true);
			} catch (CoreException ex) {
				throw new CoreException(new Status(IStatus.ERROR, HexEditorPlugin.ID,
						TextUtility.format(Texts.MANAGER_OPEN_MESSAGE_CANNOT_DETERMINE_CHARSET_OF_FILE,
								contentFile.getAbsolutePath()),
						ex));
			}
		}

		valid = true;
		inputName = contentFile.getName();
		if (fromHistory) {
			inputName = TextUtility.format(Texts.MANAGER_OPEN_TITLE_FILE_FROM_HISTORY, inputName);
		}

	}
}