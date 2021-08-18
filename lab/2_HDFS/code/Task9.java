import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

/**
 * 删除 HDFS 中指定文件
 */
public class Task9 {

    public static void example(Configuration conf) {
        String hdfsDir = "hdfs://localhost:9000/user/czt/profile";
        deleteFile(hdfsDir, conf);
    }

    /**
     * @param hdfsDir hdfs文件路径
     * @param conf    org.apache.hadoop.conf.Configuration
     */
    public static void deleteFile(String hdfsDir, Configuration conf) {
        try {
            Path hdfsPath = new Path(hdfsDir);
            FileSystem hdfs = FileSystem.get(conf);

            boolean delete = hdfs.delete(hdfsPath, true);

            System.out.println(hdfsDir + " >> 删除" + (delete ? "成功" : "失败"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
