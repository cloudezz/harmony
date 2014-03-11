
package com.cloudezz.management.paasfrontend;

import java.util.ArrayList;
import java.util.List;

/**
 * This class defines a PaaS server node configuration.
 */
public class PaaSNode {
    private String name;
    private String connection;
    private Integer weight;
    private List<String>options;
    private boolean healthCheck;
    private String healthCheckParameters;
    private boolean trackHealthCheck;

    /**
     * Create an instance of a server node.  By default nodes have health checks disabled.
     * Use setHealthCheck to enable server health checks.
     * @param name      The name of the server
     * @param connection       The connection of the server (IP:port)
     * @param weight    The weight of the server
     */
    public PaaSNode(String name, String connection, Integer weight) {
        this.name = name;
        this.connection = connection;
        this.weight = weight;
        options = new ArrayList<String>();
        healthCheck = false;
    }

    /**
     * This method returns the name of the server node
     * @return the name of the server node
     */
    public String getName() {
        return name;
    }

    /**
     * This method sets the name of the server node
     * @param name  the name of the server node
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method returns the node connection information (usually IP:port)
     * @return the node connection
     */
    public String getConnection() {
        return connection;
    }

    /**
     * This method sets the node connection information (usually IP:port)
     * @param connection the node connection
     */
    public void setConnection(String connection) {
        this.connection = connection;
    }

    /**
     * This method returns the server node weight
     * @return the node weight
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * This method sets the node weight
     * @param weight the node weight
     */
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    /**
     * This method returns the node optional settings
     * @return the node settings
     */
    public List<String> getOptions() {
        return options;
    }

    /**
     * This method adds an optional node setting
     * @param option the node setting to add
     */
    public void addOption(String option) {
        options.add(option);
    }

    /**
     * This method returns true if a server node availability is checked by the load balancer.  Health check are NOT enabled by default
     * @return  true if the server node availability is checked by the load balancer, false otherwise
     */
    public boolean hasHealthCheck() {
        return healthCheck;
    }

    /**
     * This method sets the health check options.
     * @param healthCheck  Set to true to check server node availability
     * @param trackHealthCheck  Set to true to optimize server check availability. If true will perform only one health
     * check per server even if the same server is used by several proxies
     * @param healthCheckParameters the health check optional parameters
     */
    public void setHealthCheck(boolean healthCheck, boolean trackHealthCheck, String healthCheckParameters) {
        this.healthCheck = healthCheck;
        this.healthCheckParameters = healthCheckParameters;
        this.trackHealthCheck = trackHealthCheck;
    }

    /**
     * This method returns the health check optional parameters
     * @return the health check optional parameters
     */
    public String getHealthCheckParameters() {
        return healthCheckParameters;
    }

    /**
     * This method sets the health check optional parameters
     * @param healthCheckParameters the health check optional parameters
     */
    public void setHealthCheckParameters(String healthCheckParameters) {
        this.healthCheckParameters = healthCheckParameters;
    }

    /**
     * This method returns true if the health check optimization is enabled See setHealthCheck
     * @return true if the health check optimization is enabled, false otherwise
     */
    public boolean hasTrackHealthCheck() {
        return trackHealthCheck;
    }

    /**
     * This method enables and disables the health check optimization
     * @param trackHealthCheck  true to enable the health check optimization, false to disable
     */
    public void setTrackHealthCheck(boolean trackHealthCheck) {
        this.trackHealthCheck = trackHealthCheck;
    }

}
