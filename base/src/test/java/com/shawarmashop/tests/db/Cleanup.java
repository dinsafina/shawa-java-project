package com.shawarmashop.tests.db;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Cleanup {
    private String description;
    private Runnable action;
}
