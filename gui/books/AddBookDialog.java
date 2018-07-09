package gui.books;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

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
import java.util.Comparator;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

/**JFrame which requests user input to create a new Book and add it to SalesHistory's list of books.
 * @author crhm
 *
 */
@SuppressWarnings("serial")
public class AddBookDialog extends JFrame implements ActionListener {
	
	private JPanel panelButtons;
	private JPanel panelLabels;
	private JPanel panelEntries;
	private JButton bttnConfirm;
	private JButton bttnCancel;
	private JLabel lblTitle;
	private JLabel lblAuthor1;
	private JLabel lblAuthor2;
	private JLabel lblTranslator;
	private JLabel lblAfterwordAuthor;
	private JLabel lblPrefaceAuthor;
	private JLabel lblIdentifiers;
	private JLabel lblOtherTitles;
	private JTextField tfTitle;
	private JTextField tfIdentifiers;
	private JTextField tfOtherTitles;
	private JComboBox<Person> cBAuthor1;
	private JComboBox<Person> cBAuthor2;
	private JComboBox<Person> cBPrefaceAuthor;
	private JComboBox<Person> cBAfterwordAuthor;
	private JComboBox<Person> cBTranslator;

	public AddBookDialog() {
		super();
		this.setTitle("Create A New Book");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		getContentPane().setLayout(new BorderLayout(0, 0));

		//BUTTONS
		panelButtons = new JPanel();
		getContentPane().add(panelButtons, BorderLayout.SOUTH);
		panelButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

		bttnCancel = new JButton("Cancel");
		panelButtons.add(bttnCancel);

		bttnConfirm = new JButton("Save");
		panelButtons.add(bttnConfirm);

		bttnConfirm.addActionListener(this);
		bttnCancel.addActionListener(this);


		//LABELS
		panelLabels = new JPanel();
		panelLabels.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		getContentPane().add(panelLabels, BorderLayout.WEST);
		panelLabels.setLayout(new GridLayout(8, 1, 0, 0));

		lblTitle = new JLabel("Title:");
		lblTitle.setToolTipText("Entering a title is compulsory. No book can be created without a title.");
		lblTitle.setHorizontalAlignment(SwingConstants.RIGHT);
		panelLabels.add(lblTitle);

		lblOtherTitles = new JLabel("Other Titles:");
		lblOtherTitles.setToolTipText("(Optional) Alternative spellings, Subtitle...");
		lblOtherTitles.setHorizontalAlignment(SwingConstants.RIGHT);
		panelLabels.add(lblOtherTitles);

		lblIdentifiers = new JLabel("Identifiers:");
		lblIdentifiers.setToolTipText("Such as ISBN, e-IBSN, ISBN-13, etc...");
		lblIdentifiers.setHorizontalAlignment(SwingConstants.RIGHT);
		panelLabels.add(lblIdentifiers);

		lblAuthor1 = new JLabel("Main Author:");
		lblAuthor1.setToolTipText("To create a new author, write their name instead of selecting one from the list.");
		lblAuthor1.setHorizontalAlignment(SwingConstants.RIGHT);
		panelLabels.add(lblAuthor1);

		lblAuthor2 = new JLabel("Secondary Author");
		lblAuthor2.setToolTipText("To create a new author, write their name instead of selecting one from the list.");
		panelLabels.add(lblAuthor2);

		lblTranslator = new JLabel("Translator:");
		lblTranslator.setToolTipText("To create a new author, write their name instead of selecting one from the list.");
		lblTranslator.setHorizontalAlignment(SwingConstants.RIGHT);
		panelLabels.add(lblTranslator);

		lblPrefaceAuthor = new JLabel("Preface Author:");
		lblPrefaceAuthor.setToolTipText("To create a new author, write their name instead of selecting one from the list.");
		lblPrefaceAuthor.setHorizontalAlignment(SwingConstants.RIGHT);
		panelLabels.add(lblPrefaceAuthor);

		lblAfterwordAuthor = new JLabel("Afterword Author:");
		lblAfterwordAuthor.setToolTipText("To create a new author, write their name instead of selecting one from the list.");
		lblAfterwordAuthor.setHorizontalAlignment(SwingConstants.RIGHT);
		panelLabels.add(lblAfterwordAuthor);

		//ENTRIES
		panelEntries = new JPanel();
		panelEntries.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		getContentPane().add(panelEntries, BorderLayout.CENTER);
		panelEntries.setLayout(new GridLayout(8, 1, 0, 0));

		tfTitle = new JTextField();
		tfTitle.setToolTipText("A title is required.");
		panelEntries.add(tfTitle);

		tfOtherTitles = new JTextField();
		tfOtherTitles.setToolTipText("If there are more than one, separate them with commas.");
		panelEntries.add(tfOtherTitles);

		tfIdentifiers = new JTextField();
		tfIdentifiers.setToolTipText("To define multiple identifiers, separate them by a comma (i.e. 123456789, 987654321)");
		panelEntries.add(tfIdentifiers);

		makeAuthorsComboBoxes();

		panelEntries.add(cBAuthor1);
		panelEntries.add(cBAuthor2);
		panelEntries.add(cBTranslator);
		panelEntries.add(cBPrefaceAuthor);
		panelEntries.add(cBAfterwordAuthor);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.revalidate();
	}

	private void makeAuthorsComboBoxes() {
		//Making an array of authors
		Person[] listAuthors = new Person[SalesHistory.get().getListAuthors().size()];
		int count = 0;
		for (Person p : SalesHistory.get().getListAuthors()) {
			listAuthors[count] = p;
			count++;
		}
		Arrays.sort(listAuthors, new Comparator<Person>() {

			@Override
			public int compare(Person o1, Person o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		DefaultComboBoxModel<Person> model1 = new DefaultComboBoxModel<Person>(listAuthors);
		DefaultComboBoxModel<Person> model2 = new DefaultComboBoxModel<Person>(listAuthors);
		DefaultComboBoxModel<Person> model3 = new DefaultComboBoxModel<Person>(listAuthors);
		DefaultComboBoxModel<Person> model4 = new DefaultComboBoxModel<Person>(listAuthors);
		DefaultComboBoxModel<Person> model5 = new DefaultComboBoxModel<Person>(listAuthors);

		//AUTHOR 1
		cBAuthor1 = new JComboBox<Person>();
		cBAuthor1.setModel(model1);

		cBAuthor1.insertItemAt(null, 0);
		cBAuthor1.setSelectedIndex(0);
		cBAuthor1.setEditable(true);

		//AUTHOR 2
		cBAuthor2 = new JComboBox<Person>();
		cBAuthor2.setModel(model2);
		cBAuthor2.insertItemAt(null, 0);
		cBAuthor2.setSelectedIndex(0);
		cBAuthor2.setEditable(true);

		//TRANSLATOR
		cBTranslator = new JComboBox<Person>();
		cBTranslator.setModel(model3);
		cBTranslator.insertItemAt(null, 0);
		cBTranslator.setSelectedIndex(0);
		cBTranslator.setEditable(true);

		//PREFACE AUTHOR
		cBPrefaceAuthor = new JComboBox<Person>();
		cBPrefaceAuthor.setModel(model4);
		cBPrefaceAuthor.insertItemAt(null, 0);
		cBPrefaceAuthor.setSelectedIndex(0);
		cBPrefaceAuthor.setEditable(true);

		//AFTERWORD AUTHOR
		cBAfterwordAuthor = new JComboBox<Person>();
		cBAfterwordAuthor.setModel(model5);
		cBAfterwordAuthor.insertItemAt(null, 0);
		cBAfterwordAuthor.setSelectedIndex(0);
		cBAfterwordAuthor.setEditable(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bttnCancel) {
			this.dispose();
		} else if (e.getSource() == bttnConfirm) {
			String title = this.tfTitle.getText();
			Person author1 = null;
			Person author2 = null;
			Person translator = null;
			Person prefaceAuthor = null;
			Person afterwordAuthor = null;
			try {
				author1 = (Person) this.cBAuthor1.getSelectedItem();
			} catch (ClassCastException newAuthorExc) {
				if (this.cBAuthor1.getSelectedItem() != null && !((String) this.cBAuthor1.getSelectedItem()).isEmpty()) {
					author1 = ObjectFactory.createPerson((String) this.cBAuthor1.getSelectedItem());	
				} 
			}
			
			try {
				author2 = (Person) this.cBAuthor2.getSelectedItem();
			} catch (ClassCastException newAuthorExc) {
				if (this.cBAuthor2.getSelectedItem() != null && !((String) this.cBAuthor2.getSelectedItem()).isEmpty()) {
					author2 = ObjectFactory.createPerson((String) this.cBAuthor2.getSelectedItem());	
				} 
			}
			
			try {
				translator = (Person) this.cBTranslator.getSelectedItem();
			} catch (ClassCastException newAuthorExc) {
				if (this.cBTranslator.getSelectedItem() != null && !((String) this.cBTranslator.getSelectedItem()).isEmpty()) {
					translator = ObjectFactory.createPerson((String) this.cBTranslator.getSelectedItem());	
				} 
			}
			
			try {
				prefaceAuthor = (Person) this.cBPrefaceAuthor.getSelectedItem();
			} catch (ClassCastException newAuthorExc) {
				if (this.cBPrefaceAuthor.getSelectedItem() != null && !((String) this.cBPrefaceAuthor.getSelectedItem()).isEmpty()) {
					prefaceAuthor = ObjectFactory.createPerson((String) this.cBPrefaceAuthor.getSelectedItem());	
				} 
			}
			
			try {
				afterwordAuthor = (Person) this.cBAfterwordAuthor.getSelectedItem();
			} catch (ClassCastException newAuthorExc) {
				if (this.cBAfterwordAuthor.getSelectedItem() != null && !((String) this.cBAfterwordAuthor.getSelectedItem()).isEmpty()) {
					afterwordAuthor = ObjectFactory.createPerson((String) this.cBAfterwordAuthor.getSelectedItem());	
				} 
			}
			
			String identifiers = this.tfIdentifiers.getText();
			String otherTitles = this.tfOtherTitles.getText();

			if (title == null || title.isEmpty()) { //Does not allow empty title, shows an error message
				JOptionPane.showMessageDialog(this, "Error: A book title is required to create a book. Please input a title.", 
						"Error", JOptionPane.ERROR_MESSAGE);
				return; //stop here and don't do anything below since there is no title
			}

			Book newBook = ObjectFactory.createBook(title, "", "");
			newBook.setAuthor1(author1);
			newBook.setAuthor2(author2);
			newBook.setTranslator(translator);
			newBook.setPrefaceAuthor(prefaceAuthor);
			newBook.setAfterwordAuthor(afterwordAuthor);

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

			this.dispose();
		}
	}
}	
