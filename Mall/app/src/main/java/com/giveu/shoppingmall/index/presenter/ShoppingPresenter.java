package com.giveu.shoppingmall.index.presenter;

import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.index.view.agent.IShoppingView;
import com.giveu.shoppingmall.model.bean.response.IndexResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 513419 on 2017/9/5.
 */

public class ShoppingPresenter extends BasePresenter<IShoppingView> {
    public ShoppingPresenter(IShoppingView view) {
        super(view);
    }

    String content = "[\n" +
            "    {\n" +
            "      \"modelName\": \"\",\n" +
            "      \"typeValue\": \"0\",\n" +
            "      \"decorations\": [\n" +
            "        {\n" +
            "          \"name\": null,\n" +
            "          \"code\": \"123456\",\n" +
            "          \"title\": null,\n" +
            "          \"iconSrc\": \"\",\n" +
            "          \"picSrc\": \"group1/M11/5A/FB/CgsLw1mSMk6AaK_lAAMaxoCu6as932.jpg\",\n" +
            "          \"url\": \"/v1/sc/goods/decoration/search\",\n" +
            "          \"urlTypeValue\": \"0\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": null,\n" +
            "          \"code\": \"23454\",\n" +
            "          \"title\": null,\n" +
            "          \"iconSrc\": \"\",\n" +
            "          \"picSrc\": \"group1/M0F/5A/FC/CgsLw1mSMlOAbrGOAACzCIwKa8s993.jpg\",\n" +
            "          \"url\": \"/v1/sc/goods/decoration/search\",\n" +
            "          \"urlTypeValue\": \"0\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": null,\n" +
            "          \"code\": \"123133\",\n" +
            "          \"title\": null,\n" +
            "          \"iconSrc\": \"\",\n" +
            "          \"picSrc\": \"group1/M02/4E/8B/CgsLxVmSypKAPbKqAABRa-Mz4XE919.jpg\",\n" +
            "          \"url\": \"H5\",\n" +
            "          \"urlTypeValue\": \"1\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": null,\n" +
            "          \"code\": \"3242\",\n" +
            "          \"title\": null,\n" +
            "          \"iconSrc\": \"\",\n" +
            "          \"picSrc\": \"group1/M04/78/3D/CgsLw1mSyn2AL-6eAAM6otB8uds346.jpg\",\n" +
            "          \"url\": \"H5\",\n" +
            "          \"urlTypeValue\": \"1\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"srcIp\": \"http://fastdfs.dafysz.cn\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"modelName\": \"热门品类\",\n" +
            "      \"typeValue\": \"2\",\n" +
            "      \"decorations\": [\n" +
            "        {\n" +
            "          \"name\": \"手机馆\",\n" +
            "          \"code\": \"232\",\n" +
            "          \"title\": \"新品限量抢\",\n" +
            "          \"iconSrc\": \"group1/M11/5A/FB/CgsLw1mSMk6AaK_lAAMaxoCu6as932.jpg\",\n" +
            "          \"picSrc\": \"group1/M06/7B/AB/CgsLxFmSyp-AT8EwAAKY57j_yL4201.jpg\",\n" +
            "          \"url\": \"/v1/sc/goods/decoration/search\",\n" +
            "          \"urlTypeValue\": \"0\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"智能数码\",\n" +
            "          \"code\": \"234\",\n" +
            "          \"title\": \"爆款低至6折\",\n" +
            "          \"iconSrc\": \"group1/M01/7B/AD/CgsLxFmSyq6AbbuEAACrm2Ay6jc108.jpg\",\n" +
            "          \"picSrc\": \"group1/M0C/78/3D/CgsLw1mSyn6ALMuYAANGc0W71Dc432.jpg\",\n" +
            "          \"url\": \"/v1/sc/goods/decoration/search\",\n" +
            "          \"urlTypeValue\": \"0\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"电脑办公\",\n" +
            "          \"code\": \"453\",\n" +
            "          \"title\": \"999抢笔记本\",\n" +
            "          \"iconSrc\": \"group1/M02/4E/8B/CgsLxVmSypKAPbKqAABRa-Mz4XE919.jpg\",\n" +
            "          \"picSrc\": \"group1/M02/7B/AF/CgsLxFmSysGACOzAAANGc0W71Dc902.jpg\",\n" +
            "          \"url\": \"/v1/sc/goods/decoration/search\",\n" +
            "          \"urlTypeValue\": \"0\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"相机单反\",\n" +
            "          \"code\": \"5464\",\n" +
            "          \"title\": \"每满千减百\",\n" +
            "          \"iconSrc\": \"group1/M11/78/3B/CgsLw1mSymqAA459AANGc0W71Dc293.jpg\",\n" +
            "          \"picSrc\": \"group1/M13/4E/88/CgsLxVmSyoGANfuhAAKY57j_yL4321.jpg\",\n" +
            "          \"url\": \"/v1/sc/goods/decoration/search\",\n" +
            "          \"urlTypeValue\": \"0\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"srcIp\": \"http://fastdfs.dafysz.cn\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"modelName\": null,\n" +
            "      \"typeValue\": \"1\",\n" +
            "      \"decorations\": [\n" +
            "        {\n" +
            "          \"name\": \"手机馆\",\n" +
            "          \"code\": \"12312\",\n" +
            "          \"title\": \"品质生活\",\n" +
            "          \"iconSrc\": null,\n" +
            "          \"picSrc\": \"group1/M11/5A/FB/CgsLw1mSMk6AaK_lAAMaxoCu6as932.jpg\",\n" +
            "          \"url\": \"/v1/sc/goods/decoration/search\",\n" +
            "          \"urlTypeValue\": \"0\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"家电广\",\n" +
            "          \"code\": \"32423\",\n" +
            "          \"title\": \"最好12期免息\",\n" +
            "          \"iconSrc\": null,\n" +
            "          \"picSrc\": \"group1/M0F/5A/FC/CgsLw1mSMlOAbrGOAACzCIwKa8s993.jpg\",\n" +
            "          \"url\": \"/v1/sc/goods/decoration/search\",\n" +
            "          \"urlTypeValue\": \"0\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"运动户外\",\n" +
            "          \"code\": \"4353\",\n" +
            "          \"title\": \"大致好身材\",\n" +
            "          \"iconSrc\": null,\n" +
            "          \"picSrc\": \"group1/M02/4E/8B/CgsLxVmSypKAPbKqAABRa-Mz4XE919.jpg\",\n" +
            "          \"url\": \"/v1/sc/goods/decoration/search\",\n" +
            "          \"urlTypeValue\": \"0\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"西湖没在\",\n" +
            "          \"code\": \"567\",\n" +
            "          \"title\": \"全程低至5折\",\n" +
            "          \"iconSrc\": null,\n" +
            "          \"picSrc\": \"group1/M04/78/3D/CgsLw1mSyn2AL-6eAAM6otB8uds346.jpg\",\n" +
            "          \"url\": \"/v1/sc/goods/decoration/search\",\n" +
            "          \"urlTypeValue\": \"0\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"srcIp\": \"http://fastdfs.dafysz.cn\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"modelName\": null,\n" +
            "      \"typeValue\": \"3\",\n" +
            "      \"decorations\": [\n" +
            "        {\n" +
            "          \"name\": \"话费/流量\",\n" +
            "          \"code\": \"13213\",\n" +
            "          \"title\": \"流量下单减2元\",\n" +
            "          \"iconSrc\": null,\n" +
            "          \"picSrc\": \"group1/M0C/78/3D/CgsLw1mSyn6ALMuYAANGc0W71Dc432.jpg\",\n" +
            "          \"url\": \"/v1/sc/goods/decoration/search\",\n" +
            "          \"urlTypeValue\": \"0\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"srcIp\": \"http://fastdfs.dafysz.cn\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"modelName\": \"热门单品\",\n" +
            "      \"typeValue\": \"4\",\n" +
            "      \"decorations\": [\n" +
            "        {\n" +
            "          \"name\": null,\n" +
            "          \"code\": \"123120\",\n" +
            "          \"title\": null,\n" +
            "          \"iconSrc\": null,\n" +
            "          \"picSrc\": null,\n" +
            "          \"url\": \"/v1/sc/goods/decoration/search\",\n" +
            "          \"urlTypeValue\": \"0\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"srcIp\": \"http://fastdfs.dafysz.cn\"\n" +
            "    }\n" +
            "  ]";

    public void getIndexContent() {
        Gson gson = new Gson();
        //解析json
        final ArrayList<IndexResponse> contentList = gson.fromJson(content,
                new TypeToken<List<IndexResponse>>() {
                }.getType());
        getView().getIndexContent(contentList);
    }
}
