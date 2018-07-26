package gui.importfile.newformatpanels;

import gui.importfile.NewImportFormatDialog;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import java.awt.GridBagConstraints;
import javax.swing.JTextArea;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class DateDetailsPanel extends FormatDetailsPanel {
	private JTextField tfDateColumn;
	private JTextField tfDateRow;
	private JTextField textField_2;
	
	public DateDetailsPanel(NewImportFormatDialog overallDialog) {
		this.overallDialog = overallDialog;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 80, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 45, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JCheckBox chckbxDateColumn = new JCheckBox("<html>" + "Does the file have a dedicated column giving the date for each sale?" + "</html>");
		GridBagConstraints gbc_chckbxDateColumn = new GridBagConstraints();
		gbc_chckbxDateColumn.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxDateColumn.gridheight = 2;
		gbc_chckbxDateColumn.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxDateColumn.anchor = GridBagConstraints.WEST;
		gbc_chckbxDateColumn.gridwidth = 2;
		gbc_chckbxDateColumn.gridx = 0;
		gbc_chckbxDateColumn.gridy = 0;
		add(chckbxDateColumn, gbc_chckbxDateColumn);
		
		JTextArea txtrDateColumnExplanation = new JTextArea();
		txtrDateColumnExplanation.setEditable(false);
		txtrDateColumnExplanation.setEnabled(false);
		txtrDateColumnExplanation.setText("Some files may not have a column listing the date at each sale line, and will only provide the date in a single cell at the top of the file (for example). Please tick the box only if the date is provided at each sale line.");
		txtrDateColumnExplanation.setBackground(new Color(238, 238, 238));
		txtrDateColumnExplanation.setFont(new Font("Lucida Grande", Font.ITALIC, 13));
		txtrDateColumnExplanation.setWrapStyleWord(true);
		txtrDateColumnExplanation.setLineWrap(true);
		GridBagConstraints gbc_txtrDateColumnExplanation = new GridBagConstraints();
		gbc_txtrDateColumnExplanation.gridwidth = 2;
		gbc_txtrDateColumnExplanation.insets = new Insets(0, 5, 5, 0);
		gbc_txtrDateColumnExplanation.fill = GridBagConstraints.BOTH;
		gbc_txtrDateColumnExplanation.gridx = 0;
		gbc_txtrDateColumnExplanation.gridy = 2;
		add(txtrDateColumnExplanation, gbc_txtrDateColumnExplanation);
		
		JLabel lblDateColumn = new JLabel("Date Column Number:");
		GridBagConstraints gbc_lblDateColumn = new GridBagConstraints();
		gbc_lblDateColumn.anchor = GridBagConstraints.WEST;
		gbc_lblDateColumn.insets = new Insets(0, 5, 5, 5);
		gbc_lblDateColumn.gridx = 0;
		gbc_lblDateColumn.gridy = 3;
		add(lblDateColumn, gbc_lblDateColumn);
		
		tfDateColumn = new JTextField();
		GridBagConstraints gbc_tfDateColumn = new GridBagConstraints();
		gbc_tfDateColumn.insets = new Insets(0, 0, 5, 0);
		gbc_tfDateColumn.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfDateColumn.gridx = 1;
		gbc_tfDateColumn.gridy = 3;
		add(tfDateColumn, gbc_tfDateColumn);
		tfDateColumn.setColumns(10);
		
		JLabel lblDateRow = new JLabel("Date Row Number:");
		lblDateRow.setEnabled(false);
		GridBagConstraints gbc_lblDateRow = new GridBagConstraints();
		gbc_lblDateRow.anchor = GridBagConstraints.WEST;
		gbc_lblDateRow.insets = new Insets(0, 5, 5, 5);
		gbc_lblDateRow.gridx = 0;
		gbc_lblDateRow.gridy = 4;
		add(lblDateRow, gbc_lblDateRow);
		
		tfDateRow = new JTextField();
		tfDateRow.setEditable(false);
		tfDateRow.setEnabled(false);
		GridBagConstraints gbc_tfDateRow = new GridBagConstraints();
		gbc_tfDateRow.insets = new Insets(0, 0, 5, 0);
		gbc_tfDateRow.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfDateRow.gridx = 1;
		gbc_tfDateRow.gridy = 4;
		add(tfDateRow, gbc_tfDateRow);
		tfDateRow.setColumns(10);
		
		JCheckBox chckbxDateOffset = new JCheckBox("<html>" + "Date provided needs to be offset by a number of months to be correct?" + "</html>");
		GridBagConstraints gbc_chckbxDateOffset = new GridBagConstraints();
		gbc_chckbxDateOffset.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxDateOffset.anchor = GridBagConstraints.WEST;
		gbc_chckbxDateOffset.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxDateOffset.gridwidth = 2;
		gbc_chckbxDateOffset.gridx = 0;
		gbc_chckbxDateOffset.gridy = 5;
		add(chckbxDateOffset, gbc_chckbxDateOffset);
		
		JTextArea txtrForSomeChannels = new JTextArea();
		txtrForSomeChannels.setFont(new Font("Lucida Grande", Font.ITALIC, 13));
		txtrForSomeChannels.setText("For some channels, the date provided needs to be offset by a certain "
				+ "number of months to reflect the true transaction date. Check this box if it is the case here.");
		txtrForSomeChannels.setWrapStyleWord(true);
		txtrForSomeChannels.setLineWrap(true);
		txtrForSomeChannels.setEnabled(false);
		txtrForSomeChannels.setEditable(false);
		txtrForSomeChannels.setBackground(new Color(238, 238, 238));
		GridBagConstraints gbc_txtrForSomeChannels = new GridBagConstraints();
		gbc_txtrForSomeChannels.gridwidth = 2;
		gbc_txtrForSomeChannels.insets = new Insets(0, 5, 5, 5);
		gbc_txtrForSomeChannels.fill = GridBagConstraints.BOTH;
		gbc_txtrForSomeChannels.gridx = 0;
		gbc_txtrForSomeChannels.gridy = 6;
		add(txtrForSomeChannels, gbc_txtrForSomeChannels);
		
		JLabel lblDateOffset = new JLabel("Number of months to offset date by:");
		lblDateOffset.setEnabled(false);
		GridBagConstraints gbc_lblDateOffset = new GridBagConstraints();
		gbc_lblDateOffset.anchor = GridBagConstraints.WEST;
		gbc_lblDateOffset.insets = new Insets(0, 5, 5, 5);
		gbc_lblDateOffset.gridx = 0;
		gbc_lblDateOffset.gridy = 7;
		add(lblDateOffset, gbc_lblDateOffset);
		
		textField_2 = new JTextField();
		textField_2.setEnabled(false);
		textField_2.setEditable(false);
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 0);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 1;
		gbc_textField_2.gridy = 7;
		add(textField_2, gbc_textField_2);
		textField_2.setColumns(10);
		
	}

	@Override
	public Boolean validateUserInput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveUserInput() {
		// TODO Auto-generated method stub
		
	}

}
