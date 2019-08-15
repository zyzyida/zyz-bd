package com.zyzyida.connectors.mysql.sinks;

import com.zyzyida.connectors.mysql.model.Student;
import com.zyzyida.connectors.mysql.utils.MySQLUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * Desc: 数据批量 sink 写数据到 mysql中
 * Created by zhouyizhe on 2019-08-18
 */
public class SinkToMySQL extends RichSinkFunction<List<Student>> {
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
        String sql = "insert into student(id, name, password, age) values(?, ?, ?, ?);";
        ps = this.connection.prepareStatement(sql);
    }

    /**
     * close()：程序执行完毕就可以进行，关闭连接和释放资源的动作了。
     */
    @Override
    public void close() throws Exception {
        super.close();
        if (connection != null) {
            connection.close();
        }
        if (ps != null) {
            ps.close();
        }
    }

    public void invoke(List<Student> value, Context context) throws Exception {
        for (Student student : value) {
            ps.setInt(1, student.getId());//组装数据，执行插入操作
            ps.setString(2, student.getName());
            ps.setString(3, student.getPassword());
            ps.setInt(4, student.getAge());
            ps.addBatch();
        }
        int[] count = ps.executeBatch();//批量后执行
        System.out.println("成功了插入了" + count.length + "行数据");
    }

}
