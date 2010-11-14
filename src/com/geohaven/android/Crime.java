package com.geohaven.android;

import java.util.HashSet;
import java.util.Set;

public class Crime {

	public enum Category {
		WALKING("Walking"), DRIVING("Driving"), SPENDING_THE_NIGHT("Spending the Night"), LIVING_THERE("Living There");

		private Set<Type> types = new HashSet<Type>();
		private String name;

		private Category(String n) {
			name = n;
		}

		private void addType(Type t) {
			types.add(t);
		}

		public Set<Type> getTypes() {
			return types;
		}
		
		public String getName() {
			return name;
		}
	}

	{
		Category.WALKING.addType(Type.DISORDERLY_CONDUCT);
		Category.WALKING.addType(Type.DRUNKENNESS);
		Category.WALKING.addType(Type.FELONY_ASSAULT);
		Category.WALKING.addType(Type.FORCIBLE_RAPE);
		Category.WALKING.addType(Type.KIDNAPPING);
		Category.WALKING.addType(Type.MISDEMEANOR_ASSAULT);
		Category.WALKING.addType(Type.OTHER_SEX_OFFENSES);
		Category.WALKING.addType(Type.PROSTITUTION);
		Category.WALKING.addType(Type.VANDALISM);

		Category.DRIVING.addType(Type.BURG_AUTO);
		Category.DRIVING.addType(Type.DUI);
		Category.DRIVING.addType(Type.GRAND_THEFT);
		Category.DRIVING.addType(Type.STOLEN_VEHICLE);

		Category.SPENDING_THE_NIGHT.addType(Type.BURG_RESIDENTIAL);
		Category.SPENDING_THE_NIGHT.addType(Type.BURG_OTHER);
		Category.SPENDING_THE_NIGHT.addType(Type.ARSON);
		Category.SPENDING_THE_NIGHT.addType(Type.DOMESTIC_VIOLENCE);
		Category.SPENDING_THE_NIGHT.addType(Type.KIDNAPPING);
		Category.SPENDING_THE_NIGHT.addType(Type.HOMICIDE);
		Category.SPENDING_THE_NIGHT.addType(Type.PETTY_THEFT);
		Category.SPENDING_THE_NIGHT.addType(Type.WEAPONS);

		Category.LIVING_THERE.addType(Type.ARSON);
		Category.LIVING_THERE.addType(Type.BURG_RESIDENTIAL);
		Category.LIVING_THERE.addType(Type.FELONY_ASSAULT);
		Category.LIVING_THERE.addType(Type.FORCIBLE_RAPE);
		Category.LIVING_THERE.addType(Type.GRAND_THEFT);
		Category.LIVING_THERE.addType(Type.NARCOTICS);
		Category.LIVING_THERE.addType(Type.ROBBERY);
		Category.LIVING_THERE.addType(Type.VANDALISM);
	}

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
//	private Date datetime;
	// private String casenumber;
	// private String description;
	// private String policebeat;
	// private String address;
	// private String googleAddress;
	private double latitude;
	private double longitude;

	public Crime(Type type,
//			Date datetime,
	// String casenumber, String description, String policebeat, String address,
			// String googleAddress,
			double latitude, double longitude) {

		this.type = type;
//		this.datetime = datetime;
		
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

		if (left > right) { // special case, spanning the international date
			// line!
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
		return type
//		+ " AT:" + datetime
		+ " LAT:" + latitude + " LONG:" + longitude;
	}
}
