package com.github.sylvainmaillard.gredis.domain;

import javax.crypto.spec.PSource;

public class Logging {

    public static void log(Object o) {
        System.out.println(o != null ? o.toString(): "null");
    }

    public static void log(Exception o) {
        System.out.println(o != null ? o.toString(): "null");
        if (o != null) {
            o.printStackTrace(System.out);
        }
    }

}
