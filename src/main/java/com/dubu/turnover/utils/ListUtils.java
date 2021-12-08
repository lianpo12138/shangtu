package com.dubu.turnover.utils;

import java.util.Collection;
import java.util.Map;

public class ListUtils {

    public static int size(Object o){
        if(o==null){
            return 0;
        }

        if(o instanceof Map){
            return ((Map) o).size();
        }

        if(o instanceof Collection){
            return ((Collection) o).size();
        }

        return 0;
    }

}
