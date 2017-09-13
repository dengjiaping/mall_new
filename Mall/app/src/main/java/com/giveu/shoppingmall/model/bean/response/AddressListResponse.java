package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 101900 on 2017/9/5.
 */

public class AddressListResponse extends BaseBean<List<AddressListResponse>> implements Serializable {

    /**
     * address : 南坑老村
     * addressType : 5
     * city : 深圳市
     * cityCode : 1607
     * custName : 张三
     * id : 21415
     * idPerson : 10103773
     * isDefault : 0
     * phone : 18126519781
     * province : 广东
     * provinceCode : 19
     * region : 龙岗区
     * regionCode : 40152
     * street : 横岗街道
     * streetCode : 47423
     */

    public String address;
    public int addressType;
    public String city;
    public String cityCode;
    public String custName;
    public String id;
    public String idPerson;
    public String isDefault;
    public String phone;
    public String province;
    public String provinceCode;
    public String region;
    public String regionCode;
    public String street;
    public String streetCode;

    public AddressListResponse(String address, String city, String custName, String isDefault, String phone, String province, String region, String street) {
        this.address = address;
        this.city = city;
        this.custName = custName;
        this.isDefault = isDefault;
        this.phone = phone;
        this.province = province;
        this.region = region;
        this.street = street;
    }
}
