package main.royalties;
import java.util.HashMap;

/**Type of royalty where the percentage of PLP revenue to be given to the royalty holder depends on the number of units sold
 *  (units of the book associated with the royalty). 
 *  <br>Example: 20% of PLP revenue on sales of the book if no more than 1000 books have been sold so far, and 25% after that.
 * @author crhm
 */
public class RoyaltyDependentOnUnitsSold implements IRoyaltyType, java.io.Serializable {

	private static final long serialVersionUID = 3701040635256764410L;

	//	key is the range of number of units sold for which the percentage (the value) should be applied
	private HashMap<Integer[], Double> percentagesForUnitsSold;
	//Percentage to be applied for all number of units sold not in any of the defined ranges of percentagesForUnitsSold
	private double defaultPercentage;

	/**RoyaltyDependentOnUnitsSold constructor. Arguments determines what percentage to apply for what range of units sold.
	 * @throws IllegalArgumentException if arguments are not valid.
	 * @param percentageForUnitsSold HashMap where the key is an array of integers representing a range, so it must be of length=2 
	 * and its first element must be smaller than or equal to its second element. The value should be the percentage applied to
	 * calculate the royalty when the number of units sold is within the range (inclusive of boundaries), given as a number between 0 and 1 (inclusive).
	 * @param defaultPercentage percentage to be applied for any number of units sold not whithin the ranges defined in percentageForUnitsSold, 
	 * which should be a number between 0 and 1 (inclusive).
	 */
	public RoyaltyDependentOnUnitsSold(HashMap<Integer[], Double> percentageForUnitsSold, double defaultPercentage) {
		validateData(percentageForUnitsSold, defaultPercentage);
		this.percentagesForUnitsSold = percentageForUnitsSold;
		this.defaultPercentage = defaultPercentage;
	}

	/**Returns the amount to add to the balance of the royalty holder given the revenue PLP made on this sale of the item 
	 * as well as the total number of units sold for this item so far, following the rules set when creating the instance.
	 */
	@Override
	public double getAmountDue(double revenuesPLP, double cumulativeSalesOfBook) {
		double amount = 0;
		Boolean salesNotInRange = true;
		for (Integer[] range : percentagesForUnitsSold.keySet()) {
			if (cumulativeSalesOfBook >= range[0] && cumulativeSalesOfBook <= range[1]) {
				amount = revenuesPLP * percentagesForUnitsSold.get(range);
				salesNotInRange = false;
			}
		}
		if (salesNotInRange) {
			amount = revenuesPLP * defaultPercentage;
		}
		return amount;
	}

	/**Returns a string over several lines detailing in full sentences the detail of this royalty.
	 */
	@Override
	public String toString() {
		String info = "";
		info = info.concat("RoyaltyDependentOnUnitsSold:\n");
		for (Integer[] range : percentagesForUnitsSold.keySet()) {
			info = info.concat("If the number of units sold is between " + range[0] + " and " + range[1] + " (inclusive): " 
					+ percentagesForUnitsSold.get(range) + "\n");
		}
		info = info.concat("For all other numbers of units sold: " + defaultPercentage);
		return info;
	}

	/**Returns the HashMap detailing the percentage to be applied for specific ranges of number of units sold.
	 * <br>NOTE: does not return the default percentage, which is applied when the number of units sold is not in 
	 * any of the ranges specified here. See getDefaultPercentage().
	 * @return the HashMap mapping Integer[] representing ranges to Doubles representing percentages to be applied for them
	 */
	public HashMap<Integer[], Double> getPercentagesForUnitsSold(){
		return percentagesForUnitsSold;
	}
	
	/**Returns the default percentage; the percentage applied when the number of units sold is not in one of the ranges 
	 * specified in the percentagesForUnitsSold (see getPercentagesForUnitsSold() to obtain that).
	 * @return the default percentage
	 */
	public double getDefaultPercentage() {
		return defaultPercentage;
	}
	
	
	/**Obtains the royalty percentage to be applied for the given number of units sold
	 * <br>Returns -1 if there was an error.
	 * @param unitsSold number of units sold
	 * @return Double representing the percentage to be applied to obtain the royalty, or -1 if it did not find appropriate percentage
	 */
	public double getPercentageForUnitsSold(int unitsSold) {
		double percentage = -1;
		Boolean salesNotInRange = true;
		for (Integer[] range : percentagesForUnitsSold.keySet()) {
			if (unitsSold >= range[0] && unitsSold <= range[1]) {
				percentage = percentagesForUnitsSold.get(range);
				salesNotInRange = false;
			}
		}
		if (salesNotInRange) {
			percentage = defaultPercentage;
		}
		return percentage;
	}
	
	/**Validates Data passed to constructor as arguments.
	 * <br>Rules:
	 * <br>Percentages should be a number between 0 and 1 (inclusive).
	 * <br>An Integer[] should have two elements, to represent lower and upper boundaries of the range.
	 * <br>An Integer[]'s first element should be smaller or equal to its second element.
	 * @param percentagesForUnitsSold
	 * @param defaultPercentage
	 * @throws IllegalArgumentException if data does not comply with rules.
	 */
	private void validateData(HashMap<Integer[], Double> percentagesForUnitsSold, double defaultPercentage) throws IllegalArgumentException {
		for (Integer[] range : percentagesForUnitsSold.keySet()) {
			if (range.length!= 2) {
				throw new IllegalArgumentException("Incorrect range (" + range + ") found as key in the HashMap passed as argument; " 
						+ "range should have two elements, to represent lower and upper boundaries of range.");
			} 
			if (range[0] > range[1]) {
				throw new IllegalArgumentException("Incorrect range (" + range + ") found as key in the HashMap passed as argument; " 
						+ "first element should be smaller or equal to second element.");
			}
			if (percentagesForUnitsSold.get(range) > 1 || percentagesForUnitsSold.get(range) < 0) {
				throw new IllegalArgumentException("Incorrect percentage (" + percentagesForUnitsSold.get(range) 
					+ ") found as value in the HashMap passed as argument; percentages should be a number between 0 and 1 (inclusive).");
			}
		}
		if (defaultPercentage > 1 || defaultPercentage < 0) {
			throw new IllegalArgumentException("Incorrect percentage (" + defaultPercentage 
			+ ") found as defaultPercentage argument; percentages should be a number between 0 and 1 (inclusive).");

		}
	}
}
