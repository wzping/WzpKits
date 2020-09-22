package wzp.libs.utils;

import android.text.Html;
import android.widget.TextView;

/**
 * Html 工具类
 * 原始展示方式:
 * String tipsStr = "<font color=\"#424242\">" + "亲爱的全科医生您好！如需发布相关课程请移步PC端登录全科医生个人中心" + "</font><br/>" +
 * 				"<font color=\"#2cb3f9\">" + HttpConstants.gpPrivateUrl + "</font><br/>" +
 * 				"<font color=\"#424242\">" + "进行相关课程发布！" + "</font>";
 * textview.setText(Html.fromHtml(tipsStr));
 */
public final class HtmlUtils {

    private HtmlUtils() {
    }

    /**
     * 设置 Html 内容
     * @param textView {@link TextView}
     * @param content  Html content
     */
    public static void setHtmlText(final TextView textView, final String content) {
        if (textView != null && content != null) {
            textView.setText(Html.fromHtml(content));
        }
    }

    /**
     * HTML颜色标记
     * @param content 给定的字符串
     * @param color 要设定的颜色
     * @return 返回的结果最后放在 Html.fromHtml();内
     */
    public static String addHtmlColor(String content,String color) {
        return "<font color=\""+ color +"\">" + content + "</font>";
    }

    /**
     * 加粗标记
     * @param content 给定的字符串
     * @return 最后放在 Html.fromHtml();内
     */
    public static String addHtmlBold(String content) {
        return "<b>" + content + "</b>";
    }

    /**
     * 颜色标记,以及加粗
     * @param content 给定的字符串
     * @param color 要设定的颜色
     * @return 最后放在 Html.fromHtml();内
     */
    public static String addHtmlColorAndBlod(String content,String color) {
        return "<b><font color=\""+ color +"\">" + content + "</font></b>";
    }

    /**
     * 下划线
     * @param content 给定的字符串
     * @return 最后放在 Html.fromHtml();内
     */
    public static String addHtmlUnderline(String content) {
        return "<u>" + content + "</u>";
    }

    /**
     * 字体倾斜
     * @param content 给定的字符串
     * @return 最后放在 Html.fromHtml()内;
     */
    public static String addHtmlIncline(String content) {
        return "<i>" + content + "</i>";
    }

    /**
     * 换行
     * @return
     */
    public static String addNewLine(){
        return "<br/>";
    }

    /**
     * 将给定的字符串中所有给定的关键字标色
     * @param source 给定的字符串
     * @param keyword 给定的关键字
     * @param color 需要标记的颜色
     * @return
     */
    public static String keywordTinting(String source, String keyword, String color) {
        return keywordReplaceAll(source, keyword, ("<font color=\"" + color + "\">" + keyword + "</font>"));
    }

    /**
     * 将给定的字符串中所有给定的关键字进行替换内容
     * @param source 给定的字符串
     * @param keyword 给定的关键字
     * @param replacement 替换的内容
     * @return 返回的是带Html标签的字符串，在使用时要通过Html.fromHtml() 转换为Spanned对象再传递给TextView对象
     */
    public static String keywordReplaceAll(String source, String keyword, String replacement) {
        try {
            if(source != null && source.trim().length() != 0){
                if (keyword != null && keyword.trim().length() != 0) {
                    if(replacement != null && replacement.trim().length() != 0){
                        return source.replaceAll(keyword , replacement);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return source;
    }
}
