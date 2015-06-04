package io.vertx.ext.web.templ;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.TemplateHandler;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;

public abstract class GroovyTemplateHandlerTestBase {

    protected final static String HOST = "localhost";
    protected final static Integer PORT = 7777;

    protected Vertx vertx;
    protected Router router;
    protected groovy.text.TemplateEngine groovyEngine;
    protected Map<String, Object> testData;

    @Before
    public void setUp(TestContext context) {
        Async async = context.async();
        testData = new HashMap<String, Object>();
        groovyEngine = createTemplateEngine();
        vertx = Vertx.vertx();
        HttpServerOptions options = new HttpServerOptions();
        options.setHost(HOST);
        options.setPort(PORT);
        HttpServer server = vertx.createHttpServer(options);
        router = Router.router(vertx);
        router.route().handler(routingContext -> {
            if (testData != null && !testData.isEmpty()) {
                testData.forEach((key, value) -> {
                    routingContext.put(key, value);
                });
            }
            routingContext.next();
        });
        router.route().handler(TemplateHandler.create(GroovyTemplateEngine.create(groovyEngine)));
        server.requestHandler(router::accept);
        server.listen(res -> {
            /* FIXME : replace with context.assertAsyncSuccess() */
            if (res.failed()) {
                context.fail(res.cause());
            } else {
                async.complete();
            }
        });
    }

    @After
    public void tearDown(TestContext context) {
        Async async = context.async();
        if (vertx != null) {
            groovyEngine = null;
            /* FIXME : replace with context.assertAsyncSuccess() */
            vertx.close(res -> {
                vertx = null;
                if (res.failed()) {
                    context.fail(res.cause());
                } else {
                    async.complete();
                }
            });
        }
    }

    protected HttpClient client() {
        HttpClientOptions options = new HttpClientOptions();
        options.setDefaultHost(HOST);
        options.setDefaultPort(PORT);
        return vertx.createHttpClient(options);
    }

    abstract protected groovy.text.TemplateEngine createTemplateEngine();
}
