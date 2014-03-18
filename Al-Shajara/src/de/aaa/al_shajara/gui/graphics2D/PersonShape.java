package de.aaa.al_shajara.gui.graphics2D;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import de.aaa.al_shajara.data.Person;
import de.aaa.al_shajara.gui.GuiHelper;

public class PersonShape {

  private static final int horizontalMargin = 10;
  private static final int verticalMargin = 10;
  
  int NON_ACTIVE = Integer.MIN_VALUE;
  
	private final Person p;
	private int top = NON_ACTIVE;
	private int left = NON_ACTIVE;
	
	private int centerX = NON_ACTIVE;
	private int centerY = NON_ACTIVE;

	private List<String> lines2Draw;
	private int width;
	private int height;
	private int fontHeight;
	private Font font = new Font("sansserif", Font.PLAIN, 12);

	public PersonShape(Person p) {
		this.p = p;
	}
	
	public void setTopLeft(int left, int top) {
	    this.centerY = NON_ACTIVE;
	    this.centerX = NON_ACTIVE;
		this.top = top;
		this.left = left;
	}

	  public void setCenter(int x, int y) {
	    this.left = NON_ACTIVE;
	    this.top = NON_ACTIVE;
	    this.centerX = x;
	    this.centerY = y;
	  }

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	/**
	 * Precalculates all values needed for painting this shape
	 */
	public void calculateDrawValues(){
	  FontMetrics fontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(font);
		fontHeight = fontMetrics.getHeight();
		lines2Draw = getLines2Draw(p);
		int maxWidth = getMaxWidth(fontMetrics, lines2Draw);
		width = maxWidth+2*horizontalMargin;
		height = fontHeight*lines2Draw.size() + 2*verticalMargin;
	}
	
	public void paint(GraphicsAccess g){
	  int topPos;
	  int leftPos;
	  if (this.top!=NON_ACTIVE){
	    topPos = top;
	    leftPos = left;
	  }
	  else {
	    topPos = centerY-(height/2);
	    leftPos = centerX - (width/2);
	  }
	  Font oldFont = g.getFontMetrics().getFont();
	  g.setFont(font);
		int lineIndex=0;
		for(String line: lines2Draw){
			g.drawString(line, leftPos+horizontalMargin, topPos+verticalMargin+(lineIndex+1)*fontHeight);
			lineIndex++;
		}
		g.drawRect(leftPos, topPos, width, height);
		g.setFont(oldFont);
	}

	
	/**
	 * Returns the list of lines that shall be displayed for the
	 * specified person
	 * @param p the person to be displayed
	 * @return
	 */
	private List<String> getLines2Draw(Person p){
		List<String> lines = new LinkedList<String>();
		lines.add(p.getFullName());
//		if (p.getLastNameSecondaryWritingSystem()!=null && !p.getLastNameSecondaryWritingSystem().isEmpty()){
//			lines.add(p.getFullNameSecondaryWritingSystem());
//		}
		StringBuilder birthday = new StringBuilder().append("*");
		if (p.getBirthday()!= null){
			birthday.append(" ").append(p.getBirthday().toString(Locale.GERMAN));
		}
		String countryOfBirth = p.getCountryOfBirth();
		String cityOfBirth = p.getCityOfBirth();
		if (cityOfBirth!= null || countryOfBirth!=null){
			birthday.append(" in ");
			if (cityOfBirth!=null){
				birthday.append(cityOfBirth);
			}
			if (countryOfBirth!=null){
				birthday.append(", ").append(countryOfBirth);
			}
		}
		lines.add(birthday.toString());
		return lines;
	}
	
	/**
	 * Returns the width of the widest line of the specified strings when displaying
	 * them with the specified FontMetrics
	 * @param fontMetrics
	 * @param lines
	 * @return
	 */
	private int getMaxWidth(FontMetrics fontMetrics, List<String> lines){
		int maxWidth = -1;
		for(String line: lines){
//			int width = fontMetrics.charsWidth(line.toCharArray(), 0, line.length());
//			int width = fontMetrics.stringWidth(line);
		  int width = calculateStringWidth(fontMetrics, line);
			if (maxWidth<width)
				maxWidth=width;
		}
		return maxWidth;
	}
	
	private int calculateStringWidth(FontMetrics fm, String string){
	  int sum = 0;
	  for(int i=0;i<string.length();i++){
	    char ch = string.charAt(i);
	    sum += fm.charWidth(ch);
	  }
	  return sum;
	}
	
//	private static class RegularFontMetrics extends FontMetrics {
//      protected RegularFontMetrics(Font font) {
//        super(font);
//      }
//	}
}
