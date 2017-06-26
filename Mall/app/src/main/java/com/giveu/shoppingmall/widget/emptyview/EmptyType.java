package com.giveu.shoppingmall.widget.emptyview;


import com.giveu.shoppingmall.R;

public enum EmptyType {
    /**
     * default
     */
    OPT_DEFAULT(CommonLoadingView.DEFAULT_ICON),

    /**
     * 活动搜索
     */
    OPT_ACTIVITY_SEARCH(CommonLoadingView.DEFAULT_ICON, "此条件下暂未搜索到活动，请换个条件", ""),

    /**
     * 扫描图片
     */
    OPT_SCAN_PIC(CommonLoadingView.DEFAULT_ICON, "没有扫描到图片", ""),

    PDF_LIST_ACTIVITY(R.drawable.ic_pdf_list_activity_empty, "还没有生成pdf文件", ""),
    ;

    /**
     * 图标
     */
    public int icon;
    /**
     * 布局Id
     */
    public int layoutId;

    /**
     * 主信息
     */
    public String message;
    /**
     * 小信息
     */
    public String messageSub;

    private EmptyType(int layoutId, int icon, String message, String messageSub) {
        this.layoutId = layoutId;
        this.icon = icon;
        this.message = message;
        this.messageSub = messageSub;
    }

    private EmptyType(int icon, String message, String messageSub) {
        this.icon = icon;
        this.message = message;
        this.messageSub = messageSub;
    }

    private EmptyType(int icon, String message) {
        this.icon = icon;
        this.message = message;
    }


    private EmptyType(int icon) {
        this.icon = icon;
    }
}
