package runners;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class XMLRunner {

	public static void main(String[] args) throws IOException {
		// "excel" or "tag"
		System.setProperty("runMode", System.getProperty("runMode", "excel"));
		System.setProperty("tagFilter", System.getProperty("tagFilter", "@Regression"));
		XmlSuite jobWebSiteSuite=new XmlSuite();
		jobWebSiteSuite.setName("jobWebSiteSuite");
		XmlTest test=new XmlTest(jobWebSiteSuite);
		List<XmlClass> testClass=new ArrayList<XmlClass>();
		testClass.add(new XmlClass("runners.TestRunner"));
		test.setXmlClasses(testClass);
		test.setName("Regression");
		List<String> listeners=new ArrayList<>();
		listeners.add("listeners.Listener");
		jobWebSiteSuite.setListeners(listeners);
		File fileName=new File("testng.xml");
		FileWriter writer=new FileWriter(fileName);
		writer.write(jobWebSiteSuite.toXml());
		writer.close();
		TestNG t=new TestNG();
		List<String> suiteList=new ArrayList<>();
		suiteList.add("testng.xml");
		t.setTestSuites(suiteList);
		t.run();		

	}

}
