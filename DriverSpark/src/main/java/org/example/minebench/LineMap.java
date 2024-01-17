package org.example.minebench;

public class LineMap {

    private int ver;
    private String prevBlock;
    private int time;
    private String tx;

    public LineMap(int ver, String prevBlock, int time, String tx) {
        this.ver = ver;
        this.prevBlock = prevBlock;
        this.time = time;
        this.tx = tx;
    }

    public int getVer() {
        return ver;
    }

    public String getPrevBlock() {
        return prevBlock;
    }

    public int getTime() {
        return time;
    }

    public String getTx() {
        return tx;
    }
}
