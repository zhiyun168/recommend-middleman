package recommend.utils;

import com.google.common.base.Optional;
import com.google.protobuf.*;
import recommend.model.RecommendProtos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by ouduobiao on 2017/1/6.
 */
public class ProtocolBuffersUtil {

    public static String toString(Message message){
        return message.toByteString().toStringUtf8();
    }

    public static <MessageType> Optional<MessageType> toMessage(String msgStr, Parser<MessageType> parser)
    {
        try {
            return Optional.fromNullable(parser.parseFrom(ByteString.copyFromUtf8(msgStr)));
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return Optional.absent();
        }
    }

    public static Map toMap(Message message)
    {
        Map result = new HashMap<>();
        Descriptors.Descriptor descriptor = message.getDescriptorForType();
        List fields = descriptor.getFields();

        for(int i = 0; i < fields.size(); ++i) {
            Descriptors.FieldDescriptor field = (Descriptors.FieldDescriptor)fields.get(i);
            Descriptors.OneofDescriptor oneofDescriptor = field.getContainingOneof();
            if(oneofDescriptor != null) {
                i += oneofDescriptor.getFieldCount() - 1;
                if(!message.hasOneof(oneofDescriptor)) {
                    continue;
                }

                field = message.getOneofFieldDescriptor(oneofDescriptor);
            } else {
                if(field.isRepeated()) {
                    List value = (List)message.getField(field);
                    if(!value.isEmpty()) {
                        result.put(field, value);
                    }
                    continue;
                }

                if(!message.hasField(field)) {
                    continue;
                }
            }
            Object value = message.getField(field);
            if(value instanceof Message)
                value = toMap((RecommendProtos.HealthTip) value);
            result.put(field.getJsonName(), value);
        }

        return result;
    }



}
