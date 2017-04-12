package cn.com.modernmedia.corelib.model;

/**
 * 所有model类的父类，支持泛型，复用
 * Created by Eva. on 16/9/16.
 */

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

public class Entry implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * JSONArray是否为空
     *
     * @param array
     * @return
     */
    protected static boolean isNull(JSONArray array) {
        return JSONObject.NULL.equals(array) || array == null || array.length() == 0;
    }

    /**
     * JSONObject是否为空
     *
     * @param object
     * @return
     */
    protected static boolean isNull(JSONObject object) {
        return JSONObject.NULL.equals(object) || object == null;
    }


}
