#version 330 core

in vec2 TexCoord;

uniform sampler2D texture0;
uniform sampler2D texture1;
uniform sampler2D texture2;
uniform sampler2D texture3;
uniform sampler2D texture4;
uniform sampler2D texture5;

out vec4 fragment;

void main()
{
     fragment = texture(texture0, TexCoord);
}
