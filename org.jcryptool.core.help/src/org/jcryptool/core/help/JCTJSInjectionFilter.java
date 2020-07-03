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

import org.eclipse.help.internal.webapp.servlet.InjectionFilter;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.help.webapp.IFilter;

public class JCTJSInjectionFilter implements IFilter {

	private boolean encounteredHeadClosingTag;

	public JCTJSInjectionFilter() {
		this.encounteredHeadClosingTag = false;
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
			if (tailToCompare.equals(injectBeforeToken)) {
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

		OutputStream transformedStream = new InjectionOutputStream(out, "</head>", "<!-- HELLO WORLD -->\n");
		OutputStream copiedToStdout = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				System.out.write(b);
				transformedStream.write(b);
			}
			
			@Override
			public void flush() throws IOException {
				super.flush();
				System.out.flush();
				transformedStream.flush();
			}
			
			@Override
			public void close() throws IOException {
				super.close();
				transformedStream.close();
			}
		};  
				
		return copiedToStdout;
	}

}
