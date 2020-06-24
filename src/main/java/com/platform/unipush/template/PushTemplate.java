package com.platform.unipush.template;

import com.alibaba.fastjson.JSONObject;
import com.gexin.rp.sdk.base.notify.Notify;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.base.payload.MultiMedia;
import com.gexin.rp.sdk.dto.GtReq;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.gexin.rp.sdk.template.style.AbstractNotifyStyle;
import com.platform.unipush.bean.MqCmd;

/**
 * @program: middle-server
 * @description: 个推消息模板
 * @author: fuyl
 * @create: 2020-06-18 15:54
 **/

public class PushTemplate {

    /**
     * 点击通知打开应用模板, 在通知栏显示一条含图标、标题等的通知，用户点击后激活您的应用。
     * 通知模板(点击后续行为: 支持打开应用、发送透传内容、打开应用同时接收到透传 这三种行为)
     * @return
     */
    public static NotificationTemplate getNotificationTemplate(String appId,
                                                               String appKey,
                                                               Integer transmissionType,
                                                               String transmissionContent,
                                                               AbstractNotifyStyle style) {
        NotificationTemplate template = new NotificationTemplate();
        // 设置APPID与APPKEY
        template.setAppId(appId);
        template.setAppkey(appKey);
        //设置展示样式
        template.setStyle(style);
        template.setTransmissionType(transmissionType);  // 透传消息设置，收到消息是否立即启动应用： 1为立即启动，2则广播等待客户端自启动
        template.setTransmissionContent(transmissionContent);
//        template.setSmsInfo(PushSmsInfo.getSmsInfo()); //短信补量推送

//        template.setDuration("2019-07-09 11:40:00", "2019-07-09 12:24:00");  // 设置定时展示时间，安卓机型可用
//        template.setNotifyid(123); // 在消息推送的时候设置notifyid。如果需要覆盖此条消息，则下次使用相同的notifyid发一条新的消息。客户端sdk会根据notifyid进行覆盖。
        template.setAPNInfo(getAPNPayload(transmissionContent,false)); //ios消息推送
        return template;
    }

    /**
     * 点击通知打开(第三方)网页模板, 在通知栏显示一条含图标、标题等的通知，用户点击可打开您指定的网页。
     * @return
     */
    public static LinkTemplate getLinkTemplate(String appId,
                                               String appKey,
                                               String url,
                                               AbstractNotifyStyle style) {
        LinkTemplate template = new LinkTemplate();
        // 设置APPID与APPKEY
        template.setAppId(appId);
        template.setAppkey(appKey);

        //设置展示样式
        template.setStyle(style);
        template.setUrl(url);  // 设置打开的网址地址
//        template.setNotifyid(123); // 在消息推送的时候设置notifyid。如果需要覆盖此条消息，则下次使用相同的notifyid发一条新的消息。客户端sdk会根据notifyid进行覆盖。
//         template.setSmsInfo(PushSmsInfo.getSmsInfo()); //短信补量推送
//        template.setDuration("2019-07-09 11:40:00", "2019-07-09 12:24:00");  // 设置定时展示时间，安卓机型可用
        return template;
    }


    /**
     * 透传消息模版,透传消息是指消息传递到客户端只有消息内容，展现形式由客户端自行定义。客户端可自定义通知的展现形式，也可自定义通知到达之后的动作，或者不做任何展现。
     * @return
     */
    public static TransmissionTemplate getTransmissionTemplate(String appId,
                                                               String appKey,
                                                               Integer transmissionType,
                                                               String transmissionContent) {
        TransmissionTemplate template = new TransmissionTemplate();
        // 设置APPID与APPKEY
        template.setAppId(appId);
        template.setAppkey(appKey);

        //透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(transmissionType);
        template.setTransmissionContent(transmissionContent); //透传内容
        template.setAPNInfo(getAPNPayload(transmissionContent,false)); //ios消息推送
//        template.setAPNInfo(getVoIPPayload());
//        template.setSmsInfo(PushSmsInfo.getSmsInfo()); //短信补量推送
        return template;
    }

    /**
     * IOS消息体结构 payload
     * @param msg 消息内容
     * @param isOffline 是否离线
     * @return
     */
    private static APNPayload getAPNPayload(String msg,boolean isOffline) {
        APNPayload payload = new APNPayload();
        //在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，显示指定数字
        payload.setAutoBadge("+1");
        payload.setContentAvailable(0);
        //ios 12.0 以上可以使用 Dictionary 类型的 sound
        payload.setSound("default");
        payload.setCategory("$由客户端定义");
        payload.addCustomMsg("由客户自定义消息key", "由客户自定义消息value");

        //简单模式APNPayload.SimpleMsg
        /**
         *  IOS 需要在转换一遍
         *   content :cmdMsg,cmdNo,cmdType 组成的String 字符串 将string转换为MqCmd
         */
        MqCmd mqCmd = JSONObject.parseObject(msg, MqCmd.class);
        JSONObject json = (JSONObject)JSONObject.parse(mqCmd.getCmdMsg());
        if(isOffline){ //离线 直接返回msgdigest
            payload.setAlertMsg(new APNPayload.SimpleAlertMsg(json.get("msgdigest").toString()));
        }else{ //在线 直接推msg回去
            payload.setAlertMsg(new APNPayload.SimpleAlertMsg(msg));
        }

//         payload.setAlertMsg(getDictionaryAlertMsg("","",""));  //字典模式使用APNPayload.DictionaryAlertMsg

        //设置语音播报类型，int类型，0.不可用 1.播放body 2.播放自定义文本
        payload.setVoicePlayType(2);
        //设置语音播报内容，String类型，非必须参数，用户自定义播放内容，仅在voicePlayMessage=2时生效
        //注：当"定义类型"=2, "定义内容"为空时则忽略不播放
        payload.setVoicePlayMessage("定义内容");

        // 添加多媒体资源
        payload.addMultiMedia(new MultiMedia()
                .setResType(MultiMedia.MediaType.pic)
                .setResUrl("资源文件地址")
                .setOnlyWifi(true));

        return payload;
    }

    private static APNPayload.DictionaryAlertMsg getDictionaryAlertMsg(String msgContent,
                                                                       String title,
                                                                       String subTitle) {
        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
        alertMsg.setBody("body1");
        alertMsg.setActionLocKey("显示关闭和查看两个按钮的消息");
        alertMsg.setLocKey("loc-key");
        alertMsg.addLocArg("loc-ary1");
        alertMsg.addLocArg("loc-ary2");
        alertMsg.setLaunchImage("调用已经在应用程序中绑定的图形文件名");
        // iOS8.2以上版本支持
        alertMsg.setTitle("通知标题");
        alertMsg.setTitleLocKey("自定义通知标题");
        alertMsg.addTitleLocArg("自定义通知标题组1");
        alertMsg.addTitleLocArg("自定义通知标题组2");

        alertMsg.setSubtitle("sub-title");
        alertMsg.setSubtitleLocKey("sub-loc-key1");
        alertMsg.addSubtitleLocArgs("sub-loc-arg1");
        alertMsg.addSubtitleLocArgs("sub-loc-arg2");
        return alertMsg;
    }

    /**
     * 厂商下发通知模板
     * @param appId
     * @param appKey
     * @return
     */
    public static TransmissionTemplate getManufacturerTransmissionTemplate( String appId,
                                                                            String appKey,
                                                                            String content,
                                                                            String title,
                                                                            String notifycontent){
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTransmissionContent(content);
        /**
         *   content :cmdMsg,cmdNo,cmdType 组成的String 字符串 将string转换为MqCmd
         */
        MqCmd mqCmd = JSONObject.parseObject(content, MqCmd.class);
        JSONObject json = (JSONObject)JSONObject.parse(mqCmd.getCmdMsg());
        //组装 安卓的 payload数据
        JSONObject payloadJson = new JSONObject();
        payloadJson.put("appointserialid",json.get("appointserialid").toString());
        template.setTransmissionType(2);
        Notify notify = new Notify();
        notify.setIntent("intent:#Intent;package=com.ikinloop.healthapp.banshan;launchFlags=0x14000000;component=com.ikinloop.healthapp.banshan/io.dcloud.PandoraEntry;S.UP-OL-SU=true;S.title="+title+";S.content="+notifycontent+";S.payload="+payloadJson.toJSONString()+";end");
        notify.setTitle(title);
        notify.setContent(notifycontent);
        notify.setPayload(payloadJson.toJSONString());
        notify.setType(GtReq.NotifyInfo.Type._intent);
//        notify.setUrl("https://dev.getui.com/");
//        notify.setType(Type._url);
        template.set3rdNotifyInfo(notify);//设置第三方通知
        template.setAPNInfo(getAPNPayload(content,true)); //ios消息推送
        return template;
    }
}
