package com.kopyl.commit;

import com.atlassian.jira.rest.client.api.domain.Issue;

public class IssueValidator {
    private final Issue issue;

    public IssueValidator(Issue issue) {
        this.issue = issue;
    }

    public Boolean valid() {
        if (this.issue.getSummary() == null || this.issue.getSummary().isEmpty()) {
            return false;
        }

        if (this.issue.getComponents() == null || this.issue.getComponents().iterator().next() == null) {
            return false;
        }

        return true;
    }
}
