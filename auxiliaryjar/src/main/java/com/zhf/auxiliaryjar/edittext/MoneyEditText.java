package com.zhf.auxiliaryjar.edittext;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 *
 * 自定义可输入金额的EditText
 * 【注意】在布局文件中设置 maxLength=12，是不能限制输入字符的最大长度，需要在代码中设置
 * private MoneyEditText etTradMoney;
 * //即限定最大输入字符数为12
 * etTradMoney.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
 */

public class MoneyEditText extends EditText {
    private static final String TAG = "MoneyEditText";
    private boolean textChange;

    public MoneyEditText(Context context) {
        this(context, null);
    }

    public MoneyEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoneyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置可以输入小数
        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        setFocusable(true);
        setFocusableInTouchMode(true);
        //监听文字变化
        addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!textChange) {
                    restrictText();
                }
                textChange = false;
            }
        });
    }


    /**
     * 将小数限制为2位
     */
    private void restrictText() {
        String input = getText().toString();
        if (TextUtils.isEmpty(input)) {
            return;
        }
        if (input.contains(".")) {
            int pointIndex = input.indexOf(".");
            int totalLenth = input.length();
            int len = (totalLenth - 1) - pointIndex;
            if (len > 2) {
                input = input.substring(0, totalLenth - 1);
                textChange = true;
                setText(input);
                setSelection(input.length());
            }
        }

        if (input.toString().trim().substring(0).equals(".")) {
            input = "0" + input;
            setText(input);
            setSelection(2);
        }

    }

    /**
     * 获取金额
     */
    public String getMoneyText() {
        String money = getText().toString();
        //如果最后一位是小数点
        if (money.endsWith(".")) {
            return money.substring(0, money.length() - 1);
        }
        return money;
    }
}