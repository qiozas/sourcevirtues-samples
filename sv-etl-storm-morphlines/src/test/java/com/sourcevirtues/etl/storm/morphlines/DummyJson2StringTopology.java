package com.sourcevirtues.etl.storm.morphlines;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;

import com.sourcevirtues.common.storm.bolt.LoggingBolt;
import com.sourcevirtues.common.storm.spout.RandomJsonTestSpout;
import com.sourcevirtues.common.storm.util.CmnStormCons;
import com.sourcevirtues.etl.storm.morphlines.bolt.MorphlinesBolt;
import com.sourcevirtues.etl.storm.morphlines.handler.RecordHandlerFactory;
import com.sourcevirtues.etl.storm.morphlines.mapper.result.JsonNode2StringResultMapper;
import com.sourcevirtues.etl.storm.morphlines.mapper.tuple.String2ByteArrayTupleMapper;

public class DummyJson2StringTopology {

    public static void main(String[] args) throws Exception {
        Config config = new Config();

        RandomJsonTestSpout spout = new RandomJsonTestSpout().withComplexJson(false);

        String2ByteArrayTupleMapper tuppleMapper = new String2ByteArrayTupleMapper();
        tuppleMapper.configure(CmnStormCons.TUPLE_FIELD_MSG);

        /*PairNode<String, IRecordHandler<?, ?>>[] resultRecordHandlers = new PairNode[] { new PairNode(
                CmnStormCons.TUPLE_FIELD_MSG,
                RecordHandlerFactory.createMyObject(String.class, new JsonNode2StringResultMapper())) };*/

        MorphlinesBolt morphBolt = new MorphlinesBolt()
                .withTupleMapper(tuppleMapper)
                .withMorphlineId("json2log")
                .withMorphlineConfFile("target/test-classes/morphline_confs/json2string.conf")
                //.withOutputProcessors(Arrays.asList(resultRecordHandlers));
                .withOutputFields(CmnStormCons.TUPLE_FIELD_MSG)
                .withRecordMapper(RecordHandlerFactory.createMyObject(String.class, new JsonNode2StringResultMapper()));

        LoggingBolt printBolt = new LoggingBolt().withFields(CmnStormCons.TUPLE_FIELD_MSG);

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("WORD_SPOUT", spout, 1);
        builder.setBolt("MORPH_BOLT", morphBolt, 1).shuffleGrouping("WORD_SPOUT");
        builder.setBolt("PRINT_BOLT", printBolt, 1).shuffleGrouping("MORPH_BOLT");

        if (args.length == 0) {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("MyDummyJson2StringTopology", config, builder.createTopology());
            Thread.sleep(10000);
            cluster.killTopology("MyDummyJson2StringTopology");
            cluster.shutdown();
            System.exit(0);
        } else if (args.length == 1) {
            StormSubmitter.submitTopology(args[0], config, builder.createTopology());
        } else {
            System.out.println("Usage: DummyJson2StringTopology <topology_name>");
        }
    }
}
