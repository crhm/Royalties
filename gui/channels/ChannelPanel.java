package gui.channels;

import java.awt.GridLayout;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import main.Channel;
import main.SalesHistory;

/**A JPanel containing a scollable JList of sales channel.
 * 
 * @author crhm
 *
 */
@SuppressWarnings("serial")
public class ChannelPanel extends JPanel {
	JList<String> listChannels = new JList<String>();
	
	public ChannelPanel() {
		super();
		this.setLayout(new GridLayout());

		
		listChannels.setListData(getListChannels());;
		this.add(new JScrollPane(listChannels));
	}
	
	private String[] getListChannels() {
		String[] channels = new String[SalesHistory.get().getListChannels().size()];
		int count = 0;
		for (Channel ch : SalesHistory.get().getListChannels()) {
			channels[count] = ch.getName();
			count++;
		}
		return channels;
	}

	public void updateData() {
		listChannels.setListData(getListChannels());
	}

}
