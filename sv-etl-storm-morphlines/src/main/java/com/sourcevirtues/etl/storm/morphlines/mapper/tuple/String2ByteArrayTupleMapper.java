package com.sourcevirtues.etl.storm.morphlines.mapper.tuple;

import backtype.storm.tuple.Tuple;

public class String2ByteArrayTupleMapper implements ITuple2RecordMapper<byte[]> {

    private static final long serialVersionUID = 1L;

    private String inputFieldName;

    @Override
    public void configure(String inputFieldName) {
        this.inputFieldName = inputFieldName;
    }

    @Override
    public byte[] map(Tuple tuple) {
        String inputValue = (String) tuple.getValueByField(inputFieldName);
        return inputValue.getBytes();
    }

}
