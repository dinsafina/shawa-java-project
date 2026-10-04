package com.generics;

public class Theory {
    public static void main(String[] args) {

        ApiResponse<String> apiResponse1 = new ApiResponse<>(200, "Some value");
        System.out.println(apiResponse1);
        ApiResponse<String> apiResponse1V2 = new ApiResponse<>(200, "Some value");
        System.out.println(apiResponse1.equals(apiResponse1V2));
        
        ApiResponse<Void> apiResponse2 = new ApiResponse<>(500, null);
        System.out.println(apiResponse2);

        ApiResponse<Integer> apiResponse3 = new ApiResponse<>(201, 10);
        System.out.println(apiResponse3);


    }
}
