package gui.books;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.Book;

public class EditBookDialog extends JFrame implements ActionListener {

	private static final long serialVersionUID = -3471608692321566363L;
	JPanel contentPanel;
	JButton confirmButton;
	JButton cancelButton;
	JLabel titleLabel;
	JLabel authorLabel;
	JLabel identifiersLabel;
	JLabel unitsSoldLabel;
	JTextField titleTF;
	JTextField authorTF;
	JTextField identifiersTF;
	JTextField unitsSoldTF;
	
	Book book;
	
	public EditBookDialog(Book b) {
		super();
		this.setTitle("Edit Book Details");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		book = b;
		
		contentPanel = new JPanel(new GridLayout(5, 2));
		contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		confirmButton = new JButton("Save");
		cancelButton = new JButton("Cancel");
		confirmButton.addActionListener(this);
		cancelButton.addActionListener(this);
		
		titleLabel = new JLabel("Title:");
		authorLabel = new JLabel("Author:");
		identifiersLabel = new JLabel("Identifiers:");
		unitsSoldLabel = new JLabel("Total Units Sold:");
		
		titleTF = new JTextField(b.getTitle());
		titleTF.setToolTipText("A title is required.");
		authorTF = new JTextField(b.getAuthor().getName());
		authorTF.setToolTipText("Note that changing the author does not entail changing the royalty holder for this book. "
				+ "To do so, please go in the Royalty Details Section.");
		String identifiers = "";
		int counter = 0;
		for (String s : b.getIdentifiers()) { //So that no brackets appear in edit box which could confuse user
			if (counter != 0) {
				identifiers = identifiers.concat(", ");
			}
			identifiers = identifiers.concat(s);
			counter++;
		}
		identifiersTF = new JTextField(identifiers);
		identifiersTF.setToolTipText("To define multiple identifiers, separate them by a comma (i.e. 123456789, 987654321)");
		unitsSoldTF = new JTextField("" + b.getTotalUnitsSold());
		unitsSoldTF.setToolTipText("Warning: changing the number of units sold manually will create a discrepancy with the sales history.");
		
		contentPanel.add(titleLabel);
		contentPanel.add(titleTF);
		contentPanel.add(authorLabel);
		contentPanel.add(authorTF);
		contentPanel.add(identifiersLabel);
		contentPanel.add(identifiersTF);
		contentPanel.add(unitsSoldLabel);
		contentPanel.add(unitsSoldTF);
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
			//TODO make it so for title, identifiers and units sold
			String title = titleTF.getText().trim();
			if (title == null || title.isEmpty()) { //Does not allow empty title, shows an error message
				JOptionPane.showMessageDialog(this, "Error: A book title is required. Please input a title for this book.", 
						"Error", JOptionPane.ERROR_MESSAGE);
				return; //stop here and don't do anything below since there is no title
			}

			String author = authorTF.getText().trim();
//			String identifiers = identifiersTF.getText().trim();
//			String unitsSold = unitsSoldTF.getText().trim();
			
			book.setAuthor(null); //TODO Fix
			
			this.dispose();
		}
	}	
	
}
