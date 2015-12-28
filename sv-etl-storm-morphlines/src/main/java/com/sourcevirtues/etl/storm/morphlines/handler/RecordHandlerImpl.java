package com.sourcevirtues.etl.storm.morphlines.handler;

import java.util.Collection;
import java.util.Map;

import org.kitesdk.morphline.api.Record;
import org.kitesdk.morphline.base.Fields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sourcevirtues.etl.storm.morphlines.mapper.result.IRecordResultMapper;

public class RecordHandlerImpl<T, E> implements IRecordHandler<T, E> {
    private static final long serialVersionUID = 1L;
    protected static final Logger LOG = LoggerFactory.getLogger(RecordHandlerImpl.class);

    protected Class<T> clazz;
    protected IRecordResultMapper valueMapper;

    @SuppressWarnings("unused")
    private RecordHandlerImpl() {}

    public RecordHandlerImpl(Class<T> type) {
        this.clazz = type;
    }

    public RecordHandlerImpl withValueMapper(IRecordResultMapper x) {
        valueMapper = x;
        return this;
    }

    /*public static <T> RecordMapperImpl<T> recordMapperFactory(Class<T> type) {
        return new RecordMapperImpl<T>(type);
    }*/

    @SuppressWarnings("rawtypes")
    public void configure(Map conf) {

    }

    @Override
    public T map(Record record) {
        Map<String, Collection<Object>> recordMap = record.getFields().asMap();
        T body = null;
        for (Map.Entry<String, Collection<Object>> entry : recordMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                throw new IllegalStateException(getClass().getName()
                        + " must not generate more than one output value per record field");
            }

            assert entry.getValue().size() != 0;

            Object firstValue = entry.getValue().iterator().next();
            if (Fields.ATTACHMENT_BODY.equals(entry.getKey())) {
                if (firstValue == null) {
                    LOG.trace("VALUE is null. return null");
                    body = null;
                    break;
                } else {
                    LOG.trace("currentType={} , key={} , value={}", firstValue.getClass(), entry.getKey(), entry.getValue());
                    //TODO Handle errors (classCastException, ...)
                    body = (T) valueMapper.map(firstValue);
                    break;
                }
            } else {
                //TODO Handle or ignore other values
                LOG.trace("IGNORED: key={} , value={}", entry.getKey(), entry.getValue());
            }
        }

        return body;
    }
}
