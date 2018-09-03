package com.fanhl.cachekapt.annotation

/**
 * 对使用了此注释的变量进行缓存
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
annotation class Cache(val scope: Scope = Scope.PROJECT, vararg val conditions: String = [])
