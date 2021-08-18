import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * 在 HDFS 中将文件从源路径移动到目的路径
 */
public class Task10 {

    public static void example(Configuration conf) {
        String src = "hdfs://localhost:9000/user/czt/tmp/a.txt";
        String dst = "hdfs://localhost:9000/user/czt/a.txt";
        Task10.move(src, dst, conf);
        Task10.move(dst, src, conf);
    }

    /**
     * @param src  源路径
     * @param dst  目的路径
     * @param conf org.apache.hadoop.conf.Configuration
     */
    public static void move(String src, String dst, Configuration conf) {
        try {
            Path srcPath = new Path(src);
            Path dstPath = new Path(dst);
            FileSystem hdfs = FileSystem.get(conf);

            if (!hdfs.exists(srcPath)) {
                System.out.println(src + " 不存在");
                return;
            }

            boolean rename = hdfs.rename(srcPath, dstPath);

            System.out.println(src + " -> " + dst);
            System.out.println(">> 移动" + (rename ? "成功" : "失败"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
