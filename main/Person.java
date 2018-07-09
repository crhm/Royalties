package main;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

/**Class that represents a person who has a balance, likely because they have a royalty on one of PLP's books.
 * <br>A Person has a main name and a list of names (for alternative spellings or order). Neither may be empty or null. 
 * The list of names will always at least contain the main name.
 * <br>A person has a balance, to which an amount can be added (or substracted) via the addToBalance method.
 * <br>A balanace may be negative, and is always in USD.
 * <br>A Person has a unique identifying personNumber, set by SalesHistory upon creation.
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
	 * <br>personNumber is obtained from SalesHistory.
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

	//GET METHODS
	public String getName() {
		return name;
	}

	public Set<String> getListNames() {
		return listNames;
	}
	
	public long getPersonNumber() {
		return personNumber;
	}

	/** 
	 * @return the person's balance, rounded to two decimal places.
	 */
	public double getBalance() {
		BigDecimal tempAmount = new BigDecimal(this.balance).setScale(2, RoundingMode.HALF_UP);
		return tempAmount.doubleValue();
	}
	
	//SET METHODS
	/**
	 * @param name the name to set
	 * @throws IllegalArgumentException if name empty or null
	 */
	public void setName(String name) {
		validateName(name);
		this.name = name;
	}
	
	/**
	 * @param listNames the listNames to set
	 * @throws IllegalArgumentException if any strings in listNames are empty or null
	 */
	public void setListNames(Set<String> listNames) {
		for (String s : listNames) {
			validateName(s);
		}
		this.listNames = listNames;
	}

	//ADD METHODS
	/**
	 * @param name
	 * @throws IllegalArgumentException if name empty or null
	 */
	public void addName(String name) {
		validateName(name);
		this.listNames.add(name);
	}
	
	/**Adds double passed as argument to the person's balance
	 * 
	 * @param amount Amount to add to balance.
	 */
	public void addToBalance(double amount) {
		this.balance = this.balance + amount;
	}
	
	//MERGE METHOD
	/**To merge two persons into one
	 * <br>Adds all of p's names to this one's. Adds its balance to this one's.
	 * <br>Removes p from list of persons
	 * @param p Person which will merged into this one.
	 * @throws IllegalArgumentException if person p is null
	 */
	public void merge(Person p) {
		if (p == null) {
			throw new IllegalArgumentException("Error: person with which to merge cannot be null.");
		}

		SalesHistory.get().replacePerson(p, this);
		
		this.listNames.add(p.getName());
		for (String s : p.getListNames()) {
			this.listNames.add(s);
		}
		this.addToBalance(p.getBalance());
		SalesHistory.get().removePerson(p);
	}
	
	//VALIDATION METHODS
	/**Checks if name is empty or null
	 * @throws IllegalArgumentException if field takes unauthorised value
	 */
	private void validateName(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Error: name cannot be empty or null.");
		}
	}

	//GENERATED METHODS
	@Override
	public String toString() {
		return name;
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
