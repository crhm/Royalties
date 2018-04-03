package gui.royaltyholders;

/*Taken from https://tips4java.wordpress.com/2008/10/11/table-format-renderers/
 * Used to render the royalty holder balance column as currency (with a dollar sign) and yet be able to sort it
 * as a number (double) and not a string.
 * Modified so that it always uses US locale defaults (currency = USD, etc...)
 */


import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class NumberRenderer extends FormatRenderer {
	/*
	 *  Use the specified number formatter and right align the text
	 */
	public NumberRenderer(NumberFormat formatter) {
		super(formatter);
		setHorizontalAlignment(SwingConstants.RIGHT);
	}

	/*
	 *  Use the default currency formatter for the US locale
	 */
	public static NumberRenderer getCurrencyRenderer() {
		return new NumberRenderer( NumberFormat.getCurrencyInstance(Locale.US));
	}

	/*
	 *  Use the default integer formatter for the US locale
	 */
	public static NumberRenderer getIntegerRenderer() {
		return new NumberRenderer( NumberFormat.getIntegerInstance(Locale.US));
	}

	/*
	 *  Use the default percent formatter for the US locale
	 */
	public static NumberRenderer getPercentRenderer() {
		return new NumberRenderer( NumberFormat.getPercentInstance(Locale.US));
	}
}
