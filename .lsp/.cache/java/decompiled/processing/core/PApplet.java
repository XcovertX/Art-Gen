/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  japplemenubar.JAppleMenuBar
 *  java.awt.Desktop
 *  java.awt.DisplayMode
 *  java.awt.EventQueue
 *  java.awt.Frame
 *  java.awt.GraphicsDevice
 *  java.awt.GraphicsEnvironment
 *  java.awt.HeadlessException
 *  java.awt.Image
 *  java.awt.Toolkit
 *  java.awt.image.BufferedImage
 *  java.io.BufferedInputStream
 *  java.io.BufferedOutputStream
 *  java.io.BufferedReader
 *  java.io.ByteArrayOutputStream
 *  java.io.File
 *  java.io.FileInputStream
 *  java.io.FileNotFoundException
 *  java.io.FileOutputStream
 *  java.io.IOException
 *  java.io.InputStream
 *  java.io.InputStreamReader
 *  java.io.OutputStream
 *  java.io.OutputStreamWriter
 *  java.io.PrintWriter
 *  java.io.RandomAccessFile
 *  java.io.Reader
 *  java.io.StringReader
 *  java.io.UnsupportedEncodingException
 *  java.io.Writer
 *  java.lang.ArrayIndexOutOfBoundsException
 *  java.lang.Boolean
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.ClassLoader
 *  java.lang.ClassNotFoundException
 *  java.lang.Deprecated
 *  java.lang.Exception
 *  java.lang.Float
 *  java.lang.IllegalAccessException
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.InterruptedException
 *  java.lang.Long
 *  java.lang.Math
 *  java.lang.NoSuchMethodException
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.Process
 *  java.lang.Runnable
 *  java.lang.Runtime
 *  java.lang.RuntimeException
 *  java.lang.SecurityException
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.lang.Thread
 *  java.lang.Thread$UncaughtExceptionHandler
 *  java.lang.Throwable
 *  java.lang.UnsupportedOperationException
 *  java.lang.reflect.Array
 *  java.lang.reflect.Constructor
 *  java.lang.reflect.Field
 *  java.lang.reflect.InvocationTargetException
 *  java.lang.reflect.Method
 *  java.net.HttpURLConnection
 *  java.net.JarURLConnection
 *  java.net.MalformedURLException
 *  java.net.URI
 *  java.net.URISyntaxException
 *  java.net.URL
 *  java.net.URLConnection
 *  java.net.URLDecoder
 *  java.net.URLEncoder
 *  java.nio.charset.StandardCharsets
 *  java.text.NumberFormat
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.Calendar
 *  java.util.HashMap
 *  java.util.LinkedHashMap
 *  java.util.List
 *  java.util.Random
 *  java.util.StringTokenizer
 *  java.util.concurrent.BlockingQueue
 *  java.util.concurrent.ExecutorService
 *  java.util.concurrent.Executors
 *  java.util.concurrent.LinkedBlockingQueue
 *  java.util.concurrent.ThreadFactory
 *  java.util.regex.Matcher
 *  java.util.regex.Pattern
 *  java.util.zip.GZIPInputStream
 *  java.util.zip.GZIPOutputStream
 *  javax.imageio.ImageIO
 *  javax.swing.ImageIcon
 *  javax.swing.UIManager
 *  javax.swing.filechooser.FileSystemView
 *  javax.xml.parsers.ParserConfigurationException
 *  org.xml.sax.SAXException
 *  processing.core.PApplet$2
 *  processing.core.PApplet$3
 *  processing.core.PApplet$LineThread
 *  processing.core.PApplet$RegisteredMethods
 *  processing.core.PConstants
 *  processing.core.PFont
 *  processing.core.PGraphics
 *  processing.core.PImage
 *  processing.core.PMatrix
 *  processing.core.PMatrix2D
 *  processing.core.PMatrix3D
 *  processing.core.PShape
 *  processing.core.PStyle
 *  processing.core.PSurface
 *  processing.data.JSONArray
 *  processing.data.JSONObject
 *  processing.data.StringList
 *  processing.data.Table
 *  processing.data.XML
 *  processing.event.Event
 *  processing.event.KeyEvent
 *  processing.event.MouseEvent
 *  processing.event.TouchEvent
 *  processing.opengl.PGL
 *  processing.opengl.PShader
 */
package processing.core;

import japplemenubar.JAppleMenuBar;
import java.awt.Desktop;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PMatrix;
import processing.core.PMatrix2D;
import processing.core.PMatrix3D;
import processing.core.PShape;
import processing.core.PStyle;
import processing.core.PSurface;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.data.StringList;
import processing.data.Table;
import processing.data.XML;
import processing.event.Event;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import processing.event.TouchEvent;
import processing.opengl.PGL;
import processing.opengl.PShader;

public class PApplet
implements PConstants {
    public static final String javaVersionName;
    public static final int javaPlatform;
    @Deprecated
    public static final float javaVersion;
    public static int platform;
    public static boolean useNativeSelect;
    public PGraphics g;
    public int displayWidth;
    public int displayHeight;
    public PGraphics recorder;
    public String[] args;
    private String sketchPath;
    static final boolean DEBUG = false;
    public static final int DEFAULT_WIDTH = 100;
    public static final int DEFAULT_HEIGHT = 100;
    public int[] pixels;
    public int width = 100;
    public int height = 100;
    public int pixelWidth;
    public int pixelHeight;
    protected boolean keyRepeatEnabled = false;
    public int mouseX;
    public int mouseY;
    public int pmouseX;
    public int pmouseY;
    protected int dmouseX;
    protected int dmouseY;
    protected int emouseX;
    protected int emouseY;
    @Deprecated
    public boolean firstMouse = true;
    public int mouseButton;
    public boolean mousePressed;
    private boolean macosxLeftButtonWithCtrlPressed;
    @Deprecated
    public MouseEvent mouseEvent;
    public char key;
    public int keyCode;
    public boolean keyPressed;
    List<Long> pressedKeys = new ArrayList(6);
    @Deprecated
    public KeyEvent keyEvent;
    public boolean focused = false;
    long millisOffset = System.currentTimeMillis();
    public float frameRate = 60.0f;
    protected boolean looping = true;
    protected boolean redraw = true;
    public int frameCount;
    public volatile boolean finished;
    static Throwable uncaughtThrowable;
    protected boolean exitCalled;
    public static final String ARGS_EDITOR_LOCATION = "--editor-location";
    public static final String ARGS_EXTERNAL = "--external";
    public static final String ARGS_LOCATION = "--location";
    public static final String ARGS_DISPLAY = "--display";
    public static final String ARGS_WINDOW_COLOR = "--window-color";
    public static final String ARGS_PRESENT = "--present";
    public static final String ARGS_STOP_COLOR = "--stop-color";
    public static final String ARGS_HIDE_STOP = "--hide-stop";
    public static final String ARGS_SKETCH_FOLDER = "--sketch-path";
    public static final String ARGS_DENSITY = "--density";
    public static final String EXTERNAL_STOP = "__STOP__";
    public static final String EXTERNAL_MOVE = "__MOVE__";
    boolean external = false;
    static final String ERROR_MIN_MAX = "Cannot use min() or max() on an empty array.";
    protected PSurface surface;
    public Frame frame;
    boolean insideSettings;
    String renderer = "processing.awt.PGraphicsJava2D";
    int smooth = 1;
    boolean fullScreen;
    int display = -1;
    GraphicsDevice[] displayDevices;
    public int pixelDensity = 1;
    int suggestedDensity = -1;
    boolean present;
    String outputPath;
    OutputStream outputStream;
    int windowColor = -2236963;
    HashMap<String, RegisteredMethods> registerMap = new HashMap();
    private final Object registerLock = new Object[0];
    protected boolean insideDraw;
    protected long frameRateLastNanos = 0L;
    BlockingQueue<Event> eventQueue = new LinkedBlockingQueue();
    private final Object eventQueueDequeueLock = new Object[0];
    static String openLauncher;
    Random internalRandom;
    static final int PERLIN_YWRAPB = 4;
    static final int PERLIN_YWRAP = 16;
    static final int PERLIN_ZWRAPB = 8;
    static final int PERLIN_ZWRAP = 256;
    static final int PERLIN_SIZE = 4095;
    int perlin_octaves = 4;
    float perlin_amp_falloff = 0.5f;
    int perlin_TWOPI;
    int perlin_PI;
    float[] perlin_cosTable;
    float[] perlin;
    Random perlinRandom;
    protected String[] loadImageFormats;
    private static final String REQUEST_IMAGE_THREAD_PREFIX = "requestImage";
    ExecutorService requestImagePool;
    private static boolean lookAndFeelCheck;
    static File desktopFolder;
    protected static LinkedHashMap<String, Pattern> matchPatterns;
    private static NumberFormat int_nf;
    private static int int_nf_digits;
    private static boolean int_nf_commas;
    private static NumberFormat float_nf;
    private static int float_nf_left;
    private static int float_nf_right;
    private static boolean float_nf_commas;

    static {
        String version = javaVersionName = System.getProperty((String)"java.version");
        if (javaVersionName.startsWith("1.")) {
            version = version.substring(2);
            javaPlatform = PApplet.parseInt(version.substring(0, version.indexOf(46)));
        } else {
            javaPlatform = PApplet.parseInt(version.replaceAll("-.*", "").replaceAll("\\..*", ""));
        }
        javaVersion = 1.0f + (float)javaPlatform / 10.0f;
        String osname = System.getProperty((String)"os.name");
        platform = osname.indexOf("Mac") != -1 ? 2 : (osname.indexOf("Windows") != -1 ? 1 : (osname.equals((Object)"Linux") ? 3 : 0));
        useNativeSelect = true;
    }

    public PSurface getSurface() {
        return this.surface;
    }

    boolean insideSettings(String method, Object ... args) {
        if (this.insideSettings) {
            return true;
        }
        String url = "https://processing.org/reference/" + method + "_.html";
        if (!this.external) {
            StringList argList = new StringList(args);
            System.err.println("When not using the PDE, " + method + "() can only be used inside settings().");
            System.err.println("Remove the " + method + "() method from setup(), and add the following:");
            System.err.println("public void settings() {");
            System.err.println("  " + method + "(" + argList.join(", ") + ");");
            System.err.println("}");
        }
        throw new IllegalStateException(String.valueOf((Object)method) + "() cannot be used here, see " + url);
    }

    void handleSettings() {
        this.insideSettings = true;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = ge.getDefaultScreenDevice();
        this.displayDevices = ge.getScreenDevices();
        if (this.display > 0 && this.display <= this.displayDevices.length) {
            device = this.displayDevices[this.display - 1];
        }
        DisplayMode displayMode = device.getDisplayMode();
        this.displayWidth = displayMode.getWidth();
        this.displayHeight = displayMode.getHeight();
        this.settings();
        if (this.display == 0 && platform == 2) {
            Process p = PApplet.exec("defaults", "read", "com.apple.spaces", "spans-displays");
            BufferedReader outReader = PApplet.createReader(p.getInputStream());
            BufferedReader errReader = PApplet.createReader(p.getErrorStream());
            StringBuilder stdout = new StringBuilder();
            StringBuilder stderr = new StringBuilder();
            String line = null;
            try {
                while ((line = outReader.readLine()) != null) {
                    stdout.append(line);
                }
                while ((line = errReader.readLine()) != null) {
                    stderr.append(line);
                }
            }
            catch (IOException e) {
                this.printStackTrace(e);
            }
            int resultCode = -1;
            try {
                resultCode = p.waitFor();
            }
            catch (InterruptedException interruptedException) {}
            String result = PApplet.trim(stdout.toString());
            if ("0".equals((Object)result)) {
                EventQueue.invokeLater((Runnable)new /* Unavailable Anonymous Inner Class!! */);
            } else if (!"1".equals((Object)result)) {
                System.err.println("Could not check the status of \u201cDisplays have separate spaces.\u201d");
                System.err.format("Received message '%s' and result code %d.%n", new Object[]{PApplet.trim(stderr.toString()), resultCode});
            }
        }
        this.insideSettings = false;
    }

    public void settings() {
    }

    public final int sketchWidth() {
        return this.width;
    }

    public final int sketchHeight() {
        return this.height;
    }

    public final String sketchRenderer() {
        return this.renderer;
    }

    public final int sketchSmooth() {
        return this.smooth;
    }

    public final boolean sketchFullScreen() {
        return this.fullScreen;
    }

    public final int sketchDisplay() {
        return this.display;
    }

    public final String sketchOutputPath() {
        return this.outputPath;
    }

    public final OutputStream sketchOutputStream() {
        return this.outputStream;
    }

    public final int sketchWindowColor() {
        return this.windowColor;
    }

    public final int sketchPixelDensity() {
        return this.pixelDensity;
    }

    public int displayDensity() {
        if (this.display != 0 && (this.fullScreen || this.present)) {
            return this.displayDensity(this.display);
        }
        int i = 0;
        while (i < this.displayDevices.length) {
            if (this.displayDensity(i + 1) == 2) {
                return 2;
            }
            ++i;
        }
        return 1;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int displayDensity(int display) {
        if (platform == 2) {
            GraphicsDevice device;
            String javaVendor = System.getProperty((String)"java.vendor");
            if (!javaVendor.contains((CharSequence)"Oracle")) return 1;
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            if (display == -1) {
                device = env.getDefaultScreenDevice();
            } else {
                if (display == 0) {
                    throw new RuntimeException("displayDensity() only works with specific display numbers");
                }
                GraphicsDevice[] devices = env.getScreenDevices();
                if (display > 0 && display <= devices.length) {
                    device = devices[display - 1];
                } else {
                    if (devices.length == 1) {
                        System.err.println("Only one display is currently known, use displayDensity(1).");
                        throw new RuntimeException("Display " + display + " does not exist.");
                    } else {
                        System.err.format("Your displays are numbered %d through %d, pass one of those numbers to displayDensity()%n", new Object[]{1, devices.length});
                    }
                    throw new RuntimeException("Display " + display + " does not exist.");
                }
            }
            try {
                Field field = device.getClass().getDeclaredField("scale");
                if (field == null) return 1;
                field.setAccessible(true);
                Object scale = field.get((Object)device);
                if (!(scale instanceof Integer) || (Integer)scale != 2) return 1;
                return 2;
            }
            catch (Exception exception) {
                return 1;
            }
        } else {
            if (platform != 1 && platform != 3) return 1;
            if (this.suggestedDensity == -1) {
                return 1;
            }
            if (this.suggestedDensity != 1 && this.suggestedDensity != 2) return 1;
            return this.suggestedDensity;
        }
    }

    public void pixelDensity(int density) {
        if (density != this.pixelDensity) {
            if (this.insideSettings("pixelDensity", density)) {
                if (density != 1 && density != 2) {
                    throw new RuntimeException("pixelDensity() can only be 1 or 2");
                }
                if (!"processing.javafx.PGraphicsFX2D".equals((Object)this.renderer) && density == 2 && this.displayDensity() == 1) {
                    System.err.println("pixelDensity(2) is not available for this display");
                    this.pixelDensity = 1;
                } else {
                    this.pixelDensity = density;
                }
            } else {
                System.err.println("not inside settings");
                throw new RuntimeException("pixelDensity() can only be used inside settings()");
            }
        }
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixelWidth = width * this.pixelDensity;
        this.pixelHeight = height * this.pixelDensity;
    }

    public void smooth() {
        this.smooth(1);
    }

    public void smooth(int level) {
        if (this.insideSettings) {
            this.smooth = level;
        } else if (this.smooth != level) {
            this.smoothWarning("smooth");
        }
    }

    public void noSmooth() {
        if (this.insideSettings) {
            this.smooth = 0;
        } else if (this.smooth != 0) {
            this.smoothWarning("noSmooth");
        }
    }

    private void smoothWarning(String method) {
        String where = this.external ? "setup" : "settings";
        PGraphics.showWarning((String)"%s() can only be used inside %s()", (Object[])new Object[]{method, where});
        if (this.external) {
            PGraphics.showWarning((String)"When run from the PDE, %s() is automatically moved from setup() to settings()", (Object[])new Object[]{method});
        }
    }

    public PGraphics getGraphics() {
        return this.g;
    }

    public void orientation(int which) {
    }

    public void start() {
        this.resume();
        this.handleMethods("resume");
        this.surface.resumeThread();
    }

    public void stop() {
        this.pause();
        this.handleMethods("pause");
        this.surface.pauseThread();
    }

    public void pause() {
    }

    public void resume() {
    }

    public void registerMethod(String methodName, Object target) {
        if (methodName.equals((Object)"mouseEvent")) {
            this.registerWithArgs("mouseEvent", target, new Class[]{MouseEvent.class});
        } else if (methodName.equals((Object)"keyEvent")) {
            this.registerWithArgs("keyEvent", target, new Class[]{KeyEvent.class});
        } else if (methodName.equals((Object)"touchEvent")) {
            this.registerWithArgs("touchEvent", target, new Class[]{TouchEvent.class});
        } else {
            this.registerNoArgs(methodName, target);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void registerNoArgs(String name, Object o) {
        Class c = o.getClass();
        try {
            Method method = c.getMethod(name, new Class[0]);
            Object object = this.registerLock;
            synchronized (object) {
                RegisteredMethods meth = (RegisteredMethods)this.registerMap.get((Object)name);
                if (meth == null) {
                    meth = new RegisteredMethods(this);
                    this.registerMap.put((Object)name, (Object)meth);
                }
                meth.add(o, method);
            }
        }
        catch (NoSuchMethodException noSuchMethodException) {
            this.die("There is no public " + name + "() method in the class " + o.getClass().getName());
        }
        catch (Exception e) {
            this.die("Could not register " + name + " + () for " + o, e);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void registerWithArgs(String name, Object o, Class<?>[] cargs) {
        Class c = o.getClass();
        try {
            Method method = c.getMethod(name, cargs);
            Object object = this.registerLock;
            synchronized (object) {
                RegisteredMethods meth = (RegisteredMethods)this.registerMap.get((Object)name);
                if (meth == null) {
                    meth = new RegisteredMethods(this);
                    this.registerMap.put((Object)name, (Object)meth);
                }
                meth.add(o, method);
            }
        }
        catch (NoSuchMethodException noSuchMethodException) {
            this.die("There is no public " + name + "() method in the class " + o.getClass().getName());
        }
        catch (Exception e) {
            this.die("Could not register " + name + " + () for " + o, e);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void unregisterMethod(String name, Object target) {
        Object object = this.registerLock;
        synchronized (object) {
            RegisteredMethods meth = (RegisteredMethods)this.registerMap.get((Object)name);
            if (meth == null) {
                this.die("No registered methods with the name " + name + "() were found.");
            }
            try {
                meth.remove(target);
            }
            catch (Exception e) {
                this.die("Could not unregister " + name + "() for " + target, e);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void handleMethods(String methodName) {
        Object object = this.registerLock;
        synchronized (object) {
            RegisteredMethods meth = (RegisteredMethods)this.registerMap.get((Object)methodName);
            if (meth != null) {
                meth.handle();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void handleMethods(String methodName, Object[] args) {
        Object object = this.registerLock;
        synchronized (object) {
            RegisteredMethods meth = (RegisteredMethods)this.registerMap.get((Object)methodName);
            if (meth != null) {
                meth.handle(args);
            }
        }
    }

    public void setup() {
    }

    public void draw() {
        this.finished = true;
    }

    public void fullScreen() {
        if (!this.fullScreen && this.insideSettings("fullScreen", new Object[0])) {
            this.fullScreen = true;
        }
    }

    public void fullScreen(int display) {
        if ((!this.fullScreen || display != this.display) && this.insideSettings("fullScreen", display)) {
            this.fullScreen = true;
            this.display = display;
        }
    }

    public void fullScreen(String renderer) {
        if (!(this.fullScreen && renderer.equals((Object)this.renderer) || !this.insideSettings("fullScreen", renderer))) {
            this.fullScreen = true;
            this.renderer = renderer;
        }
    }

    public void fullScreen(String renderer, int display) {
        if (!(this.fullScreen && renderer.equals((Object)this.renderer) && display == this.display || !this.insideSettings("fullScreen", renderer, display))) {
            this.fullScreen = true;
            this.renderer = renderer;
            this.display = display;
        }
    }

    public void size(int width, int height) {
        if ((width != this.width || height != this.height) && this.insideSettings("size", width, height)) {
            this.width = width;
            this.height = height;
        }
    }

    public void size(int width, int height, String renderer) {
        if ((width != this.width || height != this.height || !renderer.equals((Object)this.renderer)) && this.insideSettings("size", width, height, "\"" + renderer + "\"")) {
            this.width = width;
            this.height = height;
            this.renderer = renderer;
        }
    }

    public void size(int width, int height, String renderer, String path) {
        if ((width != this.width || height != this.height || !renderer.equals((Object)this.renderer)) && this.insideSettings("size", width, height, "\"" + renderer + "\"", "\"" + path + "\"")) {
            this.width = width;
            this.height = height;
            this.renderer = renderer;
            this.outputPath = path;
        }
    }

    public PGraphics createGraphics(int w, int h) {
        return this.createGraphics(w, h, "processing.awt.PGraphicsJava2D");
    }

    public PGraphics createGraphics(int w, int h, String renderer) {
        return this.createGraphics(w, h, renderer, null);
    }

    public PGraphics createGraphics(int w, int h, String renderer, String path) {
        return this.makeGraphics(w, h, renderer, path, false);
    }

    protected PGraphics makeGraphics(int w, int h, String renderer, String path, boolean primary) {
        if (!primary && !this.g.isGL()) {
            if (renderer.equals((Object)"processing.opengl.PGraphics2D")) {
                throw new RuntimeException("createGraphics() with P2D requires size() to use P2D or P3D");
            }
            if (renderer.equals((Object)"processing.opengl.PGraphics3D")) {
                throw new RuntimeException("createGraphics() with P3D or OPENGL requires size() to use P2D or P3D");
            }
        }
        try {
            Class rendererClass = Thread.currentThread().getContextClassLoader().loadClass(renderer);
            Constructor constructor = rendererClass.getConstructor(new Class[0]);
            PGraphics pg = (PGraphics)constructor.newInstance(new Object[0]);
            pg.setParent(this);
            pg.setPrimary(primary);
            if (path != null) {
                pg.setPath(this.savePath(path));
            }
            pg.setSize(w, h);
            return pg;
        }
        catch (InvocationTargetException ite) {
            String msg = ite.getTargetException().getMessage();
            if (msg != null && msg.indexOf("no jogl in java.library.path") != -1) {
                throw new RuntimeException("The jogl library folder needs to be specified with -Djava.library.path=/path/to/jogl");
            }
            this.printStackTrace(ite.getTargetException());
            Throwable target = ite.getTargetException();
            throw new RuntimeException(target.getMessage());
        }
        catch (ClassNotFoundException classNotFoundException) {
            if (this.external) {
                throw new RuntimeException("You need to use \"Import Library\" to add " + renderer + " to your sketch.");
            }
            throw new RuntimeException("The " + renderer + " renderer is not in the class path.");
        }
        catch (Exception e) {
            if (e instanceof IllegalArgumentException || e instanceof NoSuchMethodException || e instanceof IllegalAccessException) {
                if (e.getMessage().contains((CharSequence)"cannot be <= 0")) {
                    throw new RuntimeException((Throwable)e);
                }
                this.printStackTrace(e);
                String msg = String.valueOf((Object)renderer) + " needs to be updated " + "for the current release of Processing.";
                throw new RuntimeException(msg);
            }
            this.printStackTrace(e);
            throw new RuntimeException(e.getMessage());
        }
    }

    protected PGraphics createPrimaryGraphics() {
        return this.makeGraphics(this.sketchWidth(), this.sketchHeight(), this.sketchRenderer(), this.sketchOutputPath(), true);
    }

    public PImage createImage(int w, int h, int format) {
        PImage image = new PImage(w, h, format);
        image.parent = this;
        return image;
    }

    public void handleDraw() {
        if (this.g == null) {
            return;
        }
        if (!this.looping && !this.redraw) {
            return;
        }
        if (this.insideDraw) {
            System.err.println("handleDraw() called before finishing");
            System.exit((int)1);
        }
        this.insideDraw = true;
        this.g.beginDraw();
        if (this.recorder != null) {
            this.recorder.beginDraw();
        }
        long now = System.nanoTime();
        if (this.frameCount == 0) {
            this.setup();
        } else {
            double frameTimeSecs = (double)(now - this.frameRateLastNanos) / 1.0E9;
            double avgFrameTimeSecs = 1.0 / (double)this.frameRate;
            avgFrameTimeSecs = 0.95 * avgFrameTimeSecs + 0.05 * frameTimeSecs;
            this.frameRate = (float)(1.0 / avgFrameTimeSecs);
            if (this.frameCount != 0) {
                this.handleMethods("pre");
            }
            this.pmouseX = this.dmouseX;
            this.pmouseY = this.dmouseY;
            this.draw();
            this.dmouseX = this.mouseX;
            this.dmouseY = this.mouseY;
            this.dequeueEvents();
            this.handleMethods("draw");
            this.redraw = false;
        }
        this.g.endDraw();
        if (this.recorder != null) {
            this.recorder.endDraw();
        }
        this.insideDraw = false;
        if (this.frameCount != 0) {
            this.handleMethods("post");
        }
        this.frameRateLastNanos = now;
        ++this.frameCount;
    }

    public synchronized void redraw() {
        if (!this.looping) {
            this.redraw = true;
        }
    }

    public synchronized void loop() {
        if (!this.looping) {
            this.looping = true;
        }
    }

    public synchronized void noLoop() {
        if (this.looping) {
            this.looping = false;
        }
    }

    public boolean isLooping() {
        return this.looping;
    }

    public void postEvent(Event pe) {
        this.eventQueue.add((Object)pe);
        if (!this.looping) {
            this.dequeueEvents();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void dequeueEvents() {
        Object object = this.eventQueueDequeueLock;
        synchronized (object) {
            while (!this.eventQueue.isEmpty()) {
                Event e = (Event)this.eventQueue.remove();
                switch (e.getFlavor()) {
                    case 2: {
                        this.handleMouseEvent((MouseEvent)e);
                        break;
                    }
                    case 1: {
                        this.handleKeyEvent((KeyEvent)e);
                    }
                }
            }
        }
    }

    protected void handleMouseEvent(MouseEvent event) {
        int action = event.getAction();
        if (action == 4 || action == 5 || action == 1) {
            this.pmouseX = this.emouseX;
            this.pmouseY = this.emouseY;
            this.mouseX = event.getX();
            this.mouseY = event.getY();
        }
        int button = event.getButton();
        if (platform == 2 && event.getButton() == 37) {
            if (action == 1 && event.isControlDown()) {
                this.macosxLeftButtonWithCtrlPressed = true;
            }
            if (this.macosxLeftButtonWithCtrlPressed) {
                button = 39;
                event = new MouseEvent(event.getNative(), event.getMillis(), event.getAction(), event.getModifiers(), event.getX(), event.getY(), button, event.getCount());
            }
            if (action == 2) {
                this.macosxLeftButtonWithCtrlPressed = false;
            }
        }
        this.mouseButton = button;
        if (this.firstMouse) {
            this.pmouseX = this.mouseX;
            this.pmouseY = this.mouseY;
            this.dmouseX = this.mouseX;
            this.dmouseY = this.mouseY;
            this.firstMouse = false;
        }
        this.mouseEvent = event;
        switch (action) {
            case 1: {
                this.mousePressed = true;
                break;
            }
            case 2: {
                this.mousePressed = false;
            }
        }
        this.handleMethods("mouseEvent", new Object[]{event});
        switch (action) {
            case 1: {
                this.mousePressed(event);
                break;
            }
            case 2: {
                this.mouseReleased(event);
                break;
            }
            case 3: {
                this.mouseClicked(event);
                break;
            }
            case 4: {
                this.mouseDragged(event);
                break;
            }
            case 5: {
                this.mouseMoved(event);
                break;
            }
            case 6: {
                this.mouseEntered(event);
                break;
            }
            case 7: {
                this.mouseExited(event);
                break;
            }
            case 8: {
                this.mouseWheel(event);
            }
        }
        if (action == 4 || action == 5) {
            this.emouseX = this.mouseX;
            this.emouseY = this.mouseY;
        }
    }

    public void mousePressed() {
    }

    public void mousePressed(MouseEvent event) {
        this.mousePressed();
    }

    public void mouseReleased() {
    }

    public void mouseReleased(MouseEvent event) {
        this.mouseReleased();
    }

    public void mouseClicked() {
    }

    public void mouseClicked(MouseEvent event) {
        this.mouseClicked();
    }

    public void mouseDragged() {
    }

    public void mouseDragged(MouseEvent event) {
        this.mouseDragged();
    }

    public void mouseMoved() {
    }

    public void mouseMoved(MouseEvent event) {
        this.mouseMoved();
    }

    public void mouseEntered() {
    }

    public void mouseEntered(MouseEvent event) {
        this.mouseEntered();
    }

    public void mouseExited() {
    }

    public void mouseExited(MouseEvent event) {
        this.mouseExited();
    }

    public void mouseWheel() {
    }

    public void mouseWheel(MouseEvent event) {
        this.mouseWheel();
    }

    protected void handleKeyEvent(KeyEvent event) {
        if (!this.keyRepeatEnabled && event.isAutoRepeat()) {
            return;
        }
        this.keyEvent = event;
        this.key = event.getKey();
        this.keyCode = event.getKeyCode();
        switch (event.getAction()) {
            case 1: {
                Long hash = (long)this.keyCode << 16 | (long)this.key;
                if (!this.pressedKeys.contains((Object)hash)) {
                    this.pressedKeys.add((Object)hash);
                }
                this.keyPressed = true;
                this.keyPressed(this.keyEvent);
                break;
            }
            case 2: {
                this.pressedKeys.remove((Object)((long)this.keyCode << 16 | (long)this.key));
                this.keyPressed = !this.pressedKeys.isEmpty();
                this.keyReleased(this.keyEvent);
                break;
            }
            case 3: {
                this.keyTyped(this.keyEvent);
            }
        }
        this.handleMethods("keyEvent", new Object[]{event});
        if (event.getAction() == 1) {
            if (this.key == '\u001b') {
                this.exit();
            }
            if (this.external && event.getKeyCode() == 87 && (event.isMetaDown() && platform == 2 || event.isControlDown() && platform != 2)) {
                this.exit();
            }
        }
    }

    public void keyPressed() {
    }

    public void keyPressed(KeyEvent event) {
        this.keyPressed();
    }

    public void keyReleased() {
    }

    public void keyReleased(KeyEvent event) {
        this.keyReleased();
    }

    public void keyTyped() {
    }

    public void keyTyped(KeyEvent event) {
        this.keyTyped();
    }

    public void focusGained() {
    }

    public void focusLost() {
        this.pressedKeys.clear();
    }

    public int millis() {
        return (int)(System.currentTimeMillis() - this.millisOffset);
    }

    public static int second() {
        return Calendar.getInstance().get(13);
    }

    public static int minute() {
        return Calendar.getInstance().get(12);
    }

    public static int hour() {
        return Calendar.getInstance().get(11);
    }

    public static int day() {
        return Calendar.getInstance().get(5);
    }

    public static int month() {
        return Calendar.getInstance().get(2) + 1;
    }

    public static int year() {
        return Calendar.getInstance().get(1);
    }

    public void delay(int napTime) {
        try {
            Thread.sleep((long)napTime);
        }
        catch (InterruptedException interruptedException) {}
    }

    public void frameRate(float fps) {
        this.surface.setFrameRate(fps);
    }

    public void link(String url) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
            } else {
                PApplet.launch(url);
            }
        }
        catch (IOException e) {
            this.printStackTrace(e);
        }
        catch (URISyntaxException e) {
            this.printStackTrace(e);
        }
    }

    public static Process launch(String ... args) {
        String[] params = null;
        if (platform == 1) {
            params = new String[]{"cmd", "/c"};
        } else if (platform == 2) {
            params = new String[]{"open"};
        } else if (platform == 3) {
            String[] launchers;
            String[] stringArray = launchers = new String[]{"xdg-open", "gnome-open", "kde-open"};
            int n = launchers.length;
            int n2 = 0;
            while (n2 < n) {
                String launcher = stringArray[n2];
                if (openLauncher != null) break;
                try {
                    Process p = Runtime.getRuntime().exec(new String[]{launcher});
                    p.waitFor();
                    openLauncher = launcher;
                }
                catch (Exception exception) {}
                ++n2;
            }
            if (openLauncher == null) {
                System.err.println("Could not find xdg-open, gnome-open, or kde-open: the open() command may not work.");
            }
            if (openLauncher != null) {
                params = new String[]{openLauncher};
            }
        }
        if (params != null) {
            if (params[0].equals((Object)args[0])) {
                return PApplet.exec(args);
            }
            params = PApplet.concat(params, args);
            return PApplet.exec(params);
        }
        return PApplet.exec(args);
    }

    public static Process exec(String ... args) {
        try {
            return Runtime.getRuntime().exec(args);
        }
        catch (Exception e) {
            throw new RuntimeException("Exception while attempting " + PApplet.join(args, ' '), (Throwable)e);
        }
    }

    public static int exec(StringList stdout, StringList stderr, String ... args) {
        Process p = PApplet.exec(args);
        LineThread outThread = new LineThread(p.getInputStream(), stdout);
        LineThread errThread = new LineThread(p.getErrorStream(), stderr);
        try {
            int result = p.waitFor();
            outThread.join();
            errThread.join();
            return result;
        }
        catch (InterruptedException e) {
            throw new RuntimeException((Throwable)e);
        }
    }

    public static int shell(StringList stdout, StringList stderr, String ... args) {
        String runCmd;
        String shell;
        StringList argList = new StringList();
        if (platform == 1) {
            shell = System.getenv((String)"COMSPEC");
            runCmd = "/C";
        } else {
            shell = "/bin/sh";
            runCmd = "-c";
            argList.append("if [ -f /etc/profile ]; then . /etc/profile >/dev/null 2>&1; fi;");
            argList.append("if [ -f ~/.bash_profile ]; then . ~/.bash_profile >/dev/null 2>&1; elif [ -f ~/.bash_profile ]; then . ~/.bash_profile >/dev/null 2>&1; elif [ -f ~/.profile ]; then ~/.profile >/dev/null 2>&1; fi;");
        }
        String[] stringArray = args;
        int n = args.length;
        int n2 = 0;
        while (n2 < n) {
            String arg = stringArray[n2];
            argList.append(arg);
            ++n2;
        }
        return PApplet.exec(stdout, stderr, shell, runCmd, argList.join(" "));
    }

    protected void printStackTrace(Throwable t) {
        t.printStackTrace();
    }

    public void die(String what) {
        this.dispose();
        throw new RuntimeException(what);
    }

    public void die(String what, Exception e) {
        if (e != null) {
            e.printStackTrace();
        }
        this.die(what);
    }

    public void exit() {
        if (this.surface.isStopped()) {
            this.exitActual();
        } else if (this.looping) {
            this.finished = true;
            this.exitCalled = true;
        } else if (!this.looping) {
            this.dispose();
            this.exitActual();
        }
    }

    public boolean exitCalled() {
        return this.exitCalled;
    }

    public void exitActual() {
        try {
            System.exit((int)0);
        }
        catch (SecurityException securityException) {}
    }

    public void dispose() {
        this.finished = true;
        if (this.surface.stopThread()) {
            if (this.g != null) {
                this.g.dispose();
            }
            this.handleMethods("dispose");
        }
        if (platform == 2) {
            try {
                Class thinkDifferent = this.getClass().getClassLoader().loadClass("processing.core.ThinkDifferent");
                thinkDifferent.getMethod("cleanup", new Class[0]).invoke(null, new Object[0]);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void method(String name) {
        try {
            Method method = this.getClass().getMethod(name, new Class[0]);
            method.invoke((Object)this, new Object[0]);
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace();
        }
        catch (NoSuchMethodException noSuchMethodException) {
            System.err.println("There is no public " + name + "() method " + "in the class " + this.getClass().getName());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void thread(String name) {
        2 later = new /* Unavailable Anonymous Inner Class!! */;
        later.start();
    }

    public void save(String filename) {
        this.g.save(this.savePath(filename));
    }

    public void saveFrame() {
        try {
            this.g.save(this.savePath("screen-" + PApplet.nf(this.frameCount, 4) + ".tif"));
        }
        catch (SecurityException securityException) {
            System.err.println("Can't use saveFrame() when running in a browser, unless using a signed applet.");
        }
    }

    public void saveFrame(String filename) {
        try {
            this.g.save(this.savePath(this.insertFrame(filename)));
        }
        catch (SecurityException securityException) {
            System.err.println("Can't use saveFrame() when running in a browser, unless using a signed applet.");
        }
    }

    public String insertFrame(String what) {
        int first = what.indexOf(35);
        int last = what.lastIndexOf(35);
        if (first != -1 && last - first > 0) {
            String prefix = what.substring(0, first);
            int count = last - first + 1;
            String suffix = what.substring(last + 1);
            return String.valueOf((Object)prefix) + PApplet.nf(this.frameCount, count) + suffix;
        }
        return what;
    }

    public void cursor(int kind) {
        this.surface.setCursor(kind);
    }

    public void cursor(PImage img) {
        this.cursor(img, img.width / 2, img.height / 2);
    }

    public void cursor(PImage img, int x, int y) {
        this.surface.setCursor(img, x, y);
    }

    public void cursor() {
        this.surface.showCursor();
    }

    public void noCursor() {
        this.surface.hideCursor();
    }

    public static void print(byte what) {
        System.out.print((int)what);
        System.out.flush();
    }

    public static void print(boolean what) {
        System.out.print(what);
        System.out.flush();
    }

    public static void print(char what) {
        System.out.print(what);
        System.out.flush();
    }

    public static void print(int what) {
        System.out.print(what);
        System.out.flush();
    }

    public static void print(long what) {
        System.out.print(what);
        System.out.flush();
    }

    public static void print(float what) {
        System.out.print(what);
        System.out.flush();
    }

    public static void print(double what) {
        System.out.print(what);
        System.out.flush();
    }

    public static void print(String what) {
        System.out.print(what);
        System.out.flush();
    }

    public static void print(Object ... variables) {
        StringBuilder sb = new StringBuilder();
        Object[] objectArray = variables;
        int n = variables.length;
        int n2 = 0;
        while (n2 < n) {
            Object o = objectArray[n2];
            if (sb.length() != 0) {
                sb.append(" ");
            }
            if (o == null) {
                sb.append("null");
            } else {
                sb.append(o.toString());
            }
            ++n2;
        }
        System.out.print(sb.toString());
    }

    public static void println() {
        System.out.println();
    }

    public static void println(byte what) {
        System.out.println((int)what);
        System.out.flush();
    }

    public static void println(boolean what) {
        System.out.println(what);
        System.out.flush();
    }

    public static void println(char what) {
        System.out.println(what);
        System.out.flush();
    }

    public static void println(int what) {
        System.out.println(what);
        System.out.flush();
    }

    public static void println(long what) {
        System.out.println(what);
        System.out.flush();
    }

    public static void println(float what) {
        System.out.println(what);
        System.out.flush();
    }

    public static void println(double what) {
        System.out.println(what);
        System.out.flush();
    }

    public static void println(String what) {
        System.out.println(what);
        System.out.flush();
    }

    public static void println(Object ... variables) {
        PApplet.print(variables);
        PApplet.println();
    }

    public static void println(Object what) {
        if (what == null) {
            System.out.println("null");
        } else if (what.getClass().isArray()) {
            PApplet.printArray(what);
        } else {
            System.out.println(what.toString());
            System.out.flush();
        }
    }

    public static void printArray(Object what) {
        block22: {
            block23: {
                block21: {
                    if (what != null) break block21;
                    System.out.println("null");
                    break block22;
                }
                String name = what.getClass().getName();
                if (name.charAt(0) != '[') break block23;
                switch (name.charAt(1)) {
                    case '[': {
                        System.out.println(what);
                        break;
                    }
                    case 'L': {
                        Object[] poo = (Object[])what;
                        int i = 0;
                        while (i < poo.length) {
                            if (poo[i] instanceof String) {
                                System.out.println("[" + i + "] \"" + poo[i] + "\"");
                            } else {
                                System.out.println("[" + i + "] " + poo[i]);
                            }
                            ++i;
                        }
                        break block22;
                    }
                    case 'Z': {
                        boolean[] zz = (boolean[])what;
                        int i = 0;
                        while (i < zz.length) {
                            System.out.println("[" + i + "] " + zz[i]);
                            ++i;
                        }
                        break block22;
                    }
                    case 'B': {
                        byte[] bb = (byte[])what;
                        int i = 0;
                        while (i < bb.length) {
                            System.out.println("[" + i + "] " + bb[i]);
                            ++i;
                        }
                        break block22;
                    }
                    case 'C': {
                        char[] cc = (char[])what;
                        int i = 0;
                        while (i < cc.length) {
                            System.out.println("[" + i + "] '" + cc[i] + "'");
                            ++i;
                        }
                        break block22;
                    }
                    case 'I': {
                        int[] ii = (int[])what;
                        int i = 0;
                        while (i < ii.length) {
                            System.out.println("[" + i + "] " + ii[i]);
                            ++i;
                        }
                        break block22;
                    }
                    case 'J': {
                        long[] jj = (long[])what;
                        int i = 0;
                        while (i < jj.length) {
                            System.out.println("[" + i + "] " + jj[i]);
                            ++i;
                        }
                        break block22;
                    }
                    case 'F': {
                        float[] ff = (float[])what;
                        int i = 0;
                        while (i < ff.length) {
                            System.out.println("[" + i + "] " + ff[i]);
                            ++i;
                        }
                        break block22;
                    }
                    case 'D': {
                        double[] dd = (double[])what;
                        int i = 0;
                        while (i < dd.length) {
                            System.out.println("[" + i + "] " + dd[i]);
                            ++i;
                        }
                        break block22;
                    }
                    default: {
                        System.out.println(what);
                        break;
                    }
                }
                break block22;
            }
            System.out.println(what);
        }
        System.out.flush();
    }

    public static void debug(String msg) {
    }

    public static final float abs(float n) {
        return n < 0.0f ? -n : n;
    }

    public static final int abs(int n) {
        return n < 0 ? -n : n;
    }

    public static final float sq(float n) {
        return n * n;
    }

    public static final float sqrt(float n) {
        return (float)Math.sqrt((double)n);
    }

    public static final float log(float n) {
        return (float)Math.log((double)n);
    }

    public static final float exp(float n) {
        return (float)Math.exp((double)n);
    }

    public static final float pow(float n, float e) {
        return (float)Math.pow((double)n, (double)e);
    }

    public static final int max(int a, int b) {
        return a > b ? a : b;
    }

    public static final float max(float a, float b) {
        return a > b ? a : b;
    }

    public static final int max(int a, int b, int c) {
        return a > b ? (a > c ? a : c) : (b > c ? b : c);
    }

    public static final float max(float a, float b, float c) {
        return a > b ? (a > c ? a : c) : (b > c ? b : c);
    }

    public static final int max(int[] list) {
        if (list.length == 0) {
            throw new ArrayIndexOutOfBoundsException(ERROR_MIN_MAX);
        }
        int max = list[0];
        int i = 1;
        while (i < list.length) {
            if (list[i] > max) {
                max = list[i];
            }
            ++i;
        }
        return max;
    }

    public static final float max(float[] list) {
        if (list.length == 0) {
            throw new ArrayIndexOutOfBoundsException(ERROR_MIN_MAX);
        }
        float max = list[0];
        int i = 1;
        while (i < list.length) {
            if (list[i] > max) {
                max = list[i];
            }
            ++i;
        }
        return max;
    }

    public static final int min(int a, int b) {
        return a < b ? a : b;
    }

    public static final float min(float a, float b) {
        return a < b ? a : b;
    }

    public static final int min(int a, int b, int c) {
        return a < b ? (a < c ? a : c) : (b < c ? b : c);
    }

    public static final float min(float a, float b, float c) {
        return a < b ? (a < c ? a : c) : (b < c ? b : c);
    }

    public static final int min(int[] list) {
        if (list.length == 0) {
            throw new ArrayIndexOutOfBoundsException(ERROR_MIN_MAX);
        }
        int min = list[0];
        int i = 1;
        while (i < list.length) {
            if (list[i] < min) {
                min = list[i];
            }
            ++i;
        }
        return min;
    }

    public static final float min(float[] list) {
        if (list.length == 0) {
            throw new ArrayIndexOutOfBoundsException(ERROR_MIN_MAX);
        }
        float min = list[0];
        int i = 1;
        while (i < list.length) {
            if (list[i] < min) {
                min = list[i];
            }
            ++i;
        }
        return min;
    }

    public static final int constrain(int amt, int low, int high) {
        return amt < low ? low : (amt > high ? high : amt);
    }

    public static final float constrain(float amt, float low, float high) {
        return amt < low ? low : (amt > high ? high : amt);
    }

    public static final float sin(float angle) {
        return (float)Math.sin((double)angle);
    }

    public static final float cos(float angle) {
        return (float)Math.cos((double)angle);
    }

    public static final float tan(float angle) {
        return (float)Math.tan((double)angle);
    }

    public static final float asin(float value) {
        return (float)Math.asin((double)value);
    }

    public static final float acos(float value) {
        return (float)Math.acos((double)value);
    }

    public static final float atan(float value) {
        return (float)Math.atan((double)value);
    }

    public static final float atan2(float y, float x) {
        return (float)Math.atan2((double)y, (double)x);
    }

    public static final float degrees(float radians) {
        return radians * 57.295776f;
    }

    public static final float radians(float degrees) {
        return degrees * ((float)Math.PI / 180);
    }

    public static final int ceil(float n) {
        return (int)Math.ceil((double)n);
    }

    public static final int floor(float n) {
        return (int)Math.floor((double)n);
    }

    public static final int round(float n) {
        return Math.round((float)n);
    }

    public static final float mag(float a, float b) {
        return (float)Math.sqrt((double)(a * a + b * b));
    }

    public static final float mag(float a, float b, float c) {
        return (float)Math.sqrt((double)(a * a + b * b + c * c));
    }

    public static final float dist(float x1, float y1, float x2, float y2) {
        return PApplet.sqrt(PApplet.sq(x2 - x1) + PApplet.sq(y2 - y1));
    }

    public static final float dist(float x1, float y1, float z1, float x2, float y2, float z2) {
        return PApplet.sqrt(PApplet.sq(x2 - x1) + PApplet.sq(y2 - y1) + PApplet.sq(z2 - z1));
    }

    public static final float lerp(float start, float stop, float amt) {
        return start + (stop - start) * amt;
    }

    public static final float norm(float value, float start, float stop) {
        return (value - start) / (stop - start);
    }

    public static final float map(float value, float start1, float stop1, float start2, float stop2) {
        float outgoing = start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
        String badness = null;
        if (outgoing != outgoing) {
            badness = "NaN (not a number)";
        } else if (outgoing == Float.NEGATIVE_INFINITY || outgoing == Float.POSITIVE_INFINITY) {
            badness = "infinity";
        }
        if (badness != null) {
            String msg = String.format((String)"map(%s, %s, %s, %s, %s) called, which returns %s", (Object[])new Object[]{PApplet.nf(value), PApplet.nf(start1), PApplet.nf(stop1), PApplet.nf(start2), PApplet.nf(stop2), badness});
            PGraphics.showWarning((String)msg);
        }
        return outgoing;
    }

    public final float random(float high) {
        if (high == 0.0f || high != high) {
            return 0.0f;
        }
        if (this.internalRandom == null) {
            this.internalRandom = new Random();
        }
        float value = 0.0f;
        while ((value = this.internalRandom.nextFloat() * high) == high) {
        }
        return value;
    }

    public final float randomGaussian() {
        if (this.internalRandom == null) {
            this.internalRandom = new Random();
        }
        return (float)this.internalRandom.nextGaussian();
    }

    public final float random(float low, float high) {
        if (low >= high) {
            return low;
        }
        float diff = high - low;
        float value = 0.0f;
        while ((value = this.random(diff) + low) == high) {
        }
        return value;
    }

    public final void randomSeed(long seed) {
        if (this.internalRandom == null) {
            this.internalRandom = new Random();
        }
        this.internalRandom.setSeed(seed);
    }

    public float noise(float x) {
        return this.noise(x, 0.0f, 0.0f);
    }

    public float noise(float x, float y) {
        return this.noise(x, y, 0.0f);
    }

    public float noise(float x, float y, float z) {
        if (this.perlin == null) {
            if (this.perlinRandom == null) {
                this.perlinRandom = new Random();
            }
            this.perlin = new float[4096];
            int i = 0;
            while (i < 4096) {
                this.perlin[i] = this.perlinRandom.nextFloat();
                ++i;
            }
            this.perlin_cosTable = PGraphics.cosLUT;
            this.perlin_PI = 720;
            this.perlin_TWOPI = 720;
            this.perlin_PI >>= 1;
        }
        if (x < 0.0f) {
            x = -x;
        }
        if (y < 0.0f) {
            y = -y;
        }
        if (z < 0.0f) {
            z = -z;
        }
        int xi = (int)x;
        int yi = (int)y;
        int zi = (int)z;
        float xf = x - (float)xi;
        float yf = y - (float)yi;
        float zf = z - (float)zi;
        float r = 0.0f;
        float ampl = 0.5f;
        int i = 0;
        while (i < this.perlin_octaves) {
            int of = xi + (yi << 4) + (zi << 8);
            float rxf = this.noise_fsc(xf);
            float ryf = this.noise_fsc(yf);
            float n1 = this.perlin[of & 0xFFF];
            n1 += rxf * (this.perlin[of + 1 & 0xFFF] - n1);
            float n2 = this.perlin[of + 16 & 0xFFF];
            n2 += rxf * (this.perlin[of + 16 + 1 & 0xFFF] - n2);
            n1 += ryf * (n2 - n1);
            n2 = this.perlin[(of += 256) & 0xFFF];
            n2 += rxf * (this.perlin[of + 1 & 0xFFF] - n2);
            float n3 = this.perlin[of + 16 & 0xFFF];
            n3 += rxf * (this.perlin[of + 16 + 1 & 0xFFF] - n3);
            n2 += ryf * (n3 - n2);
            n1 += this.noise_fsc(zf) * (n2 - n1);
            r += n1 * ampl;
            ampl *= this.perlin_amp_falloff;
            xi <<= 1;
            xf *= 2.0f;
            yi <<= 1;
            yf *= 2.0f;
            zi <<= 1;
            zf *= 2.0f;
            if (xf >= 1.0f) {
                ++xi;
                xf -= 1.0f;
            }
            if (yf >= 1.0f) {
                ++yi;
                yf -= 1.0f;
            }
            if (zf >= 1.0f) {
                ++zi;
                zf -= 1.0f;
            }
            ++i;
        }
        return r;
    }

    private float noise_fsc(float i) {
        return 0.5f * (1.0f - this.perlin_cosTable[(int)(i * (float)this.perlin_PI) % this.perlin_TWOPI]);
    }

    public void noiseDetail(int lod) {
        if (lod > 0) {
            this.perlin_octaves = lod;
        }
    }

    public void noiseDetail(int lod, float falloff) {
        if (lod > 0) {
            this.perlin_octaves = lod;
        }
        if (falloff > 0.0f) {
            this.perlin_amp_falloff = falloff;
        }
    }

    public void noiseSeed(long seed) {
        if (this.perlinRandom == null) {
            this.perlinRandom = new Random();
        }
        this.perlinRandom.setSeed(seed);
        this.perlin = null;
    }

    public PImage loadImage(String filename) {
        return this.loadImage(filename, null);
    }

    public PImage loadImage(String filename, String extension) {
        block19: {
            Image awtImage;
            block21: {
                BufferedImage buffImage;
                int space;
                byte[] bytes;
                block20: {
                    if (this.g != null && !Thread.currentThread().getName().startsWith(REQUEST_IMAGE_THREAD_PREFIX)) {
                        this.g.awaitAsyncSaveCompletion(filename);
                    }
                    if (extension == null) {
                        String lower = filename.toLowerCase();
                        int dot = filename.lastIndexOf(46);
                        if (dot == -1) {
                            extension = "unknown";
                        } else {
                            extension = lower.substring(dot + 1);
                            int question = extension.indexOf(63);
                            if (question != -1) {
                                extension = extension.substring(0, question);
                            }
                        }
                    }
                    if ((extension = extension.toLowerCase()).equals((Object)"tga")) {
                        try {
                            PImage image = this.loadImageTGA(filename);
                            return image;
                        }
                        catch (IOException e) {
                            this.printStackTrace(e);
                            return null;
                        }
                    }
                    if (extension.equals((Object)"tif") || extension.equals((Object)"tiff")) {
                        bytes = this.loadBytes(filename);
                        PImage image = bytes == null ? null : PImage.loadTIFF((byte[])bytes);
                        return image;
                    }
                    if (!extension.equals((Object)"jpg") && !extension.equals((Object)"jpeg") && !extension.equals((Object)"gif") && !extension.equals((Object)"png") && !extension.equals((Object)"unknown")) break block19;
                    bytes = this.loadBytes(filename);
                    if (bytes != null) break block20;
                    return null;
                }
                awtImage = new ImageIcon(bytes).getImage();
                if (!(awtImage instanceof BufferedImage) || (space = (buffImage = (BufferedImage)awtImage).getColorModel().getColorSpace().getType()) != 9) break block21;
                System.err.println(String.valueOf((Object)filename) + " is a CMYK image, " + "only RGB images are supported.");
                return null;
            }
            try {
                PImage image = new PImage(awtImage);
                if (image.width == -1) {
                    System.err.println("The file " + filename + " contains bad image data, or may not be an image.");
                }
                if (extension.equals((Object)"gif") || extension.equals((Object)"png") || extension.equals((Object)"unknown")) {
                    image.checkAlpha();
                }
                image.parent = this;
                return image;
            }
            catch (Exception e) {
                this.printStackTrace(e);
            }
        }
        if (this.loadImageFormats == null) {
            this.loadImageFormats = ImageIO.getReaderFormatNames();
        }
        if (this.loadImageFormats != null) {
            int i = 0;
            while (i < this.loadImageFormats.length) {
                if (extension.equals((Object)this.loadImageFormats[i])) {
                    return this.loadImageIO(filename);
                }
                ++i;
            }
        }
        System.err.println("Could not find a method to load " + filename);
        return null;
    }

    public PImage requestImage(String filename) {
        return this.requestImage(filename, null);
    }

    public PImage requestImage(String filename, String extension) {
        if (this.g != null) {
            this.g.awaitAsyncSaveCompletion(filename);
        }
        PImage vessel = this.createImage(0, 0, 2);
        if (this.requestImagePool == null) {
            3 factory = new /* Unavailable Anonymous Inner Class!! */;
            this.requestImagePool = Executors.newFixedThreadPool((int)4, (ThreadFactory)factory);
        }
        this.requestImagePool.execute(() -> {
            PImage actual = this.loadImage(filename, extension);
            if (actual == null) {
                pImage.width = -1;
                pImage.height = -1;
            } else {
                pImage.width = actual.width;
                pImage.height = actual.height;
                pImage.format = actual.format;
                pImage.pixels = actual.pixels;
                pImage.pixelWidth = actual.width;
                pImage.pixelHeight = actual.height;
                pImage.pixelDensity = 1;
            }
        });
        return vessel;
    }

    protected PImage loadImageIO(String filename) {
        InputStream stream = this.createInput(filename);
        if (stream == null) {
            System.err.println("The image " + filename + " could not be found.");
            return null;
        }
        try {
            BufferedImage bi = ImageIO.read((InputStream)stream);
            PImage outgoing = new PImage(bi.getWidth(), bi.getHeight());
            outgoing.parent = this;
            bi.getRGB(0, 0, outgoing.width, outgoing.height, outgoing.pixels, 0, outgoing.width);
            outgoing.checkAlpha();
            stream.close();
            return outgoing;
        }
        catch (Exception e) {
            this.printStackTrace(e);
            return null;
        }
    }

    protected PImage loadImageTGA(String filename) throws IOException {
        PImage outgoing;
        InputStream is;
        block47: {
            boolean reversed;
            int h;
            int w;
            int format;
            block45: {
                block46: {
                    int count;
                    is = this.createInput(filename);
                    if (is == null) {
                        return null;
                    }
                    byte[] header = new byte[18];
                    int offset = 0;
                    do {
                        if ((count = is.read(header, offset, header.length - offset)) != -1) continue;
                        return null;
                    } while ((offset += count) < 18);
                    format = 0;
                    if (!(header[2] != 3 && header[2] != 11 || header[16] != 8 || header[17] != 8 && header[17] != 40)) {
                        format = 4;
                    } else if (!(header[2] != 2 && header[2] != 10 || header[16] != 24 || header[17] != 32 && header[17] != 0)) {
                        format = 1;
                    } else if (!(header[2] != 2 && header[2] != 10 || header[16] != 32 || header[17] != 8 && header[17] != 40)) {
                        format = 2;
                    }
                    if (format == 0) {
                        System.err.println("Unknown .tga file format for " + filename);
                        return null;
                    }
                    w = ((header[13] & 0xFF) << 8) + (header[12] & 0xFF);
                    h = ((header[15] & 0xFF) << 8) + (header[14] & 0xFF);
                    outgoing = this.createImage(w, h, format);
                    boolean bl = reversed = (header[17] & 0x20) == 0;
                    if (header[2] != 2 && header[2] != 3) break block45;
                    if (!reversed) break block46;
                    int index = (h - 1) * w;
                    switch (format) {
                        case 4: {
                            int y = h - 1;
                            while (y >= 0) {
                                int x = 0;
                                while (x < w) {
                                    outgoing.pixels[index + x] = is.read();
                                    ++x;
                                }
                                index -= w;
                                --y;
                            }
                            break block47;
                        }
                        case 1: {
                            int y = h - 1;
                            while (y >= 0) {
                                int x = 0;
                                while (x < w) {
                                    outgoing.pixels[index + x] = is.read() | is.read() << 8 | is.read() << 16 | 0xFF000000;
                                    ++x;
                                }
                                index -= w;
                                --y;
                            }
                            break block47;
                        }
                        case 2: {
                            int y = h - 1;
                            while (y >= 0) {
                                int x = 0;
                                while (x < w) {
                                    outgoing.pixels[index + x] = is.read() | is.read() << 8 | is.read() << 16 | is.read() << 24;
                                    ++x;
                                }
                                index -= w;
                                --y;
                            }
                            break block0;
                        }
                    }
                    break block47;
                }
                int count = w * h;
                switch (format) {
                    case 4: {
                        int i = 0;
                        while (i < count) {
                            outgoing.pixels[i] = is.read();
                            ++i;
                        }
                        break block47;
                    }
                    case 1: {
                        int i = 0;
                        while (i < count) {
                            outgoing.pixels[i] = is.read() | is.read() << 8 | is.read() << 16 | 0xFF000000;
                            ++i;
                        }
                        break block47;
                    }
                    case 2: {
                        int i = 0;
                        while (i < count) {
                            outgoing.pixels[i] = is.read() | is.read() << 8 | is.read() << 16 | is.read() << 24;
                            ++i;
                        }
                        break block5;
                    }
                }
                break block47;
            }
            int index = 0;
            int[] px = outgoing.pixels;
            block30: while (index < px.length) {
                boolean isRLE;
                int num = is.read();
                boolean bl = isRLE = (num & 0x80) != 0;
                if (isRLE) {
                    num -= 127;
                    int pixel = 0;
                    switch (format) {
                        case 4: {
                            pixel = is.read();
                            break;
                        }
                        case 1: {
                            pixel = 0xFF000000 | is.read() | is.read() << 8 | is.read() << 16;
                            break;
                        }
                        case 2: {
                            pixel = is.read() | is.read() << 8 | is.read() << 16 | is.read() << 24;
                        }
                    }
                    int i = 0;
                    while (i < num) {
                        px[index++] = pixel;
                        if (index == px.length) continue block30;
                        ++i;
                    }
                    continue;
                }
                ++num;
                switch (format) {
                    case 4: {
                        int i = 0;
                        while (i < num) {
                            px[index++] = is.read();
                            ++i;
                        }
                        continue block30;
                    }
                    case 1: {
                        int i = 0;
                        while (i < num) {
                            px[index++] = 0xFF000000 | is.read() | is.read() << 8 | is.read() << 16;
                            ++i;
                        }
                        continue block30;
                    }
                    case 2: {
                        int i = 0;
                        while (i < num) {
                            px[index++] = is.read() | is.read() << 8 | is.read() << 16 | is.read() << 24;
                            ++i;
                        }
                        continue block30;
                    }
                }
            }
            if (!reversed) {
                int[] temp = new int[w];
                int y = 0;
                while (y < h / 2) {
                    int z = h - 1 - y;
                    System.arraycopy((Object)px, (int)(y * w), (Object)temp, (int)0, (int)w);
                    System.arraycopy((Object)px, (int)(z * w), (Object)px, (int)(y * w), (int)w);
                    System.arraycopy((Object)temp, (int)0, (Object)px, (int)(z * w), (int)w);
                    ++y;
                }
            }
        }
        is.close();
        return outgoing;
    }

    public XML loadXML(String filename) {
        return this.loadXML(filename, null);
    }

    public XML loadXML(String filename, String options) {
        try {
            BufferedReader reader = this.createReader(filename);
            if (reader != null) {
                return new XML((Reader)reader, options);
            }
            return null;
        }
        catch (IOException e) {
            throw new RuntimeException((Throwable)e);
        }
        catch (ParserConfigurationException e) {
            throw new RuntimeException((Throwable)e);
        }
        catch (SAXException e) {
            throw new RuntimeException((Throwable)e);
        }
    }

    public XML parseXML(String xmlString) {
        return this.parseXML(xmlString, null);
    }

    public XML parseXML(String xmlString, String options) {
        try {
            return XML.parse((String)xmlString, (String)options);
        }
        catch (Exception e) {
            throw new RuntimeException((Throwable)e);
        }
    }

    public boolean saveXML(XML xml, String filename) {
        return this.saveXML(xml, filename, null);
    }

    public boolean saveXML(XML xml, String filename, String options) {
        return xml.save(this.saveFile(filename), options);
    }

    public JSONObject parseJSONObject(String input) {
        return new JSONObject((Reader)new StringReader(input));
    }

    public JSONObject loadJSONObject(String filename) {
        BufferedReader reader = this.createReader(filename);
        JSONObject outgoing = new JSONObject((Reader)reader);
        try {
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return outgoing;
    }

    public static JSONObject loadJSONObject(File file) {
        BufferedReader reader = PApplet.createReader(file);
        JSONObject outgoing = new JSONObject((Reader)reader);
        try {
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return outgoing;
    }

    public boolean saveJSONObject(JSONObject json, String filename) {
        return this.saveJSONObject(json, filename, null);
    }

    public boolean saveJSONObject(JSONObject json, String filename, String options) {
        return json.save(this.saveFile(filename), options);
    }

    public JSONArray parseJSONArray(String input) {
        return new JSONArray((Reader)new StringReader(input));
    }

    public JSONArray loadJSONArray(String filename) {
        BufferedReader reader = this.createReader(filename);
        JSONArray outgoing = new JSONArray((Reader)reader);
        try {
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return outgoing;
    }

    public static JSONArray loadJSONArray(File file) {
        BufferedReader reader = PApplet.createReader(file);
        JSONArray outgoing = new JSONArray((Reader)reader);
        try {
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return outgoing;
    }

    public boolean saveJSONArray(JSONArray json, String filename) {
        return this.saveJSONArray(json, filename, null);
    }

    public boolean saveJSONArray(JSONArray json, String filename, String options) {
        return json.save(this.saveFile(filename), options);
    }

    public Table loadTable(String filename) {
        return this.loadTable(filename, null);
    }

    public Table loadTable(String filename, String options) {
        InputStream input;
        String optionStr;
        block5: {
            try {
                optionStr = Table.extensionOptions((boolean)true, (String)filename, (String)options);
                String[] optionList = PApplet.trim(PApplet.split(optionStr, ','));
                Table dictionary = null;
                String[] stringArray = optionList;
                int n = optionList.length;
                int n2 = 0;
                while (n2 < n) {
                    String opt = stringArray[n2];
                    if (opt.startsWith("dictionary=")) {
                        dictionary = this.loadTable(opt.substring(opt.indexOf(61) + 1), "tsv");
                        return dictionary.typedParse(this.createInput(filename), optionStr);
                    }
                    ++n2;
                }
                input = this.createInput(filename);
                if (input != null) break block5;
                System.err.println(String.valueOf((Object)filename) + " does not exist or could not be read");
                return null;
            }
            catch (IOException e) {
                this.printStackTrace(e);
                return null;
            }
        }
        return new Table(input, optionStr);
    }

    public boolean saveTable(Table table, String filename) {
        return this.saveTable(table, filename, null);
    }

    public boolean saveTable(Table table, String filename, String options) {
        try {
            File outputFile = this.saveFile(filename);
            return table.save(outputFile, options);
        }
        catch (IOException e) {
            this.printStackTrace(e);
            return false;
        }
    }

    public PFont loadFont(String filename) {
        if (!filename.toLowerCase().endsWith(".vlw")) {
            throw new IllegalArgumentException("loadFont() is for .vlw files, try createFont()");
        }
        try {
            InputStream input = this.createInput(filename);
            return new PFont(input);
        }
        catch (Exception e) {
            this.die("Could not load font " + filename + ". " + "Make sure that the font has been copied " + "to the data folder of your sketch.", e);
            return null;
        }
    }

    public PFont createFont(String name, float size) {
        return this.createFont(name, size, true, null);
    }

    public PFont createFont(String name, float size, boolean smooth) {
        return this.createFont(name, size, smooth, null);
    }

    public PFont createFont(String name, float size, boolean smooth, char[] charset) {
        if (this.g == null) {
            throw new RuntimeException("createFont() can only be used inside setup() or after setup() has been called.");
        }
        return this.g.createFont(name, size, smooth, charset);
    }

    private static void checkLookAndFeel() {
        if (!lookAndFeelCheck) {
            if (platform == 1) {
                try {
                    UIManager.setLookAndFeel((String)UIManager.getSystemLookAndFeelClassName());
                }
                catch (Exception exception) {}
            }
            lookAndFeelCheck = true;
        }
    }

    public void selectInput(String prompt, String callback) {
        this.selectInput(prompt, callback, null);
    }

    public void selectInput(String prompt, String callback, File file) {
        this.selectInput(prompt, callback, file, this);
    }

    public void selectInput(String prompt, String callback, File file, Object callbackObject) {
        PApplet.selectInput(prompt, callback, file, callbackObject, null, this);
    }

    public static void selectInput(String prompt, String callbackMethod, File file, Object callbackObject, Frame parent, PApplet sketch) {
        PApplet.selectImpl(prompt, callbackMethod, file, callbackObject, parent, 0, sketch);
    }

    public static void selectInput(String prompt, String callbackMethod, File file, Object callbackObject, Frame parent) {
        PApplet.selectImpl(prompt, callbackMethod, file, callbackObject, parent, 0, null);
    }

    public void selectOutput(String prompt, String callback) {
        this.selectOutput(prompt, callback, null);
    }

    public void selectOutput(String prompt, String callback, File file) {
        this.selectOutput(prompt, callback, file, this);
    }

    public void selectOutput(String prompt, String callback, File file, Object callbackObject) {
        PApplet.selectOutput(prompt, callback, file, callbackObject, null, this);
    }

    public static void selectOutput(String prompt, String callbackMethod, File file, Object callbackObject, Frame parent) {
        PApplet.selectImpl(prompt, callbackMethod, file, callbackObject, parent, 1, null);
    }

    public static void selectOutput(String prompt, String callbackMethod, File file, Object callbackObject, Frame parent, PApplet sketch) {
        PApplet.selectImpl(prompt, callbackMethod, file, callbackObject, parent, 1, sketch);
    }

    protected static void selectImpl(String prompt, String callbackMethod, File defaultSelection, Object callbackObject, Frame parentFrame, int mode, PApplet sketch) {
        EventQueue.invokeLater((Runnable)new /* Unavailable Anonymous Inner Class!! */);
    }

    public void selectFolder(String prompt, String callback) {
        this.selectFolder(prompt, callback, null);
    }

    public void selectFolder(String prompt, String callback, File file) {
        this.selectFolder(prompt, callback, file, this);
    }

    public void selectFolder(String prompt, String callback, File file, Object callbackObject) {
        PApplet.selectFolder(prompt, callback, file, callbackObject, null, this);
    }

    public static void selectFolder(String prompt, String callbackMethod, File defaultSelection, Object callbackObject, Frame parentFrame) {
        PApplet.selectFolder(prompt, callbackMethod, defaultSelection, callbackObject, parentFrame, null);
    }

    public static void selectFolder(String prompt, String callbackMethod, File defaultSelection, Object callbackObject, Frame parentFrame, PApplet sketch) {
        EventQueue.invokeLater((Runnable)new /* Unavailable Anonymous Inner Class!! */);
    }

    private static void selectCallback(File selectedFile, String callbackMethod, Object callbackObject) {
        try {
            Class callbackClass = callbackObject.getClass();
            Method selectMethod = callbackClass.getMethod(callbackMethod, new Class[]{File.class});
            selectMethod.invoke(callbackObject, new Object[]{selectedFile});
        }
        catch (IllegalAccessException illegalAccessException) {
            System.err.println(String.valueOf((Object)callbackMethod) + "() must be public");
        }
        catch (InvocationTargetException ite) {
            ite.printStackTrace();
        }
        catch (NoSuchMethodException noSuchMethodException) {
            System.err.println(String.valueOf((Object)callbackMethod) + "() could not be found");
        }
    }

    public String[] listPaths(String path, String ... options) {
        File[] list = this.listFiles(path, options);
        int offset = 0;
        String[] stringArray = options;
        int n = options.length;
        int n2 = 0;
        while (n2 < n) {
            String opt = stringArray[n2];
            if (opt.equals((Object)"relative")) {
                if (!path.endsWith(File.pathSeparator)) {
                    path = String.valueOf((Object)path) + File.pathSeparator;
                }
                offset = path.length();
                break;
            }
            ++n2;
        }
        String[] outgoing = new String[list.length];
        int i = 0;
        while (i < list.length) {
            outgoing[i] = list[i].getAbsolutePath().substring(offset);
            ++i;
        }
        return outgoing;
    }

    public File[] listFiles(String path, String ... options) {
        File file = new File(path);
        if (!file.isAbsolute()) {
            file = this.sketchFile(path);
        }
        return PApplet.listFiles(file, options);
    }

    public static File[] listFiles(File base, String ... options) {
        boolean recursive = false;
        String[] extensions = null;
        boolean directories = true;
        boolean files = true;
        boolean hidden = false;
        String[] stringArray = options;
        int n = options.length;
        int n2 = 0;
        while (n2 < n) {
            String opt = stringArray[n2];
            if (opt.equals((Object)"recursive")) {
                recursive = true;
            } else if (opt.startsWith("extension=")) {
                extensions = new String[]{opt.substring(10)};
            } else if (opt.startsWith("extensions=")) {
                extensions = PApplet.split(opt.substring(10), ',');
            } else if (opt.equals((Object)"files")) {
                directories = false;
            } else if (opt.equals((Object)"directories")) {
                files = false;
            } else if (opt.equals((Object)"hidden")) {
                hidden = true;
            } else if (!opt.equals((Object)"relative")) {
                throw new RuntimeException(String.valueOf((Object)opt) + " is not a listFiles() option");
            }
            ++n2;
        }
        if (extensions != null) {
            int i = 0;
            while (i < extensions.length) {
                extensions[i] = "." + (String)extensions[i];
                ++i;
            }
        }
        if (!files && !directories) {
            files = true;
            directories = true;
        }
        if (!base.canRead()) {
            return null;
        }
        ArrayList outgoing = new ArrayList();
        PApplet.listFilesImpl(base, recursive, extensions, hidden, directories, files, (List<File>)outgoing);
        return (File[])outgoing.toArray((Object[])new File[0]);
    }

    static void listFilesImpl(File folder, boolean recursive, String[] extensions, boolean hidden, boolean directories, boolean files, List<File> list) {
        File[] items = folder.listFiles();
        if (items != null) {
            File[] fileArray = items;
            int n = items.length;
            int n2 = 0;
            while (n2 < n) {
                File item = fileArray[n2];
                String name = item.getName();
                if (hidden || name.charAt(0) != '.') {
                    if (item.isDirectory()) {
                        if (recursive) {
                            PApplet.listFilesImpl(item, recursive, extensions, hidden, directories, files, list);
                        }
                        if (directories) {
                            list.add((Object)item);
                        }
                    } else if (files) {
                        if (extensions == null) {
                            list.add((Object)item);
                        } else {
                            String[] stringArray = extensions;
                            int n3 = extensions.length;
                            int n4 = 0;
                            while (n4 < n3) {
                                String ext = stringArray[n4];
                                if (item.getName().toLowerCase().endsWith(ext)) {
                                    list.add((Object)item);
                                }
                                ++n4;
                            }
                        }
                    }
                }
                ++n2;
            }
        }
    }

    public static String checkExtension(String filename) {
        int dotIndex;
        if (filename.toLowerCase().endsWith(".gz")) {
            filename = filename.substring(0, filename.length() - 3);
        }
        if ((dotIndex = filename.lastIndexOf(46)) != -1) {
            return filename.substring(dotIndex + 1).toLowerCase();
        }
        return null;
    }

    public BufferedReader createReader(String filename) {
        InputStream is = this.createInput(filename);
        if (is == null) {
            System.err.println("The file \"" + filename + "\" " + "is missing or inaccessible, make sure " + "the URL is valid or that the file has been " + "added to your sketch and is readable.");
            return null;
        }
        return PApplet.createReader(is);
    }

    public static BufferedReader createReader(File file) {
        try {
            FileInputStream is = new FileInputStream(file);
            if (file.getName().toLowerCase().endsWith(".gz")) {
                is = new GZIPInputStream((InputStream)is);
            }
            return PApplet.createReader((InputStream)is);
        }
        catch (IOException e) {
            throw new RuntimeException((Throwable)e);
        }
    }

    public static BufferedReader createReader(InputStream input) {
        InputStreamReader isr = new InputStreamReader(input, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader((Reader)isr);
        try {
            reader.mark(1);
            int c = reader.read();
            if (c != 65279) {
                reader.reset();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return reader;
    }

    public PrintWriter createWriter(String filename) {
        return PApplet.createWriter(this.saveFile(filename));
    }

    public static PrintWriter createWriter(File file) {
        if (file == null) {
            throw new RuntimeException("File passed to createWriter() was null");
        }
        try {
            PApplet.createPath(file);
            FileOutputStream output = new FileOutputStream(file);
            if (file.getName().toLowerCase().endsWith(".gz")) {
                output = new GZIPOutputStream((OutputStream)output);
            }
            return PApplet.createWriter((OutputStream)output);
        }
        catch (Exception e) {
            throw new RuntimeException("Couldn't create a writer for " + file.getAbsolutePath(), (Throwable)e);
        }
    }

    public static PrintWriter createWriter(OutputStream output) {
        BufferedOutputStream bos = new BufferedOutputStream(output, 8192);
        OutputStreamWriter osw = new OutputStreamWriter((OutputStream)bos, StandardCharsets.UTF_8);
        return new PrintWriter((Writer)osw);
    }

    public InputStream createInput(String filename) {
        InputStream input = this.createInputRaw(filename);
        if (input != null) {
            String lower = filename.toLowerCase();
            if (lower.endsWith(".gz") || lower.endsWith(".svgz")) {
                try {
                    return new BufferedInputStream((InputStream)new GZIPInputStream(input));
                }
                catch (IOException e) {
                    this.printStackTrace(e);
                }
            } else {
                return new BufferedInputStream(input);
            }
        }
        return null;
    }

    public InputStream createInputRaw(String filename) {
        String cn;
        File file;
        InputStream stream;
        block36: {
            if (filename == null) {
                return null;
            }
            if (this.sketchPath == null) {
                System.err.println("The sketch path is not set.");
                throw new RuntimeException("Files must be loaded inside setup() or after it has been called.");
            }
            if (filename.length() == 0) {
                return null;
            }
            if (filename.contains((CharSequence)":")) {
                try {
                    URL url = new URL(filename);
                    URLConnection conn = url.openConnection();
                    if (conn instanceof HttpURLConnection) {
                        HttpURLConnection httpConn = (HttpURLConnection)conn;
                        httpConn.setInstanceFollowRedirects(true);
                        int response = httpConn.getResponseCode();
                        if (response >= 300 && response < 400) {
                            String newLocation = httpConn.getHeaderField("Location");
                            return this.createInputRaw(newLocation);
                        }
                        return conn.getInputStream();
                    }
                    if (conn instanceof JarURLConnection) {
                        return url.openStream();
                    }
                }
                catch (MalformedURLException malformedURLException) {
                }
                catch (FileNotFoundException fileNotFoundException) {
                }
                catch (IOException e) {
                    this.printStackTrace(e);
                    return null;
                }
            }
            stream = null;
            file = new File(this.dataPath(filename));
            if (!file.exists()) {
                file = this.sketchFile(filename);
            }
            if (!file.isDirectory()) break block36;
            return null;
        }
        try {
            if (file.exists()) {
                try {
                    String filePath = file.getCanonicalPath();
                    String filenameActual = new File(filePath).getName();
                    String filenameShort = new File(filename).getName();
                    if (!filenameActual.equals((Object)filenameShort)) {
                        throw new RuntimeException("This file is named " + filenameActual + " not " + filename + ". Rename the file " + "or change your code.");
                    }
                }
                catch (IOException iOException) {}
            }
            if ((stream = new FileInputStream(file)) != null) {
                return stream;
            }
        }
        catch (IOException iOException) {
        }
        catch (SecurityException securityException) {}
        ClassLoader cl = this.getClass().getClassLoader();
        stream = cl.getResourceAsStream("data/" + filename);
        if (stream != null && !(cn = stream.getClass().getName()).equals((Object)"sun.plugin.cache.EmptyInputStream")) {
            return stream;
        }
        stream = cl.getResourceAsStream(filename);
        if (stream != null && !(cn = stream.getClass().getName()).equals((Object)"sun.plugin.cache.EmptyInputStream")) {
            return stream;
        }
        try {
            try {
                try {
                    stream = new FileInputStream(this.dataPath(filename));
                    if (stream != null) {
                        return stream;
                    }
                }
                catch (IOException iOException) {}
                try {
                    stream = new FileInputStream(this.sketchPath(filename));
                    if (stream != null) {
                        return stream;
                    }
                }
                catch (Exception exception) {}
                try {
                    stream = new FileInputStream(filename);
                    if (stream != null) {
                        return stream;
                    }
                }
                catch (IOException iOException) {
                }
            }
            catch (SecurityException securityException) {}
        }
        catch (Exception e) {
            this.printStackTrace(e);
        }
        return null;
    }

    public static InputStream createInput(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File passed to createInput() was null");
        }
        if (!file.exists()) {
            System.err.println(file + " does not exist, createInput() will return null");
            return null;
        }
        try {
            FileInputStream input = new FileInputStream(file);
            String lower = file.getName().toLowerCase();
            if (lower.endsWith(".gz") || lower.endsWith(".svgz")) {
                return new BufferedInputStream((InputStream)new GZIPInputStream((InputStream)input));
            }
            return new BufferedInputStream((InputStream)input);
        }
        catch (IOException e) {
            System.err.println("Could not createInput() for " + file);
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] loadBytes(String filename) {
        block32: {
            block31: {
                lower = filename.toLowerCase();
                if (lower.endsWith(".gz") || !filename.contains((CharSequence)":")) break block32;
                input = null;
                url = new URL(filename);
                conn = url.openConnection();
                length = -1;
                if (!(conn instanceof HttpURLConnection)) ** GOTO lbl26
                httpConn = (HttpURLConnection)conn;
                httpConn.setInstanceFollowRedirects(true);
                response = httpConn.getResponseCode();
                if (response < 300 || response >= 400) break block31;
                newLocation = httpConn.getHeaderField("Location");
                var11_13 = this.loadBytes(newLocation);
                if (input == null) return var11_13;
                try {
                    input.close();
                    return var11_13;
                }
                catch (IOException v0) {}
                return var11_13;
            }
            try {
                block34: {
                    block35: {
                        block33: {
                            length = conn.getContentLength();
                            input = conn.getInputStream();
                            break block33;
lbl26:
                            // 1 sources

                            if (conn instanceof JarURLConnection) {
                                length = conn.getContentLength();
                                input = url.openStream();
                            }
                        }
                        if (input == null) break block34;
                        buffer = null;
                        if (length == -1) break block35;
                        buffer = new byte[length];
                        offset = 0;
                        if (true) ** GOTO lbl56
                    }
                    buffer = PApplet.loadBytes(input);
lbl38:
                    // 2 sources

                    while (true) {
                        input.close();
                        var11_14 = buffer;
                        if (input == null) return var11_14;
                        try {
                            input.close();
                            return var11_14;
                        }
                        catch (IOException v1) {}
                        return var11_14;
                    }
                }
                if (input == null) break block32;
                try {
                    input.close();
                    break block32;
                }
                catch (IOException v2) {}
                break block32;
                do {
                    offset += count;
lbl56:
                    // 2 sources

                } while ((count = input.read(buffer, offset, length - offset)) > 0);
                ** continue;
            }
            catch (MalformedURLException v3) {
                ** if (input == null) goto lbl-1000
lbl-1000:
                // 1 sources

                {
                    try {
                        input.close();
                    }
                    catch (IOException v4) {}
                }
lbl-1000:
                // 2 sources

                {
                }
                catch (FileNotFoundException v5) {
                    ** if (input == null) goto lbl-1000
lbl-1000:
                    // 1 sources

                    {
                        try {
                            input.close();
                        }
                        catch (IOException v6) {}
                    }
lbl-1000:
                    // 2 sources

                    {
                    }
                    catch (IOException e) {
                        try {
                            this.printStackTrace(e);
                            if (input == null) return null;
                        }
                        catch (Throwable var10_15) {
                            if (input == null) throw var10_15;
                            try {
                                input.close();
                                throw var10_15;
                            }
                            catch (IOException v7) {}
                            throw var10_15;
                        }
                        try {
                            input.close();
                            return null;
                        }
                        catch (IOException v8) {}
                        return null;
                    }
                }
            }
        }
        if ((is = this.createInput(filename)) == null) {
            System.err.println("The file \"" + filename + "\" " + "is missing or inaccessible, make sure " + "the URL is valid or that the file has been " + "added to your sketch and is readable.");
            return null;
        }
        outgoing = PApplet.loadBytes(is);
        try {
            is.close();
            return outgoing;
        }
        catch (IOException e) {
            this.printStackTrace(e);
        }
        return outgoing;
    }

    public static byte[] loadBytes(InputStream input) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead = input.read(buffer);
            while (bytesRead != -1) {
                out.write(buffer, 0, bytesRead);
                bytesRead = input.read(buffer);
            }
            out.flush();
            return out.toByteArray();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static byte[] loadBytes(File file) {
        if (!file.exists()) {
            System.err.println(file + " does not exist, loadBytes() will return null");
            return null;
        }
        try {
            BufferedInputStream input;
            int length;
            if (file.getName().toLowerCase().endsWith(".gz")) {
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                raf.seek(raf.length() - 4L);
                int b4 = raf.read();
                int b3 = raf.read();
                int b2 = raf.read();
                int b1 = raf.read();
                length = b1 << 24 | (b2 << 16) + (b3 << 8) + b4;
                raf.close();
                input = new BufferedInputStream((InputStream)new GZIPInputStream((InputStream)new FileInputStream(file)));
            } else {
                int maxArraySize;
                long len = file.length();
                if (len > (long)(maxArraySize = 0x7FFFFFFA)) {
                    System.err.println("Cannot use loadBytes() on a file larger than " + maxArraySize);
                    return null;
                }
                length = (int)len;
                input = new BufferedInputStream((InputStream)new FileInputStream(file));
            }
            byte[] buffer = new byte[length];
            int offset = 0;
            while (true) {
                int count;
                if ((count = input.read(buffer, offset, length - offset)) <= 0) {
                    input.close();
                    return buffer;
                }
                offset += count;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String[] loadStrings(File file) {
        if (!file.exists()) {
            System.err.println(file + " does not exist, loadStrings() will return null");
            return null;
        }
        InputStream is = PApplet.createInput(file);
        if (is != null) {
            String[] outgoing = PApplet.loadStrings(is);
            try {
                is.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return outgoing;
        }
        return null;
    }

    public String[] loadStrings(String filename) {
        InputStream is = this.createInput(filename);
        if (is != null) {
            String[] strArr = PApplet.loadStrings(is);
            try {
                is.close();
            }
            catch (IOException e) {
                this.printStackTrace(e);
            }
            return strArr;
        }
        System.err.println("The file \"" + filename + "\" " + "is missing or inaccessible, make sure " + "the URL is valid or that the file has been " + "added to your sketch and is readable.");
        return null;
    }

    public static String[] loadStrings(InputStream input) {
        try {
            BufferedReader reader = new BufferedReader((Reader)new InputStreamReader(input, "UTF-8"));
            return PApplet.loadStrings(reader);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String[] loadStrings(BufferedReader reader) {
        try {
            String[] lines = new String[100];
            int lineCount = 0;
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (lineCount == lines.length) {
                    String[] temp = new String[lineCount << 1];
                    System.arraycopy((Object)lines, (int)0, (Object)temp, (int)0, (int)lineCount);
                    lines = temp;
                }
                lines[lineCount++] = line;
            }
            reader.close();
            if (lineCount == lines.length) {
                return lines;
            }
            String[] output = new String[lineCount];
            System.arraycopy((Object)lines, (int)0, (Object)output, (int)0, (int)lineCount);
            return output;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public OutputStream createOutput(String filename) {
        return PApplet.createOutput(this.saveFile(filename));
    }

    public static OutputStream createOutput(File file) {
        try {
            PApplet.createPath(file);
            FileOutputStream output = new FileOutputStream(file);
            if (file.getName().toLowerCase().endsWith(".gz")) {
                return new BufferedOutputStream((OutputStream)new GZIPOutputStream((OutputStream)output));
            }
            return new BufferedOutputStream((OutputStream)output);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean saveStream(String target, String source) {
        return this.saveStream(this.saveFile(target), source);
    }

    public boolean saveStream(File target, String source) {
        return PApplet.saveStream(target, this.createInputRaw(source));
    }

    public boolean saveStream(String target, InputStream source) {
        return PApplet.saveStream(this.saveFile(target), source);
    }

    public static boolean saveStream(File target, InputStream source) {
        File tempFile = null;
        try {
            PApplet.createPath(target);
            tempFile = PApplet.createTempFile(target);
            FileOutputStream targetStream = new FileOutputStream(tempFile);
            PApplet.saveStream((OutputStream)targetStream, source);
            targetStream.close();
            targetStream = null;
            if (target.exists() && !target.delete()) {
                System.err.println("Could not replace " + target.getAbsolutePath() + ".");
            }
            if (!tempFile.renameTo(target)) {
                System.err.println("Could not rename temporary file " + tempFile.getAbsolutePath());
                return false;
            }
            return true;
        }
        catch (IOException e) {
            if (tempFile != null) {
                tempFile.delete();
            }
            e.printStackTrace();
            return false;
        }
    }

    public static void saveStream(OutputStream target, InputStream source) throws IOException {
        int bytesRead;
        BufferedInputStream bis = new BufferedInputStream(source, 16384);
        BufferedOutputStream bos = new BufferedOutputStream(target);
        byte[] buffer = new byte[8192];
        while ((bytesRead = bis.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }
        bos.flush();
    }

    public void saveBytes(String filename, byte[] data) {
        PApplet.saveBytes(this.saveFile(filename), data);
    }

    private static File createTempFile(File file) throws IOException {
        String prefix;
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        String name = file.getName();
        String suffix = null;
        int dot = name.lastIndexOf(46);
        if (dot == -1) {
            prefix = name;
        } else {
            prefix = name.substring(0, dot);
            suffix = name.substring(dot);
        }
        if (prefix.length() < 3) {
            prefix = String.valueOf((Object)prefix) + "processing";
        }
        return File.createTempFile((String)prefix, (String)suffix, (File)parentDir);
    }

    public static void saveBytes(File file, byte[] data) {
        File tempFile = null;
        try {
            tempFile = PApplet.createTempFile(file);
            OutputStream output = PApplet.createOutput(tempFile);
            PApplet.saveBytes(output, data);
            output.close();
            output = null;
            if (file.exists() && !file.delete()) {
                System.err.println("Could not replace " + file.getAbsolutePath());
            }
            if (!tempFile.renameTo(file)) {
                System.err.println("Could not rename temporary file " + tempFile.getAbsolutePath());
            }
        }
        catch (IOException e) {
            System.err.println("error saving bytes to " + file);
            if (tempFile != null) {
                tempFile.delete();
            }
            e.printStackTrace();
        }
    }

    public static void saveBytes(OutputStream output, byte[] data) {
        try {
            output.write(data);
            output.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveStrings(String filename, String[] data) {
        PApplet.saveStrings(this.saveFile(filename), data);
    }

    public static void saveStrings(File file, String[] data) {
        PApplet.saveStrings(PApplet.createOutput(file), data);
    }

    public static void saveStrings(OutputStream output, String[] data) {
        PrintWriter writer = PApplet.createWriter(output);
        int i = 0;
        while (i < data.length) {
            writer.println(data[i]);
            ++i;
        }
        writer.flush();
        writer.close();
    }

    protected static String calcSketchPath() {
        String folder = null;
        try {
            folder = System.getProperty((String)"user.dir");
            URL jarURL = PApplet.class.getProtectionDomain().getCodeSource().getLocation();
            String jarPath = jarURL.toURI().getSchemeSpecificPart();
            if (platform == 2) {
                if (jarPath.contains((CharSequence)"Contents/Java/")) {
                    String appPath = jarPath.substring(0, jarPath.indexOf(".app") + 4);
                    File containingFolder = new File(appPath).getParentFile();
                    folder = containingFolder.getAbsolutePath();
                }
            } else if (jarPath.contains((CharSequence)"/lib/")) {
                folder = new File(jarPath, "../..").getCanonicalPath();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return folder;
    }

    public String sketchPath() {
        if (this.sketchPath == null) {
            this.sketchPath = PApplet.calcSketchPath();
        }
        return this.sketchPath;
    }

    public String sketchPath(String where) {
        if (this.sketchPath() == null) {
            return where;
        }
        try {
            if (new File(where).isAbsolute()) {
                return where;
            }
        }
        catch (Exception exception) {}
        return String.valueOf((Object)this.sketchPath()) + File.separator + where;
    }

    public File sketchFile(String where) {
        return new File(this.sketchPath(where));
    }

    public String savePath(String where) {
        if (where == null) {
            return null;
        }
        String filename = this.sketchPath(where);
        PApplet.createPath(filename);
        return filename;
    }

    public File saveFile(String where) {
        return new File(this.savePath(where));
    }

    public static File desktopFile(String what) {
        if (desktopFolder == null && !(desktopFolder = new File(System.getProperty((String)"user.home"), "Desktop")).exists()) {
            if (platform == 1) {
                FileSystemView filesys = FileSystemView.getFileSystemView();
                desktopFolder = filesys.getHomeDirectory();
            } else {
                throw new UnsupportedOperationException("Could not find a suitable desktop foldder");
            }
        }
        return new File(desktopFolder, what);
    }

    public static String desktopPath(String what) {
        return PApplet.desktopFile(what).getAbsolutePath();
    }

    public String dataPath(String where) {
        return this.dataFile(where).getAbsolutePath();
    }

    public File dataFile(String where) {
        String jarPath;
        File why = new File(where);
        if (why.isAbsolute()) {
            return why;
        }
        URL jarURL = this.getClass().getProtectionDomain().getCodeSource().getLocation();
        try {
            jarPath = jarURL.toURI().getPath();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
        if (jarPath.contains((CharSequence)"Contents/Java/")) {
            File containingFolder = new File(jarPath).getParentFile();
            File dataFolder = new File(containingFolder, "data");
            return new File(dataFolder, where);
        }
        File workingDirItem = new File(String.valueOf((Object)this.sketchPath) + File.separator + "data" + File.separator + where);
        return workingDirItem;
    }

    public static void createPath(String path) {
        PApplet.createPath(new File(path));
    }

    public static void createPath(File file) {
        try {
            File unit;
            String parent = file.getParent();
            if (parent != null && !(unit = new File(parent)).exists()) {
                unit.mkdirs();
            }
        }
        catch (SecurityException securityException) {
            System.err.println("You don't have permissions to create " + file.getAbsolutePath());
        }
    }

    public static String getExtension(String filename) {
        String lower = filename.toLowerCase();
        int dot = filename.lastIndexOf(46);
        if (dot == -1) {
            return "";
        }
        String extension = lower.substring(dot + 1);
        int question = extension.indexOf(63);
        if (question != -1) {
            extension = extension.substring(0, question);
        }
        return extension;
    }

    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode((String)str, (String)"UTF-8");
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            return null;
        }
    }

    public static String urlDecode(String str) {
        try {
            return URLDecoder.decode((String)str, (String)"UTF-8");
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            return null;
        }
    }

    public static byte[] sort(byte[] list) {
        return PApplet.sort(list, list.length);
    }

    public static byte[] sort(byte[] list, int count) {
        byte[] outgoing = new byte[list.length];
        System.arraycopy((Object)list, (int)0, (Object)outgoing, (int)0, (int)list.length);
        Arrays.sort((byte[])outgoing, (int)0, (int)count);
        return outgoing;
    }

    public static char[] sort(char[] list) {
        return PApplet.sort(list, list.length);
    }

    public static char[] sort(char[] list, int count) {
        char[] outgoing = new char[list.length];
        System.arraycopy((Object)list, (int)0, (Object)outgoing, (int)0, (int)list.length);
        Arrays.sort((char[])outgoing, (int)0, (int)count);
        return outgoing;
    }

    public static int[] sort(int[] list) {
        return PApplet.sort(list, list.length);
    }

    public static int[] sort(int[] list, int count) {
        int[] outgoing = new int[list.length];
        System.arraycopy((Object)list, (int)0, (Object)outgoing, (int)0, (int)list.length);
        Arrays.sort((int[])outgoing, (int)0, (int)count);
        return outgoing;
    }

    public static float[] sort(float[] list) {
        return PApplet.sort(list, list.length);
    }

    public static float[] sort(float[] list, int count) {
        float[] outgoing = new float[list.length];
        System.arraycopy((Object)list, (int)0, (Object)outgoing, (int)0, (int)list.length);
        Arrays.sort((float[])outgoing, (int)0, (int)count);
        return outgoing;
    }

    public static String[] sort(String[] list) {
        return PApplet.sort(list, list.length);
    }

    public static String[] sort(String[] list, int count) {
        Object[] outgoing = new String[list.length];
        System.arraycopy((Object)list, (int)0, (Object)outgoing, (int)0, (int)list.length);
        Arrays.sort((Object[])outgoing, (int)0, (int)count);
        return outgoing;
    }

    public static void arrayCopy(Object src, int srcPosition, Object dst, int dstPosition, int length) {
        System.arraycopy((Object)src, (int)srcPosition, (Object)dst, (int)dstPosition, (int)length);
    }

    public static void arrayCopy(Object src, Object dst, int length) {
        System.arraycopy((Object)src, (int)0, (Object)dst, (int)0, (int)length);
    }

    public static void arrayCopy(Object src, Object dst) {
        System.arraycopy((Object)src, (int)0, (Object)dst, (int)0, (int)Array.getLength((Object)src));
    }

    @Deprecated
    public static void arraycopy(Object src, int srcPosition, Object dst, int dstPosition, int length) {
        System.arraycopy((Object)src, (int)srcPosition, (Object)dst, (int)dstPosition, (int)length);
    }

    @Deprecated
    public static void arraycopy(Object src, Object dst, int length) {
        System.arraycopy((Object)src, (int)0, (Object)dst, (int)0, (int)length);
    }

    @Deprecated
    public static void arraycopy(Object src, Object dst) {
        System.arraycopy((Object)src, (int)0, (Object)dst, (int)0, (int)Array.getLength((Object)src));
    }

    public static boolean[] expand(boolean[] list) {
        return PApplet.expand(list, list.length > 0 ? list.length << 1 : 1);
    }

    public static boolean[] expand(boolean[] list, int newSize) {
        boolean[] temp = new boolean[newSize];
        System.arraycopy((Object)list, (int)0, (Object)temp, (int)0, (int)Math.min((int)newSize, (int)list.length));
        return temp;
    }

    public static byte[] expand(byte[] list) {
        return PApplet.expand(list, list.length > 0 ? list.length << 1 : 1);
    }

    public static byte[] expand(byte[] list, int newSize) {
        byte[] temp = new byte[newSize];
        System.arraycopy((Object)list, (int)0, (Object)temp, (int)0, (int)Math.min((int)newSize, (int)list.length));
        return temp;
    }

    public static char[] expand(char[] list) {
        return PApplet.expand(list, list.length > 0 ? list.length << 1 : 1);
    }

    public static char[] expand(char[] list, int newSize) {
        char[] temp = new char[newSize];
        System.arraycopy((Object)list, (int)0, (Object)temp, (int)0, (int)Math.min((int)newSize, (int)list.length));
        return temp;
    }

    public static int[] expand(int[] list) {
        return PApplet.expand(list, list.length > 0 ? list.length << 1 : 1);
    }

    public static int[] expand(int[] list, int newSize) {
        int[] temp = new int[newSize];
        System.arraycopy((Object)list, (int)0, (Object)temp, (int)0, (int)Math.min((int)newSize, (int)list.length));
        return temp;
    }

    public static long[] expand(long[] list) {
        return PApplet.expand(list, list.length > 0 ? list.length << 1 : 1);
    }

    public static long[] expand(long[] list, int newSize) {
        long[] temp = new long[newSize];
        System.arraycopy((Object)list, (int)0, (Object)temp, (int)0, (int)Math.min((int)newSize, (int)list.length));
        return temp;
    }

    public static float[] expand(float[] list) {
        return PApplet.expand(list, list.length > 0 ? list.length << 1 : 1);
    }

    public static float[] expand(float[] list, int newSize) {
        float[] temp = new float[newSize];
        System.arraycopy((Object)list, (int)0, (Object)temp, (int)0, (int)Math.min((int)newSize, (int)list.length));
        return temp;
    }

    public static double[] expand(double[] list) {
        return PApplet.expand(list, list.length > 0 ? list.length << 1 : 1);
    }

    public static double[] expand(double[] list, int newSize) {
        double[] temp = new double[newSize];
        System.arraycopy((Object)list, (int)0, (Object)temp, (int)0, (int)Math.min((int)newSize, (int)list.length));
        return temp;
    }

    public static String[] expand(String[] list) {
        return PApplet.expand(list, list.length > 0 ? list.length << 1 : 1);
    }

    public static String[] expand(String[] list, int newSize) {
        String[] temp = new String[newSize];
        System.arraycopy((Object)list, (int)0, (Object)temp, (int)0, (int)Math.min((int)newSize, (int)list.length));
        return temp;
    }

    public static Object expand(Object array) {
        int len = Array.getLength((Object)array);
        return PApplet.expand(array, len > 0 ? len << 1 : 1);
    }

    public static Object expand(Object list, int newSize) {
        Class type = list.getClass().getComponentType();
        Object temp = Array.newInstance((Class)type, (int)newSize);
        System.arraycopy((Object)list, (int)0, (Object)temp, (int)0, (int)Math.min((int)Array.getLength((Object)list), (int)newSize));
        return temp;
    }

    public static byte[] append(byte[] array, byte value) {
        array = PApplet.expand(array, array.length + 1);
        array[array.length - 1] = value;
        return array;
    }

    public static char[] append(char[] array, char value) {
        array = PApplet.expand(array, array.length + 1);
        array[array.length - 1] = value;
        return array;
    }

    public static int[] append(int[] array, int value) {
        array = PApplet.expand(array, array.length + 1);
        array[array.length - 1] = value;
        return array;
    }

    public static float[] append(float[] array, float value) {
        array = PApplet.expand(array, array.length + 1);
        array[array.length - 1] = value;
        return array;
    }

    public static String[] append(String[] array, String value) {
        array = PApplet.expand(array, array.length + 1);
        array[array.length - 1] = value;
        return array;
    }

    public static Object append(Object array, Object value) {
        int length = Array.getLength((Object)array);
        array = PApplet.expand(array, length + 1);
        Array.set((Object)array, (int)length, (Object)value);
        return array;
    }

    public static boolean[] shorten(boolean[] list) {
        return PApplet.subset(list, 0, list.length - 1);
    }

    public static byte[] shorten(byte[] list) {
        return PApplet.subset(list, 0, list.length - 1);
    }

    public static char[] shorten(char[] list) {
        return PApplet.subset(list, 0, list.length - 1);
    }

    public static int[] shorten(int[] list) {
        return PApplet.subset(list, 0, list.length - 1);
    }

    public static float[] shorten(float[] list) {
        return PApplet.subset(list, 0, list.length - 1);
    }

    public static String[] shorten(String[] list) {
        return PApplet.subset(list, 0, list.length - 1);
    }

    public static Object shorten(Object list) {
        int length = Array.getLength((Object)list);
        return PApplet.subset(list, 0, length - 1);
    }

    public static final boolean[] splice(boolean[] list, boolean value, int index) {
        boolean[] outgoing = new boolean[list.length + 1];
        System.arraycopy((Object)list, (int)0, (Object)outgoing, (int)0, (int)index);
        outgoing[index] = value;
        System.arraycopy((Object)list, (int)index, (Object)outgoing, (int)(index + 1), (int)(list.length - index));
        return outgoing;
    }

    public static final boolean[] splice(boolean[] list, boolean[] value, int index) {
        boolean[] outgoing = new boolean[list.length + value.length];
        System.arraycopy((Object)list, (int)0, (Object)outgoing, (int)0, (int)index);
        System.arraycopy((Object)value, (int)0, (Object)outgoing, (int)index, (int)value.length);
        System.arraycopy((Object)list, (int)index, (Object)outgoing, (int)(index + value.length), (int)(list.length - index));
        return outgoing;
    }

    public static final byte[] splice(byte[] list, byte value, int index) {
        byte[] outgoing = new byte[list.length + 1];
        System.arraycopy((Object)list, (int)0, (Object)outgoing, (int)0, (int)index);
        outgoing[index] = value;
        System.arraycopy((Object)list, (int)index, (Object)outgoing, (int)(index + 1), (int)(list.length - index));
        return outgoing;
    }

    public static final byte[] splice(byte[] list, byte[] value, int index) {
        byte[] outgoing = new byte[list.length + value.length];
        System.arraycopy((Object)list, (int)0, (Object)outgoing, (int)0, (int)index);
        System.arraycopy((Object)value, (int)0, (Object)outgoing, (int)index, (int)value.length);
        System.arraycopy((Object)list, (int)index, (Object)outgoing, (int)(index + value.length), (int)(list.length - index));
        return outgoing;
    }

    public static final char[] splice(char[] list, char value, int index) {
        char[] outgoing = new char[list.length + 1];
        System.arraycopy((Object)list, (int)0, (Object)outgoing, (int)0, (int)index);
        outgoing[index] = value;
        System.arraycopy((Object)list, (int)index, (Object)outgoing, (int)(index + 1), (int)(list.length - index));
        return outgoing;
    }

    public static final char[] splice(char[] list, char[] value, int index) {
        char[] outgoing = new char[list.length + value.length];
        System.arraycopy((Object)list, (int)0, (Object)outgoing, (int)0, (int)index);
        System.arraycopy((Object)value, (int)0, (Object)outgoing, (int)index, (int)value.length);
        System.arraycopy((Object)list, (int)index, (Object)outgoing, (int)(index + value.length), (int)(list.length - index));
        return outgoing;
    }

    public static final int[] splice(int[] list, int value, int index) {
        int[] outgoing = new int[list.length + 1];
        System.arraycopy((Object)list, (int)0, (Object)outgoing, (int)0, (int)index);
        outgoing[index] = value;
        System.arraycopy((Object)list, (int)index, (Object)outgoing, (int)(index + 1), (int)(list.length - index));
        return outgoing;
    }

    public static final int[] splice(int[] list, int[] value, int index) {
        int[] outgoing = new int[list.length + value.length];
        System.arraycopy((Object)list, (int)0, (Object)outgoing, (int)0, (int)index);
        System.arraycopy((Object)value, (int)0, (Object)outgoing, (int)index, (int)value.length);
        System.arraycopy((Object)list, (int)index, (Object)outgoing, (int)(index + value.length), (int)(list.length - index));
        return outgoing;
    }

    public static final float[] splice(float[] list, float value, int index) {
        float[] outgoing = new float[list.length + 1];
        System.arraycopy((Object)list, (int)0, (Object)outgoing, (int)0, (int)index);
        outgoing[index] = value;
        System.arraycopy((Object)list, (int)index, (Object)outgoing, (int)(index + 1), (int)(list.length - index));
        return outgoing;
    }

    public static final float[] splice(float[] list, float[] value, int index) {
        float[] outgoing = new float[list.length + value.length];
        System.arraycopy((Object)list, (int)0, (Object)outgoing, (int)0, (int)index);
        System.arraycopy((Object)value, (int)0, (Object)outgoing, (int)index, (int)value.length);
        System.arraycopy((Object)list, (int)index, (Object)outgoing, (int)(index + value.length), (int)(list.length - index));
        return outgoing;
    }

    public static final String[] splice(String[] list, String value, int index) {
        String[] outgoing = new String[list.length + 1];
        System.arraycopy((Object)list, (int)0, (Object)outgoing, (int)0, (int)index);
        outgoing[index] = value;
        System.arraycopy((Object)list, (int)index, (Object)outgoing, (int)(index + 1), (int)(list.length - index));
        return outgoing;
    }

    public static final String[] splice(String[] list, String[] value, int index) {
        String[] outgoing = new String[list.length + value.length];
        System.arraycopy((Object)list, (int)0, (Object)outgoing, (int)0, (int)index);
        System.arraycopy((Object)value, (int)0, (Object)outgoing, (int)index, (int)value.length);
        System.arraycopy((Object)list, (int)index, (Object)outgoing, (int)(index + value.length), (int)(list.length - index));
        return outgoing;
    }

    public static final Object splice(Object list, Object value, int index) {
        Class type = list.getClass().getComponentType();
        Object outgoing = null;
        int length = Array.getLength((Object)list);
        if (value.getClass().getName().charAt(0) == '[') {
            int vlength = Array.getLength((Object)value);
            outgoing = Array.newInstance((Class)type, (int)(length + vlength));
            System.arraycopy((Object)list, (int)0, (Object)outgoing, (int)0, (int)index);
            System.arraycopy((Object)value, (int)0, (Object)outgoing, (int)index, (int)vlength);
            System.arraycopy((Object)list, (int)index, (Object)outgoing, (int)(index + vlength), (int)(length - index));
        } else {
            outgoing = Array.newInstance((Class)type, (int)(length + 1));
            System.arraycopy((Object)list, (int)0, (Object)outgoing, (int)0, (int)index);
            Array.set((Object)outgoing, (int)index, (Object)value);
            System.arraycopy((Object)list, (int)index, (Object)outgoing, (int)(index + 1), (int)(length - index));
        }
        return outgoing;
    }

    public static boolean[] subset(boolean[] list, int start) {
        return PApplet.subset(list, start, list.length - start);
    }

    public static boolean[] subset(boolean[] list, int start, int count) {
        boolean[] output = new boolean[count];
        System.arraycopy((Object)list, (int)start, (Object)output, (int)0, (int)count);
        return output;
    }

    public static byte[] subset(byte[] list, int start) {
        return PApplet.subset(list, start, list.length - start);
    }

    public static byte[] subset(byte[] list, int start, int count) {
        byte[] output = new byte[count];
        System.arraycopy((Object)list, (int)start, (Object)output, (int)0, (int)count);
        return output;
    }

    public static char[] subset(char[] list, int start) {
        return PApplet.subset(list, start, list.length - start);
    }

    public static char[] subset(char[] list, int start, int count) {
        char[] output = new char[count];
        System.arraycopy((Object)list, (int)start, (Object)output, (int)0, (int)count);
        return output;
    }

    public static int[] subset(int[] list, int start) {
        return PApplet.subset(list, start, list.length - start);
    }

    public static int[] subset(int[] list, int start, int count) {
        int[] output = new int[count];
        System.arraycopy((Object)list, (int)start, (Object)output, (int)0, (int)count);
        return output;
    }

    public static long[] subset(long[] list, int start) {
        return PApplet.subset(list, start, list.length - start);
    }

    public static long[] subset(long[] list, int start, int count) {
        long[] output = new long[count];
        System.arraycopy((Object)list, (int)start, (Object)output, (int)0, (int)count);
        return output;
    }

    public static float[] subset(float[] list, int start) {
        return PApplet.subset(list, start, list.length - start);
    }

    public static float[] subset(float[] list, int start, int count) {
        float[] output = new float[count];
        System.arraycopy((Object)list, (int)start, (Object)output, (int)0, (int)count);
        return output;
    }

    public static double[] subset(double[] list, int start) {
        return PApplet.subset(list, start, list.length - start);
    }

    public static double[] subset(double[] list, int start, int count) {
        double[] output = new double[count];
        System.arraycopy((Object)list, (int)start, (Object)output, (int)0, (int)count);
        return output;
    }

    public static String[] subset(String[] list, int start) {
        return PApplet.subset(list, start, list.length - start);
    }

    public static String[] subset(String[] list, int start, int count) {
        String[] output = new String[count];
        System.arraycopy((Object)list, (int)start, (Object)output, (int)0, (int)count);
        return output;
    }

    public static Object subset(Object list, int start) {
        int length = Array.getLength((Object)list);
        return PApplet.subset(list, start, length - start);
    }

    public static Object subset(Object list, int start, int count) {
        Class type = list.getClass().getComponentType();
        Object outgoing = Array.newInstance((Class)type, (int)count);
        System.arraycopy((Object)list, (int)start, (Object)outgoing, (int)0, (int)count);
        return outgoing;
    }

    public static boolean[] concat(boolean[] a, boolean[] b) {
        boolean[] c = new boolean[a.length + b.length];
        System.arraycopy((Object)a, (int)0, (Object)c, (int)0, (int)a.length);
        System.arraycopy((Object)b, (int)0, (Object)c, (int)a.length, (int)b.length);
        return c;
    }

    public static byte[] concat(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy((Object)a, (int)0, (Object)c, (int)0, (int)a.length);
        System.arraycopy((Object)b, (int)0, (Object)c, (int)a.length, (int)b.length);
        return c;
    }

    public static char[] concat(char[] a, char[] b) {
        char[] c = new char[a.length + b.length];
        System.arraycopy((Object)a, (int)0, (Object)c, (int)0, (int)a.length);
        System.arraycopy((Object)b, (int)0, (Object)c, (int)a.length, (int)b.length);
        return c;
    }

    public static int[] concat(int[] a, int[] b) {
        int[] c = new int[a.length + b.length];
        System.arraycopy((Object)a, (int)0, (Object)c, (int)0, (int)a.length);
        System.arraycopy((Object)b, (int)0, (Object)c, (int)a.length, (int)b.length);
        return c;
    }

    public static float[] concat(float[] a, float[] b) {
        float[] c = new float[a.length + b.length];
        System.arraycopy((Object)a, (int)0, (Object)c, (int)0, (int)a.length);
        System.arraycopy((Object)b, (int)0, (Object)c, (int)a.length, (int)b.length);
        return c;
    }

    public static String[] concat(String[] a, String[] b) {
        String[] c = new String[a.length + b.length];
        System.arraycopy((Object)a, (int)0, (Object)c, (int)0, (int)a.length);
        System.arraycopy((Object)b, (int)0, (Object)c, (int)a.length, (int)b.length);
        return c;
    }

    public static Object concat(Object a, Object b) {
        Class type = a.getClass().getComponentType();
        int alength = Array.getLength((Object)a);
        int blength = Array.getLength((Object)b);
        Object outgoing = Array.newInstance((Class)type, (int)(alength + blength));
        System.arraycopy((Object)a, (int)0, (Object)outgoing, (int)0, (int)alength);
        System.arraycopy((Object)b, (int)0, (Object)outgoing, (int)alength, (int)blength);
        return outgoing;
    }

    public static boolean[] reverse(boolean[] list) {
        boolean[] outgoing = new boolean[list.length];
        int length1 = list.length - 1;
        int i = 0;
        while (i < list.length) {
            outgoing[i] = list[length1 - i];
            ++i;
        }
        return outgoing;
    }

    public static byte[] reverse(byte[] list) {
        byte[] outgoing = new byte[list.length];
        int length1 = list.length - 1;
        int i = 0;
        while (i < list.length) {
            outgoing[i] = list[length1 - i];
            ++i;
        }
        return outgoing;
    }

    public static char[] reverse(char[] list) {
        char[] outgoing = new char[list.length];
        int length1 = list.length - 1;
        int i = 0;
        while (i < list.length) {
            outgoing[i] = list[length1 - i];
            ++i;
        }
        return outgoing;
    }

    public static int[] reverse(int[] list) {
        int[] outgoing = new int[list.length];
        int length1 = list.length - 1;
        int i = 0;
        while (i < list.length) {
            outgoing[i] = list[length1 - i];
            ++i;
        }
        return outgoing;
    }

    public static float[] reverse(float[] list) {
        float[] outgoing = new float[list.length];
        int length1 = list.length - 1;
        int i = 0;
        while (i < list.length) {
            outgoing[i] = list[length1 - i];
            ++i;
        }
        return outgoing;
    }

    public static String[] reverse(String[] list) {
        String[] outgoing = new String[list.length];
        int length1 = list.length - 1;
        int i = 0;
        while (i < list.length) {
            outgoing[i] = list[length1 - i];
            ++i;
        }
        return outgoing;
    }

    public static Object reverse(Object list) {
        Class type = list.getClass().getComponentType();
        int length = Array.getLength((Object)list);
        Object outgoing = Array.newInstance((Class)type, (int)length);
        int i = 0;
        while (i < length) {
            Array.set((Object)outgoing, (int)i, (Object)Array.get((Object)list, (int)(length - 1 - i)));
            ++i;
        }
        return outgoing;
    }

    public static String trim(String str) {
        if (str == null) {
            return null;
        }
        return str.replace('\u00a0', ' ').trim();
    }

    public static String[] trim(String[] array) {
        if (array == null) {
            return null;
        }
        String[] outgoing = new String[array.length];
        int i = 0;
        while (i < array.length) {
            if (array[i] != null) {
                outgoing[i] = PApplet.trim(array[i]);
            }
            ++i;
        }
        return outgoing;
    }

    public static String join(String[] list, char separator) {
        return PApplet.join(list, String.valueOf((char)separator));
    }

    public static String join(String[] list, String separator) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < list.length) {
            if (i != 0) {
                sb.append(separator);
            }
            sb.append(list[i]);
            ++i;
        }
        return sb.toString();
    }

    public static String[] splitTokens(String value) {
        return PApplet.splitTokens(value, " \t\n\r\f\u00a0");
    }

    public static String[] splitTokens(String value, String delim) {
        StringTokenizer toker = new StringTokenizer(value, delim);
        String[] pieces = new String[toker.countTokens()];
        int index = 0;
        while (toker.hasMoreTokens()) {
            pieces[index++] = toker.nextToken();
        }
        return pieces;
    }

    public static String[] split(String value, char delim) {
        if (value == null) {
            return null;
        }
        char[] chars = value.toCharArray();
        int splitCount = 0;
        int i = 0;
        while (i < chars.length) {
            if (chars[i] == delim) {
                ++splitCount;
            }
            ++i;
        }
        if (splitCount == 0) {
            String[] splits = new String[]{value};
            return splits;
        }
        String[] splits = new String[splitCount + 1];
        int splitIndex = 0;
        int startIndex = 0;
        int i2 = 0;
        while (i2 < chars.length) {
            if (chars[i2] == delim) {
                splits[splitIndex++] = new String(chars, startIndex, i2 - startIndex);
                startIndex = i2 + 1;
            }
            ++i2;
        }
        splits[splitIndex] = new String(chars, startIndex, chars.length - startIndex);
        return splits;
    }

    public static String[] split(String value, String delim) {
        int index;
        ArrayList items = new ArrayList();
        int offset = 0;
        while ((index = value.indexOf(delim, offset)) != -1) {
            items.add((Object)value.substring(offset, index));
            offset = index + delim.length();
        }
        items.add((Object)value.substring(offset));
        Object[] outgoing = new String[items.size()];
        items.toArray(outgoing);
        return outgoing;
    }

    static Pattern matchPattern(String regexp) {
        Pattern p = null;
        if (matchPatterns == null) {
            matchPatterns = new /* Unavailable Anonymous Inner Class!! */;
        } else {
            p = (Pattern)matchPatterns.get((Object)regexp);
        }
        if (p == null) {
            p = Pattern.compile((String)regexp, (int)40);
            matchPatterns.put((Object)regexp, (Object)p);
        }
        return p;
    }

    public static String[] match(String str, String regexp) {
        Pattern p = PApplet.matchPattern(regexp);
        Matcher m = p.matcher((CharSequence)str);
        if (m.find()) {
            int count = m.groupCount() + 1;
            String[] groups = new String[count];
            int i = 0;
            while (i < count) {
                groups[i] = m.group(i);
                ++i;
            }
            return groups;
        }
        return null;
    }

    public static String[][] matchAll(String str, String regexp) {
        int i;
        Pattern p = PApplet.matchPattern(regexp);
        Matcher m = p.matcher((CharSequence)str);
        ArrayList results = new ArrayList();
        int count = m.groupCount() + 1;
        while (m.find()) {
            String[] groups = new String[count];
            i = 0;
            while (i < count) {
                groups[i] = m.group(i);
                ++i;
            }
            results.add((Object)groups);
        }
        if (results.isEmpty()) {
            return null;
        }
        String[][] matches = new String[results.size()][count];
        i = 0;
        while (i < matches.length) {
            matches[i] = (String[])results.get(i);
            ++i;
        }
        return matches;
    }

    public static final boolean parseBoolean(int what) {
        return what != 0;
    }

    public static final boolean parseBoolean(String what) {
        return Boolean.parseBoolean((String)what);
    }

    public static final boolean[] parseBoolean(int[] what) {
        boolean[] outgoing = new boolean[what.length];
        int i = 0;
        while (i < what.length) {
            outgoing[i] = what[i] != 0;
            ++i;
        }
        return outgoing;
    }

    public static final boolean[] parseBoolean(String[] what) {
        boolean[] outgoing = new boolean[what.length];
        int i = 0;
        while (i < what.length) {
            outgoing[i] = Boolean.parseBoolean((String)what[i]);
            ++i;
        }
        return outgoing;
    }

    public static final byte parseByte(boolean what) {
        return what ? (byte)1 : 0;
    }

    public static final byte parseByte(char what) {
        return (byte)what;
    }

    public static final byte parseByte(int what) {
        return (byte)what;
    }

    public static final byte parseByte(float what) {
        return (byte)what;
    }

    public static final byte[] parseByte(boolean[] what) {
        byte[] outgoing = new byte[what.length];
        int i = 0;
        while (i < what.length) {
            outgoing[i] = what[i] ? (byte)1 : 0;
            ++i;
        }
        return outgoing;
    }

    public static final byte[] parseByte(char[] what) {
        byte[] outgoing = new byte[what.length];
        int i = 0;
        while (i < what.length) {
            outgoing[i] = (byte)what[i];
            ++i;
        }
        return outgoing;
    }

    public static final byte[] parseByte(int[] what) {
        byte[] outgoing = new byte[what.length];
        int i = 0;
        while (i < what.length) {
            outgoing[i] = (byte)what[i];
            ++i;
        }
        return outgoing;
    }

    public static final byte[] parseByte(float[] what) {
        byte[] outgoing = new byte[what.length];
        int i = 0;
        while (i < what.length) {
            outgoing[i] = (byte)what[i];
            ++i;
        }
        return outgoing;
    }

    public static final char parseChar(byte what) {
        return (char)(what & 0xFF);
    }

    public static final char parseChar(int what) {
        return (char)what;
    }

    public static final char[] parseChar(byte[] what) {
        char[] outgoing = new char[what.length];
        int i = 0;
        while (i < what.length) {
            outgoing[i] = (char)(what[i] & 0xFF);
            ++i;
        }
        return outgoing;
    }

    public static final char[] parseChar(int[] what) {
        char[] outgoing = new char[what.length];
        int i = 0;
        while (i < what.length) {
            outgoing[i] = (char)what[i];
            ++i;
        }
        return outgoing;
    }

    public static final int parseInt(boolean what) {
        return what ? 1 : 0;
    }

    public static final int parseInt(byte what) {
        return what & 0xFF;
    }

    public static final int parseInt(char what) {
        return what;
    }

    public static final int parseInt(float what) {
        return (int)what;
    }

    public static final int parseInt(String what) {
        return PApplet.parseInt(what, 0);
    }

    public static final int parseInt(String what, int otherwise) {
        try {
            int offset = what.indexOf(46);
            if (offset == -1) {
                return Integer.parseInt((String)what);
            }
            return Integer.parseInt((String)what.substring(0, offset));
        }
        catch (NumberFormatException numberFormatException) {
            return otherwise;
        }
    }

    public static final int[] parseInt(boolean[] what) {
        int[] list = new int[what.length];
        int i = 0;
        while (i < what.length) {
            list[i] = what[i] ? 1 : 0;
            ++i;
        }
        return list;
    }

    public static final int[] parseInt(byte[] what) {
        int[] list = new int[what.length];
        int i = 0;
        while (i < what.length) {
            list[i] = what[i] & 0xFF;
            ++i;
        }
        return list;
    }

    public static final int[] parseInt(char[] what) {
        int[] list = new int[what.length];
        int i = 0;
        while (i < what.length) {
            list[i] = what[i];
            ++i;
        }
        return list;
    }

    public static int[] parseInt(float[] what) {
        int[] inties = new int[what.length];
        int i = 0;
        while (i < what.length) {
            inties[i] = (int)what[i];
            ++i;
        }
        return inties;
    }

    public static int[] parseInt(String[] what) {
        return PApplet.parseInt(what, 0);
    }

    public static int[] parseInt(String[] what, int missing) {
        int[] output = new int[what.length];
        int i = 0;
        while (i < what.length) {
            try {
                output[i] = Integer.parseInt((String)what[i]);
            }
            catch (NumberFormatException numberFormatException) {
                output[i] = missing;
            }
            ++i;
        }
        return output;
    }

    public static final float parseFloat(int what) {
        return what;
    }

    public static final float parseFloat(String what) {
        return PApplet.parseFloat(what, Float.NaN);
    }

    public static final float parseFloat(String what, float otherwise) {
        try {
            return Float.parseFloat((String)what);
        }
        catch (NumberFormatException numberFormatException) {
            return otherwise;
        }
    }

    public static final float[] parseFloat(byte[] what) {
        float[] floaties = new float[what.length];
        int i = 0;
        while (i < what.length) {
            floaties[i] = what[i];
            ++i;
        }
        return floaties;
    }

    public static final float[] parseFloat(int[] what) {
        float[] floaties = new float[what.length];
        int i = 0;
        while (i < what.length) {
            floaties[i] = what[i];
            ++i;
        }
        return floaties;
    }

    public static final float[] parseFloat(String[] what) {
        return PApplet.parseFloat(what, Float.NaN);
    }

    public static final float[] parseFloat(String[] what, float missing) {
        float[] output = new float[what.length];
        int i = 0;
        while (i < what.length) {
            try {
                output[i] = Float.parseFloat((String)what[i]);
            }
            catch (NumberFormatException numberFormatException) {
                output[i] = missing;
            }
            ++i;
        }
        return output;
    }

    public static final String str(boolean x) {
        return String.valueOf((boolean)x);
    }

    public static final String str(byte x) {
        return String.valueOf((int)x);
    }

    public static final String str(char x) {
        return String.valueOf((char)x);
    }

    public static final String str(int x) {
        return String.valueOf((int)x);
    }

    public static final String str(float x) {
        return String.valueOf((float)x);
    }

    public static final String[] str(boolean[] x) {
        String[] s = new String[x.length];
        int i = 0;
        while (i < x.length) {
            s[i] = String.valueOf((boolean)x[i]);
            ++i;
        }
        return s;
    }

    public static final String[] str(byte[] x) {
        String[] s = new String[x.length];
        int i = 0;
        while (i < x.length) {
            s[i] = String.valueOf((int)x[i]);
            ++i;
        }
        return s;
    }

    public static final String[] str(char[] x) {
        String[] s = new String[x.length];
        int i = 0;
        while (i < x.length) {
            s[i] = String.valueOf((char)x[i]);
            ++i;
        }
        return s;
    }

    public static final String[] str(int[] x) {
        String[] s = new String[x.length];
        int i = 0;
        while (i < x.length) {
            s[i] = String.valueOf((int)x[i]);
            ++i;
        }
        return s;
    }

    public static final String[] str(float[] x) {
        String[] s = new String[x.length];
        int i = 0;
        while (i < x.length) {
            s[i] = String.valueOf((float)x[i]);
            ++i;
        }
        return s;
    }

    public static String nf(float num) {
        int inum = (int)num;
        if (num == (float)inum) {
            return PApplet.str(inum);
        }
        return PApplet.str(num);
    }

    public static String[] nf(float[] nums) {
        String[] outgoing = new String[nums.length];
        int i = 0;
        while (i < nums.length) {
            outgoing[i] = PApplet.nf(nums[i]);
            ++i;
        }
        return outgoing;
    }

    public static String[] nf(int[] nums, int digits) {
        String[] formatted = new String[nums.length];
        int i = 0;
        while (i < formatted.length) {
            formatted[i] = PApplet.nf(nums[i], digits);
            ++i;
        }
        return formatted;
    }

    public static String nf(int num, int digits) {
        if (int_nf != null && int_nf_digits == digits && !int_nf_commas) {
            return int_nf.format((long)num);
        }
        int_nf = NumberFormat.getInstance();
        int_nf.setGroupingUsed(false);
        int_nf_commas = false;
        int_nf.setMinimumIntegerDigits(digits);
        int_nf_digits = digits;
        return int_nf.format((long)num);
    }

    public static String[] nfc(int[] nums) {
        String[] formatted = new String[nums.length];
        int i = 0;
        while (i < formatted.length) {
            formatted[i] = PApplet.nfc(nums[i]);
            ++i;
        }
        return formatted;
    }

    public static String nfc(int num) {
        if (int_nf != null && int_nf_digits == 0 && int_nf_commas) {
            return int_nf.format((long)num);
        }
        int_nf = NumberFormat.getInstance();
        int_nf.setGroupingUsed(true);
        int_nf_commas = true;
        int_nf.setMinimumIntegerDigits(0);
        int_nf_digits = 0;
        return int_nf.format((long)num);
    }

    public static String nfs(int num, int digits) {
        return num < 0 ? PApplet.nf(num, digits) : String.valueOf((char)' ') + PApplet.nf(num, digits);
    }

    public static String[] nfs(int[] nums, int digits) {
        String[] formatted = new String[nums.length];
        int i = 0;
        while (i < formatted.length) {
            formatted[i] = PApplet.nfs(nums[i], digits);
            ++i;
        }
        return formatted;
    }

    public static String nfp(int num, int digits) {
        return num < 0 ? PApplet.nf(num, digits) : String.valueOf((char)'+') + PApplet.nf(num, digits);
    }

    public static String[] nfp(int[] nums, int digits) {
        String[] formatted = new String[nums.length];
        int i = 0;
        while (i < formatted.length) {
            formatted[i] = PApplet.nfp(nums[i], digits);
            ++i;
        }
        return formatted;
    }

    public static String[] nf(float[] nums, int left, int right) {
        String[] formatted = new String[nums.length];
        int i = 0;
        while (i < formatted.length) {
            formatted[i] = PApplet.nf(nums[i], left, right);
            ++i;
        }
        return formatted;
    }

    public static String nf(float num, int left, int right) {
        if (float_nf != null && float_nf_left == left && float_nf_right == right && !float_nf_commas) {
            return float_nf.format((double)num);
        }
        float_nf = NumberFormat.getInstance();
        float_nf.setGroupingUsed(false);
        float_nf_commas = false;
        if (left != 0) {
            float_nf.setMinimumIntegerDigits(left);
        }
        if (right != 0) {
            float_nf.setMinimumFractionDigits(right);
            float_nf.setMaximumFractionDigits(right);
        }
        float_nf_left = left;
        float_nf_right = right;
        return float_nf.format((double)num);
    }

    public static String[] nfc(float[] nums, int right) {
        String[] formatted = new String[nums.length];
        int i = 0;
        while (i < formatted.length) {
            formatted[i] = PApplet.nfc(nums[i], right);
            ++i;
        }
        return formatted;
    }

    public static String nfc(float num, int right) {
        if (float_nf != null && float_nf_left == 0 && float_nf_right == right && float_nf_commas) {
            return float_nf.format((double)num);
        }
        float_nf = NumberFormat.getInstance();
        float_nf.setGroupingUsed(true);
        float_nf_commas = true;
        if (right != 0) {
            float_nf.setMinimumFractionDigits(right);
            float_nf.setMaximumFractionDigits(right);
        }
        float_nf_left = 0;
        float_nf_right = right;
        return float_nf.format((double)num);
    }

    public static String[] nfs(float[] nums, int left, int right) {
        String[] formatted = new String[nums.length];
        int i = 0;
        while (i < formatted.length) {
            formatted[i] = PApplet.nfs(nums[i], left, right);
            ++i;
        }
        return formatted;
    }

    public static String nfs(float num, int left, int right) {
        return num < 0.0f ? PApplet.nf(num, left, right) : String.valueOf((char)' ') + PApplet.nf(num, left, right);
    }

    public static String[] nfp(float[] nums, int left, int right) {
        String[] formatted = new String[nums.length];
        int i = 0;
        while (i < formatted.length) {
            formatted[i] = PApplet.nfp(nums[i], left, right);
            ++i;
        }
        return formatted;
    }

    public static String nfp(float num, int left, int right) {
        return num < 0.0f ? PApplet.nf(num, left, right) : String.valueOf((char)'+') + PApplet.nf(num, left, right);
    }

    public static final String hex(byte value) {
        return PApplet.hex(value, 2);
    }

    public static final String hex(char value) {
        return PApplet.hex(value, 4);
    }

    public static final String hex(int value) {
        return PApplet.hex(value, 8);
    }

    public static final String hex(int value, int digits) {
        int length;
        String stuff = Integer.toHexString((int)value).toUpperCase();
        if (digits > 8) {
            digits = 8;
        }
        if ((length = stuff.length()) > digits) {
            return stuff.substring(length - digits);
        }
        if (length < digits) {
            return String.valueOf((Object)"00000000".substring(8 - (digits - length))) + stuff;
        }
        return stuff;
    }

    public static final int unhex(String value) {
        return (int)Long.parseLong((String)value, (int)16);
    }

    public static final String binary(byte value) {
        return PApplet.binary(value, 8);
    }

    public static final String binary(char value) {
        return PApplet.binary(value, 16);
    }

    public static final String binary(int value) {
        return PApplet.binary(value, 32);
    }

    public static final String binary(int value, int digits) {
        int length;
        String stuff = Integer.toBinaryString((int)value);
        if (digits > 32) {
            digits = 32;
        }
        if ((length = stuff.length()) > digits) {
            return stuff.substring(length - digits);
        }
        if (length < digits) {
            int offset = 32 - (digits - length);
            return String.valueOf((Object)"00000000000000000000000000000000".substring(offset)) + stuff;
        }
        return stuff;
    }

    public static final int unbinary(String value) {
        return Integer.parseInt((String)value, (int)2);
    }

    public final int color(int gray) {
        if (this.g == null) {
            if (gray > 255) {
                gray = 255;
            } else if (gray < 0) {
                gray = 0;
            }
            return 0xFF000000 | gray << 16 | gray << 8 | gray;
        }
        return this.g.color(gray);
    }

    public final int color(float fgray) {
        if (this.g == null) {
            int gray = (int)fgray;
            if (gray > 255) {
                gray = 255;
            } else if (gray < 0) {
                gray = 0;
            }
            return 0xFF000000 | gray << 16 | gray << 8 | gray;
        }
        return this.g.color(fgray);
    }

    public final int color(int gray, int alpha) {
        if (this.g == null) {
            if (alpha > 255) {
                alpha = 255;
            } else if (alpha < 0) {
                alpha = 0;
            }
            if (gray > 255) {
                return alpha << 24 | gray & 0xFFFFFF;
            }
            return alpha << 24 | gray << 16 | gray << 8 | gray;
        }
        return this.g.color(gray, alpha);
    }

    public final int color(float fgray, float falpha) {
        if (this.g == null) {
            int gray = (int)fgray;
            int alpha = (int)falpha;
            if (gray > 255) {
                gray = 255;
            } else if (gray < 0) {
                gray = 0;
            }
            if (alpha > 255) {
                alpha = 255;
            } else if (alpha < 0) {
                alpha = 0;
            }
            return alpha << 24 | gray << 16 | gray << 8 | gray;
        }
        return this.g.color(fgray, falpha);
    }

    public final int color(int v1, int v2, int v3) {
        if (this.g == null) {
            if (v1 > 255) {
                v1 = 255;
            } else if (v1 < 0) {
                v1 = 0;
            }
            if (v2 > 255) {
                v2 = 255;
            } else if (v2 < 0) {
                v2 = 0;
            }
            if (v3 > 255) {
                v3 = 255;
            } else if (v3 < 0) {
                v3 = 0;
            }
            return 0xFF000000 | v1 << 16 | v2 << 8 | v3;
        }
        return this.g.color(v1, v2, v3);
    }

    public final int color(int v1, int v2, int v3, int alpha) {
        if (this.g == null) {
            if (alpha > 255) {
                alpha = 255;
            } else if (alpha < 0) {
                alpha = 0;
            }
            if (v1 > 255) {
                v1 = 255;
            } else if (v1 < 0) {
                v1 = 0;
            }
            if (v2 > 255) {
                v2 = 255;
            } else if (v2 < 0) {
                v2 = 0;
            }
            if (v3 > 255) {
                v3 = 255;
            } else if (v3 < 0) {
                v3 = 0;
            }
            return alpha << 24 | v1 << 16 | v2 << 8 | v3;
        }
        return this.g.color(v1, v2, v3, alpha);
    }

    public final int color(float v1, float v2, float v3) {
        if (this.g == null) {
            if (v1 > 255.0f) {
                v1 = 255.0f;
            } else if (v1 < 0.0f) {
                v1 = 0.0f;
            }
            if (v2 > 255.0f) {
                v2 = 255.0f;
            } else if (v2 < 0.0f) {
                v2 = 0.0f;
            }
            if (v3 > 255.0f) {
                v3 = 255.0f;
            } else if (v3 < 0.0f) {
                v3 = 0.0f;
            }
            return 0xFF000000 | (int)v1 << 16 | (int)v2 << 8 | (int)v3;
        }
        return this.g.color(v1, v2, v3);
    }

    public final int color(float v1, float v2, float v3, float alpha) {
        if (this.g == null) {
            if (alpha > 255.0f) {
                alpha = 255.0f;
            } else if (alpha < 0.0f) {
                alpha = 0.0f;
            }
            if (v1 > 255.0f) {
                v1 = 255.0f;
            } else if (v1 < 0.0f) {
                v1 = 0.0f;
            }
            if (v2 > 255.0f) {
                v2 = 255.0f;
            } else if (v2 < 0.0f) {
                v2 = 0.0f;
            }
            if (v3 > 255.0f) {
                v3 = 255.0f;
            } else if (v3 < 0.0f) {
                v3 = 0.0f;
            }
            return (int)alpha << 24 | (int)v1 << 16 | (int)v2 << 8 | (int)v3;
        }
        return this.g.color(v1, v2, v3, alpha);
    }

    public int lerpColor(int c1, int c2, float amt) {
        if (this.g != null) {
            return this.g.lerpColor(c1, c2, amt);
        }
        return PGraphics.lerpColor((int)c1, (int)c2, (float)amt, (int)1);
    }

    public static int blendColor(int c1, int c2, int mode) {
        return PImage.blendColor((int)c1, (int)c2, (int)mode);
    }

    public void frameMoved(int x, int y) {
        if (!this.fullScreen) {
            System.err.println("__MOVE__ " + x + " " + y);
            System.err.flush();
        }
    }

    public void frameResized(int w, int h) {
    }

    public static void main(String[] args) {
        PApplet.runSketch(args, null);
    }

    public static void main(Class<?> mainClass, String ... args) {
        PApplet.main(mainClass.getName(), args);
    }

    public static void main(String mainClass) {
        PApplet.main(mainClass, null);
    }

    public static void main(String mainClass, String[] sketchArgs) {
        String[] args = new String[]{mainClass};
        if (sketchArgs != null) {
            args = PApplet.concat(args, sketchArgs);
        }
        PApplet.runSketch(args, null);
    }

    public static void runSketch(String[] args, PApplet constructedSketch) {
        PApplet sketch;
        System.setProperty((String)"sun.awt.noerasebackground", (String)"true");
        System.setProperty((String)"javafx.animation.fullspeed", (String)"true");
        Thread.setDefaultUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new /* Unavailable Anonymous Inner Class!! */);
        try {
            Toolkit.getDefaultToolkit().setDynamicLayout(true);
        }
        catch (HeadlessException headlessException) {
            System.err.println("Cannot run sketch without a display. Read this for possible solutions:");
            System.err.println("https://github.com/processing/processing/wiki/Running-without-a-Display");
            System.exit((int)1);
        }
        System.setProperty((String)"java.net.useSystemProxies", (String)"true");
        if (args.length < 1) {
            System.err.println("Usage: PApplet [options] <class name> [sketch args]");
            System.err.println("See the Javadoc for PApplet for an explanation.");
            System.exit((int)1);
        }
        boolean external = false;
        int[] location = null;
        int[] editorLocation = null;
        String name = null;
        int windowColor = 0;
        int stopColor = -8355712;
        boolean hideStop = false;
        int displayNum = -1;
        boolean present = false;
        int density = -1;
        String param = null;
        String value = null;
        String folder = PApplet.calcSketchPath();
        int argIndex = 0;
        while (argIndex < args.length) {
            block31: {
                block29: {
                    block37: {
                        block36: {
                            block35: {
                                block34: {
                                    block33: {
                                        block32: {
                                            block30: {
                                                int equals = args[argIndex].indexOf(61);
                                                if (equals == -1) break block29;
                                                param = args[argIndex].substring(0, equals);
                                                value = args[argIndex].substring(equals + 1);
                                                if (!param.equals((Object)ARGS_EDITOR_LOCATION)) break block30;
                                                external = true;
                                                editorLocation = PApplet.parseInt(PApplet.split(value, ','));
                                                break block31;
                                            }
                                            if (!param.equals((Object)ARGS_DISPLAY)) break block32;
                                            displayNum = PApplet.parseInt(value, -2);
                                            if (displayNum == -2) {
                                                System.err.println(String.valueOf((Object)value) + " is not a valid choice for " + ARGS_DISPLAY);
                                                displayNum = -1;
                                            }
                                            break block31;
                                        }
                                        if (!param.equals((Object)ARGS_WINDOW_COLOR)) break block33;
                                        if (value.charAt(0) == '#' && value.length() == 7) {
                                            value = value.substring(1);
                                            windowColor = 0xFF000000 | Integer.parseInt((String)value, (int)16);
                                        } else {
                                            System.err.println("--window-color should be a # followed by six digits");
                                        }
                                        break block31;
                                    }
                                    if (!param.equals((Object)ARGS_STOP_COLOR)) break block34;
                                    if (value.charAt(0) == '#' && value.length() == 7) {
                                        value = value.substring(1);
                                        stopColor = 0xFF000000 | Integer.parseInt((String)value, (int)16);
                                    } else {
                                        System.err.println("--stop-color should be a # followed by six digits");
                                    }
                                    break block31;
                                }
                                if (!param.equals((Object)ARGS_SKETCH_FOLDER)) break block35;
                                folder = value;
                                break block31;
                            }
                            if (!param.equals((Object)ARGS_LOCATION)) break block36;
                            location = PApplet.parseInt(PApplet.split(value, ','));
                            break block31;
                        }
                        if (!param.equals((Object)ARGS_DENSITY)) break block31;
                        density = PApplet.parseInt(value, -1);
                        if (density != -1) break block37;
                        System.err.println("Could not parse " + value + " for " + ARGS_DENSITY);
                        break block31;
                    }
                    if (density == 1 || density == 2) break block31;
                    density = -1;
                    System.err.println("--density should be 1 or 2");
                    break block31;
                }
                if (args[argIndex].equals((Object)ARGS_PRESENT)) {
                    present = true;
                } else if (args[argIndex].equals((Object)ARGS_HIDE_STOP)) {
                    hideStop = true;
                } else if (args[argIndex].equals((Object)ARGS_EXTERNAL)) {
                    external = true;
                } else {
                    name = args[argIndex];
                    break;
                }
            }
            ++argIndex;
        }
        if (constructedSketch != null) {
            sketch = constructedSketch;
        } else {
            try {
                Class c = Thread.currentThread().getContextClassLoader().loadClass(name);
                sketch = (PApplet)c.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            }
            catch (RuntimeException re) {
                throw re;
            }
            catch (Exception e) {
                throw new RuntimeException((Throwable)e);
            }
        }
        if (platform == 2) {
            try {
                Class thinkDifferent = Thread.currentThread().getContextClassLoader().loadClass("processing.core.ThinkDifferent");
                Method method = thinkDifferent.getMethod("init", new Class[]{PApplet.class});
                method.invoke(null, new Object[]{sketch});
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        sketch.display = displayNum;
        sketch.suggestedDensity = density;
        sketch.present = present;
        sketch.sketchPath = folder;
        if (args.length != argIndex + 1) {
            sketch.args = PApplet.subset(args, argIndex + 1);
        }
        sketch.handleSettings();
        sketch.external = external;
        if (windowColor != 0) {
            sketch.windowColor = windowColor;
        }
        PSurface surface = sketch.initSurface();
        if (present) {
            if (hideStop) {
                stopColor = 0;
            }
            surface.placePresent(stopColor);
        } else {
            surface.placeWindow(location, editorLocation);
        }
        if (sketch.external) {
            surface.setupExternalMessages();
        }
        sketch.showSurface();
        sketch.startSurface();
    }

    protected void showSurface() {
        if (this.getGraphics().displayable()) {
            this.surface.setVisible(true);
        }
    }

    protected void startSurface() {
        this.surface.startThread();
    }

    protected PSurface initSurface() {
        this.g = this.createPrimaryGraphics();
        this.surface = this.g.createSurface();
        if (this.g.displayable()) {
            this.frame = new /* Unavailable Anonymous Inner Class!! */;
            this.surface.initFrame(this);
            this.surface.setTitle(this.getClass().getSimpleName());
        } else {
            this.surface.initOffscreen(this);
        }
        return this.surface;
    }

    public static void hideMenuBar() {
        if (platform == 2) {
            JAppleMenuBar.hide();
        }
    }

    protected void runSketch(String[] args) {
        String cleanedClass;
        String[] argsWithSketchName = new String[args.length + 1];
        System.arraycopy((Object)args, (int)0, (Object)argsWithSketchName, (int)0, (int)args.length);
        String className = this.getClass().getSimpleName();
        argsWithSketchName[args.length] = cleanedClass = className.replaceAll("__[^_]+__\\$", "").replaceAll("\\$\\d+", "");
        PApplet.runSketch(argsWithSketchName, this);
    }

    protected void runSketch() {
        this.runSketch(new String[0]);
    }

    public PGraphics beginRecord(String renderer, String filename) {
        filename = this.insertFrame(filename);
        PGraphics rec = this.createGraphics(this.width, this.height, renderer, filename);
        this.beginRecord(rec);
        return rec;
    }

    public void beginRecord(PGraphics recorder) {
        this.recorder = recorder;
        recorder.beginDraw();
    }

    public void endRecord() {
        if (this.recorder != null) {
            this.recorder.endDraw();
            this.recorder.dispose();
            this.recorder = null;
        }
    }

    public PGraphics beginRaw(String renderer, String filename) {
        filename = this.insertFrame(filename);
        PGraphics rec = this.createGraphics(this.width, this.height, renderer, filename);
        this.g.beginRaw(rec);
        return rec;
    }

    public void beginRaw(PGraphics rawGraphics) {
        this.g.beginRaw(rawGraphics);
    }

    public void endRaw() {
        this.g.endRaw();
    }

    public void loadPixels() {
        this.g.loadPixels();
        this.pixels = this.g.pixels;
    }

    public void updatePixels() {
        this.g.updatePixels();
    }

    public void updatePixels(int x1, int y1, int x2, int y2) {
        this.g.updatePixels(x1, y1, x2, y2);
    }

    public PGL beginPGL() {
        return this.g.beginPGL();
    }

    public void endPGL() {
        if (this.recorder != null) {
            this.recorder.endPGL();
        }
        this.g.endPGL();
    }

    public void flush() {
        if (this.recorder != null) {
            this.recorder.flush();
        }
        this.g.flush();
    }

    public void hint(int which) {
        if (this.recorder != null) {
            this.recorder.hint(which);
        }
        this.g.hint(which);
    }

    public void beginShape() {
        if (this.recorder != null) {
            this.recorder.beginShape();
        }
        this.g.beginShape();
    }

    public void beginShape(int kind) {
        if (this.recorder != null) {
            this.recorder.beginShape(kind);
        }
        this.g.beginShape(kind);
    }

    public void edge(boolean edge) {
        if (this.recorder != null) {
            this.recorder.edge(edge);
        }
        this.g.edge(edge);
    }

    public void normal(float nx, float ny, float nz) {
        if (this.recorder != null) {
            this.recorder.normal(nx, ny, nz);
        }
        this.g.normal(nx, ny, nz);
    }

    public void attribPosition(String name, float x, float y, float z) {
        if (this.recorder != null) {
            this.recorder.attribPosition(name, x, y, z);
        }
        this.g.attribPosition(name, x, y, z);
    }

    public void attribNormal(String name, float nx, float ny, float nz) {
        if (this.recorder != null) {
            this.recorder.attribNormal(name, nx, ny, nz);
        }
        this.g.attribNormal(name, nx, ny, nz);
    }

    public void attribColor(String name, int color) {
        if (this.recorder != null) {
            this.recorder.attribColor(name, color);
        }
        this.g.attribColor(name, color);
    }

    public void attrib(String name, float ... values) {
        if (this.recorder != null) {
            this.recorder.attrib(name, values);
        }
        this.g.attrib(name, values);
    }

    public void attrib(String name, int ... values) {
        if (this.recorder != null) {
            this.recorder.attrib(name, values);
        }
        this.g.attrib(name, values);
    }

    public void attrib(String name, boolean ... values) {
        if (this.recorder != null) {
            this.recorder.attrib(name, values);
        }
        this.g.attrib(name, values);
    }

    public void textureMode(int mode) {
        if (this.recorder != null) {
            this.recorder.textureMode(mode);
        }
        this.g.textureMode(mode);
    }

    public void textureWrap(int wrap) {
        if (this.recorder != null) {
            this.recorder.textureWrap(wrap);
        }
        this.g.textureWrap(wrap);
    }

    public void texture(PImage image) {
        if (this.recorder != null) {
            this.recorder.texture(image);
        }
        this.g.texture(image);
    }

    public void noTexture() {
        if (this.recorder != null) {
            this.recorder.noTexture();
        }
        this.g.noTexture();
    }

    public void vertex(float x, float y) {
        if (this.recorder != null) {
            this.recorder.vertex(x, y);
        }
        this.g.vertex(x, y);
    }

    public void vertex(float x, float y, float z) {
        if (this.recorder != null) {
            this.recorder.vertex(x, y, z);
        }
        this.g.vertex(x, y, z);
    }

    public void vertex(float[] v) {
        if (this.recorder != null) {
            this.recorder.vertex(v);
        }
        this.g.vertex(v);
    }

    public void vertex(float x, float y, float u, float v) {
        if (this.recorder != null) {
            this.recorder.vertex(x, y, u, v);
        }
        this.g.vertex(x, y, u, v);
    }

    public void vertex(float x, float y, float z, float u, float v) {
        if (this.recorder != null) {
            this.recorder.vertex(x, y, z, u, v);
        }
        this.g.vertex(x, y, z, u, v);
    }

    public void beginContour() {
        if (this.recorder != null) {
            this.recorder.beginContour();
        }
        this.g.beginContour();
    }

    public void endContour() {
        if (this.recorder != null) {
            this.recorder.endContour();
        }
        this.g.endContour();
    }

    public void endShape() {
        if (this.recorder != null) {
            this.recorder.endShape();
        }
        this.g.endShape();
    }

    public void endShape(int mode) {
        if (this.recorder != null) {
            this.recorder.endShape(mode);
        }
        this.g.endShape(mode);
    }

    public PShape loadShape(String filename) {
        return this.g.loadShape(filename);
    }

    public PShape loadShape(String filename, String options) {
        return this.g.loadShape(filename, options);
    }

    public PShape createShape() {
        return this.g.createShape();
    }

    public PShape createShape(int type) {
        return this.g.createShape(type);
    }

    public PShape createShape(int kind, float ... p) {
        return this.g.createShape(kind, p);
    }

    public PShader loadShader(String fragFilename) {
        return this.g.loadShader(fragFilename);
    }

    public PShader loadShader(String fragFilename, String vertFilename) {
        return this.g.loadShader(fragFilename, vertFilename);
    }

    public void shader(PShader shader) {
        if (this.recorder != null) {
            this.recorder.shader(shader);
        }
        this.g.shader(shader);
    }

    public void shader(PShader shader, int kind) {
        if (this.recorder != null) {
            this.recorder.shader(shader, kind);
        }
        this.g.shader(shader, kind);
    }

    public void resetShader() {
        if (this.recorder != null) {
            this.recorder.resetShader();
        }
        this.g.resetShader();
    }

    public void resetShader(int kind) {
        if (this.recorder != null) {
            this.recorder.resetShader(kind);
        }
        this.g.resetShader(kind);
    }

    public void filter(PShader shader) {
        if (this.recorder != null) {
            this.recorder.filter(shader);
        }
        this.g.filter(shader);
    }

    public void clip(float a, float b, float c, float d) {
        if (this.recorder != null) {
            this.recorder.clip(a, b, c, d);
        }
        this.g.clip(a, b, c, d);
    }

    public void noClip() {
        if (this.recorder != null) {
            this.recorder.noClip();
        }
        this.g.noClip();
    }

    public void blendMode(int mode) {
        if (this.recorder != null) {
            this.recorder.blendMode(mode);
        }
        this.g.blendMode(mode);
    }

    public void bezierVertex(float x2, float y2, float x3, float y3, float x4, float y4) {
        if (this.recorder != null) {
            this.recorder.bezierVertex(x2, y2, x3, y3, x4, y4);
        }
        this.g.bezierVertex(x2, y2, x3, y3, x4, y4);
    }

    public void bezierVertex(float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
        if (this.recorder != null) {
            this.recorder.bezierVertex(x2, y2, z2, x3, y3, z3, x4, y4, z4);
        }
        this.g.bezierVertex(x2, y2, z2, x3, y3, z3, x4, y4, z4);
    }

    public void quadraticVertex(float cx, float cy, float x3, float y3) {
        if (this.recorder != null) {
            this.recorder.quadraticVertex(cx, cy, x3, y3);
        }
        this.g.quadraticVertex(cx, cy, x3, y3);
    }

    public void quadraticVertex(float cx, float cy, float cz, float x3, float y3, float z3) {
        if (this.recorder != null) {
            this.recorder.quadraticVertex(cx, cy, cz, x3, y3, z3);
        }
        this.g.quadraticVertex(cx, cy, cz, x3, y3, z3);
    }

    public void curveVertex(float x, float y) {
        if (this.recorder != null) {
            this.recorder.curveVertex(x, y);
        }
        this.g.curveVertex(x, y);
    }

    public void curveVertex(float x, float y, float z) {
        if (this.recorder != null) {
            this.recorder.curveVertex(x, y, z);
        }
        this.g.curveVertex(x, y, z);
    }

    public void point(float x, float y) {
        if (this.recorder != null) {
            this.recorder.point(x, y);
        }
        this.g.point(x, y);
    }

    public void point(float x, float y, float z) {
        if (this.recorder != null) {
            this.recorder.point(x, y, z);
        }
        this.g.point(x, y, z);
    }

    public void line(float x1, float y1, float x2, float y2) {
        if (this.recorder != null) {
            this.recorder.line(x1, y1, x2, y2);
        }
        this.g.line(x1, y1, x2, y2);
    }

    public void line(float x1, float y1, float z1, float x2, float y2, float z2) {
        if (this.recorder != null) {
            this.recorder.line(x1, y1, z1, x2, y2, z2);
        }
        this.g.line(x1, y1, z1, x2, y2, z2);
    }

    public void triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        if (this.recorder != null) {
            this.recorder.triangle(x1, y1, x2, y2, x3, y3);
        }
        this.g.triangle(x1, y1, x2, y2, x3, y3);
    }

    public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        if (this.recorder != null) {
            this.recorder.quad(x1, y1, x2, y2, x3, y3, x4, y4);
        }
        this.g.quad(x1, y1, x2, y2, x3, y3, x4, y4);
    }

    public void rectMode(int mode) {
        if (this.recorder != null) {
            this.recorder.rectMode(mode);
        }
        this.g.rectMode(mode);
    }

    public void rect(float a, float b, float c, float d) {
        if (this.recorder != null) {
            this.recorder.rect(a, b, c, d);
        }
        this.g.rect(a, b, c, d);
    }

    public void rect(float a, float b, float c, float d, float r) {
        if (this.recorder != null) {
            this.recorder.rect(a, b, c, d, r);
        }
        this.g.rect(a, b, c, d, r);
    }

    public void rect(float a, float b, float c, float d, float tl, float tr, float br, float bl) {
        if (this.recorder != null) {
            this.recorder.rect(a, b, c, d, tl, tr, br, bl);
        }
        this.g.rect(a, b, c, d, tl, tr, br, bl);
    }

    public void square(float x, float y, float extent) {
        if (this.recorder != null) {
            this.recorder.square(x, y, extent);
        }
        this.g.square(x, y, extent);
    }

    public void ellipseMode(int mode) {
        if (this.recorder != null) {
            this.recorder.ellipseMode(mode);
        }
        this.g.ellipseMode(mode);
    }

    public void ellipse(float a, float b, float c, float d) {
        if (this.recorder != null) {
            this.recorder.ellipse(a, b, c, d);
        }
        this.g.ellipse(a, b, c, d);
    }

    public void arc(float a, float b, float c, float d, float start, float stop) {
        if (this.recorder != null) {
            this.recorder.arc(a, b, c, d, start, stop);
        }
        this.g.arc(a, b, c, d, start, stop);
    }

    public void arc(float a, float b, float c, float d, float start, float stop, int mode) {
        if (this.recorder != null) {
            this.recorder.arc(a, b, c, d, start, stop, mode);
        }
        this.g.arc(a, b, c, d, start, stop, mode);
    }

    public void circle(float x, float y, float extent) {
        if (this.recorder != null) {
            this.recorder.circle(x, y, extent);
        }
        this.g.circle(x, y, extent);
    }

    public void box(float size) {
        if (this.recorder != null) {
            this.recorder.box(size);
        }
        this.g.box(size);
    }

    public void box(float w, float h, float d) {
        if (this.recorder != null) {
            this.recorder.box(w, h, d);
        }
        this.g.box(w, h, d);
    }

    public void sphereDetail(int res) {
        if (this.recorder != null) {
            this.recorder.sphereDetail(res);
        }
        this.g.sphereDetail(res);
    }

    public void sphereDetail(int ures, int vres) {
        if (this.recorder != null) {
            this.recorder.sphereDetail(ures, vres);
        }
        this.g.sphereDetail(ures, vres);
    }

    public void sphere(float r) {
        if (this.recorder != null) {
            this.recorder.sphere(r);
        }
        this.g.sphere(r);
    }

    public float bezierPoint(float a, float b, float c, float d, float t) {
        return this.g.bezierPoint(a, b, c, d, t);
    }

    public float bezierTangent(float a, float b, float c, float d, float t) {
        return this.g.bezierTangent(a, b, c, d, t);
    }

    public void bezierDetail(int detail) {
        if (this.recorder != null) {
            this.recorder.bezierDetail(detail);
        }
        this.g.bezierDetail(detail);
    }

    public void bezier(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        if (this.recorder != null) {
            this.recorder.bezier(x1, y1, x2, y2, x3, y3, x4, y4);
        }
        this.g.bezier(x1, y1, x2, y2, x3, y3, x4, y4);
    }

    public void bezier(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
        if (this.recorder != null) {
            this.recorder.bezier(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
        }
        this.g.bezier(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
    }

    public float curvePoint(float a, float b, float c, float d, float t) {
        return this.g.curvePoint(a, b, c, d, t);
    }

    public float curveTangent(float a, float b, float c, float d, float t) {
        return this.g.curveTangent(a, b, c, d, t);
    }

    public void curveDetail(int detail) {
        if (this.recorder != null) {
            this.recorder.curveDetail(detail);
        }
        this.g.curveDetail(detail);
    }

    public void curveTightness(float tightness) {
        if (this.recorder != null) {
            this.recorder.curveTightness(tightness);
        }
        this.g.curveTightness(tightness);
    }

    public void curve(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        if (this.recorder != null) {
            this.recorder.curve(x1, y1, x2, y2, x3, y3, x4, y4);
        }
        this.g.curve(x1, y1, x2, y2, x3, y3, x4, y4);
    }

    public void curve(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
        if (this.recorder != null) {
            this.recorder.curve(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
        }
        this.g.curve(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
    }

    public void imageMode(int mode) {
        if (this.recorder != null) {
            this.recorder.imageMode(mode);
        }
        this.g.imageMode(mode);
    }

    public void image(PImage img, float a, float b) {
        if (this.recorder != null) {
            this.recorder.image(img, a, b);
        }
        this.g.image(img, a, b);
    }

    public void image(PImage img, float a, float b, float c, float d) {
        if (this.recorder != null) {
            this.recorder.image(img, a, b, c, d);
        }
        this.g.image(img, a, b, c, d);
    }

    public void image(PImage img, float a, float b, float c, float d, int u1, int v1, int u2, int v2) {
        if (this.recorder != null) {
            this.recorder.image(img, a, b, c, d, u1, v1, u2, v2);
        }
        this.g.image(img, a, b, c, d, u1, v1, u2, v2);
    }

    public void shapeMode(int mode) {
        if (this.recorder != null) {
            this.recorder.shapeMode(mode);
        }
        this.g.shapeMode(mode);
    }

    public void shape(PShape shape) {
        if (this.recorder != null) {
            this.recorder.shape(shape);
        }
        this.g.shape(shape);
    }

    public void shape(PShape shape, float x, float y) {
        if (this.recorder != null) {
            this.recorder.shape(shape, x, y);
        }
        this.g.shape(shape, x, y);
    }

    public void shape(PShape shape, float a, float b, float c, float d) {
        if (this.recorder != null) {
            this.recorder.shape(shape, a, b, c, d);
        }
        this.g.shape(shape, a, b, c, d);
    }

    public void textAlign(int alignX) {
        if (this.recorder != null) {
            this.recorder.textAlign(alignX);
        }
        this.g.textAlign(alignX);
    }

    public void textAlign(int alignX, int alignY) {
        if (this.recorder != null) {
            this.recorder.textAlign(alignX, alignY);
        }
        this.g.textAlign(alignX, alignY);
    }

    public float textAscent() {
        return this.g.textAscent();
    }

    public float textDescent() {
        return this.g.textDescent();
    }

    public void textFont(PFont which) {
        if (this.recorder != null) {
            this.recorder.textFont(which);
        }
        this.g.textFont(which);
    }

    public void textFont(PFont which, float size) {
        if (this.recorder != null) {
            this.recorder.textFont(which, size);
        }
        this.g.textFont(which, size);
    }

    public void textLeading(float leading) {
        if (this.recorder != null) {
            this.recorder.textLeading(leading);
        }
        this.g.textLeading(leading);
    }

    public void textMode(int mode) {
        if (this.recorder != null) {
            this.recorder.textMode(mode);
        }
        this.g.textMode(mode);
    }

    public void textSize(float size) {
        if (this.recorder != null) {
            this.recorder.textSize(size);
        }
        this.g.textSize(size);
    }

    public float textWidth(char c) {
        return this.g.textWidth(c);
    }

    public float textWidth(String str) {
        return this.g.textWidth(str);
    }

    public float textWidth(char[] chars, int start, int length) {
        return this.g.textWidth(chars, start, length);
    }

    public void text(char c, float x, float y) {
        if (this.recorder != null) {
            this.recorder.text(c, x, y);
        }
        this.g.text(c, x, y);
    }

    public void text(char c, float x, float y, float z) {
        if (this.recorder != null) {
            this.recorder.text(c, x, y, z);
        }
        this.g.text(c, x, y, z);
    }

    public void text(String str, float x, float y) {
        if (this.recorder != null) {
            this.recorder.text(str, x, y);
        }
        this.g.text(str, x, y);
    }

    public void text(char[] chars, int start, int stop, float x, float y) {
        if (this.recorder != null) {
            this.recorder.text(chars, start, stop, x, y);
        }
        this.g.text(chars, start, stop, x, y);
    }

    public void text(String str, float x, float y, float z) {
        if (this.recorder != null) {
            this.recorder.text(str, x, y, z);
        }
        this.g.text(str, x, y, z);
    }

    public void text(char[] chars, int start, int stop, float x, float y, float z) {
        if (this.recorder != null) {
            this.recorder.text(chars, start, stop, x, y, z);
        }
        this.g.text(chars, start, stop, x, y, z);
    }

    public void text(String str, float x1, float y1, float x2, float y2) {
        if (this.recorder != null) {
            this.recorder.text(str, x1, y1, x2, y2);
        }
        this.g.text(str, x1, y1, x2, y2);
    }

    public void text(int num, float x, float y) {
        if (this.recorder != null) {
            this.recorder.text(num, x, y);
        }
        this.g.text(num, x, y);
    }

    public void text(int num, float x, float y, float z) {
        if (this.recorder != null) {
            this.recorder.text(num, x, y, z);
        }
        this.g.text(num, x, y, z);
    }

    public void text(float num, float x, float y) {
        if (this.recorder != null) {
            this.recorder.text(num, x, y);
        }
        this.g.text(num, x, y);
    }

    public void text(float num, float x, float y, float z) {
        if (this.recorder != null) {
            this.recorder.text(num, x, y, z);
        }
        this.g.text(num, x, y, z);
    }

    public void push() {
        if (this.recorder != null) {
            this.recorder.push();
        }
        this.g.push();
    }

    public void pop() {
        if (this.recorder != null) {
            this.recorder.pop();
        }
        this.g.pop();
    }

    public void pushMatrix() {
        if (this.recorder != null) {
            this.recorder.pushMatrix();
        }
        this.g.pushMatrix();
    }

    public void popMatrix() {
        if (this.recorder != null) {
            this.recorder.popMatrix();
        }
        this.g.popMatrix();
    }

    public void translate(float x, float y) {
        if (this.recorder != null) {
            this.recorder.translate(x, y);
        }
        this.g.translate(x, y);
    }

    public void translate(float x, float y, float z) {
        if (this.recorder != null) {
            this.recorder.translate(x, y, z);
        }
        this.g.translate(x, y, z);
    }

    public void rotate(float angle) {
        if (this.recorder != null) {
            this.recorder.rotate(angle);
        }
        this.g.rotate(angle);
    }

    public void rotateX(float angle) {
        if (this.recorder != null) {
            this.recorder.rotateX(angle);
        }
        this.g.rotateX(angle);
    }

    public void rotateY(float angle) {
        if (this.recorder != null) {
            this.recorder.rotateY(angle);
        }
        this.g.rotateY(angle);
    }

    public void rotateZ(float angle) {
        if (this.recorder != null) {
            this.recorder.rotateZ(angle);
        }
        this.g.rotateZ(angle);
    }

    public void rotate(float angle, float x, float y, float z) {
        if (this.recorder != null) {
            this.recorder.rotate(angle, x, y, z);
        }
        this.g.rotate(angle, x, y, z);
    }

    public void scale(float s) {
        if (this.recorder != null) {
            this.recorder.scale(s);
        }
        this.g.scale(s);
    }

    public void scale(float x, float y) {
        if (this.recorder != null) {
            this.recorder.scale(x, y);
        }
        this.g.scale(x, y);
    }

    public void scale(float x, float y, float z) {
        if (this.recorder != null) {
            this.recorder.scale(x, y, z);
        }
        this.g.scale(x, y, z);
    }

    public void shearX(float angle) {
        if (this.recorder != null) {
            this.recorder.shearX(angle);
        }
        this.g.shearX(angle);
    }

    public void shearY(float angle) {
        if (this.recorder != null) {
            this.recorder.shearY(angle);
        }
        this.g.shearY(angle);
    }

    public void resetMatrix() {
        if (this.recorder != null) {
            this.recorder.resetMatrix();
        }
        this.g.resetMatrix();
    }

    public void applyMatrix(PMatrix source) {
        if (this.recorder != null) {
            this.recorder.applyMatrix(source);
        }
        this.g.applyMatrix(source);
    }

    public void applyMatrix(PMatrix2D source) {
        if (this.recorder != null) {
            this.recorder.applyMatrix(source);
        }
        this.g.applyMatrix(source);
    }

    public void applyMatrix(float n00, float n01, float n02, float n10, float n11, float n12) {
        if (this.recorder != null) {
            this.recorder.applyMatrix(n00, n01, n02, n10, n11, n12);
        }
        this.g.applyMatrix(n00, n01, n02, n10, n11, n12);
    }

    public void applyMatrix(PMatrix3D source) {
        if (this.recorder != null) {
            this.recorder.applyMatrix(source);
        }
        this.g.applyMatrix(source);
    }

    public void applyMatrix(float n00, float n01, float n02, float n03, float n10, float n11, float n12, float n13, float n20, float n21, float n22, float n23, float n30, float n31, float n32, float n33) {
        if (this.recorder != null) {
            this.recorder.applyMatrix(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22, n23, n30, n31, n32, n33);
        }
        this.g.applyMatrix(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22, n23, n30, n31, n32, n33);
    }

    public PMatrix getMatrix() {
        return this.g.getMatrix();
    }

    public PMatrix2D getMatrix(PMatrix2D target) {
        return this.g.getMatrix(target);
    }

    public PMatrix3D getMatrix(PMatrix3D target) {
        return this.g.getMatrix(target);
    }

    public void setMatrix(PMatrix source) {
        if (this.recorder != null) {
            this.recorder.setMatrix(source);
        }
        this.g.setMatrix(source);
    }

    public void setMatrix(PMatrix2D source) {
        if (this.recorder != null) {
            this.recorder.setMatrix(source);
        }
        this.g.setMatrix(source);
    }

    public void setMatrix(PMatrix3D source) {
        if (this.recorder != null) {
            this.recorder.setMatrix(source);
        }
        this.g.setMatrix(source);
    }

    public void printMatrix() {
        if (this.recorder != null) {
            this.recorder.printMatrix();
        }
        this.g.printMatrix();
    }

    public void beginCamera() {
        if (this.recorder != null) {
            this.recorder.beginCamera();
        }
        this.g.beginCamera();
    }

    public void endCamera() {
        if (this.recorder != null) {
            this.recorder.endCamera();
        }
        this.g.endCamera();
    }

    public void camera() {
        if (this.recorder != null) {
            this.recorder.camera();
        }
        this.g.camera();
    }

    public void camera(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ) {
        if (this.recorder != null) {
            this.recorder.camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
        }
        this.g.camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    public void printCamera() {
        if (this.recorder != null) {
            this.recorder.printCamera();
        }
        this.g.printCamera();
    }

    public void ortho() {
        if (this.recorder != null) {
            this.recorder.ortho();
        }
        this.g.ortho();
    }

    public void ortho(float left, float right, float bottom, float top) {
        if (this.recorder != null) {
            this.recorder.ortho(left, right, bottom, top);
        }
        this.g.ortho(left, right, bottom, top);
    }

    public void ortho(float left, float right, float bottom, float top, float near, float far) {
        if (this.recorder != null) {
            this.recorder.ortho(left, right, bottom, top, near, far);
        }
        this.g.ortho(left, right, bottom, top, near, far);
    }

    public void perspective() {
        if (this.recorder != null) {
            this.recorder.perspective();
        }
        this.g.perspective();
    }

    public void perspective(float fovy, float aspect, float zNear, float zFar) {
        if (this.recorder != null) {
            this.recorder.perspective(fovy, aspect, zNear, zFar);
        }
        this.g.perspective(fovy, aspect, zNear, zFar);
    }

    public void frustum(float left, float right, float bottom, float top, float near, float far) {
        if (this.recorder != null) {
            this.recorder.frustum(left, right, bottom, top, near, far);
        }
        this.g.frustum(left, right, bottom, top, near, far);
    }

    public void printProjection() {
        if (this.recorder != null) {
            this.recorder.printProjection();
        }
        this.g.printProjection();
    }

    public float screenX(float x, float y) {
        return this.g.screenX(x, y);
    }

    public float screenY(float x, float y) {
        return this.g.screenY(x, y);
    }

    public float screenX(float x, float y, float z) {
        return this.g.screenX(x, y, z);
    }

    public float screenY(float x, float y, float z) {
        return this.g.screenY(x, y, z);
    }

    public float screenZ(float x, float y, float z) {
        return this.g.screenZ(x, y, z);
    }

    public float modelX(float x, float y, float z) {
        return this.g.modelX(x, y, z);
    }

    public float modelY(float x, float y, float z) {
        return this.g.modelY(x, y, z);
    }

    public float modelZ(float x, float y, float z) {
        return this.g.modelZ(x, y, z);
    }

    public void pushStyle() {
        if (this.recorder != null) {
            this.recorder.pushStyle();
        }
        this.g.pushStyle();
    }

    public void popStyle() {
        if (this.recorder != null) {
            this.recorder.popStyle();
        }
        this.g.popStyle();
    }

    public void style(PStyle s) {
        if (this.recorder != null) {
            this.recorder.style(s);
        }
        this.g.style(s);
    }

    public void strokeWeight(float weight) {
        if (this.recorder != null) {
            this.recorder.strokeWeight(weight);
        }
        this.g.strokeWeight(weight);
    }

    public void strokeJoin(int join) {
        if (this.recorder != null) {
            this.recorder.strokeJoin(join);
        }
        this.g.strokeJoin(join);
    }

    public void strokeCap(int cap) {
        if (this.recorder != null) {
            this.recorder.strokeCap(cap);
        }
        this.g.strokeCap(cap);
    }

    public void noStroke() {
        if (this.recorder != null) {
            this.recorder.noStroke();
        }
        this.g.noStroke();
    }

    public void stroke(int rgb) {
        if (this.recorder != null) {
            this.recorder.stroke(rgb);
        }
        this.g.stroke(rgb);
    }

    public void stroke(int rgb, float alpha) {
        if (this.recorder != null) {
            this.recorder.stroke(rgb, alpha);
        }
        this.g.stroke(rgb, alpha);
    }

    public void stroke(float gray) {
        if (this.recorder != null) {
            this.recorder.stroke(gray);
        }
        this.g.stroke(gray);
    }

    public void stroke(float gray, float alpha) {
        if (this.recorder != null) {
            this.recorder.stroke(gray, alpha);
        }
        this.g.stroke(gray, alpha);
    }

    public void stroke(float v1, float v2, float v3) {
        if (this.recorder != null) {
            this.recorder.stroke(v1, v2, v3);
        }
        this.g.stroke(v1, v2, v3);
    }

    public void stroke(float v1, float v2, float v3, float alpha) {
        if (this.recorder != null) {
            this.recorder.stroke(v1, v2, v3, alpha);
        }
        this.g.stroke(v1, v2, v3, alpha);
    }

    public void noTint() {
        if (this.recorder != null) {
            this.recorder.noTint();
        }
        this.g.noTint();
    }

    public void tint(int rgb) {
        if (this.recorder != null) {
            this.recorder.tint(rgb);
        }
        this.g.tint(rgb);
    }

    public void tint(int rgb, float alpha) {
        if (this.recorder != null) {
            this.recorder.tint(rgb, alpha);
        }
        this.g.tint(rgb, alpha);
    }

    public void tint(float gray) {
        if (this.recorder != null) {
            this.recorder.tint(gray);
        }
        this.g.tint(gray);
    }

    public void tint(float gray, float alpha) {
        if (this.recorder != null) {
            this.recorder.tint(gray, alpha);
        }
        this.g.tint(gray, alpha);
    }

    public void tint(float v1, float v2, float v3) {
        if (this.recorder != null) {
            this.recorder.tint(v1, v2, v3);
        }
        this.g.tint(v1, v2, v3);
    }

    public void tint(float v1, float v2, float v3, float alpha) {
        if (this.recorder != null) {
            this.recorder.tint(v1, v2, v3, alpha);
        }
        this.g.tint(v1, v2, v3, alpha);
    }

    public void noFill() {
        if (this.recorder != null) {
            this.recorder.noFill();
        }
        this.g.noFill();
    }

    public void fill(int rgb) {
        if (this.recorder != null) {
            this.recorder.fill(rgb);
        }
        this.g.fill(rgb);
    }

    public void fill(int rgb, float alpha) {
        if (this.recorder != null) {
            this.recorder.fill(rgb, alpha);
        }
        this.g.fill(rgb, alpha);
    }

    public void fill(float gray) {
        if (this.recorder != null) {
            this.recorder.fill(gray);
        }
        this.g.fill(gray);
    }

    public void fill(float gray, float alpha) {
        if (this.recorder != null) {
            this.recorder.fill(gray, alpha);
        }
        this.g.fill(gray, alpha);
    }

    public void fill(float v1, float v2, float v3) {
        if (this.recorder != null) {
            this.recorder.fill(v1, v2, v3);
        }
        this.g.fill(v1, v2, v3);
    }

    public void fill(float v1, float v2, float v3, float alpha) {
        if (this.recorder != null) {
            this.recorder.fill(v1, v2, v3, alpha);
        }
        this.g.fill(v1, v2, v3, alpha);
    }

    public void ambient(int rgb) {
        if (this.recorder != null) {
            this.recorder.ambient(rgb);
        }
        this.g.ambient(rgb);
    }

    public void ambient(float gray) {
        if (this.recorder != null) {
            this.recorder.ambient(gray);
        }
        this.g.ambient(gray);
    }

    public void ambient(float v1, float v2, float v3) {
        if (this.recorder != null) {
            this.recorder.ambient(v1, v2, v3);
        }
        this.g.ambient(v1, v2, v3);
    }

    public void specular(int rgb) {
        if (this.recorder != null) {
            this.recorder.specular(rgb);
        }
        this.g.specular(rgb);
    }

    public void specular(float gray) {
        if (this.recorder != null) {
            this.recorder.specular(gray);
        }
        this.g.specular(gray);
    }

    public void specular(float v1, float v2, float v3) {
        if (this.recorder != null) {
            this.recorder.specular(v1, v2, v3);
        }
        this.g.specular(v1, v2, v3);
    }

    public void shininess(float shine) {
        if (this.recorder != null) {
            this.recorder.shininess(shine);
        }
        this.g.shininess(shine);
    }

    public void emissive(int rgb) {
        if (this.recorder != null) {
            this.recorder.emissive(rgb);
        }
        this.g.emissive(rgb);
    }

    public void emissive(float gray) {
        if (this.recorder != null) {
            this.recorder.emissive(gray);
        }
        this.g.emissive(gray);
    }

    public void emissive(float v1, float v2, float v3) {
        if (this.recorder != null) {
            this.recorder.emissive(v1, v2, v3);
        }
        this.g.emissive(v1, v2, v3);
    }

    public void lights() {
        if (this.recorder != null) {
            this.recorder.lights();
        }
        this.g.lights();
    }

    public void noLights() {
        if (this.recorder != null) {
            this.recorder.noLights();
        }
        this.g.noLights();
    }

    public void ambientLight(float v1, float v2, float v3) {
        if (this.recorder != null) {
            this.recorder.ambientLight(v1, v2, v3);
        }
        this.g.ambientLight(v1, v2, v3);
    }

    public void ambientLight(float v1, float v2, float v3, float x, float y, float z) {
        if (this.recorder != null) {
            this.recorder.ambientLight(v1, v2, v3, x, y, z);
        }
        this.g.ambientLight(v1, v2, v3, x, y, z);
    }

    public void directionalLight(float v1, float v2, float v3, float nx, float ny, float nz) {
        if (this.recorder != null) {
            this.recorder.directionalLight(v1, v2, v3, nx, ny, nz);
        }
        this.g.directionalLight(v1, v2, v3, nx, ny, nz);
    }

    public void pointLight(float v1, float v2, float v3, float x, float y, float z) {
        if (this.recorder != null) {
            this.recorder.pointLight(v1, v2, v3, x, y, z);
        }
        this.g.pointLight(v1, v2, v3, x, y, z);
    }

    public void spotLight(float v1, float v2, float v3, float x, float y, float z, float nx, float ny, float nz, float angle, float concentration) {
        if (this.recorder != null) {
            this.recorder.spotLight(v1, v2, v3, x, y, z, nx, ny, nz, angle, concentration);
        }
        this.g.spotLight(v1, v2, v3, x, y, z, nx, ny, nz, angle, concentration);
    }

    public void lightFalloff(float constant, float linear, float quadratic) {
        if (this.recorder != null) {
            this.recorder.lightFalloff(constant, linear, quadratic);
        }
        this.g.lightFalloff(constant, linear, quadratic);
    }

    public void lightSpecular(float v1, float v2, float v3) {
        if (this.recorder != null) {
            this.recorder.lightSpecular(v1, v2, v3);
        }
        this.g.lightSpecular(v1, v2, v3);
    }

    public void background(int rgb) {
        if (this.recorder != null) {
            this.recorder.background(rgb);
        }
        this.g.background(rgb);
    }

    public void background(int rgb, float alpha) {
        if (this.recorder != null) {
            this.recorder.background(rgb, alpha);
        }
        this.g.background(rgb, alpha);
    }

    public void background(float gray) {
        if (this.recorder != null) {
            this.recorder.background(gray);
        }
        this.g.background(gray);
    }

    public void background(float gray, float alpha) {
        if (this.recorder != null) {
            this.recorder.background(gray, alpha);
        }
        this.g.background(gray, alpha);
    }

    public void background(float v1, float v2, float v3) {
        if (this.recorder != null) {
            this.recorder.background(v1, v2, v3);
        }
        this.g.background(v1, v2, v3);
    }

    public void background(float v1, float v2, float v3, float alpha) {
        if (this.recorder != null) {
            this.recorder.background(v1, v2, v3, alpha);
        }
        this.g.background(v1, v2, v3, alpha);
    }

    public void clear() {
        if (this.recorder != null) {
            this.recorder.clear();
        }
        this.g.clear();
    }

    public void background(PImage image) {
        if (this.recorder != null) {
            this.recorder.background(image);
        }
        this.g.background(image);
    }

    public void colorMode(int mode) {
        if (this.recorder != null) {
            this.recorder.colorMode(mode);
        }
        this.g.colorMode(mode);
    }

    public void colorMode(int mode, float max) {
        if (this.recorder != null) {
            this.recorder.colorMode(mode, max);
        }
        this.g.colorMode(mode, max);
    }

    public void colorMode(int mode, float max1, float max2, float max3) {
        if (this.recorder != null) {
            this.recorder.colorMode(mode, max1, max2, max3);
        }
        this.g.colorMode(mode, max1, max2, max3);
    }

    public void colorMode(int mode, float max1, float max2, float max3, float maxA) {
        if (this.recorder != null) {
            this.recorder.colorMode(mode, max1, max2, max3, maxA);
        }
        this.g.colorMode(mode, max1, max2, max3, maxA);
    }

    public final float alpha(int rgb) {
        return this.g.alpha(rgb);
    }

    public final float red(int rgb) {
        return this.g.red(rgb);
    }

    public final float green(int rgb) {
        return this.g.green(rgb);
    }

    public final float blue(int rgb) {
        return this.g.blue(rgb);
    }

    public final float hue(int rgb) {
        return this.g.hue(rgb);
    }

    public final float saturation(int rgb) {
        return this.g.saturation(rgb);
    }

    public final float brightness(int rgb) {
        return this.g.brightness(rgb);
    }

    public static int lerpColor(int c1, int c2, float amt, int mode) {
        return PGraphics.lerpColor((int)c1, (int)c2, (float)amt, (int)mode);
    }

    public static void showDepthWarning(String method) {
        PGraphics.showDepthWarning((String)method);
    }

    public static void showDepthWarningXYZ(String method) {
        PGraphics.showDepthWarningXYZ((String)method);
    }

    public static void showMethodWarning(String method) {
        PGraphics.showMethodWarning((String)method);
    }

    public static void showVariationWarning(String str) {
        PGraphics.showVariationWarning((String)str);
    }

    public static void showMissingWarning(String method) {
        PGraphics.showMissingWarning((String)method);
    }

    public int get(int x, int y) {
        return this.g.get(x, y);
    }

    public PImage get(int x, int y, int w, int h) {
        return this.g.get(x, y, w, h);
    }

    public PImage get() {
        return this.g.get();
    }

    public PImage copy() {
        return this.g.copy();
    }

    public void set(int x, int y, int c) {
        if (this.recorder != null) {
            this.recorder.set(x, y, c);
        }
        this.g.set(x, y, c);
    }

    public void set(int x, int y, PImage img) {
        if (this.recorder != null) {
            this.recorder.set(x, y, img);
        }
        this.g.set(x, y, img);
    }

    public void mask(PImage img) {
        if (this.recorder != null) {
            this.recorder.mask(img);
        }
        this.g.mask(img);
    }

    public void filter(int kind) {
        if (this.recorder != null) {
            this.recorder.filter(kind);
        }
        this.g.filter(kind);
    }

    public void filter(int kind, float param) {
        if (this.recorder != null) {
            this.recorder.filter(kind, param);
        }
        this.g.filter(kind, param);
    }

    public void copy(int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh) {
        if (this.recorder != null) {
            this.recorder.copy(sx, sy, sw, sh, dx, dy, dw, dh);
        }
        this.g.copy(sx, sy, sw, sh, dx, dy, dw, dh);
    }

    public void copy(PImage src, int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh) {
        if (this.recorder != null) {
            this.recorder.copy(src, sx, sy, sw, sh, dx, dy, dw, dh);
        }
        this.g.copy(src, sx, sy, sw, sh, dx, dy, dw, dh);
    }

    public void blend(int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh, int mode) {
        if (this.recorder != null) {
            this.recorder.blend(sx, sy, sw, sh, dx, dy, dw, dh, mode);
        }
        this.g.blend(sx, sy, sw, sh, dx, dy, dw, dh, mode);
    }

    public void blend(PImage src, int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh, int mode) {
        if (this.recorder != null) {
            this.recorder.blend(src, sx, sy, sw, sh, dx, dy, dw, dh, mode);
        }
        this.g.blend(src, sx, sy, sw, sh, dx, dy, dw, dh, mode);
    }

    static /* synthetic */ void access$0() {
        PApplet.checkLookAndFeel();
    }

    static /* synthetic */ void access$1(File file, String string, Object object) {
        PApplet.selectCallback(file, string, object);
    }
}
