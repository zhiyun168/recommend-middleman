package recommend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * Created by oudb on 2014/8/20.
 */
public class JsonSerializer {

    static final byte[] EMPTY_ARRAY = new byte[0];
    public static final String EMPTY_JSON = "{}";
    private static final ObjectMapper objectMapper = new ObjectMapper();//线程安全的，可重用

    /**
     * java-object as json-string
     * @param object
     * @return
     */
    public static String serializeAsString(Object object) throws SerializationException{
        if (object== null) {
            return EMPTY_JSON;
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception ex) {
            throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

    /**
     * json-string to java-object
     * @param str
     * @return
     */
    public static <T> T deserializeAsObject(String str, Class<T> clazz) throws SerializationException{
        if(str == null || clazz == null){
            return null;
        }
        try{
            return objectMapper.readValue(str, clazz);
        }catch (Exception ex) {
            throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }


    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] bytes, Class<T> clazz) throws SerializationException {
        if (bytes == null || bytes.length == 0 ||  clazz == null) {
            return null;
        }
        try {
            return (T) objectMapper.readValue(bytes, 0, bytes.length, clazz);
        } catch (Exception ex) {
            throw new SerializationException("Could not read JSON: " + ex.getMessage(), ex);
        }
    }

    public static byte[] serialize(Object t) throws SerializationException {
        if (t == null) {
            return EMPTY_ARRAY;
        }
        try {
            return objectMapper.writeValueAsBytes(t);
        }catch (Exception ex) {
            throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

}
