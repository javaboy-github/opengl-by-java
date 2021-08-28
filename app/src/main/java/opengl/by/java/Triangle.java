package opengl.by.java;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.BufferUtils;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.joml.Vector2f;
import org.joml.Vector3f;

// 参考:https://tus-ricora.com/programming/lwjgl/
public class Triangle implements AutoCloseable{
    String path;
    Vector2f wh;
    Vector3f pos;
    Vector3f rot;
    Vector3f col;
    double scl = 1.0;
    double alp = 1.0;
    int textureId;

    /**Triangleクラスのビルダー。 */
    public static class Builder {
        private String path = null;
        private Vector2f wh = new Vector2f();
        private Vector3f pos = new Vector3f();
        private Vector3f rot = new Vector3f();
        private Vector3f col = new Vector3f();
        private double scl = 1.0;
        private double alp = 1.0;

        // セッター
        public Builder setPath(String path) {this.path = path;return this;}
        public Builder setWh(Vector2f wh) {this.wh = wh;return this;}
        public Builder setPos(Vector3f pos) {this.pos = pos;return this;}
        public Builder setRot(Vector3f rot) {this.rot = rot;return this;}
        public Builder setCol(Vector3f col) {this.col = col;return this;}
        public Builder setScl(double scl) {this.scl = scl;return this;}
        public Builder setAlp(double alp) {this.alp = alp;return this;}

        // ゲッター
        public String getPath() {return this.path;}
        public Vector2f getWh() {return this.wh;}
        public Vector3f getPos() {return this.pos;}
        public Vector3f getRot() {return this.rot;}
        public Vector3f getCol() {return this.col;}
        public double getScl() {return this.scl;}
        public double getAlp() {return this.alp;}

        public Triangle build() throws IOException{
            return new Triangle(path, wh, pos, rot, col, scl, alp);
        }
    }

    public Triangle(String path, Vector2f wh, Vector3f pos, Vector3f rot, Vector3f col, double scl, double alp) throws IOException{
        this.path = path;
        this.wh = wh;
        this.pos = pos;
        this.rot = rot;
        this.col = col;
        this.scl = scl;
        this.alp = alp;

        // ※IOExceptionをスローする場合あり
        // イメージファイルの読み込み
        BufferedImage bi = ImageIO.read(new File(path));
        int width = bi.getWidth();
        int height = bi.getHeight();

        // ピクセルを保存する配列
        int[] pixelsRaw = bi.getRGB(0, 0, width, height, null, 0, width);
        ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);

        // 一ピクセルごとに読み込む
        for (int i=0;i<height;i++) {
            for (int j=0;j<height;j++) {
                int p = pixelsRaw[i * width + j];
                pixels.put((byte) ((p >> 16) & 0xFF));// byte型のサイズは、int型の1/4
                pixels.put((byte) ((p >> 8) & 0xFF));
                pixels.put((byte) (p & 0xFF));
                pixels.put((byte) ((p >> 24) & 0xFF));
            }
        }
        pixels.flip();

        // テクスチャの作成
        textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

        // テクスチャの設定
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        // テクスチャの解除
        glBindTexture(GL_TEXTURE_2D, 0);

    }
    
    public void draw() {
        glBindTexture(GL_TEXTURE_2D, textureId);//　テクスチャのバインド

        glPushMatrix();

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glTranslated(pos.x, pos.y, pos.z);
        glRotated(rot.x, 1.0, 0.0, 0.0);
        glRotated(rot.y, 0.0, 1.0, 0.0);
        glRotated(rot.z, 0.0, 0.0, 1.0);
        glScaled(scl, scl, scl);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glFrustum(-5.0, 5.0, -5.0, 5.0, 10.0, 100.0); // 透視投影

        glViewport(0, 0, Main.width, Main.height);

        glColor4d(col.x, col.y, col.z, alp);

            
            
        glBegin(GL_TRIANGLE_STRIP);
        glTexCoord2d(0.0, 0.0);
        glVertex2d(-wh.x / 2, wh.y / 2);
        glTexCoord2d(0.0, 1.0);
        glVertex2d(-wh.x / 2, -wh.y / 2);
        glTexCoord2d(1.0, 0.0);
        glVertex2d(wh.x / 2, wh.y / 2);
        glTexCoord2d(1.0, 1.0);
        glVertex2d(wh.x / 2, -wh.y / 2);
        glEnd();

        glPopMatrix();

        // バインドを解除
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    // 事実上のデストラクタ
    @Override
    public void close() {
        glDeleteTextures(textureId);
    }
}
