package com.box.lib.widget.text;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * 输入框过滤系统自带表情Watcher
 */
public class RemoveEmojiWatcher implements TextWatcher {
    private EditText et;
    private TextWatcher textWatcher;

    public RemoveEmojiWatcher(EditText et) {
        this.et = et;
    }

    public RemoveEmojiWatcher(EditText et, TextWatcher textWatcher) {
        this.et = et;
        this.textWatcher = textWatcher;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (textWatcher != null)
            textWatcher.beforeTextChanged(s, start, count, after);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //输入的类容
        CharSequence input = s.subSequence(start, start + count);
        // 退格
        if (count == 0) return;
        if (isEmojiCharacter(input)) //如果 输入的类容包含有Emoji
            et.setText(removeEmoji(s));
        et.setSelection(et.getText().toString().length());
        if (textWatcher != null)
            textWatcher.onTextChanged(s, start, before, count);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (textWatcher != null)
            textWatcher.afterTextChanged(s);
    }

    /**
     * 去除字符串中的Emoji表情
     *
     * @param source
     * @return
     */
    private String removeEmoji(CharSequence source) {
        String result = "";
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            if (isEmojiCharacter(c)) {
                continue;
            }
            result += c;
        }
        return result;
    }


    /**
     * 判断一个字符串中是否包含有Emoji表情
     *
     * @param input
     * @return true 有Emoji
     */
    private boolean isEmojiCharacter(CharSequence input) {
        for (int i = 0; i < input.length(); i++) {
            if (isEmojiCharacter(input.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是Emoji 表情
     *
     * @param codePoint
     * @return true 是Emoji表情
     */
    public static boolean isEmojiCharacter(char codePoint) {
        // Emoji 范围
        boolean isScopeOf = (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF) && (codePoint != 0x263a))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
        return !isScopeOf;
    }
}
