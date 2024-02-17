/**
 * Autogenerated by Thrift Compiler (0.15.0)
 * <p>
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *
 * @generated
 */
package org.ignis.rpc.driver;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
public class IBackendService {

    public interface Iface {

        public void stop() throws org.apache.thrift.TException;

    }

    public interface AsyncIface {

        public void stop(org.apache.thrift.async.AsyncMethodCallback<Void> resultHandler) throws org.apache.thrift.TException;

    }

    public static class Client extends org.apache.thrift.TServiceClient implements Iface {
        public static class Factory implements org.apache.thrift.TServiceClientFactory<Client> {
            public Factory() {
            }

            public Client getClient(org.apache.thrift.protocol.TProtocol prot) {
                return new Client(prot);
            }

            public Client getClient(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) {
                return new Client(iprot, oprot);
            }
        }

        public Client(org.apache.thrift.protocol.TProtocol prot) {
            super(prot, prot);
        }

        public Client(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) {
            super(iprot, oprot);
        }

        public void stop() throws org.apache.thrift.TException {
            send_stop();
        }

        public void send_stop() throws org.apache.thrift.TException {
            stop_args args = new stop_args();
            sendBaseOneway("stop", args);
        }

    }

    public static class AsyncClient extends org.apache.thrift.async.TAsyncClient implements AsyncIface {
        public static class Factory implements org.apache.thrift.async.TAsyncClientFactory<AsyncClient> {
            private org.apache.thrift.async.TAsyncClientManager clientManager;
            private org.apache.thrift.protocol.TProtocolFactory protocolFactory;

            public Factory(org.apache.thrift.async.TAsyncClientManager clientManager, org.apache.thrift.protocol.TProtocolFactory protocolFactory) {
                this.clientManager = clientManager;
                this.protocolFactory = protocolFactory;
            }

            public AsyncClient getAsyncClient(org.apache.thrift.transport.TNonblockingTransport transport) {
                return new AsyncClient(protocolFactory, clientManager, transport);
            }
        }

        public AsyncClient(org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.async.TAsyncClientManager clientManager, org.apache.thrift.transport.TNonblockingTransport transport) {
            super(protocolFactory, clientManager, transport);
        }

        public void stop(org.apache.thrift.async.AsyncMethodCallback<Void> resultHandler) throws org.apache.thrift.TException {
            checkReady();
            stop_call method_call = new stop_call(resultHandler, this, ___protocolFactory, ___transport);
            this.___currentMethod = method_call;
            ___manager.call(method_call);
        }

        public static class stop_call extends org.apache.thrift.async.TAsyncMethodCall<Void> {
            public stop_call(org.apache.thrift.async.AsyncMethodCallback<Void> resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
                super(client, protocolFactory, transport, resultHandler, true);
            }

            public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
                prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("stop", org.apache.thrift.protocol.TMessageType.ONEWAY, 0));
                stop_args args = new stop_args();
                args.write(prot);
                prot.writeMessageEnd();
            }

            public Void getResult() throws org.apache.thrift.TException {
                if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
                    throw new java.lang.IllegalStateException("Method call not finished!");
                }
                org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
                org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
                return null;
            }
        }

    }

    public static class Processor<I extends Iface> extends org.apache.thrift.TBaseProcessor<I> implements org.apache.thrift.TProcessor {
        private static final org.slf4j.Logger _LOGGER = org.slf4j.LoggerFactory.getLogger(Processor.class.getName());

        public Processor(I iface) {
            super(iface, getProcessMap(new java.util.HashMap<java.lang.String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>>()));
        }

        protected Processor(I iface, java.util.Map<java.lang.String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>> processMap) {
            super(iface, getProcessMap(processMap));
        }

        private static <I extends Iface> java.util.Map<java.lang.String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>> getProcessMap(java.util.Map<java.lang.String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>> processMap) {
            processMap.put("stop", new stop());
            return processMap;
        }

        public static class stop<I extends Iface> extends org.apache.thrift.ProcessFunction<I, stop_args> {
            public stop() {
                super("stop");
            }

            public stop_args getEmptyArgsInstance() {
                return new stop_args();
            }

            protected boolean isOneway() {
                return true;
            }

            @Override
            protected boolean rethrowUnhandledExceptions() {
                return false;
            }

            public org.apache.thrift.TBase getResult(I iface, stop_args args) throws org.apache.thrift.TException {
                iface.stop();
                return null;
            }
        }

    }

    public static class AsyncProcessor<I extends AsyncIface> extends org.apache.thrift.TBaseAsyncProcessor<I> {
        private static final org.slf4j.Logger _LOGGER = org.slf4j.LoggerFactory.getLogger(AsyncProcessor.class.getName());

        public AsyncProcessor(I iface) {
            super(iface, getProcessMap(new java.util.HashMap<java.lang.String, org.apache.thrift.AsyncProcessFunction<I, ? extends org.apache.thrift.TBase, ?>>()));
        }

        protected AsyncProcessor(I iface, java.util.Map<java.lang.String, org.apache.thrift.AsyncProcessFunction<I, ? extends org.apache.thrift.TBase, ?>> processMap) {
            super(iface, getProcessMap(processMap));
        }

        private static <I extends AsyncIface> java.util.Map<java.lang.String, org.apache.thrift.AsyncProcessFunction<I, ? extends org.apache.thrift.TBase, ?>> getProcessMap(java.util.Map<java.lang.String, org.apache.thrift.AsyncProcessFunction<I, ? extends org.apache.thrift.TBase, ?>> processMap) {
            processMap.put("stop", new stop());
            return processMap;
        }

        public static class stop<I extends AsyncIface> extends org.apache.thrift.AsyncProcessFunction<I, stop_args, Void> {
            public stop() {
                super("stop");
            }

            public stop_args getEmptyArgsInstance() {
                return new stop_args();
            }

            public org.apache.thrift.async.AsyncMethodCallback<Void> getResultHandler(final org.apache.thrift.server.AbstractNonblockingServer.AsyncFrameBuffer fb, final int seqid) {
                final org.apache.thrift.AsyncProcessFunction fcall = this;
                return new org.apache.thrift.async.AsyncMethodCallback<Void>() {
                    public void onComplete(Void o) {
                    }

                    public void onError(java.lang.Exception e) {
                        if (e instanceof org.apache.thrift.transport.TTransportException) {
                            _LOGGER.error("TTransportException inside handler", e);
                            fb.close();
                        } else {
                            _LOGGER.error("Exception inside oneway handler", e);
                        }
                    }
                };
            }

            protected boolean isOneway() {
                return true;
            }

            public void start(I iface, stop_args args, org.apache.thrift.async.AsyncMethodCallback<Void> resultHandler) throws org.apache.thrift.TException {
                iface.stop(resultHandler);
            }
        }

    }

    public static class stop_args implements org.apache.thrift.TBase<stop_args, stop_args._Fields>, java.io.Serializable, Cloneable, Comparable<stop_args> {
        private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("stop_args");


        private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new stop_argsStandardSchemeFactory();
        private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new stop_argsTupleSchemeFactory();


        /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
        public enum _Fields implements org.apache.thrift.TFieldIdEnum {
            ;

            private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

            static {
                for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
                    byName.put(field.getFieldName(), field);
                }
            }

            /**
             * Find the _Fields constant that matches fieldId, or null if its not found.
             */
            @org.apache.thrift.annotation.Nullable
            public static _Fields findByThriftId(int fieldId) {
                switch (fieldId) {
                    default:
                        return null;
                }
            }

            /**
             * Find the _Fields constant that matches fieldId, throwing an exception
             * if it is not found.
             */
            public static _Fields findByThriftIdOrThrow(int fieldId) {
                _Fields fields = findByThriftId(fieldId);
                if (fields == null)
                    throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
                return fields;
            }

            /**
             * Find the _Fields constant that matches name, or null if its not found.
             */
            @org.apache.thrift.annotation.Nullable
            public static _Fields findByName(java.lang.String name) {
                return byName.get(name);
            }

            private final short _thriftId;
            private final java.lang.String _fieldName;

            _Fields(short thriftId, java.lang.String fieldName) {
                _thriftId = thriftId;
                _fieldName = fieldName;
            }

            public short getThriftFieldId() {
                return _thriftId;
            }

            public java.lang.String getFieldName() {
                return _fieldName;
            }
        }

        public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;

        static {
            java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
            metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
            org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(stop_args.class, metaDataMap);
        }

        public stop_args() {
        }

        /**
         * Performs a deep copy on <i>other</i>.
         */
        public stop_args(stop_args other) {
        }

        public stop_args deepCopy() {
            return new stop_args(this);
        }

        @Override
        public void clear() {
        }

        public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
            switch (field) {
            }
        }

        @org.apache.thrift.annotation.Nullable
        public java.lang.Object getFieldValue(_Fields field) {
            switch (field) {
            }
            throw new java.lang.IllegalStateException();
        }

        /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
        public boolean isSet(_Fields field) {
            if (field == null) {
                throw new java.lang.IllegalArgumentException();
            }

            switch (field) {
            }
            throw new java.lang.IllegalStateException();
        }

        @Override
        public boolean equals(java.lang.Object that) {
            if (that instanceof stop_args)
                return this.equals((stop_args) that);
            return false;
        }

        public boolean equals(stop_args that) {
            if (that == null)
                return false;
            if (this == that)
                return true;

            return true;
        }

        @Override
        public int hashCode() {
            int hashCode = 1;

            return hashCode;
        }

        @Override
        public int compareTo(stop_args other) {
            if (!getClass().equals(other.getClass())) {
                return getClass().getName().compareTo(other.getClass().getName());
            }

            int lastComparison = 0;

            return 0;
        }

        @org.apache.thrift.annotation.Nullable
        public _Fields fieldForId(int fieldId) {
            return _Fields.findByThriftId(fieldId);
        }

        public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
            scheme(iprot).read(iprot, this);
        }

        public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
            scheme(oprot).write(oprot, this);
        }

        @Override
        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder("stop_args(");
            boolean first = true;

            sb.append(")");
            return sb.toString();
        }

        public void validate() throws org.apache.thrift.TException {
            // check for required fields
            // check for sub-struct validity
        }

        private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
            try {
                write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
            } catch (org.apache.thrift.TException te) {
                throw new java.io.IOException(te);
            }
        }

        private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
            try {
                read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
            } catch (org.apache.thrift.TException te) {
                throw new java.io.IOException(te);
            }
        }

        private static class stop_argsStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
            public stop_argsStandardScheme getScheme() {
                return new stop_argsStandardScheme();
            }
        }

        private static class stop_argsStandardScheme extends org.apache.thrift.scheme.StandardScheme<stop_args> {

            public void read(org.apache.thrift.protocol.TProtocol iprot, stop_args struct) throws org.apache.thrift.TException {
                org.apache.thrift.protocol.TField schemeField;
                iprot.readStructBegin();
                while (true) {
                    schemeField = iprot.readFieldBegin();
                    if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
                        break;
                    }
                    switch (schemeField.id) {
                        default:
                            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
                    }
                    iprot.readFieldEnd();
                }
                iprot.readStructEnd();

                // check for required fields of primitive type, which can't be checked in the validate method
                struct.validate();
            }

            public void write(org.apache.thrift.protocol.TProtocol oprot, stop_args struct) throws org.apache.thrift.TException {
                struct.validate();

                oprot.writeStructBegin(STRUCT_DESC);
                oprot.writeFieldStop();
                oprot.writeStructEnd();
            }

        }

        private static class stop_argsTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
            public stop_argsTupleScheme getScheme() {
                return new stop_argsTupleScheme();
            }
        }

        private static class stop_argsTupleScheme extends org.apache.thrift.scheme.TupleScheme<stop_args> {

            @Override
            public void write(org.apache.thrift.protocol.TProtocol prot, stop_args struct) throws org.apache.thrift.TException {
                org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
            }

            @Override
            public void read(org.apache.thrift.protocol.TProtocol prot, stop_args struct) throws org.apache.thrift.TException {
                org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
            }
        }

        private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
            return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
        }
    }

}