package com.shawarmashop.tests.ui.components;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class OrderModal {
    private final SelenideElement root = $("div.arcade-modal");
    private final SelenideElement quantityInput = root.$("input[type=number]");
    private final SelenideElement cancelBtn = root.$("button.arcade-btn--secondary");
    private final SelenideElement submitBtn = root.$("button.arcade-btn--primary");

    /**Проверка видимости модального окна заказа*/
    public OrderModal shouldOpened(){
        root.shouldBe(Condition.visible);
        return this;
    }

    /**Заполнение кол-ва товара*/
    public OrderModal setQty(int qty){
        quantityInput.sendKeys(String.valueOf(qty));
        return this;
    }

    @Step("Отменить создание заказа")
    public void cancel() {
        cancelBtn.shouldBe(Condition.enabled).click();
        root.shouldNot(Condition.exist);
    }

    @Step("Подтвердить создание заказа")
    public void submit() {
        submitBtn.shouldBe(Condition.enabled).click();
    }
}
