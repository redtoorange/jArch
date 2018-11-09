#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aUV;

out vec4 vertexColor;
out vec2 texCoords;

void main() {
    gl_Position = vec4(aPos, 1.0);

    vertexColor = vec4(1.0f, 1.0f, 1.0f, 1.0f);
    texCoords = aUV;
}