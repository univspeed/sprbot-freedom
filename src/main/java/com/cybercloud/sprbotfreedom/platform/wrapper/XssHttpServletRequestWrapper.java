package com.cybercloud.sprbotfreedom.platform.wrapper;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liuyutang
 * @date 2023/7/6
 */
@Slf4j
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    /**
     * 判断是否是上传 上传忽略
     */
    boolean isUpData = false;

    /**
     * 不期待被过滤的的链接和字段（管理后台使用了富文本，希望有可编辑的内容）
     */
    Map<String, String> doNotFilterURLAndParamMap = new HashMap<String, String>() {
        {
            /*put("/api/v2/group/manage", "description");
            put("/api/v1/sendNews", "content");*/
        }
    };



    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        String contentType = request.getContentType ();
        if (null != contentType) {
            isUpData = contentType.startsWith("multipart");
        }
    }

    /**
     * 过滤单个参数
     * @param name
     * @return
     */
    @Override
    public String getParameter(String name) {
        String parameter = super.getParameter(name);
        if(StringUtils.isNotBlank(parameter)){
            //这里使用的阿帕奇的common-lang3中的转义html方法,也可以自己实现,
            String escapeParameter = this.cleanXSS(parameter);
            return escapeParameter;
        }
        return parameter;
    }

    /**
     * 过滤实体的每个参数
     * @param name
     * @return
     */
    @Override
    public String[] getParameterValues(String name) {

        String[] parameterValues = super.getParameterValues(name);
        if (parameterValues == null) {
            return null;
        }
        for (int i = 0; i < parameterValues.length; ++i) {
            String value = parameterValues[i];
            parameterValues[i] = this.cleanXSS(value);
        }
        return parameterValues;

    }

    /**
     * 处理@RequestBody的形式传入的json数据
     * @return
     * @throws IOException
     */
    @Override
    public ServletInputStream getInputStream () throws IOException {
        if (isUpData){
            return super.getInputStream ();
        }else{

            final ByteArrayInputStream bais = new ByteArrayInputStream(inputHandlers(super.getInputStream ()).getBytes ());

            return new ServletInputStream() {

                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {

                }

                @Override
                public int read() throws IOException {
                    return bais.read();
                }
            };
        }

    }


    public String inputHandlers(ServletInputStream servletInputStream){
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(servletInputStream, Charset.forName("UTF-8")));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (servletInputStream != null) {
                try {
                    servletInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String requestUrl = StringUtils.replaceOnce(this.getRequestURI(), this.getContextPath(), StringUtils.EMPTY);
        boolean needFilter = false;
        String key = "";
        String param = "";
        for(Map.Entry<String, String> entry : doNotFilterURLAndParamMap.entrySet()){

            key = entry.getKey();

            int index = StringUtils.indexOf(key, "*");
            if (index > 0) {
                String[] array = key.split("\\*");
                StringBuffer stringBuffer = new StringBuffer();
                for (String s : array) {
                    stringBuffer.append(s).append("(.*)");
                }
                Pattern p = Pattern.compile(stringBuffer.toString());
                Matcher m = p.matcher(requestUrl);
                if (m.find()) {
                    needFilter = true;
                    param = entry.getValue();
                    break;
                }
            } else {
                if (requestUrl.equals(key)) {
                    needFilter = true;
                    param = entry.getValue();
                    break;
                }
            }
        }


        if(needFilter) {   //有需要特殊处理的字段，不希望过滤标签
            try {
                /*String param = doNotFilterURLAndParamMap.get(requestUrl);*/
                JSONObject jsonObject = JSONObject.parseObject(sb.toString());
                if(jsonObject.containsKey(param)) {
                    Object notFilterValue = jsonObject.get(param);
                    String cleanXSSParams = cleanXSS(sb.toString ());
                    JSONObject filteredJson = JSONObject.parseObject(cleanXSSParams);
                    filteredJson.put(param, notFilterValue);
                    return filteredJson.toJSONString();
                }else {
                    return cleanXSS(sb.toString ());
                }

            }catch (Exception e) {
                log.error("XssHttpServletRequestWrapper转换json数据失败",e);
                return cleanXSS(sb.toString ());  //异常时，就直接过滤，不管需要特殊处理的参数
            }


        }else {
            return cleanXSS(sb.toString ());
        }
    }

    /**
     * 过滤规则，这里不直接使用StringEscapeUtils.escapeHtml，因为获取的是一个json字符串，会将" 替换导致数据异常，没有""进行分割，无法正常注入到@RequestBody
     * @param value
     * @return
     */
    private String cleanXSS(String value) {
        if (value != null) {
            //推荐使用ESAPI库来避免脚本攻击,value = ESAPI.encoder().canonicalize(value);
            // 避免空字符串
            //value = value.replaceAll(" ", "");
            value = value.trim();
            // 避免script 标签
            Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // 避免src形式的表达式
            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*(.*?)\\s",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // 删除单个的 </script> 标签
            scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // 删除单个的<script ...> 标签
            scriptPattern = Pattern.compile("<script(.*?)>",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // 避免 eval(...) 形式表达式
            scriptPattern = Pattern.compile("eval\\((.*?)\\)",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // 避免 e­xpression(...) 表达式
            scriptPattern = Pattern.compile("e­xpression\\((.*?)\\)",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // 避免 javascript: 表达式
            scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // 避免 vbscript:表达式
            scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // 避免 onload= 表达式
            scriptPattern = Pattern.compile("onload(.*?)=",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // 避免 onerror= 表达式
            scriptPattern = Pattern.compile("onerror(.*?)=",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            //特殊字符
//            value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
//            value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
//            value = value.replaceAll("'", "&#39;");//.replaceAll("\"", "&quot;"); 双引号留着避免json失效
        }
        return value;
    }


}
