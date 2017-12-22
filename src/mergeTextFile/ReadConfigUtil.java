package mergeTextFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;


public class ReadConfigUtil {
	
	private static  Properties p=new Properties();
	
	static{
		try {
			InputStream in=new FileInputStream("merge.properties");
			 p.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getSrcDir(){
		return isBlank(p.getProperty("srcDir"))?"srcDir":p.getProperty("srcDir");
	}
	
	public static String getTargetDir(){
		return isBlank(p.getProperty("targetDir"))?"targetDir":p.getProperty("targetDir");
	}
	
	public static String getFileSuffix(){
		return isBlank(p.getProperty("fileSuffix"))?"fileSuffix":p.getProperty("fileSuffix");
	}
	
	public static String getTargetCharset(){
		return isBlank(p.getProperty("targetCharset"))?"GBK":p.getProperty("targetCharset");
	}
	
	public static String getSrcCharset(){
		return isBlank(p.getProperty("srcCharset"))?"GBK":p.getProperty("srcCharset");
	}
	
	private static boolean isBlank(String str){
		if(str==null||"".equals(str.trim())){
			return true;
		}
		return false;
	}
}
