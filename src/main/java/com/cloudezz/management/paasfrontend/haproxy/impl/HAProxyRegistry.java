
package com.cloudezz.management.paasfrontend.haproxy.impl;

import com.cloudezz.management.paasfrontend.LoadBalancer;
import com.cloudezz.management.paasfrontend.LoadBalancerFactory;
import com.cloudezz.management.paasfrontend.Node;
import com.cloudezz.management.paasfrontend.Proxy;
import com.cloudezz.management.paasfrontend.Settings;

public class HAProxyRegistry {
    public static String TYPE = "HAProxy";

    static {
        LoadBalancerFactory.register("HAProxy", LoadBalancer.class, HAProxyImpl.class);
        LoadBalancerFactory.register("HAProxy", Proxy.class, ProxyHAProxyImpl.class);
        LoadBalancerFactory.register("HAProxy", Node.class, ServerHAProxyImpl.class);
        LoadBalancerFactory.register("HAProxy", Settings.class, SettingsHAProxyImpl.class);
    }
}
