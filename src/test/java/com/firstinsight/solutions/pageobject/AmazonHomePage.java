package com.firstinsight.solutions.pageobject;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Random;

public class AmazonHomePage {
    WebDriver driver = null;
    WebDriverWait wait;

    public AmazonHomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 20);
    }

    @FindBy(xpath = "//a[@id='nav-hamburger-menu']")
    WebElement hamburgerMenu_B;

    @FindBy(xpath = "//div[text()='Electronics']/parent::a")
    public WebElement mainMenuOption_WE;

    @FindBy(xpath = "//a[text()='Headphones']")
    public WebElement subCategoryOption_WE;

    @FindBy(xpath = "//h2/a[contains(@class,'a-link-normal a-text-normal')]/span[contains(@class,'a-color-base a-text-normal')]")
    List<WebElement> products_OnPage_WE;

    @FindBy(xpath = "//input[@id='add-to-cart-button']")
    WebElement addTOCart_B;

    @FindBy(xpath = "//span[contains(@class,'sc-product-title a-text-bold')]")
    List<WebElement> productName_InCart_WE;

    @FindBy(xpath = "//span[contains(@class,'sc-product-price a-text-bold')]")
    List<WebElement> productPrice_InCart_WE;

    @FindBy(xpath = "//span[@data-action='a-dropdown-button']")
    List<WebElement> quantity_InCart_dropdown;

    @FindBy(xpath = "//a[contains(@class,'a-dropdown-link') and contains(text(),'2')]")
    List<WebElement> changeQuantity_InCart_WE;

    @FindBy(xpath = "//input[@name='proceedToRetailCheckout']")
    WebElement proceedToCheckOut_B;

    @FindBy(xpath = "//h1[text()='Select a shipping address']")
    WebElement afterProceedToCheckOut_WE;

    @FindBy(xpath = "//input[contains(@value,'Delete')]")
    List<WebElement> deleteProduct_FromCart_WE;

    @FindBy(xpath = "//span[(@class='nav-line-2') and contains(text(),'Cart')]")
    WebElement cart_B;

    @FindBy(xpath = "//h1[contains(text(),'Shopping Cart')] | //h2[contains(text(),'Shopping Cart')]")
    WebElement shoppingCart_Page;

    @FindBy(xpath = "//a[contains(@class,'close-button')]")
    WebElement closeCart_Popup;

    @FindBy(xpath = "//span[@id='productTitle']")
    WebElement productName_WE;

    @FindBy(xpath = "//span[@id='priceblock_ourprice']")
    WebElement productPrice_WE;

    @FindBy(xpath = "//span[@id='priceblock_saleprice']")
    WebElement salers_ProductPrice_WE;

    /**
     * This method returns the main selected product's Name/Title
     * @return : Returns String Name/Title of the current Product.
     */
    public String getProductName() {
        return productName_WE.getText().trim();
    }

    /**
     *
     * @return It returns the price of the current product's price as string
     */
    public String getProductPrice() {
        try {
            return productPrice_WE.getText().trim();
        } catch (NoSuchElementException e) {
            return salers_ProductPrice_WE.getText().trim();
        }
    }

    /**
     * This method navigated to the Cart of the user.
     * @throws InterruptedException
     */
    public void navigateToCart() throws InterruptedException {
        try {
            cart_B.click();
        } catch (ElementClickInterceptedException e) {
            new Actions(driver).moveToElement(closeCart_Popup).click().build().perform();
            System.out.println("popup is closed.");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cart_B);
        }
        Thread.sleep(1000);
        if (shoppingCart_Page.isDisplayed()) {
            new SoftAssert().assertTrue(true);
            System.out.println("Navigated to Shopping Cart Page Successfully.");
        } else {
            new SoftAssert().assertTrue(false);
            System.out.println("Could not navigated to Shopping Cart Page.");
        }
    }

    /**
     * This method clicks on the Hamburger icon.
     */
    public void clickOnShopByCategory() {
        try {
            //((JavascriptExecutor) driver).executeScript("arguments[0].click();", hamburgerMenu_B);
            hamburgerMenu_B = wait.until(ExpectedConditions.elementToBeClickable(hamburgerMenu_B));
            new Actions(driver).moveToElement(hamburgerMenu_B).click().build().perform();
        } catch (ElementClickInterceptedException e) {
            new Actions(driver).moveToElement(closeCart_Popup).click().build().perform();
            System.out.println("popup is closed.");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", hamburgerMenu_B);
        }
    }

    /**
     * This method selects the random product from the current list of the products
     * @throws InterruptedException
     */
    public void selectRandomProductOnPage() throws InterruptedException {
        int numOfProductsOnPage = products_OnPage_WE.size();
        int randomProductNumber = new Random().nextInt(numOfProductsOnPage);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", products_OnPage_WE.get(randomProductNumber));
        Thread.sleep(1000);
        products_OnPage_WE.get(randomProductNumber).click();
    }

    /**
     * This method clicks on the Add to Cart button for the product.
     */
    public void addProductToCart() {
        addTOCart_B.click();
    }

    /**
     * Returns the product Name from the cart as per the given index.
     * @param productIndexInCart : index of the product
     * @return
     */
    public String getProductNameFromCart(int productIndexInCart) {
        return productName_InCart_WE.get(productIndexInCart).getText().trim();
    }

    /**
     * Returns the price of the product as the given index
     * @param productIndexInCart : Index of the product to get price of.
     * @return : Returns the price of the product as the given index
     */
    public String getProductPriceFromCart(int productIndexInCart) {
        return productPrice_InCart_WE.get(productIndexInCart).getText().trim();
    }

    /**
     *
     * @return : Returns the number of products in the cart.
     */
    public int getProductsCountInCart() {
        return productName_InCart_WE.size();
    }

    /**
     * Method to change the quantity of the product.
     * @param productIndexInCart : Index of the product to be modified
     * @param quantity : Quantity of the product
     * @throws InterruptedException
     */
    public void changeQuantityInCart(int productIndexInCart, int quantity) throws InterruptedException {
        String quantity_xpath = String.format("//a[contains(@class,'a-dropdown-link') and contains(text(),'%s')]", quantity);
        WebElement quantity_WE = null;
        WebElement dropdownForCurrentProduct = wait.until(ExpectedConditions.elementToBeClickable(quantity_InCart_dropdown.get(productIndexInCart)));

        Thread.sleep(1000);
        //changeQuantity_InCart_WE = driver.findElements(By.xpath(quantity_xpath));
        //System.out.println("Xpath - " + quantity_xpath);
        //System.out.println("size - " + changeQuantity_InCart_WE.size());
        try {
            new Actions(driver).moveToElement(dropdownForCurrentProduct).click().build().perform();
            System.out.println("Quantity dropdown is clicked for index " + productIndexInCart);
            changeQuantity_InCart_WE = driver.findElements(By.xpath(quantity_xpath));
            Thread.sleep(1000);
            quantity_WE = wait.until(ExpectedConditions.elementToBeClickable(changeQuantity_InCart_WE.get(productIndexInCart)));
            new Actions(driver).moveToElement(quantity_WE).click().build().perform();
        } catch (IndexOutOfBoundsException e) {
            new Actions(driver).moveToElement(dropdownForCurrentProduct).click().build().perform();
            changeQuantity_InCart_WE = driver.findElements(By.xpath(quantity_xpath));
            Thread.sleep(1000);
            quantity_WE = wait.until(ExpectedConditions.elementToBeClickable(changeQuantity_InCart_WE.get(productIndexInCart)));
            new Actions(driver).moveToElement(quantity_WE).click().build().perform();
        }
        //changeQuantity_InCart_WE.get(productIndexInCart).click();
    }

    /**
     * Method to click on Proceed to checkout button
     * @throws InterruptedException
     */
    public void clickOnProceedToCheckOut() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(proceedToCheckOut_B)).click();
        //proceedToCheckOut_B.click();
        Thread.sleep(1000);
        if (afterProceedToCheckOut_WE.isDisplayed()) {
            new SoftAssert().assertTrue(true);
            System.out.println("Clicked on Proceed to checkout successfully.");
        } else {
            new SoftAssert().assertTrue(false);
            System.out.println("Could not Click on Proceed to checkout.");
        }
    }

    /**
     * This method deletes the product as per the given index
     * @param productIndexInCart : index of the product to be deleted.
     * @throws InterruptedException
     */
    public void deleteProduct_FromCart(int productIndexInCart) throws InterruptedException {
        //wait.until(ExpectedConditions.elementToBeClickable(deleteProduct_FromCart_WE.get(productIndexInCart))).click();
        new Actions(driver).moveToElement(deleteProduct_FromCart_WE.get(productIndexInCart)).doubleClick().build().perform();
        Thread.sleep(1000);
        deleteProduct_FromCart_WE.get(productIndexInCart).click();
        //((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteProduct_FromCart_WE.get(productIndexInCart));
    }
}
