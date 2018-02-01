package main;
import java.util.HashMap;

/**Type of royalty where the percentage of PLP revenue to be given to the royalty holder depends on the number of units sold
 *  (units of the book associated with the royalty). 
 *  <br>Example: 20% of PLP revenue on sales of the book if no more than 1000 books have been sold so far, and 25% after that.
 * @author crhm
 *
 */
public class RoyaltyDependentOnUnitsSold implements IRoyaltyType {

//	key is the range of number of units sold for which the percentage (the value) should be applied
	private HashMap<Integer[], Double> percentageForUnitsSold;
	
	/**RoyaltyDependentOnUnitsSold constructor. Argument determines what percentage to apply for what range of units sold.
	 * @throws IllegalArgumentException if one of the HashMap's keys is not of length 2, 
	 * or if its first element is not smaller or equal to its second element, or if the percentage is not given as a number between 0 and 1.
	 * @param percentageForUnitsSold HashMap where the key is a array of integers representing a range, so it must be of length=2 
	 * and its first element must be smaller than or equal to its second element. The value should be the percentage applied to
	 * calculate the royalty when the number of units sold is within the range, given as a number between 0 and 1.
	 */
	public RoyaltyDependentOnUnitsSold(HashMap<Integer[], Double> percentageForUnitsSold) {
		Boolean flag = false;
		for (Integer[] range : percentageForUnitsSold.keySet()) {
			if (range.length!= 2 || range[0] > range[1]) {
				flag = true;
			}
			if (percentageForUnitsSold.get(range) > 1 || percentageForUnitsSold.get(range) < 0) {
				flag = true;
			}
		}
		if (flag) {
			throw new IllegalArgumentException("Incorrect range found as key in the HashMap passed as argument; "
					+ "keys should be an array of length=2, where the first element is the smallest and the second is the largest.");
		}
		this.percentageForUnitsSold = percentageForUnitsSold;
	}
	
	/**Returns the amount to add to the balance of the royalty holder given the revenue PLP made on this sale of the item 
	 * as well as the total number of units sold for this item so far, following the rules set when creating the instance.
	 */
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
