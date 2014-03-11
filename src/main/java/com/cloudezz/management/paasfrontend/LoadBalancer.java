
package com.cloudezz.management.paasfrontend;

import java.util.List;

/**
 * Load Balancer.  A load balancer is composed of various proxies and settings
 */
public interface LoadBalancer {
    /**
     * @return the load balancer description
     */
    String getDescription();

    /**
     * Set the load balancer description
     * @param description  the balancer description
     */
    void setDescription(String description);

    /**
     * This method creates settings instance for a specified category
     * @param category  The settings category
     * @return The Settings object created
     */
    Settings createSettings(String category);

    /**
     * Add settings definition to the load balancer
     * @param settings the settings to addOption
     **/
    void addSettings(Settings settings);

    /**
     * This method creates a proxy definition instance
     * @param name  The name of the proxy
     * @param address  The address of the proxy
     * @return the proxy instance created
     */
    Proxy createProxy(String name, String address);

    /**
     * This method adds a proxy definition to the load blancer
     * @param proxy The proxy definition to addOption
     */
    void addProxy(Proxy proxy);

    /**
     * This method returns the list of Settings
     * @return  The List of settings
     */
    List<Settings> getSettings();

    /**
     *  This method returns the list of Proxies
     * @return The List of proxies
     */
    List<Proxy> getProxies();
}
