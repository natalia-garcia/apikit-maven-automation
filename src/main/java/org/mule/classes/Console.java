package org.mule.classes;

import com.google.common.base.Function;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.raml.emitter.RamlEmitter;
import org.raml.model.Raml;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by natalia.garcia on 6/4/14.
 */
public class Console {

    private static RemoteWebDriver driver;

    public Console() throws MalformedURLException, InterruptedException {
        setDriver();
        driver.navigate().refresh();
    }

    public void quit(){
        driver.quit();
    }

    private void setDriver() throws MalformedURLException {
        DesiredCapabilities capabillities = DesiredCapabilities.chrome();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("disable-web-security");
        capabillities.setCapability("args", options);
        driver = new RemoteWebDriver(new URL("http://localhost:9515"), capabillities);
//        driver.get("http://localhost:8081/api/console/");
        driver.get("http://ec2-54-69-7-25.us-west-2.compute.amazonaws.com:9000/");
    }

    public WebElement getRamlTextArea() throws InterruptedException {
//        return driver.findElementById("raml");
//        return driver.findElementByCssSelector("raml-console-initializer-input-container");
        return driver.findElementByCssSelector("div.CodeMirror-code pre");
    }

    public WebElement getLoadRamlButton() throws InterruptedException {
        return driver.findElementById("loadRaml");
    }

    public RemoteWebDriver getDriver(){
        return driver;
    }

    public WebElement getTitle() throws InterruptedException {
//        return driver.findElementById("raml-console-api-title");
        return driver.findElementByCssSelector("h1.raml-console-title.ng-binding.ng-scope");
    }

    public WebElement findElementByCssSelector(final String cssSelector) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 50);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));
        WebElement webElement = driver.findElementByCssSelector(cssSelector);

        return webElement;
    }

    public WebElement getDocumentationTitle(){
//        return findElementByCssSelector("h2.ng-binding");
        return findElementByCssSelector("li.raml-console-resource-list-item.raml-console-documentation.ng-scope");
    }

    public WebElement itemsGet(){

//        return findElementByCssSelector("div.resource-placeholder:nth-of-type(1) li.method-name.ng-scope.ng-binding.get");
        return findElementByCssSelector("#items > header > div.raml-console-tab-list > div:nth-child(1) > span");
    }

    public WebElement itemGet(){
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
//        return findElementByCssSelector("button.ng-binding.btn-get");
        return findElementByCssSelector("#items div div form div div div section:nth-child(4) div div button.raml-console-sidebar-action.raml-console-sidebar-action-get");
    }

    public void goToDocumentation(){
        WebElement documentation = findElementByCssSelector("a.btn.ng-scope");
        if (documentation.getText().toLowerCase().contains("documentation"))
            documentation.click();
    }

    public void goToApi(){
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

    public void refreshPage() {
        driver.navigate().refresh();
    }

    public WebElement getItemId() {
        return findElementByCssSelector("input.ng-pristine.ng-valid");
    }

    public void parseRaml(Raml raml) throws InterruptedException {

        RamlEmitter emitter = new RamlEmitter();
        String dumpFromRaml = emitter.dump(raml);

        String[] ramlArray = dumpFromRaml.split("\\n");

        for (int i = 0; i < ramlArray.length; i++) {
            driver.executeScript("jQuery('.CodeMirror')[0].CodeMirror.setLine(" + i + ", '" + ramlArray[i] + " \\n')");
        }

    }

    public void loadRaml(Raml raml) throws InterruptedException {
        parseRaml(raml);
        WebElement loadRamlButton = getLoadRamlButton();
        loadRamlButton.click();
    }
}
