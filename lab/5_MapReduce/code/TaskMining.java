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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 给出一个 child-parent 表格
 * 要求挖掘其中的父子关系，输出反映祖孙关系的表格
 */
public class TaskMining {

    public static class MiningMapper extends Mapper<LongWritable, Text, Text, Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            String line = value.toString().trim();
            int blankIndex = line.indexOf(" ");
            if (blankIndex >= 0) {
                String child = line.substring(0, blankIndex);
                String parent = line.substring(blankIndex).trim();
                context.write(new Text(parent), new Text("#" + child));
                context.write(new Text(child), new Text("$" + parent));
            }
        }
    }

    public static class MiningReducer extends Reducer<Text, Text, Text, Text> {

        private Map<String, String> childParent = new HashMap<>();

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            List<String> children = new ArrayList<>();
            List<String> parents = new ArrayList<>();
            for (Text value : values) {
                String valueStr = value.toString();
                if (valueStr.startsWith("#")) {
                    children.add(valueStr.substring(1));
                } else {
                    parents.add(valueStr.substring(1));
                }
            }
            if (!children.isEmpty() && !parents.isEmpty()) {
                for (String child : children) {
                    Text childText = new Text(child);
                    for (String parent : parents) {
                        context.write(childText, new Text(parent));
                    }
                }
            }
        }
    }

    public static boolean run(Configuration conf, String[] paths)
            throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance(conf, "mining");
        job.setMapperClass(MiningMapper.class);
        job.setReducerClass(MiningReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        for (int i = 0; i < paths.length - 1; i++) {
            FileInputFormat.addInputPath(job, new Path(paths[i]));
        }
        FileOutputFormat.setOutputPath(job, new Path(paths[paths.length - 1]));
        return job.waitForCompletion(true); // set verbose to true as to print detail info
    }

}
