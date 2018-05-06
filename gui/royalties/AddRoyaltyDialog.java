package gui.royalties;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JTextField;

import main.Book;

/**Abstract class holding variables and methods shared between AddRoyaltyDialogOneChannel and AddRoyaltyDialogAllChannels, such as 
 * the data validation methods for user inputs and the display of different things in the lower panel depending on the user's choice 
 * of royalty type.
 * @author crhm
 *
 */
@SuppressWarnings("serial")
abstract class AddRoyaltyDialog extends JFrame implements ActionListener {

	protected Book book;

	protected JPanel panelDataEntryDependent; //Where everything that depends on the choice of royalty type is put.

	protected JComboBox<String> cbbChannels;
	protected JLabel lblChannel;

	protected JComboBox<String> cbbRoyaltyHolder;
	protected JComboBox<String> cbbRoyaltyType;

	protected JTextField tfFixedPercentage;
	protected JLabel lblFixedPercentage;

	protected JLabel lblFixedAmount;
	protected JTextField tfFixedAmount;

	protected JTextField tfDefaultPercentage;
	protected JLabel lblDefaultPercentage;
	protected JTextField tfRange1Percentage;
	protected JTextField tfRange2Percentage;
	protected JLabel lblRange1Percentage;
	protected JLabel lblRange2Percentage;
	protected JTextField tfRange1;
	protected JTextField tfRange2;
	protected JLabel lblRange1;
	protected JLabel lblRange2;

	protected JButton btnCancel;
	protected JButton btnAdd;
	
	/**Method to call for the creation of a fixed amount royalty
	 */
	abstract void addFixedAmount();
	
	/**Method to call for the creation of a fixed percentage royalty
	 */
	abstract void addFixedPercentage();
	
	/**Method to call for the creation of a units sold royalty
	 */
	abstract void addUnitsSold();
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cbbRoyaltyType) { //Event comes from RoyaltyType combo box
			if (((String) cbbRoyaltyType.getSelectedItem()).equals("Fixed Amount Royalty")) {
				displayDataEntriesFixedAmount();
			} else if (((String) cbbRoyaltyType.getSelectedItem()).equals("Fixed Percentage Royalty")) {
				displayDataEntriesFixedPercentage();
			} else if (((String) cbbRoyaltyType.getSelectedItem()).equals("Units Sold Royalty")) {
				displayDataEntriesUnitsSold();
			}
		} else if (e.getSource() == btnCancel) { //Event is cancel button
			this.dispose();
		} else if (e.getSource() == btnAdd) { //Event is from add Button
			if (cbbRoyaltyHolder.getSelectedIndex() == -1 || cbbRoyaltyType.getSelectedIndex() == -1) {
				JOptionPane.showMessageDialog(this, "Error: Please select a royalty holder and a royalty type.", 
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (((String) cbbRoyaltyType.getSelectedItem()).equals("Fixed Amount Royalty")) {
				addFixedAmount();
			} else if (((String) cbbRoyaltyType.getSelectedItem()).equals("Fixed Percentage Royalty")) {
				addFixedPercentage();
			} else if (((String) cbbRoyaltyType.getSelectedItem()).equals("Units Sold Royalty")) {
				addUnitsSold();
			}
		}
	}

	/**Method that removes whatever is in the lower panel and puts in it instead the data entry fields required 
	 * for a units sold royalty.
	 */
	protected void displayDataEntriesUnitsSold() {
		panelDataEntryDependent.removeAll();
		panelDataEntryDependent.setLayout(new GridLayout(5, 2, 0, 0));
		tfDefaultPercentage = new JTextField();
		lblDefaultPercentage = new JLabel("Percentage to apply when outside of either range:");
		lblDefaultPercentage.setHorizontalAlignment(SwingConstants.CENTER);
		tfRange1Percentage = new JTextField();
		tfRange2Percentage = new JTextField();
		lblRange1Percentage = new JLabel("Percentage for Range 1:");
		lblRange1Percentage.setHorizontalAlignment(SwingConstants.CENTER);
		lblRange2Percentage = new JLabel("Percentage for Range 2:");
		lblRange2Percentage.setHorizontalAlignment(SwingConstants.CENTER);
		tfRange1 = new JTextField();
		tfRange2 = new JTextField();
		lblRange1 = new JLabel("Range of units sold 1:");
		lblRange1.setHorizontalAlignment(SwingConstants.CENTER);
		lblRange2 = new JLabel("Range of units sold 2:");
		lblRange2.setHorizontalAlignment(SwingConstants.CENTER);

		panelDataEntryDependent.add(lblRange1);
		panelDataEntryDependent.add(tfRange1);
		panelDataEntryDependent.add(lblRange1Percentage);
		panelDataEntryDependent.add(tfRange1Percentage);
		panelDataEntryDependent.add(lblRange2);
		panelDataEntryDependent.add(tfRange2);
		panelDataEntryDependent.add(lblRange2Percentage);
		panelDataEntryDependent.add(tfRange2Percentage);
		panelDataEntryDependent.add(lblDefaultPercentage);
		panelDataEntryDependent.add(tfDefaultPercentage);
		this.revalidate();
		this.pack();
		this.repaint();
	}

	/**Method that removes whatever is in the lower panel and puts in it instead the data entry fields required 
	 * for a fixed amount royalty.
	 */
	protected void displayDataEntriesFixedAmount() {
		panelDataEntryDependent.removeAll();
		panelDataEntryDependent.setLayout(new GridLayout(1, 2, 0, 0));
		lblFixedAmount = new JLabel("Fixed Amount: (USD)");
		lblFixedAmount.setHorizontalAlignment(SwingConstants.CENTER);
		tfFixedAmount = new JTextField("");
		panelDataEntryDependent.add(lblFixedAmount);
		panelDataEntryDependent.add(tfFixedAmount);
		this.revalidate();
		this.pack();
		this.repaint();
	}

	/**Method that removes whatever is in the lower panel and puts in it instead the data entry fields required 
	 * for a fixed percentage royalty.
	 */
	protected void displayDataEntriesFixedPercentage() {
		panelDataEntryDependent.removeAll();
		panelDataEntryDependent.setLayout(new GridLayout(1, 2, 0, 0));
		lblFixedPercentage = new JLabel("Fixed Percentage:");
		lblFixedPercentage.setHorizontalAlignment(SwingConstants.CENTER);
		tfFixedPercentage = new JTextField("");
		panelDataEntryDependent.add(lblFixedPercentage);
		panelDataEntryDependent.add(tfFixedPercentage);
		this.revalidate();
		this.pack();
		this.repaint();
	}

	/**Checks that the string passed as argument represents a range of the format 1-200, and returns the range as an array of length two 
	 * where the first integer is the lower limit and the second is the upper limit, so that it can be plugged into the RoyaltyDependentOnUnitsSold 
	 * constructor.
	 * <br>It will return null if any of the following criteria are not met:
	 * <br>-String does not contain "-" (used to separate the limits)
	 * <br>-Strings before and after the "-" cannot be parsed into integers
	 * <br>-String before the "-" represents a number greater than the one represented by the String after the "-" 
	 * @param range String to be checked and processed
	 * @return null if checks fail, correctly formatted range if checks are passed.
	 */
	protected Integer[] checkAndReturnRange(String range) {
		if (!range.contains("-")) {
			JOptionPane.showMessageDialog(this, "Error: Please enter a valid range (must follow following format: 33-205).", 
					"Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		String[] splitRange = range.split("-");
		int limitLow = -1;
		int limitHigh = -1;
		try {
			limitLow = Integer.parseInt(splitRange[0]);
			limitHigh = Integer.parseInt(splitRange[1]);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Error: Please enter a valid range (both limits must be integers).", 
					"Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (limitLow > limitHigh) {
			JOptionPane.showMessageDialog(this, "Error: Please enter a valid range (first number must be lower than second number, e.g. 33-205).", 
					"Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		Integer[] rangeFormatted = {limitLow,  limitHigh};
		return rangeFormatted;
	}

	/**Checks that the string passed as argument is a percentage in the format of a number between 1-100, and returns it as a double if so.
	 *<br>It will return null if any of the following criteria are not met:
	 *<br>-String cannot be parsed into a double
	 *<br>-String represents a number that is smaller than 1 or greater than 100.
	 * @param percentageString String to be checked and processed
	 * @return null if checks fail, percentage as a double if they pass.
	 */
	protected Double checkAndReturnPercentage(String percentageString) {
		Double percentage = null;	
		try {
			percentage = Double.parseDouble(percentageString);
			if (percentage < 1 || percentage > 100) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Error: Please enter a valid percentage (must be a number between 1-100).", 
					"Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		return percentage;
	}
}
