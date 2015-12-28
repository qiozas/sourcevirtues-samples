package com.sourcevirtues.common.basic.util;

import java.util.Collection;
import java.util.Map;

/**
 * Utilities to check if Object is null or empty.
 * 
 * @author Adrianos Dadis
 *
 */
public class EmptyUtils {

    public static boolean nullOrEmpty(String in) {
        if ((in == null) || (in.isEmpty())) {
            return true;
        }
        return false;
    }

    public static boolean nullOrEmptyTrimmed(String in) {
        if ((in == null) || (in.trim().isEmpty())) {
            return true;
        }
        return false;
    }

    public static <E> boolean nullOrEmpty(final Collection<E> in) {
        if ((in == null) || (in.isEmpty())) {
            return true;
        }
        return false;
    }

    public static <E, V> boolean nullOrEmpty(final Map<E, V> in) {
        if ((in == null) || (in.isEmpty())) {
            return true;
        }
        return false;
    }

    public static <T> boolean nullOrEmpty(T[] in) {
        if ((in == null) || (in.length == 0)) {
            return true;
        }
        return false;
    }

    public static boolean nullOrEmpty(byte[] in) {
        if ((in == null) || (in.length == 0)) {
            return true;
        }
        return false;
    }

    public static boolean nullOrEmpty(short[] in) {
        if ((in == null) || (in.length == 0)) {
            return true;
        }
        return false;
    }

    public static boolean nullOrEmpty(int[] in) {
        if ((in == null) || (in.length == 0)) {
            return true;
        }
        return false;
    }

    public static boolean nullOrEmpty(long[] in) {
        if ((in == null) || (in.length == 0)) {
            return true;
        }
        return false;
    }

    public static boolean nullOrEmpty(float[] in) {
        if ((in == null) || (in.length == 0)) {
            return true;
        }
        return false;
    }

    public static boolean nullOrEmpty(double[] in) {
        if ((in == null) || (in.length == 0)) {
            return true;
        }
        return false;
    }

    public static boolean nullOrEmpty(boolean[] in) {
        if ((in == null) || (in.length == 0)) {
            return true;
        }
        return false;
    }

    public static boolean nullOrEmpty(char[] in) {
        if ((in == null) || (in.length == 0)) {
            return true;
        }
        return false;
    }
}
