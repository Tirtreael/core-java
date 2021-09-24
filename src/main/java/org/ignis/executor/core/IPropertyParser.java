package org.ignis.executor.core;

import java.security.InvalidKeyException;
import java.util.Properties;
import java.util.regex.Pattern;

public class IPropertyParser {

    private final Properties properties;
    private final Pattern bool = Pattern.compile("y|Y|yes|Yes|YES|true|True|TRUE|on|On|ON");

    public IPropertyParser() {
        this.properties = new Properties();
    }

    public IPropertyParser(Properties properties) {
        this.properties = properties;
    }

    public int cores() {
        return getNumber("ignis.executor.cores");
    }

    public int transportCores() {
        return getMinNumber("ignis.transport.cores", 0);
    }

    public long partitionMinimal() {
        try {
            return getSize("ignis.partition.minimal");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public float sortSamples() {
        return getMinFloat("ignis.modules.sort.samples", 0);
    }

    public boolean sortResampling() {
        try {
            return getBoolean("ignis.modules.sort.resampling");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean loadType() {
        try {
            return getBoolean("ignis.modules.load.type");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isOverwrite() {
        try {
            return getBoolean("ignis.modules.io.overwrite");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int isCompression() {
        return getRangeNumber("ignis.modules.io.compression", 0, 9);
    }

    public int msgCompression() {
        return getRangeNumber("ignis.transport.compression", 0, 9);
    }

    public int partitionCompression() {
        return getRangeNumber("ignis.partition.compression", 0, 9);
    }

    public boolean nativeSerialization() {
        try {
            return getString("ignis.partition.serialization").equals("native");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String partitionType() {
        try {
            return getString("ignis.partition.type");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String exchangeType() {
        try {
            return getString("ignis.modules.exchange.type");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String jobName() {
        try {
            return getString("ignis.job.name");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String jobDirectory() {
        try {
            return getString("ignis.job.directory");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String executorDirectory() {
        try {
            return getString("ignis.executor.directory");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }



    /* Auxiliary methods */

    // @Todo KeyException
    public String getString(String key) throws InvalidKeyException {
        if (properties.containsKey(key))
            return this.properties.getProperty(key);
        else throw new InvalidKeyException(key + " is empty");
    }

    public int getNumber(String key) {
        return Integer.parseInt(this.properties.getProperty(key));
    }

    public float getFloat(String key) {
        return Float.parseFloat(this.properties.getProperty(key));
    }

    public int getRangeNumber(String key, int min, int max) throws IllegalArgumentException {
        int value = this.getNumber(key);
        if (value < min)
            throw new IllegalArgumentException(key + " error " + value + " is less than " + value);
        if (value > max)
            throw new IllegalArgumentException(key + " error " + value + " is greater than " + value);
        return value;
    }

    public int getMaxNumber(String key, int max) {
        return this.getRangeNumber(key, Integer.MIN_VALUE, max);
    }

    public int getMinNumber(String key, int min) {
        return this.getRangeNumber(key, min, Integer.MAX_VALUE);
    }

    public float getRangeFloat(String key, float min, float max) throws IllegalArgumentException {
        float value = this.getFloat(key);
        if (value < min)
            throw new IllegalArgumentException(key + " error " + value + " is less than " + value);
        if (value > max)
            throw new IllegalArgumentException(key + " error " + value + " is greater than " + value);
        return value;
    }

    public float getMaxFloat(String key, float max) {
        return this.getRangeFloat(key, Float.MIN_VALUE, max);
    }

    public float getMinFloat(String key, float min) {
        return this.getRangeFloat(key, min, Float.MAX_VALUE);
    }

    public void parseError(String key, String value, int pos) throws IllegalArgumentException {
        throw new IllegalArgumentException(key + " parsing error " + value.charAt(pos) + "(" + pos + 1 + ") in " + value);
    }

    public long getSize(String key) throws InvalidKeyException {
        String value = this.getString(key).strip();
        String UNITS = "KMGTPEZY";
        boolean decimal = false;
        int length = value.length();
        int exp = 0;
        int i = 0;
        int base = 1000;

        while (i < length) {
            if ('9' >= value.charAt(i) && value.charAt(i) >= '0') {
                i++;
            } else if (value.charAt(i) == '.' || value.charAt(i) == ',') {
                i++;
                decimal = true;
            } else break;
        }
        float num = Float.parseFloat(value.substring(0, i));
        if (i < length)
            if (value.charAt(i) == ' ')
                i++;
        if (i < length) {
            exp = UNITS.indexOf(value.charAt(i)) + 1;
            if (exp > 0)
                i++;
        }
        if (i < length && exp > 0 && value.charAt(i) == 'i') {
            i++;
            base = 1024;
        }

        if (i < length) {
            if (value.charAt(i) == 'B') ;
            else if (value.charAt(i) == 'b')
                num = num / 8;
            else this.parseError(key, value, i);
            i++;
        }
        if (i != length)
            this.parseError(key, value, i);
        return (long) Math.ceil(num * Math.pow(base, exp));
    }

    public boolean getBoolean(String key) throws InvalidKeyException {
        return bool.matcher(this.getString(key)).matches();
    }

}
