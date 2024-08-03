package com.fitpeotest;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

public class FitPeoAutomation {

	private WebDriver driver;

	@BeforeClass
	public void setUp() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}

	@Test
	public void testRevenueCalculator() {

		// Step1: Navigate to the FitPeo HomePage
		driver.get("https://fitpeo.com");

		// Step2: Navigate to the Revenue Calculator Page
		WebElement revenueCalculatorLink = driver.findElement(By.linkText("Revenue Calculator"));
		revenueCalculatorLink.click();

		// Step3: Scroll Down to the Slider Section
		WebElement sliderSection = driver.findElement(By.xpath("//div[@class='MuiBox-root css-j7qwjs']/span[1]"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", sliderSection);

		// Step4: Adjust the Slider to Set its value to 820
		WebElement slider = driver.findElement(By.xpath(
				"//div[@class='MuiInputBase-root MuiOutlinedInput-root MuiInputBase-colorPrimary MuiInputBase-formControl MuiInputBase-sizeSmall css-1kkflqu']/input"));
		Actions action = new Actions(driver);
		action.clickAndHold(slider).moveByOffset(100, 0).release().build().perform();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Verify the slider's value is updated to 820
		WebElement sliderValueField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"//div[@class='MuiGrid-root MuiGrid-item MuiGrid-grid-xs-12 MuiGrid-grid-md-6 css-iol86l']//div[2]")));
		String actualSliderValue = sliderValueField.getText().replaceAll("[^0-9]", "").trim();

		// Debugging
		System.out.println("Actual slider value: " + actualSliderValue);

		// Assert the slider's value is correctly set to 820
		Assert.assertEquals(actualSliderValue, "820", "Failed to set the slider to 820.");

		// Step5: Update the text field to 560
		WebElement textField = driver.findElement(By.xpath("//input[@id='text-field-id']"));
		textField.clear();
		textField.sendKeys("560");

		// Verify the slider's value is updated to 560 after updating the text field
		sliderValueField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"//div[@class='MuiGrid-root MuiGrid-item MuiGrid-grid-xs-12 MuiGrid-grid-md-6 css-iol86l']//div[2]")));
		String updatedSliderValue = sliderValueField.getText().replaceAll("[^0-9]", "").trim();

		// Debugging
		System.out.println("Updated slider value: " + updatedSliderValue);

		Assert.assertEquals(updatedSliderValue, "560", "Failed to update the slider to 560.");

		// Step6: Select CPT Codes
		String[] cptCodes = { "CPT-99091", "CPT-99453", "CPT-99454", "CPT-99474" };

		for (String code : cptCodes) {
			WebElement checkbox = driver
					.findElement(By.xpath("//label[normalize-space(text())='" + code + "']/following-sibling::input"));
			if (!checkbox.isSelected()) {
				checkbox.click();
			}
		}

		// Step7: Validate Total Recurring Reimbursement
		WebElement totalReimbursementHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//p[@class='MuiTypography-root MuiTypography-body1 inter css-hocx5c']")));
		String totalReimbursementText = totalReimbursementHeader.getText().trim();

		// Debugging 
		System.out.println("Total reimbursement text: " + totalReimbursementText);

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
