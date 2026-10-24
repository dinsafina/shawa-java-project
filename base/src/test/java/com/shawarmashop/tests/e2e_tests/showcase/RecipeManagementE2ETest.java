package com.shawarmashop.tests.e2e_tests.showcase;

import com.shawarmashop.tests.auth.ApiClient;
import com.shawarmashop.tests.auth.Users;
import com.shawarmashop.tests.db.factories.RecipeFixture;
import com.shawarmashop.tests.db.rows.RecipeRow;
import com.shawarmashop.tests.dto.recipes.RecipeResponse;
import com.shawarmashop.tests.dto.recipes.UpdateRecipeRequest;
import com.shawarmashop.tests.junit.DbTest;
import com.shawarmashop.tests.junit.PartnersTest;
import com.shawarmashop.tests.partners.PartnerStubs;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

@PartnersTest
@DbTest
@DisplayName("E2E: управление рецептами")
class RecipeManagementE2ETest {

    private static final String SANDWICH_IMAGE = "src/test/resources/test-data/sandwich.jpg";
    private static final Double DEFAULT_PRICE = 199.00;

    private final ApiClient owner = ApiClient.asUser(Users.OWNER);

    @Test
    @DisplayName("PATCH обновляет имя, описание и цену")
    void patchUpdatesFields() {
        RecipeRow recipe = createRecipe("PATCH_SOURCE");

        String newName = uniqueName("PATCHED");
        String newDescription = "Обновлённое описание";
        BigDecimal newPrice = new BigDecimal("345.00");

        RecipeResponse updated = owner.recipes()
                .update(
                        recipe.getId(),
                        UpdateRecipeRequest.builder()
                                .name(newName)
                                .description(newDescription)
                                .price(newPrice)
                                .build()
                )
                .success();

        assertThat(updated.getId()).isEqualTo(recipe.getId());

        assertThat(updated.getName()).isEqualTo(newName);

        assertThat(updated.getPrice()).isEqualByComparingTo(newPrice);

        RecipeResponse fetched = owner.recipes()
                .get(recipe.getId())
                .success();

        assertThat(fetched.getName()).isEqualTo(newName);

        assertThat(fetched.getPrice()).isEqualByComparingTo(newPrice);
    }

    @Test
    @DisplayName("PATCH несуществующего рецепта возвращает 404")
    void patchUnknownIs404() {
        long unknownRecipeId = 99_999_999L;

        owner.recipes()
                .update(
                        unknownRecipeId,
                        UpdateRecipeRequest.builder()
                                .price(new BigDecimal("10.00"))
                                .build()
                )
                .assertStatus(404);
    }

    @Test
    @DisplayName("PATCH с занятым именем возвращает 409")
    void patchDuplicateNameIs409() {
        RecipeRow existing = createRecipe("DUPLICATE_EXISTING");
        RecipeRow target = createRecipe("DUPLICATE_TARGET");

        owner.recipes()
                .update(
                        target.getId(),
                        UpdateRecipeRequest.builder()
                                .name(existing.getName())
                                .build()
                )
                .assertStatus(409);

        RecipeResponse unchanged = owner.recipes()
                .get(target.getId())
                .success();

        assertThat(unchanged.getName()).isEqualTo(target.getName());
    }

    @Test
    @DisplayName("POST /image принимает JPEG и возвращает URL")
    void uploadImageHappy() {
        RecipeRow recipe = createRecipe("IMAGE_UPLOAD");
        byte[] image = readResource(SANDWICH_IMAGE);

        var result = owner.recipes()
                .uploadImage(
                        recipe.getId(),
                        "sandwich.jpg",
                        "image/jpeg",
                        image
                );

        result.assertStatus(200);

        assertThat(result.getRawBody()).contains("/uploads/recipes/");
    }

    @Test
    @DisplayName("POST /image с неподдерживаемым типом возвращает 400")
    void uploadImageWrongTypeIs400() {
        RecipeRow recipe = createRecipe("IMAGE_WRONG_TYPE");

        owner.recipes()
                .uploadImage(
                        recipe.getId(),
                        "note.txt",
                        "text/plain",
                        "not an image".getBytes()
                )
                .assertStatus(400);
    }


    @Test
    @DisplayName("GraphQL-ошибка партнёра на GET /reviews возвращает 502")
    void reviewsPartnerErrorIs502(PartnerStubs stubs) {
        RecipeRow recipe = createRecipe("REVIEWS_ERROR");

        stubs.reviews()
                .reviewsByRecipeError(recipe.getId(), "rate-limit exceeded");

        owner.recipes()
                .reviews(recipe.getId(), 0)
                .assertStatus(502);
    }

    private static RecipeRow createRecipe(String prefix) {
        return RecipeFixture.builder()
                .name(uniqueName(prefix))
                .price(DEFAULT_PRICE)
                .build()
                .insert();
    }

    private static String uniqueName(String prefix) {
        return "E2E_" + prefix + "_" + System.nanoTime();
    }

    private static byte[] readResource(String path) {
        try {
            return Files.readAllBytes(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
