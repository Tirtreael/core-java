package org.ignis.executor.core;

import org.json.JSONObject;

import java.util.*;
import java.util.stream.Stream;

public class IElements {

    public static Stream<Object> createBoolean() {
        return Stream.of(new Random().nextBoolean());
    }

    public static  Stream<Object> createByte() {
        return Stream.of(new Random().nextInt(16));
    }

    public static  Stream<Object> createShort() {
        return Stream.of(new Random().nextInt(128));
    }

    public static  Stream<Object> createInteger() {
        return Stream.of(new Random().nextInt());
    }

    public static  Stream<Object> createLong() {
        return Stream.of(new Random().nextLong());
    }

    public static  Stream<Object> createDouble() {
        return Stream.of(new Random().nextDouble());
    }

    public static  Stream<Object> createString() {
        return Stream.of(new Random().ints().toString());
    }

    public static  Stream<Object> createList() {
        Random random = new Random();
        return Stream.of(List.of(random.nextDouble(), random.nextDouble(), random.nextDouble()));
    }

    public static  Stream<Object> createSet() {
        Random random = new Random();
        return Stream.of(Set.of(random.nextDouble(), random.nextDouble(), random.nextDouble()));
    }

    public static  Stream<Object> createMap() {
        Random random = new Random();
        return Stream.of(Map.of(random.nextInt(), random.nextDouble(), random.nextInt(), random.nextDouble(),
                random.nextInt(), random.nextDouble()));
    }

    public static  Stream<Object> createPair() {
        Random random = new Random();
        return Stream.of(new AbstractMap.SimpleEntry<>(random.nextInt(), random.nextDouble()));
    }

    public static  Stream<Object> createBinary() {
        Random random = new Random();
        return Stream.of(new byte[]{
                (byte) random.nextInt(16), (byte) random.nextInt(16),
                (byte) random.nextInt(16), (byte) random.nextInt(16)
        });
    }

    public static  Stream<Object> createPairList() {
        List<Map.Entry<Integer, String>> elements = List.of(new AbstractMap.SimpleEntry<>(1, "Mateo"),
                new AbstractMap.SimpleEntry<>(3, "Tomas"), new AbstractMap.SimpleEntry<>(17, "Berto"));
        return Stream.of(elements);
    }

    public static  Stream<Object> createJson() {
        return Stream.of(new JSONObject("{ raiz: { hijo1: 4, hijo2: 37 }, raiz2: { hijo1: 4, hijo2: 37 } }"));
    }
}
