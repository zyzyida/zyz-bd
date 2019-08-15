package com.zyzyida.data.sources.operator;

import akka.stream.impl.fusing.Fold;
import com.alibaba.fastjson.JSON;
import com.zyzyida.data.sources.model.Student;
import com.zyzyida.data.sources.model.Metric;
import com.zyzyida.data.sources.sources.SourceFromMySQL;
import org.apache.flink.api.common.functions.*;
import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.util.Collector;
import org.apache.flink.streaming.api.datastream.DataStream;

import java.util.Properties;
import java.util.ArrayList;
import java.util.List;


/**
 * Desc: data Transformation，使用算子进行flink中数据的转换
 * Flink Data 的常用转换方式：Map、FlatMap、Filter、KeyBy、Reduce、Fold、Aggregations、
 *                  Window、WindowAll、Union、Window Join、Split、Select、Project 等
 * Created by zhouyizhe on 2019-08-13
 */
public class Transformation {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        SingleOutputStreamOperator<Student> student = env.addSource(new SourceFromMySQL());

        // map算子: add 5 in age，将数据进行 map 转换操作
        SingleOutputStreamOperator<Student> map = student.map(new MapFunction<Student, Student>() {
            @Override
            public Student map(Student value) throws Exception {
                Student student1 = new Student();
                student1.id = value.id;
                student1.name = value.name;
                student1.password = value.password;
                student1.age = value.age + 5;
                return student1;
            }
        });
//        map.print();

        // flatMap算子：筛选id为偶数的数据
        SingleOutputStreamOperator<Student> flatmap = student.flatMap(new FlatMapFunction<Student, Student>() {
            @Override
            public void flatMap(Student value, Collector<Student> out) throws Exception {
                if (value.id % 2 == 0) {
                    out.collect(value);
                }
            }
        });
//        flatmap.print();

        // filter算子：根据条件判断出结果
        SingleOutputStreamOperator<Student> filter = student.filter(new FilterFunction<Student>() {
            @Override
            public boolean filter(Student value) throws Exception {
                if (value.id > 5) {
                    return true;
                }
                return false;
            }
        });
//        filter.print();

        // KeyBy：基于 key 对流进行分区。在内部，它使用 hash 函数对流进行分区
        KeyedStream<Student, Integer> keyBy = student.keyBy(new KeySelector<Student, Integer>() {
            @Override
            public Integer getKey(Student value) throws Exception {
                return value.age;
            }
        });
//        keyBy.print();

        // Reduce:返回单个的结果值，并且 reduce 操作每处理一个元素总是创建一个新值
        SingleOutputStreamOperator<Student> reduce = keyBy.reduce(new ReduceFunction<Student>() {
            @Override
            public Student reduce(Student value1, Student value2) throws Exception {
                Student student1 = new Student();
                student1.name = value1.name + value2.name;
                student1.id = (value1.id + value2.id) / 2;
                student1.password = value1.password + value2.password;
                student1.age = (value1.age + value2.age) / 2;
                return student1;
            }
        });
//        reduce.print();

        // Aggregations:DataStream API 支持各种聚合
//        keyBy.sum(0);
//        keyBy.sum("key");
//        keyBy.min(0);
//        keyBy.min("key");
//        keyBy.max(0);
//        keyBy.max("key");
//        keyBy.minBy(0);
//        keyBy.minBy("key");
//        keyBy.maxBy(0);
//        keyBy.maxBy("key");
//        keyBy.print();

        // window:允许按时间或其他条件对现有 KeyedStream 进行分组
        // windowAll:允许对常规数据流进行分组
        // union:将两个或多个数据流结合在一起。 这样就可以并行地组合数据流
        // Window join:可以通过一些 key 将同一个 window 的两个数据流 join 起来

        // split:根据条件将流拆分为两个或多个流
        // select:从拆分流中选择特定流
        SplitStream<Student> split = map.split(new OutputSelector<Student>() {
            @Override
            public Iterable<String> select(Student value) {
                List<String> output = new ArrayList<>();
                if (value.id % 2 == 0) {
                    output.add("even");
                } else {
                    output.add("odd");
                }
                return output;
            }
        });
        DataStream<Student> even = split.select("even");
        even.print();

        // Project:允许从事件流中选择属性子集，并仅将所选元素发送到下一个处理流

        env.execute("Flink add data source");
    }
}
