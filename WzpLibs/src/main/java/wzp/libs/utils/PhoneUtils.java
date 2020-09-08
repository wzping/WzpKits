package wzp.libs.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 手机相关工具类
 */
public class PhoneUtils {
    private static final String TAG = "PhoneUtils";

    /**
     * Don't let anyone instantiate this class.
     */
    private PhoneUtils() {
        throw new Error("Do not need instantiate!");
    }

    // == ----------------------------------------- ==

    /**
     * 获取手机联系人
     * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
     * <uses-permission android:name="android.permission.READ_CONTACTS" />
     * @return
     */
    public static ArrayList<HashMap<String, String>> getAllContactInfo(Context mContext) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            try {
                Cursor cursor = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
                while (cursor.moveToNext()) {
                    HashMap<String, String> map = new HashMap<>();
                    // 电话号码
                    String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).trim().replaceAll(" ", "");
                    // 手机联系人名字
                    String phoneName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).trim();
                    //电话号码不为空的情况下才去保存
                    if(!StringUtils.isEmpty(phoneNumber)){
                        //如果手机号不为空  ，名字为空的情况下，名字就是手机号码
                        if(StringUtils.isEmpty(phoneName)){
                            map.put(phoneNumber,phoneNumber);
                        }else{
                            map.put(phoneName,phoneNumber);
                        }
//                        LogUtils.d(TAG,"通讯录号码：" + phoneNumber + ",通讯录名字：" + phoneName);
                        // ==
                        list.add(map);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 返回数据
        return list;
    }
}
