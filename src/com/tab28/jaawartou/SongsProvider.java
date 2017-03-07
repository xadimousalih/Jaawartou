package com.tab28.jaawartou;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.Environment;
import android.util.Log;
/**
 * @author xadimouSALIH
 * 
 */

public class SongsProvider {
	static File root = Environment.getExternalStorageDirectory();
	static String mypath = root.getAbsolutePath() + "/xacida/";
	private static final String path = new String(mypath);

	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

	public SongsProvider() {

	}

	public ArrayList<HashMap<String, String>> getPlayList() {
		File musicFolder = null;
		HashMap<String, String> song = null;

		song = new HashMap<String, String>();
		song.put("songTitle", "Jaawartou(Serigne Mountaqa Gueye).mp3");
		song.put("songPath", "jaawartou.mp3");
		songsList.add(song);
		if (existFile(path)) {
			musicFolder = new File(path);

			if (musicFolder.listFiles(new FileExtensionFilter()).length > 0) {
				for (File file : musicFolder
						.listFiles(new FileExtensionFilter())) {
					song = new HashMap<String, String>();
					String chemin = file.getName().substring(0,
							(file.getName().length() - 4));
					song.put("songTitle", chemin);
					song.put("songPath", file.getPath());
					songsList.add(song);
				}
			}
		}
		return songsList;
	}

	class FileExtensionFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return (name.endsWith(".mp3") || name.endsWith(".MP3"));
		}
	}

	public boolean existFile(String folderURL) {
		boolean isExist = false;
		File dir = null;
		try {
			dir = new File(folderURL);
			if (dir.exists())
				isExist = true;
			else {
				if (isExternalStorageAvailableAndWriteable())
					dir.mkdir();
				else {
					dir = new File(Environment.getDataDirectory(), "xacida");
					dir.mkdir();
				}
			}
		} catch (Exception e) {
			Log.d("existFile", e.getMessage());
		}
		return isExist;
	}

	private boolean externalStorageAvailable, externalStorageWriteable;


	private void checkStorage() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			externalStorageAvailable = externalStorageWriteable = true;
		} else if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			externalStorageAvailable = true;
			externalStorageWriteable = false;
		} else {
			externalStorageAvailable = externalStorageWriteable = false;
		}
	}

	public boolean isExternalStorageAvailableAndWriteable() {
		checkStorage();
		if (!externalStorageAvailable) {
			return false;
		} else if (!externalStorageWriteable) {
			return false;
		} else {
			return true;
		}
	}

}
