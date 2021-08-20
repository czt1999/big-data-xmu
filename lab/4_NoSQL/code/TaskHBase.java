import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;

public class TaskHBase {

    public static void main(String[] args) {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.rootdir", "hdfs://192.168.3.9:9000/hbase");
        conf.set("hbase.zookeeper.quorum", "192.168.3.9"); // 默认IP: 127.0.0.1 默认端口: 2181

        try (Connection connection = ConnectionFactory.createConnection(conf);
             Admin admin = connection.getAdmin()) {
            // 1:
            Table table = connection.getTable(TableName.valueOf("Student"));
            byte[] rowBytes = "scofield".getBytes();
            byte[] familyBytes = "Score".getBytes();
            Put put = new Put(rowBytes)
                    .addColumn(familyBytes, "English".getBytes(), "45".getBytes())
                    .addColumn(familyBytes, "Math".getBytes(), "89".getBytes())
                    .addColumn(familyBytes, "Computer".getBytes(), "100".getBytes());
            table.put(put);
            // 2:
            Get get = new Get(rowBytes).addColumn(familyBytes, "English".getBytes());
            byte[] value = table.get(get).getFamilyMap(familyBytes).get("English".getBytes());
            System.out.println("scofield, Score:English >> " + new String(value));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
