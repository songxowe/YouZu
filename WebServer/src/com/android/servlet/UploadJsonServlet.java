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
	    // 判断是否有文件上传
	    boolean flag = ServletFileUpload.isMultipartContent(request);
	    if (flag) {
	      // 创建临时文件存放位置
	      DiskFileItemFactory factory = new DiskFileItemFactory(10000,
	          new File("/"));
	      // 创建包装的上传文件请求对象
	      ServletFileUpload upload = new ServletFileUpload(factory);
	      // 设置编码
	      upload.setHeaderEncoding("UTF-8");
	      try {
	        // 分离获取上传文件集合
	        List list = upload.parseRequest(request);
	        Iterator<FileItem> it = list.iterator();
	        while (it.hasNext()) {
	          // 获取单个上传文件对象
	          FileItem file = it.next();
	          // 判断是否是文件
	          if (!file.isFormField()) {
	            // 获取文件名
	            String filename = file.getName().substring(
	                file.getName().lastIndexOf("\\") + 1);
	            // 创建文件保存目录
	            String path = "/images";
	            if(name!=null){
	            	path += "/"+name; 
	            }
	            File uploadFile = new File(getServletContext().getRealPath(
	                path));
	            if (!uploadFile.exists()) {
	              uploadFile.mkdir();
	            }
	            // 将文件写入上传目录
	            file.write(new File(uploadFile, filename));
	            // 删除临时文件
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