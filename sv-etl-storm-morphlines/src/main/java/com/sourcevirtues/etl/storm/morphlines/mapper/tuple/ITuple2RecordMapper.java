package com.sourcevirtues.etl.storm.morphlines.mapper.tuple;

import java.io.Serializable;

import backtype.storm.tuple.Tuple;

public interface ITuple2RecordMapper<T> extends Serializable {
    void configure(String inputFieldName);

    T map(Tuple tuple);
}
