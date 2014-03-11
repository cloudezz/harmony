
package com.cloudezz.management.paasfrontend.haproxy.impl;

import java.util.Iterator;

import com.cloudezz.management.paasfrontend.impl.NodeImpl;


public class ServerHAProxyImpl extends NodeImpl {

    public ServerHAProxyImpl(String type, String name, String address) {
        super(type, "server", name, address);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(tag);
        sb.append(TAB).append(getName()).append(SP).append(getAddress());
        for (Iterator it = getOptions().iterator(); it.hasNext();) {
            sb.append(SP).append(it.next());
            sb.append(NL);
        }
        return sb.toString();
    }
}
