package com.prokopiy.crypack;

import java.nio.charset.Charset;

/**
 * Created by Прокопий on 27.08.2015.
 */

public class cryPackParser {

    public cryPackParseResult parse(byte[] b, int start_pos){
        int T = 0xFF & b[start_pos];
        System.out.println("cryPackParser.parse: T = " + T);
        cryPackParseResult obj;
        switch ((byte) T){
            case cryPackId.CRYPACK_NULL:{
                System.out.println("cryPackParser.parse: CRYPACK_BOOL_TRUE");
                obj = new cryPackParseResult(null, start_pos+1);
                break;
            }
            case cryPackId.CRYPACK_BOOL_TRUE:{
                System.out.println("cryPackParser.parse: CRYPACK_BOOL_TRUE");
                obj = new cryPackParseResult(Boolean.TRUE, start_pos+1);
                break;
            }
            case cryPackId.CRYPACK_BOOL_FALSE:{
                System.out.println("cryPackParser.parse: CRYPACK_BOOL_FALSE");
                obj = new cryPackParseResult(Boolean.FALSE, start_pos+1);
                break;
            }
            case cryPackId.CRYPACK_INTEGER:{
                System.out.println("cryPackParser.parse: CRYPACK_INTEGER");
                byte[] buf = new byte[8];
                System.arraycopy(b, start_pos + 1, buf, 0, 8);
                obj = new cryPackParseResult(parseInteger(buf), start_pos+1+8);
                break;
            }
            case cryPackId.CRYPACK_FLOAT:{
                System.out.println("cryPackParser.parse: CRYPACK_FLOAT");
                byte[] buf = new byte[8];
                System.arraycopy(b, start_pos+1, buf, 0, 8);
                obj = new cryPackParseResult(parseFloat(buf), start_pos+1+8);
                break;
            }
            case cryPackId.CRYPACK_STRING_8:{
                System.out.println("cryPackParser.parse: CRYPACK_STRING_8");
                int len = ((b[start_pos+1] & 255) <<  0);
                byte[] buf = new byte[len];
                System.arraycopy(b, start_pos+2, buf, 0, len);
                obj = new cryPackParseResult(parseString(buf), start_pos+1+1+len);
                break;
            }
            case cryPackId.CRYPACK_STRING_16:{
                System.out.println("cryPackParser.parse: CRYPACK_STRING_16");
                int len = (((b[start_pos+1] & 255) <<  8) +
                           ((b[start_pos+2] & 255) <<  0));
                byte[] buf = new byte[len];
                System.arraycopy(b, start_pos+3, buf, 0, len);
                obj = new cryPackParseResult(parseString(buf), start_pos+1+2+len);
                break;
            }
            case cryPackId.CRYPACK_STRING_32:{
                System.out.println("cryPackParser.parse: CRYPACK_STRING_32");
                int len = (((b[start_pos+1] & 255) << 24) +
                           ((b[start_pos+2] & 255) << 16) +
                           ((b[start_pos+3] & 255) <<  8) +
                           ((b[start_pos+4] & 255) <<  0));
                byte[] buf = new byte[len];
                System.arraycopy(b, start_pos+5, buf, 0, len);
                obj = new cryPackParseResult(parseString(buf), start_pos+1+4+len);
                break;
            }
            case cryPackId.CRYPACK_ARRAY_32:{
                System.out.println("cryPackParser.parse: CRYPACK_ARRAY_32");
                int len = (((b[start_pos+1] & 255) << 24) +
                           ((b[start_pos+2] & 255) << 16) +
                           ((b[start_pos+3] & 255) <<  8) +
                           ((b[start_pos+4] & 255) <<  0));
                cryPackArray Array = new cryPackArray();
                int start = start_pos+1+4;
                cryPackParseResult pResult1;
                for (int i = 0; i<len; i++){
                    pResult1 = parse(b, start);
                    Array.add(pResult1.obj);
                    start = pResult1.last_pos;
                }
                obj = new cryPackParseResult(Array, start);
                break;
            }
            case cryPackId.CRYPACK_MAP_32:{
                System.out.println("cryPackParser.parse: CRYPACK_MAP_32");
                int len = (((b[start_pos+1] & 255) << 24) +
                           ((b[start_pos+2] & 255) << 16) +
                           ((b[start_pos+3] & 255) <<  8) +
                           ((b[start_pos+4] & 255) <<  0));
                cryPackMap Map = new cryPackMap();
                int start = start_pos+1+4;
                cryPackParseResult pResult1;
                cryPackParseResult pResult2;
                for (int i = 0; i<len; i++){
                    pResult1 = parse(b, start);
                    pResult2 = parse(b, pResult1.last_pos);
                    Map.put(pResult1.obj, pResult2.obj);
                    start = pResult2.last_pos;
                }
                obj = new cryPackParseResult(Map, start);
                break;
            }
            default: obj = null;
        }
        return obj;
    }

    private static Object parseInteger(byte[] b){
        long v;
        v = (((long)b[0] << 56) +
                ((long)(b[1] & 255) << 48) +
                ((long)(b[2] & 255) << 40) +
                ((long)(b[3] & 255) << 32) +
                ((long)(b[4] & 255) << 24) +
                ((b[5] & 255) << 16) +
                ((b[6] & 255) <<  8) +
                ((b[7] & 255) <<  0));
        return Long.valueOf(v);
    };

    private static Object parseFloat(byte[] b){
        long v = (((long)b[0] << 56) +
                ((long)(b[1] & 255) << 48) +
                ((long)(b[2] & 255) << 40) +
                ((long)(b[3] & 255) << 32) +
                ((long)(b[4] & 255) << 24) +
                ((b[5] & 255) << 16) +
                ((b[6] & 255) <<  8) +
                ((b[7] & 255) <<  0));
        return Double.valueOf(v);
    };


    private static Object parseString(byte[] b){
        final Charset UTF8_CHARSET = Charset.forName("UTF-8");
        return new String(b, UTF8_CHARSET);
    }
}
