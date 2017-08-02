./gradlew clean assemblePlugin
adb push app/build/outputs/apk/app-release-unsigned.apk /sdcard/Test.apk
adb shell am force-stop github.shicaid.host
adb shell am start -n github.shicaid.host/github.shicaid.host.MainActivity