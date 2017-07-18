package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.List;

/**
 * Created by 513419 on 2017/7/18.
 */

public class FeedBackResponse extends BaseBean<List<FeedBackResponse>> {

    /**
     * id : 117563
     * name : null
     * ident : null
     * openid : null
     * phone : null
     * content : ss
     * createdate : 1500343024000
     * replyContent : null
     * replyAccount : null
     * isreadflag : 1
     * status : 0
     * replayDate : null
     * readDate : 1500343170000
     * nickName : null
     * questionImageList : [{"id":175553,"questionId":117563,"smallImage":"https://idcwxtest.dafysz.cn/act/photoD:\\photo\\question\\201707\\18\\201707180956162868.jpg","bigImage":"https://idcwxtest.dafysz.cn/act/photoD:\\photo\\question\\201707\\18\\201707180956162868.jpg","createdate":1500343024000,"orderfalg":"0","status":"1","openid":"149"}]
     * source : null
     */

    public String id;
    public String name;
    public String ident;
    public String openid;
    public String phone;
    public String content;
    public long createdate;
    public String replyContent;
    public String replyAccount;
    public String isreadflag;
    public long replayDate;
    public String readDate;
    public String nickName;
    public String source;
    public boolean fold = false;
    public List<QuestionImageListBean> questionImageList;

    public static class QuestionImageListBean {
        /**
         * id : 175553
         * questionId : 117563
         * smallImage : https://idcwxtest.dafysz.cn/act/photoD:\photo\question\201707\18\201707180956162868.jpg
         * bigImage : https://idcwxtest.dafysz.cn/act/photoD:\photo\question\201707\18\201707180956162868.jpg
         * createdate : 1500343024000
         * orderfalg : 0
         * status : 1
         * openid : 149
         */

        public String id;
        public String questionId;
        public String smallImage;
        public String bigImage;
        public String createdate;
        public String orderfalg;
        public String status;
        public String openid;
    }
}
