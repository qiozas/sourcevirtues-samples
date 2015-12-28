package com.sourcevirtues.etl.storm.morphlines.handler;

import java.io.Serializable;
import java.util.Map;

import org.kitesdk.morphline.api.Record;

public interface IRecordHandler<T, E> extends Serializable {
    T map(Record record);

    @SuppressWarnings("rawtypes")
    void configure(Map conf);
}