import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

/**
 * 提供一个 HDFS 中的目录路径，对该文件进行创建和删除操作
 * 创建目录时，如果目录文件所在目录不存在则自动创建相应目录
 * 删除目录时，由用户指定当该目录不为空时是否删除
 */
public class Task7 {

    public static void example(Configuration conf) {
        String dirToCreate = "hdfs://localhost:9000/user/czt/tmp/a/b";
        String dirToDelete = "hdfs://localhost:9000/user/czt/tmp/a";
        Task7.createDirectory(dirToCreate, conf);
        Task7.deleteDirectory(dirToDelete, true, conf);
    }

    /**
     * @param hdfsDir hdfs文件路径
     * @param conf    org.apache.hadoop.conf.Configuration
     */
    public static void createDirectory(String hdfsDir, Configuration conf) {
        try {
            Path hdfsPath = new Path(hdfsDir);
            FileSystem hdfs = FileSystem.get(conf);

            if (hdfs.exists(hdfsPath)) {
                System.out.println(hdfsDir + " >> 已存在");
                return;
            }

            // 父目录若不存在会被自动创建
            boolean create = hdfs.mkdirs(hdfsPath);

            System.out.println(hdfsDir + " >> 创建" + (create ? "成功" : "失败"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param hdfsDir hdfs文件路径
     * @param conf    org.apache.hadoop.conf.Configuration
     */
    public static void deleteDirectory(String hdfsDir, boolean force, Configuration conf) {
        try {
            Path hdfsPath = new Path(hdfsDir);
            FileSystem hdfs = FileSystem.get(conf);

            if (!hdfs.exists(hdfsPath)) {
                System.out.println(hdfsDir + " >> 不存在");
                return;
            }

            FileStatus status = hdfs.getFileStatus(hdfsPath);
            if (!status.isDirectory()) {
                System.out.println(hdfsDir + " >> 不是目录");
                return;
            }

            if (!force && hdfs.listStatus(hdfsPath).length > 0) {
                System.out.println(hdfsDir + " >> 不为空，不执行删除操作");
                return;
            }

            boolean delete = hdfs.delete(hdfsPath, true);

            System.out.println(hdfsDir + " >> 删除" + (delete ? "成功" : "失败"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
