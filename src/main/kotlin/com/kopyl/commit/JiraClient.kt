package com.kopyl.commit

import com.atlassian.jira.rest.client.api.JiraRestClient
import com.atlassian.jira.rest.client.api.JiraRestClientFactory
import com.atlassian.jira.rest.client.api.domain.Issue
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory
import java.net.URI

class JiraClient(private val token: String, private val jiraUrl: String) {
    private val restClient: JiraRestClient

    init {
        restClient = jiraRestClient
    }

    private val jiraRestClient: JiraRestClient
        private get() {
            val handler = BearerHttpAuthenticationHandler(token)
            val factory: JiraRestClientFactory = AsynchronousJiraRestClientFactory()
            return factory.create(jiraUri, handler)
        }
    private val jiraUri: URI
        private get() = URI.create(jiraUrl)

    fun getIssue(issueKey: String?): Issue {
        return restClient.issueClient
                .getIssue(issueKey)
                .claim()
    }
}
