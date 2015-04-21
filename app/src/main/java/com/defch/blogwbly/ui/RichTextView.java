package com.defch.blogwbly.ui;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

/**
 * Created by DiegoFranco on 4/19/15.
 */
public class RichTextView {

    public final static int BOLD = 1;
    public final static int ITALIC = 2;
    public final static int UNDERLINE = 3;
    public final static int SIZE = 4;

    private int option;

    public RichTextView(int option) {
        this.option = option;
    }

    public int getOption() {
        return this.option;
    }

    public boolean isBold() {
        return option == BOLD;
    }

    public boolean isItalic() {
        return option == ITALIC;
    }

    public boolean isUnderline() {
        return option == UNDERLINE;
    }

    public boolean isSizeOption() { return option == SIZE; }

    //RichTextView option = new RichTextView(TextTypeOption.BOLD);//for bold

    public SpannableStringBuilder getBoldText(String s, int start, int end) {
        SpannableStringBuilder span = new SpannableStringBuilder(s);
        if(isBold()) {
            span.setSpan(new StyleSpan(Typeface.BOLD), start, end,  0);
        }
        return span;
    }

    public SpannableStringBuilder getItalicText(String s, int start, int end) {
        SpannableStringBuilder span = new SpannableStringBuilder(s);
        if(isItalic()) {
            span.setSpan(new StyleSpan(Typeface.ITALIC), start, end, 0);;
        }
        return span;
    }

    public SpannableStringBuilder getUnderlineText(String s, int start, int end) {
        SpannableStringBuilder span = new SpannableStringBuilder(s);
        if(isUnderline()) {
            span.setSpan(new UnderlineSpan(), start, end, 0);
        }
        return span;
    }

    public Spannable getSizeText(String s, float size, int start, int end) {
        Spannable span = new SpannableString(s);
        if(isSizeOption()) {
            span.setSpan(new RelativeSizeSpan(size), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return span;
    }
}
