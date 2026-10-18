package com.shawarmashop.tests.junit;

import com.shawarmashop.tests.partners.PartnerStubs;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.*;

public class PartnerStubsExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(PartnerStubsExtension.class);
    public static String KEY = "partnerStubs";

    /** Сбрасывает WireMock к дефолтам после каждого теста для изоляции тестов друг от друга. */
    @Override
    public void afterEach(ExtensionContext context) {
        PartnerStubs stubs = context.getStore(NAMESPACE).get(KEY, PartnerStubs.class);
        if (stubs != null) {
            stubs.resetToDefaults();
        }
    }

    /** Создает новый PartnerStubs и сбрасывает WireMock к дефолтам перед каждым тестом. */
    @Override
    public void beforeEach(ExtensionContext context) {
        PartnerStubs stubs = new PartnerStubs();
        stubs.resetToDefaults();
        context.getStore(NAMESPACE).put(KEY, stubs);
    }

    /** Проверяет, что параметр теста имеет тип PartnerStubs для инъекции. */
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == PartnerStubs.class;
    }

    /** Возвращает экземпляр PartnerStubs из хранилища Extension для инъекции в параметр теста. */
    @Override
    public @Nullable Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(KEY, PartnerStubs.class);
    }
}
