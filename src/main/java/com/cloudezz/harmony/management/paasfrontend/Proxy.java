
package com.cloudezz.harmony.management.paasfrontend;

import java.util.List;

/**
 * This class represents a load balancer proxy definition. Proxies are definied by Settings and Nodes
 */
public interface Proxy extends Base {
    /**
     * This method sets the name of the proxy
     * @param name
     */
    void setName(String name);

    /**
     * This method returns the name of the proxy
     * @return The name of the proxy
     */
    String getName();

    /**
     * This method returns the address of the proxy
     * @return The address of the proxy
     */
    String getAddress();

    /**
     * This method returns the Settings of the proxy
     * @return The Settings instance of the proxy
     */
    Settings getSettings();

    /**
     * This method creates a Settings defintion instance
     * @return The Settings instance created
     */
    Settings createSettings();

    /**
     * This method sets the Settings definition of the proxy
     * @param settings The Settings instance
     */
    void setSetting(Settings settings);

    /**
     * This method returns the nodes of the proxy
     * @return The node List of the proxy
     */
    List<Node> getNodes();

    /**
     * This method creates an instance of a proxy node
     * @param name   The name of the node
     * @param address The addres of the node
     * @return The Node instance created
     */
    Node createNode(String name, String address);

    /**
     * This method adds a node definition to addOption to the proxy
     * @param node The Node to addOption
     */
    void addNode(Node node);
}
