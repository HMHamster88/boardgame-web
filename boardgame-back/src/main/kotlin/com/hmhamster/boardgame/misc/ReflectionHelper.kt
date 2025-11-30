package com.hmhamster.boardgame.misc

import jakarta.el.MethodNotFoundException
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.functions

object ReflectionHelper {

    inline fun <reified T> invokeMethod(target: T, methodName: String, vararg params: Any) {
        var serviceClass = T::class
        val methodToInvoke: KFunction<*>? = serviceClass.functions.find { function ->
            checkMethod(function, methodName, target, *params)
        }
        if (methodToInvoke != null) {
            try {
                methodToInvoke.call(target, *params)
            } catch (ex: InvocationTargetException) {
                throw ex.cause!!
            }
        } else {
            throw MethodNotFoundException("Method $methodName not found")
        }
    }

    inline fun <reified T> checkMethod(
        function: KFunction<*>,
        methodName: String,
        target: T,
        vararg params: Any
    ): Boolean {
        if (function.name != methodName) {
            return false
        }
        if (function.parameters.size != params.size + 1) {
            return false
        }
        val allParams = arrayOf(target, *params)
        return function.parameters.zip(allParams).all { pair ->
            checkParamTypes(pair.first, pair.second)
        }
    }

    fun checkParamTypes(param1: KParameter, param2: Any?): Boolean {
        if (param1.type.isMarkedNullable && param2 == null) {
            return true
        }
        return param1.type.classifier == param2!!::class
    }
}