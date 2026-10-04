package com.base.junit_extension;

import lombok.Data;

import java.util.List;

@Data
public class UserWithGarage {

    private List<Car> cars;
    private String userName;
}
