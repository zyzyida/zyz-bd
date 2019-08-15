package com.zyzyida.connectors.kafka;

import com.zyzyida.common.model.MetricEvent;
import com.zyzyida.common.schemas.MetricSchema;
import com.zyzyida.common.utils.KafkaConfigUtil;
import com.zyzyida.common.utils.ExecutionEnvUtil;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;

/**
 * Desc: 通过flink写数据到kafka中
 * Created by zhouyizhe on 2019-08-15
 */
public class Main {
    public static void main(String[] args) throws Exception {
//        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        MetricEvent metricEvent=new MetricEvent();
//        DataStreamSource<MetricEvent> data = KafkaConfigUtil.buildSource(env);
//
//        data.addSink(new FlinkKafkaProducer011<>(
//
//        ))
////
////        data.addSink(new FlinkKafkaProducer011<Metrics>(
////                parameterTool.get("kafka.sink.brokers"),
////                parameterTool.get("kafka.sink.topic"),
////                new MetricSchema()
////        )).name("flink-connectors-kafka")
////                .setParallelism(parameterTool.getInt("stream.sink.parallelism"));
////
////        env.execute("flink learning connectors kafka");

        final ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);
        DataStreamSource<MetricEvent> data = KafkaConfigUtil.buildSource(env);

        data.addSink(new FlinkKafkaProducer011<>(
                parameterTool.get("kafka.sink.brokers"),
                parameterTool.get("kafka.sink.topic"),
                new MetricSchema()
        )).name("flink-connectors-kafka")
                .setParallelism(parameterTool.getInt("stream.sink.parallelism"));

        env.execute("flink learning connectors kafka");

    }
}
