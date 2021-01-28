package com.ui.qa;

import com.codeborne.selenide.SelenideElement;
import org.testng.annotations.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class SubscribeEmailField extends BaseTest {

    SelenideElement email;
    SelenideElement submit;
    SelenideElement confirm;

    @DataProvider
    public static Object[] valid() {
        return new Object[]{
                "email@example.com",
                "firstname.lastname@example.com",
                "email@subdomain.example.com",
                "firstname+lastname@example.com",
                "email@123.123.123.123",
                "email@[123.123.123.123]",
                "\"email\"@example.com",
                "1234567890@example.com",
                "email@example-one.com",
                "_______@example.com",
                "email@example.name",
                "email@example.museum",
                "email@example.co.jp",
                "firstname-lastname@example.com"};
    }

    @DataProvider
    public static Object[] invalid() {
        return new Object[]{
                "plainaddress",
                "#@%^%#$@#$@#.com",
                "@example.com",
                "Joe Smith <email@example.com>",
                "email.example.com",
                "email@example@example.com",
                ".email@example.com",
                "email.@example.com",
                "email..email@example.com",
                "email@example.com (Joe Smith)",
                "email@example",
                "email@-example.com",
                "email@example..com",
                "Abc..123@example.com"};
    }


    @BeforeClass
    @Parameters({"url"})
    public void beforeSubscribeEmail(@Optional() String url) {
        open(url==null ? defaults.getSubscribeEmailUrl():url);
    }

    @BeforeMethod
    public void beforeMethod() {
        refresh();
        email = $("#email");
        submit = email.parent().find(byAttribute("type", "submit"));
        confirm = $(byText("Thank you for subscribing!"));
    }

    @Test(testName = "Valid email",
            dataProvider = "valid")
    public void validEmail(String valid) {
        email.val(valid);
        submit.click();
        email.shouldNotBe(exist.because("Confirmation message instead of email field"));
        submit.shouldNotBe(exist.because("Confirmation message instead of submit button"));
        confirm.shouldBe(visible.because("Confirmation message instead of email field"));
    }

    @Test(testName = "Invalid email",
            dataProvider = "invalid")
    public void InvalidEmail(String invalid) {
        email.val(invalid);
        submit.click();
        email.shouldHave(cssValue("border", "1px solid rgb(224, 0, 0)").because("Red border"));
        confirm.shouldNotBe(exist.because("Confirmation message should not appear"));
        email.click();
        email.shouldHave(cssValue("border", "0px none rgb(70, 70, 70)").because("Default border"));
    }
}
