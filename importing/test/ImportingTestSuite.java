package importing.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AmazonFileFormatTest.class, 
	AppleFileFormatTest.class, 
	CreatespaceFileFormatTest.class, 
	KoboFileFormatTest.class,
	NookFileFormatTest.class,
	AmazonForexFileFormatTest.class,
	AppleForexFileFormatTest.class,
	ImportFactoryTest.class })
public class ImportingTestSuite {

}
