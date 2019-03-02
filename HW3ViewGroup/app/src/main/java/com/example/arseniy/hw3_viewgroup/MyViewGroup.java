package com.example.arseniy.hw3_viewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class MyViewGroup extends FrameLayout {
    int mHorizontalSpace;
    int mVerticalSpace;
    int mRowHeight;

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
        mRowHeight = typedArray.getDimensionPixelSize(R.styleable.MyViewGroup_mvg_height, -1);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();

        // Максимальное накопленное значение высоты
        int sumHeight = this.getPaddingTop() + this.getPaddingBottom();

        int rowWidth = this.getPaddingLeft() + this.getPaddingRight();
        int rowHeight = 0;

        int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int childWidth;
        int childHeight;
        // флаг, переключающий размер строчки между фиксированным и динамическим (высота = максимально высокий элемент в строчке)
        boolean fixedHeight = (mRowHeight != -1);
        // флаг, определяющий сделали ли мы перенос на следующую строку
        boolean reachedMaxWidth = false;

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams childLP = (LayoutParams) child.getLayoutParams();

            if (child.getVisibility() != GONE) {
                childWidth = childLP.leftMargin + child.getMeasuredWidth() + childLP.rightMargin;
                childHeight = childLP.topMargin + child.getMeasuredHeight() + childLP.bottomMargin;
                rowHeight = fixedHeight ? mRowHeight : Math.max(rowHeight, childHeight);

                //если накопленная ширина + текущая больше дозволенных рамок, переносим строку
                if ((rowWidth + childWidth) > specWidthSize && widthMode != MeasureSpec.UNSPECIFIED) {
                    reachedMaxWidth = true;
                    sumHeight += rowHeight + mVerticalSpace;
                    if (!fixedHeight)
                        // сбрасываем высоту новой строчки на высоту элемента
                        rowHeight = childHeight;
                    rowWidth = childWidth + mHorizontalSpace + this.getPaddingLeft() + this.getPaddingRight();
                }
                else {
                    rowWidth += childWidth + mHorizontalSpace;
                }
            }
        }
        sumHeight += rowHeight;

        if (reachedMaxWidth || widthMode == MeasureSpec.EXACTLY)
            setMeasuredDimension(specWidthSize, resolveSize(sumHeight, heightMeasureSpec));
        else if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            setMeasuredDimension(rowWidth, resolveSize(sumHeight, heightMeasureSpec));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int widthOffset = 0;
        int heightOffset = 0;

        int childWidth;
        int childHeight;
        l += this.getPaddingLeft();
        r -= this.getPaddingRight();
        int width = r - l;

        int rowHeight = 0;
        boolean fixedHeight = (mRowHeight != -1);

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams childLP = (LayoutParams) child.getLayoutParams();

            if (child.getVisibility() != GONE) {
                childWidth = childLP.leftMargin + child.getMeasuredWidth() + childLP.rightMargin;
                childHeight = fixedHeight ? mRowHeight : childLP.topMargin + child.getMeasuredHeight() + childLP.bottomMargin ;
                rowHeight = Math.max(rowHeight, childHeight);

                if ((widthOffset + childWidth) > width) {
                    heightOffset += rowHeight + mVerticalSpace;
                    widthOffset = 0;
                }
                child.layout( this.getPaddingLeft() + widthOffset + childLP.leftMargin,
                        this.getPaddingTop() + heightOffset + childLP.topMargin,
                        this.getPaddingLeft() + widthOffset + childWidth - childLP.rightMargin,
                        this.getPaddingTop() + heightOffset + childHeight - childLP.bottomMargin);
                widthOffset += childWidth + mHorizontalSpace;
            }
            else {
                child.layout(0,0,0,0);
            }
        }
    }

}
