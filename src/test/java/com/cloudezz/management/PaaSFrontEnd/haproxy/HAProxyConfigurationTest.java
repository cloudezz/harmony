package com.cloudezz.management.PaaSFrontEnd.haproxy;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.cloudezz.management.paasfrontend.PaaSApplication;
import com.cloudezz.management.paasfrontend.PaaSNode;
import com.cloudezz.management.paasfrontend.haproxy.HAProxyConfiguration;

/**
 * HAProxyConfiguration Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>10/06/2010</pre>
 */
public class HAProxyConfigurationTest extends TestCase {
    public HAProxyConfigurationTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }


    /**
     * Method: toString()
     */
    public void testToString() throws Exception {

        HAProxyConfiguration lbc;

        lbc = new HAProxyConfiguration(this.getClass().getResourceAsStream("/HAProxyDefaults.conf"));

        PaaSApplication app1 = new PaaSApplication("company1.domain.com/company1/", PaaSApplication.COOKIE, PaaSApplication.HTTP | PaaSApplication.HTTPS);
        app1.addErrorRedirect("503", "http://someredirect/503.html");
        app1.addErrorRedirect("504", "http://someredirect/504.html");
        app1.addOption("reqadd\tFRONT_END_COMPANY:\\ company1");
        PaaSNode node1 = new PaaSNode("node1", "10.205.11.191:8080", null);
        node1.setHealthCheck(true, true, "inter 2000");
        app1.addNode(node1);
        PaaSNode node2 = new PaaSNode("node2", "10.205.11.191:9090", null);
        node1.setHealthCheck(true, true, "inter 2000");
        app1.addNode(node2);
        lbc.addApplication(app1);

        PaaSApplication app2 = new PaaSApplication("company2.domain.com/company2/", PaaSApplication.NON_STICKY, PaaSApplication.HTTP);
        app2.addOption("reqadd\tFRONT_END_COMPANY:\\ company2");
        node1 = new PaaSNode("node1", "127.0.0.1:8080", null);
        node1.setHealthCheck(true, true, null);
        app2.addNode(node1);
        lbc.addApplication(app2);

        PaaSApplication app3 = new PaaSApplication("company3.domain.com/company3/", PaaSApplication.COOKIE | PaaSApplication.SOURCE_IP, PaaSApplication.HTTPS);
        app3.addOption("reqadd\tFRONT_END_COMPANY:\\ company3");
        node1 = new PaaSNode("node1", "127.0.0.1:8080", null);
        node1.setHealthCheck(true, false, null);
        app3.addNode(node1);
        lbc.addApplication(app3);

        System.out.println(lbc);
    }

    public static Test suite() {
        return new TestSuite(HAProxyConfigurationTest.class);
    }
}
