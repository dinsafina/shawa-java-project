package com.shawarmashop.tests.partners.meat_soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "placeMeatOrderResponse", namespace = "http://shawarmashop.com/partners/meat")
@XmlAccessorType(XmlAccessType.FIELD)
public class MeatOrderResponse {

    @XmlElement(namespace = "http://shawarmashop.com/partners/meat")
    private String invoiceNumber;
    @XmlElement(namespace = "http://shawarmashop.com/partners/meat")
    private String totalCost;
    @XmlElement(namespace = "http://shawarmashop.com/partners/meat")
    private String eta;
    @XmlElement(namespace = "http://shawarmashop.com/partners/meat")
    private String status;
    @XmlElement(namespace = "http://shawarmashop.com/partners/meat")
    private String warehouseCode;
    @XmlElement(namespace = "http://shawarmashop.com/partners/meat")
    private String contactPhone;

    /**Билдит готовый объект для успешного ответа*/
    public static MeatOrderResponse accepted() {
        return MeatOrderResponse.builder()
                .invoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 8))
                .totalCost("1500.0")
                .eta(LocalDate.now().plusDays(1).atStartOfDay() + "Z")
                .status("ACCEPTED")
                .warehouseCode("WH-EAST")
                .contactPhone("+799999999")
                .build();
    }

    /**Возвращает текущий экземпляр класса, но с заменой статуса*/
    public MeatOrderResponse withStatus(String status) {
        return MeatOrderResponse.builder()
                .invoiceNumber(invoiceNumber)
                .totalCost(totalCost)
                .eta(eta)
                .status(status)
                .warehouseCode(warehouseCode)
                .contactPhone(contactPhone)
                .build();
    }
}
