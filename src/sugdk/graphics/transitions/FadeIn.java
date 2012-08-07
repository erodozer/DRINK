package sugdk.graphics.transitions;

import java.awt.Color;

/**
 * @author nhydock
 * FadeIn transitions from the saved buffer to black
 */
public class FadeIn extends FadeToBlack {

	@Override
	protected Color getCurrentAlpha() {
		int index = (int)(timePercentage*(alpha.length-1));
		return alpha[index];
	}

}
