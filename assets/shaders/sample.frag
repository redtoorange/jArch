#version 330 core

//out vec4 FragColor;
in vec4 vertexColor;

in vec2 texCoords;

uniform sampler2D tex;

void main() {
    gl_FragColor = texture(tex, texCoords);
}