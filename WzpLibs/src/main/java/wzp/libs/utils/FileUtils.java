package wzp.libs.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件相关工具类
 * (使用时记得加权限)
 *
 * File的三个构造函数
 * new File(String path) : 传递路径名，path可以写到文件夹，也可以写到一个文件
 * new File(String parent,String child) : 传递字符串父路径，字符串子路径（好处：单独操作，方便）/ child可以写到文件，也可以写到文件夹，写到文件的话就可以直接往里写数据了
 * new File(File parent,String child) : 传递file父路径，字符串子路径（好处：父路径可以直接传递File类方法）/ child可以写到文件，也可以写到文件夹，写到文件的话就可以直接往里写数据了
 *
 * File.separator 代表的是 ("C:/tmp/test.txt") 中的"/"
 */
public class FileUtils {

    /**
     * Don't let anyone instantiate this class.
     */
    private FileUtils() {
        throw new Error("Do not need instantiate!");
    }

    // == ----------------------------------------- ==

    /**
     * 通过路径获取文件
     * @param filePath
     * @return
     */
    public static File pathToFile(String filePath){
        return filePath != null ? new File(filePath) : null;
    }

    /**
     * @param fPath 目录（文件夹）
     * @param fName 文件名
     * @return
     */
    public static File pathToFile(String fPath,String fName){
        // 防止不存在该目录，不存在则创建
        createFolder(fPath);
        return new File(fPath + fName);
        //返回后，fName这个文件名的文件是不存在的
        //但是这个 File 对象，可以直接写入数据,写到 fName 这个文件里面
    }

    /**
     * 将file转换成Uri
     * @param mContext
     * @param file
     */
    public static Uri fileToUri(Context mContext, File file){
        if (file == null) return null;
        // 7.0以后才会需要进行处理
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){   //24
            //生成Uri
            return FileProvider.getUriForFile(mContext,mContext.getPackageName()+".fileProvider",file);
        }else{
            return Uri.fromFile(file);
        }
    }

    // == ----------------------------------------- ==

    /**
     * 传入对应的文件大小Long,返回转换后文件大小
     * @param fileS 这里的单位是Bytes(B)
     * @return 返回String文件大小
     */
    public static String formatFileSize(long fileS) {
        // 转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 获得文件名(无后缀)
     * @param fName 文件全名
     * @return 文件名
     */
    public static String getFileNotSuffix(String fName) {
        String result = null;
        if (fName != null) {
            if (fName.lastIndexOf('.') != -1) {
                result = fName.substring(0,fName.lastIndexOf('.'));
            } else {
                result = fName;
            }
        }
        return result;
    }

    /**
     * 获得文件后缀名
     * @param fName 文件全名
     * @return 后缀名
     */
    public static String getFileSuffix(String fName) {
        String result = null;
        if (fName != null) {
            result = "";
            if (fName.lastIndexOf('.') != -1) {
                result = fName.substring(fName.lastIndexOf('.'));
                if (result.startsWith(".")) {
                    result = result.substring(1);
                }
            }
        }
        return result;
    }

    // == ----------------------------------------- ==

    /**
     * 判断某个文件夹是否创建,未创建则创建
     * @param fPath 文件夹路径
     * 例如：createFolder("/storage/emulated/0/aaaaaaaa")  ——> 在/storage/emulated/0下创建了一个名叫aaaaaaaa的文件夹
     */
    public static File createFolder(String fPath){
        try {
            File file = new File(fPath);
            // 当这个文件夹不存在的时候则创建文件夹
            if(!file.exists()){
                // 允许创建多级目录
                file.mkdirs();         //创建文件夹并且创建缺失的父文件夹
                // 这个无法创建多级目录
                // file.mkdir();      //创建文件夹但不会创建缺失的父文件夹
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建单个文件
     * 例如： 传入"/storage/emulated/0/aaaaaaaa" , 则是在 “/storage/emulated/0” 下创建了一个名叫aaaaaaaa的文件（不是文件夹）
     * 传入 "/storage/emulated/0/aaaaaaaa/bb"  ,如果aaaaaaaa是个文件夹，并且存在，则bb文件可以创建成功，否则失败
     * @param path
     * @return
     */
    public static File createFile(String path) {
        try {
            File file = new File(path);
            file.createNewFile();   //创建单个文件（不是文件夹）
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 判断目录(文件夹)、文件是否存在
     * @param file
     * @return true : 存在
     */
    public static boolean isExists(File file) {
        return file != null && file.exists();
    }

    /**
     * 判断目录(文件夹)、文件是否存在
     * @param fPath 路径
     * @return
     */
    public static boolean isExists(String fPath) {
        try {
            File file = new File(fPath);
            if(file.exists()){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 检查是否存在某个文件
     * @param fPath 文件路径
     * @param fName 文件名
     * @return 是否存在文件
     */
    public static boolean isExists(String fPath, String fName) {
        try {
            File file = new File(fPath,fName); //在fPath路径下名为fName的文件
            if(file.exists()){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除文件
     * @param fPath 路径
     * @return
     */
    public static boolean deleteFile(String fPath){
        try {
            File file = new File(fPath);
            // 文件存在，并且不是目录文件,则直接删除（是文件（不是文件夹），则直接删除）
            if(file.exists() && !file.isDirectory()){
                return file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除文件
     * @param file 操作File对象
     * @return
     */
    public static boolean deleteFile(File file){
        try {
            // 文件存在，并且不是目录文件,则直接删除
            if(file != null && file.exists() && !file.isDirectory()){
                return file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除文件夹下面的文件
     * @param file
     * @param isKeepFolder 是否保留文件夹 ： true - 保留    false - 不保留
     */
    public static void deleteFolderFile(File file,boolean isKeepFolder) {
        if (file==null)
            return;
        if (file.isDirectory()) {  //是目录文件
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFolderFile(f,false);
            }
            if (!isKeepFolder)
                file.delete();
        } else if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 保存文件
     * @param txt 保存内容
     * @param fPath 保存路径
     * @param fName 文件
     * @return 是否保存成功
     * 例如：FileUtils.saveFile("今天是个好日子", "/storage/emulated/0", "aaaaaaaa")
     * 将“今天是个好日子”写入到/storage/emulated/0"路径下的aaaaaaaa文件(aaaaaaaa是个文件，可以不写文件后缀)
     */
    public static boolean saveFile(String txt, String fPath, String fName) {
        try {
            // 防止文件没创建
            createFolder(fPath);
            // 保存路径
            File sFile = new File(fPath, fName);
            // 保存内容到一个文件
            FileOutputStream fos = new FileOutputStream(sFile);  //输出流FileOutputStream把数据写入本地文件(输入流FileInputStream从本地文件读取数据)
            fos.write(txt.getBytes());
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 保存文件
     * @param txt 保存内容
     * @param fPath 保存路径
     * @param fName 文件
     * @param coding 编码
     * @return 是否保存成功
     */
    public static boolean saveFile(String txt, String fPath, String fName, String coding) {
        try {
            // 防止文件没创建
            createFolder(fPath);
            // 保存路径
            File sFile = new File(fPath, fName);
            // 保存内容到一个文件
            FileOutputStream fos = new FileOutputStream(sFile);
            Writer out = new OutputStreamWriter(fos, coding); //OutputStreamWriter将写入的字符编码成字节后写入一个字节流
            out.write(txt);
            out.close();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 复制单个文件
     * @param srcFilePath  待复制的文件地址
     * @param destFilePath 目标文件地址
     * @param overlay      如果目标文件存在, 是否覆盖
     * @return {@code true} success, {@code false} fail
     */
    public static boolean copyFile(final String srcFilePath, final String destFilePath, final boolean overlay) {
        if (srcFilePath == null || destFilePath == null) {
            return false;
        }
        File srcFile = new File(srcFilePath);
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            return false;
        } else if (!srcFile.isFile()) { // srcFile.isDirectory();
            return false;
        }
        // 判断目标文件是否存在
        File destFile = new File(destFilePath);
        // 如果属于文件夹则跳过
        if (destFile.isDirectory()) {
            return false;
        }
        if (destFile.exists()) {
            // 如果目标文件存在并允许覆盖
            if (overlay) {
                // 删除已经存在的目标文件, 无论目标文件是目录还是单个文件
                new File(destFilePath).delete();
            }
        } else {
            // 如果目标文件所在目录不存在, 则创建目录
            if (!destFile.getParentFile().exists()) {
                // 目标文件所在目录不存在
                if (!destFile.getParentFile().mkdirs()) {
                    // 复制文件失败: 创建目标文件所在目录失败
                    return false;
                }
            }
        }
        // 复制文件
        int byteread = 0; // 读取的字节数
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(srcFile);
            os = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            while ((byteread = is.read(buffer)) != -1) {
                os.write(buffer, 0, byteread);
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null)
                    os.close();
                if (is != null)
                    is.close();
            } catch (IOException e) {
            }
        }
    }
}
