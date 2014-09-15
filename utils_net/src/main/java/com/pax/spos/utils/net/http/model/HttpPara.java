package com.pax.spos.utils.net.http.model;

/**
 * Created by fable on 14-9-12.
 */
public class HttpPara {
   private int timeout_c;
   private int timeout_r;

    @Override
    public String toString() {
        return "HttpPara{" +
                "timeout_c=" + timeout_c +
                ", timeout_r=" + timeout_r +
                '}';
    }

    public int getTimeout_c() {
        return timeout_c;
    }

    public void setTimeout_c(int timeout_c) {
        this.timeout_c = timeout_c;
    }

    public int getTimeout_r() {
        return timeout_r;
    }

    public void setTimeout_r(int timeout_r) {
        this.timeout_r = timeout_r;
    }
}
