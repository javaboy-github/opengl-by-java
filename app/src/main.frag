#version 150 core

in vec4 vertex_color;
out vec4 fragment;
uniform float t;

void main() {
    fragment = vec4(vertex_color.x, 0.0, 0.5, 1.0);
    // fragment = vertex_color;
}