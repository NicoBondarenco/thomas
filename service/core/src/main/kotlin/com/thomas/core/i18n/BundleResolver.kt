package com.thomas.core.i18n

import com.thomas.core.context.SessionContextHolder.currentLocale
import com.thomas.core.extension.formattedMessage
import java.util.Locale
import java.util.ResourceBundle
import java.util.concurrent.ConcurrentHashMap

abstract class BundleResolver(
    private val bundleName: String
) {

    private val resources = ConcurrentHashMap<String, ResourceBundle>()

    init {
        Locale.getAvailableLocales().forEach { locale ->
            ResourceBundle.getBundle(bundleName, locale)
                .takeIf { it.locale.equals(locale) }
                ?.apply { resources[locale.toLanguageTag()] = this }
        }
    }

    protected fun bundle(): ResourceBundle = resources
        .getOrDefault(
            currentLocale.toLanguageTag(),
            resources[Locale.ROOT.toLanguageTag()]!!
        )

    fun getFormattedMessage(key: String, vararg arguments: Any) = bundle().formattedMessage(key, *arguments)

}
