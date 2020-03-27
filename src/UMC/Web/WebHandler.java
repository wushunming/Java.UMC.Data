package UMC.Web;

import java.util.HashMap;
import java.util.Map;

public abstract class WebHandler {

    public WebContext context() {
        return _context;
    }

    WebContext _context;


    public String asyncDialog(String asyncId, UIDialog.Callback callback) {

        return UIDialog.asyncDialog(_context, asyncId, callback);
    }

    public WebMeta asyncDialog(UIDialog.Callback callback, String asyncId) {

        return UIDialog.asyncDialog(asyncId, _context, callback);
    }

    public String asyncDialog(String asyncId, String model, String cmd) {
        return this.asyncDialog(asyncId, a -> {

            UIClick click = new UIClick(new WebMeta(_context.request().arguments()).put(a, "Value")).send(_context.request().model(), _context.request().cmd());
            ;
            _context.runtime.client.UIEvent = click;
            Map data = new HashMap();
            data.put("Click", UMC.Data.JSON.serialize(click));

            data.put("POS-MODEL", model);
            data.put("POS-COMMAND", cmd);

            _context.runtime.client.session.storage(data, _context);
            _context.response().redirect(model, cmd, "Click", true);
            return UIDialog.returnValue(a);

        }, false);

    }

    public String asyncDialog(String asyncId, String model, String cmd, WebMeta meta) {
        return this.asyncDialog(asyncId, a ->
        {
            UIClick click = new UIClick(new WebMeta(_context.request().arguments()).put(a, "Value")).send(_context.request().model(), _context.request().cmd());
            ;
            _context.runtime.client.UIEvent = click;
            Map data = new HashMap();
            data.put("Click", UMC.Data.JSON.serialize(click));

            data.put("POS-MODEL", model);
            data.put("POS-COMMAND", cmd);

            _context.runtime.client.session.storage(data, _context);
            _context.response().redirect(model, cmd, meta.put("Key", "Click"), true);
            return UIDialog.returnValue(a);
        });

    }

    protected String asyncDialog(String asyncId, UIDialog.Callback callback, boolean isDialog) {
        return UIDialog.asyncDialog(_context, asyncId, callback, isDialog);
    }

    protected String asyncDialog(String asyncId, String deValue) {
        return asyncDialog(asyncId, anyc -> dialogValue(deValue), false);

    }

    protected UIDialog dialogValue(String value) {
        return UIDialog.returnValue(value);
    }

    protected UIFormDialog dialogValue(WebMeta value) {
        return UIDialog.returnValue(value);
    }


    protected void prompt(String text) {

        prompt(text, true);
    }

    protected void prompt(String title, String text) {
        this.prompt(title, text, true);
    }

    protected void prompt(String text, boolean endResponse) {
        WebResponse response = _context.response();
        response.ClientEvent |= WebEvent.PROMPT;

        response.Headers.put("Prompt", new WebMeta().put("Text", text));
        if (endResponse) {
            _context.end();
        }

    }

    protected void prompt(String title, String text, boolean endResponse) {


        WebResponse response = _context.response();
        response.ClientEvent |= WebEvent.ASYNCDIALOG | WebClient.Prompt;
        WebMeta prompt = new WebMeta();
        prompt.put("Text", text).put("Title", title).put("Type", "Prompt");// = text;

        response.Headers.put("AsyncDialog", prompt);
        if (endResponse) {
            _context.end();
        }


    }
}
