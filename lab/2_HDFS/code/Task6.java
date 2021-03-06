import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
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
        Task6.deleteFile(hdfsDir, conf);
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
                System.out.println(hdfsDir + " >> 已存在");
                return;
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

    /**
     * @param hdfsDir hdfs文件路径
     * @param conf    org.apache.hadoop.conf.Configuration
     */
    public static void deleteFile(String hdfsDir, Configuration conf) {
        try {
            Path hdfsPath = new Path(hdfsDir);
            FileSystem hdfs = FileSystem.get(conf);

            if (!hdfs.exists(hdfsPath)) {
                System.out.println(hdfsDir + " >> 不存在");
                return;
            }

            FileStatus status = hdfs.getFileStatus(hdfsPath);
            if (!status.isFile()) {
                System.out.println(hdfsDir + " >> 不是文件");
                return;
            }

            boolean delete = hdfs.delete(hdfsPath, false); // 第二个参数的意义为是否递归删除

            System.out.println(hdfsDir + " >> 删除" + (delete ? "成功" : "失败"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
