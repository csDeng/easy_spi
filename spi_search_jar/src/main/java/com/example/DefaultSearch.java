package com.example;

import java.util.List;

public class DefaultSearch implements Search{

    @Override
    public List<String> searchDoc(String keyword) {
        System.out.println("default search");
        return null;
    }
}
