package org.jcryptool.visual.jctca.listeners;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.jcryptool.visual.jctca.UserViews.CreateCert;
import org.jcryptool.visual.jctca.UserViews.RevokeCert;
import org.jcryptool.visual.jctca.UserViews.ShowCert;
import org.jcryptool.visual.jctca.UserViews.SignCert;

// TODO: dispose left side of usertab

public class TabItemListener implements SelectionListener {
	TabFolder parent;
	Composite grp_exp;

	public TabItemListener(TabFolder parent, Composite grp_exp) {
		this.parent = parent;
		this.grp_exp = grp_exp;

	}

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		System.out.println(parent.getSelectionIndex());
		Label lbl_exp = (Label) grp_exp.getChildren()[0];
		if (parent.getSelectionIndex() == 0) {
			lbl_exp.setText("In der Ansicht \"Benutzer\" haben Sie die Möglichkeit, ein neues Zertifikat anzufordern, Ihre bereits ausgestellten Zertifikate zu verwalten sowie Texte oder Dateien mit dem zu einem Ihrer ausgestellten Zertifikate gehörigen privaten Schlüssel zu signieren.\n\n"
					// +
					// "Unter \"Neues Zertifikat anfordern\" haben Sie die Möglichkeit, ein Zertifikat für einen öffentlichen Schlüssel anzufordern. Sollten Sie noch kein Schlüsselpaar haben, so können Sie sich eines generieren lassen.\nUnter \"Zertifikate verwalten\" können Sie sich, wenn Sie bereits ausgestellte Zertifikate haben, diese anzeigen lassen. Dort haben Sie außerdem die Möglichkeit, Zertifikate zu widerrufen.\nUnter \"Text oder Datei signieren\" können Sie Signaturen für von Ihnen eingegebenen Text oder Dateien auf Ihrem Computer erstellen.\n\n"
					+ "Wählen Sie eine der Aktionen aus, um fortzufahren und weitere Informationen dazu zu erhalten.\nWenn Sie dieses Plugin zum ersten Mal ausführen, empfiehlt es sich, mit \"Neues Zertifikat anfordern\" zu beginnen.");
			Group x = (Group) (parent.getChildren()[0]);
			Control[] foo = x.getChildren();
			System.out.println(foo.length);
			System.out.println(foo[0].getClass());
			System.out.println(foo[1].getClass());
			Composite right = (Composite) foo[1];
			if (right.getChildren().length > 0) {
				Composite right2 = (Composite) right.getChildren()[0];
				right2.dispose();
				x.layout(true);
			}
		} else if (parent.getSelectionIndex() == 1) {
			lbl_exp.setText("Die \"Registration Authority\" (RA) ist die Entität einer PKI, die dafür zuständig ist, festzustellen, ob Zertifikatsanfragen authentisch sind oder nicht. Im Zusammenhang mit einem persönlichen Zertifikat wie in diesem Fall bedeutet das, dass der Antragsteller auch wirklich die Person ist, für die das Zertifikat beantragt wird.\n\n"
					+ "Linkerhand finden Sie eine Liste von Zertifikatsanfragen. Darunter befinden sich die Anfragen, die Sie selbst in der Benutzer-Ansicht erstellt haben. Mittels \"Identität verifizieren\" können Sie diese als korrekt erklären und an die CA weiterleiten. Doch vorsicht, es befinden sich auch Anfragen darunter, die nicht mit den jeweiligen Ausweisen übereinstimmen. Achten Sie darauf, keine dieser Anfragen an die CA weiterzuleiten!\n\n"
					+ "Die Tätigkeit der RA ist für das Funktionieren einer PKI absolut entscheidend. Nur durch die ausführliche und korrekte Prüfung der RA kann verhindert werden, dass sich etwa Personen mit kriminellen Absichten als andere Personen ausgeben können. Es ist also für jeden PKI-Betreiber notwendig, hier größte Vorsicht und Genauigkeit walten zu lassen. Mehr über die Tätigkeit einer RA und zu verschiedenen Verfahren, die von PKI-Betreibern in der realen Welt angewandt werden, um die Identität eines Antragstellers sicherzustellen, finden Sie in der Onlinehilfe.");
		} else if (parent.getSelectionIndex() == 2) {
			lbl_exp.setText("Die \"Certification Authority\" (CA) hat zwei Aufgaben in einer PKI: Einerseits bearbeitet sie von der RA verifizierte Zertifikatsanfragen und stellt darin beantragte Zertifikate aus. Andererseits unterhält sie eine \"Certificate Revocation List\" (CRL). Möchte der Zertifikatsbesitzer, dass sein Zertifikat nicht länger gültig ist, so fügt die CA das betroffenen Zertifikat dieser CRL hinzu. Der Zertifikatswiderruf, mögliche Gründe und seine Auswirkungen werden in der Benutzer-Ansicht noch genauer behandelt. Die CA muss lediglich dafür sorgen, Widerrufsanfragen zeitnahe zu bearbeiten und die CRL auf dem neuesten Stand zu halten.\n" +
					"Ähnlich zum CSR ist das Format der CRL im X.509 Standard festgeschrieben. Mehr dazu lesen Sie in der Onlinehilfe.\n\n" +
					"Linkerhand finden Sie eine Liste an Zertifikatsanfragen (CSRs, von engl. \"Certificate Signing Request\") und Widerrufsanfragen (RRs, von engl. \"Revocation Request\"). Wenn Sie eine Zertifikatsanfrage annehmen, so wird dem Antragsteller ein Zertifikat ausgestellt. Sie können dieses dann in der Benutzer-Ansicht einsehen und verwenden. Wenn Sie eine Widerrufsanfrage annehmen, so wird das betroffene Zertifikat der CRL hinzugefügt.\n" +
					"Mehr zu den technischen Hintergründen von Zertifikatsausstellung und CRLs lesen Sie in der Onlinehilfe.");
		} else if (parent.getSelectionIndex() == 3) {
			lbl_exp.setText("In dieser Ansicht haben Sie die Möglichkeit, die von Ihnen erstellen Signaturen zu überprüfen.\n" +
					"Linkerhand können Sie Texte oder Dateien auswählen, für die Sie in der Ansicht \"Benutzer\" Signaturen erstellt haben. Der Text oder Dateiname wird anschließend zusammen mit der dazugehörigen Signatur angezeigt. Mittels \"Signatur überprüfen\" können Sie prüfen, ob diese Signatur gültig und vertrauenswürdig ist.\n" +
					"Achten Sie aber darauf, dass standardmäßig nicht geprüft wird, ob das Zertifikat, mit dem Sie diese Signatur prüfen, widerrufen wurde. Das ist hier so, weil auch viele Anwendungen (wie E-Mail-Clients oder Webbrowsern) standardmäßig nicht überprüfen, ob ein Zertifikat widerrufen wurde. Dies muss oft vom Benutzer selbst eingestellt werden.\n" +
					"Beachten Sie außerdem, dass Signaturen, die erstellt wurden, bevor das Zertifikat widerrufen wurde, ihre Gültigkeit behalten. Ein Widerruf hat nur zur Folge, dass keine weiteren gültigen Signaturen mehr erstellt werden können. Mehr über den Widerruf von Zertifikaten und technische Hintergründe zur Signaturprüfung lesen Sie in der Onlinehilfe.");
		}
		parent.layout(true);
		grp_exp.layout(true);
	}

}
