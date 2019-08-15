package com.zyzyida.data.sources;

import com.zyzyida.data.sources.sources.SourceFromMySQL;
import org.apache.flink.streaming.api.environment.StreamContextEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Desc: 自定义source，从mysql中读取数据
 * 步骤：运行Main2类。
 * Created by zhouyizhe on 2019-08-11
 */
public class Main2 {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.addSource(new SourceFromMySQL()).print();
        env.execute("Flink add data source");
    }

}
