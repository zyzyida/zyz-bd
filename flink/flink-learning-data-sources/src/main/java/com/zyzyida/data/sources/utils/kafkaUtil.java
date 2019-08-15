package com.zyzyida.data.sources.utils;

import com.alibaba.fastjson.JSON;
import com.zyzyida.data.sources.model.Metric;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Desc: 通过端口写数据到kafka中
 * Created by zhouyizhe on 2019-08-07
 */
public class kafkaUtil {
    public static final String broker_list = "localhost:9092";
    public static final String topic = "metric";

    public static void writeToKafka() throws InterruptedException {
        Properties props = new Properties();
        props.put("bootstrap.servers", broker_list);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer"); //key 序列化
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer"); //value 序列化
        KafkaProducer producer = new KafkaProducer<String, String>(props);

        Metric metric = new Metric();
        metric.setTimeStamp(System.currentTimeMillis());
        metric.setName("mem");
        Map<String, String> tags = new HashMap<>();
        Map<String, Object> fields = new HashMap<>();

        tags.put("cluster", "zyzyida");
        tags.put("host_ip", "127.0.0.1");

        fields.put("used_percent", 90d);
        fields.put("max", 27244873d);
        fields.put("used", 17244873d);
        fields.put("init", 27244873d);

        metric.setTags(tags);
        metric.setFileds(fields);

        ProducerRecord record=new ProducerRecord<String,String>(topic,null,null,JSON.toJSONString(metric));
        producer.send(record);
        System.out.println("发送数据："+JSON.toJSONString(metric));

        producer.flush();
    }

    public static void main(String[] args) throws InterruptedException{
        while(true){
            Thread.sleep(300);
            writeToKafka();
        }
    }
}
