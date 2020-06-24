# platform-unipush
集成个推的unipush推送maven 开箱即用

### 可自行开启线程执行push推送 达到不阻塞主业务执行的效果
### 依赖
```
<dependency>
    <groupId>com.platform.unipush</groupId>
    <artifactId>platform-unipush</artifactId>
    <version>0.1</version>
</dependency>
```

### 接口
```
void transmission(String cmdNo, String cmdMsg, String ostype);
void notifyOpenApp(String cmdNo, String msgtitle, String cmdMsg, String ostype);
void singleTransmission(String clinetid, String cmdNo, String cmdMsg, String ostype);
void singleManufacturerTransmission(String clinetid, String cmdNo, String cmdMsg, String ostype);
void singleNotifyOpenApp(String clinetid, String cmdNo, String msgtitle, String cmdMsg, String ostype);
IQueryResult getClientIdStatus(String appid, String clientid);
boolean validateConnectIgetuiService(String appId, String appKey, String masterSecret);
```

### 使用

构建推送服务
        
```  
PushService pushService = new PushService("pushTypeCode","appId","appKey","masterSecret",75000);
```
示例代码，仅供参考
```
//3.构建推送服务
            pushService = new PushService(pushCfgParams.getPushtypecode(),
                    pushCfgParams.getPushappid(),pushCfgParams.getPushappkey(),pushCfgParams.getPushmastersecret(),pushCfgParams.getExpiretime());
            lock.lock();
            try{
                //4.循环单推
                if(ssPushParams.size()>0){
                    SsPushParam ssPushParam = ssPushParams.get(0);
//                    for(SsPushParam ssPushParam : ssPushParams){
                        //发送给个人 判断client是否在线 且用户处于登录状态 redis拿到登录信息
//                        TAppLoginCache loginCache = ServiceBeanConfig.baseComponent.getRedisComponent().get(params.get("ssid"));
//                        log.info("loginCache:[{}]",loginCache);
//                        if (loginCache != null) { //登录状态才允许推送
                            if(pushService.getClientIdStatus(pushCfgParams.getPushappid(),ssPushParam.getClientid())){
                                //透传
                                log.info("用户 [{}] 登录：[{}] 客户端 [{}] 在线，个推透传开始",params.get("ssid"),ssPushParam.getOstypename(),ssPushParam.getClientid());
                                pushService.singleTransmission(ssPushParam.getClientid(), Constants.CMD2APP,JSONObject.toJSONString(cmdParam),"");
                                log.info("用户 [{}] 登录：[{}] 客户端 [{}] 在线，个推透传结束",params.get("ssid"),ssPushParam.getOstypename(),ssPushParam.getClientid());
//                        pushService.singleNotifyOpenApp(ssPushParam.getClientid(),cmdNo,"您有一条新消息！",params.get("content").toString(),"");
                            }else{ //离线 厂商下发透传
                                //通知
                                log.info("用户 [{}] 登录：[{}] 客户端 [{}] 离线，厂商推送开始",params.get("ssid"),ssPushParam.getOstypename(),ssPushParam.getClientid());
                                pushService.singleManufacturerTransmission(ssPushParam.getClientid(),Constants.CMD2APP,JSONObject.toJSONString(cmdParam),"");
                                log.info("用户 [{}] 登录：[{}] 客户端 [{}] 离线，厂商推送结束",params.get("ssid"),ssPushParam.getOstypename(),ssPushParam.getClientid());
//                        pushService.singleNotifyOpenApp(ssPushParam.getClientid(),cmdNo,"您有一条新消息！",params.get("content").toString(),"");
                            }
//                        }else{
//                            log.error("用户未登录，不推送消息！");
//                        }

//                    }

                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
```
通知结构体可自行定义：
```json
{"appointserialid":"ed3d6ec895424759bfddfb2697ce25ce","doctorid":"e783e30922a545a0a19908006250c6bc","hospcode":"5101003010004","msgdigest":"您有一条新消息！","msgtitle":"XXXAPP","msgway":"","serialid":"59114024cfc74d59b3c1775abc508134","ssid":"ecb9c50241a847b789cd850be25eee5c"}
```


### application.yml
```yml
logging:
  file: logs/platform-unipush.log
  level:
    root: INFO
    com:
      ikinloop: DEBUG

unipush:
  app-id: 5qJ7a1uI3a6NkmHjFty6l4
  app-key: eNJ8so8kq77bHwKmJLLv09
  master-secret: QQ9TtJLUyF8rMW9tZAkmPA
  url: http://sdk.open.api.igexin.com/apiex.htm
```



