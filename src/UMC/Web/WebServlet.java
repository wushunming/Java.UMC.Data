package UMC.Web;

import UMC.Data.*;
import UMC.Data.Sql.Initializer;
import UMC.Net.NetContext;
import UMC.Security.Membership;


import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class WebServlet implements UMC.Net.INetHandler {


    private static void addJar(File file) {


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
                if (fileName.startsWith("UMC.")) {
                    String ultimaName = fileName.substring(0, name.length() - 6);
                    try {
                        WebRuntime.register(Class.forName(ultimaName));
                    } catch (Throwable e) {
                    }
                }

            }
        }
    }


    public static boolean isScanning() {
        return WebRuntime.facClas.size() > 0 || WebRuntime.activities.size() > 0 || WebRuntime.flows.size() > 0;
    }

    public static void scanningClass(boolean isCache, String... pkgNames) {


        URL url = Utility.class.getProtectionDomain().getCodeSource().getLocation();
        File _file = new File(url.getFile());
        Long lastTime = _file.lastModified();
        String mapFile = Utility.mapPath("App_Data/register.java");
        File parent = _file.getParentFile();
        System.out.print("Scanning:");
        System.out.println(_file.getAbsolutePath());


        if (isCache) {
            String m = Utility.reader(mapFile);
            if (!Utility.isEmpty(m)) {
                System.out.print("lastTime:");
                System.out.println(lastTime);
                Map map = (Map) JSON.deserialize(m);
                if (map.containsKey("time")) {

                    System.out.print("time:");
                    System.out.println(map.get("time"));
                    if (Long.parseLong(map.get("time").toString()) >= lastTime) {
                        Object mapings = map.get("data");
                        if (mapings != null && mapings.getClass().isArray()) {
                            int l = java.lang.reflect.Array.getLength(mapings);
                            for (int i = 0; i < l; i++) {
                                String className = (String) Array.get(mapings, i);
                                if (Utility.isEmpty(className) == false) {
                                    try {
                                        WebRuntime.register(Class.forName(className));
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
        if ((WebRuntime.facClas.size() == 0 && WebRuntime.activities.size() == 0 && WebRuntime.flows.size() == 0) ||
                isCache == false) {

            if (_file.getPath().endsWith(".jar")) {
                addJar(_file);
            } else {
                if (pkgNames.length == 0) {
                    WebRuntime.register(ClazzUtils.getClazzName(url));
                } else {
                    for (String pkg : pkgNames) {

                        WebRuntime.register(ClazzUtils.getClazzName(pkg, true));
                    }
                }
            }


            File lib = parent.toURI().toString().endsWith("/lib/") ? parent : new File(parent, "lib");
            if (lib.exists()) {

                File[] files = new File(_file.getParentFile(), "lib").listFiles();
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        File file = files[i];
                        if (file.isFile()) {
                            String name = file.getName();
                            if (name.endsWith(".jar") && name.startsWith("UMC.")) {
                                addJar(file);

                            }

                        }

                    }
                }
            }
            if (isCache) {

                WebMeta map = new WebMeta().put("time", lastTime).put("data", registerCls());
                Utility.writer(mapFile, JSON.serialize(map), false);
            }
        }
        WebRuntime.facClas.sort((x, y) -> Integer.compare(y.Weight, x.Weight));
        for (Map.Entry<String, List<WebRuntime.WeightClass>> entry : WebRuntime.flows.entrySet()) {
            entry.getValue().sort((x, y) -> Integer.compare(y.Weight, x.Weight));
        }

    }

    protected void authorization(NetContext context) {


        String[] Segments = context.getUrl().getPath().split("/");
        String CookieKey = "";

        if (Segments.length > 2) {
            CookieKey = Segments[2];
        }
        if (Utility.isEmpty(CookieKey)) {
            CookieKey = context.getCookie(Membership.SessionCookieName);
            if (Utility.isEmpty(CookieKey)) {
                CookieKey = Utility.uuid(UUID.randomUUID());
                context.addCookie(Membership.SessionCookieName, CookieKey);
            }
        }


        String UserAgent = context.getUserAgent();
        String contentType = "Client/" + context.getUserHostAddress();
        if (UserAgent.contains("UMC Client")) {
            contentType = "App/" + context.getUserHostAddress();
        }

        Enumeration<String> headers = context.getHeaderNames();
        String sign = "";
        Map<String, String> map = new HashMap<>();
        while (headers.hasMoreElements()) {
            String key = headers.nextElement().toLowerCase();
            switch (key) {
                case "umc-request-sign":
                    sign = context.getHeader(key);
                    break;
                default:
                    if (key.startsWith("umc-")) {
                        try {
                            map.put(key, URLDecoder.decode(context.getHeader(key), "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }

        }
        if (!Utility.isEmpty(sign)) {
            if (Utility.sign(map, WebResource.Instance().AppSecret(false)).equalsIgnoreCase(sign)) {
                String roles = map.get("umc-request-user-role");
                String id = map.get("umc-request-user-id");
                String name = map.get("umc-request-user-name");
                String alias = map.get("umc-request-user-alias");
                UUID sid = Utility.uuid(CookieKey, true);//.Value;
                if (Utility.isEmpty(roles) == false) {
                    UMC.Security.Identity user = UMC.Security.Identity.create(Utility.isNull(Utility.uuid(id), sid), name, alias, roles.split(","));
                    UMC.Security.Principal.create(user, UMC.Security.AccessToken.create(user, sid, contentType, 0));
                } else {
                    UMC.Security.Identity user = UMC.Security.Identity.create(Utility.isNull(Utility.uuid(id), sid), name, alias);
                    UMC.Security.Principal.create(user, UMC.Security.AccessToken.create(user, sid, contentType, 0));
                }
                return;
            }
        }
        Membership.Instance().Authorization(CookieKey, contentType);


    }

    private boolean checkUMC(NetContext context) {
        if (Utility.IsApp(context.getUserAgent())) {
            return true;
        }
        String c = context.getUrl().getQuery();
        if (Utility.isEmpty(c) == false) {
            if (c.contains("?jsonp=") || c.contains("&jsonp=") || c.contains("&_model=") || c.contains("?_model=")) {
                return false;
            }
        }
        return false;


    }

    public void processRequest(NetContext context) throws IOException {


        List<String> Segments = Utility.findAll(context.getUrl().getPath().split("/"), d -> Utility.isEmpty(d) == false);//.toArray(new String[0]);


        String staticFile = Utility.mapPath("App_Data/Static" + context.getUrl().getPath());
        if (Segments.size() == 0) {
            Segments.add("index");
            staticFile += "index.html";
        }
        switch (context.getHttpMethod()) {
            case "GET":
                File headerFile = new File(staticFile);
                if (headerFile.exists()) {
                    transmitFile(context, headerFile);
                    return;
                }
                break;
        }

        switch (Segments.get(0)) {
            case "Page":
            case "Click":
                context.setContentType("text/html;charset=utf-8");

                indexResource("Page.js", context.getOutput(), "page.html");
                return;
            case "TEMP":
                temp(context);
                return;

            case "js":
                if (Segments.size() > 1) {
                    String name = Segments.get(1);
                    String na = name.substring(0, name.lastIndexOf('.'));
                    if ("UMC.Conf".equals(na)) {

                        context.setContentType("text/javascript");
                        Initializer[] initializers = Initializer.Initializers();
                        for (Initializer initializer : initializers) {
                            if (Utility.isEmpty(initializer.resourceJS()) == false) {
                                PrintWriter writer=   context.getOutput();
                                writer.write("\n/*****");
                                writer.write(initializer.name());
                                writer.write(" Conf*****/\n");
                                writerResource(initializer.getClass(), initializer.resourceJS(), writer);
                            }
                        }
                    } else {

                        String key = na.equals("Page") ? "UMC" : na;

                        Initializer initializer = Utility.find(Initializer.Initializers(), e -> e.name().equalsIgnoreCase(key));
                        if (initializer != null) {
                            initializer.Resource(context);
                        }
                    }
                    return;
                }
                break;

            case "UMC":
                if (Segments.size() == 1) {
                    indexResource("UMC", context.getOutput(), "index.html");
                    return;
                }
                break;

        }


        if (checkUMC(context) == false) {
            String lastPath = Segments.get(Segments.size() - 1);
            if (lastPath.indexOf('.') > 0) {
                String[] names = lastPath.split("\\.");
                ProviderConfiguration configs = ProviderConfiguration.configuration(names[0]);
                if (configs != null) {
                    Provider provider = configs.get(names[1]);
                    if (provider != null) {
                        UMC.Net.INetHandler handler = (UMC.Net.INetHandler) Utility.createInstance(provider);

                        if (handler != null) {
                            handler.processRequest(context);
                            return;

                        }
                    }
                }

            }
            switch (context.getHttpMethod()) {
                case "GET":

                    String file = Utility.mapPath("App_Data/Static/" + String.join("/", Segments.toArray(new String[0])) + ".html");
                    if (new File(file).exists()) {
                        transmitFile(context, file);
                        return;
                    }

                    if (Segments.size() < 4) {
                        if (Segments.size() > 1 && Segments.get(1).length() > 18) {
                        } else {
                            Initializer[] initializers = Initializer.Initializers();
                            Arrays.sort(initializers, (x, y) -> Integer.compare(x.pageIndex(), y.pageIndex()));
                            for (Initializer initializer : initializers) {
                                if (initializer.Resource(context)) {
                                    return;
                                }
                            }
                            if (Segments.size() < 2) {
                                context.redirect("/UMC");
                                return;
                            }
                        }
                    }

                    break;
            }
        }
        process(context);


    }

    private void process(NetContext context) throws IOException {
        context.addHeader("Access-Control-Allow-Origin", "*");
        context.addHeader("Access-Control-Allow-Credentials", "true");
        context.addHeader("Access-Control-Max-Age", "18000");
        context.addHeader("Access-Control-Allow-Methods", "*");
        context.addHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");


        Process(context.getParameterMap(), context.getInputStream(), context.getOutput(),
                context.getUrl(), context.getUrlReferrer(), context.getUserHostAddress(), context.getUserAgent(), e -> {

                    context.redirect(e.toString());

                }, context);

    }

    /**
     * 获取注册的类实例需要授权才能访问的类型
     *
     * @return
     */
    public static List<WebMeta> auths() {
        List<WebMeta> metas = new LinkedList<>();
        if (WebRuntime.flows.size() > 0) {
            for (Map.Entry<String, List<WebRuntime.WeightClass>> entry : WebRuntime.flows.entrySet()) {

                Mapping mapping = (Mapping) entry.getValue().get(0).Type.getAnnotation(Mapping.class);

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

    public static List<String> registerCls() {
        List<String> metas = new LinkedList<>();

        if (WebRuntime.facClas.size() > 0) {
            for (WebRuntime.WeightClass t : WebRuntime.facClas) {
                metas.add(t.Type.getName());
            }
        }
        if (WebRuntime.flows.size() > 0) {
            for (Map.Entry<String, List<WebRuntime.WeightClass>> entry : WebRuntime.flows.entrySet()) {

                for (WebRuntime.WeightClass t : entry.getValue()) {
                    metas.add(t.Type.getName());
                }

            }
        }
        if (WebRuntime.activities.size() > 0) {
            for (Map.Entry<String, Map<String, Class>> entry : WebRuntime.activities.entrySet()) {
                Map<String, Class> map = entry.getValue();
                for (Map.Entry<String, Class> entry1 : map.entrySet()) {
                    Class t = entry1.getValue();
                    metas.add(t.getName());
                }


            }
        }

        for (Class t : UMC.Data.Sql.Initializer.__initializers) {
            metas.add(t.getName());
        }
        return metas;

    }

    public static List<WebMeta> mapping() {
        List<WebMeta> metas = new LinkedList<>();
        if (WebRuntime.facClas.size() > 0) {
            for (WebRuntime.WeightClass t : WebRuntime.facClas) {
                WebMeta meta = new WebMeta();
                meta.put("type", t.Type.getName());

                meta.put("name", "." + t.Type.getSimpleName());
                metas.add(meta);

                Mapping mapping = (Mapping) t.Type.getAnnotation(Mapping.class);
                if (Utility.isEmpty(mapping.desc()) == false) {
                    meta.put("desc", mapping.desc());

                }

            }

        }
        if (WebRuntime.flows.size() > 0) {
            for (Map.Entry<String, List<WebRuntime.WeightClass>> entry : WebRuntime.flows.entrySet()) {

                for (WebRuntime.WeightClass t : entry.getValue()) {
                    WebMeta meta = new WebMeta();
                    meta.put("type", t.Type.getName());
                    metas.add(meta);
                    meta.put("auth", WebRuntime.authKeys.get(entry.getKey()).name());
                    meta.put("model", entry.getKey());

                    meta.put("name", entry.getKey() + ".");
                    Mapping mapping = (Mapping) t.Type.getAnnotation(Mapping.class);
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

    private void indexResource(String root, PrintWriter writer, String html) {


        writerResource(this.getClass(), "header.html", writer);
        if (Utility.isEmpty(root)) {

            writer.write("    <script src=\"/js/UMC.js\"></script>");

        } else if (root.endsWith(".js")) {
            writer.write("    <script src=\"/js/");
            writer.write(root);
            writer.write("></script>");
        } else {
            writer.write("    <script>WDK.POS.Config({posurl: ['/");
            writer.write(root);

            writer.write("/', WDK.cookie('device') || WDK.cookie('device', WDK.uuid())].join('')});</script>");
        }
        writer.write("\n");
        writerResource(WebServlet.class, html, writer);

    }

    private void writerResource(Class cls, String resKey, PrintWriter writer) {
        InputStream inputStream = cls.getClassLoader().getResourceAsStream(resKey);


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

    private void temp(NetContext context) {
        String file = context.getUrl().getPath().substring(5);
        String filename = UMC.Data.Utility.mapPath("App_Data\\Static\\TEMP\\" + file);
        switch (context.getHttpMethod()) {
            case "GET":
                transmitFile(context, filename);
                break;
            case "PUT":
                try {
                    Utility.copy(context.getInputStream(), filename);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    private void transmitFile(NetContext resp, File file) {

        int lastIndex = file.getPath().lastIndexOf('.');

        String extName = "html";
        if (lastIndex > -1) {
            extName = file.getPath().substring(lastIndex + 1);
        }
        switch (extName.toLowerCase()) {
            case "pdf":
                resp.setContentType("application/pdf");
                break;
            case "txt":
                resp.setContentType("text/plain");
                break;
            case "htm":
            case "html":
                resp.setContentType("text/html");
                break;
            case "json":
                resp.setContentType("text/json");
                break;
            case "js":
                resp.setContentType("text/javascript");
                break;
            case "css":
                resp.setContentType("text/css");
                break;
            case "bmp":
                resp.setContentType("image/bmp");
                break;
            case "gif":
                resp.setContentType("image/gif");
                break;
            case "jpeg":
            case "jpg":
                resp.setContentType("image/jpeg");
                break;
            case "png":
                resp.setContentType("image/png");
                break;
            case "svg":
                resp.setContentType("image/svg+xml");
                break;
            case "mp3":
                resp.setContentType("audio/mpeg");
                break;
            case "mp4":
                resp.setContentType("video/mpeg4");
                break;
            default:
                resp.setContentType("application/octet-stream");
                break;
        }
        resp.addHeader("Last-Modified", new Date(file.lastModified()).toGMTString());

        String Since = resp.getHeader("If-Modified-Since");
        if (Utility.isEmpty(Since) == false) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
            try {
                Date date = dateFormat.parse(Since);
                if (date.getTime() >= file.lastModified()) {

                    resp.setStatusCode(304);
                    return;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        ;

        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);

            Utility.copy(inputStream, resp.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void transmitFile(NetContext resp, String name) {
        File file1 = new File(name);
        if (file1.exists()) {
            transmitFile(resp, file1);
        } else {
            resp.setStatusCode(404);
        }
    }


    private void Process(Map<String, String[]> nvs, InputStream input, PrintWriter writer,
                         URL Url, URL UrlReferrer, String UserHostAddress, String UserAgent, WebClient.IWebRedirect
                                 redirec, NetContext context) {

        String[] Segments = Utility.findAll(Url.getPath().split("/"), d -> Utility.isEmpty(d) == false).toArray(new String[0]);


        Map QueryString = new HashMap();

        String model = null, cmd = null;
        String start = null;
        String jsonp = null;
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
//

                        QueryString.put(key, value);

                    }
                    break;
            }
        }
        if (Utility.isEmpty(model))

        {
            if (Segments.length > 3 && Segments[Segments.length - 1].indexOf('.') == -1) {
                if (Segments.length > 3) {
                    model = Segments[2];
                    cmd = Segments[3];
                }
                if (Segments.length > 4) {
                    QueryString.put(Segments[4], null);
                }

            }
        }
        context.setContentType("text/json;charset=utf-8");
        if (Utility.isEmpty(model) == false)

        {
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

        WebSession session = WebSession.Instance();
        try

        {
            switch (Utility.isNull(model, "")) {
                case "System":
                    switch (cmd) {
                        case "Icon":
                        case "TimeSpan":
                        case "Start":
                        case "Setup":
                        case "Log":
                        case "Mapping":
                            session = new sysSession();
                            break;
                        default:
                            authorization(context);
                            break;
                    }
                    break;
                default:
                    authorization(context);

                    break;
            }
        } catch (SetupException e) {
            session = new sysSession();
            model = "System";
            cmd = "Start";

        }

        WebClient client = new WebClient(session, Url, UrlReferrer, UserAgent, UserHostAddress);
        client.InputStream = input;


        if (Utility.isEmpty(jsonp) == false && jsonp.startsWith("app"))

        {
            client.isApp = true;
        }

        client.XHRTime = Utility.parse(context.getHeader("umc-request-time"), 0) + 1;

        if (Utility.isEmpty(start) == false)

        {
            client.Start(start);
        } else if (Utility.isEmpty(model))

        {

            client.SendDialog(QueryString);
        } else

        {
            if (Utility.isEmpty(cmd)) {
                if (model.startsWith("[") == false) {
                    throw new IllegalArgumentException("command is empty");

                }
            } else {
                client.Command(model, cmd, QueryString);
            }
        }


        if (Utility.isEmpty(model) == false && model.startsWith("[") && Utility.isEmpty(cmd))

        {
            try {
                client.JSONP(model, jsonp, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else

        {
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
