package nio;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Scanner;

public class NonBlockNIO {
    @Test
    public void ClientTest() throws Exception{
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 6666));
//        FileChannel fChannel = FileChannel.open(Paths.get("E:\\1.png"));
        sChannel.configureBlocking(false);  //切换为非阻塞式
        ByteBuffer buf = ByteBuffer.allocate(1024);
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()){
            String s = sc.next();
            buf.put(new String(new Date()+"\n"+s).getBytes());
            buf.flip();
            sChannel.write(buf);
            buf.clear();
        }
        sChannel.close();

    }

    @Test
    public void ServerTest()throws Exception{
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        ssChannel.configureBlocking(false); //变为非阻塞式
        ssChannel.bind(new InetSocketAddress(6666));
        Selector selector = Selector.open();
        ssChannel.register(selector,SelectionKey.OP_ACCEPT);

    }

}
