
package com.cloudezz.management.paasfrontend.impl;

import java.util.ArrayList;
import java.util.List;

import com.cloudezz.management.paasfrontend.LoadBalancerFactory;
import com.cloudezz.management.paasfrontend.Node;
import com.cloudezz.management.paasfrontend.Proxy;
import com.cloudezz.management.paasfrontend.Settings;

/**
 */
public abstract class ProxyImpl extends BaseImpl implements Proxy {
    private String name;
    private String address;
    private Settings settings;
    private List<Node> nodes;

    public ProxyImpl(String type, String tag, String name, String address) {
        super(type, tag);
        nodes = new ArrayList<Node>();
        this.name = name;
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the name of the server
     */
    public String getName() {
        return name;
    }

    /**
     * @return the address information  ip:port
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return  Return the set of settings
     */
    public Settings getSettings() {
        return settings;
    }

    public Settings createSettings() {
        Settings settings = LoadBalancerFactory.getSettings(type, null);
        setSetting(settings);
        return settings;
    }

    public void setSetting(Settings settings) {
        this.settings = settings;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public Node createNode(String name, String address) {
        Node node = LoadBalancerFactory.getNode(type, name, address);
        addNode(node);
        return node;
    }

    public void addNode(Node node) {
        nodes.add(node);    
    }
}
