package com.sourcevirtues.common.storm.util;

import backtype.storm.Constants;
import backtype.storm.tuple.Tuple;

/**
 * Utility class.
 * 
 * @author Adrianos Dadis
 * 
 */
public class Utils {

    /*
     * Code from official Storm (master branch). Needed to run with Flux.
     */
    public static boolean isTickTuple(Tuple tuple) {
        return tuple != null && tuple.getSourceComponent().equals(Constants.SYSTEM_COMPONENT_ID)
                && tuple.getSourceStreamId().equals(Constants.SYSTEM_TICK_STREAM_ID);
    }

    /*public static Fields genFields(String... fieldNames) {
        return new 
    }*/
}
