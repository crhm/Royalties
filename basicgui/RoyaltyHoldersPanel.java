package basicgui;

import java.awt.GridLayout;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

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
		};
		JTable table = new JTable(model);
		table.setAutoCreateRowSorter(true);
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
