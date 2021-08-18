import org.apache.commons.io.FileExistsException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 将 HDFS 中指定文件的内容输出到终端
 */
public class Task3 {

    public static void example(Configuration conf) {
        String hdfsDir = "hdfs://localhost:9000/user/czt/profile";
        printFile(hdfsDir, conf);
    }

    /**
     * @param hdfsDir hdfs文件路径
     * @param conf    org.apache.hadoop.conf.Configuration
     */
    public static void printFile(String hdfsDir, Configuration conf) {
        FSDataInputStream input = null;

        try {
            Path hdfsPath = new Path(hdfsDir);
            FileSystem hdfs = FileSystem.get(conf);

            if (!hdfs.exists(hdfsPath)) {
                throw new FileExistsException(hdfsDir + " does not exist");
            }

            FileStatus hdfsFileStatus = hdfs.getFileStatus(hdfsPath);
            if (!hdfsFileStatus.isFile()) {
                throw new FileNotFoundException(hdfsDir + " is not a file");
            }

            input = hdfs.open(hdfsPath);

            IOUtils.copyBytes(input, System.out, 4096);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != input) {
                    input.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
