package com.asuprojects.tarefafeita.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

public class RecyclerViewItemListener implements RecyclerView.OnItemTouchListener {

    private OnClickItemListener listener;

    private GestureDetector detectorMovimento;


    public RecyclerViewItemListener(Context context, final RecyclerView recyclerView, OnClickItemListener meuListener){
        listener = meuListener;
        detectorMovimento = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent event) {
                View childView = recyclerView.findChildViewUnder(event.getX(), event.getY());
                if(childView != null && listener != null){
                    listener.onClickItemLongo(childView, recyclerView.getChildAdapterPosition(childView));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent event) {
        View childView = recyclerView.findChildViewUnder(event.getX(), event.getY());
        if(childView != null && listener != null && detectorMovimento.onTouchEvent(event)){
            listener.onClickItem(childView, recyclerView.getChildAdapterPosition(childView));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public interface OnClickItemListener extends AdapterView.OnItemClickListener{

        void onClickItem(View view, int position);
        void onClickItemLongo(View view, int position);
    }
}
