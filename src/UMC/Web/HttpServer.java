package UMC.Web;

import UMC.Data.Utility;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class HttpServer {
    public static void addUrl(URLClassLoader urlClassLoader, URL url) {

        URL[] urls = urlClassLoader.getURLs();
        for (URL url1 : urls) {
            if (url1.toString().equals(url.toString())) {
                return;
            }
        }
        Method add = null;
        try {
            add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});

            add.setAccessible(true);
            add.invoke(urlClassLoader, url);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {


        URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        File _file = new File(HttpServer.class.getProtectionDomain().getCodeSource().getLocation().getFile());

        File parent = _file.getParentFile();

        File lib = parent.toURI().toString().endsWith("/lib/") ? parent : new File(parent, "lib");

        if (lib.exists()) {
            for (File f : lib.listFiles((f, n) -> n.toLowerCase().endsWith(".jar"))) {
                addUrl(urlClassLoader, f.toURI().toURL());
            }
        }



        URL[] urls = urlClassLoader.getURLs();
        for (URL url1 : urls) {
            if (url1.toString().endsWith("/")) {

                System.out.print("Scanning:");
                System.out.println(url1.toString());

                WebRuntime.register(ClazzUtils.getClazzName(url1));
            }
        }
        WebServlet.scanningClass(false);
        int port = args.length > 0 ? Utility.parse(args[0], 5188) : 5188;
        com.sun.net.httpserver.HttpServer httpServer = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port), 0);

        httpServer.createContext("/", new UMCHandler());
        ExecutorService executorService = Executors.newCachedThreadPool();

        httpServer.setExecutor(executorService);
        httpServer.start();

        System.out.println("UMC Http Server Port:" + port);
        Scanner scanner = new Scanner(System.in);
        String nextLine = scanner.nextLine();

        while (nextLine != null && !nextLine.equals("")) {

            if (nextLine.equalsIgnoreCase("exit")) {
                executorService.shutdown();
                httpServer.stop(0);
                break;
            } else {
                System.out.println("只接收exit退出指令");
            }
            nextLine = scanner.nextLine();
        }

    }

    private static class UMCHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            HttpContext context = new HttpContext(httpExchange);
            try

            {
                new WebServlet().processRequest(context);
                context.printWriter.flush();
            } catch (WebRuntime.AbortException e)
            {
            } catch (Throwable e) {
                OutputStream outputStream = httpExchange.getResponseBody();
                try {
                    httpExchange.sendResponseHeaders(500, 0);
                    PrintStream printStream = new PrintStream(outputStream);
                    e.printStackTrace(printStream);
                    printStream.flush();
                    outputStream.close();
                } catch (IOException e1) {
                }
                return;

            }

            httpExchange.sendResponseHeaders(context.stattusCode, context.bufferedOutputStream.size());
            OutputStream outputStream = httpExchange.getResponseBody();
            context.bufferedOutputStream.writeTo(outputStream);
            context.bufferedOutputStream.close();
            outputStream.close();


        }
    }

    private static class HttpContext implements UMC.Net.NetContext {
        private URL _url;


        public HttpContext(HttpExchange exchange) {
            httpExchange = exchange;
            respheaders = exchange.getResponseHeaders();
            try {
                this._url = new URL(String.format("http://%s%s", getHeader("Host"), httpExchange.getRequestURI().toString()));// httpExchange.getRequestURI().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


        }

        public String getUserHostAddress() {
            return httpExchange.getRemoteAddress().getHostString();
        }

        public URL getUrl() {

            return _url;
        }

        protected HttpExchange httpExchange;
        private int stattusCode = 200;

        public int getStatusCode() {
            return stattusCode;

        }


        public void setStatusCode(int code) {
            stattusCode = code;

        }

        private Headers respheaders;

        public String getContentType() {
            return Utility.isNull(httpExchange.getRequestHeaders().getFirst("Content-Type"), "*/*");

        }

        public void setContentType(String contentType) {
            List<String> ls = new LinkedList<>();
            ls.add(contentType);
            respheaders.put("Content-Type", ls);

        }


        private String _userAgent;

        public String getUserAgent() {
            if (Utility.isEmpty(_userAgent)) {

                _userAgent = Utility.isNull(this.getHeader("User-Agent"), "UMC");

            }
            return _userAgent;
        }


        public void addHeader(String name, String value) {

            List<String> values = httpExchange.getResponseHeaders().get(name);
            if (values == null) {
                values = new LinkedList<>();
            }
            values.add(value);
            respheaders.put(name, values);


        }

        public String getHeader(String name) {
            return httpExchange.getRequestHeaders().getFirst(name);
        }

        public void addCookie(String name, String value) {
            String values = null;
            try {
                values = String.format("%s=%s", URLEncoder.encode(name, "utf-8"), URLEncoder.encode(value, "utf-8"));


                this.addHeader("Set-Cookie", values);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        public String getCookie(String name) {
            List<String> cookies = httpExchange.getRequestHeaders().get("Cookie");
            for (String c : cookies
                    ) {
                if (c.startsWith(name + "=")) {
                    try {
                        return URLDecoder.decode(c.substring(name.length() + 1), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    ;
                }
            }
            return null;
        }


        public InputStream getInputStream() throws IOException {

            return httpExchange.getRequestBody();

        }

        public Enumeration<String> getHeaderNames() {

            Vector<String> dayNames = new Vector<String>();
            dayNames.addAll(httpExchange.getRequestHeaders().keySet());


            return dayNames.elements();

        }

        PrintWriter printWriter;

        public PrintWriter getOutput() throws IOException {
            if (printWriter == null) {
                printWriter = new PrintWriter(new OutputStreamWriter(this.getOutputStream()));
            }
            return printWriter;
        }

        ByteArrayOutputStream bufferedOutputStream = new java.io.ByteArrayOutputStream();

        public OutputStream getOutputStream() throws IOException {

            return bufferedOutputStream;//httpExchange.getResponseBody();
        }

        private URL _Referrer;

        public URL getUrlReferrer() {
            if (_Referrer == null) {

                String referer = this.getHeader("Referer");
                if (Utility.isEmpty(referer) == false) {
                    try {
                        _Referrer = new URL(referer);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        _Referrer = this.getUrl();
                    }
                } else {

                    _Referrer = this.getUrl();
                }
            }
            return _Referrer;
        }


        public String getHttpMethod() {
            return httpExchange.getRequestMethod();

        }

        public void redirect(String url) {

            this.addHeader("Location", url);
            stattusCode = 302;
            throw new WebRuntime.AbortException();

        }

        Map<String, String[]> formData2Dic(String formData) {
            Map<String, String[]> result = new HashMap<>();
            if (formData == null || formData.trim().length() == 0) {
                return result;
            }
            final String[] items = formData.split("&");
            Arrays.stream(items).forEach(item -> {
                final String[] keyAndVal = item.split("=");
                try {
                    if (keyAndVal.length > 0) {
                        String key = URLDecoder.decode(keyAndVal[0], "utf8");
                        if (keyAndVal.length == 2) {
                            String val = URLDecoder.decode(keyAndVal[1], "utf8");
                            result.put(key, new String[]{val});
                        } else {

                            result.put(key, new String[0]);
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                }
            });
            return result;
        }

        String readFileContent(Reader sreader) {
            BufferedReader reader = null;
            StringBuffer sbf = new StringBuffer();
            try {
                reader = new BufferedReader(sreader);
                String tempStr;
                while ((tempStr = reader.readLine()) != null) {
                    sbf.append(tempStr);
                }
                reader.close();
                return sbf.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            return sbf.toString();
        }

        private Map<String, String[]> map;

        public Map<String, String[]> getParameterMap() {
            if (map == null) {
                map = formData2Dic(httpExchange.getRequestURI().getQuery());
                if (getContentType().contains("www-form-urlencoded")) {
                    String postString = readFileContent(new InputStreamReader(httpExchange.getRequestBody()));

                    map.putAll(formData2Dic(postString));
                }


            }

            return map;
        }
    }
}
