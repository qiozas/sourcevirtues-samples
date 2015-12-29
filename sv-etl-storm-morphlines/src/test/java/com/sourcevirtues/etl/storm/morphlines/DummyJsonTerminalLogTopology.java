package com.sourcevirtues.etl.storm.morphlines;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;

import com.sourcevirtues.common.storm.spout.RandomJsonTestSpout;
import com.sourcevirtues.common.storm.util.CmnStormCons;
import com.sourcevirtues.etl.storm.morphlines.bolt.MorphlinesBolt;
import com.sourcevirtues.etl.storm.morphlines.mapper.tuple.String2ByteArrayTupleMapper;

public class DummyJsonTerminalLogTopology {

    public static void main(String[] args) throws Exception {
        Config config = new Config();

        RandomJsonTestSpout spout = new RandomJsonTestSpout().withComplexJson(false);

        String2ByteArrayTupleMapper tuppleMapper = new String2ByteArrayTupleMapper();
        tuppleMapper.configure(CmnStormCons.TUPLE_FIELD_MSG);

        MorphlinesBolt morphBolt = new MorphlinesBolt()
                .withTupleMapper(tuppleMapper)
                .withMorphlineId("json_terminal_log")
                .withMorphlineConfFile("target/test-classes/morphline_confs/json_terminal_log.conf");

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("WORD_SPOUT", spout, 1);
        builder.setBolt("MORPH_BOLT", morphBolt, 1).shuffleGrouping("WORD_SPOUT");

        if (args.length == 0) {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("MyDummyJsonTerminalLogTopology", config, builder.createTopology());
            Thread.sleep(10000);
            cluster.killTopology("MyDummyJsonTerminalLogTopology");
            cluster.shutdown();
            System.exit(0);
        } else if (args.length == 1) {
            StormSubmitter.submitTopology(args[0], config, builder.createTopology());
        } else {
            System.out.println("Usage: DummyJson2StringTopology <topology_name>");
        }
    }
}
