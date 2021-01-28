package com.ui.qa;

import com.codeborne.selenide.Configuration;
import org.testng.ITest;
import org.testng.annotations.*;

import java.lang.reflect.Method;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.ui.qa.Helpers.setWindowWidth;

public class BaseTest implements ITest {

    Defaults defaults = new Defaults().load();
    String testName;

    @BeforeSuite
    @Parameters({"browser", "headless"})
    public void beforeSuite(@Optional() String browser, @Optional() Boolean headless) {
        Configuration.browser = browser==null ? defaults.getBrowser():browser;
        Configuration.headless = headless==null ? defaults.getHeadless():headless;
        open(defaults.getMainPageUrl());
        setWindowWidth(1300);
    }

    @AfterSuite
    public void afterSuite() {
        getWebDriver().close();
    }

    @BeforeMethod
    public void BeforeMethod(Method method, Object[] testData) {
        testName = method.getAnnotation(Test.class).testName();
    }

    @Override
    public String getTestName() {
        return testName;
    }
}
