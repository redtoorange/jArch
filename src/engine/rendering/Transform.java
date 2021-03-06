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
package engine.rendering;

import org.joml.Vector3f;

public class Transform {
    private Vector3f position;
    private Vector3f scale;
    private Vector3f rotation;

    public Transform() {
        position = new Vector3f( 0, 0, 0 );
        scale = new Vector3f( 1, 1, 1 );
        rotation = new Vector3f( 0, 0, 0 );
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition( Vector3f position ) {
        this.position = position;
    }

    public void translate( Vector3f delta ) {
        position.add( delta );
    }

    public void translate( float dx, float dy, float dz ) {
        position.add( dx, dy, dz );
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale( Vector3f scale ) {
        this.scale = scale;
    }

    public void scale( Vector3f delta ) {
        scale.add( delta );
    }

    public void scale( float dx, float dy, float dz ) {
        scale.add( dx, dy, dz );
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation( Vector3f rotation ) {
        this.rotation = rotation;
    }

    public void rotate( Vector3f delta ) {
        rotation.add( delta );
    }

    public void rotate( float dx, float dy, float dz ) {
        rotation.add( dx, dy, dz );
    }
}
