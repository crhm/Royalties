package main;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

/**Class that represents a person who has a balance, likely because they have a royalty on one of PLP's books.
 * <br>A Person has a name and a balance, to which an amount can be added (or substracted) via the addToBalance method.
 * <br>A balanace may be negative, and is always in USD.
 * @author crhm
 *
 */
public class Person implements java.io.Serializable {

	private static final long serialVersionUID = -6122237628268675623L;
	private String name;
	private Set<String> listNames = new HashSet<String>();
	private double balance;
	private final long personNumber; 

	/**Person constructor. Initialises Person name to the String passed as argument by the user (removing quote characters), 
	 * and Person balance to 0.
	 * @param name String name of Person. Cannot be empty or null.
	 * @throws IllegalArgumentException if person name is empty or null
	 */
	public Person(String name) {
		this.personNumber = SalesHistory.get().getNextPersonID();
		validateName(name);
		this.name = name.replace("\"", "");
		listNames.add(name);
		this.balance = 0;
	}

	public String getName() {
		return name;
	}

	/**
	 * @return the listNames
	 */
	public Set<String> getListNames() {
		return listNames;
	}

	/**
	 * @param listNames the listNames to set
	 */
	public void setListNames(Set<String> listNames) {
		this.listNames = listNames;
	}
	
	public void addName(String name) {
		this.listNames.add(name);
	}

	/**
	 * @return the personNumber
	 */
	public long getPersonNumber() {
		return personNumber;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	
	public void merge(Person p) {
		this.listNames.add(p.getName());
		for (String s : p.getListNames()) {
			this.listNames.add(s);
		}
		this.addToBalance(p.getBalance());
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", balance=" + balance + "]";
	}
	
	/**Checks if name is empty or null
	 * @throws IllegalArgumentException if field takes unauthorised value
	 */
	private void validateName(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Error: name cannot be empty or null.");
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (personNumber ^ (personNumber >>> 32));
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
		Person other = (Person) obj;
		if (personNumber != other.personNumber)
			return false;
		return true;
	}

}
