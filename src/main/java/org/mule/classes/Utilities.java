package org.mule.classes;

import org.apache.commons.exec.ExecuteException;

import java.io.File;
import java.io.IOException;


import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.raml.model.Raml;
import org.raml.parser.visitor.RamlDocumentBuilder;

/**
 * Created by natalia.garcia on 6/3/14.
 */
public class Utilities {

    public static void executeCommand(String command) throws ExecuteException, IOException {
        System.out.println("Running: " + command);
        CommandLine cmdLine = CommandLine.parse(command);
        DefaultExecutor executor = new DefaultExecutor();
        int exitValue = executor.execute(cmdLine);
        System.out.println("Done. Exit Value:" + exitValue);
    }

    public static void createAndDeployProject(String shellScriptFolder, String mule_home, String testFolder, String pathToRaml, String apikitVersion, String muleStartCommand) throws IOException {
        Utilities.executeCommand("sh " + shellScriptFolder + "/mavenAutomation.sh -p " + mule_home + " -a " + testFolder + " -r " + pathToRaml + " -v " + apikitVersion + " -s " + muleStartCommand);
    }

    public static Raml getRamlFromFile() {

        Raml raml = new RamlDocumentBuilder().build("interop.raml");
        return raml;
    }

    public static boolean verifyAppHasBeenDeployed(File app)
    {

        Timeout timeout = new Timeout(80000);

        while (true)
        {
            if (app.exists())
            {
                return true;
            }
            else if (timeout.hasTimedOut())
            {
                return false;
            }
            else
            {
                waitFor(100);
            }
        }
    }

    private static void waitFor(long duration)
    {
        try
        {
            Thread.sleep(duration);
        }
        catch (InterruptedException e)
        {
            throw new IllegalStateException("unexpected interrupt", e);
        }
    }

    public static String getBody(String uri) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(uri);

            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            return httpclient.execute(httpget, responseHandler);

        } finally {
            httpclient.close();
        }
    }

    public static boolean verifyStatusCode(String url) throws IOException {

        Timeout timeout = new Timeout(80000);

        CloseableHttpClient httpClient = null;
        HttpGet httpget = null;
        CloseableHttpResponse response = null;
        Integer statusCode =0;

        while (true)
        {
            try
            {
                httpClient = HttpClients.createDefault();
                httpget = new HttpGet(url);
                response = httpClient.execute(httpget);
                statusCode = response.getStatusLine().getStatusCode();
            }
            catch (Exception exception)
            {
                System.out.println(exception.toString());
            }
            finally {

                if (statusCode == 200) {
                    return true;
                } else if (timeout.hasTimedOut()) {
                    return false;
                } else {
                    waitFor(1000);
                }
            }
        }

    }
}
