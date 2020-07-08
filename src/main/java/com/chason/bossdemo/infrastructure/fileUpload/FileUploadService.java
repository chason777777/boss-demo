package com.chason.bossdemo.infrastructure.fileUpload;

import com.alibaba.fastjson.JSONObject;
import com.chason.bossdemo.common.Result;
import com.chason.bossdemo.common.util.IpUtil;
import com.chason.bossdemo.common.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service("fileUploadService")
public class FileUploadService {
    private static Logger logger = LoggerFactory.getLogger(FileUploadService.class);

    @Value("${filepath}")
    private String filepath;
    @Value("${fileUri}")
    private String fileUri;
    @Value("${deviceModelFilepath}")
    private String deviceModelFilepath;
    @Value("${deviceModelFileUri}")
    private String deviceModelFileUri;

    public Result common(HttpServletRequest request) {
        // MultipartHttpServletRequest spring mvc封装上传文件类
        MultipartHttpServletRequest mutipartHttpServletRequest = (MultipartHttpServletRequest) request;
        // 获得文件：
        MultipartFile multipartFile = mutipartHttpServletRequest.getFile("file");
        JSONObject jsonObject = new JSONObject();
        if (null == multipartFile) {
            return Result.error("上传文件不能为空");
        }
        // 获得文件名：
        String fileName = multipartFile.getOriginalFilename();
        String storageName = UuidUtil.randomUUID();
        try {
            String fileMd5 = commonUpload4Md5(multipartFile, filepath + storageName, fileName);
            jsonObject.put("fileName", fileName);
            jsonObject.put("fileUrl", fileUri + storageName + "?filename=" + fileName);
            jsonObject.put("fileLenth", multipartFile.getSize());
            jsonObject.put("fileUploadIp", IpUtil.getIpAddr(request));
            jsonObject.put("fileMd5", fileMd5);
            return Result.success(jsonObject);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return Result.success(e.getMessage());
        }
    }

    public Result deviceModelFiles(HttpServletRequest request, String fileName) {
        MultipartHttpServletRequest mutipartHttpServletRequest = (MultipartHttpServletRequest) request;
        // 获得文件：
        MultipartFile multipartFile = mutipartHttpServletRequest.getFile("file");
        JSONObject jsonObject = new JSONObject();
        if (null == multipartFile) {
            return Result.error("上传文件不能为空");
        }
        // 获得文件名：
        if (StringUtils.isEmpty(fileName))
            fileName = multipartFile.getOriginalFilename();
        String storageName = System.currentTimeMillis() + "";
        try {
            commonUpload(multipartFile, deviceModelFilepath + storageName, fileName);
            jsonObject.put("fileName", fileName);
            jsonObject.put("fileUrl", deviceModelFileUri + storageName + "/" + fileName);
            return Result.success(jsonObject);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return Result.success(e.getMessage());
        }
    }


    public String saveAndGetUrl(MultipartFile multipartFile) {
        if (null == multipartFile) {
            return null;
        }
        // 获得文件名：
        String fileName = multipartFile.getOriginalFilename();
        String extFileName = UuidUtil.randomUUID().concat(fileName.substring(fileName.indexOf(".")));
        try {
            String fileMd5 = commonUpload4Md5(multipartFile, filepath, extFileName);
            return fileUri + extFileName;
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 公共文件上传文件方法，使用上传的方法中，可以使用这部分公共用的上传代码
     *
     * @param multipartFile ：请求的上传的文件
     * @param uploadPath    ：上传保存的路径
     * @param extFileName   ：最后保存的文件名称，包含文件后缀名
     * @throws IOException
     */
    private void commonUpload(MultipartFile multipartFile, String uploadPath, String extFileName) throws IOException {
        logger.info("存储文件路径：" + uploadPath);
        File fileDrect = new File(uploadPath);
        if (!fileDrect.exists())
            fileDrect.mkdirs();
        File upoloadFile = new File(fileDrect, extFileName);
        FileOutputStream fileOutputStream = new FileOutputStream(upoloadFile);
        FileCopyUtils.copy(multipartFile.getBytes(), fileOutputStream);
        fileOutputStream.close();
    }

    /**
     * 返回MD5
     *
     * @param multipartFile ：请求的上传的文件
     * @param uploadPath    ：上传保存的路径
     * @param extFileName   ：最后保存的文件名称，包含文件后缀名
     * @throws IOException
     */
    private String commonUpload4Md5(MultipartFile multipartFile, String uploadPath, String extFileName) throws IOException {
        logger.info("存储文件路径：" + uploadPath);
        File fileDrect = new File(uploadPath);
        if (!fileDrect.exists())
            fileDrect.mkdirs();
        File upoloadFile = new File(fileDrect, extFileName);
        FileOutputStream fileOutputStream = new FileOutputStream(upoloadFile);
        FileCopyUtils.copy(multipartFile.getBytes(), fileOutputStream);
        fileOutputStream.close();

        return getFileMd5(fileDrect, extFileName);
    }

    private String getFileMd5(File filepath, String filename) {
        try {
            File file = new File(filepath, filename);
            FileInputStream fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
            BigInteger bigInt = new BigInteger(1, md.digest());
            String md5Str = bigInt.toString(16);
            int md5Lenth = md5Str.length();
            if (md5Lenth < 32) {
                for (int i = 0; i < (32 - md5Lenth); i++) {
                    md5Str = "0" + md5Str;
                }
            }
            return md5Str;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
