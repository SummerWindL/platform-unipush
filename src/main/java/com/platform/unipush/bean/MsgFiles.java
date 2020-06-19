package com.platform.unipush.bean;

import lombok.Data;

/**
 * @program: platform-base
 * @description:
 * @author: fuyl
 * @create: 2020-06-02 16:47
 **/
@Data
public class MsgFiles {
    private String fileid;
    private String filetypecode;
    private String filetypename;
    private String filename;
    private String fileextname;
    private String fileencode;
    private String filecontent;
    private String filelocationflag;
    private String fileurl;
}
