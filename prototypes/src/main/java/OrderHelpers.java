import java.util.ArrayList;
import java.util.List;

/**
 * Маленький утилитный класс для проверки тестов
 */
public final class OrderHelpers {

    public static String format(long orderId, String status, double total) {
        return "Order #" + orderId + " | " + status + " | " + total;
    }

    public static boolean isFinal(String status) {
        if (status == null) {
            return false;
        }

        return switch (status) {
            case "PAID", "READY", "COMPLETED", "CANCELLED" -> true;
            default -> false;
        };
    }

    public static List<String> keepFinal(List<String> statuses) {
        List<String> result = new ArrayList<>();

        for (String status : statuses) {
            if (isFinal(status)) {
                result.add(status);
            }
        }

        return result;
    }
}