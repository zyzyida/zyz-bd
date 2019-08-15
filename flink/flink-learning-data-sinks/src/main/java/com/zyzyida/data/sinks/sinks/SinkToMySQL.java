package com.zyzyida.data.sinks.sinks;

import com.zyzyida.data.sinks.model.Student;
import com.zyzyida.data.sinks.utils.MySQLUtil;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Desc: sink 写数据到 mysql中
 * Created by zhouyizhe on 2019-08-12
 */
public class SinkToMySQL extends RichSinkFunction<Student> {
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

    @Override
    /**
     * 每条数据的插入都要调用一次 invoke() 方法
     */
    public void invoke(Student value,Context context) throws Exception{
        ps.setInt(1,value.getId());//组装数据，执行插入操作
        ps.setString(2,value.getName());
        ps.setString(3,value.getPassword());
        ps.setInt(4,value.getAge());
        ps.executeUpdate();
    }

}
