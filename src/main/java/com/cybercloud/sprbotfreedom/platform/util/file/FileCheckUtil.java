package com.cybercloud.sprbotfreedom.platform.util.file;

import com.cybercloud.sprbotfreedom.platform.util.CheckFileTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件检查工具类
 * @author liuyutang
 */
@Slf4j
public class FileCheckUtil {

    private static final String NULLSTR = "空";
    private static final String PIPE = "|";
    private static final String ERROR_MESSAGE = "未读取到文件信息";
    /**
     * 检查文件类型
     * @param multipartFile 上传文件内容
     * @throws Exception
     */
    public static void checkFileType(MultipartFile multipartFile)throws Exception{
        //检查扩展名是否合法
        if(multipartFile==null){throw new Exception(ERROR_MESSAGE);};
        String fileName = multipartFile.getOriginalFilename();
        String extNameStr="|doc|docx|xls|xlsx|pdf|jpg|jpeg|png|bmp|zip|rar|7z|kmz|mp4|wmv|";
        String extendFileName=(null==fileName || fileName.indexOf(".")<0)?NULLSTR:fileName.substring(fileName.lastIndexOf(".")+1);
        if(NULLSTR.equals(extendFileName) || extNameStr.indexOf(PIPE+extendFileName+PIPE)<0) {
            throw new Exception("不允许上传扩展名为 "+extendFileName+" 的文件");
        }else {
            //检查文件内容是否与扩展名符合
            CheckFileTypeUtil cu=new CheckFileTypeUtil();
            String mType=multipartFile.getContentType();
            String tType=cu.getFileMimeType(multipartFile.getInputStream(),multipartFile.getOriginalFilename());
            Boolean sameFlag=mType.equals(tType);
            if(!sameFlag){
                Boolean image = ("image/bmp".equalsIgnoreCase(mType)|| "application/octet-stream".equalsIgnoreCase(mType))
                        && ("image/bmp".equalsIgnoreCase(tType) || "image/x-ms-bmp".equalsIgnoreCase(tType));
                Boolean zip = ("application/zip".equalsIgnoreCase(mType) || "application/x-zip-compressed".equalsIgnoreCase(mType) || "application/octet-stream".equalsIgnoreCase(mType))
                        && ("application/zip".equalsIgnoreCase(tType) || "application/x-zip-compressed".equalsIgnoreCase(tType));
                Boolean rar = ("application/rar".equalsIgnoreCase(mType) || "application/x-rar-compressed".equalsIgnoreCase(mType) || "application/octet-stream".equalsIgnoreCase(mType))
                        && ("application/rar".equalsIgnoreCase(tType)|| "application/x-rar-compressed".equalsIgnoreCase(tType));
                Boolean sevenZ =("application/7z".equalsIgnoreCase(mType) || "application/x-7z-compressed".equalsIgnoreCase(mType) || "application/octet-stream".equalsIgnoreCase(mType))
                        && ("application/7z".equalsIgnoreCase(tType) ||  "application/x-7z-compressed".equalsIgnoreCase(tType));
                Boolean word = ("application/doc".equalsIgnoreCase(mType) || "application/docx".equalsIgnoreCase(mType) || "application/msword".equalsIgnoreCase(mType)
                        || "application/vnd.ms-word".equalsIgnoreCase(mType) || "application/vnd.openxmlformats-officedocument.wordprocessingml.document".equalsIgnoreCase(mType)
                        || "application/kswps".equalsIgnoreCase(mType) || "application/octet-stream".equalsIgnoreCase(mType))
                        && ("application/doc".equalsIgnoreCase(tType) || "application/docx".equalsIgnoreCase(tType) || "application/msword".equalsIgnoreCase(tType)
                        || "application/vnd.ms-word".equalsIgnoreCase(tType) || "application/vnd.openxmlformats-officedocument.wordprocessingml.document".equalsIgnoreCase(tType)
                        || "application/kswps".equalsIgnoreCase(tType));
                Boolean excel = ("application/xls".equalsIgnoreCase(mType) || "application/xlsx".equalsIgnoreCase(mType) || "application/msexcel".equalsIgnoreCase(mType)
                        || "application/vnd.ms-excel".equalsIgnoreCase(mType) || "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equalsIgnoreCase(mType)
                        || "application/kset".equalsIgnoreCase(mType) || "application/octet-stream".equalsIgnoreCase(mType))
                        && ("application/xls".equalsIgnoreCase(tType) || "application/xlsx".equalsIgnoreCase(tType) || "application/msexcel".equalsIgnoreCase(tType)
                        || "application/vnd.ms-excel".equalsIgnoreCase(tType) || "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equalsIgnoreCase(tType)
                        || "application/kset".equalsIgnoreCase(tType));

                if(image || zip || rar || sevenZ || word || excel){
                    sameFlag=true;
                }
            }

            if(!sameFlag){
                log.error(multipartFile.getContentType());
                log.error(cu.getFileMimeType(multipartFile.getInputStream(),multipartFile.getOriginalFilename()));
                throw new Exception("上传文件内容与扩展名不符合");
            }
        }
    }
}
