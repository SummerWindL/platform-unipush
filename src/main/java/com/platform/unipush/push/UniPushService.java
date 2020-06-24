package com.platform.unipush.push;

import cn.hutool.json.JSONUtil;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.IQueryResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.GtPush;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.gexin.rp.sdk.template.style.AbstractNotifyStyle;
import com.platform.unipush.bean.MqCmd;
import com.platform.unipush.bean.Notify;
import com.platform.unipush.template.PushStyle;
import com.platform.unipush.template.PushTemplate;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @program: platform-base
 * @description: unipush 服务
 * @author: fuyl
 * @create: 2020-06-01 10:38
 **/
@Slf4j
public class UniPushService implements IPushService{

    private Integer expiretime;
    private String appId;
    private String appKey;
    private String masterSecret;

    public UniPushService(String appId, String appKey, String masterSecret, Integer expiretime) {
        this.expiretime = expiretime;
        this.appId = appId;
        this.appKey = appKey;
        this.masterSecret = masterSecret;
    }


    /**
     * 官方样式 Style0
     * @param notifyTitle 消息标题
     * @param notifyContent 消息内容
     * @param notifyLogo 消息LOGO
     * @param isRing 是否响铃
     * @param isVibrate 是否震动
     * @param isClearable 是否允许清除
     * @return
     */
    private AbstractNotifyStyle getNotifyStyle0(String notifyTitle,
                                                String notifyContent,
                                                String notifyLogo,
                                                boolean isRing,
                                                boolean isVibrate,
                                                boolean isClearable){
        return PushStyle.getStyle0(notifyTitle,notifyContent,notifyLogo,isRing,isVibrate,isClearable);
    }

    /**
     * 获取通知模板
     * @param appId
     * @param appKey
     * @param transmissionType
     * @param transmissionContent
     * @param style
     * @return
     */
    private NotificationTemplate getNotifictionTemplate(String appId,
                                                        String appKey,
                                                        Integer transmissionType,
                                                        String transmissionContent,
                                                        AbstractNotifyStyle style){
        return PushTemplate.getNotificationTemplate(appId,appKey,transmissionType,transmissionContent,style);
    }

    /**
     * 获取透传消息模板
     * @param appId APPID
     * @param appKey APPKEY
     * @param transmissionType 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
     * @param transmissionContent 透传消息内容
     * @return
     */
    public TransmissionTemplate getTransmissionTemplate(String appId,
                                                        String appKey,
                                                        Integer transmissionType,
                                                        String transmissionContent){
        return PushTemplate.getTransmissionTemplate(appId,appKey,transmissionType,transmissionContent);
    }

    /**
     * 获取连接模板
     * @param appId
     * @param appKey
     * @param url
     * @param style
     * @return
     */
    public LinkTemplate getLinkTemplate(String appId,
                                        String appKey,
                                        String url,
                                        AbstractNotifyStyle style){
        return PushTemplate.getLinkTemplate(appId,appKey,url,style);
    }

    /**
     * 获取厂商推送透传模板
     * @param content
     * @param title
     * @param notifycontent
     * @param appId
     * @param appKey
     * @return
     */
    public TransmissionTemplate getManufacturerTransmissionTemplate(String appId,
                                                                    String appKey,
                                                                    String content,
                                                                    String title,
                                                                    String notifycontent){
        return PushTemplate.getManufacturerTransmissionTemplate(appId,appKey,content,title,notifycontent);
    }

    /**
     * 获取用户状态
     * @param appid
     * @param clientid
     * @return
     */
    @Override
    public IQueryResult getClientIdStatus(String appid, String clientid) {
        IGtPush push = new IGtPush(this.appKey,this.masterSecret);
        IQueryResult ret = push.getClientIdStatus(appid, clientid);
        log.info("userstauts:[{}]",ret.getResponse());
        return ret;
    }

    /**
     * 验证unipush配置是否生效
     * 只校验appKey masterKey
     * 不使用IGtPush 因为里面的实现是采用Map形式 存放之前校验过成功的参数 再次校验不在获取masterSecret
     *
     * @param appId
     * @param appKey
     * @param masterSecret
     * @return
     */
    @Override
    public boolean validateConnectIgetuiService(String appId, String appKey, String masterSecret) {
        String key = genKey(null,appKey,null);
        GtPush push = new GtPush(null,appKey,masterSecret,null,key);
        try {
            if(push.connect()){
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                push.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private String genKey(String host, String appKey, Boolean useSSL) {
        return host + "|" + appKey + "|" + (useSSL == null ? false : useSSL);
    }


    @Override
    public void transmission(String cmdNo,String cmdMsg,String ostype)  {

        MqCmd mqCmd = new MqCmd();
        mqCmd.setCmdNo(cmdNo);
        mqCmd.setCmdMsg(cmdMsg);
        JSONObject mqCmdJson = JSONObject.fromObject(mqCmd);

        TransmissionTemplate template = getTransmissionTemplate(this.appId,
                this.appKey, 2, mqCmdJson.toString());
        // STEP5：定义"AppMessage"类型消息对象,设置推送消息有效期等推送参数
        List<String> appIds = new ArrayList<String>();
        appIds.add(appId);
        AppMessage message = new AppMessage();
        message.setData(template);
        message.setAppIdList(appIds);
        message.setOffline(true);
        message.setOfflineExpireTime(this.expiretime);  // 时间单位为毫秒
        // 厂商下发策略；1: 个推通道优先，在线经个推通道下发，离线经厂商下发(默认);2: 在离线只经厂商下发;3: 在离线只经个推通道下发;4: 优先经厂商下发，失败后经个推通道下发;
        message.setStrategyJson("{\"default\":4,\"ios\":4,\"st\":4}");
        // STEP6：执行推送
        IGtPush push = new IGtPush(this.appKey,this.masterSecret);
        IPushResult ret = push.pushMessageToApp(message);
        log.info("消息 [{}] ：[{}] 发送成功; 返回信息：[{}]",cmdNo,cmdMsg,ret.getResponse().toString());

    }

    @Override
    public void notifyOpenApp(String cmdNo, String msgtitle,String cmdMsg,String ostype) {

        MqCmd mqCmd = new MqCmd();
        mqCmd.setCmdNo(cmdNo);
        mqCmd.setCmdMsg(cmdMsg);
        JSONObject mqCmdJson = JSONObject.fromObject(mqCmd);

        //组装样式 获取notifytitle，notifycontent
        AbstractNotifyStyle style = getNotifyStyle0(msgtitle,cmdMsg,"",true,true,true);
        NotificationTemplate notifictionTemplate = getNotifictionTemplate(this.appId,
                this.appKey, 2, mqCmdJson.toString(), style);
        // STEP5：定义"AppMessage"类型消息对象,设置推送消息有效期等推送参数
        List<String> appIds = new ArrayList<String>();
        appIds.add(appId);
        AppMessage message = new AppMessage();
        message.setData(notifictionTemplate);
        message.setAppIdList(appIds);
        message.setOffline(true);
        message.setOfflineExpireTime(this.expiretime);  // 时间单位为毫秒
        // 厂商下发策略；1: 个推通道优先，在线经个推通道下发，离线经厂商下发(默认);2: 在离线只经厂商下发;3: 在离线只经个推通道下发;4: 优先经厂商下发，失败后经个推通道下发;
        message.setStrategyJson("{\"default\":4,\"ios\":4,\"st\":4}");
        // STEP6：执行推送
        IGtPush push = new IGtPush(this.appKey,this.masterSecret);
        IPushResult ret = push.pushMessageToApp(message);
        log.info("消息 [{}] ：[{}] 发送成功; 返回信息：[{}]",cmdNo,cmdMsg,ret.getResponse().toString());
    }


    @Override
    public void singleTransmission(String clinetid,String cmdNo,String cmdMsg,String ostype) {

        MqCmd mqCmd = new MqCmd();
        mqCmd.setCmdNo(cmdNo);
        mqCmd.setCmdMsg(cmdMsg);
        JSONObject mqCmdJson = JSONObject.fromObject(mqCmd);

        TransmissionTemplate template = getTransmissionTemplate(this.appId,
                this.appKey, 2, mqCmdJson.toString());
        Target target = new Target();
        target.setClientId(clinetid);
        target.setAppId(this.appId);

        SingleMessage message = new SingleMessage();
        message.setData(template);

        message.setOffline(true);
        message.setOfflineExpireTime(this.expiretime);  // 时间单位为毫秒
        // 厂商下发策略；1: 个推通道优先，在线经个推通道下发，离线经厂商下发(默认);2: 在离线只经厂商下发;3: 在离线只经个推通道下发;4: 优先经厂商下发，失败后经个推通道下发;
        message.setStrategyJson("{\"default\":4,\"ios\":4,\"st\":4}");
        // STEP6：执行推送
        IGtPush push = new IGtPush(this.appKey,this.masterSecret);
        IPushResult ret = push.pushMessageToSingle(message,target);
        log.info("消息 [{}] ：[{}] 发送成功; 返回信息：[{}]",cmdNo,cmdMsg,ret.getResponse().toString());
    }

    @Override
    public void singleManufacturerTransmission(String clinetid, String cmdNo, String cmdMsg, String ostype) {
        MqCmd mqCmd = new MqCmd();
        mqCmd.setCmdNo(cmdNo);
        mqCmd.setCmdMsg(cmdMsg);
        JSONObject mqCmdJson = JSONObject.fromObject(mqCmd);
        //获取通知栏信息 title ，notifycontent
        Notify notify = JSONUtil.parseObj(cmdMsg).toBean(Notify.class);
        TransmissionTemplate template = getManufacturerTransmissionTemplate(this.appId,
                this.appKey, mqCmdJson.toString(), notify.getMsgtitle(),notify.getMsgdigest());
        Target target = new Target();
        target.setClientId(clinetid);
        target.setAppId(this.appId);

        SingleMessage message = new SingleMessage();
        message.setData(template);

        message.setOffline(true);
        message.setOfflineExpireTime(this.expiretime);  // 时间单位为毫秒
        // 厂商下发策略；1: 个推通道优先，在线经个推通道下发，离线经厂商下发(默认);2: 在离线只经厂商下发;3: 在离线只经个推通道下发;4: 优先经厂商下发，失败后经个推通道下发;
        message.setStrategyJson("{\"default\":4,\"ios\":4,\"st\":4}");
        // STEP6：执行推送
        IGtPush push = new IGtPush(this.appKey,this.masterSecret);
        IPushResult ret = push.pushMessageToSingle(message,target);
        log.info("消息 [{}] ：[{}] 发送成功; 返回信息：[{}]",cmdNo,cmdMsg,ret.getResponse().toString());
    }

    @Override
    public void singleNotifyOpenApp(String clinetid,String cmdNo, String msgtitle,String cmdMsg,String ostype) {

        MqCmd mqCmd = new MqCmd();
        mqCmd.setCmdNo(cmdNo);
        mqCmd.setCmdMsg(cmdMsg);
        JSONObject mqCmdJson = JSONObject.fromObject(mqCmd);

        //组装样式 获取notifytitle，notifycontent
        AbstractNotifyStyle style = getNotifyStyle0(msgtitle,cmdMsg,"",true,true,true);
        NotificationTemplate notifictionTemplate = getNotifictionTemplate(this.appId,
                this.appKey, 2, mqCmdJson.toString(), style);
        // STEP5：定义"AppMessage"类型消息对象,设置推送消息有效期等推送参数
        Target target = new Target();
        target.setClientId(clinetid);
        target.setAppId(this.appId);

        SingleMessage message = new SingleMessage();
        message.setData(notifictionTemplate);

        message.setOffline(true);
        message.setOfflineExpireTime(this.expiretime);  // 时间单位为毫秒
        // 厂商下发策略；1: 个推通道优先，在线经个推通道下发，离线经厂商下发(默认);2: 在离线只经厂商下发;3: 在离线只经个推通道下发;4: 优先经厂商下发，失败后经个推通道下发;
        message.setStrategyJson("{\"default\":4,\"ios\":4,\"st\":4}");
        // STEP6：执行推送
        IGtPush push = new IGtPush(this.appKey,this.masterSecret);
        IPushResult ret = push.pushMessageToSingle(message,target);
        log.info("消息 [{}] ：[{}] 发送成功; 返回信息：[{}]",cmdNo,cmdMsg,ret.getResponse().toString());
    }

    /**
     * 批量单推
     * <p>
     * 当单推任务较多时，推荐使用该接口，可以减少与服务端的交互次数。
     */
//    private static void pushToSingleBatch() {
//        IBatch batch = push.getBatch();
//
//        IPushResult ret = null;
//        try {
//            //构建客户a的透传消息a
//            constructClientTransMsg(CID, batch);
//            //构建客户B的点击通知打开网页消息b
//            constructClientLinkMsg(CID_2, batch);
//            ret = batch.submit();
//        } catch (Exception e) {
//            e.printStackTrace();
//            try {
//                ret = batch.retry();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//        if (ret != null) {
//            System.out.println(ret.getResponse().toString());
//        } else {
//            System.out.println("服务器响应异常");
//        }
//    }

   /* @Override
    public void singleNotifyWithLink(String url,String clinetid, ZxMsg zxMsg) {
        JSONObject jsonObject = JSONObject.fromObject(zxMsg);
        String msgcontent = jsonObject.get("msgcontent").toString();
        String msgtitle = jsonObject.get("msgtitle").toString();
        String msgno = jsonObject.get("msgNo").toString();
        //组装样式 获取notifytitle，notifycontent
        AbstractNotifyStyle style = getNotifyStyle0(msgtitle,msgcontent,"",true,true,true);
        LinkTemplate linkTemplate = getLinkTemplate(this.appId,
                this.appKey, "", style);
        Target target = new Target();
        target.setClientId(clinetid);
        target.setAppId(this.appId);
        SingleMessage message = new SingleMessage();
        message.setData(linkTemplate);
        message.setOffline(true);
        message.setOfflineExpireTime(this.expiretime);  // 时间单位为毫秒
        // STEP6：执行推送
        IGtPush push = new IGtPush(this.appKey,this.masterSecret);
        IPushResult ret = push.pushMessageToSingle(message,target);
        log.info("消息 [{}] ：[{}] 发送成功; 返回信息：[{}]",msgno,msgcontent,ret.getResponse().toString());
    }

    @Override
    public void notifyWithLink(ZxMsg zxMsg,String url) {
        JSONObject jsonObject = JSONObject.fromObject(zxMsg);
        String msgcontent = jsonObject.get("msgcontent").toString();
        String msgtitle = jsonObject.get("msgtitle").toString();


        //组装样式 获取notifytitle，notifycontent
        AbstractNotifyStyle style = getNotifyStyle0(msgtitle,msgcontent,"",true,true,true);
        LinkTemplate linkTemplate = getLinkTemplate(this.appId,
                this.appKey, "", style);
        // STEP5：定义"AppMessage"类型消息对象,设置推送消息有效期等推送参数
        List<String> appIds = new ArrayList<String>();
        appIds.add(appId);
        AppMessage message = new AppMessage();
        message.setData(linkTemplate);
        message.setAppIdList(appIds);
        message.setOffline(true);
        message.setOfflineExpireTime(this.expiretime);  // 时间单位为毫秒
        // STEP6：执行推送
        IGtPush push = new IGtPush(this.appKey,this.masterSecret);
        IPushResult ret = push.pushMessageToApp(message);
        log.info("消息 ：{} 发送成功; 返回信息：{}",msgcontent,ret.getResponse().toString());
    }*/


}
