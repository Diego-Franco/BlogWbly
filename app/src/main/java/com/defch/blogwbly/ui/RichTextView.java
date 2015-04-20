package com.defch.blogwbly.ui;

import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

//TODO implement RichTextView
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

    public SpannableStringBuilder boldText(String s) {
        SpannableStringBuilder span = new SpannableStringBuilder();
        if(isBold()) {
            span.setSpan(new StyleSpan(Typeface.BOLD), 0, s.length() ,  0);
        }
        return span;
    }

    public SpannableStringBuilder italicText(String s) {
        SpannableStringBuilder span = new SpannableStringBuilder();
        if(isItalic()) {
            span.setSpan(new StyleSpan(Typeface.ITALIC), 0, s.length(), 0);;
        }
        return span;
    }

    public SpannableStringBuilder underlineText(String s) {
        SpannableStringBuilder span = new SpannableStringBuilder(s);
        if(isUnderline()) {
            span.setSpan(new UnderlineSpan(), 0, s.length(), 0);
        }
        return span;
    }

    public Spannable sizeText(String s, float size) {
        Spannable span = new SpannableString(s);
        if(isSizeOption()) {
            span.setSpan(new RelativeSizeSpan(size), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return span;
    }

    public Spanned getHtmlText(String s) {
        return Html.fromHtml(s);
    }
}
