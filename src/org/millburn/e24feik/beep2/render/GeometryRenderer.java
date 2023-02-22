package org.millburn.e24feik.beep2.render;

import org.millburn.e24feik.beep2.core.Window;
import org.millburn.e24feik.beep2.scene.Camera;
import org.millburn.e24feik.beep2.scene.Entity;
import org.millburn.e24feik.beep2.scene.Mesh;
import org.millburn.e24feik.beep2.scene.Model;
import org.millburn.e24feik.beep2.scene.Scene;
import org.millburn.e24feik.beep2.scene.ShaderProgram;

import java.util.Collection;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class GeometryRenderer implements Renderable {
    private final ShaderProgram shaderProgram;

    public GeometryRenderer() {
        this.shaderProgram = new ShaderProgram(
                """
                        #version 460
                                        
                        layout (location=0) in vec3 position;
                        layout (location=1) in vec4 color;
                                        
                        out vec4 outColor;
                                        
                        uniform mat4 viewMatrix;
                        uniform mat4 projectionMatrix;
                        uniform mat4 modelMatrix;
                                        
                        void main()
                        {
                            gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(position, 1.0);
                            outColor = color;
                        }
                                        
                        """,
                """
                        #version 460
                                        
                        in  vec4 outColor;
                        out vec4 fragColor;
                                        
                        void main()
                        {
                            fragColor = outColor;
                        }
                        """
        );

        this.shaderProgram.createUniform("viewMatrix");
        this.shaderProgram.createUniform("projectionMatrix");
        this.shaderProgram.createUniform("modelMatrix");

        glDisable(GL_CULL_FACE);
    }

    @Override
    public void render(Window window, Scene scene) {
        this.shaderProgram.bind();

        Camera camera = scene.getCamera();
        this.shaderProgram.uploadUniformMat4("viewMatrix", camera.getView());
        this.shaderProgram.uploadUniformMat4("projectionMatrix", camera.getProjection());

        Collection<Model> models = scene.getModels();
        for(Model model : models) {
            List<Entity> entities = model.getEntitiesList();

            model.getMaterialList().forEach(material -> {
                for(Mesh mesh : material.getMeshList()) {
                    mesh.bind();

                    for(Entity entity : entities) {
                        this.shaderProgram.uploadUniformMat4("modelMatrix", entity.getModelMatrix());
                        glDrawElements(mesh.getMode(), mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
                    }
                }
            });
        }

        glBindVertexArray(0);
        shaderProgram.unbind();
    }

    @Override
    public void clean() {
        this.shaderProgram.clean();
    }
}
