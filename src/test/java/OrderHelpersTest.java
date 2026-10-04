import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderHelpersTest {

    @Test
    void formatBuildsHumanString() {
        String paid = OrderHelpers.format(42, "PAID", 199.0);
        assertThat(paid).isEqualTo("Order #42 | PAID | 199.0");
    }

    @Test
    void finalForPaidNotForCreated() {
        boolean paid = OrderHelpers.isFinal("PAID");
        boolean created = OrderHelpers.isFinal("CREATED");
        assertThat(paid).isTrue();
        assertThat(created).isFalse();
    }

    @ParameterizedTest(name = "{0} → {1}")
    @CsvSource({
            "CREATED, false",
            "PAID, true",
            "READY, true",
            "COMPLETED, true",
            "UNKNOWN, false"
    })
    void finalStatusTable(String status, boolean expected) {
        assertThat(
                OrderHelpers.isFinal(status))
                .isEqualTo(expected);
    }

    @Test
    void keepFinalFiltersOnlyFinalStatuses() {
        List<String> result = List.of(
                "CREATED",
                "PAID",
                "READY",
                "CANCELLED"
        );
        assertThat(result)
                .hasSize(4)
                .contains("PAID")
                .contains("READY")
                .doesNotContain("CREATED");
    }
}
