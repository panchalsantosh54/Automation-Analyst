package com.fitpeotest;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class FitPeoTest {

	private WebDriver driver;
	private WebDriverWait wait;
	private JavascriptExecutor js;

	@BeforeClass
	public void setUp() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		js = (JavascriptExecutor) driver;
		driver.manage().window().maximize();
	}

	@Test
	public void testRevenueCalculator() throws InterruptedException {

		// Step 1: Navigate to the FitPeo HomePage
		driver.get("https://fitpeo.com");

		// Step 2: Navigate to the Revenue Calculator Page
		WebElement revenueCalculatorLink = wait
				.until(ExpectedConditions.elementToBeClickable(By.linkText("Revenue Calculator")));
		revenueCalculatorLink.click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

		// Step 3: Scroll down to the Slider section
		WebElement slider = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//div[@class=\"MuiBox-root css-j7qwjs\"]/span[1]")));
		js.executeScript("window.scrollBy(0,500)", "");
		Thread.sleep(2000);

		Actions actions = new Actions(driver);
		actions.clickAndHold(slider).moveByOffset(-27, 20).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT)
				.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).release().perform();

		// Step 4: Adjust the Slider to 820
		WebElement textField = driver.findElement(By.xpath(
				"//div[@class=\"MuiInputBase-root MuiOutlinedInput-root MuiInputBase-colorPrimary MuiInputBase-formControl MuiInputBase-sizeSmall css-1kkflqu\"]/input"));
		Assert.assertEquals(textField.getAttribute("value"), "820");

		// Step 5: Update the Text Field to 560
		Thread.sleep(2000);
		textField.sendKeys(Keys.CONTROL + "a");
		textField.sendKeys(Keys.DELETE);
		textField.sendKeys("560");
		Assert.assertEquals(560, 560);

		// Step 6:Select check Box
		js.executeScript("window.scrollBy(0,500)", "");
		// CPT-99091
		driver.findElement(By.xpath("//div[@class=\"MuiBox-root css-1p19z09\"]/div[1]/label/span")).click();
		// CPT-99453
		driver.findElement(By.xpath("//div[@class=\"MuiBox-root css-1p19z09\"]/div[2]/label/span")).click();
		// CPT-99454
		driver.findElement(By.xpath("//div[@class=\"MuiBox-root css-1p19z09\"]/div[3]/label/span")).click();
		// CPT-99474
		js.executeScript("window.scrollBy(0,500)", "");
		driver.findElement(By.xpath("//div[@class=\"MuiBox-root css-1p19z09\"]/div[8]/label/span")).click();
		
		/*
		String[] cptCodes = { "CPT-99091", "CPT-99453", "CPT-99454", "CPT-99474" };

		for (String code : cptCodes) {
			WebElement checkbox = driver
					.findElement(By.xpath("//label[normalize-space(text())='" + code + "']/following-sibling::input"));
			if (!checkbox.isSelected()) {
				checkbox.click();
			}
		}
		*/
		
		// Step7: Validate Total Recurring Reimbursement
		WebElement totalReimbursementHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//p[@class='MuiTypography-root MuiTypography-body1 inter css-hocx5c']")));
		String totalReimbursementText = totalReimbursementHeader.getText().trim();

		Assert.assertTrue(totalReimbursementText.contains("$95760"),
				"Total Recurring Reimbursement validation failed.");
	}

	@AfterClass
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
