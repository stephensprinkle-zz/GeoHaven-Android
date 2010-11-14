package com.geohaven.android;

import java.util.Date;

public class Crime {

	public enum Type {

		ARSON("ARSON"), BURG_AUTO("BURG - AUTO"), BURG_COMMERCIAL("BURG - COMMERCIAL"), BURG_OTHER("BURG - OTHER"), BURG_RESIDENTIAL(
				"BURG - RESIDENTIAL"), CHILD_ABUSE("CHILD ABUSE"), DISORDERLY_CONDUCT("DISORDERLY CONDUCT"), DOMESTIC_VIOLENCE(
				"DOMESTIC VIOLENCE"), DRUNKENNESS("DRUNKENNESS"), DUI("DUI"), EMBEZZLEMENT("EMBEZZLEMENT"), FELONY_ASSAULT(
				"FELONY ASSAULT"), FELONY_WARRANT("FELONY WARRANT"), FORCIBLE_RAPE("FORCIBLE RAPE"), FORGERY_AND_COUNTERFEITING(
				"FORGERY & COUNTERFEITING"), FRAUD("FRAUD"), GRAND_THEFT("GRAND THEFT"), HOMICIDE("HOMICIDE"), INCIDENT_TYPE(
				"INCIDENT TYPE"), KIDNAPPING("KIDNAPPING"), MISCELLANEOUS_TRAFFIC_CRIME("MISCELLANEOUS TRAFFIC CRIME"), MISDEMEANOR_ASSAULT(
				"MISDEMEANOR ASSAULT"), MISDEMEANOR_WARRANT("MISDEMEANOR WARRANT"), NARCOTICS("NARCOTICS"), OTHER("OTHER"), OTHER_SEX_OFFENSES(
				"OTHER SEX OFFENSES"), PETTY_THEFT("PETTY THEFT"), POSSESSION_STOLEN_PROPERTY("POSSESSION - STOLEN PROPERTY"), PROSTITUTION(
				"PROSTITUTION"), RECOVERED_O_S_STOLEN("RECOVERED O/S STOLEN"), RECOVERED_VEHICLE_OAKLAND_STOLEN(
				"RECOVERED VEHICLE - OAKLAND STOLEN"), ROBBERY("ROBBERY"), STOLEN_AND_RECOVERED_VEHICLE(
				"STOLEN AND RECOVERED VEHICLE"), STOLEN_VEHICLE("STOLEN VEHICLE"), THREATS("THREATS"), TOWED_VEHICLE(
				"TOWED VEHICLE"), VANDALISM("VANDALISM"), WEAPONS("WEAPONS");

		private String name;

		private Type(String name) {
			this.name = name;
		}

		public static Type getByKey(String key) {
			for (Type t : Type.values()) {
				if (t.name.equals(key)) {
					return t;
				}
			}

			return null; // should never happen!
		}
	}

	private Type type;
	private Date datetime;
	// private String casenumber;
	// private String description;
	// private String policebeat;
	// private String address;
	// private String googleAddress;
	private double latitude;
	private double longitude;

	public Crime(Type type, Date datetime,
	// String casenumber, String description, String policebeat, String address,
			// String googleAddress,
			double latitude, double longitude) {

		this.type = type;
		this.datetime = datetime;
		// this.casenumber = casenumber;
		// this.description = description;
		// this.policebeat = policebeat;
		// this.address = address;
		// this.googleAddress = googleAddress;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public boolean isInRect(double left, double right, double top, double bottom) {
		if (latitude > top || latitude < bottom) {
			return false;
		}

		if (left > right) { // special case, spanning the international date line!
			if (longitude > left || longitude < right) {
				return false;
			}
		} else {
			if (longitude < left || longitude > right) {
				return false;
			}
		}
		
		return true;
	}

	public String toString() {
		return type + " AT:" + datetime + " LAT:" + latitude + " LONG:" + longitude;
	}
}
