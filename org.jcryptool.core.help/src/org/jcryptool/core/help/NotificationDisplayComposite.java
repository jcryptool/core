package org.jcryptool.core.help;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.jcryptool.core.help.ServerStartup.Notification;

public class NotificationDisplayComposite extends Composite {

	public NotificationDisplayComposite(Composite parent) {
		super(parent, SWT.NONE);
		var viewer = new Browser(parent, SWT.NONE);
		viewer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}
	
	public void setNotifications(List<Notification> notifications) {
//		TODO
	}

	public void setNotificationIdx(int idx) {
//		TODO
	}

}
