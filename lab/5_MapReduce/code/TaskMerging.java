import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 对两个输入文件 A 和 B 进行合并和去重
 * 输出新文件 C
 */
public class TaskMerging {

    public static class MergingMapper extends Mapper<LongWritable, Text, Text, Text> {
        /**
         * @param key   the index of line in the input file
         * @param value the full line
         */
        @Override
        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            String line = value.toString().trim();
            int blankIndex = line.indexOf(" ");
            if (blankIndex >= 0) {
                Text keyOut = new Text(line.substring(0, blankIndex));
                Text valOut = new Text(line.substring(blankIndex).trim());
                context.write(keyOut, valOut);
            }
        }
    }

    public static class MergingReducer extends Reducer<Text, Text, Text, Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            Set<String> valueSet = new HashSet<>();
            for (Text value : values) {
                valueSet.add(value.toString());
            }
            for (String value : valueSet) {
                context.write(key, new Text(value));
            }
        }
    }

    public static boolean run(Configuration conf, String[] paths)
            throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance(conf, "merging");
        job.setMapperClass(MergingMapper.class);
        job.setReducerClass(MergingReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        for (int i = 0; i < paths.length - 1; i++) {
            FileInputFormat.addInputPath(job, new Path(paths[i]));
        }
        FileOutputFormat.setOutputPath(job, new Path(paths[paths.length - 1]));
        return job.waitForCompletion(true); // set verbose to true as to print detail info
    }

}
