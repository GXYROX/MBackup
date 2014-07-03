/*
 * Copyright (c) 2010-2011, The MiCode Open Source Community (www.micode.net)
 *
 * This file is part of FileExplorer.
 *
 * FileExplorer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FileExplorer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SwiFTP.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.teentian.util;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class IntentBuilder {

	public static void viewFile(final Context context, final String filePath) {
		
		//获取文件路径
		File files = new File(filePath);
		//打开这个文件
		Intent openFile = getFileIntent(files);
		context.startActivity(openFile);
		
//        String type = getMimeType(filePath);

//        if (!TextUtils.isEmpty(type) && !TextUtils.equals(type, "*/*")) {
//            /* 设置intent的file与MimeType */
//            Intent intent = new Intent();
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setAction(android.content.Intent.ACTION_VIEW);
//            intent.setDataAndType(Uri.fromFile(new File(filePath)), type);
//            //by LJ. 2013_12_05
//            //修正在文件未安装相程序应用时崩溃情况  
//            try {
//            	context.startActivity(intent);
//            }
//            catch(Exception e) {
//            	System.out.println(e);
//            }
//            finally {
//            	System.out.println("...");
//            }
//            
//        } else {
//            // unknown MimeType
//            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
//            dialogBuilder.setTitle(R.string.dialog_select_type);
//
//            CharSequence[] menuItemArray = new CharSequence[] {
//                    context.getString(R.string.dialog_type_text),
//                    context.getString(R.string.dialog_type_audio),
//                    context.getString(R.string.dialog_type_video),
//                    context.getString(R.string.dialog_type_image),};
//            dialogBuilder.setItems(menuItemArray,
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            String selectType = "*/*";
//                            switch (which) {
//                            case 0:
//                                selectType = "text/plain";
//                                break;
//                            case 1:
//                                selectType = "audio/*";
//                                break;
//                            case 2:
//                                selectType = "video/*";
//                                break;
//                            case 3:
//                                selectType = "image/*";
//                                break;
//                            default:
//                            	break;
//                            }
//                            Intent intent = new Intent();
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.setAction(android.content.Intent.ACTION_VIEW);
//                            intent.setDataAndType(Uri.fromFile(new File(filePath)), selectType);
//                            context.startActivity(intent);
//                        }
//                    });
//            dialogBuilder.show();
//        }
    }

    public static Intent buildSendFile(ArrayList<FileInfo> files) {
        ArrayList<Uri> uris = new ArrayList<Uri>();

        String mimeType = "*/*";
        for (FileInfo file : files) {
            if (file.IsDir)
                continue;

            File fileIn = new File(file.filePath);
            mimeType = getMimeType(file.fileName);
            Uri u = Uri.fromFile(fileIn);
            uris.add(u);
        }

        if (uris.size() == 0)
            return null;

        boolean multiple = uris.size() > 1;
        Intent intent = new Intent(multiple ? android.content.Intent.ACTION_SEND_MULTIPLE
                : android.content.Intent.ACTION_SEND);

        if (multiple) {
            intent.setType("*/*");
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        } else {
            intent.setType(mimeType);
            intent.putExtra(Intent.EXTRA_STREAM, uris.get(0));
        }

        return intent;
    }

    private static String getMimeType(String filePath) {
        int dotPosition = filePath.lastIndexOf('.');
        if (dotPosition == -1)
            return "*/*";

        String ext = filePath.substring(dotPosition + 1, filePath.length()).toLowerCase();
        String mimeType = MimeUtils.guessMimeTypeFromExtension(ext);
        if (ext.equals("mtz")) {
            mimeType = "application/miui-mtz";
        }

        return mimeType != null ? mimeType : "*/*";
    }
    
    public static Intent getFileIntent(File file) {
		Uri uri = Uri.fromFile(file);
		String type = getMIMEType(file);
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(uri, type);
		return intent;
	}
    
	private static String getMIMEType(File f){   
	      String type="";  
	      String fName=f.getName();  
	      /* 取得扩展名 */  
	      String end=fName.substring(fName.lastIndexOf(".")+1,fName.length()).toLowerCase();
	      
	      /* 依扩展名的类型决定MimeType */
//	      if(end.equals("pdf")){
//	              type = "application/pdf";//
//	      }
	      if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||  
	      end.equals("xmf")||end.equals("ogg")||end.equals("wav")){  
	        type = "audio/*";   
	      }  
	      else if(end.equals("3gp")||end.equals("mp4")){  
	        type = "video/*";  
	      }  
	      else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||  
	      end.equals("jpeg")||end.equals("bmp")){  
	        type = "image/*";  
	      }  
	      else if(end.equals("apk")){   
	        type = "application/vnd.android.package-archive"; 
	      }
	      else{
//	              /*如果无法直接打开，就跳出软件列表给用户选择 */  
	        type="*/*";
	      }
	      return type;
	    }
	
}



