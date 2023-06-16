/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.awt.Image
 *  java.awt.image.BufferedImage
 *  java.lang.IllegalArgumentException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.HashMap
 *  java.util.Map
 *  javax.swing.ImageIcon
 *  javax.xml.bind.DatatypeConverter
 *  processing.core.PConstants
 *  processing.core.PGraphics
 *  processing.core.PImage
 *  processing.core.PMatrix
 *  processing.core.PMatrix2D
 *  processing.core.PMatrix3D
 *  processing.core.PVector
 */
package processing.core;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.xml.bind.DatatypeConverter;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PMatrix;
import processing.core.PMatrix2D;
import processing.core.PMatrix3D;
import processing.core.PVector;

public class PShape
implements PConstants {
    protected String name;
    protected Map<String, PShape> nameTable;
    public static final int PRIMITIVE = 101;
    public static final int PATH = 102;
    public static final int GEOMETRY = 103;
    protected int family;
    protected int kind;
    protected PMatrix matrix;
    protected int textureMode;
    protected PImage image;
    protected String imagePath = null;
    public static final String OUTSIDE_BEGIN_END_ERROR = "%1$s can only be called between beginShape() and endShape()";
    public static final String INSIDE_BEGIN_END_ERROR = "%1$s can only be called outside beginShape() and endShape()";
    public static final String NO_SUCH_VERTEX_ERROR = "%1$s vertex index does not exist";
    public static final String NO_VERTICES_ERROR = "getVertexCount() only works with PATH or GEOMETRY shapes";
    public static final String NOT_A_SIMPLE_VERTEX = "%1$s can not be called on quadratic or bezier vertices";
    public static final String PER_VERTEX_UNSUPPORTED = "This renderer does not support %1$s for individual vertices";
    public float width;
    public float height;
    public float depth;
    PGraphics g;
    protected boolean visible = true;
    protected boolean openShape = false;
    protected boolean openContour = false;
    protected boolean stroke;
    protected int strokeColor;
    protected float strokeWeight;
    protected int strokeCap;
    protected int strokeJoin;
    protected boolean fill;
    protected int fillColor;
    protected boolean tint;
    protected int tintColor;
    protected int ambientColor;
    protected boolean setAmbient;
    protected int specularColor;
    protected int emissiveColor;
    protected float shininess;
    protected int sphereDetailU;
    protected int sphereDetailV;
    protected int rectMode;
    protected int ellipseMode;
    protected boolean style = true;
    protected float[] params;
    protected int vertexCount;
    protected float[][] vertices;
    protected PShape parent;
    protected int childCount;
    protected PShape[] children;
    protected int vertexCodeCount;
    protected int[] vertexCodes;
    protected boolean close;
    protected float calcR;
    protected float calcG;
    protected float calcB;
    protected float calcA;
    protected int calcRi;
    protected int calcGi;
    protected int calcBi;
    protected int calcAi;
    protected int calcColor;
    protected boolean calcAlpha;
    public int colorMode;
    public float colorModeX;
    public float colorModeY;
    public float colorModeZ;
    public float colorModeA;
    boolean colorModeScale;
    boolean colorModeDefault;
    protected boolean is3D = false;
    protected boolean perVertexStyles = false;

    public PShape() {
        this.family = 0;
    }

    public PShape(int family) {
        this.family = family;
    }

    public PShape(PGraphics g, int family) {
        this.g = g;
        this.family = family;
        this.textureMode = g.textureMode;
        this.colorMode(g.colorMode, g.colorModeX, g.colorModeY, g.colorModeZ, g.colorModeA);
        this.fill = g.fill;
        this.fillColor = g.fillColor;
        this.stroke = g.stroke;
        this.strokeColor = g.strokeColor;
        this.strokeWeight = g.strokeWeight;
        this.strokeCap = g.strokeCap;
        this.strokeJoin = g.strokeJoin;
        this.tint = g.tint;
        this.tintColor = g.tintColor;
        this.setAmbient = g.setAmbient;
        this.ambientColor = g.ambientColor;
        this.specularColor = g.specularColor;
        this.emissiveColor = g.emissiveColor;
        this.shininess = g.shininess;
        this.sphereDetailU = g.sphereDetailU;
        this.sphereDetailV = g.sphereDetailV;
        this.rectMode = g.rectMode;
        this.ellipseMode = g.ellipseMode;
    }

    public PShape(PGraphics g, int kind, float ... params) {
        this(g, 101);
        this.setKind(kind);
        this.setParams(params);
    }

    public void setFamily(int family) {
        this.family = family;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void disableStyle() {
        this.style = false;
        int i = 0;
        while (i < this.childCount) {
            this.children[i].disableStyle();
            ++i;
        }
    }

    public void enableStyle() {
        this.style = true;
        int i = 0;
        while (i < this.childCount) {
            this.children[i].enableStyle();
            ++i;
        }
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public float getDepth() {
        return this.depth;
    }

    public boolean is2D() {
        return !this.is3D;
    }

    public boolean is3D() {
        return this.is3D;
    }

    public void set3D(boolean val) {
        this.is3D = val;
    }

    public void textureMode(int mode) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"textureMode()"});
            return;
        }
        this.textureMode = mode;
    }

    public void texture(PImage tex) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"texture()"});
            return;
        }
        this.image = tex;
    }

    public void noTexture() {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"noTexture()"});
            return;
        }
        this.image = null;
    }

    protected void solid(boolean solid) {
    }

    public void beginContour() {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"beginContour()"});
            return;
        }
        if (this.family == 0) {
            PGraphics.showWarning((String)"Cannot begin contour in GROUP shapes");
            return;
        }
        if (this.openContour) {
            PGraphics.showWarning((String)"Already called beginContour().");
            return;
        }
        this.openContour = true;
        this.beginContourImpl();
    }

    protected void beginContourImpl() {
        if (this.vertexCodes == null) {
            this.vertexCodes = new int[10];
        } else if (this.vertexCodes.length == this.vertexCodeCount) {
            this.vertexCodes = PApplet.expand(this.vertexCodes);
        }
        this.vertexCodes[this.vertexCodeCount++] = 4;
    }

    public void endContour() {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"endContour()"});
            return;
        }
        if (this.family == 0) {
            PGraphics.showWarning((String)"Cannot end contour in GROUP shapes");
            return;
        }
        if (!this.openContour) {
            PGraphics.showWarning((String)"Need to call beginContour() first.");
            return;
        }
        this.endContourImpl();
        this.openContour = false;
    }

    protected void endContourImpl() {
    }

    public void vertex(float x, float y) {
        if (this.vertices == null) {
            this.vertices = new float[10][2];
        } else if (this.vertices.length == this.vertexCount) {
            this.vertices = (float[][])PApplet.expand(this.vertices);
        }
        this.vertices[this.vertexCount++] = new float[]{x, y};
        if (this.vertexCodes == null) {
            this.vertexCodes = new int[10];
        } else if (this.vertexCodes.length == this.vertexCodeCount) {
            this.vertexCodes = PApplet.expand(this.vertexCodes);
        }
        this.vertexCodes[this.vertexCodeCount++] = 0;
        if (x > this.width) {
            this.width = x;
        }
        if (y > this.height) {
            this.height = y;
        }
    }

    public void vertex(float x, float y, float u, float v) {
    }

    public void vertex(float x, float y, float z) {
        this.vertex(x, y);
    }

    public void vertex(float x, float y, float z, float u, float v) {
    }

    public void normal(float nx, float ny, float nz) {
    }

    public void attribPosition(String name, float x, float y, float z) {
    }

    public void attribNormal(String name, float nx, float ny, float nz) {
    }

    public void attribColor(String name, int color) {
    }

    public void attrib(String name, float ... values) {
    }

    public void attrib(String name, int ... values) {
    }

    public void attrib(String name, boolean ... values) {
    }

    public void beginShape() {
        this.beginShape(20);
    }

    public void beginShape(int kind) {
        this.kind = kind;
        this.openShape = true;
    }

    public void endShape() {
        this.endShape(1);
    }

    public void endShape(int mode) {
        if (this.family == 0) {
            PGraphics.showWarning((String)"Cannot end GROUP shape");
            return;
        }
        if (!this.openShape) {
            PGraphics.showWarning((String)"Need to call beginShape() first");
            return;
        }
        this.close = mode == 2;
        this.openShape = false;
    }

    public void strokeWeight(float weight) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"strokeWeight()"});
            return;
        }
        this.strokeWeight = weight;
    }

    public void strokeJoin(int join) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"strokeJoin()"});
            return;
        }
        this.strokeJoin = join;
    }

    public void strokeCap(int cap) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"strokeCap()"});
            return;
        }
        this.strokeCap = cap;
    }

    public void noFill() {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"noFill()"});
            return;
        }
        this.fill = false;
        this.fillColor = 0;
        if (!this.setAmbient) {
            this.ambientColor = this.fillColor;
        }
    }

    public void fill(int rgb) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"fill()"});
            return;
        }
        this.fill = true;
        this.colorCalc(rgb);
        this.fillColor = this.calcColor;
        if (!this.setAmbient) {
            this.ambientColor = this.fillColor;
        }
    }

    public void fill(int rgb, float alpha) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"fill()"});
            return;
        }
        this.fill = true;
        this.colorCalc(rgb, alpha);
        this.fillColor = this.calcColor;
        if (!this.setAmbient) {
            this.ambientColor = this.fillColor;
        }
    }

    public void fill(float gray) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"fill()"});
            return;
        }
        this.fill = true;
        this.colorCalc(gray);
        this.fillColor = this.calcColor;
        if (!this.setAmbient) {
            this.ambientColor = this.fillColor;
        }
    }

    public void fill(float gray, float alpha) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"fill()"});
            return;
        }
        this.fill = true;
        this.colorCalc(gray, alpha);
        this.fillColor = this.calcColor;
        if (!this.setAmbient) {
            this.ambient(this.fillColor);
            this.setAmbient = false;
        }
        if (!this.setAmbient) {
            this.ambientColor = this.fillColor;
        }
    }

    public void fill(float x, float y, float z) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"fill()"});
            return;
        }
        this.fill = true;
        this.colorCalc(x, y, z);
        this.fillColor = this.calcColor;
        if (!this.setAmbient) {
            this.ambientColor = this.fillColor;
        }
    }

    public void fill(float x, float y, float z, float a) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"fill()"});
            return;
        }
        this.fill = true;
        this.colorCalc(x, y, z, a);
        this.fillColor = this.calcColor;
        if (!this.setAmbient) {
            this.ambientColor = this.fillColor;
        }
    }

    public void noStroke() {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"noStroke()"});
            return;
        }
        this.stroke = false;
    }

    public void stroke(int rgb) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"stroke()"});
            return;
        }
        this.stroke = true;
        this.colorCalc(rgb);
        this.strokeColor = this.calcColor;
    }

    public void stroke(int rgb, float alpha) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"stroke()"});
            return;
        }
        this.stroke = true;
        this.colorCalc(rgb, alpha);
        this.strokeColor = this.calcColor;
    }

    public void stroke(float gray) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"stroke()"});
            return;
        }
        this.stroke = true;
        this.colorCalc(gray);
        this.strokeColor = this.calcColor;
    }

    public void stroke(float gray, float alpha) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"stroke()"});
            return;
        }
        this.stroke = true;
        this.colorCalc(gray, alpha);
        this.strokeColor = this.calcColor;
    }

    public void stroke(float x, float y, float z) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"stroke()"});
            return;
        }
        this.stroke = true;
        this.colorCalc(x, y, z);
        this.strokeColor = this.calcColor;
    }

    public void stroke(float x, float y, float z, float alpha) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"stroke()"});
            return;
        }
        this.stroke = true;
        this.colorCalc(x, y, z, alpha);
        this.strokeColor = this.calcColor;
    }

    public void noTint() {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"noTint()"});
            return;
        }
        this.tint = false;
    }

    public void tint(int rgb) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"tint()"});
            return;
        }
        this.tint = true;
        this.colorCalc(rgb);
        this.tintColor = this.calcColor;
    }

    public void tint(int rgb, float alpha) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"tint()"});
            return;
        }
        this.tint = true;
        this.colorCalc(rgb, alpha);
        this.tintColor = this.calcColor;
    }

    public void tint(float gray) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"tint()"});
            return;
        }
        this.tint = true;
        this.colorCalc(gray);
        this.tintColor = this.calcColor;
    }

    public void tint(float gray, float alpha) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"tint()"});
            return;
        }
        this.tint = true;
        this.colorCalc(gray, alpha);
        this.tintColor = this.calcColor;
    }

    public void tint(float x, float y, float z) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"tint()"});
            return;
        }
        this.tint = true;
        this.colorCalc(x, y, z);
        this.tintColor = this.calcColor;
    }

    public void tint(float x, float y, float z, float alpha) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"tint()"});
            return;
        }
        this.tint = true;
        this.colorCalc(x, y, z, alpha);
        this.tintColor = this.calcColor;
    }

    public void ambient(int rgb) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"ambient()"});
            return;
        }
        this.setAmbient = true;
        this.colorCalc(rgb);
        this.ambientColor = this.calcColor;
    }

    public void ambient(float gray) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"ambient()"});
            return;
        }
        this.setAmbient = true;
        this.colorCalc(gray);
        this.ambientColor = this.calcColor;
    }

    public void ambient(float x, float y, float z) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"ambient()"});
            return;
        }
        this.setAmbient = true;
        this.colorCalc(x, y, z);
        this.ambientColor = this.calcColor;
    }

    public void specular(int rgb) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"specular()"});
            return;
        }
        this.colorCalc(rgb);
        this.specularColor = this.calcColor;
    }

    public void specular(float gray) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"specular()"});
            return;
        }
        this.colorCalc(gray);
        this.specularColor = this.calcColor;
    }

    public void specular(float x, float y, float z) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"specular()"});
            return;
        }
        this.colorCalc(x, y, z);
        this.specularColor = this.calcColor;
    }

    public void emissive(int rgb) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"emissive()"});
            return;
        }
        this.colorCalc(rgb);
        this.emissiveColor = this.calcColor;
    }

    public void emissive(float gray) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"emissive()"});
            return;
        }
        this.colorCalc(gray);
        this.emissiveColor = this.calcColor;
    }

    public void emissive(float x, float y, float z) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"emissive()"});
            return;
        }
        this.colorCalc(x, y, z);
        this.emissiveColor = this.calcColor;
    }

    public void shininess(float shine) {
        if (!this.openShape) {
            PGraphics.showWarning((String)OUTSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"shininess()"});
            return;
        }
        this.shininess = shine;
    }

    public void bezierDetail(int detail) {
    }

    public void bezierVertex(float x2, float y2, float x3, float y3, float x4, float y4) {
        if (this.vertices == null) {
            this.vertices = new float[10][];
        } else if (this.vertexCount + 2 >= this.vertices.length) {
            this.vertices = (float[][])PApplet.expand(this.vertices);
        }
        this.vertices[this.vertexCount++] = new float[]{x2, y2};
        this.vertices[this.vertexCount++] = new float[]{x3, y3};
        this.vertices[this.vertexCount++] = new float[]{x4, y4};
        if (this.vertexCodes.length == this.vertexCodeCount) {
            this.vertexCodes = PApplet.expand(this.vertexCodes);
        }
        this.vertexCodes[this.vertexCodeCount++] = 1;
        if (x4 > this.width) {
            this.width = x4;
        }
        if (y4 > this.height) {
            this.height = y4;
        }
    }

    public void bezierVertex(float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
    }

    public void quadraticVertex(float cx, float cy, float x3, float y3) {
        if (this.vertices == null) {
            this.vertices = new float[10][];
        } else if (this.vertexCount + 1 >= this.vertices.length) {
            this.vertices = (float[][])PApplet.expand(this.vertices);
        }
        this.vertices[this.vertexCount++] = new float[]{cx, cy};
        this.vertices[this.vertexCount++] = new float[]{x3, y3};
        if (this.vertexCodes.length == this.vertexCodeCount) {
            this.vertexCodes = PApplet.expand(this.vertexCodes);
        }
        this.vertexCodes[this.vertexCodeCount++] = 2;
        if (x3 > this.width) {
            this.width = x3;
        }
        if (y3 > this.height) {
            this.height = y3;
        }
    }

    public void quadraticVertex(float cx, float cy, float cz, float x3, float y3, float z3) {
    }

    public void curveDetail(int detail) {
    }

    public void curveTightness(float tightness) {
    }

    public void curveVertex(float x, float y) {
    }

    public void curveVertex(float x, float y, float z) {
    }

    protected void pre(PGraphics g) {
        if (this.matrix != null) {
            g.pushMatrix();
            g.applyMatrix(this.matrix);
        }
        if (this.style) {
            g.pushStyle();
            this.styles(g);
        }
    }

    protected void styles(PGraphics g) {
        if (this.stroke) {
            g.stroke(this.strokeColor);
            g.strokeWeight(this.strokeWeight);
            g.strokeCap(this.strokeCap);
            g.strokeJoin(this.strokeJoin);
        } else {
            g.noStroke();
        }
        if (this.fill) {
            g.fill(this.fillColor);
        } else {
            g.noFill();
        }
    }

    protected void post(PGraphics g) {
        if (this.matrix != null) {
            g.popMatrix();
        }
        if (this.style) {
            g.popStyle();
        }
    }

    protected static PShape createShape(PApplet parent, PShape src) {
        PShape dest = null;
        if (src.family == 0) {
            dest = parent.createShape(0);
            PShape.copyGroup(parent, src, dest);
        } else if (src.family == 101) {
            dest = parent.createShape(src.kind, src.params);
            PShape.copyPrimitive(src, dest);
        } else if (src.family == 103) {
            dest = parent.createShape(src.kind);
            PShape.copyGeometry(src, dest);
        } else if (src.family == 102) {
            dest = parent.createShape(102);
            PShape.copyPath(src, dest);
        }
        dest.setName(src.name);
        return dest;
    }

    protected static void copyGroup(PApplet parent, PShape src, PShape dest) {
        PShape.copyMatrix(src, dest);
        PShape.copyStyles(src, dest);
        PShape.copyImage(src, dest);
        int i = 0;
        while (i < src.childCount) {
            PShape c = PShape.createShape(parent, src.children[i]);
            dest.addChild(c);
            ++i;
        }
    }

    protected static void copyPrimitive(PShape src, PShape dest) {
        PShape.copyMatrix(src, dest);
        PShape.copyStyles(src, dest);
        PShape.copyImage(src, dest);
    }

    protected static void copyGeometry(PShape src, PShape dest) {
        dest.beginShape(src.getKind());
        PShape.copyMatrix(src, dest);
        PShape.copyStyles(src, dest);
        PShape.copyImage(src, dest);
        if (src.style) {
            int i = 0;
            while (i < src.vertexCount) {
                float[] vert = src.vertices[i];
                dest.fill((int)(vert[6] * 255.0f) << 24 | (int)(vert[3] * 255.0f) << 16 | (int)(vert[4] * 255.0f) << 8 | (int)(vert[5] * 255.0f));
                if (0.0f < PApplet.dist(vert[9], vert[10], vert[11], 0.0f, 0.0f, 0.0f)) {
                    dest.normal(vert[9], vert[10], vert[11]);
                }
                dest.vertex(vert[0], vert[1], vert[2], vert[7], vert[8]);
                ++i;
            }
        } else {
            int i = 0;
            while (i < src.vertexCount) {
                float[] vert = src.vertices[i];
                if (vert[2] == 0.0f) {
                    dest.vertex(vert[0], vert[1]);
                } else {
                    dest.vertex(vert[0], vert[1], vert[2]);
                }
                ++i;
            }
        }
        dest.endShape();
    }

    protected static void copyPath(PShape src, PShape dest) {
        PShape.copyMatrix(src, dest);
        PShape.copyStyles(src, dest);
        PShape.copyImage(src, dest);
        dest.close = src.close;
        dest.setPath(src.vertexCount, src.vertices, src.vertexCodeCount, src.vertexCodes);
    }

    protected static void copyMatrix(PShape src, PShape dest) {
        if (src.matrix != null) {
            dest.applyMatrix(src.matrix);
        }
    }

    protected static void copyStyles(PShape src, PShape dest) {
        dest.ellipseMode = src.ellipseMode;
        dest.rectMode = src.rectMode;
        if (src.stroke) {
            dest.stroke = true;
            dest.strokeColor = src.strokeColor;
            dest.strokeWeight = src.strokeWeight;
            dest.strokeCap = src.strokeCap;
            dest.strokeJoin = src.strokeJoin;
        } else {
            dest.stroke = false;
        }
        if (src.fill) {
            dest.fill = true;
            dest.fillColor = src.fillColor;
        } else {
            dest.fill = false;
        }
    }

    protected static void copyImage(PShape src, PShape dest) {
        if (src.image != null) {
            dest.texture(src.image);
        }
    }

    public void draw(PGraphics g) {
        if (this.visible) {
            this.pre(g);
            this.drawImpl(g);
            this.post(g);
        }
    }

    protected void drawImpl(PGraphics g) {
        if (this.family == 0) {
            this.drawGroup(g);
        } else if (this.family == 101) {
            this.drawPrimitive(g);
        } else if (this.family == 103) {
            this.drawGeometry(g);
        } else if (this.family == 102) {
            this.drawPath(g);
        }
    }

    protected void drawGroup(PGraphics g) {
        int i = 0;
        while (i < this.childCount) {
            this.children[i].draw(g);
            ++i;
        }
    }

    protected void drawPrimitive(PGraphics g) {
        if (this.kind == 2) {
            g.point(this.params[0], this.params[1]);
        } else if (this.kind == 4) {
            if (this.params.length == 4) {
                g.line(this.params[0], this.params[1], this.params[2], this.params[3]);
            } else {
                g.line(this.params[0], this.params[1], this.params[2], this.params[3], this.params[4], this.params[5]);
            }
        } else if (this.kind == 8) {
            g.triangle(this.params[0], this.params[1], this.params[2], this.params[3], this.params[4], this.params[5]);
        } else if (this.kind == 16) {
            g.quad(this.params[0], this.params[1], this.params[2], this.params[3], this.params[4], this.params[5], this.params[6], this.params[7]);
        } else if (this.kind == 30) {
            if (this.imagePath != null) {
                this.loadImage(g);
            }
            if (this.image != null) {
                int oldMode = g.imageMode;
                g.imageMode(0);
                g.image(this.image, this.params[0], this.params[1], this.params[2], this.params[3]);
                g.imageMode(oldMode);
            } else {
                int oldMode = g.rectMode;
                g.rectMode(this.rectMode);
                if (this.params.length == 4) {
                    g.rect(this.params[0], this.params[1], this.params[2], this.params[3]);
                } else if (this.params.length == 5) {
                    g.rect(this.params[0], this.params[1], this.params[2], this.params[3], this.params[4]);
                } else if (this.params.length == 8) {
                    g.rect(this.params[0], this.params[1], this.params[2], this.params[3], this.params[4], this.params[5], this.params[6], this.params[7]);
                }
                g.rectMode(oldMode);
            }
        } else if (this.kind == 31) {
            int oldMode = g.ellipseMode;
            g.ellipseMode(this.ellipseMode);
            g.ellipse(this.params[0], this.params[1], this.params[2], this.params[3]);
            g.ellipseMode(oldMode);
        } else if (this.kind == 32) {
            int oldMode = g.ellipseMode;
            g.ellipseMode(this.ellipseMode);
            if (this.params.length == 6) {
                g.arc(this.params[0], this.params[1], this.params[2], this.params[3], this.params[4], this.params[5]);
            } else if (this.params.length == 7) {
                g.arc(this.params[0], this.params[1], this.params[2], this.params[3], this.params[4], this.params[5], (int)this.params[6]);
            }
            g.ellipseMode(oldMode);
        } else if (this.kind == 41) {
            if (this.params.length == 1) {
                g.box(this.params[0]);
            } else {
                g.box(this.params[0], this.params[1], this.params[2]);
            }
        } else if (this.kind == 40) {
            g.sphere(this.params[0]);
        }
    }

    protected void drawGeometry(PGraphics g) {
        g.beginShape(this.kind);
        if (this.style) {
            int i = 0;
            while (i < this.vertexCount) {
                g.vertex(this.vertices[i]);
                ++i;
            }
        } else {
            int i = 0;
            while (i < this.vertexCount) {
                float[] vert = this.vertices[i];
                if (vert[2] == 0.0f) {
                    g.vertex(vert[0], vert[1]);
                } else {
                    g.vertex(vert[0], vert[1], vert[2]);
                }
                ++i;
            }
        }
        g.endShape(this.close ? 2 : 1);
    }

    protected void drawPath(PGraphics g) {
        if (this.vertices == null) {
            return;
        }
        boolean insideContour = false;
        g.beginShape();
        if (this.vertexCodeCount == 0) {
            if (this.vertices[0].length == 2) {
                int i = 0;
                while (i < this.vertexCount) {
                    g.vertex(this.vertices[i][0], this.vertices[i][1]);
                    ++i;
                }
            } else {
                int i = 0;
                while (i < this.vertexCount) {
                    g.vertex(this.vertices[i][0], this.vertices[i][1], this.vertices[i][2]);
                    ++i;
                }
            }
        } else {
            int index = 0;
            if (this.vertices[0].length == 2) {
                int j = 0;
                while (j < this.vertexCodeCount) {
                    switch (this.vertexCodes[j]) {
                        case 0: {
                            g.vertex(this.vertices[index][0], this.vertices[index][1]);
                            ++index;
                            break;
                        }
                        case 2: {
                            g.quadraticVertex(this.vertices[index + 0][0], this.vertices[index + 0][1], this.vertices[index + 1][0], this.vertices[index + 1][1]);
                            index += 2;
                            break;
                        }
                        case 1: {
                            g.bezierVertex(this.vertices[index + 0][0], this.vertices[index + 0][1], this.vertices[index + 1][0], this.vertices[index + 1][1], this.vertices[index + 2][0], this.vertices[index + 2][1]);
                            index += 3;
                            break;
                        }
                        case 3: {
                            g.curveVertex(this.vertices[index][0], this.vertices[index][1]);
                            ++index;
                            break;
                        }
                        case 4: {
                            if (insideContour) {
                                g.endContour();
                            }
                            g.beginContour();
                            insideContour = true;
                        }
                    }
                    ++j;
                }
            } else {
                int j = 0;
                while (j < this.vertexCodeCount) {
                    switch (this.vertexCodes[j]) {
                        case 0: {
                            g.vertex(this.vertices[index][0], this.vertices[index][1], this.vertices[index][2]);
                            ++index;
                            break;
                        }
                        case 2: {
                            g.quadraticVertex(this.vertices[index + 0][0], this.vertices[index + 0][1], this.vertices[index + 0][2], this.vertices[index + 1][0], this.vertices[index + 1][1], this.vertices[index + 0][2]);
                            index += 2;
                            break;
                        }
                        case 1: {
                            g.bezierVertex(this.vertices[index + 0][0], this.vertices[index + 0][1], this.vertices[index + 0][2], this.vertices[index + 1][0], this.vertices[index + 1][1], this.vertices[index + 1][2], this.vertices[index + 2][0], this.vertices[index + 2][1], this.vertices[index + 2][2]);
                            index += 3;
                            break;
                        }
                        case 3: {
                            g.curveVertex(this.vertices[index][0], this.vertices[index][1], this.vertices[index][2]);
                            ++index;
                            break;
                        }
                        case 4: {
                            if (insideContour) {
                                g.endContour();
                            }
                            g.beginContour();
                            insideContour = true;
                        }
                    }
                    ++j;
                }
            }
        }
        if (insideContour) {
            g.endContour();
        }
        g.endShape(this.close ? 2 : 1);
    }

    private void loadImage(PGraphics g) {
        if (this.imagePath.startsWith("data:image")) {
            this.loadBase64Image();
        }
        if (this.imagePath.startsWith("file://")) {
            this.loadFileSystemImage(g);
        }
        this.imagePath = null;
    }

    private void loadFileSystemImage(PGraphics g) {
        this.imagePath = this.imagePath.substring(7);
        PImage loadedImage = g.parent.loadImage(this.imagePath);
        if (loadedImage == null) {
            System.err.println("Error loading image file: " + this.imagePath);
        } else {
            this.setTexture(loadedImage);
        }
    }

    private void loadBase64Image() {
        BufferedImage buffImage;
        int space;
        String[] parts = this.imagePath.split(";base64,");
        String extension = parts[0].substring(11);
        String encodedData = parts[1];
        byte[] decodedBytes = DatatypeConverter.parseBase64Binary((String)encodedData);
        if (decodedBytes == null) {
            System.err.println("Decode Error on image: " + this.imagePath.substring(0, 20));
            return;
        }
        Image awtImage = new ImageIcon(decodedBytes).getImage();
        if (awtImage instanceof BufferedImage && (space = (buffImage = (BufferedImage)awtImage).getColorModel().getColorSpace().getType()) == 9) {
            return;
        }
        PImage loadedImage = new PImage(awtImage);
        int cfr_ignored_0 = loadedImage.width;
        if (extension.equals((Object)"gif") || extension.equals((Object)"png") || extension.equals((Object)"unknown")) {
            loadedImage.checkAlpha();
        }
        this.setTexture(loadedImage);
    }

    public PShape getParent() {
        return this.parent;
    }

    public int getChildCount() {
        return this.childCount;
    }

    protected void crop() {
        if (this.children.length != this.childCount) {
            this.children = (PShape[])PApplet.subset(this.children, 0, this.childCount);
        }
    }

    public PShape[] getChildren() {
        this.crop();
        return this.children;
    }

    public PShape getChild(int index) {
        this.crop();
        return this.children[index];
    }

    public PShape getChild(String target) {
        PShape found;
        if (this.name != null && this.name.equals((Object)target)) {
            return this;
        }
        if (this.nameTable != null && (found = (PShape)this.nameTable.get((Object)target)) != null) {
            return found;
        }
        int i = 0;
        while (i < this.childCount) {
            PShape found2 = this.children[i].getChild(target);
            if (found2 != null) {
                return found2;
            }
            ++i;
        }
        return null;
    }

    public PShape findChild(String target) {
        if (this.parent == null) {
            return this.getChild(target);
        }
        return this.parent.findChild(target);
    }

    public void addChild(PShape who) {
        if (this.children == null) {
            this.children = new PShape[1];
        }
        if (this.childCount == this.children.length) {
            this.children = (PShape[])PApplet.expand(this.children);
        }
        this.children[this.childCount++] = who;
        who.parent = this;
        if (who.getName() != null) {
            this.addName(who.getName(), who);
        }
    }

    public void addChild(PShape who, int idx) {
        if (idx < this.childCount) {
            if (this.childCount == this.children.length) {
                this.children = (PShape[])PApplet.expand(this.children);
            }
            int i = this.childCount - 1;
            while (i >= idx) {
                this.children[i + 1] = this.children[i];
                --i;
            }
            ++this.childCount;
            this.children[idx] = who;
            who.parent = this;
            if (who.getName() != null) {
                this.addName(who.getName(), who);
            }
        }
    }

    public void removeChild(int idx) {
        if (idx < this.childCount) {
            PShape child = this.children[idx];
            int i = idx;
            while (i < this.childCount - 1) {
                this.children[i] = this.children[i + 1];
                ++i;
            }
            --this.childCount;
            if (child.getName() != null && this.nameTable != null) {
                this.nameTable.remove((Object)child.getName());
            }
        }
    }

    public void addName(String nom, PShape shape) {
        if (this.parent != null) {
            this.parent.addName(nom, shape);
        } else {
            if (this.nameTable == null) {
                this.nameTable = new HashMap();
            }
            this.nameTable.put((Object)nom, (Object)shape);
        }
    }

    public int getChildIndex(PShape who) {
        int i = 0;
        while (i < this.childCount) {
            if (this.children[i] == who) {
                return i;
            }
            ++i;
        }
        return -1;
    }

    public PShape getTessellation() {
        return null;
    }

    public int getFamily() {
        return this.family;
    }

    public int getKind() {
        return this.kind;
    }

    public float[] getParams() {
        return this.getParams(null);
    }

    public float[] getParams(float[] target) {
        if (target == null || target.length != this.params.length) {
            target = new float[this.params.length];
        }
        PApplet.arrayCopy(this.params, target);
        return target;
    }

    public float getParam(int index) {
        return this.params[index];
    }

    protected void setParams(float[] source) {
        if (this.params == null) {
            this.params = new float[source.length];
        }
        if (source.length != this.params.length) {
            PGraphics.showWarning((String)"Wrong number of parameters");
            return;
        }
        PApplet.arrayCopy(source, this.params);
    }

    public void setPath(int vcount, float[][] verts) {
        this.setPath(vcount, verts, 0, null);
    }

    protected void setPath(int vcount, float[][] verts, int ccount, int[] codes) {
        if (verts == null || verts.length < vcount) {
            return;
        }
        if (ccount > 0 && (codes == null || codes.length < ccount)) {
            return;
        }
        int ndim = verts[0].length;
        this.vertexCount = vcount;
        this.vertices = new float[this.vertexCount][ndim];
        int i = 0;
        while (i < this.vertexCount) {
            PApplet.arrayCopy(verts[i], this.vertices[i]);
            ++i;
        }
        this.vertexCodeCount = ccount;
        if (this.vertexCodeCount > 0) {
            this.vertexCodes = new int[this.vertexCodeCount];
            PApplet.arrayCopy(codes, this.vertexCodes, this.vertexCodeCount);
        }
    }

    public int getVertexCount() {
        if (this.family == 0 || this.family == 101) {
            PGraphics.showWarning((String)NO_VERTICES_ERROR);
        }
        return this.vertexCount;
    }

    public PVector getVertex(int index) {
        return this.getVertex(index, null);
    }

    public PVector getVertex(int index, PVector vec) {
        if (vec == null) {
            vec = new PVector();
        }
        float[] vert = this.vertices[index];
        vec.x = vert[0];
        vec.y = vert[1];
        vec.z = vert.length > 2 ? vert[2] : 0.0f;
        return vec;
    }

    public float getVertexX(int index) {
        return this.vertices[index][0];
    }

    public float getVertexY(int index) {
        return this.vertices[index][1];
    }

    public float getVertexZ(int index) {
        return this.vertices[index][2];
    }

    public void setVertex(int index, float x, float y) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setVertex()"});
            return;
        }
        this.vertices[index][0] = x;
        this.vertices[index][1] = y;
    }

    public void setVertex(int index, float x, float y, float z) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setVertex()"});
            return;
        }
        this.vertices[index][0] = x;
        this.vertices[index][1] = y;
        this.vertices[index][2] = z;
    }

    public void setVertex(int index, PVector vec) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setVertex()"});
            return;
        }
        this.vertices[index][0] = vec.x;
        this.vertices[index][1] = vec.y;
        if (this.vertices[index].length > 2) {
            this.vertices[index][2] = vec.z;
        } else if (vec.z != 0.0f && vec.z == vec.z) {
            throw new IllegalArgumentException("Cannot set a z-coordinate on a 2D shape");
        }
    }

    public PVector getNormal(int index) {
        return this.getNormal(index, null);
    }

    public PVector getNormal(int index, PVector vec) {
        if (vec == null) {
            vec = new PVector();
        }
        vec.x = this.vertices[index][9];
        vec.y = this.vertices[index][10];
        vec.z = this.vertices[index][11];
        return vec;
    }

    public float getNormalX(int index) {
        return this.vertices[index][9];
    }

    public float getNormalY(int index) {
        return this.vertices[index][10];
    }

    public float getNormalZ(int index) {
        return this.vertices[index][11];
    }

    public void setNormal(int index, float nx, float ny, float nz) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setNormal()"});
            return;
        }
        this.vertices[index][9] = nx;
        this.vertices[index][10] = ny;
        this.vertices[index][11] = nz;
    }

    public void setAttrib(String name, int index, float ... values) {
    }

    public void setAttrib(String name, int index, int ... values) {
    }

    public void setAttrib(String name, int index, boolean ... values) {
    }

    public float getTextureU(int index) {
        return this.vertices[index][7];
    }

    public float getTextureV(int index) {
        return this.vertices[index][8];
    }

    public void setTextureUV(int index, float u, float v) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setTextureUV()"});
            return;
        }
        if (this.vertices == null || index >= this.vertices.length) {
            PGraphics.showWarning((String)("%1$s vertex index does not exist (" + index + ")"), (Object[])new Object[]{"setTextureUV()"});
            return;
        }
        this.vertices[index][7] = u;
        this.vertices[index][8] = v;
    }

    public void setTextureMode(int mode) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setTextureMode()"});
            return;
        }
        this.textureMode = mode;
    }

    public void setTexture(PImage tex) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setTexture()"});
            return;
        }
        this.image = tex;
    }

    public int getFill(int index) {
        if (this.vertices == null || index >= this.vertices.length) {
            PGraphics.showWarning((String)("%1$s vertex index does not exist (" + index + ")"), (Object[])new Object[]{"getFill()"});
            return this.fillColor;
        }
        if (this.image == null) {
            int a = (int)(this.vertices[index][6] * 255.0f);
            int r = (int)(this.vertices[index][3] * 255.0f);
            int g = (int)(this.vertices[index][4] * 255.0f);
            int b = (int)(this.vertices[index][5] * 255.0f);
            return a << 24 | r << 16 | g << 8 | b;
        }
        return 0;
    }

    public void setFill(boolean fill) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setFill()"});
            return;
        }
        this.fill = fill;
    }

    public void setFill(int fill) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setFill()"});
            return;
        }
        this.fillColor = fill;
        if (this.vertices != null && this.perVertexStyles) {
            int i = 0;
            while (i < this.vertexCount) {
                this.setFill(i, fill);
                ++i;
            }
        }
    }

    public void setFill(int index, int fill) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setFill()"});
            return;
        }
        if (!this.perVertexStyles) {
            PGraphics.showWarning((String)PER_VERTEX_UNSUPPORTED, (Object[])new Object[]{"setFill()"});
            return;
        }
        if (this.vertices == null || index >= this.vertices.length) {
            PGraphics.showWarning((String)("%1$s vertex index does not exist (" + index + ")"), (Object[])new Object[]{"getFill()"});
            return;
        }
        if (this.image == null) {
            this.vertices[index][6] = (float)(fill >> 24 & 0xFF) / 255.0f;
            this.vertices[index][3] = (float)(fill >> 16 & 0xFF) / 255.0f;
            this.vertices[index][4] = (float)(fill >> 8 & 0xFF) / 255.0f;
            this.vertices[index][5] = (float)(fill >> 0 & 0xFF) / 255.0f;
        }
    }

    public int getTint(int index) {
        if (this.vertices == null || index >= this.vertices.length) {
            PGraphics.showWarning((String)("%1$s vertex index does not exist (" + index + ")"), (Object[])new Object[]{"getTint()"});
            return this.tintColor;
        }
        if (this.image != null) {
            int a = (int)(this.vertices[index][6] * 255.0f);
            int r = (int)(this.vertices[index][3] * 255.0f);
            int g = (int)(this.vertices[index][4] * 255.0f);
            int b = (int)(this.vertices[index][5] * 255.0f);
            return a << 24 | r << 16 | g << 8 | b;
        }
        return 0;
    }

    public void setTint(boolean tint) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setTint()"});
            return;
        }
        this.tint = tint;
    }

    public void setTint(int fill) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setTint()"});
            return;
        }
        this.tintColor = fill;
        if (this.vertices != null) {
            int i = 0;
            while (i < this.vertices.length) {
                this.setFill(i, fill);
                ++i;
            }
        }
    }

    public void setTint(int index, int tint) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setTint()"});
            return;
        }
        if (this.vertices == null || index >= this.vertices.length) {
            PGraphics.showWarning((String)("%1$s vertex index does not exist (" + index + ")"), (Object[])new Object[]{"setTint()"});
            return;
        }
        if (this.image != null) {
            this.vertices[index][6] = (float)(tint >> 24 & 0xFF) / 255.0f;
            this.vertices[index][3] = (float)(tint >> 16 & 0xFF) / 255.0f;
            this.vertices[index][4] = (float)(tint >> 8 & 0xFF) / 255.0f;
            this.vertices[index][5] = (float)(tint >> 0 & 0xFF) / 255.0f;
        }
    }

    public int getStroke(int index) {
        if (this.vertices == null || index >= this.vertices.length) {
            PGraphics.showWarning((String)("%1$s vertex index does not exist (" + index + ")"), (Object[])new Object[]{"getStroke()"});
            return this.strokeColor;
        }
        int a = (int)(this.vertices[index][16] * 255.0f);
        int r = (int)(this.vertices[index][13] * 255.0f);
        int g = (int)(this.vertices[index][14] * 255.0f);
        int b = (int)(this.vertices[index][15] * 255.0f);
        return a << 24 | r << 16 | g << 8 | b;
    }

    public void setStroke(boolean stroke) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setStroke()"});
            return;
        }
        this.stroke = stroke;
    }

    public void setStroke(int stroke) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setStroke()"});
            return;
        }
        this.strokeColor = stroke;
        if (this.vertices != null && this.perVertexStyles) {
            int i = 0;
            while (i < this.vertices.length) {
                this.setStroke(i, stroke);
                ++i;
            }
        }
    }

    public void setStroke(int index, int stroke) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setStroke()"});
            return;
        }
        if (!this.perVertexStyles) {
            PGraphics.showWarning((String)PER_VERTEX_UNSUPPORTED, (Object[])new Object[]{"setStroke()"});
            return;
        }
        if (this.vertices == null || index >= this.vertices.length) {
            PGraphics.showWarning((String)("%1$s vertex index does not exist (" + index + ")"), (Object[])new Object[]{"setStroke()"});
            return;
        }
        this.vertices[index][16] = (float)(stroke >> 24 & 0xFF) / 255.0f;
        this.vertices[index][13] = (float)(stroke >> 16 & 0xFF) / 255.0f;
        this.vertices[index][14] = (float)(stroke >> 8 & 0xFF) / 255.0f;
        this.vertices[index][15] = (float)(stroke >> 0 & 0xFF) / 255.0f;
    }

    public float getStrokeWeight(int index) {
        if (this.vertices == null || index >= this.vertices.length) {
            PGraphics.showWarning((String)("%1$s vertex index does not exist (" + index + ")"), (Object[])new Object[]{"getStrokeWeight()"});
            return this.strokeWeight;
        }
        return this.vertices[index][17];
    }

    public void setStrokeWeight(float weight) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setStrokeWeight()"});
            return;
        }
        this.strokeWeight = weight;
        if (this.vertices != null && this.perVertexStyles) {
            int i = 0;
            while (i < this.vertexCount) {
                this.setStrokeWeight(i, weight);
                ++i;
            }
        }
    }

    public void setStrokeWeight(int index, float weight) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setStrokeWeight()"});
            return;
        }
        if (!this.perVertexStyles) {
            PGraphics.showWarning((String)PER_VERTEX_UNSUPPORTED, (Object[])new Object[]{"setStrokeWeight()"});
            return;
        }
        if (this.vertices == null || index >= this.vertices.length) {
            PGraphics.showWarning((String)("%1$s vertex index does not exist (" + index + ")"), (Object[])new Object[]{"setStrokeWeight()"});
            return;
        }
        this.vertices[index][17] = weight;
    }

    public void setStrokeJoin(int join) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setStrokeJoin()"});
            return;
        }
        this.strokeJoin = join;
    }

    public void setStrokeCap(int cap) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setStrokeCap()"});
            return;
        }
        this.strokeCap = cap;
    }

    public int getAmbient(int index) {
        if (this.vertices == null || index >= this.vertices.length) {
            PGraphics.showWarning((String)("%1$s vertex index does not exist (" + index + ")"), (Object[])new Object[]{"getAmbient()"});
            return this.ambientColor;
        }
        int r = (int)(this.vertices[index][25] * 255.0f);
        int g = (int)(this.vertices[index][26] * 255.0f);
        int b = (int)(this.vertices[index][27] * 255.0f);
        return 0xFF000000 | r << 16 | g << 8 | b;
    }

    public void setAmbient(int ambient) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setAmbient()"});
            return;
        }
        this.ambientColor = ambient;
        if (this.vertices != null) {
            int i = 0;
            while (i < this.vertices.length) {
                this.setAmbient(i, ambient);
                ++i;
            }
        }
    }

    public void setAmbient(int index, int ambient) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setAmbient()"});
            return;
        }
        if (this.vertices == null || index >= this.vertices.length) {
            PGraphics.showWarning((String)("%1$s vertex index does not exist (" + index + ")"), (Object[])new Object[]{"setAmbient()"});
            return;
        }
        this.vertices[index][25] = (float)(ambient >> 16 & 0xFF) / 255.0f;
        this.vertices[index][26] = (float)(ambient >> 8 & 0xFF) / 255.0f;
        this.vertices[index][27] = (float)(ambient >> 0 & 0xFF) / 255.0f;
    }

    public int getSpecular(int index) {
        if (this.vertices == null || index >= this.vertices.length) {
            PGraphics.showWarning((String)("%1$s vertex index does not exist (" + index + ")"), (Object[])new Object[]{"getSpecular()"});
            return this.specularColor;
        }
        int r = (int)(this.vertices[index][28] * 255.0f);
        int g = (int)(this.vertices[index][29] * 255.0f);
        int b = (int)(this.vertices[index][30] * 255.0f);
        return 0xFF000000 | r << 16 | g << 8 | b;
    }

    public void setSpecular(int specular) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setSpecular()"});
            return;
        }
        this.specularColor = specular;
        if (this.vertices != null) {
            int i = 0;
            while (i < this.vertices.length) {
                this.setSpecular(i, specular);
                ++i;
            }
        }
    }

    public void setSpecular(int index, int specular) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setSpecular()"});
            return;
        }
        if (this.vertices == null || index >= this.vertices.length) {
            PGraphics.showWarning((String)("%1$s vertex index does not exist (" + index + ")"), (Object[])new Object[]{"setSpecular()"});
            return;
        }
        this.vertices[index][28] = (float)(specular >> 16 & 0xFF) / 255.0f;
        this.vertices[index][29] = (float)(specular >> 8 & 0xFF) / 255.0f;
        this.vertices[index][30] = (float)(specular >> 0 & 0xFF) / 255.0f;
    }

    public int getEmissive(int index) {
        if (this.vertices == null || index >= this.vertices.length) {
            PGraphics.showWarning((String)("%1$s vertex index does not exist (" + index + ")"), (Object[])new Object[]{"getEmissive()"});
            return this.emissiveColor;
        }
        int r = (int)(this.vertices[index][32] * 255.0f);
        int g = (int)(this.vertices[index][33] * 255.0f);
        int b = (int)(this.vertices[index][34] * 255.0f);
        return 0xFF000000 | r << 16 | g << 8 | b;
    }

    public void setEmissive(int emissive) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setEmissive()"});
            return;
        }
        this.emissiveColor = emissive;
        if (this.vertices != null) {
            int i = 0;
            while (i < this.vertices.length) {
                this.setEmissive(i, emissive);
                ++i;
            }
        }
    }

    public void setEmissive(int index, int emissive) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setEmissive()"});
            return;
        }
        if (this.vertices == null || index >= this.vertices.length) {
            PGraphics.showWarning((String)("%1$s vertex index does not exist (" + index + ")"), (Object[])new Object[]{"setEmissive()"});
            return;
        }
        this.vertices[index][32] = (float)(emissive >> 16 & 0xFF) / 255.0f;
        this.vertices[index][33] = (float)(emissive >> 8 & 0xFF) / 255.0f;
        this.vertices[index][34] = (float)(emissive >> 0 & 0xFF) / 255.0f;
    }

    public float getShininess(int index) {
        if (this.vertices == null || index >= this.vertices.length) {
            PGraphics.showWarning((String)("%1$s vertex index does not exist (" + index + ")"), (Object[])new Object[]{"getShininess()"});
            return this.shininess;
        }
        return this.vertices[index][31];
    }

    public void setShininess(float shine) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setShininess()"});
            return;
        }
        this.shininess = shine;
        if (this.vertices != null) {
            int i = 0;
            while (i < this.vertices.length) {
                this.setShininess(i, shine);
                ++i;
            }
        }
    }

    public void setShininess(int index, float shine) {
        if (this.openShape) {
            PGraphics.showWarning((String)INSIDE_BEGIN_END_ERROR, (Object[])new Object[]{"setShininess()"});
            return;
        }
        if (this.vertices == null || index >= this.vertices.length) {
            PGraphics.showWarning((String)("%1$s vertex index does not exist (" + index + ")"), (Object[])new Object[]{"setShininess()"});
            return;
        }
        this.vertices[index][31] = shine;
    }

    public int[] getVertexCodes() {
        if (this.vertexCodes == null) {
            return null;
        }
        if (this.vertexCodes.length != this.vertexCodeCount) {
            this.vertexCodes = PApplet.subset(this.vertexCodes, 0, this.vertexCodeCount);
        }
        return this.vertexCodes;
    }

    public int getVertexCodeCount() {
        return this.vertexCodeCount;
    }

    public int getVertexCode(int index) {
        return this.vertexCodes[index];
    }

    public boolean isClosed() {
        return this.close;
    }

    public boolean contains(float x, float y) {
        if (this.family == 102) {
            PMatrix inverseCoords = this.matrix.get();
            inverseCoords.invert();
            inverseCoords.invert();
            PVector p = new PVector();
            inverseCoords.mult(new PVector(x, y), p);
            boolean c = false;
            int i = 0;
            int j = this.vertexCount - 1;
            while (i < this.vertexCount) {
                if (this.vertices[i][1] > p.y != this.vertices[j][1] > p.y && p.x < (this.vertices[j][0] - this.vertices[i][0]) * (y - this.vertices[i][1]) / (this.vertices[j][1] - this.vertices[i][1]) + this.vertices[i][0]) {
                    c = !c;
                }
                j = i++;
            }
            return c;
        }
        if (this.family == 0) {
            int i = 0;
            while (i < this.childCount) {
                if (this.children[i].contains(x, y)) {
                    return true;
                }
                ++i;
            }
            return false;
        }
        throw new IllegalArgumentException("The contains() method is only implemented for paths.");
    }

    public void translate(float x, float y) {
        this.checkMatrix(2);
        this.matrix.translate(x, y);
    }

    public void translate(float x, float y, float z) {
        this.checkMatrix(3);
        this.matrix.translate(x, y, z);
    }

    public void rotateX(float angle) {
        this.rotate(angle, 1.0f, 0.0f, 0.0f);
    }

    public void rotateY(float angle) {
        this.rotate(angle, 0.0f, 1.0f, 0.0f);
    }

    public void rotateZ(float angle) {
        this.rotate(angle, 0.0f, 0.0f, 1.0f);
    }

    public void rotate(float angle) {
        this.checkMatrix(2);
        this.matrix.rotate(angle);
    }

    public void rotate(float angle, float v0, float v1, float v2) {
        this.checkMatrix(3);
        float norm2 = v0 * v0 + v1 * v1 + v2 * v2;
        if (Math.abs((float)(norm2 - 1.0f)) > 1.0E-4f) {
            float norm = PApplet.sqrt(norm2);
            v0 /= norm;
            v1 /= norm;
            v2 /= norm;
        }
        this.matrix.rotate(angle, v0, v1, v2);
    }

    public void scale(float s) {
        this.checkMatrix(2);
        this.matrix.scale(s);
    }

    public void scale(float x, float y) {
        this.checkMatrix(2);
        this.matrix.scale(x, y);
    }

    public void scale(float x, float y, float z) {
        this.checkMatrix(3);
        this.matrix.scale(x, y, z);
    }

    public void resetMatrix() {
        this.checkMatrix(2);
        this.matrix.reset();
    }

    public void applyMatrix(PMatrix source) {
        if (source instanceof PMatrix2D) {
            this.applyMatrix((PMatrix2D)source);
        } else if (source instanceof PMatrix3D) {
            this.applyMatrix((PMatrix3D)source);
        }
    }

    public void applyMatrix(PMatrix2D source) {
        this.applyMatrix(source.m00, source.m01, 0.0f, source.m02, source.m10, source.m11, 0.0f, source.m12, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void applyMatrix(float n00, float n01, float n02, float n10, float n11, float n12) {
        this.checkMatrix(2);
        this.matrix.apply(n00, n01, n02, n10, n11, n12);
    }

    public void applyMatrix(PMatrix3D source) {
        this.applyMatrix(source.m00, source.m01, source.m02, source.m03, source.m10, source.m11, source.m12, source.m13, source.m20, source.m21, source.m22, source.m23, source.m30, source.m31, source.m32, source.m33);
    }

    public void applyMatrix(float n00, float n01, float n02, float n03, float n10, float n11, float n12, float n13, float n20, float n21, float n22, float n23, float n30, float n31, float n32, float n33) {
        this.checkMatrix(3);
        this.matrix.apply(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22, n23, n30, n31, n32, n33);
    }

    protected void checkMatrix(int dimensions) {
        if (this.matrix == null) {
            this.matrix = dimensions == 2 ? new PMatrix2D() : new PMatrix3D();
        } else if (dimensions == 3 && this.matrix instanceof PMatrix2D) {
            this.matrix = new PMatrix3D(this.matrix);
        }
    }

    public void colorMode(int mode) {
        this.colorMode(mode, this.colorModeX, this.colorModeY, this.colorModeZ, this.colorModeA);
    }

    public void colorMode(int mode, float max) {
        this.colorMode(mode, max, max, max, max);
    }

    public void colorMode(int mode, float maxX, float maxY, float maxZ) {
        this.colorMode(mode, maxX, maxY, maxZ, this.colorModeA);
    }

    public void colorMode(int mode, float maxX, float maxY, float maxZ, float maxA) {
        this.colorMode = mode;
        this.colorModeX = maxX;
        this.colorModeY = maxY;
        this.colorModeZ = maxZ;
        this.colorModeA = maxA;
        this.colorModeScale = maxA != 1.0f || maxX != maxY || maxY != maxZ || maxZ != maxA;
        this.colorModeDefault = this.colorMode == 1 && this.colorModeA == 255.0f && this.colorModeX == 255.0f && this.colorModeY == 255.0f && this.colorModeZ == 255.0f;
    }

    protected void colorCalc(int rgb) {
        if ((rgb & 0xFF000000) == 0 && (float)rgb <= this.colorModeX) {
            this.colorCalc((float)rgb);
        } else {
            this.colorCalcARGB(rgb, this.colorModeA);
        }
    }

    protected void colorCalc(int rgb, float alpha) {
        if ((rgb & 0xFF000000) == 0 && (float)rgb <= this.colorModeX) {
            this.colorCalc((float)rgb, alpha);
        } else {
            this.colorCalcARGB(rgb, alpha);
        }
    }

    protected void colorCalc(float gray) {
        this.colorCalc(gray, this.colorModeA);
    }

    protected void colorCalc(float gray, float alpha) {
        if (gray > this.colorModeX) {
            gray = this.colorModeX;
        }
        if (alpha > this.colorModeA) {
            alpha = this.colorModeA;
        }
        if (gray < 0.0f) {
            gray = 0.0f;
        }
        if (alpha < 0.0f) {
            alpha = 0.0f;
        }
        this.calcG = this.calcR = this.colorModeScale ? gray / this.colorModeX : gray;
        this.calcB = this.calcR;
        this.calcA = this.colorModeScale ? alpha / this.colorModeA : alpha;
        this.calcRi = (int)(this.calcR * 255.0f);
        this.calcGi = (int)(this.calcG * 255.0f);
        this.calcBi = (int)(this.calcB * 255.0f);
        this.calcAi = (int)(this.calcA * 255.0f);
        this.calcColor = this.calcAi << 24 | this.calcRi << 16 | this.calcGi << 8 | this.calcBi;
        this.calcAlpha = this.calcAi != 255;
    }

    protected void colorCalc(float x, float y, float z) {
        this.colorCalc(x, y, z, this.colorModeA);
    }

    protected void colorCalc(float x, float y, float z, float a) {
        if (x > this.colorModeX) {
            x = this.colorModeX;
        }
        if (y > this.colorModeY) {
            y = this.colorModeY;
        }
        if (z > this.colorModeZ) {
            z = this.colorModeZ;
        }
        if (a > this.colorModeA) {
            a = this.colorModeA;
        }
        if (x < 0.0f) {
            x = 0.0f;
        }
        if (y < 0.0f) {
            y = 0.0f;
        }
        if (z < 0.0f) {
            z = 0.0f;
        }
        if (a < 0.0f) {
            a = 0.0f;
        }
        block0 : switch (this.colorMode) {
            case 1: {
                if (this.colorModeScale) {
                    this.calcR = x / this.colorModeX;
                    this.calcG = y / this.colorModeY;
                    this.calcB = z / this.colorModeZ;
                    this.calcA = a / this.colorModeA;
                    break;
                }
                this.calcR = x;
                this.calcG = y;
                this.calcB = z;
                this.calcA = a;
                break;
            }
            case 3: {
                x /= this.colorModeX;
                z /= this.colorModeZ;
                float f = this.calcA = this.colorModeScale ? a / this.colorModeA : a;
                if ((y /= this.colorModeY) == 0.0f) {
                    this.calcG = this.calcB = z;
                    this.calcR = this.calcB;
                    break;
                }
                float which = (x - (float)((int)x)) * 6.0f;
                float f2 = which - (float)((int)which);
                float p = z * (1.0f - y);
                float q = z * (1.0f - y * f2);
                float t = z * (1.0f - y * (1.0f - f2));
                switch ((int)which) {
                    case 0: {
                        this.calcR = z;
                        this.calcG = t;
                        this.calcB = p;
                        break block0;
                    }
                    case 1: {
                        this.calcR = q;
                        this.calcG = z;
                        this.calcB = p;
                        break block0;
                    }
                    case 2: {
                        this.calcR = p;
                        this.calcG = z;
                        this.calcB = t;
                        break block0;
                    }
                    case 3: {
                        this.calcR = p;
                        this.calcG = q;
                        this.calcB = z;
                        break block0;
                    }
                    case 4: {
                        this.calcR = t;
                        this.calcG = p;
                        this.calcB = z;
                        break block0;
                    }
                    case 5: {
                        this.calcR = z;
                        this.calcG = p;
                        this.calcB = q;
                    }
                }
            }
        }
        this.calcRi = (int)(255.0f * this.calcR);
        this.calcGi = (int)(255.0f * this.calcG);
        this.calcBi = (int)(255.0f * this.calcB);
        this.calcAi = (int)(255.0f * this.calcA);
        this.calcColor = this.calcAi << 24 | this.calcRi << 16 | this.calcGi << 8 | this.calcBi;
        this.calcAlpha = this.calcAi != 255;
    }

    protected void colorCalcARGB(int argb, float alpha) {
        if (alpha == this.colorModeA) {
            this.calcAi = argb >> 24 & 0xFF;
            this.calcColor = argb;
        } else {
            this.calcAi = (int)((float)(argb >> 24 & 0xFF) * (alpha / this.colorModeA));
            this.calcColor = this.calcAi << 24 | argb & 0xFFFFFF;
        }
        this.calcRi = argb >> 16 & 0xFF;
        this.calcGi = argb >> 8 & 0xFF;
        this.calcBi = argb & 0xFF;
        this.calcA = (float)this.calcAi / 255.0f;
        this.calcR = (float)this.calcRi / 255.0f;
        this.calcG = (float)this.calcGi / 255.0f;
        this.calcB = (float)this.calcBi / 255.0f;
        this.calcAlpha = this.calcAi != 255;
    }
}
