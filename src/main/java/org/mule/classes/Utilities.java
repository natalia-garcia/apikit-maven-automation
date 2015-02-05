package org.mule.classes;

import org.apache.commons.exec.ExecuteException;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

    public static String getStringFromFile(String path) throws IOException {

        File file = new File(path);
        BufferedReader reader = new BufferedReader( new FileReader(file));
        String         line = null;
        StringBuilder  stringBuilder = new StringBuilder();
        String         ls = System.getProperty("line.separator");

        while( ( line = reader.readLine() ) != null ) {
            stringBuilder.append( line );
            stringBuilder.append( ls );
        }

        return stringBuilder.toString();
    }

    public static void updateConfig(String mule_home, String applicationName) throws IOException {

        String configPath = mule_home + "/apps/" + applicationName + "/mule-config.xml";
        String originalConfig= getStringFromFile(configPath);

        String updatedFlows = "<cors:config name=\"Cors_Configuration\" >\n" +
                "        <cors:origins>\n" +
                "            <cors:origin url=\"http://ec2-54-69-7-25.us-west-2.compute.amazonaws.com:9000\">\n" +
                "                <cors:methods>\n" +
                "                    <cors:method>POST</cors:method>\n" +
                "                    <cors:method>PUT</cors:method>\n" +
                "                    <cors:method>DELETE</cors:method>\n" +
                "                    <cors:method>GET</cors:method>\n" +
                "                </cors:methods>\n" +
                "                <cors:headers>\n" +
                "                    <cors:header>content-type</cors:header>\n" +
                "                </cors:headers>\n" +
                "            </cors:origin>\n" +
                "        </cors:origins>\n" +
                "    </cors:config>" +
                "<flow name=\"api-main\">\n" +
                "        <http:inbound-endpoint exchange-pattern=\"request-response\" host=\"localhost\" path=\"api\" port=\"9091\" />\n" +
                "        <cors:validate config-ref=\"Cors_Configuration\" publicResource=\"false\" acceptsCredentials=\"false\" />\n" +
                "        <apikit:router config-ref=\"apiConfig\" />\n" +
                "        <exception-strategy ref=\"apiKitGlobalExceptionMapping\" />\n" +
                "    </flow>\n" +
                "    <flow name=\"api-console\">\n" +
                "        <http:inbound-endpoint exchange-pattern=\"request-response\" host=\"localhost\" port=\"9090\" path=\"console\" />\n" +
                "        <apikit:console config-ref=\"apiConfig\"  />\n" +
                "    </flow>\n" +
                "    <flow name=\"put:/items/{itemId}:application/json:apiConfig\">\n";

        originalConfig = originalConfig.replaceAll("(?s)<flow name=\"api-main\"*(.*)<flow name=\"put:/items/\\{itemId\\}:application/json:apiConfig\">", updatedFlows);

        originalConfig = originalConfig.replace("<mule ", "<mule xmlns:cors=\"http://www.mulesoft.org/schema/mule/cors\" \n");
        originalConfig = originalConfig.replace("xsi:schemaLocation=\"", "xsi:schemaLocation=\"http://www.mulesoft.org/schema/mule/cors http://www.mulesoft.org/schema/mule/cors/current/mule-cors.xsd \n");
        writeStringToFile(configPath, originalConfig);

    }

    public static void writeStringToFile(String path, String originalConfig) throws IOException {

        File file = new File(path);
        FileWriter fileWriter = new FileWriter(file, false);

        fileWriter.write(originalConfig);
        fileWriter.close();
    }
}
