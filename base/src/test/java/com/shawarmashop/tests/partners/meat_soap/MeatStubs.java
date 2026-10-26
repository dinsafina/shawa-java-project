package com.shawarmashop.tests.partners.meat_soap;

import com.shawarmashop.tests.partners.wiremock.StubBuilder;
import com.shawarmashop.tests.partners.wiremock.WireMockAdminClient;
import com.shawarmashop.tests.partners.wiremock.WiremockStubBase;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import static com.shawarmashop.tests.partners.wiremock.StubBuilder.post;

public class MeatStubs extends WiremockStubBase {

    private static final String SOAP_PATH = "/soap/meat";
    private static final String SOAP_NAMESPACE = "http://shawarmashop.com/partners/meat";
    private static final int OVERRIDE_PRIORITY = 1;

    private static final JAXBContext JAXB_CONTEXT = createJaxbContext();

    public MeatStubs(WireMockAdminClient admin) {
        super(admin);
    }

    /** Регистрирует SOAP-ответ со статусом ACCEPTED для любого запроса на поставку мяса. */
    public void respondAccepted(MeatOrderResponse response) {
        admin.addMapping(post(SOAP_PATH)
                .withPriority(OVERRIDE_PRIORITY)
                .willReturnXml(200, marshalToSoap(response.withStatus("ACCEPTED"))));
    }

    /** Регистрирует SOAP-ответ ACCEPTED только для запросов с orderRef, начинающимся на указанный ingredientId. */
    public void respondAcceptedForIngredient(long ingredientId, MeatOrderResponse response) {
        admin.addMapping(mappingForIngredient(ingredientId)
                .withPriority(OVERRIDE_PRIORITY)
                .willReturnXml(200, marshalToSoap(response.withStatus("ACCEPTED"))));
    }

    /** Регистрирует SOAP-ответ с ошибкой 503 для запросов поставки конкретного ингредиента. */
    public void respondUnavailableForIngredient(long ingredientId) {
        admin.addMapping(mappingForIngredient(ingredientId)
                .withPriority(OVERRIDE_PRIORITY)
                .willReturnXml(503, "<error>meat supplier unavailable</error>"));
    }

    /** Возвращает список сырых SOAP-конвертов, отправленных бэкендом для конкретного ингредиента. */
    public List<String> recordedEnvelopesForIngredient(long ingredientId) {
        return findBodies(requestCriteriaForIngredient(ingredientId));
    }

    /** Возвращает ровно один SOAP-конверт для ингредиента, бросая AssertionError, если их 0 или больше 1. */
    public String requireSingleEnvelopeForIngredient(long ingredientId) {
        List<String> envelopes = recordedEnvelopesForIngredient(ingredientId);
        if (envelopes.size() != 1) {
            throw new AssertionError("Ожидался ровно один SOAP-запрос restock для ingredientId="
                    + ingredientId + ", но найдено: " + envelopes.size());
        }
        return envelopes.get(0);
    }

    /** Возвращает список всех сырых SOAP-конвертов, отправленных бэкендом на эндпоинт мяса. */
    public List<String> recordedEnvelopes() {
        return findBodies(Map.of("method", "POST", "url", SOAP_PATH));
    }

    /** Строит StubBuilder с XPath-условием для фильтрации запросов по orderRef конкретного ингредиента. */
    private static StubBuilder mappingForIngredient(long ingredientId) {
        String orderRefPrefix = "restock-" + ingredientId + "-";
        String xpath = "//*[local-name()='orderRef' and starts-with(normalize-space(.), '" + orderRefPrefix + "')]";
        return post(SOAP_PATH).withXpath(xpath);
    }

    /** Формирует Map-критерии для поиска запросов конкретного ингредиента в истории WireMock. */
    private static Map<String, Object> requestCriteriaForIngredient(long ingredientId) {
        return mappingForIngredient(ingredientId).buildRequest();
    }

    /** Извлекает тела запросов из результатов поиска в WireMock по заданным критериям. */
    private List<String> findBodies(Map<String, Object> criteria) {
        return admin.findRequests(criteria).stream()
                .map(request -> request.get("body"))
                .map(body -> body == null ? "" : body.toString())
                .toList();
    }

    /**Превращает объект в SOAP формат*/
    private static String marshalToSoap(MeatOrderResponse response) {
        try {
            Marshaller marshaller = JAXB_CONTEXT.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            StringWriter bodyWriter = new StringWriter();
            marshaller.marshal(response, bodyWriter);
            return """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                                      xmlns:m="%s">
                      <soapenv:Body>
                        %s
                      </soapenv:Body>
                    </soapenv:Envelope>
                    """.formatted(SOAP_NAMESPACE, bodyWriter.toString().trim());
        } catch (JAXBException e) {
            throw new IllegalStateException("Не удалось сформировать SOAP-ответ", e);
        }
    }

    /** Инициализирует JAXB-контекст на основе аннотаций класса MeatOrderResponse (выполняется один раз). */
    private static JAXBContext createJaxbContext() {
        try {
            return JAXBContext.newInstance(MeatOrderResponse.class);
        } catch (JAXBException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
