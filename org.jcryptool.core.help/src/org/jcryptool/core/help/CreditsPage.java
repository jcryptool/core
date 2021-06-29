package org.jcryptool.core.help;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Scanner;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.about.InstallationPage;
import org.jcryptool.core.util.fonts.FontService;
import org.osgi.framework.Bundle;

public class CreditsPage extends InstallationPage {

	private Composite comp;

	public CreditsPage() {
		// TODO Auto-generated constructor stub
	}

	
    public static final String PLUGIN_ID = "org.jcryptool.core.help"; //$NON-NLS-1$
    
    public static File getBundleFile(String path) {
    	Bundle bundle = Platform.getBundle(PLUGIN_ID);
    	URL fileURL = bundle.getEntry(path);
    	File file = null;
    	try {
    	   URL resolvedFileURL = FileLocator.toFileURL(fileURL);
    	   URI resolvedURI = new URI(resolvedFileURL.getProtocol(), resolvedFileURL.getPath(), null);
    	   file = new File(resolvedURI);
    	} catch (URISyntaxException e1) {
    	    e1.printStackTrace();
    	} catch (IOException e1) {
    	    e1.printStackTrace();
    	}	
    	return file;
    }
	private static String slurpFile(String relPath) throws FileNotFoundException {
		File bundleFile = getBundleFile(relPath);
		FileInputStream fis = null;
		var content = new byte[0];
		try {
			fis = new FileInputStream(bundleFile);
			content = fis.readAllBytes();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return new String(content);
	}
	
	@Override
	public void createControl(Composite parent) {
		comp = new Composite(parent, SWT.NONE);
		comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		comp.setLayout(new GridLayout());

		Text text = new Text(comp, SWT.MULTI | SWT.V_SCROLL);
		String abouttext = "no about text could be loaded here";
		try {
			abouttext = slurpFile(Messages.CreditsPage_filename);
		} catch (FileNotFoundException e) { }

		text.setFont(FontService.getNormalMonospacedFont());
		text.setText(abouttext); //$NON-NLS-1$
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		text.setEditable(false);
	}
	
	@Override
	public Control getControl() {
		return comp;
	}

}
