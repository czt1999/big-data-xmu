import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

public class Main {

    // HBase 的配置信息
    private static Configuration conf;

    // HBase 连接
    private static Connection connection;

    // HBase 的表信息
    private static Admin admin;

    public static void main(String[] args) {
        try {
            init();

            // Task1.showTables(admin);
            Task1.showRecord("t2", admin);
            // Task1.addColumnFamily("t1", "java", admin);
            // Task1.deleteColumnFamily("t1", "java", admin);
            // Task1.count("t1", admin);
            // Task1.truncate("t1", admin);
            // Task1.count("t1", admin);

            // Task2.createTable("t2", new String[]{"f1", "f2", "f3"}, admin);
            // Task2.addRecord("t2", "1001",
            //        new String[]{"f1:c1", "f1:c10", "f2:c2", "f3:c3"},
            //        new String[]{"v1", "v10", "v2", "v3"}, admin);
            // Task2.scanColumn("t2", "f1", admin);
            // Task2.modifyData("t2", "1001", "f1:c1", "v10", admin);
            // Task2.deleteRow("t2", "1001", admin);

            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化，建立连接
     */
    public static void init() throws IOException {
        conf = HBaseConfiguration.create();
        conf.set("hbase.rootdir", "hdfs://192.168.3.9:9000/hbase");
        conf.set("hbase.zookeeper.quorum", "192.168.3.9"); // 默认IP: 127.0.0.1 默认端口: 2181
        connection = ConnectionFactory.createConnection(conf);
        admin = connection.getAdmin();
    }

    /**
     * 关闭连接
     */
    public static void close() throws IOException {
        if (null != admin) {
            admin.close();
        }
        if (null != connection) {
            connection.close();
        }
    }

}