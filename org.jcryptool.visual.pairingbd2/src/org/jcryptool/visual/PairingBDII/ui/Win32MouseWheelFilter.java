package org.jcryptool.visual.PairingBDII.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;

/**
 * The standard platform behavior on Windows is to scroll the widget with
 * keyboard focus when the user turns the mouse wheel, instead of the widget
 * currently under the mouse pointer. Many consider this annoying and Windows
 * itself, as well as many popular Windows software, breaks this rule and
 * implements the behavior seen on other platforms, which is to scroll the
 * widget under the mouse.
 * 
 * Win32MouseWheelFilter is a Listener implementation which will filter for
 * SWT.MouseWheel events delivered to any Widget and try to redirect the event
 * to the widget under the mouse or one of it's parents. The widget, or one of
 * it's parents is considered a suitable target, if it either has Listeners for
 * SWT.MouseWheel attached (assuming that those listeners would do something
 * sensible with the event), or if its style bits contain SWT.H_SCROLL and/or
 * SWT.V_SCROLL. In the later case a low level system event is generated, which
 * is necessary to get the event handled by the native ScrollBar widgets. A
 * vertical ScrollBar is preferred as the target, unless it is for some reason
 * unsuitable for scrolling. In that case, horizontal scrolling would take
 * place, if there is a suitable horizontal ScrollBar.
 * 
 * Simply creating a new Win32MouseWheelFilter instance will install it as an
 * event filter in the Display passed to the constructor. At an appropriate
 * time, you may call dispose() to remove the filter again. On SWT platforms
 * other than "win32", constructing an Win32MouseWheelFilter will have no effect.
 */
public class Win32MouseWheelFilter implements Listener {

    private final Display   fDisplay;

    private int             WM_VSCROLL;
    private int             WM_HSCROLL;
    private int             SB_LINEUP;
    private int             SB_LINEDOWN;

    private Method          fSendEventMethod32;
    private Method          fSendEventMethod64;

    /**
     * Creates a new Win32MouseWheelFilter instance and registers it as global
     * event filter in the provided Display. Nothing will happen if the SWT
     * platform is not "win32". If for some reason some SWT internals have
     * changed since the writing of this class, and the Reflection-based
     * extraction of some win32 specific fields of the SWT OS class fails,
     * no filtering of wheel events will take place either.
     * 
     * @param display
     *      The Display instance that the Win32MouseWheelFilter should install
     *      itself into as global event filter.
     */
    public Win32MouseWheelFilter(Display display) {
        fDisplay = display;

        if (!SWT.getPlatform().equals("win32"))
            return;

        try {
            Class<?> os = Class.forName("org.eclipse.swt.internal.win32.OS");
            WM_VSCROLL = os.getDeclaredField("WM_VSCROLL").getInt(null);
            WM_HSCROLL = os.getDeclaredField("WM_HSCROLL").getInt(null);
            SB_LINEUP = os.getDeclaredField("SB_LINEUP").getInt(null);
            SB_LINEDOWN = os.getDeclaredField("SB_LINEDOWN").getInt(null);

            try {
                // Try the 32-bit version first
                fSendEventMethod32 = os.getDeclaredMethod("SendMessage",
                    int.class, int.class, int.class, int.class);
            } catch (NoSuchMethodException e) {
                // Fall back to the 64-bit version
                fSendEventMethod64 = os.getDeclaredMethod("SendMessage",
                    long.class, int.class, long.class, long.class);
            }

            display.addFilter(SWT.MouseWheel, this);
            return;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        System.out.println("Warning: Running on win32 SWT platform, "
            + "but unable to install Win32MouseWheelFilter filter.");
    }

    /**
     * If the receiver had previously installed itself as global event filter,
     * this method will remove it again from the display's filters.
     */
    public final void dispose() {
        fDisplay.removeFilter(SWT.MouseWheel, this);
    }

    public final void handleEvent(Event event) {
        Control cursorControl = event.display.getCursorControl();
        if (event.widget == cursorControl || cursorControl == null)
            return;

        if (event.widget instanceof Control) {
            // If the original target control's bounds contain the mouse
            // location, do not re-target the event, since it may indeed be the
            // Control that needs to handle scrolling for an embedded Control
            // that has focus.
            Control control = (Control) event.widget;
            Rectangle bounds = control.getBounds();
            bounds.x = 0;
            bounds.y = 0;
            Point cursorPos = control.toControl(Display.getCurrent().getCursorLocation());
            if (bounds.contains(cursorPos))
                return;
        }

        // Try to find the best target widget for the event, based on the
        // cursorControl. A suitable target control is either one that has
        // a listener for SWT.MouseWheel attached, or one that has either
        // SWT.H_SCROLL or SWT.V_SCROLL in its style bits.
        Control wheelControl = cursorControl;
        int scrollStyle = SWT.H_SCROLL | SWT.V_SCROLL;
        while (wheelControl != null
            && (wheelControl.getStyle() & scrollStyle) == 0
            && wheelControl.getListeners(SWT.MouseWheel).length == 0) {
            wheelControl = wheelControl.getParent();
        }
        if (wheelControl == null) {
            // The event would not be handled by anyone, bail out.
            return;
        }

        int style = wheelControl.getStyle();

        if ((style & scrollStyle) != 0 && wheelControl instanceof Scrollable) {
            // Construct the data for the low level event based on which
            // direction the target can scroll in. We need to use a low-level
            // event since otherwise it won't be handled by the native
            // ScrollBar widgets.
            int msg;

            // Prefer vertical scrolling. However, if the
            // there is no vertical ScrollBar, or if it's somehow disabled,
            // then switch to horizontal scrolling instead.
            if ((style & SWT.V_SCROLL) != 0 ) {
                ScrollBar vBar = ((Scrollable) wheelControl).getVerticalBar();
                if (vBar == null
                    || ((vBar.getMinimum() == 0
                        && vBar.getMaximum() == 0
                        && vBar.getSelection() == 0)
                            || !vBar.isEnabled()
                            || !vBar.isVisible())) {
                    // There is no vertical ScrollBar, or it can't be used.
                    msg = WM_HSCROLL;
                } else
                    msg = WM_VSCROLL;
            } else {
                msg = WM_HSCROLL;
            }

            int count = event.count;
            int wParam = SB_LINEUP;
            if (event.count < 0) {
                count = -count;
                wParam = SB_LINEDOWN;
            }

            try {
                // Obtain the control's handle via Reflection and
                // deliver the event using the low level platform method.
                // (64 and 32 bit versions)
                if (fSendEventMethod32 != null) {
                    int handle = org.eclipse.swt.widgets.Control.class
                        .getDeclaredField("handle").getInt(wheelControl);
                    for (int i = 0; i < count; i++)
                        fSendEventMethod32.invoke(null, handle, msg, wParam, 0);
                } else {
                    long handle = org.eclipse.swt.widgets.Control.class
                        .getDeclaredField("handle").getLong(wheelControl);
                    for (int i = 0; i < count; i++)
                        fSendEventMethod64.invoke(null, handle, msg, wParam, 0);
                }

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        } else {
            // It makes no sense using the low-level OS event delivery, since
            // Widgets without the scrolling style bits won't receive this
            // event. Since we selected this widget based on the fact that it
            // has SWT.MouseWheel listeners attached, use the regular SWT event
            // notification system.

            // Convert mouse location, since the event contains it in the wrong
            // coordinate space (the one of the original event target).
            Point cursorPos = wheelControl.toControl(
                event.display.getCursorLocation());
            event.x = cursorPos.x;
            event.y = cursorPos.y;

            event.widget = wheelControl;
            wheelControl.notifyListeners(event.type, event);
        }

        // We re-targeted the event, or re-posted a new event to another widget,
        // so prevent this event from being processed any further.
        event.type = SWT.None;
        event.doit = false;
    }
}