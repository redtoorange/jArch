#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aUV;

out vec4 vertexColor;
out vec2 texCoords;

uniform mat4 modelTransform;
uniform mat4 view;
uniform mat4 projection;


void main() {
    gl_Position = projection * view * modelTransform * vec4(aPos, 1.0);

    vertexColor = vec4(1.0f, 1.0f, 1.0f, 1.0f);
    texCoords = aUV;
}