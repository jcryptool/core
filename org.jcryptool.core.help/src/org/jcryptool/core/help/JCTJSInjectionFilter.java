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
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.help.webapp.IFilter;

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
			super.close();
			this.wrapped.close();
		}
		
	}

	public static class InjectionOutputStream extends OutputStream {
		
		private LinkedList<Integer> buffer;
		private String injectBeforeToken;
		private OutputStream outStream;
		private String injectionContent;
		private Charset charset = StandardCharsets.UTF_8;

		public InjectionOutputStream(OutputStream outStream, String injectBeforeToken, String injectionContent) {
			this.outStream = outStream;
			this.injectBeforeToken = injectBeforeToken;
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
			String tailToCompare = getBufferTail(injectBeforeToken.length());
			if (tailToCompare.contains("head")) {
// 				System.out.println(String.format("########### |%s|", tailToCompare));
			}
			if (tailToCompare.equals(injectBeforeToken)) {
// 				System.out.println(String.format("!!!!!!!!!!! |%s|", tailToCompare));
				// now, before emitting the token and continuing, emit the specified sequence of this injector.
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.outStream, this.charset);
				outputStreamWriter.write(this.injectionContent);
				outputStreamWriter.flush();
			}
			if (this.buffer.size() > this.injectBeforeToken.getBytes(this.charset).length) {
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
			this.outStream.close();
		}

	}
	
	@Override
	public OutputStream filter(HttpServletRequest req, OutputStream out) {

		// construct the javascript injection: provide local port and source the bootstrapping script
		// see more in JCTJS_Server.java
		int JCTJSPort               = JCTJS_Server.getInstance().getPort();
		String injectionPayload = "\n"
				+ String.format("<script>bootstrap_JCTJS_PORT=%s</script>\n", JCTJSPort)
				+ "<script src=\"org.jcryptool.core.help/javascript/bootstrap_jct_utilities.js\"/>\n";
		
		// transform the stream
		//		OutputStream stdoutTee         = new TeeStdoutStream(out); // debug utility to see in the console what the transformed content is
		// 		OutputStream transformedStream = new InjectionOutputStream(out, "</head>", "<script>alert('Hello, world!')</script>\n"); // debug utility to display an alert() window in the online help to directly see if this works
		OutputStream transformedStream = new InjectionOutputStream(out, "</head>", injectionPayload);
		return transformedStream;
	}

}
