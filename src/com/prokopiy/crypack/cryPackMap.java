package com.prokopiy.crypack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Прокопий on 25.08.2015.
 */

public class cryPackMap extends HashMap implements Map {


    public cryPackMap(){
    }

    public cryPackMap(Map map) {
        super(map);
    }

    public static String toCryPackString(Map map){
        if(map == null) {
            return "null";
        } else {
            StringBuffer sb = new StringBuffer();
            boolean first = true;
            Iterator iter = map.entrySet().iterator();
            sb.append("#{");

            while(iter.hasNext()) {
                if(first) {
                    first = false;
                } else {
                    sb.append(',');
                }

                Entry entry = (Entry)iter.next();
                toCryPackString(String.valueOf(entry.getKey()), entry.getValue(), sb);
            }

            sb.append('}');
            return sb.toString();
        }
    };

    public String toCryPackString() {
        return toCryPackString(this);
    }

    private static String toCryPackString(String key, Object value, StringBuffer sb) {
        if(key == null) {
            sb.append("null");
        } else {
            cryPackValue.escape(key, sb);
        }
        sb.append("=>");
        sb.append(cryPackValue.toCryPackString(value));
        return sb.toString();
    }

    public String toString() {
        return this.toCryPackString();
    }


    public static byte[] encode (Map map) {
        byte[] b = new byte[5];
        b[0] = cryPackId.CRYPACK_MAP_32;
        int l = map.size() & 0xff;
        b[1] = (byte) ((l >>> 24) & 0xFF);
        b[2] = (byte) ((l >>> 16) & 0xFF);
        b[3] = (byte) ((l >>>  8) & 0xFF);
        b[4] = (byte) ((l >>>  0) & 0xFF);
        if(map == null) {
            return b;
        } else {
            byte[] b1;
            byte[] b2;
            byte[] k;
            byte[] v;
            Iterator iter = map.entrySet().iterator();
            while(iter.hasNext()) {
                b1 = b;
                Entry entry = (Entry)iter.next();
                k = cryPackValue.encode(entry.getKey());
                v = cryPackValue.encode(entry.getValue());
                b2 = cryPackValue.concatarray(k, v);
                b = cryPackValue.concatarray(b1, b2);
            }
            return b;
        }
    }

    public byte[] encode() {
        return encode(this);
    }

}
