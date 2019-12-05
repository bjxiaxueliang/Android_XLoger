package com.xiaxl.core.log.base;

public class PalLoggerFactory {

    public PalLoggerFactory() {
    }

    public static PalLoger getLogger() {
        return new PalLoger();
    }
}
