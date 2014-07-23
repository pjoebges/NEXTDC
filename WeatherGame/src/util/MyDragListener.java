package util;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnTouchListener;

public class MyDragListener implements OnTouchListener {
	/**
	 * OnTouchListener-Klasse für den "Drag-Teil" von Drag&Drop
	 */

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public boolean onTouch(View myView, MotionEvent myEvent) {
		/**
		 * Wenn das Element gezogen wird...
		 */
		if (myEvent.getAction() == MotionEvent.ACTION_DOWN) {
			/**
			 * ...erzeuge den Schatten und folge damit den Benutzereingaben.
			 */
			ClipData data = ClipData.newPlainText("", "");
			DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(myView);
			myView.startDrag(data, shadowBuilder, myView, 0);
			return true;
		} else {
			return false;
		}

	}

}
