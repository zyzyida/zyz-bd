package com.zyzyida.sql;

import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

import java.time.Instant;
import java.util.Random;

/**
 * Desc: 使用 Table / SQL API 根据事件时间和水印对无序流进行排序
 * Created by zhouyizhe on 2019-08-23
 */
public class Sort {
    public static final int OUT_OF_ORDERNESS = 1000;

    public static void main(String args[]) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(1);

        SingleOutputStreamOperator<Event> source = env.addSource(new OutOfOrderEventSource())
                .assignTimestampsAndWatermarks(new TimestampsAndWaterMmarks());
//        source.print();

        Table table = tableEnv.fromDataStream(source, "eventTime.rowtime");
        tableEnv.registerTable("zyzyida", table);
        Table sorted = tableEnv.sqlQuery("select eventTime from zyzyida order by eventTime ASC");

//        sorted.printSchema();
//        tableEnv.toRetractStream(sorted, Row.class).print();

        DataStream<Row> rowDataStream = tableEnv.toAppendStream(sorted, Row.class);
        rowDataStream.print();

//        System.out.println(env.getExecutionPlan()); //把执行计划打印出来
        env.execute("sort streaming data");
    }

    public static class Event {
        long eventTime;

        Event() {
            //构造生成带有事件时间的乱序数据
            this.eventTime = Instant.now().toEpochMilli() + (new Random().nextInt(OUT_OF_ORDERNESS));
        }

        @Override
        public String toString() {
            return "Event{" + "eventTime" + eventTime + "}";
        }
    }

    /**
     * Desc: 数据源，不断地产生事件时间数据
     */
    public static class OutOfOrderEventSource extends RichSourceFunction<Event> {
        private volatile boolean running = true;

        @Override
        public void run(SourceContext<Event> ctx) throws Exception {
            while (running) {
                ctx.collect(new Event());
                Thread.sleep(1);
            }
        }

        @Override
        public void cancel() {
            running = false;
        }
    }

    /**
     * Desc: 数据源，产生水印
     */
    public static class TimestampsAndWaterMmarks extends BoundedOutOfOrdernessTimestampExtractor<Event> {
        public TimestampsAndWaterMmarks() {
            super(Time.milliseconds(OUT_OF_ORDERNESS));
        }

        @Override
        public long extractTimestamp(Event event) {
            return event.eventTime;
        }
    }
}
