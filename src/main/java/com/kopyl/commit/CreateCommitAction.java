package com.kopyl.commit;

import com.intellij.dvcs.repo.RepositoryManager;
import com.intellij.openapi.project.Project;
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
import com.atlassian.jira.rest.client.api.domain.Issue;

public class CreateCommitAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent actionEvent) {
        CommitMessageI commitPanel = getCommitPanel(actionEvent);
        if (commitPanel == null) return;

        String jiraId = getJiraIdFromBranchName(actionEvent.getProject());

        JiraClient jiraClient = new JiraClient("key", "url");
        Issue issue = jiraClient.getIssue(jiraId);

        CommitMessage commitMessage = new CommitMessage(issue);

        commitPanel.setCommitMessage(commitMessage.toString());
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
