package com.base.junit_extension;

import net.datafaker.Faker;
import org.junit.jupiter.api.extension.*;

import java.util.List;
import java.util.stream.IntStream;

public class GarageParamResolver implements ParameterResolver {

    private final static Faker faker = new Faker();

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == UserWithGarage.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Garage annotation = extensionContext.getRequiredTestMethod().getAnnotation(Garage.class);

        if(annotation == null) {
            throw new ExtensionConfigurationException("@Garage аннотация отсутствует");
        }

        UserWithGarage userWithGarage = new UserWithGarage();
        userWithGarage.setUserName(faker.funnyName().name());

        List<Car> cars = IntStream.range(0, annotation.cars())
                .mapToObj(x -> randomCar())
                .toList();

        userWithGarage.setCars(cars);
        return userWithGarage;
    }

    private Car randomCar() {
        Car car = new Car();
        car.setBrand(faker.company().name());
        car.setColor(faker.color().name());
        car.setPrice(faker.number().numberBetween(100, 500));
        return car;
    }
 }
