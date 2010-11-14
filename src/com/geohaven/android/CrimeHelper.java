package com.geohaven.android;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

public class CrimeHelper {

	private static final DateFormat tsvDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static HashMap<Crime.Type, List<Crime>> lists = new HashMap<Crime.Type, List<Crime>>();

	public static void init(InputStream instream) {
		try {
			DataInputStream in = new DataInputStream(instream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// int count = 0;
			while ((strLine = br.readLine()) != null) {
				// Log.w("building", "got " + count++);
				String[] cols = strLine.split("\t");
				if (cols.length >= 3) {
					Crime.Type type = Crime.Type.getByKey(cols[0]);
					List<Crime> list = lists.get(type);
					if (list == null) {
						list = new ArrayList<Crime>();
						lists.put(type, list);
					}
					list.add(new Crime(type,
					// tsvDf.parse(cols[1]),
							Double.valueOf(cols[1]), Double.valueOf(cols[2])));
				}
			}
			in.close();
		} catch (Exception e) {// Catch exception if any
			Log.e("CRIME LOAD", "failed", e);
			// System.err.println("Error: " + e.getMessage());
		}

	}

	public static List<Crime> getByRectangle(Crime.Type type, double latC, double longC, double width, double height) {
		return getByRectangle(lists.get(type), latC, longC, width, height);
	}

	public static int countInRectangle(Crime.Type type, double latC, double longC, double width, double height) {
		return getByRectangle(type, latC, longC, width, height).size();
	}

	public static int countInRectangle(Crime.Category cat, double latC, double longC, double width, double height) {
		return getByRectangle(cat, latC, longC, width, height).size();
	}

	public static List<Crime> getByRectangle(Crime.Category cat, double latC, double longC, double width, double height) {
		List<Crime> result = new ArrayList<Crime>();

		for (Crime.Type t : cat.getTypes()) {
			result.addAll(getByRectangle(t, latC, longC, width, height));
		}

		return result;
	}

	public static List<Crime> getByRectangle(List<Crime> list, double latC, double longC, double width, double height) {
		List<Crime> result = new ArrayList<Crime>();

		if (list == null)
			return result;

		for (Crime c : list) {
			if (c.isInRect(longC - width / 2, longC + width / 2, latC + height / 2, latC - height / 2)) {
				result.add(c);
			}
		}

		return result;
	}

	public static int countInRectangle(List<Crime> list, double latC, double longC, double width, double height) {
		return getByRectangle(list, latC, longC, width, height).size();
	}
}
