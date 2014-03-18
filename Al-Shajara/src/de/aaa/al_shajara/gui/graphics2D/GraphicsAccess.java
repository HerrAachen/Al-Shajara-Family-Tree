package de.aaa.al_shajara.gui.graphics2D;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Paint;

public interface GraphicsAccess {

	public void drawString(String string, float x, float y);
	public void drawRect(int top, int left, int width, int height);
	public FontMetrics getFontMetrics();
	public void setFont(Font font);
	public void setPaint(Paint paint);
}
