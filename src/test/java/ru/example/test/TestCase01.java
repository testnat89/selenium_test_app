package ru.example.test;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestCase01 {

    private WebDriver driver;

    @BeforeClass
    public void init() {
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
    }

    @Test
    public void testOpenSite() {
        driver.navigate().to("http://www.1gl.ru/");

        WebElement el = driver.findElement(By.id("user-enter"));
        el = el.findElement(By.tagName("a"));
        Assert.assertEquals(el.getText(), "Вход и регистрация");
        el.click();

        driver.findElement(By.id("wf-enter")).click();
        driver.findElement(By.id("email")).sendKeys("autoUser@mailinator.com");
        driver.findElement(By.id("password")).sendKeys("autoPass");
        driver.findElement(By.id("customer-enter")).click();

        driver.findElement(By.id("main_menu_law")).findElement(By.tagName("a")).click();

        new WebDriverWait(driver, 10).until(new ExpectedCondition<Boolean>() {
            @Nullable
            @Override
            public Boolean apply(@Nullable WebDriver webDriver) {
                WebElement button = webDriver.findElement(By.id("search-button-extended"));
                return button.isDisplayed();
            }
        });

        //WebElement searchButton = driver.findElement(By.id("search-button-extended"));
        //searchButton.click();
        //new Actions(driver).moveToElement(searchButton).click().perform();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        //js.executeScript("$('#search-extended-wrapper').show();");
        js.executeScript("$(\"#search-button-extended\").click()");

        //WebElement li1 = driver.findElement(By.xpath("li[data-id-attr=\"2\"]"));
        new WebDriverWait(driver, 10).until(new ExpectedCondition<Boolean>() {
            @Nullable
            public Boolean apply(@Nullable WebDriver webDriver) {
                WebElement typeList = webDriver.findElement(By.id("typelist"));
                WebElement regionList = webDriver.findElement(By.id("regionlist"));
                if (!typeList.isDisplayed()) {
                    return false;
                }
                if (!regionList.isDisplayed()) {
                    return false;
                }
                if (typeList.findElements(By.tagName("li")).isEmpty())
                    return false;
                if (regionList.findElements(By.tagName("li")).isEmpty())
                    return false;
                return true;
            }
        });

        List<WebElement> typeList = driver.findElement(By.id("typelist")).findElements(By.tagName("li"));
        typeList.get(3).click();
        List<WebElement> regionList = driver.findElement(By.id("regionlist")).findElements(By.tagName("li"));
        regionList.get(1).click();

        el = driver.findElement(By.id("search-text"));
        Assert.assertTrue(el.isDisplayed());
        driver.findElement(By.id("search-text")).sendKeys("налог");

        js.executeScript("$(\"#button-search-extended\").click()");

        new WebDriverWait(driver, 20).until(new ExpectedCondition<Boolean>() {
            @Nullable
            @Override
            public Boolean apply(@Nullable WebDriver webDriver) {
                WebElement resultSection = webDriver.findElement(By.id("searchResultsSection"));
                if (!resultSection.isDisplayed()) {
                    return false;
                }
                WebElement div = resultSection.findElement(By.className("widget__in"));
                if (!div.isDisplayed()) {
                    return false;
                }
                List<WebElement> divs = div.findElements(By.tagName("div"));
                if (divs.isEmpty()) {
                    return false;
                }
                return true;
            }
        });

        el = driver.findElement(By.id("ex-search-string"));
        Assert.assertTrue(el.isDisplayed());
        Assert.assertTrue(el.getText().contains("налог"));
        List<WebElement> searchExtendeds = driver.findElements(By.className("search-extended-string"));
        Assert.assertEquals(searchExtendeds.size(), 2);
        List<WebElement> spans = searchExtendeds.get(0).findElements(By.tagName("span"));
        Assert.assertEquals(spans.size(), 2);
        Assert.assertTrue(spans.get(0).isDisplayed());
        Assert.assertTrue(spans.get(1).isDisplayed());
        Assert.assertTrue(spans.get(0).getText().contains("Постановление"));
        Assert.assertTrue(spans.get(1).getText().contains("Москва"));

        Assert.assertTrue(driver.findElement(By.className("menu__title")).getText().contains("Результаты:"));
        el = driver.findElement(By.cssSelector("ul[data-filtr-name=\"pubDiv\"]"));
        Assert.assertTrue(el.isDisplayed());
        List<WebElement> menuItems = el.findElements(By.tagName("li"));

        Assert.assertEquals(menuItems.size(), 8);

        Assert.assertTrue(menuItems.get(0).getText().contains("Рекомендации"));
        Assert.assertTrue(menuItems.get(1).getText().contains("Правовая база"));
        Assert.assertTrue(menuItems.get(2).getText().contains("Судебная практика"));
        Assert.assertTrue(menuItems.get(3).getText().contains("Формы"));
        Assert.assertTrue(menuItems.get(4).getText().contains("Справочники"));
        Assert.assertTrue(menuItems.get(5).getText().contains("Журналы"));
        Assert.assertTrue(menuItems.get(6).getText().contains("Видео"));
        Assert.assertTrue(menuItems.get(7).getText().contains("Сервисы"));

        el = driver.findElement(By.id("searchResultsSection"));
        el = el.findElement(By.className("widget__in"));
        List<WebElement> resultItems = el.findElements(By.xpath("./*"));
        Assert.assertFalse(resultItems.isEmpty());

        Assert.assertTrue(resultItems.get(0).getText().contains("Постановление Правительства Москвы от 14.05.2014 № 257-ПП"));
        Assert.assertTrue(resultItems.get(0).getText().contains("О порядке определения вида фактического использования зданий (строений, сооружений) и нежилых помещений для целей налогообложения"));
        Assert.assertTrue(resultItems.get(1).getText().contains("постановлением Правительства Москвы от 18 ноября 2014 года № 682-ПП (Официальный сайт Мэра и Правительства Москвы www.mos.ru, 20.11.2014); постановлением Правительства Москвы от 17 июня 2015 года № 365-ПП (Официальный сайт Мэра..."));


        Assert.assertTrue(resultItems.get(2).getText().contains("Постановление Правительства Москвы от 30.06.2015 № 402-ПП"));
        Assert.assertTrue(resultItems.get(2).getText().contains("О Межведомственной комиссии по рассмотрению вопросов налогообложения в городе Москве"));
        Assert.assertTrue(resultItems.get(3).getText().contains("постановлением Правительства Москвы от 25 ноября 2015 года № 783-ПП (Официальный сайт Мэра и Правительства Москвы www.mos.ru, 25.11.2015) (вступил в силу с 1 января 2016 года); постановлением Правительства Москвы..."));

        Assert.assertTrue(resultItems.get(4).getText().contains("Постановление Правительства Москвы от 30.06.2015 № 401-ПП"));
        Assert.assertTrue(resultItems.get(4).getText().contains("О Порядке сбора, обработки и передачи налоговым органам сведений об объектах обложения торговым сбором в городе Москве"));
        Assert.assertTrue(resultItems.get(5).getText().contains("постановлением Правительства Москвы от 16 февраля 2016 года № 43-ПП (Официальный сайт Мэра и Правительства Москвы www.mos.ru, 17.02.2016); постановлением Правительства Москвы от 30 августа 2016 года № 535-ПП (Официальный сайт Мэра..."));

    }

    @AfterClass
    public void end() {
        driver.quit();
    }


}
