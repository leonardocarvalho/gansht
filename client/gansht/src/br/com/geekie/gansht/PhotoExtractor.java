package br.com.geekie.gansht;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

public class PhotoExtractor {

	private int quality;
	
	public PhotoExtractor() {
		this(100);
	}
	
	public PhotoExtractor(int quality) {
		this.quality = quality;
	}
	
	public byte[] extractImage(Uri imageFileUri) {
		Bitmap bmp = BitmapFactory.decodeFile(imageFileUri.getPath()); 
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, this.quality, baos);
		return baos.toByteArray();
	}
	
}
