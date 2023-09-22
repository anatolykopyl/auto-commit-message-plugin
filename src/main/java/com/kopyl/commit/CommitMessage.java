package com.kopyl.commit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author Damien Arrachequesne <damien.arrachequesne@gmail.com>
 */
public class CommitMessage {
    private static final int MAX_LINE_LENGTH = 72; // https://stackoverflow.com/a/2120040/5138796

    public static final Pattern COMMIT_FIRST_LINE_FORMAT = Pattern.compile("^([a-z]+)(\\((.+)\\))?: (.+)");
    public static final Pattern COMMIT_CLOSES_FORMAT = Pattern.compile("Closes (.+)");
    public static final Pattern JIRA_ID_FORMAT = Pattern.compile("\\[(\\w+[_-]\\d+)]");

    private ChangeType changeType = ChangeType.FEAT;
    private String changeScope = "";
    private String shortDescription = "";
    private String longDescription = "";
    private String breakingChanges = "";
    private String closedIssues = "";
    private boolean wrapText = true;
    private boolean skipCI = false;
    private String jiraId = null;
    private JiraIdMode jiraIdMode = JiraIdMode.AUTODETECT;

    private CommitMessage() {
        this.longDescription = "";
        this.breakingChanges = "";
        this.closedIssues = "";
    }

    public CommitMessage(ChangeType changeType, String changeScope, String shortDescription, String longDescription,
                         String breakingChanges, String closedIssues, boolean wrapText, boolean skipCI
    ) {
        this(changeType, changeScope, shortDescription, longDescription,
             breakingChanges, closedIssues, wrapText, skipCI, JiraIdMode.NONE, "");
    }

    public CommitMessage(ChangeType changeType, String changeScope, String shortDescription, String longDescription,
                         String breakingChanges, String closedIssues, boolean wrapText, boolean skipCI,
                         JiraIdMode jiraIdMode, String jiraId
    ) {
        this.changeType = changeType;
        this.changeScope = changeScope;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.breakingChanges = breakingChanges;
        this.closedIssues = closedIssues;
        this.wrapText = wrapText;
        this.skipCI = skipCI;
        this.jiraIdMode = jiraIdMode;
        this.jiraId = jiraId;
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

        final String lineSeparator = "\n";
        if (isNotBlank(longDescription)) {
            builder.append(lineSeparator.repeat(2))
                    .append(wrapText ? WordUtils.wrap(longDescription, MAX_LINE_LENGTH, "\n", true) : longDescription);
        }

        if (isNotBlank(breakingChanges)) {
            String content = "BREAKING CHANGE: " + breakingChanges;
            builder.append(lineSeparator.repeat(2))
                    .append(wrapText ? WordUtils.wrap(content, MAX_LINE_LENGTH, "\n", true) : content);
        }

        if (isNotBlank(closedIssues)) {
            builder.append(lineSeparator);
            for (String closedIssue : closedIssues.split(",")) {
                builder.append(lineSeparator)
                        .append("Closes ")
                        .append(formatClosedIssue(closedIssue));
            }
        }

        if (skipCI) {
            builder.append(lineSeparator.repeat(2))
                    .append("[skip ci]");
        }

        return builder.toString();
    }

    private String formatClosedIssue(String closedIssue) {
        String trimmed = closedIssue.trim();
        return (StringUtils.isNumeric(trimmed) ? "#" : "") + trimmed;
    }

    public static CommitMessage parse(String message) {
        CommitMessage commitMessage = new CommitMessage();

        try {
            Matcher matcher = COMMIT_FIRST_LINE_FORMAT.matcher(message);
            if (!matcher.find()) return commitMessage;

            commitMessage.changeType = ChangeType.valueOf(matcher.group(1).toUpperCase());
            commitMessage.changeScope = matcher.group(3);
            commitMessage.shortDescription = matcher.group(4);

            String[] strings = message.split("\n");
            if (strings.length < 2) return commitMessage;

            int pos = 1;
            StringBuilder stringBuilder;
            StringBuilder jiraIdStringBuilder = new StringBuilder();

            stringBuilder = new StringBuilder();
            for (; pos < strings.length; pos++) {
                String lineString = strings[pos];
                if (lineString.startsWith("BREAKING") || lineString.startsWith("Closes") || lineString.equalsIgnoreCase("[skip ci]")) break;

                matcher = JIRA_ID_FORMAT.matcher(lineString);
                int matchTotal = 0;
                while (matcher.find()) {
                    jiraIdStringBuilder.append(matcher.group(1));
                    jiraIdStringBuilder.append(' ');
                    int matchStart = matcher.start(0);
                    int matchEnd = matcher.end(0);
                    lineString = lineString.substring(0, matchStart - matchTotal) + lineString.substring(matchEnd - matchTotal);
                    matchTotal += matchEnd - matchStart;
                }

                stringBuilder.append(lineString).append('\n');
            }
            commitMessage.longDescription = stringBuilder.toString().trim();

            stringBuilder = new StringBuilder();
            for (; pos < strings.length; pos++) {
                String lineString = strings[pos];
                if (lineString.startsWith("Closes") || lineString.equalsIgnoreCase("[skip ci]")) break;
                stringBuilder.append(lineString).append('\n');
            }
            commitMessage.breakingChanges = stringBuilder.toString().trim().replace("BREAKING CHANGE: ", "");

            matcher = COMMIT_CLOSES_FORMAT.matcher(message);
            stringBuilder = new StringBuilder();
            while (matcher.find()) {
                stringBuilder.append(matcher.group(1)).append(',');
            }
            if (stringBuilder.length() > 0) stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
            commitMessage.closedIssues = stringBuilder.toString();

            commitMessage.jiraId = jiraIdStringBuilder.toString();

            commitMessage.skipCI = message.contains("[skip ci]");
        } catch (RuntimeException e) {}

        return commitMessage;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public String getChangeScope() {
        return changeScope;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String getBreakingChanges() {
        return breakingChanges;
    }

    public String getClosedIssues() {
        return closedIssues;
    }

    public boolean isSkipCI() {
        return skipCI;
    }

    public String getJiraId() { return jiraId; }

    public JiraIdMode getJiraIdMode() { return jiraIdMode; }
}