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
public class ChannelPanel extends JPanel{
	
	public ChannelPanel() {
		super();
		this.setLayout(new GridLayout());

		String[] channels = new String[SalesHistory.get().getListChannels().values().size()];
		int count = 0;
		for (Channel ch : SalesHistory.get().getListChannels().values()) {
			channels[count] = ch.getName();
			count++;
		}

		this.add(new JScrollPane(new JList<String>(channels)));
	}

}
