package com.shawarmashop.tests.ui;

import com.shawarmashop.tests.ui.pages.LoginPage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class LoginUiTests {

    private final LoginPage loginPage = new LoginPage();

    /** Базовая проверка UI-формы логина: успешный переход и сабмит с валидными данными. */
    @Test
    public void test1() {
        loginPage.openLogin().submit("owner", "owner123");
    }
}
