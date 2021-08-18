import org.apache.hadoop.conf.Configuration;

public class Main {

    public static void main(String[] args) {
        Configuration conf = new Configuration();
        conf.set("fs.default.name", "hdfs://localhost:9000");
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");

        // required by APPEND_FILE
        conf.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        conf.set("dfs.client.block.write.replace-datanode-on-failure.enabled", "true");

        //Task1.example(conf);
        //Task2.example(conf);
        //Task3.example(conf);
        //Task4.example(conf);
        //Task5.example(conf);
        //Task6.example(conf);
        //Task7.example(conf);
        //Task8.example(conf);
        //Task9.example(conf);
        //Task10.example(conf);

        //TaskOfURL.example(conf);
    }
}
