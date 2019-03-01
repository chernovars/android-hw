package com.example.arseniy.hw3_viewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

public class MyViewGroup extends FrameLayout {
    int mHorizontalSpace;
    int mVerticalSpace;

    public MyViewGroup(Context context) {
        super(context);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(context, attrs);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttributes(context, attrs);
    }

    private void setAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyViewGroup);
        mHorizontalSpace = typedArray.getDimensionPixelSize(R.styleable.MyViewGroup_mvg_horizontal_space_size, 0);
        mVerticalSpace = typedArray.getDimensionPixelSize(R.styleable.MyViewGroup_mvg_vertical_space_size, 0);
        typedArray.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();

        int maxHeight = 0;
        int maxWidth = 0;

        int specSizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int specModeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int specSizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int specModeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int childWidth;
        int childHeight;

        measureChildren(widthMeasureSpec, heightMeasureSpec);


        // Find rightmost and bottom-most child
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams childLP = (LayoutParams) child.getLayoutParams();

            if (child.getVisibility() != GONE) {
                childWidth = childLP.leftMargin + child.getMeasuredWidth() + childLP.rightMargin;
                childHeight = childLP.topMargin + child.getMeasuredHeight() + childLP.bottomMargin;

                if ((maxWidth + childWidth) > specSizeWidth) {
                    maxHeight += childHeight;
                }
                else {
                    maxWidth += childWidth + mHorizontalSpace;
                    maxHeight = Math.max(maxHeight, childHeight);
                }
            }
        }

        // Account for padding too
        maxWidth +=  this.getPaddingLeft() + this.getPaddingRight();
        maxHeight += this.getPaddingTop() + this.getPaddingBottom();

        // Check against minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());


        int resolved1 = resolveSize(maxWidth, widthMeasureSpec);
        int resolved2 = resolveSize(maxHeight, heightMeasureSpec);
        setMeasuredDimension(resolved1, resolved2);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int widthOffset = 0;
        int heightOffset = 0;

        int childWidth;
        int childHeight;

        int rowHeight = 0;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams childLP = (LayoutParams) child.getLayoutParams();

            if (child.getVisibility() != GONE) {
                int lm = childLP.leftMargin;
                childWidth = lm + child.getMeasuredWidth() + childLP.rightMargin;
                childHeight = childLP.topMargin + child.getMeasuredHeight() + childLP.bottomMargin ;
                rowHeight = Math.max(rowHeight, childHeight);

                if ((widthOffset + childWidth) > r) {
                    heightOffset += rowHeight + mVerticalSpace;
                    widthOffset = 0;
                }
                child.layout(widthOffset + childLP.leftMargin,
                        heightOffset + childLP.topMargin,
                        widthOffset + childWidth - childLP.rightMargin,
                        heightOffset + childHeight - childLP.topMargin);
                widthOffset += childWidth + mHorizontalSpace;
            }
            else {
                child.layout(0,0,0,0);
            }
        }
    }

}
