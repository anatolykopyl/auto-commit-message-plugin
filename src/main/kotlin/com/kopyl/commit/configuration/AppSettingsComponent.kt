package com.kopyl.commit.configuration

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Supports creating and managing a [JPanel] for the Settings Dialog.
 */
class AppSettingsComponent {
    val panel: JPanel
    private val personalAccessToken = JBTextField()
    private val jiraUrl = JBTextField()

    init {
        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Enter personal access token: "), personalAccessToken, 1, false)
            .addLabeledComponent(JBLabel("Enter Jira url: "), jiraUrl, 1, false)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    val preferredFocusedComponent: JComponent
        get() = personalAccessToken

    fun getPersonalAccessToken(): String {
        return personalAccessToken.getText()
    }

    fun setPersonalAccessToken(newText: String) {
        personalAccessToken.setText(newText)
    }

    fun getJiraUrl(): String {
        return jiraUrl.getText()
    }

    fun setJiraUrl(newText: String) {
        jiraUrl.setText(newText)
    }
}