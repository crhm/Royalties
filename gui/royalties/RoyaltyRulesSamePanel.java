package gui.royalties;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
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
import main.Channel;
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

	private JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
	private JButton editButton = new JButton("Edit Selected Royalty");
	private JButton addAllButton = new JButton("Add Royalty to Selected Book (All Channels)");
	private JButton addOneButton = new JButton("Add Royalty to Selected Book (One Channel)");
	private JButton deleteButton = new JButton("Delete Selected Royalty");

	private JPanel containerPanel = new JPanel(new GridLayout(1, 2, 5, 0));

	//Displays the book titles for which there are royalty holders (for the selected channel)
	private JTable bookTitles = null; 

	//Displays the royalties for the selected book
	private JTable royaltiesTable = null;

	//Displays the details of the royalties for the book selected amongst the book titles. If none are selected, no details are shown
	private JPanel royaltyDetailsPanel = new JPanel(new BorderLayout()); 

	//Holds the index of selected book title so that in case of sorting the selection can be maintained despite index change
	private int selectionIndexBeforeSort = 0;

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
		containerPanel.add(new JScrollPane(royaltyDetailsPanel));

		//Because no royalty is selected to start with
		deleteButton.setEnabled(false);
		editButton.setEnabled(false);
		addAllButton.setEnabled(false);
		addOneButton.setEnabled(false);

		//Adding ActionListener to buttons
		editButton.addActionListener(this);
		deleteButton.addActionListener(this);
		addAllButton.addActionListener(this);
		addOneButton.addActionListener(this);

		//Set up buttonPanel
		buttonPanel.add(addOneButton);
		buttonPanel.add(addAllButton);
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
			if (bookTitles.getSelectedRow() != -1 && royaltiesTable.getSelectedRow() != -1) {
				int userChoice = JOptionPane.showConfirmDialog(null, "Please confirm that you want to delete this royalty. ",
						"Confirmation Required", JOptionPane.OK_CANCEL_OPTION);
				if (userChoice == JOptionPane.OK_OPTION) {
					int bookRow = bookTitles.convertRowIndexToModel(bookTitles.getSelectedRow());
					Long bookNumber = (Long) bookTitles.getModel().getValueAt(bookRow, 0);
					Book book = SalesHistory.get().getBookWithNumber(bookNumber);

					int royaltiesRow = royaltiesTable.convertRowIndexToModel(royaltiesTable.getSelectedRow());
					Long personNumber = (Long) royaltiesTable.getModel().getValueAt(royaltiesRow, 0);
					Person person = SalesHistory.get().getPersonWithNumber(personNumber);

					for (Channel ch : SalesHistory.get().getListChannels()) {
						ch.deleteRoyalty(book, person);
					}
				}
				updateData();
			}
		} else if (e.getSource() == addAllButton) {
			if (bookTitles.getSelectedRow() != -1) {
				int bookRow = bookTitles.convertRowIndexToModel(bookTitles.getSelectedRow());
				Long bookNumber = (Long) bookTitles.getModel().getValueAt(bookRow, 0);
				Book book = SalesHistory.get().getBookWithNumber(bookNumber);

				AddRoyaltyDialogAllChannels addRoyaltyDialog = new AddRoyaltyDialogAllChannels(book);
				addRoyaltyDialog.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) { //update and repaint table on close of addRoyaltyDialog
						updateData();
					}				
				});
			}
		} else if (e.getSource() == addOneButton) {
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
		}else if (e.getSource() == editButton) {
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
				addAllButton.setEnabled(true);
				addOneButton.setEnabled(true);
				int tableRow = bookTitles.convertRowIndexToModel(bookTitles.getSelectedRow());
				Long bookNumber = (Long) bookTitles.getModel().getValueAt(tableRow, 0);
				Book book = SalesHistory.get().getBookWithNumber(bookNumber);
				if (royaltiesTable != null && royaltiesTable.isShowing()) {
					royaltiesTable.getSelectionModel().removeListSelectionListener(this);
				}
				royaltyDetailsPanel.removeAll();
				royaltyDetailsPanel.revalidate();
				if (royaltiesTable != null) {
					TableModel model = getTableRoyalties(book).getModel(); 
					royaltiesTable.setModel(model);
					royaltiesTable.getColumnModel().removeColumn(royaltiesTable.getColumnModel().getColumn(0));
					royaltiesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				} else {
					royaltiesTable = getTableRoyalties(book);
					royaltiesTable.getColumnModel().removeColumn(royaltiesTable.getColumnModel().getColumn(0));
					royaltiesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				}
				royaltiesTable.getSelectionModel().addListSelectionListener(this);
				royaltyDetailsPanel.add(new JScrollPane(royaltiesTable), BorderLayout.CENTER);
				royaltyDetailsPanel.revalidate();
				royaltyDetailsPanel.repaint();
			}	
		} else if (e.getSource() == royaltiesTable.getSelectionModel()) { //Change in selection within royaltiesTable
			if (royaltiesTable.getSelectedRow() != -1) {
				deleteButton.setEnabled(true);
				editButton.setEnabled(true);
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
		String[] columnNames = {"Person Number", "Royalty Holder", "Royalty"};
		DefaultTableModel model = new DefaultTableModel(getDataRoyalties(b), columnNames) {
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
		Object[][] data = new Object[numberOfRows][3];
		int count = 0;
		for (Person p : listRoyalties.keySet()) {
			data[count][0] = p.getPersonNumber();
			data[count][1] = p.getName();
			data[count][2] = listRoyalties.get(p).toString();
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

	/**Method to call when underlying data may have changed and the tables needs to be updated,
	 *  more specifically bookTitles is updated and royaltyDetailsPanel is emptied since no book will be selected
	 */
	public void updateData() {
		//Disabling buttons since selection is going to empty
		addAllButton.setEnabled(false);
		addOneButton.setEnabled(false);
		editButton.setEnabled(false);
		deleteButton.setEnabled(false);
		
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
		
		this.revalidate();
		this.repaint();
	}
}
