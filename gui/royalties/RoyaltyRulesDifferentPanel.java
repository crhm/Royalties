package gui.royalties;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import main.Book;
import main.Person;
import main.SalesHistory;
import main.royalties.IRoyaltyType;

@SuppressWarnings("serial")
public class RoyaltyRulesDifferentPanel extends JPanel implements ActionListener, ListSelectionListener {

	private JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
	private JButton editButton = new JButton("Edit Selected Royalty");
	private JButton addButton = new JButton("Create New Royalty For Selected Book");
	private JButton deleteButton = new JButton("Delete Selected Royalty");

	private JPanel containerPanel = new JPanel(new GridLayout(1, 2, 5, 0));

	//Displays the book titles for which there are royalty holders (for the selected channel)
	private JTable bookTitles = null; 

	//Displays the details of the royalties for the book selected amongst the book titles. If none are selected, no details are shown
	private JPanel royaltyDetailsPanel = new JPanel(new GridLayout(5, 1, 0, 5)); 
	private JTable royaltiesAmazon;
	private JTable royaltiesKobo;
	private JTable royaltiesNook;
	private JTable royaltiesApple;
	private JTable royaltiesCreatespace;
	private Set<ListSelectionModel> listRoyaltiesTables;

	//Holds the index of selected book title so that in case of sorting the selection can be maintained despite index change
	private int selectionIndexBeforeSort = 0;

	//Reflects whether a royalty is already selected in another table
	private Boolean isAnotherRoyaltySelected = false;

	public RoyaltyRulesDifferentPanel() {
		super();
		this.setLayout(new BorderLayout());

		//Gets the list of book titles
		bookTitles = getTableBooks();
		setTableSettings(bookTitles);

		//Add ListSelectionListener to list of books
		bookTitles.getSelectionModel().addListSelectionListener(this);

		//Adds components to the main container panel
		containerPanel.add(new JScrollPane(bookTitles));
		containerPanel.add(royaltyDetailsPanel);

		//Because no royalty is selected to start with
		deleteButton.setEnabled(false);
		editButton.setEnabled(false);
		addButton.setEnabled(false);

		//Adding ActionListener to buttons
		deleteButton.addActionListener(this);
		addButton.addActionListener(this);
		editButton.addActionListener(this);

		//Set up buttonPanel
		buttonPanel.add(addButton);
		buttonPanel.add(editButton);
		buttonPanel.add(deleteButton);

		//Add both containerPanel and ButtonPanel to main Panel
		this.add(buttonPanel, BorderLayout.NORTH);
		this.add(containerPanel, BorderLayout.CENTER);
	}

	/**When the user selects another channel in the combobox, it changes the the list of book titles 
	 * accordingly and erases displayed royalties data.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == deleteButton) {
			if (bookTitles.getSelectedRow() != -1) {
				//TODO
			}
		} else if (e.getSource() == addButton) {
			if (bookTitles.getSelectedRow() != -1) {
				int bookRow = bookTitles.convertRowIndexToModel(bookTitles.getSelectedRow());
				Long bookNumber = (Long) bookTitles.getModel().getValueAt(bookRow, 0);
				Book book = SalesHistory.get().getBookWithNumber(bookNumber);

				AddRoyaltyDialogOneChannel addRoyaltyDialog = new AddRoyaltyDialogOneChannel(book);
				addRoyaltyDialog.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) { //update and repaint table on close of addRoyaltyDialog
						updateData();
					}				
				});
			}
		} else if (e.getSource() == editButton) {
			//new EditRoyaltyDialog(currentChannel, currentBook, currentPerson);
			//TODO
		}
	}

	/**When a book title is selected in the list of titles, it displays the royalties for that book in the royaltyDetailsPanel panel
	 * as a table with royalty holders and their royalty type (and percentage)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == bookTitles.getSelectionModel()) {
			//Change in the selection of Book Title
			if (bookTitles.getSelectedRow() < 0) { 
				//Sorting caused the selection to be void somehow and return an index of -1
				// So, I clear the selection and set it at the same model index (which is now a different view index)
				bookTitles.clearSelection();
				bookTitles.getSelectionModel().setSelectionInterval(0, bookTitles.convertRowIndexToView(selectionIndexBeforeSort));
			} else {
				//Genuine value change, another book title has been selected
				bookTitles.repaint(); //Without this the change in selection occurs but does not show visually...
				editButton.setEnabled(false);
				deleteButton.setEnabled(false);
				addButton.setEnabled(true);
				isAnotherRoyaltySelected = false;
				royaltyDetailsPanel.removeAll();
				royaltyDetailsPanel.revalidate();
				int tableRow = bookTitles.convertRowIndexToModel(bookTitles.getSelectedRow());
				Long bookNumber = (Long) bookTitles.getModel().getValueAt(tableRow, 0);
				Book book = SalesHistory.get().getBookWithNumber(bookNumber);
				royaltiesAmazon = getTableRoyalties(book, "Amazon");
				royaltiesApple = getTableRoyalties(book, "Apple");
				royaltiesKobo = getTableRoyalties(book, "Kobo");
				royaltiesNook = getTableRoyalties(book, "Nook");
				royaltiesCreatespace = getTableRoyalties(book, "Createspace");
				listRoyaltiesTables = new HashSet<ListSelectionModel>();
				setUpRoyaltiesTable(royaltiesAmazon, "Amazon");
				setUpRoyaltiesTable(royaltiesApple, "Apple");
				setUpRoyaltiesTable(royaltiesKobo, "Kobo");
				setUpRoyaltiesTable(royaltiesNook, "Nook");
				setUpRoyaltiesTable(royaltiesCreatespace, "Createspace");
				royaltyDetailsPanel.revalidate();
				royaltyDetailsPanel.repaint();
//				currentBook = (String) bookTitles.getModel().getValueAt(tableRow, 1); 
			}	
		} else if (listRoyaltiesTables.contains((ListSelectionModel) e.getSource()) 
				&& !((ListSelectionModel) e.getSource()).isSelectionEmpty()) { //Change comes from royalty details, and is not one fired by clearSelection
			editButton.setEnabled(true);
			deleteButton.setEnabled(true);
			if (isAnotherRoyaltySelected) { //clear previous selection if there is one
				for (ListSelectionModel m : listRoyaltiesTables) {
					if (m != (ListSelectionModel) e.getSource()) {
						m.clearSelection();
					}
				}
			}
			isAnotherRoyaltySelected = true;
		}
	}
	
	/**If the table isn't null (aka there are royalties for that channel), set selection mode to single selection, add it to 
	 * royaltyDetails panel, add a listSelectionListener to its SelectionModel (which is added to listRoyaltiesTable).
	 * <br>If the table is null, instead add to royaltyDetailsPanel  a label saying there were no royalties found for that channel.
	 * @param table table of royalties for that channel (may be null)
	 * @param channelName name of channel
	 */
	private void setUpRoyaltiesTable(JTable table, String channelName) {
		if (table != null) {
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			royaltyDetailsPanel.add(table);
			table.getSelectionModel().addListSelectionListener(this);
			listRoyaltiesTables.add(table.getSelectionModel());
		} else {
			royaltyDetailsPanel.add(new JLabel("No royalties found for " + channelName));
		}
	}

	/**Returns a table contains the royalty information for the book passed as argument
	 * @param b Book of which we want the royalty details
	 * @return a JTable (not sortable, not editable) containing royalties data for that book
	 */
	private JTable getTableRoyalties(Book b, String channelName) {
		String[] columnNames = {"Channel", "Royalty Holder", "Royalty"};
		if (getDataRoyalties(b, channelName) != null) {
			DefaultTableModel model = new DefaultTableModel(getDataRoyalties(b, channelName), columnNames) {
				@Override
				public Class<?> getColumnClass(int column) {
					switch (column ) {
					default : return String.class;
					}
				}
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			JTable table = new JTable(model);
			table.getColumnModel().getColumn(0).setMaxWidth(120);
			//Disables the user-reordering table columns
			table.getTableHeader().setReorderingAllowed(false);

			return table;
		} else {
			return null;
		}	
	}

	/**Obtains the data to be put into a table model for the royalties of the book passed as argument
	 * @param b Book whose royalty data we want
	 * @return The royalties data to be put in to a tablemodel
	 */
	private Object[][] getDataRoyalties(Book b, String channelName){
		HashMap<Person, IRoyaltyType> listRoyalties = SalesHistory.get().getChannel(channelName).getListRoyalties().get(b);
		if (listRoyalties != null) {
			int numberOfRows = listRoyalties.keySet().size();
			Object[][] data = new Object[numberOfRows][3];
			int count = 0;
			for (Person p : listRoyalties.keySet()) {
				data[count][0] = channelName;
				data[count][1] = p.getName();
				data[count][2] = listRoyalties.get(p);
				count++;
			}
			return data;
		} else {
			return null;
		}
	}

	/**Returns a JTable containing the list of book titles that have royalties for the currently selected channel
	 * @return the JTable of book titles (sortable alphabetically, single selection only, not editable)
	 */
	private JTable getTableBooks() {
		String[] columnNames = {"Book Number", "Book Title"};
		DefaultTableModel model = new DefaultTableModel(getDataBooks(), columnNames) {
			@Override
			public Class<?> getColumnClass(int column) {
				switch (column ) {
				case 0 : return Long.class;
				default : return String.class;
				}
			}
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}	
		};
		JTable table = new JTable(model);

		return table;
	}

	/**Returns the data to be put into a tablemodel for the list of books 
	 * 
	 * @return data to be put into a tablemodel for the list of books
	 */
	private Object[][] getDataBooks(){
		Set<Book> listBooks = new HashSet<Book>();
		for (Book b : SalesHistory.get().getListPLPBooks()) {
			if (!SalesHistory.get().getUniformRoyalties().keySet().contains(b)) {
				listBooks.add(b);
			} else {
			}
		}
		int numberOfRows = listBooks.size();
		Object[][] data = new Object[numberOfRows][2];
		int count = 0;

		for (Book b : listBooks) {
			data[count][0] = b.getBookNumber();
			data[count][1] = b.getTitle();
			count++;
		}
		return data;
	}

	/**Makes sure the table is in single selection mode, that columns cannot be reordered by the user,
	 *  and that the table is sorted by its second column.
	 * @param table
	 */
	private void setTableSettings(JTable table) {
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		table.getColumnModel().removeColumn(table.getColumnModel().getColumn(0));

		//Sort the table by the second column 
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(sorter);
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		int columnIndexToSort = 1; //because though column 0 is removed from columnModel and hence view, it is still in the table model
		sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);
		sorter.sort();	

		table.getTableHeader().setReorderingAllowed(false);
	}

	/**Method to be called when the underlying data may have changed and the tables need to be updated, 
	 * namely by updating bookTitles and emptying royaltyDetailsPanel since no book will be selected.
	 */
	public void updateData() {
		bookTitles.getSelectionModel().removeListSelectionListener(this);
		selectionIndexBeforeSort = 0;
		editButton.setEnabled(false);
		addButton.setEnabled(false);
		deleteButton.setEnabled(false);

		royaltyDetailsPanel.removeAll();
		royaltyDetailsPanel.revalidate();

		TableModel model = getTableBooks().getModel();
		bookTitles.setModel(model);
		setTableSettings(bookTitles);

		bookTitles.getSelectionModel().addListSelectionListener(this);
	}
}
