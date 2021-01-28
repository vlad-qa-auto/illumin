package com.ui.qa;

import com.codeborne.selenide.SelenideElement;
import org.testng.annotations.*;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.ui.qa.Helpers.*;

public class StickyMenu extends BaseTest {

    @DataProvider
    public static Object[] menuItems() {
        return new Object[][]{
                {"a[href='#overview']", "//*[@class='main-video-block']", "Overview"},
                {"a[href='#capabilities']", "//h2[text()='Capabilities']", "Capabilities"},
                {"a[href='#how-it-works']", "//div[contains(text(),'How it works')]", "How it works"},
                {"a[href='#powered-by-acuity']", "//h2[starts-with(text(),'Powered by')]", "Powered by Acuity"}};
    }

    @DataProvider
    public static Object[] windowWidth() {
        return new Object[]{1300, 700};
    }

    @BeforeClass
    @Parameters({"url"})
    public void beforeStickyMenu(@Optional() String url) {
        open(url==null ? defaults.getMainPageUrl():url);
        acceptCookies();
    }

    @AfterClass
    public void afterStickyMenu() {
        setWindowWidth(1300);
    }

    @Test(testName = "Menu items behavior on the wide window (1300px)",
            dataProvider = "menuItems")
    public void wideWindow(String menuItem, String anchor, String section) {
        setWindowWidth(1300);
        $(menuItem).click();
        $x(anchor).shouldHave(visibleTop.because("'" + section + "' section"));
    }

    @Test(testName = "Menu items behavior on the narrow window (700px)",
            dataProvider = "menuItems")
    public void narrowWindow(String menuItem, String anchor, String section) {
        setWindowWidth(700);
        $(".menu-items-wrap").click();
        $(menuItem).click();
        $x(anchor).shouldHave(visibleTop.because("'" + section + "' section"));
    }

    @Test(testName = "Menu behavior during scrolling",
            dataProvider = "windowWidth")
    public void scrollDown(int windowWidth) {
        setWindowWidth(windowWidth);
        SelenideElement illuminStickyHeader = $(".illumin-sticky-menu");
        int i = getRectangle(illuminStickyHeader).y - 400;
        long end = getRectangle($("body")).height;
        while (i < end) {
            executeJavaScript("window.scroll(0," + i + ")");
            illuminStickyHeader.shouldBe(visible);
            i = i + 100;
        }
    }

    @Test(testName = "Menu doesn't roll down at 900px")
    public void rollDown900() {
        setWindowWidth(900);
        $(".menu-items-wrap").click();
        $("a[href='#powered-by-acuity']").shouldBe(visible.because("Sticky menu should be open"));
    }

    @Test(testName = "Menu is not sticky at after resize")
    public void stickyAfterResize() {
        setWindowWidth(1300);
        executeJavaScript("window.scroll(0,document.body.scrollHeight)");
        setWindowWidth(700);
        executeJavaScript("window.scroll(0," + (getRectangle($("body")).height - 3000) + ")");
        $(".illumin-sticky-menu").shouldBe(visibleTop);
    }
}
