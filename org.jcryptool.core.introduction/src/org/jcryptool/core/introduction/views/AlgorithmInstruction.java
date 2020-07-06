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
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.jcryptool.core.CorePlugin;
import org.jcryptool.core.introduction.utils.ImageScaler;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.core.util.ui.auto.SmoothScroller;

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
	 * True, if the autoslide slides the next image after 30 seconds</br>
	 * False, if the user slides by hand. The autoslider waits for another 30 secs
	 * to slide to the next image. The autoslider set this value after an automatic
	 * slide to true.
	 */
	private boolean allowNextAutoSlide = true;
	private GridData gridData_cnvs;
	private Canvas cnvs;
	
	
	/**
	 * Composite containing the whole GUI of the plugin.
	 */
	private Composite content;
	
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
			ImageService.getImage(IntroductionPlugin.PLUGIN_ID, Messages.AlgorithmInstruction_image_7) 
	};
	
	/**
	 * Images for the slideshow scaled to the canvas size.
	 */
	private Image[] scaled_imgs;
	
	/**
	 * The number of the current image in the slideshow.</br>
	 * Minimum: 0; Maximum: original_imgs.length(),
	 */
	private int curImage = 0;
	
	
	private SlideTransition slideTransition;
	
	/**
	 * A thread that switches the images every x seconds.
	 */
	private Thread t = new Thread(new Runnable() {

		@Override
		public void run() {
			while (true) {
				try {
					// Wait 15 seconds between switching the images.
					Thread.sleep(15000);
				} catch (InterruptedException e) {
					return;
				}

				// We want to access a GUI element that is
				// used by the GUI thread, so we can not access
				// it via this thread. We have to use a thread
				// that can access GUI elements.
				
				while (transitionTimerThread.isAlive()) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						LogUtil.logError(IntroductionPlugin.PLUGIN_ID, e);
					}
				}
				
				if (allowNextAutoSlide) {
					Display.getDefault().asyncExec(new Runnable() {

						@Override
						public void run() {
							slideToNextImage();
						}
					});
				} else {
					allowNextAutoSlide = true;
				}

			}
		}
	});

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
			return content;
		}

		@Override
		public void addSelectionListener(SelectionListener listener) {

		}
		
		
	};

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
			Point relativeCurserLocation = Display.getCurrent().getFocusControl().toControl(cursorLocation);
			
			if (relativeCurserLocation.y > 0 && relativeCurserLocation.y < scaled_imgs[curImage].getImageData().height) {
				if (relativeCurserLocation.x < (scaled_imgs[curImage].getImageData().width / 2)) {
					// Slide to the left.
					allowNextAutoSlide = false;
					slideToPrevImage();
				} else {
					// Slide to the right.
					allowNextAutoSlide = false;
					slideToNextImage();
				}
			}



		}

		@Override
		public void mouseDoubleClick(MouseEvent e) {

		}
	};
	
	
	
	private Runnable transitionTimerRunnable = new Runnable() {
		
		@Override
		public void run() {
			try {
				Thread.sleep((long) slideTransition.getTotalTransitionTime());
			} catch (InterruptedException e) {
				LogUtil.logError(IntroductionPlugin.PLUGIN_ID, e);
			}
			
		}
	};
	
	private Thread transitionTimerThread = new Thread();
	
	private Thread resizeThread = new Thread();
	
	private Runnable resizeRunnable = new Runnable() {
		
		@Override
		public void run() {
			System.out.println("resizeRunnable RUnning"); //$NON-NLS-1$
			// TODO Auto-generated method stub
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

		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		content = new Composite(scrolledComposite, SWT.NONE);
		GridLayout gl_content = new GridLayout(3, false);
		gl_content.horizontalSpacing = 0;
		gl_content.verticalSpacing = 0;
		content.setLayout(gl_content);
		content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//		content.setBackground(ColorService.GREEN);
		
		scrolledComposite.setContent(content);


		initializeScaledImages();
		
		content.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event event) {
				int[] sizehint = computeSlideshowSizeHint();
				gridData_cnvs.widthHint = sizehint[0];
				gridData_cnvs.heightHint = sizehint[1];
				content.layout(new Control[] {cnvs});
			}
		});
		
		
		// The canvas the slideshow is painted on.
		cnvs = new Canvas(content, SWT.DOUBLE_BUFFERED);
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
				Utilities utils = new  Utilities(scaled_imgs[curImage], scaled_imgs.length, curImage);
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
				
				scaleImagesToCanvasSize();
//				resizeThread = new Thread(resizeRunnable);
//				System.out.println("resizeThread status " + resizeThread.getState());
//				if (!resizeThread.isAlive()) {
//					resizeThread.start();
//				}
				
			}
			
			@Override
			public void controlMoved(ControlEvent e) {
				// No need to resize, because the size of the 
				// canvas does not change when moving the plugin.
			}
		});

		Composite lowerArea = new Composite(content, SWT.NONE);
		lowerArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		GridLayout gl_lowerArea = new GridLayout();
		gl_lowerArea.marginHeight = 0;
		gl_lowerArea.marginWidth = 0;
		lowerArea.setLayout(gl_lowerArea);

		// This is the "do not show again" checkbox in the bottom right corner.
		Button checkbox = new Button(lowerArea, SWT.CHECK);
		checkbox.setOrientation(SWT.RIGHT_TO_LEFT);
		checkbox.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, true));
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
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, "org.jcryptool.core.introduction.viewer"); //$NON-NLS-1$

		// Start the thread that changes the images after 15 seconds.
		startAutoSwitchImages();

	}

	private int[] computeSlideshowSizeHint() {
		Rectangle parentSize = content.getBounds();
		float aspectRatio = getCurrentSlideAspectRatio();
		float parentAspectRatio = (float) parentSize.width / (float) parentSize.height;
		int adaptedWidth = parentSize.width;
		int adaptedHeight = parentSize.height;
		if(adaptedWidth <= 0 || adaptedHeight <= 0) {
			adaptedHeight = 10; // TODO: handle better?
			adaptedWidth = 10;
		}
		if (aspectRatio > parentAspectRatio) { // broader than allowed -> adapt height to match parent width
			adaptedHeight = (int) Math.round(adaptedWidth / aspectRatio);
		} else {                               // other way around
			adaptedWidth = (int) Math.round(adaptedHeight * aspectRatio);
		}
		int[] adaptedSize = new int[] {adaptedWidth, adaptedHeight};
		return adaptedSize;
	}

	protected float getCurrentSlideAspectRatio() {
		return 16.0f / 9.0f; //TODO: dynamically get aspect ratio from displayed img
	}

	/**
	 * This method only initializes <code>scaled_imgs = new Image[original_imgs.length];</code>
	 * and fills it with the images from <code>original_imgs</code> array.
	 */
	private void initializeScaledImages() {
		scaled_imgs = new Image[original_imgs.length];
		System.arraycopy(original_imgs, 0, scaled_imgs, 0, original_imgs.length);
	}

	/**
	 * This starts the automatic switching of images in the slideshow.
	 */
	private void startAutoSwitchImages() {
		t.start();
	}

	/**
	 * This stops the automatic switching of images in the slideshow.
	 */
	private void stopAutoSwitchImages() {
		t.interrupt();
	}

	/**
	 * Slides an Image to the right.
	 */
	private void slideToNextImage() {
		int nextImage = Math.floorMod(curImage + 1, scaled_imgs.length);
		transitionTimerThread = new Thread(transitionTimerRunnable);
		transitionTimerThread.start();
		slideTransition.start(scaled_imgs[curImage], scaled_imgs[nextImage], cnvs, SlideTransition.DIR_LEFT);
		curImage = nextImage;
	}

	/**
	 * Slides an image to the left.
	 */
	private void slideToPrevImage() {
		int previousImage = Math.floorMod(curImage - 1, scaled_imgs.length);
		transitionTimerThread = new Thread(transitionTimerRunnable);
		transitionTimerThread.start();
		slideTransition.start(scaled_imgs[curImage], scaled_imgs[previousImage], cnvs, SlideTransition.DIR_RIGHT);
		curImage = previousImage;
	}
	
	/**
	 * Scales the image to available size of the canvas.
	 */
	private void scaleImagesToCanvasSize() {
		long start = System.nanoTime();
		System.out.println("scaleImagesToCanvasSize() called"); //$NON-NLS-1$
		// If a transition is currently in progress do nothing.
		if (transitionTimerThread.isAlive()) {
			return;
		}
		
		// The following code calculates the side ratios of the image
		// to fit perfectly in the canvas
		ImageData imageData;
		
		// Attributes of the original image.
		float imageWidth, imageHeight, imageRatio;
		
		// Attributs of the canvas
		float canvasWidth, canvasHeight, canvasRatio;
		
		// A factor to calculate the width/height of the scaled image.
		float resizeFactor;

		// Width and height the image should be scaled to.
//		int width, height;
		
//		ExecutorService es = Executors.newCachedThreadPool();
//		ExecutorService es = Executors.newFixedThreadPool(10);
		
		// Iterate through all images.
		for (int i = 0; i < original_imgs.length; i++) {
			final int inner_i = i;
			imageData = original_imgs[i].getImageData();
			imageWidth = imageData.width;
			imageHeight = imageData.height;
			imageRatio = imageWidth / imageHeight;
			
			canvasWidth = cnvs.getClientArea().width;
			canvasHeight = cnvs.getClientArea().height;
			canvasRatio = canvasWidth / canvasHeight;
			
			long resizeStart = System.nanoTime();
			
			if (imageRatio <= canvasRatio) {
				// The canvas height is the restricting size.
				resizeFactor = canvasHeight / imageHeight;
				int width = (int) (imageWidth * resizeFactor);
				int height = (int) canvasHeight;
				// Use the original, unscaled images as source. This
				// keeps up the quality of the images if the 
				// window is often resized.
				scaled_imgs[i] = ImageScaler.resize(original_imgs[i], width, height);
//				es.execute(new Runnable() {
//					
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						scaled_imgs[inner_i] = ImageScaler.resize(original_imgs[inner_i], width, height);
//					}
//				});
			} else {
				// The width of the composite is the restricting factor.
				resizeFactor = canvasWidth / imageWidth;
				int width = (int) canvasWidth;
				int height = (int) (imageData.height * resizeFactor);
				// Use the original, unscaled images as source. This
				// keeps up the quality of the images if the 
				// window is often resized.
				scaled_imgs[i] = ImageScaler.resize(original_imgs[i], width, height);
//				es.execute(new Runnable() {
//					
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						scaled_imgs[inner_i] = ImageScaler.resize(original_imgs[inner_i], width, height);
//					}
//				});
				
				
			}
			

			
			long resizeStop = System.nanoTime();
			System.out.println("Resize time: " + ((resizeStop - resizeStart) / 1000000) + "ms"); //$NON-NLS-1$ //$NON-NLS-2$
			
		}
		
//		es.shutdown();
//		try {
//			es.awaitTermination(1, TimeUnit.SECONDS);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		es.awaitTermination(10, TimeUnit.SECONDS);

		// Set the new scaled images to the transition.
		transitionManager.clearControlImages();
		transitionManager.setControlImages(scaled_imgs);
		
		long stop = System.nanoTime();
		System.out.println("scaleImagesToCanvasSize() size: " + ((stop - start) / 1000000) + "ms"); //$NON-NLS-1$ //$NON-NLS-2$
		
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
		super.dispose();
	}

}
