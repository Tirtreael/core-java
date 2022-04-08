package org.ignis.executor.core.storage;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TMemoryBuffer;
import org.apache.thrift.transport.TTransport;
import org.ignis.executor.core.IElements;
import org.ignis.executor.core.protocol.IObjectProtocol;
import org.ignis.executor.core.transport.IZlibTransport;
import org.json.JSONObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.NotSerializableException;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IMemoryPartitionTest extends IElements{

//    @BeforeAll
//    void exampleData(){
//    }


    @ParameterizedTest
    @MethodSource({"createBoolean", "createByte", "createShort", "createInteger", "createLong", "createDouble",
            "createString", "createList", "createSet", "createMap", "createPair", "createBinary",
            "createPairList", "createJson"})
    void readWriteNativ(Object method) {
        int size = 1;
        boolean rNativ = false;
        IMemoryPartition partition1 = new IMemoryPartition(10);

        try {

            this.read(method, partition1, rNativ);

            assertEquals(partition1.size(), 1);
            if (method instanceof JSONObject && partition1.getElements().get(0) instanceof JSONObject)
                assertEquals(method.toString(), partition1.getElements().get(0).toString());
            else if (method instanceof byte[] && partition1.getElements().get(0) instanceof byte[]) {
                assertEquals(((byte[]) method).length, ((byte[]) partition1.getElements().get(0)).length);
                for (int i = 0; i < ((byte[]) method).length; i++)
                    assertEquals(((byte[]) method)[i], ((byte[]) partition1.getElements().get(0))[i]);
            }
            else assertEquals(method, partition1.getElements().get(0));

        } catch (TException | NotSerializableException e) {
            e.printStackTrace();
            assert false;
        }
    }

/*

    @Test
    void iterator() {
    }
*/


    /*
    UTILITY METHODS
     */
    /*
        Read from protocol to partitions
     */
    void read(Object elements, IMemoryPartition partition, boolean nativ) throws TException, NotSerializableException {
        TTransport memoryBuffer = new TMemoryBuffer(4096);
        TTransport zlib = new IZlibTransport(memoryBuffer);
        IObjectProtocol proto = new IObjectProtocol(zlib);

        proto.writeObject(elements, nativ, true);
        zlib.flush();
        partition.read(memoryBuffer);
    }

    /*Object write(IMemoryPartition partition, boolean nativ) throws TException, NotSerializableException {
        TTransport memoryBuffer = new TMemoryBuffer(4096);
        partition.write(memoryBuffer, 0, nativ);
        TTransport zlib = new IZlibTransport(memoryBuffer);
        IObjectProtocol proto = new IObjectProtocol(zlib);
        return proto.readObject();
    }*/

/*

    @Test
    void testClone() {
    }

    @Test
    void testWrite() {
    }

    @Test
    void testWrite1() {
    }

    @Test
    void readIterator() {
    }

    @Test
    void writeIterator() {
    }

    @Test
    void copyFrom() {
    }

    @Test
    void moveFrom() {
    }

    @Test
    void size() {
    }

    @Test
    void toBytes() {
    }

    @Test
    void clear() {
    }

    @Test
    void fit() {
    }

    @Test
    void type() {
    }

*/

}