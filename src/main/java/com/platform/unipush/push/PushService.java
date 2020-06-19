package com.platform.unipush.push;

import com.gexin.rp.sdk.base.IQueryResult;

import java.util.Map;


/**
 * @program: platform-base
 * @description:
 * @author: fuyl
 * @create: 2020-06-02 17:30
 **/

public class PushService {
    public static final String MQ="1";
    public static final String UNIPUSH="2";
    public static final String ONLINE ="Online";
    public static final String OFFLINE ="Offline";
    private IPushService pushService  = null;

    public PushService(String pushTypeCode,String appId, String appKey, String masterSecret, Integer expireTime) {
      pushService = new UniPushService(appId, appKey, masterSecret, expireTime);
    }

    public void transmission(String cmdNo,String cmdMsg,String ostype){

        pushService.transmission(cmdNo,cmdMsg,ostype);
    }
    public void notifyOpenApp(String cmdNo,String msgtitle,String cmdMsg,String ostype){
        pushService.notifyOpenApp(cmdNo,msgtitle,cmdMsg,ostype);
    }

    public void singleTransmission(String clinetid,String cmdNo,String cmdMsg,String ostype){
        pushService.singleTransmission(clinetid,cmdNo,cmdMsg,ostype);
    }

    public void singleManufacturerTransmission(String clinetid,String cmdNo,String cmdMsg,String ostype){
        pushService.singleManufacturerTransmission(clinetid,cmdNo,cmdMsg,ostype);
    }

    public void singleNotifyOpenApp(String clinetid,String cmdNo,String msgtitle,String cmdMsg,String ostype){
        pushService.singleNotifyOpenApp(clinetid,cmdNo,msgtitle,cmdMsg,ostype);
    }

    public boolean getClientIdStatus(String appid, String clientid) {
        IQueryResult clientIdStatus = pushService.getClientIdStatus(appid, clientid);
        Map<String, Object> response = clientIdStatus.getResponse();
        if(ONLINE.equals(response.get("result").toString())){
            return true;
        }else if(OFFLINE.equals(response.get("result").toString())){
            return false;
        }
        return false;
    }

    public boolean validateConnectIgetuiService(String appId,String appKey,String masterSecret){
        return pushService.validateConnectIgetuiService(appId,appKey,masterSecret);
    }


}
