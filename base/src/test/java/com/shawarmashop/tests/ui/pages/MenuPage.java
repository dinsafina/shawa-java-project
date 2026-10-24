package com.shawarmashop.tests.ui.pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.shawarmashop.tests.ui.components.MenuRecipeCard;
import com.shawarmashop.tests.ui.components.OrderModal;

import static com.codeborne.selenide.Selenide.$$;

public class MenuPage extends BasePage<MenuPage> {

    private final ElementsCollection cards = $$("div[data-testid=recipe-card]");

    /** Проверка, что открылась нужная страница-
     * Переходит в меню, ожидает загрузки страницы и проверяет наличие карточек рецептов. */
    public MenuPage openMenu() {
        return open("/menu")
                .shouldBeOpened()
                .shouldHaveRecipes();
    }

    /**
     * кол-во карточек больше 0
     */
    public MenuPage shouldHaveRecipes() {
        cards.should(CollectionCondition.sizeGreaterThan(0));
        return this;
    }

    /** Находит и возвращает компонент карточки рецепта по его атрибуту data-recipe-name. */
    public MenuRecipeCard cardByName(String name) {
        SelenideElement root = cards
                .find(Condition.attribute("data-recipe-name", name))
                .shouldBe(Condition.visible);
        return new MenuRecipeCard(root);
    }

    /**Открывает карточку, делает заказ и проверяет, что модальное окно заказа открылось*/
    public OrderModal startOrderFor(String name){
        cardByName(name).clickOrder();
        return new OrderModal().shouldOpened();
    }

}
