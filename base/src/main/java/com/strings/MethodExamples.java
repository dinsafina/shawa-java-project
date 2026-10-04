package com.strings;

public class MethodExamples {
    public static void main(String[] args) {
        String googleCode = "GOOGLE-4813";
        String onlyNumbers = googleCode.substring(7);
        System.out.println(onlyNumbers);
        int numbersToInt = Integer.parseInt(onlyNumbers);
        System.out.println(numbersToInt + 10);
        String replaceWithoutNums = googleCode.replaceAll("\\d+", "");
        System.out.println(replaceWithoutNums);

        googleCode = "";
        System.out.println(googleCode.isEmpty());

        String text = """
                Hello
                World
                Java
                """;
        System.out.println(text);


    }
}
