package com.giveu.shoppingmall.model.bean.response;

import java.util.List;

/**
 * 常见问题相关数据
 * Created by 101900 on 2017/6/23.
 */

public class CommontProblemResponse {
   public List<ListBean> list;

    public static class ListBean{
        public String problemFlag;//问题类型
        public String question;
        public String answer;
        public boolean clickFlag = false;//是否打开

        public ListBean(String problemFlag,String question, String answer) {
            this.problemFlag = problemFlag;
            this.question = question;
            this.answer = answer;
        }
    }

}
