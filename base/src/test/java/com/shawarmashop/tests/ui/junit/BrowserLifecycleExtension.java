package com.shawarmashop.tests.ui.junit;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class BrowserLifecycleExtension implements AfterEachCallback {
    /** Гарантированно закрывает WebDriver и браузер после каждого теста для освобождения ресурсов системы. */
    @Override
    public void afterEach(ExtensionContext context) {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Selenide.closeWebDriver();
        }
    }
}
