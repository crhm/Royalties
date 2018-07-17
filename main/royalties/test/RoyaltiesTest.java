package main.royalties.test;

import importing.ImportFactory;
import main.royalties.RoyaltyPercentage;
import main.*;

public class RoyaltiesTest {
	
	public static void main(String args[]) {
		ImportFactory.ImportSales("Data/UsuableFormats/Apple payment data Oct 2017 -- financial_report.csv");
		ImportFactory.ImportSales("Data/UsuableFormats/Amazon payment data for Oct 2017 -- KDP Payments-1517170629138-0c18d9f0770c13d87289dfe806919f31.csv");
		ImportFactory.ImportSales("Data/UsuableFormats/85711804_1017_AU.txt");
		Book uACS = SalesHistory.get().getBook("Under A Cruel Star");
		Book jDPAB = SalesHistory.get().getBook("Jacqueline du Pré: A Biography");
		Person AAAAA = new Person("AAAAA");
		Person BB = new Person("BB");
		Person CCC = new Person("CCC");
		SalesHistory.get().addPerson(AAAAA);
		SalesHistory.get().addPerson(BB);
		SalesHistory.get().addPerson(CCC);
		SalesHistory.get().getChannel("Apple").addRoyalty(uACS, AAAAA, new RoyaltyPercentage(0.5));
		SalesHistory.get().getChannel("Apple").addRoyalty(uACS, BB, new RoyaltyPercentage(0.1));
		SalesHistory.get().getChannel("Apple").addRoyalty(jDPAB, CCC, new RoyaltyPercentage(0.25));
		for (Sale s : SalesHistory.get().getSalesHistory()) {
			System.out.println(s);
		}
		System.out.println("Echange rate: " + SalesHistory.get().getChannel("Apple").getHistoricalForex().get("Oct 2017").get("CAD"));
		System.out.println("AAAAA expected balance = $2.5 (approx)");
		System.out.println("BB expected balance = $0.5 (approx)");
		System.out.println("CCC expected balance = $2 (approx)");
		SalesHistory.get().calculateAllRoyalies();
		for (Person p : SalesHistory.get().getListRoyaltyHolders()) {
			System.out.println(p);
		}
	}

}
