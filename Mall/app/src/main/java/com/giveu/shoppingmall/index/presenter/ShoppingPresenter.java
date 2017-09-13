package com.giveu.shoppingmall.index.presenter;

import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.index.view.agent.IShoppingView;
import com.giveu.shoppingmall.model.bean.response.IndexResponse;
import com.giveu.shoppingmall.model.bean.response.ShoppingResponse;
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

    String commoidty = "{\"code\":\"sc0\",\"message\":\"成功\",\"result\":\"success\",\"data\":{\"srcIp\":\"http://fastdfs.dafysz.cn\",\"pages\":2,\"pageSize\":10,\"pageNum\":1,\"resultList\":[{\"keywords\":\"荣耀,华为,手机\",\"monthAmount\":\"124.00\",\"name\":\"荣耀 畅玩6X 4GB 32GB 全网通4G手机 高配版 铂光金\",\"salePrice\":\"1,299.00\",\"skuCode\":\"K00002719\",\"isInstallments\":1,\"src\":\"group1/M0F/69/55/CgsLxFmSb6OAXCa9AAT8G3zKiLU625.jpg\",\"isPromotion\":1},{\"keywords\":\"手机,小米\",\"monthAmount\":\"160.00\",\"name\":\"小米Max2 全网通 4GB+64GB 金色 移动联通电信4G手机 双卡双待\",\"salePrice\":\"1,669.00\",\"skuCode\":\"K00002733\",\"isInstallments\":1,\"src\":\"group1/M10/6D/5B/CgsLw1mSh6KAebBeAAE1bwEvgwI737.jpg\",\"isPromotion\":1},{\"keywords\":\"手机,华为,畅想\",\"monthAmount\":\"86.00\",\"name\":\"华为 畅享6 金色 移动联通电信4G手机 双卡双待\",\"salePrice\":\"899.00\",\"skuCode\":\"K00002718\",\"isInstallments\":1,\"src\":\"group1/M11/3B/F7/CgsLxVmSbteASJpsAAUDAh955Go248.jpg\",\"isPromotion\":0},{\"keywords\":\"苹果,手机,iPhone\",\"monthAmount\":\"341.00\",\"name\":\"Apple iPhone 7 Plus (A1661) 128G 黑色 移动联通电信4G手机\",\"salePrice\":\"6,299.00\",\"skuCode\":\"K00002857\",\"isInstallments\":1,\"src\":\"group1/M0C/BB/63/CgsLxFmUQAKAeZ2QAAIJb733Hlc459.jpg\",\"isPromotion\":0},{\"keywords\":\"iphone7,红色\",\"monthAmount\":\"287.00\",\"name\":\"Apple iPhone 7 (A1660) 128G 黑色 移动联通电信4G手机\",\"salePrice\":\"5,299.00\",\"skuCode\":\"K00002702\",\"isInstallments\":1,\"src\":\"group1/M07/17/7A/CgsLxVmRoKSAStTdAAIIYD7F3xQ614.jpg\",\"isPromotion\":0},{\"keywords\":\"oppo\",\"monthAmount\":\"195.00\",\"name\":\"OPPO R11 Plus 6GB+64GB内存版 全网通4G手机 双卡双待 玫瑰金色\",\"salePrice\":\"3,600.00\",\"skuCode\":\"K00002723\",\"isInstallments\":1,\"src\":\"group1/M0F/6D/B2/CgsLw1mSiQ2AFMxsAAK1UfSjVQs554.jpg\",\"isPromotion\":0},{\"keywords\":\"荣耀,手机,华为\",\"monthAmount\":\"287.00\",\"name\":\"荣耀 V9 全网通 高配版 6GB+64GB 极光蓝 移动联通电信4G手机 双卡双待\",\"salePrice\":\"2,999.00\",\"skuCode\":\"K00002726\",\"isInstallments\":1,\"src\":\"group1/M0C/67/E9/CgsLxFmSa26ANV4mAAVU7f0xRxk731.jpg\",\"isPromotion\":0},{\"keywords\":\"荣耀,手机\",\"monthAmount\":\"77.00\",\"name\":\"荣耀 畅玩6A 2GB+16GB 银色 全网通4G手机 双卡双待\",\"salePrice\":\"799.00\",\"skuCode\":\"K00002729\",\"isInstallments\":1,\"src\":\"group1/M09/65/6C/CgsLw1mSbgmARd2QAAVfH05J87E312.jpg\",\"isPromotion\":0},{\"keywords\":\"手机,oppo\",\"monthAmount\":\"249.00\",\"name\":\"OPPO R9s 全网通4G+64G 双卡双待手机 金色\",\"salePrice\":\"2,599.00\",\"skuCode\":\"K00002737\",\"isInstallments\":1,\"src\":\"group1/M14/6D/96/CgsLw1mSiJmAMM3TAAGSdKjtf-E709.jpg\",\"isPromotion\":0},{\"keywords\":\"手机\",\"monthAmount\":\"86.00\",\"name\":\"乐视（LeEco）乐2 全网通（Le X520）3GB+32GB 原力金 移动联通电信4G手机 双卡双待\",\"salePrice\":\"899.00\",\"skuCode\":\"K00002732\",\"isInstallments\":1,\"src\":\"group1/M12/43/9B/CgsLxVmSh4KASDERAAMBvft15vk832.jpg\",\"isPromotion\":0}]}}";

    public void getIndexContent() {
        Gson gson = new Gson();
        //解析json
        final ArrayList<IndexResponse> contentList = gson.fromJson(content,
                new TypeToken<List<IndexResponse>>() {
                }.getType());
        getView().getIndexContent(contentList);

        final ShoppingResponse shoppingResponse = gson.fromJson(commoidty, ShoppingResponse.class);
        getView().showCommodity(shoppingResponse.data);
    }
}
