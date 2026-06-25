#!/bin/bash
export JAVA_HOME=/root/.local/share/mise/installs/java/17.0.2
export PATH=$JAVA_HOME/bin:/tmp/gradle-8.7/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
export ANDROID_HOME=/workspace/android-sdk
cd /workspace/TingshuApp
echo "Java version:"
java -version 2>&1
echo "---"
echo "Gradle version:"
gradle --version 2>&1 | head -3
echo "---"
echo "Starting build..."
gradle assembleDebug --no-daemon --stacktrace 2>&1
echo "---"
echo "Build exit code: $?"
find /workspace/TingshuApp -name "*.apk" 2>/dev/null
