package basicgui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import main.Book;
import main.IRoyaltyType;
import main.Person;
import main.SalesHistory;

@SuppressWarnings("serial")
public class RoyaltiesPerChannelPanel extends JPanel implements ActionListener, ListSelectionListener{
	
	private JComboBox<String> channelsDropDown = null;
	private JList<String> listBooks = new JList<String>();
	private JPanel royaltyDetails = new JPanel(new BorderLayout());
	private String channel = "Apple";

	public RoyaltiesPerChannelPanel() {
		super();
        this.setLayout(new BorderLayout());
        String[] listChannels = new String[SalesHistory.get().getListChannels().keySet().size()];
        int counter = 0;
        for (String s : SalesHistory.get().getListChannels().keySet()) {
			listChannels[counter] = s;
			counter++;
        }
        channelsDropDown = new JComboBox<String>(listChannels);
        channelsDropDown.addActionListener(this);
        listBooks.addListSelectionListener(this);
        this.add(channelsDropDown, BorderLayout.NORTH);
        this.add(new JScrollPane(listBooks), BorderLayout.WEST);
        this.add(royaltyDetails, BorderLayout.CENTER);
     
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.channel = (String) channelsDropDown.getSelectedItem();
		String[] arrayBooks = new String[SalesHistory.get().getListChannels().get(channel).getListRoyalties().keySet().size()];
		int counter = 0;
		for (Book b : SalesHistory.get().getListChannels().get(channel).getListRoyalties().keySet()) {
			arrayBooks[counter] = b.getTitle();
			counter++;
		}
		listBooks.removeListSelectionListener(this);
		listBooks.setListData(arrayBooks);
		listBooks.revalidate();
		royaltyDetails.removeAll();
		royaltyDetails.revalidate();
		royaltyDetails.repaint();
		listBooks.repaint();
		listBooks.addListSelectionListener(this);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		String bookTitle = (String) listBooks.getSelectedValue();
		Book book = SalesHistory.get().getListPLPBooks().get(bookTitle);
		royaltyDetails.removeAll();
		royaltyDetails.revalidate();
		royaltyDetails.add(new JScrollPane(getTable(book)), BorderLayout.CENTER);
		royaltyDetails.revalidate();
		royaltyDetails.repaint();
	}
	
	private JTable getTable(Book b) {
		String[] columnNames = {"Royalty Holder", "Royalty"};
		DefaultTableModel model = new DefaultTableModel(getData(b), columnNames) {
				@Override
	            public Class<?> getColumnClass(int column) {
				 switch (column ) {
				 default : return String.class;
				 }
			 }
		};
		JTable table = new JTable(model);
		table.setAutoCreateRowSorter(true);
		return table;
	}
	
	private Object[][] getData(Book b){
		int numberOfRows = SalesHistory.get().getListChannels().get(channel).getListRoyalties().get(b).keySet().size();
		HashMap<Person, IRoyaltyType> listRoyalties = SalesHistory.get().getListChannels().get(channel).getListRoyalties().get(b);
		Object[][] data = new Object[numberOfRows][2];
		int count = 0;
		for (Person p : SalesHistory.get().getListChannels().get(channel).getListRoyalties().get(b).keySet()) {
			data[count][0] = p.getName();
			data[count][1] = listRoyalties.get(p);
			count++;
		}
		return data;
	}
}
