package io.vertx.groovy.ext.web.templ

import groovy.text.GStringTemplateEngine
import io.vertx.groovy.ext.web.templ.impl.GStringTemplateEngineImpl

class GroovyTemplateEngine {

    public static String DEFAULT_TEMPLATE_EXTENSION = "groovy"
    public static int DEFAULT_MAX_CACHE_SIZE = 10000

    private static Map<Class<? extends groovy.text.TemplateEngine>, Class<? extends TemplateEngine>> impls
    static {
        impls[GStringTemplateEngine.class] = GStringTemplateEngineImpl.class
    }

    static TemplateEngine create(groovy.text.TemplateEngine engine) {
        Class<? extends TemplateEngine> impl = impls[engine.getClass() as Class<? extends groovy.text.TemplateEngine>]
        if (!impl) {
            return null
        }
        impl.newInstance()
    }

    static TemplateEngine create(Class<? extends groovy.text.TemplateEngine> clazz) {
        Class<? extends TemplateEngine> impl = impls[clazz]
        if (!impl) {
            return null
        }
        impl.newInstance()
    }
}
