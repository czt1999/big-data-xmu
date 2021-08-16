import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;

/**
 * 从 HDFS 下载指定文件，如果本地文件同名，则自动对下载的文件重命名
 */
public class Task2 {

    public static void getFile(String src, String dst, Configuration conf) {
        FSDataInputStream inputStream = null;
        FSDataOutputStream outputStream = null;
        try {
            // 获取输入流（HDFS上的文件）
            Path pathSrc = new Path(src);
            FileSystem fsSrc = FileSystem.get(URI.create(src), conf);
            if (!fsSrc.exists(pathSrc) && !fsSrc.getFileStatus(pathSrc).isFile()) {
                throw new FileNotFoundException("源文件不存在");
            }
            inputStream = fsSrc.open(pathSrc);

            // 获取输出流
            Path pathDst = new Path(dst);
            FileSystem fsDst = FileSystem.get(URI.create(dst), conf);
            if (fsSrc.exists(pathDst)) {
                // 需要重命名
                String[] tmp = dst.split(File.pathSeparator);
                int index = dst.lastIndexOf(File.pathSeparator);
                String filename = tmp[tmp.length - 1] + "~";
                String newDst = (index >= 0 ? dst.substring(index + 1) : "") + filename;
                pathDst = new Path(newDst);
            }
            outputStream = fsDst.create(pathDst);

            // 复制数据
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
                if (null != outputStream) {
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
