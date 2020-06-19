package com.platform.unipush.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: middle-server
 * @description:
 * @author: fuyl
 * @create: 2020-06-17 20:50
 **/
@Data
public class Notify implements Serializable {
    private String msgtitle;
    private String msgdigest;
}
