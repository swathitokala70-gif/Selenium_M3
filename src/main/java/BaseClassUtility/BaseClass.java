package BaseClassUtility;

import java.io.IOException;
import java.sql.SQLException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import GenericUtilities.DataBaseUtility;
import GenericUtilities.PropertyFileUtility;
import GenericUtilities.UtilityObjectClass;
import GenericUtilities.WebDriverUtility;
import POMUtilities.HomePompage;
import POMUtilities.LoginPompage;

public class BaseClass {
	
	//mvn cmd 
	//mvn test -P batch -Dbrowser=edge -Durl=http://localhost:8888/ -Dusername=admin -Dpassword=admin -Dtimeouts=10

	public DataBaseUtility dutil = new DataBaseUtility();
	public PropertyFileUtility putil = new PropertyFileUtility();
	public WebDriverUtility wutil = new WebDriverUtility();
	public WebDriver driver = null;

	@BeforeSuite(alwaysRun=true)
	public void connectToDatabase() throws SQLException {
		Reporter.log("Get connection with the Database", true);
		dutil.connectToDatabase();

	}

	@BeforeTest(alwaysRun=true)
	public void configParallelExe() {
		Reporter.log("Configuration of parallel execution", true);
	}

//	@Parameters("browser")
	@BeforeClass(alwaysRun=true)
	public void launchTheBrowser() throws IOException {
		String browser = putil.fetchDataFromPropertyFile("browser");

		Reporter.log("Launch the browser", true);
		if (browser.equals("chrome")) {
			driver = new ChromeDriver();
		} else if (browser.equals("edge")) {
			driver = new EdgeDriver();
		} else if (browser.equals("firefox")) {
			driver = new FirefoxDriver();
		} else {
			driver = new ChromeDriver();
		}

		UtilityObjectClass.setSdriver(driver);

	}

	@BeforeMethod(alwaysRun=true)
	public void login() throws IOException {
		//String url = putil.fetchDataFromPropertyFile("url");
	//	String username = putil.fetchDataFromPropertyFile("username");
	//	String password = putil.fetchDataFromPropertyFile("password");
	//	String timeouts = putil.fetchDataFromPropertyFile("timeouts");
		
		String url=System.getProperty("url", putil.fetchDataFromPropertyFile("url"));
		String username=System.getProperty("username", putil.fetchDataFromPropertyFile("username"));
		String password=System.getProperty("password", putil.fetchDataFromPropertyFile("password"));
		String timeouts=System.getProperty("timeouts", putil.fetchDataFromPropertyFile("timeouts"));
		
		wutil.maximizeTheWindow(driver);
		wutil.waitForAnElement(driver, timeouts);
		wutil.navigateToAnAppln(driver, url);
		Reporter.log("Login to the application", true);

		LoginPompage l = new LoginPompage(driver);
		l.login(username, password);
	}

	@AfterMethod(alwaysRun=true)
	public void logout() {
		HomePompage home = new HomePompage(driver);
		Reporter.log("Logout from the application", true);
		wutil.mousehoverOnEle(driver, home.getAdmin());
		home.getSignout();
	}

	@AfterClass(alwaysRun=true)
	public void quitTheBrowser() {
		Reporter.log("Quit the Browser", true);
		wutil.quitTheBrowser(driver);
	}

	@AfterTest(alwaysRun=true)
	public void closeParallelExe() {
		Reporter.log("Close Parallel Execution", true);
	}

	@AfterSuite(alwaysRun=true)
	public void disconnectWithDatabase() throws SQLException {
		Reporter.log("Disconnect with the Database", true);
		dutil.closeDatabaseConnection();
	}
}
