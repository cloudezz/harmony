
package com.cloudezz.management.paasfrontend;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class defines a PaaSFrontEnd application configuration.
 */
public class PaaSApplication {
    /**
     * HTTPS protocol
     */
    public static int HTTPS = 1;
    /**
     * HTTP protocol
     */
    public static int HTTP = 2;

    /**
     * NON STICKY connection
     */
    public static int NON_STICKY = 0;
    /**
     * Sticky connection implementation using a HTTP cookie
     */
    public static int COOKIE = 1;
    /**
     * Sticky connection implementation using client source IP address
     */
    public static int SOURCE_IP = 2;

    private String url;
    private int stickynessMode;
    private String IPStickynessOption;
    private int protocol;
    private List<String>options;
    private List<PaaSNode> nodes;
    private Map<String, String> errorRedirects;
    private String nodeHealthCheckOption;


    /**
     * PaasApplication configuration.
     * @param url  The url of the application (should be unique among all application)
     * @param stickynessMode  Set to stickyness mode (NON_STICKY: no stickyness, COOKIE: use cookies, SOURCE_IP: use the client source IP address). Bitmap value
     * @param protocol  Set the protocol allowed for connection: HTTP, HTTPS. Bitmap value.
     */
    public PaaSApplication(String url, int stickynessMode, int protocol) {
        this.url = url;
        this.stickynessMode = stickynessMode;
        this.protocol = protocol;
        options = new ArrayList<String>();
        nodes = new ArrayList<PaaSNode>();
        errorRedirects = new HashMap<String, String>();
    }

    /**
     * PaasApplication configuration.
     * @param url  The url of the application (should be unique among all application)
     * @param stickynessMode  Set to stickyness mode (NON_STICKY: no stickyness, COOKIE: use cookies, SOURCE_IP: use the client source IP address). Bitmap value
     * @param protocol  Set the protocol allowed for connection: HTTP, HTTPS. Bitmap value.
     */
    public PaaSApplication(String url, String path, int stickynessMode, int protocol) {
        this(url + ((path.startsWith("/")) ? path : "/" + path), stickynessMode, protocol);
    }

    /**
     * This method returns the application url
     * @return The url as a String
     */
    public String getUrl() {
        return url;
    }

    /**
     * This method sets the application url
     * @param url  The url as a String
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * This method returns the application stickyness mode [NON_STICKY, COOKIE, SOURCE_IP]
     * @return the stickyness mode
     */
    public int getStickynessMode() {
        return stickynessMode;
    }

    /**
     * This method sets the application stickyness mode [NON_STICKY, COOKIE, SOURCE_IP]. The mode is a bit map value so
     * more both COOKIE and SOURCE_IP can be set (COOKIE | SOURCE_IP)
     * @param stickynessMode  The stickyness mode
     */
    public void setStickynessMode(int stickynessMode) {
        this.stickynessMode = stickynessMode;
    }

    /**
     * This method returns the stickyness optional settings of the SOURCE_IP mode
     * @return  the stickyness settings
     */
    public String getIPStickynessOption() {
        return IPStickynessOption;
    }

    /**
     * This method sets the stickyness optional settings of the SOURCE_IP mode
     * @param IPStickynessOption   the stickyness settings
     */
    public void setIPStickynessOption(String IPStickynessOption) {
        this.IPStickynessOption = IPStickynessOption;
    }

    /**
     * This method returns the application protocol supported [HTTP, HTTPS]. This is a bit map value so
     * both mode can be supported [HTTP | HTTPS]
     * @return The protocol supported
     */
    public int getProtocol() {
        return protocol;
    }

    /**
     * This method sets the application protocol supported [HTTP, HTTPS]. This is a bit map value so
     * both mode can be supported [HTTP | HTTPS]
     * @param protocol  The protocol supported
     */
    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    /**
     * This method returns the list a server nodes
     * @return  The List of server (nodes PaasNode)
     */
    public List<PaaSNode> getNodes() {
        return nodes;
    }

    /**
     * This method sets the list a server nodes
     * @param nodes  the list a server nodes
     */
    public void setNodes(List<PaaSNode> nodes) {
        this.nodes = nodes;
    }

    /**
     * This method adds a server node to the application
     * @param node  The node to add
     */
    public void addNode(PaaSNode node) {
        nodes.add(node);
    }

    /**
     * This method returns a list of optional settings for the application
     * @return  The List of settings
     */
    public List<String> getOptions() {
        return options;
    }

    /**
     * This method adds an optional setting
     * @param option  The settinf to add
     */
    public void addOption(String option) {
        options.add(option);
    }

    /**
     * This method returns a Map of error redirect URLs. The map key is the error code and the map value is the redirect URL
     * @return the Map of error redirect URLs
     */
    public Map<String, String> getErrorRedirects() {
        return errorRedirects;
    }

    /**
     * This method returns true of error redirects have been defined
     * @return  true if redirects are defined, false otherwise
     */
    public boolean hasErrorRedirects() {
        if (errorRedirects.size() > 0)
            return true;
        return false;
    }

    /**
     * This method adds a error redirect URL for a specific error code
     * @param errorCode     The error code (i.e.  503  Server node not available)
     * @param errorRedirectUrl  The redirect URL
     */
    public void addErrorRedirect(String errorCode, String errorRedirectUrl) {
        errorRedirects.put(errorCode, errorRedirectUrl);
    }

    /**
     * This method returns the nodes health check optional setting
     * @return The nodes health check setting
     */
    public String getNodeHealthCheckOption() {
        return nodeHealthCheckOption;
    }

    /**
     * This method sets the nodes health check optional setting
     * @param nodeHealthCheckOption  The nodes health check setting
     */
    public void setNodeHealthCheckOption(String nodeHealthCheckOption) {
        this.nodeHealthCheckOption = nodeHealthCheckOption;
    }

    /**
     * This method return the application URL domain
     * @return  the application URL domain
     * @throws MalformedURLException
     */
    public String getDomain() throws MalformedURLException {
        URL url = getURL(this.url);
        return url.getHost();
    }

    /**
     * This method return the application URL path
     * @return  the application URL path
     * @throws MalformedURLException
     */
    public String getPath() throws MalformedURLException {
        URL url = getURL(this.url);
        String path = url.getPath();
        if (path.length() == 0)
            path = null;
        return path;
    }

    private URL getURL(String urlStr)  throws MalformedURLException {
        URL url = null;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            if (e.getMessage().indexOf("no protocol") > -1) {
                return getURL("http://" + urlStr);
            } else {
                throw e;
            }
        }
        return url;
    }
}
