package main.royalties;

import java.text.NumberFormat;

/**Standard royalty type; amount due is a straight percentage of the revenues PLP made on a sale of the item.
 * <br>Example: 20 % of PLP revenue on sales of the book
 * @author crhm
 *
 */
public class RoyaltyPercentage implements IRoyaltyType, java.io.Serializable {

	private static final long serialVersionUID = 591464409407047796L;
	private double percentage;

	/**RoyaltyPercentage constructor. Initialises the percentage of revenue to be given to royalty holder as the argument passed by user.
	 * @param percentage Percentage to be set as royalty. Must be a number between 0 and 1.
	 * @throws IllegalArgumentException if percentage is not a number between 0 and 1
	 */
	public RoyaltyPercentage(double percentage) {
		if (percentage > 1 || percentage < 0) {
			throw new IllegalArgumentException("Percentage must be represented as a number between 0 and 1.");
		}
		this.percentage = percentage;
	}

	/**Returns the amount to add to the balance of the royalty holder given the revenues made by PLP on the sale of the item.
	 * <br>Note: parameter cumulativeSalesOfBook is ignored (for this type of royalty).
	 */
	@Override
	public double getAmountDue(double revenuesPLP, double cumulativeSalesOfBook) {
		return percentage * revenuesPLP;
	}

	@Override
	public String toString() {
		NumberFormat percentFormat = NumberFormat.getPercentInstance();
		percentFormat.setMaximumFractionDigits(2);
		return "Royalty Percentage: " + percentFormat.format(percentage);
	}

	public double getPercentage() {
		return percentage;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(percentage);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoyaltyPercentage other = (RoyaltyPercentage) obj;
		if (Double.doubleToLongBits(percentage) != Double.doubleToLongBits(other.percentage))
			return false;
		return true;
	}

}
