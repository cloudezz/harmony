import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.cloudezz.management.paasfrontend.PaaSApplication;
import com.cloudezz.management.paasfrontend.PaaSNode;

/**
 * HAProxyConfigurator Tester.
 *
 * @author <Authors name>
 * @since <pre>10/06/2010</pre>
 * @version 1.0
 */
public class HAProxyConfiguratorTest extends TestCase {
    public HAProxyConfiguratorTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }


    /**
     *
     *
     */
    public void testMain() throws Exception {
        String configurationFile = "<path_to>/HAProxyConfigurator.properties";

        HAProxyConfigurator lbc = new HAProxyConfigurator(configurationFile);

        for (int i=1; i<3; i++) {
            PaaSApplication app1 = new PaaSApplication("company"+i+".domain.com/company"+i+"/", PaaSApplication.COOKIE | PaaSApplication.SOURCE_IP, PaaSApplication.HTTP | PaaSApplication.HTTPS);
            app1.addErrorRedirect("503", "http://redirect/503");
            app1.addErrorRedirect("504", "http://redirect/504");
            app1.addOption("reqadd\tFRONT_END_COMPANY:\\ company"+ i);
            PaaSNode node1 = new PaaSNode("node1", "10.205.11.191:8080", null);
            node1.setHealthCheck(true, true, null);
            app1.addNode(node1);
            PaaSNode node2 = new PaaSNode("node2", "10.205.11.191:9090", null);
            node2.setHealthCheck(true, true, null);
            app1.addNode(node2);
            lbc.addApplication(app1);
        }

        lbc.displayConfiguration();

        lbc.pushConfiguration();
        lbc.close();
    }

    public void testMichael() throws Exception {
        Properties properties = new Properties();
        properties.load(this.getClass().getResourceAsStream("/HAProxyConfigurator.properties"));
        properties.setProperty("SSH.hostname", "haproxy.cbruntime.us");
        properties.setProperty("SSH.username", "root");
        properties.setProperty("SSH.keyfile", "/Users/michaelneale/.ssh/mic-dev.pem");


        HAProxyConfigurator lbc = new HAProxyConfigurator(properties);

        //for (int i=1; i<3; i++) {
            PaaSApplication app1 = new PaaSApplication("something.paas.cbruntime.us", "/businessGov/pad/businessgov.html", PaaSApplication.COOKIE | PaaSApplication.SOURCE_IP, PaaSApplication.HTTP | PaaSApplication.HTTPS);
            app1.addErrorRedirect("503", "http://www.cloudbees.com");
            app1.addErrorRedirect("504", "http://www.cloudbees.com");
            //app1.addOption("reqadd\tFRONT_END_COMPANY:\\ company"+ i);
            PaaSNode node1 = new PaaSNode("node1", "www.unsw.edu.au:80", null);
            node1.setHealthCheck(true, true, null);
            app1.addNode(node1);
//            PaaSNode node2 = new PaaSNode("node2", "10.205.11.191:9090", null);
//            node2.setHealthCheck(true, true, null);
//            app1.addNode(node2);
            lbc.addApplication(app1);
        //}

        lbc.displayConfiguration();

        assertTrue(lbc.pushConfiguration());
        lbc.close();
    }



    public static Test suite() {
        return new TestSuite(HAProxyConfiguratorTest.class);
    }
}
