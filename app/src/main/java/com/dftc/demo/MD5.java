package com.dftc.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;

/**
 * @author hyy
 * md5 工具类
 */
public class MD5 {

    /**
     * 获取MD5 结果字符串
     *
     * @param source the source
     * @return string
     */
    public static String encode(String source) {
        byte[] src=source.getBytes();
        String s = null;
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(src);
            byte tmp[] = md.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            s = new String(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 获取文件的  MD5 值
     *
     * @param file the file
     * @return file md 5
     */
    public static synchronized String getFileMD5(File file) {

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        if (!file.isFile()) {
            return null;
        }
        String s=null;
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
            byte tmp[] = digest.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            s = new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return s;
    }

    /**
     * **
     * 获取文件的  MD5 值
     *
     * @param inputStream the input stream
     * @return file md 5
     */
    public static String getFileMD5(InputStream inputStream) {

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        String s=null;
        MessageDigest digest = null;
        InputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = inputStream;
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
            byte tmp[] = digest.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            s = new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return s;
    }

    /**
     * 字符串ma5
     *
     * @param s MD5计算前的原始字符串
     * @return string
     */
    public final static String decode(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Decode string.
     *
     * @param file the file
     * @return 16位md5 string
     */
    public static String decode(File file) {
        FileInputStream fis = null;
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");

            fis = new FileInputStream(file);

            byte[] buffer = new byte[2048];

            int length = -1;

            long s = System.currentTimeMillis();

            while ((length = fis.read(buffer)) != -1) {
                md.update(buffer, 0, length);

            }
            byte[] b = md.digest();
            // 16位加密
            return byteToHexString(b);
           // 32位加密
           // return byteToHexString(b);

        } catch (Exception ex) {

            ex.printStackTrace();

            return null;

        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                ex.printStackTrace();

            }

        }

    }
    /** */

    /**

     * 把byte[]数组转换成十六进制字符串表示形式

     * @param tmp    要转换的byte[]

     * @return 十六进制字符串表示形式

     */

    private static String byteToHexString(byte[] tmp) {
        char hexdigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8',

                '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        String s;

        // 用字节表示就是 16 个字节

        char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，

        // 所以表示成 16 进制需要 32 个字符

        int k = 0; // 表示转换结果中对应的字符位置

        for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节

            // 转换成 16 进制字符的转换

            byte byte0 = tmp[i]; // 取第 i 个字节

            str[k++] = hexdigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,

            // >>> 为逻辑右移，将符号位一起右移

            str[k++] = hexdigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换

        }
        s = new String(str); // 换后的结果转换为字符串
        return s;

    }


    /**
     * 读取MD5文件中的
     *
     * @param filePath the file path
     * @return txt md 5 string
     */
    public static String getTxtMd5String(String filePath) {
        StringBuilder sb = new StringBuilder();
            try {
                String encoding = "utf-8";
                File file_md5 = new File(filePath);
                boolean x = file_md5.isFile();
                boolean y = file_md5.exists();
                if (file_md5.isFile() && file_md5.exists()) { //判断文件是否存在
                    InputStreamReader read = new InputStreamReader(new FileInputStream(file_md5), encoding);//考虑到编码格式
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    while ((lineTxt = bufferedReader.readLine()) != null) {
                        sb.append(lineTxt);
                    }
                    read.close();
                }
            } catch (IOException e) {
//                LibLog.eLog("读取MD5", e.toString());
            }
        return sb.toString();
    }


    /**
     * Md 5 sum string.
     *
     * @param filename the filename
     * @return the string
     */
    public static String md5sum(String filename) {
        InputStream fis;
        byte[]buffer = new byte[1024];
        int numRead = 0;
        MessageDigest md5;
        try{
            fis= new FileInputStream(filename);
            md5 = MessageDigest.getInstance("MD5");
            while((numRead=fis.read(buffer)) > 0) {
                md5.update(buffer,0,numRead);
            }
            fis.close();
            return toHexString(md5.digest());
        }catch (Exception e) {
            System.out.println("error");
            return null;
        }
    }

    /**
     * 16进值
     *
     * @param b the b
     * @return string
     */
    public static String toHexString(byte[] b) {  //转化成16进制
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i]& 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8','9', 'A', 'B', 'C', 'D', 'E', 'F' };
}
