
package com.cloudezz.management.paasfrontend.haproxy.impl;

import com.cloudezz.management.paasfrontend.impl.SettingsImpl;

public class SettingsHAProxyImpl extends SettingsImpl {

    public SettingsHAProxyImpl(String type, String category) {
        super(type, category);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (tag != null) {
            sb.append(tag).append(NL);
        }
        for (String setting : getOptions()) {
            sb.append(TAB).append(setting).append(NL);
        }

        return sb.toString();
    }
}
