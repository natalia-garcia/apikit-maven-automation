package org.mule.test;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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
    private Console console;
    private Raml raml;

    @BeforeClass
    public void deployAndRunMule() throws IOException, InterruptedException {

        raml = Utilities.getRamlFromFile();
        console = new Console();
        console.loadRaml(raml);

    }
//
//    @BeforeMethod
//    public void refreshPage() throws InterruptedException {
//        console.refreshPage();
//        console.loadRaml(raml);
//    }

    @Test
    public void testTitle() throws InterruptedException {

        WebElement title = console.getTitle();
        assert title.getText().equals(raml.getTitle());
    }

    @Test
    public void testDocumentationTitle() {

//        if(console.inApi())
//            console.goToDocumentation();

        WebElement documentationTitle = console.getDocumentationTitle();

        System.out.println("Documentation Title: " + documentationTitle.getText());
        assert documentationTitle.getText().equals("Interop documentation");

//        console.goToApi();
    }

    @Test
    public void testGetItemsResponseCode() throws Exception {

        String relativeUri = "/items";

        WebElement itemsGet = console.itemsGet();
        itemsGet.click();
//
//        WebElement itemsGetTryIt = console.tryIt();
//        itemsGetTryIt.click();

        WebElement getButton = console.getButton();
        getButton.click();

        String responseCode = console.getResponseCode().getText();
//
//        String urlItems = raml.getBaseUri() + relativeUri;
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
        console.quit();
        Utilities.executeCommand("sh " + getClass().getResource("/stopMule.sh").getPath() + " -p " + mule_home + " -s " + muleStartCommand);
    }

}