package com.sourcevirtues.etl.storm.morphlines.handler;

import com.sourcevirtues.etl.storm.morphlines.mapper.result.IRecordResultMapper;

public class RecordHandlerFactory {

    @SuppressWarnings("unchecked")
    public static <T, E> RecordHandlerImpl<T, E> genDefaultRecordHandler(Class<T> type, IRecordResultMapper<E, T> x) {
        return new RecordHandlerImpl<T, E>(type).withValueMapper(x);
    }
}
