package org.ignis.executor.core.io;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;
import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


interface IWriter {
    void writeBoolean(TProtocol protocol, boolean obj) throws TException;
    void writeByte(TProtocol protocol, byte obj) throws TException;
    void writeShort(TProtocol protocol, short obj) throws TException;
    void writeInt(TProtocol protocol, int obj) throws TException;
    void writeLong(TProtocol protocol, long obj) throws TException;
    void writeDouble(TProtocol protocol, double obj) throws TException;
    void writeString(TProtocol protocol, String obj) throws TException;
    <T> void writeList(TProtocol protocol, List<T> obj) throws TException;
    <T> void writeSet(TProtocol protocol, Set<T> obj) throws TException;
    <K, V> void writeMap(TProtocol protocol, Map<K,V> obj) throws TException;
    <K, V> void writePair(TProtocol protocol, AbstractMap.SimpleEntry<K, V> obj) throws TException;
    void writeBinary(TProtocol protocol, byte[] obj) throws TException;
    <K,V,T extends Map.Entry<K,V>> void writePairList(TProtocol protocol, List<T> obj) throws TException;
    void writeJSON(TProtocol protocol, JSONObject obj) throws TException;
}
