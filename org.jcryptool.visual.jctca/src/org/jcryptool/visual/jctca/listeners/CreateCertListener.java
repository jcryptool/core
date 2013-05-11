package org.jcryptool.visual.jctca.listeners;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;

import org.bouncycastle.asn1.pkcs.RSAPublicKey;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.crypto.certificates.CertificateFactory;
import org.jcryptool.crypto.keys.KeyType;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.jctca.Util;
import org.jcryptool.visual.jctca.CertificateClasses.RegistrarCSR;
import org.jcryptool.visual.jctca.UserViews.Messages;

public class CreateCertListener implements SelectionListener {
	Text txt_first_name, txt_last_name, txt_street, txt_zip, txt_town,
			txt_country, txt_mail;
	Combo cmb_keys;
	String path;

	public CreateCertListener(Text first_name, Text last_name, Text street,
			Text zip, Text town, Text country, Text mail, Combo keys) {
		this.txt_first_name = first_name;
		this.txt_last_name = last_name;
		this.txt_street = street;
		this.txt_zip = zip;
		this.txt_town = town;
		this.txt_country = country;
		this.txt_mail = mail;
		this.cmb_keys = keys;
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		Button src = (Button) arg0.getSource();
		String text = src.getText();
		KeyStoreManager mng = KeyStoreManager.getInstance();

		if (text.equals("CSR abschicken")) {
			KeyStoreAlias pubAlias = null;
			KeyStoreAlias privAlias = null;

			pubAlias = (KeyStoreAlias)cmb_keys.getData(cmb_keys.getItem(cmb_keys.getSelectionIndex()));
			privAlias = mng.getPrivateForPublic(pubAlias);
			RegistrarCSR.getInstance().addCSR(txt_first_name.getText(), txt_last_name.getText(),
					txt_street.getText(), txt_zip.getText(),
					txt_town.getText(), txt_country.getText(),
					txt_mail.getText(), path, pubAlias, privAlias);
			Util.showMessageBox(
					"CSR abgeschickt",
					"Ihr CSR wurde an die RA weitergeleitet. Damit sind Sie in der Benutzer Ansicht zunächst einmal fertig. Bitte wechseln Sie in die Ansicht \"Registration Authority.\"",
					SWT.ICON_INFORMATION);
		} else if (text.equals("Datei auswählen") || text.equals(path)) {
			FileDialog f = new FileDialog(
					Display.getCurrent().getActiveShell(), SWT.OPEN);
			f.setFilterExtensions(new String[] {
					Messages.CreateCertListener_fileext_jpg,
					Messages.CreateCertListener_fileext_gif,
					Messages.CreateCertListener_fileext_bmp,
					Messages.CreateCertListener_fileext_png });
			path = f.open();
			if(path!=null){
				src.setText(path);
			}
		} else if (text.equals("Neues Schlüsselpaar generieren")) {
			String first = txt_first_name.getText();
			String last = txt_last_name.getText();
			String street = txt_street.getText();
			String zip = txt_zip.getText();
			String town = txt_town.getText();
			String country = txt_country.getText();
			String mail = txt_mail.getText();
			if (first.length() != 0 && last.length() != 0
					&& street.length() != 0 && zip.length() != 0
					&& town.length() != 0 && country.length() != 0
					&& mail.length() != 0) {
				RSAKeyPairGenerator gen = new RSAKeyPairGenerator();
				SecureRandom sr = new SecureRandom();
				gen.init(new RSAKeyGenerationParameters(BigInteger.valueOf(3),
						sr, 1024, 80));
				AsymmetricCipherKeyPair keypair = gen.generateKeyPair();
				RSAKeyParameters publicKey = (RSAKeyParameters) keypair
						.getPublic();
				RSAPrivateCrtKeyParameters privateKey = (RSAPrivateCrtKeyParameters) keypair
						.getPrivate();
				try {
					// used to get proper encoding for the certificate
					RSAPublicKey pkStruct = new RSAPublicKey(
							publicKey.getModulus(), publicKey.getExponent());
					// JCE format needed for the certificate - because
					// getEncoded() is necessary...
					PublicKey pubKey = KeyFactory
							.getInstance(
									Messages.CreateCertListener_instance_of_keyfactory_pubkey)
							.generatePublic(
									new RSAPublicKeySpec(
											publicKey.getModulus(), publicKey
													.getExponent()));
					// and this one for the KeyStore
					PrivateKey privKey = KeyFactory
							.getInstance(
									Messages.CreateCertListener_instance_of_keyfactory_prikey)
							.generatePrivate(
									new RSAPrivateCrtKeySpec(publicKey
											.getModulus(), publicKey
											.getExponent(), privateKey
											.getExponent(), privateKey.getP(),
											privateKey.getQ(), privateKey
													.getDP(), privateKey
													.getDQ(), privateKey
													.getQInv()));
					String name = first + " " + last;
					KeyStoreAlias privAlias = new KeyStoreAlias(name,
							KeyType.KEYPAIR_PRIVATE_KEY, "RSA", 1024,
							(name.concat(privKey.toString())).hashCode() + " ",
							privKey.getClass().getName());
					KeyStoreAlias pubAlias = new KeyStoreAlias(name,
							KeyType.KEYPAIR_PUBLIC_KEY, "RSA", 1024,
							(name.concat(privKey.toString())).hashCode() + " ",
							pubKey.getClass().getName());
					mng.addKeyPair(privKey, CertificateFactory
							.createJCrypToolCertificate(pubKey), new String(
							KeyStoreManager.getDefaultKeyPassword()),
							privAlias, pubAlias);
					System.out.println(pubAlias.getContactName());
					String entry = pubAlias.getContactName() + "(Hash: " + pubAlias.getHashValue() + ")";
					cmb_keys.add(entry);
					cmb_keys.getParent().layout();
					cmb_keys.select(cmb_keys.getItemCount() - 1);
					cmb_keys.setData(entry, pubAlias);
				} catch (InvalidKeySpecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Util.showMessageBox("Leeres Feld!",
						"Bitte füllen Sie alle Felder aus.",
						SWT.ICON_INFORMATION);
			}
		}

	}

}
