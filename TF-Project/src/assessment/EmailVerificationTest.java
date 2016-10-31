package assessment;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;

import net.sourceforge.htmlunit.corejs.javascript.ObjArray;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EmailVerificationTest {

	WebDriver driver;
	WebDriverWait wait;
	
	/*	setUp will run every time before the @Test method is begins.*/ 
	@BeforeMethod
	public void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver", "C:\\SeleniumDriver\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 10);
		driver.manage().window().maximize();
	}
	
	/*	tearDown will run every time after the @Test method is finished.*/ 
	@AfterMethod
	public void tearDown() throws Exception {
		driver.quit();
	}
	
	
	/*	emailValidationTest test is validating correctness of emails field in the live chat window.
	 *  the test is a data driven test witch means that it runs the amount of times as the data is provided (in the @DataProvider below)
	 * 	when there will be a lot of data, we will use external excel file to store the data and to be the data provider for the test.
	 *  in this way the test is written once and repeats itself as long as the data exist.
	 */
	@Test(dataProvider="EmailVerification")
	public void emailValidationTest(String name, String email, String message, String emailErrorMessage) throws Exception {
		driver.get("https://www.24option.com/24option");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("chat"))).click();
		
		//get the handels of both windows
		Set<String> windows = driver.getWindowHandles();
		Iterator<String> itr = windows.iterator();

		String patName = itr.next();  //patName will provide you parent window	
		String chldName = itr.next();  //chldName will provide you child window
		
		driver.switchTo().window(chldName.toString());
		
		driver.findElement(By.xpath("//label[@__jx__id='___$_29__profile__internal_placeholder']")).click(); // click on name,email
		driver.findElement(By.id("___$_29__profile__name")).sendKeys(name); //set name
		driver.findElement(By.id("___$_29__profile__email")).sendKeys(email); //set email
		driver.findElement(By.xpath("//textarea[@name='message']")).sendKeys(message); // set message
		
		String emailText = driver.findElement(By.xpath("//div[@__jx__id='___$_29__profile__email_error']")).getText();
		Assert.assertEquals(emailErrorMessage, emailText);
	}
	
	/* This is the data provided for the emailValidationTest*/
	@DataProvider(name="EmailVerification")
	public Object [][]passData()
	{
		Object[][] data = new Object[6][4];
		
		data[0][0] = "Name";
		data[0][1] = "Abc.example.com";
		data[0][2] = "Message";
		data[0][3] = "Please provide a valid email";
		
		data[1][0] = "Name";
		data[1][1] = "A@b@c@example.com";
		data[1][2] = "Message";
		data[1][3] = "Please provide a valid email";
		
		data[2][0] = "Name";
		data[2][1] = "a\"" + "b(c)d,e:f;g<h>i[j\\k]l@example.com";
		data[2][2] = "Message";
		data[2][3] = "Please provide a valid email";
		
		data[3][0] = "Name";
		data[3][1] = "just\"" + "not\"" + "right@example.com";
		data[3][2] = "Message";
		data[3][3] = "Please provide a valid email";
		
		data[4][0] = "Name";
		data[4][1] = "john..doe@example.com";
		data[4][2] = "Message";
		data[4][3] = "Please provide a valid email";
		
		data[5][0] = "Name";
		data[5][1] = "john.doe@example..com";
		data[5][2] = "Message";
		data[5][3] = "Please provide a valid email";
		
		return data;
	}

	

}
