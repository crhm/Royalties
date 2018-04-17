package main.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import main.royalties.RoyaltyFixedAmount;
import main.royalties.RoyaltyPercentage;
import main.royalties.test.RoyaltyDependentOnUnitsSoldTest;

@RunWith(Suite.class)
@SuiteClasses({ BookTest.class,
	ChannelTest.class,
	PersonTest.class,
	SaleTest.class,
	RoyaltyPercentage.class,
	RoyaltyDependentOnUnitsSoldTest.class,
	RoyaltyFixedAmount.class,
	DataVerificationTest.class,
	SalesHistoryTest.class,
	ObjectFactoryTest.class})

public class MainTestSuite {

}
