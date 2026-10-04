package com.base.junit_extension;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Optional;

public class LogTestNameExtension implements BeforeEachCallback, AfterEachCallback {


    @Override
    public void beforeEach(ExtensionContext context)  {
        String displayName = context.getDisplayName();
        System.out.println("Before each название тестового метода: " + displayName);
    }

    @Override
    public void afterEach(ExtensionContext context)  {
        String displayName = context.getDisplayName();
        Optional<Throwable> executionException = context.getExecutionException();
        if(executionException.isPresent()) {
            System.out.println(displayName + " упал неудачно");
        }
     }
}
