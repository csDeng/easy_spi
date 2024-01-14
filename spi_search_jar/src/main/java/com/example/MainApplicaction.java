package com.example;

import java.util.Iterator;
import java.util.ServiceLoader;

public class MainApplicaction {

    public static void main(String[] args) {
        ServiceLoader<Search> s = ServiceLoader.load(Search.class);
        Iterator<Search> iterator = s.iterator();
        while (iterator.hasNext()) {
            Search search =  iterator.next();
            search.searchDoc("hello world");
        }
        System.out.println(">>>> jar main end");
    }


}
