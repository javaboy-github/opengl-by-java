package opengl.by.java;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import de.matthiasmann.twl.utils.PNGDecoder;
import java.nio.ByteBuffer;
import java.io.IOException;

public class Texture {
    private int id;
    public Texture(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Texture loadTexture(String filename) throws IOException {
        PNGDecoder decoder = new PNGDecoder(Texture.class.getResourceAsStream(filename));
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        buffer.flip();

        int id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glGenerateMipmap(GL_TEXTURE_2D);

        return new Texture(id); 
    }
}
