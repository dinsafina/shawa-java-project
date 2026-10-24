package com.shawarmashop.tests.ui.junit;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class SelenideProxyExtension implements BeforeEachCallback, AfterEachCallback {

    private boolean previousProxyState;

    @Override
    public void afterEach(ExtensionContext context) {
        Configuration.proxyEnabled = previousProxyState;
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        previousProxyState = Configuration.proxyEnabled;

        Configuration.proxyEnabled = true;
    }
}
