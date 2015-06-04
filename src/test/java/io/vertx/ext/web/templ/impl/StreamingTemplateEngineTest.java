package io.vertx.ext.web.templ.impl;

import static org.junit.Assert.assertEquals;
import groovy.text.StreamingTemplateEngine;
import groovy.text.TemplateEngine;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.templ.GroovyTemplateHandlerTestBase;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class StreamingTemplateEngineTest extends GroovyTemplateHandlerTestBase {

    @Override
    protected TemplateEngine createTemplateEngine() {
        return new StreamingTemplateEngine();
    }

    @Test
    public void testTplWithContext(TestContext context) {
        Async async = context.async();
        String firstName = "Snoopy";
        testData.put("firstName", firstName);
        client().getNow("/streaming/withcontext.gtpl", response -> {
            assertEquals(200, response.statusCode());
            response.bodyHandler(buff -> {
                assertEquals("Hello, " + firstName, buff.toString("UTF-8"));
                async.complete();
            });
        });
    }

}
