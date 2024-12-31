package com.kaihang.common.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageType {
    REQUEST(0),
    RESPONSE(1);
    private int code;
}
