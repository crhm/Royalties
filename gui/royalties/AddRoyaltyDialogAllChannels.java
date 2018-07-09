package gui.royalties;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import main.Book;
import main.Channel;
import main.Person;
import main.SalesHistory;
import main.royalties.RoyaltyDependentOnUnitsSold;
import main.royalties.RoyaltyFixedAmount;
import main.royalties.RoyaltyPercentage;

/**The dialog to add a royalty for all channels at the same time.
 * See AddRoyaltyDialog.
 * @author crhm
 *
 */
@SuppressWarnings("serial")
public class AddRoyaltyDialogAllChannels extends AddRoyaltyDialog {

	public AddRoyaltyDialogAllChannels(Book b) {
		super();
		book = b;

		setTitle("Create A New Royalty");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		//Sets up data entry panels
		JPanel panelBoth = new JPanel(new GridLayout(2, 1, 0, 0));
		JPanel panelDataEntry = new JPanel(new GridLayout(2, 2, 0, 0));
		panelDataEntryDependent = new JPanel();
		panelBoth.add(panelDataEntry);
		panelBoth.add(panelDataEntryDependent);
		getContentPane().add(panelBoth, BorderLayout.CENTER);

		//Royalty holder label
		JLabel lblRoyaltyHolder = new JLabel("Royalty Holder:");
		lblRoyaltyHolder.setHorizontalAlignment(SwingConstants.CENTER);
		panelDataEntry.add(lblRoyaltyHolder);

		//Making the comboBox of persons
		Person[] listPersons = new Person[SalesHistory.get().getListPersons().size()];
		int count = 0;
		for (Person p : SalesHistory.get().getListPersons()) {
			listPersons[count] = p;
			count++;
		}
		Arrays.sort(listPersons, new Comparator<Person>() {
			@Override
			public int compare(Person o1, Person o2) {
				return o1.getName().compareTo(o2.getName());
			}			
		});
		DefaultComboBoxModel<Person> modelPersons = new DefaultComboBoxModel<Person>(listPersons);
		cbbRoyaltyHolder = new JComboBox<Person>();
		cbbRoyaltyHolder.setModel(modelPersons);
		cbbRoyaltyHolder.setSelectedIndex(-1);
		panelDataEntry.add(cbbRoyaltyHolder);

		//Royalty type label
		JLabel lblRoyaltyType = new JLabel("Royalty Type:");
		lblRoyaltyType.setHorizontalAlignment(SwingConstants.CENTER);
		panelDataEntry.add(lblRoyaltyType);

		//Making the comboBox of royaltyTypes
		String[] listRoyaltyTypes = {"Fixed Percentage Royalty", "Fixed Amount Royalty", "Units Sold Royalty"};
		DefaultComboBoxModel<String> modelRoyaltyTypes = new DefaultComboBoxModel<String>(listRoyaltyTypes);
		cbbRoyaltyType = new JComboBox<String>();
		cbbRoyaltyType.setModel(modelRoyaltyTypes);
		cbbRoyaltyType.setSelectedIndex(-1);
		cbbRoyaltyType.addActionListener(this);
		panelDataEntry.add(cbbRoyaltyType);

		//Buttons panel
		JPanel panelButtons = new JPanel();
		getContentPane().add(panelButtons, BorderLayout.SOUTH);
		panelButtons.setLayout(new GridLayout(1, 2, 0, 0));

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(this);
		panelButtons.add(btnCancel);

		btnAdd = new JButton("Create Royalty");
		btnAdd.addActionListener(this);
		panelButtons.add(btnAdd);

		//Display Set up
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.revalidate();
	}

	/**Checks user has entered everything correctly for the creation of a new fixed amount royalty, and if so creates it accordingly.
	 * <br>Checks that an amount has been entered, and that it can be parsed as a double.
	 */
	protected void addFixedAmount() {
		if (tfFixedAmount.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Error: An amount in USD is required to create fixed-amount royalty.", 
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		try {
			Double fixedAmount = Double.parseDouble(tfFixedAmount.getText());
			for (Channel ch : SalesHistory.get().getListChannels()) {
				ch.addRoyalty(book, (Person) cbbRoyaltyHolder.getSelectedItem(), new RoyaltyFixedAmount(fixedAmount));
			}
		} catch (NumberFormatException error) {
			JOptionPane.showMessageDialog(this, "Error: Please enter a valid amount (must be a number).", 
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		this.dispose();
	}

	/**Checks user has entered everything correctly for the creation of a new fixed percentage royalty, and if so creates it accordingly.
	 * <br>Checks that a percentage has been entered, and that it can be parsed as a double, and that it is between 1-100.
	 */
	protected void addFixedPercentage() {
		if (tfFixedPercentage.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Error: A percentage is required to create fixed-percentage royalty.", 
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		try {
			Double percentage = Double.parseDouble(tfFixedPercentage.getText());
			if (percentage < 1 || percentage > 100) {
				throw new NumberFormatException();
			}
			for (Channel ch : SalesHistory.get().getListChannels()) {
				ch.addRoyalty(book, (Person) cbbRoyaltyHolder.getSelectedItem(), new RoyaltyPercentage(percentage/100));
			}
		} catch (NumberFormatException error) {
			JOptionPane.showMessageDialog(this, "Error: Please enter a valid percentage (must be a number between 1-100).", 
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		this.dispose();
	}

	/**Checks user has entered everything correctly for the creation of a new units sold royalty, and if so creates it accordingly.
	 * <br>Checks that a default percentage, a range1 and a range1percentage have been entered.
	 * <br>Checks that percentages can be parsed as doubles and are between 1-100.
	 * <br>Checks that ranges are of the correct format.
	 */
	protected void addUnitsSold() {
		if (tfDefaultPercentage.getText().isEmpty() || tfRange1.getText().isEmpty() || tfRange1Percentage.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Error: At least one range and one percentage, and a default percentage, "
					+ "are required to create units-sold royalty.", 
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		Double defaultPercentage = checkAndReturnPercentage(tfDefaultPercentage.getText());
		HashMap<Integer[], Double> ranges = new HashMap<Integer[], Double>();

		Double range1Percentage = checkAndReturnPercentage(tfRange1Percentage.getText());
		Integer[] range1 = checkAndReturnRange(tfRange1.getText());
		if (range1 == null || range1Percentage == null || defaultPercentage == null) { //because the checkAndReturn methods return null if check failed
			return;
		}
		ranges.put(range1, range1Percentage/100);

		//If there are inputs for both range2 and range2Percentage, process them
		if(!tfRange2.getText().isEmpty() && !tfRange2Percentage.getText().isEmpty()) {
			Integer[] range2 = checkAndReturnRange(tfRange2.getText());
			Double range2Percentage = checkAndReturnPercentage(tfRange2Percentage.getText());
			if (range1 == null || range1Percentage == null) { //because the checkAndReturn methods return null if check failed
				return;
			}
			ranges.put(range2, range2Percentage/100);
		}

		for (Channel ch : SalesHistory.get().getListChannels()) {
			ch.addRoyalty(book, (Person) cbbRoyaltyHolder.getSelectedItem(), new RoyaltyDependentOnUnitsSold(ranges, defaultPercentage/100));
		}
		this.dispose();
	}
}
