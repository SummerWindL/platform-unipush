package com.platform.unipush.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: middle-server
 * @description: 通知消息格式 （APP通知栏通知消息）
 * @author: fuyl
 * @create: 2020-06-17 20:50
 **/
@Data
public class Notify implements Serializable {
    private String msgtitle;
    private String msgdigest;
}
