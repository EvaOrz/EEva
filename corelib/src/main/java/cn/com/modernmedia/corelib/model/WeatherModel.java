package cn.com.modernmedia.corelib.model;

/**
 * Created by Eva. on 17/4/6.
 */

public class WeatherModel extends Entry {

    private String icon;
    private String alarm;
    private String desc;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public WeatherModel() {
    }

}
