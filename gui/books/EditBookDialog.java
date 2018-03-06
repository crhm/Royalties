package gui.books;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
		authorTF = new JTextField(b.getAuthor());
		identifiersTF = new JTextField(b.getIdentifiers().toString());
		unitsSoldTF = new JTextField("" + b.getTotalUnitsSold());
		
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
		//TODO
		}
	}	
	
}
