package com.zyzyida.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;

import java.util.Arrays;

/**
 * Desc: 官网 StreamSQLExample Demo
 * Created by zhouyizhe on 2019-08-24
 */
public class StreamSQLExample {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env);

        DataStream<Order> orderA = env.fromCollection(Arrays.asList(
                new Order(1L, "kuaizi", 3),
                new Order(1L, "phone", 4),
                new Order(3L, "bed", 2)));

        DataStream<Order> orderB = env.fromCollection(Arrays.asList(
                new Order(2L, "pen", 3),
                new Order(2L, "bed", 3),
                new Order(4L, "kuaizi", 1)));

        Table tableA = tEnv.fromDataStream(orderA, "user,product,amount");
        tEnv.registerDataStream("OrderB", orderB, "user,product,amount");

//        Table result = tEnv.sqlQuery("SELECT * FROM " + tableA + "WHERE amount>2 UNION ALL" +
//                "select * from OrderB WHERE amount<2");
        Table result = tEnv.sqlQuery("SELECT * FROM " + tableA + " WHERE amount > 2 UNION ALL " +
                "SELECT * FROM OrderB WHERE amount < 2");

        tEnv.toAppendStream(result, Order.class).print();
        env.execute();

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Order {
        public Long user;

        public String product;

        public int amount;
    }
}
