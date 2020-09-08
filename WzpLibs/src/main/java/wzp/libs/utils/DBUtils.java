package wzp.libs.utils;

import android.os.Environment;

/**
 * 数据库工具类 (导入导出等)
 */
public final class DBUtils {

    private DBUtils() {
    }

    // 日志 TAG
    private static final String TAG = DBUtils.class.getSimpleName();

    /**
     * 导出数据库
     * @param targetFile  目标文件(比如test.db导出成aaa.db),这个就是aaa.db(全路径的格式)
     * @param dbPath  原数据库地址
     * @param dbName 原数据库名称
     * @return 是否导出成功
     */
    public static boolean startExportDatabase(final String targetFile, String dbPath, final String dbName) {
        // 判断 SDCard 是否挂载
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return false;
        }
        try {
            //dbPath一般为 ： Environment.getDataDirectory() + "/data/" + AppUtils.getPackageName() + "/databases/"
            // Database 文件地址
            String sourceFilePath = dbPath + dbName;
            // 获取结果
            boolean result = FileUtils.copyFile(sourceFilePath, targetFile, true);
            // 返回结果
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
