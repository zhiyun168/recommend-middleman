package recommend.redisson;

import org.redisson.codec.JsonJacksonCodec;
import org.redisson.codec.StringCodec;

import java.nio.ByteBuffer;

/**
 * Created by ouduobiao on 15/6/25.
 */
public class StringCodecExt extends StringCodec{
    private final JsonJacksonCodec jacksonCodec = new JsonJacksonCodec();

    @Override
    public Object decodeValue(ByteBuffer bytes) {
        try {
            return  jacksonCodec.decodeValue(bytes);
        }
        catch (Exception e)
        {
            //do nothing
            System.out.println(e.getMessage());
        }
        return super.decodeValue(bytes);
    }

    @Override
    public byte[] encodeValue(Object value) {
        if(value instanceof String)
            return super.encodeValue(value);
        else
            return jacksonCodec.encodeValue(value);
    }
}
