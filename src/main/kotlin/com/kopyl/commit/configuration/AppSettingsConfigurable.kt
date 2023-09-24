package com.kopyl.commit.configuration

import com.intellij.openapi.options.Configurable
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

/**
 * Provides controller functionality for application settings.
 */
class AppSettingsConfigurable : Configurable {
    private var mySettingsComponent: AppSettingsComponent? = null

    // A default constructor with no arguments is required because this implementation
    // is registered in an applicationConfigurable EP
    override fun getDisplayName(): @Nls(capitalization = Nls.Capitalization.Title) String {
        return "Auto Commit Message Settings"
    }

    override fun getPreferredFocusedComponent(): JComponent {
        return mySettingsComponent!!.preferredFocusedComponent
    }

    override fun createComponent(): JComponent {
        mySettingsComponent = AppSettingsComponent()
        return mySettingsComponent!!.panel
    }

    override fun isModified(): Boolean {
        val settings = AppSettingsState.instance
        var modified = mySettingsComponent!!.getPersonalAccessToken() != settings.personalAccessToken
        modified = modified or (mySettingsComponent!!.getJiraUrl() != settings.jiraUrl)
        return modified
    }

    override fun apply() {
        val settings = AppSettingsState.instance
        settings.personalAccessToken = mySettingsComponent!!.getPersonalAccessToken()
        settings.jiraUrl = mySettingsComponent!!.getJiraUrl()
    }

    override fun reset() {
        val settings = AppSettingsState.instance
        mySettingsComponent!!.setPersonalAccessToken(settings.personalAccessToken)
        mySettingsComponent!!.setJiraUrl(settings.jiraUrl)
    }

    override fun disposeUIResources() {
        mySettingsComponent = null
    }
}