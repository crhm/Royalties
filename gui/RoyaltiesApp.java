package gui;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import gui.authors.AuthorsPanel;
import gui.books.BookPanel;
import gui.channels.ChannelPanel;
import gui.dataverification.DataVerificationPanel;
import gui.dataverification.SalesImportSummaryPanel;
import gui.importfile.ImportSalesListener;
import gui.persons.PersonsPanel;
import gui.royalties.RoyaltyRulesDifferentPanel;
import gui.royalties.RoyaltyRulesSamePanel;
import gui.royaltyholders.RoyaltyHoldersPanel;
import gui.sales.SalesPanel;
import importing.ChannelRoyaltiesFileFormat;
import main.Channel;
import main.SalesHistory;

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

	JMenuBar menuBar = new JMenuBar();
	JMenu fileMenu = new JMenu("File");
	JMenuItem importSalesFile = new JMenuItem("Import Sales File");
	JMenuItem importFXFile = new JMenuItem("Import FX File");

	public static void main(String[] args) {
		try {
			obtainData();

			RoyaltiesApp test = new RoyaltiesApp();
			Thread guiThread = new Thread(test, "Swing GUI");
			guiThread.start();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initialize() {
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setTitle("Royalties App");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Saves data by serialising it on close.
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				int saveChanges = JOptionPane.showOptionDialog(null, "Do you want to save changes?", "Save changes?", 
						JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
				switch (saveChanges) {
				case JOptionPane.YES_OPTION : SalesHistory.get().serialise(); break;
				case JOptionPane.NO_OPTION : break;
				}
			}
		});

		fileMenu.add(importSalesFile);
		fileMenu.add(importFXFile);
		menuBar.add(fileMenu);
		this.setJMenuBar(menuBar);
		ImportSalesListener importSalesListener = new ImportSalesListener();
		importSalesListener.setAppToUpdate(this);
		importSalesFile.addActionListener(importSalesListener);

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

	/**Gets data by deserialising SalesHistory if it finds the corresponding file.
	 * If it doesn't, it at least imports the royalty holders. 
	 * @throws InterruptedException if a thread interrupts the import thread.
	 */
	private static void obtainData() throws InterruptedException {
		File f = new File("/tmp/data42.ser");
		if(f.exists() && !f.isDirectory()) { 
			SalesHistory.get().deSerialise();
		} else {
			SalesHistory.get().addChannel(new Channel("Kobo", true));
			Channel createspace = new Channel("Createspace", true);
			createspace.addName("CreateSpace");
			SalesHistory.get().addChannel(createspace);
			SalesHistory.get().addChannel(new Channel("Apple", false));
			SalesHistory.get().addChannel(new Channel("Amazon", false));
			SalesHistory.get().addChannel(new Channel("Nook", true));
			ChannelRoyaltiesFileFormat test = new ChannelRoyaltiesFileFormat();
			test.importData("Data/Amazon Royalties.csv");
			test.importData("Data/Nook Royalties.csv");
			test.importData("Data/Createspace Royalties.csv");
			test.importData("Data/Kobo Royalties.csv");
			test.importData("Data/Apple Royalties.csv");
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
