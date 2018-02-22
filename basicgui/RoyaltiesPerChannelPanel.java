package basicgui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import main.Book;
import main.IRoyaltyType;
import main.Person;
import main.SalesHistory;

@SuppressWarnings("serial")
public class RoyaltiesPerChannelPanel extends JPanel implements ActionListener, ListSelectionListener{
	
	//ComboBox to select the channel whose royalties are to be displayed
	private JComboBox<String> channelsDropDown = null; 
	
	//Displays the book titles for which there are royalty holders (for the selected channel)
	private JTable bookTitles = null; 

	//Displays the details of the royalties for the book selected amongst the book titles. If none are selected, no details are shown
	private JPanel royaltyDetails = new JPanel(new BorderLayout()); 
	
	//Holds the name of the channel that is currently selected. Initialised as "Apple" because it is first in the Channel list
	private String channel = "Apple";
	
	//Holds the index of selected book title so that in case of sorting the selection can be maintained despite index change
	private int selectionIndexBeforeSort = 0;

	public RoyaltiesPerChannelPanel() {
		super();
        this.setLayout(new BorderLayout());
        
        //Obtains list of channels and passes it to the combobox
        String[] listChannels = new String[SalesHistory.get().getListChannels().keySet().size()];
        int counter = 0;
        for (String s : SalesHistory.get().getListChannels().keySet()) {
			listChannels[counter] = s;
			counter++;
        }
        channelsDropDown = new JComboBox<String>(listChannels);
        channelsDropDown.addActionListener(this);
        
        //Gets the list of book titles for initial channel (apple)
        bookTitles = getTableBooks();
        bookTitles.getSelectionModel().addListSelectionListener(this);
        
        //Adds components to the main container panel (the tab)
        this.add(channelsDropDown, BorderLayout.NORTH);
        this.add(new JScrollPane(bookTitles), BorderLayout.WEST);
        this.add(royaltyDetails, BorderLayout.CENTER);    
	}

	/**When the user selects another channel in the combobox, it changes the the list of book titles 
	 * accordingly and erases displayed royalties data.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//Change channel variable to reflect one currently selected
		this.channel = (String) channelsDropDown.getSelectedItem();
		
		//Empty and erase royalty details panel
		royaltyDetails.removeAll();
		royaltyDetails.revalidate();
		royaltyDetails.repaint();
		
		//Have to do this to avoid crashes as it would listen to a selection on a table that is going to be completely redone
		bookTitles.getSelectionModel().removeListSelectionListener(this);
		
		//Fills the table with the new data (as obtained by getting the model off of a new table)
		TableModel model = getTableBooks().getModel();
		bookTitles.setModel(model);
		
		//Repaint with current data
		bookTitles.revalidate();
		bookTitles.repaint();
		
		//Reinstate list selection listener so that royalty details can be obtained for this new list
		bookTitles.getSelectionModel().addListSelectionListener(this);
	}

	/**When a book title is selected in the list of titles, it displays the royalties for that book in the royaltyDetails panel
	 * as a table with royalty holders and their royalty type (and percentage)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		//Need this if because sorting caused the selection to be void somehow and return an index of -1
		// Instead, I clear the selection and set it at the same model index (which is now a different view index)
		if (bookTitles.getSelectedRow() < 0) { 
			bookTitles.clearSelection();
			bookTitles.getSelectionModel().setSelectionInterval(0, bookTitles.convertRowIndexToView(selectionIndexBeforeSort));
		} else {
			int tableRow = bookTitles.getSelectedRow();
			String bookTitle = bookTitles.getValueAt(tableRow, 0).toString();
			Book book = SalesHistory.get().getListPLPBooks().get(bookTitle);
			royaltyDetails.removeAll();
			royaltyDetails.revalidate();
			royaltyDetails.add(new JScrollPane(getTableRoyalties(book)), BorderLayout.CENTER);
			royaltyDetails.revalidate();
			royaltyDetails.repaint();
			
			//For preserving the selection when sorting; save the model row so that you can give it the appropriate new view row 
			int modelRow = bookTitles.convertRowIndexToModel(tableRow);
			selectionIndexBeforeSort = modelRow;
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
		return table;
	}
	
	/**Obtains the data to be put into a table model for the royalties of the book passed as argument
	 * @param b Book whose royalty data we want
	 * @return The royalties data to be put in to a tablemodel
	 */
	private Object[][] getDataRoyalties(Book b){
		HashMap<Person, IRoyaltyType> listRoyalties = SalesHistory.get().getListChannels().get(channel).getListRoyalties().get(b);
		int numberOfRows = listRoyalties.keySet().size();
		Object[][] data = new Object[numberOfRows][2];
		int count = 0;
		for (Person p : listRoyalties.keySet()) {
			data[count][0] = p.getName();
			data[count][1] = listRoyalties.get(p);
			count++;
		}
		return data;
	}
	
	/**Returns a JTable containing the list of book titles that have royalties for the currently selected channel
	 * @return the JTable of book titles (sortable alphabetically, single selection only, not editable)
	 */
	private JTable getTableBooks() {
		String[] columnNames = {"Book Title"};
		DefaultTableModel model = new DefaultTableModel(getDataBooks(), columnNames) {
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
		table.setAutoCreateRowSorter(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		return table;
	}
	
	/**Returns the data to be put into a tablemodel for the list of books with royalties info for currently selected channel
	 * 
	 * @return data to be put into a tablemodel for the list of books with royalties info for currently selected channel
	 */
	private Object[][] getDataBooks(){
		int numberOfRows = SalesHistory.get().getListChannels().get(channel).getListRoyalties().keySet().size();
		Object[][] data = new Object[numberOfRows][1];
		int count = 0;
		for (Book b : SalesHistory.get().getListChannels().get(channel).getListRoyalties().keySet()) {
			data[count][0] = b.getTitle().replace("\"", ""); //So that sorting can disregard quotes
			count++;
		}
		return data;
	}

}
