package esa.mylibrary.utils

import android.content.Context


object Reflect {

    private fun getResourceId(context: Context, name: String, type: String): Int {
        var id = 0
        id = context.resources.getIdentifier(name, type, context.packageName)
        return id
    }

    fun getViewId(context: Context, name: String): Int {
        return getResourceId(context, name, "id")
    }

    fun getLayoutId(context: Context, name: String): Int {
        return getResourceId(context, name, "layout")
    }

    fun getStringId(context: Context, name: String): Int {
        return getResourceId(context, name, "string")
    }

    fun getDrawableId(context: Context, name: String): Int {
        return getResourceId(context, name, "drawable")
    }

    fun getStyleId(context: Context, name: String): Int {
        return getResourceId(context, name, "style")
    }

    fun getDimenId(context: Context, name: String): Int {
        return getResourceId(context, name, "dimen")
    }

    fun getArrayId(context: Context, name: String): Int {
        return getResourceId(context, name, "array")
    }

    fun getColorId(context: Context, name: String): Int {
        return getResourceId(context, name, "color")
    }

    fun getAnimId(context: Context, name: String): Int {
        return getResourceId(context, name, "anim")
    }

    fun isClassFounded(className: String?): Boolean {
        return try {
            val aClass = Class.forName(className)
            true
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            false
        }
    }

    fun getObjectByClassName(className: String?): Any? {
        try {
            val aClass = Class.forName(className)
            return aClass.newInstance()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return null
    }
}