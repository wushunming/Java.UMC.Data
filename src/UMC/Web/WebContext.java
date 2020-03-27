package UMC.Web;

import java.lang.reflect.Array;
import java.util.*;

public class WebContext {
    WebRuntime runtime;



    public WebRequest request() {
        return runtime.request;
    }

    public WebResponse response() {
        return runtime.response;
    }

    protected void complete() {

    }

    public WebActivity activity() {
        return runtime.cuurActivity;
    }

    public WebFlow flow() {
        return runtime.cuurFlow;
    }


    public Map items() {
        return runtime.items;
    }

    protected void init(WebClient client) {
    }

    public static void end() {

        throw new WebRuntime.AbortException();
    }

    public void reset() {
        runtime.response.ClientEvent |= WebEvent.RESET;
    }

    public void send(WebMeta data, boolean endResponse) {
        if (data.containsKey("type") && data.get("type").equals("UI.Event")) {
            if (runtime.client.UIEvent != null) {

                String key = data.get("key");
                if ("Click".equals(key) && data.map().get("value") instanceof ListItem) {
                    UIClick click = runtime.client.UIEvent;
                    runtime.client.UIEvent = null;
                    runtime.client.session.storage(new HashMap(), this);
                    ListItem value = (ListItem) data.map().get("value");
                    Object objValue = click.send();
                    if (objValue instanceof Map) {
                        Map<Object, Object> val = (Map) objValue;

                        for (Map.Entry entry : val.entrySet()) {
                            if ("Value".equals(entry.getValue())) {

                                val.put(entry.getKey() + "_Text", value.Text);
                                val.put(entry.getKey(), value.Value);
                                break;
                            }
                        }
                        this.response().redirect(click.getModel(), click.getCommand(), new WebMeta(val), false);

                    } else if (objValue instanceof WebMeta) {
                        WebMeta val2 = (WebMeta) objValue;
                        Map<String, Object> val = val2.map();

                        for (Map.Entry<String, Object> entry : val.entrySet()) {
                            if ("Value".equals(entry.getValue())) {

                                val.put(entry.getKey() + "_Text", value.Text);
                                val.put(entry.getKey(), value.Value);
                                break;
                            }
                        }

                        this.response().redirect(click.getModel(), click.getCommand(), new WebMeta(val), false);


                    } else {
                        this.response().redirect(click.getModel(), click.getCommand(), value.Value, false);



                    }
                }

            }
        }
        WebResponse response = this.response();
        response.ClientEvent |= WebEvent.DATAEVENT;
        if (response.Headers.containsKey("DataEvent")) {
            Object ts = response.Headers.map().get("DataEvent");
            if (ts instanceof WebMeta) {
                response.Headers.put("DataEvent", new WebMeta[]{(WebMeta) ts, data});

            } else if (ts instanceof Map) {

                response.Headers.put("DataEvent", new WebMeta[]{new WebMeta((Map) ts), data});

            } else if (ts.getClass().isArray()) {

                WebMeta[] mts = new WebMeta[Array.getLength(ts) + 1];
                Arrays.copyOf(mts, mts.length - 2);

                mts[mts.length - 1] = data;

                response.Headers.put("DataEvent", mts);
            } else {
                response.Headers.put("DataEvent", data);
            }

        } else {

            response.Headers.put("DataEvent", data);
        }
        if (endResponse) {
            response.ClientEvent ^= response.ClientEvent & WebEvent.NORMAL;
            this.end();
        }

    }

    public void send(String type, WebMeta data, boolean endResponse) {
        this.send(data.put("type", type), endResponse);
    }

    public void send(String type, boolean endResponse) {
        WebMeta data = new WebMeta();
        send(type, data, endResponse);
    }


}
