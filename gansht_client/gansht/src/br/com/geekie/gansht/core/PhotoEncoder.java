package br.com.geekie.gansht.core;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;

public class PhotoEncoder {

	private int quality;
	
	public PhotoEncoder() {
		this(100);
	}
	
	public PhotoEncoder(int quality) {
		this.quality = quality;
	}
	
	public byte[] extractImage(Bitmap bmp) { 
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, this.quality, baos);
		return baos.toByteArray();
	}
	
}
