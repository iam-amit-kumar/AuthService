package org.springBoot.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.springBoot.model.UserInfoDto;

import java.util.Map;

public class UserInfoSerializer implements Serializer<UserInfoDto> {
    @Override
    public void configure(Map<String, ?> map, boolean b) { }

    @Override
    public byte[] serialize(String arg0, UserInfoDto arg1) {
        byte[] returnValue = null;

        ObjectMapper objectMapper = new ObjectMapper();
        try{
            returnValue = objectMapper.writeValueAsString(arg1).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    @Override
    public void close() { }
}
