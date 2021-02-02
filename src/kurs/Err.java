package kurs;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Err {
    private boolean er = false;
    private String msg = null;
    private Exception e = null;

    public boolean hasError() {
        return er;
    }

    public void set(String msg, Exception e) {
        er = true;
        if (msg == null && e != null) {
            this.msg = e.getMessage();
            //здесь тоже может так случиться, что запишется null
            if (this.msg == null) {
                //можно попробовать хоть что-то записать
                //вот только забыл, как такое смоделировать, но вроде сталкивался где-то
                this.msg = e.toString();
            }

        } else
            if (msg != null)
                this.msg = msg;
        if (e != null)
            this.e = e;
    }

    public void set(String msg) {
        er = true;
        if (msg == null) return;
        this.msg = msg;
    }

    public String getMsg() {
        if (msg == null) return "";
        return msg;
    }

    public String getDetailMsg() {
        if (e == null) return "";
        String s = e.getMessage();
        if (s == null || s.isEmpty()) return "";
        return s;
    }

    public void printStackTrace() {
        if (e == null) return;
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        System.out.println(sw.toString());
    }

    public void printMsg() {
        String s = getMsg();
        if (s == null || s.isEmpty()) return;
        System.out.println(s);
    }

    public void printDetailMsg() {
        String s = getDetailMsg();
        if (s == null || s.isEmpty()) return;
        System.out.println(s);
    }

    public void clear() {
        er = false;
        msg = null;
        e = null;
    }

    public static void clear(Err err) {
        if (err == null) return;
        err.clear();
    }

    public static void set(Err err, String msg) {
        if (err == null) return;
        err.set(msg);
    }

    public static void set(Err err, String msg, Exception e) {
        if (err == null) return;
        err.set(msg, e);
    }
}
