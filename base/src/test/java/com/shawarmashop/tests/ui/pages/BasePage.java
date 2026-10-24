package com.shawarmashop.tests.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage<T extends BasePage<T>> { //ограничение обобщения типов

    private final SelenideElement toast = $("div[role=status]");
    protected final SelenideElement navLogo = $("nav a[href='/menu']");
    protected final SelenideElement pageHeading = $("h1.arcade-title");

    /** Открывает указанный относительный путь в браузере и возвращает экземпляр текущей страницы для fluent-цепочки. */
    public T  open(String path) {
        Selenide.open(path);
        return self();
    }

    /** Ожидает появления toast-уведомления и проверяет, что оно содержит заданный текст. */
    public T expectToast(String value){
        toast.shouldBe(Condition.visible).shouldBe(Condition.text(value));
        return self();
    }

    /** Ожидает видимости базовых элементов (лого, заголовок), подтверждая успешную загрузку страницы. */
    public T shouldBeOpened() {
        navLogo.shouldBe(Condition.visible);
        pageHeading.shouldBe(Condition.visible);
        return self();
    }

    /** Приводит текущий экземпляр к конкретному типу T для сохранения типизации при цепочке вызовов. */
    @SuppressWarnings("unchecked")
    private T self(){
        return (T) this;
    }
}
