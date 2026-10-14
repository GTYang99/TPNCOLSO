# Runtime Policy

Read this policy before running Gradle or diagnosing Android/JDK toolchain behavior.

## Gradle JDK

All Gradle commands MUST use the Android Studio JBR for the active host:

| Host | Required JBR path |
|---|---|
| macOS | `/Applications/Android Studio.app/Contents/jbr/Contents/Home` |
| Windows | `C:\Program Files\Android\Android Studio1\jbr` |

Use the matching command-local environment assignment:

```bash
JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew <task>
```

```powershell
$previousJavaHome = $env:JAVA_HOME
try {
    $env:JAVA_HOME = 'C:\Program Files\Android\Android Studio1\jbr'
    .\gradlew.bat <task>
} finally {
    $env:JAVA_HOME = $previousJavaHome
}
```

Do not silently fall back to the shell, system, or another installed JDK. Before the first Gradle command, verify that `<JBR path>/bin/java` (macOS) or `<JBR path>/bin/java.exe` (Windows) exists and record `java -version` in execution evidence when runtime identity matters.

If the JBR path is unavailable or incompatible, do not change production code to compensate. Mark the Gradle check `NOT VERIFIED` and route the environment problem to Infrastructure.
