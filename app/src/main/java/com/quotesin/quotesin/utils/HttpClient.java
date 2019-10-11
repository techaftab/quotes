package com.quotesin.quotesin.utils;
/*
 * Copyright (C) 2013 Surviving with Android (http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpClient {
	private String url;
    private HttpURLConnection con;
    private OutputStream os;
	private PrintWriter writer;
	private static final String LINE_FEED = "\r\n";
	private String delimiter = "--";
    private String boundary =  "SwA"+ Long.toString(System.currentTimeMillis())+"SwA";



	public HttpClient(String url) {
		this.url = url;
	}

	public byte[] downloadImage(String imgName) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			System.out.println("URL ["+url+"] - Name ["+imgName+"]");

			HttpURLConnection con = (HttpURLConnection) ( new URL(url)).openConnection();
			con.setRequestMethod("POST");

			con.setDoInput(true);
			con.setDoOutput(true);
			con.connect();
			con.getOutputStream().write( ("name=" + imgName).getBytes());

			InputStream is = con.getInputStream();
			byte[] b = new byte[1024];

			while ( is.read(b) != -1)
				baos.write(b);

			con.disconnect();
		}
		catch(Throwable t) {
			t.printStackTrace();
		}

		return baos.toByteArray();
	}

	public void connectForMultipart() throws Exception {
		con = (HttpURLConnection) ( new URL(url)).openConnection();
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Accept-Charset", "UTF-8");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
		con.connect();
		os = con.getOutputStream();
	}

	public void addFormPart(String paramName, String value) throws Exception {
		writeParamData(paramName, value);
	}

	public void addFilePart(String paramName, String fileName, byte[] data) throws Exception {
		os.write( (delimiter + boundary + "\r\n").getBytes());
		os.write( ("Content-Disposition: form-data; name=\"" + paramName +  "\"; filename=\"" + fileName + "\"\r\n"  ).getBytes());
		os.write( ("Content-Type: application/octet-stream\r\n"  ).getBytes());
		os.write( ("Content-Transfer-Encoding: binary\r\n"  ).getBytes());
		os.write("\r\n".getBytes());

		os.write(data);

		os.write("\r\n".getBytes());
	}

	public void finishMultipart() throws Exception {
		os.write( (delimiter + boundary + delimiter + "\r\n").getBytes());
	}


	public String getResponse() throws Exception {
		/*InputStream is = con.getInputStream();
		byte[] b1 = new byte[1024];
		StringBuffer buffer = new StringBuffer();
		
		while ( is.read(b1) != -1)
			buffer.append(new String(b1));
		
		con.disconnect();
		
		return buffer.toString();*/
		StringBuilder stringBuilder = new StringBuilder();

		BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
		String line = null;
		while ((line = reader.readLine())!= null) {
			stringBuilder.append(line);
		}
		String responseData = stringBuilder.toString();
		con.disconnect();
		return responseData;
	}



	public void addFilePart(String fieldName, File uploadFile) throws IOException {
		String fileName = uploadFile.getName();
		writer.append("--" + boundary).append(LINE_FEED);
		writer.append(
				"Content-Disposition: form-data; name=\"" + fieldName
						+ "\"; filename=\"" + fileName + "\"")
				.append(LINE_FEED);
		writer.append(
				"Content-Type: "
						+ URLConnection.guessContentTypeFromName(fileName))
				.append(LINE_FEED);
		writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
		writer.append(LINE_FEED);
		writer.flush();



		FileInputStream inputStream = new FileInputStream(uploadFile);
		byte[] buffer = new byte[4096];

		int bytesRead = -1;
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.flush();
		inputStream.close();
		writer.append(LINE_FEED);
		writer.flush();
	}

	private void writeParamData(String paramName, String value) throws Exception {
		os.write( (delimiter + boundary + "\r\n").getBytes());
		os.write( "Content-Type: text/plain\r\n".getBytes());
		os.write( ("Content-Disposition: form-data; name=\"" + paramName + "\"\r\n").getBytes());;
		os.write( ("\r\n" + value + "\r\n").getBytes());


	}

}
