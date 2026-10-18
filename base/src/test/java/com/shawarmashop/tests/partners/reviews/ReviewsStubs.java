package com.shawarmashop.tests.partners.reviews;

import com.shawarmashop.tests.partners.reviews.dto.GraphQLRatingResponse;
import com.shawarmashop.tests.partners.reviews.dto.RecipeRatingDto;
import com.shawarmashop.tests.partners.wiremock.StubBuilder;
import com.shawarmashop.tests.partners.wiremock.WireMockAdminClient;
import com.shawarmashop.tests.partners.wiremock.WiremockStubBase;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import static com.shawarmashop.tests.partners.wiremock.StubBuilder.post;

public class ReviewsStubs extends WiremockStubBase {

    private final static String GRAPHQL_PATH = "/graphql";
    private final static String OPERATION_RECIPE_RATING = "RecipeRating";
    private final static String OPERATION_REVIEWS_BY_RECIPE = "ReviewsByRecipe";

    public ReviewsStubs(WireMockAdminClient admin) {
        super(admin);
    }

    /** Возвращает билдер для настройки мока рейтинга конкретного рецепта с гибкой конфигурацией ответа. */
    public RatingBuilder ratingByRecipe(int recipeId) {
        return new RatingBuilder(recipeId);
    }

    /** Регистрирует мок GraphQL-ошибки для запроса ReviewsByRecipe с указанным сообщением. */
    public void reviewsByRecipeError(int recipeId, String message) {
        admin.addMapping(post(GRAPHQL_PATH)
                .withPriority(1)
                .withJsonPath("$.operationName", OPERATION_REVIEWS_BY_RECIPE)
                .withJsonPath("$.variables.recipeId", String.valueOf(recipeId))
                .willReturnJson(200, GraphQLRatingResponse.error(message)));
    }

    /** Внутренний билдер для гибкой настройки ответов рейтинга рецепта (успех, null, GraphQL-ошибка). */
    @RequiredArgsConstructor
    @Data
    public final class RatingBuilder {
        private final int recipeId;

        /** Регистрирует мок успешного рейтинга с переданными числовыми и строковыми параметрами. */
        public void respondWith(double averageStars, int totalReviews, String topComplaint, String trend) {
            respondWith(RecipeRatingDto.builder()
                    .recipeId(recipeId)
                    .averageStars(averageStars)
                    .totalReviews(totalReviews)
                    .topComplaint(topComplaint)
                    .trend(trend)
                    .build());
        }

        /** Регистрирует мок успешного рейтинга с полностью сформированным DTO RecipeRatingDto. */
        public void respondWith(RecipeRatingDto ratingDto) {
            admin.addMapping(graphqlMapping().willReturnJson(200, GraphQLRatingResponse.of(ratingDto)));
        }

        /** Регистрирует мок ответа с пустыми данными (data: null) для несуществующего рецепта. */
        public void respondWithNull() {
            admin.addMapping(graphqlMapping().willReturnJson(200, GraphQLRatingResponse.empty()));
        }

        /** Регистрирует мок ответа с GraphQL-ошибкой в массиве errors при HTTP 200. */
        public void respondWithGraphQLError(String message) {
            admin.addMapping(graphqlMapping().willReturnJson(200, GraphQLRatingResponse.error(message)));
        }

        /** Строит базовый StubBuilder с условиями по operationName и recipeId для операций рейтинга. */
        private StubBuilder graphqlMapping() {
            return post(GRAPHQL_PATH)
                    .withPriority(1)
                    .withJsonPath("$.operationName", OPERATION_RECIPE_RATING)
                    .withJsonPath("$.variables.recipeId", String.valueOf(recipeId));
        }
    }
}