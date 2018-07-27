package gui.importfile.newformatpanels;

import gui.importfile.NewImportFormatDialog;
import importing.ObjectToImport;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JSeparator;

@SuppressWarnings("serial")
public class DateProcessingDetailsPanel extends FormatDetailsPanel {
	private JTextField tfFixedValue;
	private JTextField tfDefaultValueCondition;
	private JTextField tfDefaultValue;
	private JCheckBox chckbxRemoveQuotationMarks;
	private JCheckBox chckbxRemoveFirstCharacter;
	private JCheckBox chckbxRemoveLastCharacter;
	private JTextField tfRemoveString;
	
	public DateProcessingDetailsPanel(NewImportFormatDialog overallDialog) {
		this.overallDialog = overallDialog;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		chckbxRemoveQuotationMarks = new JCheckBox("Remove quotation marks from cell value?");
		GridBagConstraints gbc_chckbxRemoveQuotationMarks = new GridBagConstraints();
		gbc_chckbxRemoveQuotationMarks.anchor = GridBagConstraints.WEST;
		gbc_chckbxRemoveQuotationMarks.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxRemoveQuotationMarks.gridx = 0;
		gbc_chckbxRemoveQuotationMarks.gridy = 0;
		add(chckbxRemoveQuotationMarks, gbc_chckbxRemoveQuotationMarks);
		
		chckbxRemoveFirstCharacter = new JCheckBox("Remove first character?");
		GridBagConstraints gbc_chckbxRemoveFirstCharacter = new GridBagConstraints();
		gbc_chckbxRemoveFirstCharacter.anchor = GridBagConstraints.WEST;
		gbc_chckbxRemoveFirstCharacter.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxRemoveFirstCharacter.gridx = 1;
		gbc_chckbxRemoveFirstCharacter.gridy = 0;
		add(chckbxRemoveFirstCharacter, gbc_chckbxRemoveFirstCharacter);
		
		chckbxRemoveLastCharacter = new JCheckBox("Remove last character?");
		GridBagConstraints gbc_chckbxRemoveLastCharacter = new GridBagConstraints();
		gbc_chckbxRemoveLastCharacter.anchor = GridBagConstraints.WEST;
		gbc_chckbxRemoveLastCharacter.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxRemoveLastCharacter.gridx = 2;
		gbc_chckbxRemoveLastCharacter.gridy = 0;
		add(chckbxRemoveLastCharacter, gbc_chckbxRemoveLastCharacter);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(192, 192, 192));
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.gridwidth = 3;
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.insets = new Insets(0, 5, 5, 0);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 1;
		add(separator, gbc_separator);
		
		JLabel lblRemoveString = new JLabel("Remove the following text from cell value (Optional):");
		GridBagConstraints gbc_lblRemoveString = new GridBagConstraints();
		gbc_lblRemoveString.anchor = GridBagConstraints.WEST;
		gbc_lblRemoveString.insets = new Insets(0, 5, 5, 5);
		gbc_lblRemoveString.gridx = 0;
		gbc_lblRemoveString.gridy = 2;
		add(lblRemoveString, gbc_lblRemoveString);
		
		tfRemoveString = new JTextField();
		GridBagConstraints gbc_tfRemoveString = new GridBagConstraints();
		gbc_tfRemoveString.insets = new Insets(0, 0, 5, 5);
		gbc_tfRemoveString.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfRemoveString.gridx = 1;
		gbc_tfRemoveString.gridy = 2;
		add(tfRemoveString, gbc_tfRemoveString);
		tfRemoveString.setColumns(10);
		
		JTextArea txtrRemoveStringExplanation = new JTextArea();
		txtrRemoveStringExplanation.setLineWrap(true);
		txtrRemoveStringExplanation.setWrapStyleWord(true);
		txtrRemoveStringExplanation.setBackground(new Color(238, 238, 238));
		txtrRemoveStringExplanation.setFont(new Font("Lucida Grande", Font.ITALIC, 13));
		txtrRemoveStringExplanation.setEnabled(false);
		txtrRemoveStringExplanation.setEditable(false);
		txtrRemoveStringExplanation.setText("If the cell values in the date column have extraneous characters "
				+ "which are not the date itself, "
				+ "please indicate that they should be removed by typing them above. For example \"EST\" in \"20/03/17 EST\".");
		GridBagConstraints gbc_txtrRemoveStringExplanation = new GridBagConstraints();
		gbc_txtrRemoveStringExplanation.gridwidth = 3;
		gbc_txtrRemoveStringExplanation.insets = new Insets(0, 5, 5, 0);
		gbc_txtrRemoveStringExplanation.fill = GridBagConstraints.BOTH;
		gbc_txtrRemoveStringExplanation.gridx = 0;
		gbc_txtrRemoveStringExplanation.gridy = 3;
		add(txtrRemoveStringExplanation, gbc_txtrRemoveStringExplanation);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setForeground(new Color(192, 192, 192));
		GridBagConstraints gbc_separator_2 = new GridBagConstraints();
		gbc_separator_2.gridwidth = 3;
		gbc_separator_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_2.insets = new Insets(0, 5, 5, 0);
		gbc_separator_2.gridx = 0;
		gbc_separator_2.gridy = 4;
		add(separator_2, gbc_separator_2);
		
		JLabel lblFixedValue = new JLabel("Fixed value (Optional):");
		GridBagConstraints gbc_lblFixedValue = new GridBagConstraints();
		gbc_lblFixedValue.anchor = GridBagConstraints.WEST;
		gbc_lblFixedValue.insets = new Insets(0, 5, 5, 5);
		gbc_lblFixedValue.gridx = 0;
		gbc_lblFixedValue.gridy = 5;
		add(lblFixedValue, gbc_lblFixedValue);
		
		tfFixedValue = new JTextField();
		GridBagConstraints gbc_tfFixedValue = new GridBagConstraints();
		gbc_tfFixedValue.insets = new Insets(0, 0, 5, 5);
		gbc_tfFixedValue.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfFixedValue.gridx = 1;
		gbc_tfFixedValue.gridy = 5;
		add(tfFixedValue, gbc_tfFixedValue);
		tfFixedValue.setColumns(10);
		
		JTextArea txtrFixedValueExplanation = new JTextArea();
		txtrFixedValueExplanation.setBackground(new Color(238, 238, 238));
		txtrFixedValueExplanation.setEnabled(false);
		txtrFixedValueExplanation.setEditable(false);
		txtrFixedValueExplanation.setFont(new Font("Lucida Grande", Font.ITALIC, 13));
		txtrFixedValueExplanation.setWrapStyleWord(true);
		txtrFixedValueExplanation.setLineWrap(true);
		txtrFixedValueExplanation.setText("If you want the value for the date to be determined by you and stay always the same"
				+ " for each sale found in this file, then write the fixed value you'd like to give the date above.");
		GridBagConstraints gbc_txtrFixedValueExplanation = new GridBagConstraints();
		gbc_txtrFixedValueExplanation.gridwidth = 3;
		gbc_txtrFixedValueExplanation.anchor = GridBagConstraints.WEST;
		gbc_txtrFixedValueExplanation.insets = new Insets(0, 5, 5, 0);
		gbc_txtrFixedValueExplanation.fill = GridBagConstraints.BOTH;
		gbc_txtrFixedValueExplanation.gridx = 0;
		gbc_txtrFixedValueExplanation.gridy = 6;
		add(txtrFixedValueExplanation, gbc_txtrFixedValueExplanation);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(new Color(192, 192, 192));
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_1.gridwidth = 3;
		gbc_separator_1.insets = new Insets(0, 5, 5, 0);
		gbc_separator_1.gridx = 0;
		gbc_separator_1.gridy = 7;
		add(separator_1, gbc_separator_1);
		
		JLabel lblDefaultValueCondition = new JLabel("If cell value is this... (Optional):");
		GridBagConstraints gbc_lblDefaultValueCondition = new GridBagConstraints();
		gbc_lblDefaultValueCondition.anchor = GridBagConstraints.WEST;
		gbc_lblDefaultValueCondition.insets = new Insets(0, 5, 5, 5);
		gbc_lblDefaultValueCondition.gridx = 0;
		gbc_lblDefaultValueCondition.gridy = 8;
		add(lblDefaultValueCondition, gbc_lblDefaultValueCondition);
		
		tfDefaultValueCondition = new JTextField();
		GridBagConstraints gbc_tfDefaultValueCondition = new GridBagConstraints();
		gbc_tfDefaultValueCondition.insets = new Insets(0, 0, 5, 5);
		gbc_tfDefaultValueCondition.anchor = GridBagConstraints.NORTH;
		gbc_tfDefaultValueCondition.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfDefaultValueCondition.gridx = 1;
		gbc_tfDefaultValueCondition.gridy = 8;
		add(tfDefaultValueCondition, gbc_tfDefaultValueCondition);
		tfDefaultValueCondition.setColumns(10);
		
		JTextArea txtrIfYouWant = new JTextArea();
		txtrIfYouWant.setBackground(new Color(238, 238, 238));
		txtrIfYouWant.setFont(new Font("Lucida Grande", Font.ITALIC, 13));
		txtrIfYouWant.setWrapStyleWord(true);
		txtrIfYouWant.setLineWrap(true);
		txtrIfYouWant.setEnabled(false);
		txtrIfYouWant.setEditable(false);
		txtrIfYouWant.setText("If you want to have a fixed value for the date only when the cell value "
				+ "is equal to something, please write the cell value to be replaced above and the replacement below.");
		GridBagConstraints gbc_txtrIfYouWant = new GridBagConstraints();
		gbc_txtrIfYouWant.anchor = GridBagConstraints.WEST;
		gbc_txtrIfYouWant.gridwidth = 3;
		gbc_txtrIfYouWant.insets = new Insets(0, 5, 5, 0);
		gbc_txtrIfYouWant.fill = GridBagConstraints.BOTH;
		gbc_txtrIfYouWant.gridx = 0;
		gbc_txtrIfYouWant.gridy = 9;
		add(txtrIfYouWant, gbc_txtrIfYouWant);
		
		JLabel lblDefaultValue = new JLabel("...then change it to this:");
		GridBagConstraints gbc_lblDefaultValue = new GridBagConstraints();
		gbc_lblDefaultValue.anchor = GridBagConstraints.WEST;
		gbc_lblDefaultValue.insets = new Insets(0, 5, 0, 5);
		gbc_lblDefaultValue.gridx = 0;
		gbc_lblDefaultValue.gridy = 10;
		add(lblDefaultValue, gbc_lblDefaultValue);
		
		tfDefaultValue = new JTextField();
		GridBagConstraints gbc_tfDefaultValue = new GridBagConstraints();
		gbc_tfDefaultValue.insets = new Insets(0, 0, 0, 5);
		gbc_tfDefaultValue.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfDefaultValue.gridx = 1;
		gbc_tfDefaultValue.gridy = 10;
		add(tfDefaultValue, gbc_tfDefaultValue);
		tfDefaultValue.setColumns(10);
	}
	
	@Override
	public Boolean validateUserInput() {
		Boolean success = true;
		if (tfDefaultValue.getText().isEmpty() && !tfDefaultValueCondition.getText().isEmpty() ||
				!tfDefaultValue.getText().isEmpty() && tfDefaultValueCondition.getText().isEmpty()) {
			success = false;
		}
		return success;
	}

	@Override
	public void saveUserInput() {
		ObjectToImport dateSettings = new ObjectToImport(this.overallDialog.getDateColumnIndex(), chckbxRemoveQuotationMarks.isSelected(), 
				chckbxRemoveFirstCharacter.isSelected(), chckbxRemoveLastCharacter.isSelected());
		if (!tfRemoveString.getText().isEmpty()) {
			dateSettings.setStringToRemove(tfRemoveString.getText());
		}
		if (!tfFixedValue.getText().isEmpty()) {
			dateSettings.setFixedValue(tfFixedValue.getText());
		}
		if (!tfDefaultValue.getText().isEmpty() && !tfDefaultValueCondition.getText().isEmpty()) {
			dateSettings.setConditionAndDefaultValue(tfDefaultValueCondition.getText(), tfDefaultValue.getText());
		}
		this.overallDialog.setDateSettings(dateSettings);
	}

}
