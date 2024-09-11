package ru.itis.game.levels;

import ru.itis.game.Entities;
import ru.itis.game.components.*;
import ru.itis.gengine.application.Application;
import ru.itis.gengine.base.GSize;
import ru.itis.gengine.gamelogic.Entity;
import ru.itis.gengine.gamelogic.LevelBase;
import ru.itis.gengine.gamelogic.World;
import ru.itis.gengine.gamelogic.components.BoxCollider;
import ru.itis.gengine.gamelogic.components.Camera;
import ru.itis.gengine.gamelogic.components.Mesh;
import ru.itis.gengine.gamelogic.components.Transform;
import ru.itis.gengine.gamelogic.primitives.MeshData;
import ru.itis.gengine.gamelogic.primitives.Primitives;
import ru.itis.gengine.network.model.NetworkEntity;
import ru.itis.gengine.renderer.Shader;
import ru.itis.gengine.renderer.Texture;

import java.util.ArrayList;

public class FirstLevel extends LevelBase {
    Shader baseShader;
    static boolean isStarted;

    float houseX = 0;
    float houseY = 0;

    static ArrayList<Entity> cloudArray = new ArrayList();


    @Override
    public void startServer(World world) {
        System.out.println("SERVER LEVEL START!");

        isStarted = false;
//        light blue
//        world.getRenderer().setClearColor(0.65f, 0.75f, 0.81f, 1.0f);
//        dark blue
        world.getRenderer().setClearColor(0.2f, 0.5f, 0.7f, 1.0f);

        baseShader = new Shader(
                "resources/shaders/vertex_shader.glsl",
                "resources/shaders/fragment_shader.glsl"
        );

        createHouse(world);

//      ball
        Entity ballEntity = world.instantiateEntity(Entities.Ball.id, true,"Ball");
        Texture ballTexture = new Texture("resources/textures/ball.png");
        MeshData ballData = Primitives.createSquare(2.f);
        Mesh ballMesh = new Mesh(ballData, false, ballTexture, baseShader);
        ballEntity.addComponent(ballMesh);
        ballEntity.addComponent(new BoxCollider(new GSize(2.f, 2.f)));
        ballEntity.addComponent(new BallMove(10));
        ballEntity.getTransform().setPosition(0.f,0.f,0.f);

//        camera
        createCamera(world, ballEntity.getTransform(), Entities.Camera.id);

//        cloud
        createCloud(world, houseX, houseY);

//        sending
        ballEntity.sendCurrentState();
    }

    @Override
    public void startClient(World world) {
        System.out.println("CLIENT LEVEL START!");

        world.getRenderer().setClearColor(0.2f, 0.5f, 0.7f, 1.0f);

        baseShader = new Shader(
                "resources/shaders/vertex_shader.glsl",
                "resources/shaders/fragment_shader.glsl"
        );

//      ball
        Entity ballEntity2 = world.instantiateEntity(Entities.Ball2.id, true,"Ball2");
        Texture ballTexture2 = new Texture("resources/textures/ball.png");
        MeshData ballData2 = Primitives.createSquare(2.f);
        Mesh ballMesh2 = new Mesh(ballData2, false, ballTexture2, baseShader);
        ballEntity2.addComponent(ballMesh2);
        ballEntity2.addComponent(new BoxCollider(new GSize(2.f, 2.f)));
        ballEntity2.addComponent(new BallMove(20));
        ballEntity2.getTransform().setPosition(-1.f,1.f,0.f);

//        camera
        createCamera(world, ballEntity2.getTransform(), 2);

//        sending
        ballEntity2.sendCurrentState();
    }

    @Override
    public void createEntityNetworkEvent(NetworkEntity entity) {
        World world = Application.shared.getWorld();
        int id = entity.getId();
        if (world.findEntityById(id).isPresent()) {
            System.out.println("ENTITY WITH ID " + id + " ALREADY EXISTS");
            return;
        }
        System.out.println("CREATE ENTITY WITH ID " + id);
        if (id == Entities.Ball.id) {
            Entity ballEntity = world.instantiateEntity(Entities.Ball.id, true,"Ball");

            Texture ballTexture = new Texture("resources/textures/ball.png");
            MeshData ballData = Primitives.createSquare(2.f);
            Mesh ballMesh = new Mesh(ballData, false, ballTexture, baseShader);

            ballEntity.addComponent(ballMesh);
            ballEntity.addComponent(new BoxCollider(new GSize(2.f, 2.f)));
            ballEntity.addComponent(new NetBallMove(10));

            ballEntity.getTransform().setPosition(0.f,0.f,0.f);

            ballEntity.applyEntity(entity);
        }
        if (id == Entities.Ball2.id) {
            Entity ballEntity2 = world.instantiateEntity(Entities.Ball2.id, true,"Ball2");

            Texture ballTexture2 = new Texture("resources/textures/ball.png");
            MeshData ballData2 = Primitives.createSquare(2.f);
            Mesh ballMesh2 = new Mesh(ballData2, false, ballTexture2, baseShader);

            ballEntity2.addComponent(ballMesh2);
            ballEntity2.addComponent(new BoxCollider(new GSize(2.f, 2.f)));
            ballEntity2.addComponent(new NetBallMove(20));

            ballEntity2.getTransform().setPosition(-1.f,1.f,0.f);

            ballEntity2.applyEntity(entity);
        }
        if (id == Entities.House.id) {
            createHouseNet(world, entity);
        }
        if (id >= 9 && id <= 80) {
            createCloudNet(world, entity);
        }
    }

    @Override
    public void terminate() {
        baseShader.delete();
    }

    private void createCamera(World world, Transform target, int id) {
        Entity cameraEntity = world.instantiateEntity(id, false,"camera");
        Camera camera = new Camera();
        cameraEntity.addComponent(camera);

        cameraEntity.addComponent(new CameraFollow(id, target));

        camera.setFieldOfView(60.f);
        camera.setShader(baseShader);
    }

    public void createCloud(World world, float houseX, float houseY) {
//        int id = 9;
        for (int i = 2; i <= 80; i++) {
            Entity cloudEntity = world.instantiateEntity(i+7, true, "Cloud" + i+7);

            Texture cloudTexture = new Texture("resources/textures/cloud-2.png");
            MeshData cloudData = Primitives.createSquare(3.f);
            Mesh cloudeMesh = new Mesh(cloudData, false, cloudTexture, baseShader);

            cloudEntity.addComponent(cloudeMesh);
            cloudEntity.addComponent(new BoxCollider(new GSize(3.f, 3.f)));
            cloudEntity.addComponent(new CloudMove(i+7));

            float cloudX = (float) (-8 + Math.random() * (8 + 8 + 1));
//            float cloudY = (float) (2 + Math.random() * (39 - 2 + 1));
            float cloudY = (float) i;

            if (cloudY == houseY) {
                continue;
            }
            else {
                cloudEntity.getTransform().setPosition(cloudX, cloudY, 0.f);
            }
            cloudEntity.sendCurrentState();
            cloudArray.add(cloudEntity);
            i++;
        }
    }
    public void createCloudNet(World world, NetworkEntity entity) {
        Texture cloudTexture = new Texture("resources/textures/cloud-2.png");

        Entity cloudEntity = world.instantiateEntity(entity.getId(), true, "Cloud" + entity.getId());
        MeshData cloudData = Primitives.createSquare(3.f);
        Mesh cloudMesh = new Mesh(cloudData, false, cloudTexture, baseShader);

        cloudEntity.addComponent(cloudMesh);
        cloudEntity.addComponent(new BoxCollider(new GSize(3.f, 3.f)));
        cloudEntity.addComponent(new CloudMove(entity.getId()));

        cloudEntity.applyEntity(entity);
    }
    public void createHouse(World world) {
        Texture houseTexture = new Texture("resources/textures/house.png");

        Entity houseEntity = world.instantiateEntity(Entities.House.id, true,"House");
        MeshData houseData = Primitives.createSquare(3.f);
        Mesh houseMesh = new Mesh(houseData, false, houseTexture, baseShader);

        houseEntity.addComponent(houseMesh);
        houseEntity.addComponent(new BoxCollider(new GSize(3.f, 3.f)));
        houseEntity.addComponent(new HouseMove(1));

        houseX = (float) (-5 + Math.random() * (5 + 5 + 1));
        houseY = (float) (30 + Math.random() * (70 - 30 + 1));

        houseEntity.getTransform().setPosition(houseX, houseY,0.f);

        houseEntity.sendCurrentState();
    }
    public void createHouseNet(World world, NetworkEntity entity) {
        Texture houseTexture = new Texture("resources/textures/house.png");

        Entity houseEntity = world.instantiateEntity(entity.getId(), true,"House");
        MeshData houseData = Primitives.createSquare(3.f);
        Mesh houseMesh = new Mesh(houseData, false, houseTexture, baseShader);

        houseEntity.addComponent(houseMesh);
        houseEntity.addComponent(new BoxCollider(new GSize(3.f, 3.f)));
        houseEntity.addComponent(new HouseMove(1));

        houseEntity.applyEntity(entity);
    }
    private static boolean checkCollisionWithCloud(World world, Entity player, Entity cloud) {
        if (player != null && cloud != null) {
            Transform ballTransform = player.getTransform();
            Transform cloudTransform = cloud.getTransform();

            if ((cloudTransform.getPosition().y - ballTransform.getPosition().y <= 1.5) && (cloudTransform.getPosition().y - ballTransform.getPosition().y >= -1)) {
                if (((cloudTransform.getPosition().x >= ballTransform.getPosition().x) && (cloudTransform.getPosition().x <= ballTransform.getPosition().x + 1)) || ((cloudTransform.getPosition().x <= ballTransform.getPosition().x) && (cloudTransform.getPosition().x >= ballTransform.getPosition().x - 1))) {
//                    System.out.println("ball+cloud + ball Name + " + player.getName());
                    return true;
                }
            }
        }
        return false;
    }
    private static boolean checkCollisionWithHouse(World world, Entity player, Entity house) {
        if (player != null && house != null) {
            Transform ballTransform = player.getTransform();
            Transform houseTransform = house.getTransform();

            if ((houseTransform.getPosition().y - ballTransform.getPosition().y <= 2) && (houseTransform.getPosition().y - ballTransform.getPosition().y >= -1.8)) {
                if (((houseTransform.getPosition().x >= ballTransform.getPosition().x) && (houseTransform.getPosition().x <= ballTransform.getPosition().x + 1.1)) || ((houseTransform.getPosition().x <= ballTransform.getPosition().x) && (houseTransform.getPosition().x >= ballTransform.getPosition().x - 1.1))) {
//                    System.out.println("ball+house + ball Name + " + player.getName());
                    return true;
                }
            }
        }
        return false;
    }
    public static void gameplay() {
        World world = Application.shared.getWorld();

        Entity player1 = world.findEntityByName("Ball").orElse(null);
        Entity player2 = world.findEntityByName("Ball2").orElse(null);

        Entity house = world.findEntityByName("House").orElse(null);

        boolean winPlayer1 = checkCollisionWithHouse(world, player1, house);
        boolean winPlayer2 = checkCollisionWithHouse(world, player2, house);

        boolean losePlayer1 = false;
        boolean losePlayer2 = false;

        isStarted = true;

        if (!winPlayer1 && !winPlayer2) {
            for (Entity cloud : cloudArray) {
                losePlayer1 = losePlayer1 || checkCollisionWithCloud(world, player1, cloud);
                losePlayer2 = losePlayer2 || checkCollisionWithCloud(world, player2, cloud);
            }
        }

        if (isStarted) {
            if (winPlayer1) {
                if (player2 == null) {
                    new Final(8);
                }
                else {
                    new Final(4);
                }
                isStarted = false;
            } else if (winPlayer2) {
                new Final(5);
                isStarted = false;
            } else if (losePlayer1) {
                if (player2 == null) {
                    new Final(7);
                }
                else {
                    new Final(1);
                }
                isStarted = false;
            } else if (losePlayer2) {
                new Final(2);
                isStarted = false;
            }
        }

    }
}