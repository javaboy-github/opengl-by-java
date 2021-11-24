package opengl.by.java;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.glfw.GLFW.*;

import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;

/** This class has fragment shader and vertex shader */
public class Program {
	public int program;

	public Program(String vertex, String fragment) {
		program = glCreateProgram();
		int vobj = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vobj, vertex, "");
		glCompileShader(vobj);
		var status = new int[1];
		glGetShaderiv(vobj, GL_COMPILE_STATUS, status);
		if (status[0] == GL_FALSE){
			var bufSize = new int[1];
			glGetShaderiv(vobj, GL_INFO_LOG_LENGTH, bufSize);
			if (bufSize[0] > 1) {
				throw new RuntimeException("vertex shaderのコンパイルに失敗しました。エラーメッセージ:"
						+ glGetShaderInfoLog(vobj, bufSize[0]));
			}
			else throw new RuntimeException("vertex shaderのコンパイルに失敗しました");
		}
		glAttachShader(program, vobj);
		glDeleteShader(vobj);
		int fobj = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fobj, fragment, "");
		glCompileShader(fobj);
		status = new int[1];
		glGetShaderiv(vobj, GL_COMPILE_STATUS, status);
		if (status[0] == GL_FALSE){
			var bufSize = new int[1];
			glGetShaderiv(vobj, GL_INFO_LOG_LENGTH, bufSize);
			if (bufSize[0] > 1) {
				throw new RuntimeException("fragment shaderのコンパイルに失敗しました。エラーメッセージ:"
						+ glGetShaderInfoLog(vobj, bufSize[0]));
			}
			else throw new RuntimeException("fragment shaderのコンパイルに失敗しました");
		}
		glAttachShader(program, fobj);
		glDeleteShader(fobj);
	}

	public void link() {
		glLinkProgram(program);
	}

	public void use() {
		glUseProgram(program);
	}

	public static Program createFromSourcefile(String vertexShaderSourceFilename, String fragmentShaderSourceFilename) {
		var vertexShaderSourceFile = Paths.get(vertexShaderSourceFilename);
		String vertexShaderSource = null;
		try {
			vertexShaderSource = Files.readString(vertexShaderSourceFile);
		} catch(IOException e) {
			System.err.println("Failed to load " + vertexShaderSourceFilename);
			throw new RuntimeException(e);
		}
		var fragmentShaderSourceFile = Paths.get(fragmentShaderSourceFilename);
		String fragmentShaderSource = null;
		try {
			fragmentShaderSource = Files.readString(fragmentShaderSourceFile);
		} catch(IOException e) {
			System.err.println("Failed to load " + fragmentShaderSourceFilename);
			throw new RuntimeException(e);
		}
		return new Program(vertexShaderSource, fragmentShaderSource);
	}
}
