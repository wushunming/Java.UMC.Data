package UMC.Net;

import UMC.Data.Provider;

import java.util.Map;

public abstract class Message extends UMC.Data.DataProvider {

    protected static Message instance;

    public static Message instance() {
        if (instance == null) {
            instance = (Message) UMC.Data.Utility.createObject("Message");//as Message;
            if (instance == null) {
                instance = new Message() {

                };
                instance.provider = Provider.create("Message", "UMC.Data.Message");
            }
        }
        return instance;
    }


    /**
     * 短信模板发送
     * @param type 对应模板
     * @param data 替换的字典
     * @param mobile 手机号码
     */
    public void send(String type, Map data, String mobile) {

    }

    /**
     * 发送短信
     * @param content 短信内容
     * @param to 手机号码
     */
    public void send(String content, String... to) {

    }

    public void sendEmail(String title, String context, String... to) {

    }
}
