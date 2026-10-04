package com.base.owner;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.Test;

public class OwnerExamplesTest {

   private final TestConfig config = ConfigFactory.create(TestConfig.class);

   @Test
   public void test1() {
       System.out.println(config.baseUrl());
       System.out.println(config.browser());
   }
}
