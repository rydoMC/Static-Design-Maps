package Jung;

import java.awt.Color;
import java.awt.Paint;

import org.apache.commons.collections15.Transformer;

/**
 * This a a customer transformer class which allows the edges
 * colour to change on a graph. It works by evaluating the colour from
 * darkest red to a lighter pink. If the edge is not considered significant
 * it will return grey.
 *
 * @author Ryan McNulty
 * @date 13 Feb 2012
 * @organisation Computer and Information Science, Strathclyde University, Glasgow, Scotland.
 */
public class RedColourRenderer implements Transformer<String, Paint>{
	
	
	public RedColourRenderer(){}

	@Override
	public Paint transform(String arg0) {
		int edgePriority = Integer.parseInt(arg0.substring(0,arg0.indexOf("_")));
		
		// 1 - 5 shows they are an important relationship.
		if(edgePriority > 5){
			// Return grey
			return new Color(170,170,170);
		}
		else {
			int adjust = 45*edgePriority;
			int red = 255;
			
			// Return a shade of red
			return new Color(red,adjust,adjust);
		}
	}
}
