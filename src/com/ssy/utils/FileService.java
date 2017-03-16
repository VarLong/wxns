package com.ssy.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;

@SuppressLint({ "WorldReadableFiles", "WorldWriteableFiles" })
public class FileService {
	private Context context;
	String dirPath = Constants.DIR_PATH;
	public FileService() {
	}
	public FileService(Context context) {
		this.context = context;
	}

	/**
	 * 读取文件的内容
	 * 
	 * @param filename
	 *            文件名称
	 * @return
	 * @throws Exception
	 */
	public String readSDCardFile(String filename) {
		try {
			// 获得输入流
			File file = new File(dirPath + File.separator + filename);
			FileInputStream inStream = new FileInputStream(file);
			// new一个缓冲区
			byte[] buffer = new byte[1024];
			int len = 0;
			// 使用ByteArrayOutputStream类来处理输出流
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			while ((len = inStream.read(buffer)) != -1) {
				// 写入数据
				outStream.write(buffer, 0, len);
			}
			// 得到文件的二进制数据
			byte[] data = outStream.toByteArray();
			// 关闭流
			outStream.close();
			inStream.close();
			return new String(data);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 保存文件内容至SDCard中
	 * 
	 * @param filename
	 *            文件名称
	 * @param content
	 *            文件内容
	 * @throws Exception
	 */
	public void saveToSDCard(String fileName, String content) {
		try {
			// 通过getExternalStorageDirectory方法获取SDCard的文件路径
			File dirFile = new File(dirPath);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			File file = new File(dirPath, fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			// 获取输出流
			FileOutputStream outStream = new FileOutputStream(file);
			outStream.write(content.getBytes());
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 以默认私有方式保存文件内容，存放在手机存储空间中
	 * 
	 * @param filename
	 *            文件名称
	 * @param content
	 *            文件内容
	 * @throws Exception
	 */
	public void save(String fileName, String content) {
		try {
			FileOutputStream outStream = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			outStream.write(content.getBytes());
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取文件的内容
	 * 
	 * @param filename
	 *            文件名称
	 * @return
	 * @throws Exception
	 */
	public String readFile(String filename) {
		try {
			// 获得输入流
			FileInputStream inStream = context.openFileInput(filename);
			// new一个缓冲区
			byte[] buffer = new byte[1024];
			int len = 0;
			// 使用ByteArrayOutputStream类来处理输出流
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			while ((len = inStream.read(buffer)) != -1) {
				// 写入数据
				outStream.write(buffer, 0, len);
			}
			// 得到文件的二进制数据
			byte[] data = outStream.toByteArray();
			// 关闭流
			outStream.close();
			inStream.close();
			return new String(data);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	

	/**
	 * 从网络获取图片，如果本地有就从本地获取
	 * 
	 * @param path
	 *            网络图片路径
	 * @param cache
	 *            缓存地址
	 * @return
	 * @throws Exception
	 */
	public Uri getImageURI(String path, File cache) {
		if (!cache.exists()) {
			cache.mkdirs();
		}
		Uri uri = null;
		String name = path.substring(path.lastIndexOf("/") + 1, path.length());
		File file = new File(cache, name);
		// 如果图片存在本地缓存目录，则不去服务器下载
		if (file.exists()) {
			// 得到文件的URI
			uri = Uri.fromFile(file);
		} else {
			// 如果文件不存在就新创建一个
			try {
				file.createNewFile();
				// 从网络上获取图片
				URL url = new URL(path);
				HttpURLConnection conn;
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(10000);
				conn.setRequestMethod("GET");
				conn.setDoInput(true);
				if (conn.getResponseCode() == 200) {
					InputStream is = conn.getInputStream();
					FileOutputStream fos = new FileOutputStream(file);
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = is.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
					}
					is.close();
					fos.close();
					uri = Uri.fromFile(file);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return uri;
			}
		}
		return uri;
	}

	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}