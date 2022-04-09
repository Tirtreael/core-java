package org.ignis.executor.core;

import org.json.JSONObject;

import java.util.*;
import java.util.stream.Stream;

public class IElements {

    public static List<Object> createBoolean(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for(int i=0; i<n; i++) {
            list.add(random.nextBoolean());
        }
        return list;
    }

    public static List<Object> createByte(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for(int i=0; i<n; i++) {
            list.add(random.nextInt(16));
        }
        return list;
    }

    public static List<Object> createShort(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for(int i=0; i<n; i++) {
            list.add(random.nextInt(128));
        }
        return list;
    }

    public static List<Object> createInteger(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for(int i=0; i<n; i++) {
            list.add(random.nextInt());
        }
        return list;
    }

    public static List<Object> createLong(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for(int i=0; i<n; i++) {
            list.add(random.nextLong());
        }
        return list;
    }

    public static List<Object> createDouble(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for(int i=0; i<n; i++) {
            list.add(random.nextDouble());
        }
        return list;
    }

    public static List<Object> createString(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for(int i=0; i<n; i++) {
            list.add(random.ints().toString());
        }
        return list;
    }

    public static List<Object> createList(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for(int i=0; i<n; i++) {
            list.add(List.of(random.nextDouble(), random.nextDouble(), random.nextDouble()));
        }
        return list;
    }

    public static List<Object> createSet(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for(int i=0; i<n; i++) {
            list.add(Set.of(random.nextDouble(), random.nextDouble(), random.nextDouble()));
        }
        return list;
    }

    public static List<Object> createMap(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for(int i=0; i<n; i++) {
            list.add(Map.of(random.nextInt(), random.nextDouble(), random.nextInt(), random.nextDouble(),
                    random.nextInt(), random.nextDouble()));
        }
        return list;
    }

    public static List<Object> createPair(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for(int i=0; i<n; i++) {
            list.add(new AbstractMap.SimpleEntry<>(random.nextInt(), random.nextDouble()));
        }
        return list;
    }

    public static List<Object> createBinary(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for(int i=0; i<n; i++) {
            list.add(new byte[]{
                    (byte) random.nextInt(16), (byte) random.nextInt(16),
                    (byte) random.nextInt(16), (byte) random.nextInt(16)
            });
        }
        return list;
    }

    public static List<Object> createPairList(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for(int i=0; i<n; i++) {
            list.add(List.of(new AbstractMap.SimpleEntry<>(1, "Mateo"),
                    new AbstractMap.SimpleEntry<>(3, "Tomas"), new AbstractMap.SimpleEntry<>(17, "Berto")));
        }
        return list;
    }

    public static List<Object> createJson(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for(int i=0; i<n; i++) {
            list.add(new JSONObject("{ raiz: { hijo1: 4, hijo2: 37 }, raiz2: { hijo1: 4, hijo2: 37 } }"));
        }
        return list;
    }
}
