package sugdk.graphics.transitions;

import java.awt.Color;

/**
 * @author nhydock
 * FadeOut fades from black to the new buffer
 */
public class FadeOut extends FadeToBlack {

	@Override
	protected Color getCurrentAlpha() {
		int index = (alpha.length-1) - (int)(timePercentage*(alpha.length-1));
		return alpha[index];
	}

}
