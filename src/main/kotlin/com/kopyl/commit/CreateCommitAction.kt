package com.kopyl.commit

import com.intellij.dvcs.repo.RepositoryManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.CommitMessageI
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.ui.Refreshable
import com.kopyl.commit.configuration.AppSettingsState.Companion.instance
import git4idea.GitUtil
import git4idea.repo.GitRepository

class CreateCommitAction : AnAction() {
    override fun actionPerformed(actionEvent: AnActionEvent) {
        val commitPanel = getCommitPanel(actionEvent) ?: return

        val personalAccessToken = instance.personalAccessToken
        val jiraUrl = instance.jiraUrl
        if (jiraUrl == "" || personalAccessToken == "") {
            val errorMessage = "Provide a Jira URL and personal access token in the Settings."
            ErrorNotification.show(errorMessage)
            return
        }
        val jiraClient = JiraClient(personalAccessToken, jiraUrl)
        try {
            val project = actionEvent.project
            if (project == null) {
                val errorMessage = "No project open."
                ErrorNotification.show(errorMessage)
                return
            }

            val jiraId = getJiraIdFromBranchName(project)
            val issue = jiraClient.getIssue(jiraId)
            val issueValidator = IssueValidator(issue)
            if (!issueValidator.valid()) {
                val errorMessage = "Issue $jiraId is not valid"
                ErrorNotification.show(errorMessage)
                return
            }
            val commitMessage = CommitMessage(issue)
            commitPanel.setCommitMessage(commitMessage.toString())
        } catch (e: Exception) {
            val errorMessage = "Could not connect to Jira or issue does not exist. Check if $jiraUrl is available and the access token is valid."
            ErrorNotification.show(errorMessage)
        }
    }

    private fun getJiraIdFromBranchName(project: Project): String {
        val repositoryManager = GitUtil.getRepositoryManager(project)
        val branch = repositoryManager.repositories[0].currentBranch ?: return ""
        return branch.name
    }

    companion object {
        private fun getCommitPanel(actionEvent: AnActionEvent?): CommitMessageI? {
            if (actionEvent == null) {
                return null
            }
            val data = Refreshable.PANEL_KEY.getData(actionEvent.dataContext)

            return if (data is CommitMessageI) {
                data
            } else VcsDataKeys.COMMIT_MESSAGE_CONTROL.getData(actionEvent.dataContext)
        }
    }
}
