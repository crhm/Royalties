package main;

import java.util.HashMap;

import main.royalties.IRoyaltyType;

/**Class for static methods intended to check whether a book has uniform royalties across all channels
 * (same royalty holders, same percentages, ...) (excluding createspace).
 * @author crhm
 *
 */
public class UniformRoyalties {
	
	/**Checks whether the royalties for this book are found for every channel, and are the same regardless of channel.
	 * <br>If it does not have royalties for createspace, that will be ignored, but if it does, they will be compared to the rest.
	 * @param book Book in salesHistory whose royalties need to be checked.
	 * @return true if royalties are uniform, false if not.
	 */
	public static Boolean check(Book book) {
		Boolean uniformity = true;
		Boolean createSpace = false;
		for (Channel ch : SalesHistory.get().getListChannels()) {
			//Check if there are royalties for every channel (except createspace)
			if (!ch.getName().equals("Createspace") && ch.getListRoyalties().get(book) == null) {
				uniformity = false;
			}
			//Check if there are royalties for createspace
			if (ch.getName().equals("Createspace") && ch.getListRoyalties().get(book) != null) {
				createSpace = true;
			}
		}
		if (uniformity) { //If there are royalties for all main channels, check that they match.
			HashMap<Person, IRoyaltyType> royaltiesAmazon = SalesHistory.get().getChannel("Amazon").getListRoyalties().get(book);
			HashMap<Person, IRoyaltyType> royaltiesApple = SalesHistory.get().getChannel("Apple").getListRoyalties().get(book);
			HashMap<Person, IRoyaltyType> royaltiesKobo = SalesHistory.get().getChannel("Kobo").getListRoyalties().get(book);
			HashMap<Person, IRoyaltyType> royaltiesNook = SalesHistory.get().getChannel("Nook").getListRoyalties().get(book);

			if (!checkRoyaltyRules(royaltiesAmazon, royaltiesApple) || !checkRoyaltyRules(royaltiesAmazon, royaltiesKobo)
					|| !checkRoyaltyRules(royaltiesAmazon, royaltiesNook)) {
				uniformity = false;
			}
		}
		if (uniformity && createSpace) { //If there are royalties for createspace, and other royalties match, check that the createspace royalties do to. 
			HashMap<Person, IRoyaltyType> royaltiesOthers = SalesHistory.get().getChannel("Amazon").getListRoyalties().get(book);
			HashMap<Person, IRoyaltyType> royaltiesCreatespace = SalesHistory.get().getChannel("Createspace").getListRoyalties().get(book);
			if (!checkRoyaltyRules(royaltiesOthers, royaltiesCreatespace)) {
				uniformity = false;
			}
		}
		return uniformity;
	}

	/**Checks that the royalty mappings passed as arguments are equivalent.
	 * 
	 * @param channel1 royalty mappings from one channel
	 * @param channel2 royalty mappings from another channel
	 * @return true if royalties are the same, false if not.
	 */
	private static Boolean checkRoyaltyRules(HashMap<Person, IRoyaltyType> channel1, HashMap<Person, IRoyaltyType> channel2) {
		Boolean same = true;

		if (channel1.keySet().size() != channel2.keySet().size()) { //Checks that there are the same number of mappings
			same = false;
		}
		for (Person p : channel1.keySet()) { //Checks that every person in mapping 1 is also in mapping 2
			if (!channel2.keySet().contains(p)) {
				same = false;
			}
		}
		if (same) {
			for (Person p : channel1.keySet()) { //Checks that for the same royalty holder the same royalty is applied.
				if (!channel1.get(p).equals(channel2.get(p))){
					same = false;
				}
			}
		}
		return same;
	}

}
