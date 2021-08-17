import org.apache.commons.io.FileExistsException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

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
        InputStreamReader reader = null;
        OutputStreamWriter writer = null;

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

            reader = new InputStreamReader(hdfs.open(hdfsPath));
            writer = new OutputStreamWriter(System.out);

            char[] cbuf = new char[1024];
            for (int charsRead = reader.read(cbuf); charsRead >= 0; charsRead = reader.read(cbuf)) {
                writer.write(cbuf, 0, charsRead);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != writer) {
                    writer.close();
                }
                if (null != reader) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
