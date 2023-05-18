# Git commit template Plugin for IntelliJ
<!-- Plugin description -->
This plugin allows to create a commit message with the following template:

```
<type>(<scope>): <subject>
<BLANK LINE>
<body>
<BLANK LINE>
<footer>
<BLANK LINE>
<jira id>
```

<!-- Plugin description end -->

From the contribution guidelines of the Angular project [here](https://github.com/angular/angular.js/blob/master/CONTRIBUTING.md#commit-message-format).

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

## Usage

![Commit-step1](static/commit-template-1.png)

![Commit-step2](static/commit-template-2.png)

![Commit-step3](static/commit-template-3.png)




## License

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

## Original Work

This plugin is based off the [Git Commit Template](https://github.com/MobileTribe/commit-template-idea-plugin) plugin.

### Changes made:

* Refactored to use standard Gradle project structure
* Added option in commit dialog to automatically detect Jira ID(s) from the branch name, or to use fixed Jira IDs