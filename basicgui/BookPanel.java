package basicgui;

import java.awt.GridLayout;
import java.math.BigDecimal;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import main.Book;
import main.SalesHistory;

@SuppressWarnings("serial")
public class BookPanel extends JPanel{
	
	public BookPanel() {
		super();
		this.setLayout(new GridLayout());
        this.add(new JScrollPane(getTable()));
	}
	
	private JTable getTable() {
		Object[] columnNames = {"Title", "Author", "Identifiers", "Total Sold"};
		DefaultTableModel model = new DefaultTableModel(getData(), columnNames) {
			@Override
            public Class<?> getColumnClass(int column) {
			 switch (column) {
			 case 3 : return Double.class;
			 default : return String.class;
			 }
		 }
		};
		JTable table = new JTable(model);
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(3).setMaxWidth(100);
		table.setAutoCreateRowSorter(true);
		return table;
	}
	
	private Object[][] getData(){
		Object[][] data = new Object[SalesHistory.get().getListPLPBooks().values().size()][4];
		int count = 0;
		for (Book b : SalesHistory.get().getListPLPBooks().values()) {
			data[count][0] = b.getTitle();
			data[count][1] = b.getAuthor();
			data[count][2] = b.getIdentifier();
			BigDecimal totalSold = new BigDecimal(0.00);
			try {
				totalSold = new BigDecimal(SalesHistory.get().getCumulativeSalesPerBook().get(b));
			} catch (NullPointerException e) {
			}
			data[count][3] = totalSold.setScale(0);
			count++;
		}
		return data;
	}

}
