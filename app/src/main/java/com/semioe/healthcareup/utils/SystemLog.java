package com.semioe.healthcareup.utils;

import android.util.Log;

public class SystemLog {

	public static boolean DEBUG = true;

	public static boolean isDEBUG() {
		return DEBUG;
	}

	public static void setDEBUG(boolean dEBUG) {
		DEBUG = dEBUG;
	}

	public static void Log(String data) {
		if (DEBUG) {
			Log.d("调试", data);
			System.out.println("调试:" + data);
		}
	}

}
