package wzp.libs.utils;

import android.net.Uri;

import java.security.MessageDigest;


/**
 * 加密工具
 */
public class EncryptionUtils {

    /**
     * Don't let anyone instantiate this class.
     */
    private EncryptionUtils() {
        throw new Error("Do not need instantiate!");
    }

    // == ----------------------------------------- ==

    /**
     * 对url进行编码(比如url中含有中文，需要进行编码)
     * @param url  需要编码的url
     * @return
     */
    public static String urlEncode(String url) {
        return Uri.encode(url, "-![.:/,%?&=]");
    }


    /**
     * 对文本内容进行编码（比如文本中含有表情符号，需要进行编码）
     * @param content 需要编码的内容
     * @return
     */
    public static String textEncode(String content){
        return Uri.encode(content);
    }

    /**
     * 解码
     * @param content  需要解码的内容
     * @return
     */
    public static String decode(String content){
        return Uri.decode(content);
    }

    // == ----------------------------------------- ==

    /**
     * MD5加密(32位 小写)
     * @param inStr 要加密的字符串
     * @return 加密后的字符串
     */
    public static String stringToMD5(String inStr){
        MessageDigest md5 = null;
        try{
            md5 = MessageDigest.getInstance("MD5");
        }catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++){
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}

