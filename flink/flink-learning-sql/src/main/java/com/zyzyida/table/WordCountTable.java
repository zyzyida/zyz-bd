package com.zyzyida.table;

import com.zyzyida.model.WordCount;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.BatchTableEnvironment;
import sun.tools.jconsole.Tab;

/**
 * Desc: Convert DataSets to Tables(Use Table API)
 * Created by zhouyizhe on 2019-08-26
 */
public class WordCountTable {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.createCollectionsEnvironment();
        BatchTableEnvironment tEnv = BatchTableEnvironment.create(env);

        DataSet<WordCount> input=env.fromElements(
                new WordCount("Hello",1),
                new WordCount("zyzyida",1),
                new WordCount("Hello",1));

        Table table=tEnv.fromDataSet(input);

        Table filtered=table.groupBy("word")
                .select("word, count.sum as count")
                .filter("count=1");

        DataSet<WordCount> result = tEnv.toDataSet(filtered, WordCount.class);

        result.print();
    }
}
