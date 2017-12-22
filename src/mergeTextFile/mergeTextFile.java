package mergeTextFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class mergeTextFile {
			public static void main(String[] args) throws Exception {
				//findAllFiles(new File("F:\\workspace\\cutTextFile\\src1"),new ArrayList<List<File>>());
				mergeTextFile();
			}
	
	/**
	 * 合并文件		
	 * @throws Exception 
	 */
	 public static void mergeTextFile() throws Exception{
			String s_srcDir=ReadConfigUtil.getSrcDir();
			File srcDir=new File(s_srcDir);
			if(!srcDir.exists()){
				srcDir.mkdirs();
			}
			
			zipFileWrite(srcDir);
			
			ArrayList<List<File>> allFiles = new ArrayList<List<File>>();
			findAllFiles(srcDir, allFiles);
			
			for(List<File> dirFile:allFiles){
				mergeDirFile(dirFile);
			}
			
	}
	
	 private static void mergeDirFile(List<File> dirFile) throws Exception{
		 String fileName=null;
		 PrintWriter out=null;
		 for(File file:dirFile){
			 if(fileName==null){
				 fileName=file.getAbsolutePath().replace(ReadConfigUtil.getSrcDir(), ReadConfigUtil.getTargetDir());
				 String  fPath=fileName.substring(0, fileName.lastIndexOf(File.separator)) ;
				 File dir=new File(fPath);
				 if(!dir.exists()){
					 dir.mkdirs();
				 }
				 out=new PrintWriter(fileName, ReadConfigUtil.getTargetCharset());
			 }
			 mergeToFile(file, out);
		 }
		if(out!=null)out.close();
	 }
	 
	 private static void mergeToFile(File file,PrintWriter out) throws Exception{
		 BufferedReader read=new BufferedReader(
				 new InputStreamReader(
						 new FileInputStream(file), ReadConfigUtil.getSrcCharset()));
		 String lineStr="";
		 while((lineStr=read.readLine())!=null){
			 out.println(lineStr);
		 }
		read.close();
	 }
	 
	/**
	 * 找到所有文件，以及子目录的文件
	 * @param dir
	 * @param allFiles
	 */
	private static void findAllFiles(File dir,List<List<File>> allFiles){
		File[] files=dir.listFiles();
		List<File> merginFiles=new ArrayList<File>();
		allFiles.add(merginFiles);
		for(File file:files){
			if(file.isFile()&&file.getAbsolutePath().endsWith(ReadConfigUtil.getFileSuffix())){
				merginFiles.add(file);
			}else if(file.isDirectory()){
				findAllFiles(file,allFiles);
			}
		}
	}
	
	/**
	 * 找出所有的zip文件，以及子目录的zip文件
	 * @throws IOException 
	 * @throws ZipException 
	 */
	private static void ZipText(File dir,List<File> allZipFiles){
		File[] files=dir.listFiles();
		for(File file:files){
			if(file.isFile()&&file.getAbsolutePath().toUpperCase().endsWith(".ZIP")){
				allZipFiles.add(file);
			}else if(file.isDirectory()){
				ZipText(file,allZipFiles);
			}
		}
	}
	
	/**
	 * 解压所有找到的zip文件
	 * @param allZipFiles
	 * @throws ZipException
	 * @throws IOException
	 */
	private static void zipFileWrite(File dir) throws Exception{
		List<File> allZipFiles=new ArrayList<File>();
		ZipText(dir, allZipFiles);
		for(File file:allZipFiles){
			ZipFile zf=new ZipFile(file,ZipFile.OPEN_READ);
			writeZipText(zf,file);
			zf.close();
			file.delete();
		}
	}
	
	/**
	 * 解压一个zip文件
	 * @param zipFile
	 * @param file
	 * @throws IOException
	 */
	private static void writeZipText(ZipFile zipFile,File file) throws IOException{
		Enumeration<? extends ZipEntry> zf = zipFile.entries();
		ZipEntry ze=null;
		
		while(zf.hasMoreElements()){
			String textFile=file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf(File.separator)+1);
			ze=zf.nextElement();
			BufferedInputStream in=new BufferedInputStream(zipFile.getInputStream(ze));
			BufferedOutputStream out= new BufferedOutputStream(new FileOutputStream(
					textFile+ze.getName()));
			byte[] buffer=new byte[1024*1024];
			int size=0;
			while((size=in.read(buffer))>0){
				out.write(buffer, 0, size);
			}
			
			out.close();
			in.close();
		}
	}
}
