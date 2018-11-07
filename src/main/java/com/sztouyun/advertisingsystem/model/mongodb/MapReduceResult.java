package com.sztouyun.advertisingsystem.model.mongodb;

import lombok.Data;

@Data
public class MapReduceResult<S,T> {
    private S id;
    private T value;
}
