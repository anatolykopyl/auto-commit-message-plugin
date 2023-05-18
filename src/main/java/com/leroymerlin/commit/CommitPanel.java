package com.leroymerlin.commit;

import com.intellij.dvcs.repo.RepositoryManager;
import com.intellij.openapi.project.Project;
import git4idea.GitLocalBranch;
import git4idea.GitUtil;
import git4idea.repo.GitRepository;

import javax.swing.*;
import java.io.File;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Damien Arrachequesne
 */
public class CommitPanel {
    public static final Pattern JIRA_ID_FORMAT = Pattern.compile("(\\w+[_-]\\d+)");

    private JPanel mainPanel;
    private JComboBox<String> changeScope;
    private JTextField shortDescription;
    private JTextArea longDescription;
    private JTextArea breakingChanges;
    private JTextField closedIssues;
    private JCheckBox wrapTextCheckBox;
    private JCheckBox skipCICheckBox;
    private JRadioButton featRadioButton;
    private JRadioButton fixRadioButton;
    private JRadioButton docsRadioButton;
    private JRadioButton styleRadioButton;
    private JRadioButton refactorRadioButton;
    private JRadioButton perfRadioButton;
    private JRadioButton testRadioButton;
    private JRadioButton buildRadioButton;
    private JRadioButton ciRadioButton;
    private JRadioButton choreRadioButton;
    private JRadioButton revertRadioButton;
    private JComboBox<JiraIdMode> jiraIdModeCombobox;
    private JTextField jiraIdTextField;
    private ButtonGroup changeTypeGroup;

    private final Project project;

    CommitPanel(Project project, CommitMessage commitMessage) {
        this.project = project;

        File workingDirectory = new File(project.getBasePath());
        GitLogQuery.Result result = new GitLogQuery(workingDirectory).execute();
        if (result.isSuccess()) {
            if (changeScope != null) {
                changeScope.addItem(""); // no value by default
                result.getScopes().forEach(changeScope::addItem);
            }
        }

        jiraIdModeCombobox.setModel(new DefaultComboBoxModel<>(new JiraIdMode[] {
                JiraIdMode.AUTODETECT,
                JiraIdMode.FIXED,
                JiraIdMode.NONE
        }));

        //Perform actions when the chosen ID mode changes
        jiraIdModeCombobox.addActionListener(e -> {
            Object oSource = e.getSource();
            JComboBox<JiraIdMode> source = (oSource instanceof JComboBox) ? (JComboBox<JiraIdMode>) oSource : null;
            if (source == null) return;

            //Get the chosen ID mode
            JiraIdMode jiraIdMode = (JiraIdMode) source.getSelectedItem();

            //Only enable the ID text field if the ID mode is "fixed"
            jiraIdTextField.setEnabled(jiraIdMode == JiraIdMode.FIXED);

            if (jiraIdMode == JiraIdMode.AUTODETECT) {
                String jiraIdText = getJiraIdFromBranchName();
                jiraIdTextField.setText(jiraIdText);
            } else if (jiraIdMode == JiraIdMode.NONE) {
                jiraIdTextField.setText("");
            }
        });

        if (commitMessage != null) {
            restoreValuesFromParsedCommitMessage(commitMessage);
        }
    }

    JPanel getMainPanel() {
        return mainPanel;
    }

    CommitMessage getCommitMessage() {
        JiraIdMode jiraIdMode = (JiraIdMode) jiraIdModeCombobox.getSelectedItem();
        String jiraIdText;
        if (jiraIdMode == JiraIdMode.NONE) {
            jiraIdText = null;
        } else {
            jiraIdText = jiraIdTextField.getText();
        }

        return new CommitMessage(
                getSelectedChangeType(),
                (String) changeScope.getSelectedItem(),
                shortDescription.getText().trim(),
                longDescription.getText().trim(),
                breakingChanges.getText().trim(),
                closedIssues.getText().trim(),
                wrapTextCheckBox.isSelected(),
                skipCICheckBox.isSelected(),
                jiraIdMode,
                jiraIdText
        );
    }

    private String getJiraIdFromBranchName() {
        RepositoryManager<GitRepository> repositoryManager = GitUtil.getRepositoryManager(project);
        GitLocalBranch branch = repositoryManager.getRepositories().get(0).getCurrentBranch();

        StringBuilder jiraIdBuilder = new StringBuilder();

        if (branch == null) return "";

        Matcher matcher = JIRA_ID_FORMAT.matcher(branch.getName());
        while (matcher.find()) {
            jiraIdBuilder.append(matcher.group(1));
            jiraIdBuilder.append(' ');
        }

        return jiraIdBuilder.toString();
    }

    private ChangeType getSelectedChangeType() {
        for (Enumeration<AbstractButton> buttons = changeTypeGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return ChangeType.valueOf(button.getActionCommand().toUpperCase());
            }
        }
        return null;
    }

    private void restoreValuesFromParsedCommitMessage(CommitMessage commitMessage) {
        if (commitMessage.getChangeType() != null) {
            for (Enumeration<AbstractButton> buttons = changeTypeGroup.getElements(); buttons.hasMoreElements();) {
                AbstractButton button = buttons.nextElement();

                if (button.getActionCommand().equalsIgnoreCase(commitMessage.getChangeType().label())) {
                    button.setSelected(true);
                }
            }
        }
        changeScope.setSelectedItem(commitMessage.getChangeScope());
        shortDescription.setText(commitMessage.getShortDescription());
        longDescription.setText(commitMessage.getLongDescription());
        breakingChanges.setText(commitMessage.getBreakingChanges());
        closedIssues.setText(commitMessage.getClosedIssues());
        skipCICheckBox.setSelected(commitMessage.isSkipCI());
        jiraIdModeCombobox.setSelectedItem(commitMessage.getJiraIdMode());

        //Only enable the ID text field if the ID mode is "fixed"
        jiraIdTextField.setEnabled(commitMessage.getJiraIdMode() == JiraIdMode.FIXED);

        if (commitMessage.getJiraIdMode() == JiraIdMode.AUTODETECT) {
            jiraIdTextField.setText(getJiraIdFromBranchName());
        } else if (commitMessage.getJiraIdMode() == JiraIdMode.NONE) {
            jiraIdTextField.setText("");
        } else {
            jiraIdTextField.setText(commitMessage.getJiraId());
        }
    }
}
