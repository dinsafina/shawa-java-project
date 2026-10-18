package com.shawarmashop.tests.partners;

import com.shawarmashop.tests.env.TestEnvironment;
import com.shawarmashop.tests.partners.bread_grpc.BreadStubs;
import com.shawarmashop.tests.partners.meat_soap.MeatStubs;
import com.shawarmashop.tests.partners.payments.PaymentsStubs;
import com.shawarmashop.tests.partners.reviews.ReviewsStubs;
import com.shawarmashop.tests.partners.wiremock.WireMockAdminClient;


public final class PartnerStubs {

    private final WireMockAdminClient restAdmin;
    private final WireMockAdminClient grpcAdmin;


    public PartnerStubs() {
        restAdmin = new WireMockAdminClient(TestEnvironment.INSTANCE.wiremockRestBaseUrl());
        grpcAdmin = new WireMockAdminClient(TestEnvironment.INSTANCE.wiremockRestBaseUrl());
    }

    /** Сбрасывает оба WireMock-контейнера к базовым правилам и очищает историю запросов. */
    public void resetToDefaults(){
        restAdmin.resetToDefaults();
        grpcAdmin.resetToDefaults();
        restAdmin.clearRecorded();
        grpcAdmin.clearRecorded();
    }

    /** Возвращает доменный стаб для мокинга платежного шлюза (REST). */
    public PaymentsStubs payments(){
        return new PaymentsStubs(restAdmin);
    }

    public ReviewsStubs reviews() {
        return new ReviewsStubs(restAdmin);
    }

    public MeatStubs meat(){
        return new MeatStubs(restAdmin);
    }

    public BreadStubs bread(){
        return new BreadStubs(grpcAdmin);
    }
}
