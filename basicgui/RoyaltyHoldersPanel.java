package basicgui;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import main.Person;
import main.SalesHistory;

@SuppressWarnings("serial")
public class RoyaltyHoldersPanel extends JPanel {

	public RoyaltyHoldersPanel() {
		super();
        this.setLayout(new GridLayout());
        JList<String> royaltiesList = new JList<String>();
        String[] royaltyHolders = new String[SalesHistory.get().getListRoyaltyHolders().values().size()];
        int count = 0;
        for (Person p : SalesHistory.get().getListRoyaltyHolders().values()) {
        		royaltyHolders[count] = p.toString();
        		count++;
        }
        royaltiesList.setListData(royaltyHolders);
        this.add(new JScrollPane(getTable()));

	}
	
	private JTable getTable() {
		String[] columnNames = {"Name", "Balance (USD)"};
		DefaultTableModel model = new DefaultTableModel(getData(), columnNames) {
			@Override
            public Class<?> getColumnClass(int column) {
				switch (column ) {
				case 1 : return Double.class;
				default : return String.class;
				}
			}
			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		JTable table = new JTable(model);
		
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(sorter);
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		int columnIndexToSort = 0;
		sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);
		sorter.sort();
		
		return table;
	}
	
	private Object[][] getData(){
		Object[][] data = new Object[SalesHistory.get().getListRoyaltyHolders().values().size()][2];
		int count = 0;
		for (Person p : SalesHistory.get().getListRoyaltyHolders().values()) {
			data[count][0] = p.getName();
			data[count][1] = p.getBalance();
			count++;
		}
		return data;
	}
	
}
