#version 330 core
uniform mat4 modelview;
uniform mat4 projection;
uniform float t;
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aColor;
layout (location = 2) in vec2 aTexCoord;

out vec3 ourColor;
out vec2 TexCoord;

void main()
{
    gl_Position = projection * modelview * position;
    ourColor = aColor;
    TexCoord = aTexCoord;
}
