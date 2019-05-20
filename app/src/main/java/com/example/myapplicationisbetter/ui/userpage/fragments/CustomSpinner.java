package com.example.myapplicationisbetter.ui.userpage.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplicationisbetter.R;
import com.example.myapplicationisbetter.ui.userpage.UserAdapter;

import java.util.ArrayList;
import java.util.List;


public class CustomSpinner extends AppCompatSpinner {
    private static final String TAG = "CustomSpinner";
    private OnSpinnerEventsListener mListener;
    private boolean mOpenInitiated = false;
    private CustomAdapter spinSettingAdapter;
    private CustomSpinner ca;

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSpinner(Context context, int mode) {
        super(context, mode);
    }

    public CustomSpinner(Context context) {
        super(context);
    }

    public interface OnSpinnerEventsListener {

        void onSpinnerOpened();

        void onSpinnerClosed();

    }

    @Override
    public boolean performClick() {
        // register that the Spinner was opened so we have a status
        // indicator for the activity(which may lose focus for some other
        // reasons)
        mOpenInitiated = true;
        if (mListener != null) {
            mListener.onSpinnerOpened();
        }
        return super.performClick();
    }

    public void setSpinnerEventsListener(OnSpinnerEventsListener onSpinnerEventsListener) {
        mListener = onSpinnerEventsListener;
    }

    /**
     * Propagate the closed Spinner event to the listener from outside.
     */
    public void performClosedEvent() {
        mOpenInitiated = false;
        if (mListener != null) {
            mListener.onSpinnerClosed();
        }
    }

    /**
     * A boolean flag indicating that the Spinner triggered an open event.
     *
     * @return true for opened Spinner
     */
    public boolean hasBeenOpened() {
        return mOpenInitiated;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        android.util.Log.d(TAG, "onWindowFocusChanged");
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasBeenOpened() && hasWindowFocus) {
            android.util.Log.i(TAG, "closing popup");
            performClosedEvent();
        }
    }

    public void initAdapter(Activity activity, String[] items) {
        ca = this;
        spinSettingAdapter = new CustomAdapter<String>(
                activity,
                R.layout.spinner_layout,
                R.id.spinner_textview,
                items
        );
        setClickable(false);
        setEnabled(false);
        setAdapter(spinSettingAdapter);
        setPopupBackgroundResource(R.drawable.drop_down_spinner_background);
        setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
            @Override
            public void onSpinnerOpened() {
                for(int i =0;i<spinSettingAdapter.viewMy.size();i++) {
                    ViewHolderMain viewHolderMain = (ViewHolderMain) spinSettingAdapter.viewMy.get(i);
                    viewHolderMain.triangleIm.setImageResource(R.drawable.triangle_png_close);
                }
            }

            @Override
            public void onSpinnerClosed() {
                for(int i =0;i<spinSettingAdapter.viewMy.size();i++) {
                    ViewHolderMain viewHolderMain = (ViewHolderMain) spinSettingAdapter.viewMy.get(i);
                    viewHolderMain.triangleIm.setImageResource(R.drawable.triangle_png_28);
                }
            }
        });
    }



//    public @Nullable Object getItem(int position) {
//        if(spinSettingAdapter != null && position < spinSettingAdapter.getCount()) {
//            return spinSettingAdapter.getItem(position);
//        } else {
//            return null;
//        }
//    }


    public class CustomAdapter<String> extends ArrayAdapter<String> {
        private LayoutInflater flater;
        private int layout;
        private String[] strAtr;

        List<ViewHolderMain> viewMy = new ArrayList<ViewHolderMain>();

        public CustomAdapter(Activity context, int resouceId, int textviewId, String[] strAtr) {

            super(context, resouceId, textviewId, strAtr);
            this.flater = context.getLayoutInflater();
            this.strAtr = strAtr;
            this.layout =resouceId;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolderMain viewHolder;

            if (convertView == null) {
                convertView = flater.inflate(this.layout, parent, false);
                viewHolder = new ViewHolderMain(convertView);
                convertView.setTag(viewHolder);
                viewMy.add(viewHolder);
            }else {
                viewHolder = (ViewHolderMain)convertView.getTag();
            }
            viewHolder.gearIm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ca.performClick();
                }
            });
            viewHolder.triangleIm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ca.performClick();
                }
            });
            viewHolder.strTxt.setText("");
            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            ViewHolderMain viewHolder1;

            if (convertView == null) {
                convertView = flater.inflate(R.layout.spinner_dropdown_layout, parent, false);
                viewHolder1 = new ViewHolderMain(convertView);
                convertView.setTag(viewHolder1);
            }else {
                viewHolder1 = (ViewHolderMain)convertView.getTag();
            }

            if(position == 0){
                //viewHolder1.setVisibility(View.INVISIBLE);
                //viewHolder1.viewMain.getLayoutParams().height = 1;
                viewHolder1.strTxt.setHeight(0);
                viewHolder1.viewMain.setVisibility(View.INVISIBLE);

            }else{
                viewHolder1.strTxt.setHeight(140);
            }
            viewHolder1.strTxt.setText(strAtr[position].toString());
            return convertView;
        }

    }
    public class ViewHolderMain{
        ImageView gearIm;
        ImageView triangleIm;
        TextView strTxt;
        View viewMain;
        ViewHolderMain(View view){
            viewMain = view;
            gearIm  = (ImageView)viewMain.findViewById(R.id.image_spinner_layout);
            triangleIm  =(ImageView)viewMain.findViewById(R.id.image_spinner_triangle);
            strTxt = (TextView)viewMain.findViewById(R.id.spinner_textview);
        }
    }
}