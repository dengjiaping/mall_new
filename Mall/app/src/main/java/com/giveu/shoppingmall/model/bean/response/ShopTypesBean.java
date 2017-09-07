package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

import java.util.List;

/**
 * Created by 524202 on 2017/9/7.
 */

public class ShopTypesBean extends BaseBean<List<ShopTypesBean>> {

    /**
     * id : 2
     * name : iPhone
     * parentId : 1
     * iconSrc : null
     * child : [{"id":3,"name":"iPhone7","parentId":2,"iconSrc":"http://fastdfs.dafysz.cn/group1/M00/00/84/CgoLiFkpQ-aAUiEDAAAMI-eSuo4981.jpg","child":null},{"id":4,"name":"iPhone8","parentId":2,"iconSrc":"http://fastdfs.dafysz.cn/group1/M00/00/84/CgoLiFkpO6eAcv_CAAAkUHV7Kdk206.jpg","child":null},{"id":5,"name":"iPhone9","parentId":2,"iconSrc":"http://fastdfs.dafysz.cn/group1/M00/00/84/CgoLiFkpPzCAHfohAAAMI-eSuo4938.jpg","child":null}]
     */

    private int id;
    private String name;
    private int parentId;
    private Object iconSrc;
    private List<ChildBean> child;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public Object getIconSrc() {
        return iconSrc;
    }

    public void setIconSrc(Object iconSrc) {
        this.iconSrc = iconSrc;
    }

    public List<ChildBean> getChild() {
        return child;
    }

    public void setChild(List<ChildBean> child) {
        this.child = child;
    }

    public static class ChildBean {
        /**
         * id : 3
         * name : iPhone7
         * parentId : 2
         * iconSrc : http://fastdfs.dafysz.cn/group1/M00/00/84/CgoLiFkpQ-aAUiEDAAAMI-eSuo4981.jpg
         * child : null
         */

        private int id;
        private String name;
        private int parentId;
        private String iconSrc;
        private Object child;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public String getIconSrc() {
            return iconSrc;
        }

        public void setIconSrc(String iconSrc) {
            this.iconSrc = iconSrc;
        }

        public Object getChild() {
            return child;
        }

        public void setChild(Object child) {
            this.child = child;
        }
    }
}
