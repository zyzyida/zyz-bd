package com.zyzyida.common.schemas;

import com.google.gson.Gson;
import com.zyzyida.common.model.MetricEvent;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Desc: MetricSchema: 支持序列化和反序列化
 * Created by zhouyizhe on 2019-08-14
 */
public class MetricSchema implements DeserializationSchema<MetricEvent>, SerializationSchema<MetricEvent> {
    private static final Gson gson = new Gson();

    @Override
    public MetricEvent deserialize(byte[] bytes) throws IOException {
        return gson.fromJson(new String(bytes), MetricEvent.class);
    }

    @Override
    public boolean isEndOfStream(MetricEvent metricEvent) {
        return false;
    }

    @Override
    public byte[] serialize(MetricEvent metricEvent) {
        return gson.toJson(metricEvent).getBytes(Charset.forName("UTF-8"));
    }

    @Override
    public TypeInformation<MetricEvent> getProducedType() {
        return TypeInformation.of(MetricEvent.class);
    }

}