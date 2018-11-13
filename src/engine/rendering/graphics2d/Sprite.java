/******************************************************************************
 * Copyright 2018 Andrew James McGuiness
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to 
 * deal in the Software without restriction, including without limitation the 
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or 
 * sell copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
 * IN THE SOFTWARE.
 *****************************************************************************/
package engine.rendering.graphics2d;

import engine.rendering.Transform;
import engine.rendering.material.Texture;
import engine.rendering.mesh.Mesh;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Sprite {
    private Mesh spriteMesh;
    private Texture spriteTexture;
    private Transform transform;

    public Sprite(Texture texture){
        this.transform = new Transform();
        this.spriteTexture = texture;
        this.spriteMesh = new QuadMesh();
    }

    public Matrix4f getModelTransform() {
        Matrix4f mat = new Matrix4f();

        mat = mat.translate( transform.getPosition() );
        mat = mat.scale( transform.getScale() );
        mat = mat.rotate( ( float ) Math.toRadians( transform.getRotation().x ), 1, 0, 0 );
        mat = mat.rotate( ( float ) Math.toRadians( transform.getRotation().y ), 0, 1, 0 );
        mat = mat.rotate( ( float ) Math.toRadians( transform.getRotation().z ), 0, 0, 1 );

        return mat;
    }

    public Texture getSpriteTexture() {
        return spriteTexture;
    }

    public void render() {
        spriteMesh.draw();
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform( Transform transform ) {
        this.transform = transform;
    }

    public void translate(float dx, float dy, float dz){
        Vector3f temp = transform.getPosition();
        temp.add( dx, dy, dz );
        transform.setPosition( temp );
    }

    public void scale(float dx, float dy, float dz){
        Vector3f temp = transform.getScale();
        temp.add( dx, dy, dz );
        transform.setScale( temp );
    }

    public void rotate(float dx, float dy, float dz){
        Vector3f temp = transform.getRotation();
        temp.add( dx, dy, dz );
        transform.setRotation( temp );
    }

}
