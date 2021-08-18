import org.apache.commons.io.FileExistsException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

/**
 * 给定 HDFS 中一个目录，输出该目录下所有文件的读写权限、大小、创建时间、路径等信息，
 * 若该文件是目录，则递归输出该目录下所有文件的相关信息
 */
public class Task5 {

    public static void example(Configuration conf) {
        Task5.showInfo("hdfs://localhost:9000/", conf);
    }

    /**
     * @param hdfsDir hdfs文件路径
     * @param conf    org.apache.hadoop.conf.Configuration
     */
    public static void showInfo(String hdfsDir, Configuration conf) {
        try {
            Path hdfsPath = new Path(hdfsDir);
            FileSystem hdfs = FileSystem.get(conf);

            if (!hdfs.exists(hdfsPath)) {
                throw new FileExistsException(hdfsDir + " does not exist");
            }

            showInfo(hdfs, hdfsPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showInfo(FileSystem fs, Path path) throws IOException {
        FileStatus status = fs.getFileStatus(path);
        // 输出信息
        Task4.showInfo(status);
        System.out.println("=======================");
        // 如果是目录则递归处理
        if (status.isDirectory()) {
            for (FileStatus s : fs.listStatus(path)) {
                Task5.showInfo(fs, s.getPath());
            }
        }
    }
}
