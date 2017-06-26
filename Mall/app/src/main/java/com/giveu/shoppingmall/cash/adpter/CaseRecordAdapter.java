package com.giveu.shoppingmall.cash.adpter;

import android.content.Context;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;

import java.util.List;

/**
 * Created by 513419 on 2017/6/26.
 */

public class CaseRecordAdapter extends LvCommonAdapter<String> {
    public CaseRecordAdapter(Context context, List<String> datas) {
        super(context, R.layout.lv_case_record_item, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, String item, int position) {

    }
}
