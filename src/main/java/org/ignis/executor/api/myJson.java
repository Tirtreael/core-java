package org.ignis.executor.api;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

public class myJson extends JSONObject implements Serializable {
    public myJson() {
        super();
    }

    public myJson(JSONObject jo, String... names) {
        super(jo, names);
    }

    public myJson(JSONTokener x) throws JSONException {
        super(x);
    }

    public myJson(Map<?, ?> m) {
        super(m);
    }

    public myJson(Object bean) {
        super(bean);
    }

    public myJson(Object object, String... names) {
        super(object, names);
    }

    public myJson(String source) throws JSONException {
        super(source);
    }

    public myJson(String baseName, Locale locale) throws JSONException {
        super(baseName, locale);
    }

    protected myJson(int initialCapacity) {
        super(initialCapacity);
    }
}
