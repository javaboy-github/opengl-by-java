#version 330 core
layout (location = 0) in vec3 position;
layout (location = 1) in vec3 color;
layout (location = 2) in vec2 texCoord;
uniform mat4 modelview;
uniform mat4 projection;
uniform sampler2D texture0;
uniform sampler2D texture1;
uniform sampler2D texture2;
uniform sampler2D texture3;
uniform sampler2D texture4;
uniform sampler2D texture5;

out vec2 TexCoord;

void main()
{
    gl_Position = projection * modelview * vec4(position, 1.0);
    TexCoord = texCoord;
}
