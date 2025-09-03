package ru.freestyle.websync.exstension

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


@Suppress("UNCHECKED_CAST")
fun <V> Any.genericClass(index: Int = 0): Class<V> {
    val clazz: Class<Any> = this.javaClass
    val type: Type = clazz.genericSuperclass?.takeIf { it != Any::class.java } ?: clazz.genericInterfaces.first()

    return (type as ParameterizedType).actualTypeArguments[index] as Class<V>
}
