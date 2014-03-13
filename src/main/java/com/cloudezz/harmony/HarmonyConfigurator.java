package com.cloudezz.harmony;



import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Properties;

import com.cloudezz.harmony.management.paasfrontend.PaaSApplication;
import com.cloudezz.harmony.management.paasfrontend.PaaSNode;
import com.cloudezz.harmony.management.paasfrontend.haproxy.HAProxyConfiguration;
import com.cloudezz.harmony.management.ssh.SSHConnection;


/**
 * This is partly a demo class - and also contains the SSH code to push the configuration to a front
 * end server.
 */
public class HarmonyConfigurator {
  private Properties properties;
  private SSHConnection connection;
  private HAProxyConfiguration lbc;
  private boolean sudo;


  /** Path to the properties file of where to find the config */
  public HarmonyConfigurator(String parameterFile) throws Exception {
    properties = new Properties();
    FileInputStream in = new FileInputStream(parameterFile);
    properties.load(in);
    in.close();

    init();
  }

  private void init() throws IOException {
    String HAProxyDefaultsFile = properties.getProperty("HAProxy.defaultsFile");
    if (HAProxyDefaultsFile != null) {
      lbc = new HAProxyConfiguration(this.getClass().getResourceAsStream(HAProxyDefaultsFile));
    } else {
      lbc = new HAProxyConfiguration(this.getClass().getResourceAsStream("/HAProxyDefaults.conf"));
    }

  }

  public HarmonyConfigurator(Properties props) throws Exception {
    properties = props;
    init();
  }

  public void addApplication(PaaSApplication app) throws MalformedURLException {
    lbc.addApplication(app);
  }

  private SSHConnection getConnection() throws IOException {
    if (connection == null) {
      /* Create a connection instance */


      String hostName = properties.getProperty("SSH.hostname");
      String userName = properties.getProperty("SSH.username");
      String password = properties.getProperty("SSH.password");
      String port = properties.getProperty("SSH.port");
      this.sudo = Boolean.parseBoolean(properties.getProperty("CMD.sudo"));

      connection = new SSHConnection(hostName, Integer.parseInt(port), userName, password);
    }
    return connection;
  }

  private boolean execCommand(String command, String expectedResult) {
    List<String> output = null;
    try {
      output = getConnection().execCommand(command);
    } catch (IOException e) {
      return false;
    }
    if (expectedResult == null)
      return true;

    for (String result : output) {
      String r = result;
      if (result.indexOf(expectedResult) > -1)
        return true;
    }
    return false;
  }

  public boolean proxyConnectionStatus() {
    boolean ok = false;
    try {
      ok =
          execCommand(properties.getProperty("HAProxy.cmd.status"),
              properties.getProperty("HAProxy.cmd.status.result"));
      return ok;
    } catch (Exception e) {
      // do nothing
    }
    return false;
  }

  public boolean pushConfiguration() throws IOException {
    // Check HAProxy status
    boolean ok =
        execCommand(properties.getProperty("HAProxy.cmd.status"),
            properties.getProperty("HAProxy.cmd.status.result"));
    if (!ok) {
      System.err.println("haproxy not running, restarting");
      ok =
          execCommand(properties.getProperty("HAProxy.cmd.start"),
              properties.getProperty("HAProxy.cmd.start.result"));
      if (!ok) {
        System.err.println("Cannot start HAProxy");
        return false;
      }
    }

    if (lbc != null) {
      String confFile = properties.getProperty("HAProxy.conf.file");
      String confDir = properties.getProperty("HAProxy.conf.dir");
      String tmpDir = properties.getProperty("HAProxy.temp.dir");
      // cp current config to old
      String cpCmd = "cp ";
      if (sudo)
        cpCmd = "sudo " + cpCmd;

      String mvCmd = "mv ";
      if (sudo)
        mvCmd = "sudo " + mvCmd;

      execCommand(cpCmd + confDir + confFile + " " + confDir + confFile + ".old", null);

      System.out.println("Upload new configuration to " + confDir + confFile);
      byte[] buf = lbc.toString().getBytes();
      getConnection().upload(buf, confFile, tmpDir);

      // mv new config to current
      execCommand(mvCmd + tmpDir + confFile + " " + confDir + confFile, null);

      long start = System.currentTimeMillis();
      boolean failed =
          execCommand(properties.getProperty("HAProxy.cmd.reload"),
              properties.getProperty("HAProxy.cmd.reload.result"));
      long end = System.currentTimeMillis();
      System.out.println("HAProxy reload time:" + (end - start) + " ms");
      if (failed) {
        System.err.println("Cannot reload new haproxy configuration");
        System.err.println("Reloading old haproxy configuration");

        // revert old config
        execCommand(cpCmd + confDir + confFile + ".old " + confDir + confFile, null);
        execCommand(properties.getProperty("HAProxy.cmd.reload"),
            properties.getProperty("HAProxy.cmd.reload.result"));
        return false;
      }
      return true;

    } else {
      System.err.println("Configuration not generated");
      return false;
    }

  }

  public void close() {
    if (connection != null) {
      connection.close();
      connection = null;
    }
  }

  public void displayConfiguration() {
    System.out.println(lbc);
  }

  public static void main(String[] args) {
    try {
      HarmonyConfigurator lbc = new HarmonyConfigurator(args[0]);

      for (int i = 1; i < 3; i++) {
        PaaSApplication app1 =
            new PaaSApplication("company" + i + ".highstor.com/company" + i + "/",
                PaaSApplication.COOKIE | PaaSApplication.SOURCE_IP, PaaSApplication.HTTP
                    | PaaSApplication.HTTPS);
        app1.addErrorRedirect("503", "http://www.google.com/503");
        app1.addErrorRedirect("504", "http://www.google.com/504");
        app1.addOption("reqadd\tFRONT_END_COMPANY:\\ company" + i);
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
    } catch (Exception e) {
      e.printStackTrace(System.err);
      System.exit(2);
    }
  }
}
