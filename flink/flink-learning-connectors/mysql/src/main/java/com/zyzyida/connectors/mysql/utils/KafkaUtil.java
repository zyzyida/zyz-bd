package com.zyzyida.connectors.mysql.utils;

import com.alibaba.fastjson.JSON;
import com.zyzyida.connectors.mysql.model.Student;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * Desc: 通过端口写数据到kafka中
 * Created by zhouyizhe on 2019-08-12
 */
public class KafkaUtil {
    public static final String broker_list = "localhost:9092";
    public static final String topic = "student";

    public static void writeToKafka() throws InterruptedException {
        Properties props = new Properties();
        props.put("bootstrap.servers", broker_list);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer"); //key 序列化
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer"); //value 序列化
        KafkaProducer producer = new KafkaProducer<String, String>(props);

        for (int i = 40; i < 50; i++) {
            Student student = new Student(i, "zyzyida" + i, "password" + i, 18 + i);
            ProducerRecord record = new ProducerRecord<String, String>(topic, null, null, JSON.toJSONString(student));
            producer.send(record);
            System.out.println("发送数据：" + JSON.toJSONString(student));
        }

        producer.flush();
    }

    public static void main(String[] args) throws InterruptedException {
        writeToKafka();
    }

}
