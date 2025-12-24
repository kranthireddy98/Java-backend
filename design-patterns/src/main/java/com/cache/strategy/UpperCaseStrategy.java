package com.cache.strategy;

public class UpperCaseStrategy implements TextFormatter{

    @Override
    public String format(String text) {
        return text.toUpperCase();
    }
}
