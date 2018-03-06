package main.royalties;

/**Royalty type that sets a fixed amount which is owed per sale regardless of sale revenue.
 * <br>For example: $0.05 per sale.
 * @author crhm
 */
public class RoyaltyFixedAmount implements IRoyaltyType, java.io.Serializable {

	private static final long serialVersionUID = 2681735788487282364L;
	private double fixedAmount;
	
	/**RoyaltyFixedAmount constructor. Initialises the fixed amount to the argument passed.
	 * @param fixedAmount amount in dollars owed per sale to the royalty holder
	 */
	public RoyaltyFixedAmount(double fixedAmount) {
		this.fixedAmount = fixedAmount;
	}
	
	/**Returns the amount to add to the balance of the royalty holder for the sale of the item.
	 * <br>Note: parameter cumulativeSalesOfBook is ignored (for this type of royalty, since it is a fixed amount).
	 * <br>Note: parameter revenuesPLP is ignored (for this type of royalty, since it is a fixed amount).
	 */
	@Override
	public double getAmountDue(double revenuesPLP, double totalUnitsSold) {
		return fixedAmount;
	}
	
	/**Returns the fixed amount (in dollars) owed to the royalty holder per sale of the item.
	 * @return double - the fixed amount (in dollars) owed to the royalty holder per sale of the item.
	 */
	public double getFixedAmount() {
		return fixedAmount;
	}

	@Override
	public String toString() {
		return "RoyaltyFixedAmount: $" + fixedAmount;
	}

}
