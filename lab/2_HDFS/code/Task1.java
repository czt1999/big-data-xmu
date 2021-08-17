import org.apache.commons.io.FileExistsException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;

/**
 * 向 HDFS 上传任意文本文件，若指定文件已存在，由用户判断执行“追加”还是“覆盖”。
 */
public class Task1 {

    public static void example(Configuration conf) {
        String localDir = "/etc/profile";
        String hdfsDir = "hdfs://localhost:9000/user/czt/profile";
        Task1.copyFromLocal(localDir, hdfsDir, false, conf);
    }

    /**
     * @param localDir  源文件名（非目录）
     * @param hdfsDir   目标文件名（非目录）
     * @param overwrite true-覆盖 false-追加
     * @param conf      org.apache.hadoop.conf.Configuration
     */
    public static void copyFromLocal(String localDir, String hdfsDir, boolean overwrite, Configuration conf) {
        FSDataInputStream inputStream = null;
        FSDataOutputStream outputStream = null;

        try {
            Path localPath = new Path(localDir);
            Path hdfsPath = new Path(hdfsDir);

            FileSystem local = FileSystem.getLocal(conf);
            FileSystem hdfs = FileSystem.get(conf);

            if (!local.exists(localPath)) {
                throw new FileExistsException(localDir + " does not exist");
            }

            FileStatus localFileStatus = local.getFileStatus(localPath);
            if (!localFileStatus.isFile()) {
                throw new FileExistsException(localDir + " is not a file");
            }

            boolean exists;
            if (exists = hdfs.exists(hdfsPath)) {
                FileStatus hdfsFileStatus = hdfs.getFileStatus(hdfsPath);
                if (!hdfsFileStatus.isFile()) {
                    throw new FileExistsException(hdfsDir + " is not a file");
                }
            }

            // 打开输入流
            inputStream = local.open(localPath);

            // 打开输出流
            if (overwrite || !exists) {
                // 覆盖/创建
                outputStream = hdfs.create(hdfsPath, true);
            } else {
                // 追加
                outputStream = hdfs.append(hdfsPath);
            }

            // 复制数据
            IOUtils.copyBytes(inputStream, outputStream, 4096);

            System.out.println("上传成功");

        } catch (IOException e) {
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