package com.ui.qa;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.conditions.Not;
import org.openqa.selenium.WebElement;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.executeJavaScript;

@ParametersAreNonnullByDefault
public class VisibleTop extends Condition {

    String js = """
            var rect = arguments[0].getBoundingClientRect();
            return (0 <= rect.y && rect.y <= window.innerHeight && 
                    0 <= rect.x && rect.x <= window.innerWidth);""";

    public VisibleTop() {
        super("visible top");
    }

    @Override
    @CheckReturnValue
    public boolean apply(Driver driver, WebElement element) {
        return executeJavaScript(js, element);
    }

    @Override
    @CheckReturnValue
    @Nonnull
    public String actualValue(Driver driver, WebElement element) {
        return "The Top Of Element Is Visible: " + executeJavaScript(js, element);
    }

    @Override
    @CheckReturnValue
    @Nonnull
    public Condition negate() {
        return new Not(this, true);
    }
}