package main;

/**Interface which all types of royalties must implement, to allow the polymorphic call to getAmountDue() on any
 * royalty type.
 * @author crhm
 *
 */
public interface IRoyaltyType {
	
	/**Returns the amount to add to the balance of the royalty holder, given the revenue PLP made for a sale.
	 * <br>The total number of units sold for the given item is required in case the royalty type depends on it.
	 * @param revenuesPLP revenue PLP made on a sale.
	 * @param totalUnitsSold total number of units sold for the given item so far.
	 * @return The amount to add to the balance of the royalty holder
	 */
	public double getAmountDue(double revenuesPLP, double totalUnitsSold);

}
