package basicgui;

import java.awt.GridLayout;
import java.math.BigDecimal;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

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
				case 2 : return Array.class;
				case 3 : return Double.class;
				default : return String.class;
				}
			}
			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
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
			data[count][0] = b.getTitle().replace("\"", "");
			data[count][1] = b.getAuthor().replace("\"", "");
			data[count][2] = b.getIdentifiers();
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
