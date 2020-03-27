package UMC.Web;

import UMC.Data.JSON;
import UMC.Data.SetupException;
import UMC.Data.Utility;
import UMC.Security.AccessToken;
import UMC.Security.Membership;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class WebServlet extends HttpServlet {

    //    private class loader extends ClassLoader {
//        public  static  void  L(ClassLoader l){
//        }
//    }
    private void addJar(File file, ClassLoader loader) {


        JarFile jarFile = null;
        try {
            jarFile = new JarFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Enumeration<JarEntry> files = jarFile.entries();
        while (files.hasMoreElements()) {
            ;
            JarEntry entry = files.nextElement();
            String name = entry.getName();
            if (name.endsWith(".class")) {
                String fileName = name.replaceAll("/", ".");
                String ultimaName = fileName.substring(0, name.length() - 6);
                try {
                    WebRuntime.register(Class.forName(ultimaName));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (java.lang.NoClassDefFoundError error) {
                    error.printStackTrace();
                }

            }
        }
    }


    @Override
    public void init() throws ServletException {
        super.init();
        try {

            String path = Utility.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            path = path.substring(0, path.lastIndexOf(File.separator));

            File[] files = new File(path).listFiles();

            ClassLoader loader = ClassLoader.getSystemClassLoader();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isFile()) {
                    String name = file.getName();
                    if (name.endsWith(".jar") && name.startsWith("UMC.")) {
                        this.addJar(file, loader);

                    }

                }

            }

            WebRuntime.register(ClazzUtils.getClazzName(true));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.ProcessRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.ProcessRequest(req, resp);
    }

    protected void authorization(HttpServletRequest req, HttpServletResponse resp) {


        String[] Segments = req.getRequestURI().split("/");
        String CookieKey = "";

        if (Segments.length > 2) {
            CookieKey = Segments[2];
        }
        if (Utility.isEmpty(CookieKey)) {
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equalsIgnoreCase(Membership.SessionCookieName)) {
                        CookieKey = cookie.getValue();
                    }
                }
            }
            if (Utility.isEmpty(CookieKey)) {
                CookieKey = Utility.uuid(UUID.randomUUID());
                Cookie cookie = new Cookie(Membership.SessionCookieName, CookieKey);
                resp.addCookie(cookie);
            }
        }


        String UserAgent = req.getHeader("User-Agent");
        String contentType = "Client/" + req.getRemoteAddr();
        if (UserAgent.indexOf("UMC Client") > -1) {
            contentType = "App/" + req.getRemoteAddr();
        }

        Membership.Instance().Authorization(CookieKey, contentType);

        String referer = req.getHeader("referer");
        if (Utility.isEmpty(referer) == false) {
            URI uri = URI.create(referer);
            Map<String, String> query = Utility.queryString(uri.getQuery());

            UUID sp = Utility.uuid(query.get("sp"));
            if (sp != null) {
                if (sp.toString().equalsIgnoreCase(AccessToken.get("Spread-Id")) == false) {
                    AccessToken.set("Spread-Id", sp.toString());
                }
            }
        }


    }

    void ProcessRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf8");

        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Max-Age", "18000");
        resp.setHeader("Access-Control-Allow-Methods", "*");
        resp.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");


        String ip = req.getRemoteAddr();
        URI Url = URI.create(req.getRequestURL().toString());

        String xRIP = req.getHeader("X-Real-IP");
        if (Utility.isEmpty(xRIP) == false) {
            ip = xRIP;
        }
        String chost = req.getHeader("CA-Host");

        if (Utility.isEmpty(chost) == false) {
            Url = URI.create(String.format("https://%s%s", chost, Url.getPath()));
        }
        URI urireferer = null;
        String referer = req.getHeader("referer");
        if (Utility.isEmpty(referer) == false) {
            urireferer = URI.create(referer);
        }
        String UserAgent = req.getHeader("User-Agent");
        Process(req.getParameterMap(), req.getInputStream(), resp.getWriter(),
                Url, urireferer, ip, UserAgent, e -> {
                    try {
                        resp.sendRedirect(e.toString());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }, req, resp);
    }

    public static List<WebMeta> auths() {
        List<WebMeta> metas = new LinkedList<>();
        if (WebRuntime.flows.size() > 0) {
            for (Map.Entry<String, List<Class>> entry : WebRuntime.flows.entrySet()) {

                Mapping mapping = (Mapping) entry.getValue().get(0).getAnnotation(Mapping.class);

                WebAuthType authType = WebRuntime.authKeys.get(entry.getKey());
                if (authType == WebAuthType.check) {
                    metas.add(new WebMeta().put("key", entry.getKey() + ".*").put("desc", mapping.desc()));

                } else if (authType == WebAuthType.userCheck) {
                    metas.add(new WebMeta().put("key", entry.getKey() + ".*").put("desc", mapping.desc()));

                }


            }
        }

        if (WebRuntime.activities.size() > 0) {
            for (Map.Entry<String, Map<String, Class>> entry : WebRuntime.activities.entrySet()) {
                Map<String, Class> map = entry.getValue();
                for (Map.Entry<String, Class> entry1 : map.entrySet()) {
                    Class t = entry1.getValue();


                    Mapping mapping = (Mapping) t.getAnnotation(Mapping.class);
                    WebAuthType authType = mapping.auth();

                    if (authType == WebAuthType.check) {
                        metas.add(new WebMeta().put("key", mapping.model() + "." + mapping.cmd()).put("desc", mapping.desc()));

                    } else if (authType == WebAuthType.userCheck) {
                        metas.add(new WebMeta().put("key", mapping.model() + "." + mapping.cmd()).put("desc", mapping.desc()));

                    }

                }


            }
        }
        return metas;

    }

    public static List<WebMeta> mapping() {
        List<WebMeta> metas = new LinkedList<>();
        if (WebRuntime.facClas.size() > 0) {
            for (Class t : WebRuntime.facClas) {
                WebMeta meta = new WebMeta();
                meta.put("type", t.getName());

                meta.put("name", "." + t.getSimpleName());
                metas.add(meta);

                Mapping mapping = (Mapping) t.getAnnotation(Mapping.class);
                if (Utility.isEmpty(mapping.desc()) == false) {
                    meta.put("desc", mapping.desc());

                }

            }

        }
        if (WebRuntime.flows.size() > 0) {
            for (Map.Entry<String, List<Class>> entry : WebRuntime.flows.entrySet()) {

                for (Class t : entry.getValue()) {
                    WebMeta meta = new WebMeta();
                    meta.put("type", t.getName());
                    metas.add(meta);
                    meta.put("auth", WebRuntime.authKeys.get(entry.getKey()).name());
                    meta.put("model", entry.getKey());

                    meta.put("name", entry.getKey() + ".");
                    Mapping mapping = (Mapping) t.getAnnotation(Mapping.class);
                    if (Utility.isEmpty(mapping.desc()) == false) {

                        meta.put("desc", mapping.desc());
                    }

                }

            }
        }

        if (WebRuntime.activities.size() > 0) {
            for (Map.Entry<String, Map<String, Class>> entry : WebRuntime.activities.entrySet()) {
                Map<String, Class> map = entry.getValue();
                for (Map.Entry<String, Class> entry1 : map.entrySet()) {
                    Class t = entry1.getValue();

                    WebMeta meta = new WebMeta();
                    metas.add(meta);

                    Mapping mapping = (Mapping) t.getAnnotation(Mapping.class);
                    meta.put("auth", mapping.auth().name());
                    meta.put("type", t.getName());
                    meta.put("name", mapping.model() + "." + mapping.cmd());
                    meta.put("cmd", entry1.getKey());
                    meta.put("model", entry.getKey());
                    if (Utility.isEmpty(mapping.desc()) == false) {

                        meta.put("desc", mapping.desc());

                    }
                }


            }
        }
        return metas;

    }

    static String _header;

    class sysSession extends WebSession {

        @Override
        public String header() {
            return _header;
        }

        @Override
        public void storage(Map header, WebContext context) {
            _header = JSON.serialize(header);
        }

        @Override
        public boolean authorization(String model, String command) {
            return true;
        }
    }

    private void IndexResource(String root, PrintWriter writer) {
//        resp.setContentType("text/html;charset=utf-8");


        writerResource("header.html", writer);
        writer.write("    <script>WDK.POS.Config({posurl: ['/");
        writer.write(root);

        writer.write("/', WDK.cookie('device') || WDK.cookie('device', WDK.uuid())].join('')});</script>");
        writer.write("\n");
        writerResource("index.html", writer);

    }

    private void writerResource(String resKey, PrintWriter writer) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resKey);


        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > -1) {
                writer.write(new String(buffer, 0, len));
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void Process(Map<String, String[]> nvs, InputStream input, PrintWriter writer,
                 URI Url, URI UrlReferrer, String UserHostAddress, String UserAgent, WebClient.IWebRedirect
                         redirec, HttpServletRequest req, HttpServletResponse resp) {
        Map QueryString = new HashMap();

        String model = null, cmd = null;
        String start = null;// nvs.get("_start");
        String jsonp = null;//QueryString.get("jsonp");
        Set<Map.Entry<String, String[]>> set = nvs.entrySet();
        for (Map.Entry<String, String[]> entry : set) {
            String key = entry.getKey();
            switch (key) {
                case "_start":
                    start = String.join(",", entry.getValue());
                    break;
                case "_model":
                    model = String.join(",", entry.getValue());
                    break;
                case "_cmd":
                    cmd = String.join(",", entry.getValue());
                    break;
                case "jsonp":
                    jsonp = String.join(",", entry.getValue());
                    break;
                default:

                    if (!key.startsWith("_")) {
                        String value = String.join(",", entry.getValue());
                        try {

                            String urlencode = Utility.isNull(this.getServletConfig().getInitParameter("urlencode"), "ISO-8859-1");
                            if (urlencode.equalsIgnoreCase("utf-8") == false) {
                                value = new String(value.getBytes(urlencode), "utf-8");
                                key = new String(key.getBytes(urlencode), "utf-8");
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        QueryString.put(key, value);

                    }
                    break;
            }
        }
        String[] Segments = Utility.findAll(Url.getPath().split("/"), d -> Utility.isEmpty(d) == false).toArray(new String[0]);


        String rkey = "";
        if (Segments.length > 1) {

            switch (Segments[0]) {
                case "Click":
                    rkey = "click.html";
                    break;
                case "Page":
                    rkey = "page.html";
                    break;
                case "Clear":
                    UMC.Data.ProviderConfiguration.clear();
                    break;
            }
        }
        if (Segments.length < 2) {
            resp.setContentType("text/html;charset=utf-8");
            IndexResource(Segments.length == 1 ? Segments[0] : "UMC", writer);
            return;
        }
        if (Utility.isEmpty(rkey) == false) {
            resp.setContentType("text/html;charset=utf-8");

            writerResource(rkey, writer);

            return;
        }
        if (Utility.isEmpty(model)) {
            if (Segments.length > 3 && Segments[Segments.length - 1].indexOf('.') == -1) {
                if (Segments.length > 4) {
                    model = Segments[2];
                    cmd = Segments[3];
                }
                if (Segments.length > 4) {
                    QueryString.put(Segments[4], null);
                }

            }
        }
        resp.setContentType("text/json;charset=utf-8");
        if (Utility.isEmpty(model) == false) {
            switch (model) {
                case "Upload":
                    switch (cmd) {
                        case "Command":

                            StringBuffer stringBuilder = new StringBuffer();
                            try {
                                URL url = new URL(QueryString.get("src").toString());
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                InputStream inputStream = connection.getInputStream();
                                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");


                                char[] cha = new char[1024];
                                int len = 0;
                                while ((len = reader.read(cha)) != -1) {
                                    stringBuilder.append(cha, 0, len);
                                }
                                reader.close();
                                inputStream.close();


                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            Object omap = JSON.deserialize(stringBuilder.toString());
                            if (omap instanceof Map) {
                                Map map = (Map) omap;
                                model = (String) map.get("_model");
                                cmd = (String) map.get("_cmd");
                                map.remove("_model");
                                map.remove("_cmd");
                                QueryString.clear();
                                QueryString.putAll(map);
                            } else {
                                return;
                            }
                            break;
                    }
                    break;

            }
        }
        WebSession session;
        switch (Utility.isNull(model, "")) {
            case "System":
                session = new sysSession();
                break;
            default:
                try {
                    authorization(req, resp);
                    session = WebSession.Instance();
                } catch (SetupException e) {
                    session = new sysSession();
                    model = "System";
                    cmd = "Start";

                }
                break;
        }


        WebClient client = new WebClient(session, Url, UrlReferrer, UserAgent, UserHostAddress);
        client.InputStream = input;

        if (Utility.isEmpty(jsonp) == false && jsonp.startsWith("app")) {
            client.isApp = true;
        }

        if (Utility.isEmpty(start) == false) {
            client.Start(start);
        } else if (Utility.isEmpty(model)) {

            client.SendDialog(QueryString);
        } else {
            if (Utility.isEmpty(cmd)) {
                if (model.startsWith("[") == false) {
                    throw new IllegalArgumentException("command is empty");

                }
            } else {
                client.Command(model, cmd, QueryString);
            }
        }


        if (Utility.isEmpty(model) == false && model.startsWith("[") && Utility.isEmpty(cmd)) {
            try {
                client.JSONP(model, jsonp, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (Utility.isEmpty(jsonp) == false) {
                writer.write(jsonp);
                writer.write('(');
            }
            try {
                client.WriteTo(writer, redirec);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (Utility.isEmpty(jsonp) == false) {
                writer.write(")");
            }
        }


    }
}
