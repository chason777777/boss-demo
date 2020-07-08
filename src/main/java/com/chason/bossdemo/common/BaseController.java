package com.chason.bossdemo.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseController {

    protected static final Log logger = LogFactory.getLog(BaseController.class);

    // 每页显示的记录条数
    protected static final int PAGE_SIZE = 10;

    protected static final String SYSTEM_ERROR_MESSAGE = "系统繁忙，请稍候再试！";
    protected static final String RESULT_SUCCESS = "0";
    protected static final String RESULT_FAIL = "1";
    protected static final String ERROR = "error";
    protected static final String RESULT = "result";
    protected static final String RESULT_LIST = "resultList";
    protected static final String TOTAL_RECORD = "totalRecord";
    protected static final String MESSAGE = "message";

    public static final String CODE = "code";
    protected static final int CODE_SUCC = 0;
    protected static final int CODE_ERROR = 1;

    @Resource
    protected HttpServletRequest request;
    @Resource
    protected HttpSession session;

    /**
     * 从请求中获取POST过来的内容
     *
     * @return
     */
    protected String getRequestPostContent() {
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(request.getInputStream());
            byte[] contents = new byte[request.getContentLength()];
            bis.read(contents);
            return new String(contents, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null)
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    /**
     * 向请求响应写消息
     *
     * @param response
     * @param message
     */
    protected void writeMessageToResponse(HttpServletResponse response, String message) {
        PrintWriter writer = null;
        try {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            writer = response.getWriter();
            writer.write(message);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    /**
     * 向请求响应写异步的json格式消息
     *
     * @param response
     * @param jsonString
     */
    protected void writeJsonAjaxResponse(HttpServletResponse response, String jsonString) {
        PrintWriter writer = null;
        try {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType("application/json; charset=UTF-8");
            writer = response.getWriter();
            writer.write(jsonString);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    /**
     * 获取某个路径在应用上下文中的绝对路径
     *
     * @param path
     * @return
     */
    protected String getServletContextRealPath(String path) {
        return session.getServletContext().getRealPath(path);
    }

    /**
     * 获取请求中的属性
     *
     * @param key
     * @return
     */
    protected Object getRequestAttribute(String key) {
        if (null == request)
            return null;
        return request.getAttribute(key);
    }

    /**
     * 设置请求中的属性
     *
     * @param key
     * @param val
     */
    protected void setRequestAttribute(String key, Object val) {
        request.setAttribute(key, val);
    }

    /**
     * 获取会话中存放的属性值
     *
     * @param key
     * @return
     */
    protected Object getSessionAttribute(String key) {
        if (null == session)
            return null;
        return session.getAttribute(key);
    }

    /**
     * 向会话中存放属性值
     *
     * @param key
     * @param val
     */
    protected void setSessionAttribute(String key, Object val) {
        session.setAttribute(key, val);
    }

    /**
     * 删除会话中的属性
     *
     * @param key
     */
    protected void removeSessionAttribute(String key) {
        session.removeAttribute(key);
    }

    protected HttpSession getSession() {
        return session;
    }

    protected HttpServletRequest getRequest() {
        return request;
    }


    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
    }

}
