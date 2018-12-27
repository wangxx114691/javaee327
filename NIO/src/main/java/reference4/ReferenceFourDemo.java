package reference4;

import org.junit.Test;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class ReferenceFourDemo {
    @Test
    public void testStrongRef(){
        User user = new User("zhangfei");
        User strongRef = user;  // 强引用
        user = null;
        System.gc();    // 垃圾回收
        try {
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Strong:" + strongRef.getName());   // Strong:zhangfei
        // 结果说明强引用， 如果user被置空， 但是堆空间中的对象并没有变为空，
        // 所以即使调用gc依旧strongRef依旧有对应的对应堆空间作为对象存储

    }

    @Test
    public void testStrong2(){
        //------测试发现，String不会被回收------
            String str = "hello";
            String strongRef = str;    //强引用
            str = null;
            System.gc();            //垃圾回收
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Strong: " + strongRef); //Strong: hello
    }

    @Test
    public void testWeakRef(){
        User user = new User("zhangfei");
        WeakReference<User> weakRef = new WeakReference<User>(user);
        System.out.println("weakRef: "+weakRef.get().getName());//weakRef: zhangfei
        user = null;
        System.gc();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("weakRf: "+ weakRef.get());//weakRf: null

        //-----------测试发现String不会被回收---------
//        String str = "hello";   // 这种是存在常量池中， 即使弱引用也不会被回收
        String str = new String("hello");   // 在堆中， 变为弱引用就会被回收
        ReferenceQueue stringReference = new ReferenceQueue<>();
        WeakReference<String> wr = new WeakReference<String>(str,stringReference);
        System.out.println("weakRef: " + wr.get());//weakRef: hello
        str = null;
        System.gc();
        System.gc();
        System.gc();
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            // TODO: handle exception
        }
        System.out.println("weakRef: " + wr.get());//weakRef: hello
    }

    @Test
    public void testSoftRef(){
//        User user = new User("pan");
//        SoftReference<User> softRef = new SoftReference<>(user);
//        System.out.println("SoftRef: "+softRef.get().getName());//SoftRef: pan
//        user = null;
//        System.gc();
//        try {
//            Thread.sleep(1000);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        // 非空， 说明软引用并不会直接被gc回收
//        System.out.println("SoftRef: " + softRef.get());//SoftRef: reference4.User@22927a81


        User user = new User("pan");
//        SoftReference<User> softRef = new SoftReference<>(user);
        ReferenceQueue referenceQueue = new ReferenceQueue();
        WeakReference<User> userWeakReference = new WeakReference<>(user,referenceQueue);
//        System.out.println("SoftRef: "+softRef.get().getName());//SoftRef: pan
        System.out.println("userWeakRef: "+userWeakReference.get().getName());//SoftRef: pan
        user = null;
        System.gc();
        try {
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        // 非空， 说明软引用并不会直接被gc回收
//        System.out.println("SoftRef: " + softRef.get());//SoftRef: reference4.User@22927a81
        System.out.println("userWeakRef: "+userWeakReference.get());
    }

}
