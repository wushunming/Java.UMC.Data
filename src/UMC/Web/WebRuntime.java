
package UMC.Web;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.*;

import UMC.Data.*;

class WebRuntime {
    static {
        System.out.println("                                                                     ");
        System.out.println("                                                                     ");
        System.out.println("    $$         $$          $$$$$$$$   $$$$$$$            $$$$$$$$    ");
        System.out.println("    $$         $$         $$      $$$$      $$         $$            ");
        System.out.println("    $$         $$        $$        $$        $$       $$             ");
        System.out.println("    $$         $$        $$        $$        $$       $$             ");
        System.out.println("    $$         $$        $$        $$        $$       $$             ");
        System.out.println("    $$         $$        $$        $$        $$       $$             ");
        System.out.println("    $$         $$        $$        $$        $$       $$             ");
        System.out.println("     $$       $$         $$        $$        $$        $$            ");
        System.out.println("       $$$$$$$           $$        $$        $$          $$$$$$$$    ");
        System.out.println("                                                                     ");
        System.out.println("                                                                     ");

    }
    public static class AbortException extends RuntimeException {

    }

    WebRequest request;


    WebResponse response;


    IWebFactory cuurFlowFactory;
    WebFlow cuurFlow;
    WebActivity cuurActivity;
    WebContext context;
    Map items = new HashMap();
    WebClient client;

    private WebRuntime(Map header, WebClient client) {
        this.request = client.session().request();
        this.request.init(header, client);
        this.response = client.session().response();
        this.response.init(client);
        this.client = client;
    }

    private static class MappingFLow extends WebFlow {


        @Override
        public WebActivity firstActivity() {

            WebRequest webRequest = this.context().request();
            Map<String, Class> dic = activities.get(webRequest.model());
            if (dic.containsKey(webRequest.cmd())) {
                try {
                    Constructor constructor = dic.get(webRequest.cmd()).getDeclaredConstructor();
                    constructor.setAccessible(true);
                    return (WebActivity) constructor.newInstance();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
            return WebActivity.Empty;


        }
    }

    private static class MappingActivityFactory implements IWebFactory {
        @Override
        public void init(WebContext context) {

        }

        @Override
        public WebFlow flowHandler(String mode) {
            if (activities.containsKey(mode)) {

                return new MappingFLow();

            }
            return WebFlow.Empty;
        }


    }

    private static class MappingFlowFactory implements IWebFactory {
        @Override
        public void init(WebContext context) {

        }

        @Override
        public WebFlow flowHandler(String mode) {
            if (flows.containsKey(mode)) {
                try {

                    Constructor constructor = flows.get(mode).get(index).Type.getDeclaredConstructor();//[0];
                    constructor.setAccessible(true);
                    return (WebFlow) constructor.newInstance();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }

            }
            return WebFlow.Empty;
        }

        int index = 0;

        public MappingFlowFactory(int i) {
            this.index = i;
        }


    }

    private static IWebFactory[] getFactory(String model) {
        if (flows.containsKey(model)) {
            int len = flows.get(model).size();
            List<IWebFactory> list = new LinkedList<>();
            for (int i = 0; i < len; i++) {
                list.add(new MappingFlowFactory(i));

            }
            return list.toArray(new IWebFactory[0]);
        }
        return new IWebFactory[0];

    }

    public static WebContext ProcessRequest(Map header, WebClient client) {

        if (header == null) {

            WebRuntime runtime = new WebRuntime(new HashMap(), client);
            try {
                runtime.context = client.session().context();
                runtime.context.runtime = runtime;//_init(runtime);
                runtime.context.init(client);
            } catch (AbortException a) {
            }

            return runtime.context;
        } else {
            WebRuntime runtime = new WebRuntime(header, client);

            if (Utility.isEmpty(UMC.Security.AccessToken.get("Debug"))) {

                try {
                    runtime.DoFactory();
                } catch (AbortException a) {
                }

            } else {
                try {
                    runtime.DoFactory();
                } catch (AbortException a) {
                } catch (Throwable e) {
                    e.printStackTrace();

                    runtime.response.ClientEvent = WebEvent.ERROR;
                    runtime.response.headers().put("Error", e.getMessage());
                    System.out.println("--ERROR--");
                    System.out.println(runtime.request.uri().toString());
                    System.out.println(e.toString());
                    System.out.println("--END--");

                }
            }


            return runtime.context;
        }

    }

    public static class WeightClass {
        public int Weight;
        public Class Type;

        public WeightClass(Class type, int weight) {
            Type = type;
            this.Weight = weight;
        }
    }

    static Map<String, WebAuthType> authKeys = new HashMap<>();
    static List<WeightClass> facClas = new LinkedList<>();
    static Map<String, Map<String, Class>> activities = new HashMap<>();
    static Map<String, List<WeightClass>> flows = new HashMap<>();
    static Map<String, Integer> weightKeys = new HashMap<>();

    public static void register(Class cl) {

        if (cl.isAnnotationPresent(Mapping.class)) {
            Annotation[] annotations = cl.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Mapping) {
                    Mapping mapping = (Mapping) annotation;
                    if (!Utility.isEmpty(mapping.model()) && !Utility.isEmpty(mapping.cmd())) {

                        if (WebActivity.class.isAssignableFrom(cl)) {
                            if (!activities.containsKey(mapping.model())) {
                                activities.put(mapping.model(), new HashMap<>());
                            }
                            String key = String.format("%s.%s", mapping.model(), mapping.cmd());
                            if (weightKeys.containsKey(key)) {
                                Integer weight = weightKeys.get(key);
                                if (weight > mapping.weight()) {
                                    continue;
                                }
                            }
                            weightKeys.put(key, mapping.weight());
                            authKeys.put(key, mapping.auth());

                            activities.get(mapping.model()).put(mapping.cmd(), cl);


                        }
                    } else if (!Utility.isEmpty(mapping.model())) {

                        if (WebFlow.class.isAssignableFrom(cl)) {
                            if (!flows.containsKey(mapping.model())) {
                                flows.put(mapping.model(), new LinkedList<>());
                            }
                            List<WeightClass> list = flows.get(mapping.model());
                            if (Utility.exists(list, w -> w.Type.equals(cl)) == false) {
                                list.add(new WeightClass(cl, mapping.weight()));
                                authKeys.put(mapping.model(), mapping.auth());
                            }
                        }
                    } else if (IWebFactory.class.isAssignableFrom(cl)) {

                        if (Utility.exists(facClas, w -> w.Type.equals(cl)) == false) {
                            facClas.add(new WeightClass(cl, mapping.weight()));
                        }
                    } else if (UMC.Data.Sql.Initializer.class.isAssignableFrom(cl)) {
                        if (UMC.Data.Sql.Initializer.__initializers.indexOf(cl) == -1)
                            UMC.Data.Sql.Initializer.__initializers.add(cl);
                    }
                }
            }
        }
    }


    static void register(List<String> clses) {


        for (String cls : clses) {
            Class cl = null;
            try {
                cl = Class.forName(cls);
            } catch (Throwable e) {
                // e.printStackTrace();
                continue;
            }
            register(cl);


        }
    }


    void DoFactory() {


        context = client.session().context();
        context.runtime = this;//_init(this);
        context.init(this.client);

        List<IWebFactory> factorys = new LinkedList<>();
        factorys.add(new MappingActivityFactory());
        factorys.addAll(Arrays.asList(getFactory(request.model())));
        int webIndex = 0;

        for (WeightClass cls : facClas) {
            try {

                Constructor constructor = cls.Type.getDeclaredConstructor();//[0];
                constructor.setAccessible(true);
                IWebFactory webFactory = (IWebFactory) constructor.newInstance();
                webFactory.init(context);
                if (webFactory instanceof WebFactory) {
                    factorys.add(0, webFactory);
                    webIndex++;
                } else {
                    factorys.add(webFactory);
                }

            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        WebFactory webf = new WebFactory();
        webf.init(context);

        factorys.add(webIndex, webf);

        for (int Index = 0; Index < factorys.size(); Index++) {

            IWebFactory flowFactory = factorys.get(Index);
            this.cuurFlowFactory = flowFactory;

            WebFlow flow = flowFactory.flowHandler(this.request.model());

            flow._context = context;

            this.cuurFlow = flow;

            ProcessActivity(flow, flow.firstActivity());

        }
        context.complete();


    }

    void ProcessActivity(WebFlow flow, WebActivity active) {
        active._context = context;
        this.cuurActivity = active;
        active.processActivity(this.request, this.response);
        if (active.equals(WebActivity.Empty)) {
        } else {
            ProcessActivity(flow, flow.nextActivity(active.Id()));
        }
    }
}
