package main;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**Class that represents a person who has a balance, likely because they have a royalty on one of PLP's books.
 * <br>A Person has a name and a balance, to which an amount can be added (or substracted) via the addToBalance method.
 * <br>A balanace may be negative, and is always in USD.
 * @author crhm
 *
 */
public class Person implements java.io.Serializable {

	private static final long serialVersionUID = -6122237628268675623L;
	private final String name;
	private double balance;

	/**Person constructor. Initialises Person name to the String passed as argument by the user, and Person balance to 0.
	 * @param name String name of Person.
	 */
	public Person(String name) {
		this.name = name;
		this.balance = 0;
	}

	public String getName() {
		return name;
	}

	public double getBalance() {
		BigDecimal tempAmount = new BigDecimal(this.balance).setScale(2, RoundingMode.HALF_UP);
		return tempAmount.doubleValue();
	}

	/**Adds double passed as argument to the person's balance (rounded half up to 2 decimal places).
	 * 
	 * @param amount Amount to add to balance.
	 */
	public void addToBalance(double amount) {
		BigDecimal tempAmount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);
		this.balance = this.balance + tempAmount.doubleValue();
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", balance=" + balance + "]";
	}

}
