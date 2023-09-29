package com.roydon.community.utils.string;

import com.blankj.utilcode.util.StringUtils;

import java.util.regex.Pattern;

/**
 * 中国电信号段
 * <p>
 * 133、153、173、177、180、181、189、190、191、193、199
 * <p>
 * 中国联通号段
 * <p>
 * 130、131、132、145、155、156、166、167、171、175、176、185、186、196
 * <p>
 * 中国移动号段
 * <p>
 * 134(0-8)、135、136、137、138、139、1440、147、148、150、151、152、157、158、159、172、178、182、183、184、187、188、195、197、198
 * <p>
 * 中国广电号段
 * <p>
 * 192
 * <p>
 * 其他号段
 * <p>
 * 14号段部分为上网卡专属号段：中国联通145，中国移动147，中国电信149
 * <p>
 * 虚拟运营商：
 * <p>
 * 电信：1700、1701、1702、162
 * 移动：1703、1705、1706、165
 * 联通：1704、1707、1708、1709、171、167
 * 卫星通信：1349、174
 * 物联网：140、141、144、146、148
 * <p>
 * /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/
 */
public class TelephoneUtils {

    private static final int START_INDEX = 3;
    private static final int TELEPHONE_PLAIN_TEXT_LENGTH = 4;
    private static final char ASTERISK = '*';

    /**
     * 手机号码 * 号加密
     *
     * @param telephone
     * @return
     */
    public static String replaceSomeCharByAsterisk(String telephone) {
        if (StringUtils.isEmpty(telephone)) {
            return telephone;
        }
        char[] chars = telephone.toCharArray();
        for (int i = START_INDEX; i < chars.length - TELEPHONE_PLAIN_TEXT_LENGTH; i++) {
            chars[i] = ASTERISK;
        }
        String result = new String(chars);
        return result;
    }

    /**
     * 手机号码正则校验
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if ((phoneNumber != null) && (!phoneNumber.isEmpty())) {
            return Pattern.matches("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$", phoneNumber);
        }
        return false;
    }

}
