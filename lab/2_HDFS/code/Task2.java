import org.apache.commons.io.FileExistsException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;

import java.io.File;

/**
 * 从 HDFS 下载指定文件，如果本地文件同名，则自动对下载的文件重命名
 */
public class Task2 {

    public static void example(Configuration conf) {
        String hdfsDir = "hdfs://localhost:9000/user/czt/profile";
        String localDIr = "/tmp/hdfs/czt/profile";
        Task2.copyToLocalFile(hdfsDir, localDIr, conf);
    }

    /**
     * @param hdfsDir  源文件名（非目录）
     * @param localDir 目标文件名（非目录）
     * @param conf     org.apache.hadoop.conf.Configuration
     */
    public static void copyToLocalFile(String hdfsDir, String localDir, Configuration conf) {
        FSDataInputStream input = null;
        FSDataOutputStream output = null;

        try {
            Path hdfsPath = new Path(hdfsDir);
            Path localPath = new Path(localDir);

            FileSystem hdfs = FileSystem.get(conf);
            FileSystem local = FileSystem.getLocal(conf);

            if (!hdfs.exists(hdfsPath)) {
                throw new FileExistsException(hdfsDir + " does not exist");
            }

            FileStatus hdfsFileStatus = hdfs.getFileStatus(hdfsPath);
            if (!hdfsFileStatus.isFile()) {
                throw new FileExistsException(hdfsDir + " is not a file");
            }

            if (local.exists(localPath)) {
                // 重命名
                String newLocalDir;
                int indexOfSep = localDir.lastIndexOf(File.separator);
                int indexOfDot = localDir.lastIndexOf('.');
                String filename = localDir.substring(indexOfSep + 1);
                String suffix = "";
                if (indexOfSep + 1 < indexOfDot) {
                    // 文件名有后缀
                    suffix = localDir.substring(indexOfDot);
                    filename = filename.substring(0, filename.length() - suffix.length());
                }
                newLocalDir = localDir.substring(0, indexOfSep + 1) + filename + "~" + suffix;
                localPath = new Path(newLocalDir);
            }

            // 打开输入流
            input = hdfs.open(hdfsPath);

            // 打开输出流
            output = local.create(localPath);

            // 复制数据
            IOUtils.copyBytes(input, output, 4096);

            System.out.println("下载成功 >> " + localPath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStreams(output, input);
        }
    }
}
