
package com.cloudezz.management.paasfrontend.impl;

import java.util.ArrayList;
import java.util.List;

import com.cloudezz.management.paasfrontend.Settings;

/**
 */
public abstract class SettingsImpl extends BaseImpl implements Settings {
    private List<String> options;

    public SettingsImpl(String type, String tag) {
        super(type, tag);
        options = new ArrayList<String>();
    }

    public void addOption(String value) {
        options.add(value);
    }

    public List<String> getOptions() {
        return options;
    }

}
