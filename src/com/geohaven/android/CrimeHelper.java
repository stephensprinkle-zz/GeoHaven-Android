package com.geohaven.android;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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

	public static List<Crime> getByRectangle(Crime.Category cat, MyGeoArea a) {
		return getByRectangle(cat, a.getCenter().getLat(), a.getCenter().getLon(), a.getLatSpan(), a.getLonSpan());
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

	public static int[] getAreaRatings(int perSide, MyGeoArea overallArea) {
		return getAreaRatings(Crime.Category.WALKING, perSide, overallArea);
	}

	public static int[] getAreaRatings(Crime.Category cat, int perSide, MyGeoArea overallArea) {
		AreaInfo[] ais = getAreaInfos(cat, perSide, overallArea);
		List<Integer> l = new ArrayList<Integer>();
		for (AreaInfo ai : ais) {
			l.add(ai.getCrimeCount());
		}

		return normalize(3, l);
	}

	public static AreaInfo[] getAreaInfos(Crime.Category cat, int perSide, MyGeoArea overallArea) {
		AreaInfo[] results = new AreaInfo[perSide * perSide];

		double latSpan = overallArea.getLatSpan() / perSide;
		double lonSpan = overallArea.getLonSpan() / perSide;

		MyGeoPoint[] centers = overallArea.getCentersOfSubAreas(perSide);

		int seqNo = 0;
		for (MyGeoPoint p : centers) {
			AreaInfo ai = new AreaInfo(new MyGeoArea(p, latSpan, lonSpan));
			ai.populate(cat);
			ai.setSequenceNumber(seqNo++);
			results[ai.getSequenceNumber()] = ai;
		}

		List<Integer> l = new ArrayList<Integer>();
		for (AreaInfo ai : results) {
			l.add(ai.getCrimeCount());
		}

		int[] normalized = normalize(3, l);
		for (AreaInfo ai : results) {
			ai.setNormalizedRating(normalized[ai.getSequenceNumber()]);
		}
		
		return results;
	}

	public static int[] getAreaRatings(double latC, double longC, double width, double height) {
		List<Integer> counts = new ArrayList<Integer>();

		Crime.Category cat = Crime.Category.WALKING;

		counts.add(countInRectangle(cat, latC + height / 3, longC - width / 3, width / 3, height / 3));
		counts.add(countInRectangle(cat, latC + height / 3, longC, width / 3, height / 3));
		counts.add(countInRectangle(cat, latC + height / 3, longC + width / 3, width / 3, height / 3));
		counts.add(countInRectangle(cat, latC, longC - width / 3, width / 3, height / 3));
		counts.add(countInRectangle(cat, latC, longC, width / 3, height / 3));
		counts.add(countInRectangle(cat, latC, longC + width / 3, width / 3, height / 3));
		counts.add(countInRectangle(cat, latC - height / 3, longC - width / 3, width / 3, height / 3));
		counts.add(countInRectangle(cat, latC - height / 3, longC, width / 3, height / 3));
		counts.add(countInRectangle(cat, latC - height / 3, longC + width / 3, width / 3, height / 3));

		return normalize(3, counts);
	}

	public static int[] normalize(int range, List<Integer> values) {
		if (range != 3) {
			// TODO: make this generalizable to any (sane) range
			throw new UnsupportedOperationException("Sorry, I can only handle normalizing to 3 values right now");
		}

		int[] results = new int[values.size()];

		List<Integer> l = new ArrayList<Integer>();
		for (int i : values) {
			l.add(i);
		}
		Collections.sort(l);

		double lowCutoff = (l.get(3) + l.get(2)) * 1.0 / 2;
		double highCutoff = (l.get(6) + l.get(5)) * 1.0 / 2;

		for (int i = 0; i < values.size(); i++) {
			if (values.get(i) < lowCutoff) {
				results[i] = 1;
			} else if (values.get(i) > highCutoff) {
				results[i] = 3;
			} else {
				results[i] = 2;
			}
		}

		return results;
	}
}
