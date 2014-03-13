package com.cloudezz.harmony.management.paasfrontend.haproxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.cloudezz.harmony.management.paasfrontend.LoadBalancer;
import com.cloudezz.harmony.management.paasfrontend.LoadBalancerFactory;
import com.cloudezz.harmony.management.paasfrontend.Node;
import com.cloudezz.harmony.management.paasfrontend.PaaSApplication;
import com.cloudezz.harmony.management.paasfrontend.PaaSNode;
import com.cloudezz.harmony.management.paasfrontend.Proxy;
import com.cloudezz.harmony.management.paasfrontend.Settings;
import com.cloudezz.harmony.management.paasfrontend.haproxy.impl.HAProxyRegistry;

/**
 * Class defining the HAProxy load balancer configuration.<br>
 * The default configuration settings and global parameters are defined in a configuration file (see
 * setDefaults)<br>
 * <br>
 * Currently 2 front ends/proxy are supported (HTTP and HTTPS). An application can support either or
 * both.<br>
 * <br>
 * Application stickyness is supported via client source IP and HTTP cookie. An application can
 * support either or both.<br>
 * In case where both are defined, cookie is the preferred mechanism and IP source the fall back
 * mechanism if cookies are not supported by the client.<br>
 * - client IP tracking: Default is 1000 entries with 30 minutes expiration. This can be changed via
 * PaaSApplication.setIPStickynessOption<br>
 * - cookie tracking: The cookie name is SERVERID<br>
 * <br>
 * Server node error can be redirected to a URL. The redirect uses HTTP status 303 to handle non GET
 * requests<br>
 * <br>
 * The load balancing algorithm is roundrobin<br>
 * <br>
 * Server node health check are by default performed by establishing a TCP connection. A full HTTP
 * request can be used see PaaSApplication.setNodeHealthCheckOption<br>
 * <br>
 * URL request rewrite is added if URL path is defined: for example request to
 * "company.domain.com/request?param=x" is rewritten to
 * "company.domain.com/NODEPATH/request?param=x" where NODEPATH is the url path defined in the
 * PaasNode.url<br>
 * <br>
 * Frontend/proxy rules for the selection of the appropriate backend is based on matching the
 * PaaSApplication name to the HTTP request source domain URL<br>
 * 
 */
public class HAProxyConfiguration {
  
  private static final String GLOBAL_SETTINGS = "global";
  private static final String DEFAULT_SETTINGS = "defaults";
  private static final String DEFAULT_FRONTEND = "frontend";
  private static final String DEFAULT_BACKEND = "backend";
  private static final String LISTEN_FRONTEND = "listen";
  private static final String HTTP_FRONTEND = "http";
  private static final String HTTPS_FRONTEND = "https";

  private LoadBalancer lb;
  private Map<String, Proxy> frontEnds = new HashMap<String, Proxy>();
  private Map<String, String> nodes = new HashMap<String, String>();

  private void setDefaults(InputStream confFile) throws IOException {
    InputStreamReader fr = new InputStreamReader(confFile);
    BufferedReader br = new BufferedReader(fr);
    String s;
    Settings settings = null;
    while ((s = br.readLine()) != null) {
      s = s.trim();
      if (s.equalsIgnoreCase(GLOBAL_SETTINGS)) {
        settings = lb.createSettings(GLOBAL_SETTINGS);
      } else if (s.equalsIgnoreCase(DEFAULT_SETTINGS)) {
        settings = lb.createSettings(DEFAULT_SETTINGS);
      } else if (s.equalsIgnoreCase("listen")) {
        settings = lb.createSettings("listen");
      } else if (s.startsWith(DEFAULT_FRONTEND)) {
        StringTokenizer st = new StringTokenizer(s, " \t");
        int tokenCount = st.countTokens();
        if (tokenCount < 2)
          throw new IllegalArgumentException("Illegal frontend definition: frontend name url");
        Proxy frontEnd = null;
        st.nextToken();
        String name = st.nextToken();
        String connection = "";
        if (tokenCount > 2) {
          connection = st.nextToken();
        }
        frontEnd = addFrontEnd(name, connection);
        settings = frontEnd.createSettings();
      } else if (s.startsWith(LISTEN_FRONTEND)) {
        StringTokenizer st = new StringTokenizer(s, " \t");
        if (st.countTokens() < 3)
          throw new IllegalArgumentException("Illegal listen definition: listen name url");

        st.nextToken();
        String name = st.nextToken();
        String connection = st.nextToken();
        Proxy listen = addListen(name, connection);
        settings = listen.createSettings();
      } else if (s.startsWith(DEFAULT_BACKEND)) {
        StringTokenizer st = new StringTokenizer(s, " \t");
        if (st.countTokens() < 2)
          throw new IllegalArgumentException("Illegal backend definition: backend name");

        st.nextToken();
        String name = st.nextToken();
        Proxy backEnd = addBackEnd(name);
        settings = backEnd.createSettings();
      } else {
        if (settings != null && s.length() > 0) {
          settings.addOption(s);
        }
      }
    }
    fr.close();
  }

  private Proxy addFrontEnd(String name, String connection) {
    Proxy frontEnd = lb.createProxy(name, connection);
    frontEnd.setTag("frontend");
    frontEnds.put(name, frontEnd);
    return frontEnd;
  }

  private Proxy addListen(String name, String connection) {
    Proxy listen = lb.createProxy(name, connection);
    listen.setTag("listen");
    return listen;
  }

  private Proxy addBackEnd(String name) {
    Proxy backEnd = lb.createProxy(name, "");
    backEnd.setTag("backend");
    return backEnd;
  }

  /**
   * The load balancer default configuration parameters and defined in a specified file. The load
   * balancer configuration type is HAProxy.
   * 
   * @param defaultConfigurationFile The default configuration file name
   * @throws IOException
   */
  public HAProxyConfiguration(InputStream defaultConfigurationFile) throws IOException {
    lb =
        LoadBalancerFactory.getLoadBalancer(HAProxyRegistry.TYPE, "HAProxy configuration: "
            + new Date());

    setDefaults(defaultConfigurationFile);
  }

  /**
   * This method set an application load balancing configuration. Two front ends are currently
   * supported (HTTP and HTTPS) An application can support either or both.
   * 
   * @param app The application definition object
   */
  public void addApplication(PaaSApplication app) throws MalformedURLException {
    String appName = app.getDomain();

    // Add frontend rules for application
    if ((app.getProtocol() & PaaSApplication.HTTP) == PaaSApplication.HTTP) {
      Proxy frontEnd = frontEnds.get(HTTP_FRONTEND);
      setFrontEndRule(frontEnd, appName);
    }
    if ((app.getProtocol() & PaaSApplication.HTTPS) == PaaSApplication.HTTPS) {
      Proxy frontEnd = frontEnds.get(HTTPS_FRONTEND);
      setFrontEndRule(frontEnd, appName);
    }

    // Add backend options
    Proxy backEnd = addBackEnd(appName);
    Settings settings = backEnd.createSettings();

    // Add handling of sticky sessions
    // Add client IP tracking. Default is 1000 entries with 30 minutes expiration. This can be
    // changed
    // via PaaSApplication.setIPStickynessOption
    if ((app.getStickynessMode() & PaaSApplication.SOURCE_IP) == PaaSApplication.SOURCE_IP) {
      if (app.getIPStickynessOption() != null) {
        settings.addOption("stick-table type ip " + app.getIPStickynessOption());
      } else {
        settings.addOption("stick-table type ip size 1k expire 30m");
      }
      settings.addOption("stick on src");
    }
    // Add cookie based session stickyness.
    // Current strategy is transparent to server meaning that the cookie is inserted by the proxy
    // and is
    // not visible to the backend
    if ((app.getStickynessMode() & PaaSApplication.COOKIE) == PaaSApplication.COOKIE) {
      settings.addOption("cookie SERVERID insert indirect nocache");
    }

    // load balancing algorithm is roundrobin
    if (app.getNodes().size() > 1) {
      settings.addOption("balance roundrobin");
    }

    // server error redirects
    if (app.hasErrorRedirects()) {
      for (Map.Entry<String, String> entry : app.getErrorRedirects().entrySet()) {
        settings.addOption("errorloc303 " + entry.getKey() + " " + entry.getValue());
      }
    }

    // server health check option
    if (app.getNodeHealthCheckOption() != null) {
      settings.addOption("option httpchk " + app.getNodeHealthCheckOption());
    }

    // Check if application URL defines a path
    String nodePath = app.getPath();
    // Add URL request rewrite if URL path is defined: company.domain.com/request?param=x to
    // company.domain.com/NODEPATH/request?param=x where NODEPATH is the url path defined in the
    // PaasNode.url
    if (nodePath != null) {
      // reqirep ^([^\ \t])(.*)[\ \t]/(.*) \1\2\ /default/\3
      settings.addOption("reqirep ^([^\\ \\t])(.*)[\\ \\t]/(.*) \\1\\2\\ " + nodePath + "\\3");

    }

    // Add misc options
    for (String option : app.getOptions()) {
      settings.addOption(option);
    }

    // Add nodes to backend
    for (PaaSNode pnode : app.getNodes()) {
      String nodeAddress = pnode.getConnection();
      Node node = backEnd.createNode(pnode.getName(), nodeAddress);
      String option = "";

      // Add cookie handling for session persistence
      // Current strategy is transparent to server
      if ((app.getStickynessMode() & PaaSApplication.COOKIE) == PaaSApplication.COOKIE)
        option = "cookie " + pnode.getName() + " " + option;

      // Add misc server options
      for (String opt : pnode.getOptions()) {
        option += " " + opt;
      }

      // Add server weight
      if (pnode.getWeight() != null)
        option += " weight " + pnode.getWeight();

      // Add health check
      if (pnode.hasHealthCheck()) {
        if (pnode.hasTrackHealthCheck()) {
          // Get first server
          String serverName = nodes.get(nodeAddress);
          if (serverName != null) {
            option += " track " + serverName;
          } else {
            option += " check";
            String parameters = pnode.getHealthCheckParameters();
            if (parameters != null)
              option += " " + parameters;
          }
        } else {
          option += " check";
          String parameters = pnode.getHealthCheckParameters();
          if (parameters != null)
            option += " " + parameters;
        }
        // Track first node that has health check enabled
        if (nodes.get(nodeAddress) == null) {
          nodes.put(nodeAddress, appName + "/" + pnode.getName());
        }
      }

      node.addOption(option);
    }

  }

  /**
   * This method defines the proxy rules based on the Host/domain name
   * 
   * @param frontEnd The front end/proxy to add the rule to
   * @param hostDomainName The Host domain name (PaasApplication.getConnection()
   */
  private void setFrontEndRule(Proxy frontEnd, String hostDomainName) {
    Settings settings = frontEnd.getSettings();
    settings.addOption("acl acl_" + hostDomainName + " hdr_beg(Host) " + hostDomainName);
    settings.addOption("use_backend " + hostDomainName + " if acl_" + hostDomainName);
  }

  /**
   * This method returns the load balancer (HAProxy) configuration content as a string in the native
   * HAProxy configuration format
   * 
   * @return the load balancer (HAProxy) configuration content as a String
   */
  public String toString() {
    return lb.toString();
  }

}
