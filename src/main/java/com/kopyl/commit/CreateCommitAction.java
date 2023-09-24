package com.kopyl.commit;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.intellij.dvcs.repo.RepositoryManager;
import com.intellij.openapi.project.Project;
import com.kopyl.commit.configuration.AppSettingsState;
import git4idea.GitLocalBranch;
import git4idea.GitUtil;
import git4idea.repo.GitRepository;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vcs.CommitMessageI;
import com.intellij.openapi.vcs.VcsDataKeys;
import com.intellij.openapi.vcs.ui.Refreshable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CreateCommitAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent actionEvent) {
        CommitMessageI commitPanel = getCommitPanel(actionEvent);
        if (commitPanel == null) return;

        String personalAccessToken = AppSettingsState.getInstance().personalAccessToken;
        String jiraUrl = AppSettingsState.getInstance().jiraUrl;

        if (Objects.equals(jiraUrl, "") || Objects.equals(personalAccessToken, "")) {
            String errorMessage = "Provide a Jira URL and personal access token in the Settings.";
            ErrorNotification.show(errorMessage);
            return;
        }

        JiraClient jiraClient = new JiraClient(personalAccessToken, jiraUrl);

        try {
            String jiraId = getJiraIdFromBranchName(actionEvent.getProject());
            Issue issue = jiraClient.getIssue(jiraId);

            IssueValidator issueValidator = new IssueValidator(issue);
            if (!issueValidator.valid()) {
                String errorMessage = String.format("Issue %s is not valid", jiraId);
                ErrorNotification.show(errorMessage);
                return;
            }

            CommitMessage commitMessage = new CommitMessage(issue);

            commitPanel.setCommitMessage(commitMessage.toString());
        } catch (Exception e) {
            String errorMessage = String.format("Could not connect to Jira or issue does not exist. Check if %s is available and the access token is valid.", jiraUrl);
            ErrorNotification.show(errorMessage);
        }
    }

    @Nullable
    private static CommitMessageI getCommitPanel(@Nullable AnActionEvent e) {
        if (e == null) {
            return null;
        }
        Refreshable data = Refreshable.PANEL_KEY.getData(e.getDataContext());
        if (data instanceof CommitMessageI) {
            return (CommitMessageI) data;
        }
        return VcsDataKeys.COMMIT_MESSAGE_CONTROL.getData(e.getDataContext());
    }

    private String getJiraIdFromBranchName(Project project) {
        RepositoryManager<GitRepository> repositoryManager = GitUtil.getRepositoryManager(project);
        GitLocalBranch branch = repositoryManager.getRepositories().get(0).getCurrentBranch();

        if (branch == null) return "";

        return branch.getName();
    }
}
