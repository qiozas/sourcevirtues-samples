package com.sourcevirtues.etl.storm.morphlines.mapper.result;

import java.io.Serializable;

public interface IRecordResultMapper<T, E> extends Serializable {
    E map(T value);
}