package com.kopyl.commit;

import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

import java.net.URI;

public class JiraClient {
    private final String token;
    private final String jiraUrl;
    private final JiraRestClient restClient;

    public JiraClient(String token, String jiraUrl) {
        this.token = token;
        this.jiraUrl = jiraUrl;
        this.restClient = getJiraRestClient();
    }

    private JiraRestClient getJiraRestClient() {
        BearerHttpAuthenticationHandler handler = new BearerHttpAuthenticationHandler(this.token);
        JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
        return factory.create(getJiraUri(), handler);
    }

    private URI getJiraUri() {
        return URI.create(this.jiraUrl);
    }

    public Issue getIssue(String issueKey) {
        return restClient.getIssueClient()
            .getIssue(issueKey)
            .claim();
    }
}
