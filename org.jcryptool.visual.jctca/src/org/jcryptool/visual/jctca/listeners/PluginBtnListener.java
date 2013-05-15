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
			exp.setText("M\u00F6chte ein Benutzer beweisen, dass ein \u00F6ffentlicher Schl\u00FCssel tats\u00F6chlich zu einem privaten Schl\u00FCssel in seinem Besitz geh\u00F6rt, kann er sich ein Zertifikat ausstellen lassen. Dazu erstellt der Benutzer einen \"Certificate Signing Request\" (CSR) f\u00FCr seinen \u00F6ffentlichen Schl\u00FCssel und leitet diesen zusammen mit einem Identit\u00F6tsnachweis (beispielsweise einer Ausweiskopie) an eine sogenannte \"Registration Authority\" (RA) weiter. Die RA \u00FCberpr\u00FCft, ob es sich beim Antragsteller auch tats\u00F6chlich um die Person handelt, f\u00FCr die das Zertifikat ausgestellt werden soll. Wenn dies der Fall ist, leitet sie den CSR an eine \"Certification Authority\" (CA) weiter. Die CA f\u00FCgt dem CSR dann ihre eigene Signatur hinzu, wodurch der CSR zu einem von dieser CA ausgestellten Zertifikat wird. Das Zertifikat geht zur\u00FCck an den Antragsteller. Die RA, die CA und die damit verbundenen Prozesse werden auch als \"Public-Key-Infrastruktur\" (PKI) bezeichnet.");
			util.set_image_name("Architekturskizze Zertifikatserzeugung");//$NON-NLS-1$
			break;
		case 1: //data is set to 1 if revoke needs to be shown - see JCTCA_Visual.java
			help = Activator.getImageDescriptor("icons/minica_revoke.png")//$NON-NLS-1$
			.createImage();
			exp.setText("Es gibt mehrere Gr\u00FCnde, warum ein Zertifikat widerrufen werden muss, bevor es seine G\u00FCltigkeitsdauer \u00FCberschritten hat, welche Sie im Detail in der Onlinehilfe nachlesen k\u00F6nnen. Will der Zertifikatsinhaber sein Zertifikat widerrufen, so muss er daf\u00FCr einen \"Revocation Request\" (RR) erstellen. Dieser RR geht \u00F6hnlich wie ein CSR zun\u00F6chst an die RA zur Pr\u00FCfung. Erkl\u00F6rt die RA diesen Request als authentisch, so wird er an die CA weitergeleitet. Die CA verwaltet eine \"Certificate Revocation List\" (CRL), also eine Liste aller Zertifikate, die von ihr ausgestellt wurden und ihr G\u00FCltigkeitsdatum noch nicht \u00FCberschritten haben, aber vom Zertifikatsinhaber als ung\u00FCltig erkl\u00F6rt wurden. Wenn eine CA einen RR weitergeleitet bekommt, so f\u00FCgt sie das betroffene Zertifikat dieser CRL hinzu.");
			util.resize_image(lbl_img, comp_image, help);
			util.set_image_name("Architekturskizze Zertifikatswiderruf");//$NON-NLS-1$
			break;
		case 2: //data is set to 2 if check needs to be shown - see JCTCA_Visual.java
			help = Activator.getImageDescriptor("icons/minica_check.png")//$NON-NLS-1$
			.createImage();
			exp.setText("Will der Empf\u00F6nger signierter Daten (in diesem Beispiel einer E-Mail) die Authentizit\u00E4t dieser pr\u00FCfen, so muss daf\u00FCr im Groben zwei Schritte durchf\u00FChren. Zun\u00F6chst muss er \u00FCberpr\u00FCft, ob die Signatur der E-Mail tats\u00F6chlich von dem privaten Schl\u00FCssel stammt, der zum Zertifikat des Empf\u00F6ngers geh\u00F6rt. Anschlie\u00DFend muss er die CRL der ausstellenden CA anfordern und pr\u00FCfen, ob das Zertifikat vom Inhaber widerrufen wurde. Befindet sich das Zertifikat auf der CRL, so ist die Signatur nur dann g\u00FCltig, wenn das Datum der Signatur vor dem Datum des Widerrufs liegt.");
			util.resize_image(lbl_img, comp_image, help);
			util.set_image_name("Architekturskizze Signaturpr\u00FCfung");//$NON-NLS-1$
			break;
		case 3: // is set to 3 if user wants to continue to the plugin - see JCTCA_Visual.java
			visual.disposeCompCenter();
			visual.showCenter();
			exp.setText("Dieses Plugin stellt die grundlegende Funktionsweise einer Public-Key-Infrastruktur (PKI) dar. Sie k\u00F6nnen ein Schl\u00FCsselpaar und eine dazugeh\u00F6rige Zertifikatsanfrage erstellen. Anschlie\u00DFend haben Sie die M\u00F6glichkeit, den Weg zu verfolgen, den Ihre Anfrage durch die verschiedenen Teile der PKI durchl\u00F6uft. Nach Ausstellung eines Zertifikats haben Sie die M\u00F6glichkeit, Signaturen zu erstellen und zu \u00FCberpr\u00FCfen. Jeder Schritt wird von Erkl\u00F6rungstexten begleitet, der Ihnen Informationen zum jeweiligen Schritt liefert. Ausf\u00F6hrliche Hintergrundinformationen zu PKIs finden Sie in der Onlinehilfe.");
			break;
		}

	}

}
