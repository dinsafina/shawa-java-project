# 🛡️ ShawarmaShop Automated Testing Framework

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![JUnit 5](https://img.shields.io/badge/JUnit-5.9.x-green.svg)](https://junit.org/junit5/)
[![Allure](https://img.shields.io/badge/Allure-Report-009688.svg)](https://docs.qameta.io/allure/)
[![Selenide](https://img.shields.io/badge/UI-Selenide-blue.svg)](https://selenide.org/)
[![License](https://img.shields.io/badge/License-Proprietary-red.svg)]()

Высоконадежный фреймворк для сквозного (E2E) и интеграционного тестирования распределенной системы ShawarmaShop. Обеспечивает детерминированную проверку бизнес-сценариев, охватывающих REST API, асинхронные события (Kafka), прямую валидацию состояния БД и UI-интеракции.

## 🏗 Архитектура фреймворка

Фреймворк построен по принципу многоуровневой абстракции, что обеспечивает изоляцию тестов от изменений в инфраструктуре и высокую скорость выполнения.

```text
[ E2E / Integration Tests ]  <- Бизнес-сценарии (Given-When-Then)
          │
          ├── [ API Layer ]      <- Типизированные доменные клиенты (RestAssured + Lazy JWT)
          ├── [ UI Layer ]       <- Page Object Model + Selenide Proxy Interception
          ├── [ DB Layer ]       <- Прямой JDBC без ORM, LIFO-очистка фикстур (TestDbScope)
          ├── [ Kafka Layer ]    <- Early-poll консьюмеры с парсингом Envelope-сообщений
          └── [ Partners Layer ] <- WireMock фасады для REST, gRPC, SOAP (JAXB) и GraphQL
          │
[ Infrastructure ]           <- HikariCP, Testcontainers, Allure Extensions
```

## 🚀 Ключевые особенности

* **Мультипротокольное мокирование:** Единый фасад (`PartnerStubs`) для управления заглушками внешних интеграций: REST (Payments), gRPC (Bread Bakery), SOAP (Meat Supplier через JAXB) и GraphQL (Reviews).
* **Детерминированная работа с БД:** Собственная легковесная JDBC-обертка с `RowMapper`. Гарантированная очистка данных после тестов в обратном порядке (LIFO) для соблюдения Foreign Key ограничений без использования тяжелых миграционных инструментов в тестах.
* **Асинхронная валидация (Kafka):** Паттерн `KafkaTestBus` с автоматическим `seekToEnd` и ранним `poll()`. Исключает гонки данных и потерю сообщений при параллельном запуске.
* **Гибридные UI-проверки:** Механизм `interceptResponse` позволяет перехватывать сетевые ответы бэкенда во время UI-взаимодействия (через Selenide Proxy), получая доступ к сгенерированным ID сущностей для последующих проверок.
* **Zero-Flake дизайн:** Полное отсутствие `Thread.sleep()`. Используются встроенные механизмы polling (Selenide `shouldBe`, Awaitility) и строгие таймауты.
* **Быстрая авторизация:** Инъекция JWT-токенов напрямую в `localStorage` браузера или заголовки API-клиентов, что исключает необходимость прохождения UI-формы логина в каждом тесте.

## 🛠 Технологический стек

| Категория | Технологии |
| :--- | :--- |
| **Язык и сборка** | Java 17+, Maven / Gradle |
| **Тест-раннер** | JUnit 5 (Jupiter), Custom Extensions |
| **API Тестирование** | REST Assured, Jackson (Custom `ApiResult` wrapper) |
| **UI Тестирование** | Selenide, Page Object & Component Pattern (CRTP) |
| **База данных** | HikariCP, Pure JDBC, JUnit `TestDbScope` |
| **Очереди сообщений** | Apache Kafka (Custom `KafkaTopic` polling wrapper) |
| **Мокирование** | WireMock (Admin API), JAXB (для SOAP) |
| **Отчетность** | Allure Framework (Screenshots, Network logs, DB state on failure) |

## 📂 Структура проекта

```text
src/test/java/com/shawarmashop/tests/
 ├── api/                # Доменные REST-клиенты и ApiResult wrapper
 ├── db/                 # Jdbc фасад, RowMapper, Репозитории и Фикстуры с авто-очисткой
 ├── kafka/              # KafkaTestBus и доменные обертки для ожидания событий
 ├── partners/           # WireMock-стабы для внешних интеграций (payments, bread, meat, reviews)
 ├── ui/                 # Selenide Page Objects, Components и Proxy-перехватчики
 ├── junit/              # Кастомные JUnit 5 Extensions (Lifecycle, Allure, DbScope)
 └── e2e_tests/          # Сценарии сквозного тестирования (Showcase)
```

## ⚙️ Начало работы

### Предварительные требования
* JDK 17 или выше
* Docker & Docker Compose (для поднятия тестовых зависимостей: PostgreSQL, Kafka, WireMock)
* Доступ к реестру артефактов (если применимо)

### Локальный запуск зависимостей
```bash
docker-compose -f docker-compose.test.yml up -d
```

### Выполнение тестов

Запуск полного набора E2E тестов:
```bash
mvn clean verify -DskipUi=false
```

Запуск только API и интеграционных тестов (быстрый фидбек):
```bash
mvn test -Dgroups="api,integration"
```

Запуск с генерацией Allure-отчета:
```bash
mvn clean test allure:serve
```

## 📜 Руководство по разработке тестов (Best Practices)

1. **Используйте доменные фасады:** Никогда не вызывайте `given().when().then()` или сырой JDBC напрямую в теле теста. Используйте `ApiClient`, `XxxRepository` или `PartnerStubs`.
2. **Очистка данных:** Любая запись в БД, созданная в тесте, должна проходить через `XxxFixture.builder().build().insert()`. Это автоматически регистрирует сущность в `TestDbScope` для гарантированного удаления.
3. **Проверка асинхронщины:** Для проверки фоновых процессов используйте `orderDb.awaitStatus(...)` или `kafka.orders().waitPlaced(...)`. Избегайте статических задержек.
4. **Селекторы в UI:** Используйте стабильные атрибуты (`data-testid`, классы дизайн-системы `.arcade-*`). Запрещено привязываться к тексту, который может быть локализован (i18n).
5. **Идемпотентность:** Сценарии с платежами обязательно должны покрывать тесты на повторную отправку запроса с тем же `Idempotency-Key`.

## 📊 Отчетность

В случае падения теста, кастомный `AllureExtension` автоматически прикрепляет к шагу:
* Скриншот состояния браузера (для UI тестов).
* Логи консоли браузера и Network Performance Entries.
* Снимок `localStorage` и `Cookies`.
* Тело последнего неудачного HTTP-запроса/ответа.