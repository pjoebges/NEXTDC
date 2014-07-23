package util;

import android.annotation.TargetApi;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.TextView;
import android.widget.Toast;

import com.dhbw.wetterSpiel.SpielActivity;
import com.dhbw.wetterSpiel.R;

/**
 * OnDragListener-Klasse für den "Drop-Teil" von Drag&Drop
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MyDropListener implements OnDragListener {

	static SpielActivity mySpielActivity;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public boolean onDrag(View myView, DragEvent myEvent) {
		switch (myEvent.getAction()) {
		case DragEvent.ACTION_DRAG_STARTED:
			break;
		case DragEvent.ACTION_DRAG_ENTERED:
			break;
		case DragEvent.ACTION_DRAG_EXITED:
			break;
		case DragEvent.ACTION_DROP:
			/**
			 * Herausfinden, welches Element gezogen und auf welchem abgelegt
			 * wurde.
			 */
			View view = (View) myEvent.getLocalState();
			TextView dropTarget = (TextView) myView;
			TextView dropped = (TextView) view;
			int currentContent_1 = Integer.parseInt((String) dropTarget
					.getText());
			String currentContent_2 = null;
			try{
				currentContent_2 = (String) dropped.getText();
			}catch(Exception e){
				Log.i("MyDropListener", e.getStackTrace().toString());
			}
			
			/**
			 * Prüfung, ob die Daten stimmen. Wenn ja, Temperatur-TextView
			 * unsichtbar machen.
			 */
			if (SpielActivity.checkValues(currentContent_2, currentContent_1)) {
				view.setVisibility(View.INVISIBLE);
				dropTarget.setText(dropped.getText());
				dropTarget.setTypeface(Typeface.DEFAULT_BOLD);
			
				/**
				 * Zähle die Variable hoch
				 */
				SpielActivity.correctAssignments++;
			}else{
				/**
				 * Bei falscher Zuordnung soll als Rückmeldung ein Toast ausgegeben werden.
				 */
				Toast toast = Toast.makeText(SpielActivity.context, R.string.falsche_Zuordnung, Toast.LENGTH_SHORT);
				toast.show();
			}
			break;
		case DragEvent.ACTION_DRAG_ENDED:
			break;
		default:
			break;
		}
		return true;
	}
}
