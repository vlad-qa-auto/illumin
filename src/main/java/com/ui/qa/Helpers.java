package com.ui.qa;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Rectangle;

import java.util.Arrays;
import java.util.Map;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static java.lang.Double.parseDouble;

public class Helpers {

    public static final Condition visibleTop = new VisibleTop();

    public static void setWindowWidth(int width) {
        getWebDriver().manage().window().setSize(new Dimension(width, getWebDriver().manage().window().getSize().getHeight()));
    }

    public static String getBGColor(SelenideElement elementToSearch) {
        SelenideElement current = elementToSearch;
        while (isTransparent(current.getCssValue("background-color"))) {
            if (current.getTagName().equals("body")) return null;
            current = current.parent();
        }
        return current.getCssValue("background-color");
    }

    private static boolean isTransparent(String color) {
        String colorMod = color.replaceAll("\\s+", "").toLowerCase();
        return Arrays.asList("transparent", "", "rgba(0,0,0,0)").contains(colorMod);
    }

    public static void acceptCookies() {
        SelenideElement acceptCookies = $("[aria-label=Accept");
        if (acceptCookies.is(visible)) acceptCookies.click();
    }

    public static Rectangle getRectangle(SelenideElement element) {
        Map<String, Object> map = executeJavaScript("return arguments[0].getBoundingClientRect()", element);
        return new Rectangle(
                (int) parseDouble(map.get("x").toString()),
                (int) parseDouble(map.get("y").toString()),
                (int) parseDouble(map.get("height").toString()),
                (int) parseDouble(map.get("width").toString()));
    }
}
