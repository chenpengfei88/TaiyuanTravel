package com.chenpengfei.taiyuantravel.pojo;

/**
 *  @copyright  陈鹏飞
 *  @author pengfei.chen
 *  @email 450032215@qq.com
 *  @description  事件类型实体类
 */
public class EventType {

    private int type;

    private String content;

    public EventType(int type) {
        this.type = type;
    }

    public EventType(int type, String content) {
        this.type = type;
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
