package org.jcryptool.visual.hashing.views;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bouncycastle.crypto.digests.MD2Digest;
import org.bouncycastle.crypto.digests.MD4Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.util.encoders.Hex;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.hashing.HashingPlugin;

/**
 * 
 * @author Ferit Dogan
 * 
 */
public class HashingView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.jcryptool.visual.hashing.views.HashingView"; //$NON-NLS-1$
	private static final int OUTPUT_SEPERATOR = 144;

	public enum HashFunction {
		MD2("MD2 (128 bits)"), MD4("MD4 (128 bits)"), MD5("MD5 (128 bits)"), SHA1("SHA-1 (160 bits)"), SHA256( //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				"SHA-256 (256 bits)"), SHA512("SHA-512 (512 bits)"), RIPEMD160("RIPEMD-160 (160 bits)"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		private final String hashFunctionName;

		private HashFunction(String name) {
			hashFunctionName = name;
		}

		public HashFunction getName(String name) {
			for (HashFunction h : values()) {
				if (h.hashFunctionName.compareToIgnoreCase(name) == 0) {
					HashFunction value = valueOf(h.name());
					return value;
				}
			}
			return null;
		}
	}

	private HashFunction hash = HashFunction.MD2;
	private String hashInputValueHex = ""; //$NON-NLS-1$
	private String hashOutputValueHex = ""; //$NON-NLS-1$

	private StyledText styledTextDescription;
	private StyleRange header;
	private Text textInput;
	private Text textHashInput;
	private Text textOutput;
	private Text textHashOutput;
	private StyledText textDifference;
	private Combo comboHash;
	private Button btnHexadezimal;
	private Button btnDezimal;
	private Button btnBinary;

	/**
	 * The constructor.
	 */
	public HashingView() {
	}

	@Override
	public void createPartControl(Composite parent) {

		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		Composite compositeMain = new Composite(scrolledComposite, SWT.NONE);
		compositeMain.setLayout(new GridLayout(2, false));

		styledTextDescription = new StyledText(compositeMain, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		styledTextDescription.setEditable(false);
		GridData gd_styledTextDescription = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gd_styledTextDescription.widthHint = 300;
		gd_styledTextDescription.heightHint = 60;
		styledTextDescription.setLayoutData(gd_styledTextDescription);
		styledTextDescription.setText(Messages.HashingView_0 + Messages.HashingView_1);

		header = new StyleRange();
		header.start = 0;
		header.length = Messages.HashingView_0.length();
		header.fontStyle = SWT.BOLD;
		styledTextDescription.setStyleRange(header);

		Group grpHashfunction = new Group(compositeMain, SWT.NONE);
		grpHashfunction.setLayout(new GridLayout(1, false));
		grpHashfunction.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpHashfunction.setText(Messages.HashingView_2);

		comboHash = new Combo(grpHashfunction, SWT.READ_ONLY);
		comboHash.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (!textInput.getText().isEmpty()) {
					hashInputValueHex = computeHash(comboHash.getText(), textInput.getText(), textHashInput,
							hashInputValueHex);
				}

				if (!textOutput.getText().isEmpty()) {
					hashOutputValueHex = computeHash(comboHash.getText(), textOutput.getText(), textHashOutput,
							hashOutputValueHex);
				}

				if (!textInput.getText().isEmpty() && !textOutput.getText().isEmpty()) {
					computeDifference();
				} else {
					textDifference.setText(""); //$NON-NLS-1$
				}
			}
		});
		comboHash.setItems(new String[] { "MD2 (128 bits)", "MD4 (128 bits)", "MD5 (128 bits)", "SHA-1 (160 bits)", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				"SHA-256 (256 bits)", "SHA-512 (512 bits)", "RIPEMD-160 (160 bits)" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		comboHash.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboHash.select(0);

		Group grpTypeHash = new Group(compositeMain, SWT.NONE);
		grpTypeHash.setLayout(new GridLayout(3, false));
		grpTypeHash.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpTypeHash.setText(Messages.HashingView_3);

		btnHexadezimal = new Button(grpTypeHash, SWT.RADIO);
		btnHexadezimal.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String hash = null;
				if (!textInput.getText().isEmpty()) {
					hash = hashInputValueHex.toUpperCase().replaceAll(".{2}", "$0 "); //$NON-NLS-1$ //$NON-NLS-2$
					textHashInput.setText(hash);
				}

				if (!textOutput.getText().isEmpty()) {
					hash = hashOutputValueHex.toUpperCase().replaceAll(".{2}", "$0 "); //$NON-NLS-1$ //$NON-NLS-2$
					textHashOutput.setText(hash);
				}
			}
		});
		btnHexadezimal.setSelection(true);
		btnHexadezimal.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true, 1, 1));
		btnHexadezimal.setText(Messages.HashingView_4);

		btnDezimal = new Button(grpTypeHash, SWT.RADIO);
		btnDezimal.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String hash = null;
				if (!textInput.getText().isEmpty()) {
					hash = hexToDecimal(hashInputValueHex);
					hash = hash.replaceAll(".{3}", "$0 "); //$NON-NLS-1$ //$NON-NLS-2$
					textHashInput.setText(hash);
				}

				if (!textOutput.getText().isEmpty()) {
					hash = hexToDecimal(hashOutputValueHex);
					hash = hash.replaceAll(".{3}", "$0 "); //$NON-NLS-1$ //$NON-NLS-2$
					textHashOutput.setText(hash);
				}

			}
		});
		btnDezimal.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true, 1, 1));
		btnDezimal.setText(Messages.HashingView_5);

		btnBinary = new Button(grpTypeHash, SWT.RADIO);
		btnBinary.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String hash = null;
				if (!textInput.getText().isEmpty()) {
					hash = hexToBinary(hashInputValueHex);
					hash = hash.replaceAll(".{8}", "$0#"); //$NON-NLS-1$ //$NON-NLS-2$
					textHashInput.setText(hash);
				}

				if (!textOutput.getText().isEmpty()) {
					hash = hexToBinary(hashOutputValueHex);
					hash = hash.replaceAll(".{8}", "$0#"); //$NON-NLS-1$ //$NON-NLS-2$
					textHashOutput.setText(hash);
				}

			}
		});
		btnBinary.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true, 1, 1));
		btnBinary.setText(Messages.HashingView_6);

		Group grpInput = new Group(compositeMain, SWT.NONE);
		grpInput.setLayout(new GridLayout(1, false));
		grpInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		grpInput.setText(Messages.HashingView_7);

		textInput = new Text(grpInput, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		textInput.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!textInput.getText().isEmpty()) {
					hashInputValueHex = computeHash(comboHash.getText(), textInput.getText(), textHashInput,
							hashInputValueHex);
				} else {
					textHashInput.setText(""); //$NON-NLS-1$
				}

				if (!textInput.getText().isEmpty() && !textOutput.getText().isEmpty()) {
					computeDifference();
				} else {
					textDifference.setText(""); //$NON-NLS-1$
				}
			}
		});
		GridData gd_textInput = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textInput.heightHint = 90;
		textInput.setLayoutData(gd_textInput);

		Group grpHashInput = new Group(compositeMain, SWT.NONE);
		grpHashInput.setLayout(new GridLayout(1, false));
		grpHashInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		grpHashInput.setText(Messages.HashingView_10);

		textHashInput = new Text(grpHashInput, SWT.BORDER | SWT.READ_ONLY);
		textHashInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Group grpOutput = new Group(compositeMain, SWT.NONE);
		grpOutput.setLayout(new GridLayout(1, false));
		grpOutput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		grpOutput.setText(Messages.HashingView_9);

		textOutput = new Text(grpOutput, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		textOutput.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!textOutput.getText().isEmpty()) {
					hashOutputValueHex = computeHash(comboHash.getText(), textOutput.getText(), textHashOutput,
							hashOutputValueHex);
				} else {
					textHashOutput.setText(""); //$NON-NLS-1$
				}

				if (!textInput.getText().isEmpty() && !textOutput.getText().isEmpty()) {
					computeDifference();
				} else {
					textDifference.setText(""); //$NON-NLS-1$
				}
			}
		});
		GridData gd_textOutput = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textOutput.heightHint = 90;
		textOutput.setLayoutData(gd_textOutput);

		Group grpHashOutput = new Group(compositeMain, SWT.NONE);
		grpHashOutput.setLayout(new GridLayout(1, false));
		grpHashOutput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		grpHashOutput.setText(Messages.HashingView_10);

		textHashOutput = new Text(grpHashOutput, SWT.BORDER | SWT.READ_ONLY);
		textHashOutput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Group grpUnterschied = new Group(compositeMain, SWT.NONE);
		grpUnterschied.setLayout(new GridLayout(1, false));
		grpUnterschied.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 2, 1));
		grpUnterschied.setText(Messages.HashingView_11);

		textDifference = new StyledText(grpUnterschied, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
		textDifference.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite.setContent(compositeMain);
		scrolledComposite.setMinSize(new Point(1010, 566));

		loadExampleText();
	}

	private String hexToDecimal(String hex) {
		StringBuilder sb = new StringBuilder();
		String[] a = hex.toUpperCase().split("(?<=\\G..)"); //$NON-NLS-1$

		for (String s : a) {
			sb.append(String.format("%3s", (new BigInteger(s, 16)).toString()).replace(' ', '0')); //$NON-NLS-1$
		}

		return sb.toString();
	}

	private String hexToBinary(String hex) {
		String result = new BigInteger(hex, 16).toString(2);

		switch (hash) {
		case MD2:
		case MD4:
		case MD5:
			result = String.format("%128s", result).replace(' ', '0'); //$NON-NLS-1$

			break;
		case SHA256:
			result = String.format("%256s", result).replace(' ', '0'); //$NON-NLS-1$
			break;

		case SHA512:
			result = String.format("%512s", result).replace(' ', '0'); //$NON-NLS-1$
			break;

		case SHA1:
		case RIPEMD160:
			result = String.format("%160s", result).replace(' ', '0'); //$NON-NLS-1$
			break;

		default:
			break;
		}

		return result;
	}

	@Override
	public void setFocus() {
		textOutput.setFocus();
	}

	public void resetView() {
		styledTextDescription.setText(Messages.HashingView_0 + Messages.HashingView_1);
		styledTextDescription.setStyleRange(header);

		comboHash.select(0);
		btnHexadezimal.setSelection(true);
		btnDezimal.setSelection(false);
		btnBinary.setSelection(false);

		textInput.setText(""); //$NON-NLS-1$
		textHashInput.setText(""); //$NON-NLS-1$
		textOutput.setText(""); //$NON-NLS-1$
		textHashOutput.setText(""); //$NON-NLS-1$
		textDifference.setText(""); //$NON-NLS-1$

		loadExampleText();
	}

	private String computeHash(String hashName, String inputText, Text hashText, String hashInOutValue) {
		hash = hash.getName(hashName);
		byte[] digest = null;
		switch (hash) {
		case MD2:
			MD2Digest md2 = new MD2Digest();
			md2.update(inputText.getBytes(), 0, inputText.getBytes().length);
			digest = new byte[md2.getDigestSize()];
			md2.doFinal(digest, 0);

			break;
		case MD4:
			MD4Digest md4 = new MD4Digest();
			md4.update(inputText.getBytes(), 0, inputText.getBytes().length);
			digest = new byte[md4.getDigestSize()];
			md4.doFinal(digest, 0);

			break;
		case MD5:
			MD5Digest md5 = new MD5Digest();
			md5.update(inputText.getBytes(), 0, inputText.getBytes().length);
			digest = new byte[md5.getDigestSize()];
			md5.doFinal(digest, 0);

			break;

		case SHA1:
			SHA1Digest sha1 = new SHA1Digest();
			sha1.update(inputText.getBytes(), 0, inputText.getBytes().length);
			digest = new byte[sha1.getDigestSize()];
			sha1.doFinal(digest, 0);

			break;
		case SHA256:
			SHA256Digest sha256 = new SHA256Digest();
			sha256.update(inputText.getBytes(), 0, inputText.getBytes().length);
			digest = new byte[sha256.getDigestSize()];
			sha256.doFinal(digest, 0);

			break;
		case SHA512:
			SHA512Digest sha512 = new SHA512Digest();
			sha512.update(inputText.getBytes(), 0, inputText.getBytes().length);
			digest = new byte[sha512.getDigestSize()];
			sha512.doFinal(digest, 0);

			break;
		case RIPEMD160:
			RIPEMD160Digest ripemd160 = new RIPEMD160Digest();
			ripemd160.update(inputText.getBytes(), 0, inputText.getBytes().length);
			digest = new byte[ripemd160.getDigestSize()];
			ripemd160.doFinal(digest, 0);

			break;
		default:
			break;
		}

		hashInOutValue = new String(Hex.encode(digest));
		if (btnHexadezimal.getSelection()) {
			String hashValueOutput = hashInOutValue.toUpperCase().replaceAll(".{2}", "$0 "); //$NON-NLS-1$ //$NON-NLS-2$
			hashText.setText(hashValueOutput);
		} else if (btnDezimal.getSelection()) {
			String hashValue = hexToDecimal(hashInOutValue);
			hashValue = hashValue.replaceAll(".{3}", "$0 "); //$NON-NLS-1$ //$NON-NLS-2$
			hashText.setText(hashValue);
		} else if (btnBinary.getSelection()) {
			String hashValue = hexToBinary(hashInOutValue);
			hashValue = hashValue.replaceAll(".{8}", "$0#"); //$NON-NLS-1$ //$NON-NLS-2$
			hashText.setText(hashValue);
		}

		return hashInOutValue;
	}

	private void computeDifference() {
		BigInteger input = new BigInteger(hashInputValueHex, 16);
		BigInteger output = new BigInteger(hashOutputValueHex, 16);

		String result = input.xor(output).toString(16);
		result = hexToBinary(result);

		if (result.toString().equalsIgnoreCase("0")) { //$NON-NLS-1$
			textDifference.setText((hexToBinary("0").replaceAll(".{8}", "$0#"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} else {
			int count = result.length();
			int zeroBits = result.length() - result.replace("0", "").length(); //$NON-NLS-1$ //$NON-NLS-2$
			int oneBits = result.length() - result.replace("1", "").length(); //$NON-NLS-1$ //$NON-NLS-2$
			double percent = ((double) oneBits / (double) count) * 100;
			int[] sequence = find(result);

			result = result.replaceAll(".{8}", "$0#"); //$NON-NLS-1$ //$NON-NLS-2$

			int lenghtPrettyPrint = result.length();
			count = lenghtPrettyPrint / OUTPUT_SEPERATOR;

			StringBuilder sb = new StringBuilder(result);

			for (int i = 0; i < count; i++) {
				sb.insert(((OUTPUT_SEPERATOR) * (i + 1) + i), "\n"); //$NON-NLS-1$
			}

			if (hash == HashFunction.RIPEMD160 || hash == HashFunction.SHA1) {
				sb.insert(sb.length(), "\n"); //$NON-NLS-1$
			}

			result = sb.toString();

			char[] bitArray = result.toCharArray();

			textDifference.setText(result + "\n" + String.format("%1$,.2f", percent) //$NON-NLS-1$ //$NON-NLS-2$
					+ Messages.HashingView_12 + oneBits + Messages.HashingView_13 + (zeroBits + oneBits)
					+ Messages.HashingView_14 + sequence[1] + Messages.HashingView_15 + sequence[0] + "."); //$NON-NLS-1$
			for (int i = 0; i < bitArray.length; i++) {
				if (bitArray[i] == '1') {
					StyleRange bits = new StyleRange();
					bits.start = i;
					bits.length = 1;
					bits.foreground = this.getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_RED);
					textDifference.setStyleRange(bits);
				}
			}
		}
	}

	private int[] find(String s) {
		int[] result = new int[2];
		String currentSequence = null;
		String prevSequence = null;

		Matcher m = Pattern.compile("(0+)").matcher(s); //$NON-NLS-1$
		m.find();
		prevSequence = m.group();
		currentSequence = m.group();

		result[0] = prevSequence.length();
		result[1] = m.start();

		while (m.find()) {
			currentSequence = m.group();
			if (prevSequence.length() < currentSequence.length()) {
				prevSequence = m.group();
				int pos = m.start();

				result[0] = prevSequence.length();
				result[1] = pos;
			}
		}
		return result;
	}

	private void loadExampleText() {
		try {
			URL url = HashingPlugin.getDefault().getBundle().getEntry("/"); //$NON-NLS-1$
			File template = new File(FileLocator.toFileURL(url).getFile() + "templates" + File.separatorChar //$NON-NLS-1$
					+ Messages.HashingView_16);

			Scanner scanner = new Scanner(template, "UTF-8"); //$NON-NLS-1$
			String fileString = scanner.useDelimiter("\\Z").next(); //$NON-NLS-1$
			scanner.close();

			textInput.setText(fileString);
			textOutput.setText(fileString);

		} catch (IOException e) {
			LogUtil.logError(e);
		}
	}
}
