import org.apache.hadoop.conf.Configuration;

public class Main {

    public static void main(String[] args) {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://localhost:9000");
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        //Task1.example(conf);
        //Task2.example(conf);
        //Task3.example(conf);
    }
}
