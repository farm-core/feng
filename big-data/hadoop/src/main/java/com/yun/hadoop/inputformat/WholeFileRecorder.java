package com.yun.hadoop.inputformat;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

/**
 * 咱们自定义的RecordReader只处理一个文件，把这个文件度成一个KV值。即一次性把一个小文件全部读完。
 */
public class WholeFileRecorder extends RecordReader<Text, BytesWritable> {

    //notRead初始值为true，表示还没有读文件
    private boolean notRead = true;
    //定义key类型
    private Text key = new Text();
    //定义value类型
    private BytesWritable value = new BytesWritable();
    //定义一个输入流对象
    private FSDataInputStream inputStream;
    //定义一个FileSplit对象
    private FileSplit fs;

    /**
     * 初始化方法，框架会在初始化时调用一次。
     *
     * @param split             :定义要读取的记录范围的拆分
     * @param context:关于这项任务的信息
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        //由于咱们的输入流继承自FileInputFormat,因此我们需要将InputSplit强制类型转换为FileSplit，即转换切片类型到文件切片。
        //FileSplit是InputSplit的一个子类,希望你还没有忘记多态的知识点。
        fs = (FileSplit) split;
        //通过切片获取文件路径
        Path path = fs.getPath();
        //通过path获取文件系统
        FileSystem fileSystem = path.getFileSystem(context.getConfiguration());
        //开一个输入流,别忘记在close方法中释放输入流资源
        inputStream = fileSystem.open(path);
    }

    /**
     * 读取下一个键，值对。
     *
     * @return :如果读到了，返回true,读完了就返回false。
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        if (notRead) {
            //具体读文件的过程
            //读key
            key.set(fs.getPath().toString());
            //生成一个跟文件一样长的字节数组
            byte[] buf = new byte[(int) fs.getLength()];
            //一次性将文件内容读取
            inputStream.read(buf);
            //读取value
            value.set(buf, 0, buf.length);
            //notRead设置为false表示已经读取
            notRead = false;
            //在第一次读取时返回true
            return true;
        }
        return false;
    }

    /**
     * 获取当前读到的key
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public Text getCurrentKey() throws IOException, InterruptedException {
        return key;
    }

    /**
     * 获取当前读到的value
     *
     * @throws IOException
     * @throws InterruptedException
     * @return:当前value
     */
    @Override
    public BytesWritable getCurrentValue() throws IOException, InterruptedException {
        return value;
    }

    /**
     * 记录读取器通过其数据的当前进度。
     *
     * @throws IOException
     * @throws InterruptedException
     * @return:返回一个0.0~1.0之间的小数
     */
    @Override
    public float getProgress() throws IOException, InterruptedException {
        return notRead ? 0 : 1;
    }

    /**
     * 关闭记录读取器。
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        //使用hadoop为咱们提供的释放流的工具
        IOUtils.closeStream(inputStream);
    }
}