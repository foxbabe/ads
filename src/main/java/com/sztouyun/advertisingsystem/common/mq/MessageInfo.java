package com.sztouyun.advertisingsystem.common.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageInfo {
    public MessageInfo(String content, String label) {
        this.content = content;
        this.label = label;
    }

    public MessageInfo(String content, String label, Object lockToken) {
        this.content = content;
        this.label = label;
        this.lockToken = lockToken;
    }

    private String content;
    private String label;
    private Object lockToken;
    private Duration duration;
}
