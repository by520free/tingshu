#!/bin/bash
export JAVA_HOME=/root/.local/share/mise/installs/java/17.0.2
export PATH=$JAVA_HOME/bin:/root/.local/share/mise/installs/gradle/8.14.4/gradle-8.14.4/bin:$PATH
export ANDROID_HOME=/workspace/android-sdk
cd /workspace/TingshuApp
echo "Java version:"
java -version 2>&1
echo "---"
echo "Starting build..."
gradle assembleDebug --no-daemon 2>&1
echo "---"
echo "Build exit code: $?"
