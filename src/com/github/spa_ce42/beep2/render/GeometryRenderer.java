package com.github.spa_ce42.beep2.render;

import com.github.spa_ce42.projectxl.core.Window;
import com.github.spa_ce42.projectxl.entity.AbstractMesh;
import com.github.spa_ce42.projectxl.entity.AbstractModel;
import com.github.spa_ce42.projectxl.entity.Entity;
import com.github.spa_ce42.projectxl.render.Renderable;
import com.github.spa_ce42.projectxl.render.ShaderProgram;
import com.github.spa_ce42.projectxl.scene.Camera;
import com.github.spa_ce42.projectxl.scene.Scene;

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

        Collection<AbstractModel> models = scene.getModels();
        for(AbstractModel model : models) {
            List<Entity> entities = model.getEntitiesList();

            model.getMaterialList().forEach(material -> {
                for(AbstractMesh am : material.getMeshList()) {
                    GeometryMesh mesh = (GeometryMesh)am;
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
