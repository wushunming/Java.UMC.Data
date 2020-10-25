package UMC.Net;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;

public interface NetContext {
    int getStatusCode();

    void setStatusCode(int code);

    String getContentType();

    void setContentType(String contentType);

    String getUserHostAddress();

    String getUserAgent();

    void addHeader(String name, String value);

    String getHeader(String name);

    void addCookie(String name, String value);

    String getCookie(String name);

    InputStream getInputStream() throws IOException;

    Enumeration<String> getHeaderNames();

    PrintWriter getOutput() throws IOException;

    OutputStream getOutputStream() throws IOException;

    URL getUrlReferrer();

    URL getUrl();

    String getHttpMethod();

    void redirect(String url);

    Map<String, String[]> getParameterMap();

}
