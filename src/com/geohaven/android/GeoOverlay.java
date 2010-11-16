package com.geohaven.android;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class GeoOverlay extends ItemizedOverlay<OverlayItem> {
	private Context context;
	private ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();

	public GeoOverlay(Drawable defaultMarker) {
		super(boundCenter(defaultMarker));
	}

	public GeoOverlay(Drawable defaultMarker, Context context) {
		super(boundCenter(defaultMarker));
		this.context = context;
	}

	public void addOverlay(OverlayItem overlay) {
		overlays.add(overlay);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return overlays.get(i);
	}

	@Override
	public int size() {
		return overlays.size();
	}

	@Override
	public void draw(android.graphics.Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, false);
	}

	@Override
	protected boolean onTap(int index) {
		OverlayItem item = overlays.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		return true;
	}
}
