package com.zyzyida.data.sources.model;

import java.util.Map;

/**
 * Desc: Metric实体类
 * Created by zhouyizhe on 2019-08-07
 */
public class Metric {
    public String name;
    public long timeStamp;
    public Map<String, Object> fields;
    public Map<String, String> tags;

    public Metric() {
    }

    public Metric(String name, long timeStamp, Map<String, Object> fileds, Map<String, String> tags) {
        this.name = name;
        this.timeStamp = timeStamp;
        this.fields = fileds;
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Metric{" + "name=" + name + '\''
                + ",timeStamp=" + timeStamp + '\''
                + ",fileds=" + fields + '\''
                + ",tags=" + tags + '\''
                + '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Map<String, Object> getFileds() {
        return fields;
    }

    public void setFileds(Map<String, Object> fileds) {
        this.fields = fileds;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

}
