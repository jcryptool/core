// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2019 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.util.images;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

/**
 * Utility class for providing a functionality to load images. In addition the Service provides
 * a set a of default images that most plug-ins use.
 * @author Thorben Groos (thorben.groos@student.uni-siegen.de)
 * @version 1.0
 *
 */
public class ImageService {
	
	public static final Image InfoIcon, WarningIcon, ErrorIcon, HelpIcon, ResetIcon, VisualizationsIcon, CheckboxIcon, SearchIcon, RunIcon, FileIcon;
	
	public static final ImageDescriptor InfoIconImageDescriptor, HelpIconImageDescriptor, ResetIconImageDescriptor;
	
	static {
		WarningIcon = ImageService.getImage("org.eclipse.ui", "icons/full/obj16/warn_tsk.png");
		ErrorIcon = ImageService.getImage("org.eclipse.ui", "icons/full/obj16/error_tsk.png");
		
		HelpIcon = ImageService.getImage("org.eclipse.ui", "icons/full/etool16/help_contents.png");
		HelpIconImageDescriptor = ImageService.getImageDescriptor("org.eclipse.ui", "icons/full/etool16/help_contents.png");
		
		ResetIcon = ImageService.getImage("org.eclipse.ui", "icons/full/etool16/new_wiz.png");
		ResetIconImageDescriptor = ImageService.getImageDescriptor("org.eclipse.ui", "icons/full/etool16/new_wiz.png");
		
		InfoIcon = ImageService.getImage("org.eclipse.jface", "icons/full/message_info.png");
		InfoIconImageDescriptor = ImageService.getImageDescriptor("org.eclipse.jface", "icons/full/message_info.png");

		VisualizationsIcon = ImageService.getImage("org.eclipse.jface", "icons/full/message_info.png");
		CheckboxIcon = ImageService.getImage("org.jcryptool.core.util", "/icons/check.png");
		SearchIcon = ImageService.getImage("org.eclipse.ui", "icons/full/etool16/search.png");
		RunIcon = ImageService.getImage("org.jcryptool.core.util", "icons/run_exc.png");
		FileIcon = ImageService.getImage("org.jcryptool.core.util", "icons/fileType_filter.png");
		
	}
	

	/**
	 * Loads an image from the plug-in and filepath you specified. </br></br>
	 * You can display that image in the GUI of your plug-in. </br></br>
	 * This is an example how you can use this method: </br></br>
	 * {@code label.setImage(ImageService.getImage("org.jcryptool.visual.secretsharing"; "images/saveIcon.png");} </br></br>+
	 * 
	 * @param PLUGIN_ID The ID of your PLugin (e.g. org.jcryptool.visual.secretsharing). You will 
	 * most likely find it in the class of your plugin that extends {@link org.eclipse.ui.plugin.AbstractUIPlugin}.
	 * @param filepath The relative path to the image (e.g. images/saveIcon.png).
	 * @return An Image that you can display in your Plugins GUI.
	 */
	public static Image getImage(String PLUGIN_ID, String filepath) {
		// Get the bundle ID of the callers plugin.
		Bundle bundle = Platform.getBundle(PLUGIN_ID);
		Path path = new Path(filepath);
		final URL fullPathString = FileLocator.find(bundle, path, null);
		ImageDescriptor imageDesc = ImageDescriptor.createFromURL(fullPathString);
		Image image = imageDesc.createImage();
		return image;
	}
	
	/**
	 * Loads the image descriptor of the image specified in FilePath.</br></br>
	 * The imageDescitpor can be transformed into an image via: {@code mage image = imageDesc.createImage();}.
	 * @param PLUGIN_ID The ID of your PLugin (e.g. org.jcryptool.visual.secretsharing). You will 
	 * most likely find it in the class of your plugin that extends {@link org.eclipse.ui.plugin.AbstractUIPlugin}.
	 * @param filepath The relative path to the image (e.g. images/saveIcon.png).
	 * @return
	 */
	public static ImageDescriptor getImageDescriptor(String PLUGIN_ID, String filepath) {
		// Get the bundle ID of the callers plugin.
		Bundle bundle = Platform.getBundle(PLUGIN_ID);
		Path path = new Path(filepath);
		final URL fullPathString = FileLocator.find(bundle, path, null);
		ImageDescriptor imageDesc = ImageDescriptor.createFromURL(fullPathString);
		return imageDesc;
	}
	
	/**
	 * Help icon</br></br>
	 * The method will return the help icon of org.eclipse.ui/icons/full/etool16/help_contents.png
	 * @return a help icon.
	 */
//	public static Image getHelpIconImage() {
//		Bundle bundle = Platform.getBundle("org.eclipse.ui");
//		Path path = new Path("icons/full/etool16/help_contents.png");
//		final URL fullPathString = FileLocator.find(bundle, path, null);
//		ImageDescriptor imageDesc = ImageDescriptor.createFromURL(fullPathString);
//		Image image = imageDesc.createImage();
//		return image;
//	}
	
	/**
	 * The method will return the ImageDescriptor of the help icon of 
	 * org.eclipse.ui/icons/full/etool16/help_contents.png
	 * @return a help icon.
	 */
//	public static ImageDescriptor getHelpIconImageDescriptor() {
//		Bundle bundle = Platform.getBundle("org.eclipse.ui");
//		Path path = new Path("icons/full/etool16/help_contents.png");
//		final URL fullPathString = FileLocator.find(bundle, path, null);
//		ImageDescriptor imageDesc = ImageDescriptor.createFromURL(fullPathString);
//		return imageDesc;
//	}
	
	/**
	 * The method will return the reset icon of 
	 * org.eclipse.ui/icons/full/etool16/new_wiz.png
	 * @return a help icon.
	 */
//	public static Image getResetIconImage() {
//		Bundle bundle = Platform.getBundle("org.eclipse.ui");
//		Path path = new Path("icons/full/etool16/new_wiz.png");
//		final URL fullPathString = FileLocator.find(bundle, path, null);
//		ImageDescriptor imageDesc = ImageDescriptor.createFromURL(fullPathString);
//		Image image = imageDesc.createImage();
//		return image;
//	}
	
	/**
	 * The method will return the ImageDescriptor of the reset icon 
	 * of org.eclipse.ui/icons/full/etool16/new_wiz.png
	 * @return a help icon.
	 */
	public static ImageDescriptor getResetIconImageDescriptor() {
		Bundle bundle = Platform.getBundle("org.eclipse.ui");
		Path path = new Path("icons/full/etool16/new_wiz.png");
		final URL fullPathString = FileLocator.find(bundle, path, null);
		ImageDescriptor imageDesc = ImageDescriptor.createFromURL(fullPathString);
		return imageDesc;
	}
	
	/**
	 * Info icon</br></br>
	 * The method will return the info icon of 
	 * org.eclipse.jface/icons/full/message_info.png
	 * @return a help icon
	 */
//	public static Image getInfoIconImage() {
//		Bundle bundle = Platform.getBundle("org.eclipse.jface");
//		Path path = new Path("icons/full/message_info.png");
//		final URL fullPathString = FileLocator.find(bundle, path, null);
//		ImageDescriptor imageDesc = ImageDescriptor.createFromURL(fullPathString);
//		Image image = imageDesc.createImage();
//		return image;
//	}
	
	/**
	 * The method will return the ImageDescriptor of the info icon 
	 * of org.eclipse.jface/icons/full/message_info.png
	 * @return The ImageDescriptor of the info icon
	 */
	public static ImageDescriptor getInfoIconImageDescriptor() {
		Bundle bundle = Platform.getBundle("org.eclipse.jface");
		Path path = new Path("icons/full/message_info.png");
		final URL fullPathString = FileLocator.find(bundle, path, null);
		ImageDescriptor imageDesc = ImageDescriptor.createFromURL(fullPathString);
		return imageDesc;
	}

	/**
	 * Visualizations icon.</br></br>
	 * The method will return the visualization icon of 
	 * org.eclipse.jface/icons/full/message_info.png
	 * @return The icon for visualizations.
	 */
//	public static Image getVisualizationsIconImage() {
//		Bundle bundle = Platform.getBundle("org.eclipse.ui");
//		Path path = new Path("icons/full/eview16/defaultview_misc.png");
//		final URL fullPathString = FileLocator.find(bundle, path, null);
//		ImageDescriptor imageDesc = ImageDescriptor.createFromURL(fullPathString);
//		Image image = imageDesc.createImage();
//		return image;
//	}
	
	/**
	 * The method will return the ImageDescriptor of the visualization icon 
	 * of org.eclipse.jface/icons/full/message_info.png
	 * @return The ImageDescriptor for the icon for visualizations.
	 */
	public static ImageDescriptor getVisualizationsIconImageDescriptor() {
		Bundle bundle = Platform.getBundle("org.eclipse.ui");
		Path path = new Path("icons/full/eview16/defaultview_misc.png");
		final URL fullPathString = FileLocator.find(bundle, path, null);
		ImageDescriptor imageDesc = ImageDescriptor.createFromURL(fullPathString);
		return imageDesc;
	}
		
	/**
	 * Error icon.</br></br>
	 * The method will return the error icon of 
	 * org.eclipse.ui/icons/full/obj16/error_tsk.png
	 * @return The icon for errors.
	 */
//	public static Image getErrorIconImage() {
//		Bundle bundle = Platform.getBundle("org.eclipse.ui");
//		Path path = new Path("icons/full/obj16/error_tsk.png");
//		final URL fullPathString = FileLocator.find(bundle, path, null);
//		ImageDescriptor imageDesc = ImageDescriptor.createFromURL(fullPathString);
//		Image image = imageDesc.createImage();
//		return image;
//	}
	
	/**
	 * The method will return the ImageDescriptor of the error icon 
	 * of org.eclipse.ui/icons/full/obj16/error_tsk.png
	 * @return The ImageDescriptor for the icon for errors.
	 */
	public static ImageDescriptor getErrorIconImageDescriptor() {
		Bundle bundle = Platform.getBundle("org.eclipse.ui");
		Path path = new Path("icons/full/obj16/error_tsk.png");
		final URL fullPathString = FileLocator.find(bundle, path, null);
		ImageDescriptor imageDesc = ImageDescriptor.createFromURL(fullPathString);
		return imageDesc;
	}
	
	/**
	 * Warning icon.</br></br>
	 * The method will return the warning icon of 
	 * org.eclipse.ui/icons/full/obj16/warn_tsk.png
	 * @return The icon for errors.
	 */
//	public static Image getWarningIconImage() {
//		Bundle bundle = Platform.getBundle("org.eclipse.ui");
//		Path path = new Path("icons/full/obj16/warn_tsk.png");
//		final URL fullPathString = FileLocator.find(bundle, path, null);
//		ImageDescriptor imageDesc = ImageDescriptor.createFromURL(fullPathString);
//		Image image = imageDesc.createImage();
//		return image;
//	}
	
	/**
	 * The method will return the ImageDescriptor of the warning icon 
	 * of org.eclipse.ui/icons/full/obj16/warn_tsk.png
	 * @return The ImageDescriptor for the icon for warnings.
	 */
	public static ImageDescriptor getWarningIconImageDescriptor() {
		Bundle bundle = Platform.getBundle("org.eclipse.ui");
		Path path = new Path("icons/full/obj16/warn_tsk.png");
		final URL fullPathString = FileLocator.find(bundle, path, null);
		ImageDescriptor imageDesc = ImageDescriptor.createFromURL(fullPathString);
		return imageDesc;
	}
	
	/**
	 * Checkbox icon.</br></br>
	 * The method will return the checkbox icon of 
	 * org.jcryptool.core.util/icons/check.png
	 * @return An checkbox icon.
	 */
//	public static Image getCheckIconImage() {
//		return getImage("org.jcryptool.core.util", "/icons/check.png");
//	}
	
	/**
	 * The method will return the ImageDescriptor of the checkbox icon 
	 * of org.jcryptool.core.util/icons/check.png
	 * @return ImageDescriptor for the checkbox icon.
	 */
	public static ImageDescriptor getCheckIconImageDescriptor() {
		return getImageDescriptor("org.jcryptool.core.util", "/icons/check.png");
	}
	

}
