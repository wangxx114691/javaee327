package nio;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class BlockNIO {
    @Test
    public void ClientTest()throws Exception{
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 6666));
        FileChannel fileChannel = FileChannel.open(Paths.get("E:\\2.png"), StandardOpenOption.READ);
        ByteBuffer bBuf = ByteBuffer.allocate(1024);
        while (fileChannel.read(bBuf)!=-1){
            bBuf.flip();
            socketChannel.write(bBuf);
            bBuf.clear();
        }
        socketChannel.shutdownOutput();
        // 接收服务器数据
        int len = 0;
        while ((len = socketChannel.read(bBuf))!=-1){
            bBuf.flip();
            System.out.println(new String(bBuf.array(),0,len));
            bBuf.clear();
        }
        socketChannel.close();
        fileChannel.close();
    }

    @Test
    public void ServerTest()throws Exception{
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        FileChannel fileChannel = FileChannel.open(Paths.get("E:\\4.png"), StandardOpenOption.WRITE,StandardOpenOption.CREATE);
        ssChannel.bind(new InetSocketAddress(6666));
        SocketChannel sChannel = ssChannel.accept();
        ByteBuffer bBuf = ByteBuffer.allocate(1024);
        while (sChannel.read(bBuf)!=-1){
            bBuf.flip();
            fileChannel.write(bBuf);
            bBuf.clear();
        }
        sChannel.shutdownInput();
        // 向客户端返回数据
        bBuf.put("服务器接收到数据".getBytes());
        bBuf.flip();
        sChannel.write(bBuf);
        fileChannel.close();
        sChannel.close();
        ssChannel.close();
    }
}
