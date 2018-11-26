package com.yyz.ard.cactus.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yyz.ard.cactus.uiaf.FindView;

/**
 * Created by dell on 3/21/2018.
 *
 * @author yyz
 */
public class SupperRecyclerHolder extends RecyclerView.ViewHolder {

    private FindView mFindView;
    private ItemCell mItemCell;

    public SupperRecyclerHolder(View itemView) {
        super(itemView);
        mFindView = new FindView(itemView);
    }

    public void setItemCell(ItemCell itemCell) {
        mItemCell = itemCell;
    }

    public ItemCell getItemCell() {
        return mItemCell;
    }

    public FindView getFindView() {
        return mFindView;
    }

}
