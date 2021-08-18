import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 使用 `java.net.URL` 和 `org.apache.hadoop.fs.FsURLStreamHandlerFactory`
 * 输出 HDFS 指定文件的文本到终端
 */
public class TaskOfURL {

    static {
        // 注册HDFS提供的URL协议
        // 该方法只能被JVM调用一次
        URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
    }

    /**
     * @param hdfsDir hdfs文件路径
     * @param conf    org.apache.hadoop.conf.Configuration
     */
    public static void printText(String hdfsDir, Configuration conf) {
        try (InputStream in = new URL(hdfsDir).openStream()) {
            IOUtils.copyBytes(in, System.out, 4096);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void example(Configuration conf) {
        TaskOfURL.printText("hdfs://localhost:9000/user/czt/profile", conf);
    }
}
