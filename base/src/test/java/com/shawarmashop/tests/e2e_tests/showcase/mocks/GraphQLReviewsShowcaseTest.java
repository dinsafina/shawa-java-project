package com.shawarmashop.tests.e2e_tests.showcase.mocks;

import com.shawarmashop.tests.auth.ApiClient;
import com.shawarmashop.tests.auth.Users;
import com.shawarmashop.tests.db.factories.RecipeFixture;
import com.shawarmashop.tests.db.rows.RecipeRow;
import com.shawarmashop.tests.dto.recipes.RecipeResponse;
import com.shawarmashop.tests.junit.DbTest;
import com.shawarmashop.tests.junit.PartnersTest;
import com.shawarmashop.tests.partners.PartnerStubs;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


@PartnersTest
@DbTest
@DisplayName("Showcase: GraphQL reviews — rating мерджится в /recipes/{id}, GraphQL error → null")
class GraphQLReviewsShowcaseTest {

    private final ApiClient owner = ApiClient.asUser(Users.OWNER);

    @Test
    @DisplayName("happy: застабленный rating оказывается в теле ответа /recipes/{id}")
    void ratingMergedIntoRecipe(PartnerStubs stubs) {
        RecipeRow seeded = freshRecipe();
        stubs.reviews()
                .ratingByRecipe(seeded.getId())
                .respondWith(4.4, 12, "слишком сухой", "UP");

        RecipeResponse loaded = owner.recipes().get(seeded.getId()).success();
        assertThat(loaded.getRating()).isNotNull();
        assertThat(loaded.getRating().getAverageStars()).isEqualTo(4.4);
        assertThat(loaded.getRating().getTotalReviews()).isEqualTo(12);
        assertThat(loaded.getRating().getTopComplaint()).isEqualTo("слишком сухой");
        assertThat(loaded.getRating().getTrend()).isEqualTo("UP");
    }

    @Test
    @DisplayName("партнёр вернул GraphQL errors → recipe грузится с rating=null")
    void graphQLErrorFallsBackToNullRating(PartnerStubs stubs) {
        RecipeRow seeded = freshRecipe();
        stubs.reviews()
                .ratingByRecipe(seeded.getId())
                .respondWithGraphQLError("rate-limit exceeded");

        RecipeResponse loaded = owner.recipes().get(seeded.getId()).success();
        assertThat(loaded.getRating()).isNull();
        assertThat(loaded.getId()).isEqualTo(seeded.getId());
        assertThat(loaded.getName()).isEqualTo(seeded.getName());
    }

    private static RecipeRow freshRecipe() {
        return RecipeFixture.builder()
                .name("REVIEWS_SHOWCASE_" + System.nanoTime())
                .prepSeconds(20)
                .build()
                .insert();
    }
}
