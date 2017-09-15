package com.android.util;

import java.security.MessageDigest;

public class MD5 {

	/**
	   * ���ܺ��ַ���
	   * 
	   * @param source
	   * @return
	   */
	  public static String getMD5(String source) {
	    String s = null;
	    // �������ֽ�ת���� 16 ���Ʊ�ʾ���ַ�
	    char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
	        'b', 'c', 'd', 'e', 'f' };
	    try {
	      // MessageDigest ��ΪӦ�ó����ṩ��ϢժҪ�㷨�Ĺ��ܣ��� MD5 �� SHA �㷨
	      // ��ϢժҪ�ǰ�ȫ�ĵ����ϣ�����������������С�����ݣ�������̶����ȵĹ�ϣֵ
	      MessageDigest md5 = MessageDigest.getInstance("MD5");
	      md5.update(source.getBytes());

	      // MD5 �ļ�������һ�� 128 λ�ĳ�����,���ֽڱ�ʾ���� 16 ���ֽ�
	      byte[] tmp = md5.digest();
	      // ÿ���ֽ��� 16 ���Ʊ�ʾ�Ļ���ʹ�������ַ������Ա�ʾ�� 16 ������Ҫ 32���ַ�
	      char[] str = new char[16 * 2];
	      // ��ʾת������ж�Ӧ���ַ�λ��
	      int k = 0;
	      // �ӵ�һ���ֽڿ�ʼ���� MD5 ��ÿһ���ֽ�ת���� 16 �����ַ���ת��
	      for (int i = 0; i < 16; i++) {
	        byte b = tmp[i];// ȡ�� i ���ֽ�

	        // ȡ�ֽ��и� 4 λ������ת��, >>> Ϊ�߼����ƣ�������λһ������
	        str[k++] = hexDigits[b >>> 4 & 0xf];
	        // ȡ�ֽ��е� 4 λ������ת��
	        str[k++] = hexDigits[b & 0xf];
	      }
	      // ����Ľ��ת��Ϊ�ַ���
	      s = new String(str);

	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return s;
	  }
}
