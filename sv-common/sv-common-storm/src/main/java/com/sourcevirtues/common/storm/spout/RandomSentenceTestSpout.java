package com.sourcevirtues.common.storm.spout;

import java.util.Map;
import java.util.Random;

import com.sourcevirtues.common.storm.util.CmnStormCons;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

/**
 * Simple Spout that emit (for ever) a few hardcoded sentences.
 * 
 * @author Adrianos Dadis
 */
public class RandomSentenceTestSpout extends BaseRichSpout {
    private static final long serialVersionUID = 1L;

    protected SpoutOutputCollector _collector;
    protected Random _rand;
    protected String[] sentences;

    protected String fieldName = CmnStormCons.TUPLE_FIELD_MSG;
    protected long sleepMillis = 100;

    @SuppressWarnings("rawtypes")
    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        _collector = collector;
        _rand = new Random();

        genSentences();
    }

    @Override
    public void nextTuple() {
        Utils.sleep(sleepMillis);

        String sentence = sentences[_rand.nextInt(sentences.length)];

        _collector.emit(new Values(sentence));
    }

    @Override
    public void ack(Object id) {}

    @Override
    public void fail(Object id) {}

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(fieldName));
    }

    protected void genSentences() {
        sentences = new String[] {
                "abort and abort and calm",
                "admire FSF admire GNU crash DRM",
                "nothing relevant",
                "calm when others cannot",
                "bonus sometimes works",
                "abort and crash" };
    }

    public RandomSentenceTestSpout withFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public RandomSentenceTestSpout withSleepMillies(long sleepMillis) {
        this.sleepMillis = sleepMillis;
        return this;
    }
}