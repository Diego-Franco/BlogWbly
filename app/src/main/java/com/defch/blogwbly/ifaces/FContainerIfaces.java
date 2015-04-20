package com.defch.blogwbly.ifaces;

import android.text.SpannableStringBuilder;
import android.widget.EditText;

/**
 * Created by DiegoFranco on 4/20/15.
 */
public interface FContainerIfaces {

    void receiveText(String string, int id);

    void showTextWithRitchText(SpannableStringBuilder string, EditText editText);


}
