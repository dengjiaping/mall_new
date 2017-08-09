/*
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.giveu.shoppingmall.base;

import android.content.Context;

import com.giveu.shoppingmall.widget.wheelview.adapter.AbstractWheelTextAdapter;

import java.util.List;

/**
 */
public class WheelCityAdapter extends AbstractWheelTextAdapter {
    List<String> data;

    public WheelCityAdapter(Context context) {
        super(context);
    }


    @Override
    protected CharSequence getItemText(int index) {
        if (index >= 0 && index < data.size()) {
            String item = data.get(index);
            return item;
        }
        return null;
    }

    public void setData(List<String> data2){
        this.data = data2;

//        notifyDataChangedEvent();
        notifyDataInvalidatedEvent();
    }

    public List<String> getData(){
        return data;
    }

    @Override
    public int getItemsCount() {
        return data == null ? 0 : data.size();
    }



}
