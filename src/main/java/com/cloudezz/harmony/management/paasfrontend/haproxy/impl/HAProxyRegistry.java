
package com.cloudezz.harmony.management.paasfrontend.haproxy.impl;

import com.cloudezz.harmony.management.paasfrontend.LoadBalancer;
import com.cloudezz.harmony.management.paasfrontend.LoadBalancerFactory;
import com.cloudezz.harmony.management.paasfrontend.Node;
import com.cloudezz.harmony.management.paasfrontend.Proxy;
import com.cloudezz.harmony.management.paasfrontend.Settings;

public class HAProxyRegistry {
    public static String TYPE = "HAProxy";

    static {
        LoadBalancerFactory.register("HAProxy", LoadBalancer.class, HAProxyImpl.class);
        LoadBalancerFactory.register("HAProxy", Proxy.class, ProxyHAProxyImpl.class);
        LoadBalancerFactory.register("HAProxy", Node.class, ServerHAProxyImpl.class);
        LoadBalancerFactory.register("HAProxy", Settings.class, SettingsHAProxyImpl.class);
    }
}
