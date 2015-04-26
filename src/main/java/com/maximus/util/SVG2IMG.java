package com.maximus.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;

public class SVG2IMG {
	/**
	 * svg to jpg
	 * @param svgfilename
	 * @param imgName
	 */
	public void doSVG2JPEGConvert(String svgfilename, String imgName) {
		try {
			JPEGTranscoder transcoder = new JPEGTranscoder();
			transcoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(1.0));
			TranscoderInput input = new TranscoderInput(new FileInputStream(svgfilename));
			OutputStream ostream = new FileOutputStream(imgName);
			TranscoderOutput output = new TranscoderOutput(ostream);
			transcoder.transcode(input, output);
			ostream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * svg to png
	 * @param svgfilename
	 * @param imgName
	 */
	public void doSVG2PNGConvert(String svgfilename, String imgName) {
		try {
			PNGTranscoder transcoder = new PNGTranscoder();
//			transcoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY,new Float(1.0));
			TranscoderInput input = new TranscoderInput(new FileInputStream(svgfilename));
			OutputStream ostream = new FileOutputStream(imgName);
			TranscoderOutput output = new TranscoderOutput(ostream);
			transcoder.transcode(input, output);
			ostream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
