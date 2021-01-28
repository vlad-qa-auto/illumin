package com.ui.qa;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Defaults {

    private String mainPageUrl;
    private String subscribeEmailUrl;
    private String browser;
    private Boolean headless;

    public Boolean getHeadless() {
        return headless;
    }

    public void setHeadless(Boolean headless) {
        this.headless = headless;
    }

    public String getSubscribeEmailUrl() {
        return subscribeEmailUrl;
    }

    public void setSubscribeEmailUrl(String subscribeEmailUrl) {
        this.subscribeEmailUrl = subscribeEmailUrl;
    }

    public String getMainPageUrl() {
        return mainPageUrl;
    }

    public void setMainPageUrl(String mainPageUrl) {
        this.mainPageUrl = mainPageUrl;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public Defaults load() {
        InputStream input = null;
        try {
            input = new FileInputStream("src/defaults.yaml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new Yaml().loadAs(input, Defaults.class);
    }
}
