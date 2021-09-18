package org.jcryptool.devtools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.Workbench;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.core.util.directories.DirectoryService;

public class EditorAPIStartup implements IStartup {

	public class EditorListener implements Observer {

		@Override
		public void update(Observable o, Object arg) {
			// TODO Auto-generated method stub

			List<IEditorReference> editorlist = manager.getEditorReferences();
			if (! editorlist.isEmpty()) {
				IEditorReference first = editorlist.get(0);
// 				System.out.println(first.getTitle());
				IEditorPart firstPart = first.getEditor(false);
				byte[] content = manager.getContentAsBytes(firstPart);
// 				System.out.println(HexEditorDebugLogic.bytesToString(content));
			}
		}

	}

	private Observable observable;
	private Observer observer;
	private EditorsManager manager;
	private File outputDir;
	protected Display currentDisplay = Display.getCurrent();

	
	private static void rmdir(String path) {
		String cmd = "rm -rf " + path;
		Runtime run = Runtime.getRuntime();
		try {
			Process pr = run.exec(cmd);
			pr.waitFor();
			BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line = "";
			while ((line=buf.readLine())!=null) {
				System.err.println(line);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void putBytes(String path, byte[] content) {
		try {
			Files.write(Path.of(path), content, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private void putString(String path, String content) {
		try {
			Files.writeString(Path.of(path), content, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	protected Map<String, Boolean> inProcessRequests = new HashMap<>();
	protected List<String> listRequestDirs() {
		var result = new LinkedList<String>();
		for (File file: outputDir.listFiles()) {
			if (file.isDirectory()) {
				result.add(file.getAbsolutePath());
			}
		}
		return result;
	}
	
	private void runner(int seqIdx) {
		
		var requestDirs = listRequestDirs();
		List<String> unprocessed = requestDirs.stream().filter(d -> ! (inProcessRequests.getOrDefault(d, false))).collect(Collectors.toList());
		unprocessed.forEach(d -> inProcessRequests.put(d, true));
		int processed = 0;
		for (String unprocessedRequest: unprocessed) {
			String currentDirPath = unprocessedRequest;
			System.out.println(String.format("handling request %s", currentDirPath));

			List<IEditorReference> editorlist = manager.getEditorReferences();
			for(IEditorReference editorRef: editorlist) {
				var title = editorRef.getTitle();
				IEditorPart part = editorRef.getEditor(false);
				byte[] content = manager.getContentAsBytes(part);
				putBytes(currentDirPath + "/" + title, content);
			}
			putString(currentDirPath + "/finished.txt", "yes");
			processed++;
		}
// 		System.out.println("run finished; processed dirs: " + processed);
	}
	
	@Override
	public void earlyStartup() {
		// TODO Auto-generated method stub

		manager = EditorsManager.getInstance();
		observable = manager.getEditorAvailabilityObservable();
		this.observer = new EditorAPIStartup.EditorListener();
		
		outputDir = new File(DirectoryService.getWorkspaceDir() + "/editors");
		if (outputDir.exists()) {
			rmdir(outputDir.getAbsolutePath());
		}
		outputDir.mkdir();
		try {
			Files.writeString(Path.of("/home/snuc/jct-current-ws.txt"), outputDir.getAbsolutePath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
//		observable.addObserver(this.observer);

		(new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (!Thread.interrupted())
                    try
                    {
                        Thread.sleep(1000);
                        if (currentDisplay == null) {
                        	System.out.println("attempting to acquire display...");
                        	
                        	var currentWorkbench = Workbench.getInstance().getWorkbenchWindows();
                        	if (currentWorkbench != null) {
                        		if (currentWorkbench.length > 0) {
									var newCurrentDisplay = currentWorkbench[0].getShell().getDisplay();
									if (newCurrentDisplay != null) {
										currentDisplay = newCurrentDisplay;
										Runtime.getRuntime().addShutdownHook(new ShutdownHook());
									}
								} else {	
									System.out.println("could not get first workbench window");
								}
                        	} else {	
                        		System.out.println("could not get current workbench windows");
							}
                        } else {
                        	if (! currentDisplay.isDisposed()) {
								currentDisplay.syncExec(
									new Runnable() // start actions in UI thread
									{
										int i = 0;
										@Override
										public void run()
										{
	//										System.out.println("running consistently...");
											i++;
											runner(i); // this action have to be in UI thread
										}
									}	
								);
							}
						}
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
            }
        })).start();
	}
	
	private class ShutdownHook extends Thread {
		  @Override
		  public void run() {
		    try {
		      System.out.println("executing shutdown hook");
		      final IWorkbench workbench = PlatformUI.getWorkbench();
		      final Display display = PlatformUI.getWorkbench()
		                                        .getDisplay();
			  if (currentDisplay != null) {
			    putString(outputDir + "/finished.txt", "yes");
			  }
		      if (workbench != null && !workbench.isClosing()) {
		        display.syncExec(new Runnable() {
		          public void run() {
		          }
		        });
		        display.syncExec(new Runnable() {
		          public void run() {
		            workbench.close();
		          }
		        });
		      }
		    } catch (IllegalStateException e) {
		      // ignore
		    }
		  }
		}	

}
