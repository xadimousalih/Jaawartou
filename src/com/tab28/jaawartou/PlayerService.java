package com.tab28.jaawartou;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author xadimouSALIH
 * 
 */
public class PlayerService extends Service implements OnCompletionListener,
		OnClickListener, OnSeekBarChangeListener {

	private WeakReference<ImageButton> btnRepeat, btnShuffle;
	private WeakReference<ImageView> btnPlay, btnForward, btnBackward, btnNext,
			btnPrevious;
	private WeakReference<SeekBar> songProgressBar;
	private WeakReference<TextView> songTitleLabel;
	private WeakReference<TextView> songCurrentDurationLabel;
	private WeakReference<TextView> songTotalDurationLabel;
	public static MediaPlayer mp;
	private Handler progressBarHandler = new Handler();;
	private Utilities utils;
	private int seekForwardTime = 5000;
	private int seekBackwardTime = 5000;
	private boolean isShuffle = false;
	private boolean isRepeat = false;
	private ArrayList<HashMap<String, String>> songsListingSD = new ArrayList<HashMap<String, String>>();
	public static int currentSongIndex = -1;
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;

	@Override
	public void onCreate() {
		mp = new MediaPlayer();
		mp.setOnCompletionListener(this);
		mp.reset();
		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);//
		utils = new Utilities();
		songsListingSD = JaawartouActivity.songsList;
		songCurrentDurationLabel = new WeakReference<TextView>(
				JaawartouActivity.songCurrentDurationLabel);
		super.onCreate();

	}

	@SuppressWarnings("deprecation")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {

			initUI();
			int songIndex = intent.getIntExtra("songIndex", 0);
			if (songIndex != currentSongIndex) {
				playSong(songIndex);
				initNotification(songIndex);
				currentSongIndex = songIndex;
			} else if (currentSongIndex != -1) {
				String monTitre = songsListingSD.get(currentSongIndex).get(
						"songTitle");
				songTitleLabel.get().setText(
						monTitre.substring(0, (monTitre.length() - 4)));
				if (mp.isPlaying())
					btnPlay.get().setImageResource(R.drawable.ic_media_pause);
				else
					btnPlay.get().setImageResource(R.drawable.ic_media_play);
			}

			super.onStart(intent, startId);
		} catch (NullPointerException e) {
		} catch (Exception e) {
		}

		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void initUI() {
		songTitleLabel = new WeakReference<TextView>(JaawartouActivity.songTitle);
		songCurrentDurationLabel = new WeakReference<TextView>(
				JaawartouActivity.songCurrentDurationLabel);
		songTotalDurationLabel = new WeakReference<TextView>(
				JaawartouActivity.songTotalDurationLabel);
		btnPlay = new WeakReference<ImageView>(JaawartouActivity.btnPlay);
		btnForward = new WeakReference<ImageView>(JaawartouActivity.btnForward);
		btnBackward = new WeakReference<ImageView>(JaawartouActivity.btnBackward);
		btnNext = new WeakReference<ImageView>(JaawartouActivity.btnNext);
		btnPrevious = new WeakReference<ImageView>(JaawartouActivity.btnPrevious);
		btnRepeat = new WeakReference<ImageButton>(JaawartouActivity.btnRepeat);
		btnShuffle = new WeakReference<ImageButton>(JaawartouActivity.btnShuffle);
		btnPlay.get().setOnClickListener(this);
		btnForward.get().setOnClickListener(this);
		btnBackward.get().setOnClickListener(this);
		btnNext.get().setOnClickListener(this);
		btnPrevious.get().setOnClickListener(this);
		btnRepeat.get().setOnClickListener(this);
		btnShuffle.get().setOnClickListener(this);
		songProgressBar = new WeakReference<SeekBar>(
				JaawartouActivity.songProgressBar);
		songProgressBar.get().setOnSeekBarChangeListener(this);
	}

	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.btn_play_imageview:
				if (currentSongIndex != -1) {
					if (mp.isPlaying()) {
						if (mp != null) {
							mp.pause();
							btnPlay.get().setImageResource(
									R.drawable.ic_media_play);
							Log.d("Player Service", "Pause");

						}
					} else {
						if (mp != null) {
							mp.start();
							btnPlay.get().setImageResource(
									R.drawable.ic_media_pause);
							Log.d("Player Service", "Play");
						}
					}
				}
				break;

			case R.id.btn_next_imageview:
				Log.d("Player Service", "Next");
				if (currentSongIndex < (songsListingSD.size() - 1)) {
					playSong(currentSongIndex + 1);
					currentSongIndex = currentSongIndex + 1;
				} else {
					playSong(0);
					currentSongIndex = 0;
				}
				break;

			case R.id.btn_forward_imageview:
				int currentPosition = mp.getCurrentPosition();
				if (currentPosition + seekForwardTime <= mp.getDuration()) {
					mp.seekTo(currentPosition + seekForwardTime);
				} else {
					mp.seekTo(mp.getDuration());
				}
				break;

			case R.id.btn_backward_imagview:
				int currentPosition2 = mp.getCurrentPosition();
				if (currentPosition2 - seekBackwardTime >= 0) {
					mp.seekTo(currentPosition2 - seekBackwardTime);
				} else {
					mp.seekTo(0);
				}
				break;

			case R.id.btn_previous_imageview:

				if (currentSongIndex > 0) {
					playSong(currentSongIndex - 1);
					currentSongIndex = currentSongIndex - 1;
				} else {
					playSong(songsListingSD.size() - 1);
					currentSongIndex = songsListingSD.size() - 1;
				}
				break;

			case R.id.btnRepeat:

				if (isRepeat) {
					isRepeat = false;
					Toast.makeText(getApplicationContext(), "Repeat is OFF",
							Toast.LENGTH_SHORT).show();
					btnRepeat.get().setImageResource(R.drawable.btn_repeat);
				} else {
					isRepeat = true;
					Toast.makeText(getApplicationContext(), "Repeat is ON",
							Toast.LENGTH_SHORT).show();
					isShuffle = false;
					btnRepeat.get().setImageResource(
							R.drawable.btn_repeat_focused);
					btnShuffle.get().setImageResource(R.drawable.btn_shuffle);
				}
				break;

			case R.id.btnShuffle:

				if (isShuffle) {
					isShuffle = false;
					Toast.makeText(getApplicationContext(), "Shuffle is OFF",
							Toast.LENGTH_SHORT).show();
					btnShuffle.get().setImageResource(R.drawable.btn_shuffle);
				} else {
					isShuffle = true;
					Toast.makeText(getApplicationContext(), "Shuffle is ON",
							Toast.LENGTH_SHORT).show();
					isRepeat = false;
					btnShuffle.get().setImageResource(
							R.drawable.btn_shuffle_focused);
					btnRepeat.get().setImageResource(R.drawable.btn_repeat);
				}
				break;
			}
		} catch (NullPointerException e) {
		} catch (IllegalStateException e) {
		} catch (Exception e) {
		}

	}

	/**
	 * @author xadimouSALIH
	 * 
	 */
	public void playSong(int songIndex) {
		String songTitle = null;
		try {
			mp.reset();
			songTitle = songsListingSD.get(songIndex).get("songTitle");

			if (!songTitle.equals("Jaawartou(Serigne Mountaqa Gueye).mp3"))
				mp.setDataSource(songsListingSD.get(songIndex).get("songPath"));
			else {
				mp.setDataSource(
						getApplicationContext(),
						Uri.parse("android.resource://com.tab28.jaawartou/raw/"
								+ R.raw.jaawartou));
			}
			mp.setVolume(1f, 1f);
			mp.prepare();
			mp.start();
			String monTitre = songTitle.substring(0, (songTitle.length() - 4));
			songTitleLabel.get().setText(monTitre);
			btnPlay.get().setImageResource(R.drawable.ic_media_pause);
			songProgressBar.get().setProgress(0);
			songProgressBar.get().setMax(100);
			updateProgressBar();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateProgressBar() {
		progressBarHandler.postDelayed(mUpdateTimeTask, 100);
	}

	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			long totalDuration = 0;
			try {
				totalDuration = mp.getDuration();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
			long currentDuration = 0;
			try {
				currentDuration = mp.getCurrentPosition();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
			songTotalDurationLabel.get().setText(
					"" + utils.milliSecondsToTimer(totalDuration));
			songCurrentDurationLabel.get().setText(
					"" + utils.milliSecondsToTimer(currentDuration));

			int progress = (int) (utils.getProgressPercentage(currentDuration,
					totalDuration));
			songProgressBar.get().setProgress(progress);
			progressBarHandler.postDelayed(this, 100);
		}
	};

	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromTouch) {
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
		progressBarHandler.removeCallbacks(mUpdateTimeTask);
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
		progressBarHandler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = mp.getDuration();
		int currentPosition = utils.progressToTimer(seekBar.getProgress(),
				totalDuration);
		mp.seekTo(currentPosition);
		updateProgressBar();
	}

	public void onCompletion(MediaPlayer arg0) {
		try {

			if (isRepeat) {
				playSong(currentSongIndex);
			} else if (isShuffle) {
				Random rand = new Random();
				currentSongIndex = rand
						.nextInt((songsListingSD.size() - 1) - 0 + 1) + 0;
				playSong(currentSongIndex);
			} else {
				if (currentSongIndex < (songsListingSD.size() - 1)) {
					playSong(currentSongIndex + 1);
					currentSongIndex = currentSongIndex + 1;
				} else {
					playSong(0);
					currentSongIndex = 0;
				}
			}
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		currentSongIndex = -1;
		progressBarHandler.removeCallbacks(mUpdateTimeTask);
		Log.d("Player Service", "Player Service Stopped");
		if (mp != null) {
			if (mp.isPlaying()) {
				mp.stop();
			}
			mp.release();
		}

	}

	@SuppressWarnings("deprecation")
	private void initNotification(int songIndex) {
		String ns = Context.NOTIFICATION_SERVICE;
		mNotificationManager = (NotificationManager) getSystemService(ns);
		int icon = R.drawable.notification;
		CharSequence songName = songsListingSD.get(songIndex).get("songTitle");
		CharSequence songNameOK = songName.toString().substring(0,
				(songName.toString().length() - 4));
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, songNameOK, when);
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		Context context = getApplicationContext();
		Intent notificationIntent = new Intent(this, JaawartouActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, songName, null, contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, notification);
	}

}
