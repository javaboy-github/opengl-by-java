#version 330 core
in vec4 position;
in vec4 color;
in vec2 texCoord;
uniform mat4 modelview;
uniform mat4 projection;
uniform float t;

out vec4 ourColor;
out vec2 TexCoord;

void main()
{
    gl_Position = projection * modelview * position;
    ourColor = color;
    TexCoord = texCoord;
}
