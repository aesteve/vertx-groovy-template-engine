package io.vertx.groovy.ext.web.templ.impl

import groovy.text.GStringTemplateEngine
import groovy.text.Template
import groovy.transform.TypeChecked
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.impl.Utils
import io.vertx.ext.web.templ.impl.CachingTemplateEngine
import io.vertx.groovy.core.buffer.Buffer
import io.vertx.groovy.ext.web.RoutingContext
import io.vertx.groovy.ext.web.templ.GroovyTemplateEngine

@TypeChecked
class GStringTemplateEngineImpl extends CachingTemplateEngine<Template> implements GroovyTemplateEngine {

	GStringTemplateEngine engine

	public GStringTemplateEngineImpl() {
		this.engine = new GStringTemplateEngine(Utils.getClassLoader())
	}

	public void render(RoutingContext context, String templateFileName, Handler<AsyncResult<Buffer>> handler) {
		
	}
}
