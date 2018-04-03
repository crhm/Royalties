package gui.books;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.Book;
import main.SalesHistory;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**JDialog which requests user input to create a new Book and add it to SalesHistory's list of books.
 * @author crhm
 *
 */
@SuppressWarnings("serial")
public class AddBookDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField tfTitle;
	private JTextField tfAuthor;
	private JTextField tfIdentifiers;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			new AddBookDialog();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AddBookDialog() {
		setTitle("Create A New Book");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		//Setting up labels and text fields
		{
			JLabel lblTitle = new JLabel("Title:");
			lblTitle.setBounds(5, 5, 220, 76);
			lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblTitle);
		}
		{
			tfTitle = new JTextField();
			tfTitle.setToolTipText("Entering a title is compulsory. No book can be created without a title.");
			tfTitle.setBounds(195, 28, 220, 31);
			contentPanel.add(tfTitle);
			tfTitle.setColumns(10);
		}
		{
			JLabel lblAuthor = new JLabel("Author:");
			lblAuthor.setBounds(5, 81, 220, 76);
			lblAuthor.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblAuthor);
		}
		{
			tfAuthor = new JTextField();
			tfAuthor.setBounds(195, 104, 220, 31);
			contentPanel.add(tfAuthor);
			tfAuthor.setColumns(10);
		}
		{
			JLabel lblIdentifiers = new JLabel("Identifier(s):");
			lblIdentifiers.setToolTipText("Such as ISBN, e-IBSN, ISBN-13, etc...");
			lblIdentifiers.setBounds(5, 157, 220, 76);
			lblIdentifiers.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblIdentifiers);
		}
		{
			tfIdentifiers = new JTextField();
			tfIdentifiers.setToolTipText("To define multiple identifiers, separate them by a comma (i.e. 123456789, 987654321)");
			tfIdentifiers.setBounds(195, 180, 220, 31);
			contentPanel.add(tfIdentifiers);
			tfIdentifiers.setColumns(10);
		}

		//Setting up buttons and actions
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{ //Cancel button
				AddBookDialog window = this;
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setToolTipText("Exit without saving/adding a book.");
				cancelButton.addActionListener(new ActionListener() { //Window closes if cancel button is pressed
					public void actionPerformed(ActionEvent e) {
						window.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
			{ //Add Button
				AddBookDialog window = this;
				JButton addButton = new JButton("Add");
				addButton.setToolTipText("Create a book with the above details and add it to the list of books managed by PLP.");
				addButton.addActionListener(new ActionListener() { //Book is created from info in text fields and added to SalesHistory's list
					public void actionPerformed(ActionEvent e) {
						String title = window.tfTitle.getText();
						String author = window.tfAuthor.getText();
						String identifiers = window.tfIdentifiers.getText();
						if (title == null || title.isEmpty()) { //Does not allow empty title, shows an error message
							JOptionPane.showMessageDialog(window, "Error: A book title is required to create a book. Please input a title.", 
									"Error", JOptionPane.ERROR_MESSAGE);
							return; //stop here and don't do anything below since there is no title
						}

						Book newBook = new Book(title, null, ""); //TODO fix - so far defaults to null author because needs to implement drop down

						//Making sure that if several identifiers are inputted, they are all added separately
						String[] identifiersSeparated = null;
						if (identifiers.contains(",") ) {
							identifiersSeparated = identifiers.split(",");
						}
						if (identifiersSeparated != null) {
							for (String s : identifiersSeparated) {
								newBook.addIdentifier(s.trim());
							}
						} else if (!identifiers.isEmpty()){
							newBook.addIdentifier(identifiers.trim());
						}

						SalesHistory.get().addBook(newBook);
						window.dispose();
					}
				});
				addButton.setActionCommand("OK");
				buttonPane.add(addButton);
				getRootPane().setDefaultButton(addButton);

				this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				this.setVisible(true);

			}
		}
	}

}
