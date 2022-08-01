package org.ignis.executor.core;

import org.ignis.executor.api.Pair;
import org.ignis.executor.api.myJson;

import java.util.*;

public interface IElements {

    int N = 180;
    int SEED = 17;

    static List<Object> createBoolean() {
        return createBoolean(N, SEED);
    }

    static List<Object> createByte() {
        return createByte(N, SEED);
    }

    static List<Object> createShort() {
        return createShort(N, SEED);
    }

    static List<Object> createInteger() {
        return createInteger(N, SEED);
    }

    static List<Object> createLong() {
        return createLong(N, SEED);
    }

    static List<Object> createDouble() {
        return createDouble(N, SEED);
    }

    static List<Object> createString() {
        return createString(N, SEED);
    }

    static List<Object> createList() {
        return createList(N, SEED);
    }

    static List<Object> createSet() {
        return createSet(N, SEED);
    }

    static List<Object> createMap() {
        return createMap(N, SEED);
    }

    static List<Object> createPair() {
        return createPair(N, SEED);
    }

    static List<Object> createBinary() {
        return createBinary(N, SEED);
    }

    static List<Object> createPairList() {
        return createPairList(N, SEED);
    }

    static List<Object> createJson() {
        return createJson(N, SEED);
    }


    static List<Object> createBoolean(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for (int i = 0; i < n; i++) {
            list.add(random.nextBoolean());
        }
        return List.of(list);
    }

    static List<Object> createByte(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for (int i = 0; i < n; i++) {
            list.add(random.nextInt(16));
        }
        return List.of(list);
    }

    static List<Object> createShort(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for (int i = 0; i < n; i++) {
            list.add(random.nextInt(128));
        }
        return List.of(list);
    }

    static List<Object> createInteger(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for (int i = 0; i < n; i++) {
            list.add(random.nextInt());
        }
        return List.of(list);
    }

    static List<Object> createLong(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for (int i = 0; i < n; i++) {
            list.add(random.nextLong());
        }
        return List.of(list);
    }

    static List<Object> createDouble(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for (int i = 0; i < n; i++) {
            list.add(random.nextDouble());
        }
        return List.of(list);
    }

    static List<Object> createString(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for (int i = 0; i < n; i++) {
            list.add(random.ints().toString());
        }
        return List.of(list);
    }

    static List<Object> createList(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for (int i = 0; i < n; i++) {
            list.add(List.of(random.nextDouble(), random.nextDouble(), random.nextDouble()));
        }
        return List.of(list);
    }

    static List<Object> createSet(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for (int i = 0; i < n; i++) {
            list.add(Set.of(random.nextDouble(), random.nextDouble(), random.nextDouble()));
        }
        return List.of(list);
    }

    static List<Object> createMap(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for (int i = 0; i < n; i++) {
            list.add(Map.of(random.nextInt(), random.nextDouble(), random.nextInt(), random.nextDouble(),
                    random.nextInt(), random.nextDouble()));
        }
        return List.of(list);
    }

    static List<Object> createPair(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for (int i = 0; i < n; i++) {
            list.add(new Pair<>(random.nextInt(), random.nextDouble()));
        }
        return List.of(list);
    }

    static List<Object> createBinary(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for (int i = 0; i < n; i++) {
            list.add(new byte[]{
                    (byte) random.nextInt(16), (byte) random.nextInt(16),
                    (byte) random.nextInt(16), (byte) random.nextInt(16)
            });
        }
        return List.of(list);
    }

    static List<Object> createPairList(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for (int i = 0; i < n; i++) {
            list.add(List.of(new Pair<>(1, "Mateo"),
                    new Pair<>(3, "Tomas"), new Pair<>(17, "Berto")));
        }
        return List.of(list);
    }

    static List<Object> createJson(int n, int seed) {
        List<Object> list = new ArrayList<>(n);
        Random random = new Random(seed);
        for (int i = 0; i < n; i++) {
            list.add(new myJson("{ raiz: { hijo1: 4, hijo2: 37 }, raiz2: { hijo1: 4, hijo2: 37 } }"));
        }
        return List.of(list);
    }
}
