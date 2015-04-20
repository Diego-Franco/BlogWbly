package com.defch.blogwbly.ifaces;

import com.defch.blogwbly.ui.RichTextView;

/**
 * Created by DiegoFranco on 4/20/15.
 */
public interface FContainerIfaces {

    void receiveText(String string, int id);

    void showTextWithRitchText(RichTextView richTextView);

}
