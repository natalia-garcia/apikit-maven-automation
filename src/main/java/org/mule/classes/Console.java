package org.mule.classes;

import com.google.common.base.Function;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by natalia.garcia on 6/4/14.
 */
public class Console {

    private static RemoteWebDriver driver;

    public Console() throws MalformedURLException {
        setDriver();
        driver.navigate().refresh();
    }

    public void quit(){
        driver.quit();
    }

    private void setDriver() throws MalformedURLException {

        DesiredCapabilities capabillities = DesiredCapabilities.chrome();
        driver = new RemoteWebDriver(new URL("http://localhost:9515"), capabillities);
        driver.get("http://localhost:8081/api/console/");
    }

    public RemoteWebDriver getDriver(){
        return driver;
    }

    public WebElement getTitle(){

        return driver.findElementById("raml-console-api-title");
    }

    public WebElement fluentWait(final By locator) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(30, TimeUnit.SECONDS)
                .pollingEvery(5, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);

        WebElement foo = wait.until(
                new Function<WebDriver, WebElement>() {
                    public WebElement apply(WebDriver driver) {
                        return driver.findElement(locator);
                    }
                }
        );
        return foo;
    }

    public WebElement findElementByCssSelector(final String cssSelector) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 50);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(cssSelector)));
        WebElement webElement = driver.findElementByCssSelector(cssSelector);

        return webElement;
    }

    public WebElement getDocumentationTitle(){
        return findElementByCssSelector("h2.ng-binding");
    }

    public WebElement itemsGet(){
        driver.navigate().refresh();
        return findElementByCssSelector("div.resource-placeholder:nth-of-type(1) li.method-name.ng-scope.ng-binding.get");
    }

    public WebElement itemGet(){
        driver.navigate().refresh();
        return findElementByCssSelector("div.resource-placeholder:nth-of-type(2) li.method-name.ng-scope.ng-binding.get");
    }

    public WebElement tryIt() throws InterruptedException {
        return findElementByCssSelector("ul.method-nav-group li:nth-of-type(4) a.ng-binding");
    }

    public WebElement getResponseCode() {
        return findElementByCssSelector("div.status code.response-value.ng-binding");
    }

    public WebElement popupCloseButton(){
        return findElementByCssSelector("i.icon-remove.collapse");
    }

    public WebElement getButton() {
        return findElementByCssSelector("button.ng-binding.btn-get");
    }

    public void goToDocumentation(){
        driver.navigate().refresh();
        WebElement documentation = findElementByCssSelector("a.btn.ng-scope");
        if (documentation.getText().toLowerCase().contains("documentation"))
            documentation.click();
    }

    public void goToApi(){
        driver.navigate().refresh();
        WebElement documentation = findElementByCssSelector("a.btn.ng-scope");
        if (documentation.getText().toLowerCase().contains("api reference"))
            documentation.click();
    }

    public boolean inApi(){

        if (driver.findElements(By.cssSelector("i.icon-remove.collapse")).size() > 0) {
            WebElement popupCloseButton = popupCloseButton();
            popupCloseButton.click();
        }

        WebElement documentation = findElementByCssSelector("a.btn.ng-scope");

        if (documentation.getText().toLowerCase().contains("documentation"))
            return true;
        else
            return false;
    }

}
