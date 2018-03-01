package main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class DataVerification {

	/**Checks whether all channels have sales data for all months found in data 
	 * 
	 * @return An empty string if all channels have sales data for all months found in data, or a string detailing which channels 
	 * are missing data for which months if not.
	 */
	public static String checkSalesDataForAllChannels() {
		String output = "";

		//Set of all channels in app
		Set<String> allChannels = SalesHistory.get().getListChannels().keySet();

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

	public static String checkRoyaltiesDataForAllChannels() {
		String output = "";

		HashMap<String, Set<Book>> listBooksSoldPerChannel = new HashMap<String, Set<Book>>();

		for (Sale s : SalesHistory.get().getSalesHistory()) {
			if (listBooksSoldPerChannel.containsKey(s.getChannel().getName())) {
				Set<Book> bookTitles = listBooksSoldPerChannel.get(s.getChannel().getName());
				bookTitles.add(s.getBook());
				listBooksSoldPerChannel.put(s.getChannel().getName(), bookTitles);
			} else {
				Set<Book> bookTitles = new HashSet<Book>();
				bookTitles.add(s.getBook());
				listBooksSoldPerChannel.put(s.getChannel().getName(), bookTitles);
			}
		}

		for (String channel : listBooksSoldPerChannel.keySet()) {
			for (Book b : listBooksSoldPerChannel.get(channel)) {
				if (!SalesHistory.get().getListChannels().get(channel).getListRoyalties().keySet().contains(b)) {
					output = output.concat("No royalty information was found for \"" + b.getTitle().replace("\"", "") 
							+ "\" at this channel: " + channel + "\n");
				}
			}
		}

		return output;
	}

	public static String checkForexDataForRelevantChannels() {
		String output = "";

		Set<Channel> relevantChannels = new HashSet<Channel>();
		Set<String> months = new TreeSet<String>();

		for (Channel ch : SalesHistory.get().getListChannels().values()) {
			if (!ch.getSaleCurrencyIsAlwaysUSD()) {
				relevantChannels.add(ch);
			}
		}

		for (Sale s : SalesHistory.get().getSalesHistory()) {
			months.add(s.getDate());
		}

		HashMap<String, HashMap<String, Set<String>>> saleCurrenciesPerMonthPerChannel = new HashMap<String, HashMap<String, Set<String>>>();

		for (Sale s : SalesHistory.get().getSalesHistory()) {
			if (!s.getChannel().getSaleCurrencyIsAlwaysUSD()) {
				if (saleCurrenciesPerMonthPerChannel.containsKey(s.getChannel().getName())) {
					HashMap<String, Set<String>> saleCurrenciesPerMonth = saleCurrenciesPerMonthPerChannel.get(s.getChannel().getName());
					if (saleCurrenciesPerMonth.containsKey(s.getDate())) {
						Set<String> currencies = saleCurrenciesPerMonth.get(s.getDate());
						currencies.add(s.getCurrency().getCurrencyCode());
					} else {
						Set<String> currencies = new TreeSet<String>();
						currencies.add(s.getCurrency().getCurrencyCode());
						saleCurrenciesPerMonth.put(s.getDate(), currencies);
					}
				} else {
					Set<String> currencies = new TreeSet<String>();
					currencies.add(s.getCurrency().getCurrencyCode());
					HashMap<String, Set<String>> saleCurrenciesPerMonth = new HashMap<String, Set<String>>();
					saleCurrenciesPerMonth.put(s.getDate(), currencies);
					saleCurrenciesPerMonthPerChannel.put(s.getChannel().getName(), saleCurrenciesPerMonth);
				}
			}
		}

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
