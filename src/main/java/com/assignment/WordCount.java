package com.assignment;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class WordCount {

    public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();
        private Set<String> stopWords = new HashSet<>();

        @Override
        protected void setup(Context context) throws IOException {
            Configuration conf = context.getConfiguration();

            Path stopWordsPath = new Path("/assignment/config/stopwords.txt");
            FileSystem fs = FileSystem.get(conf);

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(fs.open(stopWordsPath))
            );

            String line;

            while ((line = reader.readLine()) != null) {
                stopWords.add(line.trim().toLowerCase());
            }

            reader.close();
        }

        @Override
        public void map(Object key, Text value, Context context)
                throws IOException, InterruptedException {

            String[] tokens = value.toString().split("\\s+");

            for (String token : tokens) {
                token = token.replaceAll("[^a-zA-Z]", "").toLowerCase();

                if (!token.isEmpty() && !stopWords.contains(token)) {
                    word.set(token);
                    context.write(word, one);
                }
            }
        }
    }

    public static class IntSumReducer extends Reducer<Text, IntWritable, Text, Text> {

        private boolean firstRun = true;

        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {

            if (firstRun) {
                context.write(new Text("WORD"), new Text("\t| FREQUENCY"));
                context.write(new Text("----------------"), new Text("\t| ---------"));
                firstRun = false;
            }

            int sum = 0;

            for (IntWritable val : values) {
                sum += val.get();
            }

            context.write(key, new Text("\t| " + sum));
        }
    }

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf, "word count with stopwords");
        job.setJarByClass(WordCount.class);

        job.setMapperClass(TokenizerMapper.class);
        job.setReducerClass(IntSumReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
