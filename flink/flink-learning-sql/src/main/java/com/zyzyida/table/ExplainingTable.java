package com.zyzyida.table;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;

/**
 * Desc: Convert DataSets to Tables(Use Table API)
 * Created by zhouyizhe on 2019-08-26
 */
public class ExplainingTable {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env);

        DataStream<Tuple2<Integer, String>> stream1 = env.fromElements(new Tuple2<>(1, "hello"));
        DataStream<Tuple2<Integer, String>> stream2 = env.fromElements(new Tuple2<>(1, "hello"));

        Table table1 = tEnv.fromDataStream(stream1, "count,word");
        Table table2 = tEnv.fromDataStream(stream2, "count,word");
        Table table = table1.where("LIKE(word,'F%')").unionAll(table2);

        // 可以打出待执行Sql的抽象语法树(Abstract Syntax Tree)、优化后的逻辑计划以及物理计划
        String explantion = tEnv.explain(table);
        System.out.println(explantion);

    }
}
