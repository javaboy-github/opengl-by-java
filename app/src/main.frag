#version 150 core

in vec4 vertex_color;
out vec4 fragment;
uniform float t;

void main() {
    fragment = vertex_color;
}