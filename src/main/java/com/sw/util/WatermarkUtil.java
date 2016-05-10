package com.sw.util;

import java.io.FileOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class WatermarkUtil {

	public static void watermark(String localPath, String pdf, String waterPdf, String watermark) throws Exception{
		// 待加水印的文件  
        PdfReader reader = new PdfReader(pdf);
        // 加完水印的文件  
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(waterPdf));
        int total = reader.getNumberOfPages() + 1;  
        PdfContentByte content;  
        // 设置字体  
        String fontPath = localPath + SystemProperty.getInstance("config").getProperty("pdf.water.font");
        BaseFont base = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        int j = watermark.length(); // 水印文字长度  
        char c = 0;  
        // 循环对每页插入水印  
        for (int i = 1; i < total; i++) {  
            // 水印的起始  
            content = stamper.getUnderContent(i);  
            // 开始  
            content.beginText();  
            // 设置颜色  
            content.setColorFill(BaseColor.GRAY);  
            // 设置字体及字号  
            content.setFontAndSize(base, 28);  
            // 设置起始位置  
            float x = 150;
            if(j > 12){
            	x = 20;
            }
            content.setTextMatrix(x, 300);  
            // 开始写入水印  
            for (int k = 0; k < j; k++) {  
                content.setTextRise(14);  
                c = watermark.charAt(k);  
                // 将char转成字符串  
                content.showText(c + "");  
            }  
            content.endText();  
        }  
		stamper.close();
	}
}
