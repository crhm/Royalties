package main;
import java.util.HashMap;

public class RoyaltyDependentOnUnitsSold implements IRoyaltyType {

//	key is the range of number of units sold for which the percentage (the value) should be applied
	private HashMap<Integer[], Double> percentageForUnitsSold;
	
	/**
	 * 
	 * @param percentageForUnitsSold HashMap where the key is a array of integers representing a range, so it should be of length=2 
	 * and its first element should be smaller than or equal to its second element. The value should be the percentage applied to
	 * calculate the royalty when the number of units sold is within the range.
	 */
	public RoyaltyDependentOnUnitsSold(HashMap<Integer[], Double> percentageForUnitsSold) {
		Boolean flag = false;
		for (Integer[] range : percentageForUnitsSold.keySet()) {
			if (range.length!= 2 || range[0] > range[1]) {
				flag = true;
			}
		}
		if (flag) {
			throw new IllegalArgumentException("Incorrect range found as key in the HashMap passed as argument; "
					+ "keys should be an array of length=2, where the first element is the smallest and the second is the largest.");
		}
		this.percentageForUnitsSold = percentageForUnitsSold;
	}
	
	@Override
	public double getAmountDue(double revenuesPLP, double cumulativeSalesOfBook) {
		double amount = 0;
		for (Integer[] range : percentageForUnitsSold.keySet()) {
			if (cumulativeSalesOfBook >= range[0] && cumulativeSalesOfBook <= range[1]) {
				amount = revenuesPLP * percentageForUnitsSold.get(range);
			}
		}
		return amount;
	}
	
}
