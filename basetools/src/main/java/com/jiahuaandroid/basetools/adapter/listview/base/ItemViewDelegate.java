package com.jiahuaandroid.basetools.adapter.listview.base;


import com.jiahuaandroid.basetools.adapter.listview.ViewHolder;

public interface ItemViewDelegate<T>
{

    public abstract int getItemViewLayoutId();

    public abstract boolean isForViewType(T item, int position);

    public abstract void convert(ViewHolder holder, T t, int position);



}
