/*
 * Created on Feb 26, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package projects.ecommunicator.tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import projects.ecommunicator.utility.Property;
import projects.ecommunicator.utility.ScoolConstants;
import projects.ecommunicator.utility.Utility;

/**
 * @author anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TextTool extends Text {

	private StringBuffer text;
	private int insertionIndex;
	private Point2D.Float origin;
	private TextLayout textLayout;
	private boolean textEntered;
	private String fontName;
	private String fontNameId;
	private int fontStyle;
	private int fontSize;
	private static final Color STRONG_CARET_COLOR = Color.red;
	private static final Color WEAK_CARET_COLOR = Color.black;

	public TextTool() {
		text = new StringBuffer();
		insertionIndex = text.length();
		origin = new Point2D.Float();
	}

	public void setOrigin(float x, float y) {
		origin.x = x;
		origin.y = y;
	}

	public void computeNextOrigin() {
		origin.y += textLayout.getAscent() + 3;
	}

	public void resetInsertionIndex() {
		insertionIndex = 0;
	}

	public void setTextEntered(boolean textEntered) {
		this.textEntered = textEntered;
	}

	public boolean isTextEntered() {
		return textEntered;
	}

	public void setText(StringBuffer text) {
		this.text = text;
	}

	public StringBuffer getText() {
		return text;
	}

	public void setFontNameId(String fontNameId) {
		this.fontNameId = fontNameId;
		fontName = Property.getString("font", fontNameId);
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public void setFontStyle(int fontStyle) {
		this.fontStyle = fontStyle;
	}

	public void keyPressed(KeyEvent evt) {

		int keyCode = evt.getKeyCode();
		TextHitInfo newPosition = null;
		if (keyCode == KeyEvent.VK_RIGHT) {
			newPosition = textLayout.getNextRightHit(insertionIndex);
			if (newPosition != null) {
				insertionIndex = newPosition.getInsertionIndex();
			}
		} else if (keyCode == KeyEvent.VK_LEFT) {
			newPosition = textLayout.getNextLeftHit(insertionIndex);
			if (newPosition != null) {
				insertionIndex = newPosition.getInsertionIndex();
			}
		} else if (keyCode == KeyEvent.VK_BACK_SPACE) {
			newPosition = textLayout.getNextLeftHit(insertionIndex);
			if (newPosition != null) {
				insertionIndex = newPosition.getInsertionIndex();
				if (text.length() > 0) {
					text.deleteCharAt(insertionIndex);
				}
			}

		} else if (keyCode == KeyEvent.VK_DELETE) {
			if (insertionIndex > 0) {
				newPosition = textLayout.getNextRightHit(insertionIndex - 1);
				if (newPosition != null) {
					insertionIndex = newPosition.getInsertionIndex();
				}
			}
			if (insertionIndex < 0) {
				insertionIndex = 0;
			}
			if (text.length() > 0 && insertionIndex < text.length()) {
				text.deleteCharAt(insertionIndex);
			}
		} else if (keyCode == KeyEvent.VK_SPACE) {
			text.insert(insertionIndex, ' ');
			insertionIndex++;
		} else if (
			(keyCode >= 44 && keyCode <= 93)
				|| (keyCode >= 96 && keyCode <= 111)
				|| keyCode == 192
				|| keyCode == 222) {
			char ch = evt.getKeyChar();
			text.insert(insertionIndex, ch);
			insertionIndex++;
		}
	}

	public void drawComponent(Graphics g) {
		Graphics2D graphics2D = (Graphics2D) g;
		graphics2D.translate(origin.getX(), origin.getY());
		graphics2D.setColor(color);
		/*
		graphics2D.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.setStroke(stroke);
		*/

		if (text.length() > 0) {

			if (!textEntered) {

				Font font = new Font(fontName, fontStyle, fontSize);
				FontRenderContext frc = graphics2D.getFontRenderContext();
				textLayout = new TextLayout(text.toString(), font, frc);
				textLayout.draw(graphics2D, 0, 0);

				Shape[] carets = textLayout.getCaretShapes(insertionIndex);
				graphics2D.setColor(STRONG_CARET_COLOR);

				graphics2D.draw(carets[0]);

				if (carets[1] != null) {
					graphics2D.setColor(WEAK_CARET_COLOR);
					graphics2D.draw(carets[1]);
				}
				LineMetrics metrics = font.getLineMetrics(text.toString(), frc);
				Rectangle2D bounds = textLayout.getBounds();
				bounds.setRect(
					bounds.getX() - 4,
					bounds.getY() - 6,
					bounds.getWidth() + 11,
					bounds.getHeight() + metrics.getAscent());
				graphics2D.setColor(Color.BLACK);
				graphics2D.draw(bounds);
			}
		} else if (text.length() <= 2 || insertionIndex <= 0) {
			Rectangle2D.Double bounds = new Rectangle2D.Double(-3, -15, 5, 22);
			graphics2D.setColor(Color.BLACK);
			graphics2D.draw(bounds);
		}
	}

	public void draw(Graphics g) {
		if (text.length() > 0) {
			/*
			Hashtable map = new Hashtable();
			Font font = new Font(fontName, fontStyle, fontSize);
			map.put(TextAttribute.FONT, font);
			AttributedString attributedString =
				new AttributedString(text.toString(), map);
			AttributedCharacterIterator iterator =
				attributedString.getIterator();
			Graphics2D graphics2D = (Graphics2D) g;
			graphics2D.setColor(color);			
			graphics2D.drawString(
				iterator,
				(float) origin.getX(),
				(float) origin.getY());
			*/

			Graphics2D graphics2D = (Graphics2D) g;
			graphics2D.setColor(color);
			Font font = new Font(fontName, fontStyle, fontSize);
			FontRenderContext frc = graphics2D.getFontRenderContext();
			textLayout = new TextLayout(text.toString(), font, frc);
			textLayout.draw(
				graphics2D,
				(float) origin.getX(),
				(float) origin.getY());

			LineMetrics metrics = font.getLineMetrics(text.toString(), frc);
			Rectangle2D bounds = textLayout.getBounds();
			bounds.setRect(
				bounds.getX() + origin.getX() - 4,
				bounds.getY() + origin.getY() - 6,
				bounds.getWidth() + 11,
				bounds.getHeight() + metrics.getAscent());
			startX = (int) (bounds.getX());
			startY = (int) (bounds.getY());
			width = (int) (bounds.getWidth());
			height = (int) (bounds.getHeight());			
		}
	}

	public String getToolString() {
		System.out.println("TextTool parametres:");
		System.out.println(startX + " " + startY + " " + width + " " + height);

		String toolString =
			Utility.convertTo4Byte(ScoolConstants.TEXT_TOOL)
				+ Utility.convertTo4Byte((int) origin.getX())
				+ Utility.convertTo4Byte((int) origin.getY())
				+ Utility.convertTo4Byte(startX)
				+ Utility.convertTo4Byte(startY)
				+ Utility.convertTo4Byte(width)
				+ Utility.convertTo4Byte(height)
				+ Utility.convertTo3Byte(color.getRed())
				+ Utility.convertTo3Byte(color.getGreen())
				+ Utility.convertTo3Byte(color.getBlue())
				+ stroke.getLineWidth()
				+ fontNameId
				+ Utility.convertTo2Byte(fontSize)
				+ Utility.convertTo2Byte(fontStyle)
				+ text.toString();
		return toolString;
	}

}
