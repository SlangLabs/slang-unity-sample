package in.slanglabs.unitysample;

import com.unity3d.player.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;

import java.util.Locale;

import in.slanglabs.platform.SlangBuddy;
import in.slanglabs.platform.SlangBuddyOptions;
import in.slanglabs.platform.SlangEntity;
import in.slanglabs.platform.SlangIntent;
import in.slanglabs.platform.SlangSession;
import in.slanglabs.platform.action.SlangIntentAction;

public class UnityPlayerActivity extends Activity
{
    private static final String TAG = UnityPlayerActivity.class.getSimpleName();

    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code

    // Setup activity layout
    @Override protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        mUnityPlayer = new UnityPlayer(this);
        setContentView(mUnityPlayer);
        mUnityPlayer.requestFocus();
    }

    @Override protected void onNewIntent(Intent intent)
    {
        // To support deep linking, we need to make sure that the client can get access to
        // the last sent intent. The clients access this through a JNI api that allows them
        // to get the intent set on launch. To update that after launch we have to manually
        // replace the intent with the one caught here.
        setIntent(intent);
    }

    // Quit Unity
    @Override protected void onDestroy ()
    {
        mUnityPlayer.destroy();
        super.onDestroy();
    }

    // Pause Unity
    @Override protected void onPause()
    {
        super.onPause();
        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override protected void onResume()
    {
        super.onResume();
        mUnityPlayer.resume();
    }

    @Override protected void onStart()
    {
        super.onStart();
        mUnityPlayer.start();
        initializeSlang();
    }

    @Override protected void onStop()
    {
        super.onStop();
        mUnityPlayer.stop();
    }

    // Low Memory Unity
    @Override public void onLowMemory()
    {
        super.onLowMemory();
        mUnityPlayer.lowMemory();
    }

    // Trim Memory Unity
    @Override public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL)
        {
            mUnityPlayer.lowMemory();
        }
    }

    // This ensures the layout will be correct.
    @Override public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override public boolean onKeyUp(int keyCode, KeyEvent event)     { return mUnityPlayer.injectEvent(event); }
    @Override public boolean onKeyDown(int keyCode, KeyEvent event)   { return mUnityPlayer.injectEvent(event); }
    @Override public boolean onTouchEvent(MotionEvent event)          { return mUnityPlayer.injectEvent(event); }
    /*API12*/ public boolean onGenericMotionEvent(MotionEvent event)  { return mUnityPlayer.injectEvent(event); }

    private void initializeSlang() {
        try {
            Context appContext = this.getApplicationContext();
            SlangBuddyOptions options = new SlangBuddyOptions.Builder()
                    .setBuddyId(appContext.getResources().getString(R.string.slang_buddy_id))
                    .setAPIKey(appContext.getResources().getString(R.string.slang_api_key))
                    .setEnvironment(SlangBuddy.Environment.STAGING)
                    .setContext(appContext)
                    .setStartActivity(this)
                    .setIntentAction(new UnityIntentAction(mUnityPlayer))
                    .setListener(new SlangBuddyListener())
                    .build();
            SlangBuddy.initialize(options);
        } catch (SlangBuddyOptions.InvalidOptionException | SlangBuddy.InsufficientPrivilegeException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

    public static class UnityIntentAction implements SlangIntentAction {
        final UnityPlayer mUnityPlayer;

        UnityIntentAction(UnityPlayer unityPlayer) {
            mUnityPlayer = unityPlayer;
        }

        @Override
        public Status action(SlangIntent slangIntent, SlangSession slangSession) {
            final String unityObj = "Cube";

            switch (slangIntent.getName()) {
                case "start_rotation":
                    UnityPlayer.UnitySendMessage(unityObj, "StartRotation", "");
                    break;

                case "stop_rotation":
                    UnityPlayer.UnitySendMessage(unityObj, "StopRotation", "");
                    break;

                case "set_rotation_speed":
                    SlangEntity speed = slangIntent.getEntity("rotation_speed");
                    if (speed == null || speed.getValue().length() == 0) {
                        slangIntent.setCompletionStatement(speed.getStatement());
                    } else {
                        UnityPlayer.UnitySendMessage(unityObj, "SetRotationSpeed", speed.getValue());
                    }
            }
            return Status.SUCCESS;
        }
    }

    public static class SlangBuddyListener implements SlangBuddy.Listener {
        @Override
        public void onInitialized() {
            Log.d(TAG, "Initialized successfully");
        }

        @Override
        public void onInitializationFailed(SlangBuddy.InitializationError initializationError) {
            Log.e(TAG, "Initialization failed: " + initializationError.getMessage());
        }

        @Override
        public void onLocaleChanged(Locale locale) {
            Log.d(TAG, "Locale changed to: " + locale.getDisplayName());
        }

        @Override
        public void onLocaleChangeFailed(Locale locale, SlangBuddy.LocaleChangeError localeChangeError) {
            Log.e(TAG, "Locale change failed for: " + locale.getDisplayName());
        }
    }
}
