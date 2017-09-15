package com.android.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * ������ ��������ͼƬ��֤��
 * 
 * @author Nick
 * 
 */
public class RandomCode {
  /**
   * ���ȡ��һ������
   * 
   * @param Random
   *                random �����
   * @return Font ����һ��������
   */
  private synchronized Font getsFont(Random random) {
    return new Font("Fixedsys", Font.CENTER_BASELINE, 18);
  }

  /**
   * ����һ�������ɫ
   * 
   * @param int
   *                fc �����
   * @param int
   *                bc �����
   * @param Random
   *                random �����
   * @return Color ����һ������ɫ
   */
  synchronized Color getRandColor(int fc, int bc, Random random) {
    if (fc > 255)
      fc = 255;
    if (bc > 255)
      bc = 255;
    int r = fc + random.nextInt(bc - fc - 6);
    int g = fc + random.nextInt(bc - fc - 4);
    int b = fc + random.nextInt(bc - fc - 8);
    return new Color(r, g, b);
  }

  /**
   * ���������ͼƬ
   */
  public synchronized void getRandcode(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    System.setProperty("java.awt.headless", "true");
    HttpSession session = request.getSession();
    int width = 80, height = 26;// ����ͼƬ��С
    BufferedImage image = new BufferedImage(width, height,
        BufferedImage.TYPE_INT_RGB);
    Graphics g = image.getGraphics();
    Random random = new Random();
    g.fillRect(0, 0, width, height);// �趨�߿�
    g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 18));
    g.setColor(getRandColor(111, 133, random));
    // ���������(������)
    for (int i = 0; i < 30; i++) {
      int x = random.nextInt(width);
      int y = random.nextInt(height);
      int xl = random.nextInt(13);
      int yl = random.nextInt(15);
      g.drawLine(x, y, x + xl, y + yl);
    }
    // ���������
    g.setColor(getRandColor(130, 150, random));
    // ����5�������
    String sRand = "";
    for (int i = 0; i < 5; i++) {
      g.setFont(getsFont(random));
      g.setColor(new Color(random.nextInt(101), random.nextInt(111), random
          .nextInt(121)));
      //String rand=String.valueOf(getRandomString(random.nextInt(36)));
      String rand = String.valueOf(getRandomString(random.nextInt(10)));
      sRand += rand;
      g.translate(random.nextInt(3), random.nextInt(3));
      g.drawString(rand, 13 * i, 16);
    }
    session.removeAttribute("Rand");
    session.setAttribute("Rand", sRand);
    g.dispose();
    ImageIO.write(image, "JPEG", response.getOutputStream());
  }

  public synchronized String getRandomString(int num) {
    String randstring = "0123456789";
    //String randstring = "0123456789abcdefghijklmnopqrstuvwxyz";
    return String.valueOf(randstring.charAt(num));
  }
}
