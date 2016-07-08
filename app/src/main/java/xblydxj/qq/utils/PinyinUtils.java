package xblydxj.qq.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by sanpi on 2016/6/30.
 */

public class PinyinUtils {
    public static String getPinyin(String text){
        StringBuilder sb = new StringBuilder();
        //输出格式
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        //去掉音调
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        //转化成大写
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
             //如果c是空格，直接跳过
            if(Character.isWhitespace(c)){
                continue;
            }
            //如果是键盘上直接打出来的字符 A-Z 0-9
            if(-128< c && c <= 127){
                sb.append(c);
            }else {
                try {
                    //多音字 单  DAN SHAN
                    String[] strings = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    sb.append(strings[0]);
                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                    badHanyuPinyinOutputFormatCombination.printStackTrace();
                }
            }
        }
        return  sb.toString();
    }

}
