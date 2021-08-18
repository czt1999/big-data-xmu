import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileAlreadyExistsException;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

/**
 * 提供一个 HDFS 中的文件路径，对该文件进行创建和删除操作
 * 如果文件所在目录不存在，则自动创建目录
 */
public class Task6 {

    public static void example(Configuration conf) {
        String hdfsDir = "hdfs://localhost:9000/user/czt/tmp/a.txt";
        Task6.createFile(hdfsDir, conf);
    }

    /**
     * @param hdfsDir hdfs文件路径
     * @param conf    org.apache.hadoop.conf.Configuration
     */
    public static void createFile(String hdfsDir, Configuration conf) {
        try {
            Path hdfsPath = new Path(hdfsDir);
            FileSystem hdfs = FileSystem.get(conf);

            if (hdfs.exists(hdfsPath)) {
                throw new FileAlreadyExistsException(hdfsDir + " already exists");
            }

            Path parentPath = hdfsPath.getParent();
            if (!hdfs.exists(parentPath)) {
                hdfs.mkdirs(parentPath);
            }

            boolean create = hdfs.createNewFile(hdfsPath);

            System.out.println(hdfsDir + " >> 创建" + (create ? "成功" : "失败"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
