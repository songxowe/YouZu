package android.com.changyou.util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * 工具类
 */
public class ChangYouUtil {
    public static final String TAG = "CYUtil";

    public static void toast(Context context,CharSequence text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context,int resId){
        Toast.makeText(context,resId,Toast.LENGTH_SHORT).show();
    }

    private static int MODE = Context.MODE_PRIVATE;

    private static final String PREFERENCE_NAME = "CYSettings";

    public static boolean isLogined(Context context){
        boolean flag = false;
        //读取选项存储的用户信息(类似浏览器的Cookie)
        SharedPreferences preferences = context
                .getSharedPreferences(PREFERENCE_NAME,MODE);
        int id = preferences.getInt("userid",0);
        String username = preferences.getString("username","");

        if (id > 0 && username.length() > 0){
            flag = true;//已登录
        }

        return flag;
    }

    public static void savePreferences(Context context,
                                       int userid,String username,String model,
                                       String realname,String userimgpath){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME,MODE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("userid", userid);
        editor.putString("username", username);
        editor.putString("model", model);
        editor.putString("realname",realname);
        editor.putString("userimgpath",userimgpath);

        editor.commit();
    }

    public static String getUsername(Context context){
        SharedPreferences preferences =
                context.getSharedPreferences(PREFERENCE_NAME, MODE);
        return preferences.getString("username","");
    }

    public static String getRealname(Context context){
        SharedPreferences preferences =
                context.getSharedPreferences(PREFERENCE_NAME, MODE);
        return preferences.getString("realname","");
    }

    public static String getImgPath(Context context){
        SharedPreferences preferences =
                context.getSharedPreferences(PREFERENCE_NAME, MODE);
        return preferences.getString("userimgpath","");
    }

    public static int getUserId(Context context){
        SharedPreferences preferences =
                context.getSharedPreferences(PREFERENCE_NAME, MODE);
        return preferences.getInt("userid",0);
    }

    public static String getModel(Context context){
        SharedPreferences preferences =
                context.getSharedPreferences(PREFERENCE_NAME, MODE);
        return preferences.getString("model","");
    }
}
