package main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**Class for static methods desgined to check integrity of imported data.
 * @author crhm
 *
 */
public class DataVerification {
	
	/**Checks whether all channels have sales data for all months found in data 
	 * 
	 * @return An empty string if all channels have sales data for all months found in data, or a string detailing which channels 
	 * are missing data for which months if not.
	 */
	public static String checkSalesDataForAllChannels() {
		String output = "";

		//Set of all channels in app
		Set<String> allChannels = new HashSet<String>();
		for (Channel ch : SalesHistory.get().getListChannels()) {
			allChannels.add(ch.getName());
		}

		//Maps all dates in app to the channels that have sales for that date in the app
		HashMap<String, Set<String>> channelsPerMonth = new HashMap<String, Set<String>>();
		for (Sale s : SalesHistory.get().getSalesHistory()) {
			if (channelsPerMonth.containsKey(s.getDate())) {
				Set<String> channelsAtDate = channelsPerMonth.get(s.getDate());
				channelsAtDate.add(s.getChannel().getName());
				channelsPerMonth.put(s.getDate(), channelsAtDate);
			} else {
				Set<String> channelsAtDate = new TreeSet<String>();
				channelsAtDate.add(s.getChannel().getName());
				channelsPerMonth.put(s.getDate(), channelsAtDate);
			}
		}

		//compares the set of all channels to the set of channels with sales in app for each date,
		//and where it finds channels missing it adds their name to the output string.
		for (String date : channelsPerMonth.keySet()) {
			if (!channelsPerMonth.get(date).containsAll(allChannels)) {
				output = output.concat("For " + date + ", no sales were found for the following channels: \n");
				for (String ch : allChannels) {
					if (!channelsPerMonth.get(date).contains(ch)) {
						output = output.concat(ch + "\n");
					}
				}
			}
		}

		return output;
	}

	/**Returns a mapping of which channels have been found to have sales for which dates.
	 * 
	 * @return A HashMap mapping strings representing dates (month and year) to a set of channel names
	 */
	public static HashMap<String, Set<String>> getListChannelsWithSalesForEachMonth() {

		HashMap<String, Set<String>> channelsPerMonth = new HashMap<String, Set<String>>();
		
		for (Sale s : SalesHistory.get().getSalesHistory()) {
			if (channelsPerMonth.containsKey(s.getDate())) {
				Set<String> channelsAtDate = channelsPerMonth.get(s.getDate());
				channelsAtDate.add(s.getChannel().getName());
				channelsPerMonth.put(s.getDate(), channelsAtDate);
			} else {
				Set<String> channelsAtDate = new TreeSet<String>();
				channelsAtDate.add(s.getChannel().getName());
				channelsPerMonth.put(s.getDate(), channelsAtDate);
			}
		}
		return channelsPerMonth;
	}
	
	/**Checks whether royalties can be found for each sale.
	 * 
	 * @return An empty string if no issue is detected, and a string detailing which book at which channel is missing royalties if issues are detected.
	 */
	public static String checkRoyaltiesDataForAllChannels() {
		String output = "";

		HashMap<String, Set<Book>> listBooksSoldPerChannel = new HashMap<String, Set<Book>>();

		//Building the list of books sold per channel, so that they can be checked for royalties per channel later
		for (Sale s : SalesHistory.get().getSalesHistory()) {
			if (listBooksSoldPerChannel.containsKey(s.getChannel().getName())) { //If there's already a mapping for that channel
				Set<Book> bookTitles = listBooksSoldPerChannel.get(s.getChannel().getName());
				bookTitles.add(s.getBook());
				listBooksSoldPerChannel.put(s.getChannel().getName(), bookTitles);
			} else { //if there's not
				Set<Book> bookTitles = new HashSet<Book>();
				bookTitles.add(s.getBook());
				listBooksSoldPerChannel.put(s.getChannel().getName(), bookTitles);
			}
		}

		//Checking all books sold by each channel has a royalty.
		for (String channel : listBooksSoldPerChannel.keySet()) {
			for (Book b : listBooksSoldPerChannel.get(channel)) {
				if (!SalesHistory.get().getChannel(channel).getListRoyalties().keySet().contains(b)) {
					output = output.concat("No royalty information was found for \"" + b.getTitle() 
							+ "\" at this channel: " + channel + "\n");
				}
			}
		}

		return output;
	}

	/**Checks that for all channels that do not have all exchanges in USD, an exchange rate can be found for each currency at each date
	 * present in the sales.
	 * @return an empty string if no issues are detected, and one detailing, line by line, the missing information, if they are.
	 */
	public static String checkForexDataForRelevantChannels() {
		String output = "";
		
		//Setting up what to compare against (list of channels that aren't in USD and list of months for which there are sales)
		Set<String> months = SalesHistory.get().getListMonths();

		Set<Channel> relevantChannels = new HashSet<Channel>();
		for (Channel ch : SalesHistory.get().getListChannels()) {
			if (!ch.getSaleCurrencyIsAlwaysUSD()) {
				relevantChannels.add(ch);
			}
		}

		//Mapping, for each channel, which currencies were found in the sales for which month.
		HashMap<String, HashMap<String, Set<String>>> saleCurrenciesPerMonthPerChannel = new HashMap<String, HashMap<String, Set<String>>>();

		for (Sale s : SalesHistory.get().getSalesHistory()) {
			if (!s.getChannel().getSaleCurrencyIsAlwaysUSD()) { //For the relevant sales...
				if (saleCurrenciesPerMonthPerChannel.containsKey(s.getChannel().getName())) { //if there's already a mapping for that channel
					HashMap<String, Set<String>> saleCurrenciesPerMonth = saleCurrenciesPerMonthPerChannel.get(s.getChannel().getName());
					if (saleCurrenciesPerMonth.containsKey(s.getDate())) { //if there's already a mapping for that date for that channel
						Set<String> currencies = saleCurrenciesPerMonth.get(s.getDate());
						currencies.add(s.getCurrency().getCurrencyCode());
					} else { //If there's no mapping of that date for that channel
						Set<String> currencies = new TreeSet<String>();
						currencies.add(s.getCurrency().getCurrencyCode());
						saleCurrenciesPerMonth.put(s.getDate(), currencies);
					}
				} else { //if there's no mapping for that channel yet.
					Set<String> currencies = new TreeSet<String>();
					currencies.add(s.getCurrency().getCurrencyCode());
					HashMap<String, Set<String>> saleCurrenciesPerMonth = new HashMap<String, Set<String>>();
					saleCurrenciesPerMonth.put(s.getDate(), currencies);
					saleCurrenciesPerMonthPerChannel.put(s.getChannel().getName(), saleCurrenciesPerMonth);
				}
			}
		}

		//Building the output by comparing what ought to be found with what was found.
		for (Channel ch : relevantChannels) {
			if (ch.getHistoricalForex().isEmpty()) {
				output = output.concat("No historical FX rates where found for this channel: " + ch);
			} else {
				for (String date : months) {
					if (!ch.getHistoricalForex().keySet().contains(date)) {
						output = output.concat("No historical FX rates where found for " + date + " for this channel: " + ch);
					} else {
						for (String currency : saleCurrenciesPerMonthPerChannel.get(ch.getName()).get(date)) {
							if (!ch.getHistoricalForex().get(date).containsKey(currency)){
								output = output.concat("No FX rate was found for " + currency + " for this month: " + date 
										+ " for this channel: " + ch);
							}
						}
					}
				}
			}
		}

		return output;
	}
}
