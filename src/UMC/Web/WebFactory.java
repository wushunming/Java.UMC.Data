package UMC.Web;


import UMC.Data.*;
import UMC.Security.AccessToken;
import UMC.Security.Identity;

import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.*;
import java.util.regex.Pattern;

public class WebFactory implements IWebFactory {
    static class XHR implements IJSON {
        public XHR(String xhr) {
            this.expression = xhr;
        }

        public String expression;


        @Override
        public void write(Writer writer) {

            try {
                writer.write(expression);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void read(String key, Object value) {

        }
    }

    class XHRFlow extends WebFlow {
        private URI uri;
        private String appSecret;
        private List<String> cmds = new LinkedList<>();

        public XHRFlow(URI uri, String secret) {
            this.uri = uri;
            this.appSecret = secret;

        }

        public XHRFlow(URI uri, String secret, String... cmds) {

            this.appSecret = secret;
            this.uri = uri;
            for (String c : cmds) {
                this.cmds.add(c);
            }
        }

        private String pattern;

        public XHRFlow(URI uri, String secret, String pattern) {

            this.uri = uri;
            this.appSecret = secret;
            this.pattern = pattern;

        }

        @Override
        public WebActivity firstActivity() {
            String cmd = this._context.request().cmd();
            if (this.cmds.size() > 0) {
                if (this.cmds.get(0).equals("*")) {
                    if (Utility.exists(this.cmds, e -> e.equals(cmd))) {

                        return WebActivity.Empty;
                    }
                } else if (Utility.exists(this.cmds, e -> e.equals(cmd)) == false) {
                    return WebActivity.Empty;
                }
            } else if (Utility.isEmpty(pattern) == false && Pattern.matches(pattern, cmd) == false) {
                return WebActivity.Empty;
            }
            StringBuilder sb = new StringBuilder();
            WebRequest req = this.context().request();
            if (uri.getPath().endsWith("/*")) {
                sb.append(uri.toString().substring(0, uri.toString().length() - 1));

            } else {
                sb.append(Utility.trim(uri.toString(), '/'));
                sb.append(req.uri().getPath().split("/")[1]);
                sb.append("/");
            }
            sb.append(AccessToken.token().toString());
            sb.append("/");
            if (req.headers().containsKey(UIDialog.Dialog)) {
                WebMeta meta = req.headers().meta(UIDialog.Dialog);
                if (meta != null) {

                    Map<String, Object> map = meta.map();
                    Set<Map.Entry<String, Object>> set = map.entrySet();
                    boolean isOne = true;
                    for (Map.Entry<String, Object> entry : set) {
                        if (isOne) {
                            sb.append("?");
                            isOne = false;
                        } else {
                            sb.append("&");
                        }
                        try {
                            sb.append(URLEncoder.encode(entry.getKey(), "utf-8"));
                            sb.append("=");
                            sb.append(URLEncoder.encode(entry.getValue().toString(), "utf-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    String dg = req.headers().get(UIDialog.Dialog);
                    sb.append("?");
                    try {
                        sb.append(URLEncoder.encode(dg, "utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
            } else {
                sb.append(req.model());
                sb.append("/");
                sb.append(req.cmd());
                sb.append("/");
                WebMeta meta = req.sendValues();
                if (meta != null) {

                    Map<String, Object> map = meta.map();
                    Set<Map.Entry<String, Object>> set = map.entrySet();
                    boolean isOne = true;
                    for (Map.Entry<String, Object> entry : set) {
                        if (isOne) {
                            sb.append("?");
                            isOne = false;
                        } else {
                            sb.append("&");
                        }
                        try {
                            sb.append(URLEncoder.encode(entry.getKey(), "utf-8"));
                            sb.append("=");
                            Object v = entry.getValue();
                            if (v != null)
                                sb.append(URLEncoder.encode(v.toString(), "utf-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                } else {

                    String dg = req.sendValue();
                    if (Utility.isEmpty(dg) == false) {
                        sb.append("?");
                        try {
                            sb.append(URLEncoder.encode(dg, "utf-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }

            Identity user = Identity.current();
            //打开和url之间的连接
            Map<String, String> headers = new HashMap<>();
            headers.put("umc-request-time", this._context.runtime.client.XHRTime + "");

            if (Utility.isEmpty(appSecret) == false) {

                headers.put("umc-request-user-id", Utility.uuid(user.id()));
                headers.put("umc-request-user-name", user.name());
                headers.put("umc-request-user-alias", user.alias());
                if (user.roles() != null && user.roles().length > 0) {
                    headers.put("umc-request-user-role", String.join(",", user.roles()));

                }
                headers.put("umc-request-sign", Utility.sign(headers, this.appSecret));
            }
            StringBuffer result = new StringBuffer();
            try {
                URL url = new URL(sb.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("User-Agent", req.userAgent());
                headers.forEach((k, v) -> {
                    try {
                        connection.setRequestProperty(k, URLEncoder.encode(v, "UTF-8"));
                    } catch (Throwable ex) {

                    }
                });
                connection.connect();

                int resultCode = connection.getResponseCode();
                if (HttpURLConnection.HTTP_OK == resultCode) {

                    //读取URL的响应
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            connection.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        result.append(line);
                    }
                    in.close();

                } else if (resultCode >= 300 && resultCode < 400) {
                    String location = connection.getHeaderField("Location");
                    connection.disconnect();
                    this.context().response().redirect(new URL(location));
                }
                connection.disconnect();
            } catch (Throwable ex) {
                this.prompt(ex.getMessage());
                // return;
            }


            String stringBuilder = result.toString();
            String eventPfx = "{\"ClientEvent\":";
            if (stringBuilder.startsWith(eventPfx)) {

                int index = stringBuilder.indexOf(",");
                if (index > -1) {
                    int webEvent = Utility.parse(stringBuilder.substring(eventPfx.length(), index), 0);
                    if ((webEvent & WebEvent.ASYNCDIALOG) == WebEvent.ASYNCDIALOG) {

                        this.context().response().redirect(new XHR(stringBuilder));
                    } else {


                        Map header = (Map) ((Map) JSON.deserialize(stringBuilder)).get("Headers");// as Hashtable;
                        if ((webEvent & WebEvent.RESET) == WebEvent.RESET) {
                            if (header.containsKey("Reset")) {

                                Map reset = (Map) header.get("Reset");// as Hashtable;
                                if (reset != null) {
                                    String Command = (String) reset.get("Command");//as string;
                                    String Model = (String) reset.get("Model"); //as string;
                                    String Value = (String) reset.get("Value"); //as string;
                                    if (Utility.isEmpty(Model) == false && Utility.isEmpty(Command) == false) {
                                        if (Utility.isEmpty(Value)) {
                                            this.context().response().redirect(Model, Command);
                                        } else {
                                            this.context().response().redirect(Model, Command, Value);
                                        }
                                    }

                                }
                            }
                        } else if ((webEvent & WebEvent.DATAEVENT) == WebEvent.DATAEVENT) {
                            WebRuntime rtime =
                                    this.context().runtime;

                            if (rtime.client.UIEvent != null) {
                                if (header.containsKey("DataEvent")) {
                                    List<WebMeta> dataEvents = new LinkedList<>();
                                    Object dataEvent = header.get("DataEvent");
                                    if (dataEvent instanceof Map) {
                                        dataEvents.add(new WebMeta((Map) dataEvent));
                                    } else if (dataEvent.getClass().isArray()) {
                                        int l = Array.getLength(dataEvent);
                                        for (int i = 0; i < l; i++) {
                                            Object o = Array.get(dataEvent, i);
                                            if (o instanceof Map) {
                                                dataEvents.add(new WebMeta((Map) o));
                                            }
                                        }
                                    }
                                    WebMeta value = Utility.find(dataEvents, g -> {
                                        String type = (String) g.get("type");
                                        String key = (String) g.get("key");
                                        return ("UI.Event".equals(type) && "Click".equals(key));
                                    });

                                    if (value != null && value.containsKey("value")) {
                                        Map va = (Map) value.map().get("value");
                                        if (va.containsKey("Value") && va.containsKey("Text")) {
                                            dataEvents.remove(va);
                                            value.put("value", new ListItem((String) va.get("Text"), (String) va.get("Value")));
                                            if (dataEvents.size() > 0) {
                                                this.context().response().headers().put("DataEvent", dataEvents.toArray(new WebMeta[0]));
//
                                                this.context().send(value, true);

                                            }
                                        }
                                    }
                                }
                            }
                        }
                        this.context().response().redirect(JSON.expression(stringBuilder));
//                    } else{
//
//                        this.context().response().redirect(JSON.expression(stringBuilder));
                    }
                }
            } else {

                this.context().response().redirect(JSON.expression(stringBuilder));
            }

            return WebActivity.Empty;
        }
    }

    public void init(WebContext context) {

    }

    @Override
    public WebFlow flowHandler(String mode) {
        ProviderConfiguration cfg = UMC.Data.ProviderConfiguration.configuration("UMC");
        if (cfg != null) {

            Provider provder = cfg.get(mode);
            if (provder != null) {
                String surl = provder.get("src");//provder["src"]);
                if (Utility.isEmpty(surl) == false) {
                    URI url = URI.create(provder.get("src"));
                    String secret = provder.get("secret");//as string;
                    if (Utility.isEmpty(provder.type()) == false) {
                        String type = provder.type();
                        if (type.startsWith("/") && type.endsWith("/")) {

                            return new XHRFlow(url, secret, Utility.trim(type, '/'));
                        } else if ("*".equals(type) == false) {
                            String[] cmds = type.split(",");
                            return new XHRFlow(url, secret, cmds);

                        } else {
                            return new XHRFlow(url, secret);
                        }
                    } else {

                        return new XHRFlow(url, secret);
                    }
                }
            }
        }
        return WebFlow.Empty;
    }
}
