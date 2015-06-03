package io.vertx.groovy.ext.web.templ.impl

import groovy.text.GStringTemplateEngine
import groovy.text.Template
import groovy.transform.TypeChecked
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.impl.Utils
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.templ.impl.CachingTemplateEngine
import io.vertx.groovy.ext.web.templ.GroovyTemplateEngine
import io.vertx.groovy.ext.web.templ.TemplateEngine

@TypeChecked
class GStringTemplateEngineImpl extends CachingTemplateEngine<Template>, TemplateEngine {

	GStringTemplateEngine engine

	public GStringTemplateEngineImpl() {
        super(GroovyTemplateEngine.DEFAULT_TEMPLATE_EXTENSION, GroovyTemplateEngine.DEFAULT_MAX_CACHE_SIZE)
        this.engine = new GStringTemplateEngine(Utils.getClassLoader())
    }

    @Override
    void render(RoutingContext context, String templateFileName, Handler<AsyncResult<Buffer>> handler) {
        Template template = cache.get(templateFileName)
        if (template) {
            renderTemplate(context, template, handler)
        } else {
            Vertx vertx = context.vertx()
            String location = adjustLocation(templateFileName)
            vertx.fileSystem().readFile(location, { result ->
                if (result.failed()) {
                    context.fail(result.cause())
                } else {
                    Buffer buff = result.result()
                    template = engine.createTemplate(buff.toString("UTF-8"))
                    cache.put(templateFileName, template)
                    renderTemplate(context, template, handler)
                }
            })
        }
    }

    @Override
    void render(io.vertx.groovy.ext.web.RoutingContext context, String templateFileName, Handler<AsyncResult<io.vertx.groovy.core.buffer.Buffer>> handler) {
        render(context.delegate as RoutingContext, templateFileName, { result ->
            if (result.succeeded()) {
                Buffer buff = result.result()
                io.vertx.groovy.core.buffer.Buffer groovyBuffer = new io.vertx.groovy.core.buffer.Buffer(buff)
                handler.handle(Future.succeededFuture(groovyBuffer))
            } else {
                handler.handle(Future.failedFuture(result.cause()))
            }
        })
    }

    private
    static void renderTemplate(RoutingContext context, Template template, Handler<AsyncResult<Buffer>> handler) {
        try {
            String rendered = template.make(context.data())
            handler.handle(Future.succeededFuture(Buffer.buffer(rendered)))
        } catch (all) {
            handler.handle(Future.failedFuture(all))
        }
    }
}
