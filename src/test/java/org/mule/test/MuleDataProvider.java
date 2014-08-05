package org.mule.test;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by natalia.garcia on 7/18/14.
 */
public class MuleDataProvider {

    @DataProvider(parallel = false)
    public static Iterator<Object[]> muleDataProvider (ITestContext context) {
        List<Object[]> dataToBeReturned = new ArrayList<Object[]>();

//         dataToBeReturned.add(new String[]{"/Users/natalia.garcia/Standalone/api-gateway-standalone-0.9.1-SNAPSHOT", "gateway"});
//         dataToBeReturned.add(new String[]{"/Users/natalia.garcia/Standalone/mule-enterprise-standalone-3.5.0", "mule"});
//        dataToBeReturned.add(new String[]{"/Users/natalia.garcia/Standalone/mule-enterprise-standalone-3.4.2", "mule"});
        dataToBeReturned.add(new String[]{"/Users/natalia.garcia/Standalone/mule-enterprise-standalone-3.6.0-M1-SNAPSHOT", "mule"});
        return dataToBeReturned.iterator();

    }
}
