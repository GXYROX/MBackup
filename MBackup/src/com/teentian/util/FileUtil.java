package com.teentian.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;


public class FileUtil {

	//Save Video File
	public static File saveSDCardFolder(String filePathName) {
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/LJRFOXStudio/MBackup/" + filePathName + "/");
		return file;
	}
	
	
	//isExist Video File 
	public static boolean isFileExist(String filePathName, String fileName) {
		File file = new File(saveSDCardFolder(filePathName), fileName);
		return file.exists();
	}
	
	//Get Video File
	public static String getSDCardFile(String filePathName, String fileName)
	{
		File file = new File(saveSDCardFolder(filePathName), fileName);
		return file.getAbsolutePath();
	}
	
	// 在SD卡中创建文件夹,创建文件,并存储文件(字节流)
	public static void saveFile2SD(Context context, byte[] fileByte, String strFilePathName, String strFileName) throws IOException {
		// 创建文件夹
		File filePath = saveSDCardFolder(strFilePathName);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}

		//  创建文件
		String file = filePath.getAbsolutePath() + File.separator + strFileName;
		File f = new File(file);
		if (!f.exists()) {
			f.createNewFile();
		}

		// 字节输出流 文件写入SD卡
		FileOutputStream fos = new FileOutputStream(f);
		// 字节流
		InputStream inStream = new ByteArrayInputStream(fileByte);
		// 字符流
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream, "gb2312")); //防止中文出现乱码  gb2312 
		// FileInputStream fis = (FileInputStream)inStream;

		System.out.println("fileSave.length---->" + fileByte.length);
		long max = fileByte.length;

		try {
			byte[] buffer = new byte[1024];
			int length = 0;

			while (true) {
				//InputStream 字节流
				int temp = inStream.read(buffer, 0, buffer.length);
				if (temp == -1) {
					break;
				}
				fos.write(buffer, 0, temp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inStream.close();
				fos.close();

				String strDownloadFilePath = getSDCardFile(strFilePathName, strFileName);
				Toast.makeText(context, "文件已保存至: " + strDownloadFilePath, Toast.LENGTH_LONG).show();

			} catch (Exception e) {
				System.out.println(e);
			}
		}
		
	} // -end of _method saveFile2SD()_ by LJ.
	
	public static void createFile2SD(Context context, String strFilePathName, String strFileName) throws IOException {
		//创建文件夹
		File filePath = saveSDCardFolder(strFilePathName);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}

		// 创建文件
		String file = filePath.getAbsolutePath() + File.separator + strFileName;
		File f = new File(file);
		if (!f.exists()) {
			f.createNewFile();
		}
	}
	
}




