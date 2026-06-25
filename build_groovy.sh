#!/bin/bash
export JAVA_HOME=/root/.local/share/mise/installs/java/17.0.2
export PATH=$JAVA_HOME/bin:/root/.local/share/mise/installs/gradle/8.14.4/gradle-8.14.4/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
export ANDROID_HOME=/workspace/android-sdk
cd /workspace/TingshuApp
echo "Java version:"
java -version 2>&1
echo "---"
echo "Gradle version:"
gradle --version 2>&1 | head -5
echo "---"
echo "Starting build with Groovy DSL..."
gradle assembleDebug --no-daemon --info 2>&1 | tee /tmp/groovy_build.log
echo "---"
echo "Build exit code: $?"
