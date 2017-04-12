package cn.com.modernmedia.corelib.model;

import org.json.JSONObject;

public class ErrorMsg extends Entry {
    private static final long serialVersionUID = 1L;
    private int no = -1;
    private String desc;

    public ErrorMsg() {
    }

    public void parse(JSONObject j) {
        if (j == null) return;
        setNo(j.optInt("no"));
        setDesc(j.optString("desc"));
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
