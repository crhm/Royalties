package main;

import java.util.HashMap;

import main.royalties.IRoyaltyType;
import main.royalties.RoyaltyPercentage;

public class UniformRoyalties {
	
	public static Boolean check(Book book) {
		Boolean uniformity = true;
		//For each channel (except createspace), check that it has a royalty list for that book
		for (Channel ch : SalesHistory.get().getListChannels()) {
			if (!ch.getName().equals("Createspace") && ch.getListRoyalties().get(book) == null) {
				uniformity = false;
			}
		}
		if (uniformity) {
			HashMap<Person, IRoyaltyType> royaltiesAmazon = SalesHistory.get().getChannel("Amazon").getListRoyalties().get(book);
			HashMap<Person, IRoyaltyType> royaltiesApple = SalesHistory.get().getChannel("Apple").getListRoyalties().get(book);
			HashMap<Person, IRoyaltyType> royaltiesKobo = SalesHistory.get().getChannel("Kobo").getListRoyalties().get(book);
			HashMap<Person, IRoyaltyType> royaltiesNook = SalesHistory.get().getChannel("Nook").getListRoyalties().get(book);

			if (!checkRoyaltyRules(royaltiesAmazon, royaltiesApple) || !checkRoyaltyRules(royaltiesAmazon, royaltiesKobo)
					|| !checkRoyaltyRules(royaltiesAmazon, royaltiesNook)) {
				uniformity = false;
			}
		}
		return uniformity;
	}

	private static Boolean checkRoyaltyRules(HashMap<Person, IRoyaltyType> channel1, HashMap<Person, IRoyaltyType> channel2) {
		Boolean same = true;

		for (Person p : channel1.keySet()) {
			if (!channel2.keySet().contains(p)) {
				same = false;
			}
		}
		if (same) {
			for (Person p : channel1.keySet()) {
				if (((RoyaltyPercentage) channel1.get(p)).getPercentage() != ((RoyaltyPercentage) channel2.get(p)).getPercentage()) {
					same = false;
				}
			}
		}
		return same;
	}

}
