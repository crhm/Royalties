package gui.persons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import main.Person;

@SuppressWarnings("serial")
public class EditPersonDialog extends JFrame implements ActionListener {
	private JTextField tfName;
	private JTextField tfOtherNames;
	private JButton btnCancel;
	private JButton btnConfirm;
	private Person person;

	public EditPersonDialog(Person p) {
		super();
		person = p;

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Edit Person Details");

		JPanel panelDataEntry = new JPanel();
		getContentPane().add(panelDataEntry, BorderLayout.CENTER);
		panelDataEntry.setLayout(new GridLayout(3, 2, 0, 0));

		JLabel lblName = new JLabel("Person Name:");
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		panelDataEntry.add(lblName);

		tfName = new JTextField(person.getName());
		panelDataEntry.add(tfName);
		tfName.setColumns(10);

		JLabel lblOtherNames = new JLabel("Name Variations:");
		lblOtherNames.setHorizontalAlignment(SwingConstants.CENTER);
		panelDataEntry.add(lblOtherNames);

		String otherNames = "";
		Boolean isFirstName = true;
		for (String s : person.getListNames()) {
			if (!s.equals(person.getName())) {
				if (isFirstName) {
					otherNames = otherNames.concat(s);
					isFirstName = false;
				} else {
					otherNames = otherNames.concat(", " + s);
				}
			}
		}
		tfOtherNames = new JTextField(otherNames);
		panelDataEntry.add(tfOtherNames);
		tfOtherNames.setColumns(10);

		JLabel lblBalance = new JLabel("Balance (USD):");
		lblBalance.setHorizontalAlignment(SwingConstants.CENTER);
		panelDataEntry.add(lblBalance);

		JLabel lblBalanceAmount = new JLabel(Double.toString(person.getBalance()));
		panelDataEntry.add(lblBalanceAmount);

		JPanel panelButtons = new JPanel();
		getContentPane().add(panelButtons, BorderLayout.SOUTH);
		panelButtons.setLayout(new GridLayout(1, 2, 0, 0));

		btnCancel = new JButton("Cancel");
		panelButtons.add(btnCancel);

		btnConfirm = new JButton("Save Changes");
		panelButtons.add(btnConfirm);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.revalidate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
