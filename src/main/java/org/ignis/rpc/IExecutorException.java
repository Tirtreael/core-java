/**
 * Autogenerated by Thrift Compiler (0.15.0)
 * <p>
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *
 * @generated
 */
package org.ignis.rpc;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
public class IExecutorException extends org.apache.thrift.TException implements org.apache.thrift.TBase<IExecutorException, IExecutorException._Fields>, java.io.Serializable, Cloneable, Comparable<IExecutorException> {
    private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("IExecutorException");

    private static final org.apache.thrift.protocol.TField MESSAGE_FIELD_DESC = new org.apache.thrift.protocol.TField("message", org.apache.thrift.protocol.TType.STRING, (short) 1);
    private static final org.apache.thrift.protocol.TField CAUSE__FIELD_DESC = new org.apache.thrift.protocol.TField("cause_", org.apache.thrift.protocol.TType.STRING, (short) 2);

    private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new IExecutorExceptionStandardSchemeFactory();
    private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new IExecutorExceptionTupleSchemeFactory();

    private @org.apache.thrift.annotation.Nullable java.lang.String message; // required
    private @org.apache.thrift.annotation.Nullable java.lang.String cause_; // required

    /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
    public enum _Fields implements org.apache.thrift.TFieldIdEnum {
        MESSAGE((short) 1, "message"),
        CAUSE_((short) 2, "cause_");

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
                case 1: // MESSAGE
                    return MESSAGE;
                case 2: // CAUSE_
                    return CAUSE_;
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
            if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
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

    // isset id assignments
    public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;

    static {
        java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
        tmpMap.put(_Fields.MESSAGE, new org.apache.thrift.meta_data.FieldMetaData("message", org.apache.thrift.TFieldRequirementType.REQUIRED,
                new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
        tmpMap.put(_Fields.CAUSE_, new org.apache.thrift.meta_data.FieldMetaData("cause_", org.apache.thrift.TFieldRequirementType.REQUIRED,
                new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
        metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
        org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(IExecutorException.class, metaDataMap);
    }

    public IExecutorException() {
    }

    public IExecutorException(
            java.lang.String message,
            java.lang.String cause_) {
        this();
        this.message = message;
        this.cause_ = cause_;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public IExecutorException(IExecutorException other) {
        if (other.isSetMessage()) {
            this.message = other.message;
        }
        if (other.isSetCause_()) {
            this.cause_ = other.cause_;
        }
    }

    public IExecutorException deepCopy() {
        return new IExecutorException(this);
    }

    @Override
    public void clear() {
        this.message = null;
        this.cause_ = null;
    }

    @org.apache.thrift.annotation.Nullable
    public java.lang.String getMessage() {
        return this.message;
    }

    public IExecutorException setMessage(@org.apache.thrift.annotation.Nullable java.lang.String message) {
        this.message = message;
        return this;
    }

    public void unsetMessage() {
        this.message = null;
    }

    /** Returns true if field message is set (has been assigned a value) and false otherwise */
    public boolean isSetMessage() {
        return this.message != null;
    }

    public void setMessageIsSet(boolean value) {
        if (!value) {
            this.message = null;
        }
    }

    @org.apache.thrift.annotation.Nullable
    public java.lang.String getCause_() {
        return this.cause_;
    }

    public IExecutorException setCause_(@org.apache.thrift.annotation.Nullable java.lang.String cause_) {
        this.cause_ = cause_;
        return this;
    }

    public void unsetCause_() {
        this.cause_ = null;
    }

    /** Returns true if field cause_ is set (has been assigned a value) and false otherwise */
    public boolean isSetCause_() {
        return this.cause_ != null;
    }

    public void setCause_IsSet(boolean value) {
        if (!value) {
            this.cause_ = null;
        }
    }

    public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
        switch (field) {
            case MESSAGE:
                if (value == null) {
                    unsetMessage();
                } else {
                    setMessage((java.lang.String) value);
                }
                break;

            case CAUSE_:
                if (value == null) {
                    unsetCause_();
                } else {
                    setCause_((java.lang.String) value);
                }
                break;

        }
    }

    @org.apache.thrift.annotation.Nullable
    public java.lang.Object getFieldValue(_Fields field) {
        switch (field) {
            case MESSAGE:
                return getMessage();

            case CAUSE_:
                return getCause_();

        }
        throw new java.lang.IllegalStateException();
    }

    /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
    public boolean isSet(_Fields field) {
        if (field == null) {
            throw new java.lang.IllegalArgumentException();
        }

        switch (field) {
            case MESSAGE:
                return isSetMessage();
            case CAUSE_:
                return isSetCause_();
        }
        throw new java.lang.IllegalStateException();
    }

    @Override
    public boolean equals(java.lang.Object that) {
        if (that instanceof IExecutorException)
            return this.equals((IExecutorException) that);
        return false;
    }

    public boolean equals(IExecutorException that) {
        if (that == null)
            return false;
        if (this == that)
            return true;

        boolean this_present_message = true && this.isSetMessage();
        boolean that_present_message = true && that.isSetMessage();
        if (this_present_message || that_present_message) {
            if (!(this_present_message && that_present_message))
                return false;
            if (!this.message.equals(that.message))
                return false;
        }

        boolean this_present_cause_ = true && this.isSetCause_();
        boolean that_present_cause_ = true && that.isSetCause_();
        if (this_present_cause_ || that_present_cause_) {
            if (!(this_present_cause_ && that_present_cause_))
                return false;
            if (!this.cause_.equals(that.cause_))
                return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;

        hashCode = hashCode * 8191 + ((isSetMessage()) ? 131071 : 524287);
        if (isSetMessage())
            hashCode = hashCode * 8191 + message.hashCode();

        hashCode = hashCode * 8191 + ((isSetCause_()) ? 131071 : 524287);
        if (isSetCause_())
            hashCode = hashCode * 8191 + cause_.hashCode();

        return hashCode;
    }

    @Override
    public int compareTo(IExecutorException other) {
        if (!getClass().equals(other.getClass())) {
            return getClass().getName().compareTo(other.getClass().getName());
        }

        int lastComparison = 0;

        lastComparison = java.lang.Boolean.compare(isSetMessage(), other.isSetMessage());
        if (lastComparison != 0) {
            return lastComparison;
        }
        if (isSetMessage()) {
            lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.message, other.message);
            if (lastComparison != 0) {
                return lastComparison;
            }
        }
        lastComparison = java.lang.Boolean.compare(isSetCause_(), other.isSetCause_());
        if (lastComparison != 0) {
            return lastComparison;
        }
        if (isSetCause_()) {
            lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.cause_, other.cause_);
            if (lastComparison != 0) {
                return lastComparison;
            }
        }
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
        java.lang.StringBuilder sb = new java.lang.StringBuilder("IExecutorException(");
        boolean first = true;

        sb.append("message:");
        if (this.message == null) {
            sb.append("null");
        } else {
            sb.append(this.message);
        }
        first = false;
        if (!first) sb.append(", ");
        sb.append("cause_:");
        if (this.cause_ == null) {
            sb.append("null");
        } else {
            sb.append(this.cause_);
        }
        first = false;
        sb.append(")");
        return sb.toString();
    }

    public void validate() throws org.apache.thrift.TException {
        // check for required fields
        if (message == null) {
            throw new org.apache.thrift.protocol.TProtocolException("Required field 'message' was not present! Struct: " + toString());
        }
        if (cause_ == null) {
            throw new org.apache.thrift.protocol.TProtocolException("Required field 'cause_' was not present! Struct: " + toString());
        }
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

    private static class IExecutorExceptionStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
        public IExecutorExceptionStandardScheme getScheme() {
            return new IExecutorExceptionStandardScheme();
        }
    }

    private static class IExecutorExceptionStandardScheme extends org.apache.thrift.scheme.StandardScheme<IExecutorException> {

        public void read(org.apache.thrift.protocol.TProtocol iprot, IExecutorException struct) throws org.apache.thrift.TException {
            org.apache.thrift.protocol.TField schemeField;
            iprot.readStructBegin();
            while (true) {
                schemeField = iprot.readFieldBegin();
                if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
                    break;
                }
                switch (schemeField.id) {
                    case 1: // MESSAGE
                        if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
                            struct.message = iprot.readString();
                            struct.setMessageIsSet(true);
                        } else {
                            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
                        }
                        break;
                    case 2: // CAUSE_
                        if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
                            struct.cause_ = iprot.readString();
                            struct.setCause_IsSet(true);
                        } else {
                            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
                        }
                        break;
                    default:
                        org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
                }
                iprot.readFieldEnd();
            }
            iprot.readStructEnd();

            // check for required fields of primitive type, which can't be checked in the validate method
            struct.validate();
        }

        public void write(org.apache.thrift.protocol.TProtocol oprot, IExecutorException struct) throws org.apache.thrift.TException {
            struct.validate();

            oprot.writeStructBegin(STRUCT_DESC);
            if (struct.message != null) {
                oprot.writeFieldBegin(MESSAGE_FIELD_DESC);
                oprot.writeString(struct.message);
                oprot.writeFieldEnd();
            }
            if (struct.cause_ != null) {
                oprot.writeFieldBegin(CAUSE__FIELD_DESC);
                oprot.writeString(struct.cause_);
                oprot.writeFieldEnd();
            }
            oprot.writeFieldStop();
            oprot.writeStructEnd();
        }

    }

    private static class IExecutorExceptionTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
        public IExecutorExceptionTupleScheme getScheme() {
            return new IExecutorExceptionTupleScheme();
        }
    }

    private static class IExecutorExceptionTupleScheme extends org.apache.thrift.scheme.TupleScheme<IExecutorException> {

        @Override
        public void write(org.apache.thrift.protocol.TProtocol prot, IExecutorException struct) throws org.apache.thrift.TException {
            org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
            oprot.writeString(struct.message);
            oprot.writeString(struct.cause_);
        }

        @Override
        public void read(org.apache.thrift.protocol.TProtocol prot, IExecutorException struct) throws org.apache.thrift.TException {
            org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
            struct.message = iprot.readString();
            struct.setMessageIsSet(true);
            struct.cause_ = iprot.readString();
            struct.setCause_IsSet(true);
        }
    }

    private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
        return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
    }
}

