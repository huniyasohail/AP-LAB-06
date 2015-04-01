import java.util.*;
import java.io.*;
 
public class SearchFile {
 
  private FileList workQ;
  static int i = 0;
 
 private class Search implements Runnable {
 
  private FileList list;
 
  public Search(FileList q) {
   list = q;
  }

//  since main thread has placed all directo
  //ries into the workQ, we
//  know that all of them are legal directories; therefore, do not need
//  to try ... catch in the while loop below
 
  public void run() {
   String name;
   while ((name = list.remove()) != null) {
    File file = new File(name);
    String entries[] = file.list();
    if (entries == null)
     continue;
    for (String entry : entries) {
     if (entry.compareTo(".") == 0)
      continue;
     if (entry.compareTo("..") == 0)
      continue;
     String fn = name + "\\" + entry;
     System.out.println(fn);
    }
   }
  }
 }
 
 public SearchFile() {
  workQ = new FileList();
 }
 
 public Search StartSearch() {
  return new Search(workQ);
 }
 
 
// need try ... catch below in case the directory is not legal
 
 public void SearchDirectory(String dir) {
   try{
   File file = new File(dir);
   if (file.isDirectory()) {
    String files[] = file.list();
    if (files != null)
     workQ.add(dir);
 
    for (String entry : files) {
     String subdir;
     if (entry.compareTo(".") == 0)
      continue;
     if (entry.compareTo("..") == 0)
      continue;
     if (dir.endsWith("\\"))
      subdir = dir+entry;
     else
      subdir = dir+"\\"+entry;
     SearchDirectory(subdir);
    }
   }}catch(Exception e){}
 }
 
 public static void main(String Args[]) {
 
  SearchFile fc = new SearchFile();
 
//  now start all of the worker threads
 
  int N = 5;
  ArrayList<Thread> thread = new ArrayList<Thread>(N);
  for (int i = 0; i < N; i++) {
   Thread t = new Thread(fc.StartSearch());
   thread.add(t);
   t.start();
  }
 
//  now place each directory into the workQ

  fc.SearchDirectory("C:/Users/abbu.Abbucomputer/Desktop/");
 
//  indicate that there are no more directories to add
 
  fc.workQ.finish();
 
  for (int i = 0; i < N; i++){
   try {
    thread.get(i).join();
   } catch (Exception e) {};
  }
 }
}
