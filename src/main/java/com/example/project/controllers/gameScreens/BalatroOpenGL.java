package com.example.project.controllers.gameScreens;


import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import java.nio.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class BalatroOpenGL {

    private long window;
    private int shaderProgram;
    private int vao;

    private final String vertexShaderSource =
            "#version 330 core\n" +
                    "layout(location = 0) in vec2 aPos;\n" +
                    "out vec2 uv;\n" +
                    "void main() {\n" +
                    "    uv = aPos * 0.5 + 0.5;\n" +
                    "    gl_Position = vec4(aPos, 0.0, 1.0);\n" +
                    "}";

    // Convert your Balatro fragment shader to GLSL 330
    private final String fragmentShaderSource =
            "#version 330 core\n" +
                    "out vec4 FragColor;\n" +
                    "in vec2 uv;\n" +
                    "uniform float iTime;\n" +
                    "uniform vec2 iResolution;\n" +

                    "vec4 effect(vec2 screenSize, vec2 screen_coords){\n" +
                    "    // --- simplified Balatro effect for example ---\n" +
                    "    float x = screen_coords.x / screenSize.x;\n" +
                    "    float y = screen_coords.y / screenSize.y;\n" +
                    "    vec3 color = 0.5 + 0.5*cos(iTime + vec3(x,y,x+y)*10.0);\n" +
                    "    return vec4(color, 1.0);\n" +
                    "}\n" +

                    "void main(){\n" +
                    "    FragColor = effect(iResolution, uv * iResolution);\n" +
                    "}";

    public void run() {
        init();
        loop();
        cleanup();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        window = glfwCreateWindow(800, 600, "Balatro OpenGL", NULL, NULL);
        if (window == NULL) throw new RuntimeException("Failed to create window");

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1); // vsync
        glfwShowWindow(window);

        GL.createCapabilities();

        shaderProgram = createShaderProgram(vertexShaderSource, fragmentShaderSource);

        vao = glGenVertexArrays();
        glBindVertexArray(vao);
    }

    private int createShaderProgram(String vertexSrc, String fragmentSrc) {
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexSrc);
        glCompileShader(vertexShader);
        checkCompileErrors(vertexShader, "VERTEX");

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentSrc);
        glCompileShader(fragmentShader);
        checkCompileErrors(fragmentShader, "FRAGMENT");

        int program = glCreateProgram();
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);
        checkCompileErrors(program, "PROGRAM");

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);

        return program;
    }

    private void checkCompileErrors(int shader, String type) {
        int success;
        if (!type.equals("PROGRAM")) {
            success = glGetShaderi(shader, GL_COMPILE_STATUS);
            if (success == GL_FALSE) {
                System.err.println("ERROR::SHADER_COMPILATION_ERROR of type: " + type);
                System.err.println(glGetShaderInfoLog(shader));
            }
        } else {
            success = glGetProgrami(shader, GL_LINK_STATUS);
            if (success == GL_FALSE) {
                System.err.println("ERROR::PROGRAM_LINKING_ERROR of type: " + type);
                System.err.println(glGetProgramInfoLog(shader));
            }
        }
    }

    private void loop() {
        float[] vertices = {
                -1f, -1f,
                1f, -1f,
                -1f,  1f,
                1f,  1f
        };

        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 2 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT);

            glUseProgram(shaderProgram);

            int iTimeLoc = glGetUniformLocation(shaderProgram, "iTime");
            glUniform1f(iTimeLoc, (float) glfwGetTime());

            int resLoc = glGetUniformLocation(shaderProgram, "iResolution");
            glUniform2f(resLoc, 800, 600);

            glBindVertexArray(vao);
            glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private void cleanup() {
        glDeleteVertexArrays(vao);
        glDeleteProgram(shaderProgram);
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public static void main(String[] args) {
        new BalatroOpenGL().run();
    }
}

