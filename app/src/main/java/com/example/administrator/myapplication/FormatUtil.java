package com.example.administrator.myapplication;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cai.jia on 2016/11/18.
 */
public class FormatUtil {

    /**
     * 删除末尾的0  12.20000 ->12.2
     * @param value
     * @return
     */
    public static String deleteEndOfZero(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
        return decimalFormat.format(value);
    }

    /**
     * 检查String 是否是整数, 如果不是整数返回0
     * @param s
     * @return
     */
    public static String parseInt2s(String s) {
        return String.valueOf(parseInt(s));
    }

    /**
     * 检查String 是否是整数, 如果不是整数返回0
     * @param s
     * @return
     */
    public static int parseInt(String s) {
        Pattern p = Pattern.compile("[+-]?\\d+$");
        Matcher matcher = p.matcher(s);
        if (matcher.matches()) {
            return Integer.parseInt(s);
        } else {
            return 0;
        }
    }

    /**
     * 检查String 是否是Double类型, 如果不是Double类型返回0
     * @param s
     * @return
     */
    public static double parseDouble(String s) {
        Pattern p = Pattern.compile("[+-]?((0\\.\\d+)|([1-9]\\d*\\.\\d+)|\\d+)");
        Matcher matcher = p.matcher(s);
        double result = 0;
        if (matcher.matches()) {
            result =  Double.parseDouble(s);
        }
        return result;
    }

    /**
     * 检查String 是否是Double类型, 如果不是Double类型返回0 ,并且将Double类型后面多余的0去掉
     * @param s
     * @return
     */
    public static String parseDouble2s(String s) {
        double d = parseDouble(s);
        return deleteEndOfZero(d);
    }
}
