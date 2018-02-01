package main;

public class RoyaltyPercentage implements IRoyaltyType {

	private double percentage;
	
	public RoyaltyPercentage(double percentage) {
		this.percentage = percentage;
	}

	@Override
	public double getAmountDue(double revenuesPLP, double cumulativeSalesOfBook) {
		return percentage * revenuesPLP;
	}

}
