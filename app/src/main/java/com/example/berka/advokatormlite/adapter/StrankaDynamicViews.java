package com.example.berka.advokatormlite.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by berka on 27-Dec-17.
 */

public class StrankaDynamicViews {

    Context context;

    public StrankaDynamicViews(Context context) {
        this.context = context;
    }

    public TextView strankaNumTextView(Context context, String str){
        final ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(context);
        textView.setPadding(10, 10, 0, 0);
        textView.setTextColor(Color.rgb(0,0,0));
        textView.setLayoutParams(layoutParams);
        textView.setMaxEms(2);
        textView.setText(str + ".");
        return textView;

    }

    public EditText recivedImeEditText(Context context){
        final ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText editText = new EditText(context);
        int id = 0;
        editText.setId(id);
        editText.setHint("Ime i Prezime");
        editText.setLayoutParams(layoutParams);
        editText.setTextColor(Color.rgb(0,0,0));
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        return editText;
    }
    public EditText recivedAdersaEditText(Context context){
        final ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText editText = new EditText(context);
        int id = 1;
        editText.setId(id);
        editText.setHint("Adresa");
        editText.setLayoutParams(layoutParams);
        editText.setTextColor(Color.rgb(0,0,0));
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        return editText;
    }
    public EditText recivedMestoEditText(Context context){
        final ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText editText = new EditText(context);
        int id = 2;
        editText.setId(id);
        editText.setHint("Mesto");
        editText.setLayoutParams(layoutParams);
        editText.setTextColor(Color.rgb(0,0,0));
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        return editText;
    }

}
