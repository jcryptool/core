// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
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
	
	/**
	 * Icon showing a blue "i" in a circle on a white background.</br>
	 * The icon can be found in: platform:/plugin/org.eclipse.jface/icons/full/message_info.png
	 */
	public static final Image ICON_INFO;
	/**
	 * ImageDescriptor of {@link org.jcryptool.core.util.images.ImageService#ICON_INFO ICON_INFO}.
	 */
	public static final ImageDescriptor IMAGEDESCRIPTOR_INFO;
	
	/**
	 * Icon showing a black "!" in triangular on yellow background.</br>
	 * The icon can be found in: platform:/plugin/org.eclipse.ui/icons/full/obj16/warn_tsk.png
	 */
	public static final Image ICON_WARNING;
	/**
	 * ImageDescriptor of {@link org.jcryptool.core.util.images.ImageService#ICON_WARNING ICON_WARNING}.
	 */
	public static final ImageDescriptor IMAGEDESCRIPTOR_WARNING;
		
	/**
	 * Icon showing a white cross on a red circle.</br>
	 * The icon can be found in: platform:/plugin/org.eclipse.ui/icons/full/obj16/error_tsk.png
	 */
	public static final Image ICON_ERROR;
	/**
	 * ImageDescriptor of {@link org.jcryptool.core.util.images.ImageService#ICON_ERROR ICON_ERROR}.
	 */
	public static final ImageDescriptor IMAGEDESCRIPTOR_ERROR;
	
	/**
	 * Icon showing a blue question mark on a white circle with a blue border.</br>
	 * The icon can be found in: platform:/plugin/org.eclipse.ui/icons/full/etool16/help_contents.png
	 */
	public static final Image ICON_HELP;
	/**
	 * ImageDescriptor of {@link org.jcryptool.core.util.images.ImageService#ICON_HELP ICON_HELP}.
	 */
	public static final ImageDescriptor IMAGEDESCRIPTOR_HELP;
	
	/**
	 * This is the default icon for the button that resets a plugin.</br>
	 * The icon can be found in: platform:/plugin/org.jcryptool.core.util/icons/icon_reset.png
	 */
	public static final Image ICON_RESET;
	/**
	 * ImageDescriptor of {@link org.jcryptool.core.util.images.ImageService#ICON_RESET ICON_RESET}.
	 */
	public static final ImageDescriptor IMAGEDESCRIPTOR_RESET;
	
	/**
	 * The standard icon symbolizing a visualization plugin</br>
	 * The icon can be found in: platform:/plugin/org.eclipse.ui/icons/full/eview16/defaultview_misc.png
	 */
	public static final Image ICON_VISUALIZATIONS;
	/**
	 * ImageDescriptor of {@link org.jcryptool.core.util.images.ImageService#ICON_VISUALIZATIONS ICON_VISUALIZATIONS}.
	 */
	public static final ImageDescriptor IMAGEDESCRIPTOR_VISUALIZATIONS;
	
	/**
	 * A checked Checkbox</br>
	 * The icon can be found in: platform:/plugin/org.jcryptool.core.util/icons/check.png
	 */
	public static final Image ICON_CHECKBOX;
	/**
	 * ImageDescriptor of {@link org.jcryptool.core.util.images.ImageService#ICON_CHECKBOX ICON_CHECKBOX}.
	 */
	public static final ImageDescriptor IMAGEDESCRIPTOR_CHECKBOX;
	
	/**
	 * A small lens symboling a search possibility.</br>
	 * The icon can be found in: platform:/plugin/org.eclipse.ui/icons/full/etool16/search.png
	 */
	public static final Image ICON_SEARCH;
	/**
	 * ImageDescriptor of {@link org.jcryptool.core.util.images.ImageService#ICON_SEARCH ICON_SEARCH}.
	 */
	public static final ImageDescriptor IMAGEDESCRIPTOR_SEARCH;
	
	/**
	 * The run icon you may know from the eclipse IDE. A white triangular on a green circle.</br>
	 * The icon can be found in: platform:/plugin/org.jcryptool.core.util/icons/run_exc.png
	 */
	public static final Image ICON_RUN;
	/**
	 * ImageDescriptor of {@link org.jcryptool.core.util.images.ImageService#ICON_RUN ICON_RUN}.
	 */
	public static final ImageDescriptor IMAGEDESCRIPTOR_RUN;
	
	/**
	 * An icon of a white sheet.</br>
	 * The icon can be found in: platform:/plugin/org.jcryptool.core.util/icons/fileType_filter.png
	 */
	public static final Image ICON_FILE;
	/**
	 * ImageDescriptor of {@link org.jcryptool.core.util.images.ImageService#ICON_FILE ICON_FILE}.
	 */
	public static final ImageDescriptor IMAGEDESCRIPTOR_FILE;
	
	/**
	 * An icon of a white sheet.</br>
	 * The icon can be found in: platform:/plugin/org.jcryptool.core.util/icons/analysis_icon.png
	 */
	public static final Image ICON_ANALYSIS;
	/**
	 * ImageDescriptor of {@link org.jcryptool.core.util.images.ImageService#ICON_ANALYSIS ICON_ANALYSIS}.
	 */
	public static final ImageDescriptor IMAGEDESCRIPTOR_ANALYSIS;
	
	/**
	 * An icon of a red square. Indicates a missing item.</br>
	 * The icon can be found in: platform:/plugin/org.jcryptool.core.util/icons/analysis_icon.png
	 */
	public static final Image ICON_NOTFOUND;
	
	public static final ImageDescriptor IMAGEDESCRIPTOR_NOTFOUND;
	
		
	static {
		ICON_INFO = ImageService.getImage("org.eclipse.jface", "icons/full/message_info.png");
		IMAGEDESCRIPTOR_INFO = ImageService.getImageDescriptor("org.eclipse.jface", "icons/full/message_info.png");
		
		ICON_WARNING = ImageService.getImage("org.eclipse.ui", "icons/full/obj16/warn_tsk.png");
		IMAGEDESCRIPTOR_WARNING = ImageService.getImageDescriptor("org.eclipse.ui", "icons/full/obj16/warn_tsk.png");
		
		ICON_ERROR = ImageService.getImage("org.eclipse.ui", "icons/full/obj16/error_tsk.png");
		IMAGEDESCRIPTOR_ERROR = ImageService.getImageDescriptor("org.eclipse.ui", "icons/full/obj16/error_tsk.png");
		
		ICON_HELP = ImageService.getImage("org.eclipse.ui", "icons/full/etool16/help_contents.png");
		IMAGEDESCRIPTOR_HELP = ImageService.getImageDescriptor("org.eclipse.ui", "icons/full/etool16/help_contents.png");
		
		ICON_RESET = ImageService.getImage("org.jcryptool.core.util", "icons/icon_reset.png");
		IMAGEDESCRIPTOR_RESET = ImageService.getImageDescriptor("org.jcryptool.core.util", "icons/icon_reset.png");

		ICON_VISUALIZATIONS = ImageService.getImage("org.eclipse.ui", "icons/full/eview16/defaultview_misc.png");
		IMAGEDESCRIPTOR_VISUALIZATIONS = ImageService.getImageDescriptor("org.eclipse.ui", "icons/full/eview16/defaultview_misc.png");
		
		ICON_ANALYSIS = ImageService.getImage("org.jcryptool.core.util", "icons/analysis_icon.gif");
		IMAGEDESCRIPTOR_ANALYSIS = ImageService.getImageDescriptor("org.jcryptool.core.util", "icons/analysis_icon.gif");
		
		ICON_CHECKBOX = ImageService.getImage("org.jcryptool.core.util", "icons/check.png");
		IMAGEDESCRIPTOR_CHECKBOX = ImageService.getImageDescriptor("org.jcryptool.core.util", "icons/check.png");
		
		ICON_SEARCH = ImageService.getImage("org.eclipse.ui", "icons/full/etool16/search.png");
		IMAGEDESCRIPTOR_SEARCH = ImageService.getImageDescriptor("org.eclipse.ui", "icons/full/etool16/search.png");
		
		ICON_RUN = ImageService.getImage("org.jcryptool.core.util", "icons/run_exc.png");
		IMAGEDESCRIPTOR_RUN = ImageService.getImageDescriptor("org.jcryptool.core.util", "icons/run_exc.png");
		
		ICON_FILE = ImageService.getImage("org.jcryptool.core.util", "icons/fileType_filter.png");	
		IMAGEDESCRIPTOR_FILE = ImageService.getImageDescriptor("org.jcryptool.core.util", "icons/fileType_filter.png");
	
		ICON_NOTFOUND = ImageService.getImage("org.jcryptool.core.util", "icons/red_square.png");
		IMAGEDESCRIPTOR_NOTFOUND = ImageService.getImageDescriptor("org.jcryptool.core.util", "icons/red_square.png");
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

}
