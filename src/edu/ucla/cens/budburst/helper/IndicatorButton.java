package edu.ucla.cens.budburst.helper;

import edu.ucla.cens.budburst.R;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;

public class IndicatorButton extends Button{
	private Drawable separator;

	private static final String TAG = "selectedButton";

	public IndicatorButton(Context context) {
		this(context, null, 0);
	}

	public IndicatorButton(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}

	public IndicatorButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		separator = getResources().getDrawable(R.drawable.btn_strip_divider);
		setBackgroundDrawable(getResources().getDrawable(R.drawable.button_bar));

		setCompoundDrawablesWithIntrinsicBounds(getSelectedDrawable(), null, separator, null);

		//will be centered automatically? so lets just default to the left. (saves some space)
		this.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
		
	}
	
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
		this.setSelected(focused);
	}



	protected Drawable getSelectedDrawable() {
		return getResources().getDrawable(isSelected()?R.drawable.btn_strip_mark_on : R.drawable.btn_strip_mark_off);
	}

	@Override
	protected void drawableStateChanged () {
		super.drawableStateChanged();

		setCompoundDrawablesWithIntrinsicBounds(getSelectedDrawable(), null, separator, null);

	}

}
