package basicgui;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

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
	    		"Price", "Delivery Costs", "PLP's Revenues", "Currency", "Royalties Have Been Calculated"};
		DefaultTableModel model = new DefaultTableModel(getData(), columnNames) {
			@Override
            public Class<?> getColumnClass(int column) {
				switch (column) {
				case 4 : return Double.class;
				case 5 : return Double.class;
				case 6 : return Double.class;
				case 7 : return Double.class;
				case 8 : return Double.class;
				case 9 : return Currency.class;
				case 10 : return Boolean.class;
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
		columnModel.getColumn(10).setMaxWidth(110);
		
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(sorter);
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		int columnIndexToSortFirst = 2;
		sortKeys.add(new RowSorter.SortKey(columnIndexToSortFirst, SortOrder.ASCENDING));
		int columnIndexToSortSecond = 0;
		sortKeys.add(new RowSorter.SortKey(columnIndexToSortSecond, SortOrder.ASCENDING));
		int columnIndexToSortThird = 1;
		sortKeys.add(new RowSorter.SortKey(columnIndexToSortThird, SortOrder.ASCENDING));
		int columnIndexToSortFourth = 3;
		sortKeys.add(new RowSorter.SortKey(columnIndexToSortFourth, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);
		sorter.sort();
		
		return table;
    }
    
    private Object[][] getData() {
		int numberOfSales = SalesHistory.get().getSalesHistory().size();
		Object[][] data = new Object[numberOfSales][11];
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
			data[rowCounter][10] = s.getRoyaltyHasBeenCalculated();
			rowCounter++;
		}
		return data;
   }
}
