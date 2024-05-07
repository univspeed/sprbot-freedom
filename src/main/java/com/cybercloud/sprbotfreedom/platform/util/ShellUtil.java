package com.cybercloud.sprbotfreedom.platform.util;

import com.cybercloud.sprbotfreedom.platform.annotation.log.PrintFunctionLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author liuyutang
 * @date 2024/5/6
 */
@Slf4j
@PrintFunctionLog
public class ShellUtil {

    // 基本路径
    private static final String BASE_PATH = "F:\\";

    // 记录Shell执行状况的日志文件的位置(绝对路径)
    private static final String EXECUTE_SHELL_LOG_FILE = BASE_PATH + "executeShell.log";

    private static final String LOG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final DateFormat DATE_FORMAT;
    private static final StringBuilder STRB;

    static {
        DATE_FORMAT = new SimpleDateFormat(LOG_DATE_FORMAT);
        STRB = new StringBuilder();
    }

    enum Platform {
        WINDOWS,
        LINUX,
        MAC
    }

    enum LogOutput {
        LOG,
        File
    }

    public static boolean executeShell(Platform type,LogOutput logOutput,String shellCommand) {
        boolean success = false;
        BufferedReader bufferedReader = null;
        BufferedReader stdError = null;
        // 格式化日期时间，记录日志时使用
        try {
            appendLog(String.format("%s:\t%s","prepare to exec command",shellCommand),true);
            Process pid;
            String[] cmd = type == Platform.WINDOWS ? new String[]{ "cmd.exe","/c", shellCommand} : new String[] { "/bin/sh", "-c", shellCommand };
            // 设置字符集避免乱码
            Charset charset = type == Platform.WINDOWS ? Charset.forName("GBK") : StandardCharsets.UTF_8;
            // 执行Shell命令
            if (shellCommand.contains(".")) {
                pid = Runtime.getRuntime().exec(shellCommand);
            }
            else{
                pid = Runtime.getRuntime().exec(cmd);
            }
            appendLog(String.format("%s:\t%s","PID",pid),false);
            // bufferedReader用于读取Shell的输出内容
            bufferedReader = new BufferedReader(new InputStreamReader(pid.getInputStream(),charset));
            //读到标准出错的信息
            stdError = new BufferedReader(new InputStreamReader(pid.getErrorStream(),charset));

            appendLog("Shell命令执行完毕！",true);

            //将标准输入流上面的内容写到stringBuffer里面
            writeLog(bufferedReader);

            //将标准输入流上面的内容写到stringBuffer里面
            writeLog(stdError);

            //这个是或得脚本执行的返回值
            int status = pid.waitFor();
            //如果脚本执行的返回值不是0,则表示脚本执行失败，否则（值为0）脚本执行成功。
            if(status!=0) {
                appendLog("shell脚本执行失败！",false);
            } else{
                appendLog("shell脚本执行成功！",false);
                success = true;
            }
        } catch (Exception ioe) {
            appendLog(String.format("执行Shell命令时发生异常：%s",ioe.getMessage()),false);
        } finally {
            IOUtils.closeQuietly(bufferedReader);
            IOUtils.closeQuietly(stdError);
            try {
                outputLogFile(logOutput);
            }catch (Exception e){
                log.error("{0}",e);
                success = false;
            }
        }
        return success;
    }

    private static void writeLog(BufferedReader bufferedReader) throws IOException {
        String line;
        // 读取Shell的输出内容，并添加到stringBuffer中
        while (bufferedReader != null
                && (line = bufferedReader.readLine()) != null) {
                appendLog(line,false);
        }
    }

    /**
     * 输出命令执行日志文件
     */
    private static void outputLogFile(LogOutput logOutput) throws IOException {
        OutputStreamWriter outputStreamWriter = null;
        try {
            if(logOutput==LogOutput.LOG){
                log.info(STRB.toString());
            } else if (logOutput == LogOutput.File) {
                // 将Shell的执行情况输出到日志文件中
                OutputStream outputStream = new FileOutputStream(EXECUTE_SHELL_LOG_FILE);
                outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                outputStreamWriter.write(STRB.toString());
            }
        } finally {
            IOUtils.closeQuietly(outputStreamWriter);
            STRB.delete(0,STRB.length());
        }
    }

    /**
     * 添加日志内容
     * @param content 日志内容
     * @param date 是否要添加时间
     */
    private static void appendLog(String content,boolean date){
        if(date){
            STRB.append(DATE_FORMAT.format(new Date())).append(" ");
        }
        STRB.append(content).append("\r\n");
    }

    public static void main(String[] args) throws IOException {
        //ShellUtil.executeShell(Platform.WINDOWS,LogOutput.LOG,"F:\\env\\workspace\\cybercloud_workspace\\git\\sprbot-freedom\\src\\main\\resources\\test.bat");
        System.out.println(ShellUtil.executeShell(Platform.WINDOWS,LogOutput.LOG,"dir"));
    }
}
