package io.vertx.ext.web.templ.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import groovy.text.SimpleTemplateEngine;
import groovy.text.TemplateEngine;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.templ.GroovyTemplateHandlerTestBase;

@RunWith(VertxUnitRunner.class)
public class SimpleTemplateEngineTest extends GroovyTemplateHandlerTestBase {

    @Override
    protected TemplateEngine createTemplateEngine() {
        return new SimpleTemplateEngine();
    }

    @Test
    public void testTplWithContext(TestContext context) {
        Async async = context.async();
        String firstName = "Snoopy";
        testData.put("firstName", firstName);
        client().getNow("/simple/withcontext.gtpl", response -> {
            assertEquals(200, response.statusCode());
            response.bodyHandler(buff -> {
                assertEquals("Hello, " + firstName, buff.toString("UTF-8"));
                async.complete();
            });
        });
    }

}
