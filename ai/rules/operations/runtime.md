# Runtime Policy

Read this policy before running Gradle or diagnosing Android/JDK toolchain behavior.

## Gradle JDK

All Gradle commands MUST use the Android Studio JBR at:

```text
/Applications/Android Studio.app/Contents/jbr/Contents/Home
```

Use a command-local environment assignment:

```bash
JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew <task>
```

Do not silently fall back to the shell, system, or another installed JDK. Before the first Gradle command, verify that `$JAVA_HOME/bin/java` exists and record `java -version` in execution evidence when runtime identity matters.

If the JBR path is unavailable or incompatible, do not change production code to compensate. Mark the Gradle check `NOT VERIFIED` and route the environment problem to Infrastructure.
