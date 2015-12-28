package com.sourcevirtues.common.storm.spout;

/**
 * Simple Spout that emit (for ever) a few hardcoded JSON message.
 * 
 * @author Adrianos Dadis
 * 
 */
public class RandomJsonTestSpout extends RandomSentenceTestSpout {
    private static final long serialVersionUID = 1L;

    public static final String DUMMY_AVRO_SCHEMA_SIMPLE =
            "{ \"type\":\"record\", \"namespace\":\"sourcevirtues\", \"name\":\"Person\", \"fields\":[ "
                    + "{\"name\":\"name\",\"type\":\"string\"}, "
                    + "{\"name\":\"age\",\"type\":\"int\"}]"
                    + "}";

    public static final String DUMMY_AVRO_SCHEMA_COMPLEX =
            "TODO";

    protected static boolean complexJson = false;

    protected void genSentences() {
        sentences = complexJson ? getComplexJsonSentences() : getFlatJsonSentences();
    }

    public RandomJsonTestSpout withComplexJson(boolean complex) {
        complexJson = complex;
        return this;
    }

    protected String[] getFlatJsonSentences() {
        return new String[] {
                "{\"name\":\"name1\",\"age\": 11}",
                "{\"name\":\"name2\",\"age\": 12}",
                "{\"name\":\"name3\",\"age\": 13}",
                "{\"name\":\"name4\",\"age\": 14}",
                "{\"name\":\"name5\",\"age\": 15}" };
    }

    protected String[] getComplexJsonSentences() {
        return new String[] {
                "{\"name\":\"name1\",\"age\": 11,\"info\":{\"id\":1,\"team\":\"team1\"}}",
                "{\"name\":\"name2\",\"age\": 12,\"info\":{\"id\":2,\"team\":\"team2\"}}",
                "{\"name\":\"name3\",\"age\": 13,\"info\":{\"id\":3,\"team\":\"team3\"}}",
                "{\"name\":\"name4\",\"age\": 14,\"info\":{\"id\":4,\"team\":\"team4\"}}",
                "{\"name\":\"name5\",\"age\": 15,\"info\":{\"id\":5,\"team\":\"team5\"}}" };
    }

    public static String avroSchema() {
        return (complexJson) ? DUMMY_AVRO_SCHEMA_COMPLEX : DUMMY_AVRO_SCHEMA_SIMPLE;
    }
}