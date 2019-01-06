package cn.flysdk.mock.fileencoding;

import info.monitorenter.cpdetector.io.*;

import java.io.File;

public class FileEncodingUtils {

    /*
     * 代码来自http://blog.csdn.net/yangxiaojun9238/article/details/8250592
     * detector是探测器，它把探测任务交给具体的探测实现类的实例完成。
     * cpDetector内置了一些常用的探测实现类，这些探测实现类的实例可以通过add方法 加进来，如ParsingDetector、
     * JChardetFacade、ASCIIDetector、UnicodeDetector。
     * detector按照“谁最先返回非空的探测结果，就以该结果为准”的原则返回探测到的
     * 字符集编码。使用需要用到三个第三方JAR包：antlr.jar、chardet.jar和cpdetector.jar
     * cpDetector是基于统计学原理的，不保证完全正确。
     */
    private static String getEncodingByPath(String filePath) {
        String fileEncoding=null;
        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
        /*
         * ParsingDetector可用于检查HTML、XML等文件或字符流的编码,构造方法中的参数用于
         * 指示是否显示探测过程的详细信息，为false不显示。
         */
        detector.add(new ParsingDetector(false));
        /*
         * JChardetFacade封装了由Mozilla组织提供的JChardet，它可以完成大多数文件的编码
         * 测定。所以，一般有了这个探测器就可满足大多数项目的要求，如果你还不放心，可以
         * 再多加几个探测器，比如下面的ASCIIDetector、UnicodeDetector等。
         */
        detector.add(JChardetFacade.getInstance());
        // ASCIIDetector用于ASCII编码测定
        detector.add(ASCIIDetector.getInstance());
        // UnicodeDetector用于Unicode家族编码的测定
        detector.add(UnicodeDetector.getInstance());
        java.nio.charset.Charset charset = null;
        File f = new File(filePath);
        try {
            charset = detector.detectCodepage(f.toURI().toURL());
            if (charset != null){
                fileEncoding=charset.name();
            }else{
                fileEncoding=null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return fileEncoding;
    }

    public static void main(String[] args){
        System.out.println(getEncodingByPath("/usr/local/go/src/run.rc"));
    }


}
