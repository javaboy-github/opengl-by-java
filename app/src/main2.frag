#version 330 core
out vec4 FragColor;
  
in vec4 vertex_color;
in vec3 ourColor;
in vec2 TexCoord;

uniform sampler2D ourTexture;

void main()
{
    FragColor = texture(ourTexture, TexCoord);
}