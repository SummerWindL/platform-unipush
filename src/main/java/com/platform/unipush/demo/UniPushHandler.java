package com.platform.unipush.demo;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.IQueryResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.style.Style0;
import com.platform.unipush.config.UniPushConfigService;
import com.platform.unipush.config.UniPushProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.platform.unipush.demo.AppInfo.*;

/**
 * @program: platform-base
 * @description: 个推
 * @author: fuyl
 * @create: 2020-05-29 17:30
 **/

public class UniPushHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static UniPushProperties uniPushProperties = UniPushConfigService.getBean("uniPushProperties");
    // STEP1：获取应用基本信息
    //半山健康
    private static String appId = "meiyFC5uhS9WTraRD9mx66";
    private static String appKey = "D3il2JRXKGARgbWXCgq0v9";
    private static String masterSecret = "Zwa2i45jzf96ux1iWqDJvA";
    private static String CID = "552619364f117d5f07f69ee374a673fa";
    private static String HOST = "http://sdk.open.api.igexin.com/apiex.htm";
    //个推demo
//    private static String appId = "5qJ7a1uI3a6NkmHjFty6l4";
//    private static String appKey = "eNJ8so8kq77bHwKmJLLv09";
//    private static String masterSecret = "QQ9TtJLUyF8rMW9tZAkmPA";
//    private static String CID = "ae6fd5c5ffb331d4781121135bd61be5";

    public static void main(String[] args) throws IOException {
        IGtPush push = new IGtPush(appKey, masterSecret);
        Style0 style = new Style0();
        // STEP2：设置推送标题、推送内容
        style.setTitle("伴山健康");
        style.setText("测试发送消息544445");
        style.setLogo("push.png");  // 设置推送图标
        // STEP3：设置响铃、震动等推送效果
        style.setRing(true);  // 设置响铃
        style.setVibrate(true);  // 设置震动

        IQueryResult result = push.getClientIdStatus(APPID, CID);
        System.out.println(result.getResponse());

        // STEP4：选择通知模板
        NotificationTemplate template = new NotificationTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setStyle(style);
//        TransmissionTemplate transmissionTemplate = PushTemplate.getTransmissionTemplate("","",2,"");

        Target target = new Target();
        target.setAppId(appId);
        target.setClientId(CID);


        // STEP5：定义"AppMessage"类型消息对象,设置推送消息有效期等推送参数
        List<String> appIds = new ArrayList<String>();
        appIds.add(appId);
        AppMessage message = new AppMessage();
        message.setData(template);
        message.setAppIdList(appIds);
        message.setOffline(true);
        message.setOfflineExpireTime(1000 * 600);  // 时间单位为毫秒

        SingleMessage message1 =new SingleMessage();
        message1.setData(template);
        message1.setOffline(true);
        message1.setOfflineExpireTime(1000 * 600);  // 时间单位为毫秒
        // STEP6：执行推送
//        IPushResult ret = push.pushMessageToApp(message);
        IPushResult ret = push.pushMessageToSingle(message1,target);
        System.out.println(ret.getResponse().toString());
    }
}
