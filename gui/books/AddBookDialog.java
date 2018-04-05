package gui.books;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.Book;
import main.ObjectFactory;
import main.Person;
import main.SalesHistory;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

/**JDialog which requests user input to create a new Book and add it to SalesHistory's list of books.
 * @author crhm
 *
 */
@SuppressWarnings("serial")
public class AddBookDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField tfTitle;
	private JTextField tfIdentifiers;
	private JTextField tfOtherTitles;
	private JComboBox<String> cBAuthor;
	private JComboBox<String> cBSecondAuthor;

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
			lblTitle.setToolTipText("Entering a title is compulsory. No book can be created without a title.");
			lblTitle.setBounds(74, 6, 48, 31);
			lblTitle.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblTitle);
		}
		{
			tfTitle = new JTextField();
			tfTitle.setToolTipText("Entering a title is compulsory. No book can be created without a title.");
			tfTitle.setBounds(195, 11, 220, 31);
			contentPanel.add(tfTitle);
			tfTitle.setColumns(10);
		}
		{
			JLabel lblAuthor = new JLabel("Author:");
			lblAuthor.setToolTipText("To create a new author, please go to the Author Pane.");
			lblAuthor.setBounds(29, 95, 93, 37);
			lblAuthor.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblAuthor);
		}
		{
			JLabel lblIdentifiers = new JLabel("Identifier(s):");
			lblIdentifiers.setToolTipText("Such as ISBN, e-IBSN, ISBN-13, etc...");
			lblIdentifiers.setBounds(17, 171, 105, 48);
			lblIdentifiers.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblIdentifiers);
		}
		{
			tfIdentifiers = new JTextField();
			tfIdentifiers.setToolTipText("To define multiple identifiers, separate them by a comma (i.e. 123456789, 987654321)");
			tfIdentifiers.setBounds(195, 180, 220, 31);
			contentPanel.add(tfIdentifiers);
			tfIdentifiers.setColumns(10);
		}
		
		//Making an array of authors
		String[] listAuthors = new String[SalesHistory.get().getListAuthors().size()];
		int count = 0;
		for (Person p : SalesHistory.get().getListAuthors()) {
			listAuthors[count] = p.getName();
			count++;
		}
		Arrays.sort(listAuthors);
		DefaultComboBoxModel<String> model1 = new DefaultComboBoxModel<String>(listAuthors);
		DefaultComboBoxModel<String> model2 = new DefaultComboBoxModel<String>(listAuthors);

		
		cBAuthor = new JComboBox<String>();
		cBAuthor.setBounds(195, 101, 220, 27);
		cBAuthor.setModel(model1);
		cBAuthor.insertItemAt("", 0);
		cBAuthor.setSelectedIndex(0);
		contentPanel.add(cBAuthor);
		
		JLabel lblOtherTitles = new JLabel("Other Titles:");
		lblOtherTitles.setToolTipText("(Optional) Alternative spellings, Subtitle...");
		lblOtherTitles.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOtherTitles.setBounds(35, 58, 87, 25);
		contentPanel.add(lblOtherTitles);
		
		tfOtherTitles = new JTextField();
		tfOtherTitles.setToolTipText("If there are more than one, separate them with commas.");
		tfOtherTitles.setBounds(195, 54, 220, 31);
		contentPanel.add(tfOtherTitles);
		tfOtherTitles.setColumns(10);
		
		JLabel lblSecondAuthor = new JLabel("Second Author:");
		lblSecondAuthor.setToolTipText("(Optional) Translator, Cover Designer...");
		lblSecondAuthor.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSecondAuthor.setBounds(17, 138, 105, 31);
		contentPanel.add(lblSecondAuthor);
		
		cBSecondAuthor = new JComboBox<String>();
		cBSecondAuthor.setToolTipText("(Optional) Translator, Cover Designer...");
		cBSecondAuthor.setBounds(195, 140, 220, 27);
		cBSecondAuthor.setModel(model2);
		cBSecondAuthor.insertItemAt("", 0);
		cBSecondAuthor.setSelectedIndex(0);
		contentPanel.add(cBSecondAuthor);

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
						String author = "";
						if (window.cBAuthor.getSelectedItem() != null) {
							author = (String) window.cBAuthor.getSelectedItem();
						}						
						String identifiers = window.tfIdentifiers.getText();
						String otherTitles = window.tfOtherTitles.getText();
						
						String otherAuthor = "";
						if (window.cBSecondAuthor.getSelectedItem() != null) {
							otherAuthor = (String) window.cBSecondAuthor.getSelectedItem();
						}
						if (title == null || title.isEmpty()) { //Does not allow empty title, shows an error message
							JOptionPane.showMessageDialog(window, "Error: A book title is required to create a book. Please input a title.", 
									"Error", JOptionPane.ERROR_MESSAGE);
							return; //stop here and don't do anything below since there is no title
						}

						Book newBook = ObjectFactory.createBook(title, author, "");

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
						
						//Adding other titles if any
						String[] otherTitlesSeparated = null;
						if (otherTitles.contains(",") ) {
							otherTitlesSeparated = otherTitles.split(",");
						}
						if (otherTitlesSeparated != null) {
							for (String s : otherTitlesSeparated) {
								newBook.addTitle(s.trim());
							}
						} else if (!otherTitles.isEmpty()){
							newBook.addTitle(otherTitles.trim());
						}
						
						//Adding other author if any
						if (!otherAuthor.isEmpty()) {
							newBook.addAuthor(SalesHistory.get().getPerson(otherAuthor));
						}

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
