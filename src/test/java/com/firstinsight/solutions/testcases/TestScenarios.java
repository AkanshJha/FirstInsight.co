package com.firstinsight.solutions.testcases;

import com.firstinsight.solutions.pageobject.AmazonHomePage;
import com.firstinsight.solutions.pageobject.AmazonLaunchPage;
import com.firstinsight.solutions.utilities.ReadPropertiesFile;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class TestScenarios {
    private final String currDir = System.getProperty("user.dir");
    protected String propFilePath = currDir + "\\configurations\\FirstInsight_Application.properties";
    private Properties prop = ReadPropertiesFile.loadPropertiesFile(propFilePath);
    String url = prop.getProperty("URL");
    public WebDriver driver = null;
    protected WebDriverWait exWait = null;
    private int iteration = 1;
    List<ArrayList<String>> productsInCart = new ArrayList<ArrayList<String>>();

    /**
     * This is a setup method, which is executed before any test starts executing.
     * In this, we are instantiating the Chrome webdriver and maximizing the window.
     */
    @BeforeTest
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get(url);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        exWait = new WebDriverWait(driver, 10);

    }

    /**
     * This test will be executed after the execution of the each tests in this class.
     */
    @AfterTest
    void tearDown() {
        driver.quit();
        System.out.println("Execution Completed...");
    }

    /**
     * This is the scenario 1, as per the assignment.
     *
     * @param map : It contains the parameters from the excel sheet, to be passed in the Test Case
     * @throws InterruptedException : It is added in signature because use of Thread.sleep();.
     */
    @Test(dataProvider = "DataProvider", dataProviderClass = com.firstinsight.solutions.utilities.ExcelDataProvider.class)
    void scenario_1(Map<Object, Object> map) throws InterruptedException {
        String userName = (String) map.get("userName");
        String password = (String) map.get("password");
        String mainMenuOption = (String) map.get("mainMenuOptions");
        String subMenuOption = (String) map.get("subMenuOptions");
        ArrayList<String> productDetail = new ArrayList<String>();
        String mainMenu_Xpath = "//div[text()='" + mainMenuOption + "']/parent::a";
        String subMenu_Xpath = "//a[text()='" + subMenuOption + "']";
        AmazonHomePage homePage = new AmazonHomePage(driver);
        AmazonLaunchPage launchPage = new AmazonLaunchPage(driver);
        //login once
        if (iteration++ == 1)
            launchPage.signInToApplication(userName, password);

        System.out.println(mainMenu_Xpath);
        System.out.println(subMenu_Xpath);

        Thread.sleep(1000);

        try {
            homePage.clickOnShopByCategory();
            homePage.mainMenuOption_WE = driver.findElement(By.xpath(mainMenu_Xpath));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", homePage.mainMenuOption_WE);
            homePage.mainMenuOption_WE.click();

            Thread.sleep(1000);

            homePage.subCategoryOption_WE = driver.findElement(By.xpath(subMenu_Xpath));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", homePage.subCategoryOption_WE);
            homePage.subCategoryOption_WE.click();

            Thread.sleep(1000);
        } catch (NoSuchElementException e) {
            homePage.clickOnShopByCategory();
            homePage.mainMenuOption_WE = driver.findElement(By.xpath(mainMenu_Xpath));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", homePage.mainMenuOption_WE);
            homePage.mainMenuOption_WE.click();

            Thread.sleep(1000);

            homePage.subCategoryOption_WE = driver.findElement(By.xpath(subMenu_Xpath));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", homePage.subCategoryOption_WE);
            homePage.subCategoryOption_WE.click();

            Thread.sleep(1000);
        } catch (ElementNotInteractableException e) {
            homePage.clickOnShopByCategory();
            homePage.mainMenuOption_WE = driver.findElement(By.xpath(mainMenu_Xpath));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", homePage.mainMenuOption_WE);
            homePage.mainMenuOption_WE.click();

            Thread.sleep(1000);

            homePage.subCategoryOption_WE = driver.findElement(By.xpath(subMenu_Xpath));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", homePage.subCategoryOption_WE);
            homePage.subCategoryOption_WE.click();

            Thread.sleep(1000);
        }

        homePage.selectRandomProductOnPage();
        productDetail.add(homePage.getProductName()); // Adding Project Name to the ArrayList
        productDetail.add(homePage.getProductPrice()); // Adding Product Price in the ArrayList
        productsInCart.add(productDetail); // Adding this product details to the Another ArrayList
        homePage.addProductToCart();

        if (map.size() == iteration) {
            Thread.sleep(1000);
            homePage.navigateToCart();
            for (int i = 0; i < homePage.getProductsCountInCart(); i++) {
                homePage.changeQuantityInCart(i, 2);
            }
            String deletedProduct = homePage.getProductNameFromCart(0);
            System.out.println("Deleted Product ==>> " + deletedProduct);
            homePage.deleteProduct_FromCart(0); // Deleting product on index 1 from the cart.
            //homePage.changeQuantityInCart(1,0); //Deleting product by setting quantity 0.
            Thread.sleep(1000);

            int i = 0;
            int indexToRemove = 0;
            for (ArrayList individualProductDetail : productsInCart) {
                for (Object ar : individualProductDetail) {
                    if (ar.toString().equalsIgnoreCase(deletedProduct)) {
                        //productsInCart.remove(i);
                        indexToRemove = i;
                        break;
                    }
                }
                System.out.println();
                i++;
            }
            productsInCart.remove(indexToRemove);
            homePage.clickOnProceedToCheckOut();
            driver.navigate().back();
            Thread.sleep(5000);
            launchPage.signOutFromApplication();

        }
    }

    /**
     * This is the second scenario as per the assignment.
     */
    @Test(dataProvider = "DataProvider", dataProviderClass = com.firstinsight.solutions.utilities.ExcelDataProvider.class)
    void scenario_2(Map<Object, Object> map) throws InterruptedException {
        AmazonLaunchPage launchPage = new AmazonLaunchPage(driver);
        AmazonHomePage homePage = new AmazonHomePage(driver);
        List<ArrayList<String>> actualProductsInCart = new ArrayList<ArrayList<String>>();
        ArrayList<String> prodDetails = new ArrayList<String>();

        launchPage.signInToApplication((String) map.get("userName"), (String) map.get("password"));
        homePage.navigateToCart();
        for (int i = 0; i < homePage.getProductsCountInCart(); i++) {
            prodDetails.add(homePage.getProductNameFromCart(i));
            prodDetails.add(homePage.getProductPriceFromCart(i));
            actualProductsInCart.add(prodDetails);
        }

        System.out.println("Scenario 2 ===> Validating after logout.\n\n");
        for (ArrayList individualProductDetail : actualProductsInCart) {
            for (Object ar : individualProductDetail) {
                System.out.print("  " + ar.toString() + " ===>> ");
            }
            System.out.println();
        }

        new SoftAssert().assertTrue(compareListOfLists(productsInCart, actualProductsInCart));
        launchPage.signOutFromApplication();

    }

    /**
     * This method compares the two List of ArrayList objects for equality to validate the products in cart.
     * @param expectedList
     * @param actualList
     * @return
     */
    public boolean compareListOfLists(List<ArrayList<String>> expectedList, List<ArrayList<String>> actualList) {
        ArrayList<String> innerExpected = null;
        ArrayList<String> innerActual = null;
        boolean result = false;
        if (expectedList.size() != actualList.size()) {
            System.out.println("Lists are not equal.");
            return result;
        }
        for (int i = 0; i < expectedList.size(); i++) {
            innerExpected = expectedList.get(i);
            innerActual = actualList.get(i);
            for (int j = 0; j < expectedList.size(); j++) {
                System.out.println("Expected Text == >>" + innerExpected.get(j));
                System.out.println("Actual Text == >" + innerActual.get(j));
                if (!innerExpected.get(j).equals(innerActual.get(j))) {
                    return result;
                }
            }
        }
        result = true;
        return result;
    }

}
