package myrpc.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 简易json工具类
 * */
public class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String obj2Str(Object obj){
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("obj2Str error",e);
        }
    }

    public static <T> T json2Obj(String jsonStr, Class<T> objClass) {
        try {
            return OBJECT_MAPPER.readValue(jsonStr, objClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json2Obj error",e);
        }
    }
}
