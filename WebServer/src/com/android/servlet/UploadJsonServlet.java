package com.android.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadJsonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	  public void doGet(HttpServletRequest request, HttpServletResponse response)
	      throws ServletException, IOException {

	    doPost(request, response);
	  }

	  @SuppressWarnings("unchecked")
	  public void doPost(HttpServletRequest request, HttpServletResponse response)
	      throws ServletException, IOException {
	    String result = "";
	    String name = request.getParameter("name");
	    //System.out.println(name);
	    // �ж��Ƿ����ļ��ϴ�
	    boolean flag = ServletFileUpload.isMultipartContent(request);
	    if (flag) {
	      // ������ʱ�ļ����λ��
	      DiskFileItemFactory factory = new DiskFileItemFactory(10000,
	          new File("/"));
	      // ������װ���ϴ��ļ��������
	      ServletFileUpload upload = new ServletFileUpload(factory);
	      // ���ñ���
	      upload.setHeaderEncoding("UTF-8");
	      try {
	        // �����ȡ�ϴ��ļ�����
	        List list = upload.parseRequest(request);
	        Iterator<FileItem> it = list.iterator();
	        while (it.hasNext()) {
	          // ��ȡ�����ϴ��ļ�����
	          FileItem file = it.next();
	          // �ж��Ƿ����ļ�
	          if (!file.isFormField()) {
	            // ��ȡ�ļ���
	            String filename = file.getName().substring(
	                file.getName().lastIndexOf("\\") + 1);
	            // �����ļ�����Ŀ¼
	            String path = "/images";
	            if(name!=null){
	            	path += "/"+name; 
	            }
	            File uploadFile = new File(getServletContext().getRealPath(
	                path));
	            if (!uploadFile.exists()) {
	              uploadFile.mkdir();
	            }
	            // ���ļ�д���ϴ�Ŀ¼
	            file.write(new File(uploadFile, filename));
	            // ɾ����ʱ�ļ�
	            file.delete();
	          }
	        }
	        result = "{'flag':'ok'}";
	      } catch (Exception e) {
	        result = "{'flag':'error'}";
	        e.printStackTrace();
	      }
	    }
	    PrintWriter out = response.getWriter();
	    out.println(result);
	    out.flush();
	    out.close();
	  }
	}