package io.vertx.ext.web.templ.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import groovy.text.TemplateEngine;
import groovy.text.markup.MarkupTemplateEngine;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.templ.GroovyTemplateHandlerTestBase;

@RunWith(VertxUnitRunner.class)
public class MarkupTemplateEngineTest extends GroovyTemplateHandlerTestBase {

    @Override
    protected TemplateEngine createTemplateEngine() {
        return new MarkupTemplateEngine();
    }

    @Test
    public void testWithContext(TestContext context) {
        Async async = context.async();
        String firstName = "Snoopy";
        testData.put("firstName", firstName);
        client().getNow("/markup/withcontext.gtpl", response -> {
            assertEquals(200, response.statusCode());
            response.bodyHandler(buff -> {
                assertEquals("<html><body>" + firstName.toUpperCase() + "</body></html>", buff.toString("UTF-8"));
                async.complete();
            });
        });
    }

}
