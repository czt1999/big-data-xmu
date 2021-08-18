import org.apache.commons.io.FileExistsException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 显示 HDFS 中指定的文件读写权限、大小、创建时间、路径等信息
 */
public class Task4 {

    public static void example(Configuration conf) {
        Task4.showInfo("hdfs://localhost:9000/user/czt/profile", conf);
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
            // 输出信息
            showInfo(hdfs.getFileStatus(hdfsPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void showInfo(FileStatus status) {
        System.out.println("读写权限: " + status.getPermission());
        System.out.println("大小: " + status.getLen() + "B");
        System.out.println("块大小: " + status.getBlockSize() + "B");
        System.out.println("修改时间: " + formatter.format(LocalDateTime.ofInstant(
                Instant.ofEpochMilli(status.getModificationTime()), ZoneId.systemDefault())));
        System.out.println("最近访问时间: " + formatter.format(LocalDateTime.ofInstant(
                Instant.ofEpochMilli(status.getAccessTime()), ZoneId.systemDefault())));
        System.out.println("路径: " + status.getPath());
    }
}
