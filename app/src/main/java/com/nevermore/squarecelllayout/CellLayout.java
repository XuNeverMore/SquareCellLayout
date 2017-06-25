package com.nevermore.squarecelllayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


/**
 *
 * @ClassName:CellLayout

 * @PackageName:com.nevermore.squarecelllayout

 * @Create On 2017/6/25   11:36

 * @author:xuchuanting

 * @Copyrights 2017/6/25 nevermore All rights reserved.
 */

public class CellLayout extends ViewGroup {
    private static final String TAG = "CellLayout";

    private int columCount = 3;
    private int space = 0;
    private boolean isSquareMode = false;
    private SparseIntArray rowMaxHeights = new SparseIntArray();

    public CellLayout(Context context) {
        this(context, null);
    }

    public CellLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public CellLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CellLayout);
        space = typedArray.getDimensionPixelSize(R.styleable.CellLayout_cellSpcing, 0);
        isSquareMode = typedArray.getBoolean(R.styleable.CellLayout_isSquareCell, true);
        columCount = typedArray.getInteger(R.styleable.CellLayout_columCount,3);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //总宽度
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        Log.i(TAG, "widthSize: "+widthSize);
        //根据列数columCount和间隙space求出每个格子宽度
        int hs = space * (columCount - 1);
        Log.i(TAG, "hs: "+hs);
        int cellWidth = getCellWidth();
        Log.i(TAG, "cellWidth: "+cellWidth);
        int childCount = getChildCount();

        rowMaxHeights.clear();

        int rowMaxHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LayoutParams layoutParams = child.getLayoutParams();
            layoutParams.width = cellWidth;
            if(isSquareMode){
                layoutParams.height = cellWidth;
            }
            //测量子view
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            int childMeasuredHeight = child.getMeasuredHeight();
            //保存子view宽高
            child.measure(getChildMeasureSpec(widthMeasureSpec, getPaddingLeft()+getPaddingRight(), cellWidth),
                    getChildMeasureSpec(heightMeasureSpec, 0, isSquareMode ? cellWidth : childMeasuredHeight));

            //记录每一行的最大高度
            if (!isSquareMode) {
                if (rowMaxHeight < childMeasuredHeight) {
                    rowMaxHeight = childMeasuredHeight;
                }
                //测量到一行的最后一个child保存该行的最大高度
                if ((i + 1) % columCount == 0 || i == childCount - 1) {
                    int height = rowMaxHeight;
                    rowMaxHeights.put(getRowIndex(i), height);
                    //换行了
                    rowMaxHeight = 0;
                }
            }
        }

        //总行高
        int totalRowHeight = 0;
        int rowCount = getRowCount();//行数
        if (isSquareMode) {
            totalRowHeight = rowCount * cellWidth;
        } else {
            for (int i = 0; i < rowMaxHeights.size(); i++) {
                totalRowHeight += rowMaxHeights.get(i);
            }
        }
        int heightSize = space * (rowCount - 1) + totalRowHeight+getPaddingTop()+getPaddingBottom();
        setMeasuredDimension(widthSize, heightSize);

    }

    /**
     * 获取行数
     */
    public int getRowCount() {
        return (int) Math.ceil(getChildCount() / (double) columCount);
    }

    /**
     * 获取child所在的行数
     * 从第0行算
     *
     * @param childIndex
     * @return
     */
    public int getRowIndex(int childIndex) {
        return (int) Math.ceil((childIndex + 1) / (double) columCount) - 1;
    }

    /**
     * 获取总列数
     *
     * @return
     */
    public int getColumCount() {
        return columCount;
    }

    /**
     * 获取每格宽度
     *
     * @return
     */
    public int getCellWidth() {
        return (getMeasuredWidth() - space * (columCount - 1)-getPaddingRight()-getPaddingRight()) / columCount;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int left = paddingLeft;
        int top = paddingTop;
        int cellWidth = getCellWidth();
        for (int i = 0; i < getChildCount(); i++) {

            View child = getChildAt(i);


            int height = isSquareMode?cellWidth:rowMaxHeights.get(getRowIndex(i));
            child.layout(left, top, left + cellWidth, top + (isSquareMode ? cellWidth : child.getMeasuredHeight()));
            if ((i + 1) % columCount == 0) {//换行了
                left = paddingLeft;
                top += height + space;
            } else {
                left += cellWidth + space;
            }
        }
    }


    public interface OnItemClickListener {
        void onItemClick(ViewGroup parent, View view, int position, long id);
    }

    @Override
    public void removeViewAt(int index) {
        super.removeViewAt(index);
        if (onItemClickListener != null) {
            setOnItemClickListener(onItemClickListener);
        }
    }

    private OnItemClickListener onItemClickListener;


    /**
     * 写个适配器简化一下添加view
     * 暂不支持回收
     * @param adapter
     */
    public void setAdapter(BaseAdapter adapter){
        if(adapter==null){
            return;
        }
        removeAllViews();
        for (int i = 0; i < adapter.getCount(); i++) {
            addView(adapter.getView(i,null,null));
        }

    }

    /**
     * Item的点击事件
     *
     * @param listener
     */
    public void setOnItemClickListener(final OnItemClickListener listener) {
        if (listener == null) {
            return;
        }
        this.onItemClickListener = listener;
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            final int position = i;
            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(CellLayout.this, child, position, child.getId());
                }
            });
        }
    }


}
