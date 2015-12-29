package com.sourcevirtues.etl.storm.morphlines.bolt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kitesdk.morphline.api.Command;
import org.kitesdk.morphline.api.MorphlineContext;
import org.kitesdk.morphline.api.Record;
import org.kitesdk.morphline.base.Compiler;
import org.kitesdk.morphline.base.FaultTolerance;
import org.kitesdk.morphline.base.Notifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.sourcevirtues.common.basic.util.EmptyUtils;
import com.sourcevirtues.common.basic.util.FileUtils;
import com.sourcevirtues.common.basic.util.PairNode;
import com.sourcevirtues.etl.storm.morphlines.handler.IRecordHandler;
import com.sourcevirtues.etl.storm.morphlines.mapper.tuple.ITuple2RecordMapper;
import com.sourcevirtues.etl.storm.morphlines.util.SimpleCommandCollector;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class MorphlinesBolt extends BaseRichBolt {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(MorphlinesBolt.class);

    private transient OutputCollector collector;
    private transient MorphlineContext morphlineContext;
    private transient Command morphline;
    private transient SimpleCommandCollector finalChild;

    private String morphlineId;
    private String morphlineConfFullString, morphlineConfpath;
    private String[] outputFieldNames;
    private boolean terminalBolt = true;
    private boolean anchorTuple = false;
    private IRecordHandler<?, ?> recordMapper;
    private List<IRecordHandler<?, ?>> recordMappers;
    private ITuple2RecordMapper<?> tupleMapper;

    @SuppressWarnings("rawtypes")
    @Override
    public void prepare(Map stormConf, TopologyContext topologyContext, OutputCollector collector) {
        LOG.info("START prepare");

        this.collector = collector;

        File confFile = loadFile(topologyContext.getThisWorkerPort().toString());

        if (morphlineContext == null) {
            //TODO Make FaultTolerance configurable
            FaultTolerance faultTolerance = new FaultTolerance(true, false, null);

            morphlineContext = new MorphlineContext.Builder()
                    .setExceptionHandler(faultTolerance)
                    //.setMetricRegistry(SharedMetricRegistries.getOrCreate(customMorphlineId))
                    .build();
        }

        Config override = ConfigFactory.parseMap(new HashMap<String, Object>());
        finalChild = new SimpleCommandCollector();
        morphline = new Compiler().compile(confFile, morphlineId, morphlineContext, finalChild, override);

        if (!EmptyUtils.nullOrEmpty(outputFieldNames)) {
            terminalBolt = false;
        }
    }

    @Override
    public void execute(Tuple tuple) {
        try {
            finalChild.reset();

            Record record = new Record();
            record.put(org.kitesdk.morphline.base.Fields.ATTACHMENT_BODY, tupleMapper.map(tuple));

            Notifications.notifyStartSession(morphline);
            boolean exceptionRaised = false;
            try {
                boolean processed = morphline.process(record);
                if (!processed) {
                    //TODO handle Morphline returned false
                    LOG.error("Morphline processing returned false. inputTuple = {}", tuple);
                    collector.fail(tuple);
                    return;
                }
            } catch (RuntimeException rt) {
                exceptionRaised = true;
                morphlineContext.getExceptionHandler().handleException(rt, record);
            }

            if (terminalBolt) {
                collector.ack(tuple);
                return;
            }

            if (exceptionRaised) {
                //Decide if you need extra handling apart from FaultTolerance handler provided by Morphline
            }

            List<Record> morphlineResults = finalChild.getRecords();
            if (morphlineResults.size() == 0) {
                //TODO handle zero result
                LOG.warn("Zero result by morphline processing. inputTuple: {}", tuple);
                collector.ack(tuple);
                return;
            }
            if (morphlineResults.size() > 1) {
                //TODO Emit to error stream, ignore or fail tuple
                LOG.error("Morphline must not generate more than one output record per input record. returnedSize="
                        + morphlineResults.size());
                collector.fail(tuple);
            }

            Object finalResults = recordMapper.map(morphlineResults.get(0));

            // Useful when expected more than one output from Morphline execution
            /*Object[] finalResults = new Object[recordMappers.size()];
            for (int i = 0; i < morphlineResults.size(); i++) {
                finalResults[i] = recordMappers.get(i).map(morphlineResults.get(i));
            }*/

            if (anchorTuple) {
                collector.emit(tuple, new Values(finalResults));
            } else {
                collector.emit(new Values(finalResults));
            }

            collector.ack(tuple);

        } catch (Exception e) {
            this.collector.reportError(e);
            collector.fail(tuple);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        LOG.info("START declareOutputFields");
        if (terminalBolt || EmptyUtils.nullOrEmpty(outputFieldNames)) {
            LOG.info("This is TERMINAL BOLT");
            terminalBolt = true;
            return;
        }

        declarer.declare(new Fields(outputFieldNames));
    }

    @Override
    public void cleanup() {
        if (morphline != null) {
            Notifications.notifyShutdown(morphline);
        }
    }

    public MorphlinesBolt withMorphlineId(String morphId) {
        LOG.trace("withMorphlineId: {}", morphId);
        this.morphlineId = morphId;
        return this;
    }

    public MorphlinesBolt withMorphlineConfFullString(String confString) {
        LOG.trace("withMorphlineConfFullString: {}", confString);
        this.morphlineConfFullString = confString;
        return this;
    }

    public MorphlinesBolt withMorphlineConfFile(String confPath) {
        LOG.trace("withMorphlineConfFile: {}", confPath);

        try {
            morphlineConfFullString = FileUtils.readFileUTF8(confPath);
            LOG.info("morphlineConfFullString: \n{}", morphlineConfFullString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public MorphlinesBolt withRecordMapper(IRecordHandler<?, ?> recordMapper) {
        LOG.trace("withRecordMapper: {}", recordMapper);
        this.recordMapper = recordMapper;
        return this;
    }

    public MorphlinesBolt withAnchor(boolean anchorTuple) {
        LOG.trace("withAnchor: {}", anchorTuple);
        this.anchorTuple = anchorTuple;
        return this;
    }

    public MorphlinesBolt withOutputFields(String... outputFieldNames) {
        LOG.trace("withOutputFields: {}", outputFieldNames);
        if (EmptyUtils.nullOrEmpty(outputFieldNames)) {
            throw new IllegalArgumentException("Invalid outputFieldNames: " + outputFieldNames);
        }
        this.outputFieldNames = outputFieldNames;

        terminalBolt = false;

        return this;
    }

    @SuppressWarnings("rawtypes")
    public MorphlinesBolt withTupleMapper(ITuple2RecordMapper tupleMapper) {
        LOG.trace("withTupleMapper: {}", tupleMapper);
        this.tupleMapper = tupleMapper;
        return this;
    }

    // {OutputFieldName , RecordMapper}
    public MorphlinesBolt withOutputProcessors(List<PairNode<String, IRecordHandler<?, ?>>> processors) {
        LOG.trace("withOutputProcessors: {}", processors);
        if (EmptyUtils.nullOrEmpty(processors)) {
            throw new IllegalArgumentException("Invalid processors: " + processors);
        }

        outputFieldNames = new String[processors.size()];
        recordMappers = new ArrayList<>(processors.size());
        for (int i = 0; i < processors.size(); i++) {
            outputFieldNames[i] = processors.get(i).getPart1();
            recordMappers.add(processors.get(i).getPart2());
            terminalBolt = false;
        }
        return this;
    }

    private File loadFile(String tmpId) {
        //TODO Load file from URL

        File confFile = null;
        if (!EmptyUtils.nullOrEmpty(morphlineConfFullString)) {
            confFile = FileUtils.createTempFileWithContent(morphlineConfFullString, "morphlines_"
                    + tmpId + "_" + System.currentTimeMillis() + "_", ".conf");
        } else {
            LOG.warn("variable morphlineConfFullString is empty. Try to load directly from local file: {}", morphlineConfpath);
            confFile = new File(morphlineConfpath);
        }

        this.morphlineConfpath = confFile.getAbsolutePath();
        LOG.info("Morphline conf file: {}", morphlineConfpath);

        return confFile;
    }
}
