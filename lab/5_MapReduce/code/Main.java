import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.GenericOptionsParser;

import java.util.Arrays;
import java.util.Locale;

public class Main {

    public static void main(String[] args) {
        try {
            Configuration conf = new Configuration();
            String[] remainingArgs = (new GenericOptionsParser(conf, args)).getRemainingArgs();
            if (remainingArgs.length < 3) {
                System.err.println("Usage: <job_name> <in> [<in>...] <out>");
                System.exit(1);
            }
            String program = remainingArgs[0].toLowerCase(Locale.ROOT);
            String[] taskArgs = Arrays.copyOfRange(remainingArgs, 1, remainingArgs.length);
            boolean exec = false;
            switch (program) {
                case "merging":
                    exec = TaskMerging.run(conf, taskArgs);
                    break;
                case "sorting":
                    exec = TaskSorting.run(conf, taskArgs);
                    break;
                case "mining":
                    exec = TaskMining.run(conf, taskArgs);
                    break;
                default:
                    System.err.println("Unknown job");
            }
            System.out.println(program + " >> " + (exec ? "SUCCESS" : "FAILED"));
            System.exit(exec ? 0 : 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
