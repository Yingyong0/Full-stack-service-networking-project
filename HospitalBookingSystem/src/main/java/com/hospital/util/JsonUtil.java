package com.hospital.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.util.stream.Collectors;

/**
 * JSON工具类
 */
public class JsonUtil {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static String readRequestBody(BufferedReader reader) {
        return reader.lines().collect(Collectors.joining("\n"));
    }
}

