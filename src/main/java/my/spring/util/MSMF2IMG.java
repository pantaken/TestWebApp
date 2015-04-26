package my.spring.util;

public class MSMF2IMG {

	static {
		System.loadLibrary("msmf2img");
	}
	public native int[] conv(int scale, String inputFile, String outputFile);
	private static MSMF2IMG msmf2img = null;
	public static MSMF2IMG getInstance() {
		if (msmf2img == null) {
			return new MSMF2IMG();
		} else {
			return msmf2img;
		}
	}
//	public static void main(String[] args) {
//		MSMF2IMG msmf2img = MSMF2IMG.getInstance();
//		String inputFile = "F:/webapps/docx4j/resource/aaa.wmf";
//		String outputFile = "F:/webapps/docx4j/resource/aaa.wmf.png";
//		int[] size = msmf2img.conv(Integer.parseInt(Configuration.getInstance().getValue("msmf2imgScale")), inputFile, outputFile);
//		System.out.println(size[0]);
//		System.out.println(size[1]);
//	}
	
}
