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

package org.eclipse.nebula.effects.stw.transitions;

import org.eclipse.nebula.effects.stw.Transition;
import org.eclipse.nebula.effects.stw.TransitionManager;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

/**
 * Applies a slide effect. The <i>from</i> control slides out and the the <i>to</i>
 * control slides in smoothly accelerating up then down after a while until it stops.
 *  
 * @author Ahmed Mahran (ahmahran@gmail.com)
 */
public class SlideTransition extends org.eclipse.nebula.effects.stw.Transition {

    private int _w, _halfW, _h, _halfH;
    private double _a, _x, _y, _x0, _y0, _v0;
    private boolean _flag1;
    private ImageData _fromData;
    private long _halfT, _t1;
    
    /**
     * This constructor creates a SlideTransition with number of frames per second of {@link Transition#DEFAULT_FPS}
     * and total transition time of {@link Transition#DEFAULT_T} milliseconds. It is similar to 
     * new SlideTransition(transitionManager, {@link Transition#DEFAULT_FPS}, {@link Transition#DEFAULT_T})
     * 
     * @param transitionManager the transition manager to be used to manage transitions
     */
    public SlideTransition(TransitionManager transitionManager) {
        this(transitionManager, DEFAULT_FPS, DEFAULT_T);
    }
    
    /**
     * This constructor creates a SlideTransition with <i>fps</i> number of frames per
     * second and <i>T</i> total transition time in milliseconds.
     * 
     * @param transitionManager the transition manager to be used to manage transitions 
     * @param fps number of frames per second
     * @param T the total time the transition effect will take in milliseconds
     */
    public SlideTransition(TransitionManager transitionManager, long fps, long T) {
        super(transitionManager, fps, T);
    }
    
    @Override
    protected void initTransition(Image from, Image to, GC gc, double direction) {
        
        _halfT = (long) (_T / 2.0);
        _fromData = from.getImageData();
        switch((int)direction) {
        
        case (int)DIR_RIGHT:
        case (int)DIR_LEFT:
            _w = _fromData.width;
            _halfW = (int) (_w / 2.0);
            _a = _w / (double)(_halfT * _halfT);
            _x = 0;
            //_factor = direction == Transition.DIR_RIGHT ? 1 : -1;
            break;
        
        case (int)DIR_UP:
        case (int)DIR_DOWN:
            _h = _fromData.height;
            _halfH = (int) (_h / 2.0);
            _a = _h / (double)(_halfT * _halfT);
            _y = 0;
            //_factor = direction == Transition.DIR_DOWN ? 1 : -1;
            break;
        
        }
        
        _flag1 = false;
        
    }
    

    @Override
    protected void stepTransition(long t, Image from, Image to, GC gc,
            double direction) {
        
        switch((int)direction) {
        
        case (int)DIR_RIGHT:
            
            gc.drawImage(from, (int)_x, 0);
            gc.drawImage(to, (int)(_x - _w), 0);
            
            if( t <= _halfT ) {
                
                _x = Math.min(0.5 * _a * t * t, _halfW);
                
            } else {
                
                if(!_flag1) {
                    
                    _x0 = _x;
                    _v0 = _a * t;
                    _a *= -1.0;
                    _flag1 = true;
                    
                }
                
                _t1 = t - _halfT;
                _x = Math.min(_x0 + _v0 * _t1 + 0.5 * _a * _t1 * _t1, _w);
                
            }
            break;
            
        case (int)DIR_LEFT:
            
            gc.drawImage(from, (int)_x, 0);
            gc.drawImage(to, (int)(_x + _w), 0);
        
            if( t <= _halfT ) {
                
                _x = Math.max(-0.5 * _a * t * t, -_halfW);
                
            } else {
                
                if(!_flag1) {
                    
                    _x0 = _x;
                    _v0 = _a * t;
                    _a *= -1.0;
                    _flag1 = true;
                    
                }
                
                _t1 = t - _halfT;
                _x = Math.max(_x0 - _v0 * _t1 - 0.5 * _a * _t1 * _t1, -_w);
                
            }
            break;
        
        case (int)DIR_UP:
            
            gc.drawImage(from, 0, (int)_y);
            gc.drawImage(to, 0, (int)(_y + _h));
            
            if( t <= _halfT ) {
                
                _y = Math.max(-0.5 * _a * t * t, -_halfH);
                
            } else {
                
                if(!_flag1) {
                    
                    _y0 = _y;
                    _v0 = _a * t;
                    _a *= -1.0;
                    _flag1 = true;
                    
                }
                
                _t1 = t - _halfT;
                _y = Math.max(_y0 - _v0 * _t1 - 0.5 * _a * _t1 * _t1, -_h);
                
            }
            break;
        
        case (int)DIR_DOWN:
            
            gc.drawImage(from, 0, (int)_y);
            gc.drawImage(to, 0, (int)(_y - _h));
            
            if( t <= _halfT ) {
                
                _y = Math.min(0.5 * _a * t * t, _halfH);
                
            } else {
                
                if(!_flag1) {
                    
                    _y0 = _y;
                    _v0 = _a * t;
                    _a *= -1.0;
                    _flag1 = true;
                    
                }
                
                _t1 = t - _halfT;
                _y = Math.min(_y0 + _v0 * _t1 + 0.5 * _a * _t1 * _t1, _h);
                
            }
            break;
        
        }
        
    }

    @Override
    protected void endTransition(Image from, Image to, GC gc, double direction) {
    }
    
    
}
