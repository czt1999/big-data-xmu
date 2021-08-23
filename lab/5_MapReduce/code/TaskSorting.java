import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 有多个输入文件，每个文件的每行内容为一个整数
 * 要求读取所有文件的整数，进行升序排序，输出到一个新文件中
 * 输出的数据格式为每行两个整数，第一个整数为排序位次，第二个整数为原整数
 * <p>
 * Note：MapReduce框架会自动对Mapper的输出根据key进行排序
 */
public class TaskSorting {

    public static class SortingMapper extends Mapper<LongWritable, Text, IntWritable, IntWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            if (!value.toString().matches("[-|+]?\\d+")) {
                System.err.printf("Wrong input for mapper: [%s. %s]", key, value);
                return;
            }
            context.write(new IntWritable(Integer.parseInt(value.toString())), new IntWritable());
        }
    }

    public static class SortingReducer extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {
        private int index = 0;

        @Override
        protected void reduce(IntWritable key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            for (IntWritable v : values) {
                context.write(new IntWritable(++index), key);
            }
        }
    }

    public static boolean run(Configuration conf, String[] paths)
            throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance(conf, "sorting");
        job.setMapperClass(SortingMapper.class);
        job.setReducerClass(SortingReducer.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(IntWritable.class);
        for (int i = 0; i < paths.length - 1; i++) {
            FileInputFormat.addInputPath(job, new Path(paths[i]));
        }
        FileOutputFormat.setOutputPath(job, new Path(paths[paths.length - 1]));
        return job.waitForCompletion(true); // set verbose to true as to print detail info
    }
}
