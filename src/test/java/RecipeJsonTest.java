import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RecipeJsonTest {

    @Test
    @SneakyThrows
    public void serializeViaBuilder() {
        Recipe recipe = Recipe.builder()
                .id(1)
                .name("Margarita")
                .price(299.0)
                .build();
        String json =
                new ObjectMapper()
                        .writeValueAsString(recipe);

        JsonNode node = new ObjectMapper().readTree(json);

        assertThat(node.get("id").asInt()).isEqualTo(1);
        assertThat(node.get("name").asText()).isEqualTo("Margarita");
        assertThat(node.get("price").asDouble()).isEqualTo(299.0);
    }

    @Test
    @SneakyThrows
    void deserializeOneRecipe() {
        String json = """
                {
                  "id": 1,
                  "name": "Margarita",
                  "price": 299.0
                }
                """;
        Recipe recipe =
                new ObjectMapper()
                        .readValue(
                                json,
                                Recipe.class
                        );
        assertThat(recipe.getId()).isEqualTo(1);
        assertThat(recipe.getName()).isEqualTo("Margarita");
        assertThat(recipe.getPrice()).isEqualTo(299.0);
    }

    @Test
    @SneakyThrows
    void deserializeListViaTypeReference() {
        String json = """
                [
                  {
                    "id": 1,
                    "name": "Margarita",
                    "price": 299.0
                  },
                  {
                    "id": 2,
                    "name": "Pepperoni",
                    "price": 349.0
                  },
                  {
                    "id": 3,
                    "name": "Four Cheese",
                    "price": 399.0
                  }
                ]
                """;
        List<Recipe> recipes =
                new ObjectMapper()
                        .readValue(
                                json,
                                new TypeReference<List<Recipe>>() {
                                }
                        );
        assertThat(recipes).hasSize(3);
        assertThat(recipes)
                .extracting(Recipe::getName)
                .containsExactly(
                        "Margarita",
                        "Pepperoni",
                        "Four Cheese"
                );
    }
}


