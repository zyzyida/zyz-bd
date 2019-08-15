package com.zyzyida.data.sources.sources;

import com.zyzyida.data.sources.model.Student;
import com.zyzyida.data.sources.utils.MySQLUtil;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Desc: 自定义source，从mysql中读取数据
 * Created by zhouyizhe on 2019-08-11
 */
public class SourceFromMySQL extends RichSourceFunction<Student> {
    PreparedStatement ps;
    private Connection connection;

    /**
     * open()：建立连接，这样不用每次invoke的时候都要建立连接和释放连接。
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        connection = MySQLUtil.getConnection("com.mysql.cj.jdbc.Driver",
                "jdbc:mysql://localhost:3306/ad_data?useUnicode=true&characterEncoding=UTF-8",
                "root",
                "root123456");
        String sql = "select * from Student;";
        ps = this.connection.prepareStatement(sql);
    }

    /**
     * close()：程序执行完毕就可以进行，关闭连接和释放资源的动作了。
     */
    @Override
    public void close() throws Exception {
        super.close();
        if (connection != null) {
            connection.close();//释放连接
        }
        if (ps != null) {
            ps.close();//释放资源
        }
    }

    /**
     * DataStream调用一次run()方法用来获取数据
     */
    @Override
    public void run(SourceContext<Student> ctx) throws Exception {
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            Student student = new Student(resultSet.getInt("id"),
                    resultSet.getString("name").trim(),
                    resultSet.getString("password").trim(),
                    resultSet.getInt("age"));
            ctx.collect(student);
        }
    }

    /**
     * cancel()：
     */
    @Override
    public void cancel() {

    }

}
