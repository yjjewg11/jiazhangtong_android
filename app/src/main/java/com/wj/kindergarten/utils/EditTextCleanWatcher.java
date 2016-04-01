package com.wj.kindergarten.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;


public class EditTextCleanWatcher implements TextWatcher {
    private View view = null;
    private String text;
    private EditText editText;

    public EditTextCleanWatcher(View view, EditText editText) {
        this.view = view;
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        text = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!Utils.stringIsNull(s.toString())) {//不能输入表情符号
            if (null != editText && EmojiFilter.containsEmoji(s.toString())) {
                editText.setText(text);
            }

            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0,
                            s.toString().indexOf(".") + 3);
                    editText.setText(s);
                    editText.setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                editText.setText(s);
                editText.setSelection(2);
            }

//            if (s.toString().startsWith("0")
//                    && s.toString().trim().length() > 1) {
//                if (!s.toString().substring(1, 2).equals(".")) {
//                    editText.setText(s);
//                    editText.setSelection(1);
//                    return;
//                }
//            }

        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length() > 0 && !EmojiFilter.containsEmoji(s.toString())) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }
}
