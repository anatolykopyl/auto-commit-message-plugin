# Auto commit message Plugin for IntelliJ

<!-- Plugin description -->
This plugin assumes that your branch name is a Jira task ID. 

It adds a button to create a conventional commit message automatically using 
the branch name and connecting to the Jira api.

The commit message is using the Selectel conventional commit flavour: 
<pre>type(component_from_Jira): subject TASK_ID</pre>

For example:
<pre>feat(common): Did some stuff TASK-123</pre>

Configure the plugin in <b>Settings > Tools > Auto Commit Message</b>.

<!-- Plugin description end -->

## Installation

1. Obtain the plugin zip file (either from a known source, or by following the steps in [Release](#release))
2. From a Jetbrains IDE, go to Settings -> Plugins
3. Click the options button
![Install-local-plugin-button](static/install-local-plugin-button.png)

4. Navigate to the folder containing the plugin zip file
5. Select the zip file, and press "Open"
6. Press "save" to install the plugin

## Release

* Ensure the project is set up properly and runs in development.
* Run the `buildPlugin` gradle task
  * From IntelliJ IDEA, open the Gradle tool window, navigate to Tasks -> intellij -> double click on `buildPlugin`
* The zip file will be generated in `build/distributions`

## Original Work

This plugin is based off the [Git Commit Template With Jira Id](https://bitbucket.org/crm-uk/commit-template-with-jira-id) plugin.
