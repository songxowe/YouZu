package com.android.util;

import java.util.List;

@SuppressWarnings("unchecked")
public class Pager {
  // 设置每页显示
  private static final int size = 8;
  // 总页数
  private int totalpage;
  // 分页数据集合
  private List list;

  public static int getSize() {
    return size;
  }

  public int getTotalpage() {
    return totalpage;
  }

  public void setTotalpage(int totalpage) {
    this.totalpage = totalpage;
  }

  public List getList() {
    return list;
  }

  public void setList(List list) {
    this.list = list;
  }
}
