package com.shatteredpixel.shatteredpixeldungeon.utils;

public class Debug {
    public boolean debugon = true;

    // Converting values and printing in the log
    public void dlog (int text){
        if(debugon) {
            GLog.d(String.valueOf(text));
        }
    }
    public void dlog (String text){
        if(debugon) {
            GLog.d(text);
        }
    }
}

