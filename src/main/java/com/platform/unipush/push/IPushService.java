package com.platform.unipush.push;


import com.gexin.rp.sdk.base.IQueryResult;

/**
 * @program: platform-base
 * @description:
 * @author: fuyl
 * @create: 2020-06-01 15:04
 **/

public interface IPushService {

     void transmission(String cmdNo, String cmdMsg, String ostype);
     void notifyOpenApp(String cmdNo, String msgtitle, String cmdMsg, String ostype);
     void singleTransmission(String clinetid, String cmdNo, String cmdMsg, String ostype);
     void singleManufacturerTransmission(String clinetid, String cmdNo, String cmdMsg, String ostype);
     void singleNotifyOpenApp(String clinetid, String cmdNo, String msgtitle, String cmdMsg, String ostype);
     IQueryResult getClientIdStatus(String appid, String clientid);

     /**
      * 验证unipush配置是否生效
      * @param appId
      * @param appKey
      * @param masterSecret
      * @return
      */
     boolean validateConnectIgetuiService(String appId, String appKey, String masterSecret);
     //     void notifyWithLink(String url,ZxMsg zxMsg);
//     void singleNotifyWithLink(String url,String clinetid,ZxMsg zxMsg);

}
