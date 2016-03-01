package com.prokopiy.crypack;

import java.io.UnsupportedEncodingException;


/**
 * Created by Прокопий on 26.08.2015.
 */



public class cryPackValue {


    public static byte[] concatarray(byte[] a, byte[] b){
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    public static String escape(String s) {
        if(s == null) {
            return null;
        } else {
            StringBuffer sb = new StringBuffer();
            escape(s, sb);
            return sb.toString();
        }
    }

    static void escape(String s, StringBuffer sb) {
        for(int i = 0; i < s.length(); ++i) {
            char ch = s.charAt(i);
            switch(ch) {
                case '\b':
                    sb.append("\\b");
                    continue;
                case '\t':
                    sb.append("\\t");
                    continue;
                case '\n':
                    sb.append("\\n");
                    continue;
                case '\f':
                    sb.append("\\f");
                    continue;
                case '\r':
                    sb.append("\\r");
                    continue;
                case '\"':
                    sb.append("\\\"");
                    continue;
                case '/':
                    sb.append("\\/");
                    continue;
                case '\\':
                    sb.append("\\\\");
                    continue;
            }

            if(ch >= 0 && ch <= 31 || ch >= 127 && ch <= 159 || ch >= 8192 && ch <= 8447) {
                String ss = Integer.toHexString(ch);
                sb.append("\\u");

                for(int k = 0; k < 4 - ss.length(); ++k) {
                    sb.append('0');
                }

                sb.append(ss.toUpperCase());
            } else {
                sb.append(ch);
            }
        }

    }

    public static String toCryPackString(Object value) {
        return value == null?"null":(value instanceof String?"\"" + escape((String)value) + "\"":(value instanceof Double?(!((Double)value).isInfinite() && !((Double)value).isNaN()?value.toString():"null"):(value instanceof Float?(!((Float)value).isInfinite() && !((Float)value).isNaN()?value.toString():"null"):(value instanceof Number?value.toString():(value instanceof Boolean?value.toString():value.toString())))));
    }

    public String toString() {
        return toCryPackString(this);
    }


    public static byte[] encodeNull(){
        byte writeBuffer[] = new byte[1];
        writeBuffer[0] = cryPackId.CRYPACK_NULL;
        return writeBuffer;
    }

    public static byte[] encodeBoolean(Boolean value){
        byte writeBuffer[] = new byte[1];
        if (value.booleanValue()) {
            writeBuffer[0] = cryPackId.CRYPACK_BOOL_TRUE;
        } else {
            writeBuffer[0] = cryPackId.CRYPACK_BOOL_FALSE;
        }
        return writeBuffer;
    }

    public static byte[] encodeInteger(Integer value){

        long v = value.longValue();

        byte writeBuffer[] = new byte[9];
        writeBuffer[0] = cryPackId.CRYPACK_INTEGER;//(byte) 0xd3;
        writeBuffer[1] = (byte)(v >>> 56);
        writeBuffer[2] = (byte)(v >>> 48);
        writeBuffer[3] = (byte)(v >>> 40);
        writeBuffer[4] = (byte)(v >>> 32);
        writeBuffer[5] = (byte)(v >>> 24);
        writeBuffer[6] = (byte)(v >>> 16);
        writeBuffer[7] = (byte)(v >>>  8);
        writeBuffer[8] = (byte)(v >>>  0);
        return writeBuffer;
    }

    public static byte[] encodeLong(Long value){
        long v = value.longValue();
        byte writeBuffer[] = new byte[9];
        writeBuffer[0] = cryPackId.CRYPACK_INTEGER;//(byte) 0xd3;
        writeBuffer[1] = (byte)(v >>> 56);
        writeBuffer[2] = (byte)(v >>> 48);
        writeBuffer[3] = (byte)(v >>> 40);
        writeBuffer[4] = (byte)(v >>> 32);
        writeBuffer[5] = (byte)(v >>> 24);
        writeBuffer[6] = (byte)(v >>> 16);
        writeBuffer[7] = (byte)(v >>>  8);
        writeBuffer[8] = (byte)(v >>>  0);
        return writeBuffer;
    }

    public static byte[] encodeFloat(Double value){
        long v = value.longValue();

        byte writeBuffer[] = new byte[9];
        writeBuffer[0] = cryPackId.CRYPACK_FLOAT;
        writeBuffer[1] = (byte)(v >>> 56);
        writeBuffer[2] = (byte)(v >>> 48);
        writeBuffer[3] = (byte)(v >>> 40);
        writeBuffer[4] = (byte)(v >>> 32);
        writeBuffer[5] = (byte)(v >>> 24);
        writeBuffer[6] = (byte)(v >>> 16);
        writeBuffer[7] = (byte)(v >>>  8);
        writeBuffer[8] = (byte)(v >>>  0);
        return writeBuffer;
    }

    public static byte[] encodeString(String value) {
        byte[] s;
        try {
            s = value.getBytes("UTF8");
        } catch (UnsupportedEncodingException e) {
            s = new byte[0];
            e.printStackTrace();
        }

        byte[] result;

        if (s.length <= 0xff) {
//            System.out.println("encodeString: CRYPACK_STRING_8");
            byte[] h = new byte[2];
            h[0] = cryPackId.CRYPACK_STRING_8;//(byte) 0xdb;
            int l = s.length & 0xff;
            h[1] = (byte) ((l >>> 0) & 0xFF);
            result = concatarray(h,s);
        } else if (s.length <= 0xffff) {
//            System.out.println("encodeString: CRYPACK_STRING_16");
            byte[] h = new byte[3];
            h[0] = cryPackId.CRYPACK_STRING_16;//(byte) 0xdb;
            int l = s.length & 0xffff;
            h[1] = (byte) ((l >>> 8) & 0xFF);
            h[2] = (byte) ((l >>> 0) & 0xFF);
            result = concatarray(h,s);
        } else {
            byte[] h = new byte[5];
            h[0] = cryPackId.CRYPACK_STRING_32;//(byte) 0xdb;
            int l = s.length & 0xffffffff;
            h[1] = (byte) ((l >>> 24) & 0xFF);
            h[2] = (byte) ((l >>> 16) & 0xFF);
            h[3] = (byte) ((l >>> 8) & 0xFF);
            h[4] = (byte) ((l >>> 0) & 0xFF);
            result = concatarray(h,s);
        }
        return result;
    }



    public static byte[] encode(Object value){

        return value == null?encodeNull():
                (value instanceof Integer?encodeInteger((Integer)value):
                        value instanceof Long?encodeLong((Long) value):
                                value instanceof Float?encodeFloat((Double) value):
                                        value instanceof Double?encodeFloat((Double) value):
                                                value instanceof String?encodeString((String) value):
                                                        value instanceof cryPackArray ?((cryPackArray) value).encode():
                                                                value instanceof cryPackMap ?((cryPackMap) value).encode():
                                                                        value instanceof Boolean?encodeBoolean((Boolean) value):
                                                                                null);
    }






}
