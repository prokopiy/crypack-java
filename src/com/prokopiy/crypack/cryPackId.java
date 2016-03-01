package com.prokopiy.crypack;

/**
 * Created by Прокопий on 19.08.2015.
 */


public class cryPackId {
    public static final byte CRYPACK_NULL       = (byte) 0x01;

    public static final byte CRYPACK_BOOL_FALSE = (byte) 0x10;
    public static final byte CRYPACK_BOOL_TRUE  = (byte) 0x11;

    public static final byte CRYPACK_INTEGER = (byte) 0x80;
    public static final byte CRYPACK_FLOAT   = (byte) 0x90;

    public static final byte CRYPACK_STRING_8  = (byte) 0xA1;
    public static final byte CRYPACK_STRING_16 = (byte) 0xA2;
    public static final byte CRYPACK_STRING_32 = (byte) 0xA3;

    public static final byte CRYPACK_ARRAY_8  = (byte) 0xB1;
    public static final byte CRYPACK_ARRAY_16 = (byte) 0xB2;
    public static final byte CRYPACK_ARRAY_32 = (byte) 0xB3;

    public static final byte CRYPACK_MAP_8  = (byte) 0xC1;
    public static final byte CRYPACK_MAP_16 = (byte) 0xC2;
    public static final byte CRYPACK_MAP_32 = (byte) 0xC3;

    public static final byte CRYPACK_BIN_8  = (byte) 0xD1;
    public static final byte CRYPACK_BIN_16 = (byte) 0xD2;
    public static final byte CRYPACK_BIN_32 = (byte) 0xD3;

}

