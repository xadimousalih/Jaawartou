package com.tab28.jaawartou;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author xadimouSALIH
 * 
 */
public class JaawartouActivity extends Activity implements OnClickListener,
		OnItemClickListener, AnimationListener {

	public static ImageView btnPlay, btnForward, btnBackward, btnNext,
			btnPrevious, listSongBtn, lecteurBtn, backBtn;
	public static ImageButton btnShuffle, btnRepeat;
	public static SeekBar songProgressBar;
	private static final int RESULT_SETTINGS = 1;
	public static ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	private ListAdapter adapter;
	private ListView listSongLv;
	private LinearLayout playerScreen, listSongScreen;
	public static TextView songTitle, songCurrentDurationLabel,
			songTotalDurationLabel;
	public Intent playerService;
	ListView listView;
	List<RowItem> rowItems;
	String url = null;
	String qacidaName = "Jaawartou";
	String[] arab = null;
	String[] fran = null;
	String[] tran = null;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);
		AlertDialog alertDialog1 = new AlertDialog.Builder(this).create();
		alertDialog1.setTitle(this.getString(R.string.bienvenu));
		alertDialog1.setMessage(Html.fromHtml("<center>"
				+ this.getString(R.string.obj1) + "<br/>"
				+ this.getString(R.string.obj2) + "</center>"));
		alertDialog1.setIcon(R.drawable.serignsaliou);
		alertDialog1.setButton(this.getString(R.string.str_yes),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (dialog != null)
							dialog.dismiss();
						Toast.makeText(
								getApplicationContext(),
								JaawartouActivity.this
										.getString(R.string.dieredieuf),
								Toast.LENGTH_SHORT).show();
					}
				});
		alertDialog1.show();
		try {

			initViews();
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				qacidaName = extras.getString("QacidaName");
			}
			if (qacidaName.equals("Jaawartou")) {
				url = "http://www.tab8.com/media/Jaawartou.mp3";
				arab = getResources().getStringArray(R.array.Jaawartou_Ar);
				fran = getResources().getStringArray(R.array.Jaawartou_Fr);
				tran = getResources().getStringArray(R.array.Jaawartou_Tr);

			}
			ArrayList<HashMap<String, String>> songsListData = new ArrayList<HashMap<String, String>>();

			SongsProvider plm = new SongsProvider();
			try {
				songsList = plm.getPlayList();
				for (int i = 0; i < songsList.size(); i++) {
					HashMap<String, String> song = songsList.get(i);
					songsListData.add(song);
				}
				adapter = new SimpleAdapter(this, songsListData,
						R.layout.listsong_item, new String[] { "songTitle" },
						new int[] { R.id.songTitle });
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			rowItems = new ArrayList<RowItem>();
			RowItem item = null;
			for (int i = 0; i < arab.length; i++) {
				item = new RowItem(arab[i], fran[i], tran[i]);
				rowItems.add(item);
			}

			listView = (ListView) findViewById(R.id.list);
			CustomListViewAdapter adapter = new CustomListViewAdapter(this,
					R.layout.list_item, rowItems);
			listView.setAdapter(adapter);
			listSongLv = (ListView) findViewById(R.id.listsong_listview);
			playerService = new Intent(this, PlayerService.class);
			playerService.putExtra("songIndex", PlayerService.currentSongIndex);
			startService(playerService);
		} catch (Exception e) {
		}
	}

	@Override
	protected void onDestroy() {
		try {
			super.onDestroy();
			if (!PlayerService.mp.isPlaying()) {
				stopService(playerService);
				cancelNotification();
			}
		} catch (IllegalStateException e) {
		} catch (NullPointerException e) {
		} catch (Exception e) {
		}
	}

	private void exitPlayer() {
		try {
			if (PlayerService.mp.isPlaying())
				PlayerService.mp.stop();
			cancelNotification();
			finish();
		} catch (IllegalStateException e) {
		} catch (Exception e) {
		}
	}

	public void cancelNotification() {
		String notificationServiceStr = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(notificationServiceStr);
		mNotificationManager.cancel(PlayerService.NOTIFICATION_ID);
	}

	private void initViews() {
		try {
			playerScreen = (LinearLayout) findViewById(R.id.playerScreen);
			listSongScreen = (LinearLayout) findViewById(R.id.list_song_layout);
			listSongScreen.setVisibility(View.INVISIBLE);
			btnPlay = (ImageView) findViewById(R.id.btn_play_imageview);
			btnForward = (ImageView) findViewById(R.id.btn_forward_imageview);
			btnBackward = (ImageView) findViewById(R.id.btn_backward_imagview);
			btnNext = (ImageView) findViewById(R.id.btn_next_imageview);
			btnPrevious = (ImageView) findViewById(R.id.btn_previous_imageview);
			listSongBtn = (ImageView) findViewById(R.id.listsong_btn);
			btnShuffle = (ImageButton) findViewById(R.id.btnShuffle);
			btnRepeat = (ImageButton) findViewById(R.id.btnRepeat);
			songProgressBar = (SeekBar) findViewById(R.id.song_playing_progressbar);
			songTitle = (TextView) findViewById(R.id.song_title_txt);
			songCurrentDurationLabel = (TextView) findViewById(R.id.current_time_txt);
			songTotalDurationLabel = (TextView) findViewById(R.id.total_time_txt);
			backBtn = (ImageView) findViewById(R.id.back_btn);
			btnPlay.setOnClickListener(this);
			btnForward.setOnClickListener(this);
			btnBackward.setOnClickListener(this);
			btnNext.setOnClickListener(this);
			btnPrevious.setOnClickListener(this);
			btnShuffle.setOnClickListener(this);
			btnRepeat.setOnClickListener(this);
			listSongBtn.setOnClickListener(this);
			backBtn.setOnClickListener(this);
		} catch (Exception e) {
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_about:
			openOptionsDialog();
			return true;
		case R.id.app_exit:
			exitOptionsDialog();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void exitOptionsDialog() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.str_exit)
				.setMessage(R.string.app_exit_message)
				.setNegativeButton(R.string.str_no,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
							}
						})
				.setPositiveButton(R.string.str_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
								exitPlayer();
							}
						}).show();
	}

	private void openOptionsDialog() {
		AboutDialog about = new AboutDialog(this);
		about.setTitle(Html.fromHtml(this.getString(R.string.app_about)));
		about.show();
	}

	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.listsong_btn:
				showListSongScreen();
				listSongLv.setAdapter(adapter);
				listSongLv.setOnItemClickListener(this);
				break;
			case R.id.back_btn:
				showListSongScreen();
				break;
			}
		} catch (IllegalStateException e) {
		} catch (Exception e) {
		}

	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.listsong_listview:
			playerService = new Intent(this, PlayerService.class);
			playerService.putExtra("songIndex", position);
			startService(playerService);
			showListSongScreen();
			break;
		}

	}

	private boolean isVisible = false;

	private void showListSongScreen() {
		Animation anim;
		if (isVisible == false) {
			listSongScreen.setVisibility(View.VISIBLE);
			anim = AnimationUtils.loadAnimation(this, R.anim.push_down_in);
			isVisible = true;
		} else {
			anim = AnimationUtils.loadAnimation(this, R.anim.push_down_out);
			isVisible = false;
			playerScreen.setVisibility(View.VISIBLE);

		}
		anim.setAnimationListener(this);
		listSongScreen.startAnimation(anim);

	}

	public void onAnimationStart(Animation animation) {

	}

	public void onAnimationEnd(Animation animation) {
		if (isVisible == true)
			playerScreen.setVisibility(View.INVISIBLE);
		else
			listSongScreen.setVisibility(View.INVISIBLE);
	}

	public void onAnimationRepeat(Animation animation) {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case RESULT_SETTINGS:
			break;

		}

	}

}
