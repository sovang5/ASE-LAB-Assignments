package com.tutsplus.cardboardvideo;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private static final String STATE_PROGRESS = "state_progress";
    private static final String STATE_DURATION = "state_duration";

    private VrVideoView mVrVideoView;
    private SeekBar mSeekBar;
    private Button mVolumeButton;

    private boolean mIsPaused;
    private boolean mIsMuted;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initViews();

        VideoLoaderTask mBackgroundVideoLoaderTask = new VideoLoaderTask();
        mBackgroundVideoLoaderTask.execute();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initViews() {
        mVrVideoView = (VrVideoView) findViewById(R.id.video_view);
        mSeekBar = (SeekBar) findViewById(R.id.seek_bar);
        mVolumeButton = (Button) findViewById(R.id.btn_volume);

        mVolumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onVolumeToggleClicked();
            }
        });

        mVrVideoView.setEventListener(new ActivityEventListener());
        mSeekBar.setOnSeekBarChangeListener(this);

    }



    public void playPause() {
        if (mIsPaused) {
            mVrVideoView.playVideo();
        } else {
            mVrVideoView.pauseVideo();
        }

        mIsPaused = !mIsPaused;
    }

    public void onVolumeToggleClicked() {
        mIsMuted = !mIsMuted;
        mVrVideoView.setVolume(mIsMuted ? 0.0f : 1.0f);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mVrVideoView.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //no op
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //no op
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVrVideoView.pauseRendering();
        mIsPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVrVideoView.resumeRendering();
        mIsPaused = false;
    }

    @Override
    protected void onDestroy() {
        mVrVideoView.shutdown();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(STATE_PROGRESS, mVrVideoView.getCurrentPosition());
        outState.putLong(STATE_DURATION, mVrVideoView.getDuration());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        long progress = savedInstanceState.getLong(STATE_PROGRESS);

        mVrVideoView.seekTo(progress);
        mSeekBar.setMax((int) savedInstanceState.getLong(STATE_DURATION));
        mSeekBar.setProgress((int) progress);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    class VideoLoaderTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                VrVideoView.Options options = new VrVideoView.Options();
                options.inputType = VrVideoView.Options.TYPE_MONO;
                mVrVideoView.loadVideoFromAsset("seaturtle.mp4", options);
            } catch (IOException e) {
                //Handle exception
            }

            return true;
        }
    }

    private class ActivityEventListener extends VrVideoEventListener {
        @Override
        public void onLoadSuccess() {
            super.onLoadSuccess();

            mSeekBar.setMax((int) mVrVideoView.getDuration());
            mIsPaused = false;
        }

        @Override
        public void onLoadError(String errorMessage) {
            super.onLoadError(errorMessage);
            //Oh no.
        }

        @Override
        public void onClick() {
            playPause();
        }

        @Override
        public void onNewFrame() {
            super.onNewFrame();

            mSeekBar.setProgress((int) mVrVideoView.getCurrentPosition());
        }

        @Override
        public void onCompletion() {
            //Restart the video, allowing it to loop
            mVrVideoView.seekTo(0);
        }
    }
}
