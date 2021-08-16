import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

/**
 * 向 HDFS 上传任意文本文件，若指定文件已存在，由用户判断执行“追加”还是“覆盖”。
 */
public class Task1 {

    public static void putFile(String src, String dst, boolean overwrite, Configuration conf) {
        FSDataInputStream inputStream = null;
        FSDataOutputStream outputStream = null;
        try {
            // 获取输入流（待上传文件）
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
                if (!overwrite) {
                    System.out.println("指定文件已存在，不允许覆盖写，退出...");
                    return;
                }
                if (!fsSrc.getFileStatus(pathDst).isFile()) {
                    throw new IOException("已存在目录文件");
                }
                outputStream = fsDst.append(pathDst);
            } else {
                outputStream = fsDst.create(pathDst);
            }

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
