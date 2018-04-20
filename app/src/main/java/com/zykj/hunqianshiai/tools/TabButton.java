package com.zykj.hunqianshiai.tools;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.zykj.hunqianshiai.R;

public class TabButton extends android.support.v7.widget.AppCompatRadioButton {

    private int mDrawableSize;// xml文件中设置图片的大小

    public TabButton(Context context) {
        super(context);
        initView(context, null);
    }
    public TabButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }
    public TabButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }

    private void initView(Context context, AttributeSet attrs){
        Drawable drawableLeft = null, drawableTop = null, drawableRight = null, drawableBottom = null;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabButton);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++){
            int attr = a.getIndex(i);
            //ToolUtils.printLogD("TabView", "attr:" + attr);
            switch (attr) {
                case R.styleable.TabButton_drawable_size:
                    mDrawableSize = a.getDimensionPixelSize(R.styleable.TabButton_drawable_size, 20);
                    break;
                case R.styleable.TabButton_drawableTop:
                    drawableTop = a.getDrawable(attr);
                    break;
                case R.styleable.TabButton_drawableBottom:
                    drawableBottom = a.getDrawable(attr);
                    break;
                case R.styleable.TabButton_drawableRight:
                    drawableRight = a.getDrawable(attr);
                    break;
                case R.styleable.TabButton_drawableLeft:
                    drawableLeft = a.getDrawable(attr);
                    break;
                default :
                    break;
            }
        }
        a.recycle();
        setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }

    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        if (left != null) {
            left.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        if (right != null) {
            right.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        if (top != null) {
            top.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        setCompoundDrawables(left, top, right, bottom);
    }
}