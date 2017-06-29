package com.giveu.shoppingmall.model.bean.response;


import com.android.volley.mynet.BaseBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 */
public class FeedBackListResponse extends BaseBean<List<FeedBackListResponse>> {


    /**
     * ident : 1
     * name : 1
     * nickName : 1
     * openid : 1
     * phone : 1
     * questionImageList : [{"bigImage":"http://idcwxtest.dafysz.cn/act/photo/question/201701/18/201701180942367788.jpg","createdate":1484704000000,"id":1690,"openid":"11416671","orderfalg":"0","questionId":968,"smallImage":"http://idcwxtest.dafysz.cn/act/photo/question/201701/18/201701180942367788.jpg","status":"0"}]
     * readDate : 1
     * replayDate : 1
     * replyAccount : 1
     * replyContent : 1
     * content : 我的钱包提现有问题
     * createdate : 1484704000000
     * id : 968
     * isreadflag : 0
     */

    public int ident;
    public String name;
    public String nickName;
    public int openid;
    public int phone;
    public long readDate;
    public long replayDate;
    public String replyAccount;
    public String replyContent;
    public String content;
    public long createdate;
    public int id;
    public String isreadflag;
    public List<QuestionImageListBean> questionImageList;
    public boolean fold = true;
    public static class QuestionImageListBean {
        /**
         * bigImage : http://idcwxtest.dafysz.cn/act/photo/question/201701/18/201701180942367788.jpg
         * createdate : 1484704000000
         * id : 1690
         * openid : 11416671
         * orderfalg : 0
         * questionId : 968
         * smallImage : http://idcwxtest.dafysz.cn/act/photo/question/201701/18/201701180942367788.jpg
         * status : 0
         */

        public String bigImage;
        public long createdate;
        public int id;
        public String openid;
        public String orderfalg;
        public int questionId;
        public String smallImage;
        @SerializedName("status")
        public String statusX;
    }
}