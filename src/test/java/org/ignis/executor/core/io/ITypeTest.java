package org.ignis.executor.core.io;

import org.json.JSONObject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ITypeTest {

    @Test
    void getIdBoolean() {
        Random random = new Random(12345678);
        Object obj = random.nextBoolean();
        byte id = IType.I_BOOL.id;

        byte id2 = IType.getId(obj);

        assertEquals(id, id2);
    }

    @Test
    void getIdI08() {
        Random random = new Random(12345678);
        Object obj = (byte) random.nextInt(16);
        byte id = IType.I_I08.id;

        byte id2 = IType.getId(obj);

        assertEquals(id, id2);
    }

    @Test
    void getIdI16() {
        Random random = new Random(12345678);
        Object obj = (short) random.nextInt(128);
        byte id = IType.I_I16.id;

        byte id2 = IType.getId(obj);

        assertEquals(id, id2);
    }

    @Test
    void getIdI32() {
        Random random = new Random(12345678);
        Object obj = random.nextInt(16384);
        byte id = IType.I_I32.id;

        byte id2 = IType.getId(obj);

        assertEquals(id, id2);
    }

    @Test
    void getIdI64() {
        Random random = new Random(12345678);
        Object obj = random.nextLong();
        byte id = IType.I_I64.id;

        byte id2 = IType.getId(obj);

        assertEquals(id, id2);
    }

    @Test
    void getIdDouble() {
        Random random = new Random(12345678);
        Object obj = random.nextDouble();
        byte id = IType.I_DOUBLE.id;

        byte id2 = IType.getId(obj);

        assertEquals(id, id2);
    }

    @Test
    void getIdString() {
        Object obj = "testString1";
        byte id = IType.I_STRING.id;

        byte id2 = IType.getId(obj);

        assertEquals(id, id2);
    }

    @Test
    void getIdList() {
        Random random = new Random(12345678);
        Object obj = List.of(random.nextDouble(), random.nextDouble());
        byte id = IType.I_LIST.id;

        byte id2 = IType.getId(obj);

        assertEquals(id, id2);
    }

    @Test
    void getIdSet() {
        Random random = new Random(12345678);
        Object obj = Set.of(random.nextDouble(), random.nextDouble());
        byte id = IType.I_SET.id;

        byte id2 = IType.getId(obj);

        assertEquals(id, id2);
    }

    @Test
    void getIdMap() {
        Random random = new Random(12345678);
        Object obj = Map.of(random.nextDouble(), random.nextDouble(), random.nextDouble(), random.nextDouble());
        byte id = IType.I_MAP.id;

        byte id2 = IType.getId(obj);

        assertEquals(id, id2);
    }

    @Test
    void getIdPair() {
        Random random = new Random(12345678);
        Object obj = new AbstractMap.SimpleEntry<>(random.nextDouble(), random.nextDouble());
        byte id = IType.I_PAIR.id;

        byte id2 = IType.getId(obj);

        assertEquals(id, id2);
    }

    @Test
    void getIdBinary() {
        Object obj = new Byte[]{0x3, 0x7, 0xa, 0x5};
        byte id = IType.I_BINARY.id;

        byte id2 = IType.getId(obj);

        assertEquals(id, id2);
    }

    @Test
    void getIdPairList() {
        Random random = new Random(12345678);
        Object obj = List.of(new AbstractMap.SimpleEntry<>(random.nextDouble(), random.nextDouble()),
                new AbstractMap.SimpleEntry<>(random.nextDouble(), random.nextDouble())
        );
        byte id = IType.I_PAIR_LIST.id;

        byte id2 = IType.getId(obj);

        assertEquals(id, id2);
    }

    @Test
    void getIdJSON() {
        Object obj = new JSONObject();
        byte id = IType.I_JSON.id;

        byte id2 = IType.getId(obj);

        assertEquals(id, id2);
    }


    @Test
    void getIdClazzBoolean() {
        Class<?> obj = Boolean.class;
        byte id = IType.I_BOOL.id;

        byte id2 = IType.getIdClazz(obj);

        assertEquals(id, id2);
    }

    @Test
    void getIdClazzI08() {
        Class<?> obj = Byte.class;
        byte id = IType.I_I08.id;

        byte id2 = IType.getIdClazz(obj);

        assertEquals(id, id2);
    }

    @Test
    void getIdClazzI16() {
        Class<?> obj = Short.class;
        byte id = IType.I_I16.id;

        byte id2 = IType.getIdClazz(obj);

        assertEquals(id, id2);
    }

    @Test
    void getIdClazzI32() {
        Class<?> obj = Integer.class;
        byte id = IType.I_I32.id;

        byte id2 = IType.getIdClazz(obj);

        assertEquals(id, id2);
    }

    @Test
    void getIdClazzI64() {
        Class<?> obj = Long.class;
        byte id = IType.I_I64.id;

        byte id2 = IType.getIdClazz(obj);

        assertEquals(id, id2);
    }

    @Test
    void getIdClazzDouble() {
        Class<?> obj = Double.class;
        byte id = IType.I_DOUBLE.id;

        byte id2 = IType.getIdClazz(obj);

        assertEquals(id, id2);
    }

    @Test
    void getIdClazzString() {
        Class<?> obj = String.class;
        byte id = IType.I_STRING.id;

        byte id2 = IType.getIdClazz(obj);

        assertEquals(id, id2);
    }

    @Test
    void getIdClazzList() {
        Class<?> obj = List.class;
        byte id = IType.I_LIST.id;

        byte id2 = IType.getIdClazz(obj);

        assertEquals(id, id2);
    }

    @Test
    void getIdClazzSet() {
        Class<?> obj = Set.class;
        byte id = IType.I_SET.id;

        byte id2 = IType.getIdClazz(obj);

        assertEquals(id, id2);
    }

    @Test
    void getIdClazzMap() {
        Class<?> obj = Map.class;
        byte id = IType.I_MAP.id;

        byte id2 = IType.getIdClazz(obj);

        assertEquals(id, id2);
    }

    @Test
    void getIdClazzPair() {
        Class<?> obj = Map.Entry.class;
        byte id = IType.I_PAIR.id;

        byte id2 = IType.getIdClazz(obj);

        assertEquals(id, id2);
    }

    @Test
    void getIdClazzBinary() {
        Class<?> obj = IType.I_BINARY.type;
        byte id = IType.I_BINARY.id;

        byte id2 = IType.getIdClazz(obj);

        assertEquals(id, id2);
    }

    @Disabled
    @Test
    void getIdClazzPairList() {
        Class<?> obj = IType.I_PAIR_LIST.type;
        byte id = IType.I_PAIR_LIST.id;

        byte id2 = IType.getIdClazz(obj);

        assertEquals(id, id2);
    }

    @Test
    void getIdClazzJSON() {
        Class<?> obj = IType.I_JSON.type;
        byte id = IType.I_JSON.id;

        byte id2 = IType.getIdClazz(obj);

        assertEquals(id, id2);
    }


}