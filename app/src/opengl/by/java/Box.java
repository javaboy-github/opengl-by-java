package opengl.by.java;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/** 三角形を表すクラスです。 drawメソッドで描画でき、このインスタンスを破棄する場合は必ずcloseメソッドを呼んでください。
*/
public class Box implements AutoCloseable{
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
    float[] cubeVertex;
        int[] solidCubeIndex = {
            0, 1, 2, 0, 2, 3, // 左
            0, 3, 4, 0, 4, 5, // 裏
            0, 5, 6, 0, 6, 1, // 下
            7, 6, 5, 7, 5, 4, // 右
            7, 4, 3, 7, 3, 2, // 上
            7, 2, 1, 7, 1, 6  // 前
        };

    public Triangle(Vec3 pos) {
        new Box(3, 8, cubeVertex, 36, solidCubeIndex);
        var size = 3;
        this.vertexCount = 8;
        this.indexCount = 36;
cubeVertex = {
        -1f + pos.x, -1f + pos.y, -1f + pos.z,  0f,    0f,   0f,  // (0)
        -1f + pos.x, -1f + pos.y,  1f + pos.z,  0f,    0f,   0.8f,  // (1)
        -1f + pos.x,  1f + pos.y,  1f + pos.z,  0f,    0.8f, 0f,  // (2)
        -1f + pos.x,  1f + pos.y, -1f + pos.z,  0f,    0.8f, 0.8f,  // (3)
        1f + pos.x,  1f + pos.y, -1f + pos.z,  0.8f,  0f,   0f,  // (4)
        1f + pos.x, -1f + pos.y, -1f + pos.z,  0.8f,  0f,   0.8f,  // (5)
        1f + pos.x, -1f + pos.y,  1f + pos.z,  0.8f,  0.8f, 0f,  // (6)
        1f + pos.x,  1f + pos.y,  1f + pos.z,  0.8f,  0.8f, 0.8f   // (7)
    };

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
