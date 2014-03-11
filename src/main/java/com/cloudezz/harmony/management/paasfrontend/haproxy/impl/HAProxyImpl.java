
package com.cloudezz.harmony.management.paasfrontend.haproxy.impl;

import com.cloudezz.harmony.management.paasfrontend.Proxy;
import com.cloudezz.harmony.management.paasfrontend.Settings;
import com.cloudezz.harmony.management.paasfrontend.impl.LoadBalancerImpl;


public class HAProxyImpl extends LoadBalancerImpl {

    public HAProxyImpl(String type, String description) {
        super(type, description);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("# " + getDescription());
        sb.append(NL);
        for (Settings settings : getSettings()) {
            sb.append(settings).append(NL);
            sb.append(NL);
        }
        for (Proxy listen : getProxies()) {
            sb.append(listen).append(NL);
            sb.append(NL);
        }
        return sb.toString();
    }
}
