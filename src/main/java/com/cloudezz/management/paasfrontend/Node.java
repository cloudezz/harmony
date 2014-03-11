
package com.cloudezz.management.paasfrontend;

import java.util.Set;

/**
 * This class represents a proxy node definition
 */
public interface Node extends Base {
    /**
     * This method returns the name of the proxy
     * @return the name of the proxy
     */
    String getName();

    /**
     * This method returns the address of the proxy
     * @return the address of the proxy
     */
    String getAddress();

    /**
     * This method returns the Set of proxies
     * @return the option Set of the proxy
     */
    Set<String> getOptions();

    /**
     * Add option definition to the proxy
     * @param value  The definition of the option
     */
    void addOption(String value);
}
