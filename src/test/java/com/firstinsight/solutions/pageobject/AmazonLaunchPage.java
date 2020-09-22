package com.firstinsight.solutions.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import java.util.concurrent.TimeUnit;

public class AmazonLaunchPage {
    WebDriver driver = null;
    Actions ac = null;
    final WebDriverWait wait;

    public AmazonLaunchPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        ac = new Actions(driver);
        wait = new WebDriverWait(driver, 70);
    }

    @FindBy(xpath = "//span[text()='Account & Lists']")
    WebElement accounts_List_WE;

    @FindBy(xpath = "//a/span[text()='Sign in']")
    WebElement signIn_B;

    @FindBy(xpath = "//input[@name='email']")
    WebElement email_IB;

    @FindBy(xpath = "//input[@id='continue']")
    WebElement email_Continue_B;

    @FindBy(xpath = "//input[@name='password']")
    WebElement password_IB;

    @FindBy(xpath = "//input[@id='signInSubmit']")
    WebElement signINToApplication_B;

    @FindBy(xpath = "//span[contains(text(),'s Amazon.com')]")
    WebElement usersAmazon_WE;

    @FindBy(xpath = "//h1[contains(text(),'Sign-In')]")
    WebElement heading_SignIn_WE;

    @FindBy(xpath = "//a/span[text()='Sign Out']")
    WebElement signOut_Link;

    /**
     * This methos allows use to sign out from the application.
     * @throws InterruptedException
     */
    public void signOutFromApplication() throws InterruptedException {
        Thread.sleep(500);
        ac.moveToElement(accounts_List_WE).build().perform();
        Thread.sleep(500);
        signOut_Link.click();
        if (wait.until(ExpectedConditions.visibilityOf(heading_SignIn_WE)).isDisplayed()) {
            new SoftAssert().assertTrue(true);
            System.out.println("Signed Out successfully.");
        } else {
            new SoftAssert().assertTrue(false);
            System.out.println("Could not Sign Out.");
        }
    }

    /**
     * This method allows user to Sign into the Amazon.com application.
     * @param userName
     * @param password
     * @throws InterruptedException
     */
    public void signInToApplication(String userName, String password) throws InterruptedException {
        //wait = new WebDriverWait(driver, 70);
        ac.moveToElement(accounts_List_WE).build().perform();
        signIn_B.click();
        email_IB.sendKeys(userName);
        email_Continue_B.click();

        password_IB.sendKeys(password);
        signINToApplication_B.click();

        if (wait.until(ExpectedConditions.visibilityOf(usersAmazon_WE)).isDisplayed()) {
            new SoftAssert().assertTrue(true);
            System.out.println("Signed in successfully.");
        } else {
            new SoftAssert().assertTrue(false);
            System.out.println("Could not Sign IN.");
        }
    }

}
