package gui.royalties;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

/**A JPanel displaying royalties information.
 * <br>The user selects which channel he wishes to see the royalties for, then is shown the channel's list of books for which 
 * there are royalties.
 * <br>The user can then select a book, to display the royalty holders (and their percentages) for that book.
 * <br>Display is initialised as if Channel "Apple" had been selected.
 * <br>The list of channels is a JComboBox.
 * <br>The list of books is actually a one column JTable, which is sorted.
 * <br>The royalty details per book is a JTable (not sortable, not editable) with two columns.
 * @author crhm
 *
 */
@SuppressWarnings("serial")
public class RoyaltyRulesSamePanel extends JPanel implements ActionListener, ListSelectionListener{

	//TODO add button should be dynamic; if only no particular royalty is selected, it should be disabled.

	private JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
	private JButton editButton = new JButton("Edit");
	private JButton addButton = new JButton("Add");
	private JButton deleteButton = new JButton("Delete");

	private JPanel containerPanel = new JPanel(new GridLayout(1, 2, 5, 0));

	//Displays the book titles for which there are royalty holders (for the selected channel)
	private JTable bookTitles = null; 

	//Displays the royalties for the selected book
	private JTable royaltiesTable = null;

	//Displays the details of the royalties for the book selected amongst the book titles. If none are selected, no details are shown
	private JPanel royaltyDetailsPanel = new JPanel(new BorderLayout()); 

	//Holds the index of selected book title so that in case of sorting the selection can be maintained despite index change
	private int selectionIndexBeforeSort = 0;

	//Holds the name of the royalty holder that is currently selected
	private String currentPerson = null;

	//Holds the title of the book that is currently selected
	private String currentBook = null;

	public RoyaltyRulesSamePanel() {
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

		//Adding ActionListener to buttons
		deleteButton.addActionListener(this);
		addButton.addActionListener(this);
		editButton.addActionListener(this);

		//Set up buttonPanel
		buttonPanel.add(new JLabel());
		buttonPanel.add(new JLabel());
		buttonPanel.add(addButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(editButton);

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
				//TODO
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
				int tableRow = bookTitles.getSelectedRow();
				Long bookNumber = (Long) bookTitles.getValueAt(tableRow, 0);
				Book book = SalesHistory.get().getBookWithNumber(bookNumber);
				if (royaltiesTable != null && royaltiesTable.isShowing()) {
					royaltiesTable.getSelectionModel().removeListSelectionListener(this);
				}
				royaltyDetailsPanel.removeAll();
				royaltyDetailsPanel.revalidate();
				if (royaltiesTable != null) {
					TableModel model = getTableRoyalties(book).getModel(); 
					royaltiesTable.setModel(model);
					royaltiesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				} else {
					royaltiesTable = getTableRoyalties(book);
					royaltiesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				}
				royaltiesTable.getSelectionModel().addListSelectionListener(this);
				royaltyDetailsPanel.add(new JScrollPane(royaltiesTable), BorderLayout.CENTER);
				royaltyDetailsPanel.revalidate();
				royaltyDetailsPanel.repaint();

				currentBook = (String) bookTitles.getValueAt(tableRow, 1); 
			}	
		} else if (e.getSource() == royaltiesTable.getSelectionModel()) {
			if (royaltiesTable.getSelectedRow() != -1) {
				deleteButton.setEnabled(true);
				editButton.setEnabled(true);
				int row = royaltiesTable.convertRowIndexToModel(royaltiesTable.getSelectedRow());
				currentPerson = (String) royaltiesTable.getValueAt(row, 1);
			} else {
				deleteButton.setEnabled(false);
				editButton.setEnabled(false);
			}
		}	
	}

	/**Returns a table contains the royalty information for the book passed as argument
	 * @param b Book of which we want the royalty details
	 * @return a JTable (not sortable, not editable) containing royalties data for that book
	 */
	private JTable getTableRoyalties(Book b) {
		String[] columnNames = {"Royalty Holder", "Royalty"};
		DefaultTableModel model = new DefaultTableModel(getDataRoyalties(b), columnNames) {
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

		//Disables the user-reordering table columns
		table.getTableHeader().setReorderingAllowed(false);

		return table;
	}

	/**Obtains the data to be put into a table model for the royalties of the book passed as argument
	 * @param b Book whose royalty data we want
	 * @return The royalties data to be put in to a tablemodel
	 */
	private Object[][] getDataRoyalties(Book b){
		HashMap<Person, IRoyaltyType> listRoyalties = SalesHistory.get().getUniformRoyalties().get(b);
		int numberOfRows = listRoyalties.keySet().size();
		Object[][] data = new Object[numberOfRows][2];
		int count = 0;
		for (Person p : listRoyalties.keySet()) {
			data[count][0] = p.getName();
			data[count][1] = listRoyalties.get(p).toString();
			count++;
		}
		return data;
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
		int numberOfRows = SalesHistory.get().getUniformRoyalties().keySet().size();
		Object[][] data = new Object[numberOfRows][2];
		int count = 0;
		for (Book b : SalesHistory.get().getUniformRoyalties().keySet()) {
			data[count][0] = b.getBookNumber();
			data[count][1] = b.getTitle();
			count++;
		}
		return data;
	}
	
	/**Makes sure the table is in single selection mode and is sorted by its second column,
	 *  and that the user cannot reorder its columns
	 * @param table
	 */
	private void setTableSettings(JTable table) {
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		table.getColumnModel().getColumn(0).setMaxWidth(100);

		//Sort the table by the second column 
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(sorter);
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		int columnIndexToSort = 1;
		sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);
		sorter.sort();	
		
		table.getTableHeader().setReorderingAllowed(false);
	}

	/**Method to call when underlying data may have changed and the tables needs to be updated,
	 *  more specifically bookTitles is updated and royaltyDetailsPanel is emptied since no book will be selected
	 */
	public void updateData() {
		//Empty and erase royalty details panel
		if (royaltiesTable != null && royaltiesTable.isShowing()) {
			royaltiesTable.getSelectionModel().removeListSelectionListener(this);
		}
		royaltyDetailsPanel.removeAll();
		royaltyDetailsPanel.revalidate();
		royaltyDetailsPanel.repaint();

		//Have to do this to avoid crashes as it would listen to a selection on a table that is going to be completely redone
		bookTitles.getSelectionModel().removeListSelectionListener(this);

		//Fills the table with the new data (as obtained by getting the model off of a new table)
		TableModel model = getTableBooks().getModel();
		bookTitles.setModel(model);
		setTableSettings(bookTitles);

		//Repaint with current data
		bookTitles.revalidate();
		bookTitles.repaint();

		//Reinstate list selection listener so that royalty details can be obtained for this new list
		bookTitles.getSelectionModel().addListSelectionListener(this);
	}

//	private static Boolean royaltiesUniformAcrossChannelsCheck(Book book) {
//		Boolean uniformity = true;
//		//For each channel (except createspace), check that it has a royalty list for that book
//		for (Channel ch : SalesHistory.get().getListChannels().values()) {
//			if (!ch.getName().equals("Createspace") && ch.getListRoyalties().get(book) == null) {
//				uniformity = false;
//			}
//		}
//		if (uniformity) {
//			HashMap<Person, IRoyaltyType> royaltiesAmazon = SalesHistory.get().getListChannels().get("Amazon").getListRoyalties().get(book);
//			HashMap<Person, IRoyaltyType> royaltiesApple = SalesHistory.get().getListChannels().get("Apple").getListRoyalties().get(book);
//			HashMap<Person, IRoyaltyType> royaltiesKobo = SalesHistory.get().getListChannels().get("Kobo").getListRoyalties().get(book);
//			HashMap<Person, IRoyaltyType> royaltiesNook = SalesHistory.get().getListChannels().get("Nook").getListRoyalties().get(book);
//
//			if (!checkRoyaltyRules(royaltiesAmazon, royaltiesApple) || !checkRoyaltyRules(royaltiesAmazon, royaltiesKobo)
//					|| !checkRoyaltyRules(royaltiesAmazon, royaltiesNook)) {
//				uniformity = false;
//			}
//		}
//		return uniformity;
//	}
//
//	private static Boolean checkRoyaltyRules(HashMap<Person, IRoyaltyType> channel1, HashMap<Person, IRoyaltyType> channel2) {
//		Boolean same = true;
//
//		for (Person p : channel1.keySet()) {
//			if (!channel2.keySet().contains(p)) {
//				same = false;
//			}
//		}
//		if (same) {
//			for (Person p : channel1.keySet()) {
//				if (((RoyaltyPercentage) channel1.get(p)).getPercentage() != ((RoyaltyPercentage) channel2.get(p)).getPercentage()) {
//					same = false;
//				}
//			}
//		}
//
//
//		return same;
//	}

}
