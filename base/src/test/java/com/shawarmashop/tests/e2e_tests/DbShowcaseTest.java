package com.shawarmashop.tests.e2e_tests;

import com.shawarmashop.tests.db.factories.RecipeFixture;
import com.shawarmashop.tests.db.repository.RecipeRepository;
import com.shawarmashop.tests.db.rows.RecipeRow;
import com.shawarmashop.tests.junit.DbTest;
import org.junit.jupiter.api.Test;

@DbTest
public class DbShowcaseTest {
    private final RecipeRepository recipeRepository = new RecipeRepository();

    @Test
    public void test1() {
        RecipeFixture recipeFixture = RecipeFixture.builder().build();
        RecipeRow insert = recipeFixture.insert();
    }
}
