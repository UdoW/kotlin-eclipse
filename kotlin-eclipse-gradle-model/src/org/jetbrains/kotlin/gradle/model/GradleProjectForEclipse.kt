package org.jetbrains.kotlin.gradle.model

import java.io.Serializable

// This interface is used to communicate with gradle deamon via proxy, so it has to have all methods that are expected
// in its subclasses.
interface GradleProjectForEclipse : Serializable {
    val isKotlinProject: Boolean

    val codestyle: String?
        get() = null

    val apiVersion: String?
        get() = null

    val languageVersion: String?
        get() = null

    val jvmTarget: String?
        get() = null
    
    val compilerPlugins: List<CompilerPluginConfig>
    	get() = emptyList()
}

data class KotlinGradleProject(
    override val codestyle: String?,
    override val apiVersion: String?,
    override val languageVersion: String?,
    override val jvmTarget: String?,
    override val compilerPlugins: List<CompilerPluginConfig>
) : GradleProjectForEclipse {
    override val isKotlinProject: Boolean = true
}

object NonKotlinProject : GradleProjectForEclipse {
    override val isKotlinProject: Boolean = false
}