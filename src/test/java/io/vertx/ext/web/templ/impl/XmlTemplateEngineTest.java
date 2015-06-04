package io.vertx.ext.web.templ.impl;

import static org.junit.Assert.assertEquals;
import groovy.text.TemplateEngine;
import groovy.text.XmlTemplateEngine;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.templ.GroovyTemplateHandlerTestBase;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class XmlTemplateEngineTest extends GroovyTemplateHandlerTestBase {

    @Override
    protected TemplateEngine createTemplateEngine() {
        try {
            return new XmlTemplateEngine();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Example adapted from : http://docs.groovy-lang.org/latest/html/api/groovy/text/XmlTemplateEngine.html
     */
    @Test
    public void testTplWithContext(TestContext context) {
        Async async = context.async();
        String firstname = "Charlie";
        String nickname = "Chuck";
        String lastname = "Brown";
        String salutation = "Dear";
        testData.put("firstname", firstname);
        testData.put("lastname", lastname);
        testData.put("nickname", nickname);
        testData.put("salutation", salutation);
        String expected = "<document type='letter'>\n  " + salutation + "est\n" + "  <foo:to xmlns:foo='baz'>\n    " + firstname + " &quot;" + nickname + "&quot; " + lastname + "\n  </foo:to>\n  " + "How are you today?\n" + "</document>\n";
        client().getNow("/xml/withcontext.gtpl", response -> {
            assertEquals(200, response.statusCode());
            response.bodyHandler(buff -> {
                assertEquals(expected, buff.toString("UTF-8"));
                async.complete();
            });
        });
    }

}
