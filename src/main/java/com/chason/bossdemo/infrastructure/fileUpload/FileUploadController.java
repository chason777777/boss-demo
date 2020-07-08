package com.chason.bossdemo.infrastructure.fileUpload;

import com.alibaba.fastjson.JSONObject;
import com.chason.bossdemo.common.BaseController;
import com.chason.bossdemo.common.Result;
import com.chason.bossdemo.common.util.Base64Util;
import com.chason.bossdemo.common.util.DateTimeUtil;
import com.chason.bossdemo.common.util.UuidUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * 文件上传
 *
 * @author cp
 */
@CrossOrigin
@RestController
@RequestMapping("file/upload")
public class FileUploadController extends BaseController {
    private static final Log logger = LogFactory.getLog(FileUploadController.class);
    //服务器本地的图片存储地址
    private static final String LOCAL_IMAGE_PATH = "upload/material/images/";

    @Autowired
    private FileUploadService fileUploadService;

    /**
     * 上传文件
     *
     * @param response
     * @param base64String
     * @param fileType
     */
    @RequestMapping(value = "base64/image")
    public void base64image(HttpServletResponse response,
                            @RequestParam(value = "base64String", required = true) String base64String,
                            @RequestParam(value = "fileType", required = true) String fileType) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (!fileType.startsWith("."))
                fileType = "." + fileType;
            String fileName = UuidUtil.randomUUID() + fileType;
            String destFileDirPath = LOCAL_IMAGE_PATH + DateTimeUtil.getDateString();
            File destFilePath = new File(getServletContextRealPath(destFileDirPath));
            if (!destFilePath.exists()) {
                destFilePath.mkdirs();
            }
            String destFileDirPathName = destFileDirPath + "/" + fileName;
            FileOutputStream fileOutputStream = new FileOutputStream(new File(getServletContextRealPath(destFileDirPathName)));
            byte[] bytes = Base64Util.base64DecodeByte(base64String);
            FileCopyUtils.copy(bytes, fileOutputStream);
            fileOutputStream.close();
            jsonObject.put("result", true);
            jsonObject.put("filePath", destFileDirPathName);

        } catch (IOException e) {
            logger.error(e);
            jsonObject.put("result", false);
            jsonObject.put("errMsg", e.getMessage());
        }
        writeJsonAjaxResponse(response, jsonObject.toString());
    }

    /**
     * 公共文件上传方法，上传请求中附带需要将文件上传到哪个目录，同时会重新将文件名称重命名
     * <p/>
     * 上传后返回json格式数据，result=true表示上传成功，通过filePath可以拿到上传后的文件访问全路径；result=
     * false表示上传失败，通过errMsg可以拿到上传失败的原因说明信息。
     *
     * @param request
     * @param response
     */
    @RequestMapping("/material")
    public Result common(HttpServletRequest request, HttpServletResponse response) {
        return fileUploadService.common(request);
    }

    @RequestMapping("/deviceModelFiles")
    public Result deviceModelFiles(HttpServletRequest request, @RequestParam(value = "fileName", required = false) String fileName) {
        return fileUploadService.deviceModelFiles(request, fileName);
    }
}
