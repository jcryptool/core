package org.jcryptool.core.help;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.help.webapp.IFilter;
import org.jcryptool.core.help.HelpInjectionService.HelpInjector;


public class JCTJSInjectionFilter implements IFilter {


	public JCTJSInjectionFilter() {
	}

	public static class TeeStdoutStream extends OutputStream {
		
		private OutputStream wrapped;

		public TeeStdoutStream(OutputStream wrapped) {
			this.wrapped = wrapped;
		}

		@Override
		public void write(int b) throws IOException {
			System.out.write(b);
			this.wrapped.write(b);
		}
		
		@Override
		public void flush() throws IOException {
			super.flush();
			this.wrapped.flush();
			System.out.flush();
		}
		
		@Override
		public void close() throws IOException {
			this.flush();
			super.close();
			this.wrapped.close();
		}
		
	}

	public static class ReplacementOutputStream extends OutputStream {
		
		private LinkedList<Integer> buffer;
		private OutputStream outStream;
		private Charset charset = StandardCharsets.UTF_8;
		private String replace;
		private String with;

		public ReplacementOutputStream(OutputStream outStream, String replace, String with) {
			this.outStream = outStream;
			this.replace = replace;
			this.with = with;
			this.buffer = new LinkedList<Integer>();
		}
		
		public String getBufferTail(int maxChars) {
			List<Byte> bytelist = buffer.stream().map(i -> i.byteValue()).collect(Collectors.toList());
			byte[] bytearray = new byte[bytelist.size()];
			for (int i = 0; i < bytelist.size(); i++) {
				byte b = bytelist.get(i);
				bytearray[i] = b;
			}
			String bufferstring = new String(bytearray, this.charset);
			return bufferstring.subSequence(Math.max(bufferstring.length()-maxChars, 0), bufferstring.length()).toString();
		}
		public void popFirstIn() throws IOException {
			if (this.buffer.size() > 0) {
				this.outStream.write(this.buffer.removeFirst());
			}
		}
		public void popAll() throws IOException {
			while(this.buffer.size() > 0) {
				this.popFirstIn();
			}
		}
		
		@Override
		public void write(int arrivedByte) throws IOException {
			String tailToCompare = getBufferTail(replace.length());
			if (tailToCompare.equals(this.replace)) {
				// now, after emitting the buffer and continuing, emit the specified sequence of this injector.
				this.buffer.clear(); // TODO: assuming the buffer contains exactly the content to be replaced which might be inaccurate when the buffer length changes
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.outStream, this.charset);
				outputStreamWriter.write(this.with);
				outputStreamWriter.flush();
			}
			this.buffer.add(arrivedByte);
			if (this.buffer.size() > this.replace.getBytes(this.charset).length) {
				try {
					this.popFirstIn();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		public void flush() throws IOException {
			this.popAll();
			this.outStream.flush();
		}
		public void close() throws IOException {
			this.flush();
			this.outStream.close();
		}

	}

	public static class ReplacementLazyOutputStream extends OutputStream {
		
		private LinkedList<Integer> buffer;
		private OutputStream outStream;
		private Charset charset = StandardCharsets.UTF_8;
		private String replace;
		private Supplier<String> with;

		public ReplacementLazyOutputStream(OutputStream outStream, String replace, Supplier<String> with) {
			this.outStream = outStream;
			this.replace = replace;
			this.with = with;
			this.buffer = new LinkedList<Integer>();
		}
		
		public String getBufferTail(int maxChars) {
			List<Byte> bytelist = buffer.stream().map(i -> i.byteValue()).collect(Collectors.toList());
			byte[] bytearray = new byte[bytelist.size()];
			for (int i = 0; i < bytelist.size(); i++) {
				byte b = bytelist.get(i);
				bytearray[i] = b;
			}
			String bufferstring = new String(bytearray, this.charset);
			return bufferstring.subSequence(Math.max(bufferstring.length()-maxChars, 0), bufferstring.length()).toString();
		}
		public void popFirstIn() throws IOException {
			if (this.buffer.size() > 0) {
				this.outStream.write(this.buffer.removeFirst());
			}
		}
		public void popAll() throws IOException {
			while(this.buffer.size() > 0) {
				this.popFirstIn();
			}
		}
		
		@Override
		public void write(int arrivedByte) throws IOException {
			String tailToCompare = getBufferTail(replace.length());
			if (tailToCompare.equals(this.replace)) {
				// now, after emitting the buffer and continuing, emit the specified sequence of this injector.
				this.buffer.clear(); // TODO: assuming the buffer contains exactly the content to be replaced which might be inaccurate when the buffer length changes
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.outStream, this.charset);
				outputStreamWriter.write(this.with.get());
				outputStreamWriter.flush();
			}
			this.buffer.add(arrivedByte);
			if (this.buffer.size() > this.replace.getBytes(this.charset).length) {
				try {
					this.popFirstIn();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		public void flush() throws IOException {
			this.popAll();
			this.outStream.flush();
		}
		public void close() throws IOException {
			this.flush();
			this.outStream.close();
		}

	}

	public static class InjectionOutputStream extends OutputStream {
		
		private LinkedList<Integer> buffer;
		private String injectAfterToken;
		private OutputStream outStream;
		private String injectionContent;
		private Charset charset = StandardCharsets.UTF_8;

		public InjectionOutputStream(OutputStream outStream, String injectAfterToken, String injectionContent) {
			this.outStream = outStream;
			this.injectAfterToken = injectAfterToken;
			this.injectionContent = injectionContent;
			this.buffer = new LinkedList<Integer>();
		}
		
		public String getBufferTail(int maxChars) {
			List<Byte> bytelist = buffer.stream().map(i -> i.byteValue()).collect(Collectors.toList());
			byte[] bytearray = new byte[bytelist.size()];
			for (int i = 0; i < bytelist.size(); i++) {
				byte b = bytelist.get(i);
				bytearray[i] = b;
			}
			String bufferstring = new String(bytearray, this.charset);
			return bufferstring.subSequence(Math.max(bufferstring.length()-maxChars, 0), bufferstring.length()).toString();
		}
		public void popFirstIn() throws IOException {
			if (this.buffer.size() > 0) {
				this.outStream.write(this.buffer.removeFirst());
			}
		}
		public void popAll() throws IOException {
			while(this.buffer.size() > 0) {
				this.popFirstIn();
			}
		}
		
		@Override
		public void write(int arrivedByte) throws IOException {
			this.buffer.add(arrivedByte);
			String tailToCompare = getBufferTail(injectAfterToken.length());
			if (tailToCompare.equals(this.injectAfterToken)) {
				// now, after emitting the buffer and continuing, emit the specified sequence of this injector.
				this.popAll();
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.outStream, this.charset);
				outputStreamWriter.write(this.injectionContent);
				outputStreamWriter.flush();
			}
			if (this.buffer.size() > this.injectAfterToken.getBytes(this.charset).length) {
				try {
					this.popFirstIn();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		public void flush() throws IOException {
			this.popAll();
			this.outStream.flush();
		}
		public void close() throws IOException {
			this.flush();
			this.outStream.close();
		}

	}
	

	
	@Override
	public OutputStream filter(HttpServletRequest req, OutputStream out) {

		// construct the javascript injection: provide local port and source the bootstrapping script
		// see more in JCTJS_Server.java
		int JCTJSPort               = JCTJS_Server.getInstance().getPort();
		String jctjs_bootstrap_url  = JCTJS_Server.getInstance().makeUrlStringFor("javascript/bootstrap_jct_utilities.js");
		String injectionPayload = "\n"
				+ String.format("<script>_bootstrap_JCTJS_PORT=%s</script>\n", JCTJSPort)
				+ String.format("<script>_bootstrap_JCTHelpsystem_PORT=%s</script>\n", JCTJS_Server.getInstance().helpsystemPort)
				+ String.format("<script src=\"%s\"></script>\n", jctjs_bootstrap_url);
		
		// transform the stream

//		OutputStream stdoutTee         = new TeeStdoutStream(out); // debug utility to see in the console what the transformed content is
//		OutputStream transformedStream = new InjectionOutputStream(stdoutTee, "</head>", injectionPayload);
// 		OutputStream transformedStream = new InjectionOutputStream(stdoutTee, "</head>", "<script>alert('Hello, world!')</script>\n"); // debug utility to display an alert() window in the online help to directly see if this works

		OutputStream replacedPortStream = new ReplacementOutputStream(out, "${JCTJS_HOST}", "http://127.0.0.1:"+JCTJSPort);
 		OutputStream transformedStream = new InjectionOutputStream(replacedPortStream, "<head>", injectionPayload);
 		OutputStream byService = transformedStream;
 		for (HelpInjector tf: HelpInjectionService.injectors) {
 			byService = new ReplacementLazyOutputStream(byService, tf.toR, tf.rW);
 		}

		return byService;
	}

}
