package com.shhwang0930.mytg.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class ResponseMessage {
    public ResponseMessage(Integer code, String message, Map<String, Object> data){
        this.code = code;
        this.message = message;
        this.data = new HashMap<>();
    } //AllArgsConstructor > data를 null로 전달하여 객체를 생성하면 addData호출 시 널포인터 에러 발생

    private Integer code;
    private String message;
    private Map<String, Object> data;

    public void addData(String key, Object value){
        data.put(key, value);
    }
}
