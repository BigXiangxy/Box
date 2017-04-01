package com.box.lib.widget.text;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.box.lib.utils.StringUtil;

/**
 * 输入框过滤系统自带表情Watcher
 */
public class FilterTextWatcher implements TextWatcher {
    private EditText et;
    private TextWatcher textWatcher;

    public FilterTextWatcher(EditText et) {
        this.et = et;
    }

    public FilterTextWatcher(EditText et, TextWatcher textWatcher) {
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
        // 退格
        if (count == 0) return;
        CharSequence input = s.subSequence(start, start + count);
        if (StringUtil.haveEmojiChat(input)) { //如果 输入的类容包含有Emoji
            String str = StringUtil.removeEmojiChat(s);
            et.setText(str);
            et.setSelection(str.length());
            return;
        }
        if (textWatcher != null)
            textWatcher.onTextChanged(s, start, before, count);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (textWatcher != null)
            textWatcher.afterTextChanged(s);
    }
}
