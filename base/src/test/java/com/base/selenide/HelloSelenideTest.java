package com.base.selenide;

import com.codeborne.selenide.*;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class HelloSelenideTest {
    @BeforeAll
    public static void setUp() {
        Configuration.browser = "chrome";
        Configuration.baseUrl = "https://shawarma.threadqa.ru/";
        Configuration.timeout = 10000;
        Configuration.headless = true;

        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide()
                        .screenshots(true)
                        .savePageSource(true));
    }

    @AfterEach
    public void tearDown() {
        Selenide.closeWebDriver();
    }

    @Test
    public void test1() {
        Selenide.open("/login");

        SelenideElement usernameInput = $x("//input[@data-testid='login-username']");
        SelenideElement passwordInput = $x("//input[@data-testid='login-password']");
        SelenideElement submitButton = $x("//button[@data-testid='login-submit']");

        usernameInput.sendKeys("owner");
        passwordInput.sendKeys("owner123");
        submitButton.click();

        Selenide.webdriver().shouldHave(WebDriverConditions.urlContaining("/menu"), Duration.ofSeconds(5));
        ElementsCollection recipeCards = $$x("//div[@data-testid='recipe-card']");
        recipeCards.should(CollectionCondition.sizeGreaterThanOrEqual(1));
    }
}