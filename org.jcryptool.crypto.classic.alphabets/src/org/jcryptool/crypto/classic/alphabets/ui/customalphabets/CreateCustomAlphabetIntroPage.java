package org.jcryptool.crypto.classic.alphabets.ui.customalphabets;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.crypto.classic.alphabets.ui.customalphabets.customhistory.CustomAlphabetHistoryManager;
import org.jcryptool.crypto.classic.alphabets.ui.customalphabets.customhistory.CustomAlphabetItem;

public class CreateCustomAlphabetIntroPage extends WizardPage {
	private Label lblTheAlphabetWhich;
	private Button btnMakeTheCreated;
	private Label lblButEvenIf;
	private Group grpPermanenceOfThe;
	private Group grpReuseCustomAlphabets_1;
	private Button btnUseACustom;
	private GridData grpReuseCustomAlphabetsGData;
	final List<Button> btnsHistorySelect = new LinkedList<Button>();
	
	private int lastSelectedCustom = -1;
	private Composite compHistoryDisplays;
	private Button btnMakeTheSelected;
	
	/**
	 * Create the wizard.
	 */
	public CreateCustomAlphabetIntroPage() {
		super("Custom alphabet creation: informations");
		setTitle("Create a custom alphabet");
		setDescription("Here you can choose from any previously created alphabets, and " +
				"\ndetermine whether your alphabet should be permanently saved or not.");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(1, false));
		{
			grpPermanenceOfThe = new Group(container, SWT.NONE);
			grpPermanenceOfThe.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			grpPermanenceOfThe.setText("Permanence of the newly created alphabet");
			grpPermanenceOfThe.setLayout(new GridLayout(1, false));
			
			{
				lblTheAlphabetWhich = new Label(grpPermanenceOfThe, SWT.WRAP);
				GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
				layoutData.widthHint = 250;
				lblTheAlphabetWhich.setLayoutData(layoutData);
				lblTheAlphabetWhich.setText("The alphabet which you will create can be used one-time, but can also be saved into the standard set of alphabets of the JCrypTool.");
			}
			{
				lblButEvenIf = new Label(grpPermanenceOfThe, SWT.WRAP);
				GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
				layoutData.widthHint = 250;
				lblButEvenIf.setLayoutData(layoutData);
				lblButEvenIf.setText("But even if you don't make your alphabet permanent, you can still reuse it in this JCrypTool session. All custom alphabets you have created before restarting the JCrypTool will appear in this screen.");
			}
			{
				btnMakeTheCreated = new Button(grpPermanenceOfThe, SWT.CHECK);
				btnMakeTheCreated.setText("Make the created alphabet permanent");
				btnMakeTheCreated.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						if(btnMakeTheCreated.getSelection()) {
							setCustomAlphaPermanence(true);
						} else {
							setCustomAlphaPermanence(false);
						}
					}
				});
			}
		}
		{
			grpReuseCustomAlphabets_1 = new Group(container, SWT.NONE);
			grpReuseCustomAlphabets_1.setText("Reuse custom alphabets");
			grpReuseCustomAlphabetsGData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
			grpReuseCustomAlphabets_1.setLayoutData(grpReuseCustomAlphabetsGData);
			grpReuseCustomAlphabets_1.setLayout(new GridLayout(1, false));
			{
				btnUseACustom = new Button(grpReuseCustomAlphabets_1, SWT.CHECK);
				btnUseACustom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				btnUseACustom.setText("Use a custom alphabet from the past");
				
				btnUseACustom.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						if(btnUseACustom.getSelection()) {
							if(lastSelectedCustom >= 0) {
								selectHistory(lastSelectedCustom);
							} else {
								selectHistory(0);
							}
						} else {
							selectHistory(-1);
						}
					}
				});
				
				{
					compHistoryDisplays = new Composite(grpReuseCustomAlphabets_1, SWT.NONE);
					compHistoryDisplays.setLayout(new GridLayout());
					compHistoryDisplays.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
					{
						btnMakeTheSelected = new Button(grpReuseCustomAlphabets_1, SWT.CHECK);
						btnMakeTheSelected.setText("Make the selected alphabet permanent after finishing");
						
						btnMakeTheSelected.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								setHistoryAlphaPermanence(btnMakeTheSelected.getSelection());
							}
						});
					}
					
					
					List<AbstractAlphabet> customAlphabets = CustomAlphabetHistoryManager.customAlphabets;
					for (int i = 0; i < customAlphabets.size(); i++) {
						AbstractAlphabet alpha = customAlphabets.get(i);
						
						btnsHistorySelect.add(createCustomAlphaDisplay(alpha, compHistoryDisplays));
					}
				}
				
				
			}
			
		}
		if(CustomAlphabetHistoryManager.customAlphabets.size() == 0) {
			grpReuseCustomAlphabetsGData.exclude = true;
			grpReuseCustomAlphabets_1.setVisible(false);
		}
		selectHistory(-1);
	}

	private Button createCustomAlphaDisplay(AbstractAlphabet alpha, Composite host) {
			
		Composite comp = new Composite(host, SWT.NONE);
		comp.setLayout(new GridLayout(2, false));
		comp.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
	
		final Button radio = new Button(comp, SWT.RADIO);
		CustomAlphabetItem alphaDisplay = new CustomAlphabetItem(comp, alpha);
		alphaDisplay.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		alphaDisplay.setCursor(new Cursor(CreateCustomAlphabetIntroPage.this.getShell().getDisplay(), SWT.CURSOR_HAND));
		
		final SelectionAdapter listener = new SelectionAdapter() {
			Button defaultBtn = radio;
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(e==null) {
					selected(defaultBtn);
				} else {
					selected((Button) e.widget);
				}
			}
			private void selected(Button selectedBtn) {
				for (int j = 0; j < btnsHistorySelect.size(); j++) {
					Button b = btnsHistorySelect.get(j);
					b.setSelection(selectedBtn == b);
					if(b.getSelection()) {
						historySelected(CustomAlphabetHistoryManager.customAlphabets.get(j));
					}
				}
			}
		};
		radio.addSelectionListener(listener);
		alphaDisplay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				listener.widgetSelected(null);
			}
		});
		
		return radio;
	}

	protected void setCustomAlphaPermanence(boolean b) {
		getMyWizard().setCustomAlphaPermanence(b);
	}

	protected void setHistoryAlphaPermanence(boolean b) {
		getMyWizard().setHistoryAlphaPermanence(b);
	}

	protected void selectHistory(AbstractAlphabet alpha) {
		selectHistory(CustomAlphabetHistoryManager.customAlphabets.indexOf(alpha));
	}
	
	protected void selectHistory(int index) {
		for (int i = 0; i < btnsHistorySelect.size(); i++) {
			Button b = btnsHistorySelect.get(i);
			
			b.setSelection(index == i);
		}
		
		historySelected(index<0?null:CustomAlphabetHistoryManager.customAlphabets.get(index));
	}
	
	protected void historySelected(AbstractAlphabet abstractAlphabet) {
		if(abstractAlphabet!=null) {
			btnUseACustom.setSelection(true);
			btnMakeTheCreated.setEnabled(false);
			btnMakeTheSelected.setEnabled(true);
			btnMakeTheCreated.setSelection(false);
			lastSelectedCustom = CustomAlphabetHistoryManager.customAlphabets.indexOf(abstractAlphabet);
			setWizardMode(CustomAlphabetWizard.USE_HISTORY_ALPHABET);
			getMyWizard().setWizardSelectedAlphabet(abstractAlphabet);
		} else {
			btnUseACustom.setSelection(false);
			btnMakeTheCreated.setEnabled(true);
			btnMakeTheSelected.setEnabled(false);
			setWizardMode(CustomAlphabetWizard.MAKE_NEW_ALPHABET);
		}
	}
	
	@Override
	public boolean isPageComplete() {
		return true;
	}
	
	private CustomAlphabetWizard getMyWizard() {
		return (CustomAlphabetWizard) getWizard();
	}
	
	private void setWizardMode(String mode) {
		getMyWizard().setAlphaSelectMode(mode);
	}

	public boolean isCustomAlphabetReuse() {
		return btnUseACustom.getSelection();
	}
	
	public AbstractAlphabet getReuseAlphabet() {
		AbstractAlphabet result = null;
		for (int i = 0; i < btnsHistorySelect.size(); i++) {
			Button b = btnsHistorySelect.get(i);
			if(b.getSelection()) result = CustomAlphabetHistoryManager.customAlphabets.get(i);
		}
		
		return result;
	}
	
	
}
