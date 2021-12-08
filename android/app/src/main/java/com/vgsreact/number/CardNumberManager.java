package com.vgsreact.number;

import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;

import androidx.core.content.ContextCompat;

import com.vgsreact.OnCreateViewInstanceListener;
import com.vgsreact.R;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.google.android.material.textfield.TextInputLayout;
import com.verygoodsecurity.vgscollect.view.card.BrandParams;
import com.verygoodsecurity.vgscollect.view.card.CardBrand;
import com.verygoodsecurity.vgscollect.view.card.validation.payment.ChecksumAlgorithm;
import com.verygoodsecurity.vgscollect.widget.VGSCardNumberEditText;
import com.verygoodsecurity.vgscollect.widget.VGSTextInputLayout;

public class CardNumberManager extends ViewGroupManager<VGSTextInputLayout> {
    public static final String FIELD_NAME = "card_number";
    private VGSCardNumberEditText editText;
    private VGSTextInputLayout vgsTextInputLayout;

    private OnCreateViewInstanceListener listener;

    CardNumberManager(OnCreateViewInstanceListener listener) {
        super();
        this.listener = listener;
    }

    @Override
    public String getName() {
        return "VGSCardTextField";
    }

    @Override
    protected VGSTextInputLayout createViewInstance(ThemedReactContext reactContext) {
        createVGSTextInputLayout(reactContext);
        createVGSCardNumberEditText(reactContext);

        return vgsTextInputLayout;
    }

    private void createVGSTextInputLayout(ThemedReactContext reactContext) {
        vgsTextInputLayout = new VGSTextInputLayout(reactContext);
        vgsTextInputLayout.setBoxCornerRadius(5,5,5,5);
        vgsTextInputLayout.setHintEnabled(false);
        vgsTextInputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
        vgsTextInputLayout.setHint("Card number");
    }

    private void createVGSCardNumberEditText(ThemedReactContext reactContext) {
        editText = new VGSCardNumberEditText(reactContext);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        editText.setIsRequired(false);
        editText.setCardBrandIconGravity(Gravity.END);
        editText.setFieldName(FIELD_NAME);
        editText.setDivider('\0');
        editText.setPadding(10,10,10,10);

        vgsTextInputLayout.addView(editText);

        listener.onCreateViewInstance(editText);
    }


    public VGSCardNumberEditText getEditTextInstance() { // <-- returns the View instance
        return editText;
    }

    public String getFieldName() { // <-- returns the View instance
        return FIELD_NAME;
    }
}