package com.platform.unipush.demo;

import com.gexin.rp.sdk.http.IGtPush;

/**
 * AppInfo 相关信息
 *
 * @author zhangwf
 * @see
 * @since 2019-07-09
 */
public class AppInfo {
    public static final String APPID = "meiyFC5uhS9WTraRD9mx66";
    public static final String APPKEY = "D3il2JRXKGARgbWXCgq0v9";
    public static final String MASTERSECRET = "Zwa2i45jzf96ux1iWqDJvA";

    public static final String CID = "9c9397b4b7a713fb2c1bb9a911684aa9";
    public static final String CID_2 = "";

    public static final String DEVICETOKEN = "";

    public static final String ALIAS = "alias1";
    public static final String ALIAS_2 = "alias2";

    public static final String TAG = "tag1";
    public static final String TAG_2 = "tag2";

    public static final String PNMD5 = "xxxx";

    public static IGtPush push = new IGtPush(APPKEY, MASTERSECRET);
}
