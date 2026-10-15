package com.shawarmashop.tests.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.shawarmashop.tests.support.Json;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public abstract class KafkaTopic<T> implements AutoCloseable {

    protected final String topicName;
    protected final Class<T> payloadType;

    private final KafkaConsumer<String, String> consumer;
    private final List<T> recorded = new ArrayList<>();

    /**Инициализирует консьюмер, подписывается на топик
     *  и перематывает оффсет в конец, чтобы читать только новые сообщения теста*/
    protected KafkaTopic(String topicName, Class<T> payloadType, String groupId) {
        this.topicName = topicName;
        this.payloadType = payloadType;
        this.consumer = new KafkaConsumer<>(KafkaConfig.consumerProps(groupId));
        consumer.subscribe(List.of(topicName));
        awaitAssignment(Duration.ofSeconds(15));

        Set<TopicPartition> partitions = consumer.assignment();
        consumer.seekToEnd(partitions);

        // Принудительно инициализируем позиции после seekToEnd.
        for (TopicPartition partition : partitions) {
            long position = consumer.position(partition);
            System.out.printf(
                    "Kafka consumer initialized: topic=%s, partition=%s, position=%s%n", topicName,
                    partition.partition(),
                    position);
        }
    }

    /**Ожидает назначения партиций консьюмеру в течение заданного таймаута,
     *  предотвращая молчаливое зависание тестов.*/
    private void awaitAssignment(Duration timeout) {
        long deadline = System.nanoTime() + timeout.toNanos();

        while (consumer.assignment().isEmpty()
                && System.nanoTime() < deadline) {
            consumer.poll(Duration.ofMillis(100));
        }

        if (consumer.assignment().isEmpty()) {
            throw new IllegalStateException(
                    ("Consumer не получил партиции топика %s за %s. "
                            + "Проверь bootstrap-servers и наличие топика.")
                            .formatted(topicName, timeout)
            );
        }
        System.out.println("Consumer получил assignment для : " + topicName + "\n" + consumer.assignment());
    }


    /**Ожидает и возвращает первое сообщение заданного типа, удовлетворяющее фильтру,
     *  сохраняя все прочитанные сообщения для отладки.*/
    public T awaitMessage(String type, Predicate<T> filter, Duration timeout) {
        long deadline = System.nanoTime() + timeout.toNanos();

        while (System.nanoTime() < deadline) {
            ConsumerRecords<String, String> batch = consumer.poll(Duration.ofMillis(200));
            for (ConsumerRecord<String, String> record : batch) {
                System.out.printf("Kafka record: topic=%s, partition=%s, offset=%s, key=%s, value=%s".formatted(
                        record.topic(),
                        record.partition(),
                        record.offset(),
                        record.key(),
                        record.value()));

                JsonNode envelope = Json.tree(record.value());

                if (!type.equals(envelope.path("type").asText())) {
                    continue;
                }

                T payload = parsePayload(envelope);
                recorded.add(payload);

                if (filter.test(payload)) {
                    System.out.println("Получено " + type + " из " + topicName + " " + payload);
                    return payload;
                }
            }
        }

        throw new AssertionError(
                "За " + timeout
                        + " не пришло сообщение type=" + type
                        + " в топик " + topicName
                        + ". Распарсенные сообщения: " + recorded
        );
    }

    /**Возвращает неизменяемую копию списка всех успешно распарсенных сообщений,
     *  полученных консьюмером за время жизни объекта.*/
    public List<T> recorded() {
        return List.copyOf(recorded);
    }

    /**Корректно закрывает Kafka-консьюмер и освобождает сетевые ресурсы (реализация AutoCloseable)*/
    @Override
    public void close() {
        consumer.close(Duration.ofSeconds(2));
    }

    /**Извлекает поле "payload" из JSON-конверта сообщения
     *  и десериализует его в целевой Java-тип T с помощью Jackson*/
    private T parsePayload(JsonNode envelope) {
        return Json.MAPPER.convertValue(
                envelope.path("payload"),
                payloadType
        );
    }
}
