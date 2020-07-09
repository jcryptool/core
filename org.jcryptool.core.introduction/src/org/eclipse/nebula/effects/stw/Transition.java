/*******************************************************************************
 * Copyright (c) 2010 Ahmed Mahran and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0 
 *
 * Contributors:
 *     Ahmed Mahran - initial API and implementation
 *******************************************************************************/

package org.eclipse.nebula.effects.stw;

import org.eclipse.nebula.effects.stw.utils.Utilities;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;

/**
 * An abstract class handling the basic actions required for whatever transition
 * effect. These actions are like the transition loop.<br/>
 * <br/>
 * 
 * To implement a new transition effect, this class should be extended by the
 * new transition class and only the three methods
 * {@link Transition#initTransition(Image, Image, GC, double)} ,
 * {@link Transition#stepTransition(long, Image, Image, GC, double)} and
 * {@link Transition#endTransition(Image, Image, GC, double)} must be
 * implemented.<br/>
 * <br/>
 * 
 * The transition loop: <code><pre>
 * xitionImgGC.drawImage(from, 0, 0);
 * initTransition(from, to, xitionImgGC, direction);
 * render(xitionImgGC);
 * while(t <= T) {
 *   if(t <= T) {
 *     stepTransition(t, from, to, xitionImgGC, direction);
 *   } else {
 *     xitionImgGC.drawImage(to, 0, 0);
 *     endTransition(from, to, xitionImgGC, direction);
 *   }
 *   render(xitionImgGC);
 *   t += dt;
 * }
 * </code>
 * </pre>
 * 
 * The <code>initTransition</code> method initializes the transition variables
 * and draws the initial/first frame of the transition effect at time 0. The
 * <code>stepTransition</code> method calculates the new transition variables
 * values based on the time parameter <code>t</code> and draws the transition
 * effect at time instance t. Finally, the <code>endTransition</code> method
 * finalizes the transition and draws the last frame at instance T.
 * 
 * @author Ahmed Mahran (ahmahran@gmail.com)
 */
public abstract class Transition {

	/**
	 * The default fps (frames per second) is 60
	 */
	public static final long DEFAULT_FPS = 60;
	/**
	 * The default transition time is 1000 ms
	 */
	public static final long DEFAULT_T = 1000;

	/**
	 * The Right direction, 0 degrees
	 */
	public static final double DIR_RIGHT = 0;
	/**
	 * The Up direction, 90 degrees
	 */
	public static final double DIR_UP = 90;
	/**
	 * The Left direction, 180 degrees
	 */
	public static final double DIR_LEFT = 180;
	/**
	 * The Down direction, 270 degrees
	 */
	public static final double DIR_DOWN = 270;
	/**
	 * Flag to indicate if this OS is a MacOS X or not.
	 */
	protected static final boolean IS_MAC_OS = System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0;
	/**
	 * Flag to indicate if this OS is a Linux OS or not.
	 */
	protected static final boolean IS_LINUX_OS = System.getProperty("os.name").toLowerCase().indexOf("nux") >= 0;

	protected TransitionManager _transitionManager;

	protected long _fps; // frames per second
	protected long _T; // total transition time in milliseconds

	private long _dt; // time step
	private long _t; // time counter

	/**
	 * The PaintListener that draws the transition.
	 */
	public TransitionPainter transitionPainter;

	/**
	 * Constructs a new transition object
	 * 
	 * @param transitionManager the transition manager to be used to manage
	 *                          transitions
	 * @param fps               number of frames per second
	 * @param T                 the total time the transition effect will take
	 */
	public Transition(TransitionManager transitionManager, long fps, long T) {
		_transitionManager = transitionManager;
//        _transitionManager.getTransitionable().get
		_fps = fps;
		_T = T;
		_t = 0;
		_dt = (long) (1000.0 / _fps);
	}

	/**
	 * This constructor is similar to new Transition(transitionManager,
	 * {@link Transition#DEFAULT_FPS}, {@link Transition#DEFAULT_T})
	 * 
	 * @param transitionManager the transition manager to be used to manage
	 *                          transitions
	 */
	public Transition(TransitionManager transitionManager) {
		this(transitionManager, DEFAULT_FPS, DEFAULT_T);
	}

	/**
	 * Sets the maximum fps (number of frames per second) for the transition. The
	 * actual number of frames displayed will vary depending on the current workload
	 * on the machine.
	 * 
	 * @param fps maximum number of frames per second
	 */
	public final void setFPS(long fps) {
		_fps = fps;
		_dt = (long) (1000.0 / fps);
	}

	/**
	 * Returns the maximum number of frames per second
	 * 
	 * @return the maximum number of frames per second
	 */
	public final long getFPS() {
		return _fps;
	}

	/**
	 * Sets the total time of the transition effect in milliseconds.
	 * 
	 * @param T total time of the transition effect in milliseconds
	 */
	public final void setTotalTransitionTime(long T) {
		_T = T;
	}

	/**
	 * Returns the total time of the transition effect in millisecond
	 * 
	 * @return the total time of the transition effect in millisecond
	 */
	public final double getTotalTransitionTime() {
		return _T;
	}

	/**
	 * Returns the paint listener that draws the transitions.
	 * 
	 * @return The transitionPainter
	 */
	public final TransitionPainter getTransitionPainter() {
		return transitionPainter;
	}

	/**
	 * Starts the transition from the <i>from</i> image to the <i>to</i> image
	 * drawing the effect on the graphics context object <i>gc</i>. The
	 * <i>direction</i> parameter determines the direction of the transition in
	 * degrees starting from 0 as the right direction and increasing in counter
	 * clock wise direction.
	 * 
	 * @param from      is the image to start the transition from
	 * @param to        is the image to end the transition to
	 * @param canvas    is the canvas object to draw the transition on
	 * @param direction determines the direction of the transition in degrees
	 */
	public final void start(final Image from, final int fromNr, final Image to, final int toNr, final Canvas canvas, final double direction) {

		// _transitionManager.isAnyTransitionInProgress.setValue(true);

		boolean flag = true;
		long t0 = System.currentTimeMillis();
		long dt = 0;
		long ttemp = 0;
		_t = 0;

		// prepare transition background
		ImageData fromData = from.getImageData();
		final Image xitionBg = new Image(Display.getCurrent(), fromData.width, fromData.height);
		GC xitionBgGC = new GC(xitionBg);

		xitionBgGC.setBackground(_transitionManager.backgroundColor);
		xitionBgGC.fillRectangle(0, 0, fromData.width, fromData.height);

		if (null != _transitionManager.backgroundImage) {

			ImageData imgData = _transitionManager.backgroundImage.getImageData();
			xitionBgGC.drawImage(_transitionManager.backgroundImage, 0, 0, imgData.width, imgData.height, 0, 0,
					fromData.width, fromData.height);

		}

		xitionBgGC.dispose();

		transitionPainter = new TransitionPainter(canvas, from, fromNr, to, toNr, direction, xitionBg);

		transitionPainter.paintTransition(TransitionPainter.TRANSITION_INIT);

		// while(!_transitionManager.isCurrentTransitionCanceled.get()
		// && _t <= _T) {
		while (_t <= _T) {

			ttemp = System.currentTimeMillis() - t0;
			dt = ttemp - _t;
			if (flag)
				_t = ttemp;

			// this condition is to make sure that the
			// required fps (or less) is satisfied and
			// not more
			if (dt >= _dt) {
				if (_t <= _T) {
					transitionPainter.paintTransition(TransitionPainter.TRANSITION_STEP);

				} else {
					transitionPainter.paintTransition(TransitionPainter.TRANSITION_END);
				}

				flag = true;
				doEvents();

			} else {
				try {
					flag = false;
					Thread.sleep(_dt - dt);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

		xitionBg.dispose();
		// _transitionManager.isAnyTransitionInProgress.setValue(false);
	}

	protected void doEvents() {
		Display.getCurrent().readAndDispatch();
	}

	protected abstract void initTransition(final Image from, final Image to, final GC gc, final double direction);

	protected abstract void stepTransition(long t, final Image from, final Image to, final GC gc,
			final double direction);

	protected abstract void endTransition(final Image from, final Image to, final GC gc, final double direction);

	/**
	 * Listener to paint the canvas object, where the transition is visualized.
	 * 
	 * @author Rodrigo Cantu Polo (cantupolo@yahoo.com.br)
	 */
	public class TransitionPainter implements PaintListener {

		/** Initial transition to paint. */
		private static final int TRANSITION_INIT = 0;

		/** Step transition to paint. */
		private static final int TRANSITION_STEP = 1;

		/** End transition to paint. */
		private static final int TRANSITION_END = 2;

		/** Indicate that this object is enabled to paint the canvas. */
		private boolean _isEnabled = false;

		/** Transition to paint. */
		private int _transition = -1;

		/** GC for the canvas object. */
		private GC _gc;

		/** Canvas where to paint the transitions. */
		private Canvas _canvas;

		/** From image to paint in the canvas. */
		private final Image _from;

		/** To image to paint in the canvas. */
		private final Image _to;

		/** Direction of the animation effect. */
		private final double _direction;

		/** Initial background of the transition paint. */
		private final Image _xitionBg;

		/** Image to draw the transition. */
		private Image _xitionImg;

		/** Graphics context object for the _xitionImg object. */
		private GC _xitionImgGC;

		private Utilities utils;

		/**
		 * Constructor.
		 * 
		 * @param canvas    Canvas where to paint the transitions.
		 * @param from      From image to paint in the canvas.
		 * @param to        To image to paint in the canvas.
		 * @param direction Direction of the animation effect.
		 * @param xitionBg  Initial background of the transition paint.
		 */
		private TransitionPainter(Canvas canvas, final Image from, final int fromNr,
				final Image to, final int toNr, final double direction, final Image xitionBg){
			_canvas = canvas;
			_from = from;
			_to = to;
			_direction = direction;
			_xitionBg = xitionBg;

			utils = new Utilities(to, _transitionManager.getTotalImages(), toNr);
		}

		/**
		 * Initialize the transition image objects.
		 * 
		 * @param display Display to use for the objects.
		 */
		private void initXitionImg(Display display) {
			_xitionImg = new Image(display, _from.getBounds().width, _from.getBounds().height);
			_xitionImgGC = new GC(_xitionImg);
		}

		/**
		 * Dispose the transition image objects.
		 */
		private void disposeXitionImg() {
			_xitionImg.dispose();
			_xitionImgGC.dispose();
		}

		/**
		 * Paint a transition.
		 * 
		 * @param transition Transition to paint.
		 */
		public void paintTransition(int transition) {
			_transition = transition;
			// The check if the _canvas is not disposed
			// (= the canvas exists) is necessary, because
			// the user can close plugin while a transition
			// is in progress.
			if (!_canvas.isDisposed()) {
				if (!IS_LINUX_OS) {
					if (_transition == TRANSITION_INIT) {
						_canvas.addPaintListener(this);
					}
					_isEnabled = true;
					_canvas.redraw();
					_canvas.getDisplay().update();
					_canvas.getDisplay().readAndDispatch();
					_isEnabled = false;
					if (_transition == TRANSITION_END) {
						_canvas.removePaintListener(this);
					}

				} else {
					if (_transition == TRANSITION_INIT) {
						initXitionImg(_canvas.getDisplay());
						_gc = new GC(_canvas);
					}
					paintTransition(_xitionImgGC, _transition);
					_gc.drawImage(_xitionImg, 0, 0);
					if (_transition == TRANSITION_END) {
						disposeXitionImg();
						_gc.dispose();
					}
				}
			}
		}

		@Override
		public void paintControl(PaintEvent e) {
			if (_isEnabled) {
				initXitionImg(e.display);
				paintTransition(_xitionImgGC, _transition);
				e.gc.drawImage(_xitionImg, 0, 0);
				disposeXitionImg();

				utils.drawLeftArrow(e);
				utils.drawRightArrow(e);
				utils.showPosition(e);

			}
		}

		/**
		 * Paint a transition.
		 * 
		 * @param gc         GC object to paint.
		 * @param transition Transition to paint.
		 */
		private void paintTransition(GC gc, int transition) {
			switch (transition) {
			case TRANSITION_INIT:
				gc.drawImage(_xitionBg, 0, 0);
				gc.drawImage(_from, 0, 0);
				initTransition(_from, _to, gc, _direction);
				break;

			case TRANSITION_STEP:
				gc.drawImage(_xitionBg, 0, 0);
				stepTransition(_t, _from, _to, gc, _direction);
				break;

			case TRANSITION_END:
				gc.drawImage(_xitionBg, 0, 0);
				gc.drawImage(_to, 0, 0);
				endTransition(_from, _to, gc, _direction);
				break;

			default:
				break;
			}
		}
	}
}
