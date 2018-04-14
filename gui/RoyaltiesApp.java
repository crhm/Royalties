package gui;

import java.io.File;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import gui.authors.AuthorsPanel;
import gui.books.BookPanel;
import gui.channels.ChannelPanel;
import gui.dataverification.DataVerificationPanel;
import gui.dataverification.SalesImportSummaryPanel;
import gui.persons.PersonsPanel;
import gui.royalties.RoyaltyRulesDifferentPanel;
import gui.royalties.RoyaltyRulesSamePanel;
import gui.royaltyholders.RoyaltyHoldersPanel;
import gui.sales.SalesPanel;
import importing.ImportGeneralRoyalties;
import importing.test.ImportEverything;
import main.Book;
import main.Channel;
import main.Person;
import main.SalesHistory;
import main.royalties.IRoyaltyType;
import main.royalties.RoyaltyPercentage;

/**GUI for royalties app. Opens a full screen window with several panels displaying different information:
 * <br>Sales, Royalty Holders, PLP Books, Channels, Royalty Holders Per Channel, Data Verification.
 * 
 * @author crhm
 *
 */
@SuppressWarnings("serial")
public class RoyaltiesApp extends JFrame implements Runnable, ChangeListener {
	
	JTabbedPane allTabs = new JTabbedPane();        
	
	BookPanel bookPanel = new BookPanel();
	PersonsPanel personsPanel = new PersonsPanel();
	AuthorsPanel authorsPanel = new AuthorsPanel();
	RoyaltyHoldersPanel royaltyHoldersPanel = new RoyaltyHoldersPanel();
	ChannelPanel channelPanel = new ChannelPanel();
	RoyaltyRulesSamePanel royaltiesRulesSamePanel = new RoyaltyRulesSamePanel();
	RoyaltyRulesDifferentPanel royaltyRulesDifferentPanel = new RoyaltyRulesDifferentPanel();
	SalesImportSummaryPanel salesImportSummaryPanel = new SalesImportSummaryPanel();
	SalesPanel salesPanel= new SalesPanel();
	DataVerificationPanel dataVerificationPanel = new DataVerificationPanel();

	public static void main(String[] args) {
		try {
			obtainData();
						
			RoyaltiesApp test = new RoyaltiesApp();
			Thread guiThread = new Thread(test, "Swing GUI");
			guiThread.start();
			ImportGeneralRoyalties.importData();
//			for (Book b : SalesHistory.get().getListPLPBooks()) {
//				String amazon = null;
//				String apple = null;
//				String kobo = null;
//				String nook = null;
//				if (SalesHistory.get().getListChannels().get("Amazon").getListRoyalties().get(b) != null) {
//					amazon = SalesHistory.get().getListChannels().get("Amazon").getListRoyalties().get(b).toString();
//				}
//				if (SalesHistory.get().getListChannels().get("Apple").getListRoyalties().get(b) != null) {
//					apple = SalesHistory.get().getListChannels().get("Apple").getListRoyalties().get(b).toString();
//				}
//				if (SalesHistory.get().getListChannels().get("Kobo").getListRoyalties().get(b) != null) {
//					kobo = SalesHistory.get().getListChannels().get("Kobo").getListRoyalties().get(b).toString();
//				}
//				if (SalesHistory.get().getListChannels().get("Nook").getListRoyalties().get(b) != null) {
//					nook = SalesHistory.get().getListChannels().get("Nook").getListRoyalties().get(b).toString();
//				}
//				if (amazon != null && amazon.equals(apple) && amazon.equals(kobo) && amazon.equals(nook)) {
//				} else {
//					System.out.println(b.getTitle());
//					System.out.println(amazon + "\n" + apple + "\n" + kobo + "\n" + nook + "\n");
//				}
//			}
//			int countBooksNotUniform = 0;
//			for (Book b : SalesHistory.get().getListPLPBooks()) {
//				if(!royaltiesUniformAcrossChannelsCheck(b)) {
//					countBooksNotUniform++;
//				}
//			}
//			System.out.println(countBooksNotUniform);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Boolean royaltiesUniformAcrossChannelsCheck(Book book) {
		Boolean uniformity = true;
		//For each channel (except createspace), check that it has a royalty list for that book
		for (Channel ch : SalesHistory.get().getListChannels().values()) {
			if (!ch.getName().equals("Createspace") && ch.getListRoyalties().get(book) == null) {
				System.out.println("No royalty List Found for " + book.getTitle() + " in " + ch.getName());
				uniformity = false;
			}
		}
		if (uniformity) {
			HashMap<Person, IRoyaltyType> royaltiesAmazon = SalesHistory.get().getListChannels().get("Amazon").getListRoyalties().get(book);
			HashMap<Person, IRoyaltyType> royaltiesApple = SalesHistory.get().getListChannels().get("Apple").getListRoyalties().get(book);
			HashMap<Person, IRoyaltyType> royaltiesKobo = SalesHistory.get().getListChannels().get("Kobo").getListRoyalties().get(book);
			HashMap<Person, IRoyaltyType> royaltiesNook = SalesHistory.get().getListChannels().get("Nook").getListRoyalties().get(book);

			if (!checkRoyaltyRules(royaltiesAmazon, royaltiesApple) || !checkRoyaltyRules(royaltiesAmazon, royaltiesKobo)
					|| !checkRoyaltyRules(royaltiesAmazon, royaltiesNook)) {
				uniformity = false;
				System.out.println(book);
				System.out.println(royaltiesAmazon);
				System.out.println(royaltiesApple);
				System.out.println(royaltiesKobo);
				System.out.println(royaltiesNook);
			}
		}
		return uniformity;
	}

	private static Boolean checkRoyaltyRules(HashMap<Person, IRoyaltyType> channel1, HashMap<Person, IRoyaltyType> channel2) {
		Boolean same = true;

		for (Person p : channel1.keySet()) {
			if (!channel2.keySet().contains(p)) {
				same = false;
				System.out.println("Found a royalty holder that was in one channel but not the other");
			}
		}
		if (same) {
			for (Person p : channel1.keySet()) {
				if (((RoyaltyPercentage) channel1.get(p)).getPercentage() != ((RoyaltyPercentage) channel2.get(p)).getPercentage()) {
					same = false;
					System.out.println("Found a percentage that was different");
				}
			}
		}
		

		return same;
	}
	
	private void initialize() {
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setTitle("Royalties App");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        
		//Saves data by serialising it on close.
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				SalesHistory.get().serialise();
			}
		});


		allTabs.addTab("PLP Books", bookPanel);
		allTabs.addTab("Persons", personsPanel);
		allTabs.addTab("Authors", authorsPanel);
		allTabs.addTab("Royalty Holders", royaltyHoldersPanel);
		allTabs.addTab("Channels", channelPanel);
		allTabs.addTab("Royalties For All Channels", royaltiesRulesSamePanel);
		allTabs.addTab("Royalties For Specific Channels", royaltyRulesDifferentPanel);
		allTabs.addTab("Sales Import Summary", salesImportSummaryPanel);
		allTabs.add("Sales", salesPanel);
		allTabs.addTab("Data Verification", dataVerificationPanel);
		allTabs.addChangeListener(this);

		this.setContentPane(allTabs);
		this.pack();
		this.setVisible(true);
		this.revalidate();
	}


	@Override
	public void run() {
		initialize();
	} 

	/**Gets royalties data by deserialising SalesHistory if it finds the corresponding file, 
	 * or by importing everything if it doesn't.
	 * @throws InterruptedException if a thread interrupts the import thread.
	 */
	private static void obtainData() throws InterruptedException {
		File f = new File("/tmp/data13.ser");
		if(f.exists() && !f.isDirectory()) { 
			SalesHistory.get().deSerialise();
		} else {
			Thread importThread = new Thread(new ImportEverything(), "Importing data");
			importThread.start();
			importThread.join(); //wait for this thread to die before starting next one
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		int index = allTabs.getSelectedIndex();

		switch (index) {
		case (0) : bookPanel.updateData(); break;
		case (1) : personsPanel.updateData(); break;
		case (2) : authorsPanel.updateData(); break;
		case (3) : royaltyHoldersPanel.updateData(); break;
		case (4) : channelPanel.updateData(); break;
		case (5) : royaltiesRulesSamePanel.updateData(); break;
		case (6) : royaltyRulesDifferentPanel.updateData(); break;
		case (7) : salesImportSummaryPanel.updateData(); break;
		case (8) : salesPanel.updateData(); break;
		case (9) : dataVerificationPanel.updateData(); break;
		}
	}

}
