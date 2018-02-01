package main;

public class Person {
	
	private final String name;
	private double balance;
	
	public Person(String name) {
		this.name = name;
		this.balance = 0;
	}

	public String getName() {
		return name;
	}

	public double getBalance() {
		return balance;
	}
	
	public void addToBalance(double amount) {
		this.balance = this.balance + amount;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", balance=" + balance + "]";
	}

}
