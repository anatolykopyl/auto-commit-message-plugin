package com.kopyl.commit.configuration;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Supports creating and managing a {@link JPanel} for the Settings Dialog.
 */
public class AppSettingsComponent {

    private final JPanel mainPanel;
    private final JBTextField personalAccessToken = new JBTextField();
    private final JBTextField jiraUrl = new JBTextField();

    public AppSettingsComponent() {
        mainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Enter personal access token: "), personalAccessToken, 1, false)
                .addLabeledComponent(new JBLabel("Enter Jira url: "), jiraUrl, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return personalAccessToken;
    }

    @NotNull
    public String getPersonalAccessToken() {
        return personalAccessToken.getText();
    }

    public void setPersonalAccessToken(@NotNull String newText) {
        personalAccessToken.setText(newText);
    }

    public String getJiraUrl() {
        return jiraUrl.getText();
    }

    public void setJiraUrl(@NotNull String newText) {
        jiraUrl.setText(newText);
    }

}