package gui.royalties;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.Book;
import main.Person;
import main.SalesHistory;
import main.royalties.IRoyaltyType;
import main.royalties.RoyaltyDependentOnUnitsSold;
import main.royalties.RoyaltyFixedAmount;
import main.royalties.RoyaltyPercentage;

public class EditRoyaltyDialog extends JFrame implements ActionListener {

	private static final long serialVersionUID = -1944767798953689020L;
	JPanel contentPanel;
	JButton confirmButton;
	JButton cancelButton;
	JLabel personLabel;
	JLabel royaltyTypeLabel;
	JLabel personNameLabel;
	JComboBox<String> royaltyTypeCB;
	JTextField royaltyPercentageTF;
	JTextField fixedAmountTF;
	
	Person person;
	Book book;
	IRoyaltyType royaltyType;
	
	public EditRoyaltyDialog(String channelName, String bookTitle, String personName) {
		super();
		this.setTitle("Edit Royalty Details");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		person = SalesHistory.get().getListRoyaltyHolders().get(personName);
		book = SalesHistory.get().getListPLPBooks().get(bookTitle);
		royaltyType = SalesHistory.get().getListChannels().get(channelName).getListRoyalties().get(book).get(person);
		
		contentPanel = new JPanel(new GridLayout(4, 2));
		contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		confirmButton = new JButton("Save");
		cancelButton = new JButton("Cancel");
		confirmButton.addActionListener(this);
		cancelButton.addActionListener(this);
		
		personLabel = new JLabel("Name of Royalty Holder:");
		royaltyTypeLabel = new JLabel("Royalty Type:");
		
		personNameLabel = new JLabel(person.getName());
		String[] royaltyTypes = {"Percentage-Based Royalty", "Units Sold-Based Royalty", "Fixed-Amount Royalty"};
		royaltyTypeCB = new JComboBox<String>(royaltyTypes);
		
		contentPanel.add(personLabel);
		contentPanel.add(personNameLabel);
		contentPanel.add(royaltyTypeLabel);
		contentPanel.add(royaltyTypeCB);
		
		if (royaltyType instanceof RoyaltyPercentage) {
			royaltyTypeCB.setSelectedItem("Percentage-Based Royalty");
			String percentage = ((RoyaltyPercentage) royaltyType).getPercentage() + "";
			royaltyPercentageTF = new JTextField(percentage);
			contentPanel.add(new JLabel("Percentage Applied:"));
			contentPanel.add(royaltyPercentageTF);
		} else if (royaltyType instanceof RoyaltyDependentOnUnitsSold){
			royaltyTypeCB.setSelectedItem("Units Sold-Based Royalty");
			contentPanel.add(new JLabel());
			contentPanel.add(new JLabel());
			//TODO figure out how to display AND retrieve edited royalty info!
		} else {
			royaltyTypeCB.setSelectedItem("Fixed-Amount Royalty");
			contentPanel.add(new JLabel("Fixed Amount ($):"));
			String amount = ((RoyaltyFixedAmount) royaltyType).getFixedAmount() + "";
			fixedAmountTF = new JTextField(amount);
			contentPanel.add(fixedAmountTF);
		}

		contentPanel.add(cancelButton);
		contentPanel.add(confirmButton);
		
		
		this.setContentPane(contentPanel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.revalidate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cancelButton) {
			this.dispose();
		} else if (e.getSource() == confirmButton) {
		//TODO
		}
	}
	
	
}
