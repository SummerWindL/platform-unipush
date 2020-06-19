# platform-unipush
集成个推的unipush推送maven 开箱即用

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



