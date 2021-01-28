package com.ui.qa;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.io.IOException;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.*;
import static com.ui.qa.Helpers.getBGColor;
import static java.time.Duration.ofSeconds;
import static org.testng.Assert.assertTrue;

public class CommonTests extends BaseTest {

    @BeforeClass
    @Parameters({"url"})
    public void beforeCommonPage(@Optional() String url) {
        open(url==null ? defaults.getMainPageUrl():url);
    }

    @Test(testName = "Verification of links")
    public void linksVerification() throws IOException {
        SoftAssert soft = new SoftAssert();
        for (SelenideElement element : $$(By.tagName("a"))) {
            String href = element.getAttribute("href");
            if (element.isDisplayed() &&
                    !href.equals(url()) &&
                    !href.equals(url() + "#") &&
                    !href.contains("instagram.com") &&
                    !href.contains("twitter.com") &&
                    !href.contains("linkedin.com")) {
                new Link(href).verify(soft);
            }
        }
        soft.assertAll();
    }

    @Test(testName = "Invisible text")
    public void invisibleText() {
        SoftAssert soft = new SoftAssert();
        for (SelenideElement element : $$(By.xpath("//*[string-length(text()) > 1][count(*)=0]")).filter(visible)) {
            if (element.getText().isBlank()) continue;
            if (element.getCssValue("color").equals(getBGColor(element)))
                soft.fail("Invisible element: " + element);
        }
        soft.assertAll();
    }

    @Test(testName = "Olark button in incognito mode")
    public void olarkButton() {
        WebDriver oldDriver = getWebDriver();
        ChromeOptions options = new ChromeOptions().addArguments("--incognito");
        if (Configuration.headless) options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        setWebDriver(driver);
        try {
            open(defaults.getSubscribeEmailUrl());
            $(".olark-launch-button-wrapper").shouldBe(visible, ofSeconds(10));
        } finally {
            getWebDriver().close();
            setWebDriver(oldDriver);
        }
    }

    @DataProvider
    public static Object[] links() {
        return new Object[][]{
                {"Contact Us", "contact-us"},
                {"Schedule a demo", "schedule-a-demo"}
        };
    }

    @Test(testName = "Validation of the links of the main page",
            dataProvider = "links")
    public void linksCheck(String text, String endpoint) {
        for (SelenideElement element : $$x("//a[*/*/text()='" + text + "' or ./text()='" + text + "']")) {
            assertTrue(element.attr("href").contains(url().split("/")[2] + "/" + endpoint));
        }
    }
}
