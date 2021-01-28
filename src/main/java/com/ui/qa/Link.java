package com.ui.qa;

import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Link {

    private String url;
    private final List<String> chain = new ArrayList<>();

    public Link(String url) {
        this.url = url;
        chain.add(" (" + url + ")");
    }

    public void verify(SoftAssert soft) throws IOException {
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
        } catch (IOException e) {
            soft.fail("Malformed URL" + ": " + url + chain.get(0));
            return;
        }
        con.setRequestMethod("HEAD");
        int code = con.getResponseCode();
        if (code < 300) return;
        if (code >= 400) {
            soft.fail(code + ": " + con.getResponseMessage() + ": " + url + chain.get(0));
            return;
        }
        url = con.getHeaderField("Location");
        if (chain.contains(url)) {
            soft.fail("Redirection loop" + ": " + chain.get(0));
            return;
        }
        chain.add(url);
        verify(soft);
    }
}
