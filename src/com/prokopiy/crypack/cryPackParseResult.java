package com.prokopiy.crypack;

/**
 * Created by Даниил on 27.08.2015.
 */
public class cryPackParseResult {
    public Object obj;
    public int last_pos;

    public cryPackParseResult(Object o, int lp){
        obj = o;
        last_pos = lp;
    }
}
