
package com.cloudezz.management.paasfrontend;

import java.util.List;

/**
 * This class represents settings for load balancer and proxies
 */
public interface Settings extends Base {

    /**
     * This method addOption an option definition to the Settings
     * @param value The value of the option
     */
    void addOption(String value);

    /**
     * This method returns the list of options of the Settings
     * @return The list of options of the Settings
     */
    List<String> getOptions();
}
