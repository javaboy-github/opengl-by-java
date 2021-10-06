#version 150

in vec4 position;
uniform mat4 modelview;

void main()
{
    gl_Position = modelview * position;
}
