
package com.cloudezz.harmony.management.paasfrontend.haproxy.impl;

import com.cloudezz.harmony.management.paasfrontend.Node;
import com.cloudezz.harmony.management.paasfrontend.impl.ProxyImpl;

public class ProxyHAProxyImpl extends ProxyImpl {

    public ProxyHAProxyImpl(String type, String name, String address) {
        super(type, "listen", name, address);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(tag);
        sb.append(TAB).append(getName()).append(SP).append(getAddress()).append(NL);
        sb.append(getSettings());
        for (Node node : getNodes()) {
            sb.append(TAB).append(node);
        }
        return sb.toString();
    }

}
