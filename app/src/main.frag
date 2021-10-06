#version 150 core

out vec4 fragment;
uniform float t;

void main()
{
    // fragment = vec4(0.3, 0.3, 0.3, 1.0);
    fragment = vec4(sin(t), cos(t), 0.0, 1.0);
}