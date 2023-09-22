package com.kopyl.commit.configuration;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

/**
 * Provides controller functionality for application settings.
 */
public class AppSettingsConfigurable implements Configurable {

    private AppSettingsComponent mySettingsComponent;

    // A default constructor with no arguments is required because this implementation
    // is registered in an applicationConfigurable EP

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Auto Commit Message Settings";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return mySettingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mySettingsComponent = new AppSettingsComponent();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        AppSettingsState settings = AppSettingsState.getInstance();
        boolean modified = !mySettingsComponent.getPersonalAccessToken().equals(settings.personalAccessToken);
        modified |= !Objects.equals(mySettingsComponent.getJiraUrl(), settings.jiraUrl);
        return modified;
    }

    @Override
    public void apply() {
        AppSettingsState settings = AppSettingsState.getInstance();
        settings.personalAccessToken = mySettingsComponent.getPersonalAccessToken();
        settings.jiraUrl = mySettingsComponent.getJiraUrl();
    }

    @Override
    public void reset() {
        AppSettingsState settings = AppSettingsState.getInstance();
        mySettingsComponent.setPersonalAccessToken(settings.personalAccessToken);
        mySettingsComponent.setJiraUrl(settings.jiraUrl);
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }

}