package com.kopyl.commit

import com.atlassian.jira.rest.client.api.domain.Issue

class IssueValidator(private val issue: Issue) {
    fun valid(): Boolean {
        // TODO return error message as well, eg: {valid: false, message: "Components missing"}
        if (issue.summary == null || issue.summary.isEmpty()) {
            return false
        }
        if (issue.components == null || issue.components.iterator().next() == null) {
            return false
        }

        return true
    }
}
