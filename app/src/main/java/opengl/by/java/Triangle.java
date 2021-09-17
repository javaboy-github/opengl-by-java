package opengl.by.java;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.CallbackI.S;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.joml.Vector2f;
import org.joml.Vector3f;

/** 三角形を表すクラスです。 drawメソッドで描画でき、このインスタンスを破棄する場合は必ずcloseメソッドを呼んでください。
*/
public class Triangle implements AutoCloseable{
    /** 頂点配列オブジェクトのID */
    private IntBuffer vao;
    /**頂点バッファオブジェクトのiD */
    private IntBuffer vbo;
    /**頂点バッファイブジェクトのインデックス */
    private IntBuffer ibo;
    /**頂点の数 */
    private int vertexCount;

    /**
     * Triangleクラスを作成する。
     * @param size 頂点の位置の次元。二次元なら2、三次元なら3
     * @param vertexCount 頂点の数
     * @param vertex 頂点配列。位置(3つの要素)と色(3つの要素)を交互に入れる。
     * @param indexCount 頂点のインデックス数
     * @param index 頂点のインデックスを格納した配列
     */
    public Triangle(int size, int vertexCount, int[] vertex, int indexCount, int index) {
        this.vertexCount = vertexCount;

        // 頂点配列オブジェクト
        glGenVertexArrays(vao);
        glBindVertexArray(vao.get());

        // 頂点バッファオブジェクト
        glGenBuffers(vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo.get());
        glBufferData(GL_ARRAY_BUFFER, vertex, GL_STATIC_DRAW);

        // 結合されている頂点バッファオブジェクトを in 変数から参照できるようにする
        glVertexAttribPointer(0, size, GL_FLOAT, false, 4 * 6, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 4 * 6, (char)0 + 4 * 3);
        glEnableVertexAttribArray(1);

        // インデックスの頂点バッファオブジェクト
        glGenBuffers(ibo);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo.get());
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexCount * 4, GL_STATIC_DRAW);
    }
    
    public void draw() {
        glBindVertexArray(vao.get());
        glDrawArrays(GL_TRIANGLES, 0, vertexCount); // 折線で描画
    }

    // 事実上のデストラクタ
    @Override
    public void close() {
        // 頂点配列オブジェクトを削除する
        glDeleteVertexArrays(vao);
    
        // 頂点バッファオブジェクトを削除する
        glDeleteBuffers(vbo);
    
        // インデックスの頂点バッファオブジェクトを削除する
        glDeleteBuffers(ibo);
    }
}
