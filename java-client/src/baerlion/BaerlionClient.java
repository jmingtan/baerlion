package baerlion;

import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BaerlionClient {
	Socket socket = null;
	PrintWriter out = null;
	BufferedReader in = null;
	long startTime = 0;
	boolean connected = false;

	public void init() {
		try {
			socket = new Socket("localhost", 6123);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
			connected = true;
		} catch (IOException e) { connected = false; }
	}

	public void close() {
		try {
			if (out != null)
				out.close();
			if (in != null)
				in.close();
			if (socket != null)
				socket.close();
			connected = false;
		} catch (IOException e) { }
	}

	public void run() {
		if (connected && System.currentTimeMillis() - startTime > 1000) {
			startTime = System.currentTimeMillis();
			out.println("step");
		}
		//if (connected && in.ready())
	}
}
