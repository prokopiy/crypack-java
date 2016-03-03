package com.prokopiy.crypack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
* Created by Прокопий on 30.08.2015.
*/
public class cryPackArray extends ArrayList implements List {


  
  public static String toCryPackString(List list) {
    if(list == null) {
      return "null";
    } else {
      boolean first = true;
      StringBuffer sb = new StringBuffer();
      Iterator iter = list.iterator();
      sb.append('[');

      while(iter.hasNext()) {
        if(first) {
          first = false;
        } else {
          sb.append(',');
        }

        Object value = iter.next();
        if(value == null) {
          sb.append("null");
        } else {
          sb.append(cryPackValue.toCryPackString(value));
        }
      }
      sb.append(']');
      return sb.toString();
    }
  }

  public String toCryPackString() {
    return toCryPackString(this);
  }

  public String toString() {
    return this.toCryPackString();
  }


  public static byte[] encode (List list) {
    byte[] b = new byte[5];
    b[0] = cryPackId.CRYPACK_ARRAY_32;
    int l = list.size() & 0xff;
    b[1] = (byte) ((l >>> 24) & 0xFF);
    b[2] = (byte) ((l >>> 16) & 0xFF);
    b[3] = (byte) ((l >>>  8) & 0xFF);
    b[4] = (byte) ((l >>>  0) & 0xFF);

    if((list == null)|(l==0)) {
      return b;
    } else {
      byte[] b1;
      byte[] v;
      for (int i = 0; i < list.size(); i++) {
        b1 = b;
        v = cryPackValue.encode(list.get(i));
        b = cryPackValue.concatarray(b1, v);
      }
      return b;
    }
  }

  public byte[] encode() {
    return encode(this);
  }


}
