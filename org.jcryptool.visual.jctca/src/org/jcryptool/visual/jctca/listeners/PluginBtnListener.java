package org.jcryptool.visual.jctca.listeners;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.visual.jctca.Activator;
import org.jcryptool.visual.jctca.JCTCA_Visual;
import org.jcryptool.visual.jctca.ResizeHelper;

public class PluginBtnListener implements SelectionListener {

	JCTCA_Visual visual;
	Label lbl_img;
	Image image;
	Image help;
	StyledText exp;

	public PluginBtnListener(JCTCA_Visual visual, Label lbl_img, StyledText exp) {
		this.visual = visual;
		this.lbl_img = lbl_img;
		this.exp = exp;
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		Composite comp_image = lbl_img.getParent();
		//get the button that was pressed
		Button btn = (Button) arg0.getSource();
		//check what button it was: 0 = create, 1 = revoke, 2 = check, 3 = continue to plugin (see JCTCA_Visual.java)
		Integer data = (Integer) btn.getData();
		int pressed = data.intValue();
		ResizeHelper util = new ResizeHelper();
		
		//take action according to button pressed
		switch (pressed) {
		case 0: //data is set to 0 if create needs to be shown - see JCTCA_Visual.java
			help = Activator.getImageDescriptor("icons/minica_create.png")//$NON-NLS-1$
			.createImage();
			exp.setText("Möchte ein Benutzer beweisen, dass ein öffentlicher Schlüssel tatsächlich zu einem privaten Schlüssel in seinem Besitz gehört, hat er die Möglichkeit, sich für diesen Schlüssel ein Zertifikat ausstellen zu lassen. Zu diesem Zweck erstellt der Benutzer einen \"Certificate Signing Request\" (CSR) für seinen öffentlichen Schlüssel und leitet diesen zusammen mit einem Identitätsnachweis (beispielsweise einer Ausweiskopie) an eine sogenannte \"Registration Authority\" (RA) weiter. Die RA überprüft, ob es sich beim Antragsteller auch tatsächlich um die Person handelt, für die das Zertifikat ausgestellt werden soll und leitet den CSR, wenn dies der Fall ist, an eine \"Certification Authority\" (CA) weiter. Die CA fügt dem CSR ihre eigene Signatur hinzu, wodurch dieser zu einem von dieser CA ausgestellten Zertifikat wird. Dieses Zertifikat geht zurück an den Antragsteller. Die RA und die CA werden gemeinsam auch als \"Public Key Infrastructure\" (PKI) bezeichnet.");
			util.resize_image(lbl_img, comp_image, help);
			util.set_image_name("Architekturskizze Zertifikatserzeugung");//$NON-NLS-1$
			break;
		case 1: //data is set to 1 if revoke needs to be shown - see JCTCA_Visual.java
			help = Activator.getImageDescriptor("icons/minica_revoke.png")//$NON-NLS-1$
			.createImage();
			exp.setText("Es gibt mehrere Gründe, warum ein Zertifikat widerrufen werden muss, bevor es seine Gültigkeitsdauer überschritten hat, welche Sie im Detail in der Onlinehilfe nachlesen können. Will der Zertifikatsinhaber sein Zertifikat widerrufen, so muss er dafür einen \"Revocation Request\" (RR) erstellen. Dieser RR geht ähnlich wie ein CSR zunächst an die RA zur Prüfung. Erklärt die RA diesen Request als authentisch, so wird er an die CA weitergeleitet. Die CA verwaltet eine \"Certificate Revocation List\" (CRL), also eine Liste aller Zertifikate, die von ihr ausgestellt wurden und ihr Gültigkeitsdatum noch nicht überschritten haben, aber vom Zertifikatsinhaber als ungültig erklärt wurden. Wenn eine CA einen RR weitergeleitet bekommt, so fügt sie das betroffene Zertifikat dieser CRL hinzu.");
			util.resize_image(lbl_img, comp_image, help);
			util.set_image_name("Architekturskizze Zertifikatswiderruf");//$NON-NLS-1$
			break;
		case 2: //data is set to 2 if check needs to be shown - see JCTCA_Visual.java
			help = Activator.getImageDescriptor("icons/minica_check.png")//$NON-NLS-1$
			.createImage();
			exp.setText("Will der Empfänger signierter Daten, wie in diesem Beispiel einer E-Mail, die Echtheit dieser prüfen, so sind dafür im groben zwei Schritte notwendig. Zunächst muss überprüft werden, ob die Signatur der E-Mail tatsächlich vom privaten Schlüssel stammt, der zum Zertifikat des Empfängers gehört. Anschließend muss die CRL der ausstellenden CA angefordert und geprüft werden, ob das Zertifikat vom Inhaber widerrufen wurde. Befindet sich das Zertifikat auf der CRL, so ist die Signatur nur dann gültig, wenn das Datum der Signatur vor dem Datum des Widerrufs liegt.");
			util.resize_image(lbl_img, comp_image, help);
			util.set_image_name("Architekturskizze Signaturprüfung");//$NON-NLS-1$
			break;
		case 3: // is set to 3 if user wants to continue to the plugin - see JCTCA_Visual.java
			visual.disposeCompCenter();
			visual.showCenter();
			exp.setText("Dieses Plugin stellt die grundlegende Funktionsweise einer Public Key Infrastructure (PKI) dar. Sie können ein Schlüsselpaar und eine dazugehörige Zertifikatsanfrage erstellen. Anschließend haben Sie die Möglichkeit, den Weg zu verfolgen, den Ihre Anfrage durch die verschiedenen Teile der PKI durchläuft. Nach Ausstellung eines Zertifikats haben Sie die Möglichkeit, Signaturen zu erstellen und zu überprüfen. Jeder Schritt wird von Erklärungstexten begleitet, der Ihnen Informationen zum jeweiligen Schritt liefert. Ausführliche Hintergrundinformationen zu PKIs finden Sie in der Onlinehilfe.");
			break;
		}

	}

}
