package com.china.lhf.app.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.china.lhf.app.R;

/**
 * Created by C罗 on 2016/9/8.
 */
public class HomeToolbar extends Toolbar {

    private LayoutInflater minflater;
    private EditText mSearchview;
    private TextView mTexttile;
    private Button mRightButton;
    private ImageButton mLeftButton;
    private View view;


    public HomeToolbar(Context context) {

        this(context,null);
    }

    public HomeToolbar(Context context, @Nullable AttributeSet attrs) {

        this(context, attrs,0);
    }

    public HomeToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        iniView();
        setContentInsetsRelative(10,10);
        if(attrs!=null){
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs, R.styleable.HomeToolbar, defStyleAttr, 0);

            final Drawable leftIcon = a.getDrawable(R.styleable.HomeToolbar_leftButton);
            final Drawable rightIcon = a.getDrawable(R.styleable.HomeToolbar_rightButton);
            final CharSequence rightbuttontext = a.getText(R.styleable.HomeToolbar_rightButtonText);
            if (rightbuttontext!=null) {
                setRightButtonText(rightbuttontext);
            }


            if(leftIcon!=null){
                setLeftButtonIcon(leftIcon);
            }

            if (rightIcon != null) {
                setRightButtonIcon(rightIcon);
            }

            boolean isShowSearchView=a.getBoolean(R.styleable.HomeToolbar_isShowSearchView,false);

            if(isShowSearchView){
                showSearchView();
                hideTitleView();
            }
            a.recycle();
        }
    }

    private void iniView(){
        if(view==null) {
            minflater = LayoutInflater.from(getContext());
            view = minflater.inflate(R.layout.toolbar, null);
            mTexttile = (TextView) view.findViewById(R.id.toolbar_title_tv);
            mSearchview= (EditText) view.findViewById(R.id.toolbar_searchview);
            mRightButton = (Button) view.findViewById(R.id.toolbar_rightButton);
            mLeftButton= (ImageButton) view.findViewById(R.id.toolbar_leftButton);
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
            addView(view, lp);
        }
    }

    @Override
    public void setTitle(@StringRes int resId) {
        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
            iniView();
            if(mTexttile!=null){
                mTexttile.setText(title);
                showTitleView();
            }
    }

    public void showSearchView(){
        if(mSearchview!=null){
            mSearchview.setVisibility(VISIBLE);
        }
    }

    public void hideSearchView(){
        if(mSearchview!=null){
            mSearchview.setVisibility(GONE);
        }
    }

    public void showTitleView(){
        if(mTexttile!=null){
            mTexttile.setVisibility(VISIBLE);
        }
    }

    public void hideTitleView(){
        if(mTexttile!=null){
            mTexttile.setVisibility(GONE);
        }
        mRightButton.setVisibility(GONE);
    }

    public void setRightButtonIcon(Drawable rightButtonIcon) {
           if(mRightButton!=null){
               mRightButton.setBackground(rightButtonIcon);
           }
        mRightButton.setVisibility(VISIBLE);
    }

    public void setRightButtonIcon(int icon) {
        if(mRightButton!=null){
            mRightButton.setBackgroundResource(icon);
        }
        mRightButton.setVisibility(VISIBLE);
    }

    public void setLeftButtonIcon(Drawable rightButtonIcon) {
        if(mLeftButton!=null){
            mLeftButton.setImageDrawable(rightButtonIcon);
        }
        mLeftButton.setVisibility(VISIBLE);
    }

    public void setmRightButtonClickListener(OnClickListener listener){
        mRightButton.setOnClickListener(listener);
    }

    public void setmLeftButtonClickListener(OnClickListener listener){
        mLeftButton.setOnClickListener(listener);
    }


    public void setRightButtonText(CharSequence rightButtonText) {
        if(mRightButton!=null) {
            mRightButton.setText(rightButtonText);
        }
        mRightButton.setVisibility(VISIBLE);
    }

    public Button getmRightButton(){
        return this.mRightButton;
    }

}
