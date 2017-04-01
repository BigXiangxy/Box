package com.box.lib.utils;

/**
 * 1.EmojiChat操作
 */

public class StringUtil {
    /**
     * 是否是Emoji 表情
     *
     * @param codePoint
     * @return true是Emoji表情
     */
    public static boolean isEmojiChat(char codePoint) {
        boolean isScopeOf = (codePoint == 0x0)  // Emoji 范围
                || (codePoint == 0x9)
                || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF) && (codePoint != 0x263a))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
        return !isScopeOf;
    }

    /**
     * 判断一个字符串中是否包含有Emoji表情
     *
     * @param input
     * @return true 有Emoji
     */
    public static boolean haveEmojiChat(CharSequence input) {
        for (int i = 0; i < input.length(); i++) {
            if (isEmojiChat(input.charAt(i)))
                return true;
        }
        return false;
    }

    /**
     * 去除字符串中的Emoji表情
     *
     * @param source
     * @return
     */
    public static String removeEmojiChat(CharSequence source) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < source.length(); i++) {
            char chat = source.charAt(i);
            if (isEmojiChat(chat)) continue;
            buffer.append(chat);
        }
        return buffer.toString();
    }
}
