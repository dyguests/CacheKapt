package com.fanhl.cachekapt.processor

import com.fanhl.cachekapt.annotation.Cache
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

/**
 * 注解实现
 */
class CacheProcessor : AbstractProcessor() {
    override fun process(set: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        return true
    }

    override fun getSupportedAnnotationTypes() = mutableSetOf(Cache::class.java.canonicalName)
}
