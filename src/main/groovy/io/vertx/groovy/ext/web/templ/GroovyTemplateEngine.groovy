package io.vertx.groovy.ext.web.templ;

import groovy.text.GStringTemplateEngine
import io.vertx.groovy.ext.web.templ.impl.GStringTemplateEngineImpl

trait GroovyTemplateEngine extends TemplateEngine {

	private static Map<Class<? extends groovy.text.TemplateEngine>, Class<? extends GroovyTemplateEngine>> impls;
	static {
		impls[GStringTemplateEngine.class] = GStringTemplateEngineImpl.class
	}

	static TemplateEngine create(groovy.text.TemplateEngine engine) {
		Class<? extends GroovyTemplateEngine> impl = impls[engine.getClass()]
		if (!impl) {
			return null
		}
		impl.newInstance(engine)
	}

	static TemplateEngine create(Class<? extends groovy.text.TemplateEngine> clazz) {
		Class<? extends GroovyTemplateEngine> impl = impls[clazz]
		if (!impl) {
			return null
		}
		impl.newInstance()
	}
}
