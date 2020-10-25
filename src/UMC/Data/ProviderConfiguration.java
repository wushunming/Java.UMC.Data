package UMC.Data;


import com.sun.jndi.toolkit.url.Uri;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
//import java.awt.print.Book;
import java.io.*;
import java.net.URL;
import java.util.*;

public class ProviderConfiguration {
    private List<Provider> _config;

    private static Map<String, ProviderConfiguration> configuration;

    static {
        configuration = new HashMap<>();
    }

    public ProviderConfiguration() {

        _config = new LinkedList<>();
    }

    public ProviderConfiguration(InputStream inputStream) {
        List<String> names = new LinkedList<>();
        _config = new LinkedList<>();
        //将给定 URI 的内容解析为一个 XML 文档,并返回Document对象
        Document document = null;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
            inputStream.close();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        //按文档顺序返回包含在文档中且具有给定标记名称的所有 Element 的 NodeList
        NodeList bookList = document.getElementsByTagName("add");

        //遍历books
        for (int i = 0; i < bookList.getLength(); i++) {
            org.w3c.dom.Node node = bookList.item(i);
            Provider provider = new Provider(node);
            int index = names.indexOf(provider.name());
            if (index > -1) {
                _config.remove(index);

            }
            _config.add(provider);
        }


    }

    public int size() {
        return _config.size();
    }

    public Provider get(int index) {
        if (index > -1 && index < _config.size()) {
            return _config.get(index);
        }
        return null;
    }

    public void set(Provider provider) {
        for (int i = 0; i < _config.size(); i++) {
            Provider provider1 = _config.get(i);
            if (provider1.name().equals(provider.name())) {
                _config.set(i, provider);
                return;
            }
        }
        _config.add(provider);

    }

    public void remove(String name) {
        for (int i = 0; i < _config.size(); i++) {
            Provider provider1 = _config.get(i);
            if (provider1.name().equals(name)) {
                _config.remove(i);
                return;
            }
        }
    }

    public Provider get(String name) {
        Iterator<Provider> iterator = _config.iterator();
        while (iterator.hasNext()) {
            Provider provider = iterator.next();
            if (provider.name().equals(name)) {
                return provider;
            }
        }
        return null;
    }

    public static void clear() {
        configuration.clear();
    }

    public static ProviderConfiguration configuration(String name) {
        if (configuration.containsKey(name)) {
            return configuration.get(name);
        } else {
            try {
                File file = new File(Utility.mapPath("App_Data/UMC/" + name + ".xml"));
                if (file.exists()) {
                    FileInputStream inputStream = new FileInputStream(file);
                    ProviderConfiguration providerConfiguration = new ProviderConfiguration(inputStream);
                    configuration.put(name, providerConfiguration);
                    return providerConfiguration;
                }
            } catch (FileNotFoundException e) {
                //  e.printStackTrace();
            }
        }
        return null;
    }

    public void WriteTo(File file) throws Exception {
        if (!file.exists() == false) {

            if (file.getParentFile().exists() == false) {
                file.getParentFile().mkdirs();
            }

        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = factory.newDocumentBuilder();
        Document document = db.newDocument();
        // 创建根节点 list;
        Element root = document.createElement("umc");
        document.appendChild(root);
        Element provider = document.createElement("providers");
        root.appendChild(provider);
        for (Provider provider1 : _config) {
            Element add = document.createElement("add");
            for (Map.Entry<String, String> item : provider1.attributes().entrySet()) {
                add.setAttribute(item.getKey(), item.getValue());
            }
            add.setAttribute("name", provider1.name());
            add.setAttribute("type", provider1.type());
            provider.appendChild(add);
        }
        // 创建TransformerFactory对象
        TransformerFactory tff = TransformerFactory.newInstance();
        // 创建 Transformer对象
        Transformer tf = tff.newTransformer();

        // 输出内容是否使用换行
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        // 创建xml文件并写入内容
        if (file.getParentFile().exists() == false) {
            file.getParentFile().mkdirs();
        }
        tf.transform(new DOMSource(document), new StreamResult(file));


    }
}
