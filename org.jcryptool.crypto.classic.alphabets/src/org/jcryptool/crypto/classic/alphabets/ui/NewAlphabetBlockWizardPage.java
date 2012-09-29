package org.jcryptool.crypto.classic.alphabets.ui;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.util.input.TextfieldInput;
import org.jcryptool.crypto.classic.alphabets.composite.AtomAlphabet;
import org.jcryptool.crypto.classic.alphabets.ui.AlphabetSelectorComposite.Mode;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class NewAlphabetBlockWizardPage extends WizardPage {
	private Label lblContent;
	private AlphabetManualInputField contentField;
	private boolean ownName = false;
	private Label lblName;
	private Text text;

	/**
	 * Create the wizard.
	 */
	public NewAlphabetBlockWizardPage() {
		super(Messages.getString("NewAlphabetBlockWizardPage.0")); //$NON-NLS-1$
		setTitle(Messages.getString("NewAlphabetBlockWizardPage.0")); //$NON-NLS-1$
		setDescription(Messages.getString("NewAlphabetBlockWizardPage.2")); //$NON-NLS-1$
	}

	boolean falseNameTyping = false;
	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));
		{
			lblContent = new Label(container, SWT.NONE);
			lblContent.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					System.out.println(isPageComplete());
				}
			});
			{
				GridData gd_lblContent = new GridData(SWT.RIGHT, SWT.BEGINNING, false, false, 1, 1);
				gd_lblContent.verticalIndent = 8;
				lblContent.setLayoutData(gd_lblContent);
			}
			lblContent.setText(Messages.getString("NewAlphabetBlockWizardPage.lblContent.text")); //$NON-NLS-1$
		}
		{
			contentField = new AlphabetManualInputField(container, SWT.NONE);
			contentField.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
			
			contentField.getAlphabetInput().addObserver(new Observer() {
				
				@Override
				public void update(Observable o, Object arg) {
					if(! ownName) {
						setAlphaName(contentField.getAlphabet());
					}
				}
			});
			
			contentField.getAlphabetInput().addObserver(new Observer() {
				@Override
				public void update(Observable o, Object arg) {
					myWizard().setAlphabet(contentField.getAlphabetInput().getContent());
				}
			});
		}
		
		{
			lblName = new Label(container, SWT.NONE);
			lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblName.setText(Messages.getString("NewAlphabetBlockWizardPage.lblName.text")); //$NON-NLS-1$
		}
		{
			text = new Text(container, SWT.BORDER);
			text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			text.addKeyListener(new KeyListener() {
				
				@Override
				public void keyReleased(KeyEvent e) {
					keyPressed(null);
				}
				
				@Override
				public void keyPressed(KeyEvent e) {
					ownName = true;
					myWizard().setName(text.getText());
				}
			});
		}
		
	}

	protected void setAlphaName(AtomAlphabet alpha) {
		String alphabetContentAsString = AtomAlphabet.alphabetContentAsString(alpha.getCharacterSet());
		text.setText(alphabetContentAsString);
		myWizard().setName(alphabetContentAsString);
	}
	
	private NewAlphabetBlockWizard myWizard() {
		return (NewAlphabetBlockWizard) getWizard();
	}

	TextfieldInput<AtomAlphabet> getAlphabetInput() {
		return contentField.getAlphabetInput();
	}
	
	@Override
	public boolean isPageComplete() {
		return 
			myWizard().getAlpha() != null && 
			myWizard().getAlpha().getCharacterSet().length>0 &&
			myWizard().getName() != null && 
			myWizard().getName().length()>0;
	}
}
