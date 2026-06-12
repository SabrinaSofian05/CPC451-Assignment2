package com.assignment;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.IOException;

/**
 * WordCount Implementation for Hadoop MapReduce.
 * Processes text files to count word frequencies and outputs results in a formatted table. [cite: 191, 192]
 */
public class WordCountOriginal {

    /**
     * MAPPER CLASS
     * The Mapper reads input data line by line and tokenizes it into words.
     * Input types: <Object, Text> (Position in file, Line content)
     * Output types: <Text, IntWritable> (Word, Initial Count of 1) [cite: 194]
     */
    public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        @Override
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            // Split the line into individual words using whitespace as a delimiter
            String[] tokens = value.toString().split("\\s+");
            
            for (String token : tokens) {
                // Preprocessing: Remove non-alphabetic characters and normalize to lowercase [cite: 201]
                token = token.replaceAll("[^a-zA-Z]", "").toLowerCase();
                
                // Emit the word as the key and '1' as the value if the token is valid
                if (!token.isEmpty()) {
                    word.set(token);
                    context.write(word, one);
                }
            }
        }
    }

    /**
     * REDUCER CLASS
     * The Reducer receives a key (Word) and a list of values (counts).
     * It aggregates the counts and formats the final output as a table. 
     * Output types: <Text, Text> (Word, Formatted Frequency String)
     */
    public static class IntSumReducer extends Reducer<Text, IntWritable, Text, Text> {
        private boolean firstRun = true;

        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            // OPTIMIZATION: Write a table header only during the first call of the reduce function
            if (firstRun) {
                context.write(new Text("WORD"), new Text("\t| FREQUENCY"));
                context.write(new Text("----------------"), new Text("\t| ---------"));
                firstRun = false;
            }

            // Aggregate the occurrences for the specific word
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            
            // Format the final output value to include a vertical pipe for a table-like appearance
            context.write(key, new Text("\t| " + sum));
        }
    }

    /**
     * DRIVER CLASS
     * Configures the Hadoop Job, sets the classes, and manages input/output paths. [cite: 196]
     */
    public static void main(String[] args) throws Exception {
        // Create a new Hadoop configuration
        Configuration conf = new Configuration();
        
        // Define the MapReduce job
        Job job = Job.getInstance(conf, "assignment word count table");
        job.setJarByClass(WordCount.class);
        
        // Set the Mapper and Reducer implementations [cite: 201]
        job.setMapperClass(TokenizerMapper.class);
        job.setReducerClass(IntSumReducer.class);
        
        // IMPORTANT: The Combiner is omitted here because the Reducer's output type (Text)
        // differs from the Mapper's output type (IntWritable). [cite: 203]
        
        // Define Mapper output types (Shuffle and Sort phase uses these)
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        
        // Define Final output types (What is written to HDFS)
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // Set input and output directories from command line arguments [cite: 200]
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
        // Wait for job completion and exit
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
