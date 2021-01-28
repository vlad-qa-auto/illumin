package com.ui.qa;

import com.codeborne.selenide.SelenideElement;
import org.testng.annotations.*;

import java.util.function.Function;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.ui.qa.Helpers.setWindowWidth;
import static java.lang.Double.parseDouble;
import static java.time.Duration.ofSeconds;
import static org.testng.Assert.assertEquals;

public class Capabilities extends BaseTest {

    Function<SelenideElement, Double> currentTime = video -> parseDouble(video.getAttribute("currentTime"));

    @BeforeClass
    @Parameters({"url"})
    public void beforeCapabilities(@Optional() String url) {
        open(url==null ? defaults.getMainPageUrl():url);
    }

    @AfterClass
    public void afterCapabilities() {
        setWindowWidth(1300);
    }

    @DataProvider
    public static Object[] wide() {
        return new Object[][]{
                {3, "Inform"},
                {2, "Minimize waste"},
                {1, "Control"}};
    }

    @Test(testName = "Wide window (1300px)",
            dataProvider = "wide")
    public void wideWindow(int number, String capability) {
        setWindowWidth(1300);
        $$(".accordion-title").find(text(capability)).click();
        $("#capabilities-" + number).shouldBe(visible);
        $("#capabilities-" + (number % 3 + 1)).shouldNotBe(visible);
        $("#capabilities-" + ((number + 1) % 3 + 1)).shouldNotBe(visible);
    }

    @DataProvider
    public static Object[] narrow() {
        return new Object[]{1, 2, 3};
    }

    @Test(testName = "Narrow window (700px)",
            dataProvider = "narrow")
    public void narrowWindow(int number) {
        setWindowWidth(700);
        SelenideElement video = $("#capabilities-" + number)
                .scrollTo()
                .shouldBe(visible)
                .shouldHave(
                        or("Not playing",
                                attribute("ended", "true"),
                                attribute("currentTime", "0")),
                        ofSeconds(30));

        SelenideElement button = video.parent().find(".video-button");
        button.click();
        assertEquals(currentTime.apply(video), 1, 1, "Video time");

        button.click();
        Double paused = currentTime.apply(video);
        sleep(500);
        assertEquals(currentTime.apply(video), paused, "Paused time");
    }
}
