/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.github;

import org.apache.camel.EndpointInject;
import org.apache.camel.component.github.consumer.MockCommitService;
import org.apache.camel.component.github.consumer.MockIssueService;
import org.apache.camel.component.github.consumer.MockPullRequestService;
import org.apache.camel.component.github.consumer.MockRepositoryService;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public abstract class GitHubComponentTestBase extends CamelTestSupport {
    public static final String USERNAME = "someguy";
    public static final String PASSWORD = "apassword";
    public static final String REPO_OWNER = "anotherguy";
    public static final String REPO_NAME = "somerepo";
    public static final String GITHUB_CREDENTIALS_STRING = "username=" + USERNAME + "&password=" + PASSWORD + "&repoOwner=" + REPO_OWNER + "&repoName=" + REPO_NAME;

    @Rule
    public TestName testName = new TestName();

    protected MockCommitService commitService;
    protected MockRepositoryService repositoryService;
    protected MockPullRequestService pullRequestService;
    protected MockIssueService issueService;

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint mockResultEndpoint;

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry registry = super.createRegistry();
        commitService = new MockCommitService();
        registry.bind("githubCommitService", commitService);

        repositoryService = new MockRepositoryService();
        registry.bind("githubRepositoryService", repositoryService);

        pullRequestService = new MockPullRequestService();
        registry.bind("githubPullRequestService", pullRequestService);

        issueService = new MockIssueService(pullRequestService);
        registry.bind("githbIssueService", issueService);

        return registry;
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        log.debug("Starting test " + testName.getMethodName());
    }

    @Test
    public void emptyAtStartupTest() throws Exception {
        mockResultEndpoint.expectedMessageCount(0);
        mockResultEndpoint.assertIsSatisfied();
    }

}
