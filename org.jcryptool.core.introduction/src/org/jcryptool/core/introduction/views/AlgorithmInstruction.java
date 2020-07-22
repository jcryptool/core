// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2020, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.introduction.views;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.jcryptool.core.CorePlugin;
import org.jcryptool.core.commands.HelpHrefRegistry;
import org.jcryptool.core.introduction.utils.DebounceExecutor;
import org.jcryptool.core.introduction.utils.ImageScaler;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.core.util.ui.auto.SmoothScroller;

//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.nebula.effects.stw.ImageTransitionable;
import org.eclipse.nebula.effects.stw.Transition;
import org.eclipse.nebula.effects.stw.TransitionManager;
import org.eclipse.nebula.effects.stw.transitions.SlideTransition;
import org.eclipse.nebula.effects.stw.utils.Utilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

/**
 * This class contains the GUI and logic for the introduction plugin.
 * 
 * @author Thorben Groos
 *
 */
public class AlgorithmInstruction extends ViewPart {

	/**
	 * Indicates if the slideshow should automatically slide images. The default is
	 * true.
	 */
	private boolean autoSlide = true;
	
	/**
	 * This thing debounces the resize operations.
	 */
	private DebounceExecutor debouncer = new DebounceExecutor();

	/**
	 * GridData object used for centering the slideshow.
	 */
	private GridData gridData_cnvs;

	/**
	 * The canvas the slideshow ist printed on.
	 */
	private Canvas cnvs;
	
	/**
	 * Composite for the lower area of the Plugin containing
	 * the "do not show again" chekcbox
	 */
	private Composite lowerArea;
	
	/**
	 * A composite in which the slideshow is contained.
	 */
	private Composite cnvsComposite;
	
	/**
	 * Images in the slideshow.
	 */
	private Image[] original_imgs = new Image[] {
			ImageService.getImage(IntroductionPlugin.PLUGIN_ID, Messages.AlgorithmInstruction_image_1_1),
			ImageService.getImage(IntroductionPlugin.PLUGIN_ID, Messages.AlgorithmInstruction_image_1_2),
			ImageService.getImage(IntroductionPlugin.PLUGIN_ID, Messages.AlgorithmInstruction_image_2),
			ImageService.getImage(IntroductionPlugin.PLUGIN_ID, Messages.AlgorithmInstruction_image_3_1),
			ImageService.getImage(IntroductionPlugin.PLUGIN_ID, Messages.AlgorithmInstruction_image_3_2),
			ImageService.getImage(IntroductionPlugin.PLUGIN_ID, Messages.AlgorithmInstruction_image_3_3),
			ImageService.getImage(IntroductionPlugin.PLUGIN_ID, Messages.AlgorithmInstruction_image_3_4),
			ImageService.getImage(IntroductionPlugin.PLUGIN_ID, Messages.AlgorithmInstruction_image_4),
			ImageService.getImage(IntroductionPlugin.PLUGIN_ID, Messages.AlgorithmInstruction_image_5),
			ImageService.getImage(IntroductionPlugin.PLUGIN_ID, Messages.AlgorithmInstruction_image_6),
			ImageService.getImage(IntroductionPlugin.PLUGIN_ID, Messages.AlgorithmInstruction_image_7) };

	/**
	 * Images for the slideshow scaled to the canvas size.
	 */
	private Image[] scaled_imgs;

	/**
	 * The number of the current image in the slideshow.</br>
	 * Minimum: 0; Maximum: original_imgs.length(),
	 */
	private int curImage = 0;

	/**
	 * This is part of the SWT Transition Widget (STW)
	 */
	private SlideTransition slideTransition;

	private TransitionManager transitionManager;

	private ImageTransitionable transitionable = new ImageTransitionable() {

		@Override
		public void setSelection(int index) {
			curImage = index;
		}

		@Override
		public int getSelection() {
			return curImage;
		}

		@Override
		public double getDirection(int toIndex, int fromIndex) {
			return Transition.DIR_RIGHT;
		}

		@Override
		public Control getControl(int index) {
			return cnvs;
		}

		@Override
		public Composite getComposite() {
			return cnvsComposite;
		}

		@Override
		public void addSelectionListener(SelectionListener listener) {

		}

	};

	/**
	 * This is the mouse listener reacting to mouse clicks
	 * on the canvas.
	 */
	private MouseListener mouseListener = new MouseListener() {

		@Override
		public void mouseUp(MouseEvent e) {

		}

		@Override
		public void mouseDown(MouseEvent e) {

			// This method handles the users clicks on the left/right
			// side of the slideshow and triggers the switch of the
			// images.

			if (transitionTimerThread.isAlive()) {
				return;
			}

			Point cursorLocation = Display.getCurrent().getCursorLocation();
			Point relativeCurserLocation = cnvs.toControl(cursorLocation);

			// The width and height of the current image.
			int imageWidth = scaled_imgs[curImage].getImageData().width;
			int imageHeight = scaled_imgs[curImage].getImageData().height;

			// The user clicks of on of the points at the bottom.
			if (relativeCurserLocation.y > (imageHeight - Utilities.pointVerticalDistance)
					&& relativeCurserLocation.y < imageHeight) {
				int leftEdge = (imageWidth / 2) - ((scaled_imgs.length * Utilities.pointHorizontalSpacing) / 2);
				int rightEdge = (imageWidth / 2) + ((scaled_imgs.length * Utilities.pointHorizontalSpacing) / 2);
				if (relativeCurserLocation.x > leftEdge && relativeCurserLocation.x < rightEdge) {
					int selectedImage = (relativeCurserLocation.x - leftEdge) / Utilities.pointHorizontalSpacing;

					slideToImageNr(selectedImage);
					
					return;
				}
			}

			// The user clicks somewhere on the right or the left of the image.
			if (relativeCurserLocation.y >= 0 && relativeCurserLocation.y <= imageHeight) {
				if (relativeCurserLocation.x < (imageWidth / 2)) {
					// Slide to the left.
					slideToPrevImage();
				} else {
					// Slide to the right.
					slideToNextImage();
				}
			}

		}

		@Override
		public void mouseDoubleClick(MouseEvent e) {
			// Do nothing
		}
	};

	/**
	 * This timer simply counts down from 1 second.</br>
	 * This is used to block transition when a transition is in progress.
	 */
	private Runnable transitionTimerRunnable = new Runnable() {

		@Override
		public void run() {
			try {
				// The 100 ms are just for security reasons.
				Thread.sleep((long) slideTransition.getTotalTransitionTime() + 100);
			} catch (InterruptedException e) {
				LogUtil.logError(IntroductionPlugin.PLUGIN_ID, e);
			}

		}
	};

	/**
	 * Thread running the transitionTimerRunnable.
	 */
	private Thread transitionTimerThread = new Thread();

	/**
	 * Runnable that has a countdown and after that countdown finished
	 * it triggers the automatic sliding.
	 */
	private Runnable timerRunnable = new Runnable() {

		@Override
		public void run() {
			if (autoSlide) {
				try {

					// After the countdown has finished switch to the next image.
					// After 15 seconds it switches to the next image.
					Thread.sleep(15000);

					Display.getDefault().asyncExec(new Runnable() {

						@Override
						public void run() {
							slideToNextImage();
						}
					});

				} catch (InterruptedException e) {
					// Here is an interrupted exception thrown.
					// I ignore this. I know using exceptions in the
					// program flow is bad, but it is easy.
				}
			}
		}
	};

	/**
	 * Thread for running the timerRunnable.
	 */
	private Thread timerThread = new Thread(timerRunnable);
	
	/**
	 * This resizable just calls the resize function.</Br>
	 * As it uses GUI elements it has to be run in the GUI-thread.
	 */
	private Runnable resizeRunnable = new Runnable() {
		
		@Override
		public void run() {
			
			Display.getDefault().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					scaleImagesToCanvasSize();
				}
			});
		}
	};

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.jcryptool.core.introduction.views.AlgorithmInstruction"; //$NON-NLS-1$

	@Override
	public void createPartControl(Composite parent) {
		
    	/**
    	 * This assigns a "wrong" online-help to this plugin.
    	 * This was added, to force the JCT open the online-help of the
    	 * Algorithm perspective when clicking on the help icon in the
    	 * JCT toolbar.
    	 */
    	String linkToAlgorithmHelp = "/org.jcryptool.core.help/$nl$/help/users/general/perspective_algorithm.html";
    	HelpHrefRegistry.getInstance().registerHrefFor(IntroductionPlugin.PLUGIN_ID, linkToAlgorithmHelp);

		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		Composite content = new Composite(scrolledComposite, SWT.NONE);
		GridLayout gl_content = new GridLayout(3, false);
		gl_content.horizontalSpacing = 0;
		gl_content.verticalSpacing = 0;
		content.setLayout(gl_content);
		content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		scrolledComposite.setContent(content);

		// Load the images to the slideshow.
		initializeScaledImages();
		
		cnvsComposite = new Composite(content, SWT.NONE);
		cnvsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout gl_cnvsComposite = new GridLayout();
		gl_cnvsComposite.marginWidth = 0;
		gl_cnvsComposite.marginHeight = 0;
		cnvsComposite.setLayout(gl_cnvsComposite);
		
		cnvsComposite.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event event) {
				int[] sizehint = computeSlideshowSizeHint();
				gridData_cnvs.widthHint = sizehint[0];
				gridData_cnvs.heightHint = sizehint[1];
				cnvsComposite.layout(new Control[] { cnvs });
			}
		});

		// The canvas the slideshow is painted on.
		cnvs = new Canvas(cnvsComposite, SWT.DOUBLE_BUFFERED);
		gridData_cnvs = new GridData(SWT.CENTER, SWT.FILL, true, true);
		int[] initialSizeHint = computeSlideshowSizeHint();
		gridData_cnvs.widthHint = initialSizeHint[0];
		gridData_cnvs.heightHint = initialSizeHint[1];
		cnvs.setLayoutData(gridData_cnvs);
		cnvs.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				// Initial drawing of the first image in the slideshow
				Image img = scaled_imgs[curImage];
				e.gc.drawImage(img, 0, 0);

				// Initial drawing of the arrows on the left and right side of the slideshow
				// and the points at the bottom of the slideshow.
				Utilities utils = new Utilities(scaled_imgs[curImage], scaled_imgs.length, curImage);
				utils.drawLeftArrow(e);
				utils.drawRightArrow(e);
				utils.showPosition(e);
			}
		});

		cnvs.addMouseListener(mouseListener);

		cnvs.addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent e) {
				// This resizes the images in the slideshow.

				// Do not change the size of the images when the images slide.
				// Elsewhere a NullPointerException occurs.
				if (transitionTimerThread.isAlive()) {
					return;
				}
				
				// This causes the resize of the slideshow.
				debouncer.debounce(100, resizeRunnable);

			}

			@Override
			public void controlMoved(ControlEvent e) {
				// No need to resize, because the size of the
				// canvas does not change when moving the plugin.
			}
		});

		lowerArea = new Composite(content, SWT.NONE);
		lowerArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		GridLayout gl_lowerArea = new GridLayout(2, false);
		gl_lowerArea.marginHeight = 0;
		gl_lowerArea.marginWidth = 0;
		lowerArea.setLayout(gl_lowerArea);
		
		// Spacer of the left of the "do not show again" checkbox.
		new Label(lowerArea, SWT.NONE).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// This is the "do not show again" checkbox in the bottom right corner.
		Button checkbox = new Button(lowerArea, SWT.CHECK);
		checkbox.setOrientation(SWT.RIGHT_TO_LEFT);
		checkbox.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, true));
		checkbox.setForeground(ColorService.GRAY);
		checkbox.setText(Messages.AlgorithmInstruction_showAgain);

		// The button is set/unset depending on the setting in the preferences.
		// Therefore the preferencces are loaded and the entry
		// "DONT_SHOW_ALGORITHM_INTRODUCTION"
		// is read.
		IPreferenceStore prefs = new ScopedPreferenceStore(InstanceScope.INSTANCE, CorePlugin.PLUGIN_ID);
		boolean show = prefs.getBoolean("DONT_SHOW_ALGORITHM_INTRODUCTION"); //$NON-NLS-1$
		checkbox.setSelection(show);
		checkbox.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// This changes the preferences if the user changed the
				// "Do no show again" checkbox..
				IPreferenceStore prefs = new ScopedPreferenceStore(InstanceScope.INSTANCE, CorePlugin.PLUGIN_ID);
				prefs.setValue("DONT_SHOW_ALGORITHM_INTRODUCTION", checkbox.getSelection()); //$NON-NLS-1$

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		// The following 4 lines initialize the slideshow.
		transitionManager = new TransitionManager(transitionable);

		// This defines how the Images should slide.
		// There a several options:
		// - SlideTransition: horizontal and vertical slide transitions
		// - CubicRotationTransition: horizontal and vertical 3D cubic rotations
		// - FadeTransition: fade transition
		slideTransition = new SlideTransition(transitionManager);

		transitionManager.setTransition(slideTransition);

		transitionManager.setControlImages(scaled_imgs);

		// Calculate the minimal size of the plugin to
		// set the scrollbars correct.
		scrolledComposite.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		SmoothScroller.scrollSmooth(scrolledComposite);

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,
				IntroductionPlugin.PLUGIN_ID + ".introductionContexHelpID"); //$NON-NLS-1$
		
		// Start the thread that changes the images after 15 seconds.
		startAutoSwitchImages();

	}
	

	private int[] computeSlideshowSizeHint() {
		Rectangle parentSize = cnvsComposite.getClientArea();
		float aspectRatio = getCurrentSlideAspectRatio();
		float parentAspectRatio = (float) parentSize.width / (float) parentSize.height;
		int adaptedWidth = parentSize.width;
		int adaptedHeight = parentSize.height;
		if (adaptedWidth <= 0 || adaptedHeight <= 0) {
			adaptedHeight = 10; 
			adaptedWidth = 10;
		}
		if (aspectRatio > parentAspectRatio) { // broader than allowed -> adapt height to match parent width
			adaptedHeight = (int) Math.round(adaptedWidth / aspectRatio);
		} else { // other way around
			adaptedWidth = (int) Math.round(adaptedHeight * aspectRatio);
		}
		int[] adaptedSize = new int[] { adaptedWidth, adaptedHeight };
		return adaptedSize;
	}

	/**
	 * This functions calculates the current side ration of the slide.</br>
	 * For exaple 16:9=1.777
	 * @return The side ratio of the current slide.
	 */
	protected float getCurrentSlideAspectRatio() {
		ImageData imageData = scaled_imgs[curImage].getImageData();
		
		float ratio = (float) imageData.width / (float) imageData.height;
		
		return ratio;
	}

	/**
	 * This method only initializes
	 * <code>scaled_imgs = new Image[original_imgs.length];</code> and fills it with
	 * the images from <code>original_imgs</code> array.
	 */
	private void initializeScaledImages() {
		scaled_imgs = new Image[original_imgs.length];
		System.arraycopy(original_imgs, 0, scaled_imgs, 0, original_imgs.length);
	}

	/**
	 * This starts the automatic switching of images in the slideshow.
	 */
	private void startAutoSwitchImages() {
		autoSlide = true;
		resetTimer();
	}

	/**
	 * This stops the automatic switching of images in the slideshow.
	 */
	private void stopAutoSwitchImages() {
		autoSlide = false;
		if (timerThread.isAlive()) {
			timerThread.interrupt();
		}
	}

	/**
	 * Slides an Image to the right.
	 */
	private void slideToNextImage() {
		int nextImage = Math.floorMod(curImage + 1, scaled_imgs.length);

		transitionTimerThread = new Thread(transitionTimerRunnable);
		transitionTimerThread.start();
		slideTransition.start(scaled_imgs[curImage], curImage, scaled_imgs[nextImage], nextImage, cnvs,
				SlideTransition.DIR_LEFT);
		curImage = nextImage;
		resetTimer();
	}

	/**
	 * Slides an image to the left.
	 */
	private void slideToPrevImage() {
		int previousImage = Math.floorMod(curImage - 1, scaled_imgs.length);

		transitionTimerThread = new Thread(transitionTimerRunnable);
		transitionTimerThread.start();
		slideTransition.start(scaled_imgs[curImage], curImage, scaled_imgs[previousImage], previousImage, cnvs,
				SlideTransition.DIR_RIGHT);
		curImage = previousImage;
		resetTimer();
	}

	private void slideToImageNr(int imageNr) {
		// Only do a slide if the given image is
		// different from the current image.
		if (imageNr != curImage) {

			double direction = 0.0;

			if (imageNr < curImage) {
				// Slide left
				direction = SlideTransition.DIR_RIGHT;
			} else {
				// Slide right
				direction = SlideTransition.DIR_LEFT;
			}

			// Start the timer that avoids any interaction when a transition is in progress.
			transitionTimerThread = new Thread(transitionTimerRunnable);
			transitionTimerThread.start();

			// This starts the transition
			slideTransition.start(scaled_imgs[curImage], curImage, scaled_imgs[imageNr], imageNr, cnvs, direction);
			curImage = imageNr;
			
			
			resetTimer();
		}
	}

	/**
	 * Scales the image to available size of the canvas.
	 */
	private void scaleImagesToCanvasSize() {

		// The following code calculates the side ratios of the image
		// to fit perfectly in the canvas
		ImageData imageData;

		// Attributes of the original image.
		float imageWidth, imageHeight, imageRatio;

		// A factor to calculate the width/height of the scaled image.
		float resizeFactor;
		
		// Attributs of the canvas
		float canvasWidth = cnvs.getClientArea().width;
		float canvasHeight = cnvs.getClientArea().height;
		float canvasRatio = canvasWidth / canvasHeight;

		// Iterate through all images.
		for (int i = 0; i < original_imgs.length; i++) {
			imageData = original_imgs[i].getImageData();
			imageWidth = imageData.width;
			imageHeight = imageData.height;
			imageRatio = imageWidth / imageHeight;

			if (imageRatio <= canvasRatio) {
				// The canvas height is the restricting size.
				resizeFactor = canvasHeight / imageHeight;
				int width = (int) (imageWidth * resizeFactor);
				int height = (int) canvasHeight;
				// Use the original, unscaled images as source. This
				// keeps up the quality of the images if the
				// window is often resized.
				scaled_imgs[i] = ImageScaler.resize(original_imgs[i], width, height);

			} else {
				// The width of the composite is the restricting factor.
				resizeFactor = canvasWidth / imageWidth;
				int width = (int) canvasWidth;
				int height = (int) (imageData.height * resizeFactor);
				// Use the original, unscaled images as source. This
				// keeps up the quality of the images if the
				// window is often resized.
				scaled_imgs[i] = ImageScaler.resize(original_imgs[i], width, height);

			}
		}

		// Set the new scaled images to the transition.
		transitionManager.clearControlImages();
		transitionManager.setControlImages(scaled_imgs);

		// Redraw the slideshow, because the automatic redraw from the 
		// resize event already happened.
		cnvs.redraw();
	}

	@Override
	public void setFocus() {
		cnvs.setFocus();
	}

	@Override
	public void dispose() {
		// This is a custom dispose method.
		// It stops the automatic switching of images in the slideshow.
		// This is necessary, because the Thread that switches the images
		// would still run, if the users closes the introduction. The Thread is
		// is still running, but all widgets are disposed, this NullPointerExceptions.
		// Therefore stop it when the user closes the introduction.
		stopAutoSwitchImages();
		debouncer.cancelAllJobs();
		super.dispose();
	}

	/**
	 * Returns if the slideshow is activated or not.
	 * 
	 * @return True, if the slideshow is working, false if not.
	 */
	public boolean getAutoSlide() {
		return autoSlide;
	}

	/**
	 * Sets if the slideshow should work or not.</br>
	 * This stops / starts the thread sliding the images.
	 * 
	 * @param autoSlide True, if the slideshow should work, false it should remain
	 *                  at the current image.
	 */
	public void setAutoSlide(boolean autoSlide) {

		if (autoSlide) {
			startAutoSwitchImages();
		} else {
			stopAutoSwitchImages();
		}
	}


	/**
	 * This method triggers an slide in 15 seconds.
	 */
	private void resetTimer() {
		if (timerThread.isAlive()) {
			timerThread.interrupt();
		}

		timerThread = new Thread(timerRunnable);
		timerThread.start();
	}

}
