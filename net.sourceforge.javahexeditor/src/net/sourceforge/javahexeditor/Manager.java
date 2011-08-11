/*
 * javahexeditor, a java hex editor Copyright (C) 2006, 2009 Jordi Bergenthal, pestatije(-at_)users.sourceforge.net The
 * official javahexeditor site is sourceforge.net/projects/javahexeditor This program is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later version. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc.,
 * 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sourceforge.javahexeditor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.core.logging.utils.LogUtil;

/**
 * Manager of the javahexeditor application, either in its standalone or Eclipse plugin version. Manages creation of
 * widgets, and executes menu actions, like File->Save. Call createEditorPart() before any menu actions.
 *
 * @author Jordi
 */
public class Manager {

    class MySelectionAdapter extends SelectionAdapter {
        static final int ABOUT = 0;
        static final int PASTE = 1;
        static final int DELETE = 2;
        static final int SELECT_ALL = 3;
        static final int FIND = 4;
        static final int OPEN = 5;
        static final int SAVE = 6;
        static final int SAVE_AS = 7;
        static final int SAVE_SELECTION_AS = 8;
        static final int EXIT = 9;
        static final int CUT = 10;
        static final int COPY = 11;
        static final int GO_TO = 12;
        static final int GUIDELOCAL = 13;
        static final int GUIDEONLINE = 14;
        static final int NEW = 15;
        static final int PREFERENCES = 16;
        static final int REDO = 17;
        static final int TRIM = 18;
        static final int UNDO = 19;
        static final int UPDATE_POSITION_TEXT = 20;
        static final int SELECT_BLOCK = 21;
        int myAction = -1;

        MySelectionAdapter(int action) {
            myAction = action;
        }

        public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
            switch (myAction) {
                case ABOUT:
                    MessageBox box = new MessageBox(sShell, SWT.ICON_INFORMATION | SWT.OK);
                    box.setText("About javahexeditor");
                    box.setMessage("javahexeditor, copyright(c) 2006, 2009 Jordi Bergenthal.\n"
                            + "Released under the terms of the GNU General Public License.\n"
                            + "Visit http://sourceforge.net/projects/javahexeditor\n" + "\nContributions:\n"
                            + "Andre Bossert   Menus, dialogs, status bar.\n"
                            + "Alexander Kuramshin   Charset encoding.");
                    box.open();
                    break;
                case PASTE:
                    doPaste();
                    break;
                case DELETE:
                    doDelete();
                    break;
                case SELECT_ALL:
                    doSelectAll();
                    break;
                case FIND:
                    doFind();
                    break;
                case OPEN:
                    doOpen(null, false, null);
                    break;
                case SAVE:
                    doSave();
                    break;
                case SAVE_AS:
                    doSaveAs();
                    break;
                case SAVE_SELECTION_AS:
                    File aFile = showSaveAsDialog(sShell, true);
                    if (aFile != null && !doSaveSelectionAs(aFile))
                        showErrorBox(sShell);
                    break;
                case EXIT:
                    sShell.close();
                    sShell.dispose();
                    break;
                case CUT:
                    doCut();
                    break;
                case COPY:
                    doCopy();
                    break;
                case GO_TO:
                    doGoTo();
                    break;
                case GUIDELOCAL:
                    doOpenUserGuideUrl(false);
                    break;
                case GUIDEONLINE:
                    doOpenUserGuideUrl(true);
                    break;
                case NEW:
                    doOpen(null, true, null);
                    break;
                case PREFERENCES:
                    doPreferences();
                    break;
                case REDO:
                    doRedo();
                    break;
                case TRIM:
                    doTrim();
                    break;
                case UNDO:
                    doUndo();
                    break;
                case UPDATE_POSITION_TEXT:
                    if (statusLine != null) {
                        if (hexTexts != null) {
                            if (hexTexts.isSelected())
                                statusLine.updateSelectionValueText(hexTexts.getSelection(), hexTexts.getActualValue());
                            else
                                statusLine.updatePositionValueText(hexTexts.getCaretPos(), hexTexts.getActualValue());
                        } else {
                            statusLine.updatePositionValueText(0L, (byte) 0);
                        }
                    }
                    break;
                case SELECT_BLOCK:
                    doSelectBlock();
                    break;
                default:
                    break;
            }
        }
    }

    static final String applicationName = "javahexeditor";
    static final String fontExtension = ".font";
    static final String nameExtension = ".name";
    static final String propertiesExtension = ".properties";
    static final String sizeExtension = ".size";
    static final String styleExtension = ".style";
    static final String textCouldNotRead = "Could not read from saved file, try reopening the editor";
    static final String textCouldNotWriteOnFile = "Could not write on file ";
    static final String textFindReplace = "&Find/Replace...\tCtrl+F";
    static final String textIsBeingUsed = "\nis currently being used by the editor.\n" + "Cannot overwrite file.";
    static final String textTheFile = "The file ";
    static final String textErrorFatal = "Unexpected fatal error";
    static final String textErrorSave = "Save error";
    static final String textErrorOutOfMemory = "Out of memory error";

    BinaryContent content = null;
    @SuppressWarnings("rawtypes")
	private List findReplaceFindList = null;
    @SuppressWarnings("rawtypes")
	private List findReplaceReplaceList = null;
    FontData fontData = null; // when null uses default font
    Font fontText = null;
    File myFile = null;
    String lastErrorMessage = null;
    String lastErrorText = null;
    @SuppressWarnings("rawtypes")
	ArrayList listOfStatusChangedListeners = null;
    @SuppressWarnings("rawtypes")
	ArrayList listOfLongListeners = null;
    PreferencesManager preferences = null;

    // visual controls
    private Shell sShell = null; // @jve:decl-index=0:visual-constraint="70,45"
    private Menu menuBar = null;
    private MenuItem pushCut = null;
    private MenuItem pushCopy = null;
    private MenuItem pushDelete = null;
    private MenuItem pushFind = null;
    private MenuItem pushGoTo = null;
    private MenuItem pushPaste = null;
    private MenuItem pushPreferences = null;
    private MenuItem pushRedo = null;
    private MenuItem pushSave = null;
    private MenuItem pushSaveAs = null;
    private MenuItem pushSaveSelectionAs = null;
    private MenuItem pushSelectBlock = null;
    private MenuItem pushSelectAll = null;
    private MenuItem pushTrim = null;
    private MenuItem pushUndo = null;
    private Menu submenu = null;
    private Menu submenu1 = null;
    private Menu submenuHelp = null;
    // private HexFindDialog findDialog = null;
    private FindReplaceDialog findDialog = null;
    private GoToDialog goToDialog = null;
    private SelectBlockDialog selectBlockDialog = null;
    private HexTexts hexTexts = null;
    private StatusLine statusLine = null;
    private Composite textsParent = null;

    /**
     * Blocks the caller until the task is finished. Does not block the user interface thread.
     *
     * @param task independent of the user inteface thread (no widgets used)
     */
    public static void blockUntilFinished(Runnable task) {
        Thread thread = new Thread(task);
        thread.start();
        Display display = Display.getCurrent();
        final boolean[] pollerEnabled = {false};
        while (thread.isAlive() && !display.isDisposed()) {
            if (!display.readAndDispatch()) {
                // awake periodically so it returns when task has finished
                if (!pollerEnabled[0]) {
                    pollerEnabled[0] = true;
                    display.timerExec(300, new Runnable() {
                        public void run() {
                            pollerEnabled[0] = false;
                        }
                    });
                }
                display.sleep();
            }
        }
    }

    /**
     * Helper method to make a shell come closer to another shell
     *
     * @param fixedShell where movingShell will get closer to
     * @param movingShell shell to be relocated
     */
    public static void reduceDistance(Shell fixedShell, Shell movingShell) {
        Rectangle fixed = fixedShell.getBounds();
        Rectangle moving = movingShell.getBounds();
        int[] fixedLower = {fixed.x, fixed.y};
        int[] fixedHigher = {fixed.x + fixed.width, fixed.y + fixed.height};
        int[] movingLower = {moving.x, moving.y};
        int[] movingSpan = {moving.width, moving.height};

        for (int i = 0; i < 2; ++i) {
            if (movingLower[i] + movingSpan[i] < fixedLower[i])
                movingLower[i] = fixedLower[i] - movingSpan[i] + 10;
            else if (fixedHigher[i] < movingLower[i])
                movingLower[i] = fixedHigher[i] - 10;
        }
        movingShell.setLocation(movingLower[0], movingLower[1]);
    }

    /**
     * This method initializes composite
     */
    void createComposite() {
        GridLayout gridLayout = new GridLayout(1, true);
        gridLayout.marginHeight = 0;
        gridLayout.verticalSpacing = 3;
        gridLayout.marginBottom = 2;
        textsParent = new Composite(sShell, SWT.NONE);
        textsParent.setLayout(gridLayout);
        createEditorPart(textsParent);
        hexTexts.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        hexTexts.setEnabled(false);

        GridData statusParentGridData = new GridData();
        GC gc = new GC(textsParent);
        statusParentGridData.heightHint = gc.getFontMetrics().getHeight();
        gc.dispose();
        Composite statusParent = new Composite(textsParent, SWT.NONE);
        statusParent.setLayoutData(statusParentGridData);
        GridLayout statusParentGridLayout = new GridLayout();
        statusParentGridLayout.marginHeight = 0;
        statusParentGridLayout.marginWidth = 0;
        statusParent.setLayout(statusParentGridLayout);

        GridData statusLineGridData = new GridData();
        statusLineGridData.grabExcessVerticalSpace = true;
        statusLineGridData.verticalAlignment = SWT.FILL;
        createStatusPart(statusParent, false);
        statusLine.setLayoutData(statusLineGridData);

        DropTarget target = new DropTarget(textsParent, DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK);
        target.setTransfer(new Transfer[] {FileTransfer.getInstance(), TextTransfer.getInstance()});
        target.addDropListener(new DropTargetAdapter() {
            public void dragEnter(DropTargetEvent e) {
                if (e.detail == DND.DROP_NONE)
                    e.detail = DND.DROP_COPY;
            }

            public void drop(DropTargetEvent event) {
                if (event.data == null || ((String[]) event.data).length < 1) {
                    event.detail = DND.DROP_NONE;

                    return;
                }
                File file = new File(((String[]) event.data)[0]);
                if (!file.exists() || file.isDirectory() || !file.canRead()) {
                    event.detail = DND.DROP_NONE;
                    MessageBox box = new MessageBox(sShell, SWT.ICON_WARNING | SWT.OK);
                    box.setText("File error");
                    box.setMessage("Cannot open the file " + file);
                    box.open();
                } else {
                    doOpen(file, false, null);
                }
            }
        });
    }

    /**
     * Creates editor part of parent application. Can only be called once per Manager object.
     *
     * @param parent composite where the part will be drawn.
     * @exception IllegalStateException when editor part exists already (method called twice or more)
     * @exception NullPointerException if textsParent is null
     */
    @SuppressWarnings("rawtypes")
	public void createEditorPart(Composite parent) {
        if (hexTexts != null)
            throw new IllegalStateException("Editor part exists already");
        if (parent == null)
            throw new NullPointerException("Cannot use null parent");

        textsParent = parent;
        hexTexts = new HexTexts(textsParent, SWT.NONE);
        hexTexts.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                if (fontText != null && !fontText.isDisposed())
                    fontText.dispose();
            }
        });
        if (fontData != null) {
            fontText = new Font(Display.getCurrent(), fontData);
            hexTexts.setFont(fontText);
        }

        hexTexts.addLongSelectionListener(new MySelectionAdapter(MySelectionAdapter.UPDATE_POSITION_TEXT));
        hexTexts.addListener(SWT.Modify, new Listener() {
            public void handleEvent(Event event) {
                if (statusLine != null)
                    statusLine.updateInsertModeText(hexTexts == null ? true : !hexTexts.isOverwriteMode());
            }
        });

        if (listOfStatusChangedListeners != null) {
            for (Iterator i = listOfStatusChangedListeners.iterator(); i.hasNext();) {
                hexTexts.addListener(SWT.Modify, (Listener) i.next());
            }
            listOfStatusChangedListeners = null;
        }

        if (listOfLongListeners != null) {
            for (Iterator i = listOfLongListeners.iterator(); i.hasNext();) {
                hexTexts.addLongSelectionListener((SelectionListener) i.next());
            }
            listOfLongListeners = null;
        }
    }

    /**
     * This method initializes sShell
     */
    @SuppressWarnings("unused")
	private void createSShell() {
        sShell = new Shell(Display.getDefault(), SWT.MODELESS | SWT.SHELL_TRIM);
        InputStream stream = ClassLoader.getSystemResourceAsStream("icons/hex.png");
        if (stream != null) {
            try {
                final Image hexIcon = new Image(sShell.getDisplay(), stream);
                sShell.setImage(hexIcon);
                sShell.addDisposeListener(new DisposeListener() {
                    public void widgetDisposed(DisposeEvent e) {
                        hexIcon.dispose();
                    }
                });
            } catch (SWTException ex) {
            } finally {
                try {
                    stream.close();
                } catch (IOException ex) {
                }
            }
        }
        sShell.setText(applicationName);
        sShell.setLayout(new FillLayout());
        menuBar = new Menu(sShell, SWT.BAR);

        MenuItem submenuItem = new MenuItem(menuBar, SWT.CASCADE);
        submenuItem.setText("File");
        MenuItem submenuItem1 = new MenuItem(menuBar, SWT.CASCADE);
        submenuItem1.setText("Edit");
        MenuItem helpItem = new MenuItem(menuBar, SWT.CASCADE);
        helpItem.setText("Help");

        submenu1 = new Menu(submenuItem1);
        pushUndo = new MenuItem(submenu1, SWT.PUSH);
        pushUndo.setText("&Undo\tCtrl+Z");
        pushUndo.setEnabled(false);
        pushUndo.addSelectionListener(new MySelectionAdapter(MySelectionAdapter.UNDO));

        pushRedo = new MenuItem(submenu1, SWT.PUSH);
        pushRedo.setText("Red&o\tCtrl+Y");
        pushRedo.setEnabled(false);
        pushRedo.addSelectionListener(new MySelectionAdapter(MySelectionAdapter.REDO));

        new MenuItem(submenu1, SWT.SEPARATOR);
        pushCut = new MenuItem(submenu1, SWT.PUSH);
        pushCut.setText("Cu&t\tCtrl+X");
        pushCut.setEnabled(false);
        pushCut.addSelectionListener(new MySelectionAdapter(MySelectionAdapter.CUT));

        pushCopy = new MenuItem(submenu1, SWT.PUSH);
        pushCopy.setText("&Copy\tCtrl+C");
        pushCopy.setEnabled(false);
        pushCopy.addSelectionListener(new MySelectionAdapter(MySelectionAdapter.COPY));

        pushPaste = new MenuItem(submenu1, SWT.PUSH);
        pushPaste.setText("&Paste\tCtrl+V");
        pushPaste.setEnabled(false);
        pushPaste.addSelectionListener(new MySelectionAdapter(MySelectionAdapter.PASTE));

        new MenuItem(submenu1, SWT.SEPARATOR);
        pushDelete = new MenuItem(submenu1, SWT.PUSH);
        pushDelete.setText("&Delete\tDelete");
        pushDelete.setEnabled(false);
        pushDelete.addSelectionListener(new MySelectionAdapter(MySelectionAdapter.DELETE));

        pushTrim = new MenuItem(submenu1, SWT.PUSH);
        pushTrim.setText("T&rim");
        pushTrim.setEnabled(false);
        pushTrim.addSelectionListener(new MySelectionAdapter(MySelectionAdapter.TRIM));

        pushSelectAll = new MenuItem(submenu1, SWT.PUSH);
        pushSelectAll.setText("&Select All\tCtrl+A");
        pushSelectAll.setEnabled(false);
        pushSelectAll.addSelectionListener(new MySelectionAdapter(MySelectionAdapter.SELECT_ALL));

        pushSelectBlock = new MenuItem(submenu1, SWT.PUSH);
        pushSelectBlock.setText("Select &Block...\tCtrl+E");
        pushSelectBlock.setEnabled(false);
        pushSelectBlock.addSelectionListener(new MySelectionAdapter(MySelectionAdapter.SELECT_BLOCK));
        pushSelectBlock.setAccelerator(SWT.CONTROL | 'E');

        new MenuItem(submenu1, SWT.SEPARATOR);
        pushGoTo = new MenuItem(submenu1, SWT.PUSH);
        pushGoTo.setText("&Go to Location...\tCtrl+L");
        pushGoTo.setEnabled(false);
        pushGoTo.addSelectionListener(new MySelectionAdapter(MySelectionAdapter.GO_TO));
        pushGoTo.setAccelerator(SWT.CONTROL | 'L');

        pushFind = new MenuItem(submenu1, SWT.PUSH);
        pushFind.setText(textFindReplace);
        pushFind.setEnabled(false);
        pushFind.addSelectionListener(new MySelectionAdapter(MySelectionAdapter.FIND));
        pushFind.setAccelerator(SWT.CONTROL | 'F');

        new MenuItem(submenu1, SWT.SEPARATOR);
        pushPreferences = new MenuItem(submenu1, SWT.PUSH);
        pushPreferences.setText("Font Pr&eferences...");
        pushPreferences.setEnabled(true);
        pushPreferences.addSelectionListener(new MySelectionAdapter(MySelectionAdapter.PREFERENCES));

        submenu1.addMenuListener(new MenuAdapter() {
            public void menuShown(MenuEvent e) {
                boolean selected = isTextSelected();
                boolean lengthModifiable = selected && !isOverwriteMode();
                pushCopy.setEnabled(selected);
                pushCut.setEnabled(lengthModifiable);
                pushDelete.setEnabled(lengthModifiable);
                pushTrim.setEnabled(lengthModifiable);
                pushUndo.setEnabled(canUndo());
                pushRedo.setEnabled(canRedo());
            }
        });
        submenuItem1.setMenu(submenu1);

        submenu = new Menu(submenuItem);
        MenuItem pushNew = new MenuItem(submenu, SWT.PUSH);
        pushNew.setText("&New\tCtrl+N");
        pushNew.addSelectionListener(new MySelectionAdapter(MySelectionAdapter.NEW));
        pushNew.setAccelerator(SWT.CONTROL | 'N');
        MenuItem pushOpen = new MenuItem(submenu, SWT.PUSH);
        pushOpen.setText("&Open File...\tCtrl+O");
        pushOpen.addSelectionListener(new MySelectionAdapter(MySelectionAdapter.OPEN));
        pushOpen.setAccelerator(SWT.CONTROL | 'O');
        new MenuItem(submenu, SWT.SEPARATOR);
        pushSave = new MenuItem(submenu, SWT.PUSH);
        pushSave.setText("&Save\tCtrl+S");
        pushSave.setEnabled(false);
        pushSave.addSelectionListener(new MySelectionAdapter(MySelectionAdapter.SAVE));
        pushSave.setAccelerator(SWT.CONTROL | 'S');
        pushSaveAs = new MenuItem(submenu, SWT.PUSH);
        pushSaveAs.setText("Save &As...");
        pushSaveAs.setEnabled(false);
        pushSaveAs.addSelectionListener(new MySelectionAdapter(MySelectionAdapter.SAVE_AS));
        pushSaveSelectionAs = new MenuItem(submenu, SWT.PUSH);
        pushSaveSelectionAs.setText("Save S&election As...");
        pushSaveSelectionAs.setEnabled(false);
        pushSaveSelectionAs.addSelectionListener(new MySelectionAdapter(MySelectionAdapter.SAVE_SELECTION_AS));
        new MenuItem(submenu, SWT.SEPARATOR);
        MenuItem push5 = new MenuItem(submenu, SWT.PUSH);
        push5.setText("E&xit");
        push5.addSelectionListener(new MySelectionAdapter(MySelectionAdapter.EXIT));
        submenu.addMenuListener(new MenuAdapter() {
            public void menuShown(MenuEvent e) {
                pushSaveSelectionAs.setEnabled(isTextSelected());
            }
        });
        submenuItem.setMenu(submenu);

        submenuHelp = new Menu(helpItem);
        MenuItem pushOnlineGuide = new MenuItem(submenuHelp, SWT.PUSH);
        pushOnlineGuide.setText("&User Guide (Online)...");
        pushOnlineGuide.addSelectionListener(new MySelectionAdapter(MySelectionAdapter.GUIDEONLINE));
        MenuItem pushLocalGuide = new MenuItem(submenuHelp, SWT.PUSH);
        pushLocalGuide.setText("User Guide (&Local)...");
        pushLocalGuide.addSelectionListener(new MySelectionAdapter(MySelectionAdapter.GUIDELOCAL));
        new MenuItem(submenuHelp, SWT.SEPARATOR);
        MenuItem pushAbout = new MenuItem(submenuHelp, SWT.PUSH);
        pushAbout.setText("&About javahexeditor");
        pushAbout.addSelectionListener(new MySelectionAdapter(MySelectionAdapter.ABOUT));
        helpItem.setMenu(submenuHelp);

        sShell.setMenuBar(menuBar);
        createComposite();
        sShell.addListener(SWT.Close, new Listener() {
            public void handleEvent(Event e) {
                if (!doClose())
                    e.doit = false;
            }
        });
        addListener(new Listener() {
            public void handleEvent(Event event) {
                refreshTitleBar();
                pushSave.setEnabled(content.isDirty());
            }
        });
    }

    /**
     * Add a listener to changes of the 'dirty', 'insert/overwrite', 'selection' and 'canUndo/canRedo' status
     *
     * @param aListener the listener to be notified of changes
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void addListener(Listener aListener) {
        if (aListener == null)
            return;

        if (hexTexts == null) {
            if (listOfStatusChangedListeners == null)
                listOfStatusChangedListeners = new ArrayList();
            listOfStatusChangedListeners.add(aListener);
        } else {
            hexTexts.addListener(SWT.Modify, aListener);
        }
    }

    /**
     * Adds a long selection listener. Events sent to the listener have long start and end points.
     *
     * @see HexTexts.addLongSelectionListener(SelectionListener)
     * @param listener the listener
     * @see StyledText#addSelectionListener(org.eclipse.swt.events.SelectionListener)
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void addLongSelectionListener(SelectionListener listener) {
        if (listener == null)
            throw new IllegalArgumentException();

        if (hexTexts == null) {
            if (listOfLongListeners == null)
                listOfLongListeners = new ArrayList();
            listOfLongListeners.add(listener);
        } else {
            hexTexts.addLongSelectionListener(listener);
        }
    }

    /**
     * Tells whether the last action can be redone
     *
     * @return true: an action ca be redone
     */
    public boolean canRedo() {
        return hexTexts != null && hexTexts.canRedo();
    }

    /**
     * Tells whether the last action can be undone
     *
     * @return true: an action ca be undone
     */
    public boolean canUndo() {
        return hexTexts != null && hexTexts.canUndo();
    }

    /**
     * Creates status part of parent application.
     *
     * @param aParent composite where the part will be drawn
     * @exception NullPointerException if aParent is null
     * @param aParent
     * @param withLeftSeparator so it can be put besides other status items (for plugin)
     */
    public void createStatusPart(Composite aParent, boolean withLeftSeparator) {
        if (aParent == null)
            throw new NullPointerException("Cannot use null parent");

        statusLine = new StatusLine(aParent, SWT.NONE, withLeftSeparator);
        if (hexTexts != null && hexTexts.getEnabled()) {
            statusLine.updateInsertModeText(!hexTexts.isOverwriteMode());
            if (hexTexts.isSelected())
                statusLine.updateSelectionValueText(hexTexts.getSelection(), hexTexts.getActualValue());
            else
                statusLine.updatePositionValueText(hexTexts.getCaretPos(), hexTexts.getActualValue());
        }
    }

    boolean doClose() {
        if (content == null || !content.isDirty())
            return true;

        MessageBox box = new MessageBox(sShell, SWT.ICON_WARNING | SWT.YES | SWT.NO | SWT.CANCEL);
        box.setText("Modified file");
        box.setMessage("The current file has been modified.\nSave changes?");
        int result = box.open();
        if (result == SWT.CANCEL)
            return false;
        if (result == SWT.YES)
            return doSave();

        return true;
    }

    /**
     * Copies selection into clipboard
     */
    public void doCopy() {
        if (hexTexts == null)
            return;

        hexTexts.copy();
    }

    /**
     * Cuts selection into clipboard
     */
    public void doCut() {
        if (hexTexts == null)
            return;

        hexTexts.cut();
    }

    /**
     * While in insert mode, deletes the selection
     */
    public void doDelete() {
        hexTexts.deleteSelected();
    }

    /**
     * Open find dialog
     */
    @SuppressWarnings("rawtypes")
	public void doFind() {
        if (findDialog == null) {
            findDialog = new FindReplaceDialog(textsParent.getShell());
            if (findReplaceFindList == null) {
                findReplaceFindList = new ArrayList();
                findReplaceReplaceList = new ArrayList();
            }
            findDialog.setFindReplaceLists(findReplaceFindList, findReplaceReplaceList);
        }
        findDialog.setTarget(hexTexts);
        findDialog.open();
    }

    /**
     * Open 'go to' dialog
     */
    public void doGoTo() {
        if (content.length() < 1L)
            return;

        if (goToDialog == null)
            goToDialog = new GoToDialog(textsParent.getShell());

        long location = goToDialog.open(content.length() - 1L);
        if (location >= 0L) {
            long button = goToDialog.getButtonPressed();
            if (button == 1)
                hexTexts.showMark(location);
            else
                hexTexts.selectBlock(location, location);
        }
    }

    /**
     * Open 'select block' dialog
     */
    public void doSelectBlock() {
        if (content.length() < 1L)
            return;

        if (selectBlockDialog == null)
            selectBlockDialog = new SelectBlockDialog(textsParent.getShell());
        long start = selectBlockDialog.open(hexTexts.getSelection(), content.length() - 1L);
        long end = selectBlockDialog.getFinalEndResult();
        if ((start >= 0L) && (end >= 0L) && (start != end)) {
            hexTexts.selectBlock(start, end);
        }
    }

    void doOpen(File forceThisFile, boolean isNewFile, String charset) {
        if (!doClose())
            return;

        if (forceThisFile == null && !isNewFile) {
            String fileName = new FileDialog(sShell, SWT.OPEN).open();
            if (fileName == null)
                return;
            forceThisFile = new File(fileName);
        }
        if (forceThisFile != null) {
            try {
                forceThisFile = forceThisFile.getCanonicalFile();
            } catch (IOException e) {
            } // use non-canonical one then
        }
        hexTexts.setEnabled(true);
        pushFind.setEnabled(true);
        pushGoTo.setEnabled(true);
        pushPaste.setEnabled(true);
        pushSelectAll.setEnabled(true);
        pushSelectBlock.setEnabled(true);
        pushSaveAs.setEnabled(true);
        try {
            openFile(forceThisFile, charset);
        } catch (IOException e) {
            MessageBox box = new MessageBox(sShell, SWT.ICON_ERROR | SWT.OK);
            box.setText("File Read Error");
            box.setMessage("The file " + forceThisFile + "\n cannot be opened for reading.");
            box.open();
        }

        hexTexts.setFocus();
    }

    void doOpenUserGuideUrl(boolean online) {
        Program browser = Program.findProgram("html");
        if (browser == null) {
            MessageBox box = new MessageBox(sShell, SWT.ICON_WARNING | SWT.OK);
            box.setText("Browser not found");
            box.setMessage("Could not find a browser program to open html files.\n"
                    + "Visit javahexeditor.sourceforge.net/userGuide.html to see the User Guide.\n");
            box.open();
            return;
        }

        String fileName = "userGuide.html";
        if (online) {
            fileName = "javahexeditor.sourceforge.net/" + fileName;
        } else {
            File theFile = new File(fileName);
            if (!theFile.exists()) {
                InputStream inStream = ClassLoader.getSystemResourceAsStream(fileName);
                if (inStream != null) {
                    try {
                        FileOutputStream outStream = new FileOutputStream(theFile);
                        byte[] buffer = new byte[512];
                        int read = 0;
                        try {
                            while ((read = inStream.read(buffer)) > 0) {
                                outStream.write(buffer, 0, read);
                            }
                        } finally {
                            outStream.close();
                        }
                    } catch (IOException e) {
                        // Open browser anyway
                    }
                    try {
                        inStream.close();
                    } catch (IOException e) {
                        // Open browser anyway
                    }
                }
            }
        }
        browser.execute(fileName);
    }

    /**
     * Pastes clipboard into editor
     */
    public void doPaste() {
        if (hexTexts == null)
            return;

        hexTexts.paste();
    }

    void doPreferences() {
        if (preferences == null) {
            preferences = new PreferencesManager(fontData == null ? HexTexts.fontDataDefault : fontData);
        }
        if (preferences.openDialog(textsParent.getShell()) == SWT.OK) {
            setTextFont(preferences.getFontData());
            writeNonDefaultFont();
        }
    }

    boolean doSave() {
        if (myFile == null) {
            return doSaveAs();
        } else if (!saveFile()) {
            showErrorBox(sShell);
            return false;
        }

        return true;
    }

    boolean doSaveAs() {
        File file = showSaveAsDialog(sShell, false);
        if (file == null)
            return false;

        if (!saveAsFile(file)) {
            showErrorBox(sShell);
            return false;
        }

        return true;
    }

    /**
     * Perform save-selected-as action on selected data
     *
     * @return whether the action was successful
     */
    public boolean doSaveSelectionAs(File theFile) {
        if (isFileBeingRead(theFile)) {
            lastErrorText = textErrorSave;
            lastErrorMessage = textTheFile + theFile + textIsBeingUsed;
            return false;
        }

        long[] startAndEnd = hexTexts.getSelection();
        try {
            content.get(theFile, startAndEnd[0], startAndEnd[1] - startAndEnd[0]);
        } catch (IOException e) {
            lastErrorText = textErrorSave;
            lastErrorMessage = textCouldNotWriteOnFile + theFile;

            return false;
        }

        return true;
    }

    /**
     * Selects all file contents in editor
     */
    public void doSelectAll() {
        if (hexTexts == null)
            return;

        hexTexts.selectAll();
    }

    /**
     * Redoes the last undone action
     */
    public void doRedo() {
        hexTexts.redo();
    }

    /**
     * While in insert mode, trims the selection
     */
    public void doTrim() {
        hexTexts.deleteNotSelected();
    }

    /**
     * Undoes the last action
     */
    public void doUndo() {
        hexTexts.undo();
    }

    /**
     * Get the binary content
     *
     * @return the content being edited
     */
    public BinaryContent getContent() {
        return content;
    }

    /**
     * Get error message from last unsuccessful operation
     */
    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    String getLastErrorText() {
        return lastErrorText;
    }

    /**
     * @see HexTexts#getSelection()
     */
    public long[] getSelection() {
        if (hexTexts == null) {
            return new long[] {0L, 0L};
        }

        return hexTexts.getSelection();
    }

    /**
     * Get whether the content has been modified or not
     *
     * @return if changes have been performed
     */
    public boolean isDirty() {
        if (content == null)
            return false;

        return content.isDirty();
    }

    boolean isFileBeingRead(File aFile) {
        return aFile.equals(myFile) || content.getOpenFiles().contains(aFile);
    }

    /**
     * Tells whether the input is in overwrite or insert mode
     *
     * @return true: overwriting, false: inserting
     */
    public boolean isOverwriteMode() {
        if (hexTexts == null)
            return true;

        return hexTexts.isOverwriteMode();
    }

    /**
     * Tells whether the input has text selected
     *
     * @return true: text is selected, false: no text selected
     */
    public boolean isTextSelected() {
        if (hexTexts == null)
            return false;

        long[] selection = hexTexts.getSelection();

        return selection[0] != selection[1];
    }

    /**
     * Open file for editing
     *
     * @param aFile the file to be edited
     * @throws IOException when a file has no read access
     */
    public void openFile(File aFile, String charset) throws IOException {
        content = new BinaryContent(aFile); // throws IOException
        myFile = aFile;
        hexTexts.setCharset(charset);
        hexTexts.setContentProvider(content);
    }

    void readNonDefaultFont() {
        Properties properties = new Properties();
        try {
            FileInputStream file = new FileInputStream(applicationName + propertiesExtension);
            properties.load(file);
            file.close();
        } catch (IOException e) {
            return;
        }

        String name = properties.getProperty(applicationName + fontExtension + nameExtension);
        if (name == null)
            return;

        String styleString = properties.getProperty(applicationName + fontExtension + styleExtension);
        if (styleString == null)
            return;
        int style = PreferencesManager.fontStyleToInt(styleString);

        int size = 0;
        try {
            size = Integer.parseInt(properties.getProperty(applicationName + fontExtension + sizeExtension));
        } catch (NumberFormatException e) {
            return;
        }

        fontData = new FontData(name, size, style);
    }

    void refreshTitleBar() {
        StringBuffer title = new StringBuffer();
        if (myFile != null) {
            if (content.isDirty())
                title.append('*');
            title.append(myFile.getName()).append(" - ");
        }
        title.append(applicationName);
        sShell.setText(title.toString());
    }

    /**
     * Reuse the status line control from another manager. Useful for multiple open editors
     *
     * @param other manager to copy its control from
     */
    public void reuseStatusControlFrom(Manager other) {
        statusLine = other.statusLine;
    }

    /**
     * Perform save-as action on opened file
     *
     * @return whether the action was successful
     */
    public boolean saveAsFile(File theFile) {
        if (theFile.equals(myFile))
            return saveFile();

        if (isFileBeingRead(theFile)) {
            lastErrorText = textErrorSave;
            lastErrorMessage = textTheFile + theFile + textIsBeingUsed;
            return false;
        }

        boolean successful = true;
        String errorMessage = textCouldNotWriteOnFile + theFile;
        try {
            content.get(theFile);
            content.dispose();
            content = new BinaryContent();
            myFile = null;
            errorMessage = textCouldNotRead;
            content = new BinaryContent(theFile);
            myFile = theFile;
        } catch (IOException e) {
            successful = false;
            lastErrorText = textErrorSave;
            lastErrorMessage = errorMessage;
        }
        hexTexts.setContentProvider(content);

        return successful;
    }

    /**
     * Perform save action on opened file
     *
     * @return whether the action was successful
     */
    public boolean saveFile() {
        boolean successful = false;
        String errorMessage = "Could not create temporary file with a unique name";
        File tempFile = null;
        // It can happen that in two successive "Save File"'s the first one didn't get the temp file
        // deleted due to limitations in the os (windows). With this loop it's possible to save many times
        for (int tries = 9999; tries >= 0 && !successful; --tries) {
            try {
                // + "99" is to avoid IllegalArgumentException
                tempFile = File.createTempFile(myFile.getName() + "99", "" + tries, myFile.getParentFile());
                successful = true;
            } catch (IOException e) {
                LogUtil.logError(e);
            }
        }
        if (successful) {
            successful = false;
            try {
                errorMessage = "Could not write on temp file " + tempFile;
                content.get(tempFile);
                content.dispose();
                content = new BinaryContent();
                errorMessage = "Could not overwrite file " + myFile.getName()
                        + ", a temporary copy can be found in file " + tempFile.getAbsolutePath();
                BinaryClipboard.deleteFileALaMs(myFile);
                if (tempFile.renameTo(myFile)) { // successful delete or not try renaming anyway
                    errorMessage = textCouldNotRead;
                    content = new BinaryContent(myFile);
                    successful = true;
                }
            } catch (IOException e) {
                // error handling below
            }
            hexTexts.setContentProvider(content);
        }
        if (!successful) {
            lastErrorText = textErrorSave;
            lastErrorMessage = errorMessage;
        }

        return successful;
    }

    /**
     * Set Find/Replace combo lists pre-exisitng values
     *
     * @param findList previous find values
     * @param replaceList previous replace values
     */
    @SuppressWarnings("rawtypes")
	public void setFindReplaceLists(List findList, List replaceList) {
        findReplaceFindList = findList;
        findReplaceReplaceList = replaceList;
    }

    /**
     * Causes the text areas to have the keyboard focus
     */
    public void setFocus() {
        if (hexTexts != null)
            hexTexts.setFocus();

        if (statusLine != null) {
            statusLine.updateInsertModeText(hexTexts == null ? true : !hexTexts.isOverwriteMode());
            if (hexTexts != null) {
                if (hexTexts.isSelected())
                    statusLine.updateSelectionValueText(hexTexts.getSelection(), hexTexts.getActualValue());
                else
                    statusLine.updatePositionValueText(hexTexts.getCaretPos(), hexTexts.getActualValue());
            } else {
                statusLine.updatePositionValueText(0L, (byte) 0);
            }
        }
    }

    /**
     * Delegates to HexTexts.setSelection(start, end)
     *
     * @see HexTexts#setSelection(long, long)
     */
    public void setSelection(long start, long end) {
        hexTexts.setSelection(start, end);
    }

    /**
     * Set the editor text font.
     *
     * @param aFont new font to be used; should be a constant char width font. Use null to set to the default font.
     */
    public void setTextFont(FontData aFont) {
        fontData = aFont;
        if (HexTexts.fontDataDefault.equals(fontData))
            fontData = null;
        // dispose it after setting new one (StyledTextRenderer 3.448 bug in line 994)
        Font fontToDispose = fontText;
        fontText = null;
        if (hexTexts != null) {
            if (fontData != null)
                fontText = new Font(Display.getCurrent(), fontData);
            hexTexts.setFont(fontText);
        }
        if (fontToDispose != null && !fontToDispose.isDisposed())
            fontToDispose.dispose();
    }

    /**
     * Show an error box with the last error message
     *
     * @param aShell parent of the message box
     */
    public void showErrorBox(Shell aShell) {
        MessageBox aMessageBox = new MessageBox(aShell, SWT.ICON_ERROR | SWT.OK);
        aMessageBox.setText(getLastErrorText());
        aMessageBox.setMessage(getLastErrorMessage());
        aMessageBox.open();
    }

    /**
     * Show a file dialog with a save-as message
     *
     * @param aShell parent of the dialog
     */
    public File showSaveAsDialog(Shell aShell, boolean selection) {
        FileDialog dialog = new FileDialog(aShell, SWT.SAVE);
        if (selection)
            dialog.setText("Save Selection As");
        else
            dialog.setText("Save As");
        String fileText = dialog.open();
        if (fileText == null)
            return null;

        File file = new File(fileText);
        if (file.exists() && !showMessageBox(aShell, fileText))
            return null;

        return file;
    }

    /**
     * Show a message box with a file-already-exists message
     *
     * @param aShell parent of the dialog
     */
    public boolean showMessageBox(Shell aShell, String file) {
        MessageBox aMessageBox = new MessageBox(aShell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
        aMessageBox.setText("File already exists");
        aMessageBox.setMessage("The file " + file + " already exists.\nOverwrite file?");

        return aMessageBox.open() == SWT.YES;
    }

    void writeNonDefaultFont() {
        File propertiesFile = new File(applicationName + propertiesExtension);
        if (fontData == null) {
            if (propertiesFile.exists())
                propertiesFile.delete();
            return;
        }

        Properties properties = new Properties();
        properties.setProperty(applicationName + fontExtension + nameExtension, fontData.getName());
        properties.setProperty(applicationName + fontExtension + styleExtension, PreferencesManager.fontStyleToString(fontData.getStyle()));
        properties.setProperty(applicationName + fontExtension + sizeExtension, Integer.toString(fontData.getHeight()));
        try {
            FileOutputStream stream = new FileOutputStream(propertiesFile);
            properties.store(stream, null);
            stream.close();
        } catch (IOException e) {
        }
    }
}
