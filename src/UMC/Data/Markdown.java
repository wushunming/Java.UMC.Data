package UMC.Data;

import UMC.Web.UICell;
import UMC.Web.UIClick;
import UMC.Web.UIStyle;
import UMC.Web.WebMeta;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;


public class Markdown {
    WebMeta webRel = new WebMeta();
    List<UICell> cells = new LinkedList<>();
    WebMeta data = new WebMeta();
    UIStyle style = new UIStyle();
    StringBuilder dataText = new StringBuilder();

    void Header(String text) {
        int i = 0;
        while (i < text.length() && text.charAt(i) == '#') {
            i++;
            if (i == 6) {
                break;
            }
        }
        int size = 26 - (i - 1) * 2;

        UICell cell = UICell.create("CMSText", new WebMeta().put("text", text.substring(i).trim()).put("Key", i));
        cell.format("text", "{text}");
        cell.style().bold().size(size);
        cells.add(cell);
    }

    private Markdown() {
    }

    static String[] keys = new String[]{"abstract", "+", "-", "*", "/", "%", "var", "instanceof", "extern", "private", "protected", "public", "namespace", "class", "for", "if", "else", "async", "while", "switch", "case", "using", "get", "return", "null", "void", "int", "string", "float", "char", "this", "set", "new", "true", "false", "const", "static", "internal", "extends", "super", "import", "default", "break", "try", "catch", "finally", "implements", "package", "final"};

    private static class Highlighter {

        WebMeta data = new WebMeta();
        UIStyle style = new UIStyle();
        StringBuilder dataText = new StringBuilder();


        public UICell Paster(String text, String type) {
            switch (Utility.isNull(type, "").toLowerCase().trim()) {
                case "text":
                case "shell":
                    dataText.append(text);
                    break;
                case "html":
                case "xml":
                    CheckXml(text); 
                    Append();
                    break;
                default:
                    Check(text);
                    Append();
                    break;
            }
            if (dataText.length() > 0) {
                data.put("h" + data.size(), dataText.toString());
            }
            dataText = new StringBuilder();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.size(); i++) {
                sb.append("{h");
                sb.append(i);
                sb.append("}");
            }
            UICell cell = UICell.create("CMSCode", data);
            if (Utility.isEmpty(type) == false) {
                data.put("type", type);
            }
            cell.format("text", sb.toString());
            cell.style().copy(style);

            return cell;

        }

        public void CheckWork() {
            String value = dataText.toString();
            for (String k : keys) {
                if (value.endsWith(k)) {
                    if (value.length() > k.length()) {
                        switch (value.charAt(value.length() - k.length() - 1)) {
                            case '.':
                            case ' ':
                            case '\n':
                            case '(':
                            case '[':
                            case '{':
                            case '\t':
                                //dataText.d
                                dataText.delete(value.length() - k.length(), value.length());
                                data.put("h" + data.size(), dataText.toString());

                                style.name("h" + data.size()).color(0x00f);
                                data.put("h" + data.size(), k);
                                dataText.delete(0, dataText.length());
                                break;
                        }
                    } else {
                        style.name("h" + data.size()).color(0x00f);
                        data.put("h" + data.size(), k);
                        dataText.delete(0, dataText.length());
                    }
                }
            }
        }

        public void CheckMethod() {
            String value = dataText.toString();
            int t = value.length() - 1;
            while (t > -1) {
                switch (value.charAt(t)) {
                    case '.':
                    case ' ':
                    case '\n':
                    case '(':
                    case '[':
                    case '{':
                    case '\t':
                        if (t + 1 < value.length()) {

                            String fm = value.substring(t + 1);

                            data.put("h" + data.size(), value.substring(0, t + 1));

                            style.name("h" + data.size()).color(0x2196f3);
                            data.put("h" + data.size(), fm);
                            dataText.delete(0, dataText.length());

                        }
                        return;
                }
                t--;
            }
        }

        public void Append() {
            CheckWork();
            if (dataText.length() > 0) {

                data.put("h" + data.size(), dataText.toString());
                dataText.delete(0, dataText.length());
            }
        }

        void Append2() {
            if (dataText.length() > 0) {

                data.put("h" + data.size(), dataText.toString());
                dataText.delete(0, dataText.length());
            }
        }

        void CheckXml(String code) {
            int index = 0;
            while (index < code.length()) {
                switch (code.charAt(index)) {
                    case '<':
                        Append2();
                        String k = code.substring(index, index + 10);
                        int end = 0;
                        if (k.startsWith("<!--")) {
                            end = code.indexOf("-->", index);

                            style.name("h" + data.size()).color(0x008000);
                            if (end == -1) {

                                data.put("h" + data.size(), code.substring(index));
                                return;
                            } else {

                                data.put("h" + data.size(), code.substring(index, end + 3));
                            }
                            index = end + 3;
                        } else if (k.startsWith("<?")) {

                            end = code.indexOf(">", index);


                            style.name("h" + data.size()).color(0x999);
                            if (end == -1) {

                                data.put("h" + data.size(), code.substring(index));
                                return;
                            } else {

                                data.put("h" + data.size(), code.substring(index, end + 1));
                            }
                            index = end + 1;
                        } else if (k.startsWith("<![CDATA[")) {

                            end = code.indexOf("]]>", index);

                            style.name("h" + data.size()).color(0x999);
                            if (end == -1) {

                                data.put("h" + data.size(), code.substring(index));
                                return;
                            } else {

                                data.put("h" + data.size(), code.substring(index, end - index + 1));
                            }
                            index = end + 3;
                        } else {
                            if (code.length() > index + 1) {
                                switch (code.charAt(index + 1)) {
                                    case '<':
                                    case ' ':
                                        dataText.append(code.substring(index, index + 1));
                                        index++;
                                        continue;


                                }
                            }
                            end = code.indexOf(">", index);
                            if (end > index) {
                                String Html = code.substring(index, end + 1);
                                index = end + 1;

                                int tagIndex = Html.indexOf(' ');
                                style.name("h" + data.size()).color(0x1890ff);
                                if (tagIndex > -1) {

                                    data.put("h" + data.size(), Html.substring(0, tagIndex));

                                } else {
                                    data.put("h" + data.size(), Html);
                                    tagIndex = Html.length();


                                }
                                while (tagIndex < Html.length()) {
                                    switch (Html.charAt(tagIndex)) {
                                        case '=':
                                        case ' ':
                                            if (dataText.length() > 0) {
                                                switch (dataText.charAt(dataText.length() - 1)) {
                                                    case '=':
                                                    case ' ':
                                                        break;
                                                    default:
                                                        style.name("h" + data.size()).color(0x315efb);

                                                        data.put("h" + data.size(), dataText.toString());
                                                        dataText.delete(0, dataText.length());


                                                        break;
                                                }
                                            }


                                            dataText.append(Html.substring(tagIndex, tagIndex + 1));


                                            break;
                                        case '/':
                                        case '>':
                                            if (dataText.length() > 0) {

                                                switch (dataText.charAt(dataText.length() - 1)) {
                                                    case '=':
                                                    case ' ':

                                                        data.put("h" + data.size(), dataText.toString());
                                                        dataText.delete(0, dataText.length());
                                                        break;
                                                    default:


                                                        style.name("h" + data.size()).color(0x315efb);
                                                        data.put("h" + data.size(), dataText.toString());
                                                        dataText.delete(0, dataText.length());


                                                        break;
                                                }


                                            }
                                            if (Html.charAt(tagIndex) == '/') {

                                                style.name("h" + data.size()).color(0x1890ff);
                                                data.put("h" + data.size(), Html.substring(tagIndex));

                                                tagIndex = Html.length();


                                            } else {
                                                style.name("h" + data.size()).color(0x1890ff);
                                                data.put("h" + data.size(), ">");
                                            }
                                            break;
                                        case '\'':
                                        case '"':
                                            data.put("h" + data.size(), dataText.toString());
                                            dataText.delete(0, dataText.length());
                                            int dend = Html.indexOf(Html.charAt(tagIndex), tagIndex + 1);
                                            style.name("h" + data.size()).color(0xc00);
                                            if (dend == -1) {
                                                dend = Html.length();
                                                data.put("h" + data.size(), Html.substring(tagIndex));
                                            } else {
                                                data.put("h" + data.size(), Html.substring(tagIndex, dend + 1));
                                            }


                                            tagIndex = dend;
                                            break;
                                        default:
                                            if (dataText.length() > 0) {
                                                switch (dataText.charAt(dataText.length() - 1)) {
                                                    case '=':
                                                    case ' ':

                                                        data.put("h" + data.size(), dataText.toString());
                                                        dataText.delete(0, dataText.length());
                                                        break;
                                                }
                                            }

                                            dataText.append(Html.substring(tagIndex, tagIndex + 1));

                                            break;
                                    }
                                    tagIndex++;
                                }
                                continue;
                            }
                        }
                        break;
                }
                dataText.append(code.substring(index, index + 1));
                index++;
            }

        }

        void Check(String code) {
            int index = 0;
            while (index < code.length()) {
                switch (code.charAt(index)) {
                    case '"': {
                        Append();
                        int end = code.indexOf('"', index + 1);

                        style.name("h" + data.size()).color(0xc00);
                        if (end == -1) {
                            end = code.indexOf('\n', index);
                            if (end == -1) {
                                data.put("h" + data.size(), code.substring(index));
                                return;
                            } else {

                                data.put("h" + data.size(), code.substring(end - index + 1));
                                index = end + 1;

                                continue;
                            }
                        } else {
                            while (code.charAt(end - 1) == '\\') {

                                end = code.indexOf('"', end + 1);
                                if (end == -1) {
                                    data.put("h" + data.size(), code.substring(index));
                                    return;
                                }
                            }

                            data.put("h" + data.size(), code.substring(index, end + 1));
                        }
                        index = end + 1;

                        continue;
                    }
                    case '\'': {
                        Append();
                        int end = code.indexOf('\'', index + 1);

                        if (end > 0) {
                            style.name("h" + data.size()).color(0xc00);

                            data.put("h" + data.size(), code.substring(index, end + 1));

                            index = end + 1;
                            continue;

                        }
                    }
                    break;
                    case '/':
                        if (index + 1 < code.length()) {
                            switch (code.charAt(index + 1)) {
                                case '/': {
                                    Append();
                                    if (dataText.length() > 0) {
                                        data.put("h" + data.size(), dataText.toString());
                                    }
                                    dataText.delete(0, dataText.length());
                                    style.name("h" + data.size()).color(0x008000);
                                    int end = code.indexOf('\n', index);
                                    if (end == -1) {

                                        data.put("h" + data.size(), code.substring(index));
                                        return;
                                    } else {

                                        data.put("h" + data.size(), code.substring(index, end));
                                    }
                                    index = end;
                                }
                                continue;
                                case '*': {
                                    Append();
                                    if (dataText.length() > 0) {
                                        data.put("h" + data.size(), dataText.toString());
                                    }
                                    dataText.delete(0, dataText.length());
                                    style.name("h" + data.size()).color(0x008000);
                                    int end = code.indexOf("*/", index);
                                    if (end == -1) {

                                        data.put("h" + data.size(), code.substring(index));
                                        return;
                                    } else {

                                        data.put("h" + data.size(), code.substring(index, end + 2));
                                    }
                                    index = end + 2;
                                    continue;
                                }
                            }
                        }
                        break;
                    case ' ':
                    case '.':
                        CheckWork();
                        break;
                    case '(':
                        CheckMethod();
                        break;
                }
                dataText.append(code.substring(index, index + 1));
                index++;
            }

        }
    }

    public static UICell highlight(String text, String type) {
        return new Highlighter().Paster(text, type);
    }

    public static UICell[] transform(String text) {
        Markdown mk = new Markdown();
        mk.Check(text);
        for (UIClick click : mk.links) {
            click.send(mk.webRel.get((String) click.send()));
        }
        for (WebMeta meta : mk.webRels) {
            meta.put("src", mk.webRel.get(meta.get("src")));
        }
        return mk.cells.toArray(new UICell[0]);

    }

    void Append() {
        if (data.size() > 0 || dataText.length() > 0) {
            if (dataText.length() > 0) {
                data.put("m" + data.size(), dataText.toString());
            }
            dataText = new StringBuilder();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.size(); i++) {
                sb.append("{m");
                sb.append(i);
                sb.append("}");
            }
            UICell cell = UICell.create(Utility.isNull(data.get("type"), "CMSText"), data);
            data.remove("type");
            cell.format("text", sb.toString());
            cell.style().copy(style);
            cells.add(cell);
            data = new WebMeta();
            style = new UIStyle();
        }
    }

    int CheckRow(String text, int index) {
        return CheckRow(text, index, true);
    }

    void AppendData() {
        if (dataText.length() > 0) {
            data.put("m" + data.size(), dataText.toString());
            dataText = new StringBuilder();
        }
    }

    private List<WebMeta> webRels = new LinkedList<>();
    private List<UIClick> links = new LinkedList();

    int CheckRow(String text, int index, boolean isNextLIne) {
        if (Utility.isEmpty(text)) {
            return index + 1;
        }
        int oldIndex = -1;
        while (index + 1 < text.length() && text.charAt(index) != '\n') {
            if (oldIndex == index) {
                index++;
                continue;
            } else {
                oldIndex = index;

            }

            switch (text.charAt(index)) {
                case '\r':
                    index++;
                    continue;
                case '!':
                    if (text.charAt(index + 1) == '[' && isNextLIne) {
                        int end = text.indexOf("]", index + 1);
                        if (end > index) {

                            String content = Utility.trim(text.substring(index + 1, end - 1), '[', ']');
                            if (content.indexOf('\n') == -1) {
                                if (text.charAt(end + 1) == '(') {
                                    Append();
                                    int end2 = text.indexOf(")", end + 1);
                                    if (end2 > end) {
                                        String url = Utility.trim(text.substring(end + 1, end2 - 1), ' ', '(', ')').split(" ")[0];
                                        UICell cell = UICell.create("CMSImage", new WebMeta().put("src", url));
                                        cell.style().padding(0, 10);
                                        cells.add(cell);
                                        index = end2 + 1;

                                        continue;
                                    }
                                } else {
                                    Append();

                                    if (webRel.containsKey(content)) {
                                        UICell cell = UICell.create("CMSImage", new WebMeta().put("src", webRel.get(content)));
                                        cell.style().padding(0, 10);
                                        cells.add(cell);
                                    } else {
                                        WebMeta src = new WebMeta().put("src", content);
                                        this.webRels.add(src);
                                        UICell cell = UICell.create("CMSImage", src);
                                        cell.style().padding(0, 10);
                                        cells.add(cell);

                                    }
                                    index = end + 1;

                                    continue;
                                }

                            }

                        }
                    }
                    break;
                case '[': {
                    int end = text.indexOf("]", index + 1);
                    if (end > index) {
                        String content = Utility.trim(text.substring(index, end), '[', ']');
                        if (content.indexOf('\n') == -1) {
                            if (text.length() > end + 1 && text.charAt(end + 1) == '(') {

                                int end2 = text.indexOf(")", end + 1);
                                if (end2 > end) {
                                    String url = Utility.trim(text.substring(end + 1, end2 - 1), ' ', '(', ')').split(" ")[0];


                                    AppendData();
                                    style.name("m" + data.size(), new UIStyle().click(new UIClick(url).key("Url")));
                                    data.put("m" + data.size(), content);

                                    index = end2 + 1;

                                    continue;
                                }
                            } else {
                                if (Utility.isEmpty(content.trim()) == false) {
                                    AppendData();

                                    if (webRel.containsKey(content)) {
                                        style.name("m" + data.size(), new UIStyle().click(new UIClick(webRel.get(content)).key("Url")));
                                    } else {
                                        UIClick click = new UIClick(content).key("Url");
                                        this.links.add(click);
                                        style.name("m" + data.size(), new UIStyle().click(click));

                                    }
                                    data.put("m" + data.size(), content);
                                } else {
                                    dataText.append("[");
                                    dataText.append(content);
                                    dataText.append("]");
                                }

                                index = end + 1;

                                continue;
                            }

                        }
                    }
                }
                break;
                case '`': {

                    int end = text.indexOf("`", index + 1);
                    if (end > index) {
                        String content = text.substring(index, end);
                        if (content.indexOf('\n') == -1) {

                            AppendData();
                            style.name("m" + data.size(), new UIStyle().color(0xCC6600));
                            data.put("m" + data.size(), Utility.trim(content, '`'));


                            index = end + 1;

                            continue;
                        }
                    }
                }
                break;
                case '~':
                    if (text.charAt(index + 1) == '~') {

                        int end = text.indexOf("~~", index + 1);
                        if (end > index) {

                            String content = text.substring(index, end);
                            if (content.indexOf('\n') == -1) {

                                AppendData();
                                style.name("m" + data.size(), new UIStyle().delLine());
                                data.put("m" + data.size(), Utility.trim(content, '~'));

                                index = end + 2;

                                continue;

                            }
                        }

                    } else {
                        int end = text.indexOf("~", index + 1);
                        if (end > index) {
                            String content = text.substring(index, end);
                            if (content.indexOf('\n') == -1) {
                                AppendData();
                                style.name("m" + data.size(), new UIStyle().underLine());
                                data.put("m" + data.size(), Utility.trim(content, '~'));
                                index = end + 1;

                                continue;


                            }
                        }

                    }
                    break;
                case '*':
                    if (text.charAt(index + 1) == '*') {

                        int end = text.indexOf("**", index + 1);

                        if (end > index) {
                            String content = text.substring(index, end);
                            if (content.indexOf('\n') == -1) {

                                AppendData();
                                style.name("m" + data.size(), new UIStyle().bold());
                                data.put("m" + data.size(), Utility.trim(content, '*'));

                                index = end + 2;

                                continue;

                            }
                        }

                    } else {
                        int end = text.indexOf("*", index + 1);
                        if (end > index) {
                            String content = text.substring(index + 1, end);
                            if (content.indexOf('\n') == -1) {

                                AppendData();
                                style.name("m" + data.size(), new UIStyle().underLine());
                                data.put("m" + data.size(), Utility.trim(content, '*'));
                                index = end + 1;

                                continue;


                            }
                        }

                    }
                    break;
            }
            dataText.append(text.substring(index, index + 1));
            index++;
        }
        if (index + 1 == text.length()) {
            dataText.append(text.substring(index, index + 1));
        }
        if (isNextLIne) {
            Append();
            return index + 1;
            //Check(text, index + 1);
        }
        return index;

    }

    void Grid(List<String> rows) {
        List<UIStyle> hStyle = new LinkedList<>();
        String[] header = Utility.trim(rows.get(1), '|').split("\\|");
        List<List<WebMeta>> grid = new LinkedList<>();
        int flexs = 0;
        for (String h : header) {
            UIStyle st = new UIStyle();
            String s = h.trim();
            if (s.startsWith(":") && s.endsWith(":")) {
                st.alignCenter();
            } else if (s.endsWith(":")) {
                st.alignRight();
            } else {
                st.alignLeft();
            }
            int flex = s.split("-").length - 1;
            st.name("flex", flex);
            flexs += flex;
            hStyle.add(st);
        }
        rows.remove(1);
        for (String row : rows) {
            String[] cells = Utility.trim(row, '|').split("\\|");
            List<WebMeta> cdata = new LinkedList<>();
            for (int i = 0; i < hStyle.size(); i++) {
                UIStyle cstyle = new UIStyle();
                cstyle.copy(hStyle.get(i));
                this.style = cstyle;
                this.data = new WebMeta();
                this.dataText = new StringBuilder();
                if (i < cells.length) {
                    CheckRow(cells[i].trim(), 0, false);
                }
                if (dataText.length() > 0) {
                    this.data.put("m" + data.size(), dataText.toString().trim());
                }
                StringBuilder sb = new StringBuilder();
                for (int c = 0; c < data.size(); c++) {
                    sb.append("{m");
                    sb.append(c);
                    sb.append("}");
                }
                cdata.add(new WebMeta().put("format", sb.toString()).put("data", this.data).put("style", this.style));

            }
            grid.add(cdata);
        }

        UICell cell = UICell.create("CMSGrid", new WebMeta().put("grid", grid).put("flex", flexs));
        cells.add(cell);
        data = new WebMeta();
        style = new UIStyle();
        dataText = new StringBuilder();

    }

    void Check(String text) {
        int index = 0;
        while (index < text.length()) {
            if (index + 1 >= text.length()) {
                Append();
                return;
            }
            switch (text.charAt(index)) {
                case '#': {
                    int end = text.indexOf('\n', index);
                    if (end > index) {
                        Header(text.substring(index, end));

                        index = end + 1;
                    } else {
                        Header(text.substring(index));
                        index = text.length();
                    }
                    continue;
                }
                case '`': {
                    if (text.substring(index, index + 3) == "```") {

                        int end = text.indexOf("\n```", index + 1);
                        if (end > index) {
                            String content = text.substring(index, end - index);
                            int hindex = content.indexOf('\n');
                            String htype = content.substring(3, hindex - 3).trim();
                            content = content.substring(hindex + 1);

                            UICell cell = UICell.create("CMSCode", new WebMeta().put("code", content).put("type", htype));

                            cell.format("text", "{code}");


                            cells.add(cell);
                            index = text.indexOf('\n', end + 1) + 1;

                            continue;
                        }
                    }
                }
                break;
                case '>': {
                    if (cells.size() > 0) {
                        UICell cell = cells.get(cells.size() - 1);
                        if (cell.type().equals("CMSRel")) {
                            WebMeta d = (WebMeta) cell.data();// as WebMeta;

                            cells.remove(cells.size() - 1);
                            this.data = d;
                            this.dataText.append("\r\n");
                            this.style = cell.style();
                            this.data.put("type", "CMSRel");
                            index = CheckRow(text, index + 1);
                            continue;

                        }
                    }
                    this.data.put("type", "CMSRel");
                    index = CheckRow(text, index + 1);
                    continue;
                }
                case '|': {

                    int end = text.indexOf('\n', index);
                    if (end > index) {
                        List<String> grids = new LinkedList<>();
                        String conent = text.substring(index, end).trim().replace(" ", "");
                        if (conent.charAt(conent.length() - 1) == '|') {
                            int end2 = text.indexOf('\n', end + 1);//.Trim();
                            String conent2 = text.substring(end + 1, end2 - 1).trim();
                            grids.add(conent);
                            conent2 = conent2.replace(" ", "");

                            if (Pattern.matches("^[\\|:-]+$", conent2)) {
                                grids.add(conent2);
                                if (conent2.split("\\|").length == conent.split("\\|").length) {
                                    boolean isGO = true;
                                    while (isGO) {
                                        isGO = false;
                                        int end3 = text.indexOf('\n', end2 + 1);


                                        String conent3 = end3 > 0 ? text.substring(end2 + 1, end3 - 1).trim() : text.substring(end2 + 1).trim();
                                        if (conent3.startsWith("|") && conent3.endsWith("|")) {
                                            isGO = true;
                                            grids.add(conent3);
                                            end2 = end3 > 0 ? end3 : text.length() - 1;
                                        }
                                    }
                                    this.Grid(grids);
                                    index = end2 + 1;
                                    continue;

                                }
                            }

                        }
                    }
                }
                break;
                case '[': {
                    int end = text.indexOf("]", index + 1);
                    if (end > index && end + 1 < text.length()) {
                        if (text.charAt(end + 1) == ':') {
                            String content = Utility.trim(text.substring(index, end - index), '[', ']');
                            if (content.indexOf('\n') == -1) {
                                int end2 = text.indexOf("\n", end + 1);
                                if (end2 == -1) {
                                    String url = Utility.trim(text.substring(end + 2).trim(), ' ', '(', ')').split(" ")[0];
                                    webRel.put(content, url);
                                } else {

                                    String url = Utility.trim(text.substring(end + 2, end2 - 2).trim(), ' ', '(', ')').split(" ")[0];
                                    webRel.put(content, url);
                                    //Check(text, end2 + 1);

                                    index = end2 + 1;
                                }
                                continue;

                            }


                        }
                    }
                }
                break;
                case ' ':
                    while (text.length() > index && text.charAt(index) == ' ') {
                        index++;
                    }
                    break;

            }

            index = this.CheckRow(text, index);
        }
    }
}
