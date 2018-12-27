package nio;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ChannelDemo {

        @Test
        public void TestChannel(){
            try {
                // 创建本地IO对象
                FileInputStream fis = new FileInputStream("E:\\OSI的四层.png");
                FileOutputStream fos = new FileOutputStream("E:\\1.png");

                // 获取Channel, 使用第一种的方式
                FileChannel inChannel = fis.getChannel();
                FileChannel outChannel = fos.getChannel();
                    // 使用第二种方式
                //FileChannel open = FileChannel.open();这种方式很麻烦, 很重要是open不知道怎传参options
                    // 第三种方式
                //SeekableByteChannel byteChannel = Files.newByteChannel();   // 这种方式也是参数莫名
                // 开启缓冲区
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                // 循环读写
                while (inChannel.read(buffer)!= -1){
                    buffer.flip();  // 切换状态为写
                    outChannel.write(buffer);
                    buffer.clear(); // 清空缓冲区
                }
                outChannel.close();
                inChannel.close();
                fos.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    // 使用直接缓冲区完成文件的复制(内存映射文件)
    @Test
    public void TestChannel2() throws IOException {
        FileChannel inChannel = FileChannel.open(Paths.get("E:\\OSI的四层.png"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("E:\\3.png"), StandardOpenOption.WRITE,StandardOpenOption.READ,StandardOpenOption.CREATE_NEW);
        // 获取内存映射文件, 效果和直接缓冲区allocateDirect()一样
        MappedByteBuffer inMappedBuff = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());  // 负责读
        MappedByteBuffer outMappedBuff = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());// 负责写
        // 直接对缓冲区进行数据的读写操作
        byte[] bytes = new byte[inMappedBuff.limit()];
        inMappedBuff.get(bytes);
        outMappedBuff.put(bytes);
        inChannel.close();
        outChannel.close();
    }

    @Test   //
    public void TestChannel3() throws Exception{
        FileChannel inChannel = FileChannel.open(Paths.get("E:\\OSI的四层.png"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("E:\\2.png"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE_NEW);

//        inChannel.transferTo(0,inChannel.size(),outChannel);
        outChannel.transferFrom(inChannel,inChannel.size(),0);
        inChannel.close();
        outChannel.close();
    }
}
