package org.mule.test;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.NoSuchElementException;
import org.raml.emitter.RamlEmitter;
import org.testng.*;
import org.openqa.selenium.WebElement;
import org.raml.model.Raml;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.mule.classes.*;

import static com.jayway.restassured.RestAssured.given;

/**
 * ≈ * Created by natalia.garcia on 5/23/14.
 */


public class MavenAutomationTest {

    private String mule_home = getClass().getResource("/distributions/someFile.txt").getPath().replace("someFile.txt","") + System.getProperty("distribution.folder.final.name") + "-" + System.getProperty("mule.version");
    private String muleStartCommand = System.getProperty("mule.start.command");
    private String applicationName = System.getProperty("app.name");
    private Console console;
    private Raml raml;

    @BeforeClass
    public void deployAndRunMule() throws IOException, InterruptedException {

        raml = Utilities.getRamlFromFile();
        console = new Console();
        console.loadRaml(raml);
        Utilities.updateConfig(mule_home, applicationName);
        Utilities.executeCommand("touch " + mule_home + "/apps/" + applicationName + "mule-config.xml");
        Thread.sleep(20000);
        System.out.println("Config after updates is: \n" + Utilities.getStringFromFile(mule_home + "/apps/" + applicationName + "mule-config.xml"));
    }

    @Test
    public void testTitle() throws InterruptedException {
        WebElement title = console.getTitle();
        assert title.getText().equals(raml.getTitle());
    }

    @Test
    public void testDocumentationTitle() {
        WebElement documentationTitle = console.getDocumentationTitle();
        assert documentationTitle.getText().equals("Interop documentation");
    }

    @Test
    public void testGetItemsResponseCode() throws Exception {

        String relativeUri = "/items";

        WebElement itemsGetTab = console.itemsGetTab();
        itemsGetTab.click();

        WebElement itemsGetTryIt;
        Boolean present;
        try {
            itemsGetTryIt  = console.tryIt();
            itemsGetTryIt.click();
            present = true;
        } catch (NoSuchElementException e) {
            present = false;
        }

        WebElement itemsGetButton = console.itemsGetButton();
        itemsGetButton.click();

//        String responseCode = console.getResponseCode().getText();
//        System.out.println("ResponseCode: " + responseCode);
//
//        String urlItems = raml.getBaseUri() + relativeUri;
//        System.out.println("HttpClient url: " + urlItems);
//
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        HttpGet httpget = new HttpGet(urlItems);
//        CloseableHttpResponse response = httpClient.execute(httpget);
//
//        Integer statusCode = response.getStatusLine().getStatusCode();
//
//        WebElement closePopup = console.popupCloseButton();
//        closePopup.click();
//
//        Assert.assertEquals(responseCode, Integer.toString(statusCode));
    }
//
//    @Test
//    public void testGetItemResponseCode() throws Exception {
//
//        String relativeUri = "/items/1";
//
//        WebElement itemGet = console.itemGet();
//        Thread.sleep(1000);
//        itemGet.click();
//
//        WebElement itemGetTryIt = console.tryIt();
//        itemGetTryIt.click();
//
//        WebElement itemId = console.getItemId();
//        itemId.sendKeys("1");
//
//        WebElement getButton = console.getButton();
//        getButton.click();
//
//        String responseCode = console.getResponseCode().getText();
//
//        String urlItem = raml.getBaseUri() + relativeUri;
//
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        HttpGet httpget = new HttpGet(urlItem);
//        CloseableHttpResponse response = httpClient.execute(httpget);
//
//        Integer statusCode = response.getStatusLine().getStatusCode();
//
//        WebElement closePopup = console.popupCloseButton();
//        closePopup.click();
//
//        Assert.assertEquals(responseCode, Integer.toString(statusCode));
//    }
//
//    @Test
//    public void testGetItemBody() throws Exception {
//
//        String urlItem = raml.getBaseUri() + "/items/1";
//
//        WebElement itemGet = console.itemGet();
//        Thread.sleep(1000);
//        itemGet.click();
//
//        WebElement itemGetTryIt = console.tryIt();
//        itemGetTryIt.click();
//
//        WebElement itemId = console.getItemId();
//        itemId.sendKeys("1");
//
//        WebElement getButton = console.getButton();
//        getButton.click();
//
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        HttpGet httpget = new HttpGet(urlItem);
//        CloseableHttpResponse response = httpClient.execute(httpget);
//
//        WebElement closePopup = console.popupCloseButton();
//        closePopup.click();
//
//        Assert.assertEquals(Utilities.getBody(urlItem), EntityUtils.toString(response.getEntity()));
//    }

    @AfterClass
    public void stopMule() throws IOException {
//        console.quit();
        Utilities.executeCommand("sh " + getClass().getResource("/stopMule.sh").getPath() + " -p " + mule_home + " -s " + muleStartCommand);
    }

}