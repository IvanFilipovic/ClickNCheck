package com.exit3.testing;

import java.io.FileNotFoundException;

public class UiElement {
    private String element;
    private String androidSelector;
    private String androidLocator;
    private String iosSelector;
    private String iosLocator;
    public UiElement name(String name,
                          String aSelector,
                          String aLocator,
                          String iSelector,
                          String iLocator) {
        element = name;
        androidSelector = aSelector;
        androidLocator = aLocator;
        iosSelector = iSelector;
        iosLocator = iLocator;
        return this;
    }
    public UiObject makeUiObject() throws FileNotFoundException {
        return new UiObject(element, androidSelector, androidLocator, iosSelector, iosLocator);
    }
}
