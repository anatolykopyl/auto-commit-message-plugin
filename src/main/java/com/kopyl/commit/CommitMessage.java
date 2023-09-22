package com.kopyl.commit;

import com.atlassian.jira.rest.client.api.domain.Issue;
import java.util.Objects;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class CommitMessage {
    private ChangeType changeType = ChangeType.FEAT;
    private String changeScope = "";
    private String shortDescription = "";
    private String jiraId = null;

    public CommitMessage(Issue issue) {
        if (Objects.equals(issue.getIssueType().getName(), "Bug")) {
            this.changeType = ChangeType.FIX;
        }

        this.changeScope = issue.getComponents().iterator().next().getName();
        this.shortDescription = issue.getSummary();
        this.jiraId = issue.getKey();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(changeType.label());

        if (isNotBlank(changeScope)) {
            builder.append('(').append(changeScope).append(')');
        }
        builder.append(": ").append(shortDescription);

        if (isNotBlank(jiraId)) {
            builder.append(' ').append(jiraId);
        }

        return builder.toString();
    }
}
