
package com.cloudezz.harmony.management.paasfrontend;

/**
 * Base interface for load balancer components.
 */
public interface Base {
    /**
     * This method returns the component tag. A tag is a display label used to define the
     * component configuration section
     * @return  The tag as a String
     */
    String getTag();

    /**
     * This method sets the component tag. See getTag()
     * @param tag  The tag label
     */
    void setTag(String tag);

    /**
     * This method returns the implementation type
     * @return  The implementation type as a String
     */
    String getType();
}
