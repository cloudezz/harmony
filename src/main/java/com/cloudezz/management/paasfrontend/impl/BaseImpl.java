
package com.cloudezz.management.paasfrontend.impl;

import com.cloudezz.management.paasfrontend.Base;

public abstract class BaseImpl implements Base {
    public static String NL = System.getProperty("line.separator");
    public static String SP = " ";
    public static String TAB = "\t";
    protected String type;
    protected String tag;

    public BaseImpl(String type, String tag) {
        this.tag = tag;
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }
}
