package top.thanks_code.xiaoturobot.bean;

import top.thanks_code.xiaoturobot.enumeration.MessageType;

/**
 * 消息实体类，定义了消息的内容和类型
 * Created by Administrator on 2017/9/9.
 */

public class Message {

    private String msg;

    private MessageType mType;

    public MessageType getType() {
        return mType;
    }

    public void setType(MessageType type) {
        mType = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
