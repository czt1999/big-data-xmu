import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * 向 HDFS 指定文件追加内容，由用户指定将内容追加到原文件的开头或结尾
 */
public class Task8 {

    public static void example(Configuration conf) {
        String hdfsDir = "hdfs://localhost:9000/user/czt/profile";
        String content = "~~~~~~~~~~~~\nHello, HDFS!\n~~~~~~~~~~~~\n";
        Task8.appendFile(hdfsDir, content, true, conf);
    }

    /**
     * @param hdfsDir hdfs文件路径
     * @param content 追加内容
     * @param end     是否追加到末尾
     * @param conf    org.apache.hadoop.conf.Configuration
     */
    public static void appendFile(String hdfsDir, String content, boolean end, Configuration conf) {
        FSDataInputStream input = null;
        FSDataOutputStream output = null;

        try {
            Path hdfsPath = new Path(hdfsDir);
            FileSystem hdfs = FileSystem.get(conf);

            // 追加到末尾
            if (end) {
                output = hdfs.append(hdfsPath);
                output.writeBytes(content);
                output.close();
                return;
            }

            // 追加到开头

            // 1) 将原有内容复制到本地
            String tmpLocalDir = "/tmp/hadoop/hdfs/"
                    + UUID.nameUUIDFromBytes(hdfsDir.getBytes(StandardCharsets.UTF_8));
            Path tmpLocalPath = new Path(tmpLocalDir);
            FileSystem local = FileSystem.getLocal(conf);

            input = hdfs.open(hdfsPath);
            output = local.create(tmpLocalPath);
            IOUtils.copyBytes(input, output, 4096);

            IOUtils.closeStreams(output, input);

            // 2) 让本地待追加内容覆盖掉HDFS原文件
            output = hdfs.create(hdfsPath, true);
            output.writeBytes(content);

            // 3) 将本地临时文件追加到HDFS上
            input = local.open(tmpLocalPath);
            IOUtils.copyBytes(input, output, 4096);

            IOUtils.closeStreams(output, input);

            // 4) 删除本地临时文件
            local.delete(tmpLocalPath);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStreams(output, input);
        }
    }
}
