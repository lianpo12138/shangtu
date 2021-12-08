package com.dubu.turnover.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SortList {
  public  static List sortList(List list,List del,Integer num,Integer optionNum){
	     Integer sort=1;
		 if(num<optionNum){
			 sort= num;
		 }else{
			 sort= num%optionNum;
		 }
		 int i=1;
		 int j=0;
		 List sortList=new ArrayList();
		 List delList=new ArrayList();
		 Iterator iterator = list.iterator();
		 while(iterator.hasNext()){
		    	Object object = iterator.next();
		        if(sort==1){
		        	list.addAll(del);
				    return list;
		        }
				 if((i>=sort && sort>1) || (sort==0 && i==optionNum)){
					 iterator.remove();
					 sortList.add(object);
				 }else{
					 delList.add(object);
				 }
				 i++;
				 j++;
		}
		sortList.addAll(delList);
		sortList.addAll(del);
	  	return sortList;
	  
  }
  
  public static void main(String args[]) { 
	  List list=new ArrayList();
	  list.add("1");
	  list.add("2");
	  list.add("3");
	  list.add("4");
	  SortList.sortList(list,null ,0, 4);
  } 
}
