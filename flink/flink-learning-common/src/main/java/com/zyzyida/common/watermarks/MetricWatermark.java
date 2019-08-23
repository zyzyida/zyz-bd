package com.zyzyida.common.watermarks;

import com.zyzyida.common.model.MetricEvent;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import javax.annotation.Nullable;

/**
 * Desc: MetricWatermark:
 * Created by zhouyizhe on 2019-08-14
 */
public class MetricWatermark implements AssignerWithPeriodicWatermarks<MetricEvent> {

    private long currentTimestamp = Long.MIN_VALUE;

    @Override
    public long extractTimestamp(MetricEvent metricEvent, long previousElementTimestamp) {
        if (metricEvent.getTimestamp() > currentTimestamp) {
            this.currentTimestamp = metricEvent.getTimestamp();
        }
        return currentTimestamp;
    }

    @Nullable
    @Override
    public Watermark getCurrentWatermark() {
        long maxTimeLag = 5000;
        return new Watermark(currentTimestamp == Long.MIN_VALUE ? Long.MIN_VALUE : currentTimestamp - maxTimeLag);

    }
}
