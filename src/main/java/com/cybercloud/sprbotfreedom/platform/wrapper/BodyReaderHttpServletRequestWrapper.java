package com.cybercloud.sprbotfreedom.platform.wrapper;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.*;

/**
 * 请求Body复制装饰器
 * @author liuyutang
 */
public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] bodyArray;

    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        ServletInputStream in = request.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        bodyArray = out.toByteArray();
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream bais = new ByteArrayInputStream(bodyArray);
        return new ServletInputStream() {
            @Override
            public int read() {
                return bais.read();
            }

            @Override
            public boolean isFinished() {

                return false;
            }

            @Override
            public boolean isReady() {

                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

        };
    }

    @Override
    public BufferedReader getReader(){
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
}
