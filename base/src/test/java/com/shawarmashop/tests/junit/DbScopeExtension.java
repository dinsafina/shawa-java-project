package com.shawarmashop.tests.junit;

import com.shawarmashop.tests.db.TestDbScope;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class DbScopeExtension implements AfterEachCallback {
    @Override
    public void afterEach(ExtensionContext context) {
        TestDbScope.cleanup();
    }
}
