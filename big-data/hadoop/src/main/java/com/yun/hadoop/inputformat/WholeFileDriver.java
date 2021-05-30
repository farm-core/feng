package com.yun.hadoop.inputformat;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;


import java.io.IOException;

public class WholeFileDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //获取一个Job实例
        Job job = Job.getInstance(new Configuration());

        //设置我们的当前Driver类路径(classpath)
        job.setJarByClass(WholeFileDriver.class);

        //设置自定义的Mapper程序的输出类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(BytesWritable.class);

        //设置自定义的Reducer程序的输出类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(BytesWritable.class);

        //设置咱们自定义的InputFormat类路径(classpath)
        job.setInputFormatClass(WholeFileInputFormat.class);

        //设置输出格式是一个SequenceFile类型文件
        job.setOutputFormatClass(SequenceFileOutputFormat.class);

        //设置输入数据
        FileInputFormat.setInputPaths(job,new Path("F:\\project\\test"));

        //设置输出数据
        FileOutputFormat.setOutputPath(job,new Path("F:\\project\\output"));

        //提交我们的Job,返回结果是一个布尔值
        boolean result = job.waitForCompletion(true);

        //如果程序运行成功就打印"Task executed successfully!!!"
        if(result){
            System.out.println("Task executed successfully!!!");
        }else {
            System.out.println("Task execution failed...");
        }

        //如果程序是正常运行就返回0，否则就返回1
        System.exit(result ? 0 : 1);
    }

}