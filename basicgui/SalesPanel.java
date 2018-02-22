package basicgui;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import main.Sale;
import main.SalesHistory;

@SuppressWarnings("serial")
public class SalesPanel extends JPanel {
	
	public SalesPanel() {
		super();
        
        this.setLayout(new GridLayout());
        this.setOpaque(true);
       
        JScrollPane salesScrollPane = new JScrollPane(getTable());
        this.add(salesScrollPane);
	}


    private JTable getTable() {
		Object[] columnNames = {"Channel", "Country", "Date", "Book", "Net Units Sold", "PLP's Royalty", 
	    		"Price", "Delivery Costs", "PLP's Revenues", "Currency"};
		DefaultTableModel model = new DefaultTableModel(getData(), columnNames) {
			@Override
            public Class<?> getColumnClass(int column) {
				switch (column) {
				case 4 : return Double.class;
				case 5 : return Double.class;
				case 6 : return Double.class;
				case 7 : return Double.class;
				case 8 : return Double.class;
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
		columnModel.getColumn(0).setMaxWidth(100);
		columnModel.getColumn(1).setMaxWidth(60);
		columnModel.getColumn(2).setMaxWidth(65);
		columnModel.getColumn(4).setMaxWidth(110);
		columnModel.getColumn(5).setMaxWidth(90);
		columnModel.getColumn(6).setMaxWidth(60);
		columnModel.getColumn(7).setMaxWidth(110);
		columnModel.getColumn(8).setMaxWidth(110);
		columnModel.getColumn(9).setMaxWidth(60);
		table.setAutoCreateRowSorter(true);
		return table;
    }
    
    private Object[][] getData() {
		int numberOfSales = SalesHistory.get().getSalesHistory().size();
		Object[][] data = new Object[numberOfSales][10];
		int rowCounter = 0;
		for (Sale s : SalesHistory.get().getSalesHistory()) {
			data[rowCounter][0] = s.getChannel().getName();
			data[rowCounter][1] = s.getCountry();
			data[rowCounter][2] = s.getDate();
			data[rowCounter][3] = s.getBook().getTitle() + ", " + s.getBook().getAuthor();
			data[rowCounter][4] = s.getNetUnitsSold();
			data[rowCounter][5] = s.getRoyaltyTypePLP();
			data[rowCounter][6] = s.getPrice();
			data[rowCounter][7] = s.getDeliveryCost();
			data[rowCounter][8] = s.getRevenuesPLP();
			data[rowCounter][9] = s.getCurrency();
			rowCounter++;
		}
		return data;
   }
}
