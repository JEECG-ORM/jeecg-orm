package com.hongru.util;

import com.hongru.constant.CommonConstant;
import com.hongru.util.filter.FileTypeFilter;
import io.ebean.config.dbplatform.DbType;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.JdbcUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class CommonUtils {

   //中文正则
    private static Pattern ZHONGWEN_PATTERN = Pattern.compile("[\u4e00-\u9fa5]");
    public static String getFileExtendName(byte[] photoByte) {
        String strFileExtendName = "JPG";
        if (photoByte[0] == 71 && photoByte[1] == 73 && photoByte[2] == 70 && photoByte[3] == 56 && (photoByte[4] == 55 || photoByte[4] == 57) && photoByte[5] == 97) {
            strFileExtendName = "GIF";
        } else if (photoByte[6] == 74 && photoByte[7] == 70 && photoByte[8] == 73 && photoByte[9] == 70) {
            strFileExtendName = "JPG";
        } else if (photoByte[0] == 66 && photoByte[1] == 77) {
            strFileExtendName = "BMP";
        } else if (photoByte[1] == 80 && photoByte[2] == 78 && photoByte[3] == 71) {
            strFileExtendName = "PNG";
        }

        return strFileExtendName;
    }
    public static String uploadOnlineImage(byte[] data,String basePath,String bizPath,String uploadType){
        String dbPath = null;
        String fileName = "image" + Math.round(Math.random() * 100000000000L);
        fileName += "." + getFileExtendName(data);
        try {
            if(CommonConstant.UPLOAD_TYPE_LOCAL.equals(uploadType)){
                File file = new File(basePath + File.separator + bizPath + File.separator );
                if (!file.exists()) {
                    file.mkdirs();// 创建文件根目录
                }
                String savePath = file.getPath() + File.separator + fileName;
                File savefile = new File(savePath);
                FileCopyUtils.copy(data, savefile);
                dbPath = bizPath + File.separator + fileName;
            }else {
                InputStream in = new ByteArrayInputStream(data);
                String relativePath = bizPath+"/"+fileName;
                if(CommonConstant.UPLOAD_TYPE_MINIO.equals(uploadType)){
                    //dbPath = MinioUtil.upload(in,relativePath);
                }else if(CommonConstant.UPLOAD_TYPE_OSS.equals(uploadType)){
                    //dbPath = OssBootUtil.upload(in,relativePath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbPath;
    }

    /**
     * 判断文件名是否带盘符，重新处理
     * @param fileName
     * @return
     */
    public static String getFileName(String fileName){
        //判断是否带有盘符信息
        // Check for Unix-style path
        int unixSep = fileName.lastIndexOf('/');
        // Check for Windows-style path
        int winSep = fileName.lastIndexOf('\\');
        // Cut off at latest possible point
        int pos = (winSep > unixSep ? winSep : unixSep);
        if (pos != -1)  {
            // Any sort of path separator found...
            fileName = fileName.substring(pos + 1);
        }
        //替换上传文件名字的特殊字符
        fileName = fileName.replace("=","").replace(",","").replace("&","")
                .replace("#", "").replace("“", "").replace("”", "");
        //替换上传文件名字中的空格
        fileName=fileName.replaceAll("\\s","");
        return fileName;
    }

    // java 判断字符串里是否包含中文字符
    public static boolean ifContainChinese(String str) {
        if(str.getBytes().length == str.length()){
            return false;
        }else{
            Matcher m = ZHONGWEN_PATTERN.matcher(str);
            if (m.find()) {
                return true;
            }
            return false;
        }
    }

    /**
     * 统一全局上传
     * @Return: java.lang.String
     */
    public static String upload(MultipartFile file, String bizPath, String uploadType) {
        String url = "";
        if(CommonConstant.UPLOAD_TYPE_MINIO.equals(uploadType)){
            //url = MinioUtil.upload(file,bizPath);
        }else{
            //url = OssBootUtil.upload(file,bizPath);
        }
        return url;
    }
    /**
     * 本地文件上传
     * @param mf 文件
     * @param bizPath  自定义路径
     * @return
     */
    public static String uploadLocal(MultipartFile mf,String bizPath,String uploadpath){
        try {
            //update-begin-author:liusq date:20210809 for: 过滤上传文件类型
            FileTypeFilter.fileTypeFilter(mf);
            //update-end-author:liusq date:20210809 for: 过滤上传文件类型
            String fileName = null;
            File file = new File(uploadpath + File.separator + bizPath + File.separator );
            if (!file.exists()) {
                file.mkdirs();// 创建文件根目录
            }
            String orgName = mf.getOriginalFilename();// 获取文件名
            orgName = CommonUtils.getFileName(orgName);
            if(orgName.indexOf(".")!=-1){
                fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.lastIndexOf("."));
            }else{
                fileName = orgName+ "_" + System.currentTimeMillis();
            }
            String savePath = file.getPath() + File.separator + fileName;
            File savefile = new File(savePath);
            FileCopyUtils.copy(mf.getBytes(), savefile);
            String dbpath = null;
            if(StringUtil.isNotEmpty(bizPath)){
                dbpath = bizPath + File.separator + fileName;
            }else{
                dbpath = fileName;
            }
            if (dbpath.contains("\\")) {
                dbpath = dbpath.replace("\\", "/");
            }
            return dbpath;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    /**
     * 统一全局上传 带桶
     * @Return: java.lang.String
     */
    public static String upload(MultipartFile file, String bizPath, String uploadType, String customBucket) {
        String url = "";
        if(CommonConstant.UPLOAD_TYPE_MINIO.equals(uploadType)){
            //url = MinioUtil.upload(file,bizPath,customBucket);
        }else{
            //url = OssBootUtil.upload(file,bizPath,customBucket);
        }
        return url;
    }


}
