package com.zyzyida.window;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.ProcessingTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * Desc: flink window: 用来对一个无限的流设置一个有限的集合，在有界的数据集上进行操作的一种机制
 * Created by zhouyizhe on 2019-08-14
 */
public class Main {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> data = env.socketTextStream("127.0.0.1", 9000);

        // 基于时间窗口
        data.flatMap(new LineSplitter())
                .keyBy(1)
                .timeWindow(Time.seconds(30))
                .sum(0)
                .print();

        // 基于滑动时间窗口
        data.flatMap(new LineSplitter())
                .keyBy(1)
                .timeWindow(Time.seconds(60),Time.seconds(30))
                .sum(0)
                .print();

        //基于会话时间窗口
        data.flatMap(new LineSplitter())
                .keyBy(1)
                .window(ProcessingTimeSessionWindows.withGap(Time.seconds(5))) //表示如果 5s 内没出现数据则认为超出会话时长，然后计算这个窗口的和
                .sum(0)
                .print();

        env.execute("flink window example");

    }


    public static final class LineSplitter implements FlatMapFunction<String, Tuple2<Long, String>> {
        @Override
        public void flatMap(String s, Collector<Tuple2<Long, String>> collector) {
            String[] tokens = s.split(" ");

            if (tokens.length >= 2 && isValidLong(tokens[0])) {
                collector.collect(new Tuple2<>(Long.valueOf(tokens[0]), tokens[1]));
            }
        }
    }

    private static boolean isValidLong(String str) {
        try {
            long _v = Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
//            log.info("the str = {} is not a number", str);
            return false;
        }
    }
}
