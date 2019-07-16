//        1. Открыть главную страницу automationpractice.com
//        2. В поле поиска ввести “Blouse” и выполнить поиск
//        3. Переключится на режим просмотра ‘List”
//        4. Добавить товар в корзину
//        5. В секции Summary увеличить количество товаров на 1
//        6. Проверить что значения Total для товара , Total products, Total
//        shipping , Total всех товаров , Tax и TOTAL общий отображается
//        корректно
//        7. Удалить товар из корзины
//        8. Проверить что корзина пустая


package com.prostoshop.app;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class AppTest {

    public WebDriver driver;

    @Before
    public void setup() {
        Properties configur = new Properties();
        try {
            configur.load(new FileInputStream("G:\\QA Auto Courses\\Lesson9_2\\src\\conf.properties"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.setProperty("webdriver.chrome.driver", configur.getProperty("chromedriver"));
        driver = new ChromeDriver();
        driver.get(configur.getProperty("baseurl"));
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }

    @Test
    public void ourTest() {

        // search for Blouse
        runSearching(driver);

        // find and press search button
        pressSearchButton(driver);

        // switch page to list view
        switchToListView(driver);

        // Add goods to the cart
        addGoodsToCart(driver);

        // go to the Summary page
        moveToSummaryPage(driver);
        //System.out.println(driver.getTitle());

        // increase the quantity of goods
        increaseGoodsQuantity(driver);

        // Get Total product price
        double goodsTotal = getTotal(driver, "//span[@id='total_product_price_2_7_0']");
        Assert.assertEquals("The total values for GoodsTotal is not correct", 54, goodsTotal, 0);

        // Get Total product
        double productsTotal = getTotal(driver, "//td[@id='total_product']");
        Assert.assertEquals("The total values for ProductTotal is not correct", 54, productsTotal, 0);

        // Get Total shipping
        double shippingTotal = getTotal(driver, "//td[@id='total_shipping']");
        Assert.assertEquals("The total values for ShippingTotal is not correct", 2, shippingTotal, 0);

        // Get Total without tax
        double allGoodsTotal = getTotal(driver, "//td[@id='total_price_without_tax']");
        Assert.assertEquals("The total values for allGoodsTotal is not correct", 56, allGoodsTotal, 0);

        // Get Total tax
        double tax = getTotal(driver, "//td[@id='total_tax']");
        Assert.assertEquals("The total values for Tax is not correct", 0, tax, 0);

        // Get Total price
        double sum = getTotal(driver, "//span[@id='total_price']");
        Assert.assertEquals("The total values for sum is not correct", 56, sum, 0);

        // Deleting the goods from the cart
        deleteGoodsFromCart(driver);

        //Verify the cart is empty
        Assert.assertTrue("The cart is not empty", isCartEmpty(driver));
    }

    @After
    public void cleanup() {
        driver.manage().deleteAllCookies();
        driver.close();
    }

    public void runSearching(WebDriver driver) {

        // find Search field and enter "Blouse"
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='search_query_top']")));
        WebElement searchFiled = driver.findElement(By.xpath("//input[@id='search_query_top']"));
        searchFiled.clear();
        searchFiled.sendKeys("Blouse");
    }

    // find and press search button
    public void pressSearchButton(WebDriver driver) {
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@name='submit_search']")));
        WebElement searchButton = driver.findElement(By.xpath("//button[@name='submit_search']"));
        searchButton.click();
    }

    public void switchToListView(WebDriver driver) {
        //wait for list button
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"list\"]/a/i")));

        // find button to switch the list
        WebElement listViewButton = driver.findElement(By.xpath("//*[@id=\"list\"]/a/i"));
        listViewButton.click();
    }

    public void addGoodsToCart(WebDriver driver) {

        // add goods to the cart
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@class='button ajax_add_to_cart_button btn btn-default']")));
        WebElement addButton = driver.findElement(By.xpath("//a[@class='button ajax_add_to_cart_button btn btn-default']"));
        addButton.click();

        //close modal window
        WebElement closeButt = driver.findElement(By.xpath("//span[@title='Close window']"));
        closeButt.click();
    }

    // go to Summary page
    public void moveToSummaryPage(WebDriver driver) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@title='View my shopping cart']")));
        WebElement cartButton = driver.findElement(By.xpath("//a[@title='View my shopping cart']"));
        cartButton.click();
    }

    // increase goods quantity
    public void increaseGoodsQuantity(WebDriver driver) {

        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@id='cart_quantity_up_2_7_0_0']")));
        WebElement increaseButton = driver.findElement(By.xpath("//a[@id='cart_quantity_up_2_7_0_0']"));
        increaseButton.click();
        //Waiting for the table recalculating
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public double getTotal(WebDriver driver, String xPath) {
        double res = 0;
        WebElement total = driver.findElement(By.xpath(xPath));
        String strTotal = total.getText();
        strTotal = strTotal.substring(1, strTotal.length());
        res = Double.parseDouble(strTotal);
        return res;
    }

    public void deleteGoodsFromCart(WebDriver driver) {

        new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath("//i[@class='icon-trash']")));
        WebElement deleteButton = driver.findElement(By.xpath("//i[@class='icon-trash']"));
        deleteButton.click();
    }

    public boolean isCartEmpty(WebDriver driver) {
        // wait while cart will be cleared
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement emptyMessage = driver.findElement(By.xpath("//p[@class='alert alert-warning']"));
        if (emptyMessage.getText().equals("Your shopping cart is empty.")) {
            return true;
        } else return false;
    }
}
