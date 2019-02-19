# slang-unity-sample
An Android app to demonstrate integration of Slang with Unity 3D

This project demonstrates the integration of the Slang SDK with an Android app that's based on the Unity 3D platform. The app is a very simple [Unity 3D demonstrator](https://assetstore.unity.com/packages/essentials/tutorial-projects/simple-mobile-placeholder-62281) that displays a rotating cube controlled by a script. For the purpose of this demonstration, we add methods to the script to start and stop the rotation of the cube, as well as change it's speed. Finally, we integrate these methods with the Slang Android SDK and enable voice activation of these features.

## Code Locations
If you want to look at the code right away, here's where to look: The modified Unity script with additional methods for starting/stopping/changing speed of rotation can be found at [SpinningCube.cs](https://github.com/SlangLabs/slang-unity-sample/blob/master/unity/Assets/Scripts/SpinningCube.cs). The Android code that integrates with the Slang SDK can be found at [UnityPlayerActivity.java](https://github.com/SlangLabs/slang-unity-sample/blob/master/android/src/main/java/in/slanglabs/unitysample/UnityPlayerActivity.java).

## Building and Running
**Note**: The following steps assume you have Unity 3D and Android SDK setup correctly

### Setting up and exporting the Unity project
1. Create an empty Unity 3D project and import the SimpleMobilePlaceHolder asset, as described in the first section of this article: https://unity3d.com/learn/tutorials/topics/mobile-touch/building-your-unity-game-android-device-testing
2. Next, replace the script found in the project with the script located in this repo at [unity/Assets/Scripts/SpinningCube.cs](https://github.com/SlangLabs/slang-unity-sample/blob/master/unity/Assets/Scripts/SpinningCube.cs)
3. Export the Unity 3D project as an Android project (File -> Build Settings -> Android -> Switch Platform; Check Export Project; Click Export)

### Importing Android project and integrating with Slang
1. Import the previously exported Android project into Android Studio.
2. Update the build.gradle for the project, and add Slang dependencies as described here: https://docs.slanglabs.in/slang/developer-guide/sdk-integration/android#performing-build-integration
3. Replace the file found at src/main/java/in/slanglabs/unitysample/UnityPlayerActivity.java with the corresponding [UnityPlayerActivity.java](https://github.com/SlangLabs/slang-unity-sample/blob/master/android/src/main/java/in/slanglabs/unitysample/UnityPlayerActivity.java) from this repo.
4. Build the project and run on the device.

That's it! The app should now be integrated with Slang and you should be able to see the Slang trigger and interact with it when the app launches on your device.

### Using Slang
The following intents are currently supported by the application:
1. Start rotation: Use utterances like "Start rotation", "Begin rotation", etc.
2. Stop rotation: Use utterances like "Stop rotation", "End rotation", etc.
3. Change rotation speed: Use utterances like "Set rotation speed to 200", "Rotation speed 100", etc.
