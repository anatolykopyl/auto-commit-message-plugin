package com.kopyl.commit

import com.atlassian.jira.rest.client.api.domain.Issue
import org.apache.commons.lang3.StringUtils

class CommitMessage(issue: Issue) {
    private var changeType = ChangeType.FEAT
    private var changeScope: String?
    private var shortDescription: String?
    private var jiraId: String?

    init {
        if (issue.issueType.name == "Bug") {
            changeType = ChangeType.FIX
        }
        changeScope = issue.components.iterator().next().name
        shortDescription = issue.summary
        jiraId = issue.key
    }

    override fun toString(): String {
        val type = changeType.label()
        return "$type($changeScope): $shortDescription $jiraId"
    }
}
