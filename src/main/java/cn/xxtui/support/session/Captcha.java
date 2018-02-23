package cn.xxtui.support.session;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.Random;
import javax.imageio.ImageIO;

public class Captcha  {
	private StringBuilder code;// 对应的验证码

	private OutputStream outputStream;

	public Captcha()
	{
		StringBuilder randomStr=new StringBuilder();
		for (int i = 1; i <= stringNum; i++) {
			String rand = String.valueOf(getRandomString(random.nextInt(this.randString
					.length())));
			randomStr.append(rand);
		}
		this.code=randomStr;
	}
	
	
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public String getCode() {
		return code.toString();
	}

	private Random random = new Random();
	private String randString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
   
	private int width = 80;
	private int height = 34;
	private int lineSize = 20;
	private int stringNum = 5;

	private Color getRandColor(int fc, int bc) {
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc - 16);
		int g = fc + random.nextInt(bc - fc - 14);
		int b = fc + random.nextInt(bc - fc - 18);
		return new Color(r, g, b);
	}
	public void gen() {
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_BGR);
		Graphics g = image.getGraphics();
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 18));
		g.setColor(getRandColor(110, 133));
		for (int i = 0; i <= lineSize; i++) {
			drowLine(g);
		}
		for (int i = 0; i <stringNum; i++) {
			drowString(g, String.valueOf(this.code.charAt(i)), i);
		}
		g.dispose();
		try {
			ImageIO.write(image, "JPEG", outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private String drowString(Graphics g, String rand, int i) {
		g.setFont(new Font("Fixedsys", Font.CENTER_BASELINE, 23));
		g.setColor(new Color(random.nextInt(101), random.nextInt(111), random
				.nextInt(121)));
		g.translate(random.nextInt(5), random.nextInt(5));
		g.drawString(rand,10 * (i+1),17+(int)(5*Math.sin(random.nextInt(10))));
		return rand;
	}
	private void drowLine(Graphics g) {
		int x = random.nextInt(width);
		int y = random.nextInt(height);
		int xl = random.nextInt(13);
		int yl = random.nextInt(15);
		g.drawLine(x, y, x + xl, y + yl);
	}
	private String getRandomString(int num) {
		return String.valueOf(randString.charAt(num));
	}
}
