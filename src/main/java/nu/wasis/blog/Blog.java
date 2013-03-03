package nu.wasis.blog;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class Blog {

    private static final Logger LOG = Logger.getLogger(Blog.class);

    /**
     * @param args
     */
    public static void main(final String[] args) {
        Spark.get(new Route("/") {

            @Override
            public Object handle(final Request arg0, final Response arg1) {
                final Configuration cfg = new Configuration();
                cfg.setClassForTemplateLoading(Blog.class, "/templates");
                final StringWriter writer = new StringWriter();
                try {
                    final Template template = cfg.getTemplate("index.ftl");
                    final Map<String, Object> hashMap = new HashMap<String, Object>();
                    template.process(hashMap, writer);
                } catch (final IOException e) {
                    LOG.error(e);
                } catch (final TemplateException e) {
                    LOG.error(e);
                }
                return writer;
            }
        });
    }
}
