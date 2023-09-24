package com.kopyl.commit

import com.atlassian.httpclient.api.Request
import com.atlassian.jira.rest.client.api.AuthenticationHandler

class BearerHttpAuthenticationHandler(private val token: String) : AuthenticationHandler {
    override fun configure(builder: Request.Builder) {
        builder.setHeader(AUTHORIZATION_HEADER, "Bearer $token")
    }

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
    }
}