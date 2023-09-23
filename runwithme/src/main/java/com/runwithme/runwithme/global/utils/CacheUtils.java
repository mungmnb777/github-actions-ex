package com.runwithme.runwithme.global.utils;

import com.runwithme.runwithme.global.entity.Image;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheUtils {

    private static final Map<String, Image> imageCache = new ConcurrentHashMap<>();

    public static Image get(String key) {
        return imageCache.get(key);
    }

    public static void put(String key, Image image) {
        imageCache.put(key, image);
    }
}
