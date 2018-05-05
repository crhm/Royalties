package gui.royalties;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import main.Person;
import main.SalesHistory;

import javax.swing.JTextField;

@SuppressWarnings("serial")
public class AddRoyaltyDialog extends JFrame implements ActionListener {
	private JPanel panelDataEntryDependent;

	private JComboBox<Person> cbbRoyaltyHolder;
	private JComboBox<String> cbbRoyaltyType;

	private JTextField tfFixedPercentage;
	private JLabel lblFixedPercentage;

	private JLabel lblFixedAmount;
	private JTextField tfFixedAmount;

	private JTextField tfDefaultPercentage;
	private JLabel lblDefaultPercentage;
	private JTextField tfRange1Percentage;
	private JTextField tfRange2Percentage;
	private JLabel lblRange1Percentage;
	private JLabel lblRange2Percentage;
	private JTextField tfRange1;
	private JTextField tfRange2;
	private JLabel lblRange1;
	private JLabel lblRange2;

	private JButton btnCancel;
	private JButton btnAdd;

	public AddRoyaltyDialog() {
		super();
		setTitle("Create A New Royalty");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel panelBoth = new JPanel(new GridLayout(2, 1, 0, 0));
		JPanel panelDataEntry = new JPanel(new GridLayout(2, 2, 0, 0));
		panelDataEntryDependent = new JPanel();
		panelBoth.add(panelDataEntry);
		panelBoth.add(panelDataEntryDependent);
		getContentPane().add(panelBoth, BorderLayout.CENTER);

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
			public int compare(Person p1, Person p2) {
				return p1.getName().compareTo(p2.getName());
			}
		});
		DefaultComboBoxModel<Person> modelPersons = new DefaultComboBoxModel<Person>(listPersons);
		cbbRoyaltyHolder = new JComboBox<Person>();
		cbbRoyaltyHolder.setModel(modelPersons);
		cbbRoyaltyHolder.setSelectedIndex(-1);
		panelDataEntry.add(cbbRoyaltyHolder);

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

		JPanel panelButtons = new JPanel();
		getContentPane().add(panelButtons, BorderLayout.SOUTH);
		panelButtons.setLayout(new GridLayout(1, 2, 0, 0));

		btnCancel = new JButton("Cancel");
		panelButtons.add(btnCancel);

		btnAdd = new JButton("Create Royalty");
		panelButtons.add(btnAdd);
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.revalidate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cbbRoyaltyType) {
			if (((String) cbbRoyaltyType.getSelectedItem()).equals("Fixed Amount Royalty")) {
				panelDataEntryDependent.removeAll();
				panelDataEntryDependent.setLayout(new GridLayout(1, 2, 0, 0));
				lblFixedAmount = new JLabel("Fixed Amount: (USD)");
				tfFixedAmount = new JTextField("");
				panelDataEntryDependent.add(lblFixedAmount);
				panelDataEntryDependent.add(tfFixedAmount);
				this.revalidate();
				this.pack();
				this.repaint();
			} else if (((String) cbbRoyaltyType.getSelectedItem()).equals("Fixed Percentage Royalty")) {
				panelDataEntryDependent.removeAll();
				panelDataEntryDependent.setLayout(new GridLayout(1, 2, 0, 0));
				lblFixedPercentage = new JLabel("Fixed Percentage:");
				tfFixedPercentage = new JTextField("");
				panelDataEntryDependent.add(lblFixedPercentage);
				panelDataEntryDependent.add(tfFixedPercentage);
				this.revalidate();
				this.pack();
				this.repaint();
			} else if (((String) cbbRoyaltyType.getSelectedItem()).equals("Units Sold Royalty")) {
				panelDataEntryDependent.removeAll();
				panelDataEntryDependent.setLayout(new GridLayout(5, 2, 0, 0));
				tfDefaultPercentage = new JTextField();
				lblDefaultPercentage = new JLabel("Percentage to apply when outside of either range:");
				tfRange1Percentage = new JTextField();
				tfRange2Percentage = new JTextField();
				lblRange1Percentage = new JLabel("Percentage for Range 1:");
				lblRange2Percentage = new JLabel("Percentage for Range 2:");
				tfRange1 = new JTextField();
				tfRange2 = new JTextField();
				lblRange1 = new JLabel("Range of units sold 1:");
				lblRange2 = new JLabel("Range of units sold 2:");

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

		}

	}

}
