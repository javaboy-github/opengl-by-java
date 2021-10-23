package opengl.by.java;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/** 三角形を表すクラスです。 drawメソッドで描画でき、このインスタンスを破棄する場合は必ずcloseメソッドを呼んでください。
*/
public class Triangle implements AutoCloseable{
    /** 頂点配列オブジェクトのID */
    private int vao;
    /**頂点バッファオブジェクトのiD */
    private int vbo;
    /**頂点バッファイブジェクトのインデックス */
    private int ibo;
    /**頂点の数 */
    private int vertexCount;
    /**頂点のインデックス数 */
    private int indexCount;

    /**
     * Triangleクラスを作成する。
     * @param size 頂点の位置の次元。二次元なら2、三次元なら3
     * @param vertexCount 頂点の数
     * @param vertex 頂点配列。位置(3つの要素)と色(3つの要素)を交互に入れる。
     * @param indexCount 頂点のインデックス数
     * @param index 頂点のインデックスを格納した配列
     */
    public Triangle(int size, int vertexCount, float[] vertex, int indexCount, int[] index) {
        this.vertexCount = vertexCount;
        this.indexCount = indexCount;

        // 頂点配列オブジェクト
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        // 頂点バッファオブジェクト
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertex, GL_STATIC_DRAW);

        // 結合されている頂点バッファオブジェクトを in 変数から参照できるようにする
        final int vertexSize = 24;
        glVertexAttribPointer(0, size, GL_FLOAT, false, vertexSize, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, vertexSize, (char)0 + 2 * 4);
        glEnableVertexAttribArray(1);

        // インデックスの頂点バッファオブジェクト
        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        // glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexCount * 4, GL_STATIC_DRAW);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, index, GL_STATIC_DRAW);
    }
    
    public void draw() {
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0);
        // glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount); // 折線で描画
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
