package com.cybercloud.sprbotfreedom.platform.util;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * @author yh
 * 
 * @version 创建时间 2016年7月17日 上午10:47:26
 * 
 * 类描述：获取和判断文件头信息 
 *    |--文件头是位于文件开头的一段承担一定任务的数据，一般都在开头的部分。
 *    |--头文件作为一种包含功能函数、数据接口声明的载体文件，用于保存程序的声明(declaration),而定义文件用于保存程序的实现(implementation)。
 *    |--为了解决在用户上传文件的时候在服务器端判断文件类型的问题，故用获取文件头的方式，直接读取文件的前几个字节，来判断上传文件是否符合格式。
 * 
 */
public class CheckFileTypeUtil {
	/**
	 * 缓存文件头信息-文件头信息
	 */
	public static final HashMap<String, String> MFILETYPES = new HashMap<>(30);
	public static final HashMap<String, String> MFILETYPES1 = new HashMap<>(30);
	static {
		// MFILETYPES

		MFILETYPES.put("FFD8FF", "jpg");
		MFILETYPES.put("89504E47", "png");
		MFILETYPES.put("47494638", "gif");
		MFILETYPES.put("49492A00", "tif");
		MFILETYPES.put("424D", "bmp");
		// CAD
		MFILETYPES.put("41433130", "dwg");
		MFILETYPES.put("38425053", "psd");
		// 日记本
		MFILETYPES.put("7B5C727466", "rtf");
		MFILETYPES.put("3C3F786D6C", "xml");
		MFILETYPES.put("68746D6C3E", "html");
		// 邮件
		MFILETYPES.put("44656C69766572792D646174653A", "eml");
		MFILETYPES.put("D0CF11E0", "doc");
		//excel2003版本文件
		MFILETYPES.put("D0CF11E0", "xls");
		MFILETYPES.put("5374616E64617264204A", "mdb");
		MFILETYPES.put("252150532D41646F6265", "ps");
		MFILETYPES.put("255044462D312E", "pdf");
		MFILETYPES.put("504B0304", "docx");
		//excel2007以上版本文件
		MFILETYPES.put("504B030414000600", "xlsx");
		MFILETYPES.put("52617221", "rar");
		MFILETYPES.put("57415645", "wav");
		MFILETYPES.put("41564920", "avi");
		MFILETYPES.put("2E524D46", "rm");
		MFILETYPES.put("000001BA", "mpg");
		MFILETYPES.put("000001B3", "mpg");
		MFILETYPES.put("6D6F6F76", "mov");
		MFILETYPES.put("3026B2758E66CF11", "asf");
		MFILETYPES.put("4D546864", "mid");
		MFILETYPES.put("1F8B08", "gz");
		
		// MFILETYPES1
		MFILETYPES1.put("jpg","FFD8FF");
		MFILETYPES1.put("png","89504E47");
		MFILETYPES1.put("47494638", "gif");
		MFILETYPES1.put("49492A00", "tif");
		MFILETYPES1.put("424D", "bmp");
		// CAD
		MFILETYPES1.put("41433130", "dwg");
		MFILETYPES1.put("38425053", "psd");
		// 日记本
		MFILETYPES1.put("7B5C727466", "rtf");
		MFILETYPES1.put("3C3F786D6C", "xml");
		MFILETYPES1.put("68746D6C3E", "html");
		// 邮件
		MFILETYPES1.put("44656C69766572792D646174653A", "eml");
		MFILETYPES1.put("D0CF11E0", "doc");
		//excel2003版本文件
		MFILETYPES1.put("D0CF11E0", "xls");
		MFILETYPES1.put("5374616E64617264204A", "mdb");
		MFILETYPES1.put("252150532D41646F6265", "ps");
		MFILETYPES1.put("255044462D312E", "pdf");
		MFILETYPES1.put("504B0304", "docx");
		//excel2007以上版本文件
		MFILETYPES1.put("504B030414000600", "xlsx");
		MFILETYPES1.put("rar","52617221" );
		MFILETYPES1.put("wav","57415645") ;
		MFILETYPES1.put("avi","41564920" );
		MFILETYPES1.put("rm","2E524D46" );
		MFILETYPES1.put("mpg","000001BA" );
		MFILETYPES1.put("mpg","000001B3" );
		MFILETYPES1.put("mov","6D6F6F76" );
		MFILETYPES1.put("asf","3026B2758E66CF11");
		MFILETYPES1.put("mid","4D546864");
		MFILETYPES1.put("gz","1F8B08");
	}
 
	/**
	 * @author yh
	 *
	 * 方法描述：根据文件路径获取文件头信息
	 * @param filePath 文件路径
	 * @return 文件头信息
	 */
	public static String getFileType(String filePath) {

		return MFILETYPES.get(getFileHeader(filePath));
	}
 
	/**
	 * @author yh
	 *
	 * 方法描述：根据文件路径获取文件头信息
	 * @param filePath 文件路径
	 * @return 文件头信息
	 */
	public static String getFileHeader(String filePath) {
		FileInputStream is = null;
		String value = null;
		try {
			is = new FileInputStream(filePath);
			byte[] b = new byte[4];
			/*
			 * int read() 从此输入流中读取一个数据字节。int read(byte[] b) 从此输入流中将最多 b.length
			 * 个字节的数据读入一个 byte 数组中。 int read(byte[] b, int off, int len)
			 * 从此输入流中将最多 len 个字节的数据读入一个 byte 数组中。
			 */
			is.read(b, 0, b.length);
			value = bytesToHexString(b);
		} catch (Exception e) {
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
		return value;
	}
 
	/**
	 * @author yh
	 *
	 * 方法描述：将要读取文件头信息的文件的byte数组转换成string类型表示
	 * @param src 要读取文件头信息的文件的byte数组
	 * @return   文件头信息
	 */
	private static String bytesToHexString(byte[] src) {
		StringBuilder builder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		String hv;
		for (int i = 0; i < src.length; i++) {
			// 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
			hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
			if (hv.length() < 2) {
				builder.append(0);
			}
			builder.append(hv);
		}
		return builder.toString();
	}
	
	/**
	 * @author yh	 
	 * 方法描述：根据文件流获取文件类型信息
	 * @param inputStream 文件流
	 * @param filePath 上传文件地址
	 * @return 文件头信息
	 */
	public String getFileMimeType(InputStream inputStream,String filePath)  throws Exception {
		TikaConfig tika = new TikaConfig();
		Metadata metadata = new Metadata();
		metadata.set(Metadata.RESOURCE_NAME_KEY,filePath);
		String mimetype = tika
				.getDetector()
				.detect(
						TikaInputStream.get(inputStream)
						,metadata
				).toString();
		return mimetype;
	}
	
	/**
	 * @author yh
	 * 方法描述：main方法
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		TikaConfig tika = new TikaConfig();
		File f =new File("C:\\Users\\zxy\\Desktop\\logo.png");
		Metadata metadata = new Metadata();
		metadata.set(Metadata.RESOURCE_NAME_KEY, "C:\\Users\\zxy\\Desktop\\logo.png");
		String mimetype = tika.getDetector().detect(
		TikaInputStream.get(f), metadata).toString();
		System.out.println("File  is " + mimetype);	
	}
}	

