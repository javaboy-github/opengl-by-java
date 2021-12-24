package opengl.by.java;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/** テクスチャ付きのボックスを表すクラスです。 drawメソッドで描画でき、このインスタンスを破棄する場合は必ずcloseメソッドを呼んでください。
*/
public class TexturedBox implements Box {
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

    private Texture[] textures;

    private static final int FLOAT_SIZE = 4;

    private static final int[] indicies = new int[]{
      2,      1,     3,     1,     0,     3,
      2 +4,   1+4,   3+4,   1+4,   4,   3+3,
      2 +3*2, 1+3*2, 3+3*2, 1+3*2, 3*2, 3+3*2,
      2 +3*3, 1+3*3, 3+3*3, 1+3*3, 3*3, 3+3*3,
      2 +3*4, 1+3*4, 3+3*4, 1+3*4, 3*4, 3+3*4,
      2 +3*5, 1+3*5, 3+3*5, 1+3*5, 3*5, 3+3*5,
    };

    public TexturedBox(Vec3 pos, Vec3 boxSize, Program program, Texture texture) {
        this(pos, boxSize, program, new Texture[]{texture, texture, texture, texture, texture, texture});
    }
    public TexturedBox(Vec3 pos, Vec3 boxSize, Program program, Texture[] textures) {
        this.textures = textures;
        this.program = program;
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

        //  index buffer object
        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicies, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * FLOAT_SIZE,0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * FLOAT_SIZE,3 * FLOAT_SIZE);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * FLOAT_SIZE,6 * FLOAT_SIZE);
        glEnableVertexAttribArray(2);
        glUniform1i(glGetUniformLocation(program.program, "texture1"), textures[0].getId());
    }

    @Override
    public void draw() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textures[0].getId());
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
