package com.example.berka.advokatormlite.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.model.StrankaDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by berka on 04-Jan-18.
 */

public class MyResultAdapter extends ArrayAdapter<StrankaDetail>
{

    Context context;
    ArrayList<StrankaDetail> sveStrankeLista;
    private int layoutResourceId;
    private LayoutInflater inflater;
    private HashMap<String, String> textValues = new HashMap<String, String>();

    public MyResultAdapter(@NonNull Context context, int layoutResourceId, @NonNull List<StrankaDetail> objects) {
        super(context, layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View view = convertView;
        boolean convertViewWasNull = false;
        if(view == null){
            view = inflater.inflate(R.layout.my_adapter_listastranaka_add_edit, parent, false);
            convertViewWasNull = true;
        }


        TextView br = convertView.findViewById(R.id.adapter_svestranke_brojredni);

        EditText imeprezime = convertView.findViewById(R.id.adapter_svestranke_addedit_imeprezime);

        EditText adresa = convertView.findViewById(R.id.adapter_svestranke_addedit_adresa);

        EditText mesto = convertView.findViewById(R.id.adapter_svestranke_addedit_mesto);

        if(convertViewWasNull ){

            //be aware that you shouldn't do this for each call on getView, just once by listItem when convertView is null
            imeprezime.addTextChangedListener(new GenericTextWatcher(imeprezime));
            adresa.addTextChangedListener(new GenericTextWatcher(adresa));
            mesto.addTextChangedListener(new GenericTextWatcher(mesto));
        }

        //whereas, this should be called on each getView call, to update view tags.
        imeprezime.setTag("theFirstEditTextAtPos:"+position);
        adresa.setTag("theSecondEditTextAtPos:"+position);
        mesto.setTag("theThirdTextAtPos:"+position);

        return view;
    }


    private class GenericTextWatcher implements TextWatcher {

        private View view;
        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}


        public void afterTextChanged(Editable editable) {

            String text = editable.toString();
            //save the value for the given tag :
            MyResultAdapter.this.textValues.put(String.valueOf(view.getTag()), editable.toString());
        }
    }

    //you can implement a method like this one for each EditText with the list position as parameter :
    public String getValueFromFirstEditText(int position){
        //here you need to recreate the id for the first editText
        String result = textValues.get("theFirstEditTextAtPos:"+position);
        if(result ==null)
            result = "default value";

        return result;
    }

    public String getValueFromSecondEditText(int position){
        //here you need to recreate the id for the second editText
        String result = textValues.get("theSecondEditTextAtPos:"+position);
        if(result ==null)
            result = "default value";

        return result;
    }

     public String getValueFromThirdEditText(int position){
        //here you need to recreate the id for the second editText
        String result = textValues.get("theThirdTextAtPos:"+position);
        if(result ==null)
            result = "default value";

        return result;
    }

}