package com.base.restassured;

import lombok.Data;

@Data
public class JwtResponse {

    private String token;
    private String type;
    private String username;
    private Integer expiresIn;
}
