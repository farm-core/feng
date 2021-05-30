package com.yun.hadoop.inputformat;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

/**
 * 自定义InputFormat,我们为了偷懒不写切片方法，就是用FileInputFormat的默认切片方法。
 * <p>
 * 存储的形式如下:
 * Key为:文件路径+文件名称，Value为文件内容。
 * <p>
 * 因此指定key的泛型为Text,指定value为BytesWritable(负责存储一段二进制数值的)。
 */
public class WholeFileInputFormat extends FileInputFormat<Text, BytesWritable> {


    /**
     * 咱们自定义的输入文件不可再被切割。因此返回false即可。因为切割该文件后可能会造成数据损坏。
     */
    @Override
    protected boolean isSplitable(JobContext context, Path filename) {
        return false;
    }

    @Override
    public RecordReader<Text, BytesWritable> createRecordReader(InputSplit split, TaskAttemptContext context) {
        //返回咱们自定义的RecordReader对象
        return new WholeFileRecorder();
    }
}