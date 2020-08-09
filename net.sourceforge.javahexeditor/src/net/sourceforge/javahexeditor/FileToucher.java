package net.sourceforge.javahexeditor;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;

public interface FileToucher {
	void touchFile(File contentFile, IProgressMonitor monitor) throws IOException;
}
