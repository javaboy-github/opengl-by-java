package opengl.by.java;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/** テクスチャ付きのボックスを表すクラスです。 drawメソッドで描画でき、このインスタンスを破棄する場合は必ずcloseメソッドを呼んでください。
*/
public class TexturedBox implements AutoCloseable{
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
    /** シェーダープログラム */
    private Program program;

    public TexturedBox(Vec3 pos, Vec3 boxSize, Program program, Texture[] texture) {
        this.program = program;
        var size = 3;
        this.vertexCount = 8;
        this.indexCount = 36;
        var s = new Vec3(boxSize.x / 2, boxSize.y / 2, boxSize.z / 2);
        var vertex = new float[]{
            -s.x + pos.x, -s.y + pos.y, -s.z + pos.z,  0f,    0f,   0f,    0f, 0f, 
            -s.x + pos.x, -s.y + pos.y,  s.z + pos.z,  0f,    0f,   0.8f,  1f, 0f, 
            -s.x + pos.x,  s.y + pos.y,  s.z + pos.z,  0f,    0.8f, 0f,    1f, 1f, 
            -s.x + pos.x,  s.y + pos.y, -s.z + pos.z,  0f,    0.8f, 0.8f,  0f, 1f, 

            s.x + pos.x, -s.y + pos.y, -s.z + pos.z,  0.8f,  0f,   0.8f,   0f, 0f, 
            -s.x + pos.x, -s.y + pos.y, -s.z + pos.z,  0f,    0f,   0f,    1f, 0f, 
            -s.x + pos.x,  s.y + pos.y, -s.z + pos.z,  0f,    0.8f, 0.8f,  1f, 1f, 
             s.x + pos.x,  s.y + pos.y, -s.z + pos.z,  0.8f,  0f,   0f,    0f, 1f, 

            -s.x + pos.x, -s.y + pos.y, -s.z + pos.z,  0f,    0f,   0f,    0f, 0f, 
             s.x + pos.x, -s.y + pos.y, -s.z + pos.z,  0f,    0f,   0f,    1f, 0f, 
             s.x + pos.x, -s.y + pos.y,  s.z + pos.z,  0f,    0f,   0f,    1f, 1f, 
            -s.x + pos.x, -s.y + pos.y,  s.z + pos.z,  0f,    0f,   0f,    0f, 1f, 

            
             s.x + pos.x, -s.y + pos.y,  s.z + pos.z,  0.8f,  0.8f, 0f,    0f, 0f,
             s.x + pos.x, -s.y + pos.y,  -s.z + pos.z,  0.8f,  0.8f, 0f,   1f, 0f,
             s.x + pos.x,  s.y + pos.y,  -s.z + pos.z,  0.8f,  0.8f, 0f,   1f, 1f,
             s.x + pos.x, s.y + pos.y,  s.z + pos.z,  0.8f,  0.8f, 0f,     0f, 1f,


            -s.x + pos.x, -s.y + pos.y,  s.z + pos.z,  0.8f,  0.8f, 0f,    0f, 0f,
             s.x + pos.x, -s.y + pos.y,  s.z + pos.z,  0.8f,  0.8f, 0f,    1f, 0f,
             s.x + pos.x,  s.y + pos.y,  s.z + pos.z,  0.8f,  0.8f, 0f,    1f, 1f,
            -s.x + pos.x,  s.y + pos.y,  s.z + pos.z,  0.8f,  0.8f, 0f,    0f, 1f,

            -s.x + pos.x,  s.y + pos.y, -s.z + pos.z,  0f,    0f,   0f,    0f, 0f, 
             s.x + pos.x,  s.y + pos.y, -s.z + pos.z,  0f,    0f,   0f,    1f, 0f, 
             s.x + pos.x,  s.y + pos.y,  s.z + pos.z,  0f,    0f,   0f,    1f, 1f, 
            -s.x + pos.x,  s.y + pos.y,  s.z + pos.z,  0f,    0f,   0f,    0f, 1f, 
        };

        // 頂点配列オブジェクト
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        // 頂点バッファオブジェクト
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertex, GL_STATIC_DRAW);
    }
    
    public void draw() {
        program.use();
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
