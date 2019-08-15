package com.zyzyida.examples.streaming.file;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamContextEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Desc: 从文件读取数据 & 数据写入到文件
 * Created by zhouyizhe on 2019-08-01
 */
public class FileSource {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<String> data=env.readTextFile("/Users/zhou/IdeaProjects/zyz-bd/flink/flink-learning-examples/src/main/java/com/zyzyida/examples/streaming/file/input.txt");
        data.print();

        data.writeAsText("/Users/zhou/IdeaProjects/zyz-bd/flink/flink-learning-examples/src/main/java/com/zyzyida/examples/streaming/file/ouput.txt");

        env.execute();

    }
}
